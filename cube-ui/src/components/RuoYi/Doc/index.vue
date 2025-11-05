<template>
  <div>
    <svg-icon icon-class="documentation" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'RuoYiDoc',
  data() {
    return {
      url: '/doc'  // 可以根据实际情况修改为项目文档地址
    }
  },
  methods: {
    goto() {
      // 如果是相对路径，使用路由跳转，否则使用 window.open
      if (this.url.startsWith('http')) {
        window.open(this.url)
      } else {
        this.$router.push(this.url)
      }
    }
  }
}
</script>