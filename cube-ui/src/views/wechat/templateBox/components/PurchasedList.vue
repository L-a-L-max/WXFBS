<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="templateType === 'score' ? '模板名称' : '平台ID'" :prop="templateType === 'score' ? 'name' : 'platformId'">
        <el-input
          v-model="queryParams[currentSearchField]"
          :placeholder="templateType === 'score' ? '请输入模板名称' : '请输入平台ID'"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="small" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="templateType === 'score' ? '模板名称' : '平台ID'" align="center" :prop="templateType === 'score' ? 'name' : 'templateId'" />
      <el-table-column label="模板内容" align="center" prop="prompt" show-overflow-tooltip />
      <el-table-column label="作者" align="center" prop="authorName" width="120" />
      <el-table-column label="购买价格" align="center" prop="purchasePrice" width="100">
        <template #default="scope">
          <span>{{ formatPrice(scope.row.purchasePrice) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="购买时间" align="center" prop="purchaseTime" width="180">
        <template #default="scope">
          <span>{{ formatDate(scope.row.purchaseTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="small"
            type="text"
            @click="handleUse(scope.row)"
          >使用</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      @current-change="getList"
      @size-change="getList"
    />
  </div>
</template>

<script>
import { Search, Refresh } from '@element-plus/icons-vue'
import { getMyPurchasedTemplates, getMyPurchasedCallWords } from "@/api/wechat/template"

export default {
  name: "PurchasedList",
  components: {},
  props: {
    templateType: {
      type: String,
      default: 'score'
    }
  },
  setup() {
    return {
      Search,
      Refresh
    }
  },
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      templateList: [],
      ids: [],
      single: true,
      multiple: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        platformId: null
      }
    }
  },
  computed: {
    currentSearchField() {
      return this.templateType === 'score' ? 'name' : 'platformId'
    }
  },
  watch: {
    templateType: {
      handler() {
        this.queryParams.name = null
        this.queryParams.platformId = null
        this.queryParams.pageNum = 1
        this.getList()
      },
      immediate: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const apiCall = this.templateType === 'score'
        ? getMyPurchasedTemplates(this.queryParams)
        : getMyPurchasedCallWords(this.queryParams)
      
      apiCall.then(response => {
        this.templateList = response.rows || response.data || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields()
      }
      this.queryParams.name = null
      this.queryParams.platformId = null
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleView(row) {
      // 查看模板详情
      if (this.templateType === 'score') {
        this.$router.push({
          path: '/prompt-template/templateWorkshop',
          query: { id: row.templateId, mode: 'view', type: 'score' }
        })
      } else {
        this.$router.push({
          path: '/prompt-template/templateWorkshop',
          query: { platformId: row.templateId, mode: 'view', type: 'callword' }
        })
      }
    },
    handleUse(row) {
      // 使用模板（跳转到使用页面或触发使用事件）
      this.$modal.msgSuccess("模板已加载，可以在工作区使用")
    },
    formatPrice(value) {
      return Number(value || 0).toFixed(2)
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.getFullYear() + '-' + 
             String(d.getMonth() + 1).padStart(2, '0') + '-' + 
             String(d.getDate()).padStart(2, '0') + ' ' +
             String(d.getHours()).padStart(2, '0') + ':' + 
             String(d.getMinutes()).padStart(2, '0')
    }
  }
}
</script>

