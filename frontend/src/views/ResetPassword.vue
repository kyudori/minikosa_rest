<!-- src/views/ResetPassword.vue -->
<template>
  <div class="main-content">
    <div class="subtitle">
      <h2>비밀번호 초기화</h2>
      <img src="/images/main_logo.png" alt="Main Logo">
      <p class="description">비밀번호를 초기화하기 위해 필요한 정보를 입력해 주세요.</p>
    </div>

    <!-- 오류 메시지 표시 -->
    <div v-if="errorMessage" class="result-message error">
      <p>{{ errorMessage }}</p>
    </div>

    <!-- 성공 메시지 표시 -->
    <div v-if="successMessage" class="result-message success">
      <p>{{ successMessage }}</p>
    </div>

    <!-- 사용자 정보 확인 폼 -->
    <form v-if="!passwordReset" @submit.prevent="handleSearchMember">
      <div class="form-group">
        <label for="nickname">닉네임</label>
        <input
          type="text"
          id="nickname"
          v-model="form.nickname"
          placeholder="가입 시 입력한 닉네임을 입력해 주세요. Ex) honggildong"
          required
        >
      </div>
      <div class="form-group">
        <label for="contact">연락처</label>
        <input
          type="tel"
          id="contact"
          v-model="form.phoneNumber"
          placeholder="'-'를 포함한 연락처를 입력해 주세요. Ex) 010-1234-5678"
          required
          pattern="^[0-9\-+() ]+$"
          title="유효한 연락처를 입력해 주세요."
        >
      </div>
      <div class="form-group">
        <label for="email">이메일</label>
        <input
          type="email"
          id="email"
          v-model="form.email"
          placeholder="가입 시 사용한 이메일을 입력해 주세요. Ex) example@domain.com"
          required
        >
      </div>
      <div class="button-container">
        <input type="submit" value="회원 조회" :disabled="isSubmitting">
        <button type="button" class="cancel-button" @click="goToLogin">취소</button>
      </div>
      <!-- 버튼 아래에 추가된 링크 -->
      <p class="form-footer">
        로그인에 문제가 있나요?<br>
        <router-link to="/signup">회원가입</router-link>
        <router-link to="/findemail">ID 찾기</router-link>
        <router-link to="/reset/password">PW 초기화</router-link>
      </p>
    </form>

    <!-- 비밀번호 초기화 폼 -->
    <form v-else @submit.prevent="handleResetPassword">
      <div class="form-group">
        <label for="new_password">새 비밀번호</label>
        <input
          type="password"
          id="new_password"
          v-model="form.newPassword"
          placeholder="새로운 비밀번호를 입력해 주세요."
          required
        >
        <div v-if="errors.newPassword" class="error-message">
          <p>{{ errors.newPassword }}</p>
        </div>
      </div>
      <div class="form-group">
        <label for="confirm_password">비밀번호 확인</label>
        <input
          type="password"
          id="confirm_password"
          v-model="form.confirmPassword"
          placeholder="비밀번호를 다시 입력해 주세요."
          required
          @input="checkPasswordMatch"
        >
        <div v-if="passwordMismatch" class="error-message">
          <p>비밀번호가 일치하지 않습니다.</p>
        </div>
      </div>
      <div class="button-container">
        <input
          type="submit"
          value="비밀번호 초기화"
          :disabled="isSubmitting || passwordMismatch"
        >
        <button type="button" class="cancel-button" @click="goToLogin">취소</button>
      </div>
      <!-- 버튼 아래에 추가된 링크 -->
      <p class="form-footer">
        로그인에 문제가 있나요?<br>
        <router-link to="/signup">회원가입</router-link>
        <router-link to="/findemail">ID 찾기</router-link>
        <router-link to="/reset/password">PW 초기화</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import api from '../axios' // Axios 인스턴스 import

