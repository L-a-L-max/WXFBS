import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 7777,
    allowedHosts: true,
    proxy: {
      '/api': {
        // target: 'https://mcs.u3w.com',
        target: 'localhost:8080',
        changeOrigin: true
      }
    }
  }
})
