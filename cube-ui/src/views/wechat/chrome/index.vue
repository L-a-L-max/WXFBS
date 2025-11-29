<template>
  <div class="ai-management-platform">
    <!-- 顶部导航区 -->
    <div class="top-nav">
      <div class="logo-area">
        <img src="../../../assets/ai/logo.png" alt="Logo" class="logo" />
        <h1 class="platform-title">主机</h1>
      </div>
      <div class="nav-buttons">
        <el-button type="primary" size="small" @click="createNewChat">
          <el-icon><Plus /></el-icon>
          创建新对话
        </el-button>
        <div class="history-button">
          <el-button type="text" @click="showHistoryDrawer">
            <img :src="require('../../../assets/ai/celan.png')" alt="历史记录" class="history-icon" />
          </el-button>
        </div>
      </div>
    </div>

    <!-- 历史记录抽屉 -->
    <el-drawer title="历史会话记录" v-model="historyDrawerVisible" direction="rtl" size="30%"
      :before-close="handleHistoryDrawerClose">
      <div class="history-content">
        <!-- 加载状态 -->
        <div v-if="historyLoading" class="history-loading">
          <el-icon><Loading /></el-icon>
          <span>加载中...</span>
        </div>
        <!-- 历史记录列表 -->
        <div v-else-if="chatHistory.length > 0">
          <div v-for="(group, date) in groupedHistory" :key="date" class="history-group">
            <div class="history-date">{{ date }}</div>
            <div class="history-list">
              <div v-for="(item, index) in group" :key="index" class="history-item">
                <!-- 🔥 会话组父记录 -->
                <div class="history-parent" 
                     @click="item.isChatGroup ? toggleHistoryExpansion(item) : loadHistoryItem(item)">
                  <div class="history-header">
                    <!-- 会话组展开/收起箭头 -->
                    <i v-if="item.isChatGroup"
                       :class="[
                         'el-icon-arrow-right',
                         { 'is-expanded': item.isExpanded },
                       ]" 
                       :title="item.isExpanded ? '收起对话轮次' : '展开对话轮次'">
                    </i>
                    <!-- 单轮对话图标 -->
                    <i v-else class="el-icon-chat-dot-round" 
                       style="color: #909399; font-size: 14px;"
                       title="点击加载此对话"></i>
                    <div class="history-content-wrapper">
                      <!-- 会话组显示首轮提问 -->
                      <div v-if="item.isChatGroup" class="history-prompt">
                        {{ item.userPrompt }}
                      </div>
                      <!-- 单轮对话显示提问内容 -->
                      <div v-else class="history-prompt">{{ item.userPrompt }}</div>
                      
                      <div class="history-meta">
                        <span class="history-time">{{ formatHistoryTime(item.createTime) }}</span>
                        <span class="history-separator">•</span>
                        <span class="history-chatid" :title="'会话ID: ' + item.chatId">
                          会话 {{ item.chatId.substring(0, 8) }}
                        </span>
                        <span v-if="item.isChatGroup" class="children-count">
                          • {{ item.totalRounds }}轮对话
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 🔥 展开显示各轮对话 -->
                <div v-if="item.isChatGroup && item.children && item.children.length > 0 && item.isExpanded" 
                     class="history-children">
                  <div v-for="(round, roundIndex) in item.children" 
                       :key="roundIndex" 
                       class="history-child-item"
                       @click="loadHistoryItem(round)">
                    <div class="history-child-content">
                      <span class="child-index">第{{ roundIndex + 1 }}轮</span>
                      <div class="history-prompt">{{ round.roundPrompt }}</div>
                      <div class="history-meta">
                        <span class="history-time">{{ formatHistoryTime(round.createTime) }}</span>
                        <span class="history-separator">•</span>
                        <span class="ai-count">{{ round.aiResponseCount }}个AI响应</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 空状态 -->
        <div v-else class="history-empty">
          <el-icon><Document /></el-icon>
          <p>暂无历史记录</p>
        </div>
      </div>
    </el-drawer>

    <div class="main-content">
      <el-collapse v-model="activeCollapses">
        <el-collapse-item name="ai-selection">
          <template #title>
            <div class="ai-config-header" @click.stop="">
              <span>AI选择配置</span>
              <div class="global-controls">
                <el-button size="small" :type="allAIsEnabled ? 'danger' : 'success'" @click.stop="toggleAllAIs" class="global-control-btn">
                  {{ allAIsEnabled ? '全部关闭' : '全部开启' }}
                </el-button>
              </div>
            </div>
          </template>
          <div class="ai-selection-section">
            <div class="ai-cards">
              <el-card 
                v-for="(ai, index) in processedAiList" 
                :key="index" 
                class="ai-card" 
                :class="{ 'ai-card-offline': !ai.onlineStatus, 'ai-card-not-logged': !isAiLoggedIn(ai) }"
                shadow="hover"
              >
                <!-- 离线状态遮罩层 - 优先级最高 -->
                <div v-if="!ai.onlineStatus" class="card-offline-overlay">
                  <div class="card-offline-message">
                    <i class="el-icon-connection"></i>
                    <span>AI已离线</span>
                    <div class="offline-hint">管理员已将此AI设置为离线状态</div>
                  </div>
                </div>
                
                <!-- 未登录遮罩层 - 只在在线但未登录时显示 -->
                <div v-else-if="!isAiLoggedIn(ai)" class="card-login-overlay">
                  <div class="card-login-message">
                    <i class="el-icon-warning"></i>
                    <span>未登录</span>
                    <div class="login-hint">请先登录此AI账号</div>
                  </div>
                </div>
                
                <div class="ai-card-header">
                  <div class="ai-left">
                    <div class="ai-avatar">
                      <img :src="ai.avatar" alt="AI头像" />
                    </div>
                    <div class="ai-name">{{ ai.name }}</div>
                  </div>
                  <div class="ai-status">
                    <el-switch 
                      v-model="ai.enabled" 
                      active-color="#13ce66" 
                      inactive-color="#ff4949"
                      :disabled="!ai.onlineStatus || !isAiLoggedIn(ai)"
                    >
                    </el-switch>
                  </div>
                </div>
                <!-- 统一的AI选项配置 -->
                <div class="ai-options" v-if="ai.options && ai.options.length > 0">
                  <!-- 下拉选择框选项 -->
                  <div v-for="option in ai.selectOptions" :key="option.id" class="option-item">
                    <div class="select-option">
                      <span class="option-label">{{ option.label }}:</span>
                      <el-select 
                        v-model="ai.selectedValues[option.id]" 
                        size="small" 
                        :disabled="!ai.enabled || !isAiLoggedIn(ai) || isOptionDisabled(ai, option)"
                        @change="handleSelectChange(ai, option, $event)"
                        placeholder="请选择"
                      >
                        <el-option 
                          v-for="value in option.values" 
                          :key="value.value" 
                          :label="value.label" 
                          :value="value.value"
                        />
                      </el-select>
                    </div>
                  </div>
                  
                  <!-- 按钮选项组 -->
                  <div v-if="ai.buttonOptions && ai.buttonOptions.length > 0" class="button-options-group">
                    <div class="ai-capabilities" :class="ai.buttonLayoutClass">
                      <el-button 
                        v-for="option in ai.buttonOptions" 
                        :key="option.id"
                        :type="ai.selectedValues[option.id] ? 'primary' : 'default'"
                        size="small"
                        :disabled="!ai.enabled || !isAiLoggedIn(ai) || isOptionDisabled(ai, option)"
                        @click="handleButtonClick(ai, option)"
                        class="capability-button"
                      >
                        {{ option.label }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-collapse-item>

        <!-- 提示词输入区 -->
        <el-collapse-item title="提示词输入" name="prompt-input">
          <div class="prompt-input-section">
            <el-input type="textarea" :rows="5" placeholder="请输入提示词，支持Markdown格式" v-model="promptInput" resize="none"
              class="prompt-input">
            </el-input>
            <div class="prompt-footer">
              <div class="word-count">字数统计: {{ promptInput.length }}</div>
              <el-button type="primary" @click="sendPrompt" :disabled="!canSend" class="send-button">
                发送
              </el-button>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>

      <!-- 执行状态展示区 -->
      <div class="execution-status-section" v-if="taskStarted">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="task-flow-card">
              <template #header>
                <div class="card-header">
                  <span>任务流程</span>
                </div>
              </template>
              <div class="task-flow">
                <div v-for="(ai, index) in enabledAIs" :key="index" class="task-item">
                  <div class="task-header" @click="toggleAIExpansion(ai)">
                    <div class="header-left">
                      <i :class="[
                        'el-icon-arrow-right',
                        { 'is-expanded': ai.isExpanded },
                      ]"></i>
                      <span class="ai-name">{{ ai.name }}</span>
                    </div>
                    <div class="header-right">
                      <span class="status-text">{{
                        getStatusText(ai.status)
                      }}</span>
                      <i :class="getStatusIcon(ai.status)" class="status-icon"></i>
                    </div>
                  </div>
                  <!-- 添加进度轨迹 -->
                  <div class="progress-timeline" v-if="ai.progressLogs.length > 0 && ai.isExpanded">
                    <div class="timeline-scroll">
                      <div v-for="(log, logIndex) in ai.progressLogs" :key="logIndex" class="progress-item" :class="{
                        completed: log.isCompleted || logIndex > 0,
                        current: !log.isCompleted && logIndex === 0,
                      }">
                        <div class="progress-dot"></div>
                        <div class="progress-line" v-if="logIndex < ai.progressLogs.length - 1"></div>
                        <div class="progress-content">
                          <div class="progress-time">
                            {{ formatTime(log.timestamp) }}
                          </div>
                          <div class="progress-text">{{ log.content }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="screenshots-card">
              <template #header>
                <div class="card-header">
                  <span>主机可视化</span>
                  <div class="controls">
                    <el-switch v-model="autoPlay" active-text="自动轮播" inactive-text="手动切换">
                    </el-switch>
                  </div>
                </div>
              </template>
              <div class="screenshots">
                <el-carousel :interval="3000" :autoplay="false" indicator-position="outside" height="700px">
                  <el-carousel-item v-for="(screenshot, index) in screenshots" :key="index">
                    <img :src="screenshot" alt="执行截图" class="screenshot-image" @click="showLargeImage(screenshot)" />
                  </el-carousel-item>
                </el-carousel>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 结果展示区 -->
      <div class="results-section" v-if="results.length > 0">
        <div class="section-header">
          <h2 class="section-title">执行结果</h2>
          <el-button type="primary" @click="showScoreDialog" size="small">
            智能评分
          </el-button>
        </div>
        <el-tabs v-model="activeResultTab" type="card">
          <el-tab-pane v-for="(result, index) in results" :key="index" :label="result.aiName" :name="'result-' + index">
            <div class="result-content">
              <div class="result-header" v-if="result.shareUrl">
                <div class="result-title">{{ result.aiName }}的执行结果</div>
                <div class="result-buttons">
                  <el-button size="small" type="primary" @click="openShareUrl(result.shareUrl)"
                    class="share-link-btn">
                    <el-icon><Link /></el-icon>
                    <span>查看原链接</span>
                  </el-button>
                  <el-button v-if="!result.aiName.includes('智能排版')" size="small" type="success"
                    @click="handlePushToMedia(result)" class="push-media-btn"
                    :loading="pushingToMedia" :disabled="pushingToMedia">
                    <el-icon><Promotion /></el-icon>
                    <span>智能排版</span>
                  </el-button>
                  <el-button v-else size="small" type="success"
                    @click="pushToMediaWithContent(result)" class="push-media-btn" :loading="pushingToMedia && false"
                    :disabled="pushingToMedia && false">
                    <el-icon><Promotion /></el-icon>
                    <span>投递到公众号/媒体</span>
                  </el-button>
                </div>
              </div>
              <!-- 如果有shareImgUrl则渲染图片或PDF，否则渲染markdown -->
              <div v-if="result.shareImgUrl" class="share-content">
                <!-- 渲染图片 -->
                <img v-if="isImageFile(result.shareImgUrl)" :src="result.shareImgUrl" alt="分享图片" class="share-image"
                  :style="getImageStyle(result.aiName)" />
                <!-- 渲染PDF -->
                <iframe v-else-if="isPdfFile(result.shareImgUrl)" :src="result.shareImgUrl" class="share-pdf"
                  frameborder="0">
                </iframe>
                <!-- 其他文件类型显示链接 -->
                <div v-else class="share-file">
                  <el-button type="primary" @click="openShareUrl(result.shareImgUrl)">
                    <el-icon><Document /></el-icon>
                    <span>查看文件</span>
                  </el-button>
                </div>
              </div>
              <div v-else class="markdown-content" v-html="renderMarkdown(result.content)"></div>
              <!-- <div class="action-buttons">
                <el-button size="small" type="primary" @click="copyResult(result.content)">复制（纯文本）</el-button>
                <el-button size="small" type="success" @click="exportResult(result)">导出（MD文件）</el-button>
              </div> -->
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 大图查看对话框 -->
    <el-dialog v-model="showImageDialog" width="90%" :show-close="true" :modal="true" center class="image-dialog"
      :append-to-body="true" @close="closeLargeImage">
      <div class="large-image-container">
        <!-- 如果是单张分享图片，直接显示 -->
        <div v-if="currentLargeImage && !screenshots.includes(currentLargeImage)" class="single-image-container">
          <img :src="currentLargeImage" alt="大图" class="large-image" />
        </div>
        <!-- 如果是截图轮播 -->
        <el-carousel v-else :interval="3000" :autoplay="false" indicator-position="outside" height="80vh">
          <el-carousel-item v-for="(screenshot, index) in screenshots" :key="index">
            <img :src="screenshot" alt="大图" class="large-image" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </el-dialog>

    <!-- 评分弹窗 -->
    <el-dialog title="智能评分" v-model="scoreDialogVisible" width="60%" height="65%" :close-on-click-modal="false"
      class="score-dialog">
      <div class="score-dialog-content">
        <h3>选择评分AI：</h3>
        <el-select v-model="scoreAI" placeholder="请选择评分AI">
          <!-- <el-option v-for="(ai, index) in aiList" :key="index" :label="ai.name" :value="ai.name">
            {{ ai.name }}
          </el-option> -->
          <el-option label="豆包" value="豆包"></el-option>
          <el-option label="DeepSeek" value="DeepSeek"></el-option>
        </el-select>
        <div class="score-prompt-section">
          <h3>评分提示词：</h3>
          <el-select v-model="selectedScorePrompt" @change="loadScorePrompt">
            <el-option v-for="(prompt, index) in scorePromptList" :key="index" :label="prompt.name"
              :value="prompt.name"></el-option>
          </el-select>
          <el-input type="textarea" :rows="10" placeholder="请输入评分提示词，例如：请从内容质量、逻辑性、创新性等方面进行评分" v-model="scorePrompt"
            resize="none" class="score-prompt-input">
          </el-input>
        </div>
        <div class="selected-results">
          <h3>选择要评分的内容：</h3>
          <el-checkbox-group v-model="selectedResults">
            <el-checkbox v-for="(result, index) in results" :key="index" :label="result.aiName" class="result-checkbox">
              {{ result.aiName }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="scoreDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleScore" :disabled="!canScore">
            开始评分
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 投递到媒体弹窗 -->
    <el-dialog title="媒体投递设置" v-model="layoutDialogVisible" width="60%" height="65%" :close-on-click-modal="false"
      class="layout-dialog">
      <div class="layout-dialog-content">
        <!-- 媒体选择区域 -->
        <div class="media-selection-section">
          <h3>选择排版AI：</h3>
          <el-select v-model="layoutAI" placeholder="请选择排版AI">
            <!-- <el-option v-for="(ai, index) in aiList" :key="index" :label="ai.name" :value="ai.name">
            {{ ai.name }}
          </el-option> -->
            <el-option label="豆包" value="豆包"></el-option>
            <el-option label="DeepSeek" value="DeepSeek"></el-option>
          </el-select>
          <h3>选择投递媒体：</h3>
          <el-radio-group v-model="selectedMedia" size="small" class="media-radio-group">
            <el-radio-button label="wechat_layout" value="wechat_layout">
              <el-icon><ChatDotSquare /></el-icon>
              公众号
            </el-radio-button>
<!--            <el-radio-button label="zhihu_layout" value="zhihu_layout">-->
<!--              <el-icon><ChatDotSquare /></el-icon>-->
<!--              知乎-->
<!--            </el-radio-button>-->

          </el-radio-group>
          <div class="media-description">
            <template v-if="selectedMedia === 'wechat_layout'">
              <small>📝 将内容排版为适合微信公众号的HTML格式，并自动投递到草稿箱</small>
            </template>
            <template  v-else-if="selectedMedia === 'zhihu_layout'">
              <small>📝 将内容排版为适合知乎的文本格式，并自动投递到草稿箱</small>
            </template>

          </div>
        </div>

        <!-- <div class="layout-prompt-section">
          <h3>排版提示词：</h3>
          <el-input type="textarea" :rows="12" placeholder="请输入排版提示词" v-model="layoutPrompt" resize="none"
            class="layout-prompt-input">
          </el-input>
        </div> -->


      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="layoutDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleLayout" :disabled="!canLayout">
            开始排版
          </el-button>
        </span>
      </template>
    </el-dialog>


  </div>
</template>

<script>
import { ChatDotSquare, Document, Link, Loading, Plus, Promotion } from '@element-plus/icons-vue';

  import { marked } from "marked";
  import {
    message,
    saveUserChatData,
    getChatHistory,
    pushAutoOffice,
    getMediaCallWord,
    getAllScorePrompt,
    getScoreWord,
  } from "@/api/wechat/aigc";
  import { v4 as uuidv4 } from "uuid";
  import websocketClient from "@/utils/websocket";
  import store from "@/store";
  import TurndownService from "turndown";
  import { getCorpId, ensureLatestCorpId } from "@/utils/corpId";
  import { listUserAvailableAiagent } from "@/api/system/aiagent";

  export default {
    name: "AIManagementPlatform",
    components: {
      ChatDotSquare,
      Document,
      Link,
      Loading,
      Plus,
      Promotion
    },
    data() {
      return {
        userId: store.state.user.id,
        corpId: store.state.user.corp_id,
        chatId: uuidv4(),
        expandedHistoryItems: {},
        userInfoReq: {
          userPrompt: "",
          userId: "",
          corpId: "",
          taskId: "",
          chatId: "", // 🔥 添加chatId字段，用于标识会话
          roles: "",
          toneChatId: "",
          ybDsChatId: "",
          dbChatId: "",
          tyChatId: "",
          metasoChatId: "",
          baiduChatId: "",
          deepseekChatId: "",
          zhzdChatId: "",

          isNewChat: true,
        },
        jsonRpcReqest: {
          jsonrpc: "2.0",
          id: uuidv4(),
          method: "",
          params: {},
        },
        // 动态AI配置
        availableAiList: [], // 从数据库加载的AI列表
        aiList: [], // 实际使用的AI列表，完全从后端动态加载
        
        // 🔥 登录状态管理
        aiLoginStatus: {}, // AI登录状态 {agentCode: boolean}
        accounts: {}, // AI账户信息 {agentCode: string}
        mediaList: [
          {
            name: "wechat_layout",
            label: "公众号",
          },
          {
            name: "zhihu_layout",
            label: "知乎",
          },
          {
            name: "weitoutiao_layout",
            label: "微头条",
          },
          {
            name: "baijiahao_layout",
            label: "百家号",
          }
        ],
        promptInput: "",
        taskStarted: false,
        autoPlay: false,
        screenshots: [],
        results: [],
        activeResultTab: "result-0",
        activeCollapses: ["ai-selection", "prompt-input"], // 默认展开这两个区域
        showImageDialog: false,
        currentLargeImage: "",
        enabledAIs: [],
        turndownService: new TurndownService({
          headingStyle: "atx",
          codeBlockStyle: "fenced",
          emDelimiter: "*",
        }),
        scoreDialogVisible: false,
        selectedResults: [],
        selectedScorePrompt: "",
        scorePromptList: [],
        scorePrompt: `请你深度阅读以下几篇内容，从多个维度进行逐项打分，输出评分结果。并在以下各篇文章的基础上博采众长，综合整理一篇更全面的文章。`,
        scoreAI: "豆包", // 默认选择豆包进行评分
        layoutDialogVisible: false,
        layoutPrompt: "",
        layoutAI: "豆包", // 当前选择的排版AI
        currentLayoutResult: null, // 当前要排版的结果
        historyDrawerVisible: false,
        chatHistory: [],
        historyLoading: false, // 历史记录加载状态
        pushOfficeNum: 0, // 投递到公众号的递增编号
        pushingToWechat: false, // 投递到公众号的loading状态
        selectedMedia: "wechat_layout", // 默认选择公众号
        pushingToMedia: false // 投递到媒体的loading状态
      };
    },
    computed: {
      canSend() {
        const hasInput = this.promptInput.trim().length > 0;
        const hasEnabledAndLoggedInAI = this.aiList.some((ai) => ai.enabled && this.isAiLoggedIn(ai));
        
        console.log('🔍 [canSend] 输入检查:', hasInput, '已启用且登录的AI:', hasEnabledAndLoggedInAI);
        
        return hasInput && hasEnabledAndLoggedInAI;
      },
      canScore() {
        return (
          this.selectedResults.length > 0 && this.scorePrompt.trim().length > 0
        );
      },
      canLayout() {
        return this.currentLayoutResult !== null;
      },
      // 检查所有任务是否完成
      allTasksCompleted() {
        if(!this.taskStarted || this.enabledAIs.length === 0) {
          return false;
        }
        return this.enabledAIs.every(ai => ai.status === 'completed' || ai.status === 'failed');
      },
      // 检查是否有任务正在运行
      hasRunningTasks() {
        return this.enabledAIs.some(ai => ai.status === 'running');
      },
      groupedHistory() {
        const groups = {};
        const chatGroups = {};

        // 🔥 首先按chatId分组
        this.chatHistory.forEach((item) => {
          if(!chatGroups[item.chatId]) {
            chatGroups[item.chatId] = [];
          }
          chatGroups[item.chatId].push(item);
        });

        console.log('📊 [历史记录] 原始记录数:', this.chatHistory.length);
        console.log('📊 [历史记录] 会话组数:', Object.keys(chatGroups).length);

        // 🔥🔥 按chatId聚合，每个chatId作为一个父记录
        Object.entries(chatGroups).forEach(([chatId, chatGroup]) => {
          // 按时间升序排序
          chatGroup.sort((a, b) => {
            const timeA = new Date(a.createTime).getTime();
            const timeB = new Date(b.createTime).getTime();
            return timeA - timeB;
          });

          // 🔥 按userPrompt分组（同一个提问的多个AI响应算一轮）
          const roundGroups = {};
          chatGroup.forEach((record) => {
            const prompt = record.userPrompt || '未知提问';
            if(!roundGroups[prompt]) {
              roundGroups[prompt] = [];
            }
            roundGroups[prompt].push(record);
          });

          // 获取第一条记录用于日期分组
          const firstRecord = chatGroup[0];
          const date = this.getHistoryDate(firstRecord.createTime);

          if(!groups[date]) {
            groups[date] = [];
          }

          // 🔥🔥 将每一轮作为子记录，使用该轮最后一条记录（包含完整状态）
          const rounds = Object.entries(roundGroups).map(([prompt, roundRecords], roundIndex) => {
            // 🔥 关键：同一轮对话有多条记录，每条记录保存当时的完整状态
            // 最后一条记录包含了该轮所有AI的完整响应，直接使用它
            const lastRecord = roundRecords[roundRecords.length - 1];
            
            // 解析最后一条记录获取AI响应数量
            let aiResponseCount = 0;
            try {
              const recordData = JSON.parse(lastRecord.data);
              aiResponseCount = recordData.results ? recordData.results.length : 0;
            } catch (e) {
              console.error('解析记录失败:', e);
            }
            
            console.log(`📊 [历史记录] 第${roundIndex + 1}轮 "${prompt}" 有${aiResponseCount}个AI响应（共${roundRecords.length}条记录，使用最后一条）`);
            
            return {
              ...lastRecord,
              roundIndex: roundIndex,
              roundPrompt: prompt,
              aiResponseCount: aiResponseCount,
              isRound: true,
              allRoundRecords: roundRecords, // 保存所有记录供调试
            };
          });

          console.log(`📝 [会话${chatId.substring(0, 8)}] 总计${rounds.length}轮对话, 首轮: "${rounds[0]?.roundPrompt?.substring(0, 20)}..."`);

          // 🔥🔥 chatId作为父记录，各轮作为子记录
          groups[date].push({
            ...firstRecord,
            isParent: true,
            isChatGroup: true, // 标记这是一个会话组
            totalRounds: rounds.length,
            chatId: chatId,
            // 🔥 默认收起状态
            isExpanded: this.expandedHistoryItems[chatId] !== undefined 
              ? this.expandedHistoryItems[chatId] 
              : false,
            children: rounds, // 各轮作为子记录
          });
        });

        console.log('📊 [历史记录] 分组结果:', Object.keys(groups).map(date => `${date}: ${groups[date].length}条`).join(', '));
        return groups;
      },
      // 检查是否所有AI都已启用（只包含已登录的AI）
      allAIsEnabled() {
        const loggedInAIs = this.aiList.filter(ai => this.isAiLoggedIn(ai));
        return loggedInAIs.length > 0 && loggedInAIs.every(ai => ai.enabled);
      },
      // 🔥 处理AI选项分类和布局（参考小程序实现）
      processedAiList() {
        return this.aiList.map(ai => {
          // 初始化selectedValues（如果不存在）
          if (!ai.selectedValues) {
            this.$set(ai, 'selectedValues', this.initializeSelectedValues(ai));
          }
          
          // 处理选项分类
          const selectOptions = ai.options ? ai.options.filter(opt => opt.type === 'select') : [];
          const buttonOptions = ai.options ? ai.options.filter(opt => opt.type === 'button') : [];
          
          // 使用$set确保响应式
          this.$set(ai, 'selectOptions', selectOptions);
          this.$set(ai, 'buttonOptions', buttonOptions);
          this.$set(ai, 'buttonLayoutClass', this.getButtonLayoutClass(buttonOptions.length));
          
          return ai;
        });
      },
    },
    async created() {
      console.log(this.userId);

      // 使用企业ID工具确保获取最新的企业ID
      try {
        this.corpId = await getCorpId();
        console.log('获取最新企业ID:', this.corpId);
      } catch(error) {
        console.warn('获取企业ID失败，使用store中的值:', error);
        console.log(this.corpId);
      }

      // 加载用户可用的AI配置
      await this.loadAvailableAiList();

      this.initWebSocket(this.userId);
      
      // 同步首页的登录状态
      this.syncLoginStatusFromHomepage();
      
      // 不在created中预加载历史记录，只在打开抽屉时加载
      this.loadLastChat(); // 加载上次会话
    },
    mounted() {
      // 监听企业ID更新事件
      window.addEventListener('corpIdUpdated', this.handleCorpIdUpdated);
    },
    beforeUnmount() {
      // 移除事件监听
      window.removeEventListener('corpIdUpdated', this.handleCorpIdUpdated);
    },
    watch: {
      // 监听媒体选择变化，自动加载对应的提示词
      selectedMedia: {
        handler(newMedia) {
          this.loadMediaPrompt(newMedia);
        },
        immediate: false
      },
      // 监听任务完成状态
      allTasksCompleted: {
        handler(newValue) {
          if(newValue && this.taskStarted) {
            // 所有任务完成时的处理
            this.$nextTick(() => {
              console.log('所有AI任务已完成！');
              // 可以考虑自动折叠任务流程区域或其他UI优化
            });
          }
        },
        immediate: false
      }
    },
    methods: {
      // 根据按钮数量动态返回布局类名
      getButtonLayoutClass(buttonCount) {
        if (buttonCount === 1) return 'buttons-single';
        if (buttonCount === 2) return 'buttons-two';
        if (buttonCount === 3) return 'buttons-three';
        return 'buttons-multiple'; // 4个或更多
      },
      
      // 🔥 同步首页的登录状态到主机页面
      syncLoginStatusFromHomepage() {
        try {
          // 从localStorage或sessionStorage获取首页的登录状态
          const homepageLoginStatus = localStorage.getItem('aiLoginStatus');
          const homepageAccounts = localStorage.getItem('aiAccounts');
          
          if (homepageLoginStatus) {
            const loginStatus = JSON.parse(homepageLoginStatus);
            console.log('🔄 [状态同步] 从首页同步登录状态:', loginStatus);
            
            // 更新当前页面的登录状态
            Object.keys(loginStatus).forEach(aiCode => {
              this.$set(this.aiLoginStatus, aiCode, loginStatus[aiCode]);
            });
          }
          
          if (homepageAccounts) {
            const accounts = JSON.parse(homepageAccounts);
            console.log('🔄 [状态同步] 从首页同步账户信息:', accounts);
            
            // 更新当前页面的账户信息
            Object.keys(accounts).forEach(aiCode => {
              this.$set(this.accounts, aiCode, accounts[aiCode]);
            });
          }
          
          console.log('✅ [状态同步] 登录状态同步完成');
        } catch (error) {
          console.warn('⚠️ [状态同步] 同步首页登录状态失败:', error);
        }
      },
      
      // 🔥 保存登录状态到本地存储，供其他页面同步
      saveLoginStatusToStorage() {
        try {
          localStorage.setItem('aiLoginStatus', JSON.stringify(this.aiLoginStatus));
          localStorage.setItem('aiAccounts', JSON.stringify(this.accounts));
          console.log('💾 [状态同步] 登录状态已保存到本地存储');
        } catch (error) {
          console.warn('⚠️ [状态同步] 保存登录状态失败:', error);
        }
      },
      
      // 🔥 判断AI是否已登录（只检查登录状态，不检查在线状态）
      isAiLoggedIn(ai) {
        if (!ai || !ai.agentCode) return false;
        
        // 只检查登录状态，不检查在线状态
        const loginStatus = this.aiLoginStatus[ai.agentCode];
        const isOnline = ai.onlineStatus === 1;
        
        console.log(`🔍 [登录检查] ${ai.name}(${ai.agentCode}): 登录状态=${loginStatus}, 在线状态=${isOnline}`);
        
        // 只返回登录状态，离线状态通过其他方式处理
        return !!loginStatus;
      },
      
      // 加载用户可用的AI列表
      async loadAvailableAiList() {
        try {
          const response = await listUserAvailableAiagent();
          this.availableAiList = response.data || [];
          
          // 根据数据库配置构建aiList
          this.aiList = this.availableAiList.map(ai => {
            // 解析JSON配置
            let configJson = {};
            try {
              configJson = ai.configJson ? JSON.parse(ai.configJson) : {};
            } catch (e) {
              console.warn('解析AI配置JSON失败:', ai.agentName, e);
            }
            
            // 构建AI配置对象
            const aiConfig = {
              name: ai.agentName,
              avatar: ai.agentIcon, // 直接使用后端返回的图标URL
              options: configJson.options || [], // 直接使用数据库的options配置
              selectedValues: {}, // 存储用户选择的值
              enabled: false, // 🔥 默认关闭状态
              status: 'idle',
              progressLogs: [],
              isExpanded: true,
              agentCode: ai.agentCode, // 添加agentCode用于后端识别
              agentStatus: ai.agentStatus,
              onlineStatus: ai.onlineStatus
            };
            
            // 初始化选中值
            this.initializeSelectedValues(aiConfig);
            
            return aiConfig;
          });
          
          console.log('✅ [Chrome页面] 加载用户可用AI列表:', this.aiList.length, '个');
        } catch (error) {
          console.error('❌ [Chrome页面] 加载AI列表失败:', error);
          // 加载失败时显示空列表，不再使用本地配置
          this.aiList = [];
          this.$message.error('加载AI列表失败，请刷新页面重试');
        }
      },
      
      // 解析配置中的capabilities
      parseCapabilities(configJson) {
        if (!configJson.options) return [];
        
        return configJson.options
          .filter(option => option.type === 'select')
          .map(option => ({
            label: option.label,
            value: option.id,  // 使用option.id而不是backendField
            values: option.values || []
          }))
          .flat();
      },
      
      // 获取默认选中的capabilities
      getDefaultSelectedCapabilities(configJson) {
        if (!configJson.options) return [];
        
        const defaultValues = [];
        configJson.options.forEach(option => {
          if (option.type === 'select' && option.values) {
            const defaultItems = option.values.filter(v => v.default);
            if (option.selectType === 'single') {
              return defaultItems.length > 0 ? defaultItems[0].value : '';
            } else {
              defaultValues.push(...defaultItems.map(v => v.value));
            }
          }
        });
        
        return defaultValues;
      },
      
      // 判断是否为单选模式
      isSingleSelectMode(configJson) {
        if (!configJson.options) return false;
        return configJson.options.some(option => 
          option.type === 'select' && option.selectType === 'single'
        );
      },

      // 初始化AI的选中值
      initializeSelectedValues(aiConfig) {
        aiConfig.selectedValues = {};
        if (aiConfig.options) {
          aiConfig.options.forEach(option => {
            if (option.type === 'select') {
              const defaultValue = option.values.find(v => v.default);
              aiConfig.selectedValues[option.id] = defaultValue ? defaultValue.value : '';
            } else if (option.type === 'button') {
              aiConfig.selectedValues[option.id] = false;
            }
          });
        }
      },

      // 处理下拉选择变化
      handleSelectChange(ai, option, value) {
        console.log(`✅ [${ai.name}] 下拉选择变化:`, option.label, '选择了:', value);
        ai.selectedValues[option.id] = value;
        
        // 处理冲突逻辑
        this.handleOptionConflicts(ai, option);
      },

      // 处理按钮点击
      handleButtonClick(ai, option) {
        console.log(`✅ [${ai.name}] 按钮点击:`, option.label);
        // 切换按钮状态
        ai.selectedValues[option.id] = !ai.selectedValues[option.id];
        
        // 处理冲突逻辑
        this.handleOptionConflicts(ai, option);
      },

      // 检查选项是否被禁用
      isOptionDisabled(ai, option) {
        // 检查选项级别的冲突
        if (option.conflicts && option.conflicts.length > 0) {
          const hasConflict = option.conflicts.some(conflictId => {
            const conflictOption = ai.options.find(opt => opt.id === conflictId);
            if (!conflictOption) return false;
            
            if (conflictOption.type === 'select') {
              const selectedValue = ai.selectedValues[conflictId];
              return selectedValue && selectedValue !== '';
            } else if (conflictOption.type === 'button') {
              return !!ai.selectedValues[conflictId];
            }
            return false;
          });
          if (hasConflict) return true;
        }

        // 检查选项值级别的冲突（针对下拉选择）
        if (option.type === 'select') {
          // 检查其他选项的选项值是否与当前选项冲突
          return ai.options.some(otherOption => {
            if (otherOption.id === option.id) return false;
            
            if (otherOption.type === 'select') {
              const selectedValue = ai.selectedValues[otherOption.id];
              if (!selectedValue || selectedValue === '') return false;
              
              // 查找选中的选项值
              const selectedValueObj = otherOption.values.find(v => v.value === selectedValue);
              if (selectedValueObj && selectedValueObj.conflicts) {
                return selectedValueObj.conflicts.includes(option.id);
              }
            }
            return false;
          });
        }
        
        return false;
      },

      // 🔥 处理选项互斥逻辑
      handleOptionConflicts(ai, changedOption) {
        if (!ai.options || !ai.selectedValues) return;
        
        // 获取当前选项的冲突列表
        let conflicts = [];
        
        // 1. 检查当前选项本身的conflicts字段
        if (changedOption.conflicts && Array.isArray(changedOption.conflicts)) {
          const isCurrentActive = changedOption.type === 'select' 
            ? (ai.selectedValues[changedOption.id] && ai.selectedValues[changedOption.id] !== '')
            : !!ai.selectedValues[changedOption.id];
            
          if (isCurrentActive) {
            conflicts = [...changedOption.conflicts];
            console.log(`⚠️ [${ai.name}] ${changedOption.label} 选项级冲突:`, conflicts);
          }
        }

        // 2. 如果是select类型，检查选中值的conflicts字段
        if (changedOption.type === 'select') {
          const selectedValue = ai.selectedValues[changedOption.id];
          if (selectedValue && selectedValue !== '') {
            // 查找选中的选项值对象
            const selectedValueObj = changedOption.values.find(v => v.value === selectedValue);
            if (selectedValueObj && selectedValueObj.conflicts && Array.isArray(selectedValueObj.conflicts)) {
              conflicts = [...conflicts, ...selectedValueObj.conflicts];
              console.log(`⚠️ [${ai.name}] ${changedOption.label}="${selectedValueObj.label}" 值级冲突:`, selectedValueObj.conflicts);
            }
          }
        }
        
        if (conflicts.length === 0) {
          console.log(`📋 [${ai.name}] ${changedOption.label} 无冲突配置`);
          return;
        }
        
        // 清除冲突的选项
        conflicts.forEach(conflictId => {
          const conflictOption = ai.options.find(opt => opt.id === conflictId);
          if (!conflictOption) return;
          
          if (conflictOption.type === 'select') {
            // 下拉选择：清空选择（设为空字符串）
            const hadValue = ai.selectedValues[conflictId];
            if (hadValue && hadValue !== '') {
              ai.selectedValues[conflictId] = '';
              console.log(`🔄 [${ai.name}] 清除冲突选项: ${conflictOption.label}`);
            }
          } else if (conflictOption.type === 'button') {
            // 按钮：设为false（关闭）
            if (ai.selectedValues[conflictId]) {
              ai.selectedValues[conflictId] = false;
              console.log(`🔄 [${ai.name}] 关闭冲突按钮: ${conflictOption.label}`);
            }
          }
        });
        
        // 强制更新视图
        this.$forceUpdate();
      },

      // 全局AI控制方法（只操作已登录的AI）
      toggleAllAIs() {
        const loggedInAIs = this.aiList.filter(ai => this.isAiLoggedIn(ai));
        const newState = !this.allAIsEnabled;
        
        loggedInAIs.forEach(ai => {
          ai.enabled = newState;
        });

        // 显示操作反馈
        if(newState) {
          console.log(`已启动全部已登录的AI智能体 (${loggedInAIs.length}个)`);
        } else {
          console.log(`已关闭全部已登录的AI智能体 (${loggedInAIs.length}个)`);
        }
      },
      // 处理企业ID更新事件
      handleCorpIdUpdated(event) {
        const newCorpId = event.detail.corpId;
        if(newCorpId && newCorpId !== this.corpId) {
          console.log('Chrome页面接收到企业ID更新事件，更新本地corpId:', newCorpId);
          this.corpId = newCorpId;
          console.log(`主机ID已自动更新: ${newCorpId}`);
        }
      },

      // 确保企业ID最新
      async ensureLatestCorpId() {
        try {
          const result = await ensureLatestCorpId();
          if(result.corpId !== this.corpId) {
            this.corpId = result.corpId;
            console.log('企业ID已自动更新:', result.corpId);
          }
        } catch(error) {
          console.error('确保企业ID最新失败:', error);
        }
      },

      async sendPrompt() {
        if(!this.canSend) return;

        // 🔥 检查启用的AI中，有多少是在线的
        const enabledAIs = this.aiList.filter(ai => ai.enabled);
        const onlineEnabledAIs = enabledAIs.filter(ai => ai.onlineStatus);
        const offlineEnabledAIs = enabledAIs.filter(ai => !ai.onlineStatus);
        
        // 如果所有启用的AI都离线，则阻止请求
        if (onlineEnabledAIs.length === 0) {
          if (offlineEnabledAIs.length > 0) {
            const offlineNames = offlineEnabledAIs.map(ai => ai.name).join('、');
            this.$message.error(`所有启用的AI都已离线，无法发送请求：${offlineNames}`);
          } else {
            this.$message.warning('请至少启用一个AI');
          }
          return;
        }
        
        // 🔥 静默跳过离线AI，不显示提示（避免影响用户体验）
        if (offlineEnabledAIs.length > 0) {
          console.log(`ℹ️ [AI过滤] 以下AI已离线，将被跳过:`, offlineEnabledAIs.map(ai => ai.name).join('、'));
          console.log(`✅ [AI过滤] 将使用在线AI:`, onlineEnabledAIs.map(ai => ai.name).join('、'));
        }

        // 确保使用最新的企业ID
        await this.ensureLatestCorpId();

        this.screenshots = [];
        
        // 🔧 移除发送按钮焦点，避免折叠时的aria-hidden警告
        if (document.activeElement) {
          document.activeElement.blur();
        }
        
        // 只折叠提示词输入区域，保持AI选择配置区域展开
        this.activeCollapses = ["ai-selection"];

        this.taskStarted = true;
        this.results = []; // 清空之前的结果

        this.userInfoReq.roles = "";

        this.userInfoReq.taskId = uuidv4();
        this.userInfoReq.userId = this.userId;
        this.userInfoReq.corpId = this.corpId;
        this.userInfoReq.chatId = this.chatId; // 🔥 传递chatId给后端
        this.userInfoReq.userPrompt = this.promptInput;

        // 🔥 获取启用、在线且已登录的AI列表及其状态，并重置状态
        this.enabledAIs = this.aiList.filter((ai) => ai.enabled && ai.onlineStatus && this.isAiLoggedIn(ai)).map(ai => ({
          ...ai,
          status: "running",
          progressLogs: [], // 清空之前的进度日志
          isExpanded: true  // 确保展开状态一致
        }));
        
        console.log('🔥 [sendPrompt] 筛选后的AI数量:', this.enabledAIs.length);
        console.log('🔥 [sendPrompt] 已登录的AI:', this.enabledAIs.map(ai => ai.name).join(', '));

        // 将所有启用的AI状态设置为运行中（使用Vue的响应式更新）
        this.enabledAIs.forEach((ai) => {
            ai.status = "running";
                   ai.progressLogs = [];
                   ai.isExpanded = true;
        });

        // 🔥 统一处理AI配置（完全动态，支持占位符替换）
        this.enabledAIs.forEach((ai) => {
          // 1. 先添加基础的agentCode
          this.userInfoReq.roles += `${ai.agentCode},`;
          
          // 2. 处理AI的选项配置
          if (ai.options && ai.selectedValues) {
            // 🔥 先收集所有select选项的值，用于占位符替换
            const selectValues = {};
            ai.options.forEach(option => {
              if (option.type === 'select') {
                const selectedValue = ai.selectedValues[option.id];
                if (selectedValue) {
                  selectValues[option.id] = selectedValue;
                }
              }
            });
            
            // 🔥 然后处理所有选项
            ai.options.forEach(option => {
              const selectedValue = ai.selectedValues[option.id];
              
              if (option.type === 'select' && selectedValue) {
                // 下拉选择：直接使用value
                this.userInfoReq.roles += `${selectedValue},`;
                console.log(`✅ [${ai.name}] 下拉选择: ${option.label} = ${selectedValue}`);
              } else if (option.type === 'button' && selectedValue) {
                // 按钮选择：支持{model}占位符替换
                let buttonValue = option.value;
                
                // 🔥 通用占位符替换逻辑
                if (buttonValue.includes('{model}') && option.dependsOn) {
                  // 从dependsOn的选项中获取模型值
                  const modelSelectValue = selectValues[option.dependsOn] || '';
                  // 提取模型名称（如 yb-hunyuan-pt → hunyuan, baidu-dsr1 → dsr1）
                  // 去掉前缀，取中间部分
                  const parts = modelSelectValue.split('-');
                  const modelName = parts.length >= 2 ? parts.slice(1, -1).join('-') || parts[parts.length - 1] : parts[0];
                  // 替换占位符
                  buttonValue = buttonValue.replace('{model}', modelName);
                  console.log(`✅ [${ai.name}] 按钮占位符替换: {model} → ${modelName} (来自${modelSelectValue}), 最终: ${buttonValue}`);
                }
                
                this.userInfoReq.roles += `${buttonValue},`;
                console.log(`✅ [${ai.name}] 按钮点击: ${option.label} → ${buttonValue}`);
              }
            });
          }
          
          console.log(`✅ [${ai.name}] 配置完成，当前roles:`, this.userInfoReq.roles);
        });

        console.log("参数：", this.userInfoReq);

        //调用后端接口
        this.jsonRpcReqest.method = "AI智能对话";
        this.jsonRpcReqest.params = this.userInfoReq;
        this.message(this.jsonRpcReqest);
        this.userInfoReq.isNewChat = false;
      },

      message(data) {
        message(data)
          .then(() => {
            // 调用成功时无需额外处理，结果通过 WebSocket 返回
          })
          .catch((error) => {
            console.error('调用 message API 失败:', error);
            
            const responseData = error?.response?.data || {};
            const businessCode = responseData.code ?? error?.code;
            let errorMsg = responseData.messages || responseData.msg || error?.message || '网络异常，请检查网络连接后重试';
            let errorType = 'error';

            if(businessCode === 400) {
              if(errorMsg.includes('积分余额不足') || errorMsg.includes('余额不足')) {
                errorMsg = '⚠️ 积分余额不足，无法执行此操作。请充值后再试。';
              } else if(errorMsg.includes('规则未配置')) {
                errorMsg = '⚠️ 积分规则未配置，请联系管理员。';
              } else {
                errorMsg = `⚠️ ${errorMsg}`;
              }
              errorType = 'warning';
            } else if(businessCode === 429) {
              if(errorMsg.includes('领取上限') || errorMsg.includes('限频')) {
                errorMsg = `⏰ ${errorMsg}，请稍后再试。`;
              } else if(errorMsg.includes('累计上限')) {
                errorMsg = `📊 ${errorMsg}，无法继续发放。`;
              } else {
                errorMsg = `⏰ ${errorMsg}`;
              }
              errorType = 'warning';
            } else if(businessCode === 500) {
              errorMsg = `❌ 服务器错误：${errorMsg}`;
            } else if(businessCode === 201) {
              errorMsg = errorMsg || '操作失败';
              errorType = 'warning';
            } else if(!businessCode) {
              if(error?.message?.includes('Network')) {
                errorMsg = '网络连接异常，请检查网络';
              } else if(error?.message?.includes('timeout')) {
                errorMsg = '请求超时，请稍后重试';
              } else if(error?.response && error.response.status) {
                const status = error.response.status;
                if(status === 400) {
                  errorMsg = '请求参数错误，请检查后重试';
                } else if(status === 401) {
                  errorMsg = '未授权，请重新登录';
                } else if(status === 403) {
                  errorMsg = '无权限执行此操作';
                } else if(status === 404) {
                  errorMsg = '接口不存在';
                } else if(status >= 500) {
                  errorMsg = '服务器错误，请稍后重试';
                }
              }
            } else {
              errorMsg = errorMsg || '操作失败';
              if(!errorMsg.startsWith('❌') && !errorMsg.startsWith('⚠️') && !errorMsg.startsWith('⏰') && !errorMsg.startsWith('📊')) {
                errorMsg = `❌ ${errorMsg}`;
              }
            }

            this.$message({
              message: errorMsg,
              type: errorType,
              duration: errorType === 'warning' ? 5000 : 3000,
              showClose: true
            });

            const taskName = this.resolveTaskNameByMethod(data?.method);
            this.markTaskFailed(taskName, errorMsg);
          });
      },
      resolveTaskNameByMethod(method) {
        if(!method) {
          return '';
        }
        if(method === 'AI排版' || method === '智能排版') {
          return '智能排版';
        }
        if(method === 'AI评分' || method === '智能评分') {
          return '智能评分';
        }
        return '';
      },
      markTaskFailed(taskName, errorMsg) {
        if(!taskName) {
          return;
        }
        const targetAI = this.enabledAIs.find(ai => ai.name === taskName);
        if(targetAI && targetAI.status === 'running') {
          targetAI.status = 'failed';
          if(targetAI.progressLogs.length > 0) {
            const lastLog = targetAI.progressLogs[0];
            lastLog.isCompleted = true;
            lastLog.content = `❌ 任务失败：${errorMsg}`;
          }
          this.$forceUpdate();
        }
      },
      // 🔥 移除硬编码的辅助方法，这些方法已不再使用
      getCapabilityType(ai, value) {
        // 此方法已废弃
        return 'info';
      },

      getCapabilityPlain(ai, value) {
        // 此方法已废弃
        return true;
      },
      // 🔥 toggleCapability 已废弃，使用 handleSelectChange 和handleButtonClick 代替
      
      getStatusText(status) {
        switch(status) {
          case "idle":
            return "等待中";
          case "running":
            return "正在执行";
          case "completed":
            return "已完成";
          case "failed":
            return "执行失败";
          default:
            return "未知状态";
        }
      },
      getStatusIcon(status) {
        switch(status) {
          case "idle":
            return "el-icon-time";
          case "running":
            return "el-icon-loading";
          case "completed":
            return "el-icon-check success-icon";
          case "failed":
            return "el-icon-close error-icon";
          default:
            return "el-icon-question";
        }
      },
      renderMarkdown(text) {
        return marked(text);
      },
      // HTML转纯文本
      htmlToText(html) {
        const tempDiv = document.createElement("div");
        tempDiv.innerHTML = html;
        return tempDiv.textContent || tempDiv.innerText || "";
      },

      // HTML转Markdown
      htmlToMarkdown(html) {
        return this.turndownService.turndown(html);
      },

      copyResult(content) {
        // 将HTML转换为纯文本
        const plainText = this.htmlToText(content);
        const textarea = document.createElement("textarea");
        textarea.value = plainText;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand("copy");
        document.body.removeChild(textarea);
        console.log("已复制纯文本到剪贴板");
      },

      exportResult(result) {
        // 将HTML转换为Markdown
        const markdown = result.content;
        const blob = new Blob([markdown], { type: "text/markdown" });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `${result.aiName}_结果_${new Date()
          .toISOString()
          .slice(0, 10)}.md`;
        link.click();
        URL.revokeObjectURL(link.href);
        console.log("已导出Markdown文件");
      },

      openShareUrl(shareUrl) {
        if(shareUrl) {
          window.open(shareUrl, "_blank");
        } else {
          this.$message.warning("暂无原链接");
        }
      },
      showLargeImage(imageUrl) {
        this.currentLargeImage = imageUrl;
        this.showImageDialog = true;
        // 找到当前图片的索引，设置轮播图的初始位置
        const currentIndex = this.screenshots.indexOf(imageUrl);
        if(currentIndex !== -1) {
          this.$nextTick(() => {
            const carousel = this.$el.querySelector(".image-dialog .el-carousel");
            if(carousel && carousel.__vue__) {
              carousel.__vue__.setActiveItem(currentIndex);
            }
          });
        }
      },
      closeLargeImage() {
        this.showImageDialog = false;
        this.currentLargeImage = "";
      },
      // WebSocket 相关方法
      initWebSocket(id) {
        const wsUrl = process.env.VUE_APP_WS_API + `mypc-${id}`;
        console.log("WebSocket URL:", process.env.VUE_APP_WS_API);
        websocketClient.connect(wsUrl, (event) => {
          switch(event.type) {
            case "open":
              // this.$message.success('');
              break;
            case "message":
              this.handleWebSocketMessage(event.data);
              break;
            case "close":
              // WebSocket连接已关闭，不再显示警告
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
        
        // 🔥 添加消息接收确认，帮助调试消息丢失问题
        console.log('📨 [WebSocket] 收到消息, 类型:', dataObj.type);
        if (dataObj.messageId) {
          console.log('📨 [WebSocket] 消息ID:', dataObj.messageId);
        }
        if (dataObj.taskId) {
          console.log('📨 [WebSocket] 任务ID:', dataObj.taskId);
        }
        if (dataObj.userId) {
          console.log('📨 [WebSocket] 用户ID:', dataObj.userId, '当前用户ID:', this.userId);
        }
        if (dataObj.aiName) {
          console.log('📨 [WebSocket] AI名称:', dataObj.aiName);
        }

        // 处理chatId消息
        if(dataObj.type === "RETURN_YBT1_CHATID" && dataObj.chatId) {
          this.userInfoReq.toneChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_DB_CHATID" && dataObj.chatId) {
          this.userInfoReq.dbChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_YBDS_CHATID" && dataObj.chatId) {
          this.userInfoReq.ybDsChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_BAIDU_CHATID" && dataObj.chatId) {
          this.userInfoReq.baiduChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_DEEPSEEK_CHATID" && dataObj.chatId) {
          this.userInfoReq.deepseekChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_METASO_CHATID" && dataObj.chatId) {
          this.userInfoReq.metasoChatId = dataObj.chatId;
        } else if(dataObj.type === "RETURN_ZHZD_CHATID" && dataObj.chatId) {
          this.userInfoReq.zhzdChatId = dataObj.chatId;
        }
        else if(dataObj.type === 'RETURN_TY_CHATID' && dataObj.chatId) {
          this.userInfoReq.tyChatId = dataObj.chatId;
        }
        else if(dataObj.type === "RETURN_MAX_CHATID" && dataObj.chatId) {
          this.userInfoReq.maxChatId = dataObj.chatId;
        }

        // 处理进度日志消息
        if(dataObj.type === "RETURN_PC_TASK_LOG" && dataObj.aiName) {
          // 🔥 使用统一的消息验证逻辑
          if(!this.shouldProcessMessage(dataObj)) {
            return; // 忽略其他任务的消息
          }

          const targetAI = this.enabledAIs.find(
            (ai) => ai.name === dataObj.aiName
          );
          if(targetAI && targetAI.status === "running") { // 只在运行状态时添加日志
            // 检查是否已存在相同内容的日志，避免重复添加
            const existingLog = targetAI.progressLogs.find(log => log.content === dataObj.content);
            if(!existingLog) {
              // 将新进度添加到数组开头
              targetAI.progressLogs.unshift({
                content: dataObj.content,
                timestamp: new Date(),
                isCompleted: false,
                taskId: this.userInfoReq.taskId // 记录任务ID
              });
            }
          }
          return;
        }

        // 处理媒体日志消息
        if(dataObj.type === "RETURN_MEDIA_TASK_LOG") {
          // 只处理当前任务的日志消息
          // if(dataObj.taskId && dataObj.taskId !== this.userInfoReq.taskId) {
          //   return; // 忽略其他任务的消息
          // }

          const targetAI = this.enabledAIs.find(
            (ai) => ai.name === '媒体投递'
          );
          if(targetAI && targetAI.status === "running") { // 只在运行状态时添加日志
            // 检查是否已存在相同内容的日志，避免重复添加
            const existingLog = targetAI.progressLogs.find(log => log.content === dataObj.content);
            if(!existingLog) {
              // 将新进度添加到数组开头
              targetAI.progressLogs.unshift({
                content: dataObj.content,
                timestamp: new Date(),
                isCompleted: false,
                taskId: this.userInfoReq.taskId // 记录任务ID
              });
            }
          }
          return;
        }

        // 处理截图消息
        if(dataObj.type === "RETURN_PC_TASK_IMG" && dataObj.url) {
          // 🔥 截图消息只验证用户ID，不验证任务ID（因为截图可能没有taskId）
          const messageUserId = dataObj.userId ? String(dataObj.userId).trim() : "";
          const currentUserId = this.userId ? String(this.userId).trim() : "";
          
          if (messageUserId !== "" && currentUserId !== "" && messageUserId !== currentUserId) {
            console.log(`⚠️ [截图过滤] 用户ID不匹配 - 消息用户:${dataObj.userId}, 当前用户:${this.userId}`);
            return; // 忽略其他用户的截图
          }

          // 将新的截图添加到数组开头
          this.screenshots.unshift(dataObj.url);
          console.log(`📷 [截图消息] 添加新截图，当前截图数量: ${this.screenshots.length}`);
          return;
        }

        // 处理智能评分结果
        if(dataObj.type === "RETURN_WKPF_RES") {
          const wkpfAI = this.enabledAIs.find((ai) => ai.name === "智能评分");
          if(wkpfAI) {
            wkpfAI.status = "completed";
           if(wkpfAI.progressLogs.length > 0) {
             wkpfAI.progressLogs[0].isCompleted = true;
           }
            // 添加评分结果到results最前面
            this.results.unshift({
              aiName: "智能评分",
              content: dataObj.draftContent,
              shareUrl: dataObj.shareUrl || "",
              shareImgUrl: dataObj.shareImgUrl || "",
              timestamp: new Date(),
            });
            this.activeResultTab = "result-0";

            // 智能评分完成时，再次保存历史记录
            this.saveHistory();
          }
          return;
        }

        // 处理智能排版结果
        if(dataObj.type === "RETURN_ZNPB_RES") {
          const znpbAI = this.enabledAIs.find((ai) => ai.name === "智能排版");
          if(znpbAI) {
            znpbAI.status = "completed";
            if(znpbAI.progressLogs.length > 0) {
              znpbAI.progressLogs[0].isCompleted = true;
            }


            // 添加排版结果到results最前面
            this.results.unshift({
              aiName: "智能排版" + this.mediaList.filter(media => media.name === this.selectedMedia)[0].label,
              content: dataObj.draftContent,
              shareUrl: dataObj.shareUrl || "",
              shareImgUrl: dataObj.shareImgUrl || "",
              timestamp: new Date(),
            });
            this.activeResultTab = "result-0";
            // 智能排版完成时，保存历史记录
            this.saveHistory();


            //TODO:扩展媒体投递
            // this.pushToWechatWithContent(dataObj.draftContent);
          }
          return;
        }


        // 处理媒体投递结果
        if(dataObj.type.includes('DELIVERY_RES')) {
          const mediaAI = this.enabledAIs.find((ai) => ai.name === "媒体投递");
        if(mediaAI) {
  mediaAI.status = "completed";
  if(mediaAI.progressLogs.length > 0) {
    mediaAI.progressLogs[0].isCompleted = true;
  }
}
          this.$message(dataObj.message);
          return;
        }






        // 根据消息类型更新对应AI的状态和结果
        let targetAI = null;
        switch(dataObj.type) {
          case "RETURN_YB_RES":
          case "RETURN_YBT1_RES":
          case "RETURN_YBDS_RES":
            console.log("收到腾讯元宝消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "腾讯元宝");
            break;
          case "RETURN_DB_RES":
            console.log("收到豆包消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "豆包");
            break;
          case "RETURN_BAIDU_RES":
            console.log("收到百度AI消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "百度AI");
            break;
          case "RETURN_DEEPSEEK_RES":
            console.log("收到DeepSeek消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "DeepSeek");
            break;
          // 通义千问已注释
          // case 'RETURN_TY_RES':
          //   console.log('收到通义千问消息:', data);
          //   targetAI = this.enabledAIs.find(ai => ai.name === '通义千问');
          //   break;
          case "RETURN_METASO_RES":
            console.log("收到秘塔消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "秘塔");
            break;
          case "RETURN_ZHZD_RES":
            console.log("收到知乎直答消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "知乎直答");
            break;
          // 以下是状态和chatId消息，不需要处理结果，直接返回
          case "RETURN_YBT1_CHATID":
          case "RETURN_YBDS_CHATID":
          case "RETURN_DB_CHATID":
          case "RETURN_DEEPSEEK_CHATID":
          case "RETURN_METASO_CHATID":
          case "RETURN_BAIDU_CHATID":
          case "RETURN_ZHZD_CHATID":
          case "RETURN_YB_STATUS":
          case "RETURN_DB_STATUS":
          case "RETURN_DEEPSEEK_STATUS":
          case "RETURN_METASO_STATUS":
          case "RETURN_BAIDU_STATUS":
          case "RETURN_ZHZD_STATUS":
          case "AI智能对话":
            // 这些是状态消息和chatId消息，不需要在此处理
            console.log("ℹ️ [消息处理] 收到状态/chatId消息，类型:", dataObj.type);
            return;

        }

        if(targetAI) {
          console.log(`🎯 [结果处理] 找到目标AI: ${targetAI.name}`);
          console.log(`📋 [结果处理] 当前taskId: ${this.userInfoReq.taskId}, 消息taskId: ${dataObj.taskId}`);
          console.log(`📊 [结果处理] AI当前状态: ${targetAI.status}`);

          // 🔥 使用统一的消息验证逻辑
          if(!this.shouldProcessMessage(dataObj)) {
            return; // 忽略其他任务的消息
          }

          // 检查AI是否还在运行状态，避免重复处理
          if(targetAI.status !== "running") {
            console.warn(`⚠️ [结果处理] AI状态不是running，跳过处理: ${targetAI.status}`);
            // 如果状态已经是completed，但收到新结果，说明是重复消息或延迟消息
            // 不返回，继续处理，确保结果能被保存
          }

          // 更新AI状态为已完成
          targetAI.status = "completed";
          console.log(`✅ [结果处理] 更新${targetAI.name}状态为completed`);

          // 将最后一条进度消息标记为已完成
          if(targetAI.progressLogs.length > 0) {
            targetAI.progressLogs[0].isCompleted = true;
          }

          // 添加结果到数组开头
          const resultIndex = this.results.findIndex(
            (r) => r.aiName === targetAI.name && r.taskId === this.userInfoReq.taskId
          );
          console.log(`🔍 [结果处理] 检查是否已存在结果, 索引: ${resultIndex}`);

          if(resultIndex === -1) {
            console.log(`➕ [结果处理] 添加新结果到results`);
            this.results.unshift({
              aiName: targetAI.name,
              content: dataObj.draftContent,
              shareUrl: dataObj.shareUrl || "",
              shareImgUrl: dataObj.shareImgUrl || "",
              timestamp: new Date(),
              taskId: this.userInfoReq.taskId // 记录任务ID
            });
            this.activeResultTab = "result-0";
          } else {
            console.log(`🔄 [结果处理] 更新已存在的结果`);
            this.results.splice(resultIndex, 1);
            this.results.unshift({
              aiName: targetAI.name,
              content: dataObj.draftContent,
              shareUrl: dataObj.shareUrl || "",
              shareImgUrl: dataObj.shareImgUrl || "",
              timestamp: new Date(),
              taskId: this.userInfoReq.taskId // 记录任务ID
            });
            this.activeResultTab = "result-0";
          }
          console.log(`💾 [结果处理] 保存历史记录`);
          this.saveHistory();
          console.log(`✨ [结果处理] ${targetAI.name}结果处理完成`);
        } else {
          console.warn(`⚠️ [结果处理] 未找到目标AI，消息类型: ${dataObj.type}`);
        }


      },

      closeWebSocket() {
        websocketClient.close();
      },

      sendMessage(data) {
        if(websocketClient.send(data)) {
          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom();
          });
        } else {
          this.$message.error("WebSocket未连接");
        }
      },
      toggleAIExpansion(ai) {
        ai.isExpanded = !ai.isExpanded;
      },

      formatTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleTimeString("zh-CN", {
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
          hour12: false,
        });
      },
      showScoreDialog() {
        this.scoreDialogVisible = true;
        this.selectedResults = [];
        getAllScorePrompt().then(response => {
          this.scorePromptList = response.data || [];
        });
      },

      async handleScore() {
        if(!this.canScore) return;

        const response = await getScoreWord();

        // 获取选中的结果内容并按照指定格式拼接
        const selectedContents = this.results
          .filter((result) => this.selectedResults.includes(result.aiName))
          .map((result) => {
            // 将HTML内容转换为纯文本
            const plainContent = this.htmlToText(result.content);
            return `${result.aiName}${response.data}${plainContent}\n`;
          })
          .join("\n");

        // 构建完整的评分提示内容
        const fullPrompt = `${this.scorePrompt}\n${selectedContents}`;

        // 构建评分请求
        const scoreRequest = {
          jsonrpc: "2.0",
          id: uuidv4(),
          method: "AI评分",
          params: {
            taskId: uuidv4(),
            userId: this.userId,
            corpId: this.corpId,
            userPrompt: fullPrompt,
            // roles: "zj-db-sdsk", // 默认使用豆包进行评分
            roles: "",
          },
        };
        let ai = this.aiList.filter(ai => ai.name === this.scoreAI)[0];

        // 安全检查：确保 ai 对象存在
        if (!ai) {
          console.error("未找到匹配的AI配置:", this.scoreAI);
          return;
        }

        {
          if(ai.name === "豆包") {
            scoreRequest.params.roles = scoreRequest.params.roles + "zj-db,";
            if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("deep_thinking")) {
              scoreRequest.params.roles = scoreRequest.params.roles + "zj-db-sdsk,";
            }
          }


          // 通义千问已注释
          // if(ai.name === '通义千问') {
          //   scoreRequest.params.roles = scoreRequest.params.roles + 'ty-qw,';
          //   if(ai.selectedCapability.includes("deep_thinking")) {
          //     scoreRequest.params.roles = scoreRequest.params.roles + 'ty-qw-sdsk,'
          //   }
          // }

          if(ai.name === '腾讯元宝') {
            // 根据选择的模型设置角色
            if(ai.selectedModel === 'hunyuan') {
              scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-pt,';
              if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("deep_thinking")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-sdsk,';
              }
              if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("web_search")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-lwss,';
              }
            } else if(ai.selectedModel === 'deepseek') {
              scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-pt,';
              if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("deep_thinking")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-sdsk,';
              }
              if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("web_search")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-lwss,';
              }
            }
          }
          if(ai.name === '百度AI') {
            scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-agent,';
            if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("deep_search")) {
              scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-sdss,';
            } else if(ai.isModel) {
              if(ai.isWeb) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-web,';
              }

              if(ai.selectedModel && ai.selectedModel.includes("dsr1")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-dsr1,';
              } else if(ai.selectedModel && ai.selectedModel.includes("dsv3")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-dsv3,';
              } else if(ai.selectedModel && ai.selectedModel.includes("wenxin")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-wenxin,';
              }
            }

          }

          if(ai.name === "DeepSeek") {
            scoreRequest.params.roles = scoreRequest.params.roles + "deepseek,";
            if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("deep_thinking")) {
              scoreRequest.params.roles = scoreRequest.params.roles + "ds-sdsk,";
            }
            if(ai.selectedCapabilities && Array.isArray(ai.selectedCapabilities) && ai.selectedCapabilities.includes("web_search")) {
              scoreRequest.params.roles = scoreRequest.params.roles + "ds-lwss,";
            }
          }

          if(ai.name === "秘塔") {
            scoreRequest.params.roles = scoreRequest.params.roles + "mita,";
            if(ai.selectedCapabilities === "fast") {
              scoreRequest.params.roles = scoreRequest.params.roles + "metaso-jisu,";
            }
            if(ai.selectedCapabilities === "fast_thinking") {
              scoreRequest.params.roles = scoreRequest.params.roles + "metaso-jssk,";
            }
            if(ai.selectedCapabilities === "long_thinking") {
              scoreRequest.params.roles = scoreRequest.params.roles + "metaso-csk,";
            }
          }

          if(ai.name === "知乎直答") {
            scoreRequest.params.roles = scoreRequest.params.roles + "zhzd-chat,";
            // 使用单选思考模式
            if(ai.selectedCapability === "deep_thinking") {
              scoreRequest.params.roles = scoreRequest.params.roles + "zhzd-sdsk,";
            } else if(ai.selectedCapability === "fast_answer") {
              scoreRequest.params.roles = scoreRequest.params.roles + "zhzd-ks,";
            } else if(ai.selectedCapability === "smart_thinking") {
              scoreRequest.params.roles = scoreRequest.params.roles + "zhzd-zn,";
            } else {
              // 默认智能思考
              scoreRequest.params.roles = scoreRequest.params.roles + "zhzd-zn,";
            }
          }
        }

        // 发送评分请求
        console.log("参数", scoreRequest);
        this.message(scoreRequest);
        this.scoreDialogVisible = false;

        // 创建智能评分AI节点
        const wkpfAI = {
          name: "智能评分",
          avatar: require("../../../assets/ai/yuanbao.png"),
          capabilities: [],
          selectedCapabilities: [],
          enabled: true,
          status: "running",
          progressLogs: [
            {
              content: "智能评分任务已提交，正在评分...",
              timestamp: new Date(),
              isCompleted: false,
              type: "智能评分",
            },
          ],
          isExpanded: true,
        };

        // 检查是否已存在智能评分
        const existIndex = this.enabledAIs.findIndex(
          (ai) => ai.name === "智能评分"
        );
        if(existIndex === -1) {
          // 如果不存在，添加到数组开头
          this.enabledAIs.unshift(wkpfAI);
        } else {
          // 如果已存在，更新状态和日志
          this.enabledAIs[existIndex] = wkpfAI;
          // 将智能评分移到数组开头
          const wkpf = this.enabledAIs.splice(existIndex, 1)[0];
          this.enabledAIs.unshift(wkpf);
        }

        this.$forceUpdate();
        console.log("评分请求已发送，请等待结果");
      },
      // 显示历史记录抽屉
      showHistoryDrawer() {
        this.historyDrawerVisible = true;
        // 延迟加载历史记录，避免阻塞UI
        this.$nextTick(() => {
          this.loadChatHistory(1);
        });
      },

      // 关闭历史记录抽屉
      handleHistoryDrawerClose() {
        this.historyDrawerVisible = false;
      },

      // 加载历史记录
      async loadChatHistory(isAll) {
        this.historyLoading = true;
        try {
          console.log(`📂 [加载历史] 请求参数 isAll=${isAll}, userId=${this.userId}`);
          const res = await getChatHistory(this.userId, isAll);
          
          if(res.code === 200) {
            this.chatHistory = res.data || [];
            console.log(`✅ [加载历史] 成功加载 ${this.chatHistory.length} 条记录`);
            
            // 🔍 检查数据结构
            if (this.chatHistory.length > 0) {
              const uniqueChatIds = [...new Set(this.chatHistory.map(item => item.chatId))];
              console.log(`📋 [加载历史] 独特会话ID数: ${uniqueChatIds.length}`);
              
              // 显示每个chatId的记录数
              uniqueChatIds.forEach(chatId => {
                const count = this.chatHistory.filter(item => item.chatId === chatId).length;
                const firstPrompt = this.chatHistory.find(item => item.chatId === chatId)?.userPrompt;
                console.log(`  📝 chatId=${chatId}: ${count}条记录, 首条: ${firstPrompt?.substring(0, 30)}...`);
              });
            }
          } else {
            console.error('❌ [加载历史] 服务器返回错误:', res);
            this.$message.error(res.msg || '加载历史记录失败');
          }
        } catch(error) {
          console.error("❌ [加载历史] 请求异常:", error);
          this.$message.error("加载历史记录失败");
        } finally {
          this.historyLoading = false;
        }
      },

      // 格式化历史记录时间
      formatHistoryTime(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleTimeString("zh-CN", {
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        });
      },

      // 获取历史记录日期分组
      getHistoryDate(timestamp) {
        const date = new Date(timestamp);
        const today = new Date();
        const yesterday = new Date(today);
        yesterday.setDate(yesterday.getDate() - 1);
        const twoDaysAgo = new Date(today);
        twoDaysAgo.setDate(twoDaysAgo.getDate() - 2);
        const threeDaysAgo = new Date(today);
        threeDaysAgo.setDate(threeDaysAgo.getDate() - 3);

        if(date.toDateString() === today.toDateString()) {
          return "今天";
        } else if(date.toDateString() === yesterday.toDateString()) {
          return "昨天";
        } else if(date.toDateString() === twoDaysAgo.toDateString()) {
          return "两天前";
        } else if(date.toDateString() === threeDaysAgo.toDateString()) {
          return "三天前";
        } else {
          return date.toLocaleDateString("zh-CN", {
            year: "numeric",
            month: "long",
            day: "numeric",
          });
        }
      },

      // 加载历史记录项
      loadHistoryItem(item) {
        try {
          const historyData = JSON.parse(item.data);
          
          // 🔥 恢复AI配置：使用当前数据库的AI列表，只恢复历史记录中的选中状态
          if(historyData.aiList) {
            const historicalAiList = historyData.aiList;
            
            // 遍历当前AI列表，恢复历史记录中的状态
            this.aiList.forEach(currentAI => {
              // 在历史记录中查找同名AI
              const historicalAI = historicalAiList.find(ai => ai.name === currentAI.name);
              
              if(historicalAI) {
                // 恢复历史记录中的状态和选项，但保留当前的onlineStatus和数据库配置
                currentAI.enabled = historicalAI.enabled;
                currentAI.status = historicalAI.status || 'idle';
                currentAI.progressLogs = historicalAI.progressLogs || [];
                currentAI.isExpanded = historicalAI.isExpanded !== undefined ? historicalAI.isExpanded : true;
                currentAI.selectedOptions = historicalAI.selectedOptions || currentAI.selectedOptions;
                
                // 兼容旧格式
                if(historicalAI.selectedCapabilities) {
                  currentAI.selectedCapabilities = historicalAI.selectedCapabilities;
                }
                if(historicalAI.selectedModel) {
                  currentAI.selectedModel = historicalAI.selectedModel;
                }
              }
            });
            
            console.log('✅ [历史记录] AI状态已恢复（保留在线状态和数据库配置）');
          }
          // 恢复提示词输入
          this.promptInput = historyData.promptInput || "";
          // 恢复任务流程 - 确保包含所有启用的AI
          if(historyData.enabledAIs && historyData.enabledAIs.length > 0) {
            // 合并历史记录中的enabledAIs和当前aiList中启用的AI
            const historicalEnabledAIs = historyData.enabledAIs;
            const currentEnabledAIs = this.aiList.filter((ai) => ai.enabled);

            // 创建合并后的enabledAIs，保留历史记录中的状态，同时包含当前启用的AI
            this.enabledAIs = [...historicalEnabledAIs];

            // 添加当前启用的但不在历史记录中的AI
            currentEnabledAIs.forEach(currentAI => {
              const exists = this.enabledAIs.find(historicalAI => historicalAI.name === currentAI.name);
              if(!exists) {
                // 为新增的AI设置为idle状态
                const newAI = {
                  ...currentAI,
                  status: "idle",
                  progressLogs: [],
                  isExpanded: true
                };
                this.enabledAIs.push(newAI);
              }
            });
          } else {
            // 如果没有历史记录，使用当前启用的AI，设置为idle状态
            this.enabledAIs = this.aiList.filter((ai) => ai.enabled).map(ai => ({
              ...ai,
              status: "idle",
              progressLogs: [],
              isExpanded: true
            }));
          }
          // 恢复主机可视化
          this.screenshots = historyData.screenshots || [];
          // 恢复执行结果
          this.results = historyData.results || [];
          // 🔥 恢复chatId（关键：确保续文使用相同的chatId）
          this.chatId = item.chatId || this.chatId;
          this.userInfoReq.chatId = item.chatId || ""; // 恢复到请求参数中
          this.userInfoReq.toneChatId = item.toneChatId || "";
          this.userInfoReq.ybDsChatId = item.ybDsChatId || "";
          this.userInfoReq.dbChatId = item.dbChatId || "";
          this.userInfoReq.deepseekChatId = item.deepseekChatId || "";
          this.userInfoReq.maxChatId = item.maxChatId || "";
          this.userInfoReq.baiduChatId = item.baiduChatId || "";

          this.userInfoReq.tyChatId = item.tyChatId || "";
          this.userInfoReq.metasoChatId = item.metasoChatId || "";
          this.userInfoReq.zhzdChatId = item.zhzdChatId || "";
          this.userInfoReq.isNewChat = false;
          
          console.log('📝 [加载历史] 恢复会话ID:', item.chatId);

          // 展开相关区域
          this.activeCollapses = ["ai-selection", "prompt-input"];
          this.taskStarted = true;

          console.log("历史记录加载成功");
          this.historyDrawerVisible = false;
        } catch(error) {
          console.error("加载历史记录失败:", error);
          this.$message.error("加载历史记录失败");
        }
      },

      // 保存历史记录
      async saveHistory() {
        // if (!this.taskStarted || this.enabledAIs.some(ai => ai.status === 'running')) {
        //   return;
        // }

        const historyData = {
          aiList: this.aiList,
          promptInput: this.promptInput,
          enabledAIs: this.enabledAIs,
          screenshots: this.screenshots,
          results: this.results,
          chatId: this.chatId,
          toneChatId: this.userInfoReq.toneChatId,
          ybDsChatId: this.userInfoReq.ybDsChatId,
          dbChatId: this.userInfoReq.dbChatId,
          deepseekChatId: this.userInfoReq.deepseekChatId,
          baiduChatId: this.userInfoReq.baiduChatId,
          tyChatId: this.userInfoReq.tyChatId,
          maxChatId: this.userInfoReq.maxChatId,

          metasoChatId: this.userInfoReq.metasoChatId,
        };

        try {
          await saveUserChatData({
            userId: this.userId,
            userPrompt: this.promptInput,
            data: JSON.stringify(historyData),
            chatId: this.chatId,
            toneChatId: this.userInfoReq.toneChatId,
            ybDsChatId: this.userInfoReq.ybDsChatId,
            dbChatId: this.userInfoReq.dbChatId,
            baiduChatId: this.userInfoReq.baiduChatId,
            deepseekChatId: this.userInfoReq.deepseekChatId,
            tyChatId: this.userInfoReq.tyChatId,
            maxChatId: this.userInfoReq.maxChatId,

            metasoChatId: this.userInfoReq.metasoChatId,
            zhzdChatId: this.userInfoReq.zhzdChatId,
          });
        } catch(error) {
          console.error("保存历史记录失败:", error);
          this.$message.error("保存历史记录失败");
        }
      },

      // 修改折叠切换方法
      toggleHistoryExpansion(item) {
        // 直接使用chatId作为key
        const key = item.chatId;
        const currentState = this.expandedHistoryItems[key];
        this.$set(this.expandedHistoryItems, key, !currentState);
      },

      // 创建新对话
      createNewChat() {
        // 🔥 重置所有数据，生成新的会话ID
        this.chatId = uuidv4();
        console.log('📝 [新建会话] 生成新的chatId:', this.chatId);
        this.isNewChat = true;
        this.promptInput = "";
        this.taskStarted = false;
        this.screenshots = [];
        this.results = [];
        this.enabledAIs = [];

        // 🔥 只重置AI的状态和选项，不重置整个aiList（保留数据库配置）
        this.aiList.forEach(ai => {
          ai.status = "idle";
          ai.progressLogs = [];
          ai.isExpanded = true;
          
          // 重置AI的选项为默认值
          if (ai.options) {
            ai.options.forEach(option => {
              if (option.type === 'select') {
                // 找到默认选中的值
                const defaultValue = option.values?.find(v => v.default)?.value;
                ai.selectedOptions = ai.selectedOptions || {};
                ai.selectedOptions[option.id] = defaultValue || '';
              } else if (option.type === 'button') {
                // 按钮默认不选中
                ai.selectedOptions = ai.selectedOptions || {};
                ai.selectedOptions[option.id] = false;
              }
            });
          }
        });

        this.userInfoReq = {
          userPrompt: "",
          userId: this.userId,
          corpId: this.corpId,
          taskId: "",
          roles: "",
          toneChatId: "",
          ybDsChatId: "",
          dbChatId: "",
          baiduChatId: "",
          deepseekChatId: "",
          tyChatId: "",
          metasoChatId: "",
          maxChatId: "",
          zhzdChatId: "",
          chatId: "", // 🔥 重置chatId
          isNewChat: true,
        };

        // 展开相关区域
        this.activeCollapses = ["ai-selection", "prompt-input"];

        console.log("✅ [新建对话] 已创建新对话，AI列表已重置状态（保留在线状态和数据库配置）");
      },

      // 加载上次会话
      async loadLastChat() {
        try {
          const res = await getChatHistory(this.userId, 0);
          if(res.code === 200 && res.data && res.data.length > 0) {
            // 获取最新的会话记录
            const lastChat = res.data[0];
            this.loadHistoryItem(lastChat);
          }
        } catch(error) {
          console.error("加载上次会话失败:", error);
        }
      },

      // 判断是否为图片文件
      isImageFile(url) {
        if(!url) return false;
        const imageExtensions = [
          ".jpg",
          ".jpeg",
          ".png",
          ".gif",
          ".bmp",
          ".webp",
          ".svg",
        ];
        const urlLower = url.toLowerCase();
        return imageExtensions.some((ext) => urlLower.includes(ext));
      },

      // 判断是否为PDF文件
      isPdfFile(url) {
        if(!url) return false;
        return url.toLowerCase().includes(".pdf");
      },

      // 根据AI名称获取图片样式
      getImageStyle(aiName) {
        const widthMap = {
          baidu: "700px",
          DeepSeek: "700px",
          豆包: "560px",
          "腾讯元宝T1": "700px",
          "腾讯元宝DS": "700px",
          // 通义千问: "700px", // 通义千问已注释
          秘塔: "700px",
        };

        const width = widthMap[aiName] || "560px"; // 默认宽度

        return {
          width: width,
          height: "auto",
        };
      },

      // 投递到媒体
      handlePushToMedia(result) {
        this.currentLayoutResult = result;
        this.showLayoutDialog(result);
      },

      // 显示智能排版对话框
      showLayoutDialog(result) {
        this.currentLayoutResult = result;
        this.layoutDialogVisible = true;
        // 加载当前选择媒体的提示词
        this.loadMediaPrompt(this.selectedMedia);
      },

      loadScorePrompt() {
        this.scorePrompt = this.scorePromptList.filter(prompt => prompt.name === this.selectedScorePrompt)[0].prompt;
      },
      // 加载媒体提示词
      async loadMediaPrompt(media) {
        if(!media) return;

        let platformId;
        if(media === 'wechat') {
          platformId = 'wechat_layout';
        }
        this.layoutPrompt = (this.currentLayoutResult ? this.currentLayoutResult.content : '');
        // try {
        //   const response = await getMediaCallWord(platformId);
        //   if(response.code === 200) {
        //     this.layoutPrompt = response.data.wordContent + '\n\n' + (this.currentLayoutResult ? this.currentLayoutResult.content : '');
        //     this.layoutPrompt = (this.currentLayoutResult ? this.currentLayoutResult.content : '');
        //   } else {
        //     // 使用默认提示词
        //     this.layoutPrompt = this.getDefaultPrompt(media) + '\n\n' + (this.currentLayoutResult ? this.currentLayoutResult.content : '');
        //   }
        // } catch(error) {
        //   console.error('加载提示词失败:', error);
        //   // 使用默认提示词
        //   this.layoutPrompt = this.getDefaultPrompt(media) + '\n\n' + (this.currentLayoutResult ? this.currentLayoutResult.content : '');
        // }
      },

      // 获取默认提示词(仅在后端访问失败时使用)
      getDefaultPrompt(media) {
        if(media === 'wechat_layout') {
          return `请你对以下 HTML 内容进行排版优化，目标是用于微信公众号"草稿箱接口"的 content 字段，要求如下：

1. 仅返回 <body> 内部可用的 HTML 内容片段（不要包含 <!DOCTYPE>、<html>、<head>、<meta>、<title> 等标签）。
2. 所有样式必须以"内联 style"方式写入。
3. 保持结构清晰、视觉友好，适配公众号图文排版。
4. 请直接输出代码，不要添加任何注释或额外说明。
5. 不得使用 emoji 表情符号或小图标字符。
6. 不要显示为问答形式，以一篇文章的格式去调整

以下为需要进行排版优化的内容：`;

        } else {
          return '请对以下内容进行排版：';
        }
        return '请对以下内容进行排版：';
      },

      // 处理智能排版
      handleLayout() {
        if(!this.canLayout || !this.currentLayoutResult) return;
        this.layoutDialogVisible = false;

        // 公众号投递：创建排版任务
        this.createWechatLayoutTask();

      },



      // 创建公众号排版任务（保持原有逻辑）
      createWechatLayoutTask() {
        const layoutRequest = {
          jsonrpc: "2.0",
          id: uuidv4(),
          method: "AI排版",
          params: {
            taskId: uuidv4(),
            userId: this.userId,
            corpId: this.corpId,
            userPrompt: this.layoutPrompt,
            // roles: "znpb-ds,yb-deepseek-pt,yb-deepseek-sdsk,yb-deepseek-lwss,",
            roles: "",
            selectedMedia: this.selectedMedia,
          },
        };

        let ai = this.aiList.filter(ai => ai.name === this.layoutAI)[0];
        
        // 检查AI是否存在
        if (!ai) {
          this.$message.error(`未找到选中的排版AI: ${this.layoutAI}，请重新选择`);
          return;
        }
        
        // 确保 selectedCapabilities 是数组
        if (!ai.selectedCapabilities) {
          ai.selectedCapabilities = [];
        }
        if (!Array.isArray(ai.selectedCapabilities)) {
          ai.selectedCapabilities = [];
        }

        {
          if(ai.name === "豆包") {
            layoutRequest.params.roles = layoutRequest.params.roles + "zj-db,";
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zj-db-sdsk,";
            }
          }


          // 通义千问已注释
          // if(ai.name === '通义千问') {
          //   layoutRequest.params.roles = layoutRequest.params.roles + 'ty-qw,';
          //   if(ai.selectedCapability.includes("deep_thinking")) {
          //     layoutRequest.params.roles = layoutRequest.params.roles + 'ty-qw-sdsk,'
          //   }
          // }

          if(ai.name === '腾讯元宝') {
            // 根据选择的模型设置角色
            if(ai.selectedModel === 'hunyuan') {
              layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-pt,';
              if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_thinking")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-sdsk,';
              }
              if(ai.selectedCapabilities && ai.selectedCapabilities.includes("web_search")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-lwss,';
              }
            } else if(ai.selectedModel === 'deepseek') {
              layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-pt,';
              if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_thinking")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-sdsk,';
              }
              if(ai.selectedCapabilities && ai.selectedCapabilities.includes("web_search")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-lwss,';
              }
            }
          }
          if(ai.name === '百度AI') {
            layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-agent,';
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-sdss,';
            } else if(ai.isModel) {
              if(ai.isWeb) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-web,';
              }

              if(ai.selectedModel && ai.selectedModel.includes("dsr1")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-dsr1,';
              } else if(ai.selectedModel && ai.selectedModel.includes("dsv3")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-dsv3,';
              } else if(ai.selectedModel && ai.selectedModel.includes("wenxin")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-wenxin,';
              }
            }

          }

          if(ai.name === "DeepSeek") {
            layoutRequest.params.roles = layoutRequest.params.roles + "deepseek,";
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "ds-sdsk,";
            }
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("web_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "ds-lwss,";
            }
          }

          if(ai.name === "秘塔") {
            layoutRequest.params.roles = layoutRequest.params.roles + "mita,";
            if(ai.selectedCapabilities === "fast") {
              layoutRequest.params.roles = layoutRequest.params.roles + "metaso-jisu,";
            }
            if(ai.selectedCapabilities === "fast_thinking") {
              layoutRequest.params.roles = layoutRequest.params.roles + "metaso-jssk,";
            }
            if(ai.selectedCapabilities === "long_thinking") {
              layoutRequest.params.roles = layoutRequest.params.roles + "metaso-csk,";
            }
          }

          if(ai.name === "知乎直答") {
            layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-chat,";
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-sdsk,";
            }
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("all_web_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-qw,";
            }
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("zhihu_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-zh,";
            }
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("academic_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-xs,";
            }
            if(ai.selectedCapabilities && ai.selectedCapabilities.includes("personal_knowledge")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-wdzsk,";
            }
          }

        }

        console.log("公众号排版参数", layoutRequest);
        this.message(layoutRequest);

        const znpbAI = {
          name: "智能排版",
          avatar: require("../../../assets/ai/yuanbao.png"),
          capabilities: [],
          selectedCapabilities: [],
          enabled: true,
          status: "running",
          progressLogs: [
            {
              content: "智能排版任务已提交，正在排版...",
              timestamp: new Date(),
              isCompleted: false,
              type: "智能排版",
            },
          ],
          isExpanded: true,
        };

        // 检查是否已存在智能排版任务
        const existIndex = this.enabledAIs.findIndex(
          (ai) => ai.name === "智能排版"
        );
        // if(existIndex === -1) {
        //   this.enabledAIs.unshift(znpbAI);
        // } else {
        //   this.enabledAIs[existIndex] = znpbAI;
        //   const znpb = this.enabledAIs.splice(existIndex, 1)[0];
        //   this.enabledAIs.unshift(znpb);
        // }
        this.enabledAIs.unshift(znpbAI);


        this.$forceUpdate();
        console.log("排版请求已发送，请等待结果");
      },



      // 实际投递到公众号
      pushToMediaWithContent(result) {
        if(this.pushingToWechat) return;

        // 验证内容是否为空
        if(!result.content || result.content.trim() === '') {
          this.$message.error("投递内容为空，请先进行AI排版生成内容");
          return;
        }

        console.log("开始投递公众号！");
        this.pushingToWechat = true;
        this.pushOfficeNum += 1;

        const params = {
          contentText: result.content,
          shareUrl: result.shareUrl,
          userId: this.userId,
          num: this.pushOfficeNum,
          aiName: result.aiName,
        };
        let mediaLabel = result.aiName.substring(4);
        let mediaName = this.mediaList.filter(media => media.label === mediaLabel)[0].name;
        if(mediaName.includes('wechat')) {
          pushAutoOffice(params)
            .then((res) => {
              if(res.code === 200) {
                console.log("投递到公众号成功！");
              } else {
                this.$message.error(res.msg || "投递失败，请重试");
              }
            })
            .catch((error) => {
              console.error("投递到公众号失败:", error);
              // 提取错误消息
              let errorMsg = "投递失败，请重试";
              if(error.response && error.response.data && error.response.data.msg) {
                errorMsg = error.response.data.msg;
              } else if(error.message) {
                errorMsg = error.message;
              }

              // 针对常见错误给出友好提示
              if(errorMsg.includes("未绑定公众号")) {
                this.$message.error("⚠️ 未绑定公众号，请先在系统中绑定公众号后再进行投递");
              } else if(errorMsg.includes("内容格式错误")) {
                this.$message.error("⚠️ 内容格式错误，请检查AI排版结果是否包含《标题》格式");
              } else if(errorMsg.includes("内容解析失败") || errorMsg.includes("投递内容为空")) {
                this.$message.error("⚠️ 内容解析失败，请先完成AI排版后再投递");
              } else {
                this.$message.error(errorMsg);
              }
            })
            .finally(() => {
              this.pushingToWechat = false;
            });
        } else if(mediaName.includes('zhihu')) {
          // 构建知乎投递请求
          const mediaRequest = {
            jsonrpc: "2.0",
            id: uuidv4(),
            method: "媒体投递",
            params: {
              taskId: uuidv4(),
              userId: this.userId,
              corpId: this.corpId,
              aiName: result.aiName,
              userPrompt: result.content, // 传递排版后的内容
              selectedMedia: "zhihu_layout",
            },
          };
          this.message(mediaRequest);

          // 创建媒体投递任务节点（类似智能排版）
          const mediaDeliveryAI = {
            name: "媒体投递",
            avatar: require("../../../assets/ai/yuanbao.png"),
            capabilities: [],
            selectedCapabilities: [],
            enabled: true,
            status: "running",
            progressLogs: [
              {
                content: "知乎投递任务已提交，正在投递...",
                timestamp: new Date(),
                isCompleted: false,
                type: "媒体投递",
              },
            ],
            isExpanded: true,
          };

          // 将媒体投递任务添加到任务列表
          const existIndex = this.enabledAIs.findIndex(ai => ai.name === "媒体投递");
          if(existIndex === -1) {
            this.enabledAIs.unshift(mediaDeliveryAI);
          } else {
            this.enabledAIs[existIndex] = mediaDeliveryAI;
            const media = this.enabledAIs.splice(existIndex, 1)[0];
            this.enabledAIs.unshift(media);
          }

          this.$forceUpdate();
          console.log("知乎投递请求已发送，请等待结果");
          this.pushingToWechat = false;
        }
      },

      // 🔥 统一的消息验证方法
      shouldProcessMessage(dataObj) {
        // 用户ID验证：如果消息包含用户ID，必须匹配当前用户
        // 🔥 修复：确保转换为字符串后再调用trim()
        const messageUserId = dataObj.userId != null ? String(dataObj.userId).trim() : "";
        const currentUserId = this.userId != null ? String(this.userId).trim() : "";
        
        if (messageUserId !== "" && currentUserId !== "" && messageUserId !== currentUserId) {
          console.log(`⚠️ [消息过滤] 用户ID不匹配 - 消息用户:${dataObj.userId}, 当前用户:${this.userId}, 消息类型:${dataObj.type}`);
          return false;
        }
        
        // 任务ID验证：只有当消息明确包含taskId且与当前任务不匹配时才忽略
        // 🔥 修复：确保转换为字符串后再调用trim()
        const messageTaskId = dataObj.taskId != null ? String(dataObj.taskId).trim() : "";
        const currentTaskId = this.userInfoReq.taskId != null ? String(this.userInfoReq.taskId).trim() : "";
        
        if (messageTaskId !== "" && currentTaskId !== "" && messageTaskId !== currentTaskId) {
          console.log(`⚠️ [消息过滤] 任务ID不匹配 - 消息任务:${dataObj.taskId}, 当前任务:${this.userInfoReq.taskId}, 消息类型:${dataObj.type}`);
          return false;
        }
        
        console.log(`✅ [消息验证] 消息通过验证 - 任务ID:${dataObj.taskId || '无'}, 用户ID:${dataObj.userId || '无'}, 消息类型:${dataObj.type}`);
        return true;
      }

    }
  };
