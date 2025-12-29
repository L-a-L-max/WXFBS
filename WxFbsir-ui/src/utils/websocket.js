/**
 * WebSocket URL 解析工具
 * 从HTTP URL自动解析对应的WebSocket URL
 */

/**
 * 将HTTP URL转换为WebSocket URL
 * @param {string} httpUrl - HTTP URL (如: http://localhost:8080 或 https://api.example.com)
 * @returns {string} WebSocket URL (如: ws://localhost:8080 或 wss://api.example.com)
 */
export function httpToWs(httpUrl) {
  if (!httpUrl) {
    return '';
  }
  
  try {
    // 如果不是完整URL，添加协议
    let fullUrl = httpUrl;
    if (!httpUrl.startsWith('http://') && !httpUrl.startsWith('https://')) {
      fullUrl = 'http://' + httpUrl;
    }
    
    const url = new URL(fullUrl);
    
    // http -> ws, https -> wss
    if (url.protocol === 'https:') {
      url.protocol = 'wss:';
    } else if (url.protocol === 'http:') {
      url.protocol = 'ws:';
    }
    
    return url.toString().replace(/\/$/, ''); // 移除末尾的斜杠
  } catch (e) {
    console.error('解析URL失败:', e);
    // 降级处理：简单替换协议
    if (httpUrl.startsWith('https://')) {
      return httpUrl.replace('https://', 'wss://');
    } else if (httpUrl.startsWith('http://')) {
      return httpUrl.replace('http://', 'ws://');
    }
    return 'ws://' + httpUrl;
  }
}

/**
 * 将WebSocket URL转换为HTTP URL
 * @param {string} wsUrl - WebSocket URL (如: ws://localhost:8080 或 wss://api.example.com)
 * @returns {string} HTTP URL (如: http://localhost:8080 或 https://api.example.com)
 */
export function wsToHttp(wsUrl) {
  if (!wsUrl) {
    return '';
  }
  
  try {
    const url = new URL(wsUrl);
    
    // ws -> http, wss -> https
    if (url.protocol === 'wss:') {
      url.protocol = 'https:';
    } else if (url.protocol === 'ws:') {
      url.protocol = 'http:';
    }
    
    return url.toString().replace(/\/$/, ''); // 移除末尾的斜杠
  } catch (e) {
    console.error('解析URL失败:', e);
    return wsUrl.replace(/^ws:/, 'http:').replace(/^wss:/, 'https:');
  }
}

/**
 * 从配置的baseURL获取WebSocket URL
 * @param {string} path - WebSocket路径 (如: /ws/client)
 * @returns {string} 完整的WebSocket URL
 */
export function getWebSocketUrl(path = '/ws/client') {
  // 获取配置的API地址（可能是完整URL或路径前缀）
  let baseApi = '';
  
  // 尝试从import.meta.env获取（Vite环境）
  if (typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.VITE_APP_BASE_API) {
    baseApi = import.meta.env.VITE_APP_BASE_API;
  }
  // 兼容Vue CLI环境
  else if (typeof process !== 'undefined' && process.env && process.env.VUE_APP_BASE_API) {
    baseApi = process.env.VUE_APP_BASE_API;
  }
  
  // 判断baseApi是完整URL还是路径前缀
  let wsBaseUrl;
  if (baseApi && (baseApi.startsWith('http://') || baseApi.startsWith('https://'))) {
    // 情况1: 完整URL（如 http://localhost:8090 或 https://api.example.com）
    // 直接转换为WebSocket URL
    wsBaseUrl = httpToWs(baseApi);
  } else {
    // 情况2: 路径前缀（如 /dev-api 或 /prod-api）
    // WebSocket不能走Vite代理，需要直接连接后端服务器
    
    // 默认后端地址（开发环境）
    const DEFAULT_BACKEND = 'http://localhost:8080';
    
    // 获取自定义后端地址（可选的环境变量）
    let backendUrl = DEFAULT_BACKEND;
    if (typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.VITE_APP_WS_BASE_URL) {
      backendUrl = import.meta.env.VITE_APP_WS_BASE_URL;
    }
    
    // 如果当前在生产环境（https或非本地域名），使用当前域名
    const isProduction = window.location.protocol === 'https:' || 
                        !['localhost', '127.0.0.1'].includes(window.location.hostname);
    
    if (isProduction) {
      const protocol = window.location.protocol;
      const hostname = window.location.hostname;
      const port = window.location.port;
      
      backendUrl = `${protocol}//${hostname}`;
      if (port && port !== '80' && port !== '443') {
        backendUrl += `:${port}`;
      }
    }
    
    wsBaseUrl = httpToWs(backendUrl);
  }
  
  // 确保path以/开头
  const normalizedPath = path.startsWith('/') ? path : '/' + path;
  
  return wsBaseUrl + normalizedPath;
}

/**
 * 构建完整的WebSocket连接URL（包含查询参数）
 * @param {Object} options - 配置选项
 * @param {string} options.path - WebSocket路径
 * @param {string} options.token - 认证token
 * @param {string} options.clientType - 客户端类型 (默认: web)
 * @returns {string} 完整的WebSocket URL
 */
export function buildWebSocketUrl(options = {}) {
  const {
    path = '/ws/client',
    token = '',
    clientType = 'web'
  } = options;
  
  const baseUrl = getWebSocketUrl(path);
  const params = new URLSearchParams();
  
  if (clientType) {
    params.append('clientType', clientType);
  }
  
  if (token) {
    params.append('token', token);
  }
  
  const queryString = params.toString();
  return queryString ? `${baseUrl}?${queryString}` : baseUrl;
}
