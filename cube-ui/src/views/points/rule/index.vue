<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则名称" prop="dictLabel">
        <el-input
          v-model="queryParams.dictLabel"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="数据状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          type="primary"
          plain
          :icon="Plus"
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:dict:add']"
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
          v-hasPermi="['system:dict:edit']"
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
          v-hasPermi="['system:dict:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          :icon="Download"
          size="small"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则编码" align="center" prop="dictCode" />
      <el-table-column label="规则名称" align="center" prop="dictLabel">
        <template #default="scope">
          <span>{{ scope.row.dictLabel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="积分值" align="center" prop="dictValue">
        <template #default="scope">
          <span :class="{ positive: scope.row.dictValue > 0, negative: scope.row.dictValue < 0 }">
            {{ scope.row.dictValue > 0 ? '+' : '' }}{{ scope.row.dictValue }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="dictSort" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="限频配置" align="center" prop="remark" :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ formatRuleConfig(scope.row.remark) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            :icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >修改</el-button>
          <el-button
            size="small"
            type="text"
            :icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dict:remove']"
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

    <!-- 添加或修改积分规则配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="规则名称" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入规则名称，如：首次登录、每日打卡等" />
        </el-form-item>
        <el-form-item label="积分值" prop="dictValue">
          <el-input-number 
            v-model="form.dictValue" 
            placeholder="请输入积分值，正数表示增加，负数表示扣除" 
            :precision="0"
            controls-position="right"
            style="width: 100%"
          />
          <div class="form-tip">正数表示增加积分，负数表示扣除积分</div>
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-divider content-position="left">限频配置</el-divider>
        <el-form-item label="限频类型">
          <el-select v-model="ruleConfig.limitType" placeholder="请选择限频类型" style="width: 100%">
            <el-option v-for="item in limitTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <div class="form-tip">设置该积分规则的领取频率限制</div>
        </el-form-item>
        <el-form-item label="限频值" v-if="ruleConfig.limitType !== 'NONE'">
          <el-input-number 
            v-model="ruleConfig.limitValue" 
            :min="1" 
            placeholder="例如：每日限制次数" 
            controls-position="right"
            style="width: 100%"
          />
          <div class="form-tip">在限频类型周期内，最多可领取的次数</div>
        </el-form-item>
        <el-form-item label="累计上限">
          <el-input-number 
            v-model="ruleConfig.maxAmount" 
            placeholder="不限制可留空" 
            controls-position="right" 
            :min="1"
            style="width: 100%"
          />
          <div class="form-tip">该规则累计可领取的积分上限，留空表示不限制</div>
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

import { listData, getData, delData, addData, updateData } from "@/api/system/dict/data";

export default {
  name: "PointRule",
  dicts: ['sys_normal_disable'],
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
      // 积分规则表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dictType: 'sys_point_rule',
        dictLabel: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 积分规则限频配置
      ruleConfig: {},
      limitTypeOptions: [
        { label: '不限', value: 'NONE' },
        { label: '每日', value: 'DAILY' },
        { label: '每周', value: 'WEEKLY' },
        { label: '每月', value: 'MONTHLY' },
        { label: '累计', value: 'TOTAL' }
      ],
      // 表单校验
      rules: {
        dictLabel: [
          { required: true, message: "规则名称不能为空", trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: "积分值不能为空", trigger: "blur" }
        ],
        dictSort: [
          { required: true, message: "排序不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询积分规则列表 */
    getList() {
      this.loading = true;
      listData(this.queryParams).then(response => {
        this.dataList = response.rows;
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
        dictCode: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        dictType: 'sys_point_rule',
        dictSort: 0,
        status: "0",
        remark: undefined
      };
      this.ruleConfig = this.getDefaultRuleConfig();
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加积分规则";
      this.form.dictType = 'sys_point_rule';
      this.ruleConfig = this.getDefaultRuleConfig();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dictCode)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const dictCode = row.dictCode || this.ids[0]
      getData(dictCode).then(response => {
        this.form = response.data;
        this.ruleConfig = this.parseRuleConfig(this.form.remark);
        this.open = true;
        this.title = "修改积分规则";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将限频配置序列化到remark字段
          this.form.remark = this.serializeRuleConfig(this.ruleConfig);
          
          if (this.form.dictCode != undefined) {
            updateData(this.form).then(response => {
              this.$store.dispatch('dict/removeDict', 'sys_point_rule');
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addData(this.form).then(response => {
              this.$store.dispatch('dict/removeDict', 'sys_point_rule');
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
      const dictCodes = row.dictCode || this.ids;
      this.$modal.confirm('是否确认删除规则编码为"' + dictCodes + '"的积分规则？').then(function() {
        return delData(dictCodes);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
        this.$store.dispatch('dict/removeDict', 'sys_point_rule');
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dict/data/export', {
        ...this.queryParams
      }, `point_rule_${new Date().getTime()}.xlsx`)
    },
    getDefaultRuleConfig() {
      return {
        limitType: 'NONE',
        limitValue: null,
        maxAmount: null
      };
    },
    parseRuleConfig(remark) {
      let config = this.getDefaultRuleConfig();
      if (!remark) {
        return { ...config };
      }
      try {
        const parsed = JSON.parse(remark);
        return Object.assign({}, config, parsed || {});
      } catch (e) {
        console.warn('积分规则 remark 不是合法 JSON：', remark);
        return { ...config };
      }
    },
    serializeRuleConfig(config) {
      const data = Object.assign({}, this.getDefaultRuleConfig(), config || {});
      if (!data.limitType) {
        data.limitType = 'NONE';
      }
      if (data.limitType === 'NONE') {
        data.limitValue = null;
      }
      // 清理空值
      if (data.maxAmount === null || data.maxAmount === undefined || data.maxAmount === '') {
        delete data.maxAmount;
      }
      if (data.limitValue === null || data.limitValue === undefined || data.limitValue === '') {
        delete data.limitValue;
      }
      return JSON.stringify(data);
    },
    formatRuleConfig(remark) {
      if (!remark) {
        return '未配置';
      }
      try {
        const config = JSON.parse(remark);
        const parts = [];
        
        // 限频类型和限频值
        if (config.limitType && config.limitType !== 'NONE') {
          const limitTypeMap = {
            'DAILY': '每日',
            'WEEKLY': '每周',
            'MONTHLY': '每月',
            'TOTAL': '累计'
          };
          const limitTypeText = limitTypeMap[config.limitType] || config.limitType;
          if (config.limitValue) {
            parts.push(`${limitTypeText}限${config.limitValue}次`);
          } else {
            parts.push(`${limitTypeText}限频`);
          }
        } else {
          parts.push('不限频');
        }
        
        // 累计上限
        if (config.maxAmount) {
          parts.push(`累计上限${config.maxAmount}`);
        }
        
        return parts.length > 0 ? parts.join('，') : '未配置';
      } catch (e) {
        return remark;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }
  
  .positive {
    color: #67c23a;
    font-weight: 600;
  }
  
  .negative {
    color: #f56c6c;
    font-weight: 600;
  }
}
</style>

