<!-- src/views/EditProfile.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>내 정보 수정</h2>
        <img src="/images/main_logo.png" alt="Main Logo" />
        <p class="description">필요한 정보를 수정해 주세요.</p>
      </div>
  
      <!-- 오류 메시지 표시 -->
      <div v-if="errorMessage" class="description error">
        {{ errorMessage }}
      </div>
      <!-- 성공 메시지 표시 -->
      <div v-if="successMessage" class="description success">
        {{ successMessage }}
      </div>
  
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="name">이름</label>
          <input
            type="text"
            id="name"
            v-model="form.name"
            placeholder="이름을 입력하세요."
            required
          />
        </div>
  
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input
            type="text"
            id="nickname"
            v-model="form.nickname"
            placeholder="닉네임을 입력하세요."
            required
          />
          <button type="button" class="check-button" @click="checkNickname">중복 확인</button>
          <!-- 닉네임 중복 확인 피드백 메시지 -->
          <span :class="nicknameFeedbackClass">{{ nicknameFeedback }}</span>
        </div>
  
        <div class="form-group">
          <label for="email">이메일 (수정 불가)</label>
          <input type="email" id="email" v-model="form.email" readonly />
        </div>
  
        <div class="form-group">
          <label for="phoneNumber">연락처</label>
          <input
            type="tel"
            id="phoneNumber"
            v-model="form.phoneNumber"
            placeholder="연락처를 입력해 주세요."
            required
            pattern="^[0-9\-+() ]+$"
            title="유효한 연락처를 입력해 주세요."
          />
        </div>
  
        <div class="form-group">
          <label for="password">새 비밀번호</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            placeholder="변경을 희망할 경우만 비밀번호를 입력하세요."
          />
        </div>
  
        <div class="form-group">
          <label for="confirmPassword">비밀번호 확인</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="form.confirmPassword"
            placeholder="다시 한번 비밀번호를 입력하세요."
          />
        </div>
  
        <div class="form-group">
          <label>사용자 유형</label>
          <label>
            <input
              type="radio"
              name="user_type"
              value="사장님"
              :checked="form.roleId === 3"
              disabled
            />
            사장님
          </label>
          <label>
            <input
              type="radio"
              name="user_type"
              value="손님"
              :checked="form.roleId !== 3"
              disabled
            />
            손님
          </label>
        </div>
  
        <div
          class="form-group"
          v-if="form.roleId === 3"
        >
          <label for="storeName">가게명</label>
          <input
            type="text"
            id="storeName"
            v-model="form.storeName"
            placeholder="가게명을 입력하세요."
            readonly
          />
        </div>
  
        <!-- 제출 버튼 -->
        <div class="button-container">
          <input type="submit" value="수정 완료" :disabled="isLoading" />
          <button type="button" class="cancel-button" @click="cancel" :disabled="isLoading">
            취소
          </button>
        </div>
  
        <!-- 버튼 아래에 추가된 링크 -->
        <p class="form-footer">
          로그인에 문제가 있나요?<br />
          <router-link to="/signup">회원가입</router-link>
          <router-link to="/findemail">ID 찾기</router-link>
          <router-link to="/reset/password">PW 초기화</router-link>
        </p>
  
        <!-- 로딩 인디케이터 추가 -->
        <div v-if="isLoading" class="loading-indicator">
          정보 수정 중...
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import { ref, reactive, computed, onMounted } from 'vue'
  import api from '../axios'
  import { useAuthStore } from '../stores/auth'
  import { useRouter } from 'vue-router'
  
  export default {
    name: 'EditProfile',
    setup() {
      const authStore = useAuthStore()
      const router = useRouter()
  
      const form = reactive({
        name: '',
        nickname: '',
        email: '',
        phoneNumber: '',
        password: '',
        confirmPassword: '',
        roleId: null,
        storeName: ''
      })
  
      const errorMessage = ref('')
      const successMessage = ref('')
      const isLoading = ref(false)
  
      const nicknameFeedback = ref('')
      const nicknameFeedbackClass = ref('')
  
      // 초기 사용자 정보 가져오기
      const fetchUserInfo = async () => {
        try {
          const response = await api.get('/userinfo')
          // Assuming the response contains all necessary user data
          // e.g., { memberId, name, nickname, email, phoneNumber, roleId, storeName }
          Object.assign(form, response.data)
        } catch (error) {
          console.error('사용자 정보 가져오기 실패:', error)
          errorMessage.value = '사용자 정보를 가져오는 데 실패했습니다.'
        }
      }
  
      onMounted(() => {
        fetchUserInfo()
      })
  
      // 닉네임 중복 검사
      const checkNickname = async () => {
        const nickname = form.nickname.trim()
        if (nickname === '') {
          nicknameFeedback.value = '닉네임을 입력해 주세요.'
          nicknameFeedbackClass.value = 'error'
          return
        }
  
        try {
          const response = await api.get('/check-editnickname', {
            params: { nickname }
          })
          if (response.data.exists) {
            nicknameFeedback.value = '이미 사용 중인 닉네임입니다.'
            nicknameFeedbackClass.value = 'error'
          } else {
            nicknameFeedback.value = '사용 가능한 닉네임입니다.'
            nicknameFeedbackClass.value = 'success'
          }
        } catch (error) {
          console.error('닉네임 중복 검사 실패:', error)
          nicknameFeedback.value = '닉네임 중복 검사 중 오류가 발생했습니다.'
          nicknameFeedbackClass.value = 'error'
        }
      }
  
      // 폼 제출 핸들러
      const handleSubmit = async () => {
        // 폼 유효성 검사
        if (
          !form.name.trim() ||
          !form.nickname.trim() ||
          !form.phoneNumber.trim()
        ) {
          errorMessage.value = '이름, 닉네임, 연락처를 모두 입력해 주세요.'
          successMessage.value = ''
          return
        }
  
        if (form.password || form.confirmPassword) {
          if (form.password !== form.confirmPassword) {
            errorMessage.value = '비밀번호가 일치하지 않습니다.'
            successMessage.value = ''
            return
          }
        }
  
        isLoading.value = true
        errorMessage.value = ''
        successMessage.value = ''
  
        // 준비된 데이터 전송
        const payload = {
          name: form.name.trim(),
          nickname: form.nickname.trim(),
          phoneNumber: form.phoneNumber.trim(),
          password: form.password.trim() || null,
          memberId: form.memberId
        }
  
        try {
          const response = await api.put('/editprofile', payload)
          successMessage.value = '정보가 성공적으로 수정되었습니다.'
          // 업데이트된 사용자 정보를 스토어와 로컬 스토리지에 반영
          authStore.user = {
            ...authStore.user,
            name: response.data.name,
            nickname: response.data.nickname,
            phoneNumber: response.data.phoneNumber
          }
          localStorage.setItem('user', JSON.stringify(authStore.user))
        } catch (error) {
          console.error('정보 수정 실패:', error)
          if (error.response) {
            if (error.response.status === 401) {
              errorMessage.value = '인증이 필요합니다. 다시 로그인해주세요.'
              await authStore.logout()
              router.push('/login')
            } else if (error.response.status === 400) {
              errorMessage.value = '잘못된 요청입니다. 입력 값을 확인해주세요.'
            } else {
              errorMessage.value = error.response.data.message || '정보 수정 중 오류가 발생했습니다.'
            }
          } else {
            errorMessage.value = '정보 수정 중 오류가 발생했습니다.'
          }
        } finally {
          isLoading.value = false
        }
      }
  
      // 취소 버튼 핸들러
      const cancel = () => {
        router.go(-1)
      }
  
      return {
        form,
        errorMessage,
        successMessage,
        isLoading,
        checkNickname,
        nicknameFeedback,
        nicknameFeedbackClass,
        handleSubmit,
        cancel
      }
    }
  }
  </script>
  
  <style scoped>
  /* 메인 콘텐츠 스타일 */
  .main-content {
    width: 800px;
    margin: 75px auto 100px auto; /* 헤더와 본문 간격, 본문과 푸터 간격 */
    background-color: #fff;
    padding: 30px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
  }
  
  .main-content h2 {
    font-size: 28px;
    color: #ff885b;
    margin-bottom: 20px;
    text-align: center;
  }
  
  .main-content img {
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
  
  .error {
    color: red;
  }
  
  .success {
    color: green;
  }
  
  /* 폼 요소 스타일 */
  .form-group {
    margin-bottom: 20px;
    position: relative;
  }
  
  .form-group label {
    display: block;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
  }
  
  .form-group input[type="text"],
  .form-group input[type="password"],
  .form-group input[type="email"],
  .form-group input[type="tel"] {
    width: 100%;
    padding: 12px 15px;
    border: 1.5px solid #ff885b;
    border-radius: 5px;
    font-size: 16px;
    resize: none;
    box-sizing: border-box;
    font-family: "맑은 고딕", sans-serif;
  }
  
  /* 중복 체크 버튼 스타일 */
  .form-group .check-button {
    position: absolute;
    right: 10px;
    top: 43px;
    padding: 6px 12px;
    background-color: #ff885b;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 12px;
    transition: background-color 0.3s;
  }
  
  .form-group .check-button:hover {
    background-color: #e07a4d;
  }
  
  /* 플레이스홀더 스타일 */
  input::placeholder {
    white-space: pre-line;
    color: #999;
  }
  
  /* 버튼 컨테이너 스타일 */
  .button-container {
    text-align: center;
    margin-top: 30px;
  }
  
  /* 버튼 스타일 */
  .button-container input[type="submit"],
  .button-container .cancel-button {
    background-color: #ff885b;
    color: #fff;
    border: none;
    padding: 12px 30px;
    font-size: 16px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    margin: 0 10px;
  }
  
  /* 취소 버튼 별도 스타일 */
  .button-container .cancel-button {
    background-color: #ccc;
    color: #333;
  }
  
  /* 버튼 호버 효과 */
  .button-container input[type="submit"]:hover {
    background-color: #e07a4d;
  }
  
  .button-container .cancel-button:hover {
    background-color: #999;
  }
  
  /* 폼 하단 추가 링크 스타일 */
  .form-footer {
    text-align: center;
    margin-top: 20px;
    font-size: 14px;
    color: #666;
  }
  
  .form-footer a {
    color: #ff885b;
    text-decoration: none;
    margin: 0 10px;
    transition: color 0.3s;
  }
  
  .form-footer a:hover {
    color: #e07a4d;
  }
  
  /* 피드백 메시지 스타일 */
  .feedback-message {
    display: block;
    margin-top: 5px;
    font-size: 12px;
  }
  
  .feedback-message.success {
    color: green;
  }
  
  .feedback-message.error {
    color: red;
  }
  
  /* 로딩 인디케이터 스타일 */
  .loading-indicator {
    text-align: center;
    margin-top: 20px;
    font-size: 16px;
    color: #666;
  }
  </style>
  