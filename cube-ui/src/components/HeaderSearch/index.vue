<template>
  <div :class="{'show':show}" class="header-search">
    <svg-icon class-name="search-icon" icon-class="search" @click.stop="click" />
    <el-select
      ref="headerSearchSelect"
      v-model="search"
      :remote-method="querySearch"
      filterable
      default-first-option
      remote
      placeholder="Search"
      class="header-search-select"
      @change="change"
    >
      <el-option v-for="option in options" :key="option.item.path" :value="option.item" :label="option.item.title.join(' > ')" />
    </el-select>
  </div>
</template>

<script>
// fuse is a lightweight fuzzy-search module
// make search results more in line with expectations
import Fuse from 'fuse.js/dist/fuse.min.js'
import path from 'path'

export default {
  name: 'HeaderSearch',
  data() {
    return {
      search: '',
      options: [],
      searchPool: [],
      show: false,
      fuse: undefined
    }
  },
  computed: {
    routes() {
      return this.$store.getters.permission_routes
    }
  },
  watch: {
    routes() {
      this.searchPool = this.generateRoutes(this.routes)
    },
    searchPool(list) {
      this.initFuse(list)
    },
    show(value) {
      if (value) {
        document.body.addEventListener('click', this.close)
      } else {
        document.body.removeEventListener('click', this.close)
      }
    }
  },
  mounted() {
    this.searchPool = this.generateRoutes(this.routes)
  },
  methods: {
    click() {
      this.show = !this.show
      if (this.show) {
        this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.focus()
      }
    },
    close() {
      this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.blur()
      this.options = []
      this.show = false
    },
    change(val) {
      const path = val.path;
      const query = val.query;
      if(this.ishttp(val.path)) {
        // http(s):// 路径新窗口打开
        const pindex = path.indexOf("http");
        window.open(path.substr(pindex, path.length), "_blank");
      } else {
        if (query) {
          this.$router.push({ path: path, query: JSON.parse(query) });
        } else {
          this.$router.push(path)
        }
      }
      this.search = ''
      this.options = []
      this.$nextTick(() => {
        this.show = false
      })
    },
    initFuse(list) {
      this.fuse = new Fuse(list, {
        shouldSort: true,
        threshold: 0.4,
        location: 0,
        distance: 100,
        minMatchCharLength: 1,
        keys: [{
          name: 'title',
          weight: 0.7
        }, {
          name: 'path',
          weight: 0.3
        }]
      })
    },
    // Filter out the routes that can be displayed in the sidebar
    // And generate the internationalized title
    generateRoutes(routes, basePath = '/', prefixTitle = []) {
      let res = []

      for (const router of routes) {
        // skip hidden router
        if (router.hidden) { continue }

        const data = {
          path: !this.ishttp(router.path) ? path.resolve(basePath, router.path) : router.path,
          title: [...prefixTitle]
        }

        if (router.meta && router.meta.title) {
          data.title = [...data.title, router.meta.title]

          if (router.redirect !== 'noRedirect') {
            // only push the routes with title
            // special case: need to exclude parent router without redirect
            res.push(data)
          }
        }

        if (router.query) {
          data.query = router.query
        }

        // recursive child routes
        if (router.children) {
          const tempRoutes = this.generateRoutes(router.children, data.path, data.title)
          if (tempRoutes.length >= 1) {
            res = [...res, ...tempRoutes]
          }
        }
      }
      return res
    },
    querySearch(query) {
      if (query !== '') {
        this.options = this.fuse.search(query)
      } else {
        this.options = []
      }
    },
    ishttp(url) {
      return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
    }
  }
}
</script>

<style lang="scss" scoped>
.header-search {
  font-size: 0 !important;
  display: flex;
  align-items: center;
  position: relative; // 为绝对定位的搜索框提供参照
  width: 40px; // 固定宽度，只显示搜索图标
  height: 40px;
  justify-content: center;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    transition: all 0.3s ease;
    position: relative;
    z-index: 2; // 确保图标在搜索框上方
    
    &:hover {
      color: #409EFF;
      transform: scale(1.1);
    }
  }

  .header-search-select {
    font-size: 14px;
    transition: all 0.3s ease;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 20px;
    position: absolute; // 绝对定位，不占用空间
    right: 45px; // 从图标右侧45px开始（40px图标 + 5px间距）
    top: 50%;
    transform: translateY(-50%);
    opacity: 0;
    pointer-events: none; // 隐藏时不响应鼠标事件

    :deep(.el-input__wrapper) {
      border-radius: 20px;
      border: 0;
      padding: 5px 15px;
      box-shadow: none !important;
      background: rgba(64, 158, 255, 0.08);
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(64, 158, 255, 0.12);
      }
    }
    
    :deep(.el-input__inner) {
      border-radius: 20px;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      color: #303133;
      font-size: 14px;
      
      &::placeholder {
        color: #909399;
      }
    }
  }

  &.show {
    .header-search-select {
      width: 240px; // 展开宽度
      opacity: 1; // 显示
      pointer-events: auto; // 可以响应鼠标事件
    }
  }
}

// 美化下拉选项样式
:deep(.el-select-dropdown) {
  border-radius: 12px !important;
  box-shadow: 0 6px 24px rgba(0, 21, 41, 0.15) !important;
  border: 1px solid #e8e8e8 !important;
  padding: 8px 0 !important;
  margin-top: 8px !important;
  
  .el-select-dropdown__item {
    padding: 12px 20px !important;
    margin: 2px 8px !important;
    border-radius: 8px !important;
    transition: all 0.3s ease !important;
    font-size: 14px !important;
    color: #606266 !important;
    position: relative !important;
    overflow: hidden !important;
    
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
    
    &:hover {
      background: linear-gradient(to right, rgba(64, 158, 255, 0.1), rgba(64, 158, 255, 0.05)) !important;
      color: #409EFF !important;
      transform: translateX(4px) !important;
      
      &::before {
        height: 60%;
      }
    }
    
    &.is-selected {
      background: linear-gradient(to right, rgba(64, 158, 255, 0.15), rgba(64, 158, 255, 0.08)) !important;
      color: #409EFF !important;
      font-weight: 600 !important;
      
      &::before {
        height: 80%;
      }
    }
  }
  
  .el-select-dropdown__empty {
    padding: 20px 0 !important;
    color: #909399 !important;
    font-size: 14px !important;
  }
}

// 搜索输入框聚焦效果
:deep(.el-select) {
  .el-input.is-focus .el-input__wrapper {
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2) !important;
    background: rgba(64, 158, 255, 0.15) !important;
  }
}
</style>
