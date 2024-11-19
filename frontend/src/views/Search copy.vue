<!-- src/views/Search.vue -->
<template>
  <div class="search-wrap">
    <div class="search-header">
      <div class="tab-search">
        <ul class="inner-tab">
          <li :class="{ on: activeTab === 'store' }">
            <a href="#" @click.prevent="setActiveTab('store')" class="link-tab">식당</a>
          </li>
          <li :class="{ on: activeTab === 'review' }">
            <a href="#" @click.prevent="setActiveTab('review')" class="link-tab">리뷰</a>
          </li>
        </ul>
      </div>
    </div>

    <div class="result-search">
      <div class="search-result">
        <span>검색 결과: </span>
        <span v-if="activeTab === 'store'">{{ searchResult.storeCount }}</span>
        <span v-else>{{ searchResult.reviewCount }}</span>
        <span> 건</span>
      </div>
      <div class="result-sort">
        <a href="#" @click.prevent="setSort('latest')">
          <span :style="getSortStyle('latest')">최신순</span>
        </a>
        <a href="#" @click.prevent="setSort('rating')">
          <span :style="getSortStyle('rating')">평점순</span>
        </a>
      </div>
    </div>

    <!-- 식당 검색 결과 -->
    <div v-if="activeTab === 'store'" class="search-result-section">
      <div v-if="searchResult.storeCount > 0" class="store-list">
        <div class="store-item" v-for="store in searchResult.storeResults" :key="store.storeId">
          <router-link :to="`/store/${store.storeId}`">
            <div class="img-box">
              <img :src="store.storePhoto" alt="가게 사진" @error="handleImageError($event)" />
            </div>
            <div class="detail-box">
              <h5>{{ store.storeName }}</h5>
              <p>{{ store.storeDescription }}</p>
              <div class="detail-rate">
                <img
                  width="20"
                  height="20"
                  src="https://img.icons8.com/color/48/filled-star--v1.png"
                  alt="star"
                />
                <span class="rate-num">{{ store.ratingAvg }}</span>
              </div>
            </div>
          </router-link>
        </div>
      </div>
      <div v-else class="no-results">
        <p>가게 검색 결과가 없습니다.</p>
      </div>
    </div>

    <!-- 리뷰 검색 결과 -->
    <div v-else class="search-result-section">
      <div v-if="searchResult.reviewCount > 0" class="review-list">
        <div class="review-item" v-for="review in searchResult.reviewResults" :key="review.reviewId">
          <div class="review-header">
            <div class="review-info">
              <router-link :to="`/store/${review.storeId}`" class="search-store-name">{{ review.storeName }}</router-link>
              <div class="grade-star">
                <img
                  width="20"
                  height="20"
                  src="https://img.icons8.com/color/48/filled-star--v1.png"
                  alt="star"
                />
                <span class="num-rate">{{ review.rating }}</span>
                <span class="text-score">점</span>
              </div>
            </div>
            <div class="review-actions">
              <template v-if="isLoggedIn">
                <template v-if="userRoleId === 2">
                  <form :action="`/store/${review.storeId}/review/${review.memberId}`" method="post" @submit.prevent="deleteReview(review.reviewId)">
                    <input type="hidden" :value="review.reviewId" name="reviewId" />
                    <input type="hidden" :value="review.memberId" name="memberId" />
                    <button type="submit" class="comment-user-button">삭제</button>
                  </form>
                  <router-link :to="`/store/${review.storeId}`" class="comment-user-button">리뷰 보러가기</router-link>
                </template>
                <template v-else>
                  <router-link :to="`/store/${review.storeId}`" class="comment-user-button">리뷰 보러가기</router-link>
                </template>
              </template>
              <template v-else>
                <router-link :to="`/store/${review.storeId}`" class="comment-user-button">리뷰 보러가기</router-link>
              </template>
            </div>
          </div>
          <div class="comment-info">
            <p class="text-comment">{{ review.reviewText }}</p>
          </div>
          <div class="unit-info">
            <span class="text-item">작성자(닉네임) : </span>
            <span class="text-username">{{ review.memberNickname }}</span>
            <span class="bar"></span>
            <span class="text-item">별점 : </span>
            <span class="text-desc">{{ review.rating }}</span>
            <span class="bar"></span>
            <span class="text-item">작성일 : </span>
            <span class="time-write">{{ formatDate(review.createdAt) }}</span>
          </div>
        </div>
      </div>
      <div v-else class="no-results">
        <p>리뷰 검색 결과가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import api from '../axios' // 중앙집중식 Axios 인스턴스 임포트
