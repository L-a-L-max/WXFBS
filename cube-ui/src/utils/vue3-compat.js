/**
 * Vue3 兼容性配置
 * 用于处理从Vue2升级到Vue3后的兼容性问题
 */

/**
 * Element Plus图标CSS类向后兼容
 * Vue2的 el-icon-xxx 在Vue3中仍然可用
 */
export function setupIconCompat() {
  // Element Plus已经内置了CSS图标类的支持
  // 无需额外配置
}

/**
 * 修复v-model在某些组件中的兼容性问题
 */
export function setupVModelCompat() {
  // Vue3中v-model的默认prop从value改为modelValue
  // Element Plus已经处理了这个兼容性
}

/**
 * 修复$listeners和$attrs的变化
 */
export function setupListenersCompat(app) {
  // Vue3中$listeners已被移除，合并到$attrs中
  // 使用mixins确保向后兼容
  app.mixin({
    computed: {
      $listeners() {
        // 在Vue3中，所有事件监听器都在$attrs中
        const listeners = {};
        for (const key in this.$attrs) {
          if (key.startsWith('on')) {
            const eventName = key.slice(2).toLowerCase();
            listeners[eventName] = this.$attrs[key];
          }
        }
        return listeners;
      }
    }
  });
}

/**
 * 全局兼容性设置
 */
export function setupVue3Compat(app) {
  setupIconCompat();
  setupVModelCompat();
  setupListenersCompat(app);
  
  // 添加Vue2的全局API兼容
  if (!app.config.globalProperties.$set) {
    // Vue3不再需要$set，直接赋值即可
    app.config.globalProperties.$set = (target, key, value) => {
      target[key] = value;
      return value;
    };
  }
  
  if (!app.config.globalProperties.$delete) {
    // Vue3不再需要$delete，直接delete即可
    app.config.globalProperties.$delete = (target, key) => {
      delete target[key];
    };
  }
  
  console.log('[Vue3兼容] 兼容性配置已加载');
}

export default setupVue3Compat;

