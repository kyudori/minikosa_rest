<template>
  <div id="wrapHome" class="search_wrap">
    <!-- Search Header -->
    <div class="wrap_search_header">
      <div class="wrap_search">
        <h2 class="screen_out">검색 키워드 입력 창</h2>
        <div class="search_form">
          <form @submit.prevent="submitSearch">
            <input
              type="text"
              v-model="query"
              class="txt_search"
              id="txt_search"
              title="검색어입력"
              placeholder="검색어를 입력해 주세요."
              maxlength="20"
            />
            <button type="submit" class="btn btn-search">검색</button>
          </form>
        </div>
      </div>
    </div>

    <!-- Tabs -->
    <div id="searchWrap">
      <div class="search_header">
        <div class="tab_search">
          <ul id="contentsTab" class="inner_tab">
            <li :class="{ on: activeTab === 'store' }">
              <a href="#" @click.prevent="switchTab('store')" class="articleTab link_tab">식당</a>
            </li>
            <li :class="{ on: activeTab === 'review' }">
              <a href="#" @click.prevent="switchTab('review')" class="magazineTab link_tab">리뷰</a>
            </li>
          </ul>
        </div>
      </div>

      <!-- Store Search Results -->
      <div id="mArticle" v-show="activeTab === 'store'">
        <div id="resultArticle" class="search_result" style="margin-top: 0px;">
          <div class="result_search">
            <div class="search-result">
              <span>가게 검색 결과 : </span>
              <span>{{ storeCount }}</span>
              <span> 건</span>
            </div>
            <div class="result-sort">
              <a href="#" @click.prevent="changeSort('latest')">
                <span :style="sort === 'latest' ? activeSortStyle : {}">최신순</span>
              </a>
              <a href="#" @click.prevent="changeSort('rating')">
                <span :style="sort === 'rating' ? activeSortStyle : {}">평점순</span>
              </a>
            </div>
          </div>

          <!-- Store Results -->
          <section
            class="food_section layout_padding-bottom"
            v-if="storeCount > 0"
          >
            <div class="container">
              <div class="filters-content">
                <div class="row grid">
                  <div
                    v-for="store in storeResults"
                    :key="store.storeId"
                    :class="'col-sm-6 col-lg-4 all ' + store.categoryName"
                  >
                    <div class="box">
                      <router-link :to="`/store/${store.storeId}`">
                        <div class="img-box">
                          <img
                            :src="store.storePhoto"
                            alt="가게 사진"
                            @error="imageError($event)"
                          />
                        </div>
                        <div class="detail-box">
                          <h5>{{ store.storeName }}</h5>
                          <div class="detail-desc-info">
                            <p>{{ store.storeDescription }}</p>
                            <div class="detail-rate">
                              <ul>
                                <li>
                                  <img
                                    width="48"
                                    height="48"
                                    src="https://img.icons8.com/color/48/filled-star--v1.png"
                                    alt="filled-star--v1"
                                    class="icon_star"
                                  />
                                </li>
                                <li>
                                  <span class="rate_num">{{ store.ratingAvg }}</span>
                                </li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </router-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- No Store Results -->
          <div class="no-results" v-else>
            <p>가게 검색 결과가 없습니다.</p>
          </div>
        </div>
      </div>

      <!-- Review Search Results -->
      <div id="mReview" v-show="activeTab === 'review'">
        <div id="resultReview" class="search_result" style="margin-top: 0px;">
          <div class="result_search">
            <div class="search-result">
              <span>리뷰 검색 결과 : </span>
              <span>{{ reviewCount }}</span>
              <span> 건</span>
            </div>
            <div class="result-sort">
              <a href="#" @click.prevent="changeSort('latest')">
                <span :style="sort === 'latest' ? activeSortStyle : {}">최신순</span>
              </a>
              <a href="#" @click.prevent="changeSort('rating')">
                <span :style="sort === 'rating' ? activeSortStyle : {}">평점순</span>
              </a>
            </div>
          </div>

          <!-- Review Results -->
          <div v-if="reviewCount > 0">
            <div
              class="review-box"
              v-for="review in reviewResults"
              :key="review.reviewId"
            >
              <div class="review-header">
                <div class="review-info">
                  <router-link
                    :to="`/store/${review.storeId}`"
                    class="search_store_name"
                  >
                    {{ review.storeName }}
                  </router-link>
                  <div class="grade_star">
                    <img
                      width="48"
                      height="48"
                      src="https://img.icons8.com/color/48/filled-star--v1.png"
                      alt="filled-star--v1"
                      class="icon_star"
                    />
                    <em class="num_rate">{{ review.rating }}</em>
                    <span class="text_score">점</span>
                  </div>
                </div>
                <div class="review-actions">
                  <!-- Assume roleId is available in user data -->
                  <div v-if="user && user.roleId === 2">
                    <!-- Admin can delete reviews -->
                    <form
                      method="post"
                      :action="`/store/${review.storeId}/review/${review.memberId}`"
                    >
                      <input
                        type="hidden"
                        name="reviewId"
                        :value="review.reviewId"
                      />
                      <input
                        type="hidden"
                        name="memberId"
                        :value="review.memberId"
                      />
                      <button
                        type="submit"
                        class="comment_user_button"
                        @click.prevent="deleteReview(review.reviewId)"
                      >
                        삭제
                      </button>
                    </form>
                    <router-link
                      :to="`/store/${review.storeId}`"
                      class="comment_user_button"
                    >
                      리뷰 보러가기
                    </router-link>
                  </div>
                  <div v-else>
                    <router-link
                      :to="`/store/${review.storeId}`"
                      class="comment_user_button"
                    >
                      리뷰 보러가기
                    </router-link>
                  </div>
                </div>
              </div>
              <div class="comment_info">
                <p class="text_comment">
                  <span>{{ review.reviewText }}</span>
                </p>
              </div>
              <div class="unit_info">
                <span class="text_item">작성자(닉네임) : </span>
                <span class="text_username">{{ review.memberNickname }}</span>
                <span class="bar"></span>
                <span class="text_item">별점 : </span>
                <span class="text_desc">{{ review.rating }}</span>
                <span class="bar"></span>
                <span class="text_item">작성일 : </span>
                <span class="time_write">{{ formatDate(review.createdAt) }}</span>
              </div>
            </div>
          </div>

          <!-- No Review Results -->
          <div class="no-results" v-else>
            <p>리뷰 검색 결과가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- JavaScript Libraries -->
    <!-- Include Bootstrap JS if needed -->
  </div>
