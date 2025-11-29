<template>
  <div class="app-container">
    <!-- 模板类型选择器 -->
    <div class="template-type-selector" style="margin-bottom: 20px">
      <el-radio-group v-model="templateType" @change="handleTemplateTypeChange" size="default">
        <el-radio-button label="score">评分模板</el-radio-button>
        <el-radio-button label="callword">提示词模板</el-radio-button>
      </el-radio-group>
    </div>
    
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="我创建的" name="created">
        <template-created-list ref="createdListRef" :template-type="templateType" />
      </el-tab-pane>
      <el-tab-pane label="我购买的" name="purchased">
        <template-purchased-list ref="purchasedListRef" :template-type="templateType" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import TemplateCreatedList from './components/CreatedList.vue'
import TemplatePurchasedList from './components/PurchasedList.vue'

export default {
  name: "TemplateBox",
  components: {
    TemplateCreatedList,
    TemplatePurchasedList
  },
  data() {
    return {
      activeTab: 'created',
      templateType: 'score' // 默认选择评分模板
    }
  },
  methods: {
    handleTabChange(tabName) {
      // 切换标签时刷新对应列表
      if (tabName === 'created' && this.$refs.createdListRef) {
        this.$refs.createdListRef.getList()
      } else if (tabName === 'purchased' && this.$refs.purchasedListRef) {
        this.$refs.purchasedListRef.getList()
      }
    },
    handleTemplateTypeChange() {
      // 切换模板类型时刷新当前标签页的列表
      if (this.activeTab === 'created' && this.$refs.createdListRef) {
        this.$refs.createdListRef.getList()
      } else if (this.activeTab === 'purchased' && this.$refs.purchasedListRef) {
        this.$refs.purchasedListRef.getList()
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
</style>

