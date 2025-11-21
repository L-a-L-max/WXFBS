import Vue from 'vue'
import Vuex from 'vuex'
import user from '@/store/modules/user'
import aiagent from '@/store/modules/aiagent'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    aiagent
  },
  getters
})

export default store
