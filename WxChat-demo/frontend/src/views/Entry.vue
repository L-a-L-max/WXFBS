<template>
  <div class="entry-container">
    <el-card class="entry-card">
      <div class="loading-content">
        <el-icon class="loading-icon" :size="48"><Loading /></el-icon>
        <p>正在跳转到企业微信授权...</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { Loading } from '@element-plus/icons-vue'

onMounted(() => {
  // 直接跳转到授权接口
  // 后端会处理回调并返回HTML页面自动跳转
  const currentUrl = window.location.origin + '/api/auth/callback'
  window.location.href = `/api/auth/authorize?redirect=${encodeURIComponent(currentUrl)}`
})
</script>

<style scoped>
.entry-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.entry-card {
  width: 300px;
  text-align: center;
  padding: 40px 20px;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.loading-icon {
  animation: spin 1s linear infinite;
  color: #667eea;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.loading-content p {
  color: #606266;
  margin: 0;
}
</style>
