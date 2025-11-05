<template>
  <el-menu
    :default-active="activeMenu"
    mode="horizontal"
    @select="handleSelect"
  >
    <template v-for="(item, index) in topMenus">
      <el-menu-item :style="{'--theme': theme}" :index="item.path" :key="index" v-if="index < visibleNumber">
        <svg-icon
        v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
        :icon-class="item.meta.icon"/>
        {{ item.meta.title }}
      </el-menu-item>
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-sub-menu :style="{'--theme': theme}" index="more" v-if="topMenus.length > visibleNumber">
      <template slot="title">更多菜单</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item
          :index="item.path"
          :key="index"
          v-if="index >= visibleNumber">
          <svg-icon
            v-if="item.meta && item.meta.icon && item.meta.icon !== '#'"
            :icon-class="item.meta.icon"/>
          {{ item.meta.title }}
        </el-menu-item>
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<script>
import { constantRoutes } from "@/router";

// 隐藏侧边栏路由
const hideList = ['/index', '/user/profile'];

export default {
  data() {
    return {
      // 顶部栏初始数
      visibleNumber: 5,
      // 当前激活菜单的 index
      currentIndex: undefined
    };
  },
  computed: {
    theme() {
      return this.$store.state.settings.theme;
    },
    // 顶部显示菜单
    topMenus() {
      let topMenus = [];
      let addedPaths = new Set(); // 用于去重
      
      this.routers.map((menu) => {
        // 跳过隐藏的菜单
        if (menu.hidden === true) {
          return;
        }
        
        // 全面过滤首页相关路径（首页只通过左侧 Logo 访问）
        const isIndexPath = menu.path === '' || 
                           menu.path === '/' || 
                           menu.path === '/index' || 
                           menu.path === 'index' ||
                           (menu.meta && menu.meta.title === '首页') ||
                           (menu.name && menu.name === 'Index') ||
                           (menu.children && menu.children.length === 1 && menu.children[0].path === 'index') ||
                           (menu.children && menu.children.length === 1 && menu.children[0].name === 'Index');
        
        // 如果是首页路径，直接跳过
        if (isIndexPath) {
          console.log('🚫 过滤首页路由:', menu.path, menu.meta?.title, menu.name);
          return;
        }
        
        // 兼容顶部栏一级菜单内部跳转（但排除首页）
        if (menu.path === "/") {
          // 路径为 "/" 的菜单通常是首页容器，已经在上面过滤了
          console.log('🚫 过滤根路径菜单:', menu);
          return;
        }
        
        // 添加非首页菜单（去重）
        if (!addedPaths.has(menu.path)) {
          console.log('✅ 添加菜单:', menu.path, menu.meta?.title);
          topMenus.push(menu);
          addedPaths.add(menu.path);
        }
      });
      
      console.log('📋 最终顶部菜单列表:', topMenus.map(m => ({ path: m.path, title: m.meta?.title })));
      return topMenus;
    },
    // 所有的路由信息
    routers() {
      return this.$store.state.permission.topbarRouters;
    },
    // 设置子路由
    childrenMenus() {
      var childrenMenus = [];
      this.routers.map((router) => {
        for (var item in router.children) {
          if (router.children[item].parentPath === undefined) {
            if(router.path === "/") {
              router.children[item].path = "/" + router.children[item].path;
            } else {
              if(!this.ishttp(router.children[item].path)) {
                router.children[item].path = router.path + "/" + router.children[item].path;
              }
            }
            router.children[item].parentPath = router.path;
          }
          childrenMenus.push(router.children[item]);
        }
      });
      return constantRoutes.concat(childrenMenus);
    },
    // 默认激活的菜单
    activeMenu() {
      const path = this.$route.path;
      let activePath = path;
      
      // 如果是首页，隐藏侧边栏，不激活任何顶部菜单，不调用 activeRoutes
      if (path === '/' || path === '/index' || path === 'index') {
        this.$store.dispatch('app/toggleSideBarHide', true);
        return ''; // 返回空字符串，不激活任何菜单
      }
      
      // 处理多级路径，提取一级路径作为激活路径
      if (path !== undefined && path.lastIndexOf("/") > 0 && hideList.indexOf(path) === -1) {
        const tmpPath = path.substring(1, path.length);
        activePath = "/" + tmpPath.substring(0, tmpPath.indexOf("/"));
        if (!this.$route.meta.link) {
          this.$store.dispatch('app/toggleSideBarHide', false);
        }
      } else if(!this.$route.children) {
        activePath = path;
        this.$store.dispatch('app/toggleSideBarHide', true);
      }
      
      // 只有非首页路径才调用 activeRoutes
      this.activeRoutes(activePath);
      return activePath;
    },
  },
  beforeMount() {
    window.addEventListener('resize', this.setVisibleNumber)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.setVisibleNumber)
  },
  mounted() {
    this.setVisibleNumber();
  },
  methods: {
    // 根据宽度计算设置显示栏数
    setVisibleNumber() {
      const width = document.body.getBoundingClientRect().width / 3;
      this.visibleNumber = parseInt(width / 85);
    },
    // 菜单选择事件
    handleSelect(key, keyPath) {
      // 防止选择首页（首页应该只通过 Logo 访问）
      if (key === '' || key === '/' || key === '/index' || key === 'index') {
        return;
      }
      
      this.currentIndex = key;
      const route = this.routers.find(item => item.path === key);
      
      if (this.ishttp(key)) {
        // http(s):// 路径新窗口打开
        window.open(key, "_blank");
      } else if (!route || !route.children) {
        // 没有子路由路径内部打开
        const routeMenu = this.childrenMenus.find(item => item.path === key);
        if (routeMenu && routeMenu.query) {
          let query = JSON.parse(routeMenu.query);
          this.$router.push({ path: key, query: query });
        } else {
          this.$router.push({ path: key });
        }
        this.$store.dispatch('app/toggleSideBarHide', true);
      } else {
        // 显示左侧联动菜单
        this.activeRoutes(key);
        this.$store.dispatch('app/toggleSideBarHide', false);
      }
    },
    // 当前激活的路由
    activeRoutes(key) {
      // 如果是首页相关路径，不处理侧边栏
      if (key === '' || key === '/' || key === '/index' || key === 'index') {
        this.$store.dispatch('app/toggleSideBarHide', true);
        return;
      }
      
      var routes = [];
      if (this.childrenMenus && this.childrenMenus.length > 0) {
        this.childrenMenus.map((item) => {
          // 移除 key == "index" 的判断，首页不应该在这里处理
          if (key == item.parentPath) {
            routes.push(item);
          }
        });
      }
      
      if(routes.length > 0) {
        this.$store.commit("SET_SIDEBAR_ROUTERS", routes);
      } else {
        this.$store.dispatch('app/toggleSideBarHide', true);
      }
    },
    ishttp(url) {
      return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
    }
  },
};
</script>

