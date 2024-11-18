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
            <span :style="getSortStyle('latest', activeTab)">최신순</span>
          </a>
          <a href="#" @click.prevent="setSort('rating')">
            <span :style="getSortStyle('rating', activeTab)">평점순</span>
          </a>
        </div>
      </div>
  
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
      const searchQuery = route.query.q || ''
      const sortType = ref(route.query.sort || 'latest')
      const type = ref(route.query.type || 'store') // 'store' 또는 'review'
      const activeTab = ref(type.value)
  
      const searchResult = ref({
        storeResults: [],
        storeCount: 0,
        reviewResults: [],
        reviewCount: 0
      })
  
      const authStore = useAuthStore()
      const isLoggedIn = computed(() => !!authStore.accessToken)
      const userRoleId = computed(() => authStore.user?.roleId || null)
  
      const fetchSearchResults = async () => {
        try {
          const response = await api.get('/search', {
            params: {
              query: searchQuery,
              sort: sortType.value
            },
            headers: {
              Authorization: `Bearer ${authStore.accessToken}`
            }
          })
  
          searchResult.value.storeResults = response.data.storeResults
          searchResult.value.storeCount = response.data.storeCount
  
          // 별도로 리뷰 검색 API 호출
          const reviewResponse = await api.get('/search/reviews', {
            params: {
              query: searchQuery,
              sort: sortType.value
            },
            headers: {
              Authorization: `Bearer ${authStore.accessToken}`
            }
          })
  
          searchResult.value.reviewResults = reviewResponse.data.reviewResults
          searchResult.value.reviewCount = reviewResponse.data.reviewCount
        } catch (error) {
          console.error('검색 실패:', error)
          // 에러 처리 로직 추가 (예: 사용자에게 알림)
        }
      }
  
      const setActiveTab = (tab) => {
        activeTab.value = tab
        type.value = tab
        // 업데이트된 쿼리 파라미터로 URL 변경
        window.history.replaceState(null, '', `?q=${searchQuery}&sort=${sortType.value}&type=${tab}`)
      }
  
      const setSort = (sort) => {
        sortType.value = sort
        // 업데이트된 쿼리 파라미터로 URL 변경
        window.history.replaceState(null, '', `?q=${searchQuery}&sort=${sort}&type=${activeTab.value}`)
        fetchSearchResults()
      }
  
      const getSortStyle = (sort, currentType) => {
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
        fetchSearchResults()
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
  .search-wrap {
    width: 100%;
    padding: 20px;
  }
  
  .search-header .inner-tab {
    list-style: none;
    padding: 0;
    display: flex;
  }
  
  .search-header .inner-tab li {
    margin-right: 20px;
  }
  
  .search-header .inner-tab li.on a {
    font-weight: bold;
    color: #000;
  }
  
  .result-search {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px 0;
  }
  
  .search-result {
    font-size: 16px;
  }
  
  .result-sort a {
    margin-left: 10px;
    text-decoration: none;
    color: #333;
  }
  
  .result-sort a:hover span {
    color: #000;
  }
  
  .store-list, .review-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
  }
  
  .store-item, .review-item {
    width: calc(33.333% - 20px);
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    background-color: #fff;
  }
  
  .store-item img, .review-item img {
    width: 100%;
    height: 150px;
    object-fit: cover;
  }
  
  .detail-box {
    padding: 10px;
  }
  
  .detail-box h5 {
    margin: 0 0 10px 0;
    font-size: 18px;
    color: #FF885B;
  }
  
  .detail-box p {
    font-size: 14px;
    color: #666;
  }
  
  .detail-rate {
    display: flex;
    align-items: center;
  }
  
  .detail-rate .rate-num {
    margin-left: 5px;
    font-weight: bold;
  }
  
  .review-item {
    position: relative;
  }
  
  .review-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .review-info {
    display: flex;
    align-items: center;
  }
  
  .review-info .search-store-name {
    font-weight: bold;
    margin-right: 10px;
    text-decoration: none;
    color: #333;
  }
  
  .grade-star {
    display: flex;
    align-items: center;
  }
  
  .grade-star .num-rate {
    margin-left: 5px;
    font-weight: bold;
  }
  
  .comment-user-button {
    background-color: #FF885B;
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    text-decoration: none;
  }
  
  .comment-user-button:hover {
    background-color: #e07a4d;
  }
  
  .comment-info {
    padding: 10px;
  }
  
  .comment-info .text-comment {
    font-size: 14px;
    color: #333;
  }
  
  .unit-info {
    padding: 0 10px 10px 10px;
    font-size: 12px;
    color: #666;
  }
  
  .no-results {
    text-align: center;
    font-size: 16px;
    color: #999;
    margin-top: 20px;
  }
  
  @media (max-width: 768px) {
    .store-item, .review-item {
      width: calc(50% - 20px);
    }
  }
  
  @media (max-width: 480px) {
    .store-item, .review-item {
      width: 100%;
    }
  }
  </style>
  