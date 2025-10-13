// 应用全局配置
module.exports = {
  //baseUrl: 'https://vue.ruoyi.vip/prod-api',
  // 开发环境：使用本机IP地址（小程序不能访问localhost/127.0.0.1）
  baseUrl: 'http://192.168.0.100:8081',
  // 应用信息
  appInfo: {
    // 应用名称
    name: "ruoyi-app",
    // 应用版本
    version: "1.2.0",
    // 应用logo
    logo: "/static/logo.png",
    // 官方网站
    site_url: "http://ruoyi.vip",
    // 政策协议
    agreements: [{
        title: "隐私政策",
        url: "https://ruoyi.vip/protocol.html"
      },
      {
        title: "用户服务协议",
        url: "https://ruoyi.vip/protocol.html"
      }
    ]
  }
}