import { useAuthStore } from '../stores/auth'

export default {
  name: 'Search',
  setup() {
    const route = useRoute()
    const searchQuery = ref(route.query.q || '')
    const sortType = ref(route.query.sort || 'latest')
    const activeTab = ref(route.query.type || 'store') // 'store' 또는 'review'

    const searchResult = ref({
      storeResults: [],
      storeCount: 0,
      reviewResults: [],
      reviewCount: 0
    })

    const authStore = useAuthStore()
    const isLoggedIn = computed(() => !!authStore.accessToken)
    const userRoleId = computed(() => authStore.user?.roleId || null)

    const fetchSearchResults = async (type) => {
      try {
        const response = await api.get('/search', {
          params: {
            q: searchQuery.value,
            sort: sortType.value,
            type: type
          },
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        })

        if (type === 'store') {
          searchResult.value.storeResults = response.data.storeResults || []
          searchResult.value.storeCount = response.data.storeCount || 0
        } else if (type === 'review') {
          searchResult.value.reviewResults = response.data.reviewResults || []
          searchResult.value.reviewCount = response.data.reviewCount || 0
        }
      } catch (error) {
        console.error('검색 실패:', error)
        // 에러 처리 로직 추가 (예: 사용자에게 알림)
      }
    }

    const setActiveTab = (tab) => {
      activeTab.value = tab
      // URL 업데이트
      window.history.replaceState(null, '', `?q=${encodeURIComponent(searchQuery.value)}&sort=${sortType.value}&type=${tab}`)
      fetchSearchResults(tab)
    }

    const setSort = (sort) => {
      sortType.value = sort
      // URL 업데이트
      window.history.replaceState(null, '', `?q=${encodeURIComponent(searchQuery.value)}&sort=${sort}&type=${activeTab.value}`)
      fetchSearchResults(activeTab.value)
    }

    const getSortStyle = (sort) => {
      if (sortType.value === sort) {
        return {
          fontWeight: 'bold',
          color: '#000000'
        }
      }
      return {
        fontWeight: 'normal',
        color: ''
      }
    }

    const formatDate = (dateStr) => {
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}.${month}.${day}`
    }

    const handleImageError = (event) => {
      event.target.src = '/images/main_logo.png' // 기본 이미지로 대체
    }

    const deleteReview = async (reviewId) => {
      if (!confirm('해당 리뷰를 삭제하시겠습니까?')) {
        return
      }
      try {
        await api.delete(`/admin/review/${reviewId}`, {
          headers: {
            Authorization: `Bearer ${authStore.accessToken}`
          }
        })
        // 삭제 후 검색 결과에서 해당 리뷰 제거
        searchResult.value.reviewResults = searchResult.value.reviewResults.filter(r => r.reviewId !== reviewId)
        searchResult.value.reviewCount -= 1
        alert('리뷰가 성공적으로 삭제되었습니다.')
      } catch (error) {
        console.error('리뷰 삭제 실패:', error)
        alert('리뷰 삭제에 실패했습니다.')
      }
    }

    onMounted(() => {
      // 두 종류의 검색 결과를 모두 가져옵니다.
      fetchSearchResults('store')
      fetchSearchResults('review')
    })

    return {
      searchResult,
      activeTab,
      setActiveTab,
      setSort,
      getSortStyle,
      formatDate,
      handleImageError,
      isLoggedIn,
      userRoleId,
      deleteReview
    }
  }
}
</script>

<style scoped>
/* 스타일은 기존 코드와 동일합니다 */
</style>
