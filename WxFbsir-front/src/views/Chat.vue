<template>
  <div class="chat-container">
    <!-- 头部 -->
    <div class="chat-header">
      <div class="header-content">
        <el-icon class="header-icon"><ChatDotRound /></el-icon>
        <span class="header-title">潜力眼-HR智能助手</span>
      </div>
      <div class="header-info">
        <span>会话ID: {{ sessionId }}</span>
      </div>
    </div>

    <!-- 消息区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <!-- 欢迎消息 -->
      <div v-if="messages.length === 0" class="welcome-message">
        <el-icon class="welcome-icon"><ChatLineSquare /></el-icon>
        <h3>你好！潜力眼-HR智能助手</h3>
        <p>有什么可以帮助你的？</p>
      </div>

      <!-- 消息列表 -->
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.type]"
      >
        <div class="message-avatar">
          <el-avatar v-if="msg.type === 'user'" :size="36" :icon="User" />
          <el-avatar v-else :size="36" :icon="Monitor" />
        </div>
        <div class="message-content">
          <div class="message-bubble">{{ msg.content }}</div>
          <div class="message-time">{{ msg.time }}</div>
        </div>
      </div>

      <!-- 加载中指示器 -->
      <div v-if="isLoading" class="message ai">
        <div class="message-avatar">
          <el-avatar :size="36" :icon="Monitor" />
        </div>
        <div class="message-content">
          <div class="message-bubble typing">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
      <!-- 已选文件列表 -->
      <div v-if="selectedFiles.length > 0" class="selected-files">
        <el-tag
          v-for="(file, index) in selectedFiles"
          :key="index"
          size="small"
          closable
          @close="removeFile(index)"
        >
          {{ file.name }}
        </el-tag>
      </div>
      
      <el-input
        v-model="inputMessage"
        placeholder="输入消息..."
        :disabled="isLoading"
        @keyup.enter="sendMessageHandler"
        clearable
      >
        <template #prepend>
          <el-upload
            action="#"
            :on-change="handleFileChange"
            :before-upload="beforeUpload"
            :auto-upload="false"
            :multiple="true"
            :limit="5"
            :file-list="[]"
            :show-file-list="false"
          >
            <el-button
              type="primary"
              :disabled="isLoading"
            >
              <el-icon><Upload /></el-icon>
            </el-button>
          </el-upload>
        </template>
        <template #append>
          <el-button
            type="primary"
            :loading="isLoading"
            :disabled="!inputMessage.trim() || isLoading"
            @click="sendMessageHandler"
          >
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
        </template>
      </el-input>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onUnmounted } from 'vue'
import { ChatDotRound, ChatLineSquare, User, Monitor, Promotion, Upload } from '@element-plus/icons-vue'
import { sendMessage, pollReply } from '../api'
import { ElMessage } from 'element-plus'

// 响应式数据
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const sessionId = ref('')
const messagesContainer = ref(null)
const selectedFiles = ref([])

// 轮询相关
let pollTimer = null
let pollCount = 0
const MAX_POLL = 30

function getWeWorkLogin() {
  try {
    const raw = sessionStorage.getItem('wework_login')
    return raw ? JSON.parse(raw) : null
  } catch (e) {
    return null
  }
}

// 初始化
onMounted(() => {
  // 使用企业微信登录信息
  const login = getWeWorkLogin()
  if (login && login.weWorkUserId) {
    sessionId.value = login.weWorkUserId
    localStorage.setItem('sessionId', sessionId.value)
  } else {
    // 未登录，跳转到登录页
    window.location.href = '/'
  }
})

// 清理
onUnmounted(() => {
  if (pollTimer) {
    clearTimeout(pollTimer)
  }
})

