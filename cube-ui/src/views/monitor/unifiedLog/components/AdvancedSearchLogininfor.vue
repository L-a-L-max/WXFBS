<template>
  <div class="advanced-search-logininfor">
    <el-form :model="searchForm" ref="searchForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="用户名称" prop="userName">
            <el-input v-model="searchForm.userName" placeholder="请输入用户名称" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="登录IP地址" prop="ipaddr">
            <el-input v-model="searchForm.ipaddr" placeholder="请输入登录IP地址" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="登录状态" prop="status">
            <el-select v-model="searchForm.status" placeholder="请选择登录状态" clearable style="width: 100%;">
              <el-option label="成功" value="0" />
              <el-option label="失败" value="1" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="登录地点" prop="loginLocation">
            <el-input v-model="searchForm.loginLocation" placeholder="请输入登录地点" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="浏览器类型" prop="browser">
            <el-select v-model="searchForm.browser" placeholder="请选择浏览器" clearable style="width: 100%;">
              <el-option label="Chrome" value="Chrome" />
              <el-option label="Firefox" value="Firefox" />
              <el-option label="Safari" value="Safari" />
              <el-option label="Edge" value="Edge" />
              <el-option label="IE" value="IE" />
              <el-option label="Opera" value="Opera" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="操作系统" prop="os">
            <el-select v-model="searchForm.os" placeholder="请选择操作系统" clearable style="width: 100%;">
              <el-option label="Windows" value="Windows" />
              <el-option label="Mac OS" value="Mac OS" />
              <el-option label="Linux" value="Linux" />
              <el-option label="Android" value="Android" />
              <el-option label="iOS" value="iOS" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="16">
          <el-form-item label="登录时间范围" prop="loginTimeRange">
            <el-date-picker
              v-model="searchForm.loginTimeRange"
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
          <el-form-item label="提示信息" prop="msg">
            <el-input v-model="searchForm.msg" placeholder="请输入提示信息" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="IP地址段筛选" prop="ipRange">
            <el-input v-model="searchForm.ipRange" placeholder="例如: 192.168.1.* 或 192.168.*.*" clearable>
              <template #prepend>
                <el-select v-model="searchForm.ipRangeType" style="width: 120px;">
                  <el-option label="包含" value="include" />
                  <el-option label="排除" value="exclude" />
                </el-select>
              </template>
            </el-input>
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
  name: "AdvancedSearchLogininfor",
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
        userName: '',
        ipaddr: '',
        status: '',
        loginLocation: '',
        browser: '',
        os: '',
        loginTimeRange: [],
        msg: '',
        ipRange: '',
        ipRangeType: 'include'
      }
    };
  },
  methods: {
    handleSearch() {
      const params = { ...this.searchForm };
      // 处理时间范围
      if (params.loginTimeRange && params.loginTimeRange.length === 2) {
        params.beginTime = params.loginTimeRange[0];
        params.endTime = params.loginTimeRange[1];
      }
      delete params.loginTimeRange;
      
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
.advanced-search-logininfor {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>

