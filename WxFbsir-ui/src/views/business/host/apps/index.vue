<template>
  <div class="host-apps-container">
    <el-card class="box-card header-card">
      <template #header>
        <div class="card-header">
          <span>主机应用 - Playwright 示例中心</span>
          <el-tag type="info" size="small">共 {{ apps.length }} 个示例</el-tag>
        </div>
      </template>
      <div class="description">
        <p>本页面展示了福帮手 Playwright 框架的核心功能示例，涵盖元器平台的登录、工作流导航、节点编辑、调试和发布等操作。</p>
        <p>点击"前往操作台"按钮，可跳转到 WebSocket 调试页面，并自动带入该示例的默认配置。</p>
      </div>
    </el-card>

    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :md="8" :lg="8" v-for="app in apps" :key="app.id">
        <el-card class="app-card" shadow="hover">
          <div class="app-icon" :style="{ backgroundColor: app.iconBg }">
            <el-icon :size="32" :color="app.iconColor">
              <component :is="app.icon" />
            </el-icon>
          </div>
          <h3 class="app-title">{{ app.name }}</h3>
          <p class="app-desc">{{ app.description }}</p>
          <div class="app-tags">
            <el-tag 
              v-for="tag in app.tags" 
              :key="tag" 
              size="small" 
              :type="getTagType(tag)"
              effect="plain"
            >
              {{ tag }}
            </el-tag>
          </div>
          <div class="app-meta">
            <span class="meta-item">
              <el-icon><Timer /></el-icon>
              超时: {{ app.timeout }}s
            </span>
            <span class="meta-item">
              <el-icon><Connection /></el-icon>
              {{ app.messageType }}
            </span>
          </div>
          <el-button type="primary" @click="goToConsole(app)" class="action-btn">
            <el-icon><Promotion /></el-icon>
            前往操作台
          </el-button>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="box-card usage-card">
      <template #header>
        <div class="card-header">
          <span>使用说明</span>
        </div>
      </template>
      <el-collapse>
        <el-collapse-item title="1. 如何使用这些示例？" name="1">
          <p>点击任意示例卡片的"前往操作台"按钮，系统会自动跳转到 WebSocket 调试页面，并预填充该示例的默认配置。您只需点击"发送消息"按钮即可执行操作。</p>
        </el-collapse-item>
        <el-collapse-item title="2. 元器登录流程" name="2">
          <p>首次使用元器相关功能前，需要先完成登录：</p>
          <ol>
            <li>使用"元器登录检测"检查当前登录状态</li>
            <li>如果未登录，使用"元器扫码登录"获取二维码</li>
            <li>使用微信扫描二维码完成登录</li>
            <li>登录成功后，即可使用其他元器功能</li>
          </ol>
        </el-collapse-item>
        <el-collapse-item title="3. 节点编辑功能说明" name="3">
          <p>节点编辑功能支持以下参数配置：</p>
          <ul>
            <li><strong>modelName</strong>: 大模型名称（如 hunyuan-2.0-instruct-20251111）</li>
            <li><strong>temperature</strong>: 温度参数（0-1，控制输出随机性）</li>
            <li><strong>topP</strong>: Top-P 采样参数（0-1）</li>
            <li><strong>maxTokens</strong>: 最大输出 Token 数</li>
            <li><strong>prompt</strong>: 提示词模板</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="4. 常见问题" name="4">
          <p><strong>Q: 为什么操作失败？</strong></p>
          <p>A: 请检查是否已登录元器平台，以及网络连接是否正常。</p>
          <p><strong>Q: 如何查看操作结果？</strong></p>
          <p>A: 操作完成后，系统会返回截图和结果信息，可在消息输出区域查看。</p>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { 
  User, 
  Cellphone, 
  Guide, 
  Edit, 
  VideoPlay, 
  Upload,
  Timer,
  Connection,
  Promotion
} from '@element-plus/icons-vue';

