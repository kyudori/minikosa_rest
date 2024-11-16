// src/stores/auth.js
import { defineStore } from 'pinia'
import axios from 'axios'
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    user: null,
    errorMessage: ''
  }),
  actions: {
    async login(email, password) {
      try {
        const response = await axios.post('/login', { email, password })
        
        // Access Token 저장
        this.accessToken = response.data.accessToken
        
        // 사용자 정보 저장
        this.user = {
          memberId: response.data.memberId,
          name: response.data.name,
          email: response.data.email,
          nickname: response.data.nickname,
          roleId: response.data.roleId
        }
        
        // 로그인 성공 시 리디렉션
        router.push('/intro') // 원하는 페이지로 변경 가능
      } catch (error) {
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data
        } else {
          this.errorMessage = '로그인 중 오류가 발생했습니다.'
        }
        throw new Error(this.errorMessage)
      }
    },
    async logout() {
      try {
        await axios.post('/logout')
        this.accessToken = null
        this.user = null
        router.push('/login')
      } catch (error) {
        console.error('로그아웃 중 오류가 발생했습니다.', error)
      }
    },
    async refreshAccessToken() {
      try {
        const response = await axios.post('/refresh-token')
        this.accessToken = response.data.accessToken
      } catch (error) {
        console.error('Access Token 갱신 실패:', error)
        await this.logout()
        throw error
      }
    }
  }
})
