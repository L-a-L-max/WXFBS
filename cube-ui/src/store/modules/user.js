import {login, logout, getInfo, weChatlogin,officeLogin, refreshCorpId, updateCorpId} from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    id: '',
    corp_id:'',
    name: '',
    nickname: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_CORP_ID: (state, id) => {
      state.corp_id = id
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_NICKNAME: (state, name) => {
      state.nickname = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    OfficeLogin({ commit }, userInfo) {

      const ticket = userInfo.ticket

      return new Promise((resolve, reject) => {
        officeLogin(ticket).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    WeChatLogin({ commit }, userInfo) {

      const code = userInfo.code
      return new Promise((resolve, reject) => {
        weChatlogin(code).then(res => {
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.user
          // 只接受完整URL的头像，拒绝相对路径
          let avatar = require("@/assets/images/profile.jpg")  // 默认头像
          if (user.avatar) {
            // 验证是否为完整URL
            if (user.avatar.startsWith('http://') || user.avatar.startsWith('https://')) {
              avatar = user.avatar
            } else {
              console.warn('[前端] 头像为相对路径（已拒绝），使用默认头像:', user.avatar)
            }
          }
          
          if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_ID', user.userId)
          commit('SET_CORP_ID', user.corpId)
          commit('SET_NAME', user.userName)
          commit('SET_NICKNAME', user.nickName)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 刷新企业ID
    RefreshCorpId({ commit, state }) {
      return new Promise((resolve, reject) => {
        refreshCorpId().then(res => {
          const user = res.user
          // 只接受完整URL的头像，拒绝相对路径
          let avatar = require("@/assets/images/profile.jpg")  // 默认头像
          if (user.avatar) {
            // 验证是否为完整URL
            if (user.avatar.startsWith('http://') || user.avatar.startsWith('https://')) {
              avatar = user.avatar
            } else {
              console.warn('[前端] 头像为相对路径（已拒绝），使用默认头像:', user.avatar)
            }
          }
          
          // 更新用户信息
          commit('SET_ID', user.userId)
          
          // 只有当corpId有效时才更新，否则设置为空字符串
          if (user.corpId && user.corpId !== 'undefined' && user.corpId !== 'null' && user.corpId.trim() !== '') {
            commit('SET_CORP_ID', user.corpId)
          } else {
            console.warn('[前端] 获取到无效的企业ID，清空本地存储:', user.corpId)
            commit('SET_CORP_ID', '')
          }
          
          commit('SET_NAME', user.userName)
          commit('SET_NICKNAME', user.nickName)
          commit('SET_AVATAR', avatar)
          
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 更新主机ID
    UpdateCorpId({ commit }, corpId) {
      return new Promise((resolve, reject) => {
        updateCorpId(corpId).then(res => {
          if (res.code === 200) {
            // 更新store中的corpId
            commit('SET_CORP_ID', corpId)
            resolve(res)
          } else {
            reject(new Error(res.msg || '更新主机ID失败'))
          }
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
