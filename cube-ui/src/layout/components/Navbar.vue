<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>

    <div class="right-menu">
      <!--      <template v-if="device!=='mobile'">-->
<!--        <search id="header-search" class="right-menu-item" />-->

<!--        <el-tooltip content="源码地址" effect="dark" placement="bottom">-->
<!--          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

<!--        <el-tooltip content="文档地址" effect="dark" placement="bottom">-->
<!--          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

<!--        <screenfull id="screenfull" class="right-menu-item hover-effect" />-->

<!--        <el-tooltip content="布局大小" effect="dark" placement="bottom">-->

<!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

<!--      </template>-->
      <!-- 搜索框 -->
      <div class="right-menu-item search-item">
        <search id="header-search" />
      </div>

      <!-- 全屏按钮 -->
      <div class="right-menu-item action-item">
        <screenfull id="screenfull" />
      </div>

      <!-- 用户下拉菜单 -->
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <el-avatar :size="38" :src="avatar" class="user-avatar">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <div class="user-info">
            <span class="user-nickname">{{ nickname || name }}</span>
            <span class="user-role" :class="userRoleClass">{{ userRoleDisplay }}</span>
          </div>
          <el-icon class="arrow-icon"><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="user-dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item>
                <el-icon><User /></el-icon>
                <span>个人中心</span>
              </el-dropdown-item>
            </router-link>
            <el-dropdown-item @click="setting = true">
              <el-icon><Setting /></el-icon>
              <span>布局设置</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { CaretBottom, UserFilled, User, Setting, SwitchButton } from '@element-plus/icons-vue';

import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    UserFilled,
    CaretBottom,
    User,
    Setting,
    SwitchButton
  },
  setup() {
    return {
      UserFilled,
      CaretBottom,
      User,
      Setting,
      SwitchButton
    };
  },
  mounted() {
    // 监听头像更新事件
    window.addEventListener('avatarUpdated', this.handleAvatarUpdate);
  },
  beforeUnmount() {
    // 移除头像更新事件监听
    window.removeEventListener('avatarUpdated', this.handleAvatarUpdate);
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'name',
      'nickname',
      'device',
      'roles'
    ]),
    // 根据角色显示对应的中文名称
    userRoleDisplay() {
      if (!this.roles || this.roles.length === 0) {
        return '普通用户'
      }
      
      // 优立方用户（plugin）
      if (this.roles.includes('plugin')) {
        return '优立方用户'
      }
      
      // 超级管理员（admin）或普通角色（common）都显示"管理员"
      if (this.roles.includes('admin') || this.roles.includes('common')) {
        return '管理员'
      }
      
      // 其他所有角色都显示"普通用户"
      return '普通用户'
    },
    // 根据角色返回对应的样式类名（浅色系）
    userRoleClass() {
      if (!this.roles || this.roles.length === 0) {
        return 'role-user'
      }
      
      // 优立方用户使用紫色系
      if (this.roles.includes('plugin')) {
        return 'role-plugin'
      }
      
      // 超级管理员和普通角色使用蓝色系
      if (this.roles.includes('admin') || this.roles.includes('common')) {
        return 'role-admin'
      }
      
      // 其他角色使用灰色系
      return 'role-user'
    },
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/#/login';
        })
      }).catch(() => {});
    },
    handleAvatarUpdate(event) {
      // 当接收到头像更新事件时，强制刷新组件
      console.log('Navbar: 检测到头像更新', event.detail.avatar);
      // Vuex store 已经更新，computed 属性会自动响应
      this.$forceUpdate();
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 60px;
  overflow: hidden;
  position: relative;
  background: linear-gradient(to right, #ffffff, #fafbfc);
  box-shadow: 0 2px 8px rgba(0, 21, 41, 0.08);
  border-bottom: 1px solid rgba(0, 21, 41, 0.05);
  display: flex;
  align-items: center;

  .hamburger-container {
    height: 40px;
    width: 40px;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;
    -webkit-tap-highlight-color: transparent;
    border-radius: 8px;
    margin: 0 8px 0 16px;

    &:hover {
      background: rgba(64, 158, 255, 0.08);
      transform: scale(1.05);
    }
  }

  .breadcrumb-container {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
  }

  .topmenu-container {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    display: flex;
    align-items: center;
    gap: 8px;
    padding-right: 20px;
    flex-shrink: 0;
    margin-left: auto;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 40px;
      font-size: 18px;
      color: #5a5e66;

      &.search-item {
        // 搜索框容器，固定宽度，确保不影响其他按钮
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0; // 防止被压缩
      }

      &.action-item {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        transition: all 0.3s ease;
        cursor: pointer;

        &:hover {
          background: rgba(64, 158, 255, 0.08);
          color: #409EFF;
          transform: translateY(-2px);
        }
      }
    }

    .avatar-container {
      cursor: pointer;
      height: 50px;
      display: flex;
      align-items: center;
      padding: 0 16px;
      border-radius: 12px;
      transition: all 0.3s ease;
      margin-left: 8px;

      &:hover {
        background: rgba(64, 158, 255, 0.06);

        .avatar-wrapper .arrow-icon {
          transform: rotate(180deg);
          color: #409EFF;
        }
      }

      .avatar-wrapper {
        display: flex;
        align-items: center;
        gap: 12px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          border: 2px solid rgba(64, 158, 255, 0.2);
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
          transition: all 0.3s ease;
          flex-shrink: 0;

          &:hover {
            border-color: #409EFF;
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.25);
            transform: scale(1.05);
          }
        }

        .user-info {
          display: flex;
          flex-direction: column;
          gap: 4px;
          min-width: 80px;

          .user-nickname {
            font-size: 14px;
            font-weight: 600;
            color: #303133;
            line-height: 1.2;
            max-width: 120px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .user-role {
            font-size: 12px;
            line-height: 1.2;
            font-weight: 500;
            padding: 3px 10px;
            border-radius: 12px;
            transition: all 0.3s ease;
            display: inline-block;
            align-self: flex-start;
            
            // 管理员样式（蓝色系浅色）- admin 和 common
            &.role-admin {
              color: #409EFF;
              background: linear-gradient(135deg, rgba(64, 158, 255, 0.12), rgba(64, 158, 255, 0.08));
              border: 1px solid rgba(64, 158, 255, 0.2);
            }
            
            // 优立方用户样式（紫色系浅色）- plugin
            &.role-plugin {
              color: #9b59b6;
              background: linear-gradient(135deg, rgba(155, 89, 182, 0.12), rgba(155, 89, 182, 0.08));
              border: 1px solid rgba(155, 89, 182, 0.2);
            }
            
            // 普通用户样式（灰色系浅色）- 其他角色
            &.role-user {
              color: #909399;
              background: linear-gradient(135deg, rgba(144, 147, 153, 0.12), rgba(144, 147, 153, 0.08));
              border: 1px solid rgba(144, 147, 153, 0.2);
            }
          }
        }

        .arrow-icon {
          font-size: 14px;
          color: #909399;
          transition: all 0.3s ease;
          flex-shrink: 0;
        }
      }
    }
  }
}

