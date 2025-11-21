import { listActiveAiagent, listUserAvailableAiagent } from '@/api/system/aiagent'

// 构建AI列表的辅助函数
function buildAiList(availableList) {
  return availableList
    .filter(ai => {
      // 🔥 过滤掉下架的AI (agentStatus = 0)
      return ai.agentStatus === 1
    })
    .map(ai => {
      // 解析JSON配置
      let configJson = {}
      try {
        configJson = ai.configJson ? JSON.parse(ai.configJson) : {}
      } catch (e) {
        console.warn('解析AI配置JSON失败:', ai.agentName, e)
      }
      
      // 解析capabilities（下拉选择和按钮）
      const capabilities = []
    const models = []  // 用于腾讯元宝模型选择
    
    if (configJson.options) {
      configJson.options.forEach(option => {
        if (option.type === 'select') {
          // 腾讯元宝的模型选择特殊处理
          if (ai.agentName === '腾讯元宝' && option.id === 'model_select') {
            option.values.forEach(v => {
              models.push({
                label: v.label,
                value: v.value,
                default: v.default || false
              })
            })
          } else {
            // 其他AI的下拉选择转为capabilities
            option.values.forEach(v => {
              capabilities.push({
                label: v.label,
                value: v.value,
                default: v.default || false
              })
            })
          }
        } else if (option.type === 'button') {
          // 按钮也作为capability
          capabilities.push({
            label: option.label,
            value: option.value,
            isButton: true
          })
        }
      })
    }
    
    // 构建AI配置对象
    const aiConfig = {
      name: ai.agentName,
      avatar: ai.agentIcon,
      agentCode: ai.agentCode,
      agentStatus: ai.agentStatus,
      onlineStatus: ai.onlineStatus,
      capabilities: capabilities,
      models: models.length > 0 ? models : null,
      selectedModel: models.length > 0 ? (models.find(m => m.default)?.value || models[0]?.value) : null,
      selectedCapabilities: [],
      selectedCapability: capabilities.length > 0 && capabilities[0].default ? capabilities[0].value : '',
      enabled: false,
      status: 'idle',
      progressLogs: [],
      isExpanded: true,
      isSingleSelect: configJson.options?.some(opt => opt.type === 'select' && opt.selectType === 'single') || false,
      configJson: configJson,  // 保存完整配置
      options: configJson.options || []  // 保存options配置
    }
    
    // 初始化默认选中的capabilities
    capabilities.forEach(cap => {
      if (cap.default) {
        if (aiConfig.isSingleSelect) {
          aiConfig.selectedCapabilities = cap.value
        } else {
          aiConfig.selectedCapabilities.push(cap.value)
        }
      }
    })
    
    return aiConfig
  })
}

const state = {
  // AI列表（从数据库加载）
  aiList: [],
  // 原始AI列表（数据库返回的原始数据）
  availableAiList: [],
  // 加载状态
  loading: false,
  // 错误信息
  error: null,
  // 是否为登录用户的列表
  isUserSpecific: false
}

const mutations = {
  SET_AI_LIST(state, aiList) {
    state.aiList = aiList
  },
  SET_AVAILABLE_AI_LIST(state, list) {
    state.availableAiList = list
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_ERROR(state, error) {
    state.error = error
  },
  SET_USER_SPECIFIC(state, isUserSpecific) {
    state.isUserSpecific = isUserSpecific
  },
  // 更新单个AI的在线状态
  UPDATE_AI_ONLINE_STATUS(state, { agentCode, onlineStatus }) {
    const ai = state.aiList.find(item => item.agentCode === agentCode)
    if (ai) {
      ai.onlineStatus = onlineStatus
    }
  },
  // 批量更新AI在线状态
  BATCH_UPDATE_ONLINE_STATUS(state, { agentCodes, onlineStatus }) {
    state.aiList.forEach(ai => {
      if (agentCodes.includes(ai.agentCode)) {
        ai.onlineStatus = onlineStatus
      }
    })
  }
}

const actions = {
  // 加载所有上架的AI列表（未登录用户显示）
  async loadAllActiveAiList({ commit }) {
    commit('SET_LOADING', true)
    commit('SET_ERROR', null)
    commit('SET_USER_SPECIFIC', false)
    
    try {
      const response = await listActiveAiagent()
      const availableList = response.data || []
      commit('SET_AVAILABLE_AI_LIST', availableList)
      
      const aiList = buildAiList(availableList)
      commit('SET_AI_LIST', aiList)
      console.log('✅ [小程序] 加载所有上架AI列表:', aiList.length, '个')
      return aiList
    } catch (error) {
      // 🔥 减少错误日志输出，只在控制台记录简要信息
      console.warn('⚠️ [小程序] 加载AI列表失败，请检查网络或后端服务')
      commit('SET_ERROR', error.message || '加载失败')
      commit('SET_AI_LIST', [])
      // 🔥 不抛出错误，避免在页面上显示错误提示
      return []
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  // 加载用户可用的AI列表（已登录用户显示）
  async loadAvailableAiList({ commit }) {
    commit('SET_LOADING', true)
    commit('SET_ERROR', null)
    commit('SET_USER_SPECIFIC', true)
    
    try {
      const response = await listUserAvailableAiagent()
      const availableList = response.data || []
      commit('SET_AVAILABLE_AI_LIST', availableList)
      
      const aiList = buildAiList(availableList)
      commit('SET_AI_LIST', aiList)
      console.log('✅ [小程序] 加载用户可用AI列表:', aiList.length, '个')
      return aiList
    } catch (error) {
      // 减少错误日志输出，只在控制台记录简要信息
      console.warn('⚠️ [小程序] 加载用户AI列表失败，可能未登录或网络异常')
      commit('SET_ERROR', error.message || '加载失败')
      commit('SET_AI_LIST', [])
      // 不抛出错误，避免在页面上显示错误提示
      return []
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  // 更新AI在线状态
  updateOnlineStatus({ commit }, { agentCode, onlineStatus }) {
    commit('UPDATE_AI_ONLINE_STATUS', { agentCode, onlineStatus })
  },
  
  // 批量更新AI在线状态
  batchUpdateOnlineStatus({ commit }, { agentCodes, onlineStatus }) {
    commit('BATCH_UPDATE_ONLINE_STATUS', { agentCodes, onlineStatus })
  }
}

const getters = {
  // 获取所有AI列表
  aiList: state => state.aiList,
  // 获取原始AI列表
  availableAiList: state => state.availableAiList,
  // 获取加载状态
  loading: state => state.loading,
  // 获取错误信息
  error: state => state.error,
  // 获取启用的AI列表
  enabledAiList: state => state.aiList.filter(ai => ai.enabled),
  // 根据agentCode获取AI
  getAiByCode: state => agentCode => {
    return state.aiList.find(ai => ai.agentCode === agentCode)
  },
  // 获取在线的AI列表
  onlineAiList: state => state.aiList.filter(ai => ai.onlineStatus === 1),
  // 获取上架的AI列表
  activeAiList: state => state.aiList.filter(ai => ai.agentStatus === 1)
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
