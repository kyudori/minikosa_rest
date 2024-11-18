<!-- src/views/admin/RegisterOwner.vue -->
<template>
    <div id="adminWrapper">
      <h2>사장 등록 페이지</h2>
      <img src="/images/main_logo.png" alt="Main Logo">
      <p class="description">가게의 사장님을 등록해 주세요.</p>
  
      <!-- 사용자 검색 섹션 -->
      <div class="section">
        <h3>사용자 검색</h3>
        <div class="form-group">
          <label for="searchEmail">이메일로 검색:</label>
          <input
            type="text"
            id="searchEmail"
            v-model="searchUser.email"
            placeholder="사용자 이메일 입력"
          />
          <button @click="searchUserFunction">검색</button>
        </div>
        <div class="result-section" v-if="userResults.length > 0">
          <h4>검색 결과:</h4>
          <ul id="userList">
            <li
              v-for="user in userResults"
              :key="user.memberId"
              @click="selectUser(user)"
              :class="{ selected: selectedUser && selectedUser.memberId === user.memberId }"
            >
              {{ user.name }} ({{ user.nickname }}) - {{ user.email }} - {{ user.phoneNumber }}
            </li>
          </ul>
        </div>
        <div v-else-if="userSearched">
          <p>검색 결과가 없습니다.</p>
        </div>
      </div>
  
      <!-- 선택된 사용자 섹션 -->
      <div class="section" v-if="selectedUser">
        <h3>선택된 사용자:</h3>
        <div class="selected-box">
          <button class="remove-btn" @click="removeUser">×</button>
          <strong>{{ selectedUser.name }} ({{ selectedUser.nickname }})</strong><br>
          이메일: {{ selectedUser.email }}<br>
          연락처: {{ selectedUser.phoneNumber }}
        </div>
      </div>
  
      <!-- 가게 검색 섹션 -->
      <div class="section">
        <h3>가게 검색</h3>
        <div class="form-group">
          <label for="searchStore">가게 이름으로 검색:</label>
          <input
            type="text"
            id="searchStore"
            v-model="searchStore.storeName"
            placeholder="가게 이름 입력"
          />
          <button @click="searchStoreFunction">검색</button>
        </div>
        <div class="result-section" v-if="storeResults.length > 0">
          <h4>검색 결과:</h4>
          <ul id="storeList">
            <li
              v-for="store in storeResults"
              :key="store.storeId"
              @click="selectStore(store)"
              :class="{ selected: selectedStore && selectedStore.storeId === store.storeId }"
            >
              {{ store.storeName }} - {{ store.roadAddress }}
            </li>
          </ul>
        </div>
        <div v-else-if="storeSearched">
          <p>검색 결과가 없습니다.</p>
        </div>
      </div>
  
      <!-- 선택된 가게 섹션 -->
      <div class="section" v-if="selectedStore">
        <h3>선택된 가게:</h3>
        <div class="selected-box">
          <button class="remove-btn" @click="removeStore">×</button>
          <strong>{{ selectedStore.storeName }}</strong><br>
          주소: {{ selectedStore.roadAddress }}
        </div>
      </div>
  
      <!-- 매칭 섹션 -->
      <div class="section" v-if="selectedUser && selectedStore">
        <h3>사장과 가게 매칭</h3>
        <div class="button-container">
          <button class="match-button" @click="assignOwner">매칭하기</button>
        </div>
      </div>
  
      <!-- 결과 메시지 섹션 -->
      <div class="result-section" v-if="resultMessage">
        <p :class="resultType === 'success' ? 'result-message success' : 'result-message error'">
          {{ resultMessage }}
        </p>
      </div>
    </div>
  </template>
  
  <script>
