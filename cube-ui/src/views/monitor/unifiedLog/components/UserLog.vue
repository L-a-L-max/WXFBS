<template>
  <div class="user-log">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px" class="search-form">
      <el-form-item label="用户 ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户 ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方法名称" prop="methodName">
        <el-input
          v-model="queryParams.methodName"
          placeholder="请输入方法名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行时间" class="date-range-item">
        <el-date-picker
          v-model="queryParams.params.beginExecutionTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetime"
          placeholder="开始时间"
        ></el-date-picker>
        <span style="margin: 0 5px;">-</span>
        <el-date-picker
          v-model="queryParams.params.endExecutionTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetime"
          placeholder="结束时间"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="执行时长" prop="executionTimeMillis">
        <el-input
          v-model="queryParams.executionTimeMillis"
          placeholder="请输入执行时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否成功" prop="isSuccess">
        <el-select v-model="queryParams.isSuccess" placeholder="请选择是否成功" clearable>
          <el-option label="成功" value="1" />
          <el-option label="失败" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="small" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:userLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          size="small"
          @click="handleDeleteByCondition"
          v-hasPermi="['monitor:userLog:remove']"
        >按条件删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          size="small"
          @click="handleClean"
          v-hasPermi="['monitor:userLog:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          :icon="Download"
          size="small"
          @click="handleExport"
          v-hasPermi="['monitor:userLog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table 
      v-loading="loading" 
      :data="userLogList" 
      @selection-change="handleSelectionChange"
      stripe
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center" fixed="left" />
      <el-table-column label="日志ID" align="center" prop="id" width="80" />
      <el-table-column label="用户 ID" align="center" prop="userId" width="100" />
      <el-table-column label="方法名称" align="center" prop="methodName" min-width="180" :show-overflow-tooltip="true" />
      <el-table-column label="描述" align="center" prop="description" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="方法参数" align="center" min-width="180">
        <template #default="scope">
          <div class="copy-cell" @click="showContentDialog(scope.row.methodParams, '方法参数')">
            <div class="text-content" :title="scope.row.methodParams">
              {{ scope.row.methodParams && scope.row.methodParams.length > 30 ? scope.row.methodParams.substring(0, 30) + '...' : scope.row.methodParams }}
            </div>
            <i class="el-icon-view copy-icon"></i>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="执行时间" align="center" prop="executionTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.executionTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行结果" align="center" min-width="180">
        <template #default="scope">
          <div class="copy-cell" @click="showContentDialog(scope.row.executionResult, '执行结果')">
            <div class="text-content" :title="scope.row.executionResult">
              {{ scope.row.executionResult && scope.row.executionResult.length > 30 ? scope.row.executionResult.substring(0, 30) + '...' : scope.row.executionResult }}
            </div>
            <i class="el-icon-view copy-icon"></i>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="执行时长" align="center" prop="executionTimeMillis" width="100" />
      <el-table-column label="是否成功" align="center" prop="isSuccess" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isSuccess === 1 ? 'success' : 'danger'">
            {{ scope.row.isSuccess === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100" fixed="right">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['monitor:userLog:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      @current-change="getList"
      @size-change="getList"
    />

    <!-- 内容展示弹窗 -->
    <el-dialog :title="dialogTitle" v-model="contentDialogVisible" width="60%" append-to-body>
      <pre class="content-display">{{ dialogContent }}</pre>
      <template #footer>
        <el-button type="primary" @click="copyDialogContent">复制内容</el-button>
        <el-button @click="contentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Delete, Download, Refresh, Search } from '@element-plus/icons-vue';
import { listUserLog, delUserLog, cleanUserLog, deleteUserLogByCondition } from "@/api/monitor/userLog";

export default {
  name: "UserLog",
  components: {
    Delete,
    Download,
    Refresh,
    Search
  },
  setup() {
    return {
      Delete,
      Download,
      Refresh,
      Search
    };
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 日志信息表格数据
      userLogList: [],
      // 内容展示弹窗可见性
      contentDialogVisible: false,
      // 内容展示弹窗标题
      dialogTitle: "",
      // 内容展示弹窗内容
      dialogContent: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        methodName: null,
        executionTime: null,
        executionTimeMillis: null,
        isSuccess: null,
        params: {
          beginExecutionTime: null,
          endExecutionTime: null
        }
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户日志列表 */
    getList() {
      this.loading = true;
      listUserLog(this.queryParams).then(response => {
        this.userLogList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.params.beginExecutionTime = null;
      this.queryParams.params.endExecutionTime = null;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除日志信息编号为"' + ids + '"的数据项？').then(function() {
        return delUserLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('monitor/userLog/export', {
        ...this.queryParams
      }, `userLog_${new Date().getTime()}.xlsx`);
    },
    /** 按条件删除 */
    handleDeleteByCondition() {
      // 检查是否有筛选条件
      const hasCondition = this.queryParams.userId || this.queryParams.methodName || 
                          this.queryParams.isSuccess || this.queryParams.executionTimeMillis ||
                          this.queryParams.params.beginExecutionTime || this.queryParams.params.endExecutionTime;
      
      if (!hasCondition) {
        this.$modal.msgWarning("请至少设置一个搜索条件后再进行按条件删除！");
        return;
      }
      
      const conditionDesc = [];
      if (this.queryParams.userId) conditionDesc.push(`用户ID: ${this.queryParams.userId}`);
      if (this.queryParams.methodName) conditionDesc.push(`方法名称: ${this.queryParams.methodName}`);
      if (this.queryParams.isSuccess) conditionDesc.push(`执行状态: ${this.queryParams.isSuccess === '1' ? '成功' : '失败'}`);
      if (this.queryParams.params.beginExecutionTime) conditionDesc.push(`开始时间: ${this.queryParams.params.beginExecutionTime}`);
      if (this.queryParams.params.endExecutionTime) conditionDesc.push(`结束时间: ${this.queryParams.params.endExecutionTime}`);
      
      this.$modal.confirm(`是否确认删除符合以下条件的所有日志？<br/>${conditionDesc.join('<br/>')}`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        return deleteUserLogByCondition(this.queryParams);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有用户日志数据项？').then(function() {
        return cleanUserLog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => {});
    },
    /** 显示内容弹窗 */
    showContentDialog(content, title) {
      if (!content || content.trim() === '') {
        this.$message({
          message: `${title}内容为空，无法显示`,
          type: 'warning',
          duration: 2000
        });
        return;
      }
      this.dialogTitle = title;
      this.dialogContent = content;
      this.contentDialogVisible = true;
    },
    /** 复制弹窗内容 */
    copyDialogContent() {
      this.copyToClipboard(this.dialogContent, this.dialogTitle);
    },
    /** 复制到剪贴板 */
    async copyToClipboard(text, fieldName) {
      if (!text || text.trim() === '') {
        this.$message({
          message: `${fieldName}内容为空，无法复制`,
          type: 'warning',
          duration: 2000
        });
        return;
      }
      try {
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(text);
          this.$message({
            message: `${fieldName}已成功复制到剪贴板`,
            type: 'success',
            duration: 2000,
            showClose: true
          });
        } else {
          this.fallbackCopyTextToClipboard(text, fieldName);
        }
      } catch (err) {
        console.error('复制失败:', err);
        this.fallbackCopyTextToClipboard(text, fieldName);
      }
    },
    /** 降级复制方法 */
    fallbackCopyTextToClipboard(text, fieldName) {
      const textArea = document.createElement('textarea');
      textArea.value = text;
      textArea.style.position = 'fixed';
      textArea.style.left = '-999999px';
      textArea.style.top = '-999999px';
      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();
      try {
        const successful = document.execCommand('copy');
        if (successful) {
          this.$message({
            message: `${fieldName}已成功复制到剪贴板`,
            type: 'success',
            duration: 2000,
            showClose: true
          });
        } else {
          throw new Error('execCommand failed');
        }
      } catch (err) {
        console.error('降级复制也失败:', err);
        this.$message({
          message: '复制失败，请手动选择并复制文本',
          type: 'error',
          duration: 3000,
          showClose: true
        });
      } finally {
        document.body.removeChild(textArea);
      }
    },
    /** 高级搜索 */
    handleAdvancedSearch(params) {
      // 重置查询参数
      this.queryParams = {
        pageNum: 1,
        pageSize: this.queryParams.pageSize,
        userId: null,
        methodName: null,
        executionTime: null,
        executionTimeMillis: null,
        isSuccess: null,
        params: {
          beginExecutionTime: null,
          endExecutionTime: null
        }
      };
      
      // 合并高级搜索参数
      Object.keys(params).forEach(key => {
        if (key.startsWith('params.')) {
          // 处理嵌套的 params 对象
          const paramKey = key.substring(7); // 移除 'params.' 前缀
          this.queryParams.params[paramKey] = params[key];
        } else {
          // 直接赋值顶层参数
          this.queryParams[key] = params[key];
        }
      });
      
      this.handleQuery();
    }
  }
};
</script>

<style scoped>
.user-log {
  padding: 10px;
}

/* 复制单元格样式 */
.copy-cell {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #fafafa;
  border: 1px solid transparent;
}

.copy-cell:hover {
  background-color: #f0f9ff;
  border-color: #409EFF;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);
}

.text-content {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #606266;
  font-size: 13px;
  line-height: 1.4;
}

.copy-cell:hover .text-content {
  color: #409EFF;
}

.copy-icon {
  margin-left: 8px;
  font-size: 14px;
  color: #C0C4CC;
  opacity: 0;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.copy-cell:hover .copy-icon {
  opacity: 1;
  color: #409EFF;
}

.copy-cell:active {
  transform: scale(0.98);
  background-color: #e6f7ff;
}

.content-display {
  background-color: #f8f8f8;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 15px;
  max-height: 400px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-all;
  font-family: Consolas, Monaco, 'Andale Mono', monospace;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
}

/* 搜索表单优化 */
.search-form {
  width: 100%;
}

.search-form .el-form-item {
  margin-right: 12px;
  margin-bottom: 12px;
}

.search-form .el-input,
.search-form .el-select {
  width: 200px;
}

.search-form .date-range-item .el-date-editor {
  width: 180px;
}

/* 响应式优化 */
@media (max-width: 1400px) {
  .search-form .el-input,
  .search-form .el-select {
    width: 180px;
  }
  
  .search-form .date-range-item .el-date-editor {
    width: 160px;
  }
}

@media (max-width: 1200px) {
  .search-form .el-input,
  .search-form .el-select {
    width: 160px;
  }
  
  .search-form .date-range-item .el-date-editor {
    width: 140px;
  }
}

@media (max-width: 768px) {
  .search-form .el-input,
  .search-form .el-select {
    width: 100%;
  }
  
  .search-form .date-range-item .el-date-editor {
    width: 100%;
  }
  
  .search-form .el-form-item {
    display: block;
    width: 100%;
  }
}
</style>

