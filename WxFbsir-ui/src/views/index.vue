<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>微信福帮手后台管理框架</h2>
        <p>
          微信福帮手(WxFbsir)是一款现代化的企业级后台管理系统，基于成熟的技术栈构建。系统采用前后端分离架构，代码结构清晰，易于维护和扩展。同时支持移动端访问，提供丰富的功能模块。
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-tag type="danger">&yen;免费开源</el-tag>
        </p>
        <p>
          <el-button
            type="primary"
            icon="Cloudy"
            plain
            @click="goTarget('https://github.com/wxwxfbsir/wxwxfbsir-vue')"
            >访问GitHub</el-button
          >
          <el-button
            icon="HomeFilled"
            plain
            @click="goTarget('https://wx.wxfbsir.com')"
            >访问主页</el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>联系信息</span>
            </div>
          </template>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="https://wx.wxfbsir.com"
                target="_blank"
                >https://wx.wxfbsir.com</el-link
              >
            </p>
            <p>
              <i class="el-icon-message"></i> 邮箱：<el-link
                href="mailto:support@wx.wxfbsir.com"
                >support@wx.wxfbsir.com</el-link
              >
            </p>
            <p>
              <i class="el-icon-phone"></i> 技术支持：请通过邮箱联系我们
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>更新日志</span>
            </div>
          </template>
          <el-collapse accordion>
            <el-collapse-item title="v1.0.0 - 2025-12-04">
              <ol>
                <li>完成从若依框架到微信福帮手企业级系统的全面改造</li>
                <li>更新所有品牌标识为微信福帮手</li>
                <li>重新设计角色权限体系（超级管理员、管理员、只读权限）</li>
                <li>优化用户界面，移除冗余功能按钮</li>
                <li>更新企业联系信息为专业邮箱支持</li>
                <li>清理所有第三方联系方式（QQ群、微信等）</li>
                <li>更新项目描述为现代化企业级管理系统</li>
                <li>修复所有代码引用关系，确保系统稳定性</li>
                <li>更新定时任务示例为微信福帮手相关</li>
                <li>优化代码生成器配置</li>
                <li>更新所有外部链接指向wx.wxfbsir.com</li>
                <li>统一版权信息为2025 微信福帮手</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from "vue"

const { proxy } = getCurrentInstance()
const version = ref("1.0.0")

function goTarget(url) {
  window.open(url, "_blank")
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0px;
      margin-inline-end: 0px;
      padding-inline-start: 40px;
    }
  }
}
</style>
