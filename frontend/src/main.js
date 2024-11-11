// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import './assets/css/headerFooter.css'

import axios from 'axios'

const app = createApp(App)

app.config.globalProperties.$axios = axios

const pinia = createPinia()
app.use(pinia)

app.use(router).mount('#app')
