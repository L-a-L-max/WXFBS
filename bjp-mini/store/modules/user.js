import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { isHttp, isEmpty } from "@/utils/validate"
import { login, logout, getInfo, wechatMiniAppLogin } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defAva from '@/static/images/profile.jpg'

const baseUrl = config.baseUrl

const user = {
  state: {
    token: getToken(),
    id: storage.get(constant.id),
    name: storage.get(constant.name),
    nickName: storage.get(constant.nickName),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions)
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
      storage.set(constant.id, id)
    },
    SET_NAME: (state, name) => {
      state.name = name
      storage.set(constant.name, name)
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName
      storage.set(constant.nickName, nickName)
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
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

    // 微信登录
    WeChatLogin({ commit, dispatch }, loginInfo) {
      const code = loginInfo.code
      const nickName = loginInfo.nickName
      const avatar = loginInfo.avatar
      console.log('=== [Store] 开始微信登录 ===')
      console.log('code:', code)
      console.log('nickName:', nickName)
      console.log('avatar:', avatar)
      
      return new Promise((resolve, reject) => {
        wechatMiniAppLogin(code, nickName, avatar).then(res => {
          console.log('=== [Store] 微信登录成功 ===')
          console.log('token:', res.token)
          
          // 保存token
          setToken(res.token)
          commit('SET_TOKEN', res.token)
          
          // 获取用户详细信息
          console.log('=== [Store] 开始获取用户信息 ===')
          dispatch('GetInfo').then(() => {
            console.log('=== [Store] 获取用户信息成功 ===')
            resolve()
          }).catch(error => {
            console.error('=== [Store] 获取用户信息失败 ===', error)
            reject(error)
          })
        }).catch(error => {
          console.error('=== [Store] 微信登录失败 ===', error)
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          const user = res.user
		  let avatar = user.avatar || ""
		  if (!isHttp(avatar)) {
            avatar = (isEmpty(avatar)) ? defAva : baseUrl + avatar
          }
          const userid = (isEmpty(user) || isEmpty(user.userId)) ? "" : user.userId
		  const username = (isEmpty(user) || isEmpty(user.userName)) ? "" : user.userName
		  const nickname = (isEmpty(user) || isEmpty(user.nickName)) ? "" : user.nickName
		  if (res.roles && res.roles.length > 0) {
            commit('SET_ROLES', res.roles)
            commit('SET_PERMISSIONS', res.permissions)
          } else {
            commit('SET_ROLES', ['ROLE_DEFAULT'])
          }
          commit('SET_ID', userid)
          commit('SET_NAME', username)
          commit('SET_NICKNAME', nickname)
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
          // 清除所有用户信息
          commit('SET_TOKEN', '')
          commit('SET_ID', '')
          commit('SET_NAME', '')
          commit('SET_NICKNAME', '')
          commit('SET_AVATAR', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          
          // 清除本地存储
          removeToken()
          storage.clean()
          
          console.log('=== 退出登录成功，已清除所有缓存 ===')
          resolve()
        }).catch(error => {
          // 即使后端退出失败，也清除本地缓存
          commit('SET_TOKEN', '')
          commit('SET_ID', '')
          commit('SET_NAME', '')
          commit('SET_NICKNAME', '')
          commit('SET_AVATAR', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          storage.clean()
          
          console.log('=== 退出登录，已清除本地缓存 ===')
          resolve()
        })
      })
    },
    
    // 清除登录状态（用于401等情况的静默清理）
    ClearLoginStatus({ commit }) {
      return new Promise((resolve) => {
        console.log('=== 开始清除登录状态 ===')
        
        // 清除所有用户信息
        commit('SET_TOKEN', '')
        commit('SET_ID', '')
        commit('SET_NAME', '')
        commit('SET_NICKNAME', '')
        commit('SET_AVATAR', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        
        // 清除本地存储
        removeToken()
        storage.clean()
        
        console.log('=== 登录状态已清除 ===')
        resolve()
      })
    }
  }
}

export default user
