<template>
  <div class="chat-container">
    <!-- 头部 -->
    <div class="chat-header">
      <div class="header-content">
        <el-icon class="header-icon"><ChatDotRound /></el-icon>
        <span class="header-title">AI 智能助手</span>
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
        <h3>你好！我是 AI 智能助手</h3>
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
          <el-avatar v-else :size="36" :icon="Monitor" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" />
        </div>
        <div class="message-content">
          <div class="message-bubble">{{ msg.content }}</div>
          <div class="message-time">{{ msg.time }}</div>
        </div>
      </div>

      <!-- 加载中指示器 -->
      <div v-if="isLoading" class="message ai">
        <div class="message-avatar">
          <el-avatar :size="36" :icon="Monitor" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" />
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
      <el-input
        v-model="inputMessage"
        placeholder="输入消息..."
        :disabled="isLoading"
        @keyup.enter="sendMessageHandler"
        clearable
      >
        <template #append>
          <el-button
            type="primary"
            :loading="isLoading"
            :disabled="!inputMessage.trim()"
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
import { ChatDotRound, ChatLineSquare, User, Monitor, Promotion } from '@element-plus/icons-vue'
import { sendMessage, pollReply } from '../api'
import { ElMessage } from 'element-plus'

// 响应式数据
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const sessionId = ref('')
const messagesContainer = ref(null)

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
  if (!message || isLoading.value) return

  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: message,
    time: formatTime(new Date())
  })
  inputMessage.value = ''
  isLoading.value = true
  scrollToBottom()

  try {
    const res = await sendMessage({
      message: message,
      sessionId: sessionId.value
    })

    if (res.code === 200) {
      // 发送成功后，立即开始轮询获取AI回复
      // 不显示"消息已发送"这样的中间状态
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
  }, 1000)
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
  background: #f5f7fa;
}

.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 20px;
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
  font-size: 24px;
}

.header-title {
  font-size: 18px;
  font-weight: 500;
}

.header-info {
  font-size: 12px;
  opacity: 0.8;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-message {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
}

.welcome-icon {
  font-size: 64px;
  color: #667eea;
  margin-bottom: 16px;
}

.welcome-message h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-weight: 500;
}

.welcome-message p {
  margin: 0;
  font-size: 14px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 80%;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
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
  gap: 4px;
}

.message.user .message-content {
  align-items: flex-end;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.6;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.message.user .message-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.message.ai .message-bubble {
  background: white;
  color: #303133;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.message-time {
  font-size: 11px;
  color: #909399;
  padding: 0 4px;
}

/* 打字动画 */
.typing {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 16px 20px;
}

.typing .dot {
  width: 8px;
  height: 8px;
  background: #667eea;
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
    transform: scale(0.6);
    opacity: 0.4;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid #e4e7ed;
}

.chat-input :deep(.el-input-group__append) {
  padding: 0;
}

.chat-input :deep(.el-input-group__append .el-button) {
  border-radius: 0 4px 4px 0;
  padding: 12px 20px;
}
</style>
