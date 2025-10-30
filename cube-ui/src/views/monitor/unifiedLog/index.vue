<template>
  <div class="app-container unified-log-container">
    <!-- 统计信息卡片 - 放在最上面 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <el-card class="stats-card" @click="handleCardClick('operlog')">
          <div class="stats-item">
            <i class="el-icon-document stats-icon" style="color: #409EFF;"></i>
            <div class="stats-content">
              <div class="stats-label">操作日志总数</div>
              <div class="stats-value">{{ statistics.operlogCount.toLocaleString() }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stats-card" @click="handleCardClick('logininfor')">
          <div class="stats-item">
            <i class="el-icon-user stats-icon" style="color: #67C23A;"></i>
            <div class="stats-content">
              <div class="stats-label">登录日志总数</div>
              <div class="stats-value">{{ statistics.logininforCount.toLocaleString() }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stats-card" @click="handleCardClick('userlog')">
          <div class="stats-item">
            <i class="el-icon-s-data stats-icon" style="color: #E6A23C;"></i>
            <div class="stats-content">
              <div class="stats-label">用户日志总数</div>
              <div class="stats-value">{{ statistics.userlogCount.toLocaleString() }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="box-card">
      <!-- 日志类型切换标签 -->
      <el-tabs v-model="activeLogType" type="border-card" @tab-click="handleTabChange">
        <el-tab-pane label="操作日志" name="operlog">
          <OperationLog ref="operlogRef" />
        </el-tab-pane>
        <el-tab-pane label="登录日志" name="logininfor">
          <LoginLog ref="logininforRef" />
        </el-tab-pane>
        <el-tab-pane label="节点日志" name="userlog">
          <UserLog ref="userlogRef" />
        </el-tab-pane>
      </el-tabs>

      <!-- 高级搜索面板 -->
      <el-collapse v-model="advancedSearchVisible" class="advanced-search-panel">
        <el-collapse-item title="高级搜索" name="1">
          <component 
            :is="currentAdvancedSearchComponent" 
            @search="handleAdvancedSearch"
            @reset="handleAdvancedReset"
          />
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
import OperationLog from './components/OperationLog.vue';
import LoginLog from './components/LoginLog.vue';
import UserLog from './components/UserLog.vue';
import AdvancedSearchOperlog from './components/AdvancedSearchOperlog.vue';
import AdvancedSearchLogininfor from './components/AdvancedSearchLogininfor.vue';
import AdvancedSearchUserlog from './components/AdvancedSearchUserlog.vue';
import { countTotalOperlog } from '@/api/monitor/operlog';
import { countTotalLogininfor } from '@/api/monitor/logininfor';
import { countTotalUserLog } from '@/api/monitor/userLog';

export default {
  name: "UnifiedLog",
  components: {
    OperationLog,
    LoginLog,
    UserLog,
    AdvancedSearchOperlog,
    AdvancedSearchLogininfor,
    AdvancedSearchUserlog
  },
  data() {
    return {
      // 当前激活的日志类型
      activeLogType: 'operlog',
      // 高级搜索面板可见性
      advancedSearchVisible: [],
      // 统计信息 - 不随筛选变化，始终显示总数
      statistics: {
        operlogCount: 0,
        logininforCount: 0,
        userlogCount: 0
      }
    };
  },
  computed: {
    // 根据当前标签返回对应的高级搜索组件
    currentAdvancedSearchComponent() {
      const componentMap = {
        'operlog': 'AdvancedSearchOperlog',
        'logininfor': 'AdvancedSearchLogininfor',
        'userlog': 'AdvancedSearchUserlog'
      };
      return componentMap[this.activeLogType];
    }
  },
  mounted() {
    this.loadStatistics();
  },
  methods: {
    // 标签切换事件
    handleTabChange(tab) {
      console.log('切换到:', tab.props.name);
      this.advancedSearchVisible = []; // 关闭高级搜索面板
    },
    
    // 点击统计卡片切换标签
    handleCardClick(logType) {
      this.activeLogType = logType;
    },
    
    // 高级搜索
    handleAdvancedSearch(params) {
      const refMap = {
        'operlog': 'operlogRef',
        'logininfor': 'logininforRef',
        'userlog': 'userlogRef'
      };
      const ref = this.$refs[refMap[this.activeLogType]];
      if (ref && ref.handleAdvancedSearch) {
        ref.handleAdvancedSearch(params);
      }
    },
    
    // 重置高级搜索
    handleAdvancedReset() {
      const refMap = {
        'operlog': 'operlogRef',
        'logininfor': 'logininforRef',
        'userlog': 'userlogRef'
      };
      const ref = this.$refs[refMap[this.activeLogType]];
      if (ref && ref.resetQuery) {
        ref.resetQuery();
      }
    },
    
    // 加载统计信息 - 直接调用后端统计接口，不随筛选变化
    async loadStatistics() {
      try {
        // 并行请求三个统计接口
        const [operlogRes, logininforRes, userlogRes] = await Promise.all([
          countTotalOperlog(),
          countTotalLogininfor(),
          countTotalUserLog()
        ]);
        
        this.statistics.operlogCount = operlogRes.data || 0;
        this.statistics.logininforCount = logininforRes.data || 0;
        this.statistics.userlogCount = userlogRes.data || 0;
      } catch (error) {
        console.error('加载统计信息失败:', error);
        this.$modal.msgError('加载统计信息失败');
      }
    }
  }
};
</script>

<style scoped>
.unified-log-container {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.advanced-search-panel {
  margin-top: 20px;
  border-top: 1px solid #EBEEF5;
  padding-top: 20px;
}

.stats-card {
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #ffffff 0%, #f5f7fa 100%);
}

.stats-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-4px);
}

.stats-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stats-icon {
  font-size: 56px;
  margin-right: 20px;
  opacity: 0.8;
}

.stats-content {
  flex: 1;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
  font-weight: 500;
}

.stats-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

/* 标签页样式优化 */
::v-deep .el-tabs--border-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: none;
}

::v-deep .el-tabs__header {
  background-color: #f5f7fa;
  border-bottom: 2px solid #e4e7ed;
}

::v-deep .el-tabs__item {
  font-size: 16px;
  padding: 0 30px;
  height: 50px;
  line-height: 50px;
  transition: all 0.3s ease;
}

::v-deep .el-tabs__item:hover {
  color: #409EFF;
}

::v-deep .el-tabs__item.is-active {
  font-weight: bold;
  color: #409EFF;
  background-color: #ffffff;
}

/* 折叠面板样式优化 */
::v-deep .el-collapse-item__header {
  font-size: 16px;
  font-weight: 500;
  color: #409EFF;
}

::v-deep .el-collapse-item__header:hover {
  background-color: #f5f7fa;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-icon {
    font-size: 48px;
  }
  
  .stats-value {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .stats-icon {
    font-size: 36px;
    margin-right: 15px;
  }
  
  .stats-value {
    font-size: 24px;
  }
  
  .stats-item {
    padding: 15px;
  }
  
  ::v-deep .el-tabs__item {
    font-size: 14px;
    padding: 0 15px;
    height: 40px;
    line-height: 40px;
  }
}

@media (max-width: 576px) {
  .stats-row {
    margin-left: 0;
    margin-right: 0;
  }
  
  .stats-row .el-col {
    padding-left: 5px;
    padding-right: 5px;
    margin-bottom: 10px;
  }
}
</style>