</script>

<style scoped>
  .ai-management-platform {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 30px;
  }

  .top-nav {
    background-color: #fff;
    padding: 15px 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .logo-area {
    display: flex;
    align-items: center;
  }

  .logo {
    height: 36px;
    margin-right: 10px;
  }

  .platform-title {
    margin: 0;
    font-size: 20px;
    color: #303133;
  }

  .main-content {
    padding: 0 30px;
    width: 90%;
    margin: 0 auto;
  }

  /* Element Plus 折叠面板样式 */
  :deep(.el-collapse-item__header) {
    font-size: 16px;
    color: #333;
    padding: 12px 20px;
    height: auto !important;
    line-height: normal !important;
  }

  :deep(.el-collapse-item__wrap) {
    overflow: visible;
  }

  .section-title {
    font-size: 18px;
    color: #606266;
    margin-bottom: 15px;
  }

  /* AI配置头部样式 */
  .ai-config-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 0;
  }

  .ai-config-header > span {
    flex: 1;
    font-weight: 600;
    font-size: 16px;
  }

  /* 全局控制按钮容器 */
  .global-controls {
    display: flex;
    align-items: center;
    gap: 10px;
    padding-left: 30px;  /* 增加左边距，与箭头距离更远 */
    margin-right: 15px;  /* 增加右边距，与折叠箭头保持距离 */
  }

  /* 全局控制按钮样式 */
  .global-control-btn {
    font-size: 13px !important;
    padding: 8px 16px !important;
    border-radius: 6px !important;
    white-space: nowrap !important;
    font-weight: 500 !important;
  }

  /* 开启状态按钮（绿色） */
  .global-control-btn.el-button--success {
    background-color: #67c23a !important;
    border-color: #67c23a !important;
    color: #fff !important;
  }

  .global-control-btn.el-button--success:hover {
    background-color: #85ce61 !important;
    border-color: #85ce61 !important;
  }

  /* 关闭状态按钮（红色） */
  .global-control-btn.el-button--danger {
    background-color: #f56c6c !important;
    border-color: #f56c6c !important;
    color: #fff !important;
  }

  .global-control-btn.el-button--danger:hover {
    background-color: #f78989 !important;
    border-color: #f78989 !important;
  }

  /* 腾讯元宝模型选择样式 */
  .model-selection {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
    gap: 8px;
  }

  .selection-label {
    font-size: 12px;
    color: #606266;
    font-weight: 500;
  }

  .ai-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    margin: 16px 20px;
    padding: 0;
  }

  .ai-card {
    width: 100%;
    box-sizing: border-box;
    transition: all 0.3s ease;
    border-radius: 12px;
    overflow: hidden;
  }

  .ai-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  }

  .ai-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding: 4px 0;
  }

  .ai-left {
    display: flex;
    align-items: center;
    flex: 1;
  }

  .ai-avatar {
    margin-right: 12px;
    flex-shrink: 0;
  }

  .ai-avatar img {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #f0f0f0;
  }

  .ai-name {
    font-weight: 600;
    font-size: 14px;
    color: #303133;
    line-height: 1.2;
  }

  .ai-status {
    display: flex;
    align-items: center;
  }

  .ai-capabilities {
    margin: 15px 0;
    width: 100%;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
  }

  .button-capability-group {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 8px;
  }

  .button-capability-group .el-button {
    margin: 0;
    border-radius: 16px;
    padding: 6px 12px;
  }

  .button-capability-group .el-button.is-plain:hover,
  .button-capability-group .el-button.is-plain:focus {
    background: #ecf5ff;
    border-color: #b3d8ff;
    color: #409eff;
  }

  .prompt-input-section {
    margin-bottom: 30px;
    padding: 0 20px 0 0px;
  }

  .prompt-input {
    margin-bottom: 10px;
    margin-left: 20px;
    width: 99%;
  }

  .prompt-footer {
    display: flex;
    margin-bottom: -30px;
    justify-content: space-between;
    align-items: center;
  }

  .word-count {
    font-size: 12px;
    padding-left: 20px;
  }

  .send-button {
    padding: 10px 20px;
  }

  .execution-status-section {
    margin-bottom: 30px;
    padding: 20px 0px 0px 0px;
  }

  .task-flow-card,
  .screenshots-card {
    height: 800px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .task-flow {
    padding: 15px;
    height: 800px;
    overflow-y: auto;
    background-color: #f5f7fa;
    border-radius: 4px;
  }

  .task-flow::-webkit-scrollbar {
    width: 6px;
  }

  .task-flow::-webkit-scrollbar-thumb {
    background-color: #c0c4cc;
    border-radius: 3px;
  }

  .task-flow::-webkit-scrollbar-track {
    background-color: #f5f7fa;
  }

  .task-item {
    margin-bottom: 15px;
    border-radius: 4px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;
  }

  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 15px;
    cursor: pointer;
    transition: background-color 0.3s;
    border-bottom: 1px solid #ebeef5;
  }

  .task-header:hover {
    background-color: #f5f7fa;
  }

  .header-left {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .header-left .el-icon-arrow-right {
    transition: transform 0.3s;
    font-size: 14px;
    color: #909399;
  }

  .header-left .el-icon-arrow-right.is-expanded {
    transform: rotate(90deg);
  }

  .progress-timeline {
    position: relative;
    margin: 0;
    padding: 15px 0;
  }

  .timeline-scroll {
    max-height: 200px;
    overflow-y: auto;
    padding: 0 15px;
  }

  .timeline-scroll::-webkit-scrollbar {
    width: 4px;
  }

  .timeline-scroll::-webkit-scrollbar-thumb {
    background-color: #c0c4cc;
    border-radius: 2px;
  }

  .timeline-scroll::-webkit-scrollbar-track {
    background-color: #f5f7fa;
  }

  .progress-item {
    position: relative;
    padding: 8px 0 8px 20px;
    display: flex;
    align-items: flex-start;
    border-bottom: 1px solid #f0f0f0;
  }

  .progress-item:last-child {
    border-bottom: none;
  }

  .progress-dot {
    position: absolute;
    left: 0;
    top: 12px;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background-color: #e0e0e0;
    flex-shrink: 0;
  }

  .progress-line {
    position: absolute;
    left: 4px;
    top: 22px;
    bottom: -8px;
    width: 2px;
    background-color: #e0e0e0;
  }

  .progress-content {
    flex: 1;
    min-width: 0;
  }

  .progress-time {
    font-size: 12px;
    color: #909399;
    margin-bottom: 4px;
  }

  .progress-text {
    font-size: 13px;
    color: #606266;
    line-height: 1.4;
    word-break: break-all;
  }

  .progress-item.completed .progress-dot {
    background-color: #67c23a;
  }

  .progress-item.completed .progress-line {
    background-color: #67c23a;
  }

  .progress-item.current .progress-dot {
    background-color: #409eff;
    animation: pulse 1.5s infinite;
  }

  .progress-item.current .progress-line {
    background-color: #409eff;
  }

  .ai-name {
    font-weight: 600;
    font-size: 14px;
    color: #303133;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .status-text {
    font-size: 13px;
    color: #606266;
  }

  .status-icon {
    font-size: 16px;
  }

  .success-icon {
    color: #67c23a;
  }

  .error-icon {
    color: #f56c6c;
  }

  @keyframes pulse {
    0% {
      box-shadow: 0 0 0 0 rgba(64, 158, 255, 0.4);
    }

    70% {
      box-shadow: 0 0 0 6px rgba(64, 158, 255, 0);
    }

    100% {
      box-shadow: 0 0 0 0 rgba(64, 158, 255, 0);
    }
  }

  .screenshot-image {
    width: 100%;
    height: 100%;
    object-fit: contain;
    cursor: pointer;
    transition: transform 0.3s;
  }

  .screenshot-image:hover {
    transform: scale(1.05);
  }

  .results-section {
    margin-top: 20px;
    padding: 0 10px;
  }

  .result-content {
    padding: 20px 30px;
  }

  .result-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #ebeef5;
  }

  .result-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  .result-buttons {
    display: flex;
    gap: 10px;
    align-items: center;
  }

  .share-link-btn,
  .push-media-btn {
    border-radius: 16px;
    padding: 6px 12px;
  }

  .markdown-content {
    margin-bottom: 20px;
    max-height: 400px;
    overflow-y: auto;
    padding: 15px 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    background-color: #fff;
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding: 0 10px;
  }

  @media (max-width: 1400px) {
    .ai-cards {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  @media (max-width: 1024px) {
    .ai-cards {
      grid-template-columns: repeat(2, 1fr);
    }
  }

  @media (max-width: 768px) {
    .ai-cards {
      grid-template-columns: repeat(1, 1fr);
    }
    
    .main-content {
      width: 95%;
      padding: 0 15px;
    }
  }

  .el-collapse {
    border-top: none;
    border-bottom: none;
  }

  .el-collapse-item__content {
    padding: 15px 0;
  }

  .ai-selection-section {
    margin-bottom: 0;
  }

  .prompt-input-section {
    margin-bottom: 30px;
    padding: 0 20px 0 0px;
  }

  .image-dialog .el-dialog__body {
    padding: 0;
  }

  .large-image-container {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #000;
  }

  .large-image {
    max-width: 100%;
    max-height: 80vh;
    object-fit: contain;
  }

  .image-dialog .el-carousel {
    width: 100%;
    height: 100%;
  }

  .image-dialog .el-carousel__container {
    height: 80vh;
  }

  .image-dialog .el-carousel__item {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #000;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .score-dialog-content {
    padding: 20px;
  }

  .selected-results {
    margin-bottom: 20px;
  }

  .result-checkbox {
    margin-right: 20px;
    margin-bottom: 10px;
  }

  .score-prompt-section {
    margin-top: 20px;
  }

  .score-prompt-input {
    margin-top: 10px;
  }

  .score-prompt-input .el-textarea__inner {
    min-height: 500px !important;
  }

  .dialog-footer {
    text-align: right;
  }

  .score-dialog .el-dialog {
    height: 95vh;
    margin-top: 2.5vh !important;
  }

  .score-dialog .el-dialog__body {
    height: calc(95vh - 120px);
    overflow-y: auto;
    padding: 20px;
  }

  .layout-dialog-content {
    padding: 20px;
  }

  .layout-prompt-section {
    margin-top: 20px;
  }

  .layout-prompt-input {
    margin-top: 10px;
  }

  .layout-prompt-input .el-textarea__inner {
    min-height: 500px !important;
  }

  .layout-dialog .el-dialog {
    height: 95vh;
    margin-top: 2.5vh !important;
  }

  .layout-dialog .el-dialog__body {
    height: calc(95vh - 120px);
    overflow-y: auto;
    padding: 20px;
  }

  .nav-buttons {
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .history-button {
    display: flex;
    align-items: center;
  }

  .history-icon {
    width: 24px;
    height: 24px;
    vertical-align: middle;
  }

  .history-content {
    padding: 20px;
  }

  .history-loading {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    color: #909399;
    font-size: 14px;
  }

  .history-loading i {
    font-size: 32px;
    margin-bottom: 12px;
  }

  .history-empty {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    color: #c0c4cc;
  }

  .history-empty i {
    font-size: 48px;
    margin-bottom: 12px;
  }

  .history-empty p {
    font-size: 14px;
    margin: 0;
  }

  .history-group {
    margin-bottom: 20px;
  }

  .history-date {
    font-size: 14px;
    color: #909399;
    margin-bottom: 10px;
    padding: 5px 0;
    border-bottom: 1px solid #ebeef5;
  }

  .history-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .history-item {
    margin-bottom: 12px;
    border-radius: 8px;
    background-color: #fff;
    border: 1px solid #e4e7ed;
    overflow: hidden;
    transition: all 0.3s ease;
  }

  .history-item:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-color: #409eff;
  }

  .history-parent {
    padding: 12px 15px;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .history-parent:hover {
    background-color: #f5f7fa;
  }

  .history-content-wrapper {
    flex: 1;
    min-width: 0;
  }

  .history-children {
    padding: 0;
    background-color: #fafafa;
    border-top: 1px solid #ebeef5;
  }

  .history-child-item {
    padding: 10px 15px 10px 45px;
    cursor: pointer;
    transition: background-color 0.3s;
    border-bottom: 1px solid #f0f0f0;
    position: relative;
  }

  .history-child-item:last-child {
    border-bottom: none;
  }

  .history-child-item:hover {
    background-color: #ecf5ff;
  }

  .history-child-item::before {
    content: '';
    position: absolute;
    left: 25px;
    top: 0;
    bottom: 0;
    width: 2px;
    background-color: #e4e7ed;
  }

  .history-child-content {
    position: relative;
  }

  .child-index {
    display: inline-block;
    font-size: 11px;
    color: #fff;
    background-color: #409eff;
    padding: 2px 8px;
    border-radius: 3px;
    margin-bottom: 6px;
    font-weight: 500;
  }

  .history-child-item .history-meta {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 4px;
    font-size: 11px;
    color: #909399;
  }

  .ai-count {
    color: #67c23a;
    font-weight: 500;
  }

  .history-header {
    display: flex;
    align-items: flex-start;
    gap: 10px;
  }

  .history-header .el-icon-arrow-right {
    font-size: 14px;
    color: #909399;
    transition: transform 0.3s;
    cursor: pointer;
    margin-top: 4px;
    flex-shrink: 0;
  }

  .history-header .el-icon-arrow-right.is-expanded {
    transform: rotate(90deg);
    color: #409eff;
  }

  .history-meta {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
  }

  .history-separator {
    color: #dcdfe6;
  }

  .history-chatid {
    font-family: 'Courier New', monospace;
    background-color: #f0f2f5;
    padding: 2px 6px;
    border-radius: 3px;
    cursor: help;
  }

  .round-label {
    color: #409eff;
    font-weight: 500;
    background-color: #ecf5ff;
    padding: 2px 6px;
    border-radius: 3px;
  }

  .children-count {
    color: #67c23a;
    font-weight: 500;
  }

  .no-children-hint {
    color: #c0c4cc;
    font-style: italic;
  }

  .history-prompt {
    font-size: 14px;
    color: #303133;
    line-height: 1.6;
    word-break: break-word;
    margin-bottom: 0;
  }

  .history-time {
    font-size: 12px;
    color: #909399;
  }

  .history-child-item .history-time {
    margin-top: 6px;
  }

  .capability-button {
    transition: all 0.3s;
  }

  .capability-button.el-button--primary {
    background-color: #409eff;
    border-color: #409eff;
    color: #fff;
  }

  .capability-button.el-button--info {
    background-color: #fff;
    border-color: #dcdfe6;
    color: #606266;
  }

  .capability-button.el-button--info:hover {
    color: #409eff;
    border-color: #c6e2ff;
    background-color: #ecf5ff;
  }

  .capability-button.el-button--primary:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
    color: #fff;
  }

  /* 分享内容样式 */
  .share-content {
    margin-bottom: 20px;
    padding: 15px 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    min-height: 600px;
    max-height: 800px;
    overflow: auto;
  }

  .share-image {
    object-fit: contain;
    display: block;
  }

  .share-pdf {
    width: 100%;
    height: 600px;
    border: none;
    border-radius: 4px;
  }

  .share-file {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    flex-direction: column;
    color: #909399;
  }

  .single-image-container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 80vh;
  }

  .single-image-container .large-image {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
  }


  /* 用于处理DeepSeek特殊格式的样式 */
  .deepseek-format-container {
    margin: 20px 0;
    padding: 15px;
    background-color: #f9f9f9;
    border-radius: 5px;
    border: 1px solid #eaeaea;
  }

  /* DeepSeek响应内容的特定样式 */
  :deep(.deepseek-response) {
    max-width: 800px;
    margin: 0 auto;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    font-family: Arial, sans-serif;
  }

 :deep(.deepseek-response pre) {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  font-family: monospace;
  overflow-x: auto;
  display: block;
  margin: 10px 0;
}

:deep(.deepseek-response code) {
  background-color: #f5f5f5;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: monospace;
}

:deep(.deepseek-response table) {
  border-collapse: collapse;
  width: 100%;
  margin: 15px 0;
}

:deep(.deepseek-response th),
:deep(.deepseek-response td) {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

:deep(.deepseek-response th) {
  background-color: #f2f2f2;
  font-weight: bold;
}

:deep(.deepseek-response h1),
:deep(.deepseek-response h2),
:deep(.deepseek-response h3),
:deep(.deepseek-response h4),
:deep(.deepseek-response h5),
:deep(.deepseek-response h6) {
  margin-top: 20px;
  margin-bottom: 10px;
  font-weight: bold;
  color: #222;
}

:deep(.deepseek-response a) {
  color: #0066cc;
  text-decoration: none;
}

:deep(.deepseek-response blockquote) {
  border-left: 4px solid #ddd;
  padding-left: 15px;
  margin: 15px 0;
  color: #555;
}

:deep(.deepseek-response ul),
:deep(.deepseek-response ol) {
  padding-left: 20px;
  margin: 10px 0;
}





  /* 媒体选择区域样式 */
  .media-selection-section {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 8px;
    border: 1px solid #e9ecef;
  }

  .media-selection-section h3 {
    margin: 0 0 12px 0;
    font-size: 14px;
    font-weight: 600;
    color: #303133;
  }

  .media-radio-group {
    display: flex;
    gap: 8px;
  }

  .media-radio-group .el-radio-button__inner {
    padding: 8px 16px;
    font-size: 13px;
    border-radius: 4px;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .media-radio-group .el-radio-button__inner i {
    font-size: 14px;
  }

  .media-description {
    margin-top: 10px;
    padding: 8px 12px;
    background-color: #f0f9ff;
    border-radius: 4px;
    border-left: 3px solid #409eff;
  }

  .media-description small {
    color: #606266;
    font-size: 12px;
    line-height: 1.4;
  }

  .layout-prompt-section h3 {
    margin-bottom: 10px;
    font-size: 14px;
    font-weight: 600;
    color: #303133;
  }

  /* 统一AI选项配置样式 */
  .ai-options {
    margin-top: 12px;
    padding: 12px;
    background: #f8f9fa;
    border-radius: 8px;
    border: 1px solid #e9ecef;
  }

  .ai-options .option-item {
    margin-bottom: 10px;
  }

  .ai-options .option-item:last-child {
    margin-bottom: 0;
  }

  .select-option, .button-option {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
  }

  .option-label {
    font-weight: 600;
    color: #303133;
    font-size: 13px;
    min-width: 60px;
  }

  .disabled-hint {
    color: #909399;
    font-size: 11px;
    font-style: italic;
    margin-left: 8px;
  }

  .ai-options .el-select {
    min-width: 120px;
    flex: 1;
  }

  .select-option {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
  }

  .option-label {
    font-size: 12px;
    color: #606266;
    white-space: nowrap;
    min-width: fit-content;
  }

  /* AI卡片离线状态样式 - 简化版 */
  .ai-card-offline {
    background: #f0f0f0 !important;
    border: 2px solid #d0d0d0 !important;
    position: relative;
    cursor: not-allowed !important;
    opacity: 0.6;
    filter: grayscale(100%);
    pointer-events: none !important;
  }

  .ai-card-offline::after {
    content: '离线';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 48px;
    font-weight: bold;
    color: rgba(0, 0, 0, 0.15);
    pointer-events: none;
    z-index: 1;
  }

  .ai-card-offline:hover {
    box-shadow: none !important;
    transform: none !important;
  }

  /* 离线状态下的所有元素都已通过卡片级别的样式处理 */
  /* pointer-events: none 确保无法点击 */
  /* filter: grayscale(100%) 确保整体变灰 */
  /* opacity: 0.6 确保视觉上明显变淡 */

  /* 按钮选项组样式 */
  .button-options-group {
    margin-top: 8px;
  }

  .ai-capabilities {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    justify-content: flex-start;
  }

  /* 动态布局类 */
  .ai-capabilities.buttons-single .capability-button {
    flex: 1;
    min-width: 0;
  }

  .ai-capabilities.buttons-two .capability-button {
    flex: 0 0 calc(50% - 3px);
    min-width: 0;
  }

  .ai-capabilities.buttons-three .capability-button {
    flex: 0 0 calc(33.333% - 4px);
    min-width: 0;
  }

  .ai-capabilities.buttons-multiple .capability-button {
    flex: 0 0 calc(50% - 3px);
    min-width: 0;
  }

  .capability-button {
    margin: 0 !important;
    padding: 4px 8px !important;
    font-size: 12px !important;
    border-radius: 16px !important;
    height: 28px !important;
    line-height: 1.2 !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    transition: all 0.2s ease;
  }

  .capability-button:hover {
    transform: translateY(-1px);
  }

  .disabled-hint-group {
    margin-top: 6px;
    text-align: center;
  }

  /* 登录状态相关样式 */
  .ai-status {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  /* 未登录AI卡片样式 */
  .ai-card-not-logged {
    position: relative;
    background: #fef7e6 !important;
    border-color: #f5dab1 !important;
  }

  /* 整个卡片的未登录遮罩层 */
  .card-login-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(245, 218, 177, 0.85);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 20;
    border-radius: 12px;
    backdrop-filter: blur(2px);
  }

  .card-login-message {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    background: #f56c6c;
    color: white;
    padding: 12px 16px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
    transform: scale(1);
    transition: transform 0.2s ease;
    text-align: center;
  }

  .login-hint {
    font-size: 12px;
    font-weight: 400;
    opacity: 0.9;
    margin-top: 2px;
  }

  /* 离线状态遮罩层 */
  .card-offline-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(144, 147, 153, 0.85);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 25;
    border-radius: 12px;
    backdrop-filter: blur(2px);
  }

  .card-offline-message {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    background: #909399;
    color: white;
    padding: 12px 16px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(144, 147, 153, 0.4);
    transform: scale(1);
    transition: transform 0.2s ease;
    text-align: center;
  }

  .offline-hint {
    font-size: 12px;
    font-weight: 400;
    opacity: 0.9;
    margin-top: 2px;
  }

  .card-login-message:hover {
    transform: scale(1.05);
  }

  .card-login-message i {
    font-size: 16px;
  }

  /* 未登录状态下禁用所有交互 */
  .ai-card-not-logged .el-switch,
  .ai-card-not-logged .el-select,
  .ai-card-not-logged .el-button {
    pointer-events: none;
  }

  .ai-card-not-logged .ai-card-header,
  .ai-card-not-logged .ai-options {
    opacity: 0.6;
  }

</style>
