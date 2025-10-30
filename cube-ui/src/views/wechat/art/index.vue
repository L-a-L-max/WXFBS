<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入模板名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :icon="Plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wechat:prompt:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          :icon="Edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wechat:prompt:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wechat:prompt:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          :icon="Download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['wechat:prompt:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="promptList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="主建ID" align="center" prop="id" /> -->
      <el-table-column label="模板名称" align="center" prop="name" />
      <el-table-column label="模板" align="center" prop="prompt" />
      <el-table-column label="模板类型" align="center" prop="isCommon" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isCommon === 1 ? 'success' : 'info'">
            {{ scope.row.isCommon === 1 ? '公共模板' : '个人模板' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template #default="scope">
          <el-button
            v-if="scope.row.isCommon === 0"
            size="mini"
            type="text"
            :icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wechat:prompt:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="mini"
            type="text"
            :icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasRole="['admin']"
            v-hasPermi="['wechat:prompt:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.isCommon === 0"
            size="mini"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wechat:prompt:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="mini"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasRole="['admin']"
            v-hasPermi="['wechat:prompt:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.isCommon === 0"
            size="mini"
            type="text"
            style="color: #E6A23C"
            @click="handleSetCommon(scope.row, 1)"
            v-hasRole="['admin', 'common']"
          >设为公共</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="mini"
            type="text"
            style="color: #909399"
            @click="handleSetCommon(scope.row, 0)"
            v-hasRole="['admin', 'common']"
          >取消公共</el-button>
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

    <!-- 添加或修改评分模板配置对话框 -->
    <el-dialog :title="title" v-model="open" width="75%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板" prop="prompt">
          <el-input v-model="form.prompt" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Delete, Download, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue';

// import { listPrompt, getPrompt, delPrompt, addPrompt, updatePrompt } from "@/api/wechat/prompt";
import { getArtPromptList,getArtPrompt,deleteArtPrompt,saveArtPrompt,updateArtPrompt,setArtPromptCommon } from "@/api/wechat/aigc";

export default {
  name: "Prompt",
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
      // 评分模板配置表格数据
      promptList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        prompt: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        prompt: [
          { required: true, message: "模板不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询评分模板配置列表 */
    getList() {
      this.loading = true;
      getArtPromptList(this.queryParams).then(response => {
        this.promptList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        prompt: null,
        type: null,
        userId: null,
        isdel: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文章模板配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getArtPrompt(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文章模板配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArtPrompt(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            saveArtPrompt(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id ? [row.id]:this.ids;
      this.$modal.confirm('是否确认删除思路模板配置编号为"' + ids + '"的数据项？').then(function() {
        return deleteArtPrompt(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wechat/prompt/export', {
        ...this.queryParams
      }, `prompt_${new Date().getTime()}.xlsx`)
    },
    /** 设置公共模板 */
    handleSetCommon(row, isCommon) {
      const text = isCommon === 1 ? '设为公共' : '取消公共';
      this.$modal.confirm(`是否确认${text}该文章模板？`).then(() => {
        return setArtPromptCommon(row.id, isCommon);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(`${text}成功`);
      }).catch(() => {});
    }
  }
};
</script>
