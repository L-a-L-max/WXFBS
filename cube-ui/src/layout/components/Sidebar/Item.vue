<template>
  <span class="menu-item-container">
    <svg-icon v-if="icon" :icon-class="icon" class="menu-icon" />
    <span v-if="title" class="menu-title" :title="title">{{ title }}</span>
  </span>
</template>

<script>
export default {
  name: 'MenuItem',
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-item-container {
  display: flex;
  align-items: center;
  gap: 10px;

  .menu-icon {
    font-size: 18px;
    flex-shrink: 0;
    transition: all 0.3s;
  }

  .menu-title {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 14px;
    transition: all 0.3s;
  }
}

// 鼠标悬停效果
:deep(.el-menu-item):hover .menu-icon,
:deep(.el-sub-menu__title):hover .menu-icon {
  transform: scale(1.1);
}
</style>