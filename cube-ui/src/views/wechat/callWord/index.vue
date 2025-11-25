<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <!-- <el-button type="primary" :icon="Search" size="small" @click="handleQuery">搜索</el-button> -->
        <el-button :icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          :icon="Plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['wechat:callWord:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          :icon="Edit"
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wechat:callWord:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          :icon="Delete"
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wechat:callWord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          :icon="Download"
          size="small"
          @click="handleExport"
          v-hasPermi="['wechat:callWord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="promptList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="平台标识" align="center" prop="platformId" />
      <el-table-column label="提示词内容" align="center" prop="wordContent" />
      <el-table-column label="模板类型" align="center" prop="isCommon" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isCommon === 1 ? 'success' : 'info'">
            {{ scope.row.isCommon === 1 ? '公共模板' : '个人模板' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="价格" align="center" prop="price" width="100">
        <template #default="scope">
          <span>{{ formatPrice(scope.row.price) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上架状态" align="center" prop="status" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '已上架' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template #default="scope">
          <el-button
            v-if="scope.row.isCommon === 0"
            size="small"
            type="text"
            :icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wechat:callWord:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="small"
            type="text"
            :icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasRole="['admin']"
            v-hasPermi="['wechat:callWord:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.isCommon === 0"
            size="small"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wechat:callWord:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="small"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasRole="['admin']"
            v-hasPermi="['wechat:callWord:remove']"
          >删除</el-button>
          <el-button
            v-if="scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #E6A23C"
            :loading="setCommonLoading && commonPlatformId === scope.row.platformId"
            @click="handleSetCommon(scope.row, 1)"
            v-hasRole="['admin', 'common']"
          >设为公共</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="small"
            type="text"
            style="color: #909399"
            :loading="setCommonLoading && commonPlatformId === scope.row.platformId"
            @click="handleSetCommon(scope.row, 0)"
            v-hasRole="['admin', 'common']"
          >取消公共</el-button>
          <el-button
            v-if="scope.row.status !== 1 && scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #409EFF"
            @click="openPublishDialog(scope.row)"
          >上架市场</el-button>
          <el-button
            v-if="scope.row.status === 1 && scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #F56C6C"
            @click="handleUnpublish(scope.row)"
          >下架</el-button>
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

    <!-- 添加或修改平台提示词配置对话框 -->
    <el-dialog :title="title" v-model="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                  
        <el-form-item label="平台标识">
          <el-input v-model="form.platformId "/>
        </el-form-item>
        <el-form-item label="提示词">
          <el-input type="textarea" autosize v-model="form.wordContent" :min-height="360"/>
        </el-form-item>
        <el-form-item label="基础价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="模板上架" v-model="publishDialogVisible" width="400px" append-to-body>
      <el-form ref="publishFormRef" :model="publishForm" :rules="publishRules" label-width="100px">
        <el-form-item label="模板标识">
          <span>{{ publishForm.platformId }}</span>
        </el-form-item>
        <el-form-item label="上架价格" prop="price">
          <el-input-number v-model="publishForm.price" :min="0.01" :precision="2" :step="1" controls-position="right" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="publishDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="publishing" @click="submitPublish">上架</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Delete, Download, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue';

// import { listPrompt, getPrompt, delPrompt, addPrompt, updatePrompt } from "@/api/prompt/prompt";
import { getMediaCallWordList,getMediaCallWord,updateMediaCallWord,deleteMediaCallWord,setCallWordCommon,publishCallWord,unpublishCallWord } from "@/api/wechat/aigc";
export default {
  name: "Prompt",
  components: {},
  setup() {
    return {
      Search,
      Refresh,
      Plus,
      Edit,
      Delete,
      Download
    }
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
      // 平台提示词配置表格数据
      promptList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wordContent: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        platformId: [
          { required: true, message: "平台标识 wechat_layout-公众号排版 zhihu_layout-知乎排版不能为空", trigger: "blur" }
        ],
        wordContent: [
          { required: true, message: "提示词内容不能为空", trigger: "blur" }
        ],
      },
      // 设为公共/取消公共状态
      setCommonLoading: false,
      commonPlatformId: null,
      publishDialogVisible: false,
      publishForm: {
        platformId: '',
        price: null,
      },
      publishing: false,
      publishRules: {
        price: [
          { required: true, message: "请输入上架价格", trigger: "blur" },
          { validator: (rule, value, callback) => {
              if (value === null || value === undefined || Number(value) <= 0) {
                callback(new Error("价格必须大于0"));
              } else {
                callback();
              }
            }, trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询平台提示词配置列表 */
    getList() {
      this.loading = true;
      getMediaCallWordList(this.queryParams).then(response => {
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
        platformId: null,
        wordContent: null,
        updateTime: null,
        price: 0,
        status: 0
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
      this.ids = selection.map(item => item.platformId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加平台提示词配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const platformId = row.platformId || this.ids
      getMediaCallWord(platformId).then(response => {
        this.form = response.data;
        if (this.form.price === undefined || this.form.price === null) {
          this.form.price = 0;
        }
        if (this.form.status === undefined || this.form.status === null) {
          this.form.status = 0;
        }
        this.open = true;
        this.title = "修改平台提示词配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.platformId != null) {
            updateMediaCallWord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            updateMediaCallWord(this.form).then(response => {
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
      const platformIds = row.platformId ? [row.platformId] : this.ids;
      this.$modal.confirm('是否确认删除平台提示词配置编号为"' + platformIds + '"的数据项？').then(function() {
        return deleteMediaCallWord(platformIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('prompt/prompt/export', {
        ...this.queryParams
      }, `prompt_${new Date().getTime()}.xlsx`)
    },
    /** 设置公共模板 */
    handleSetCommon(row, isCommon) {
      const text = isCommon === 1 ? '设为公共' : '取消公共';
      this.$modal.confirm(`是否确认${text}该提示词模板？`).then(() => {
        this.setCommonLoading = true;
        this.commonPlatformId = row.platformId;
        return setCallWordCommon(row.platformId, isCommon);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(`${text}成功`);
      }).catch(() => {}).finally(() => {
        this.setCommonLoading = false;
        this.commonPlatformId = null;
      });
    },
    openPublishDialog(row) {
      this.publishForm.platformId = row.platformId;
      this.publishForm.price = row.price && Number(row.price) > 0 ? Number(row.price) : null;
      this.publishDialogVisible = true;
      this.$nextTick(() => {
        this.$refs.publishFormRef && this.$refs.publishFormRef.clearValidate();
      });
    },
    submitPublish() {
      this.$refs.publishFormRef.validate(valid => {
        if (!valid) return;
        this.publishing = true;
        publishCallWord({
          platformId: this.publishForm.platformId,
          price: this.publishForm.price
        }).then(() => {
          this.$modal.msgSuccess("上架成功");
          this.publishDialogVisible = false;
          this.getList();
        }).finally(() => {
          this.publishing = false;
        });
      });
    },
    handleUnpublish(row) {
      this.$modal.confirm(`确认下架模板「${row.platformId}」吗？`).then(() => {
        return unpublishCallWord(row.platformId);
      }).then(() => {
        this.$modal.msgSuccess("模板已下架");
        this.getList();
      }).catch(() => {});
    },
    formatPrice(value) {
      return Number(value || 0).toFixed(2);
    }
  }
};
</script>
