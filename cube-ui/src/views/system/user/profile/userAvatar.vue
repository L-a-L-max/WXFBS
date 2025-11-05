<template>
  <div>
    <!-- 头像显示区域 -->
    <div class="user-info-head" @click="editCropper">
      <img :src="options.img" title="点击上传头像" class="img-circle img-lg" />
    </div>

    <!-- 裁剪弹窗 - 简化版 -->
    <el-dialog 
      v-model="open" 
      title="修改头像" 
      width="650px" 
      append-to-body 
      @opened="modalOpened"
      @close="closeDialog"
      class="avatar-dialog-simple"
    >
      <div class="avatar-container-simple">
        <!-- 裁剪区域 -->
        <div class="cropper-section">
          <vue-cropper
            ref="cropperRef"
            :img="options.img"
            :info="true"
            :autoCrop="options.autoCrop"
            :autoCropWidth="options.autoCropWidth"
            :autoCropHeight="options.autoCropHeight"
            :fixedBox="options.fixedBox"
            :outputType="options.outputType"
            :fixed="options.fixed"
            :fixedNumber="options.fixedNumber"
            :canMove="options.canMove"
            :centerBox="options.centerBox"
            :full="options.full"
            @realTime="realTime"
            v-if="visible"
          />
        </div>

        <!-- 预览和操作区域 -->
        <div class="operation-section">
          <!-- 预览 -->
          <div class="preview-area">
            <div class="preview-title">预览</div>
            <div class="avatar-preview">
              <img :src="previews.url" :style="previews.img" />
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <el-upload 
              action="#" 
              :http-request="requestUpload" 
              :show-file-list="false" 
              :before-upload="beforeUpload"
            >
              <el-button type="primary" plain style="width: 100%; margin-bottom: 12px;">
                <el-icon class="el-icon--left"><Upload /></el-icon>
                选择图片
              </el-button>
            </el-upload>

            <div class="button-row">
              <el-button @click="changeScale(1)" class="action-btn">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button @click="changeScale(-1)" class="action-btn">
                <el-icon><Minus /></el-icon>
              </el-button>
              <el-button @click="rotateLeft" class="action-btn">
                <el-icon><RefreshLeft /></el-icon>
              </el-button>
              <el-button @click="rotateRight" class="action-btn">
                <el-icon><RefreshRight /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="primary" @click="uploadImg" :loading="uploadLoading">
            确认上传
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, getCurrentInstance, computed } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import { Upload, Plus, Minus, RefreshLeft, RefreshRight } from '@element-plus/icons-vue'
import 'vue-cropper/dist/index.css'
import { VueCropper } from 'vue-cropper'
import { uploadAvatar } from '@/api/system/user'
import store from '@/store'

// 获取当前组件实例
const { proxy } = getCurrentInstance()

// 响应式数据
const open = ref(false)
const visible = ref(false)
const uploadLoading = ref(false)
const cropperRef = ref(null)
const resizeHandler = ref(null)

// 从 store 获取用户头像（只接受完整URL，拒绝相对路径）
const userAvatar = computed(() => {
  const avatar = store.getters.avatar
  if (!avatar) {
    console.warn('[userAvatar] 无头像数据，使用默认头像')
    return require("@/assets/images/profile.jpg")
  }
  
  // 只接受完整的 HTTP/HTTPS URL
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  
  // 拒绝相对路径，使用默认头像
  console.warn('[userAvatar] 头像为相对路径（已拒绝），使用默认头像:', avatar)
  return require("@/assets/images/profile.jpg")
})

// 裁剪配置选项
const options = reactive({
  img: '', // 初始图片路径，从store获取
  autoCrop: true,
  autoCropWidth: 200,
  autoCropHeight: 200,
  fixedBox: true,
  outputType: 'png',
  fixed: true,
  fixedNumber: [1, 1], // 1:1 比例，确保是正方形
  canMove: true, // 允许移动图片
  canMoveBox: true, // 允许移动裁剪框
  centerBox: true,
  full: false,
  enlarge: 1,
  mode: 'contain',
  maxImgSize: 3000,
  limitMinSize: [50, 50],
  infoTrue: false, // 隐藏裁剪框信息
  original: false, // 上传图片按照原始比例渲染
  high: true, // 是否按照设备的dpr 输出等比例图片
  cropBoxResizable: true, // 裁剪框可以调整大小
  cropBoxMovable: true // 裁剪框可以移动
})

