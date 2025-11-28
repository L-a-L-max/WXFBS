<template>
  <div>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="templateType === 'score' ? '模板名称' : '平台ID'" prop="name">
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
      <el-table-column :label="templateType === 'score' ? '模板名称' : '平台ID'" align="center" :prop="templateType === 'score' ? 'name' : 'platformId'" />
      <el-table-column label="模板内容" align="center" prop="prompt" show-overflow-tooltip />
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
      <el-table-column label="销量" align="center" prop="salesCount" width="80" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDate(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            :icon="Edit"
            @click="handleEdit(scope.row)"
          >编辑</el-button>
          <el-button
            v-if="scope.row.status !== 1 && scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #409EFF"
            @click="handlePublish(scope.row)"
          >上架</el-button>
          <el-button
            v-if="scope.row.status === 1 && scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #F56C6C"
            @click="handleUnpublish(scope.row)"
          >下架</el-button>
          <el-button
            v-if="scope.row.isCommon === 0"
            size="small"
            type="text"
            style="color: #E6A23C"
            @click="handleSetCommon(scope.row, 1)"
          >设为公共</el-button>
          <el-button
            v-if="scope.row.isCommon === 1"
            size="small"
            type="text"
            style="color: #909399"
            @click="handleSetCommon(scope.row, 0)"
          >取消公共</el-button>
          <el-button
            size="small"
            type="text"
            style="color: #F56C6C"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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
import { Search, Refresh, Edit } from '@element-plus/icons-vue'
import { getMyCreatedTemplates, getMyCreatedCallWords } from "@/api/wechat/template"
import {
  publishScorePrompt,
  unpublishScorePrompt,
  deleteScorePrompt,
  setScorePromptCommon,
  publishCallWord,
  unpublishCallWord,
  deleteCallWord,
  setCallWordCommon,
  updateScorePrompt,
  updateMediaCallWord
} from "@/api/wechat/aigc"

export default {
  name: "CreatedList",
  components: {},
  props: {
    templateType: {
      type: String,
      default: 'score' // 'score' 或 'callword'
    }
  },
  setup() {
    return {
      Search,
      Refresh,
      Edit
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
        platformId: null // 提示词模板使用platformId搜索
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
        ? getMyCreatedTemplates(this.queryParams)
        : getMyCreatedCallWords(this.queryParams)
      
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
      this.ids = selection.map(item => this.templateType === 'score' ? item.id : item.platformId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleEdit(row) {
      if (this.templateType === 'score') {
        // 跳转到模板工作坊编辑评分模板
        this.$router.push({
          path: '/wechat/templateWorkshop',
          query: { id: row.id, mode: 'edit', type: 'score' }
        })
      } else {
        // 在模板工作坊编辑提示词模板
        this.$router.push({
          path: '/wechat/templateWorkshop',
          query: { platformId: row.platformId, mode: 'edit', type: 'callword' }
        })
      }
    },
    handlePublish(row) {
      this.ensurePublishPrice(row).then(price => {
        const publishRequest = this.templateType === 'score'
          ? publishScorePrompt({ id: row.id, price })
          : publishCallWord({ platformId: row.platformId, price })
        publishRequest.then(() => {
          this.$modal.msgSuccess("上架成功")
          this.getList()
        })
      }).catch(() => {})
    },
    handleUnpublish(row) {
      const templateName = this.templateType === 'score' ? row.name : row.platformId
      this.$modal.confirm(`确认下架模板「${templateName}」吗？`).then(() => {
        if (this.templateType === 'score') {
          return unpublishScorePrompt(row.id)
        } else {
          return unpublishCallWord(row.platformId)
        }
      }).then(() => {
        this.$modal.msgSuccess("模板已下架")
        this.getList()
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = this.templateType === 'score' 
        ? (row.id ? [row.id] : this.ids)
        : (row.platformId ? [row.platformId] : this.ids)
      this.$modal.confirm('是否确认删除模板编号为"' + ids + '"的数据项？').then(() => {
        if (this.templateType === 'score') {
          return deleteScorePrompt(ids)
        } else {
          return deleteCallWord(ids)
        }
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleSetCommon(row, isCommon) {
      const isScore = this.templateType === 'score'
      const templateName = isScore ? row.name : row.platformId
      const actionText = isCommon === 1 ? '设为公共' : '取消公共'
      this.$modal.confirm(`是否确认${actionText}模板「${templateName}」？`).then(() => {
        const setCommonRequest = isScore
          ? setScorePromptCommon(row.id, isCommon)
          : setCallWordCommon(row.platformId, isCommon)
        return setCommonRequest.then(() => ({ isScore }))
      }).then(({ isScore }) => {
        if (isScore && isCommon === 0 && row.status === 1) {
          return unpublishScorePrompt(row.id).then(() => {
            this.$modal.msgSuccess(`${actionText}成功，模板已下架`)
            this.getList()
          })
        }
        this.$modal.msgSuccess(`${actionText}成功`)
        this.getList()
      }).catch(() => {})
    },
    ensurePublishPrice(row) {
      return new Promise((resolve, reject) => {
        const currentPrice = Number(row.price)
        if (currentPrice && currentPrice > 0) {
          resolve(currentPrice)
          return
        }
        this.$prompt('请输入上架价格', '设置价格', {
          confirmButtonText: '保存',
          cancelButtonText: '取消',
          inputPattern: /^\d+(\.\d{1,2})?$/,
          inputErrorMessage: '请输入大于0且最多两位小数的价格'
        }).then(({ value }) => {
          const newPrice = Number(value)
          if (!newPrice || newPrice <= 0) {
            this.$modal.msgError("价格必须大于0")
            reject()
            return
          }
          const savePayload = this.templateType === 'score'
            ? {
                id: row.id,
                name: row.name,
                prompt: row.prompt,
                price: newPrice,
                status: row.status,
                isCommon: row.isCommon
              }
            : {
                platformId: row.platformId,
                wordContent: row.prompt || row.wordContent,
                price: newPrice,
                status: row.status,
                isCommon: row.isCommon
              }
          const saveRequest = this.templateType === 'score'
            ? updateScorePrompt(savePayload)
            : updateMediaCallWord(savePayload)
          saveRequest.then(() => {
            this.$modal.msgSuccess("价格已保存")
            row.price = newPrice
            resolve(newPrice)
          }).catch(() => reject())
        }).catch(() => reject())
      })
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

