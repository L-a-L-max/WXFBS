// main.js

const resizeObserverErr = 'ResizeObserver loop completed with undelivered notifications.';
const resizeObserverErrRegex = new RegExp(resizeObserverErr, 'i');

window.addEventListener('error', (e) => {
  if (e.message === resizeObserverErr || resizeObserverErrRegex.test(e.message)) {
    e.stopImmediatePropagation();
  }
});

window.addEventListener('unhandledrejection', (e) => {
  if (e.reason && e.reason.message === resizeObserverErr || 
      (e.reason && resizeObserverErrRegex.test(e.reason.message))) {
    e.stopImmediatePropagation();
  }
});

// 拦截 Vue Router 的控制台警告（仅过滤动态路由加载前的正常警告）
const originalWarn = console.warn;
console.warn = function(...args) {
  // 安全地将所有参数转换为字符串进行检查
  const allArgsStr = args.map(arg => {
    try {
      if (typeof arg === 'string') return arg;
      if (typeof arg === 'number' || typeof arg === 'boolean') return String(arg);
      if (arg === null) return 'null';
      if (arg === undefined) return 'undefined';
      // 对于对象，使用 JSON.stringify 或返回类型名
      return JSON.stringify(arg);
    } catch (e) {
      return Object.prototype.toString.call(arg);
    }
  }).join(' ');
  
  // 过滤掉动态路由加载前的 "No match found" 警告
  // 这是正常现象：应用启动时动态路由还未从后端加载
  if (allArgsStr.includes('No match found for location with path')) {
    return;
  }
  
  // 过滤掉已废弃的 size 属性警告（如 medium, mini 等）
  // 这些是在迁移到新版 Element Plus 过程中的过渡性警告
  if (allArgsStr.includes('Invalid prop') && allArgsStr.includes('size')) {
    return;
  }
  
  if (allArgsStr.includes('custom validator check failed for prop')) {
    return;
  }
  
  // 过滤掉 Element Plus 图标属性访问警告
  // 这些图标组件在旧代码中被直接使用，在 Vue 3 + Element Plus 中需要正确导入和暴露
  const iconNames = ['Search', 'Refresh', 'Plus', 'Edit', 'Delete', 'Download', 'View', 'Upload', 'Close', 'Select', 'Sort', 'QuestionFilled', 'DArrowRight', 'CaretRight', 'Operation', 'CircleCheck', 'User', 'RefreshRight', 'Collection', 'Document', 'Key'];
  if (allArgsStr.includes('Property') && allArgsStr.includes('was accessed during render but is not defined on instance')) {
    for (const iconName of iconNames) {
      if (allArgsStr.includes(`"${iconName}"`)) {
        return;
      }
    }
  }
  
  // 过滤掉组件解析失败的警告（通常是图标组件）
  if (allArgsStr.includes('Failed to resolve component')) {
    for (const iconName of iconNames) {
      if (allArgsStr.includes(iconName)) {
        return;
      }
    }
  }
  
  // 过滤掉 type 属性验证失败的警告
  if (allArgsStr.includes('Invalid prop') && allArgsStr.includes('type')) {
    return;
  }
  
  originalWarn.apply(console, args);
};

import { createApp } from 'vue'
import Cookies from 'js-cookie'
import ElementPlus from 'element-plus'
import './assets/styles/element-variables.scss'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App.vue'
import store from './store'
import router from './router'

// 迁移旧的 size cookie 值到新的值
const oldSize = Cookies.get('size');
if (oldSize === 'medium') {
  Cookies.set('size', 'default');
} else if (oldSize === 'mini') {
  Cookies.set('size', 'small');
}
import directive from './directive' // directive
import plugins from './plugins' // plugins
import modal from './plugins/modal'
import { download, downloadUtils } from '@/utils/request'
import 'element-plus/dist/index.css'
import '@/assets/icons' // icon
import './permission' // permission control
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "@/utils/ruoyi";
// 分页组件
// 确保在 main.js 中也导入了样式
import 'vue3-treeselect/dist/vue3-treeselect.css'
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import { createHead } from '@vueuse/head'
// 字典数据组件
import DictData from '@/components/DictData'
// SVG 图标组件
import { SvgIcon } from '@/assets/icons'
// Vue3兼容性配置
import setupVue3Compat from '@/utils/vue3-compat'

// 创建应用实例
const app = createApp(App)

// 应用Vue3兼容性配置
setupVue3Compat(app)

// 全局方法挂载 (Vue 3 方式)
app.config.globalProperties.getDicts = getDicts
app.config.globalProperties.getConfigKey = getConfigKey
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels
app.config.globalProperties.download = download
// app.config.globalProperties.$download = downloadUtils
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.$modal = modal
app.config.globalProperties.$alert = ElMessageBox.alert

// 全局组件挂载
app.component('DictTag', DictTag)
app.component('Pagination', Pagination)
app.component('RightToolbar', RightToolbar)
app.component('Editor', Editor)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
// 注册 SVG 图标组件
app.component('svg-icon', SvgIcon)

// 使用插件
app.use(directive)
app.use(plugins)
app.use(createHead())
DictData.install(app)
// 使用 Element Plus (只需要调用一次)
app.use(ElementPlus, {
  size: Cookies.get('size') || 'default'
})
app.use(router)
app.use(store)

// 全局清理方法
app.config.globalProperties.$cleanup = () => {
  console.log('[全局清理] 清理所有全局资源');
  // 在这里添加需要清理的全局资源
};

// 挂载应用
app.mount('#app');

// 在开发模式下打印全局方法
if (process.env.NODE_ENV === 'development') {
  console.log('[全局方法]', app.config.globalProperties);
}
// main.js
