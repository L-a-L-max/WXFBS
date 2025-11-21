<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="AI名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入AI名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="AI状态" prop="agentStatus">
        <el-select v-model="queryParams.agentStatus" placeholder="请选择AI状态" clearable style="width: 120px">
          <el-option label="正常" value="1" />
          <el-option label="停用" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="在线状态" prop="onlineStatus">
        <el-select v-model="queryParams.onlineStatus" placeholder="请选择在线状态" clearable style="width: 120px">
          <el-option label="离线" value="0" />
          <el-option label="在线" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="handleQuery">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button size="small" @click="resetQuery">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          size="small"
          @click="handleAdd"
          v-hasPermi="['system:aiagent:add']"
        >
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          size="small"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:aiagent:edit']"
        >
          <el-icon><Edit /></el-icon>
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          size="small"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:aiagent:remove']"
        >
          <el-icon><Delete /></el-icon>
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          size="small"
          @click="handleExport"
          v-hasPermi="['system:aiagent:export']"
        >
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          size="small"
          @click="handleRefreshCache"
          v-hasPermi="['system:aiagent:edit']"
        >
          <el-icon><Refresh /></el-icon>
          刷新缓存
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="aiagentList" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />
      <el-table-column label="图标" align="center" prop="agentIcon" width="90">
        <template #default="scope">
          <img v-if="scope.row.agentIcon" :src="scope.row.agentIcon" style="width: 40px; height: 40px; border-radius: 50%;" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="AI名称" align="center" prop="agentName" width="160" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="agentStatus" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.agentStatus"
            :active-value="1"
            :inactive-value="0"
            @change="handleAgentStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="在线" align="center" prop="onlineStatus" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.onlineStatus"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sortOrder" width="100" />
      <el-table-column label="全局" align="center" prop="isGlobal" width="100">
        <template #default="scope">
          <el-switch
            v-model="scope.row.isGlobal"
            :active-value="1"
            :inactive-value="0"
            @change="handleGlobalChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="150">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="90" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            size="small"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:aiagent:edit']"
          >
            <el-icon><Edit /></el-icon>
            修改
          </el-button>
          <el-button
            size="small"
            type="text"
            @click="handlePermission(scope.row)"
            v-hasPermi="['system:aiagent:edit']"
          >
            <el-icon><Setting /></el-icon>
            权限
          </el-button>
          <el-button
            size="small"
            type="text"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:aiagent:remove']"
          >
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改AI智能体对话框 -->
    <el-dialog :title="title" v-model="open" width="900px" append-to-body class="ai-agent-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" class="beautiful-form">
        
        <!-- 基础信息卡片 -->
        <div class="form-section">
          <div class="section-header">
            <el-icon><InfoFilled /></el-icon>
            <span>基础信息</span>
          </div>
          <div class="section-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="AI名称" prop="agentName">
                  <el-input v-model="form.agentName" placeholder="请输入AI名称" clearable>
                    <template #prefix>
                      <el-icon><User /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="智能体代码" prop="agentCode">
                  <el-input v-model="form.agentCode" placeholder="baidu" clearable>
                    <template #prefix>
                      <el-icon><Key /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="AI状态" prop="agentStatus">
                  <el-switch
                    v-model="form.agentStatus"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="正常"
                    inactive-text="停用"
                    inline-prompt
                    style="--el-switch-on-color: #67c23a; --el-switch-off-color: #909399"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="在线状态" prop="onlineStatus">
                  <el-switch
                    v-model="form.onlineStatus"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="在线"
                    inactive-text="离线"
                    inline-prompt
                    style="--el-switch-on-color: #409eff; --el-switch-off-color: #909399"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="全局可用" prop="isGlobal">
                  <el-switch
                    v-model="form.isGlobal"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="是"
                    inactive-text="否"
                    inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #909399"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="排序顺序" prop="sortOrder">
                  <el-input-number 
                    v-model="form.sortOrder" 
                    controls-position="right" 
                    :min="0" 
                    :max="999"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="后端标识" prop="backendInterface">
                  <el-input v-model="form.backendInterface" placeholder="startBaidu" clearable>
                    <template #prefix>
                      <el-icon><Connection /></el-icon>
                    </template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="AI图标" prop="agentIcon">
              <div class="icon-input-with-preview">
                <el-input v-model="form.agentIcon" placeholder="请输入AI图标URL，如：https://u3w.com/chatfile/baiduAI.png" clearable>
                  <template #prefix>
                    <el-icon><Picture /></el-icon>
                  </template>
                </el-input>
                <div v-if="form.agentIcon" class="icon-preview-box">
                  <img :src="form.agentIcon" class="icon-preview-img" @error="handleIconError" />
                  <span class="preview-label">预览</span>
                </div>
              </div>
            </el-form-item>
          </div>
        </div>

        <!-- API配置卡片 -->
        <div class="form-section">
          <div class="section-header">
            <el-icon><Link /></el-icon>
            <span>API接口配置</span>
          </div>
          <div class="section-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="登录检测" prop="loginCheckApi">
                  <el-input v-model="form.loginCheckApi" placeholder="/api/browser/checkBaiduLogin" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="登录二维码" prop="loginQrcodeApi">
                  <el-input v-model="form.loginQrcodeApi" placeholder="/api/browser/getBaiduQrCode" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="AI咨询" prop="chatApi">
              <el-input v-model="form.chatApi" placeholder="/api/browser/startBaidu" clearable />
            </el-form-item>
          </div>
        </div>

        <!-- WebSocket配置卡片 -->
        <div class="form-section">
          <div class="section-header">
            <el-icon><Connection /></el-icon>
            <span>WebSocket消息类型</span>
          </div>
          <div class="section-content">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="登录检测" prop="websocketCheckType">
                  <el-input v-model="form.websocketCheckType" placeholder="PLAY_CHECK_BAIDU_LOGIN" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="获取二维码" prop="websocketQrcodeType">
                  <el-input v-model="form.websocketQrcodeType" placeholder="PLAY_GET_BAIDU_QRCODE" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="聊天消息" prop="websocketChatType">
                  <el-input v-model="form.websocketChatType" placeholder="START_BAIDU" clearable />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 描述和配置卡片 -->
        <div class="form-section">
          <div class="section-header">
            <el-icon><Document /></el-icon>
            <span>描述与配置</span>
          </div>
          <div class="section-content">
            <el-form-item label="AI描述" prop="description">
              <el-input 
                v-model="form.description" 
                type="textarea" 
                placeholder="百度AI智能助手，支持模型选择和搜索模式配置" 
                :rows="2"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="选项配置">
                  <div class="config-card">
                    <div v-if="configOptions && configOptions.length > 0" class="config-summary">
                      <div class="config-stats">
                        <div class="stat-item">
                          <el-icon class="stat-icon"><Tools /></el-icon>
                          <span class="stat-value">{{ configOptions.length }}</span>
                          <span class="stat-label">个选项</span>
                        </div>
                        <div class="stat-divider"></div>
                        <div class="stat-tags">
                          <el-tag v-if="getSelectOptionsCount() > 0" type="primary" effect="plain" size="small">
                            <el-icon><Select /></el-icon>
                            {{ getSelectOptionsCount() }} 下拉
                          </el-tag>
                          <el-tag v-if="getButtonOptionsCount() > 0" type="success" effect="plain" size="small">
                            <el-icon><Pointer /></el-icon>
                            {{ getButtonOptionsCount() }} 按钮
                          </el-tag>
                        </div>
                      </div>
                      <el-button type="primary" plain size="small" @click="openEnhancedConfig(form)">
                        <el-icon><Edit /></el-icon>
                        修改配置
                      </el-button>
                    </div>
                    <div v-else class="config-empty">
                      <el-empty description="暂未配置选项" :image-size="80">
                        <el-button type="primary" @click="openEnhancedConfig(form)">
                          <el-icon><Plus /></el-icon>
                          配置选项
                        </el-button>
                      </el-empty>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input 
                    v-model="form.remark" 
                    type="textarea" 
                    placeholder="请输入备注信息" 
                    :rows="2"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>
        
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 权限管理对话框 -->
    <el-dialog title="权限管理" v-model="permissionOpen" width="1200px" append-to-body @close="resetPermissionForm">
      <el-tabs v-model="activeTab">
        <!-- 角色权限标签页 -->
        <el-tab-pane label="角色权限" name="role">
          <el-table :data="rolePermissions" style="width: 100%" v-loading="rolePermissionsLoading">
            <el-table-column prop="roleId" label="角色ID" min-width="80" />
            <el-table-column prop="roleName" label="角色名称" min-width="150" />
            <el-table-column prop="roleKey" label="角色标识" min-width="150" />
            <el-table-column label="权限状态" align="center" min-width="200">
              <template #default="scope">
                <el-select 
                  v-model="scope.row.permissionAction" 
                  @change="handleRolePermissionChange(scope.row)"
                  placeholder="未设置"
                  clearable
                  style="width: 100%"
                >
                  <el-option label="允许" :value="1" />
                  <el-option label="禁止" :value="0" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="100">
              <template #default="scope">
                <el-button size="small" type="text" @click="handleSaveRolePermission(scope.row)">保存</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 用户权限标签页 -->
        <el-tab-pane label="用户权限" name="user">
          <el-row :gutter="10" class="mb8" style="display: flex; align-items: center;">
            <el-col :span="8">
              <el-select 
                v-model="newUserPermission.userId" 
                placeholder="输入昵称或用户ID搜索"
                style="width: 100%"
                size="small"
                filterable
                remote
                :remote-method="searchUsers"
                :loading="userSearchLoading"
                clearable
                @clear="handleUserSearchClear"
              >
                <el-option 
                  v-for="user in filteredUsers" 
                  :key="user.userId" 
                  :label="`${user.nickName} (ID: ${user.userId})`" 
                  :value="user.userId"
                >
                  <span style="float: left">{{ user.nickName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">ID: {{ user.userId }}</span>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              <el-select 
                v-model="newUserPermission.permissionAction" 
                placeholder="请选择权限"
                style="width: 100%"
                size="small"
              >
                <el-option label="允许" :value="1" />
                <el-option label="禁止" :value="0" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" size="small" @click="handleAddUserPermission">添加权限</el-button>
            </el-col>
          </el-row>

          <el-table :data="userPermissions" style="width: 100%; margin-top: 10px" v-loading="userPermissionsLoading">
            <el-table-column prop="targetId" label="用户ID" min-width="80" />
            <el-table-column prop="targetName" label="用户名称" min-width="150" />
            <el-table-column label="权限状态" align="center" min-width="150">
              <template #default="scope">
                <el-tag :type="scope.row.permissionAction === 1 ? 'success' : 'danger'">
                  {{ scope.row.permissionAction === 1 ? '允许' : '禁止' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="200">
              <template #default="scope">
                <el-button size="small" type="text" @click="handleEditUserPermission(scope.row)">修改</el-button>
                <el-button size="small" type="text" @click="handleDeleteUserPermission(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="permissionOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 选项配置对话框 -->
    <el-dialog
      :title="optionDialogTitle"
      v-model="showOptionDialog"
      width="800px"
      append-to-body
      @close="resetOptionForm"
    >
      <el-form ref="optionForm" :model="currentOption" :rules="optionRules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="选项类型" prop="type">
              <el-select v-model="currentOption.type" placeholder="请选择选项类型" @change="onOptionTypeChange">
                <el-option label="按钮" value="button" />
                <el-option label="单选框" value="select" />
                <el-option label="多选框" value="checkbox" />
                <el-option label="输入框" value="input" />
                <el-option label="开关" value="switch" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选项标签" prop="label">
              <el-input v-model="currentOption.label" placeholder="请输入选项显示名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="选项ID" prop="id">
              <el-input v-model="currentOption.id" placeholder="请输入唯一标识符" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 按钮类型配置 -->
        <div v-if="currentOption.type === 'button'">
          <el-form-item label="按钮值" prop="value">
            <el-input v-model="currentOption.value" placeholder="请输入按钮点击时的值" />
          </el-form-item>
        </div>

        <!-- 输入框类型配置 -->
        <div v-if="currentOption.type === 'input'">
          <el-row>
            <el-col :span="12">
              <el-form-item label="输入类型">
                <el-select v-model="currentOption.inputType">
                  <el-option label="文本" value="text" />
                  <el-option label="数字" value="number" />
                  <el-option label="密码" value="password" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="占位符">
                <el-input v-model="currentOption.placeholder" placeholder="请输入占位符文本" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 选择类型配置 -->
        <div v-if="['select', 'checkbox'].includes(currentOption.type)">
          <el-form-item label="选择类型">
            <el-radio-group v-model="currentOption.selectType">
              <el-radio label="single">单选</el-radio>
              <el-radio label="multiple" v-if="currentOption.type === 'checkbox'">多选</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选项值">
            <div class="values-config">
              <div class="values-header">
                <el-button type="primary" size="small" @click="addOptionValue">
                  <i class="el-icon-plus"></i> 添加选项
                </el-button>
              </div>

              <div class="values-list" v-if="currentOption.values && currentOption.values.length > 0">
                <div
                  v-for="(value, index) in currentOption.values"
                  :key="index"
                  class="value-item"
                >
                  <el-input
                    v-model="value.label"
                    placeholder="显示文本"
                    size="small"
                    style="width: 150px; margin-right: 10px;"
                  />
                  <el-input
                    v-model="value.value"
                    placeholder="实际值"
                    size="small"
                    style="width: 150px; margin-right: 10px;"
                  />
                  <el-checkbox
                    v-model="value.default"
                    @change="onDefaultChange(index)"
                    style="margin-right: 10px;"
                  >默认</el-checkbox>
                  <el-button
                    type="text"
                    size="small"
                    @click="removeOptionValue(index)"
                    style="color: #f56c6c;"
                  >
                    <i class="el-icon-delete"></i>
                  </el-button>
                </div>
              </div>

              <div v-else class="empty-values">
                <p>暂无选项值，点击"添加选项"开始配置</p>
              </div>
            </div>
          </el-form-item>
        </div>

        <!-- 冲突检测 -->
        <el-form-item label="冲突检测" v-if="hasConflicts">
          <el-alert
            title="检测到配置冲突"
            type="warning"
            :description="conflictMessage"
            show-icon
            :closable="false"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="saveOption" :disabled="hasConflicts">确 定</el-button>
          <el-button @click="showOptionDialog = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 增强的选项配置对话框 -->
    <el-dialog
      :title="configDialogTitle"
      v-model="showConfigDialog"
      width="900px"
      :close-on-click-modal="false"
      class="enhanced-config-dialog"
    >
      <div class="config-container1">
        <!-- 左侧配置区域 -->
        <div class="config-panel">
          <el-tabs v-model="activeConfigTab" type="border-card">
            <!-- 单选框配置 -->
            <el-tab-pane label="单选框选项" name="select">
              <div class="tab-content">
                <div class="section-header">
                  <h4>单选框选项配置</h4>
                  <el-button type="primary" size="small" @click="addNewSelectOption">
                    <el-icon><Plus /></el-icon>
                    添加单选框
                  </el-button>
                </div>

                <div v-if="enhancedSelectOptions.length === 0" class="empty-state">
                  <el-empty description="暂无单选框选项" :image-size="80">
                    <el-button type="primary" @click="addNewSelectOption">添加第一个单选框</el-button>
                  </el-empty>
                </div>

                <div v-else class="options-list">
                  <div v-for="(option, index) in enhancedSelectOptions" :key="option.id" class="option-card">
                    <div class="option-header">
                      <el-input
                        v-model="option.label"
                        placeholder="请输入选项名称"
                        size="small"
                        style="width: 200px;"
                                              />
                      <div class="option-actions">
                        <el-button type="text" size="small" @click="removeEnhancedSelectOption(index)" style="color: #f56c6c;">
                          <el-icon><Delete /></el-icon>
                          删除单选框
                        </el-button>
                      </div>
                    </div>

                    <div class="values-section">
                      <div class="values-header">
                        <span>选项值</span>
                        <div class="values-actions">
                          <el-button type="text" size="small" @click="addSelectValue(index)">
                            <el-icon><Plus /></el-icon>
                            添加选项值
                          </el-button>
                        </div>
                      </div>

                      <div v-if="option.values.length === 0" class="empty-values">
                        <p>请添加选项值</p>
                        <el-button type="primary" size="small" @click="addSelectValue(index)">添加选项值</el-button>
                      </div>

                      <div v-else class="values-list">
                        <div v-for="(value, vIndex) in option.values" :key="vIndex" class="value-item">
                          <el-input
                            v-model="value.label"
                            placeholder="显示名称"
                            size="small"
                            style="width: 120px;"
                                                      />
                          <el-input
                            v-model="value.value"
                            placeholder="选项值"
                            size="small"
                            style="width: 120px;"
                                                      />
                          <el-checkbox
                            v-model="value.default"
                            @change="handleEnhancedDefaultChange(index, vIndex)"
                          >
                            默认
                          </el-checkbox>
                          <el-button type="text" size="small" @click="removeSelectValue(index, vIndex)" style="color: #f56c6c;">
                            <el-icon><Delete /></el-icon>
                          </el-button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 按钮配置 -->
            <el-tab-pane label="按钮选项" name="button">
              <div class="tab-content">
                <div class="section-header">
                  <h4>按钮选项配置</h4>
                  <el-button type="primary" size="small" @click="addNewButtonOption">
                    <el-icon><Plus /></el-icon>
                    添加按钮
                  </el-button>
                </div>

                <div v-if="enhancedButtonOptions.length === 0" class="empty-state">
                  <el-empty description="暂无按钮选项" :image-size="80">
                    <el-button type="primary" @click="addNewButtonOption">添加第一个按钮</el-button>
                  </el-empty>
                </div>

                <div v-else class="options-list">
                  <div v-for="(option, index) in enhancedButtonOptions" :key="option.id" class="option-card">
                    <div class="button-config">
                      <el-form-item label="按钮名称">
                        <el-input
                          v-model="option.label"
                          placeholder="请输入按钮名称"
                                                  />
                      </el-form-item>
                      <el-form-item label="按钮值">
                        <el-input
                          v-model="option.value"
                          placeholder="请输入按钮值"
                                                  />
                      </el-form-item>
                      <div class="option-actions">
                        <el-button type="danger" plain size="small" @click="removeEnhancedButtonOption(index)">
                          <el-icon><Delete /></el-icon>
                          删除按钮
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 互斥配置 -->
            <el-tab-pane label="互斥配置" name="conflicts">
              <div class="tab-content">
                <div class="section-header">
                  <div class="header-content">
                    <h4>选项互斥配置</h4>
                    <span class="section-desc">设置选项之间的互斥关系，支持部分选项值互斥</span>
                  </div>
                </div>

                <div v-if="allEnhancedOptions.length === 0" class="empty-state">
                  <el-empty description="请先配置选项" :image-size="80" />
                </div>

                <div v-else class="conflicts-list">
                  <div v-for="option in allEnhancedOptions" :key="option.id" class="conflict-item">
                    <div class="conflict-header">
                      <span class="option-name">{{ option.label || '未命名选项' }}</span>
                      <el-tag :type="option.type === 'select' ? 'primary' : 'success'" size="small">
                        {{ option.type === 'select' ? '下拉选择' : '按钮' }}
                      </el-tag>
                    </div>

                    <!-- 下拉选择的互斥配置 -->
                    <div v-if="option.type === 'select'" class="select-conflicts">
                      <div v-for="(value, vIndex) in option.values" :key="vIndex" class="value-conflict">
                        <div class="value-info">
                          <span class="value-label">{{ value.label || '未命名值' }}</span>
                          <el-tag size="mini" type="info">{{ value.value || '空值' }}</el-tag>
                        </div>
                        <div class="conflict-config">
                          <span>与以下选项互斥：</span>
                          <el-select
                            v-model="value.conflicts"
                            multiple
                            placeholder="选择互斥的选项"
                            style="width: 250px;"
                            size="small"
                          >
                            <el-option
                              v-for="opt in getConflictOptions(option.id)"
                              :key="opt.id"
                              :label="opt.label"
                              :value="opt.id"
                            />
                          </el-select>
                        </div>
                      </div>
                    </div>

                    <!-- 按钮的互斥配置 -->
                    <div v-else class="button-conflicts">
                      <div class="conflict-config">
                        <span>与以下选项互斥：</span>
                        <el-select
                          v-model="option.conflicts"
                          multiple
                          placeholder="选择互斥的选项"
                          style="width: 300px;"
                        >
                          <el-option
                            v-for="opt in getConflictOptions(option.id)"
                            :key="opt.id"
                            :label="opt.label"
                            :value="opt.id"
                          />
                        </el-select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelEnhancedConfig">取消</el-button>
          <el-button type="primary" @click="saveEnhancedConfig">
            {{ isEditMode ? '保存修改' : '确认添加' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- JSON预览对话框 -->
    <el-dialog title="配置JSON预览" v-model="showJsonPreview" width="600px" append-to-body>
      <el-input
        v-model="previewJsonString"
        type="textarea"
        :rows="15"
        readonly
        style="font-family: 'Courier New', monospace;"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="copyJsonToClipboard">复制到剪贴板</el-button>
          <el-button @click="showJsonPreview = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import { 
  Plus, Edit, Delete, Download, Refresh, Setting, Search, Tools,
  InfoFilled, User, Key, Connection, Picture, Link, Document,
  Select, Pointer
} from '@element-plus/icons-vue';
import { addAiagent, delAiagent, getAiagent, listAiagent, updateAiagent, updateOnlineStatus, refreshCache } from "@/api/system/aiagent";
import { listRole } from "@/api/system/role";
import { listUser } from "@/api/system/user";
import { setUserPermission, setRolePermission, getAiAgentUserPermissions, getAiAgentRolePermissions, deleteAiagentPermission } from "@/api/system/aiagent-permission";
import { ElIcon } from 'element-plus';

export default {
  name: "Aiagent",
  dicts: ['sys_normal_disable', 'sys_yes_no'],
  components: {
    Plus, Edit, Delete, Download, Refresh, Setting, Search, Tools, ElIcon,
    InfoFilled, User, Key, Connection, Picture, Link, Document,
    Select, Pointer
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
      // AI智能体表格数据
      aiagentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 权限管理弹出层
      permissionOpen: false,
      // 当前选中的智能体
      currentAgent: null,
      // 权限管理标签页
      activeTab: "role",
      // 角色权限列表
      rolePermissions: [],
      // 用户权限列表
      userPermissions: [],
      // 所有用户列表
      allUsers: [],
      // 过滤后的用户列表（用于搜索）
      filteredUsers: [],
      // 用户搜索加载状态
      userSearchLoading: false,
      // 新增用户权限表单
      newUserPermission: {
        userId: null,
        permissionAction: 1
      },
      // 角色权限加载状态
      rolePermissionsLoading: false,
      // 用户权限加载状态
      userPermissionsLoading: false,

      // 简化的选项配置相关
      showConfigDialog: false,
      showOptionDialog: false,
      showJsonPreview: false,
      activeConfigTab: 'select',
      configOptions: [],
      selectedValues: {}, // 存储用户选择的值
      conflictEnabled: false,

      // 单选框配置
      selectConfig: {
        id: '',
        type: 'select',
        label: '模型选择',
        selectType: 'single',
        values: []
      },

      // 按钮配置
      buttonConfig: {
        id: 'action_button',
        type: 'button',
        label: '',
        value: ''
      },
      editingOptionIndex: -1,
      previewJsonString: '',

      // 增强配置相关数据
      configDialogTitle: 'AI选项配置',
      isEditMode: false,
      currentPreviewAgent: {},

      // 增强的选项配置
      enhancedSelectOptions: [],
      enhancedButtonOptions: [],

      // 预览相关
      previewEnabled: true,
      previewOptions: [],
      previewValues: {},

      // 图标选项
      iconOptions: [
        { label: '百度AI', value: 'https://u3w.com/chatfile/baiduAI.png' },
        { label: '豆包', value: 'https://u3w.com/chatfile/豆包.png' },
        { label: 'DeepSeek', value: 'https://u3w.com/chatfile/Deepseek.png' },
        { label: '秘塔', value: 'https://u3w.com/chatfile/Metaso.png' },
        { label: '腾讯元宝', value: 'https://u3w.com/chatfile/yuanbao.png' },
        { label: '知乎直答', value: 'https://u3w.com/chatfile/ZHZD.png' },
        { label: 'Kimi', value: 'https://u3w.com/chatfile/kimi.png' }
      ],

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        agentName: null,
        agentCode: null,
        agentStatus: null,
        onlineStatus: null,
        isGlobal: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agentName: [
          { required: true, message: "AI名称不能为空", trigger: "blur" }
        ],
        agentCode: [
          { required: true, message: "智能体代码不能为空", trigger: "blur" }
        ],
        agentStatus: [
          { required: true, message: "AI状态不能为空", trigger: "change" }
        ],
        agentIcon: [
          { required: true, message: "AI图标不能为空", trigger: "blur" }
        ]
      },

      // 选项表单验证规则
      optionRules: {
        type: [
          { required: true, message: "请选择选项类型", trigger: "change" }
        ],
        label: [
          { required: true, message: "请输入选项标签", trigger: "blur" }
        ],
        id: [
          { required: true, message: "请输入选项ID", trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    // 选项对话框标题
    optionDialogTitle() {
      return this.editingOptionIndex >= 0 ? '编辑选项' : '添加选项';
    },

    // 检测配置冲突
    hasConflicts() {
      if (!this.currentOption.id) return false;
      if (!this.configOptions || !Array.isArray(this.configOptions)) return false;

      return this.configOptions.some((option, index) => {
        if (this.editingOptionIndex >= 0 && index === this.editingOptionIndex) return false;
        return option.id === this.currentOption.id;
      });
    },

    // 冲突消息
    conflictMessage() {
      if (!this.hasConflicts) return '';
      if (!this.configOptions || !Array.isArray(this.configOptions)) return '';

      const conflictOption = this.configOptions.find((option, index) => {
        if (this.editingOptionIndex >= 0 && index === this.editingOptionIndex) return false;
        return option.id === this.currentOption.id;
      });

      if (conflictOption) {
        if (conflictOption.id === this.currentOption.id) {
          return `选项ID "${this.currentOption.id}" 已被选项 "${conflictOption.label}" 使用`;
        }
      }
      return '存在配置冲突';
    },

    // 增强配置相关计算属性
    allEnhancedOptions() {
      return [...this.enhancedSelectOptions, ...this.enhancedButtonOptions];
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询AI智能体列表 */
    getList() {
      this.loading = true;
      listAiagent(this.queryParams).then(response => {
        this.aiagentList = response.rows;
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
        agentName: null,
        agentCode: null,
        agentIcon: null,
        backendInterface: null,
        loginCheckApi: null,
        loginQrcodeApi: null,
        chatApi: null,
        websocketCheckType: null,
        websocketQrcodeType: null,
        websocketChatType: null,
        agentStatus: 1,
        onlineStatus: 0,
        sortOrder: 0,
        isGlobal: 1,
        configJson: null,
        description: null,
        remark: null
      };
      // 重置选项配置
      this.configOptions = [];
      if (this.$refs["form"]) {
        this.$refs["form"].resetFields();
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      if (this.$refs["queryForm"]) {
        this.$refs["queryForm"].resetFields();
      }
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
      this.title = "添加AI智能体";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAiagent(id).then(response => {
        this.form = response.data;
        this.parseExistingConfig();
        this.open = true;
        this.title = "修改AI智能体";
      }).catch(error => {
        console.error('获取AI智能体数据失败:', error);
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAiagent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAiagent(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除AI智能体编号为"' + ids + '"的数据项？').then(function() {
        return delAiagent(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/aiagent/export', {
        ...this.queryParams
      }, `aiagent_${new Date().getTime()}.xlsx`)
    },
    /** AI状态修改 */
    handleAgentStatusChange(row) {
      const text = row.agentStatus === 1 ? "启用" : "停用";
      updateAiagent(row).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {
        this.$modal.msgError(text + "失败");
        // 恢复原状态
        row.agentStatus = row.agentStatus === 1 ? 0 : 1;
      });
    },
    
    /** 在线状态修改 */
    handleStatusChange(row) {
      const text = row.onlineStatus === 1 ? "上线" : "下线";
      
      // 构造完整的数据对象 - 后端需要agentCode
      const data = {
        id: row.id,
        agentCode: row.agentCode,
        onlineStatus: row.onlineStatus
      };
      
      updateOnlineStatus(data).then(response => {
        this.$modal.msgSuccess(text + "成功");
        this.getList();
      }).catch(error => {
        console.error('在线状态更新失败:', error);
        this.$modal.msgError(text + "失败: " + (error.msg || error.message || '未知错误'));
        row.onlineStatus = row.onlineStatus === 1 ? 0 : 1;
      });
    },
    /** 权限管理 */
    handlePermission(row) {
      this.currentAgent = row;
      this.permissionOpen = true;
      this.activeTab = "role";
      this.resetPermissionForm();
      this.loadRolePermissions();
      this.loadUserPermissions();
      this.loadAllUsers();
    },

    /** 重置权限表单 */
    resetPermissionForm() {
      this.newUserPermission = {
        userId: null,
        permissionAction: 1
      };
    },

    /** 加载所有角色 */
    loadRolePermissions() {
      if (!this.currentAgent) return;
      this.rolePermissionsLoading = true;

      // 先加载所有角色
      listRole().then(response => {
        const allRoles = response.rows || [];
        
        // 再加载该AI的角色权限
        return getAiAgentRolePermissions(this.currentAgent.id).then(permResponse => {
          const rolePermissionsMap = {};
          
          // 构建权限映射表
          if (permResponse.rows && Array.isArray(permResponse.rows)) {
            permResponse.rows.forEach(perm => {
              rolePermissionsMap[perm.targetId] = perm.permissionAction;
            });
          }
          
          // 合并角色和权限数据
          this.rolePermissions = allRoles.map(role => ({
            roleId: role.roleId,
            roleName: role.roleName,
            roleKey: role.roleKey,
            // 如果数据库中有权限记录，使用数据库值；否则为null（未设置）
            permissionAction: rolePermissionsMap.hasOwnProperty(role.roleId) 
              ? rolePermissionsMap[role.roleId] 
              : null
          }));
        });
      }).catch(error => {
        console.error('加载角色权限失败:', error);
        this.$message.error('加载角色权限失败');
      }).finally(() => {
        this.rolePermissionsLoading = false;
      });
    },

    /** 加载所有用户 */
    loadAllUsers() {
      listUser().then(response => {
        this.allUsers = response.rows || [];
        // 初始显示前20个用户
        this.filteredUsers = this.allUsers.slice(0, 20);
      }).catch(error => {
        console.error('加载用户列表失败:', error);
        this.$message.error('加载用户列表失败');
      });
    },

    /** 搜索用户（支持昵称模糊查询和用户ID精准查询） */
    searchUsers(query) {
      if (!query || query.trim() === '') {
        // 清空搜索时显示前20个用户
        this.filteredUsers = this.allUsers.slice(0, 20);
        return;
      }

      this.userSearchLoading = true;
      const searchQuery = query.trim().toLowerCase();

      // 延迟搜索，避免频繁过滤
      setTimeout(() => {
        // 支持两种搜索方式：
        // 1. 用户ID精准匹配（数字）
        // 2. 昵称模糊匹配
        this.filteredUsers = this.allUsers.filter(user => {
          // 用户ID精准匹配
          const userIdMatch = user.userId && user.userId.toString() === searchQuery;
          
          // 昵称模糊匹配（不区分大小写）
          const nickNameMatch = user.nickName && 
            user.nickName.toLowerCase().includes(searchQuery);
          
          return userIdMatch || nickNameMatch;
        });
        
        // 限制最多显示50个结果
        if (this.filteredUsers.length > 50) {
          this.filteredUsers = this.filteredUsers.slice(0, 50);
        }

        this.userSearchLoading = false;
      }, 300);
    },

    /** 清空用户搜索 */
    handleUserSearchClear() {
      this.filteredUsers = this.allUsers.slice(0, 20);
    },

    /** 加载用户权限 */
    loadUserPermissions() {
      if (!this.currentAgent) return;
      this.userPermissionsLoading = true;

      getAiAgentUserPermissions(this.currentAgent.id).then(response => {
        this.userPermissions = response.rows || [];
      }).catch(error => {
        console.error('加载用户权限失败:', error);
        this.userPermissions = [];
      }).finally(() => {
        this.userPermissionsLoading = false;
      });
    },

    /** 保存角色权限 */
    handleSaveRolePermission(role) {
      // 检查是否已选择权限状态
      if (role.permissionAction === null || role.permissionAction === undefined) {
        this.$message.warning('请先选择权限状态');
        return;
      }

      // 保存原始值用于恢复
      const originalValue = role.permissionAction;
      const action = role.permissionAction === 1 ? '授予' : '禁止';
      
      setRolePermission(this.currentAgent.id, role.roleId, role.permissionAction).then(response => {
        this.$modal.msgSuccess(`${action}权限成功`);
        // 使用 $set 确保响应式更新
        this.$set(role, 'permissionAction', role.permissionAction);
      }).catch(error => {
        console.error('保存角色权限失败:', error);
        this.$message.error('保存权限失败');
        // 恢复原状态
        this.$set(role, 'permissionAction', originalValue);
      });
    },

    /** 角色权限变更 */
    handleRolePermissionChange(role) {
      // 权限变更时自动保存
      this.handleSaveRolePermission(role);
    },

    /** 全局状态变更 */
    handleGlobalChange(row) {
      const text = row.isGlobal === 1 ? "启用" : "停用";
      updateAiagent(row).then(() => {
        this.$modal.msgSuccess(text + "全局可用成功");
      }).catch(() => {
        this.$modal.msgError(text + "全局可用失败");
        // 恢复原状态
        row.isGlobal = row.isGlobal === 1 ? 0 : 1;
      });
    },

    /** 添加用户权限 */
    handleAddUserPermission() {
      if (!this.newUserPermission.userId) {
        this.$message.warning('请选择用户');
        return;
      }

      const user = this.allUsers.find(u => u.userId === this.newUserPermission.userId);
      if (!user) {
        this.$message.error('用户不存在');
        return;
      }

      setUserPermission(this.currentAgent.id, this.newUserPermission.userId, this.newUserPermission.permissionAction).then(response => {
        this.$modal.msgSuccess('添加用户权限成功');
        
        // 重新加载权限列表，获取真实的数据库ID
        this.loadUserPermissions();
        
        // 重置表单
        this.resetPermissionForm();
      }).catch(error => {
        console.error('添加用户权限失败:', error);
        this.$message.error('添加权限失败');
      });
    },

    /** 修改用户权限 */
    handleEditUserPermission(permission) {
      const action = permission.permissionAction === 1 ? '禁止' : '允许';
      const newAction = permission.permissionAction === 1 ? 0 : 1;
      setUserPermission(this.currentAgent.id, permission.targetId, newAction).then(response => {
        permission.permissionAction = newAction;
        this.$modal.msgSuccess(`${action}权限成功`);
      }).catch(error => {
        console.error('修改用户权限失败:', error);
        this.$message.error('修改权限失败');
      });
    },

    /** 删除用户权限 */
    handleDeleteUserPermission(permission) {
      // 检查是否是临时ID（前端生成的时间戳）
      const isTemporaryId = permission.id > 1000000000000;
      
      if (isTemporaryId) {
        this.$message.warning('请刷新页面后再删除，或者重新添加权限');
        return;
      }
      
      // 直接删除，不需要确认对话框
      if (permission.id && permission.id > 0) {
        deleteAiagentPermission(permission.id).then(response => {
          this.loadUserPermissions();
          this.$modal.msgSuccess('删除用户权限成功');
        }).catch(error => {
          console.error('删除权限失败:', error);
          
          // 如果是"操作失败"，可能是权限记录不存在，重新加载列表
          if (error.message === '操作失败' || error.response?.data?.msg === '操作失败') {
            this.loadUserPermissions();
            this.$modal.msgSuccess('权限记录已清理');
            return;
          }
          
          // 其他错误处理
          let errorMsg = '删除权限失败';
          if (error.response?.data?.msg) {
            errorMsg = error.response.data.msg;
          } else if (error.message) {
            errorMsg = error.message;
          }
          this.$message.error(errorMsg);
        });
      } else {
        this.$message.warning('无法删除临时权限记录，请刷新页面后重试');
      }
    },
    /** 刷新缓存 */
    handleRefreshCache() {
      this.$modal.confirm('确认要刷新所有AI相关缓存吗？此操作将清除所有AI智能体、权限、用户可用列表等缓存数据。').then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在清除缓存...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        
        return refreshCache().then(() => {
          loading.close();
          this.$modal.msgSuccess("刷新缓存成功！所有AI相关缓存已清除");
          
          // 刷新当前页面数据
          this.getList();
          
          // 如果权限对话框打开，也刷新权限数据
          if (this.permissionOpen) {
            if (this.activeTab === 'role') {
              this.loadRolePermissions();
            } else {
              this.loadUserPermissions();
            }
          }
        }).catch(error => {
          loading.close();
          console.error('刷新缓存失败:', error);
          this.$modal.msgError("刷新缓存失败: " + (error.msg || error.message || '未知错误'));
        });
      }).catch(() => {
        // 用户取消操作
      });
    },

    // ==================== 选项配置相关方法 ====================

    /** 获取选项类型标签 */
    getOptionTypeLabel(type) {
      const typeMap = {
        'button': '按钮',
        'select': '单选框',
        'checkbox': '多选框',
        'input': '输入框',
        'switch': '开关'
      };
      return typeMap[type] || type;
    },

    /** 编辑选项 */
    editOption(index) {
      this.editingOptionIndex = index;
      this.currentOption = JSON.parse(JSON.stringify(this.configOptions[index]));
      this.showOptionDialog = true;
    },

    /** 删除选项 */
    deleteOption(index) {
      this.$modal.confirm(`确认要删除选项 "${this.configOptions[index].label}" 吗？`).then(() => {
        this.configOptions.splice(index, 1);
        this.updateFormConfigJson();
        this.$modal.msgSuccess('删除选项成功');
      }).catch(() => {});
    },

    /** 选项类型变更 */
    onOptionTypeChange(type) {
      // 重置类型相关的配置
      if (type === 'button') {
        this.currentOption.value = '';
        delete this.currentOption.values;
        delete this.currentOption.selectType;
        delete this.currentOption.inputType;
        delete this.currentOption.placeholder;
      } else if (type === 'input') {
        this.currentOption.inputType = 'text';
        this.currentOption.placeholder = '';
        delete this.currentOption.value;
        delete this.currentOption.values;
        delete this.currentOption.selectType;
      } else if (['select', 'checkbox'].includes(type)) {
        this.currentOption.selectType = 'single';
        this.currentOption.values = [];
        delete this.currentOption.value;
        delete this.currentOption.inputType;
        delete this.currentOption.placeholder;
      } else if (type === 'switch') {
        delete this.currentOption.value;
        delete this.currentOption.values;
        delete this.currentOption.selectType;
        delete this.currentOption.inputType;
        delete this.currentOption.placeholder;
      }
    },

    /** 添加选项值 */
    addOptionValue() {
      if (!this.currentOption.values) {
        this.currentOption.values = [];
      }
      this.currentOption.values.push({
        label: '',
        value: '',
        default: false
      });
    },

    /** 删除选项值 */
    removeOptionValue(index) {
      this.currentOption.values.splice(index, 1);
    },

    /** 默认值变更 */
    onDefaultChange(index) {
      if (this.currentOption.selectType === 'single') {
        // 单选时，只能有一个默认值
        this.currentOption.values.forEach((value, i) => {
          if (i !== index) {
            value.default = false;
          }
        });
      }
    },

    /** 保存选项 */
    saveOption() {
      this.$refs.optionForm.validate(valid => {
        if (valid && !this.hasConflicts) {
          const option = JSON.parse(JSON.stringify(this.currentOption));

          if (this.editingOptionIndex >= 0) {
            // 编辑模式
            this.configOptions.splice(this.editingOptionIndex, 1, option);
            this.$modal.msgSuccess('修改选项成功');
          } else {
            // 新增模式
            this.configOptions.push(option);
            this.$modal.msgSuccess('添加选项成功');
          }

          this.updateFormConfigJson();
          this.showOptionDialog = false;
        }
      });
    },

    /** 重置选项表单 */
    resetOptionForm() {
      this.currentOption = {
        type: '',
        label: '',
        id: '',
        value: '',
        values: [],
        selectType: 'single',
        inputType: 'text',
        placeholder: ''
      };
      this.editingOptionIndex = -1;
      if (this.$refs.optionForm) {
        this.$refs.optionForm.resetFields();
      }
    },

    /** 更新表单的configJson字段 */
    updateFormConfigJson() {
      if (this.configOptions && this.configOptions.length > 0) {
        const config = {
          options: this.configOptions
        };
        this.form.configJson = JSON.stringify(config, null, 2);
      } else {
        this.form.configJson = '';
      }
    },

    /** 预览配置JSON */
    previewConfig() {
      if (this.configOptions && this.configOptions.length > 0) {
        const config = {
          options: this.configOptions
        };
        this.previewJsonString = JSON.stringify(config, null, 2);
      } else {
        this.previewJsonString = '{\n  "options": []\n}';
      }
      this.showJsonPreview = true;
    },

    /** 处理选项点击 */
    handleOptionClick(option) {
      console.log('选项点击:', option);
    },

    /** 处理下拉选择变化 */
    handleSelectChange(option, value) {
      console.log('下拉选择变化:', option.label, '选择了:', value);
      this.selectedValues[option.id] = value;

      // 处理冲突逻辑
      if (option.conflicts && option.conflicts.length > 0) {
        option.conflicts.forEach(conflictId => {
          // 禁用冲突的选项
          this.updateConflictStatus();
        });
      }
    },

    /** 处理按钮点击 */
    handleButtonClick(option) {
      console.log('按钮点击:', option.label);
      // 切换按钮状态
      this.selectedValues[option.id] = !this.selectedValues[option.id];

      // 处理冲突逻辑
      if (option.conflicts && option.conflicts.length > 0) {
        this.updateConflictStatus();
      }
    },

    /** 检查选项是否被禁用 */
    isOptionDisabled(option) {
      if (!option.conflicts || option.conflicts.length === 0) return false;

      // 检查是否有冲突的选项被激活
      return option.conflicts.some(conflictId => {
        const conflictOption = this.configOptions.find(opt => opt.id === conflictId);
        if (!conflictOption) return false;

        if (conflictOption.type === 'select') {
          return !!this.selectedValues[conflictId];
        } else if (conflictOption.type === 'button') {
          return !!this.selectedValues[conflictId];
        }
        return false;
      });
    },

    /** 更新冲突状态 */
    updateConflictStatus() {
      // 这里可以添加更复杂的冲突处理逻辑
      this.$forceUpdate();
    },

    /** 添加单选框选项 */
    addSelectOption() {
      this.selectConfig.values.push({
        label: '',
        value: '',
        default: false
      });
    },

    /** 移除单选框选项 */
    removeSelectOption(index) {
      this.selectConfig.values.splice(index, 1);
    },

    /** 处理默认值变化 */
    handleDefaultChange(type, index) {
      if (type === 'select') {
        // 单选框只能有一个默认值
        this.selectConfig.values.forEach((item, i) => {
          if (i !== index) {
            item.default = false;
          }
        });
      }
    },

    /** 处理冲突设置变化 */
    handleConflictChange(enabled) {
      this.conflictEnabled = enabled;
    },

    /** 保存简化配置 */
    saveSimpleConfig() {
      const options = [];

      // 添加单选框配置（如果有选项值）
      if (this.selectConfig.values.length > 0) {
        const selectOption = {
          ...this.selectConfig,
          conflicts: this.conflictEnabled && this.buttonConfig.label ? ['action_button'] : []
        };
        options.push(selectOption);
      }

      // 添加按钮配置（如果有标签）
      if (this.buttonConfig.label) {
        const buttonOption = {
          ...this.buttonConfig,
          conflicts: this.conflictEnabled && this.selectConfig.values.length > 0 ? ['model_select'] : []
        };
        options.push(buttonOption);
      }

      // 生成JSON配置
      const config = { options };
      this.form.configJson = JSON.stringify(config, null, 2);

      // 更新配置选项
      this.configOptions = options;

      // 初始化选中值
      this.initializeSelectedValues();

      this.showConfigDialog = false;
      this.$message.success('配置已保存');
    },

    // ========== 增强配置方法 ==========

    /** 打开增强配置对话框 */
    openEnhancedConfig(row = null) {
      this.isEditMode = !!row;
      this.configDialogTitle = row ? `编辑 ${row.agentName} 的选项配置` : '添加AI选项配置';

      // 初始化配置数据
      if (row && row.configJson) {
        this.loadExistingConfig(row.configJson);
      } else {
        this.resetEnhancedConfig();
      }

      this.showConfigDialog = true;
    },

    /** 加载现有配置 */
    loadExistingConfig(configJson) {
      try {
        const config = JSON.parse(configJson);
        this.enhancedSelectOptions = [];
        this.enhancedButtonOptions = [];

        if (config.options) {
          config.options.forEach(option => {
            if (option.type === 'select') {
              this.enhancedSelectOptions.push({
                ...option,
                conflicts: option.conflicts || []
              });
            } else if (option.type === 'button') {
              this.enhancedButtonOptions.push({
                ...option,
                conflicts: option.conflicts || []
              });
            }
          });
        }
      } catch (e) {
        console.warn('解析配置JSON失败:', e);
        this.resetEnhancedConfig();
      }
    },

    /** 重置增强配置 */
    resetEnhancedConfig() {
      this.enhancedSelectOptions = [];
      this.enhancedButtonOptions = [];
      this.previewValues = {};
    },

    /** 图标加载错误处理 */
    handleIconError(event) {
      console.warn('图标加载失败:', this.form.agentIcon);
      event.target.style.display = 'none';
    },

    /** 添加新的单选框选项 */
    addNewSelectOption() {
      const newOption = {
        id: `select_${Date.now()}`,
        type: 'select',
        label: '',
        selectType: 'single',
        values: [],
        conflicts: []
      };
      this.enhancedSelectOptions.push(newOption);
    },

    /** 添加新的按钮选项 */
    addNewButtonOption() {
      const newOption = {
        id: `button_${Date.now()}`,
        type: 'button',
        label: '',
        value: '',
        conflicts: []
      };
      this.enhancedButtonOptions.push(newOption);
    },

    /** 移除增强单选框选项 */
    removeEnhancedSelectOption(index) {
      this.enhancedSelectOptions.splice(index, 1);
    },

    /** 移除增强按钮选项 */
    removeEnhancedButtonOption(index) {
      this.enhancedButtonOptions.splice(index, 1);
    },

    /** 添加选项值 */
    addSelectValue(selectIndex) {
      this.enhancedSelectOptions[selectIndex].values.push({
        label: '',
        value: '',
        default: false,
        conflicts: []
      });
    },

    /** 添加默认空选项 */
    addDefaultEmptyOption(selectIndex) {
      const option = this.enhancedSelectOptions[selectIndex];
      // 检查是否已有空选项
      const hasEmptyOption = option.values.some(v => v.value === '');
      if (!hasEmptyOption) {
        option.values.unshift({
          label: '不选择',
          value: '',
          default: true,
          conflicts: []
        });
        // 将其他选项的默认状态设为false
        option.values.forEach((item, i) => {
          if (i !== 0) {
            item.default = false;
          }
        });
      }
    },

    /** 移除选项值 */
    removeSelectValue(selectIndex, valueIndex) {
      this.enhancedSelectOptions[selectIndex].values.splice(valueIndex, 1);
    },

    /** 处理增强默认值变化 */
    handleEnhancedDefaultChange(selectIndex, valueIndex) {
      const option = this.enhancedSelectOptions[selectIndex];
      // 单选框只能有一个默认值
      option.values.forEach((item, i) => {
        if (i !== valueIndex) {
          item.default = false;
        }
      });
    },

    /** 获取冲突选项列表 */
    getConflictOptions(currentId) {
      return this.allEnhancedOptions.filter(opt => opt.id !== currentId);
    },

    /** 更新预览（空方法，保持兼容性） */
    updatePreview() {
      // 预览功能已移除，保留方法避免错误
    },

    /** 取消增强配置 */
    cancelEnhancedConfig() {
      this.showConfigDialog = false;
      this.resetEnhancedConfig();
    },

    /** 保存增强配置 */
    saveEnhancedConfig() {
      // 验证配置
      if (!this.validateEnhancedConfig()) {
        return;
      }

      // 生成最终配置
      const config = {
        options: [...this.enhancedSelectOptions, ...this.enhancedButtonOptions]
      };

      // 更新表单配置
      this.form.configJson = JSON.stringify(config, null, 2);
      this.configOptions = config.options;

      // 初始化选中值
      this.initializeSelectedValues();

      // 显示预览效果
      this.showPreviewEffect(config);

      this.showConfigDialog = false;
      this.resetEnhancedConfig();
    },

    /** 显示预览效果 */
    showPreviewEffect(config) {
      if (config.options && config.options.length > 0) {
        // 创建预览消息
        const optionCount = config.options.length;
        const selectCount = config.options.filter(opt => opt.type === 'select').length;
        const buttonCount = config.options.filter(opt => opt.type === 'button').length;

        let message = `配置保存成功！共配置了 ${optionCount} 个选项`;
        if (selectCount > 0) message += `（${selectCount} 个下拉选择）`;
        if (buttonCount > 0) message += `（${buttonCount} 个按钮）`;

        this.$message({
          message: message,
          type: 'success',
          duration: 3000
        });

        // 显示配置预览提示
        this.$notify({
          title: '配置预览',
          message: '配置已应用，保存AI后可在AI精选一站通页面查看实际效果',
          type: 'info',
          duration: 5000
        });
      } else {
        this.$message.success('配置已清空');
      }
    },

    /** 验证增强配置 */
    validateEnhancedConfig() {
      // 验证选择框配置
      for (let i = 0; i < this.enhancedSelectOptions.length; i++) {
        const option = this.enhancedSelectOptions[i];
        if (!option.label || !option.id) {
          this.$message.error(`第${i + 1}个选择框配置不完整`);
          return false;
        }

        // 验证选项值
        if (!option.values || option.values.length === 0) {
          this.$message.error(`第${i + 1}个选择框必须至少有一个选项值`);
          return false;
        }

        for (let j = 0; j < option.values.length; j++) {
          const value = option.values[j];
          if (!value.label) {
            this.$message.error(`第${i + 1}个选择框的第${j + 1}个选项值标签不能为空`);
            return false;
          }
          // 允许value为空值（用于默认空选项）
        }
      }

      // 验证按钮配置
      for (let i = 0; i < this.enhancedButtonOptions.length; i++) {
        const option = this.enhancedButtonOptions[i];
        if (!option.label || !option.id) {
          this.$message.error(`第${i + 1}个按钮配置不完整`);
          return false;
        }
        // 允许按钮value为空
      }

      return true;
    },

    /** 初始化选中值 */
    initializeSelectedValues() {
      this.selectedValues = {};
      this.configOptions.forEach(option => {
        if (option.type === 'select') {
          // 查找默认值，优先选择空值选项
          const defaultValue = option.values.find(v => v.default);
          const emptyValue = option.values.find(v => v.value === '');

          if (defaultValue) {
            this.selectedValues[option.id] = defaultValue.value;
          } else if (emptyValue) {
            this.selectedValues[option.id] = '';
          } else {
            this.selectedValues[option.id] = option.values.length > 0 ? option.values[0].value : '';
          }
        } else if (option.type === 'button') {
          this.selectedValues[option.id] = false;
        }
      });
    },

    /** 解析现有配置 */
    parseExistingConfig() {
      if (this.form.configJson) {
        try {
          const config = JSON.parse(this.form.configJson);
          if (config.options && Array.isArray(config.options)) {
            this.configOptions = config.options;

            // 同步到简化配置
            config.options.forEach(option => {
              if (option.type === 'select') {
                this.selectConfig = { ...option };
              } else if (option.type === 'button') {
                this.buttonConfig = { ...option };
              }
            });

            // 检查是否启用了冲突
            this.conflictEnabled = config.options.some(option =>
              option.conflicts && option.conflicts.length > 0
            );

            // 初始化选中值
            this.initializeSelectedValues();
          }
        } catch (e) {
          console.error('解析配置JSON失败:', e);
          this.configOptions = [];
        }
      } else {
        this.configOptions = [];
      }
    },

    /** 获取已配置的选项类型 */
    getConfiguredTypes() {
      if (!this.configOptions || !Array.isArray(this.configOptions)) return [];
      const types = [...new Set(this.configOptions.map(option => option.type))];
      return types.filter(type => type);
    },

    /** 获取下拉选择选项数量 */
    getSelectOptionsCount() {
      if (!this.configOptions || !Array.isArray(this.configOptions)) return 0;
      return this.configOptions.filter(option => option.type === 'select').length;
    },

    /** 获取按钮选项数量 */
    getButtonOptionsCount() {
      if (!this.configOptions || !Array.isArray(this.configOptions)) return 0;
      return this.configOptions.filter(option => option.type === 'button').length;
    },

    /** 复制JSON到剪贴板 */
    copyJsonToClipboard() {
      navigator.clipboard.writeText(this.previewJsonString).then(() => {
        this.$modal.msgSuccess('JSON已复制到剪贴板');
      }).catch(err => {
        console.error('复制失败:', err);
        this.$modal.msgError('复制失败');
      });
    },

    /** 更新表单的configJson字段 */
    updateFormConfigJson() {
      if (this.configOptions && this.configOptions.length > 0) {
        const config = {
          options: this.configOptions
        };
        this.form.configJson = JSON.stringify(config, null, 2);
      } else {
        this.form.configJson = '';
      }
    },
  }
};
</script>

<style scoped>
/* 选项配置器样式 */
.config-builder {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 16px;
  background-color: #fafafa;
}

.config-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.config-summary {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
}

.summary-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.config-count {
  font-weight: 600;
  color: #303133;
}

.option-types {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.type-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
}

.empty-summary {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
}

.empty-summary i {
  font-size: 16px;
}

.options-list {
  max-height: 300px;
  overflow-y: auto;
}

.option-item {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 8px;
  transition: all 0.3s;
}

.option-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);
}

.option-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.option-type-badge {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
  font-weight: bold;
}

.type-button { background-color: #67c23a; }
.type-select { background-color: #409eff; }
.type-checkbox { background-color: #e6a23c; }
.type-input { background-color: #909399; }
.type-switch { background-color: #f56c6c; }

.option-label {
  font-weight: bold;
  color: #303133;
  flex: 1;
}

.option-actions {
  display: flex;
  gap: 5px;
}

.option-preview {
  font-size: 12px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 10px;
}

.preview-label {
  font-weight: bold;
}

.empty-options {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.empty-options p {
  margin: 10px 0 0 0;
}

/* 选项值配置样式 */
.values-config {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  background-color: #fafafa;
}

.values-header {
  margin-bottom: 12px;
}

.value-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  padding: 8px;
  background: white;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.empty-values {
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

/* 简化配置对话框样式 */
.simple-config-dialog {
  padding: 0;
}

.config-section {
  padding: 16px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-header h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.option-values {
  max-height: 300px;
  overflow-y: auto;
}

.value-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  font-size: 14px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px dashed #e4e7ed;
}

.conflict-rules {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

/* 选项预览样式 */
.option-preview {
  margin-top: 16px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.option-item {
  margin-bottom: 12px;
  padding: 12px;
  background: white;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  cursor: pointer;
  transition: all 0.3s;
}

.option-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);
}

.option-item:last-child {
  margin-bottom: 0;
}

.select-option, .button-option {
  display: flex;
  align-items: center;
  gap: 12px;
}

.option-label {
  font-weight: 600;
  color: #303133;
  min-width: 80px;
}

.disabled-hint {
  color: #909399;
  font-size: 12px;
  font-style: italic;
}

.no-options {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  font-size: 14px;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px dashed #e4e7ed;
}

.no-options i {
  font-size: 24px;
  margin-bottom: 8px;
  display: block;
}

/* 增强配置对话框样式 */
.enhanced-config-dialog {
  .el-dialog__body {
    padding: 0;
  }
}

.config-container {
  height: 50px;
}

.config-container1 {
  max-height: 60vh;
  overflow-y: auto;
  padding: 0;
}


.config-panel {
  width: 100%;
}


.tab-content {
  padding: 20px;
  min-height: 200px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  position: sticky;
  top: 0;
  background: white;
  z-index: 10;
  padding-bottom: 10px;
}

.section-header h4 {
  margin: 0;
  color: #303133;
  font-size: 16px;
}

.options-list {
  max-height: none;
  overflow-y: visible;
}

.option-card {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.option-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.values-section {
  margin-top: 12px;
}

.values-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-weight: 600;
  color: #303133;
}

.values-list {
  max-height: 200px;
  overflow-y: auto;
}

.value-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  padding: 8px;
  background: #f5f7fa;
  border-radius: 4px;
}

.empty-values {
  text-align: center;
  padding: 20px;
  color: #909399;
  background: #f5f7fa;
  border-radius: 4px;
  border: 1px dashed #e4e7ed;
}

.button-config {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.conflicts-list {
  max-height: 300px;
  overflow-y: auto;
}

.conflict-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  padding: 12px;
  background: white;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.conflict-label {
  font-weight: 600;
  color: #303133;
  min-width: 120px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 新增样式 */
.values-actions {
  display: flex;
  gap: 8px;
}

.value-conflict {
  margin-bottom: 12px;
  padding: 8px;
  background: #f8f9fa;
  border-radius: 4px;
}

.value-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.value-label {
  font-weight: 600;
  color: #303133;
}

.conflict-config {
  display: flex;
  align-items: center;
  gap: 8px;
}

.conflict-config span {
  font-size: 13px;
  color: #606266;
  white-space: nowrap;
}

.select-conflicts {
  margin-top: 12px;
}

.button-conflicts {
  margin-top: 12px;
}

.conflict-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.option-name {
  font-weight: 600;
  color: #303133;
}

.section-desc {
  font-size: 12px;
  color: #909399;
}

.simple-config-display {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #f0f9ff;
  border: 1px solid #e1f5fe;
  border-radius: 4px;
  margin: 0;
  max-width: 100%;
  min-height: 40px;
}

.config-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #303133;
}

.config-info .el-tag {
  margin-left: 4px;
  height: 22px;
  line-height: 20px;
  font-size: 11px;
}

.config-info .el-icon {
  font-size: 14px;
}

.config-info i {
  color: #409eff;
}

.no-config-display {
  text-align: left;
  padding: 8px 0;
  min-height: 40px;
  display: flex;
  align-items: center;
}

.config-container {
  width: 100%;
}

/* 宽松显示区域样式 */
.spacious-section {
  margin-top: 8px;
  margin-bottom: 8px;
}

.spacious-section .el-form-item {
  margin-bottom: 20px !important;
}

.spacious-section .el-form-item__label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.spacious-section .el-input__wrapper {
  font-size: 13px;
  padding: 8px 12px;
}

.spacious-section .el-textarea__inner {
  font-size: 13px;
  padding: 8px 12px;
  line-height: 1.5;
}

/* 操作按钮优化 */
.el-table .small-padding .el-button--small {
  padding: 4px 8px;
  margin: 0 3px;
  font-size: 12px;
  height: 28px;
  line-height: 20px;
  flex-shrink: 0;
}

.el-table .small-padding .el-button--text {
  padding: 4px 8px;
}

.small-padding .cell {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 2px;
  flex-wrap: nowrap;
  overflow: visible;
}

/* 表格行高优化已合并到上面 */

/* 确保容器宽度100% */
.app-container {
  width: 100%;
  max-width: 100%;
}

.el-form--inline .el-form-item {
  margin-right: 10px;
}

/* 表格宽度优化 */
.el-table {
  width: 100% !important;
}

.el-table__body-wrapper {
  width: 100% !important;
}

.el-scrollbar__wrap {
  width: 100% !important;
}

/* 表格布局优化 - 放大1.5倍 */
.el-table {
  font-size: 19.5px; /* 13 * 1.5 */
}

.el-table .cell {
  padding: 0 6px; /* 4 * 1.5 */
  line-height: 2.1; /* 1.4 * 1.5 */
  font-size: 19.5px; /* 13 * 1.5 */
  word-break: break-word;
}

.el-table th {
  padding: 18px 0; /* 12 * 1.5 */
  font-size: 19.5px; /* 13 * 1.5 */
  font-weight: 600;
  background-color: #f5f7fa;
}

.el-table td {
  padding: 18px 0; /* 12 * 1.5 */
  font-size: 19.5px; /* 13 * 1.5 */
}

/* 表格图标放大 */
.el-table img {
  width: 60px !important; /* 40 * 1.5 */
  height: 60px !important; /* 40 * 1.5 */
}

/* 表格标签放大 */
.el-table .el-tag {
  font-size: 18px; /* 12 * 1.5 */
  padding: 0 12px; /* 0 8 * 1.5 */
  height: 36px; /* 24 * 1.5 */
  line-height: 33px; /* 22 * 1.5 */
}

/* 表格按钮放大 */
.el-table .el-button--small {
  padding: 6px 12px !important; /* 4 8 * 1.5 */
  font-size: 18px !important; /* 12 * 1.5 */
  height: 42px; /* 28 * 1.5 */
}

/* 表格开关放大 */
.el-table .el-switch {
  height: 36px !important; /* 24 * 1.5 */
  font-size: 18px !important; /* 12 * 1.5 */
}

.el-table .el-switch__core {
  height: 36px !important;
  min-width: 60px !important; /* 40 * 1.5 */
}

/* 标签和开关尺寸优化 */
.el-tag--small {
  height: 24px;
  line-height: 22px;
  font-size: 12px;
  padding: 0 8px;
}

.el-switch--small {
  height: 24px;
}

/* ==================== 美化表单样式 ==================== */

/* 对话框整体样式 */
.ai-agent-dialog .el-dialog__body {
  padding: 15px 25px;
  max-height: 70vh;
  overflow-y: auto;
}

/* 对话框标题美化 */
.ai-agent-dialog .el-dialog__header {
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 100%);
  border-bottom: none;
  padding: 20px 25px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
}

.ai-agent-dialog .el-dialog__title {
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
}

.ai-agent-dialog .el-dialog__headerbtn {
  top: 20px;
  right: 20px;
}

.ai-agent-dialog .el-dialog__headerbtn .el-dialog__close {
  color: #ffffff;
  font-size: 20px;
}

.ai-agent-dialog .el-dialog__headerbtn:hover .el-dialog__close {
  color: #f0f0f0;
}

/* 表单整体样式 */
.beautiful-form {
  padding: 0;
}

/* 卡片式分组样式 */
.form-section {
  background: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  margin-bottom: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.form-section:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.form-section:last-child {
  margin-bottom: 0;
}

/* 分组标题样式 - 蓝色系渐变 */
.section-header {
  background: linear-gradient(135deg, #409eff 0%, #3a8ee6 100%);
  color: #ffffff;
  padding: 10px 16px;
  font-size: 13px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.section-header .el-icon {
  font-size: 16px;
}

/* 分组内容样式 */
.section-content {
  padding: 16px;
  background: #fafbfc;
}

/* 表单项样式优化 */
.beautiful-form .el-form-item {
  margin-bottom: 18px;
}

/* 确保选项配置和备注占满宽度 */
.beautiful-form .el-row .el-col-24 .el-form-item {
  width: 100%;
}

.beautiful-form .el-row .el-col-24 .el-form-item .el-form-item__content {
  width: 100%;
}

.beautiful-form .el-form-item__label {
  font-weight: 500;
  color: #606266;
  font-size: 13px;
}

/* 输入框样式优化 */
.beautiful-form .el-input__wrapper {
  border-radius: 6px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  transition: all 0.3s ease;
}

.beautiful-form .el-input__wrapper:hover {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

.beautiful-form .el-input__wrapper.is-focus {
  box-shadow: 0 0 0 1px #409eff inset;
}

/* 图标输入组样式 */
.icon-input-with-preview {
  display: flex;
  gap: 8px;
  align-items: center;
  width: 100%;
}

.icon-input-with-preview .el-input {
  flex: 1;
  max-width: calc(100% - 48px);
}

.icon-preview-box {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  padding: 4px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fafbfc;
  position: relative;
}

.icon-preview-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 2px;
}

.preview-label {
  display: none;
}

/* 配置卡片样式 */
.config-card {
  background: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  padding: 16px;
  min-height: 100px;
  width: 100%;
}

.config-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.config-stats {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.stat-icon {
  font-size: 18px;
  color: #409eff;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: #dcdfe6;
}

.stat-tags {
  display: flex;
  gap: 6px;
}

.stat-tags .el-tag {
  display: flex;
  align-items: center;
  gap: 4px;
}

.config-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80px;
}

/* Textarea样式优化 */
.beautiful-form .el-textarea__inner {
  border-radius: 6px;
  font-family: inherit;
  line-height: 1.6;
  resize: vertical;
}

/* 数字输入框样式 */
.beautiful-form .el-input-number {
  width: 100%;
}

.beautiful-form .el-input-number .el-input__wrapper {
  padding-left: 40px;
  padding-right: 40px;
}

/* Switch开关样式优化 */
.beautiful-form .el-switch {
  height: 28px;
}

.beautiful-form .el-switch__core {
  height: 28px;
  border-radius: 14px;
}

.beautiful-form .el-switch__action {
  width: 24px;
  height: 24px;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .ai-agent-dialog {
    width: 95% !important;
  }
  
  .section-content {
    padding: 15px;
  }
  
  .icon-input-group {
    flex-direction: column;
  }
  
  .icon-select {
    width: 100%;
  }
}

/* 滚动条美化 */
.ai-agent-dialog .el-dialog__body::-webkit-scrollbar {
  width: 6px;
}

.ai-agent-dialog .el-dialog__body::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.ai-agent-dialog .el-dialog__body::-webkit-scrollbar-thumb:hover {
  background-color: #c0c4cc;
}

.ai-agent-dialog .el-dialog__body::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}

/* 对话框底部按钮样式 */
.ai-agent-dialog .dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 15px 0 0 0;
}

.ai-agent-dialog .dialog-footer .el-button {
  min-width: 80px;
  border-radius: 6px;
}

/* 互斥配置标题样式 - 与按钮选项保持一致 */
.section-header .header-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.section-header .header-content h4 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.section-header .header-content .section-desc {
  font-size: 13px;
  font-weight: 600;
  color: #606266;
  line-height: 1.5;
}
</style>