// 发送消息
const sendMessageHandler = async () => {
  const message = inputMessage.value.trim()
  
  // 检查是否有消息内容
  if (!message || isLoading.value) return

  // 添加用户消息
  let displayMessage = message
  if (selectedFiles.value.length > 0) {
    const fileNames = selectedFiles.value.map(file => file.name).join(', ')
    displayMessage = message + (message ? '\n' : '') + `[已上传文件: ${fileNames}]`
  }
  
  messages.value.push({
    type: 'user',
    content: displayMessage,
    time: formatTime(new Date())
  })
  
  // 清空输入和已选文件
  inputMessage.value = ''
  const filesToSend = [...selectedFiles.value]
  selectedFiles.value = []
  
  isLoading.value = true
  scrollToBottom()

  try {
    let res
    
    // 如果有文件，使用FormData格式发送
    if (filesToSend.length > 0) {
      const formData = new FormData()
      formData.append('message', message)
      formData.append('sessionId', sessionId.value)
      
      // 添加所有选中的文件
      filesToSend.forEach(file => {
        formData.append('files', file.raw)
      })
      
      res = await sendMessage(formData)
    } else {
      // 普通文本消息发送
      res = await sendMessage({
        message: message,
        sessionId: sessionId.value
      })
    }

    if (res.code === 200) {
      // 发送成功后，立即开始轮询获取AI回复
      pollCount = 0
      startPolling()
    } else {
      ElMessage.error(res.msg || '发送失败')
      isLoading.value = false
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('网络错误，请稍后重试')
    isLoading.value = false
  }
}

// 开始轮询
const startPolling = () => {
  if (pollTimer) clearTimeout(pollTimer)

  pollTimer = setTimeout(async () => {
    try {
      const res = await pollReply(sessionId.value)
      if (res.code === 200 && res.data) {
        if (res.data.hasReply && res.data.reply) {
          addAiMessage(res.data.reply)
          isLoading.value = false
        } else {
          pollCount++
          if (pollCount < MAX_POLL) {
            startPolling()
          } else {
            addAiMessage('消息已发送，回复可能稍有延迟')
            isLoading.value = false
          }
        }
      } else {
        pollCount++
        if (pollCount < MAX_POLL) {
          startPolling()
        } else {
          isLoading.value = false
        }
      }
    } catch (error) {
      console.error('轮询失败:', error)
      pollCount++
      if (pollCount < MAX_POLL) {
        startPolling()
      } else {
        isLoading.value = false
      }
    }
  }, 10000)
}

// 添加AI消息
const addAiMessage = (content) => {
  messages.value.push({
    type: 'ai',
    content: content,
    time: formatTime(new Date())
  })
  scrollToBottom()
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 文件处理相关方法
// 处理文件选择
const handleFileChange = (file, fileList) => {
  selectedFiles.value = fileList
}

// 上传前处理，阻止自动上传
const beforeUpload = (file) => {
  return false // 阻止自动上传，文件将在发送消息时一起上传
}

// 移除已选文件
const removeFile = (index) => {
  selectedFiles.value.splice(index, 1)
}

// 格式化时间
const formatTime = (date) => {
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}

.chat-header {
  background: #409eff;
  color: white;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-icon {
  font-size: 20px;
}

.header-title {
  font-size: 16px;
  font-weight: 500;
}

.header-info {
  font-size: 12px;
  opacity: 0.9;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.welcome-message {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.welcome-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 15px;
}

.welcome-message h3 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 18px;
}

.welcome-message p {
  margin: 0;
  font-size: 14px;
}

.message {
  display: flex;
  gap: 10px;
  max-width: 80%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message.ai {
  align-self: flex-start;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.message.user .message-content {
  align-items: flex-end;
}

.message-bubble {
  padding: 10px 15px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message.user .message-bubble {
  background: #409eff;
  color: white;
  border-bottom-right-radius: 2px;
}

.message.ai .message-bubble {
  background: white;
  color: #333;
  border-bottom-left-radius: 2px;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.typing {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 10px 15px;
}

.typing .dot {
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.typing .dot:nth-child(1) {
  animation-delay: -0.32s;
}

.typing .dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0.5);
    opacity: 0.4;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input {
  flex-shrink: 0;
  padding: 15px 20px;
  background: white;
  border-top: 1px solid #e0e0e0;
}

.selected-files {
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.chat-input :deep(.el-input-group__append .el-button) {
  border-radius: 0;
  padding: 0 20px;
  height: 40px;
  line-height: 40px;
  min-width: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-input :deep(.el-input__wrapper) {
  border-radius: 0;
  height: 40px;
  padding: 0 12px;
  display: flex;
  align-items: center;
}

.chat-input :deep(.el-input-group__prepend .el-upload .el-button) {
  border-radius: 0;
  padding: 0 15px;
  height: 40px;
  line-height: 40px;
  border-right: none;
  min-width: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-input :deep(.el-input-group) {
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  align-items: stretch;
  height: 40px;
}

.chat-input :deep(.el-input-group__prepend),
.chat-input :deep(.el-input-group__append) {
  display: flex;
  align-items: center;
}

.chat-input :deep(.el-input__inner) {
  height: 100%;
  line-height: 1.5;
}
</style>
