<template>
  <div class="dashboard-editor-container">
    <!--    <panel-group @handleSetLineChartData="handleSetLineChartData" />-->
    <div class="app-container">
      <el-row :gutter="30">
        <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>个人信息</span>
            </div>
            <div>
              <div class="text-center">
                <img :src="user.avatar || defaultAvatar" class="user-avatar-display" />
              </div>

              <ul class="list-group list-group-striped">
                <li class="list-group-item">
                  <svg-icon icon-class="user" />
                  <span>用户名称</span>
                  <div class="pull-right" id="userName">
                    {{ user.nickName }}
                  </div>
                </li>
                <li class="list-group-item">
                  <svg-icon icon-class="phone" />
                  <span>手机号码</span>
                  <div class="pull-right">{{ user.phonenumber }}</div>
                </li>
                <li class="list-group-item">
                  <svg-icon icon-class="date" />
                  <span>创建日期</span>
                  <div class="pull-right">{{ user.createTime }}</div>
                </li>
                <li class="list-group-item points-item" @click="showPointsDetail">
                  <svg-icon icon-class="star" />
                  <span>积分余额</span>
                  <div
                    :style="{ color: user.points >= 0 ? '#67c23a' : '#f56c6c' }"
                    class="pull-right points-value"
                  >
                    {{ user.points >= 0 ? '+' : '' }}{{ user.points }}
                  </div>
                  <el-tooltip
                    content="点击查看积分明细"
                    placement="top"
                    effect="light"
                  >
                    <el-icon class="info-icon"><InfoFilled /></el-icon>
                  </el-tooltip>
                </li>
              </ul>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
          <el-card class="box-card ai-status-card">
            <div slot="header" class="clearfix">
              <span class="card-title">
                <svg-icon icon-class="ai" class="title-icon" />
                AI 登录状态
              </span>
              <el-button
                style="float: right; margin-top: -30px"
                type="text"
                @click="handleRefreshAI"
              >
                <el-icon><Refresh /></el-icon> 刷新
              </el-button>
            </div>
            <div class="ai-status-list">
              <div
                class="ai-status-item"
                v-for="ai in availableAiList"
                :key="ai.agentCode"
              >
                <div class="ai-platform">
                  <div class="platform-icon">
                    <img
                      :src="ai.agentIcon"
                      :alt="ai.agentName"
                    />
                  </div>
                  <div class="platform-name">
                    {{ ai.agentName }}
                    <el-tooltip
                      v-if="isLoading[ai.agentCode]"
                      content="正在登录中..."
                      placement="top"
                    >
                      <i class="el-icon-loading loading-icon"></i>
                    </el-tooltip>
                  </div>
                </div>
                <div class="status-action">
                  <!-- 在线且已登录 -->
                  <el-tag
                    v-if="aiLoginStatus[ai.agentCode] && ai.onlineStatus === 1"
                    type="success"
                    effect="dark"
                    class="status-tag"
                  >
                    <el-icon><SuccessFilled /></el-icon>
                    <span>{{ accounts[ai.agentCode] }}</span>
                  </el-tag>
                  <!-- 离线状态 - 显示禁用的登录按钮 -->
                  <el-tooltip
                    v-else-if="ai.onlineStatus === 0"
                    content="AI服务离线，暂时无法登录"
                    placement="top"
                    effect="dark"
                  >
                    <el-button
                      type="primary"
                      size="small"
                      disabled
                      class="ai-login-btn offline-login-btn"
                    >
                      <el-icon><Connection /></el-icon> <span class="btn-text">点击登录</span>
                    </el-button>
                  </el-tooltip>
                  <!-- 在线但未登录 -->
                  <el-button
                    v-else
                    type="primary"
                    size="small"
                    :disabled="!isClick[ai.agentCode]"
                    @click="handleAiLogin(ai.agentCode)"
                    :class="'ai-login-btn'"
                    :title="'点击登录'"
                  >
                    <el-icon><Connection /></el-icon> <span class="btn-text">点击登录</span>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="24" :sm="12" :md="8" :lg="6">
          <el-card class="box-card ai-status-card">
            <div slot="header" class="clearfix">
              <span class="card-title">
                <svg-icon icon-class="media" class="title-icon" />
                媒体登录状态
              </span>
              <el-button
                style="float: right; margin-top: -30px"
                type="text"
                @click="handleRefreshMedia"
              >
                <el-icon><Refresh /></el-icon> 刷新
              </el-button>
            </div>
            <div class="ai-status-list">
              <div
                class="ai-status-item"
                v-for="(status, type) in mediaLoginStatus"
                :key="type"
              >
                <div class="ai-platform">
                  <div class="platform-icon">
                    <img
                      :src="getMediaPlatformIcon(type)"
                      :alt="getMediaPlatformName(type)"
                    />
                  </div>
                  <div class="platform-name">
                    <span>
                      {{ getMediaPlatformName(type) }}
                    </span>
                    <el-tooltip
                      v-if="mediaIsLoading[type]"
                      content="正在登录中..."
                      placement="top"
                    >
                      <i class="el-icon-loading loading-icon"></i>
                    </el-tooltip>
                  </div>
                </div>
                <div class="status-action">
                  <el-tag
                    v-if="status"
                    type="success"
                    effect="dark"
                    class="status-tag"
                  >
                    <el-icon><SuccessFilled /></el-icon>
                    <span>{{ mediaAccounts[type] }}</span>
                  </el-tag>
                  <el-button
                    v-else
                    type="primary"
                    size="small"
                    :disabled="!mediaIsClick[type]"
                    @click="handleMediaLogin(type)"
                    :class="'media-login-btn'"
                    :title="'点击登录'"
                  >
                    <el-icon><Connection /></el-icon> <span class="btn-text">点击登录</span>
                  </el-button>
                </div>
              </div>
              <!-- 新增微信公众号登录项 -->
              <div class="ai-status-item">
                <div class="ai-platform">
                  <div class="platform-icon">
                    <img
                      src="@/assets/logo/wechat.png"
                      alt="微信公众号"
                    />
                  </div>
                  <div class="platform-name">微信公众号</div>
                </div>
                <div class="status-action">
                  <el-button
                    :type="form.appId ? 'warning' : 'primary'"
                    size="small"
                    class="login-btn"
                    @click="handleBindWechat"
                    :title="form.appId ? '修改信息' : '绑定公众号'"
                  >
                    <span class="btn-text">{{ form.appId ? '修改信息' : '绑定公众号' }}</span>
                  </el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- AI登录二维码对话框 -->
    <el-dialog
      :title="getAiLoginTitle"
      v-model="aiLoginDialogVisible"
      width="1200px"
      height="800px"
      center
      @close="handleAiLoginDialogClose"
    >
      <div class="qr-code-container" v-loading="!qrCodeUrl && !qrCodeError">
        <div v-if="qrCodeUrl" class="qr-code">
          <img
            style="width: 100%; height: 100%"
            :src="qrCodeUrl"
            alt="登录二维码"
          />
          <p class="qr-tip">请使用对应AI平台APP扫码登录</p>
        </div>
        <div v-else-if="qrCodeError" class="error-tip">
          <el-icon><Warning /></el-icon>
          <p>{{qrCodeError}}</p>
          <p class="error-hint">建议：关闭此窗口后重新打开登录</p>
          <el-button size="small" type="primary" @click="handleAiLoginDialogClose">关闭窗口</el-button>
        </div>
        <div v-else class="loading-tip">正在获取登录二维码...</div>
      </div>
    </el-dialog>

    <!-- 媒体登录二维码对话框 -->
    <el-dialog
      :title="getMediaLoginTitle"
      v-model="mediaLoginDialogVisible"
      width="1200px"
      height="800px"
      center
    >
      <div class="qr-code-container" v-loading="!mediaQrCodeUrl">
        <div v-if="mediaQrCodeUrl" class="qr-code">
          <img
            style="width: 100%; height: 100%"
            :src="mediaQrCodeUrl"
            alt="登录二维码"
          />
          <p class="qr-tip">{{ getQrTipText }}</p>
        </div>
        <div v-else class="loading-tip">正在获取登录二维码...</div>
      </div>
    </el-dialog>

    <el-dialog
      title="积分详细"
      v-model="openPointsRecord"
      width="1000px"
      append-to-body
    >
      <el-select
        v-model="queryPointForm.type"
        placeholder="积分类型"
        clearable
        style="width: 240px; margin-bottom: 10px"
        @change="getUserPointsRecord"
      >
        <el-option
          v-for="dict in changeType"
          :key="dict.value"
          :label="dict.label"
          :value="dict.value"
        />
      </el-select>
      <el-table v-loading="loading" :data="pointsRecordList">
        <el-table-column
          label="用户昵称"
          align="center"
          key="nick_name"
          prop="nick_name"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="变更数量"
          align="center"
          key="change_amount"
          prop="change_amount"
          :show-overflow-tooltip="true"
        >
          <template #default="scope">
            <span
              :style="{ color: scope.row.change_amount >= 0 ? 'green' : 'red' }"
            >
              {{ scope.row.change_amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          label="积分余额"
          align="center"
          key="balance_after"
          prop="balance_after"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="变更类型"
          align="center"
          key="change_type"
          prop="change_type"
        />
        <el-table-column
          width="200"
          label="变更时间"
          align="center"
          prop="create_time"
        >
          <template #default="scope">
            <span>{{ parseTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作人"
          align="center"
          key="create_name"
          prop="create_name"
        />
        <el-table-column
          label="备注"
          align="center"
          key="remark"
          prop="remark"
        />
      </el-table>
    <pagination
  v-show="pointtotal>0"
  :total="pointtotal"
  v-model:current-page="queryPointForm.page"
  v-model:page-size="queryPointForm.limit"
  @current-change="getUserPointsRecord"
  @size-change="getUserPointsRecord"
/>
    </el-dialog>
    <!-- 公众号配置弹窗 -->
    <el-dialog
      title="绑定微信公众号"
      v-model="dialogFormVisible"
      width="500px"
      append-to-body
    >
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="appId" :label-width="formLabelWidth" prop="appId">
          <el-input v-model="form.appId" maxlength="32" placeholder="请输入appId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="appSecret" :label-width="formLabelWidth" prop="appSecret">
          <el-input v-model="form.appSecret" maxlength="50" placeholder="请输入appSecret" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="公众号名称" :label-width="formLabelWidth" prop="officeAccountName">
          <el-input v-model="form.officeAccountName" maxlength="50" placeholder="请输入公众号名称" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="素材封面图" :label-width="formLabelWidth" prop="picUrl">
          <image-upload v-model="form.picUrl" />
        </el-form-item>
        <el-form-item label="规范说明" :label-width="formLabelWidth">
          <div style="color: #f56c6c; font-size: 13px">
            请把当前后台IP添加到公众号IP白名单。步骤：登录微信公众平台→点击设置与开发→安全中心→IP白名单。一般一小时后生效。
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBind">确 定</el-button>
      </div>
    </el-dialog>
    
    <!-- 主机ID提醒弹窗 -->
    <el-dialog
      title="温馨提示"
      v-model="corpIdReminderVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="true"
      append-to-body
    >
      <div style="text-align: center; padding: 20px 0;">
        <el-icon style="font-size: 48px; color: #E6A23C; margin-bottom: 16px;">
          <Warning />
        </el-icon>
        <p style="font-size: 16px; margin-bottom: 16px;">您尚未设置主机ID</p>
        <p style="color: #909399; font-size: 14px;">
          主机ID是使用系统功能的重要标识，设置后可以获得更好的使用体验
        </p>
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: center;">
        <el-button @click="closeCorpIdReminder">稍后设置</el-button>
        <el-button type="primary" @click="goToCorpIdSettings">立即设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ChatDotRound, Connection, Refresh, SuccessFilled, Warning, InfoFilled } from '@element-plus/icons-vue';

import PanelGroup from "./dashboard/PanelGroup";
import LineChart from "./dashboard/LineChart";
import RaddarChart from "./dashboard/RaddarChart";
import PieChart from "./dashboard/PieChart";
import BarChart from "./dashboard/BarChart";
import userInfo from "@/views/system/user/profile/userInfo";
import resetPwd from "@/views/system/user/profile/resetPwd";
import { getInfo } from "@/api/login";
import { parseTime } from "@/utils/ruoyi";
import { getUserProfile, getOfficeAccount, bindWcOfficeAccount } from "@/api/system/user";
import { getUserPointsRecord } from "@/api/wechat/company";
import { listUserAvailableAiagent } from "@/api/system/aiagent";
import websocketClient from "@/utils/websocket";
import { message } from "@/api/wechat/aigc";
import { getCorpId, ensureLatestCorpId, forceGetLatestCorpId } from "@/utils/corpId";

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145],
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130],
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130],
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130],
  },
};

