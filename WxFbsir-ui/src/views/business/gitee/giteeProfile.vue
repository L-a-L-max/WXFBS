<template>
  <div class="gitee-profile">
    <el-alert
      title="Gitee授权状态"
      type="info"
      :closable="false"
      class="gitee-alert"
    >
      <template #default>
        <div class="gitee-status">
          <el-tag :type="authorized ? 'success' : 'warning'" effect="plain">
            {{ authorized ? "已授权" : "未授权" }}
          </el-tag>
          <span class="gitee-status-text">
            {{ authorized ? "已获取 Gitee 授权，可直接拉取资料数据。" : "尚未授权，请点击授权按钮完成绑定。" }}
          </span>
          <el-button
            type="primary"
            :loading="authorizing"
            @click="handleAuthorize"
          >
            {{ authorized ? "重新授权" : "使用 Gitee 授权" }}
          </el-button>
          <el-button
            type="success"
            :disabled="!authorized"
            :loading="loadingAll"
            @click="handleRefreshAll"
          >
            一键刷新
          </el-button>
        </div>
      </template>
    </el-alert>

    <el-card class="gitee-card">
      <template #header>
        <div class="gitee-card-header">
          <span>授权用户资料</span>
          <div class="gitee-card-actions">
            <el-button
              size="small"
              :disabled="!authorized"
              :loading="loadingProfile"
              @click="fetchProfile"
            >
              刷新资料
            </el-button>
            <el-button size="small" :disabled="!profile?.html_url" @click="openProfile">
              打开主页
            </el-button>
            <el-button size="small" :disabled="!profile?.html_url" @click="copyProfileUrl">
              复制主页链接
            </el-button>
          </div>
        </div>
      </template>
      <div v-if="profile" class="gitee-profile-content">
        <el-avatar :size="72" :src="profile.avatar_url" />
        <div class="gitee-profile-meta">
          <div class="gitee-profile-name">
            <span class="gitee-profile-title">{{ profile.name || profile.login }}</span>
            <el-tag size="small" effect="plain">{{ profile.login }}</el-tag>
            <el-tag v-if="profile.type" size="small" type="info" effect="plain">{{ profile.type }}</el-tag>
          </div>
          <div class="gitee-profile-desc">
            {{ profile.bio || "暂无简介" }}
          </div>
          <div class="gitee-profile-links">
            <el-link v-if="profile.blog" :href="profile.blog" target="_blank">博客</el-link>
            <el-link v-if="profile.weibo" :href="profile.weibo" target="_blank">微博</el-link>
            <el-link v-if="profile.html_url" :href="profile.html_url" target="_blank">Gitee主页</el-link>
          </div>
        </div>
      </div>
      <el-descriptions v-if="profile" :column="3" border>
        <el-descriptions-item label="邮箱">{{ profile.email || "—" }}</el-descriptions-item>
        <el-descriptions-item label="Followers">{{ profile.followers ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="Following">{{ profile.following ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="公开仓库">{{ profile.public_repos ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="公开 Gists">{{ profile.public_gists ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="Star 数">{{ profile.stared ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="Watching">{{ profile.watched ?? 0 }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTime(profile.created_at) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatTime(profile.updated_at) }}</el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="暂无资料，请先刷新获取" />
    </el-card>

    <el-card class="gitee-card">
      <template #header>
        <div class="gitee-card-header">
          <span>授权用户仓库</span>
          <div class="gitee-card-actions">
            <el-input
              v-model="repoKeyword"
              placeholder="搜索仓库"
              size="small"
              clearable
              class="gitee-search-input"
            />
            <el-button
              size="small"
              :disabled="!authorized"
              :loading="loadingRepos"
              @click="fetchRepos"
            >
              刷新仓库
            </el-button>
          </div>
        </div>
      </template>
      <el-table v-if="filteredRepos.length" :data="filteredRepos" stripe>
        <el-table-column label="仓库" min-width="220">
          <template #default="{ row }">
            <el-link :href="row.html_url" target="_blank">{{ row.full_name || row.name }}</el-link>
            <div class="gitee-muted">{{ row.description || "暂无描述" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="语言" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.language" size="small" effect="plain">{{ row.language }}</el-tag>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column label="Star" width="90" prop="stargazers_count" />
        <el-table-column label="Fork" width="90" prop="forks_count" />
        <el-table-column label="Watch" width="90" prop="watchers_count" />
        <el-table-column label="更新时间" width="180">
          <template #default="{ row }">{{ formatTime(row.updated_at) }}</template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无仓库数据" />
    </el-card>

    <el-card class="gitee-card">
      <template #header>
        <div class="gitee-card-header">
          <span>授权用户 Issues</span>
          <div class="gitee-card-actions">
            <el-input
              v-model="issueKeyword"
              placeholder="搜索 Issue"
              size="small"
              clearable
              class="gitee-search-input"
            />
            <el-button
              size="small"
              :disabled="!authorized"
              :loading="loadingIssues"
              @click="fetchIssues"
            >
              刷新 Issues
            </el-button>
          </div>
        </div>
      </template>
      <el-table v-if="filteredIssues.length" :data="filteredIssues" stripe>
        <el-table-column label="标题" min-width="240">
          <template #default="{ row }">
            <el-link :href="row.html_url" target="_blank">{{ row.title }}</el-link>
            <div class="gitee-muted">{{ row.repository?.full_name || "—" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag size="small" effect="plain">
              {{ row.issue_state_detail?.title || row.state || "未知" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="180">
          <template #default="{ row }">{{ formatTime(row.updated_at) }}</template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="{ row }">{{ formatTime(row.created_at) }}</template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无 Issue 数据" />
    </el-card>

    <el-card class="gitee-card">
      <template #header>
        <div class="gitee-card-header">
          <span>授权用户通知</span>
          <div class="gitee-card-actions">
            <el-input
              v-model="noticeKeyword"
              placeholder="搜索通知"
              size="small"
              clearable
              class="gitee-search-input"
            />
            <el-button
              size="small"
              :disabled="!authorized"
              :loading="loadingNotices"
              @click="fetchNotifications"
            >
              刷新通知
            </el-button>
          </div>
        </div>
      </template>
      <el-table v-if="filteredNotifications.length" :data="filteredNotifications" stripe>
        <el-table-column label="内容" min-width="320">
          <template #default="{ row }">
            <el-link :href="row.html_url" target="_blank">{{ row.content }}</el-link>
            <div class="gitee-muted">{{ row.repository?.full_name || "—" }}</div>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="140">
          <template #default="{ row }">{{ row.type || "—" }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.unread ? 'danger' : 'success'" size="small" effect="plain">
              {{ row.unread ? "未读" : "已读" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="触发人" width="120">
          <template #default="{ row }">{{ row.actor?.name || row.actor?.login || "—" }}</template>
        </el-table-column>
        <el-table-column label="更新时间" width="180">
          <template #default="{ row }">{{ formatTime(row.updated_at) }}</template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无通知数据" />
    </el-card>
  </div>
</template>

<script setup name="GiteeProfile">
import { ElMessage } from "element-plus"
import { parseTime } from "@/utils/WxFbsir"
import {
  getGiteeStatus,
  getGiteeAuthorizeUrl,
  fetchGiteeProfile,
  fetchGiteeRepos,
  fetchGiteeIssues,
  fetchGiteeNotifications
} from "@/api/business/gitee/profile"

const redirectPath = "/user/profile/gitee"
const profile = ref(null)
const repos = ref([])
const issues = ref([])
const notifications = ref([])

const authorized = ref(false)
const authorizing = ref(false)

const repoKeyword = ref("")
const issueKeyword = ref("")
const noticeKeyword = ref("")

const loadingProfile = ref(false)
const loadingRepos = ref(false)
const loadingIssues = ref(false)
const loadingNotices = ref(false)
const loadingAll = ref(false)

const filteredRepos = computed(() => {
  const keyword = repoKeyword.value.trim().toLowerCase()
  if (!keyword) {
    return repos.value
  }
  return repos.value.filter(repo => {
    return (repo.full_name || repo.name || "").toLowerCase().includes(keyword)
  })
})

const filteredIssues = computed(() => {
  const keyword = issueKeyword.value.trim().toLowerCase()
  if (!keyword) {
    return issues.value
  }
  return issues.value.filter(issue => {
    const title = issue.title || ""
    const repo = issue.repository?.full_name || ""
    return `${title} ${repo}`.toLowerCase().includes(keyword)
  })
})

const filteredNotifications = computed(() => {
  const keyword = noticeKeyword.value.trim().toLowerCase()
  if (!keyword) {
    return notifications.value
  }
  return notifications.value.filter(notice => {
    const content = notice.content || ""
    const repo = notice.repository?.full_name || ""
    return `${content} ${repo}`.toLowerCase().includes(keyword)
  })
})

function formatTime(value) {
  const parsed = parseTime(value)
  return parsed || "—"
}

function handleError(error, fallback) {
  const message = error?.response?.data?.message || error?.message || fallback
  ElMessage.error(message)
}

function clearData() {
  profile.value = null
  repos.value = []
  issues.value = []
  notifications.value = []
}

function handleAuthorize() {
  authorizing.value = true
  getGiteeAuthorizeUrl({ redirect: redirectPath }).then(res => {
    const authorizeUrl = res.data?.url
    if (!authorizeUrl) {
      throw new Error("授权地址获取失败")
    }
    window.location.href = authorizeUrl
  }).catch(error => {
    handleError(error, "获取授权地址失败")
    authorizing.value = false
  })
}

function openProfile() {
  if (profile.value?.html_url) {
    window.open(profile.value.html_url, "_blank")
  }
}

function copyProfileUrl() {
  if (!profile.value?.html_url) {
    return
  }
  navigator.clipboard?.writeText(profile.value.html_url).then(() => {
    ElMessage.success("链接已复制")
  }).catch(() => {
    ElMessage.error("复制失败，请手动复制")
  })
}

async function fetchProfile() {
  if (!authorized.value) {
    return
  }
  loadingProfile.value = true
  try {
    const res = await fetchGiteeProfile()
    profile.value = res.data || null
  } catch (error) {
    handleError(error, "获取用户资料失败")
  } finally {
    loadingProfile.value = false
  }
}

async function fetchRepos() {
  if (!authorized.value) {
    return
  }
  loadingRepos.value = true
  try {
    const res = await fetchGiteeRepos({ sort: "updated", per_page: 50 })
    const data = res.data
    repos.value = Array.isArray(data) ? data : []
  } catch (error) {
    handleError(error, "获取仓库失败")
  } finally {
    loadingRepos.value = false
  }
}

async function fetchIssues() {
  if (!authorized.value) {
    return
  }
  loadingIssues.value = true
  try {
    const res = await fetchGiteeIssues({ state: "all", per_page: 50 })
    const data = res.data
    issues.value = Array.isArray(data) ? data : []
  } catch (error) {
    handleError(error, "获取 Issue 失败")
  } finally {
    loadingIssues.value = false
  }
}

async function fetchNotifications() {
  if (!authorized.value) {
    return
  }
  loadingNotices.value = true
  try {
    const res = await fetchGiteeNotifications({ per_page: 50 })
    const data = res.data
    if (data && Array.isArray(data.list)) {
      notifications.value = data.list
    } else if (Array.isArray(data)) {
      notifications.value = data
    } else {
      notifications.value = []
    }
  } catch (error) {
    handleError(error, "获取通知失败")
  } finally {
    loadingNotices.value = false
  }
}

async function handleRefreshAll() {
  if (!authorized.value) {
    return
  }
  loadingAll.value = true
  try {
    await Promise.all([
      fetchProfile(),
      fetchRepos(),
      fetchIssues(),
      fetchNotifications()
    ])
  } finally {
    loadingAll.value = false
  }
}

async function loadStatus() {
  try {
    const res = await getGiteeStatus()
    authorized.value = Boolean(res.data?.authorized)
    if (!authorized.value) {
      clearData()
    }
  } catch (error) {
    handleError(error, "获取授权状态失败")
  } finally {
    authorizing.value = false
  }
}

onMounted(async () => {
  const route = useRoute()
  const router = useRouter()
  const giteeError = route.query?.giteeError
  if (giteeError) {
    ElMessage.error(decodeURIComponent(String(giteeError)))
  }
  if (route.query?.giteeAuth) {
    ElMessage.success("Gitee 授权成功")
  }
  if (giteeError || route.query?.giteeAuth) {
    const cleanQuery = { ...route.query }
    delete cleanQuery.giteeError
    delete cleanQuery.giteeAuth
    router.replace({ path: route.path, query: cleanQuery })
  }
  await loadStatus()
  if (authorized.value) {
    handleRefreshAll()
  }
})
</script>

<style scoped>
.gitee-profile {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.gitee-alert {
  margin-bottom: 6px;
}

.gitee-status {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.gitee-status-text {
  color: #606266;
}

.gitee-card {
  border-radius: 6px;
}

.gitee-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.gitee-card-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.gitee-profile-content {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 16px;
}

.gitee-profile-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.gitee-profile-title {
  font-size: 18px;
  font-weight: 600;
  margin-right: 8px;
}

.gitee-profile-name {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.gitee-profile-desc {
  color: #606266;
}

.gitee-profile-links {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.gitee-search-input {
  width: 200px;
}

.gitee-muted {
  color: #909399;
  font-size: 12px;
  margin-top: 4px;
}
</style>
