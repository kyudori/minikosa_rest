<!-- src/views/admin/AdminSuggestionList.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>고객 요청 사항</h2>
        <img src="/images/main_logo.png" alt="Main Logo" class="main-logo" />
        <p class="description">사용자들이 요청한 내용을 정리한 페이지입니다.</p>
      </div>
  
      <!-- 오류 메시지 표시 -->
      <div v-if="adminStore.errorMessage" class="description error">
        {{ adminStore.errorMessage }}
      </div>
      <!-- 성공 메시지 표시 -->
      <div v-if="adminStore.successMessage" class="description success">
        {{ adminStore.successMessage }}
      </div>
  
      <!-- 검색 폼 영역 -->
      <div id="searchOption">
        <select v-model="searchType" @change="handleSearch">
          <option value="title">제목</option>
          <option value="content">내용</option>
          <option value="writer">작성자</option>
          <option value="all">제목+내용</option>
        </select>
        <input
          type="text"
          v-model="searchKeyword"
          placeholder="검색어를 입력하세요"
        />
        <button @click="handleSearch">검색</button>
      </div>
  
      <!-- 제안 목록 테이블 -->
      <ul id="ulTable">
        <!-- 테이블 헤더 -->
        <li>
          <ul>
            <li>No</li>
            <li>제목</li>
            <li>작성일</li>
            <li>작성자</li>
            <li>조회수</li>
            <li>액션</li>
          </ul>
        </li>
        <!-- 게시물 리스트 영역 -->
        <li v-for="(suggestion, index) in adminStore.suggestions" :key="suggestion.contactId">
          <ul>
            <li>{{ totalSuggestions - ((currentPage - 1) * pageSize) - index }}</li>
            <li class="left">
              <router-link :to="{ name: 'AdminSuggestionView', params: { id: suggestion.contactId } }">
                {{ suggestion.title }}
              </router-link>
            </li>
            <li>{{ formatDate(suggestion.createdAt) }}</li>
            <li>{{ suggestion.memberName || 'Unknown' }}</li>
            <li>{{ suggestion.views }}</li>
            <li>
              <button @click="deleteSuggestion(suggestion.contactId)" class="delete-button">
                삭제
              </button>
            </li>
          </ul>
        </li>
        <!-- 빈 리스트 처리 -->
        <li v-if="adminStore.suggestions.length === 0">
          <p>등록된 요청 사항이 없습니다.</p>
        </li>
      </ul>
  
      <!-- 제안 추가 및 사장 등록 버튼 -->
      <div id="writeStore">
        <input type="button" value="맛집 추가" @click="navigateToCreateStore" class="add-button" />
        <input type="button" value="사장 등록" @click="navigateToRegisterOwner" class="register-button" />
      </div>
  
      <!-- 페이징 영역 -->
      <div id="divPaging">
        <!-- 이전 페이지 버튼 -->
        <div v-if="currentPage > 1">
          <a href="#" @click.prevent="changePage(currentPage - 1)">
            ◀
          </a>
        </div>
        <!-- 페이지 번호 -->
        <div v-for="page in totalPages" :key="page">
          <a
            href="#"
            v-if="page !== currentPage"
            @click.prevent="changePage(page)"
          >
            {{ page }}
          </a>
          <span v-else>
            {{ page }}
          </span>
        </div>
        <!-- 다음 페이지 버튼 -->
        <div v-if="currentPage < totalPages">
          <a href="#" @click.prevent="changePage(currentPage + 1)">
            ▶
          </a>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { onMounted, ref, computed } from 'vue'
  import { useAdminStore } from '../../stores/admin'
  import { useRouter } from 'vue-router'
  
  export default {
    name: 'AdminSuggestionList',
    setup() {
      const adminStore = useAdminStore()
      const router = useRouter()
  
      const currentPage = ref(1)
      const pageSize = ref(5)
      const searchType = ref('all')
      const searchKeyword = ref('')
  
      const totalSuggestions = computed(() => {
        return adminStore.suggestions.length
      })
  
      const totalPages = computed(() => {
        return adminStore.totalPages
      })
  
      onMounted(() => {
        adminStore.fetchSuggestions(currentPage.value, pageSize.value, searchType.value, searchKeyword.value)
      })
  
      const handleSearch = () => {
        adminStore.setSearch(searchType.value, searchKeyword.value)
      }
  
      const changePage = (page) => {
        if (page >= 1 && page <= adminStore.totalPages) {
          currentPage.value = page
          adminStore.fetchSuggestions(currentPage.value, pageSize.value, searchType.value, searchKeyword.value)
        }
      }
  
      const deleteSuggestion = async (id) => {
        if (confirm('정말로 이 제안을 삭제하시겠습니까?')) {
          await adminStore.deleteSuggestion(id)
        }
      }
  
      const navigateToCreateStore = () => {
        router.push('/admin/create/store')
      }
  
      const navigateToRegisterOwner = () => {
        router.push('/admin/register/owner')
      }
  
      const formatDate = (dateString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
        return new Date(dateString).toLocaleDateString('ko-KR', options)
      }
  
      return {
        adminStore,
        currentPage,
        pageSize,
        searchType,
        searchKeyword,
        totalSuggestions,
        totalPages,
        handleSearch,
        changePage,
        deleteSuggestion,
        navigateToCreateStore,
        navigateToRegisterOwner,
        formatDate
      }
    }
  }
  </script>
  
  <style scoped>
  /* 스타일은 기존 Thymeleaf 템플릿과 유사하게 유지 */
  .main-content {
    width: 800px;
    margin: 75px auto;
    background-color: #fff;
    text-align: center;
    padding: 30px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    margin-bottom: 100px;
  }
  
  .subtitle h2 {
    font-size: 28px;
    color: #ff885b;
    margin-bottom: 20px;
  }
  
  .main-logo {
    width: 150px;
    height: auto;
    margin: 10px auto;
    display: block;
  }
  
  .description {
    text-align: center;
    font-size: 15px;
    color: #666;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  
  #searchOption {
    margin-top: 30px;
    margin-bottom: 30px;
    display: flex;
    justify-content: center;
    gap: 10px;
  }
  
  #searchOption select,
  #searchOption input[type='text'] {
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
  }
  
  #searchOption button {
    background-color: #ff885b;
    border: none;
    color: white;
    padding: 8px 16px;
    font-size: 14px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s;
  }
  
  #searchOption button:hover {
    background-color: #e07a4d;
  }
  
  #ulTable {
    width: 100%;
    border-collapse: collapse;
  }
  
  #ulTable > li > ul {
    display: flex;
    border-bottom: 1px solid #ddd;
  }
  
  #ulTable > li > ul > li {
    padding: 10px;
    text-align: center;
    font-size: 14px;
  }
  
  #ulTable > li > ul > li.left {
    text-align: left;
    padding-left: 10px;
  }
  
  #ulTable > li > ul > li:first-child {
    background-color: #ff885b;
    color: #fff;
    font-weight: bold;
  }
  
  #ulTable > li > ul > li:nth-child(2) {
    width: 45%;
  }
  
  #ulTable > li > ul > li:nth-child(3) {
    width: 20%;
  }
  
  #ulTable > li > ul > li:nth-child(4) {
    width: 15%;
  }
  
  #ulTable > li > ul > li:nth-child(5) {
    width: 10%;
  }
  
  #ulTable > li > ul:hover {
    background-color: #f9f9f9;
  }
  
  .button-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
    gap: 10px;
  }
  
  .button-container .add-button,
  .button-container .register-button {
    background-color: #ff885b;
    border: none;
    color: white;
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s;
  }
  
  .button-container .add-button:hover,
  .button-container .register-button:hover {
    background-color: #e07a4d;
  }
  
  .delete-button {
    background-color: #e07a4d;
    border: none;
    color: white;
    padding: 6px 12px;
    font-size: 12px;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s;
  }
  
  .delete-button:hover {
    background-color: #c05a3f;
  }
  
  #divPaging {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 15px;
  }
  
  #divPaging > div,
  #divPaging > a,
  #divPaging > span {
    margin: 0 5px;
  }
  
  #divPaging a {
    padding: 4px 15px;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
    text-decoration: none;
  }
  
  #divPaging a:hover {
    border: 1px solid #ff885b;
    color: #ff885b;
  }
  
  #divPaging span {
    padding: 6px 12px;
    background-color: #ff885b;
    color: #fff;
    border-radius: 4px;
  }
  
  @media (max-width: 850px) {
    .main-content {
      width: 90%;
      padding: 15px;
    }
  
    #ulTable > li > ul > li {
      font-size: 12px;
      padding: 8px;
    }
  
    .button-container .add-button,
    .button-container .register-button {
      padding: 8px 16px;
      font-size: 14px;
    }
  }
  
  @media (max-width: 480px) {
    .subtitle h2 {
      font-size: 24px;
    }
  
    .description {
      font-size: 14px;
    }
  
    #ulTable > li > ul > li {
      font-size: 10px;
      padding: 6px;
    }
  
    #divPaging a {
      padding: 2px 8px;
      font-size: 12px;
    }
  }
  
  .error {
    color: red;
  }
  
  .success {
    color: green;
  }
  </style>
  