export default {
  name: "Index",
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    userInfo,
    resetPwd,
    ChatDotRound,
    Connection,
    Refresh,
    SuccessFilled,
    Warning,
    InfoFilled,
  },
  data() {
    // 在data外部定义静态资源，避免HMR警告
    const defaultAvatar = require("@/assets/images/profile.jpg");
    
    return {
      lineChartData: lineChartData.newVisitis,
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      defaultAvatar, // 默认头像
      //------ 绑定公众号相关变量 ------//
      dialogFormVisible: false, // 绑定公众号弹窗
      dialogAgentFormVisible: false, // 绑定智能体弹窗
      dialogSpaceFormVisible: false, // 绑定元器空间弹窗
      form: {
        appId: "", // 公众号appId
        appSecret: "", // 公众号appSecret
        officeAccountName: "", // 公众号名称
        picUrl: "", // 公众号封面图
      },
      // 删除Agent和Space相关表单
      formLabelWidth: "120px", //输入框宽度
      // 绑定公众号表单验证规则
      rules: {
        appId: [{ required: true, message: "请输入appId", trigger: "blur" }],
        appSecret: [
          { required: true, message: "请输入appSecret", trigger: "blur" },
        ],
        officeAccountName: [
          { required: false, message: "请输入公众号名称", trigger: "blur" },
        ],
      },
      // 删除Agent和Space相关验证规则

      //------ 积分相关变量 ------//
      loading: true, // 遮罩层
      changeType: [
        {
          label: "全部",
          value: "0",
        },
        {
          label: "增加",
          value: "1",
        },
        {
          label: "消耗",
          value: "2",
        },
      ], // 积分明细表中的积分类型
      openPointsRecord: false, // 积分明细弹窗
      pointtotal: 0, // 积分明细总数
      queryPointForm: {
        limit: 10,
        page: 1,
        type: "",
        userId: "",
      }, // 积分明细查询需要的查询参数
      pointsRecordList: null, // 积分明细列表

      //------ 签到相关变量 ------//
      weekDays: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth() + 1,
      continuousSignInDays: 7,
      monthlySignInDays: 15,
      totalSignInDays: 128,
      todaySignedIn: false,
      signInHistory: [
        { date: "2024-01-15", time: "08:30:25" },
        { date: "2024-01-14", time: "09:15:33" },
        { date: "2024-01-13", time: "07:45:12" },
        { date: "2024-01-12", time: "08:20:45" },
        { date: "2024-01-11", time: "09:00:18" },
      ],
      signedDates: [
        new Date(2024, 0, 1),
        new Date(2024, 0, 2),
        new Date(2024, 0, 3),
        new Date(2024, 0, 4),
        new Date(2024, 0, 5),
      ],
      // 动态AI配置
      availableAiList: [], // 用户可用的AI列表
      aiLoginStatus: {},
      aiOnlineStatus: {}, // AI在线状态
      accounts: {},
      isClick: {},
      // 消息类型到agentCode的映射（动态构建，完全无硬编码）
      messageTypeMapping: {},
      aiLoginDialogVisible: false,
      currentAiType: "",
      qrCodeUrl: "",
      qrCodeError: "", // QR码获取错误信息
      // 消息相关变量
      messages: [],
      messageInput: "",
      isLoading: {},
      resetStatusTimeout: null, // 状态检查超时定时器

      //------ 媒体登录状态相关变量 ------//
      mediaLoginStatus: {
        // zhihu: false, // 已注释：知乎
        // baijiahao: false, // 已注释：百家号
        // toutiao: false, // 已注释：微头条
      },
      mediaAccounts: {
        // zhihu: "", // 已注释：知乎
        // baijiahao: "", // 已注释：百家号
        // toutiao: "", // 已注释：微头条
      },
      mediaIsClick: {
        // zhihu: false, // 已注释：知乎
        // baijiahao: false, // 已注释：百家号
        // toutiao: false, // 已注释：微头条
      },
      mediaIsLoading: {
        // zhihu: true, // 已注释：知乎
        // baijiahao: true, // 已注释：百家号
        // toutiao: true, // 已注释：微头条
      },
      mediaLoginDialogVisible: false,
      currentMediaType: "",
      mediaQrCodeUrl: "",
      resetMediaStatusTimeout: null, // 媒体状态检查超时定时器
      
      //------ 主机ID提醒相关变量 ------//
      corpIdReminderVisible: false, // 主机ID提醒弹窗是否可见
      corpIdReminderShown: false, // 是否已经显示过主机ID提醒，避免重复提醒
    }
  },

  // 计算当前月份的签到日期
  computed: {
    calendarDates() {
      const dates = [];
      const firstDay = new Date(this.currentYear, this.currentMonth - 1, 1);
      const lastDay = new Date(this.currentYear, this.currentMonth, 0);

      // Fill in empty slots before first day
      for (let i = 0; i < firstDay.getDay(); i++) {
        dates.push(null);
      }

      // Fill in days of the month
      for (let i = 1; i <= lastDay.getDate(); i++) {
        dates.push(new Date(this.currentYear, this.currentMonth - 1, i));
      }

      return dates;
    },
    getAiLoginTitle() {
      // 🔥 从数据库配置获取AI名称
      const ai = this.availableAiList.find(item => item.agentCode === this.currentAiType);
      return ai ? `${ai.agentName}登录` : '登录';
    },
    getMediaLoginTitle() {
      const titles = {
        // zhihu: "知乎登录", // 已注释：知乎
        // baijiahao: "百家号登录", // 已注释：百家号
        // toutiao: "微头条登录", // 已注释：微头条
      };
      return titles[this.currentMediaType] || "媒体登录";
    },
    getQrTipText() {
      return "请使用对应APP扫码登录";
    },
  },

  async created() {
    // 每次页面刷新时重置温馨提醒状态，确保每次都能触发检查
    // 使用sessionStorage而不是localStorage，确保只在当前会话中记住状态
    const sessionReminderShown = sessionStorage.getItem('corpIdReminderShown');
    this.corpIdReminderShown = sessionReminderShown === 'true';
    
    // 确保主机ID是最新的
    try {
      await ensureLatestCorpId();
    } catch (error) {
    }
    
    this.getUser();
  },
  mounted() {
    // 监听企业ID自动更新事件
    window.addEventListener('corpIdUpdated', this.handleCorpIdUpdated);
  },
  methods: {
    // 🔥 保存登录状态到本地存储，供主机页面同步
    saveLoginStatusToStorage() {
      try {
        localStorage.setItem('aiLoginStatus', JSON.stringify(this.aiLoginStatus));
        localStorage.setItem('aiAccounts', JSON.stringify(this.accounts));
      } catch (error) {
      }
    },
    
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type];
    },
    async getUser() {
      try {
        const response = await getUserProfile();
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
        this.userId = response.data.userId;
        
        // 使用企业ID工具确保获取最新的企业ID
        try {
          // 强制从服务器获取最新的企业ID，确保与数据库一致
          this.corpId = await forceGetLatestCorpId();
        } catch (error) {
          this.corpId = response.data.corpId;
        }
        
        // 每次页面刷新时都检查主机ID状态，确保温馨提醒能够正常显示
        this.checkCorpIdStatus();

        // 加载用户可用的AI配置
        await this.loadAvailableAiList();

        // 初始化媒体登录状态
        // this.mediaIsClick.zhihu = false; // 已注释：知乎
        // this.mediaIsClick.baijiahao = false; // 已注释：百家号
        // this.mediaIsClick.toutiao = false; // 已注释：微头条

        // this.mediaIsLoading.zhihu = true; // 已注释：知乎
        // this.mediaIsLoading.baijiahao = true; // 已注释：百家号
        // this.mediaIsLoading.toutiao = true; // 已注释：微头条

        this.initWebSocket(this.userId); // 创建时建立连接

        // 延迟检查AI登录状态
        setTimeout(() => {
          this.checkAllAiLoginStatus();
        }, 1000);

        // 页面加载时自动获取公众号信息，刷新按钮状态
        getOfficeAccount().then((response) => {
          if (response.data != null) {
            this.form.appId = response.data.appId;
            this.form.appSecret = response.data.appSecret;
            this.form.officeAccountName = response.data.officeAccountName;
            this.form.picUrl = response.data.picUrl;
          } else {
            this.form.appId = '';
            this.form.appSecret = '';
            this.form.officeAccountName = '';
            this.form.picUrl = '';
          }
        });
      } catch (error) {
        // 静默处理
      }
    },
    // 获取公众号信息
    handleBindWechat() {
      getOfficeAccount().then((response) => {
        if (response.data != null) {
          this.form.appId = response.data.appId;
          this.form.appSecret = response.data.appSecret;
          this.form.officeAccountName = response.data.officeAccountName;
          this.form.picUrl = response.data.picUrl;
        }
        this.dialogFormVisible = true;
      });
    },
    // 绑定公众号
    confirmBind() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 表单验证通过，继续提交
          bindWcOfficeAccount(this.form).then((response) => {
            if (response.code === 200) {
              this.$message.success("绑定成功！");
              this.dialogFormVisible = false;
              // 刷新公众号信息，更新按钮状态
              getOfficeAccount().then((res) => {
                if (res.data != null) {
                  this.form.appId = res.data.appId;
                  this.form.appSecret = res.data.appSecret;
                  this.form.officeAccountName = res.data.officeAccountName;
                  this.form.picUrl = res.data.picUrl;
                }
              });
            } else {
              this.$message.error(response.msg || "绑定失败，请重试");
            }
          }).catch((error) => {
            console.error("绑定公众号失败:", error);
            this.$message.error(error.msg || "绑定失败，请重试");
          });
        } else {
          // 表单验证失败
          return false;
        }
      });
    },
    // 获取当前登录用户积分明细
    showPointsDetail() {
      this.queryPointForm.userId = this.user.userId;
      this.queryPointForm.page = 1; // 重置页码
      this.queryPointForm.type = ''; // 重置分类
      this.getUserPointsRecord();
    },
    // 获取积分明细
    getUserPointsRecord() {
      this.loading = true;
      // 构建请求参数，将 type 转换为正确的格式
      const requestData = {
        userId: this.queryPointForm.userId,
        page: this.queryPointForm.page,
        limit: this.queryPointForm.limit,
        type: this.queryPointForm.type === '' || this.queryPointForm.type === '0' ? null : parseInt(this.queryPointForm.type)
      };
      getUserPointsRecord(requestData).then((response) => {
        this.openPointsRecord = true;
        this.pointsRecordList = response.data.list;
        this.pointtotal = response.data.total;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    // 加载用户可用的AI列表
    async loadAvailableAiList() {
      try {
        const response = await listUserAvailableAiagent();
        this.availableAiList = response.data || [];
        
        
        // 初始化AI状态对象和消息映射
        this.aiLoginStatus = {};
        this.aiOnlineStatus = {};
        this.accounts = {};
        this.isClick = {};
        this.isLoading = {};
        this.messageTypeMapping = {}; // 清空映射
        
        // 为每个可用的AI初始化状态并构建消息映射
        this.availableAiList.forEach(ai => {
          const code = ai.agentCode;
          // 初始化AI配置
          
          // 使用$set确保响应式（Vue 2兼容），Vue 3会自动处理
          this.$set ? this.$set(this.aiLoginStatus, code, false) : this.aiLoginStatus[code] = false;
          this.$set ? this.$set(this.aiOnlineStatus, code, ai.onlineStatus === 1) : this.aiOnlineStatus[code] = ai.onlineStatus === 1;
          this.$set ? this.$set(this.accounts, code, "") : this.accounts[code] = "";
          this.$set ? this.$set(this.isClick, code, false) : this.isClick[code] = false;
          this.$set ? this.$set(this.isLoading, code, true) : this.isLoading[code] = true;
          
          // 🔥 动态构建消息类型映射（避免硬编码）
          // 从 PLAY_CHECK_YB_LOGIN 转换为 RETURN_YB_STATUS
          if (ai.websocketCheckType) {
            const returnType = ai.websocketCheckType.replace('PLAY_CHECK_', 'RETURN_').replace('_LOGIN', '_STATUS');
            this.messageTypeMapping[returnType] = code;
            // 消息映射配置
          } else {
          }
        });
        
        // AI配置加载完成
      } catch (error) {
        this.$message.error('加载AI列表失败，请刷新页面重试');
        this.availableAiList = [];
      }
    },
    // 检查所有AI的登录状态
    checkAllAiLoginStatus() {
      this.availableAiList.forEach(ai => {
        // 🔥 只检查在线且启用的AI
        if (ai.websocketCheckType && ai.agentStatus === 1 && ai.onlineStatus === 1) {
          // 检查登录状态
          this.sendMessage({
            type: ai.websocketCheckType,
            userId: this.userId,
            corpId: this.corpId,
          });
        } else if (ai.agentStatus === 0) {
        } else if (ai.onlineStatus === 0) {
        }
      });
    },
    // 获取当前月份的签到日期
    isSignedDate(date) {
      if (!date) return false;
      return this.signedDates.some(
        (signedDate) =>
          signedDate.getDate() === date.getDate() &&
          signedDate.getMonth() === date.getMonth() &&
          signedDate.getFullYear() === date.getFullYear()
      );
    },
    isToday(date) {
      if (!date) return false;
      const today = new Date();
      return (
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()
      );
    },
    isFutureDate(date) {
      if (!date) return false;
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      return date > today;
    },
    handleSignIn() {
      if (!this.todaySignedIn) {
        this.todaySignedIn = true;
        this.signedDates.push(new Date());
        const now = new Date();
        this.signInHistory.unshift({
          date: `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(
            2,
            "0"
          )}-${String(now.getDate()).padStart(2, "0")}`,
          time: `${String(now.getHours()).padStart(2, "0")}:${String(
            now.getMinutes()
          ).padStart(2, "0")}:${String(now.getSeconds()).padStart(2, "0")}`,
        });
        this.monthlySignInDays++;
        this.totalSignInDays++;
      }
    },
    handleAiLogin(type) {
      // 🔥 修复截图传台问题：切换登录时先清理之前的登录会话
      if (this.currentAiType && this.currentAiType !== type) {
        this.cleanupPreviousLogin();
      }
      
      this.currentAiType = type;
      this.aiLoginDialogVisible = true;
      this.isLoading[type] = true;
      this.isClick[type] = false;
      // 重置二维码状态
      this.qrCodeUrl = "";
      this.qrCodeError = "";
      this.getQrCode(type);
    },
    
    // 🔥 新增：处理登录对话框关闭事件
    handleAiLoginDialogClose() {
      // 关闭登录对话框
      
      // 1. 先清理后端登录会话
      this.cleanupPreviousLogin();
      
      // 2. 重置前端登录状态
      this.resetLoginState();
      
      // 3. 关闭对话框
      this.aiLoginDialogVisible = false;
      // 对话框关闭完成
    },
    
    // 🔥 新增：清理之前的登录会话
    cleanupPreviousLogin() {
      if (this.currentAiType) {
        // 清理登录会话
        
        const cleanupMessage = {
          type: "CLEANUP_LOGIN_SESSION",
          userId: this.userId,
          aiType: this.currentAiType,
          corpId: this.corpId,
        };
        
        this.sendMessage(cleanupMessage);
        // 清理消息已发送
      } else {
      }
    },
    
    // 🔥 新增：重置登录状态
    resetLoginState() {
      // 重置登录状态
      if (this.currentAiType) {
        this.isLoading[this.currentAiType] = false;
        this.isClick[this.currentAiType] = true;
      }
      // 清空状态数据
      this.qrCodeUrl = "";
      this.qrCodeError = "";
      this.currentAiType = null;
      // 状态重置完成
    },
    getQrCode(agentCode) {
      this.qrCodeUrl = "";
      
      // 🔥 从数据库配置中查找AI的websocket二维码类型
      const ai = this.availableAiList.find(item => item.agentCode === agentCode);
      
      if (!ai) {
        this.$message.error('AI配置错误，请刷新页面重试');
        return;
      }
      
      if (!ai.websocketQrcodeType) {
        this.$message.error(`${ai.agentName}未配置二维码获取接口`);
        return;
      }
      
      
      // 动态发送消息
      this.sendMessage({
        type: ai.websocketQrcodeType,
        userId: this.userId,
        corpId: this.corpId,
      });
      
      this.$message({
        message: `正在获取${ai.agentName}登录二维码...`,
        type: "info",
      });
    },
    getPlatformIcon(type) {
      // 从数据库配置获取
      const ai = this.availableAiList.find(item => item.agentCode === type);
      return (ai && ai.agentIcon) ? ai.agentIcon : "";
    },
    getPlatformName(type) {
      // 从数据库配置获取
      const ai = this.availableAiList.find(item => item.agentCode === type);
      return (ai && ai.agentName) ? ai.agentName : "";
    },

    // 媒体登录相关方法
    handleRefreshMedia() {
      // 已注释：重置媒体登录状态
      // this.mediaIsClick.zhihu = false; // 已注释：知乎
      // this.mediaIsClick.baijiahao = false; // 已注释：百家号
      // this.mediaIsClick.toutiao = false; // 已注释：微头条
      // this.mediaIsLoading.zhihu = true; // 已注释：知乎
      // this.mediaIsLoading.baijiahao = true; // 已注释：百家号
      // this.mediaIsLoading.toutiao = true; // 已注释：微头条

      // 已注释：检查知乎媒体登录状态
      // this.sendMessage({
      //   type: "PLAY_CHECK_ZHIHU_MEDIA_LOGIN",
      //   userId: this.userId,
      //   corpId: this.corpId,
      // });
      // 已注释：检查微头条登录状态
      // this.sendMessage({
      //   type: "PLAY_CHECK_TTH_LOGIN",
      //   userId: this.userId,
      //   corpId: this.corpId,
      // });
      // 已注释：检查百家号登录状态
      // this.sendMessage({
      //   type: "PLAY_CHECK_BAIJIAHAO_LOGIN",
      //   userId: this.userId,
      //   corpId: this.corpId,
      // });
    },

    handleMediaLogin(type) {
      this.currentMediaType = type;
      this.mediaLoginDialogVisible = true;
      this.mediaIsLoading[type] = true;
      this.mediaIsClick[type] = false;
      // 重置二维码状态
      this.mediaQrCodeUrl = "";
      this.getMediaQrCode(type);
    },

    getMediaQrCode(type) {
      this.mediaQrCodeUrl = "";
      // 已注释：知乎媒体
      // if (type === "zhihu") {
      //   this.sendMessage({
      //     type: "PLAY_GET_ZHIHU_MEDIA_QRCODE",
      //     userId: this.userId,
      //     corpId: this.corpId,
      //   });
      // } 
      // 已注释：微头条
      // else if (type === "toutiao") {
      //   this.sendMessage({
      //     type: "PLAY_GET_TTH_QRCODE",
      //     userId: this.userId,
      //     corpId: this.corpId,
      //   });
      // } 
      // 已注释：百家号
      // else if (type === "baijiahao") {
      //   this.sendMessage({
      //     type: "PLAY_GET_BAIJIAHAO_QRCODE",
      //     userId: this.userId,
      //     corpId: this.corpId,
      //   });
      // }
      this.$message({
        message: "正在获取登录二维码...",
        type: "info",
      });
    },

    getMediaPlatformIcon(type) {
      const icons = {
        // zhihu: require("@/assets/logo/ZhiHu.png"), // 已注释：知乎
        // baijiahao: require("@/assets/logo/baijiahao.png"), // 已注释：百家号
        // toutiao: require("@/assets/logo/toutiao.png"), // 已注释：微头条
      };
      return icons[type] || "";
    },

    getMediaPlatformName(type) {
      const names = {
        // zhihu: "知乎", // 已注释：知乎
        // baijiahao: "百家号", // 已注释：百家号
        // toutiao: "微头条", // 已注释：微头条
      };
      return names[type] || "";
    },

    resetMediaLoginStates() {
      // this.mediaIsClick.zhihu = false; // 已注释：知乎
      // this.mediaIsClick.baijiahao = false; // 已注释：百家号
      // this.mediaIsClick.toutiao = false; // 已注释：微头条
      // this.mediaIsLoading.zhihu = true; // 已注释：知乎
      // this.mediaIsLoading.baijiahao = true; // 已注释：百家号
      // this.mediaIsLoading.toutiao = true; // 已注释：微头条
    },

    // WebSocket 相关方法
    initWebSocket(id) {
      const wsUrl = process.env.VUE_APP_WS_API + `mypc-${id}`;
      websocketClient.connect(wsUrl, (event) => {
        switch (event.type) {
          case "open":
            break;
          case "message":
            this.handleWebSocketMessage(event.data);
            break;
          case "close":
            this.$message.warning("WebSocket连接已关闭");
            break;
          case "error":
            this.$message.error("WebSocket连接错误");
            break;
          case "reconnect_failed":
            this.$message.error("WebSocket重连失败，请刷新页面重试");
            break;
        }
      });
    },

    handleWebSocketMessage(data) {
      const datastr = data;
      const dataObj = JSON.parse(datastr);

      if (
        datastr.includes("RETURN_PC_YB_QRURL") ||
        datastr.includes("RETURN_PC_DB_QRURL") ||
        datastr.includes("RETURN_PC_BAIDU_QRURL") ||
        datastr.includes("RETURN_PC_DEEPSEEK_QRURL") ||
        datastr.includes("RETURN_PC_QW_QRURL") ||
        datastr.includes("RETURN_PC_METASO_QRURL") ||
        datastr.includes("RETURN_PC_ZHZD_QRURL")
      ) {
        // 🔥 防串台：提取消息中的AI类型
        let messageAiType = null;
        if (datastr.includes("RETURN_PC_YB_QRURL")) messageAiType = "yb";
        else if (datastr.includes("RETURN_PC_DB_QRURL")) messageAiType = "zj-db";
        else if (datastr.includes("RETURN_PC_BAIDU_QRURL")) messageAiType = "baidu-agent";
        else if (datastr.includes("RETURN_PC_DEEPSEEK_QRURL")) messageAiType = "deepseek";
        else if (datastr.includes("RETURN_PC_QW_QRURL")) messageAiType = "qwen";
        else if (datastr.includes("RETURN_PC_METASO_QRURL")) messageAiType = "mita";
        else if (datastr.includes("RETURN_PC_ZHZD_QRURL")) messageAiType = "zhzd-chat";
        
        // 🔥 防串台：只处理当前打开对话框的AI的二维码
        if (!this.aiLoginDialogVisible) {
          // 对话框已关闭，忽略二维码
          return;
        }
        
        if (messageAiType && this.currentAiType && messageAiType !== this.currentAiType) {
          // AI类型不匹配，忽略
          return; // 忽略其他AI的二维码，防止串台
        }
        
        if (dataObj.url && dataObj.url.trim() !== "") {
          // 接收二维码
          this.qrCodeUrl = dataObj.url;
          this.qrCodeError = ""; // 清除错误信息
        } else if (dataObj.error) {
          // 会话失效或超时：显示友好提示
          this.$message({
            message: '登录超时，请重新尝试',
            type: 'warning',
            duration: 3000
          });
          this.aiLoginDialogVisible = false;
          this.resetLoginState(this.currentAiType);
          return;
        }
      }
      
      // 🔥 通用AI状态更新（动态处理，避免硬编码）
      // 检查是否是AI状态返回消息（RETURN_*_STATUS格式）
      const statusMessageMatch = datastr.match(/RETURN_(\w+)_STATUS/);
      if (statusMessageMatch && dataObj.status !== undefined && dataObj.status !== null) {
        const messageType = statusMessageMatch[0]; // 例如：RETURN_YB_STATUS
        
        const agentCode = this.messageTypeMapping[messageType]; // 从映射表查找agentCode
        
        if (agentCode) {
          // AI状态消息映射
          
          if (!datastr.includes("false") && dataObj.status !== "false" && dataObj.status !== "") {
            // 登录成功
            this.aiLoginDialogVisible = false;
            // 使用$set确保响应式更新
            this.$set ? this.$set(this.aiLoginStatus, agentCode, true) : this.aiLoginStatus[agentCode] = true;
            this.$set ? this.$set(this.accounts, agentCode, dataObj.status) : this.accounts[agentCode] = dataObj.status;
            this.$set ? this.$set(this.isLoading, agentCode, false) : this.isLoading[agentCode] = false;
            this.$set ? this.$set(this.isClick, agentCode, true) : this.isClick[agentCode] = true;
            
            // AI登录成功
            
            // 🔥 保存登录状态到本地存储，供主机页面同步
            this.saveLoginStatusToStorage();
            
            // 检查是否所有AI都已检测完成
            const allChecked = this.availableAiList.every(ai => !this.isLoading[ai.agentCode]);
            if (allChecked && this.resetStatusTimeout) {
              clearTimeout(this.resetStatusTimeout);
              // 所有AI状态检测完成
            }
          } else {
            // 未登录或登录失败
            this.$set ? this.$set(this.isClick, agentCode, true) : this.isClick[agentCode] = true;
            this.$set ? this.$set(this.isLoading, agentCode, false) : this.isLoading[agentCode] = false;
          }
          
          return; // 已处理，直接返回
        } else {
        }
      }
      
      // 已注释：媒体登录二维码处理
      // else if (
      //   datastr.includes("RETURN_PC_ZHIHU_MEDIA_QRURL") ||
      //   datastr.includes("RETURN_PC_TTH_QRURL") ||
      //   datastr.includes("RETURN_PC_BAIJIAHAO_QRURL")
      // ) {
      //   if (dataObj.url && dataObj.url.trim() !== "") {
      //     this.mediaQrCodeUrl = dataObj.url;
      //   } else if (dataObj.error) {
      //     this.$message.error(dataObj.error);
      //     this.mediaQrCodeUrl = "";
      //   } else {
      //     this.$message.error("获取媒体登录二维码失败，请重试");
      //     this.mediaQrCodeUrl = "";
      //   }
      // }
      
      // ⚠️ 以下是旧的硬编码逻辑，保留作为备用（通用逻辑已处理）
      // 🔥 旧的硬编码逻辑已移除，使用通用的动态处理逻辑（上面的statusMessageMatch处理）
      // 已注释：微头条状态返回
      // else if(datastr.includes("RETURN_TOUTIAO_STATUS") && dataObj.status != ""){
      //   if (!datastr.includes("false")) {
      //     this.mediaLoginDialogVisible = false;
      //     // 媒体登录状态（头条号只是媒体平台）
      //     this.mediaLoginStatus.toutiao = true;
      //     this.mediaAccounts.toutiao = dataObj.status;
      //     this.mediaIsLoading.toutiao = false;
      //     this.mediaIsClick.toutiao = true;
      //     // 检查媒体状态恢复
      //     if (!this.mediaIsLoading.zhihu && !this.mediaIsLoading.baijiahao && !this.mediaIsLoading.toutiao) {
      //       if (this.resetMediaStatusTimeout) clearTimeout(this.resetMediaStatusTimeout);
      //     }
      //   } else {
      //     this.mediaIsClick.toutiao = true;
      //     this.mediaIsLoading.toutiao = false;
      //   }
      // }
      // 已注释：媒体登录状态处理
      // else if (datastr.includes("RETURN_ZHIHU_MEDIA_STATUS") && dataObj.status != "") {
      //   if (!datastr.includes("false")) {
      //     this.mediaLoginDialogVisible = false;
      //     this.mediaLoginStatus.zhihu = true;
      //     this.mediaAccounts.zhihu = dataObj.status;
      //     this.mediaIsLoading.zhihu = false;
      //     this.mediaIsClick.zhihu = true;
      //     // 检查是否所有媒体都已恢复
      //     if (!this.mediaIsLoading.zhihu && !this.mediaIsLoading.baijiahao && !this.mediaIsLoading.toutiao) {
      //       if (this.resetMediaStatusTimeout) clearTimeout(this.resetMediaStatusTimeout);
      //     }
      //   } else {
      //     this.mediaIsClick.zhihu = true;
      //     this.mediaIsLoading.zhihu = false;
      //   }
      // } 
      // 已注释：微头条状态返回
      // else if (datastr.includes("RETURN_TTH_STATUS") && dataObj.status != "") {
      //   if (!datastr.includes("false")) {
      //     this.mediaLoginDialogVisible = false;
      //     this.mediaLoginStatus.toutiao = true;
      //     this.mediaAccounts.toutiao = dataObj.status;
      //     this.mediaIsLoading.toutiao = false;
      //     this.mediaIsClick.toutiao = true;
      //     // 检查是否所有媒体都已恢复
      //     if (!this.mediaIsLoading.zhihu && !this.mediaIsLoading.baijiahao && !this.mediaIsLoading.toutiao) {
      //       if (this.resetMediaStatusTimeout) clearTimeout(this.resetMediaStatusTimeout);
      //     }
      //   } else {
      //     this.mediaIsClick.toutiao = true;
      //     this.mediaIsLoading.toutiao = false;
      //   }
      // } 
      // 已注释：百家号状态返回
      // else if (datastr.includes("RETURN_BAIJIAHAO_STATUS") && dataObj.status != "") {
      //   if (!datastr.includes("false")) {
      //     this.mediaLoginDialogVisible = false;
      //     this.mediaLoginStatus.baijiahao = true;
      //     this.mediaAccounts.baijiahao = dataObj.status;
      //     this.mediaIsLoading.baijiahao = false;
      //     this.mediaIsClick.baijiahao = true;
      //     // 检查是否所有媒体都已恢复
      //     if (!this.mediaIsLoading.zhihu && !this.mediaIsLoading.baijiahao && !this.mediaIsLoading.toutiao) {
      //       if (this.resetMediaStatusTimeout) clearTimeout(this.resetMediaStatusTimeout);
      //     }
      //   } else {
      //     this.mediaIsClick.baijiahao = true;
      //     this.mediaIsLoading.baijiahao = false;
      //   }
      // }
    },

    closeWebSocket() {
      websocketClient.close();
    },

    sendMessage(data) {
      if (websocketClient.send(data)) {
        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      } else {
        this.$message.error("WebSocket未连接");
      }
    },

    // 格式化时间
    formatTime(date) {
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      const seconds = String(date.getSeconds()).padStart(2, "0");
      return `${hours}:${minutes}:${seconds}`;
    },

    // 滚动到底部
    scrollToBottom() {
      const messageList = this.$refs.messageList;
      if (messageList) {
        messageList.scrollTop = messageList.scrollHeight;
      }
    },
    async handleRefreshAI() {
      // 首先确保企业ID最新
      try {
        const result = await ensureLatestCorpId();
        if (result.corpId !== this.corpId) {
          this.corpId = result.corpId;
        }
      } catch (error) {
        // 静默处理
      }
      
      if (!this.userId || !this.corpId) return;
      
      // 🔥 动态重置AI状态（从数据库配置获取）
      this.availableAiList.forEach(ai => {
        const code = ai.agentCode;
        this.$set(this.isLoading, code, true);
        this.$set(this.isClick, code, false);
      });
      // 清除上一次的超时定时器
      if (this.resetStatusTimeout) clearTimeout(this.resetStatusTimeout);
      // 超时自动恢复（2分半钟）
      this.resetStatusTimeout = setTimeout(() => {
        // 🔥 动态设置超时，所有AI都恢复为可点击状态
        this.availableAiList.forEach(ai => {
          const code = ai.agentCode;
          this.$set(this.isLoading, code, false);
          this.$set(this.isClick, code, true);
        });
        // AI状态超时，已恢复

        this.$message.warning('AI登录状态刷新超时，请检查网络或稍后重试');
      }, 150000);
      
      // 🔥 只检测在线且启用的AI登录状态
      this.availableAiList.forEach(ai => {
        // 检查AI状态
        if (ai.websocketCheckType && ai.agentStatus === 1 && ai.onlineStatus === 1) {
          this.sendMessage({
            type: ai.websocketCheckType,
            userId: this.userId,
            corpId: this.corpId
          });
        } else {
          let reason = [];
          if (!ai.websocketCheckType) reason.push('无checkType');
          if (ai.agentStatus === 0) reason.push('已禁用');
          if (ai.onlineStatus === 0) reason.push('已离线');
        }
      });

    },
    async handleRefreshMedia() {
      // 首先确保企业ID最新
      try {
        const result = await ensureLatestCorpId();
        if (result.corpId !== this.corpId) {
          this.corpId = result.corpId;
        }
      } catch (error) {
        // 静默处理
      }
      
      if (!this.userId || !this.corpId) return;
      // 已注释：只重置媒体相关状态
      // this.mediaIsLoading.zhihu = true; // 已注释：知乎
      // this.mediaIsLoading.toutiao = true; // 已注释：微头条
      // this.mediaIsClick.zhihu = false; // 已注释：知乎
      // this.mediaIsClick.toutiao = false; // 已注释：微头条
      // this.mediaIsLoading.baijiahao = true; // 已注释：百家号
      // this.mediaIsClick.baijiahao = false; // 已注释：百家号
      // 清除上一次的超时定时器
      if (this.resetMediaStatusTimeout) clearTimeout(this.resetMediaStatusTimeout);
      // 超时自动恢复（2分钟）
      this.resetMediaStatusTimeout = setTimeout(() => {
        // this.mediaIsLoading.zhihu = false; // 已注释：知乎
        // this.mediaIsLoading.toutiao = false; // 已注释：微头条
        // this.mediaIsClick.zhihu = true; // 已注释：知乎
        // this.mediaIsClick.toutiao = true; // 已注释：微头条
        // this.mediaIsLoading.baijiahao = false; // 已注释：百家号
        // this.mediaIsClick.baijiahao = true; // 已注释：百家号
        this.$message.warning('媒体登录状态刷新超时，请检查网络或稍后重试');
      }, 120000);
      // 已注释：只检测媒体相关登录状态
      // 已注释：检测知乎媒体状态
      // this.sendMessage({ type: "PLAY_CHECK_ZHIHU_MEDIA_LOGIN", userId: this.userId, corpId: this.corpId }); // 已注释：知乎
      // this.sendMessage({ type: "PLAY_CHECK_TTH_LOGIN", userId: this.userId, corpId: this.corpId }); // 已注释：微头条
      // this.sendMessage({ type: "PLAY_CHECK_BAIJIAHAO_LOGIN", userId: this.userId, corpId: this.corpId }); // 已注释：百家号
    },
    // 重试获取二维码
    retryGetQrCode() {
      this.qrCodeError = "";
      this.qrCodeUrl = "";
      this.getQrCode(this.currentAiType);
    },
    // 处理企业ID更新事件
    handleCorpIdUpdated(event) {
      const newCorpId = event.detail.corpId;
      if (newCorpId && newCorpId !== this.corpId) {
        this.corpId = newCorpId;
      }
    },
    
    // 处理主机ID更新事件
    handleCorpIdUpdated(event) {
      const { corpId, oldCorpId } = event.detail;
      
      // 更新本地主机ID
      this.corpId = corpId;
      
      // 只有在主机ID从无到有时才检查状态，避免重复提醒
      if (!oldCorpId && corpId) {
        // 主机ID已设置，清除提醒状态
        this.corpIdReminderShown = true;
        sessionStorage.setItem('corpIdReminderShown', 'true');
      }
    },
    
    // 检查主机ID状态
    checkCorpIdStatus() {
      // 检查是否有主机ID
      if (!this.corpId || this.corpId.trim() === '') {
        // 延迟显示提醒，避免与其他弹窗冲突
        setTimeout(() => {
          // 使用$alert而不是自定义弹窗，确保显示
          this.$alert(
            '您的主机ID（企业ID）尚未设置，这可能会影响部分功能的使用。您可以随时在顶部导航栏或个人资料页面进行设置。',
            '温馨提示',
            {
              confirmButtonText: '我知道了',
              type: 'info',
              center: true,
              customClass: 'corp-id-reminder-dialog',
              showClose: false,
              callback: action => {
                // 用户点击确认后，记录已显示过提醒
                this.corpIdReminderShown = true;
                // 将状态保存到sessionStorage，确保页面刷新后仍然有效
                sessionStorage.setItem('corpIdReminderShown', 'true');
              }
            }
          );
        }, 2000);
      } else {
        // 如果有主机ID，确保重置提醒状态，下次刷新时可以再次检查
        this.corpIdReminderShown = true;
        sessionStorage.setItem('corpIdReminderShown', 'true');
      }
    },
    
    // 关闭主机ID提醒弹窗
    closeCorpIdReminder() {
      this.corpIdReminderVisible = false;
    },
    
    // 跳转到主机ID设置页面
    goToCorpIdSettings() {
      this.corpIdReminderVisible = false;
      // 这里可以跳转到设置主机ID的页面
    },
  },
  beforeUnmount() {
    // 移除事件监听
    window.removeEventListener('corpIdUpdated', this.handleCorpIdUpdated);
    this.closeWebSocket(); // 销毁时关闭连接
  },
};
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 24px;
  background: #f5f7fa;
  position: relative;
  min-height: calc(100vh - 60px);

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }

  .app-container {
    max-width: 1600px;
    margin: 0 auto;
  }

  .box-card {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 21, 41, 0.08);
    background: #fff;
    transition: all 0.3s ease;
    height: 100%;
    
    &:hover {
      box-shadow: 0 4px 20px rgba(0, 21, 41, 0.12);
      transform: translateY(-2px);
    }
    
    :deep(.el-card__header) {
      padding: 16px 20px;
      border-bottom: 1px solid #ebeef5;
      background: linear-gradient(to right, #fafbfc, #fff);
    }
    
    .clearfix {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      padding-bottom: 0;
      border-bottom: none;
      margin-bottom: 16px;
    }
    
    .text-center {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 20px;
      padding: 8px 0;
      
      .user-avatar-display {
        border-radius: 50%;
        border: 3px solid #fff;
        box-shadow: 0 4px 16px rgba(64, 158, 255, 0.15);
        width: 90px;
        height: 90px;
        object-fit: cover;
        cursor: default;
        transition: all 0.3s ease;
        
        &:hover {
          box-shadow: 0 6px 20px rgba(64, 158, 255, 0.25);
        }
      }
    }
    
    .list-group {
      padding: 0;
      margin: 0;
      list-style: none;
      
      .list-group-item {
        display: flex;
        align-items: center;
        padding: 14px 12px;
        border-bottom: 1px solid #f5f7fa;
        font-size: 14px;
        color: #606266;
        transition: all 0.3s ease;
        border-radius: 6px;
        margin-bottom: 4px;
        
        .svg-icon {
          margin-right: 12px;
          color: #409eff;
          font-size: 16px;
          width: 20px;
          flex-shrink: 0;
        }
        
        .pull-right {
          margin-left: auto;
          font-weight: 500;
          color: #303133;
        }
        
        &:hover {
          background: linear-gradient(to right, rgba(64, 158, 255, 0.05), transparent);
          border-color: transparent;
        }
        
        &:last-child {
          border-bottom: none;
        }
      }
    }
    
    #userName {
      font-weight: 600;
      color: #409eff;
      font-size: 16px;
    }
    
    // 积分项特殊样式
    .points-item {
      cursor: pointer;
      position: relative;
      
      &:hover {
        background: linear-gradient(to right, rgba(103, 194, 58, 0.05), transparent) !important;
        
        .info-icon {
          color: #409eff;
          transform: scale(1.1);
        }
      }
      
      .points-value {
        font-weight: 600;
        font-size: 15px;
      }
      
      .info-icon {
        margin-left: 6px;
        color: #909399;
        font-size: 16px;
        transition: all 0.3s ease;
        cursor: help;
      }
    }
  }
}

