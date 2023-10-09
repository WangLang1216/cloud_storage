import { createApp } from 'vue'
// import './style.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'bootstrap-icons/font/bootstrap-icons.css'

import store from './store/store'

import router from './router/index'

// import axios from './request/index'
// import path from 'path-browserify'

import App from './App.vue'
const app = createApp(App)
app.use(ElementPlus).use(router).use(store)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
// 全局挂载
// app.config.globalProperties.axios = axios

app.mount("#app")
