<!-- src/views/Signup.vue -->
<template>
  <div class="main-content">
    <div class="subtitle">
      <h2>회원가입</h2>
      <img src="/images/main_logo.png" alt="Main Logo">
      <p class="description">필요한 정보를 입력해 주세요.</p>
    </div>

    <!-- 오류 메시지 표시 -->
    <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
    </div>

    <!-- 성공 메시지 표시 -->
    <div v-if="successMessage" class="success-message">
      <p>{{ successMessage }}</p>
    </div>

    <form @submit.prevent="handleRegister">
      <div class="user-details">
        <!-- 이름 -->
        <div class="form-group">
          <label for="name">이름</label>
          <input type="text" id="name" v-model="form.name" placeholder="이름을 입력하세요." required>
          <div v-if="errors.name" class="error-message">
            <p>{{ errors.name }}</p>
          </div>
        </div>

        <!-- 닉네임 -->
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input
            type="text"
            id="nickname"
            v-model="form.nickname"
            @input="resetNicknameCheck"
            placeholder="닉네임을 입력하세요."
            required
          >
          <button type="button" class="check-button" @click="checkNickname" :disabled="isNicknameChecked || isSubmitting">
            중복 확인
          </button>
          <div :class="feedbackNicknameClass">{{ feedbackNickname }}</div>
          <div v-if="errors.nickname" class="error-message">
            <p>{{ errors.nickname }}</p>
          </div>
        </div>

        <!-- 이메일 -->
        <div class="form-group">
          <label for="email">이메일</label>
          <input
            type="email"
            id="email"
            v-model="form.email"
            @input="resetEmailCheck"
            placeholder="이메일을 입력하세요."
            required
          >
          <button type="button" class="check-button" @click="checkEmail" :disabled="isEmailChecked || isSubmitting">
            중복 확인
          </button>
          <div :class="feedbackEmailClass">{{ feedbackEmail }}</div>
          <div v-if="errors.email" class="error-message">
            <p>{{ errors.email }}</p>
          </div>
        </div>

        <!-- 연락처 -->
        <div class="form-group">
          <label for="contact">연락처</label>
          <input
            type="tel"
            id="contact"
            v-model="form.phone_number"
            placeholder="연락처를 입력하세요."
            required
            pattern="^[0-9\-+() ]+$"
            title="유효한 연락처를 입력해 주세요."
          >
          <div v-if="errors.phone_number" class="error-message">
            <p>{{ errors.phone_number }}</p>
          </div>
        </div>

        <!-- 비밀번호 -->
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            placeholder="비밀번호를 입력하세요."
            required
          >
          <div v-if="errors.password" class="error-message">
            <p>{{ errors.password }}</p>
          </div>
        </div>

        <!-- 비밀번호 확인 -->
        <div class="form-group">
          <label for="confirm_password">비밀번호 확인</label>
          <input
            type="password"
            id="confirm_password"
            v-model="form.confirm_password"
            placeholder="다시 한번 비밀번호를 입력하세요."
            required
            @input="checkPasswordMatch"
          >
          <div v-if="passwordMismatch" class="error-message">
            <p>비밀번호가 일치하지 않습니다.</p>
          </div>
        </div>
      </div>

      <!-- 제출 버튼 -->
      <div class="button-container">
        <input
          type="submit"
          value="가입하기"
          :disabled="isSubmitting || !isNicknameChecked || !isEmailChecked || passwordMismatch"
        >
        <button type="button" class="cancel-button" @click="resetForm">취소</button>
      </div>
    </form>
  </div>
</template>

<script>
import api from '../axios' // Axios 인스턴스 import

