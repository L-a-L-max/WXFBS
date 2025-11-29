<template>
  <div class="app-container">
    <!-- 模板类型选择器 -->
    <div class="template-type-selector" style="margin-bottom: 20px">
      <el-radio-group v-model="templateType" @change="handleTemplateTypeChange" size="default">
        <el-radio-button label="score">评分模板</el-radio-button>
        <el-radio-button label="callword">提示词模板</el-radio-button>
      </el-radio-group>
    </div>
    
    <el-card v-if="templateType === 'score'">
      <template #header>
        <div class="card-header">
          <span>{{ title }}</span>
          <el-button v-if="form.id" type="primary" @click="handlePublish" :disabled="form.status === 1">
            {{ form.status === 1 ? '已上架' : '上架到市场' }}
          </el-button>
        </div>
      </template>

      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="模板内容" prop="prompt">
          <el-input
            v-model="form.prompt"
            type="textarea"
            :rows="15"
            placeholder="请输入模板内容"
            show-word-limit
          />
        </el-form-item>
        <el-form-item v-if="form.id && form.status !== 1" label="上架价格">
          <el-input-number
            v-model="form.price"
            :min="0.01"
            :precision="2"
            :step="1"
            controls-position="right"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #999">设置价格后可以上架到市场</span>
        </el-form-item>
        <el-form-item v-if="form.id">
          <el-tag :type="form.status === 1 ? 'success' : 'info'">
            {{ form.status === 1 ? '已上架' : '草稿' }}
          </el-tag>
          <el-tag v-if="form.isCommon === 1" type="warning" style="margin-left: 10px">
            公共模板
          </el-tag>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
        <el-button @click="handleCancel">取消</el-button>
        <el-button v-if="form.id" type="danger" @click="handleDelete">删除</el-button>
      </div>
    </el-card>
    
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <span>{{ callWordTitle }}</span>
          <el-button
            v-if="callWordPersisted"
            type="primary"
            @click="handleCallWordPublish"
            :disabled="callWordForm.status === 1"
          >
            {{ callWordForm.status === 1 ? '已上架' : '上架到市场' }}
          </el-button>
        </div>
      </template>

      <el-form ref="callWordFormRef" :model="callWordForm" :rules="callWordRules" label-width="100px">
        <el-form-item label="平台标识" prop="platformId">
          <el-input
            v-model="callWordForm.platformId"
            placeholder="请输入平台标识（如 wechat_layout）"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="提示词内容" prop="wordContent">
          <el-input
            v-model="callWordForm.wordContent"
            type="textarea"
            :rows="15"
            placeholder="请输入提示词内容"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="基础价格">
          <el-input-number
            v-model="callWordForm.price"
            :min="0"
            :precision="2"
            :step="1"
            controls-position="right"
            style="width: 200px"
          />
          <span style="margin-left: 10px; color: #999">设置价格后可以上架到市场</span>
        </el-form-item>
        <el-form-item v-if="callWordPersisted">
          <el-tag :type="callWordForm.status === 1 ? 'success' : 'info'">
            {{ callWordForm.status === 1 ? '已上架' : '草稿' }}
          </el-tag>
          <el-tag v-if="callWordForm.isCommon === 1" type="warning" style="margin-left: 10px">
            公共模板
          </el-tag>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-button type="primary" @click="submitCallWordForm" :loading="callWordSubmitting">保存</el-button>
        <el-button @click="handleCancel">取消</el-button>
        <el-button v-if="callWordPersisted" type="danger" @click="handleCallWordDelete">删除</el-button>
      </div>
    </el-card>

    <!-- 上架对话框 -->
    <el-dialog title="上架模板到市场" v-model="publishDialogVisible" width="500px" append-to-body>
      <el-form ref="publishFormRef" :model="publishForm" :rules="publishRules" label-width="100px">
        <el-form-item :label="publishTargetType === 'callword' ? '模板标识' : '模板名称'">
          <el-input v-model="publishForm.name" disabled />
        </el-form-item>
        <el-form-item label="上架价格" prop="price">
          <el-input-number
            v-model="publishForm.price"
            :min="0.01"
            :precision="2"
            :step="1"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPublish" :loading="publishing">确 定</el-button>
        <el-button @click="publishDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getScorePrompt,
  saveScorePrompt,
  updateScorePrompt,
  deleteScorePrompt,
  publishScorePrompt,
  getMediaCallWord,
  updateMediaCallWord,
  deleteMediaCallWord,
  publishCallWord
} from "@/api/wechat/aigc"