</template>

<script>
import api from '../axios'
import { useAuthStore } from '../stores/auth'
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'Search',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const query = ref(route.query.q || '')
    const sort = ref(route.query.sort || 'latest')
    const type = ref(route.query.type || 'store')
    const activeTab = ref(type.value)
    const storeResults = ref([])
    const storeCount = ref(0)
    const reviewResults = ref([])
    const reviewCount = ref(0)
    const user = useAuthStore().user

    const activeSortStyle = {
      fontWeight: 'bold',
      color: '#000000',
    }

    const fetchSearchResults = async () => {
      try {
        const response = await api.get('/search', {
          params: {
            q: query.value,
            sort: sort.value,
            type: type.value,
          },
        })
        if (type.value === 'store') {
          storeResults.value = response.data.storeResults || []
          storeCount.value = response.data.storeCount || 0
        } else if (type.value === 'review') {
          reviewResults.value = response.data.reviewResults || []
          reviewCount.value = response.data.reviewCount || 0
        }
      } catch (error) {
        console.error('Error fetching search results:', error)
        // Handle errors, possibly redirect to login if unauthorized
      }
    }

    const submitSearch = () => {
      router.push({
        path: '/search',
        query: {
          q: query.value,
          sort: sort.value,
          type: activeTab.value,
        },
      })
      fetchSearchResults()
    }

    const changeSort = (newSort) => {
      sort.value = newSort
      router.push({
        path: '/search',
        query: {
          q: query.value,
          sort: sort.value,
          type: activeTab.value,
        },
      })
      fetchSearchResults()
    }

    const switchTab = (tab) => {
      activeTab.value = tab
      type.value = tab
      router.push({
        path: '/search',
        query: {
          q: query.value,
          sort: sort.value,
          type: type.value,
        },
      })
      fetchSearchResults()
    }

    const imageError = (event) => {
      event.target.src = '/images/main_logo.png'
    }

    const formatDate = (dateStr) => {
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = ('0' + (date.getMonth() + 1)).slice(-2)
      const day = ('0' + date.getDate()).slice(-2)
      return `${year}.${month}.${day}`
    }

    const deleteReview = async (reviewId) => {
      if (confirm('해당 리뷰를 삭제하시겠습니까?')) {
        try {
          await api.delete(`/reviews/${reviewId}`)
          // Remove the deleted review from the list
          reviewResults.value = reviewResults.value.filter(
            (review) => review.reviewId !== reviewId
          )
          reviewCount.value--
        } catch (error) {
          console.error('Error deleting review:', error)
          // Handle error
        }
      }
    }

    onMounted(() => {
      fetchSearchResults()
    })

    return {
      query,
      sort,
      type,
      activeTab,
      storeResults,
      storeCount,
      reviewResults,
      reviewCount,
      user,
      activeSortStyle,
      submitSearch,
      changeSort,
      switchTab,
      imageError,
      formatDate,
      deleteReview,
    }
  },
}
</script>

<style scoped>
/* Include your CSS styles here */
.search_wrap {
  /* Styles for the search wrapper */
}

.wrap_search_header {
  /* Styles for the search header */
}

/* Add more styles as needed based on your CSS files */

.on {
  /* Active tab styles */
}

.no-results {
  text-align: center;
  margin: 20px 0;
}

.txt_search {
  width: 80%;
  padding: 10px;
}

.btn-search {
  padding: 10px 20px;
}

.review-box {
  border-bottom: 1px solid #ccc;
  padding: 15px 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
}

.comment_info {
  margin-top: 10px;
}

.unit_info {
  margin-top: 10px;
  color: #888;
}

/* Add more styles as needed */
</style>
