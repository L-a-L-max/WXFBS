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
          <i class="el-icon-plus"></i>
          创建新主题任务
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
          <i class="el-icon-loading"></i>
          <span>加载中...</span>
        </div>
        <!-- 历史记录列表 -->
        <div v-else-if="chatHistory.length > 0">
          <div v-for="(group, date) in groupedHistory" :key="date" class="history-group">
            <div class="history-date">{{ date }}</div>
            <div class="history-list">
              <div v-for="(item, index) in group" :key="index" class="history-item">
                <div class="history-parent" @click="loadHistoryItem(item)">
                  <div class="history-header">
                    <i :class="[
                      'el-icon-arrow-right',
                      { 'is-expanded': item.isExpanded },
                    ]" @click.stop="toggleHistoryExpansion(item)"></i>
                    <div class="history-prompt">{{ item.userPrompt }}</div>
                  </div>
                  <div class="history-time">
                    {{ formatHistoryTime(item.createTime) }}
                  </div>
                </div>
                <div v-if="
                  item.children && item.children.length > 0 && item.isExpanded
                " class="history-children">
                  <div v-for="(child, childIndex) in item.children" :key="childIndex" class="history-child-item"
                    @click="loadHistoryItem(child)">
                    <div class="history-prompt">{{ child.userPrompt }}</div>
                    <div class="history-time">
                      {{ formatHistoryTime(child.createTime) }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 空状态 -->
        <div v-else class="history-empty">
          <i class="el-icon-document"></i>
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
              <el-card v-for="(ai, index) in aiList" :key="index" class="ai-card" shadow="hover">
                <div class="ai-card-header">
                  <div class="ai-left">
                    <div class="ai-avatar">
                      <img :src="ai.avatar" alt="AI头像" />
                    </div>
                    <div class="ai-name">{{ ai.name }}</div>
                  </div>
                  <div class="ai-status">
                    <el-switch v-model="ai.enabled" active-color="#13ce66" inactive-color="#ff4949">
                    </el-switch>
                  </div>
                </div>
                <div class="ai-capabilities" v-if="ai.capabilities && ai.capabilities.length > 0">
                  <!-- 通义只支持单选-->
                  <div v-if="ai.name === '通义千问'" class="button-capability-group">
                    <el-button v-for="capability in ai.capabilities" :key="capability.value" size="mini"
                      :type="ai.selectedCapability === capability.value ? 'primary' : 'info'" :disabled="!ai.enabled"
                      :plain="ai.selectedCapability !== capability.value"
                      @click="selectSingleCapability(ai, capability.value)" class="capability-button">
                      {{ capability.label }}
                    </el-button>
                  </div>
                  <!-- 百度AI选择 -->
                  <div v-else-if="ai.name === '百度AI'" class="button-capability-group">
                    <el-button size="mini" :type="getCapabilityType(ai, 'deep_search')" :disabled="!ai.enabled"
                      :plain="getCapabilityPlain(ai, 'deep_search')" @click="toggleCapability(ai, 'deep_search')"
                      class="capability-button">
                      深度搜索
                    </el-button>
                    <!-- <el-select :disabled="!ai.enabled || ai.selectedCapabilities.includes('deep_search')"
                      v-model="ai.selectedModel" placeholder="请选择模型">
                      <el-option label="百度AI助手" value="">
                      </el-option>
                      <el-option label="DeepSeek-R1" value="dsr1">
                      </el-option>
                      <el-option label="DeepSeek-V3" value="dsv3">
                      </el-option>
                      <el-option label="文心 4.5 Turbo" value="wenxin">
                      </el-option>
                    </el-select>
                    联网搜索
                    <el-switch v-model="ai.isWeb" active-color="#13ce66" inactive-color="#ff4949"
                      :disabled="!ai.enabled || ai.selectedCapabilities.includes('deep_search')" class="web-switch">
                    </el-switch> -->
                    <el-dropdown size="mini" :disabled="!ai.enabled || ai.selectedCapabilities.includes('deep_search')"
                      :type="ai.isModel ? 'primary' : 'plain'" @click="ai.isModel = !ai.isModel" split-button
                      trigger="click" :hide-on-click="false"
                      @command="function (command) { command == ai.selectedModel ? ai.isModel = false : ((ai.selectedModel = command) & (ai.isModel = true)) }">
                      {{ ai.selectedModel == "dsr1" ? "DeepSeek-R1" : ai.selectedModel == "dsv3" ? "DeepSeek-V3"
                        : ai.selectedModel == "wenxin" ? "文心4.5Turbo" : "百度AI助手" }}
                      <template #dropdown>
                        <el-dropdown-menu>
                          <el-dropdown-item command="dsr1">DeepSeek-R1</el-dropdown-item>
                          <el-dropdown-item command="dsv3">DeepSeek-V3</el-dropdown-item>
                          <el-dropdown-item command="wenxin">文心 4.5 Turbo</el-dropdown-item>
                          <span style="font-size: 12px; text-align:center; margin: 0px 0px 0px 10px">联网搜索</span>
                          <el-switch size="mini" v-model="ai.isWeb" style="zoom: 0.8"></el-switch>
                        </el-dropdown-menu>
                      </template>
                    </el-dropdown>
                  </div>
                  <!-- 腾讯元宝 -->
                  <div v-else-if="ai.name === '腾讯元宝'" class="button-capability-group">
                    <!-- 模型选择 -->
                    <div class="model-selection">
                      <span class="selection-label">模型:</span>
                      <el-select v-model="ai.selectedModel" placeholder="选择模型" size="mini" :disabled="!ai.enabled">
                        <el-option v-for="model in ai.models" :key="model.value" :label="model.label"
                          :value="model.value">
                        </el-option>
                      </el-select>
                    </div>
                    <!-- 功能选择 -->
                    <el-button v-for="capability in ai.capabilities" :key="capability.value" size="mini"
                      :type="getCapabilityType(ai, capability.value)" :disabled="!ai.enabled"
                      :plain="getCapabilityPlain(ai, capability.value)" @click="toggleCapability(ai, capability.value)"
                      class="capability-button">
                      {{ capability.label }}
                    </el-button>
                  </div>
                  <!-- 知乎直答单选思考模式 -->
                  <div v-else-if="ai.name === '知乎直答'" class="button-capability-group">
                    <el-button v-for="capability in ai.capabilities" :key="capability.value" size="mini"
                      :type="ai.selectedCapability === capability.value ? 'primary' : 'info'" :disabled="!ai.enabled"
                      :plain="ai.selectedCapability !== capability.value"
                      @click="selectSingleCapability(ai, capability.value)" class="capability-button">
                      {{ capability.label }}
                    </el-button>
                  </div>
                  <!-- 其他AI -->
                  <div v-else class="button-capability-group">
                    <el-button v-for="capability in ai.capabilities" :key="capability.value" size="mini"
                      :type="getCapabilityType(ai, capability.value)" :disabled="!ai.enabled"
                      :plain="getCapabilityPlain(ai, capability.value)" @click="toggleCapability(ai, capability.value)"
                      class="capability-button">
                      {{ capability.label }}
                    </el-button>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-collapse-item>

        <!-- 提示词输入区 -->
        <el-collapse-item title="提示词输入" name="prompt-input">
          <div class="prompt-input-section">
            <el-input type="textarea" :rows="5" placeholder="请输入提示词。主题示例提示词示例：请详细搜集刘氏起源，撰写一篇3000字的文章，要数据详尽" v-model="promptInput" resize="none"
              class="prompt-input">
            </el-input>
           <div class="prompt-footer">
  <div class="word-count">字数统计: {{ promptInput.length }}</div>
  <div class="current-prompt">
    <span>当前提示词：{{ currentPrompt }}</span>
  </div>
  <div class="mode-switch">
    <el-radio-group v-model="promptMode" size="small">
      <el-radio-button label="idea">撰写思路模式</el-radio-button>
      <el-radio-button label="article">撰写文章模式</el-radio-button>
    </el-radio-group>
  </div>
  <div class="button-group">
    <el-button type="info" @click="showPromptDialog" class="prompt-button">
      常用提示词
    </el-button>
    <el-button type="primary" @click="sendPrompt" :disabled="!canSend" class="send-button">
      发送
    </el-button>
  </div>
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
  <div class="button-group">
 <el-button v-if="firstReviewableResult" type="warning" @click="showReviewDialog(firstReviewableResult)" size="small">
  <i class="el-icon-edit-outline"></i>
  <span>审核</span>
</el-button>

    <el-button type="primary" @click="showScoreDialog" size="small">
      智能评分
    </el-button>
    <el-button type="warning" @click="showVisibilityDialog" size="small">
      可见度评估
    </el-button>
  </div>
</div>

        <el-tabs v-model="activeResultTab" type="card">
          <el-tab-pane v-for="(result, index) in results" :key="index" :label="result.aiName" :name="'result-' + index">
            <div class="result-content">
              <div class="result-header" v-if="result.shareUrl">
                <div class="result-title">{{ result.aiName }}的执行结果</div>
                <div class="result-buttons">
                  <el-button size="mini" type="primary" @click="openShareUrl(result.shareUrl)"
                    class="share-link-btn">
                    <i class="el-icon-link"></i>
                    <span>查看原链接</span>
                  </el-button>
            
                  <el-button v-if="!result.aiName.includes('智能排版')" size="mini" type="success"
                    @click="handlePushToMedia(result)" class="push-media-btn"
                    :loading="pushingToMedia" :disabled="pushingToMedia">
                    <i class="el-icon-s-promotion" v-if="!pushingToMedia"></i>
                    <span>智能排版</span>
                  </el-button>
                  <el-button v-else size="mini" type="success"
                    @click="pushToMediaWithContent(result)" class="push-media-btn" :loading="pushingToMedia && false"
                    :disabled="pushingToMedia && false">
                    <i class="el-icon-s-promotion"></i>
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
                    <i class="el-icon-document"></i>
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
    <!-- 可见度评估弹窗 -->
<el-dialog title="可见度评估" v-model="visibilityDialogVisible" width="60%" :close-on-click-modal="false">
  <div class="visibility-dialog-content">
    <div class="keyword-input-section">
      <h3>输入评估关键词：</h3>
      <el-input
        type="textarea"
        :rows="3"
        v-model="visibilityKeyword"
        placeholder="请输入要评估的关键词"
        class="keyword-input"
      ></el-input>
    </div>
    <div class="prompt-section">
      <h3>评估提示词：</h3>
      <el-input
        type="textarea"
        :rows="8"
        v-model="visibilityPrompt"
        class="prompt-input"
      ></el-input>
    </div>
  </div>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="visibilityDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleVisibilityEvaluation" :disabled="!visibilityKeyword.trim()">
        开始评估
      </el-button>
    </span>
  </template>
</el-dialog>

<!-- 常用提示词弹窗 -->
<el-dialog title="常用提示词" v-model="promptDialogVisible" width="60%" :close-on-click-modal="false">
  <div class="prompt-dialog-content">
    <div class="prompt-dialog-header">
  <el-button type="primary" size="small" @click="handleAddPrompt">新增提示词</el-button>
</div>

    <!-- 模式切换 -->
    <div class="mode-switch">
      <el-radio-group v-model="promptMode" size="small">
        <el-radio-button label="idea">撰写思路模式</el-radio-button>
        <el-radio-button label="article">撰写文章模式</el-radio-button>
      </el-radio-group>
    </div>
    <el-table :data="promptList" style="width: 100%">
      <el-table-column prop="name" label="名称" width="180">
      </el-table-column>
      <el-table-column prop="prompt" label="提示词内容">
        <template #default="scope">
          <div class="prompt-content">{{ scope.row.prompt }}</div>
        </template>
      </el-table-column>
    <el-table-column label="操作" width="240" fixed="right">
  <template #default="scope">
    <el-button size="mini" type="text" @click="usePrompt(scope.row)">
      <i class="el-icon-check"></i> 使用
    </el-button>
    <el-button size="mini" type="text" @click="handleEditPrompt(scope.row)">
      <i class="el-icon-edit"></i> 修改
    </el-button>
    <el-button size="mini" type="text" @click="handleDeletePrompt(scope.row)">
      <i class="el-icon-delete"></i> 删除
    </el-button>
  </template>
</el-table-column>

    </el-table>
  </div>
</el-dialog>
<!-- 新增/修改提示词对话框 -->
<el-dialog :title="promptDialogTitle" v-model="promptFormDialogVisible" width="60%" append-to-body>
  <el-form ref="promptForm" :model="promptForm" :rules="promptRules" label-width="80px">
    <el-form-item label="提示词名称" prop="name">
      <el-input v-model="promptForm.name" placeholder="请输入提示词名称" />
    </el-form-item>
    <el-form-item label="提示词内容" prop="prompt">
      <el-input v-model="promptForm.prompt" type="textarea" :rows="4" placeholder="请输入提示词内容" />
    </el-form-item>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="promptFormDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitPromptForm">确 定</el-button>
    </span>
  </template>
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
    <el-checkbox 
      v-for="result in results.filter(r => 
        !r.aiName.includes('智能评分') && 
        !r.aiName.includes('智能排版') && 
        !r.aiName.includes('可见度评估')
      )" 
      :key="result.aiName" 
      :label="result.aiName" 
      class="result-checkbox">
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
              <i class="el-icon-chat-dot-square"></i>
              公众号
            </el-radio-button>
            <el-radio-button label="zhihu_layout" value="zhihu_layout">
              <i class="el-icon-chat-dot-square"></i>
              知乎
            </el-radio-button>

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
<!-- 审核弹窗 -->
<el-dialog title="内容审核" v-model="reviewDialogVisible" width="60%" :close-on-click-modal="false">
  <div class="review-dialog-content">
  <!-- AI选择 -->