const createScoreForm = () => ({
  id: null,
  name: null,
  prompt: null,
  price: null,
  status: 0,
  isCommon: 0
})

const createCallWordForm = () => ({
  platformId: null,
  wordContent: null,
  price: 0,
  status: 0,
  isCommon: 0
})

export default {
  name: "TemplateWorkshop",
  data() {
    return {
      templateType: 'score', // 默认选择评分模板
      title: "创建模板",
      callWordTitle: "创建提示词模板",
      submitting: false,
      callWordSubmitting: false,
      publishing: false,
      publishTargetType: 'score',
      publishDialogVisible: false,
      callWordPersisted: false,
      form: createScoreForm(),
      callWordForm: createCallWordForm(),
      publishForm: {
        id: null,
        platformId: null,
        name: null,
        price: null
      },
      rules: {
        name: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        prompt: [
          { required: true, message: "模板内容不能为空", trigger: "blur" }
        ]
      },
      callWordRules: {
        platformId: [
          { required: true, message: "平台标识不能为空", trigger: "blur" }
        ],
        wordContent: [
          { required: true, message: "提示词内容不能为空", trigger: "blur" }
        ]
      },
      publishRules: {
        price: [
          { required: true, message: "请输入上架价格", trigger: "blur" },
          { type: "number", min: 0.01, message: "价格必须大于0", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    const id = this.$route.query.id
    const platformId = this.$route.query.platformId
    const mode = this.$route.query.mode || 'create'
    const type = this.$route.query.type || 'score'
    this.templateType = type
    
    if (type === 'callword') {
      this.initCallWordTemplate(platformId, mode)
    } else {
      this.initScoreTemplate(id, mode)
    }
  },
  methods: {
    initScoreTemplate(id, mode) {
      if (id && mode === 'view') {
        this.title = "查看模板"
        this.loadTemplate(id)
      } else if (id && (mode === 'edit' || mode === 'publish')) {
        this.title = mode === 'publish' ? "上架模板" : "编辑模板"
        this.loadTemplate(id)
        if (mode === 'publish') {
          this.$nextTick(() => {
            this.handlePublish()
          })
        }
      } else {
        this.title = "创建模板"
        this.resetScoreForm()
      }
    },
    initCallWordTemplate(platformId, mode) {
      if (platformId) {
        this.callWordTitle = mode === 'publish' ? "上架提示词模板" : (mode === 'view' ? "查看提示词模板" : "编辑提示词模板")
        this.loadCallWord(platformId, () => {
          if (mode === 'publish') {
            this.$nextTick(() => {
              this.handleCallWordPublish()
            })
          }
        })
      } else {
        this.callWordTitle = "创建提示词模板"
        this.resetCallWordForm()
      }
    },
    handleTemplateTypeChange() {
      if (this.templateType === 'callword') {
        this.callWordTitle = "创建提示词模板"
        this.resetCallWordForm()
      } else {
        this.title = "创建模板"
        this.resetScoreForm()
      }
    },
    resetScoreForm() {
      this.form = createScoreForm()
    },
    resetCallWordForm() {
      this.callWordPersisted = false
      this.callWordForm = createCallWordForm()
    },
    loadTemplate(id) {
      getScorePrompt(id).then(response => {
        this.form = response.data || {}
      })
    },
    loadCallWord(platformId, callback) {
      if (!platformId) return
      getMediaCallWord(platformId).then(response => {
        const data = response.data || {}
        this.callWordForm = {
          platformId: data.platformId || platformId,
          wordContent: data.wordContent || data.prompt || null,
          price: data.price != null ? Number(data.price) : 0,
          status: data.status || 0,
          isCommon: data.isCommon || 0
        }
        this.callWordPersisted = true
        callback && callback()
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.submitting = true
        
        const isEdit = !!this.form.id
        const api = isEdit ? updateScorePrompt : saveScorePrompt
        api(this.form).then(() => {
          this.$modal.msgSuccess(isEdit ? "修改成功" : "创建成功")
          if (isEdit) {
            // 编辑成功后重新加载
            this.loadTemplate(this.form.id)
          } else {
            // 创建成功后保持在当前页面，不跳转
            // 不进行任何路由跳转操作
          }
        }).catch(() => {
          // 错误处理已在 request 拦截器中完成
        }).finally(() => {
          this.submitting = false
        })
      })
    },
    submitCallWordForm() {
      if (!this.$refs.callWordFormRef) return
      this.$refs.callWordFormRef.validate(valid => {
        if (!valid) return
        const isEdit = this.callWordPersisted
        this.callWordSubmitting = true
        updateMediaCallWord(this.callWordForm).then(() => {
          this.$modal.msgSuccess(isEdit ? "修改成功" : "创建成功")
          if (isEdit) {
            // 编辑成功后重新加载
            this.callWordPersisted = true
            this.callWordTitle = "编辑提示词模板"
            this.loadCallWord(this.callWordForm.platformId)
          } else {
            // 创建成功后保持在当前页面，不跳转
            this.callWordPersisted = true
            this.callWordTitle = "编辑提示词模板"
          }
        }).finally(() => {
          this.callWordSubmitting = false
        })
      })
    },
    handlePublish() {
      if (!this.form.id) {
        this.$modal.msgWarning("请先保存模板")
        return
      }
      this.publishTargetType = 'score'
      this.publishForm = {
        id: this.form.id,
        platformId: null,
        name: this.form.name,
        price: this.form.price && Number(this.form.price) > 0 ? Number(this.form.price) : null
      }
      this.publishDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.publishFormRef) {
          this.$refs.publishFormRef.clearValidate()
        }
      })
    },
    handleCallWordPublish() {
      if (!this.callWordPersisted || !this.callWordForm.platformId) {
        this.$modal.msgWarning("请先保存提示词模板")
        return
      }
      this.publishTargetType = 'callword'
      this.publishForm = {
        id: null,
        platformId: this.callWordForm.platformId,
        name: this.callWordForm.platformId,
        price: this.callWordForm.price && Number(this.callWordForm.price) > 0 ? Number(this.callWordForm.price) : null
      }
      this.publishDialogVisible = true
      this.$nextTick(() => {
        this.$refs.publishFormRef && this.$refs.publishFormRef.clearValidate()
      })
    },
    submitPublish() {
      this.$refs.publishFormRef.validate(valid => {
        if (!valid) return
        this.publishing = true
        const publishRequest = this.publishTargetType === 'callword'
          ? publishCallWord({
              platformId: this.publishForm.platformId,
              price: this.publishForm.price
            })
          : publishScorePrompt({
              id: this.publishForm.id,
              price: this.publishForm.price
            })
        publishRequest.then(() => {
          this.$modal.msgSuccess("上架成功")
          this.publishDialogVisible = false
          if (this.publishTargetType === 'callword') {
            this.loadCallWord(this.callWordForm.platformId)
          } else {
            this.loadTemplate(this.form.id)
          }
        }).finally(() => {
          this.publishing = false
        })
      })
    },
    handleCancel() {
      this.$router.back()
    },
    handleDelete() {
      if (!this.form.id) return
      this.$modal.confirm('是否确认删除该模板？').then(() => {
        return deleteScorePrompt([this.form.id])
      }).then(() => {
        this.$modal.msgSuccess("删除成功")
        this.$router.push('/prompt-template/templateBox')
      }).catch(() => {})
    },
    handleCallWordDelete() {
      if (!this.callWordPersisted || !this.callWordForm.platformId) return
      this.$modal.confirm('是否确认删除该提示词模板？').then(() => {
        return deleteMediaCallWord([this.callWordForm.platformId])
      }).then(() => {
        this.$modal.msgSuccess("删除成功")
        this.$router.push('/prompt-template/templateBox')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-footer {
  margin-top: 20px;
  text-align: right;
}
</style>

