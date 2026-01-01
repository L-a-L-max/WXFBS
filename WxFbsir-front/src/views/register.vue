<template>
  <div class="register-container">
    <el-card class="register-card">
      <div class="logo">
        <el-icon :size="64" color="#667eea"><User /></el-icon>
      </div>
      <h1>用户注册</h1>
      <p class="desc">创建您的潜力眼-HR智能助手账户</p>

      <el-form
          :model="registerForm"
          :rules="rules"
          ref="registerFormRef"
          label-position="top"
          class="register-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              :prefix-icon="Unlock"
              show-password
          />
        </el-form-item>

        <el-form-item>
          <el-checkbox v-model="agreeToTerms" class="terms-checkbox">
            我同意 <el-link type="primary" @click="showTerms" :underline="false">用户协议</el-link> 和 <el-link type="primary" @click="showPrivacy" :underline="false">隐私政策</el-link>
          </el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              size="large"
              @click="handleRegister"
              :loading="isLoading"
              class="register-button"
          >
            <el-icon v-if="!isLoading"><UserFilled /></el-icon>
            {{ isLoading ? '注册中...' : '立即注册' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User,
  Lock,
  Unlock,
  UserFilled
} from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()

// 表单数据
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  sessionId: '' // 从localStorage获取
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 15, message: '用户名长度应在3-15个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const registerFormRef = ref()
const isLoading = ref(false)
const agreeToTerms = ref(false)

// 在组件挂载时从localStorage获取sessionId
onMounted(() => {
  const sessionId = localStorage.getItem('sessionId')
  if (sessionId) {
    registerForm.sessionId = sessionId
  } else {
    // 如果localStorage中没有sessionId，可以生成一个默认值或提示用户
    console.warn('未找到sessionId，请确保已设置sessionId')
  }
})

// 注册处理函数
const handleRegister = async () => {
  if (!agreeToTerms.value) {
    ElMessage.warning('请先同意用户协议和隐私政策')
    return
  }

  registerFormRef.value.validate(async (valid) => {
    if (valid) {
      isLoading.value = true
      try {
        // 发送POST请求到/api/auth/register
        const response = await axios.post('/api/auth/register', {
          username: registerForm.username,
          password: registerForm.password,
          sessionId: registerForm.sessionId
        })

        if (response.data.code === 200) {
          ElMessage.success('注册成功！')
          // 注册成功后跳转到企业微信授权
          window.location.href = `/api/auth/authorize`
        } else {
          ElMessage.error(response.data.msg || '注册失败')
        }
      } catch (error) {
        console.error('注册失败:', error)
        if (error.response) {
          // 服务器返回了错误状态码
          ElMessage.error(error.response.data.msg || '注册失败，请重试')
        } else if (error.request) {
          // 请求已发出但没有收到响应
          ElMessage.error('网络错误，请检查网络连接')
        } else {
          // 其他错误
          ElMessage.error('注册失败，请重试')
        }
      } finally {
        isLoading.value = false
      }
    } else {
      ElMessage.warning('请填写完整的注册信息')
      return false
    }
  })
}

// 显示用户协议
const showTerms = () => {
  ElMessage.info('显示用户协议')
}

// 显示隐私政策
const showPrivacy = () => {
  ElMessage.info('显示隐私政策')
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  background-size: 200% 200%;
  animation: gradientShift 15s ease infinite;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.register-container::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 50%);
  animation: rotate 20s linear infinite;
}

@keyframes gradientShift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.register-card {
  width: 100%;
  max-width: 420px;
  text-align: center;
  padding: 50px 30px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3),
  0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  position: relative;
  z-index: 1;
  animation: slideUp 0.6s ease-out;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.register-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 25px 70px rgba(0, 0, 0, 0.4),
  0 0 0 1px rgba(255, 255, 255, 0.15) inset;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.logo {
  margin-bottom: 20px;
}

h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin: 10px 0 10px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.desc {
  color: #666;
  margin-bottom: 30px;
  font-size: 14px;
}

.register-form {
  width: 100%;
  margin: 0 auto;
}

.register-form :deep(.el-form-item__label) {
  text-align: left;
  font-weight: 600;
  color: #333;
  padding-bottom: 8px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05) !important;
  transition: all 0.3s ease;
}

.register-form :deep(.el-input__wrapper):hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1) !important;
}

.register-form :deep(.el-input__wrapper).is-focus {
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.2) !important;
  border: 1px solid #667eea !important;
}

.register-button {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}

.register-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.6);
  background: linear-gradient(135deg, #768feb 0%, #855bc5 100%);
}

.register-button:active {
  transform: translateY(0);
}

.terms-checkbox {
  text-align: left;
  margin-bottom: 20px;
}

.login-link {
  margin-top: 20px;
  text-align: center;
}

.login-link p {
  color: #666;
  font-size: 14px;
}

.login-link :deep(.el-link) {
  font-weight: 600;
}
</style>