<template>
  <div class="daily-assistant-container">
    <el-row :gutter="20">
      <!-- 左侧：创建和文章列表 -->
      <el-col :span="10" :xs="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Document /></el-icon>
                日更助手
              </span>
              <el-button
                type="primary"
                size="small"
                @click="showConfigDialog"
              >
                <el-icon><Setting /></el-icon>
                配置智能体
              </el-button>
            </div>
          </template>

          <!-- 创建文章 -->
          <div class="create-section">
            <el-input
              v-model="articleTitle"
              placeholder="请输入公众号文章标题"
              clearable
              :disabled="creating"
              @keyup.enter="handleCreate"
            >
              <template #append>
                <el-button
                  type="primary"
                  :loading="creating"
                  @click="handleCreate"
                  :disabled="!articleTitle.trim() || selectedModels.length === 0"
                >
                  <el-icon><Plus /></el-icon>
                  创建优化
                </el-button>
              </template>
            </el-input>
            
            <!-- 模型选择 -->
            <div class="model-selection">
              <span class="selection-label">选择参与的模型：</span>
              <el-checkbox-group v-model="selectedModels">
                <el-checkbox :value="1" label="混元大模型" />
                <el-checkbox :value="2" label="DeepSeek" />
                <el-checkbox :value="3" label="精调模型" />
              </el-checkbox-group>
            </div>
            
            <div class="tip-text">
              <el-icon><InfoFilled /></el-icon>
              选择的模型将生成文章初稿，再由混元优化模型整合优化
            </div>
          </div>

          <!-- 文章列表 -->
          <div class="article-list">
            <div class="list-header">
              <span>我的文章</span>
              <el-button
                type="text"
                size="small"
                @click="loadArticles"
                :loading="loading"
              >
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
            </div>

            <el-scrollbar height="500px">
              <div
                v-for="article in articles"
                :key="article.id"
                class="article-item"
                :class="{ 
                  active: selectedArticle && selectedArticle.id === article.id,
                  'article-failed': article.processStatus === 2
                }"
                @click="selectArticle(article)"
              >
                <div class="article-header">
                  <span class="article-title">{{ article.articleTitle }}</span>
                  <el-tag
                    :type="getStatusType(article.processStatus)"
                    size="small"
                  >
                    {{ getStatusText(article.processStatus) }}
                  </el-tag>
                </div>
                <!-- 失败时显示错误信息 -->
                <div v-if="article.processStatus === 2" class="error-msg">
                  <el-icon><WarningFilled /></el-icon>
                  {{ article.errorMessage || '生成失败' }}
                </div>
                <div class="article-meta">
                  <span class="time">{{ formatTime(article.createTime) }}</span>
                  <div class="meta-actions">
                    <!-- 失败时显示重试按钮 -->
                    <el-button
                      v-if="article.processStatus === 2"
                      type="primary"
                      size="small"
                      text
                      @click.stop="handleRetry(article)"
                    >
                      <el-icon><RefreshRight /></el-icon>
                      重试
                    </el-button>
                    <el-button
                      type="danger"
                      size="small"
                      text
                      @click.stop="handleDelete(article)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>

              <el-empty
                v-if="!loading && articles.length === 0"
                description="暂无文章"
                :image-size="100"
              />
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：文章内容展示 -->
      <el-col :span="14" :xs="24">
        <el-card class="box-card content-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">
                <el-icon><Reading /></el-icon>
                文章内容
              </span>
            </div>
          </template>

          <div v-if="selectedArticle" class="content-section">
            <el-tabs v-model="activeTab" type="card">
              <!-- 优化后的内容 -->
              <el-tab-pane label="优化后内容" name="optimized">
                <div class="content-wrapper">
                  <div class="content-header">
                    <h3>优化后的文章</h3>
                    <div class="action-buttons">
                      <el-button
                        v-if="selectedArticle.optimizedContent"
                        type="primary"
                        size="small"
                        @click="copyContent(selectedArticle.optimizedContent)"
                      >
                        <el-icon><CopyDocument /></el-icon>
                        复制内容
                      </el-button>
                      <el-button
                        v-if="selectedArticle.optimizedContent"
                        type="success"
                        size="small"
                        @click="handleSmartLayout(selectedArticle.optimizedContent)"
                      >
                        <el-icon><Reading /></el-icon>
                        智能排版
                      </el-button>
                    </div>
                  </div>
                  <el-scrollbar height="600px">
                    <div v-if="selectedArticle.optimizedContent" class="content-text">
                      {{ selectedArticle.optimizedContent }}
                    </div>
                    <div v-else-if="selectedArticle.processStatus === 0" class="generating-status">
                      <el-icon class="is-loading"><Loading /></el-icon>
                      <p>正在整合优化中...</p>
                      <p class="tip">已生成的模型内容可在其他标签页查看</p>
                    </div>
                    <el-empty
                      v-else
                      description="等待智能体优化..."
                      :image-size="100"
                    />
                  </el-scrollbar>
                </div>
              </el-tab-pane>

              <!-- 混元大模型内容 -->
              <el-tab-pane
                v-if="isModelSelected(selectedArticle, 1)"
                name="model1"
              >
                <template #label>
                  <span>
                    {{ selectedArticle.model1Name || '混元大模型' }}
                    <el-tag v-if="selectedArticle.model1Content" type="success" size="small" style="margin-left: 5px;">已生成</el-tag>
                    <el-tag v-else type="info" size="small" style="margin-left: 5px;">生成中</el-tag>
                  </span>
                </template>
                <div class="content-wrapper">
                  <div class="content-header">
                    <h3>{{ selectedArticle.model1Name || '混元大模型' }}</h3>
                    <div class="action-buttons">
                      <el-button
                        v-if="selectedArticle.model1Content"
                        type="primary"
                        size="small"
                        @click="copyContent(selectedArticle.model1Content)"
                      >
                        <el-icon><CopyDocument /></el-icon>
                        复制内容
                      </el-button>
                      <el-button
                        v-if="selectedArticle.model1Content"
                        type="success"
                        size="small"
                        @click="handleSmartLayout(selectedArticle.model1Content)"
                      >
                        <el-icon><Reading /></el-icon>
                        智能排版
                      </el-button>
                    </div>
                  </div>
                  <el-scrollbar height="600px">
                    <div v-if="selectedArticle.model1Content" class="content-text">
                      {{ selectedArticle.model1Content }}
                    </div>
                    <div v-else class="generating-status">
                      <el-icon class="is-loading"><Loading /></el-icon>
                      <p>正在生成中...</p>
                    </div>
                  </el-scrollbar>
                </div>
              </el-tab-pane>

              <!-- DeepSeek内容 -->
              <el-tab-pane
                v-if="isModelSelected(selectedArticle, 2)"
                name="model2"
              >
                <template #label>
                  <span>
                    {{ selectedArticle.model2Name || 'DeepSeek' }}
                    <el-tag v-if="selectedArticle.model2Content" type="success" size="small" style="margin-left: 5px;">已生成</el-tag>
                    <el-tag v-else type="info" size="small" style="margin-left: 5px;">生成中</el-tag>
                  </span>
                </template>
                <div class="content-wrapper">
                  <div class="content-header">
                    <h3>{{ selectedArticle.model2Name || 'DeepSeek' }}</h3>
                    <div class="action-buttons">
                      <el-button
                        v-if="selectedArticle.model2Content"
                        type="primary"
                        size="small"
                        @click="copyContent(selectedArticle.model2Content)"
                      >
                        <el-icon><CopyDocument /></el-icon>
                        复制内容
                      </el-button>
                      <el-button
                        v-if="selectedArticle.model2Content"
                        type="success"
                        size="small"
                        @click="handleSmartLayout(selectedArticle.model2Content)"
                      >
                        <el-icon><Reading /></el-icon>
                        智能排版
                      </el-button>
                    </div>
                  </div>
                  <el-scrollbar height="600px">
                    <div v-if="selectedArticle.model2Content" class="content-text">
                      {{ selectedArticle.model2Content }}
                    </div>
                    <div v-else class="generating-status">
                      <el-icon class="is-loading"><Loading /></el-icon>
                      <p>正在生成中...</p>
                    </div>
                  </el-scrollbar>
                </div>
              </el-tab-pane>

              <!-- 大模型3内容 -->
              <el-tab-pane
                v-if="isModelSelected(selectedArticle, 3)"
                name="model3"
              >
                <template #label>
                  <span>
                    {{ selectedArticle.model3Name || '精调模型' }}
                    <el-tag v-if="selectedArticle.model3Content" type="success" size="small" style="margin-left: 5px;">已生成</el-tag>
                    <el-tag v-else type="info" size="small" style="margin-left: 5px;">生成中</el-tag>
                  </span>
                </template>
                <div class="content-wrapper">
                  <div class="content-header">
                    <h3>{{ selectedArticle.model3Name || '精调模型' }}</h3>
                    <div class="action-buttons">
                      <el-button
                        v-if="selectedArticle.model3Content"
                        type="primary"
                        size="small"
                        @click="copyContent(selectedArticle.model3Content)"
                      >
                        <el-icon><CopyDocument /></el-icon>
                        复制内容
                      </el-button>
                      <el-button
                        v-if="selectedArticle.model3Content"
                        type="success"
                        size="small"
                        @click="handleSmartLayout(selectedArticle.model3Content)"
                      >
                        <el-icon><Reading /></el-icon>
                        智能排版
                      </el-button>
                    </div>
                  </div>
                  <el-scrollbar height="600px">
                    <div v-if="selectedArticle.model3Content" class="content-text">
                      {{ selectedArticle.model3Content }}
                    </div>
                    <div v-else class="generating-status">
                      <el-icon class="is-loading"><Loading /></el-icon>
                      <p>正在生成中...</p>
                    </div>
                  </el-scrollbar>
                </div>
              </el-tab-pane>
            </el-tabs>

            <!-- 错误信息 -->
            <el-alert
              v-if="selectedArticle.processStatus === 2 && selectedArticle.errorMessage"
              type="error"
              :title="selectedArticle.errorMessage"
              :closable="false"
              style="margin-top: 10px"
            />
          </div>

          <el-empty
            v-else
            description="请选择一篇文章查看内容"
            :image-size="150"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 智能排版对话框 -->
    <el-dialog
      v-model="layoutDialogVisible"
      title="智能排版（腾讯元器）"
      width="80%"
      :close-on-click-modal="false"
    >
      <el-tabs v-model="layoutActiveTab">
        <el-tab-pane label="原文内容" name="original">
          <el-scrollbar height="500px">
            <div class="layout-content">
              {{ layoutContent }}
            </div>
          </el-scrollbar>
        </el-tab-pane>

        <el-tab-pane label="排版后内容" name="layouted" :disabled="!layoutedContent">
          <div class="layout-actions" style="margin-bottom: 15px;">
            <el-button
              v-if="layoutedContent"
              type="primary"
              size="small"
              @click="copyLayoutedContent"
            >
              <el-icon><CopyDocument /></el-icon>
              复制排版内容
            </el-button>
            <el-button
              v-if="layoutedContent && selectedArticle"
              type="success"
              size="small"
              :loading="publishing"
              style="margin-left: 10px;"
              @click="handlePublishToWechat('layout')"
            >
              <el-icon><Upload /></el-icon>
              投递到公众号
            </el-button>
          </div>
          <el-scrollbar height="500px">
            <div class="layout-content" v-html="layoutedContent"></div>
          </el-scrollbar>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <el-button @click="layoutDialogVisible = false">关闭</el-button>
        <el-button 
          type="primary" 
          @click="startLayout" 
          :loading="layouting"
          :disabled="layouting || !layoutContent"
        >
          {{ layouting ? '排版中...' : '开始排版' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 配置智能体对话框 -->
    <el-dialog
      v-model="configDialogVisible"
      title="配置腾讯元器智能体"
      width="600px"
      @close="handleConfigDialogClose"
    >
      <el-form
        ref="configFormRef"
        :model="configForm"
        :rules="configRules"
        label-width="120px"
      >
        <el-form-item label="智能体ID" prop="agentId">
          <el-input
            v-model="configForm.agentId"
            placeholder="请输入appid"
          />
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            从 智能体配置→应用发布→体验链接 中获取
          </div>
        </el-form-item>
        <el-form-item label="智能体名称" prop="agentName">
          <el-input
            v-model="configForm.agentName"
            placeholder="自定义名称，如：文章优化助手"
          />
        </el-form-item>
        <el-form-item label="API密钥" prop="apiKey">
          <el-input
            v-model="configForm.apiKey"
            type="password"
            placeholder="请输入appkey"
            show-password
          />
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            从 应用发布→API管理 中获取
          </div>
        </el-form-item>
        <el-form-item label="API端点" prop="apiEndpoint">
          <el-input
            v-model="configForm.apiEndpoint"
            placeholder="https://yuanqi.tencent.com/openapi/v1/agent/chat/completions"
          />
          <div style="color: #909399; font-size: 12px; margin-top: 4px;">
            固定地址，直接复制即可
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveConfig">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import {
  Document,
  Plus,
  InfoFilled,
  Refresh,
  Delete,
  Reading,
  CopyDocument,
  Setting,
  Upload,
  WarningFilled,
  RefreshRight,
  Loading
} from '@element-plus/icons-vue'
import {
  getMyArticles,
  createAndOptimize,
  delDailyArticle,
  publishToWechat,
  layoutArticleNew as layoutArticle
} from '@/api/dailyArticle'
import {
  getMyConfig,
  addYuanqiConfig,
  updateYuanqiConfig
} from '@/api/yuanqiConfig'

// 获取 Vuex store
const store = useStore()

// 数据
const articleTitle = ref('')
const selectedModels = ref([1, 2, 3]) // 默认全选三个模型
const creating = ref(false)
const loading = ref(false)
const articles = ref([])
const selectedArticle = ref(null)
const activeTab = ref('optimized')
const publishing = ref(false)

// 配置对话框
const configDialogVisible = ref(false)
const configFormRef = ref(null)
const configForm = ref({
  id: null,
  agentId: '',
  agentName: '',
  apiKey: '',
  apiEndpoint: '',
  isActive: 1
})

// 排版相关
const layoutDialogVisible = ref(false)
const layoutContent = ref('')
const layoutedContent = ref('')
const layouting = ref(false)
const layoutActiveTab = ref('original')

const configRules = {
  agentId: [{ required: true, message: '请输入智能体ID', trigger: 'blur' }],
  agentName: [{ required: true, message: '请输入智能体名称', trigger: 'blur' }],
  apiKey: [{ required: true, message: '请输入API密钥', trigger: 'blur' }],
  apiEndpoint: [{ required: true, message: '请输入API端点URL', trigger: 'blur' }]
}

// 加载文章列表
const loadArticles = async () => {
  loading.value = true
  try {
    const response = await getMyArticles()
    const newArticles = response.data || []
    
    console.log('文章列表已刷新，共', newArticles.length, '篇文章')
    
    // 更新文章列表
    articles.value = newArticles
    
    // 如果有选中的文章，更新它的状态
    if (selectedArticle.value) {
      const updated = newArticles.find(a => a.id === selectedArticle.value.id)
      if (updated) {
        selectedArticle.value = updated
        console.log('已更新选中文章状态：', updated.processStatus)
      }
    }
  } catch (error) {
    console.error('加载文章列表失败:', error)
    ElMessage.error('加载文章列表失败')
  } finally {
    loading.value = false
  }
}

// 创建文章并优化
const handleCreate = async () => {
  if (!articleTitle.value.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  const title = articleTitle.value.trim()
  creating.value = true
  
  try {
    const response = await createAndOptimize(title, selectedModels.value)
    if (response.code === 200) {
      const newArticleId = response.data?.id
      articleTitle.value = ''
      
      // 显示生成中提示
      ElMessage({
        message: `文章"${title}"正在生成中，可查看已生成的模型内容`,
        type: 'info',
        duration: 10000
      })
      
      // 立即刷新列表（新文章会出现，status=0）
      await loadArticles()
      
      // 自动选中新创建的文章
      if (newArticleId) {
        const newArticle = articles.value.find(a => a.id === newArticleId)
        if (newArticle) {
          selectedArticle.value = newArticle
        }
        
        // 启动轮询，监控生成进度
        startPollingArticle(newArticleId, title)
      }
    } else {
      ElMessage.error(response.msg || '创建失败')
    }
  } catch (error) {
    ElMessage.error('创建失败，请检查智能体配置')
    console.error('创建文章失败:', error)
    if (error.response && error.response.status === 400) {
      ElMessageBox.confirm(
        '未找到智能体配置，是否现在配置？',
        '提示',
        {
          confirmButtonText: '去配置',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        showConfigDialog()
      })
    }
  } finally {
    creating.value = false
  }
}

// 新增：轮询单篇文章的生成状态
const startPollingArticle = (articleId, articleTitle) => {
  let pollCount = 0
  const maxPolls = 60  // 5分钟（60 * 5秒 = 300秒）
  
  const pollInterval = setInterval(async () => {
    pollCount++
    
    try {
      // 刷新列表，获取最新的文章内容（包括已生成的模型内容）
      await loadArticles()
      const article = articles.value.find(a => a.id === articleId)
      
      if (article) {
        // 更新选中的文章，显示最新内容
        if (selectedArticle.value && selectedArticle.value.id === articleId) {
          selectedArticle.value = article
        }
        
        // 只有在完成或失败时才停止轮询
        if (article.processStatus === 1) {
          clearInterval(pollInterval)
          ElMessage({
            message: `文章"${articleTitle}"生成完成！`,
            type: 'success',
            duration: 3000
          })
        } else if (article.processStatus === 2) {
          clearInterval(pollInterval)
          ElMessage({
            message: `文章"${articleTitle}"生成失败：${article.errorMessage || '未知错误'}`,
            type: 'error',
            duration: 5000
          })
        }
        // status=0 继续轮询，用户可以看到逐步生成的模型内容
        
      } else if (pollCount >= maxPolls) {
        // 超时处理
        clearInterval(pollInterval)
        ElMessage({
          message: `文章"${articleTitle}"生成超时（超过5分钟），请检查文章状态`,
          type: 'warning',
          duration: 5000
        })
      }
    } catch (error) {
      console.error('轮询失败:', error)
    }
  }, 5000)
}

// 选择文章
const selectArticle = (article) => {
  selectedArticle.value = article
  activeTab.value = 'optimized'
}

// 删除文章
const handleDelete = async (article) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这篇文章吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await delDailyArticle(article.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      if (selectedArticle.value && selectedArticle.value.id === article.id) {
        selectedArticle.value = null
      }
      await loadArticles()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 新增：重试生成失败的文章
const handleRetry = async (article) => {
  try {
    await ElMessageBox.confirm(
      `确定要重新生成文章"${article.articleTitle}"吗？`,
      '重新生成确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 先删除失败的记录
    await delDailyArticle(article.id)
    
    // 重新创建
    const models = article.selectedModels ? article.selectedModels.split(',').map(Number) : [1, 2, 3]
    const response = await createAndOptimize(article.articleTitle, models)
    
    if (response.code === 200) {
      const newArticleId = response.data?.id
      
      ElMessage({
        message: `文章"${article.articleTitle}"正在重新生成中`,
        type: 'info',
        duration: 10000
      })
      
      // 从列表中移除失败的文章
      await loadArticles()
      
      // 启动轮询
      if (newArticleId) {
        startPollingArticle(newArticleId, article.articleTitle)
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重试失败:', error)
      ElMessage.error('重试失败')
    }
  }
}

// 复制内容
const copyContent = (content) => {
  navigator.clipboard.writeText(content).then(() => {
    ElMessage.success('复制成功')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

// 投递文章到公众号
const handlePublishToWechat = async (contentType) => {
  if (!selectedArticle.value) {
    ElMessage.warning('请先选择文章')
    return
  }
  
  // 内容类型名称映射
  const contentTypeNames = {
    'optimized': '优化后的文章',
    'model1': selectedArticle.value.model1Name || '模型1文章',
    'model2': selectedArticle.value.model2Name || '模型2文章',
    'model3': selectedArticle.value.model3Name || '模型3文章',
    'layout': '排版后的文章'
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要将 "${contentTypeNames[contentType]}" 投递到公众号草稿箱吗？`,
      '投递确认',
      {
        confirmButtonText: '确定投递',
        cancelButtonText: '取消',
        type: 'info'
      }
    )
    
    publishing.value = true
    
    const response = await publishToWechat(
      selectedArticle.value.id,
      contentType,
      contentType === 'layout' ? layoutedContent.value : undefined
    )
    
    if (response.code === 200) {
      ElMessage.success({
        message: '投递成功！已保存到公众号草稿箱',
        duration: 3000
      })
      
      // 刷新文章列表（更新投递次数）
      await loadArticles()
    } else {
      ElMessage.error(response.msg || '投递失败')
    }
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('投递失败：', error)
      ElMessage.error('投递失败：' + (error.message || error))
    }
  } finally {
    publishing.value = false
  }
}

// 显示配置对话框
const showConfigDialog = async () => {
  try {
    const response = await getMyConfig()
    if (response.code === 200 && response.data) {
      configForm.value = { ...response.data }
    }
  } catch (error) {
    console.error('加载配置失败', error)
  }
  configDialogVisible.value = true
}

// 保存配置
const handleSaveConfig = async () => {
  try {
    await configFormRef.value.validate()
    
    // 准备保存的数据，添加userId（后端会自动从当前登录用户获取，这里传null即可被后端自动填充）
    // 或者如果需要明确传递，可以从store获取：import { useStore } from 'vuex'; const store = useStore(); configData.userId = store.state.user.userId
    const configData = {
      ...configForm.value
      // userId会由后端根据当前登录用户自动设置，无需前端传递
    }
    
    const apiFunc = configForm.value.id ? updateYuanqiConfig : addYuanqiConfig
    const response = await apiFunc(configData)
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      configDialogVisible.value = false
    }
  } catch (error) {
    console.error('保存配置失败:', error)
    ElMessage.error('保存失败: ' + (error.response?.data?.msg || error.message))
  }
}

// 关闭配置对话框
const handleConfigDialogClose = () => {
  configFormRef.value?.resetFields()
}

// 打开智能排版对话框
const handleSmartLayout = (content) => {
  if (!content) {
    ElMessage.warning('内容为空，无法排版')
    return
  }

  layoutContent.value = content
  layoutedContent.value = ''
  layoutActiveTab.value = 'original'
  layoutDialogVisible.value = true
}

// 开始排版（调用后端API）
const startLayout = async () => {
  if (!layoutContent.value) {
    ElMessage.warning('排版内容为空')
    return
  }

  layouting.value = true

  try {
    // 使用统一的request封装，自动带上token并走代理
    const result = await layoutArticle(layoutContent.value)

    // axios拦截器已保证code===200才会走到这里
    layoutedContent.value = result.data?.layoutedContent || ''
    layoutActiveTab.value = 'layouted'
    ElMessage.success('排版完成！')
  } catch (error) {
    console.error('排版失败:', error)
    ElMessage.error('排版失败：' + (error.message || error))
  } finally {
    layouting.value = false
  }
}

// 复制排版后的内容
const copyLayoutedContent = () => {
  if (!layoutedContent.value) {
    ElMessage.warning('排版内容为空')
    return
  }

  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = layoutedContent.value
  const textContent = tempDiv.textContent || tempDiv.innerText

  navigator.clipboard.writeText(textContent).then(() => {
    ElMessage.success('复制成功（纯文本）')
  }).catch(() => {
    navigator.clipboard.writeText(layoutedContent.value).then(() => {
      ElMessage.success('复制成功（HTML）')
    }).catch(() => {
      ElMessage.error('复制失败')
    })
  })
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '处理中',
    1: '已完成',
    2: '失败'
  }
  return texts[status] || '未知'
}

// 检查模型是否被选中
const isModelSelected = (article, modelIndex) => {
  if (!article || !article.selectedModels) {
    return true // 如果没有selectedModels字段，默认全部显示（兼容旧数据）
  }
  return article.selectedModels.includes(String(modelIndex))
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 页面加载
onMounted(async () => {
  loadArticles()
  
  // 检查是否有配置
  try {
    const response = await getMyConfig()
    if (!response.data) {
      // 如果没有配置，显示提示
      ElMessageBox.confirm(
        '您还未配置腾讯元器智能体，需要先配置才能使用日更助手功能。',
        '提示',
        {
          confirmButtonText: '去配置',
          cancelButtonText: '稍后配置',
          type: 'info',
          closeOnClickModal: false
        }
      ).then(() => {
        showConfigDialog()
      }).catch(() => {
        // 用户选择稍后配置
      })
    }
  } catch (error) {
    console.log('检查配置失败', error)
  }
  
  // 定期刷新列表（每30秒），保持数据新鲜
  setInterval(() => {
    loadArticles()
  }, 30000)
})

</script>

<style scoped lang="scss">
.daily-assistant-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 16px;
      font-weight: bold;
    }

    .el-button {
      margin-left: auto;
    }
  }

  .create-section {
    margin-bottom: 20px;

    .model-selection {
      margin-top: 15px;
      padding: 12px;
      background-color: #f5f7fa;
      border-radius: 4px;

      .selection-label {
        font-size: 14px;
        font-weight: 500;
        color: #606266;
        margin-right: 15px;
      }

      :deep(.el-checkbox-group) {
        display: inline-flex;
        gap: 20px;
      }

      :deep(.el-checkbox) {
        margin-right: 0;
      }
    }

    .tip-text {
      display: flex;
      align-items: center;
      gap: 5px;
      margin-top: 10px;
      font-size: 12px;
      color: #909399;
    }
  }

  .article-list {
    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      font-weight: bold;
    }

    .article-item {
      padding: 12px;
      margin-bottom: 8px;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background-color: #f5f7fa;
        border-color: #409eff;
      }

      &.active {
        background-color: #ecf5ff;
        border-color: #409eff;
      }

      // 失败文章样式
      &.article-failed {
        border-left: 4px solid #f56c6c;
        background-color: #fef0f0;

        &:hover {
          background-color: #fde2e2;
          border-color: #f56c6c;
        }

        &.active {
          background-color: #fde2e2;
          border-color: #f56c6c;
        }
      }

      .article-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;

        .article-title {
          font-weight: 500;
          color: #303133;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      // 错误信息样式
      .error-msg {
        display: flex;
        align-items: center;
        gap: 5px;
        font-size: 12px;
        color: #f56c6c;
        margin-bottom: 8px;
        padding: 4px 8px;
        background-color: #fff;
        border-radius: 4px;
      }

      .article-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .time {
          font-size: 12px;
          color: #909399;
        }

        .meta-actions {
          display: flex;
          gap: 5px;
          align-items: center;
        }
      }
    }
  }

  .content-card {
    height: calc(100vh - 140px);
  }

  .content-section {
    .content-wrapper {
      .content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid #e4e7ed;

        h3 {
          margin: 0;
          font-size: 18px;
          color: #303133;
        }

        .action-buttons {
          display: flex;
          gap: 10px;
        }
      }

      .content-text {
        padding: 15px;
        background-color: #f5f7fa;
        border-radius: 4px;
        line-height: 1.8;
        white-space: pre-wrap;
        word-wrap: break-word;
        color: #606266;
      }

      .generating-status {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 60px 20px;
        color: #909399;

        .el-icon {
          font-size: 48px;
          margin-bottom: 20px;
          color: #409eff;
        }

        p {
          margin: 8px 0;
          font-size: 16px;

          &.tip {
            font-size: 14px;
            color: #c0c4cc;
          }
        }
      }
    }
  }

  // 排版对话框样式
  .layout-content {
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 4px;
    line-height: 1.8;
    white-space: pre-wrap;
    word-wrap: break-word;
    color: #333;
    font-size: 14px;

    :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
      margin-top: 20px;
      margin-bottom: 10px;
      font-weight: bold;
    }

    :deep(p) {
      margin-bottom: 10px;
    }

    :deep(img) {
      max-width: 100%;
      height: auto;
    }
  }

  .layout-actions {
    display: flex;
    gap: 10px;
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .daily-assistant-container {
    padding: 10px;

    .content-card {
      height: auto;
      margin-top: 20px;
    }
  }
}
</style>