// 响应式布局优化
@media (max-width: 1400px) {
  .dashboard-editor-container {
    padding: 20px;
  }
  
  .ai-status-card {
    .ai-status-list {
      .ai-status-item {
        padding: 12px 10px;
        
        .ai-platform {
          .platform-icon {
            width: 32px;
            height: 32px;
            margin-right: 10px;
          }
          
          .platform-name {
            font-size: 13px;
          }
        }
        
        .status-action {
          .status-tag,
          .login-btn {
            font-size: 12px;
            padding: 6px 12px;
          }
        }
      }
    }
  }
}

@media (max-width: 1200px) {
  .dashboard-editor-container {
    padding: 16px;
    
    .box-card {
      .list-group-item {
        font-size: 13px;
        padding: 12px 10px;
      }
    }
  }
  
  .ai-status-card {
    .ai-status-list {
      .ai-status-item {
        flex-wrap: wrap;
        
        .ai-platform {
          flex: 1;
          min-width: 120px;
        }
        
        .status-action {
          margin-top: 8px;
          margin-left: 42px;
        }
      }
    }
  }
}

@media (max-width: 992px) {
  .dashboard-editor-container {
    .ai-status-card {
      .ai-status-list {
        .ai-status-item {
          .platform-name {
            .platform-text {
              display: none;
            }
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .dashboard-editor-container {
    padding: 12px;
    
    .app-container {
      :deep(.el-row) {
        margin: 0 !important;
        
        .el-col {
          padding: 0 !important;
          margin-bottom: 12px;
        }
      }
    }
    
    .box-card {
      .text-center .user-avatar-display {
        width: 70px;
        height: 70px;
      }
      
      .list-group-item {
        font-size: 12px;
        padding: 10px 8px;
        
        .svg-icon {
          font-size: 14px;
          margin-right: 8px;
        }
      }
    }
    
    .ai-status-card {
      .card-title {
        font-size: 14px;
        
        .title-icon {
          font-size: 16px;
        }
      }
      
      .ai-status-list {
        .ai-status-item {
          padding: 10px 8px;
          
          .ai-platform {
            .platform-icon {
              width: 28px;
              height: 28px;
              margin-right: 8px;
            }
            
            .platform-name {
              font-size: 12px;
            }
          }
          
          .status-action {
            margin-top: 6px;
            margin-left: 36px;
            
            .status-tag,
            .login-btn {
              font-size: 11px;
              padding: 4px 8px;
            }
          }
        }
      }
    }
  }
  
  .chart-wrapper {
    padding: 8px;
  }
}

@media (max-width: 480px) {
  .dashboard-editor-container {
    padding: 8px;
    
    .ai-status-card {
      .ai-status-list {
        .ai-status-item {
          .platform-name {
            display: none;
          }
          
          .status-action {
            margin-left: 36px;
          }
        }
      }
    }
  }
}

// 签到日历样式
.sign-in-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 16px;
  font-size: 13px;
  color: #333333;
}

.stats-cards {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.stats-card {
  flex: 1;
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.stats-number {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #ff6b6b;
}

.stats-label {
  color: #666666;
}

.calendar-section {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.calendar-header {
  margin-bottom: 16px;
}

.month-title {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  color: #666666;
  margin-bottom: 8px;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.calendar-day {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}

.calendar-day.signed {
  background-color: #ff6b6b;
  color: white;
}

.calendar-day.today {
  border: 2px solid #ff6b6b;
}

.calendar-day.future {
  color: #999999;
}

.calendar-day.empty {
  background: none;
}

.sign-in-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-bottom: 20px;
  background-color: #ff6b6b;
  border: none;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.sign-in-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.history-section {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.history-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eeeeee;
}

.history-item:last-child {
  border-bottom: none;
}

.history-time {
  color: #666666;
}

.pull-right .el-button--text {
  padding: 0;
  color: #409eff;
}

.qr-code-container {
  padding: 20px;
  text-align: center;
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qr-code {
  img {
    width: 1600px;
    height: 600px;
    margin-bottom: 15px;
  }
}

.qr-tip {
  color: #666;
  font-size: 14px;
  margin-top: 10px;
}

.loading-tip {
  color: #909399;
  font-size: 14px;
}

.error-tip {
  color: #f56c6c;
  font-size: 14px;
  text-align: center;

  i {
    font-size: 48px;
    margin-bottom: 12px;
    display: block;
  }

  p {
    margin: 12px 0;
    font-size: 16px;
  }

  .error-hint {
    color: #909399;
    font-size: 14px;
    margin-top: 16px;
  }

  .el-button {
    margin-top: 16px;
  }
}

.ai-status-card {
  .card-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #303133;

    .title-icon {
      margin-right: 8px;
      font-size: 18px;
      color: #409eff;
    }
  }

  .el-button--text {
    color: #409eff;
    font-size: 14px;
    transition: all 0.3s ease;

    &:hover {
      color: #66b1ff;
      transform: scale(1.05);
    }

    i {
      margin-right: 4px;
    }
  }

  .ai-status-list {
    .ai-status-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 14px 12px;
      border-bottom: 1px solid #f5f7fa;
      flex-wrap: nowrap;
      overflow: hidden;
      border-radius: 6px;
      margin-bottom: 4px;
      transition: all 0.3s ease;

      &:hover {
        background: linear-gradient(to right, rgba(64, 158, 255, 0.05), transparent);
        border-color: transparent;
      }

      &:last-child {
        border-bottom: none;
      }

      .ai-platform {
        display: flex;
        align-items: center;
        flex: 1;
        min-width: 0;

        .platform-icon {
          width: 36px;
          height: 36px;
          border-radius: 50%;
          background: linear-gradient(135deg, #f5f7fa, #fff);
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          overflow: hidden;
          flex-shrink: 0;
          box-shadow: 0 2px 8px rgba(0, 21, 41, 0.06);
          transition: all 0.3s ease;

          &:hover {
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
            transform: scale(1.05);
          }

          img {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .platform-name {
          font-size: 14px;
          color: #606266;
          font-weight: 500;
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          word-break: break-word;
          min-width: 0;
          overflow: hidden;

          .loading-icon {
            margin-left: 8px;
            color: #409eff;
            font-size: 16px;
            animation: rotating 2s linear infinite;
          }
        }
      }

      .status-action {
        flex-shrink: 0;
        margin-left: 12px;

        .status-tag {
          padding: 6px 14px;
          border-radius: 16px;
          white-space: nowrap;
          font-size: 13px;
          max-width: 100px;
          overflow: hidden;
          text-overflow: ellipsis;

          i {
            margin-right: 4px;
          }
        }

        .login-btn {
          padding: 8px 16px;
          border-radius: 18px;
          white-space: nowrap;
          font-size: 13px;
          transition: all 0.3s ease;
          max-width: 120px;
          overflow: hidden;
          text-overflow: ellipsis;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
          }

          i {
            margin-right: 4px;
          }
        }
      }
      
      // 按钮文字超出四个字显示省略号
      .btn-text {
        max-width: 4em;
        overflow: hidden;
        text-overflow: ellipsis;
        display: inline-block;
        vertical-align: middle;
      }
    }
  }
}

.qr-code-container {
  padding: 20px;
  text-align: center;
  min-height: 550px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 8px;
}

.qr-code {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  img {
    width: 1000px;
    height: 550px;
    margin-bottom: 15px;
  }
}

.qr-tip {
  color: #606266;
  font-size: 14px;
  margin-top: 10px;
  font-weight: 500;
}

.loading-tip {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;

  &::before {
    content: "";
    display: inline-block;
    width: 16px;
    height: 16px;
    margin-right: 8px;
    border: 2px solid #dcdfe6;
    border-top-color: #409eff;
    border-radius: 50%;
    animation: loading 1s linear infinite;
  }
}

@keyframes loading {
  to {
    transform: rotate(360deg);
  }
}

.message-card {
  margin-top: 20px;

  .message-list {
    height: 300px;
    overflow-y: auto;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;

    .message-item {
      margin-bottom: 10px;

      .message-content {
        max-width: 80%;

        .message-time {
          font-size: 12px;
          color: #909399;
          margin-bottom: 4px;
        }

        .message-text {
          padding: 8px 12px;
          border-radius: 4px;
          word-break: break-all;
        }
      }
    }

    .message-send {
      display: flex;
      justify-content: flex-end;

      .message-content {
        .message-text {
          background: #409eff;
          color: white;
        }
      }
    }

    .message-receive {
      display: flex;
      justify-content: flex-start;

      .message-content {
        .message-text {
          background: white;
          color: #303133;
        }
      }
    }
  }

  .message-input {
    margin-top: 10px;
  }
}

// 二维码相关样式
.qr-code-container {
  padding: 20px;
  text-align: center;
  min-height: 550px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 8px;
}

.qr-code {
  background: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  img {
    width: 1000px;
    height: 550px;
    margin-bottom: 15px;
  }
}

.qr-tip {
  color: #606266;
  font-size: 14px;
  margin-top: 10px;
  font-weight: 500;
}

.loading-tip {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;

  &::before {
    content: "";
    display: inline-block;
    width: 16px;
    height: 16px;
    margin-right: 8px;
    border: 2px solid #dcdfe6;
    border-top-color: #409eff;
    border-radius: 50%;
    animation: loading 1s linear infinite;
  }
}

@keyframes loading {
  to {
    transform: rotate(360deg);
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.ai-login-btn {
  background-color: #409eff !important;
  color: #fff !important;
  border-radius: 16px;
  border: none;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  &:disabled {
    background-color: #bcdcff !important;
    color: #fff !important;
    cursor: not-allowed;
  }
}

/* 离线登录按钮样式 */
.offline-login-btn {
  background: linear-gradient(135deg, #a0a0a0 0%, #808080 100%) !important;
  border: 1px solid #909399 !important;
  color: #e0e0e0 !important;
  cursor: not-allowed !important;
  opacity: 0.7;
  position: relative;
}

.offline-login-btn:hover {
  background: linear-gradient(135deg, #a0a0a0 0%, #808080 100%) !important;
  border-color: #909399 !important;
  color: #e0e0e0 !important;
}

.offline-login-btn .el-icon {
  color: #e0e0e0 !important;
}

.offline-login-btn .btn-text {
  color: #e0e0e0 !important;
}

@keyframes fadeInOut {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

/* 离线状态的AI项整体样式 */
.ai-status-item:has(.offline-login-btn) {
  background: linear-gradient(135deg, #f8f8f8 0%, #ececec 100%);
  border-radius: 8px;
  padding: 8px;
  position: relative;
  opacity: 0.85;
  transition: all 0.3s ease;
}

.ai-status-item:has(.offline-login-btn)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: repeating-linear-gradient(
    45deg,
    transparent,
    transparent 10px,
    rgba(255, 255, 255, 0.15) 10px,
    rgba(255, 255, 255, 0.15) 20px
  );
  pointer-events: none;
  border-radius: 8px;
}

.ai-status-item:has(.offline-login-btn) .platform-icon img {
  filter: grayscale(100%) brightness(0.85);
  opacity: 0.65;
  transition: all 0.3s ease;
}

.ai-status-item:has(.offline-login-btn) .platform-name {
  color: #909399;
  transition: all 0.3s ease;
}

/* 悬停效果 */
.ai-status-item:has(.offline-login-btn):hover {
  background: linear-gradient(135deg, #f0f0f0 0%, #e0e0e0 100%);
}

.ai-status-item:has(.offline-login-btn):hover .platform-icon img {
  opacity: 0.75;
}

.media-login-btn {
  background-color: #67c23a !important;
  color: #fff !important;
  border-radius: 16px;
  border: none;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  &:disabled {
    background-color: #c2e7b0 !important;
    color: #fff !important;
    cursor: not-allowed;
  }
}

// 主机ID提醒弹窗样式
.corp-id-reminder-dialog {
  .el-message-box {
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    overflow: hidden;
    
    .el-message-box__header {
      background: linear-gradient(135deg, #409eff, #66b1ff);
      padding: 16px 20px;
      
      .el-message-box__title {
        color: white;
        font-size: 18px;
        font-weight: 600;
      }
      
      .el-message-box__headerbtn {
        .el-message-box__close {
          color: white;
          font-size: 18px;
          
          &:hover {
            color: rgba(255, 255, 255, 0.8);
          }
        }
      }
    }
    
    .el-message-box__content {
      padding: 24px 20px;
      
      .el-message-box__message {
        font-size: 15px;
        line-height: 1.6;
        color: #606266;
        
        &::before {
          content: "";
          display: block;
          width: 48px;
          height: 48px;
          margin: 0 auto 16px;
          background: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDgiIGhlaWdodD0iNDgiIHZpZXdCb3g9IjAgMCA0OCA0OCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjQ4IiBoZWlnaHQ9IjQ4IiByeD0iMTIiIGZpbGw9InVybCgjZ3JhZGllbnQwXzFfMSkiLz4KPHBhdGggZD0iTTI0IDMwQzI2LjIwOTEgMzAgMjggMjguMjA5MSAyOCAyNkMyOCAyMy43OTA5IDI2LjIwOTEgMjIgMjQgMjJDMjEuNzkwOSAyMiAyMCAyMy43OTA5IDIwIDI2QzIwIDI4LjIwOTEgMjEuNzkwOSAzMCAyNCAzMFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0yNCAxOEMyNS4xMDQ2IDE4IDI2IDE3LjEwNDYgMjYgMTZDMjYgMTQuODk1NCAyNS4xMDQ2IDE0IDI0IDE0QzIyLjg5NTQgMTQgMjIgMTQuODk1NCAyMiAxNkMyMiAxNy4xMDQ2IDIyLjg5NTQgMTggMjQgMThaIiBmaWxsPSJ3aGl0ZSIvPgo8ZGVmcz4KPGxpbmVhckdyYWRpZW50IGlkPSJncmFkaWVudDBfMV8xIiB4MT0iMCIgeTE9IjAiIHgyPSI0OCIgeTI9IjQ4IiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+CjxzdG9wIHN0b3AtY29sb3I9IiM0MDlFRkYiLz4KPHN0b3Agb2Zmc2V0PSIxIiBzdG9wLWNvbG9yPSIjNjZCMUZGIi8+CjwvbGluZWFyR3JhZGllbnQ+CjwvZGVmcz4KPC9zdmc+') no-repeat center;
          background-size: contain;
        }
      }
    }
    
    .el-message-box__btns {
      padding: 0 20px 20px;
      
      .el-button {
        border-radius: 20px;
        padding: 10px 24px;
        font-size: 14px;
        font-weight: 500;
        
        &.el-button--primary {
          background: linear-gradient(135deg, #409eff, #66b1ff);
          border: none;
          
          &:hover {
            background: linear-gradient(135deg, #66b1ff, #409eff);
          }
        }
      }
    }
  }
}

// 主机ID显示区域样式
.corp-id-display {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-right: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 1);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transform: translateY(-1px);
  }
  
  .corp-id-icon {
    color: #409eff;
    margin-right: 8px;
    font-size: 16px;
  }
  
  .corp-id-text {
    font-size: 14px;
    color: #303133;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .corp-id-refresh {
    margin-left: 8px;
    color: #909399;
    font-size: 14px;
    transition: all 0.3s ease;
    
    &:hover {
      color: #409eff;
      transform: rotate(180deg);
    }
    
    &.rotating {
      animation: rotating 1s linear infinite;
    }
  }
  
  .corp-id-empty {
    color: #f56c6c;
    font-style: italic;
  }
}
</style>
