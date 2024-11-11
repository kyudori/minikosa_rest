// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import './assets/css/headerFooter.css'

import axios from 'axios'
axios.defaults.baseURL = process.env.VUE_APP_BACKEND_URL;

const app = createApp(App)
const pinia = createPinia()

app.config.globalProperties.$axios = axios

app.use(pinia)
app.use(router).mount('#app')