import api from '../../axios'
import { useAdminStore } from '../../stores/admin'
import { useRoute, useRouter } from 'vue-router'
  
  export default {
    name: 'RegisterOwner',
    data() {
      return {
        searchUser: {
          email: ''
        },
        userResults: [],
        userSearched: false,
        selectedUser: null,
  
        searchStore: {
          storeName: ''
        },
        storeResults: [],
        storeSearched: false,
        selectedStore: null,
  
        resultMessage: '',
        resultType: '' // 'success' 또는 'error'
      };
    },
    methods: {
      /**
       * 사용자 검색 함수
       */
      async searchUserFunction() {
        this.userResults = [];
        this.userSearched = false;
        this.resultMessage = '';
        this.resultType = '';
  
        const email = this.searchUser.email.trim();
  
        if (email === '') {
          this.resultMessage = '이메일을 입력해주세요.';
          this.resultType = 'error';
          return;
        }
  
        try {
          const response = await api.get('/admin/search/users', {
            params: { email }
          });
  
          this.userResults = response.data;
          this.userSearched = true;
  
          if (this.userResults.length === 0) {
            this.resultMessage = '일치하는 사용자가 없습니다.';
            this.resultType = 'error';
          }
        } catch (error) {
          console.error('사용자 검색 오류:', error);
          this.resultMessage = '사용자 검색 중 오류가 발생했습니다.';
          this.resultType = 'error';
        }
      },
  
      /**
       * 사용자 선택 함수
       */
      selectUser(user) {
        this.selectedUser = user;
        this.resultMessage = '';
        this.resultType = '';
      },
  
      /**
       * 사용자 제거 함수
       */
      removeUser() {
        this.selectedUser = null;
        this.resultMessage = '';
        this.resultType = '';
      },
  
      /**
       * 가게 검색 함수
       */
      async searchStoreFunction() {
        this.storeResults = [];
        this.storeSearched = false;
        this.resultMessage = '';
        this.resultType = '';
  
        const storeName = this.searchStore.storeName.trim();
  
        if (storeName === '') {
          this.resultMessage = '가게 이름을 입력해주세요.';
          this.resultType = 'error';
          return;
        }
  
        try {
          const response = await api.get('/admin/search/stores', {
            params: { storeName }
          });
  
          this.storeResults = response.data;
          this.storeSearched = true;
  
          if (this.storeResults.length === 0) {
            this.resultMessage = '일치하는 가게가 없습니다.';
            this.resultType = 'error';
          }
        } catch (error) {
          console.error('가게 검색 오류:', error);
          this.resultMessage = '가게 검색 중 오류가 발생했습니다.';
          this.resultType = 'error';
        }
      },
  
      /**
       * 가게 선택 함수
       */
      selectStore(store) {
        this.selectedStore = store;
        this.resultMessage = '';
        this.resultType = '';
      },
  
      /**
       * 가게 제거 함수
       */
      removeStore() {
        this.selectedStore = null;
        this.resultMessage = '';
        this.resultType = '';
      },
  
      /**
       * 사장과 가게 매칭 함수
       */
      async assignOwner() {
        if (!this.selectedUser || !this.selectedStore) {
          this.resultMessage = '사용자와 가게를 모두 선택해주세요.';
          this.resultType = 'error';
          return;
        }
  
        try {
          const payload = {
            storeId: this.selectedStore.storeId,
            memberId: this.selectedUser.memberId
          };
  
          const response = await api.post('/admin/assign/owner', payload);
  
          if (response.status === 200) {
            this.resultMessage = '사장님이 가게에 성공적으로 매칭되었습니다.';
            this.resultType = 'success';
            // 매칭 후 선택된 사용자와 가게 초기화
            this.selectedUser = null;
            this.selectedStore = null;
          } else {
            this.resultMessage = '매칭에 실패했습니다.';
            this.resultType = 'error';
          }
        } catch (error) {
          console.error('매칭 오류:', error);
          if (error.response && error.response.data) {
            this.resultMessage = `매칭 실패: ${error.response.data}`;
          } else {
            this.resultMessage = '매칭 중 오류가 발생했습니다.';
          }
          this.resultType = 'error';
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* 관리자 페이지 스타일 */
  #adminWrapper {
    width: 800px;
    margin: 50px auto; /* 상단 여백 유지 */
    background-color: #fff;
    padding: 30px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    margin-bottom: 30px;
  }
  
  #adminWrapper h2 {
    font-size: 28px;
    color: #FF885B;
    margin-bottom: 20px;
    text-align: center;
  }
  
  /* 관리자 페이지의 이미지 스타일 수정 */
  #adminWrapper img {
    display: block;
    margin: 0 auto 20px auto; /* 가운데 정렬 및 하단 여백 추가 */
    width: 150px; /* 원하는 크기로 조정 */
    height: auto;
  }
  
  .description {
    text-align: center;
    font-size: 15px;
    color: #666;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  
  .section {
    margin-bottom: 30px;
  }
  
  .section h3 {
    font-size: 20px;
    margin-bottom: 10px;
    color: #333;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
  }
  
  .form-group label {
    margin-bottom: 5px;
    font-weight: bold;
  }
  
  .form-group input[type="text"],
  .form-group input[type="email"],
  .form-group button {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
  }
  
  .form-group button {
    background-color: #FF885B;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
    margin-top: 10px;
    width: 100px;
  }
  
  .form-group button:hover {
    background-color: #e07a4d;
  }
  
  .result-section {
    margin-top: 20px;
  }
  
  .result-section h4 {
    font-size: 16px;
    margin-bottom: 10px;
    color: #333;
  }
  
  .result-section ul {
    list-style: none;
    padding: 0;
  }
  
  .result-section ul li {
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .result-section ul li:hover {
    background-color: #f1f1f1;
  }
  
  /* 선택된 항목 박스 스타일 */
  .selected-box {
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 10px;
    margin-top: 10px;
    background-color: #f9f9f9;
    position: relative;
  }
  
  .selected-box .remove-btn {
    position: absolute;
    top: 5px;
    right: 10px;
    background: none;
    border: none;
    color: #ff4d4d;
    font-size: 16px;
    cursor: pointer;
  }
  
  /* 버튼 컨테이너 스타일 */
  .button-container {
    display: flex;
    justify-content: center;
    gap: 20px; /* 버튼 간 간격 추가 */
    margin-top: 40px;
  }
  
  .button-container .match-button {
    background-color: #FF885B; /* 매칭하기 버튼 */
    border: none;
    color: white;
    padding: 12px 30px;
    font-size: 16px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .button-container .match-button:hover {
    background-color: #e07a4d;
  }
  
  /* 결과 메시지 스타일 */
  .result-message {
    text-align: center;
    font-size: 16px;
    color: #333;
    margin-top: 20px;
  }
  
  .result-message.success {
    color: green;
  }
  
  .result-message.error {
    color: red;
  }
  
  @media (max-width: 768px) {
    #adminWrapper {
      width: 90%;
      padding: 20px;
    }
  
    .form-group button {
      width: 100%;
    }
  
    .button-container {
      flex-direction: column;
      gap: 10px;
    }
  
    .button-container .match-button {
      width: 100%;
    }
  }
  
  /* 선택된 항목에 대한 강조 스타일 */
  .selected {
    background-color: #e0f7fa;
  }
  </style>
  