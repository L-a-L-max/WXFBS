<template>
  <div class="entry-container">
    <el-card class="entry-card">
      <div class="loading-content">
        <el-icon class="loading-icon" :size="48"><Loading /></el-icon>
        <p>企业微信登录中...</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import { handleOAuthCallback } from '../api'

const route = useRoute()
const router = useRouter()

onMounted(async () => {
  const teamId = Number(route.query.teamId || route.query.state || 1)
  const code = route.query.code

  // 如果没有code，跳转到授权页面
  if (!code) {
    const currentUrl = window.location.origin + window.location.pathname + window.location.search
    window.location.href = `/api/auth/authorize?teamId=${teamId}&redirect=${encodeURIComponent(currentUrl)}`
    return
  }

  try {
    const res = await handleOAuthCallback(code, teamId)
    
    if (res.code === 200 && res.data) {
      const login = res.data
      
      // 存储token
      sessionStorage.setItem('token', login.token)
      
      // 存储登录信息
      sessionStorage.setItem('wework_login', JSON.stringify({
        teamId: login.teamId || teamId,
        weWorkUserId: login.weWorkUserId,
        groupChatId: login.groupChatId,
        memberName: login.memberName,
        inGroup: login.inGroup
      }))
      
      ElMessage.success('登录成功')
      
      // 跳转到对话页面
      router.push('/chat')
    } else {
      ElMessage.error(res.msg || '登录失败')
      router.push('/')
    }
  } catch (e) {
    console.error('企业微信登录失败', e)
    ElMessage.error('企业微信登录失败: ' + (e.message || '未知错误'))
    router.push('/')
  }
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