<style lang="scss">
.topmenu-container.el-menu--horizontal {
  border-bottom: none !important;
  
  > .el-menu-item {
    float: left;
    height: 60px !important;
    line-height: 60px !important;
    color: #606266 !important;
    padding: 0 16px !important;
    margin: 0 4px !important;
    border-radius: 8px 8px 0 0 !important;
    border-bottom: 3px solid transparent !important;
    transition: all .3s ease !important;
    font-size: 14px !important;
    font-weight: 500 !important;
    position: relative !important;
    
    &:hover {
      background: rgba(64, 158, 255, 0.08) !important;
      color: #409EFF !important;
    }
    
    .svg-icon {
      margin-right: 6px;
      font-size: 16px;
    }
  }

  > .el-menu-item.is-active {
    border-bottom: 3px solid #409EFF !important;
    color: #409EFF !important;
    background: rgba(64, 158, 255, 0.05) !important;
    font-weight: 600 !important;
  }

  /* submenu item */
  > .el-sub-menu {
    .el-sub-menu__title {
      float: left;
      height: 60px !important;
      line-height: 60px !important;
      color: #606266 !important;
      padding: 0 16px !important;
      margin: 0 4px !important;
      border-radius: 8px 8px 0 0 !important;
      transition: all .3s ease !important;
      font-size: 14px !important;
      font-weight: 500 !important;
      
      &:hover {
        background: rgba(64, 158, 255, 0.08) !important;
        color: #409EFF !important;
      }
    }
  }

  > .el-sub-menu.is-active .el-sub-menu__title {
    border-bottom: 3px solid #409EFF !important;
    color: #409EFF !important;
    background: rgba(64, 158, 255, 0.05) !important;
    font-weight: 600 !important;
  }
}

// 下拉菜单美化
.el-menu--horizontal .el-menu--popup {
  border-radius: 8px !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12) !important;
  border: 1px solid #e8e8e8 !important;
  padding: 4px 0 !important;
  margin-top: 4px !important;

  .el-menu-item {
    padding: 0 20px !important;
    margin: 2px 8px !important;
    border-radius: 6px !important;
    transition: all .3s ease !important;

    &:hover {
      background: linear-gradient(to right, rgba(64, 158, 255, 0.1), rgba(64, 158, 255, 0.05)) !important;
      color: #409EFF !important;
    }
  }
}
</style>