export default {
  name: 'HostApps',
  components: {
    User,
    Cellphone,
    Guide,
    Edit,
    VideoPlay,
    Upload,
    Timer,
    Connection,
    Promotion
  },
  setup() {
    const router = useRouter();

    const apps = ref([
      {
        id: 'yuanqi-check-login',
        name: '元器登录检测',
        description: '检查用户是否已登录腾讯元器平台，返回登录状态和用户信息。',
        icon: 'User',
        iconBg: '#e6f7ff',
        iconColor: '#1890ff',
        tags: ['元器', '登录', '快速'],
        messageType: 'YUANQI_CHECK_LOGIN',
        timeout: 30,
        defaultPayload: {
          type: 'YUANQI_CHECK_LOGIN',
          engineId: 'engine-001',
          payload: {}
        }
      },
      {
        id: 'yuanqi-scan-login',
        name: '元器扫码登录',
        description: '获取元器登录二维码，实时监测登录状态，支持微信扫码登录。',
        icon: 'Cellphone',
        iconBg: '#f6ffed',
        iconColor: '#52c41a',
        tags: ['元器', '登录', '二维码'],
        messageType: 'YUANQI_SCAN_LOGIN',
        timeout: 300,
        defaultPayload: {
          type: 'YUANQI_SCAN_LOGIN',
          engineId: 'engine-001',
          payload: {}
        }
      },
      {
        id: 'yuanqi-navigate-workflow',
        name: '工作流导航',
        description: '导航到指定的元器工作流编辑页面，支持空间、智能体、工作流三级定位。',
        icon: 'Guide',
        iconBg: '#fff7e6',
        iconColor: '#fa8c16',
        tags: ['元器', '工作流', '导航'],
        messageType: 'YUANQI_NAVIGATE_WORKFLOW',
        timeout: 60,
        defaultPayload: {
          type: 'YUANQI_NAVIGATE_WORKFLOW',
          engineId: 'engine-001',
          payload: {
            spaceName: '福帮手开源',
            agentName: '123',
            workflowName: '分析助手-高优先级-多模型'
          }
        }
      },
      {
        id: 'yuanqi-edit-node',
        name: '节点编辑',
        description: '编辑工作流中的大模型节点，支持配置模型参数（Temperature、TopP、MaxTokens）和提示词。',
        icon: 'Edit',
        iconBg: '#f9f0ff',
        iconColor: '#722ed1',
        tags: ['元器', '节点', '编辑'],
        messageType: 'YUANQI_EDIT_NODE',
        timeout: 120,
        defaultPayload: {
          type: 'YUANQI_EDIT_NODE',
          engineId: 'engine-001',
          payload: {
            spaceName: '福帮手开源',
            agentName: '123',
            workflowName: '分析助手-高优先级-多模型',
            nodeName: '精调大模型',
            config: {
              modelName: 'hunyuan-2.0-instruct-20251111',
              temperature: 0.7,
              topP: 0.6,
              maxTokens: 4000,
              prompt: '请根据用户输入生成高质量的内容...'
            }
          }
        }
      },
      {
        id: 'yuanqi-debug-workflow',
        name: '工作流调试',
        description: '调试元器工作流，支持填入测试参数，执行调试并返回结果截图。',
        icon: 'VideoPlay',
        iconBg: '#fff1f0',
        iconColor: '#f5222d',
        tags: ['元器', '工作流', '调试'],
        messageType: 'YUANQI_DEBUG_WORKFLOW',
        timeout: 180,
        defaultPayload: {
          type: 'YUANQI_DEBUG_WORKFLOW',
          engineId: 'engine-001',
          payload: {
            spaceName: '福帮手开源',
            agentName: '123',
            workflowName: '分析助手-高优先级-多模型',
            debugInput: {
              article_topic: '人工智能发展趋势'
            }
          }
        }
      },
      {
        id: 'yuanqi-publish-workflow',
        name: '工作流发布',
        description: '发布元器工作流到生产环境，支持一键发布并返回发布状态。',
        icon: 'Upload',
        iconBg: '#e6fffb',
        iconColor: '#13c2c2',
        tags: ['元器', '工作流', '发布'],
        messageType: 'YUANQI_PUBLISH_WORKFLOW',
        timeout: 60,
        defaultPayload: {
          type: 'YUANQI_PUBLISH_WORKFLOW',
          engineId: 'engine-001',
          payload: {
            spaceName: '福帮手开源',
            agentName: '123',
            workflowName: '分析助手-高优先级-多模型'
          }
        }
      }
    ]);

    const getTagType = (tag) => {
      const tagTypes = {
        '元器': '',
        '登录': 'success',
        '快速': 'info',
        '二维码': 'warning',
        '工作流': 'danger',
        '导航': 'info',
        '节点': 'warning',
        '编辑': 'success',
        '调试': 'danger',
        '发布': 'success'
      };
      return tagTypes[tag] || 'info';
    };

    const goToConsole = (app) => {
      const payloadStr = encodeURIComponent(JSON.stringify(app.defaultPayload, null, 2));
      router.push({
        path: '/business/debug',
        query: {
          type: app.messageType,
          payload: payloadStr
        }
      });
    };

    return {
      apps,
      getTagType,
      goToConsole,
      Timer,
      Connection,
      Promotion
    };
  }
};
</script>

<style scoped lang="scss">
.host-apps-container {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .description {
    color: #666;
    font-size: 14px;
    line-height: 1.8;
    
    p {
      margin: 0 0 8px 0;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.app-card {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }
  
  .app-icon {
    width: 64px;
    height: 64px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16px;
  }
  
  .app-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 8px 0;
  }
  
  .app-desc {
    font-size: 13px;
    color: #909399;
    line-height: 1.6;
    margin: 0 0 12px 0;
    min-height: 60px;
  }
  
  .app-tags {
    margin-bottom: 12px;
    
    .el-tag {
      margin: 2px 4px;
    }
  }
  
  .app-meta {
    display: flex;
    justify-content: center;
    gap: 16px;
    margin-bottom: 16px;
    font-size: 12px;
    color: #909399;
    
    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
  
  .action-btn {
    width: 100%;
  }
}

.usage-card {
  margin-top: 20px;
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  p, li {
    font-size: 14px;
    color: #666;
    line-height: 1.8;
  }
  
  ol, ul {
    padding-left: 20px;
    margin: 8px 0;
  }
  
  strong {
    color: #303133;
  }
}
</style>
