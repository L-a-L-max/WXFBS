<template>
  <div class="home-container">
    <el-card class="login-card">
      <div class="logo">
        <el-icon :size="64" color="#667eea"><ChatDotRound /></el-icon>
      </div>
      <h1>潜力眼</h1>
      <p class="desc">潜力眼-HR智能助手</p>
      
      <div class="actions">
        <el-button type="primary" size="large" @click="loginWithWeWork">
          <el-icon><Connection /></el-icon>
          企业微信登录
        </el-button>
      </div>
      
      <div class="tips">
        <p>提示：企业微信登录需要在企业微信客户端中打开</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ChatDotRound, Connection } from '@element-plus/icons-vue'

function loginWithWeWork() {
  // 跳转到企业微信OAuth授权
  window.location.href = `/api/auth/authorize`
}
</script>

<style scoped>
.home-container {
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

.home-container::before {
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

.login-card {
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
  margin-bottom: 24px;
  display: inline-block;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 12px 32px rgba(102, 126, 234, 0.6);
  }
}

h1 {
  margin: 0 0 12px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 1px;
}

.desc {
  color: #6b7280;
  margin-bottom: 40px;
  font-size: 15px;
  font-weight: 400;
}

.actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.actions :deep(.el-button) {
  height: 50px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.actions :deep(.el-button)::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
  transition: left 0.5s ease;
}

.actions :deep(.el-button:hover)::before {
  left: 100%;
}

.actions :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.5);
}

.actions :deep(.el-button:active) {
  transform: translateY(0);
}

.actions :deep(.el-button .el-icon) {
  margin-right: 8px;
  font-size: 18px;
}

.tips {
  margin-top: 35px;
  padding-top: 25px;
  border-top: 2px solid #f3f4f6;
}

.tips p {
  color: #9ca3af;
  font-size: 13px;
  margin: 0;
  line-height: 1.6;
}
</style>