// 下拉菜单样式美化
:deep(.user-dropdown) {
  margin-top: 12px !important;
  border-radius: 12px !important;
  box-shadow: 0 6px 24px rgba(0, 21, 41, 0.12) !important;
  border: 1px solid rgba(0, 21, 41, 0.08) !important;
  padding: 8px 0 !important;
  min-width: 180px !important;

  .el-dropdown-menu__item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 20px !important;
    transition: all 0.3s ease !important;
    border-radius: 8px !important;
    margin: 2px 8px !important;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 3px;
      height: 0;
      background: linear-gradient(to bottom, #409EFF, #66b1ff);
      border-radius: 0 2px 2px 0;
      transition: height 0.3s ease;
    }

    .el-icon {
      font-size: 16px;
      color: #909399;
      transition: all 0.3s ease;
    }

    span {
      font-size: 14px;
      color: #606266;
      transition: all 0.3s ease;
    }

    &:hover {
      background: linear-gradient(to right, rgba(64, 158, 255, 0.1), rgba(64, 158, 255, 0.05)) !important;
      transform: translateX(4px);
      
      &::before {
        height: 60%;
      }

      .el-icon,
      span {
        color: #409EFF;
      }
    }

    &.is-divided {
      margin-top: 8px !important;
      border-top: 1px solid rgba(0, 21, 41, 0.06) !important;
      padding-top: 14px !important;
    }
  }

  a {
    text-decoration: none;
  }
}

// 平板适配
@media screen and (max-width: 1024px) {
  .navbar {
    .hamburger-container {
      margin: 0 6px 0 12px;
    }
    
    .right-menu {
      gap: 6px;
      padding-right: 16px;
      
      .avatar-container {
        padding: 0 12px;
        
        .avatar-wrapper {
          gap: 10px;
          
          .user-info {
            min-width: 70px;
            
            .user-nickname {
              font-size: 13px;
              max-width: 100px;
            }
            
            .user-role {
              font-size: 11px;
              padding: 2px 8px;
            }
          }
        }
      }
    }
  }
}

// 移动端适配
@media screen and (max-width: 768px) {
  .navbar {
    height: 56px;
    
    .hamburger-container {
      margin: 0 4px 0 8px;
      width: 36px;
      height: 36px;
    }
    
    .right-menu {
      gap: 4px;
      padding-right: 12px;
      
      .right-menu-item {
        &.action-item {
          width: 36px;
          height: 36px;
        }
      }
      
      .avatar-container {
        padding: 0 8px;
        margin-left: 4px;
        
        .avatar-wrapper {
          gap: 8px;
          
          .user-avatar {
            width: 32px !important;
            height: 32px !important;
          }
          
          .user-info {
            display: none;
          }
          
          .arrow-icon {
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