export default {
  name: 'ResetPassword',
  data() {
    return {
      form: {
        nickname: '',
        phoneNumber: '',
        email: '',
        newPassword: '',
        confirmPassword: ''
      },
      errors: {},
      errorMessage: '',
      successMessage: '',
      passwordReset: false,
      email: '',
      isSubmitting: false,
      passwordMismatch: false
    }
  },
  methods: {
    /**
     * 회원 조회 처리
     */
    async handleSearchMember() {
      this.errorMessage = ''
      this.successMessage = ''
      this.errors = {}
      this.passwordMismatch = false

      // 필수 필드 확인
      if (
        this.form.nickname.trim() === '' ||
        this.form.phoneNumber.trim() === '' ||
        this.form.email.trim() === ''
      ) {
        this.errorMessage = '모든 필드를 채워주세요.'
        return
      }

      this.isSubmitting = true

      try {
        // 회원 조회 API 호출: POST /api/v1/find/member
        const response = await api.post('/find/member', {
          nickname: this.form.nickname,
          phoneNumber: this.form.phoneNumber,
          email: this.form.email
        })

        if (response.status === 200) {
          // 백엔드에서 boolean을 반환함
          if (response.data === true) {
            this.email = this.form.email
            this.passwordReset = true
            this.successMessage = '회원 정보를 성공적으로 조회했습니다. 새로운 비밀번호를 설정하세요.'
            this.errorMessage = ''
          } else {
            this.errorMessage = '일치하는 회원을 찾을 수 없습니다.'
            this.passwordReset = false
          }
        } else {
          this.errorMessage = '회원 조회에 실패했습니다. 다시 시도해 주세요.'
          this.passwordReset = false
        }
      } catch (error) {
        if (error.response) {
          if (error.response.status === 404) {
            this.errorMessage = '일치하는 회원을 찾을 수 없습니다.'
          } else {
            this.errorMessage = '회원 조회 중 오류가 발생했습니다.'
          }
        } else {
          this.errorMessage = '서버와의 연결이 원활하지 않습니다.'
        }
        console.error('회원 조회 오류:', error)
        this.passwordReset = false
      } finally {
        this.isSubmitting = false
      }
    },

    /**
     * 비밀번호 초기화 처리
     */
    async handleResetPassword() {
      this.errorMessage = ''
      this.successMessage = ''
      this.errors = {}
      this.passwordMismatch = false

      // 비밀번호 일치 여부 확인
      if (this.form.newPassword !== this.form.confirmPassword) {
        this.passwordMismatch = true
        return
      }

      this.isSubmitting = true

      try {
        // 비밀번호 초기화 API 호출: POST /api/v1/reset/password
        const response = await api.post('/reset/password', {
          nickname: this.form.nickname,
          phoneNumber: this.form.phoneNumber,
          email: this.email,
          newPassword: this.form.newPassword,
          confirmPassword: this.form.confirmPassword
        })

        if (response.status === 200) {
          this.successMessage = response.data.message
          this.errorMessage = ''
          // 폼 초기화 후 로그인 페이지로 이동
          setTimeout(() => {
            this.resetForm()
            this.$router.push('/login')
          }, 2000)
        } else {
          this.errorMessage = '비밀번호 초기화에 실패했습니다. 다시 시도해 주세요.'
          this.successMessage = ''
        }
      } catch (error) {
        if (error.response) {
          if (error.response.status === 400) {
            // Validation 에러
            this.errors = error.response.data
          } else if (error.response.status === 404) {
            this.errorMessage = '일치하는 회원을 찾을 수 없습니다.'
          } else if (error.response.status === 409) {
            this.errorMessage = error.response.data.error
          } else {
            this.errorMessage = '비밀번호 초기화 중 오류가 발생했습니다.'
          }
        } else {
          this.errorMessage = '서버와의 연결이 원활하지 않습니다.'
        }
        console.error('비밀번호 초기화 오류:', error)
        this.successMessage = ''
      } finally {
        this.isSubmitting = false
      }
    },

    /**
     * 로그인 페이지로 이동
     */
    goToLogin() {
      this.$router.push('/login')
    },

    /**
     * 폼 초기화 및 리디렉션
     */
    resetForm() {
      this.form = {
        nickname: '',
        phoneNumber: '',
        email: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.errors = {}
      this.errorMessage = ''
      this.successMessage = ''
      this.passwordReset = false
      this.email = ''
      this.isSubmitting = false
      this.passwordMismatch = false
      // 리디렉션: 로그인 페이지로 이동 (필요 시 수정)
      // this.$router.push('/login')
    },

    /**
     * 비밀번호 일치 여부 확인
     */
    checkPasswordMatch() {
      if (this.form.newPassword !== this.form.confirmPassword) {
        this.passwordMismatch = true
      } else {
        this.passwordMismatch = false
      }
    }
  }
}
</script>

<style scoped>
/* 스타일링을 여기에 추가합니다 */

.main-content {
  width: 800px;
  margin: 75px auto 100px auto; /* 상단 여백 추가 및 가운데 정렬 */
  background-color: #fff;
  padding: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  margin-bottom: 100px;
}

.subtitle {
  text-align: center;
  margin-bottom: 20px;
}

.main-content h2 {
  font-size: 28px;
  color: #FF885B;
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.form-group input[type="text"],
.form-group input[type="tel"],
.form-group input[type="email"],
.form-group input[type="password"] {
  width: 100%;
  padding: 12px 15px;
  border: 1.5px solid #FF885B;
  border-radius: 5px;
  font-size: 16px;
  resize: none;
  box-sizing: border-box;
  font-family: "맑은 고딕", sans-serif;
}

textarea::placeholder,
input::placeholder {
  white-space: pre-line;
  color: #999;
}

.button-container {
  text-align: center;
  margin-top: 30px;
}

.button-container input[type="submit"],
.button-container .cancel-button {
  background-color: #FF885B;
  color: #fff;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin: 0 10px;
}

.button-container .cancel-button {
  background-color: #ccc;
  color: #333;
}

.button-container input[type="submit"]:hover {
  background-color: #e07a4d;
}

.button-container .cancel-button:hover {
  background-color: #999;
}

.button-container input[type="submit"]:disabled,
.button-container .cancel-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.form-footer a {
  color: #FF885B;
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s;
}

.form-footer a:hover {
  color: #e07a4d;
}

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
  .main-content {
    width: 90%;
    padding: 20px;
  }

  .button-container input[type="submit"],
  .button-container .cancel-button {
    width: 100%;
    margin: 10px 0;
  }
}
</style>
