<template>
  <div class="app-container">
    <!-- 模板类型选择器 -->
    <div class="template-type-selector" style="margin-bottom: 20px">
      <el-radio-group v-model="templateType" @change="handleTemplateTypeChange" size="default">
        <el-radio-button label="score">评分模板</el-radio-button>
        <el-radio-button label="callword">提示词模板</el-radio-button>
      </el-radio-group>
    </div>
    
    <!-- 积分信息栏 -->
    <div class="points-info-bar" style="margin-bottom: 20px; padding: 15px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; display: flex; align-items: center; justify-content: space-between;">
      <div style="display: flex; align-items: center; color: #fff;">
        <i class="el-icon-wallet" style="font-size: 24px; margin-right: 10px;"></i>
        <span style="font-size: 16px; margin-right: 5px;">我的积分：</span>
        <span style="font-size: 24px; font-weight: bold;">{{ myPoints }}</span>
      </div>
      <el-button type="warning" size="default" @click="showTaskList = true" icon="el-icon-trophy" plain>
        获取更多积分
      </el-button>
    </div>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="templateType === 'score' ? '模板名称' : '平台ID'" :prop="templateType === 'score' ? 'name' : 'platformId'">
        <el-input
          v-model="queryParams[currentSearchField]"
          :placeholder="templateType === 'score' ? '请输入模板名称' : '请输入平台ID'"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="价格范围">
        <el-input-number
          v-model="queryParams.minPrice"
          :min="0"
          :precision="2"
          placeholder="最低价"
          style="width: 120px"
        />
        <span style="margin: 0 10px">-</span>
        <el-input-number
          v-model="queryParams.maxPrice"
          :min="0"
          :precision="2"
          placeholder="最高价"
          style="width: 120px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" size="small" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20" v-loading="loading">
      <el-col :span="6" v-for="item in templateList" :key="item.id" style="margin-bottom: 20px">
        <el-card shadow="hover" class="template-card">
          <div class="template-header">
            <h3>{{ item.name || item.platformId }}</h3>
            <el-tag type="success" size="small">¥{{ formatPrice(item.price) }}</el-tag>
          </div>
          <div class="template-content">
            <p>{{ item.prompt ? (item.prompt.length > 100 ? item.prompt.substring(0, 100) + '...' : item.prompt) : '暂无描述' }}</p>
          </div>
          <div class="template-footer">
            <div class="template-stats">
              <span>销量: {{ item.salesCount || 0 }}</span>
              <span style="margin-left: 10px">作者: {{ item.authorName || '未知' }}</span>
            </div>
            <el-button
              type="primary"
              size="small"
              @click="handlePurchase(item)"
              :disabled="item.isPurchased"
            >
              {{ item.isPurchased ? '已购买' : '购买' }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      @current-change="getList"
      @size-change="getList"
    />

    <!-- 积分任务清单 -->
    <PointTaskList v-model="showTaskList" />

    <!-- 购买确认对话框 -->
    <el-dialog title="购买模板" v-model="purchaseDialogVisible" width="500px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="模板名称">
          <span>{{ purchaseForm.name }}</span>
        </el-form-item>
        <el-form-item label="价格">
          <span style="color: #F56C6C; font-size: 18px; font-weight: bold">¥{{ formatPrice(purchaseForm.price) }}</span>
        </el-form-item>
        <el-form-item label="我的积分">
          <span style="font-size: 18px; font-weight: bold; color: #409eff; margin-right: 10px">{{ myPoints }}</span>
          <el-button type="primary" size="small" @click="showTaskList = true" icon="el-icon-trophy">
            获取更多积分
          </el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmPurchase" :loading="purchasing">确认购买</el-button>
        <el-button @click="purchaseDialogVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Search, Refresh } from '@element-plus/icons-vue'
import { getMarketTemplates, purchaseTemplate, getMyPoints, getMarketCallWords, purchaseCallWord } from "@/api/wechat/template"
import PointTaskList from "@/views/wechat/points/TaskList.vue"

export default {
  name: "TemplateMarket",
  components: {
    PointTaskList
  },
  setup() {
    return {
      Search,
      Refresh
    }
  },
  data() {
    return {
      templateType: 'score', // 默认选择评分模板
      loading: false,
      showSearch: true,
      total: 0,
      templateList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        name: null,
        platformId: null,
        minPrice: null,
        maxPrice: null
      },
      purchaseDialogVisible: false,
      purchasing: false,
      purchaseForm: {
        id: null,
        name: null,
        price: null
      },
      myPoints: 0,
      showTaskList: false
    }
  },
  computed: {
    currentSearchField() {
      return this.templateType === 'score' ? 'name' : 'platformId'
    }
  },
  created() {
    this.getList()
    this.loadMyPoints()
  },
  methods: {
    handleTemplateTypeChange() {
      this.queryParams.name = null
      this.queryParams.platformId = null
      this.queryParams.pageNum = 1
      this.getList()
    },
    getList() {
      this.loading = true
      const apiCall = this.templateType === 'score'
        ? getMarketTemplates(this.queryParams)
        : getMarketCallWords(this.queryParams)
      
      apiCall.then(response => {
        this.templateList = response.rows || response.data || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadMyPoints() {
      // 获取用户积分
      getMyPoints().then(response => {
        this.myPoints = response.data || 0
      }).catch(() => {
        this.myPoints = 0
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
      this.queryParams.minPrice = null
      this.queryParams.maxPrice = null
      this.handleQuery()
    },
    handlePurchase(row) {
      this.purchaseForm = {
        id: this.templateType === 'score' ? row.id : row.id, // 提示词模板使用platformId作为id
        name: row.name || row.platformId,
        price: row.price
      }
      this.purchaseDialogVisible = true
      this.loadMyPoints()
    },
    confirmPurchase() {
      if (this.myPoints < this.purchaseForm.price) {
        this.$modal.msgError("积分不足，无法购买")
        return
      }
      this.purchasing = true
      const purchaseApi = this.templateType === 'score'
        ? purchaseTemplate(this.purchaseForm.id)
        : purchaseCallWord(this.purchaseForm.id)
      
      purchaseApi.then((response) => {
        if (response.code === 200) {
          this.$modal.msgSuccess("购买成功")
          this.purchaseDialogVisible = false
          this.getList()
          this.loadMyPoints()
        } else {
          this.$modal.msgError(response.msg || "购买失败，请稍后重试")
        }
      }).catch((error) => {
        const errorMsg = error.response?.data?.msg || error.message || "购买失败，请稍后重试"
        this.$modal.msgError(errorMsg)
      }).finally(() => {
        this.purchasing = false
      })
    },
    formatPrice(value) {
      return Number(value || 0).toFixed(2)
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.template-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.template-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.template-header h3 {
  margin: 0;
  font-size: 16px;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.template-content {
  flex: 1;
  margin-bottom: 15px;
  min-height: 60px;
}

.template-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.template-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.template-stats {
  font-size: 12px;
  color: #999;
}
</style>

