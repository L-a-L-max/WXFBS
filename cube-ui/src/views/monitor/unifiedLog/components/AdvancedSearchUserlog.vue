<template>
  <div class="advanced-search-userlog">
    <el-form :model="searchForm" ref="searchForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="用户ID" prop="userId">
            <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="方法名称" prop="methodName">
            <el-input v-model="searchForm.methodName" placeholder="请输入方法名称" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="执行状态" prop="logStatus">
            <el-select v-model="searchForm.logStatus" placeholder="请选择执行状态" clearable style="width: 100%;">
              <el-option label="成功" value="success" />
              <el-option label="失败" value="fail" />
              <el-option label="警告" value="warning" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="16">
          <el-form-item label="执行时间范围" prop="executionTimeRange">
            <el-date-picker
              v-model="searchForm.executionTimeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%;"
              :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="方法描述" prop="description">
            <el-input v-model="searchForm.description" placeholder="请输入方法描述关键词" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="执行时长(ms)" prop="executionTimeMillisMin">
            <el-input v-model.number="searchForm.executionTimeMillisMin" placeholder="最小值" type="number" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="至" prop="executionTimeMillisMax" label-width="30px">
            <el-input v-model.number="searchForm.executionTimeMillisMax" placeholder="最大值" type="number" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行结果关键词" prop="executionResult">
            <el-input v-model="searchForm.executionResult" placeholder="请输入执行结果关键词" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="方法参数关键词" prop="methodParams">
            <el-input 
              v-model="searchForm.methodParams" 
              placeholder="请输入方法参数关键词（支持JSON字段搜索）" 
              clearable 
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="快速筛选" prop="quickFilter">
            <el-radio-group v-model="searchForm.quickFilter" @change="handleQuickFilterChange">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="slow">慢查询(>3000ms)</el-radio-button>
              <el-radio-button label="failed">失败记录</el-radio-button>
              <el-radio-button label="warning">警告信息</el-radio-button>
              <el-radio-button label="today">今日记录</el-radio-button>
              <el-radio-button label="recent">最近1小时</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row>
        <el-col :span="24" style="text-align: right;">
          <el-button type="primary" :icon="Search" @click="handleSearch">高级搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { Search, Refresh } from '@element-plus/icons-vue';

export default {
  name: "AdvancedSearchUserlog",
  components: {
    Search,
    Refresh
  },
  setup() {
    return {
      Search,
      Refresh
    };
  },
  data() {
    return {
      searchForm: {
        userId: '',
        methodName: '',
        logStatus: '',
        executionTimeRange: [],
        description: '',
        executionTimeMillisMin: null,
        executionTimeMillisMax: null,
        executionResult: '',
        methodParams: '',
        quickFilter: ''
      }
    };
  },
  methods: {
    handleSearch() {
      const params = { ...this.searchForm };
      
      // 处理日志状态（区分成功、失败、警告）
      if (params.logStatus) {
        switch (params.logStatus) {
          case 'success':
            params.isSuccess = '1';
            params.isWarning = false;
            break;
          case 'fail':
            params.isSuccess = '0';
            params.isWarning = false;
            break;
          case 'warning':
            params.isSuccess = '1'; // 警告也算执行成功
            params.isWarning = true;
            break;
        }
      }
      delete params.logStatus;
      
      // 处理时间范围 - 注意这里需要使用 params 对象传递
      if (params.executionTimeRange && params.executionTimeRange.length === 2) {
        params['params.beginExecutionTime'] = params.executionTimeRange[0];
        params['params.endExecutionTime'] = params.executionTimeRange[1];
      }
      delete params.executionTimeRange;
      
      // 处理执行时长
      if (params.executionTimeMillisMin !== null && params.executionTimeMillisMin !== '') {
        params['params.executionTimeMillisMin'] = params.executionTimeMillisMin;
      }
      delete params.executionTimeMillisMin;
      
      if (params.executionTimeMillisMax !== null && params.executionTimeMillisMax !== '') {
        params['params.executionTimeMillisMax'] = params.executionTimeMillisMax;
      }
      delete params.executionTimeMillisMax;
      
      // 处理警告标志
      if (params.isWarning) {
        params['params.isWarning'] = params.isWarning;
      }
      delete params.isWarning;
      
      // 处理快速筛选
      if (params.quickFilter) {
        const now = new Date();
        switch (params.quickFilter) {
          case 'slow':
            params['params.executionTimeMillisMin'] = 3000;
            break;
          case 'failed':
            params.isSuccess = '0';
            break;
          case 'warning':
            params.isSuccess = '1';
            params['params.isWarning'] = true;
            break;
          case 'today':
            const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            params['params.beginExecutionTime'] = this.formatDateTime(todayStart);
            params['params.endExecutionTime'] = this.formatDateTime(now);
            break;
          case 'recent':
            const recentTime = new Date(now.getTime() - 60 * 60 * 1000);
            params['params.beginExecutionTime'] = this.formatDateTime(recentTime);
            params['params.endExecutionTime'] = this.formatDateTime(now);
            break;
        }
      }
      delete params.quickFilter;
      
      this.$emit('search', params);
    },
    handleQuickFilterChange() {
      // 快速筛选变化时，清除其他筛选条件
      this.searchForm.logStatus = '';
      this.searchForm.executionTimeRange = [];
      this.searchForm.executionTimeMillisMin = null;
    },
    handleReset() {
      this.$refs.searchForm.resetFields();
      this.$emit('reset');
    },
    formatDateTime(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
  }
};
</script>

<style scoped>
.advanced-search-userlog {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

::v-deep .el-radio-button__inner {
  padding: 8px 15px;
}
</style>