const previews = ref({})

// 初始化用户头像
onMounted(() => {
  // 从store获取用户头像
  options.img = userAvatar.value || require("@/assets/images/profile.jpg")
})

// 编辑头像 - 打开弹窗
const editCropper = () => {
  open.value = true
}

// 弹窗打开后的回调
const modalOpened = () => {
  visible.value = true
  
  // 添加防抖的resize事件监听[1](@ref)
  if (!resizeHandler.value) {
    resizeHandler.value = debounce(() => {
      refresh()
    }, 100)
  }
  window.addEventListener('resize', resizeHandler.value)
}

// 刷新裁剪器
const refresh = () => {
  if (cropperRef.value) {
    cropperRef.value.refresh()
  }
}

// 覆盖默认上传行为
const requestUpload = () => {
  // 空实现，使用beforeUpload处理上传逻辑[1](@ref)
}

// 向左旋转
const rotateLeft = () => {
  if (cropperRef.value) {
    cropperRef.value.rotateLeft()
  }
}

// 向右旋转
const rotateRight = () => {
  if (cropperRef.value) {
    cropperRef.value.rotateRight()
  }
}

// 图片缩放
const changeScale = (num) => {
  if (cropperRef.value) {
    cropperRef.value.changeScale(num)
  }
}

// 上传前处理 - 文件验证和转换[1,2](@ref)
const beforeUpload = (file) => {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('文件格式错误，请上传图片类型，如：JPG，PNG后缀的文件。')
    return false
  }
  
  // 检查文件大小（限制为10MB）[5](@ref)
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('上传图片大小不能超过10MB')
    return false
  }

  // 将文件转换为Base64[3](@ref)
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = (e) => {
    options.img = e.target.result
  }
  
  return false // 阻止默认上传行为
}

// 上传图片
const uploadImg = async () => {
  if (!cropperRef.value) {
    ElMessage.error('裁剪器未初始化')
    return
  }

  uploadLoading.value = true
  
  try {
    // 获取裁剪后的Blob数据
    cropperRef.value.getCropBlob(async (blobData) => {
      try {
        const formData = new FormData()
        formData.append('avatarfile', blobData, 'avatar.png')

        // 调用上传API
        const response = await uploadAvatar(formData)
        
        if (response.code === 200) {
          // 获取后端返回的完整URL
          const newAvatarUrl = response.imgUrl
          
          // 验证返回的是完整URL，拒绝相对路径
          if (!newAvatarUrl || (!newAvatarUrl.startsWith('http://') && !newAvatarUrl.startsWith('https://'))) {
            console.error('[userAvatar] 后端返回的不是完整URL:', newAvatarUrl)
            throw new Error('上传失败：后端未返回完整URL')
          }
          
          // 更新本地显示的头像
          options.img = newAvatarUrl
          
          // 更新store中的用户头像，实现全局同步
          store.commit('SET_AVATAR', newAvatarUrl)
          
          // 同时更新 localStorage 以保证刷新后也能正确显示
          const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
          userInfo.avatar = newAvatarUrl
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          
          open.value = false
          uploadLoading.value = false
          ElMessage.success('头像修改成功')
          
          console.log('[userAvatar] 头像上传成功，完整URL:', newAvatarUrl)
          
          // 触发全局事件，通知其他组件更新
          window.dispatchEvent(new CustomEvent('avatarUpdated', { 
            detail: { avatar: newAvatarUrl } 
          }))
        } else {
          throw new Error(response.msg || '上传失败')
        }
        
      } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error(error.message || '头像上传失败，请重试')
        uploadLoading.value = false
      }
    })
  } catch (error) {
    console.error('裁剪失败:', error)
    ElMessage.error('图片裁剪失败，请重试')
    uploadLoading.value = false
  }
}

// 实时预览
const realTime = (data) => {
  previews.value = data
}

// 关闭弹窗
const closeDialog = () => {
  // 恢复原始头像（从 store 获取）
  options.img = userAvatar.value || require("@/assets/images/profile.jpg")
  visible.value = false
  
  // 移除resize事件监听
  if (resizeHandler.value) {
    window.removeEventListener('resize', resizeHandler.value)
    resizeHandler.value = null
  }
}

