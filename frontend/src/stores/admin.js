// src/stores/admin.js
import { defineStore } from 'pinia'
import api from '../axios'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    suggestions: [],
    currentSuggestion: null,
    totalPages: 1,
    currentPage: 1,
    pageSize: 5,
    searchType: 'all', // 'all', 'title', 'content'
    searchKeyword: '',
    errorMessage: '',
    successMessage: '',
  }),
  actions: {
    // 제안 목록 가져오기
    async fetchSuggestions(page = 1, size = 5, type = 'all', keyword = '') {
      try {
        const response = await api.get('/admin/suggestion/list', {
          params: {
            page: page - 1, // 서버는 0부터 페이지를 시작한다고 가정
            size,
            type,
            keyword
          }
        })
        this.suggestions = response.data.content
        this.totalPages = response.data.totalPages
        this.currentPage = page
        this.errorMessage = ''
      } catch (error) {
        console.error('제안 목록 가져오기 실패:', error)
        this.errorMessage = '제안 목록을 가져오는 데 실패했습니다.'
      }
    },
    // 특정 제안 가져오기
    async fetchSuggestion(id) {
      try {
        const response = await api.get(`/admin/suggestion/${id}`)
        this.currentSuggestion = response.data
        this.errorMessage = ''
      } catch (error) {
        console.error('제안 가져오기 실패:', error)
        this.errorMessage = '제안을 가져오는 데 실패했습니다.'
      }
    },
    // 제안 삭제하기
    async deleteSuggestion(id) {
      try {
        await api.delete(`/admin/suggestion/${id}`)
        this.successMessage = '제안이 성공적으로 삭제되었습니다.'
        this.errorMessage = ''
        // 삭제 후 목록을 다시 가져옵니다.
        this.fetchSuggestions(this.currentPage, this.pageSize, this.searchType, this.searchKeyword)
      } catch (error) {
        console.error('제안 삭제 실패:', error)
        this.errorMessage = '제안을 삭제하는 데 실패했습니다.'
      }
    },
    // 검색 기능
    setSearch(type, keyword) {
      this.searchType = type
      this.searchKeyword = keyword
      this.fetchSuggestions(1, this.pageSize, this.searchType, this.searchKeyword)
    }
  }
})
