<template>
  <div class="points-overview-page">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="points-overview-card" v-loading="summaryLoading">
          <div class="card-header">
            <div class="title">我的积分总览</div>
            <div class="actions">
              <el-button type="text" size="small" @click="refreshSummary">
                <el-icon><Refresh /></el-icon>刷新
              </el-button>
              <el-button type="primary" size="small" plain @click="showTaskList = true">
                获取更多积分
              </el-button>
            </div>
          </div>
          <div class="points-overview-body">
            <div class="points-main-stat">
              <div class="label">当前积分</div>
              <div class="value">{{ formatPointValue(pointsSummary.balance) }}</div>
            </div>
            <div class="points-sub-stats">
              <div class="points-stat">
                <div class="label">今日获得</div>
                <div class="value positive">+{{ pointsSummary.todayGain || 0 }}</div>
              </div>
              <div class="points-stat">
                <div class="label">本周获得</div>
                <div class="value positive">+{{ pointsSummary.weekGain || 0 }}</div>
              </div>
            </div>
            <div class="points-last-change" v-if="pointsSummary.lastChange">
              <div class="label">最近一次变动</div>
              <div class="desc">
                <span class="change-type">{{ pointsSummary.lastChange.changeType || '—' }}</span>
                <span
                  class="change-amount"
                  :class="{
                    positive: (pointsSummary.lastChange.changeAmount || 0) >= 0,
                    negative: (pointsSummary.lastChange.changeAmount || 0) < 0
                  }"
                >
                  {{ formatChangeAmount(pointsSummary.lastChange.changeAmount) }}
                </span>
                <span class="change-time">{{ pointsSummary.lastChange.createTime || '—' }}</span>
              </div>
            </div>
            <div class="no-record" v-else>
              暂无积分变动记录
            </div>
            <el-alert
              v-if="summaryError"
              type="error"
              :closable="false"
              :description="summaryError"
              show-icon
            />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="points-quick-panel">
          <div class="card-header">
            <div class="title">快捷入口</div>
          </div>
          <div class="quick-actions">
            <el-button type="primary" icon="el-icon-document" plain @click="openPointsRecordDialog">
              查看积分明细
            </el-button>
            <el-button type="success" icon="el-icon-trophy" plain @click="showTaskList = true">
              积分任务清单
            </el-button>
          </div>
          <div class="tip-text">
            清晰掌握积分来源与去向，保障积分收支透明。
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="points-table-card">
      <div class="card-header">
        <div class="title">积分流水</div>
        <div class="actions">
          <el-select
            v-model="queryPointForm.type"
            placeholder="筛选类型"
            clearable
            size="small"
            style="width: 150px"
            @change="handleFilterChange"
          >
            <el-option label="全部" value=""></el-option>
            <el-option label="增加" value="1"></el-option>
            <el-option label="消耗" value="2"></el-option>
          </el-select>
          <el-button type="primary" size="small" @click="refreshRecords">
            <el-icon><Refresh /></el-icon>刷新
          </el-button>
        </div>
      </div>
      <el-table
        v-loading="recordLoading"
        :data="pointsRecordList"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="index" width="60" label="#"></el-table-column>
        <el-table-column prop="create_time" label="变动时间" min-width="160"></el-table-column>
        <el-table-column prop="change_type" label="变动类型" min-width="160"></el-table-column>
        <el-table-column label="变动积分" min-width="120">
          <template #default="scope">
            <span :class="{ positive: scope.row.change_amount >= 0, negative: scope.row.change_amount < 0 }">
              {{ scope.row.change_amount > 0 ? '+' : '' }}{{ scope.row.change_amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip></el-table-column>
      </el-table>
      <pagination
        class="points-pagination"
        v-show="pointtotal > 0"
        :total="pointtotal"
        v-model:page="queryPointForm.page"
        v-model:limit="queryPointForm.limit"
        @pagination="getUserPointsRecord"
      />
    </el-card>

    <PointTaskList v-model="showTaskList" />

    <el-dialog title="积分明细" v-model="openPointsRecord" width="800px" append-to-body>
      <el-table
        :data="pointsRecordList"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="create_time" label="时间" min-width="160" />
        <el-table-column prop="change_type" label="类型" min-width="160" />
        <el-table-column label="变动" min-width="120">
          <template #default="scope">
            <span :class="{ positive: scope.row.change_amount >= 0, negative: scope.row.change_amount < 0 }">
              {{ scope.row.change_amount > 0 ? '+' : '' }}{{ scope.row.change_amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
      </el-table>
      <pagination
        v-show="pointtotal > 0"
        :total="pointtotal"
        v-model:page="queryPointForm.page"
        v-model:limit="queryPointForm.limit"
        @pagination="getUserPointsRecord"
      />
    </el-dialog>
  </div>
</template>

<script>
import { Refresh } from '@element-plus/icons-vue'
import { getMyPointsSummary } from '@/api/wechat/template'
import { getUserPointsRecord } from '@/api/wechat/company'
import { getUserProfile } from '@/api/system/user'
import PointTaskList from '@/views/wechat/points/TaskList.vue'

export default {
  name: 'PointsOverview',
  components: {
    Refresh,
    PointTaskList
  },
  data() {
    return {
      summaryLoading: false,
      summaryError: null,
      pointsSummary: {
        balance: 0,
        todayGain: 0,
        weekGain: 0,
        lastChange: null
      },
      recordLoading: false,
      pointsRecordList: [],
      pointtotal: 0,
      queryPointForm: {
        page: 1,
        limit: 10,
        type: '',
        userId: null
      },
      openPointsRecord: false,
      showTaskList: false
    }
  },
  async created() {
    await this.loadUserProfile()
    this.loadSummary()
    this.getUserPointsRecord()
  },
  methods: {
    async loadUserProfile() {
      try {
        const res = await getUserProfile()
        if (res && res.data) {
          this.queryPointForm.userId = res.data.userId
        }
      } catch (error) {
        this.queryPointForm.userId = null
      }
    },
    async loadSummary() {
      this.summaryLoading = true
      this.summaryError = null
      try {
        const res = await getMyPointsSummary()
        if (res.code === 200) {
          const data = res.data || {}
          this.pointsSummary = {
            balance: data.balance ?? 0,
            todayGain: data.todayGain ?? 0,
            weekGain: data.weekGain ?? 0,
            lastChange: data.lastChange || null
          }
        } else {
          this.summaryError = res.msg || '获取积分概览失败'
        }
      } catch (error) {
        this.summaryError = error?.msg || '获取积分概览失败'
      } finally {
        this.summaryLoading = false
      }
    },
    refreshSummary() {
      this.loadSummary()
    },
    formatPointValue(value) {
      const num = Number(value || 0)
      return num.toLocaleString()
    },
    formatChangeAmount(val) {
      if (val === null || val === undefined) return '--'
      const num = Number(val)
      return (num > 0 ? '+' : '') + num
    },
    handleFilterChange() {
      this.queryPointForm.page = 1
      this.getUserPointsRecord()
    },
    openPointsRecordDialog() {
      this.openPointsRecord = true
      this.getUserPointsRecord()
    },
    refreshRecords() {
      this.getUserPointsRecord()
    },
    getUserPointsRecord(params) {
      if (!this.queryPointForm.userId) {
        return
      }
      if (params) {
        this.queryPointForm.page = params.page
        this.queryPointForm.limit = params.limit
      }
      this.recordLoading = true
      const requestData = {
        userId: this.queryPointForm.userId,
        page: this.queryPointForm.page,
        limit: this.queryPointForm.limit,
        type: this.queryPointForm.type === '' ? null : parseInt(this.queryPointForm.type)
      }
      getUserPointsRecord(requestData).then(response => {
        this.pointsRecordList = response.data?.list || []
        this.pointtotal = response.data?.total || 0
        this.recordLoading = false
      }).catch(() => {
        this.recordLoading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.points-overview-page {
  padding: 24px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;

    .title {
      font-size: 16px;
      font-weight: 600;
    }
  }

  .points-overview-card {
    .points-overview-body {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .points-main-stat {
      background: linear-gradient(135deg, #0fb2fc, #4facfe);
      color: #fff;
      padding: 20px;
      border-radius: 12px;

      .label {
        font-size: 14px;
        opacity: 0.85;
        margin-bottom: 6px;
      }

      .value {
        font-size: 32px;
        font-weight: 700;
      }
    }

    .points-sub-stats {
      display: flex;
      gap: 12px;

      .points-stat {
        flex: 1;
        background: #f5f7fa;
        border-radius: 10px;
        padding: 14px;

        .label {
          font-size: 13px;
          color: #909399;
          margin-bottom: 4px;
        }

        .value {
          font-size: 20px;
          font-weight: 600;

          &.positive {
            color: #67c23a;
          }
        }
      }
    }

    .points-last-change {
      background: #fdf6ec;
      border: 1px solid #f5dab1;
      border-radius: 10px;
      padding: 12px 16px;

      .label {
        font-size: 13px;
        color: #e6a23c;
        margin-bottom: 6px;
      }

      .desc {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        font-size: 14px;
        color: #606266;

        .change-type {
          font-weight: 600;
        }

        .change-amount {
          font-weight: 600;

          &.positive {
            color: #67c23a;
          }

          &.negative {
            color: #f56c6c;
          }
        }

        .change-time {
          color: #909399;
        }
      }
    }

    .no-record {
      font-size: 14px;
      color: #909399;
      text-align: center;
    }
  }

  .points-quick-panel {
    height: 100%;

    .quick-actions {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      margin-top: 20px;
    }

    .tip-text {
      margin-top: 24px;
      font-size: 14px;
      color: #909399;
    }
  }

  .points-table-card {
    margin-top: 20px;

    .card-header {
      margin-bottom: 16px;
    }

    .points-pagination {
      margin-top: 12px;
      text-align: right;
    }

    .positive {
      color: #67c23a;
    }

    .negative {
      color: #f56c6c;
    }
  }
}
</style>

