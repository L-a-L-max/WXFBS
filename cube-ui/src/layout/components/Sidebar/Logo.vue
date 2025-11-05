<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/index">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/index">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title" :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">{{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.jpg'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      // 优化的配色方案
      return {
        menuBackground: '#ffffff',
        menuLightBackground: '#ffffff',
        menuColor: '#2c3e50',
        menuLightColor: '#606266',
        logoTitleColor: '#409EFF',
        logoLightTitleColor: '#303133'
      };
    },
    sideTheme() {
      return this.$store.state.settings.sideTheme
    }
  },
  data() {
    return {
      title: 'U3W优立方AI平台',
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter-from,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start; // 左对齐，与菜单项保持一致
    padding-left: 16px; // 与菜单项的左内边距保持一致
    position: relative;

    & .sidebar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      border-radius: 4px;
      flex-shrink: 0; // 防止图标被压缩
      margin-right: 12px; // 与菜单项的图标右边距保持一致
      transition: transform 0.3s; // 只对缩放效果添加过渡

      &:hover {
        transform: scale(1.05);
      }
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      font-weight: 600;
      line-height: 50px;
      font-size: 15px;
      font-family: 'Microsoft YaHei', 'PingFang SC', Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
      white-space: nowrap; // 防止文字换行
      opacity: 1;
      transition: opacity 0.3s; // 文字淡入淡出效果
    }
  }

  // 折叠状态：调整间距实现图标居中
  &.collapse {
    .sidebar-logo-link {
      // 侧边栏宽度48px，图标32px，居中需要 (48-32)/2 = 8px
      padding-left: 8px;
      justify-content: center; // 居中对齐
    }
    
    .sidebar-logo {
      margin-right: 0; // 折叠时移除右边距
    }
    
    .sidebar-title {
      opacity: 0; // 通过透明度隐藏文字
      pointer-events: none; // 防止隐藏的文字被点击
    }
  }
}
</style>