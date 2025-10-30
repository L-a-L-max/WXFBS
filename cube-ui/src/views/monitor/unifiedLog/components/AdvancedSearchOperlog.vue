<template>
  <div class="advanced-search-operlog">
    <el-form :model="searchForm" ref="searchForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="操作模块" prop="title">
            <el-input v-model="searchForm.title" placeholder="请输入系统模块" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="操作人员" prop="operName">
            <el-input v-model="searchForm.operName" placeholder="请输入操作人员" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="操作地址" prop="operIp">
            <el-input v-model="searchForm.operIp" placeholder="请输入操作IP地址" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="操作类型" prop="businessType">
            <el-select v-model="searchForm.businessType" placeholder="请选择操作类型" clearable style="width: 100%;">
              <el-option label="其他" value="0" />
              <el-option label="新增" value="1" />
              <el-option label="修改" value="2" />
              <el-option label="删除" value="3" />
              <el-option label="授权" value="4" />
              <el-option label="导出" value="5" />
              <el-option label="导入" value="6" />
              <el-option label="强退" value="7" />
              <el-option label="生成代码" value="8" />
              <el-option label="清空数据" value="9" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="操作状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 100%;">
              <el-option label="成功" value="0" />
              <el-option label="失败" value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="请求方式" prop="requestMethod">
            <el-select v-model="searchForm.requestMethod" placeholder="请选择请求方式" clearable style="width: 100%;">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="操作时间范围" prop="operTimeRange">
            <el-date-picker
              v-model="searchForm.operTimeRange"
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
        <el-col :span="6">
          <el-form-item label="消耗时间(ms)" prop="costTimeMin">
            <el-input v-model.number="searchForm.costTimeMin" placeholder="最小值" type="number" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="至" prop="costTimeMax" label-width="30px">
            <el-input v-model.number="searchForm.costTimeMax" placeholder="最大值" type="number" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="操作地点" prop="operLocation">
            <el-input v-model="searchForm.operLocation" placeholder="请输入操作地点" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="请求URL" prop="operUrl">
            <el-input v-model="searchForm.operUrl" placeholder="请输入请求URL" clearable />
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
  name: "AdvancedSearchOperlog",
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
        title: '',
        operName: '',
        operIp: '',
        businessType: '',
        status: '',
        requestMethod: '',
        operTimeRange: [],
        costTimeMin: null,
        costTimeMax: null,
        operLocation: '',
        operUrl: ''
      }
    };
  },
  methods: {
    handleSearch() {
      const params = { ...this.searchForm };
      // 处理时间范围
      if (params.operTimeRange && params.operTimeRange.length === 2) {
        params.beginTime = params.operTimeRange[0];
        params.endTime = params.operTimeRange[1];
      }
      delete params.operTimeRange;
      
      this.$emit('search', params);
    },
    handleReset() {
      this.$refs.searchForm.resetFields();
      this.$emit('reset');
    }
  }
};
</script>

<style scoped>
.advanced-search-operlog {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>

