// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

import './assets/css/headerFooter.css'

import axios from 'axios'

// Pinia 스토어 임포트
import { useAuthStore } from './stores/auth'

// Vue 애플리케이션 생성
const app = createApp(App)

// Pinia 인스턴스 생성 및 설치
const pinia = createPinia()
app.use(pinia)

// 라우터 설치
app.use(router)

// Axios 기본 설정
axios.defaults.baseURL = import.meta.env.VITE_BACKEND_URL
axios.defaults.withCredentials = true // 쿠키를 포함시키기 위해 설정

// Axios 요청 인터셉터 설정
axios.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    if (authStore.accessToken) {
      config.headers['Authorization'] = `Bearer ${authStore.accessToken}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Axios 응답 인터셉터 설정 (Access Token 갱신을 위한 설정)
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const authStore = useAuthStore()
    const originalRequest = error.config

    // Access Token 만료 시 갱신 시도
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      try {
        await authStore.refreshAccessToken()
        originalRequest.headers['Authorization'] = `Bearer ${authStore.accessToken}`
        return axios(originalRequest)
      } catch (err) {
        await authStore.logout()
        return Promise.reject(err)
      }
    }
    return Promise.reject(error)
  }
)

// 전역 속성으로 Axios 설정
app.config.globalProperties.$axios = axios

// Vue 애플리케이션 마운트
app.mount('#app')
