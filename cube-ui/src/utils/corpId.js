import store from '@/store'

// 企业ID最后刷新时间缓存
let lastRefreshTime = 0
// 刷新间隔（毫秒）- 5分钟
const REFRESH_INTERVAL = 5 * 60 * 1000

/**
 * 更新本地存储的企业ID
 * @param {string} corpId 企业ID
 */
function updateLocalCorpId(corpId) {
  // 检查企业ID是否有效（非空且非undefined）
  if (corpId && corpId !== 'undefined' && corpId !== 'null' && corpId.trim() !== '') {
    // 更新localStorage
    localStorage.setItem('corp_id', corpId)
    // 更新sessionStorage
    sessionStorage.setItem('corp_id', corpId)
    // 更新Vuex store
    store.commit('SET_CORP_ID', corpId)
    console.log('本地企业ID已更新:', corpId)
  } else {
    console.warn('尝试更新无效的企业ID:', corpId)
  }
}

/**
 * 获取本地存储的企业ID
 * @returns {string} 企业ID
 */
function getLocalCorpId() {
  const localStorageCorpId = localStorage.getItem('corp_id');
  const sessionStorageCorpId = sessionStorage.getItem('corp_id');
  const storeCorpId = store.state.user.corp_id;
  
  // 检查缓存中的主机ID是否有效（非空且非undefined）
  const isValidCorpId = (corpId) => {
    return corpId && corpId !== 'undefined' && corpId !== 'null' && corpId.trim() !== '';
  };
  
  // 按优先级返回有效的主机ID，如果都无效则返回空字符串
  if (isValidCorpId(localStorageCorpId)) {
    return localStorageCorpId;
  } else if (isValidCorpId(sessionStorageCorpId)) {
    return sessionStorageCorpId;
  } else if (isValidCorpId(storeCorpId)) {
    return storeCorpId;
  } else {
    return '';
  }
}

/**
 * 确保企业ID是最新的
 * 如果距离上次刷新超过指定时间，则自动刷新企业ID
 * @param {boolean} forceRefresh 是否强制刷新
 * @returns {Promise} 返回刷新结果
 */
export function ensureLatestCorpId(forceRefresh = false) {
  const now = Date.now()
  
  // 如果强制刷新或者距离上次刷新时间超过间隔
  if (forceRefresh || (now - lastRefreshTime > REFRESH_INTERVAL)) {
    return store.dispatch('RefreshCorpId').then(res => {
      lastRefreshTime = now
      // 修复：正确获取corpId，从res.user中获取
      const newCorpId = res.user ? res.user.corpId : ''
      const currentCorpId = getLocalCorpId()
      
      console.log('企业ID已刷新:', newCorpId)
      
      // 只有当新获取的企业ID有效时才更新本地存储
      if (newCorpId && newCorpId.trim() !== '') {
        // 更新本地存储的企业ID
        updateLocalCorpId(newCorpId)
        
        // 自动更新全局主机ID值
        if (newCorpId !== currentCorpId) {
          console.log('检测到企业ID变更，自动更新全局主机ID:', newCorpId)
          // 触发全局状态更新事件
          if (window.dispatchEvent) {
            window.dispatchEvent(new CustomEvent('corpIdUpdated', { 
              detail: { corpId: newCorpId, oldCorpId: currentCorpId } 
            }))
          }
        }
      } else {
        // 如果新获取的企业ID为空，清除本地存储的企业ID
        console.log('获取到的企业ID为空，清除本地存储的企业ID')
        localStorage.removeItem('corp_id')
        sessionStorage.removeItem('corp_id')
        store.commit('SET_CORP_ID', '')
        
        // 触发全局状态更新事件，通知企业ID已清除
        if (window.dispatchEvent) {
          window.dispatchEvent(new CustomEvent('corpIdUpdated', { 
            detail: { corpId: '', oldCorpId: currentCorpId } 
          }))
        }
      }
      
      return res
    }).catch(error => {
      console.warn('企业ID刷新失败，使用缓存的企业ID:', error)
      // 即使刷新失败也返回当前的企业ID
      const cachedCorpId = getLocalCorpId()
      return Promise.resolve({ corpId: cachedCorpId })
    })
  }
  
  // 返回当前缓存的企业ID
  const cachedCorpId = getLocalCorpId()
  return Promise.resolve({ corpId: cachedCorpId })
}

/**
 * 获取当前企业ID（带自动刷新）
 * @param {boolean} autoRefresh 是否自动刷新
 * @returns {Promise<string>} 企业ID
 */
export function getCorpId(autoRefresh = true) {
  if (autoRefresh) {
    return ensureLatestCorpId().then(res => res.corpId)
  }
  return Promise.resolve(getLocalCorpId())
}

/**
 * 强制从服务器获取最新的企业ID，不使用缓存
 * @returns {Promise<string>} 企业ID
 */
export function forceGetLatestCorpId() {
  return store.dispatch('RefreshCorpId').then(res => {
    // 修复：正确获取corpId，从res.user中获取
    const newCorpId = res.user ? res.user.corpId : ''
    const currentCorpId = getLocalCorpId()
    
    console.log('强制获取企业ID:', newCorpId)
    
    // 无论新获取的企业ID是否为空，都更新本地存储
    if (newCorpId && newCorpId.trim() !== '') {
      // 更新本地存储的企业ID
      updateLocalCorpId(newCorpId)
      
      // 自动更新全局主机ID值
      if (newCorpId !== currentCorpId) {
        console.log('检测到企业ID变更，自动更新全局主机ID:', newCorpId)
        // 触发全局状态更新事件
        if (window.dispatchEvent) {
          window.dispatchEvent(new CustomEvent('corpIdUpdated', { 
            detail: { corpId: newCorpId, oldCorpId: currentCorpId } 
          }))
        }
      }
    } else {
      // 如果新获取的企业ID为空，清除本地存储的企业ID
      console.log('获取到的企业ID为空，清除本地存储的企业ID')
      localStorage.removeItem('corp_id')
      sessionStorage.removeItem('corp_id')
      store.commit('SET_CORP_ID', '')
      
      // 触发全局状态更新事件，通知企业ID已清除
      if (window.dispatchEvent) {
        window.dispatchEvent(new CustomEvent('corpIdUpdated', { 
          detail: { corpId: '', oldCorpId: currentCorpId } 
        }))
      }
    }
    
    return newCorpId
  }).catch(error => {
    console.error('强制获取企业ID失败:', error)
    // 如果获取失败，清除本地存储的企业ID并返回空字符串
    localStorage.removeItem('corp_id')
    sessionStorage.removeItem('corp_id')
    store.commit('SET_CORP_ID', '')
    return ''
  })
}

/**
 * 强制刷新企业ID
 * @returns {Promise} 返回刷新结果
 */
export function forceRefreshCorpId() {
  return ensureLatestCorpId(true)
}

/**
 * 手动设置企业ID（用于用户手动修改）
 * @param {string} corpId 企业ID
 */
export function setCorpId(corpId) {
  updateLocalCorpId(corpId)
  
  // 触发全局状态更新事件
  if (window.dispatchEvent) {
    window.dispatchEvent(new CustomEvent('corpIdUpdated', { 
      detail: { corpId: corpId, source: 'manual' } 
    }))
  }
}