export default {
  name: 'Signup',
  data() {
    return {
      form: {
        name: '',
        nickname: '',
        email: '',
        phone_number: '',
        password: '',
        confirm_password: ''
      },
      errors: {},
      errorMessage: '',
      successMessage: '',
      feedbackNickname: '',
      feedbackEmail: '',
      feedbackNicknameClass: '',
      feedbackEmailClass: '',
      isSubmitting: false,
      passwordMismatch: false,
      isNicknameChecked: false,
      isEmailChecked: false
    }
  },
  methods: {
    async handleRegister() {
      this.errorMessage = ''
      this.successMessage = ''
      this.errors = {}
      this.passwordMismatch = false

      // 비밀번호 일치 여부 확인
      if (this.form.password !== this.form.confirm_password) {
        this.passwordMismatch = true
        return
      }

      // 중복 확인 여부 확인
      if (!this.isNicknameChecked || !this.isEmailChecked) {
        this.errorMessage = '닉네임과 이메일 중복 확인을 완료해 주세요.'
        return
      }

      this.isSubmitting = true

      try {
        // 회원가입 API 호출: POST /api/v1/signup
        const response = await api.post('/signup', {
          name: this.form.name,
          nickname: this.form.nickname,
          email: this.form.email,
          phone_number: this.form.phone_number,
          password: this.form.password
        })

        if (response.status === 201) {
          this.successMessage = response.data.message
          this.resetForm(true) // 성공 시 폼 초기화 유지
        } else {
          this.errorMessage = '회원가입에 실패했습니다. 다시 시도해 주세요.'
        }
      } catch (error) {
        if (error.response) {
          if (error.response.status === 400) {
            // Validation 에러
            this.errors = error.response.data
          } else if (error.response.status === 409) {
            // 중복된 이메일 또는 닉네임
            this.errorMessage = error.response.data.error
          } else {
            // 기타 에러
            this.errorMessage = error.response.data.error || '회원가입 중 오류가 발생했습니다.'
          }
        } else {
          this.errorMessage = '서버와의 연결이 원활하지 않습니다.'
        }
        console.error('회원가입 오류:', error)
      } finally {
        this.isSubmitting = false
      }
    },
    resetForm(keepSuccess = false) {
      this.form = {
        name: '',
        nickname: '',
        email: '',
        phone_number: '',
        password: '',
        confirm_password: ''
      }
      this.errors = {}
      if (!keepSuccess) {
        this.errorMessage = ''
        this.successMessage = ''
      }
      this.feedbackNickname = ''
      this.feedbackEmail = ''
      this.feedbackNicknameClass = ''
      this.feedbackEmailClass = ''
      this.passwordMismatch = false
      this.isNicknameChecked = false
      this.isEmailChecked = false
      // 리디렉션: 로그인 페이지로 이동 (필요 시 수정)
      // this.$router.push('/login')
    },
    async checkNickname() {
      const nickname = this.form.nickname.trim()
      if (nickname === '') {
        this.feedbackNicknameClass = 'feedback-message error'
        this.feedbackNickname = '닉네임을 입력해 주세요.'
        this.isNicknameChecked = false
        return
      }

      try {
        const response = await api.get('/signup/check-nickname', {
          params: { nickname }
        })
        const data = response.data

        if (data.exists) {
          this.feedbackNicknameClass = 'feedback-message error'
          this.feedbackNickname = '이미 사용 중인 닉네임입니다.'
          this.isNicknameChecked = false
        } else {
          this.feedbackNicknameClass = 'feedback-message success'
          this.feedbackNickname = '사용 가능한 닉네임입니다.'
          this.isNicknameChecked = true
        }
      } catch (error) {
        this.feedbackNicknameClass = 'feedback-message error'
        this.feedbackNickname = '닉네임 중복 확인 중 오류가 발생했습니다.'
        this.isNicknameChecked = false
        console.error('닉네임 중복 확인 오류:', error)
      }
    },
    async checkEmail() {
      const email = this.form.email.trim()
      if (email === '') {
        this.feedbackEmailClass = 'feedback-message error'
        this.feedbackEmail = '이메일을 입력해 주세요.'
        this.isEmailChecked = false
        return
      }

      try {
        const response = await api.get('/signup/check-email', {
          params: { email }
        })
        const data = response.data

        if (data.exists) {
          this.feedbackEmailClass = 'feedback-message error'
          this.feedbackEmail = '이미 사용 중인 이메일입니다.'
          this.isEmailChecked = false
        } else {
          this.feedbackEmailClass = 'feedback-message success'
          this.feedbackEmail = '사용 가능한 이메일입니다.'
          this.isEmailChecked = true
        }
      } catch (error) {
        this.feedbackEmailClass = 'feedback-message error'
        this.feedbackEmail = '이메일 중복 확인 중 오류가 발생했습니다.'
        this.isEmailChecked = false
        console.error('이메일 중복 확인 오류:', error)
      }
    },
    resetNicknameCheck() {
      if (this.isNicknameChecked) {
        this.isNicknameChecked = false
        this.feedbackNickname = '닉네임 중복 확인을 다시 해주세요.'
        this.feedbackNicknameClass = 'feedback-message warning'
      }
    },
    resetEmailCheck() {
      if (this.isEmailChecked) {
        this.isEmailChecked = false
        this.feedbackEmail = '이메일 중복 확인을 다시 해주세요.'
        this.feedbackEmailClass = 'feedback-message warning'
      }
    },
    checkPasswordMatch() {
      if (this.form.password !== this.form.confirm_password) {
        this.passwordMismatch = true
      } else {
        this.passwordMismatch = false
      }
    }
  }
}
</script>

<style scoped>
/* 기존 스타일을 여기에 추가합니다 */

.main-content {
  width: 800px;
  margin: 75px auto 100px auto;
  background-color: #fff;
  padding: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
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
  margin: 0 auto 20px auto;
  width: 150px;
  height: auto;
}

.description {
  text-align: center;
  font-size: 15px;
  color: #666;
  margin-top: 20px;
  margin-bottom: 20px;
}

.error-message {
  color: red;
  text-align: center;
  margin-bottom: 20px;
}

.success-message {
  color: green;
  text-align: center;
  margin-bottom: 20px;
}

.warning-message {
  color: orange;
  text-align: center;
  margin-bottom: 20px;
}

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
  border: 1.5px solid #FF885B;
  border-radius: 5px;
  font-size: 16px;
  resize: none;
  box-sizing: border-box;
  font-family: "맑은 고딕", sans-serif;
}

.check-button {
  position: absolute;
  right: 10px;
  top: 43px;
  padding: 6px 12px;
  background-color: #FF885B;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s;
}

.check-button:hover {
  background-color: #e07a4d;
}

.check-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.feedback-message {
  margin-top: 5px;
  font-size: 14px;
}

.feedback-message.error {
  color: red;
}

.feedback-message.success {
  color: green;
}

.feedback-message.warning {
  color: orange;
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

.button-container input[type="submit"]:disabled {
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