// 防抖函数[1](@ref)
const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}
</script>

<style scoped lang="scss">
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
  cursor: pointer;
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.05);
  }

  &:hover::after {
    content: '点击修改';
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    color: #fff;
    background: rgba(0, 0, 0, 0.6);
    font-size: 14px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    cursor: pointer;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    letter-spacing: 2px;
  }
}

.img-circle {
  border-radius: 50% !important;
  border: 3px solid #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.img-lg {
  width: 120px !important;
  height: 120px !important;
  object-fit: cover;
  display: block;
}

// 美化对话框样式
.avatar-dialog {
  :deep(.el-dialog__header) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 20px;
    margin: 0;
    
    .el-dialog__title {
      color: #fff;
      font-size: 18px;
      font-weight: 600;
    }
    
    .el-dialog__headerbtn {
      top: 20px;
      
      .el-dialog__close {
        color: #fff;
        font-size: 20px;
        
        &:hover {
          color: #f0f0f0;
        }
      }
    }
  }
  
  :deep(.el-dialog__body) {
    padding: 30px;
    background: #f5f7fa;
  }
  
  :deep(.el-dialog__footer) {
    padding: 20px 30px;
    background: #fff;
    border-top: 1px solid #e4e7ed;
  }
}

// 简化版头像对话框样式
.avatar-dialog-simple {
  :deep(.el-dialog__body) {
    padding: 20px;
  }
}

.avatar-container-simple {
  display: flex;
  gap: 20px;
  
  .cropper-section {
    flex: 1;
    height: 400px;
    border-radius: 8px;
    overflow: hidden;
    background: #f5f7fa;
  }
  
  .operation-section {
    width: 180px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    
    .preview-area {
      .preview-title {
        font-size: 14px;
        color: #606266;
        margin-bottom: 12px;
        font-weight: 500;
        text-align: center;
      }
      
      .avatar-preview {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        overflow: hidden;
        margin: 0 auto;
        border: 3px solid #409eff;
        box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
    }
    
    .action-buttons {
      .button-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 8px;
        
        .action-btn {
          padding: 8px;
          
          .el-icon {
            font-size: 16px;
          }
        }
      }
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  
  .el-button {
    min-width: 100px;
  }
}

// 优化裁剪器样式 - 圆形裁剪框
:deep(.vue-cropper) {
  background-image: none;
  background-color: #f5f7fa;
  
  // 裁剪框容器
  .cropper-crop-box {
    border-radius: 50% !important;
    overflow: visible !important;
  }
  
  // 裁剪框的视图区域（这是实际显示的裁剪内容）
  .cropper-view-box {
    outline: 3px solid #409eff !important;
    outline-offset: -1px;
    border-radius: 50% !important;
    box-shadow: 0 0 0 9999px rgba(0, 0, 0, 0.6) !important; // 外部半透明黑色遮罩
    overflow: hidden !important;
  }
  
  // 裁剪框的面板（可拖动区域）
  .cropper-face {
    border-radius: 50% !important;
    background-color: transparent !important;
    opacity: 0.1 !important;
  }
  
  // 控制点样式
  .cropper-point {
    background-color: #409eff;
    width: 10px;
    height: 10px;
    opacity: 1;
    border-radius: 50%;
    border: 2px solid #fff;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.4);
  }
  
  // 边线（隐藏）
  .cropper-line {
    background-color: transparent !important;
    opacity: 0 !important;
  }
  
  // 虚线网格（隐藏）
  .cropper-dashed {
    border: none !important;
    opacity: 0 !important;
  }
  
  // 中心指示器（隐藏）
  .cropper-center {
    opacity: 0 !important;
  }
}

// 响应式适配
@media screen and (max-width: 768px) {
  .avatar-dialog-simple {
    :deep(.el-dialog) {
      width: 95% !important;
      margin: 20px auto;
    }
  }
  
  .avatar-container-simple {
    flex-direction: column;
    
    .cropper-section {
      height: 300px;
    }
    
    .operation-section {
      width: 100%;
      flex-direction: row;
      justify-content: space-between;
      
      .preview-area {
        .avatar-preview {
          width: 100px;
          height: 100px;
        }
      }
      
      .action-buttons {
        flex: 1;
        margin-left: 20px;
      }
    }
  }
}
</style>