<div class="ai-selector">
  <div class="ai-button-group">
    <el-button 
      v-for="result in filteredResults"
      :key="result.aiName"
      :type="selectedReviewAI === result.aiName ? 'primary' : 'default'"
      :class="{ 'is-active': selectedReviewAI === result.aiName }"
      @click="switchReviewAI(result.aiName)"
      class="ai-button"
    >
      {{ result.aiName }}
    </el-button>
  </div>
</div>



    <!-- 提示词选择按钮 -->
    <div class="prompt-button-section">
      <el-button type="info" @click="showReviewPromptDialog" class="prompt-button">
        常用提示词
      </el-button>
      <div class="current-prompt">
        <span>当前提示词：{{ currentPrompt }}</span>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-section">
      <p>{{ promptMode === 'idea' ? '撰稿思路：' : '文章内容：' }}</p>
      <el-input
        type="textarea"
        :rows="15"
        v-model="editableContent"
        class="review-content-input"
      >
      </el-input>
    </div>

    <div class="review-buttons">
      <el-button v-if="promptMode === 'idea'" type="primary" @click="handleStartWriting">
        开始撰稿
      </el-button>
      <el-button v-else type="primary" @click="handleSmartLayout">
        智能排版
      </el-button>
      <el-button type="warning" @click="handleReject">
        一键驳回
      </el-button>
    </div>
  </div>
</el-dialog>

<!-- 审核提示词弹窗 -->
<el-dialog title="常用提示词" v-model="reviewPromptDialogVisible" width="60%" :close-on-click-modal="false">
  <div class="prompt-dialog-content">
    <div class="prompt-dialog-header">
  <el-button type="primary" size="small" @click="handleAddPrompt">新增提示词</el-button>
</div>

    <!-- 模式切换 -->
    <div class="mode-switch">
      <el-radio-group v-model="promptMode" size="small">
        <el-radio-button label="idea">撰写思路模式</el-radio-button>
        <el-radio-button label="article">撰写文章模式</el-radio-button>
      </el-radio-group>
    </div>
    <el-table :data="reviewPromptList" style="width: 100%">
      <el-table-column prop="name" label="名称" width="180">
      </el-table-column>
      <el-table-column prop="prompt" label="提示词内容">
        <template #default="scope">
          <div class="prompt-content">{{ scope.row.prompt }}</div>
        </template>
      </el-table-column>
<el-table-column label="操作" width="240" fixed="right">
  <template #default="scope">
    <el-button size="mini" type="text" @click="usePrompt(scope.row)">
      <i class="el-icon-check"></i> 使用
    </el-button>
    <el-button size="mini" type="text" @click="handleEditPrompt(scope.row)">
      <i class="el-icon-edit"></i> 修改
    </el-button>
    <el-button size="mini" type="text" @click="handleDeletePrompt(scope.row)">
      <i class="el-icon-delete"></i> 删除
    </el-button>
  </template>
</el-table-column>


    </el-table>
  </div>
</el-dialog>










  </div>
</template>

<script>
  import { marked } from "marked";
