// src/stores/admin.js
import { defineStore } from 'pinia'
import api from '../axios'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    // 제안 관련 상태
    suggestions: [],
    currentSuggestion: null,
    totalPages: 1,
    totalSuggestions: 0,
    currentPage: 1,
    pageSize: 5,
    searchType: 'all',
    searchKeyword: '',

    // 사용자 및 가게 관련 상태
    users: [],
    stores: [],
    assignOwnerResponse: null,
    createdStore: null,
    updatedStore: null,

    // 메시지 상태
    errorMessage: '',
    successMessage: '',

    // 로딩 상태
    isLoading: false,

    // 현재 관리 중인 가게 정보
    currentStore: null,
  }),
  actions: {
    // 제안 검색 설정
    setSearch(type, keyword) {
      this.searchType = type
      this.searchKeyword = keyword
      this.currentPage = 1 // 검색 시 페이지를 1로 초기화
      this.fetchSuggestions(this.currentPage, this.pageSize, this.searchType, this.searchKeyword)
    },

    // 제안 목록 가져오기
    async fetchSuggestions(page = 1, size = 5, type = 'title', keyword = '') {
      this.isLoading = true
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
        this.totalSuggestions = response.data.totalElements
        this.currentPage = page
        this.errorMessage = ''
      } catch (error) {
        console.error('제안 목록 가져오기 실패:', error)
        this.errorMessage = '제안 목록을 가져오는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 특정 제안 가져오기
    async fetchSuggestion(contactId) {
      this.isLoading = true
      try {
        const response = await api.get(`/admin/suggestion/${contactId}`)
        this.currentSuggestion = response.data
        this.errorMessage = ''
      } catch (error) {
        console.error('제안 가져오기 실패:', error)
        this.errorMessage = '제안을 가져오는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 제안 삭제하기
    async deleteSuggestion(contactId) {
      this.isLoading = true
      try {
        await api.delete(`/admin/suggestion/${contactId}`)
        this.successMessage = '제안이 성공적으로 삭제되었습니다.'
        this.errorMessage = ''
        // 삭제 후 목록을 다시 가져옵니다.
        this.fetchSuggestions(this.currentPage, this.pageSize, this.searchType, this.searchKeyword)
      } catch (error) {
        console.error('제안 삭제 실패:', error)
        this.errorMessage = '제안을 삭제하는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 사용자 검색
    async searchUsers(email) {
      this.isLoading = true
      try {
        const response = await api.get('/admin/search/users', {
          params: { email }
        })
        this.users = response.data
        this.errorMessage = ''
      } catch (error) {
        console.error('사용자 검색 실패:', error)
        this.errorMessage = '사용자를 검색하는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 가게 검색
    async searchStores(storeName) {
      this.isLoading = true
      try {
        const response = await api.get('/admin/search/stores', {
          params: { storeName }
        })
        this.stores = response.data
        this.errorMessage = ''
      } catch (error) {
        console.error('가게 검색 실패:', error)
        this.errorMessage = '가게를 검색하는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 소유자 할당
    async assignOwner(assignOwnerRequest) {
      this.isLoading = true
      try {
        const response = await api.post('/admin/assign/owner', assignOwnerRequest)
        this.assignOwnerResponse = response.data
        this.successMessage = '소유자가 성공적으로 할당되었습니다.'
        this.errorMessage = ''
      } catch (error) {
        console.error('소유자 할당 실패:', error)
        this.errorMessage = '소유자 할당에 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 가게 생성 액션
    async createStore(storeData, storePhoto) {
      this.isLoading = true
      try {
        const formData = new FormData()
        formData.append('store', new Blob([JSON.stringify(storeData)], { type: 'application/json' }))
        formData.append('storePhoto', storePhoto)

        const response = await api.post('/admin/stores', formData)
        this.createdStore = response.data
        this.successMessage = '가게가 성공적으로 생성되었습니다.'
        this.errorMessage = ''
        return this.createdStore
      } catch (error) {
        console.error('가게 생성 실패:', error)
        this.errorMessage = error.response?.data || '가게 생성에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 메뉴 생성 액션
    async createMenu(storeId, menuData, menuPhoto) {
      this.isLoading = true
      try {
        const formData = new FormData()
        formData.append('menuAdminDTO', new Blob([JSON.stringify(menuData)], { type: 'application/json' }))
        formData.append('menuPhoto', menuPhoto)

        const response = await api.post(`admin/menu/${storeId}`, formData) // 수정된 엔드포인트
        this.successMessage = '메뉴가 성공적으로 추가되었습니다.'
        this.errorMessage = ''
        return response.data
      } catch (error) {
        console.error('메뉴 생성 실패:', error)
        this.errorMessage = error.response?.data || '메뉴 생성에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 가게와 메뉴를 순차적으로 생성하는 액션
    async createStoreAndMenus(storeData, storePhoto, menus) {
      this.isLoading = true
      try {
        // 가게 생성
        const createdStore = await this.createStore(storeData, storePhoto)

        // 각 메뉴 생성
        for (const menu of menus) {
          await this.createMenu(createdStore.storeId, {
            menuName: menu.menuName,
            price: menu.price
          }, menu.menuPhoto)
        }

        this.successMessage = '가게와 메뉴가 성공적으로 등록되었습니다.'
        this.errorMessage = ''
        return createdStore
      } catch (error) {
        this.errorMessage = this.errorMessage || '가게 및 메뉴 등록에 실패했습니다.'
        this.successMessage = ''
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 가게 수정 액션
    async updateStore(storeId, storeData, storePhoto) {
      this.isLoading = true
      try {
        const formData = new FormData()
        formData.append('store', new Blob([JSON.stringify(storeData)], { type: 'application/json' }))
        formData.append('storePhoto', storePhoto)

        const response = await api.put(`/admin/stores/${storeId}`, formData)
        this.updatedStore = response.data
        this.successMessage = '가게가 성공적으로 수정되었습니다.'
        this.errorMessage = ''
        return this.updatedStore
      } catch (error) {
        console.error('가게 수정 실패:', error)
        this.errorMessage = error.response?.data || '가게 수정에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 가게 정보 가져오기
    async getStore(storeId) {
      this.isLoading = true
      try {
        const response = await api.get(`/store/${storeId}`)
        this.currentStore = response.data
        this.errorMessage = ''
        return this.currentStore
      } catch (error) {
        console.error('가게 정보 가져오기 실패:', error)
        this.errorMessage = '가게 정보를 가져오는 데 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

        // 메뉴 목록 가져오기
        async getMenus(storeId) {
          this.isLoading = true
          try {
            const response = await api.get(`/menu/${storeId}`)
            this.errorMessage = ''
            return response.data
          } catch (error) {
            console.error('메뉴 정보 가져오기 실패:', error)
            this.errorMessage = '메뉴 정보를 가져오는 데 실패했습니다.'
            throw error
          } finally {
            this.isLoading = false
          }
        },

    // 메뉴 업데이트 액션 (이미 존재하는 메뉴)
    async updateMenu(menuId, menuData, menuPhoto) {
      this.isLoading = true
      try {
        const formData = new FormData()
        formData.append('menuAdminDTO', new Blob([JSON.stringify(menuData)], { type: 'application/json' }))
        if (menuPhoto) {
          formData.append('menuPhoto', menuPhoto)
        }

        const response = await api.patch(`/admin/menu/${menuId}`, formData)
        this.successMessage = '메뉴가 성공적으로 수정되었습니다.'
        this.errorMessage = ''
        return response.data
      } catch (error) {
        console.error('메뉴 수정 실패:', error)
        this.errorMessage = error.response?.data || '메뉴 수정에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },

    // 메뉴 삭제 액션
    async deleteMenu(menuId) {
      this.isLoading = true
      try {
        const response = await api.delete(`/admin/menu/${menuId}`)
        this.successMessage = '메뉴가 성공적으로 삭제되었습니다.'
        this.errorMessage = ''
        return response.data
      } catch (error) {
        console.error('메뉴 삭제 실패:', error)
        this.errorMessage = error.response?.data || '메뉴 삭제에 실패했습니다.'
        throw error
      } finally {
        this.isLoading = false
      }
    },
  }
})