import {
  message,
  saveUserChatData,
  getChatHistory,
  pushAutoOffice,
  getMediaCallWord,
  getAllScorePrompt,
  getScoreWord,
  getAllIdeaPrompt,
  getAllArtPrompt,
  saveIdeaPrompt,    // 添加这个导入
  updateIdeaPrompt,  // 添加这个导入
  deleteIdeaPrompt,  // 添加这个导入
  saveArtPrompt,     // 添加这个导入
  updateArtPrompt,   // 添加这个导入
  deleteArtPrompt    // 添加这个导入
} from "@/api/wechat/aigc";


  import { v4 as uuidv4 } from "uuid";
  import websocketClient from "@/utils/websocket";
  import store from "@/store";
  import TurndownService from "turndown";
  import { getCorpId, ensureLatestCorpId } from "@/utils/corpId";

  export default {
    name: "AIManagementPlatform",
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
          autoScoreAfterCompletion: true, // 自动评分开关
    scorePromptList: [],
    scoreAI: "DeepSeek",
    visibilityEvaluationPrompt: "你是关键词可见度分析专家，需基于目标关键词，从 “基础结果量（40 分，500 万 +≤10 分、300-500 万 10-25 分、100-300 万 26-35 分、100 万以下 36-40 分）”“标题匹配量（30 分，intitle 结果占比超 30%21-30 分、10%-30%11-20 分、10% 以下 1-10 分）”“内容质量（30 分，高权重平台占比超 40%21-30 分、20%-40%11-20 分、20% 以下 1-10 分）” 三维度综合评估（满分 100 分），先输出总分，再分维度说明评分依据，最后总结该关键词可见度强弱及核心影响因素。", // 可见度评估提示词
    selectedScorePrompt: "",
     isFromReview: false, // 标记是否来自审核
    scorePrompt: `请你深度阅读以下几篇内容，从多个维度对以下内容进行逐项打分，输出评分结果。`,
        },
        jsonRpcReqest: {
          jsonrpc: "2.0",
          id: uuidv4(),
          method: "",
          params: {},
        },
        aiList: [
          {
            name: "豆包",
            avatar: require("../../../assets/ai/豆包.png"),
            capabilities: [{ label: "深度思考", value: "deep_thinking" }],
            selectedCapabilities: ["deep_thinking"],
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false  // 添加单选标记
          },
          
          {
            name: '百度AI',
            avatar: require('../../../assets/logo/Baidu.png'),
            capabilities: [
              { label: '深度搜索', value: 'deep_search' },
            ],
            selectedCapabilities: ["deep_search"],
            selectedModel: 'dsr1',
            isModel: false,
            isWeb: false,
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true,
          },
          {
            name: '腾讯元宝',
            avatar: require('../../../assets/ai/yuanbao.png'),
            capabilities: [
              { label: '深度思考', value: 'deep_thinking' },
              { label: '联网搜索', value: 'web_search' }
            ],
            selectedCapabilities: ['deep_thinking', 'web_search'],
            selectedModel: 'hunyuan', // 默认选择混元
            models: [
              { label: '混元', value: 'hunyuan' },
              { label: 'DeepSeek', value: 'deepseek' }
            ],
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false
          },
          {
            name: "DeepSeek",
            avatar: require("../../../assets/logo/Deepseek.png"),
            capabilities: [
              { label: "深度思考", value: "deep_thinking" },
              { label: "联网搜索", value: "web_search" },
            ],
            selectedCapabilities: ["deep_thinking", "web_search"],
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false,  // 添加单选标记
          },
          {
            name: '通义千问',
            avatar: require('../../../assets/ai/qw.png'),
            capabilities: [
              { label: '深度思考', value: 'deep_thinking' },
            ],
            selectedCapability: '',
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true
          },
          {
            name: "秘塔",
            avatar: require("../../../assets/ai/Metaso.png"),
            capabilities: [
              { label: "极速", value: "fast" },
              { label: "极速思考", value: "fast_thinking" },
              { label: "长思考", value: "long_thinking" },
            ],
            selectedCapabilities: "fast",// 单选使用字符串
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: true,  // 添加单选标记,用于capabilities中状态只能多选一的时候改成true,然后把selectedCapabilities赋值为字符串，不要是数组
          },
          {
            name: "知乎直答",
            avatar: require("../../../assets/ai/ZHZD.png"),
            capabilities: [
              { label: "智能思考", value: "smart_thinking" },
              { label: "深度思考", value: "deep_thinking" },
              { label: "快速回答", value: "fast_answer" },
            ],
            selectedCapability: "smart_thinking", // 改为单选，默认智能思考
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: true, // 设为单选模式
          },

        ],
        
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
        scorePrompt: `请你深度阅读以下内容，从多个维度进行逐项打分，输出评分结果`,
        scoreAI: "DeepSeek", // 默认选择DeepSeek进行评分
        layoutDialogVisible: false,
        layoutPrompt: "",
        layoutAI: "DeepSeek", // 当前选择的排版AI
        currentLayoutResult: null, // 当前要排版的结果
        historyDrawerVisible: false,
        chatHistory: [],
        historyLoading: false, // 历史记录加载状态
        pushOfficeNum: 0, // 投递到公众号的递增编号
        pushingToWechat: false, // 投递到公众号的loading状态
        selectedMedia: "wechat_layout", // 默认选择公众号
        pushingToMedia: false, // 投递到媒体的loading状态
          // 在这里添加审核相关的数据属性
         originalPrompt: '', // 保存原始提示词
        reviewDialogVisible: false,
        currentReviewResult: null,
        reviewResult: '',
         autoScoreEnabled: false, // 自动评分开关状态
         draftPrompt: '', // 保存撰稿时的提示词
         // 修改为只在新建任务时初始化
         originalTaskPrompt: '', // 保存原始主题任务提示词
         isPublished: false, // 是否已发布
         promptMode: 'idea', // 模式：'idea'为撰写思路模式，'article'为撰写文章模式
        promptDialogVisible: false,
         promptList: [],
       defaultIdeaPrompt: "根据主题任务撰写思路。", // 末尾加逗号便于拼接
       defaultArticlePrompt: "根据以下撰稿思路完善一篇内容。", // 末尾加逗号便于拼接
       currentPrompt: '', // 初始化为空
    lastSelectedPrompts: {  // 添加用于保存每种模式下最后选择的提示词
      idea: '',
      article: ''
    },
    
       autoScoreTimer: null, // 自动评分计时器
       completedAICount: 0, // 已完成的AI数量
       autoScoreTriggered: false, // 是否已触发自动评分
       editableContent: '', // 可编辑的内容
       reviewPromptDialogVisible: false,
       reviewPromptList: [],
       promptSource: '',
       selectedReviewAI: null,
        visibilityDialogVisible: false,
    visibilityKeyword: '',
    visibilityPrompt: `你是关键词可见度分析专家，需基于目标关键词，从 "基础结果量（40 分，500 万 +≤10 分、300-500 万 10-25 分、100-300 万 26-35 分、100 万以下 36-40 分）""标题匹配量（30 分，intitle 结果占比超 30%21-30 分、10%-30%11-20 分、10% 以下 1-10 分）""内容质量（30 分，高权重平台占比超 40%21-30 分、20%-40%11-20 分、20% 以下 1-10 分）" 三维度综合评估（满分 100 分），先输出总分，再分维度说明评分依据，最后总结该关键词可见度强弱及核心影响因素。`,
      promptFormDialogVisible: false,
    promptDialogTitle: '',
    promptForm: {
      id: null,
      name: '',
      prompt: ''
    },
    promptRules: {
      name: [
        { required: true, message: '提示词名称不能为空', trigger: 'blur' }
      ],
      prompt: [
        { required: true, message: '提示词内容不能为空', trigger: 'blur' }
      ]
    }
  
  };
    },
    computed: {
        firstReviewableResult() {
    return this.results.find(result => 
      !result.aiName.includes('智能评分') && 
      !result.aiName.includes('智能排版') && 
      !result.aiName.includes('可见度评估')
    );
  },
        filteredResults() {
    return this.results.filter(result => 
      !result.aiName.includes('智能评分') && 
      !result.aiName.includes('智能排版') &&
      !result.aiName.includes('可见度评估')
    );
},
      canSend() {
        return (
          this.promptInput.trim().length > 0 &&
          this.aiList.some((ai) => ai.enabled)
        );
      },
      canScore() {
        return (
          this.selectedResults.length > 0 && this.scorePrompt.trim().length > 0
        );
      },
      canLayout() {
        return this.currentLayoutResult !== null;
      },
allTasksCompleted() {
  if (!this.taskStarted || this.enabledAIs.length === 0) {
    return false;
  }
  // 确保所有启用的AI都已完成
  const allCompleted = this.enabledAIs.every(ai => 
    ai.status === 'completed'
  );
  const hasCompleted = this.enabledAIs.some(ai => 
    ai.status === 'completed'
  );
  return allCompleted && hasCompleted;
},
      // 检查是否有任务正在运行
      hasRunningTasks() {
        return this.enabledAIs.some(ai => ai.status === 'running');
      },
      groupedHistory() {
        const groups = {};
        const chatGroups = {};

        // 首先按chatId分组
        this.chatHistory.forEach((item) => {
          if(!chatGroups[item.chatId]) {
            chatGroups[item.chatId] = [];
          }
          chatGroups[item.chatId].push(item);
        });

        // 然后按日期分组，并处理父子关系
        Object.values(chatGroups).forEach((chatGroup) => {
          // 按时间排序
          chatGroup.sort(
            (a, b) => new Date(a.createTime) - new Date(b.createTime)
          );

          // 获取最早的记录作为父级
          const parentItem = chatGroup[0];
          const date = this.getHistoryDate(parentItem.createTime);

          if(!groups[date]) {
            groups[date] = [];
          }

          // 添加父级记录
          groups[date].push({
            ...parentItem,
            isParent: true,
            isExpanded: this.expandedHistoryItems[parentItem.chatId] || false,
            children: chatGroup.slice(1).map((child) => ({
              ...child,
              isParent: false,
            })),
          });
        });

        return groups;
      },
      // 检查是否所有AI都已启用
      allAIsEnabled() {
        return this.aiList.every(ai => ai.enabled);
      },
    },
    async created() {
      console.log(this.userId);
this.currentPrompt = this.defaultIdeaPrompt; // 默认使用思路模式提示词
  this.lastSelectedPrompts.idea = this.defaultIdeaPrompt;
  this.lastSelectedPrompts.article = this.defaultArticlePrompt;
    // 从 localStorage 读取保存的提示词
  const savedPrompts = localStorage.getItem('lastSelectedPrompts');
  if (savedPrompts) {
    this.lastSelectedPrompts = JSON.parse(savedPrompts);
  }
  
  // 初始化提示词
  this.currentPrompt = this.lastSelectedPrompts.idea || this.defaultIdeaPrompt;
  if (!this.lastSelectedPrompts.idea) {
    this.lastSelectedPrompts.idea = this.defaultIdeaPrompt;
  }
  if (!this.lastSelectedPrompts.article) {
    this.lastSelectedPrompts.article = this.defaultArticlePrompt;
  }
      // 使用企业ID工具确保获取最新的企业ID
      try {
        this.corpId = await getCorpId();
        console.log('获取最新企业ID:', this.corpId);
      } catch(error) {
        console.warn('获取企业ID失败，使用store中的值:', error);
        console.log(this.corpId);
      }

      this.initWebSocket(this.userId);
      this.loadChatHistory(0); // 加载历史记录
      this.loadLastChat(); // 加载上次会话
       this.completedAICount = 0;
  this.autoScoreTriggered = false;
    },
    mounted() {
      // 监听企业ID更新事件
      window.addEventListener('corpIdUpdated', this.handleCorpIdUpdated);
    },
    beforeDestroy() {
      // 移除事件监听
      window.removeEventListener('corpIdUpdated', this.handleCorpIdUpdated);
       this.clearAutoScoreTimer();
    },
    watch: {
      // 监听媒体选择变化，自动加载对应的提示词
      selectedMedia: {
        handler(newMedia) {
          this.loadMediaPrompt(newMedia);
        },
        immediate: false
      },
  promptMode: {
    handler(newMode) {
      // 切换模式时使用该模式下最后选择的提示词
      this.currentPrompt = this.lastSelectedPrompts[newMode] || 
        (newMode === 'idea' ? this.defaultIdeaPrompt : this.defaultArticlePrompt);
      // 如果弹窗打开，重新加载提示词列表
      if (this.promptDialogVisible || this.reviewPromptDialogVisible) {
        this.loadPromptList();
      }
    },
    immediate: false
  },
   lastSelectedPrompts: {
    handler(newVal) {
      localStorage.setItem('lastSelectedPrompts', JSON.stringify(newVal));
    },
    deep: true
  },

      // 监听任务完成状态
    // 监听任务完成状态
allTasksCompleted: {
 handler(newValue) {
      if(newValue && this.taskStarted && !this.autoScoreTriggered) {
        this.$nextTick(() => {
          this.$message.success('所有AI任务已完成！');
          // 检查是否开启自动评分
          if(this.autoScoreEnabled) {
            setTimeout(() => {
              this.showScoreDialog();
            }, 1500);
          }
        });
      }
    },
    immediate: true
  }, // 监听已完成的AI数量
completedAICount: {
  handler(newCount) {
    console.log(`📊 [计数监听] AI完成数量变化: ${newCount}`);
    if (newCount >= 4 && !this.autoScoreTriggered && !this.autoScoreTimer) {
      console.log(`⏰ [计时器] 满足4个AI完成条件，启动计时器`);
      this.startAutoScoreTimer();
    }
  }
}
},
    
    methods: {
      // 全局AI控制方法
      toggleAllAIs() {
        const newState = !this.allAIsEnabled;
        this.aiList.forEach(ai => {
          ai.enabled = newState;
        });

        // 显示操作反馈
        if(newState) {
          this.$message.success('已启动全部AI智能体');
        } else {
          this.$message.success('已关闭全部AI智能体');
        }
      },
      // 显示可见度评估弹窗
showVisibilityDialog() {
  this.visibilityDialogVisible = true;
  // 使用原始主题任务作为默认关键词
  this.visibilityKeyword = this.originalTaskPrompt || this.promptInput;
},
  // 新增提示词
  handleAddPrompt() {
    this.promptForm = {
      id: null,
      name: '',
      prompt: ''
    }
    this.promptDialogTitle = '新增提示词'
    this.promptFormDialogVisible = true
  },

  // 修改提示词
  handleEditPrompt(row) {
    this.promptForm = {
      id: row.id,
      name: row.name,
      prompt: row.prompt
    }
    this.promptDialogTitle = '修改提示词'
    this.promptFormDialogVisible = true
    // 记录来源，用于提交后刷新对应的列表
    this.promptSource = this.promptDialogVisible ? 'main' : 'review'
  },

  // 删除提示词
  handleDeletePrompt(row) {
    this.$confirm('是否确认删除该提示词？', '提示', {
      type: 'warning'
    }).then(() => {
      const api = this.promptMode === 'idea' ? deleteIdeaPrompt : deleteArtPrompt
      api([row.id]).then(() => {
        this.$message.success('删除成功')
        // 根据来源刷新对应的列表
        if (this.promptDialogVisible) {
          this.loadPromptList()
        } else if (this.reviewPromptDialogVisible) {
          this.showReviewPromptDialog()
        }
      })
    })
  },

  // 提交提示词表单
  submitPromptForm() {
    this.$refs.promptForm.validate(valid => {
      if (valid) {
        const api = this.promptForm.id 
          ? (this.promptMode === 'idea' ? updateIdeaPrompt : updateArtPrompt)
          : (this.promptMode === 'idea' ? saveIdeaPrompt : saveArtPrompt)
          
        api(this.promptForm).then(() => {
          this.$message.success(this.promptForm.id ? '修改成功' : '新增成功')
          this.promptFormDialogVisible = false
          // 根据来源刷新对应的列表
          if (this.promptSource === 'main') {
            this.loadPromptList()
          } else if (this.promptSource === 'review') {
            this.showReviewPromptDialog()
          }
        })
      }
    })
  },
      // 加载提示词列表
// 修改 loadPromptList 方法
async loadPromptList() {
  try {
    let response;
    if (this.promptMode === 'idea') {
      response = await getAllIdeaPrompt();
      const list = [
        { name: '默认', prompt: this.defaultIdeaPrompt },
        ...(response.data || [])
      ];
      if (this.promptDialogVisible) {
        this.promptList = list;
      }
      if (this.reviewPromptDialogVisible) {
        this.reviewPromptList = list;
      }
      // 如果当前没有选中的提示词，使用默认值
      if (!this.currentPrompt) {
        this.currentPrompt = this.defaultIdeaPrompt;
        this.$set(this.lastSelectedPrompts, 'idea', this.defaultIdeaPrompt);
      }
    } else {
      response = await getAllArtPrompt();
      const list = [
        { name: '默认', prompt: this.defaultArticlePrompt },
        ...(response.data || [])
      ];
      if (this.promptDialogVisible) {
        this.promptList = list;
      }
      if (this.reviewPromptDialogVisible) {
        this.reviewPromptList = list;
      }
      // 如果当前没有选中的提示词，使用默认值
      if (!this.currentPrompt) {
        this.currentPrompt = this.defaultArticlePrompt;
        this.$set(this.lastSelectedPrompts, 'article', this.defaultArticlePrompt);
      }
    }
  } catch (error) {
    console.error('获取提示词列表失败:', error);
    this.$message.error('获取提示词列表失败');
  }
}
,

      // 显示审核提示词弹窗
async showReviewPromptDialog() {
  this.reviewPromptDialogVisible = true;
  try {
    let response;
    if (this.promptMode === 'idea') {
      response = await getAllIdeaPrompt();
      this.reviewPromptList = [
        { name: '默认', prompt: this.defaultIdeaPrompt },
        ...(response.data || [])
      ];
    } else {
      response = await getAllArtPrompt();
      this.reviewPromptList = [
        { name: '默认', prompt: this.defaultArticlePrompt },
        ...(response.data || [])
      ];
    }
  } catch (error) {
    console.error('获取提示词列表失败:', error);
    this.$message.error('获取提示词列表失败');
  }
},

// 使用审核提示词
useReviewPrompt(prompt) {
  this.currentPrompt = prompt.prompt;
  // 保存当前模式下的提示词选择
  this.$set(this.lastSelectedPrompts, this.promptMode, prompt.prompt);
  this.reviewPromptDialogVisible = false;
  this.$message.success('已选择提示词：' + prompt.name);
},

// 启动自动评分计时器
startAutoScoreTimer() {
  console.log(`⏰ [计时器] 启动3分钟自动评分计时器，当前已完成AI数量: ${this.completedAICount}`);
  
  // 清除可能存在的旧计时器
  if (this.autoScoreTimer) {
    clearTimeout(this.autoScoreTimer);
  }

  // 设置
  // 3分钟计时器
  this.autoScoreTimer = setTimeout(() => {
    console.log(`⏰ [计时器] 3分钟计时器触发，当前状态：`);
    console.log(`- 已完成AI数量: ${this.completedAICount}`);
    console.log(`- 自动评分开关: ${this.autoScoreEnabled}`);
    console.log(`- 是否已触发: ${this.autoScoreTriggered}`);
    
    if (this.completedAICount >= 4 && !this.autoScoreTriggered && this.autoScoreEnabled) {
      console.log(`✅ [自动评分] 满足触发条件，开始自动评分`);
      this.$message.success('已触发自动评分');
      this.showScoreDialog();
      this.autoScoreTriggered = true;
    } else {
      console.log(`❌ [自动评分] 未满足触发条件`);
    }
  }, 3 * 60 * 1000); // 3分钟
},


// 清除自动评分计时器
clearAutoScoreTimer() {
  if (this.autoScoreTimer) {
    clearTimeout(this.autoScoreTimer);
    this.autoScoreTimer = null;
  }
},

    // 处理可见度评估

handleVisibilityEvaluation() {
  if(!this.visibilityKeyword.trim()) {
    this.$message.warning('请输入评估关键词');
    return;
  }

  // 确保提示词是字符串类型
  let prompt = this.visibilityPrompt;
  if (typeof prompt !== 'string') {
    prompt = '你是关键词可见度分析专家，需基于目标关键词，从多个维度进行综合评估。';
  }
  
  // 构建请求参数
  const params = {
    taskId: uuidv4(),
    userId: this.userId,
    corpId: this.corpId,
    userPrompt: `关键词：${this.visibilityKeyword}\n${prompt}`,
    roles: "zj-db",
  };

  const visibilityRequest = {
    jsonrpc: "2.0",
    id: uuidv4(),
    method: "使用F8S",
    params: params,
  };

  // 发送评估请求
  this.message(visibilityRequest);

  // 创建可见度评估任务节点
  const visibilityAI = {
    name: "可见度评估",
    avatar: require("../../../assets/ai/豆包.png"),
    capabilities: [],
    selectedCapabilities: [],
    enabled: true,
    status: "running",
    progressLogs: [
      {
        content: "可见度评估任务已提交，正在评估...",
        timestamp: new Date(),
        isCompleted: false,
      },
    ],
    isExpanded: true,
  };

  this.enabledAIs.unshift(visibilityAI);
  this.visibilityDialogVisible = false;
  this.$message.success("可见度评估请求已发送，请等待结果");
}
,

async showPromptDialog() {
  this.promptDialogVisible = true;
  try {
    let response;
    if (this.promptMode === 'idea') {
      response = await getAllIdeaPrompt();
      // 添加默认选项
      this.promptList = [
        { name: '默认', prompt: this.defaultIdeaPrompt },
        ...(response.data || [])
      ];
    } else {
      response = await getAllArtPrompt();
      // 添加默认选项
      this.promptList = [
        { name: '默认', prompt: this.defaultArticlePrompt },
        ...(response.data || [])
      ];
    }
  } catch (error) {
    console.error('获取提示词列表失败:', error);
    this.$message.error('获取提示词列表失败');
  }
},


// 修改 usePrompt 方法
usePrompt(prompt) {
  this.currentPrompt = prompt.prompt;
  // 保存当前模式下的提示词选择
  this.$set(this.lastSelectedPrompts, this.promptMode, prompt.prompt);
  this.promptDialogVisible = false;
  this.$message.success('已选择提示词：' + prompt.name);
}
,



      // 处理企业ID更新事件
      handleCorpIdUpdated(event) {
        const newCorpId = event.detail.corpId;
        if(newCorpId && newCorpId !== this.corpId) {
          console.log('Chrome页面接收到企业ID更新事件，更新本地corpId:', newCorpId);
          this.corpId = newCorpId;
          this.$message.success(`主机ID已自动更新: ${newCorpId}`);
        }
      },
      // 切换审核AI
switchReviewAI(aiName) {
  this.selectedReviewAI = aiName; // 更新选中的AI
  const result = this.results.find(r => r.aiName === aiName);
  if (result) {
    this.currentReviewResult = result;
    // 直接使用markdown渲染后的纯文本内容
    this.editableContent = this.htmlToText(result.content);
  }
},

// 显示审核弹窗
showReviewDialog(result) {
  this.currentReviewResult = result;
   this.selectedReviewAI = result.aiName; // 设置初始选中的AI
  this.selectedReviewAI = result.aiName;
  // 直接使用markdown渲染后的纯文本内容
  this.editableContent = this.htmlToText(result.content);
  this.reviewDialogVisible = true;
},

// 处理开始撰稿（撰写思路模式）
handleStartWriting() {
  // 切换到撰写文章模式
  this.promptMode = 'article';
  // 更新当前提示词为该模式下最后选择的提示词
  this.currentPrompt = this.lastSelectedPrompts.article || this.defaultArticlePrompt;
  // 直接使用编辑后的内容作为撰稿思路
  this.promptInput = this.editableContent;
  // 保存当前思路作为历史记录
  this.draftPrompt = this.editableContent;
  // 保存原始主题任务
  this.originalTaskPrompt = this.originalPrompt;
  // 标记为来自审核
  this.isFromReview = true;
  // 关闭审核弹窗
  this.reviewDialogVisible = false;
  // 自动发送
  this.sendPrompt();
},


// 处理智能排版（撰写文章模式）
handleSmartLayout() {
  // 创建一个新的结果对象，使用编辑后的内容
  const editedResult = {
    ...this.currentReviewResult,
    content: this.editableContent
  };
  // 直接调用智能排版
  this.handlePushToMedia(editedResult);
  this.reviewDialogVisible = false;
},

// 处理驳回修改
handleReject() {
  this.reviewDialogVisible = false;
  
  if (this.promptMode === 'idea') {
    // 撰写思路模式：重新发送主题任务
    this.promptInput = this.originalPrompt;
    this.sendPrompt();
  } else {
    // 撰写文章模式：重新发送撰稿思路
    this.promptInput = this.draftPrompt;
    this.sendPrompt();
  }
},
    handleReview() {
    if(!this.reviewResult) {
      this.$message.warning('请选择审核结果');
      return;
    }
    
    if(this.reviewResult === 'approve') {
      // 确定后，将当前内容作为撰稿思路发送给AI
      this.sendAsDraft();
    } else {
      // 驳回修改
      const isEditingDraft = this.isFromReview; // 保存当前是否是撰稿状态
      
      // 清空当前审核结果
      this.currentReviewResult = null;
      this.reviewResult = '';
      
      if(isEditingDraft) {
        // 如果是修改稿件，保持撰稿状态，只修改内容
        this.promptInput = this.draftPrompt;
        this.sendPrompt();
      } else {
        // 如果是修改思路，重置所有状态
        this.createNewChat();
        this.promptInput = this.originalPrompt;
        this.sendPrompt();
      }
    }
    
    this.reviewDialogVisible = false;
  },


  sendAsDraft() {
    // 获取当前结果的内容
    const content = this.currentReviewResult.content;
    this.isFromReview = true;
    // 设置为提示词，添加指定的前缀
    this.promptInput = `根据以下思路完善一篇内容。思路:${content}`;
    // 保存撰稿提示词
    this.draftPrompt = this.promptInput;
    // 保存原始主题任务
    this.originalTaskPrompt = this.originalPrompt;
    // 标记为来自审核
    this.isFromReview = true;
    this.autoScoreEnabled = true; // 启用自动评分功能
    // 发送给所有AI
    this.sendPrompt();
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
  
  // 如果是撰写思路模式，保存原始提示词
  if (this.promptMode === 'idea') {
    this.originalPrompt = this.promptInput;
  }
  
  // 构建完整的提示词
  let fullPrompt;
  // 确保使用当前选择的提示词
  const activePrompt = this.currentPrompt || (this.promptMode === 'idea' ? this.defaultIdeaPrompt : this.defaultArticlePrompt);
  
  if (this.promptMode === 'idea') {
    // 撰写思路模式：提示词 + "主题任务：" + 用户输入
    fullPrompt = activePrompt + "主题任务：" + this.promptInput;
  } else {
    // 撰写文章模式：提示词 + "撰稿思路：" + 用户输入
    fullPrompt = activePrompt + "撰稿思路：" + this.promptInput;
  }

  // 重置计数和状态
  this.completedAICount = 0;
  this.autoScoreTriggered = false;
  this.clearAutoScoreTimer();

  
  // 确保使用最新的企业ID
  await this.ensureLatestCorpId();
  
  this.screenshots = [];
  this.activeCollapses = ["ai-selection"];
  this.taskStarted = true;
  this.results = [];

  this.userInfoReq.roles = "";
  this.userInfoReq.taskId = uuidv4();
  this.userInfoReq.userId = this.userId;
  this.userInfoReq.corpId = this.corpId;
  this.userInfoReq.userPrompt = fullPrompt;
       

        // 获取启用的AI列表及其状态，并重置状态
        this.enabledAIs = this.aiList.filter((ai) => ai.enabled).map(ai => ({
          ...ai,
          status: "running",
          progressLogs: [], // 清空之前的进度日志
          isExpanded: true  // 确保展开状态一致
        }));
         // 开启自动评分功能
        this.autoScoreEnabled = true;
        // 将所有启用的AI状态设置为运行中（使用Vue的响应式更新）
        this.enabledAIs.forEach((ai) => {
            ai.status = "running";
                   ai.progressLogs = [];
                   ai.isExpanded = true;
        });

        this.enabledAIs.forEach((ai) => {
          if(ai.name === "豆包") {
            this.userInfoReq.roles = this.userInfoReq.roles + "zj-db,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              this.userInfoReq.roles = this.userInfoReq.roles + "zj-db-sdsk,";
            }
          }


          if(ai.name === '通义千问' && ai.enabled) {
            this.userInfoReq.roles = this.userInfoReq.roles + 'ty-qw,';
            if(ai.selectedCapability.includes("deep_thinking")) {
              this.userInfoReq.roles = this.userInfoReq.roles + 'ty-qw-sdsk,'
            }
          }

          if(ai.name === '腾讯元宝') {
            // 根据选择的模型设置角色
            if(ai.selectedModel === 'hunyuan') {
              this.userInfoReq.roles = this.userInfoReq.roles + 'yb-hunyuan-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'yb-hunyuan-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'yb-hunyuan-lwss,';
              }
            } else if(ai.selectedModel === 'deepseek') {
              this.userInfoReq.roles = this.userInfoReq.roles + 'yb-deepseek-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'yb-deepseek-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'yb-deepseek-lwss,';
              }
            }
          }
          if(ai.name === '百度AI') {
            this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-agent,';
            if(ai.selectedCapabilities.includes("deep_search")) {
              this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-sdss,';
            } else if(ai.isModel) {
              if(ai.isWeb) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-web,';
              }

              if(ai.selectedModel.includes("dsr1")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-dsr1,';
              } else if(ai.selectedModel.includes("dsv3")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-dsv3,';
              } else if(ai.selectedModel.includes("wenxin")) {
                this.userInfoReq.roles = this.userInfoReq.roles + 'baidu-wenxin,';
              }
            }

          }

          if(ai.name === "DeepSeek" && ai.enabled) {
            this.userInfoReq.roles = this.userInfoReq.roles + "deepseek,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              this.userInfoReq.roles = this.userInfoReq.roles + "ds-sdsk,";
            }
            if(ai.selectedCapabilities.includes("web_search")) {
              this.userInfoReq.roles = this.userInfoReq.roles + "ds-lwss,";
            }
          }

          if(ai.name === "秘塔") {
            this.userInfoReq.roles = this.userInfoReq.roles + "mita,";
            if(ai.selectedCapabilities === "fast") {
              this.userInfoReq.roles = this.userInfoReq.roles + "metaso-jisu,";
            }
            if(ai.selectedCapabilities === "fast_thinking") {
              this.userInfoReq.roles = this.userInfoReq.roles + "metaso-jssk,";
            }
            if(ai.selectedCapabilities === "long_thinking") {
              this.userInfoReq.roles = this.userInfoReq.roles + "metaso-csk,";
            }
          }

          if(ai.name === "知乎直答") {
           
            this.userInfoReq.roles = this.userInfoReq.roles + "zhzd-chat,";
            // 使用单选思考模式
            if(ai.selectedCapability === "deep_thinking") {
              this.userInfoReq.roles = this.userInfoReq.roles + "zhzd-sdsk,";
            } else if(ai.selectedCapability === "fast_answer") {
              this.userInfoReq.roles = this.userInfoReq.roles + "zhzd-ks,";
            } else if(ai.selectedCapability === "smart_thinking") {
              this.userInfoReq.roles = this.userInfoReq.roles + "zhzd-zn,";
            } else {
              // 默认智能思考
              this.userInfoReq.roles = this.userInfoReq.roles + "zhzd-zn,";
              
            }
          
          }

        });

        console.log("参数：", this.userInfoReq);
        const dbAI = this.aiList.find(ai => ai.name === "豆包");
        if (dbAI && dbAI.enabled) {
          this.userInfoReq.userPrompt = "不要进入其他模式，直接回答结果即可。" + this.userInfoReq.userPrompt;
        }
        //调用后端接口
        this.jsonRpcReqest.method = "使用F8S";
        this.jsonRpcReqest.params = this.userInfoReq;
        this.message(this.jsonRpcReqest);
        this.userInfoReq.isNewChat = false;
      },

      message(data) {
        message(data).then((res) => {
          if(res.code == 201) {
            this.$message.error(res.messages || '操作失败');
          }
        });
      },
      // 辅助方法：判断按钮类型
      getCapabilityType(ai, value) {
        // 确保单选时使用字符串比较，多选时使用数组包含
        if(ai.isSingleSelect) {
          // 知乎直答使用selectedCapability，通义使用selectedCapability
          if(ai.name === '知乎直答') {
            return ai.selectedCapability === value ? 'primary' : 'info';
          } else {
            return ai.selectedCapabilities === value ? 'primary' : 'info';
          }
        } else {
          return ai.selectedCapabilities && ai.selectedCapabilities.includes(value) ? 'primary' : 'info';
        }
      },

      // 辅助方法：判断按钮是否为朴素样式
      getCapabilityPlain(ai, value) {
        if(ai.isSingleSelect) {
          // 知乎直答使用selectedCapability，通义使用selectedCapability
          if(ai.name === '知乎直答') {
            return ai.selectedCapability !== value;
          } else {
            return ai.selectedCapabilities !== value;
          }
        } else {
          return !(ai.selectedCapabilities && ai.selectedCapabilities.includes(value));
        }
      },
      // 处理单选逻辑（通义千问、知乎直答）
      selectSingleCapability(ai, capabilityValue) {
        if(!ai.enabled) return;

        // 知乎直答不允许取消选择，至少保持一个选项
        if(ai.name === '知乎直答') {
          ai.selectedCapability = capabilityValue;
        } else {
          // 通义千问允许取消选择
          if(ai.selectedCapability === capabilityValue) {
           ai.selectedCapability = '';
          } else {
           ai.selectedCapability = capabilityValue;
          }
        }
        this.$forceUpdate();
      },
      toggleCapability(ai, capabilityValue) {
        console.log(this.aiList)
        if(!ai.enabled) return;

        console.log("切换前:", ai.selectedCapabilities, "类型:", typeof ai.selectedCapabilities);

        // 单选逻辑
        if(ai.isSingleSelect) {
          // 强制使用字符串类型赋值
        ai.selectedCapabilities = String(capabilityValue);
        }
        // 多选逻辑
        else {
          // 确保selectedCapabilities是数组
          if(!Array.isArray(ai.selectedCapabilities)) {
           ai.selectedCapabilities = [];
          }

          const index = ai.selectedCapabilities.indexOf(capabilityValue);
               if(index === -1) {
                    // 添加选中项
                    ai.selectedCapabilities.push(capabilityValue);
                  } else {
                    // 移除选中项
                    const newCapabilities = [...ai.selectedCapabilities];
                    newCapabilities.splice(index, 1);
                    ai.selectedCapabilities = newCapabilities;
                  }
                  if(ai.name === "百度AI") {
                    // 如果选择了deep-search，则取消其他，反之亦然
                    if(capabilityValue === "deep_search" && ai.selectedCapabilities.includes("deep_search")) {
                      ai.selectedCapabilities = ["deep_search"];
                      ai.isModel = false;
                      ai.isWeb = false;
                    } else if(capabilityValue !== "deep_search" && ai.selectedCapabilities.includes("deep_search")) {
                      ai.selectedCapabilities = [];
                      ai.selectedCapabilities = filtered;
                    }
                  }
                }



        console.log("切换后:", ai.selectedCapabilities, "类型:", typeof ai.selectedCapabilities);
        this.$forceUpdate(); // 强制更新视图
      },
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
  if (!html) return '';
  
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = html;
  
  // 处理通义千问的特殊样式
  const tongyiElements = tempDiv.querySelectorAll('[class*="tongyi-response"]');
  tongyiElements.forEach(el => {
    // 移除class属性
    el.removeAttribute('class');
    // 移除style属性
    el.removeAttribute('style');
  });
  
  // 移除所有style标签和内联样式
  const styleTags = tempDiv.querySelectorAll('style');
  styleTags.forEach(tag => tag.remove());
  
  const allElements = tempDiv.querySelectorAll('*');
  allElements.forEach(el => {
    el.removeAttribute('style');
  });
  
  // 获取处理后的内容
  let text = tempDiv.textContent || tempDiv.innerText || '';
  
  // 处理换行和段落
  text = text.replace(/\n\s*\n/g, '\n\n'); // 合并多个空行
  text = text.trim();
  
  return text;
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
        this.$message.success("已复制纯文本到剪贴板");
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
        this.$message.success("已导出Markdown文件");
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
          // 只处理当前任务的日志消息
          if(dataObj.taskId && dataObj.taskId !== this.userInfoReq.taskId) {
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
          // 只处理当前任务的截图
          if(dataObj.taskId && dataObj.taskId !== this.userInfoReq.taskId) {
            return; // 忽略其他任务的截图
          }

          // 将新的截图添加到数组开头
          this.screenshots.unshift(dataObj.url);
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
           console.log('=== 排版返回原始数据 ===', dataObj); // 打印完整的返回数据
          const znpbAI = this.enabledAIs.find((ai) => ai.name === "智能排版");
          if(znpbAI) {
            znpbAI.status = "completed";
            if(znpbAI.progressLogs.length > 0) {
              znpbAI.progressLogs[0].isCompleted = true;
            }
 // 检查返回的数据结构
    console.log('=== draftContent字段 ===', dataObj.draftContent);
    console.log('=== draftContent类型 ===', typeof dataObj.draftContent);
    console.log('=== draftContent长度 ===', dataObj.draftContent?.length);
  // 打印返回内容的长度
    console.log('=== 排版返回内容长度 ===', dataObj.draftContent.length);
    console.log('=== 排版返回内容预览 ===', dataObj.draftContent.substring(0, 200) + '...');
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
  // 首先查找是否存在对应taskId的可见度评估任务
  const visibilityAI = this.enabledAIs.find(ai => 
    ai.name === "可见度评估" && 
    ai.status === "running" && 
    ai.taskId === dataObj.taskId
  );
  
  if(visibilityAI) {
    // 只处理匹配的可见度评估任务
    visibilityAI.status = "completed";
    if(visibilityAI.progressLogs.length > 0) {
      visibilityAI.progressLogs[0].isCompleted = true;
    }
    this.results.unshift({
      aiName: "可见度评估",
      content: dataObj.draftContent,
      shareUrl: dataObj.shareUrl || "",
      shareImgUrl: dataObj.shareImgUrl || "",
      timestamp: new Date(),
    });
    this.activeResultTab = "result-0";
    return; // 处理完可见度评估直接返回
  }

  // 处理普通豆包任务
  const normalAI = this.enabledAIs.find(ai => 
    ai.name === "豆包" && 
    ai.status === "running" && 
    ai.taskId === dataObj.taskId
  );
  
  if(normalAI) {
    normalAI.status = "completed";
    if(normalAI.progressLogs.length > 0) {
      normalAI.progressLogs[0].isCompleted = true;
    }
        // 添加计数逻辑
    if(!normalAI.hasCounted) {
      this.completedAICount++;
      normalAI.hasCounted = true;
      console.log(`📊 [计数更新] ${normalAI.name}完成，当前已完成AI数量: ${this.completedAICount}`);
    }
    this.results.unshift({
      aiName: "豆包",
      content: dataObj.draftContent,
      shareUrl: dataObj.shareUrl || "",
      shareImgUrl: dataObj.shareImgUrl || "",
      timestamp: new Date(),
    });
    this.activeResultTab = "result-0";
  }
  break;


          case "RETURN_BAIDU_RES":
            console.log("收到百度AI消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "百度AI");
            break;
          case "RETURN_DEEPSEEK_RES":
            console.log("收到DeepSeek消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "DeepSeek");
            break;
          case 'RETURN_TY_RES':
            console.log('收到通义千问消息:', data);
            targetAI = this.enabledAIs.find(ai => ai.name === '通义千问');
            break;
          case "RETURN_METASO_RES":
            console.log("收到秘塔消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "秘塔");
            break;
          case "RETURN_ZHZD_RES":
            console.log("收到知乎直答消息:", dataObj);
            targetAI = this.enabledAIs.find((ai) => ai.name === "知乎直答");
            break;

        }

        if(targetAI) {
          console.log(`🎯 [结果处理] 找到目标AI: ${targetAI.name}`);
          console.log(`📋 [结果处理] 当前taskId: ${this.userInfoReq.taskId}, 消息taskId: ${dataObj.taskId}`);
          console.log(`📊 [结果处理] AI当前状态: ${targetAI.status}`);
          
          // 只处理当前任务的结果
          if(dataObj.taskId && dataObj.taskId !== this.userInfoReq.taskId) {
            console.warn(`⚠️ [结果处理] 忽略其他任务的消息`);
            return; // 忽略其他任务的消息
          }

          // 检查AI是否还在运行状态，避免重复处理
          if(targetAI.status !== "running") {
            console.warn(`⚠️ [结果处理] AI状态不是running，跳过处理: ${targetAI.status}`);
            // 如果状态已经是completed，但收到新结果，说明是重复消息或延迟消息
            // 不返回，继续处理，确保结果能被保存
          }
// 更新AI状态为已完成
if(targetAI.status === "running") {
  targetAI.status = "completed";
  // 只有第一次完成时才计数
  if(!targetAI.hasCounted) {
    this.completedAICount++;
    targetAI.hasCounted = true; // 标记已计数
    console.log(`📊 [计数更新] ${targetAI.name}完成，当前已完成AI数量: ${this.completedAICount}`);
  }
  console.log(`✅ [结果处理] 更新${targetAI.name}状态为completed`);
}

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
  // 自动选择所有已完成的结果
  this.selectedResults = this.results
    .filter(result => {
      // 查找对应的AI是否已完成
      const ai = this.enabledAIs.find(ai => ai.name === result.aiName);
      return ai && ai.status === 'completed';
    })
    .map(result => result.aiName);
  
  // 加载评分提示词列表
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
  // 根据是否来自审核选择不同的提示词
    const basePrompt = this.isFromReview 
      ? `请你深度阅读以下内容，从多个维度对以下内容进行逐项打分，输出评分结果`
      : this.scorePrompt;

     const fullPrompt = `请你深度阅读以下内容，从多个维度对以下内容进行逐项打分，输出评分结果}\n\n${selectedContents}`;

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

        {
          if(ai.name === "豆包") {
            scoreRequest.params.roles = scoreRequest.params.roles + "zj-db,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              scoreRequest.params.roles = scoreRequest.params.roles + "zj-db-sdsk,";
            }
          }


          if(ai.name === '通义千问') {
            scoreRequest.params.roles = scoreRequest.params.roles + 'ty-qw,';
            if(ai.selectedCapability.includes("deep_thinking")) {
              scoreRequest.params.roles = scoreRequest.params.roles + 'ty-qw-sdsk,'
            }
          }

          if(ai.name === '腾讯元宝') {
            // 根据选择的模型设置角色
            if(ai.selectedModel === 'hunyuan') {
              scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-hunyuan-lwss,';
              }
            } else if(ai.selectedModel === 'deepseek') {
              scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'yb-deepseek-lwss,';
              }
            }
          }
          if(ai.name === '百度AI') {
            scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-agent,';
            if(ai.selectedCapabilities.includes("deep_search")) {
              scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-sdss,';
            } else if(ai.isModel) {
              if(ai.isWeb) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-web,';
              }

              if(ai.selectedModel.includes("dsr1")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-dsr1,';
              } else if(ai.selectedModel.includes("dsv3")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-dsv3,';
              } else if(ai.selectedModel.includes("wenxin")) {
                scoreRequest.params.roles = scoreRequest.params.roles + 'baidu-wenxin,';
              }
            }

          }

          if(ai.name === "DeepSeek") {
            scoreRequest.params.roles = scoreRequest.params.roles + "deepseek,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              scoreRequest.params.roles = scoreRequest.params.roles + "ds-sdsk,";
            }
            if(ai.selectedCapabilities.includes("web_search")) {
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
       this.autoScoreEnabled = false; // 关闭自动评分
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
        this.$message.success("评分请求已发送，请等待结果");
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
          const res = await getChatHistory(this.userId, isAll);
          if(res.code === 200) {
            this.chatHistory = res.data || [];
          }
        } catch(error) {
          console.error("加载历史记录失败:", error);
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
          // 恢复AI选择配置 - 确保包含新添加的AI
          if(historyData.aiList) {
            // 合并历史记录中的aiList和当前默认的aiList
            const historicalAiList = historyData.aiList;
            const currentAiList = this.aiList;

            // 创建合并后的aiList，保留历史记录中的状态，同时包含当前默认的AI
            // this.aiList = [...historicalAiList,...currentAiList];

            // 添加当前默认的但不在历史记录中的AI
            currentAiList.forEach(currentAI => {
              const exists = this.aiList.find(historicalAI => historicalAI.name === currentAI.name);
              if(!exists) {
                this.aiList.push(currentAI);
              }
            });
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
  status: "running",
  taskId: this.userInfoReq.taskId, // 使用主任务的taskId
  progressLogs: [],
  isExpanded: true,
  hasCounted: false  // 添加计数标记
}));
          }
          // 恢复主机可视化
          this.screenshots = historyData.screenshots || [];
          // 恢复执行结果
          this.results = historyData.results || [];
          // 恢复chatId
          this.chatId = item.chatId || this.chatId;
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

          // 展开相关区域
          this.activeCollapses = ["ai-selection", "prompt-input"];
          this.taskStarted = true;

          this.$message.success("历史记录加载成功");
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
              this.expandedHistoryItems[item.chatId] = !this.expandedHistoryItems[item.chatId];
            },

      // 创建新对话
      createNewChat() {
           this.completedAICount = 0;
  this.autoScoreTriggered = false;
  this.clearAutoScoreTimer();
  // 重置所有AI的计数标记
  this.aiList.forEach(ai => {
    ai.hasCounted = false;
  });
         const savedOriginalPrompt = this.originalPrompt;
        // 重置所有数据
        this.chatId = uuidv4();
        this.isNewChat = true;
        this.promptInput = "";
        this.taskStarted = false;
        this.screenshots = [];
        this.results = [];
        this.enabledAIs = [];
        this.isFromReview = false; // 重置是否来自审核标志
        this.originalTaskPrompt = '';
        // 重置所有AI状态为初始状态
        this.aiList.forEach(ai => {
          ai.status = "idle";
                   ai.progressLogs = [];
                   ai.isExpanded = true;
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
          isNewChat: true,
        };
        // 重置AI列表为初始状态
        this.aiList = [
          {
            name: "豆包",
            avatar: require("../../../assets/ai/豆包.png"),
            capabilities: [{ label: "深度思考", value: "deep_thinking" }],
            selectedCapabilities: ["deep_thinking"],
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false,  // 添加单选标记
          },


          // 元宝AI配置
          {
            name: '腾讯元宝',
            avatar: require('../../../assets/ai/yuanbao.png'),
            capabilities: [
              { label: '深度思考', value: 'deep_thinking' },
              { label: '联网搜索', value: 'web_search' }
            ],
            selectedCapabilities: ['deep_thinking', 'web_search'],
            selectedModel: 'hunyuan', // 默认选择混元
            models: [
              { label: '混元', value: 'hunyuan' },
              { label: 'DeepSeek', value: 'deepseek' }
            ],
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false
          },
          {
            name: '百度AI',
            avatar: require('../../../assets/logo/Baidu.png'),
            capabilities: [
              { label: '深度搜索', value: 'deep_search' },
            ],
            selectedCapabilities: ["deep_search"],
            selectedModel: 'dsr1',
            isModel: false,
            isWeb: false,
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true,
          },
          {
            name: "DeepSeek",
            avatar: require("../../../assets/logo/Deepseek.png"),
            capabilities: [
              { label: "深度思考", value: "deep_thinking" },
              { label: "联网搜索", value: "web_search" },
            ],
            selectedCapabilities: ["deep_thinking", "web_search"],
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: false,  // 添加单选标记
          },
          {
            name: '通义千问',
            avatar: require('../../../assets/ai/qw.png'),
            capabilities: [
              { label: '深度思考', value: 'deep_thinking' },
            ],
            selectedCapability: '',
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true
          },
          {
            name: "秘塔",
            avatar: require("../../../assets/ai/Metaso.png"),
            capabilities: [
              { label: "极速", value: "fast" },
              { label: "极速思考", value: "fast_thinking" },
              { label: "长思考", value: "long_thinking" },
            ],
            selectedCapabilities: "fast",// 单选使用字符串
            enabled: true,
            status: "idle",
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: true,  // 添加单选标记,用于capabilities中状态只能多选一的时候改成true,然后把selectedCapabilities赋值为字符串，不要是数组
          },


          {
            name: "知乎直答",
            avatar: require("../../../assets/ai/ZHZD.png"),
            capabilities: [
              { label: "智能思考", value: "smart_thinking" },
              { label: "深度思考", value: "deep_thinking" },
              { label: "快速回答", value: "fast_answer" },
            ],
            selectedCapability: "smart_thinking", // 改为单选，默认智能思考
            enabled: true,
            status: 'idle',
            progressLogs: [],
            isExpanded: true,
            isSingleSelect: true, // 设为单选模式
          },

        ];
        this.originalPrompt = savedOriginalPrompt; // 恢复原始提示词
        this.isFromReview = false; // 重置审核状态
        this.autoScoreEnabled = false; // 重置自动评分状态
        this.currentReviewResult = null; // 清空当前审核结果
        this.reviewResult = ''; // 重置审核结果
        this.$message.success("已创建新对话");

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
          通义千问: "700px",
          秘塔: "700px",
        };

        const width = widthMap[aiName] || "560px"; // 默认宽度

        return {
          width: width,
          height: "auto",
        };
      },

 handlePushToMedia(result) {
  // 创建一个新的结果对象，如果是通义千问就先清理内容
  let processedResult = { ...result };
  
  // 不管是什么AI，都使用htmlToText方法清理内容
  processedResult.content = this.htmlToText(result.content);
  
  this.currentLayoutResult = processedResult;
  this.showLayoutDialog(processedResult);
}
,

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
 // 打印原始内容长度
  console.log('=== 排版前原始内容长度 ===', this.currentLayoutResult.content.length);
        // 公众号投递：创建排版任务
        this.createWechatLayoutTask();
       // 关闭自动评分
  this.autoScoreEnabled = false;
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

        {
          if(ai.name === "豆包") {
            layoutRequest.params.roles = layoutRequest.params.roles + "zj-db,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zj-db-sdsk,";
            }
          }


          if(ai.name === '通义千问') {
            layoutRequest.params.roles = layoutRequest.params.roles + 'ty-qw,';
            if(ai.selectedCapability.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + 'ty-qw-sdsk,'
            }
          }

          if(ai.name === '腾讯元宝') {
            // 根据选择的模型设置角色
            if(ai.selectedModel === 'hunyuan') {
              layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-hunyuan-lwss,';
              }
            } else if(ai.selectedModel === 'deepseek') {
              layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-pt,';
              if(ai.selectedCapabilities.includes("deep_thinking")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-sdsk,';
              }
              if(ai.selectedCapabilities.includes("web_search")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'yb-deepseek-lwss,';
              }
            }
          }
          if(ai.name === '百度AI') {
            layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-agent,';
            if(ai.selectedCapabilities.includes("deep_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-sdss,';
            } else if(ai.isModel) {
              if(ai.isWeb) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-web,';
              }

              if(ai.selectedModel.includes("dsr1")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-dsr1,';
              } else if(ai.selectedModel.includes("dsv3")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-dsv3,';
              } else if(ai.selectedModel.includes("wenxin")) {
                layoutRequest.params.roles = layoutRequest.params.roles + 'baidu-wenxin,';
              }
            }

          }

          if(ai.name === "DeepSeek") {
            layoutRequest.params.roles = layoutRequest.params.roles + "deepseek,";
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "ds-sdsk,";
            }
            if(ai.selectedCapabilities.includes("web_search")) {
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
            if(ai.selectedCapabilities.includes("deep_thinking")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-sdsk,";
            }
            if(ai.selectedCapabilities.includes("all_web_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-qw,";
            }
            if(ai.selectedCapabilities.includes("zhihu_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-zh,";
            }
            if(ai.selectedCapabilities.includes("academic_search")) {
              layoutRequest.params.roles = layoutRequest.params.roles + "zhzd-xs,";
            }
            if(ai.selectedCapabilities.includes("personal_knowledge")) {
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
        this.$message.success("排版请求已发送，请等待结果");
      },



      // 实际投递到公众号
      pushToMediaWithContent(result) {
        if(this.pushingToWechat) return;

        // 验证内容是否为空
        if(!result.content || result.content.trim() === '') {
          this.$message.error("投递内容为空，请先进行AI排版生成内容");
          return;
        }

        this.$message.success("开始投递公众号！");
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
                this.$message.success("投递到公众号成功！");
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
    // 先打印原始HTML内容
  console.log('=== 原始HTML内容 ===');
  console.log(result.content);
  console.log('=== 原始内容长度 ===', result.content.length);
  // 将HTML内容转换为Markdown格式
  const markdownContent = this.htmlToMarkdown(result.content);
   // 打印转换后的Markdown内容到控制台
  console.log('=== 转换后的Markdown内容 ===');
  console.log(markdownContent);
  console.log('=== 内容结束 ===');
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
      userPrompt: markdownContent, // 使用转换后的Markdown内容
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
          this.$message.success("知乎投递请求已发送，请等待结果");
          this.pushingToWechat = false;
        }
      },

    },
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
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin-bottom: 0px;
    margin-left: 20px;
    margin-top: 10px;
  }

  .ai-card {
    width: calc(25% - 20px);
    box-sizing: border-box;
  }

  .ai-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .ai-left {
    display: flex;
    align-items: center;
  }

  .ai-avatar {
    margin-right: 10px;
  }

  .ai-avatar img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    object-fit: cover;
  }

  .ai-name {
    font-weight: bold;
    font-size: 12px;
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

  @media (max-width: 1200px) {
    .ai-card {
      width: calc(33.33% - 14px);
    }
  }

  @media (max-width: 992px) {
    .ai-card {
      width: calc(50% - 10px);
    }
  }

  @media (max-width: 768px) {
    .ai-card {
      width: 100%;
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
    margin-bottom: 15px;
    border-radius: 4px;
    background-color: #f5f7fa;
    overflow: hidden;
  }

  .history-parent {
    padding: 10px;
    cursor: pointer;
    transition: background-color 0.3s;
    border-bottom: 1px solid #ebeef5;
  }

  .history-parent:hover {
    background-color: #ecf5ff;
  }

  .history-children {
    padding-left: 20px;
    background-color: #fff;
    transition: all 0.3s ease;
  }

  .history-child-item {
    padding: 8px 10px;
    cursor: pointer;
    transition: background-color 0.3s;
    border-bottom: 1px solid #f0f0f0;
  }

  .history-child-item:last-child {
    border-bottom: none;
  }

  .history-child-item:hover {
    background-color: #f5f7fa;
  }

  .history-header {
    display: flex;
    align-items: flex-start;
    gap: 8px;
  }

  .history-header .el-icon-arrow-right {
    font-size: 14px;
    color: #909399;
    transition: transform 0.3s;
    cursor: pointer;
    margin-top: 3px;
  }

  .history-header .el-icon-arrow-right.is-expanded {
    transform: rotate(90deg);
  }

  .history-prompt {
    font-size: 14px;
    color: #303133;
    margin-bottom: 5px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    flex: 1;
  }

  .history-time {
    font-size: 12px;
    color: #909399;
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


  /* 审核弹窗样式 */
  .review-dialog-content {
    padding: 20px;
  }

  .review-dialog-content p {
    margin-bottom: 15px;
    color: #606266;
  }
  /* 可见度评估按钮样式 */
.visibility-btn {
  border-radius: 16px;
  padding: 6px 12px;
}

.prompt-button {
  margin-left: 10px;
  background-color: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
}
.prompt-button:hover {
  background-color: #66b1ff !important;
  border-color: #66b1ff !important;
}
.prompt-dialog-content {
  padding: 20px;
}

.prompt-content {
  max-height: 100px;
  overflow-y: auto;
  white-space: pre-wrap;
}
.review-dialog-content {
  padding: 20px;
}

.review-content {
  margin: 15px 0;
}

.review-content :deep(.el-textarea__inner) {
  font-family: monospace;
  line-height: 1.5;
}

.review-buttons {
  margin-top: 20px;
  text-align: right;
}

.review-buttons .el-button {
  margin-left: 10px;
}
.review-dialog-content {
  padding: 20px;
}

.ai-selector {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-selector span {
  color: #606266;
  font-size: 14px;
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

.markdown-content::-webkit-scrollbar {
  width: 6px;
}

.markdown-content::-webkit-scrollbar-thumb {
  background-color: #DCDFE6;
  border-radius: 3px;
}

.markdown-content::-webkit-scrollbar-track {
  background-color: #F5F7FA;
}

.review-buttons {
  margin-top: 20px;
  text-align: right;
}

.review-buttons .el-button {
  margin-left: 10px;
}

/* 复用评分结果的样式 */
:deep(.markdown-content h1),
:deep(.markdown-content h2),
:deep(.markdown-content h3),
:deep(.markdown-content h4),
:deep(.markdown-content h5),
:deep(.markdown-content h6) {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
}

:deep(.markdown-content p) {
  margin-bottom: 16px;
}

:deep(.markdown-content ul),
:deep(.markdown-content ol) {
  margin-bottom: 16px;
  padding-left: 2em;
}

:deep(.markdown-content li) {
  margin-bottom: 0.25em;
}

:deep(.markdown-content blockquote) {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
  margin-bottom: 16px;
}

:deep(.markdown-content code) {
  padding: 0.2em 0.4em;
  margin: 0;
  font-size: 85%;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
}

:deep(.markdown-content pre) {
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
  background-color: #f6f8fa;
  border-radius: 6px;
  margin-bottom: 16px;
}

:deep(.markdown-content pre code) {
  display: inline;
  max-width: auto;
  padding: 0;
  margin: 0;
  overflow: visible;
  line-height: inherit;
  word-wrap: normal;
  background-color: transparent;
  border: 0;
}
.review-content-input {
  margin: 15px 0;
}

.review-content-input :deep(.el-textarea__inner) {
  font-family: monospace;
  line-height: 1.6;
  padding: 15px;
  resize: vertical;
}

.review-buttons {
  margin-top: 20px;
  text-align: right;
}

.review-buttons .el-button {
  margin-left: 10px;
}
.prompt-section {
  margin-bottom: 15px;
}

.prompt-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.prompt-header span {
  color: #606266;
  font-size: 14px;
}

.current-prompt {
  color: #909399;
  font-size: 12px;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}


.review-content-input {
  margin: 15px 0;
}

.review-content-input :deep(.el-textarea__inner) {
  font-family: monospace;
  line-height: 1.6;
  padding: 15px;
  resize: vertical;
}
.prompt-section {
  margin-bottom: 15px;
}

.prompt-section p {
  margin-bottom: 10px;
  color: #606266;
}

.review-content-input {
  margin: 15px 0;
}

.review-content-input :deep(.el-textarea__inner) {
  font-family: monospace;
  line-height: 1.6;
  padding: 15px;
  resize: vertical;
}
.prompt-button-section {
  margin: 15px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.current-prompt {
  color: #909399;
  font-size: 12px;
}

.content-section {
  margin-bottom: 20px;
}

.content-section p {
  margin-bottom: 10px;
  color: #606266;
}

.review-content-input {
  margin: 15px 0;
}

.review-content-input :deep(.el-textarea__inner) {
  font-family: monospace;
  line-height: 1.6;
  padding: 15px;
  resize: vertical;
}

.review-buttons {
  text-align: right;
}

.review-buttons .el-button {
  margin-left: 10px;
}
.mode-switch {
  margin-bottom: 20px;
  text-align: center;
}

.prompt-dialog-content {
  padding: 20px;
}

.prompt-content {
  max-height: 100px;
  overflow-y: auto;
  white-space: pre-wrap;
}
.ai-selector {
  margin: 20px 0;
}

.ai-selector span {
  display: block;
  margin-bottom: 10px;
  color: #606266;
}

.ai-button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.ai-button {
  margin: 0;
}

.review-buttons {
  text-align: right;
  margin-top: 20px;
}

.review-buttons .el-button {
  margin-left: 10px;
}
.visibility-dialog-content {
  padding: 20px;
}

.keyword-input-section {
  margin-bottom: 20px;
}

.keyword-input-section h3 {
  margin-bottom: 10px;
  color: #303133;
}

.prompt-section h3 {
  margin-bottom: 10px;
  color: #303133;
}

.keyword-input,
.prompt-input {
  width: 100%;
}

.dialog-footer {
  text-align: right;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.header-buttons {
  display: flex;
  gap: 10px;
}
.button-group {
  display: flex;
  gap: 10px;
}
.prompt-dialog-header {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}
.el-table .el-button--text {
  padding: 2px 4px;
  margin-left: 2px;
}

.el-table .el-button--text:first-child {
  margin-left: 0;
}

.el-table .el-button--text i {
  margin-right: 2px;
}

/* 确保表格内容不换行 */
.el-table .cell {
  white-space: nowrap;
}
.ai-button.is-active {
  background-color: #409eff !important;
  border-color: #409eff !important;
  color: #fff !important;
}

.ai-button {
  transition: all 0.3s ease;
}

.ai-button:hover {
  opacity: 0.8;
}
</style>