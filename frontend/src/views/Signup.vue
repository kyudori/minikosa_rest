<!-- src/views/Signup.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>회원가입</h2>
        <img src="/images/main_logo.png" alt="Main Logo">
        <p class="description">필요한 정보를 입력해 주세요.</p>
      </div>
  
      <div v-if="errorMessage" class="error-message">
        <p>{{ errorMessage }}</p>
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
            <input type="text" id="nickname" v-model="form.nickname" placeholder="닉네임을 입력하세요." required>
            <button type="button" class="check-button" @click="checkNickname">중복 확인</button>
            <div :class="feedbackNicknameClass">{{ feedbackNickname }}</div>
            <div v-if="errors.nickname" class="error-message">
              <p>{{ errors.nickname }}</p>
            </div>
          </div>
          <!-- 이메일 -->
          <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" v-model="form.email" placeholder="이메일을 입력하세요." required>
            <button type="button" class="check-button" @click="checkEmail">중복 확인</button>
            <div :class="feedbackEmailClass">{{ feedbackEmail }}</div>
            <div v-if="errors.email" class="error-message">
              <p>{{ errors.email }}</p>
            </div>
          </div>
          <!-- 연락처 -->
          <div class="form-group">
            <label for="contact">연락처</label>
            <input type="tel" id="contact" v-model="form.phone_number" placeholder="연락처를 입력하세요." required pattern="^[0-9\-+() ]+$" title="유효한 연락처를 입력해 주세요.">
            <div v-if="errors.phone_number" class="error-message">
              <p>{{ errors.phone_number }}</p>
            </div>
          </div>
          <!-- 비밀번호 -->
          <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" id="password" v-model="form.password" placeholder="비밀번호를 입력하세요." required>
            <div v-if="errors.password" class="error-message">
              <p>{{ errors.password }}</p>
            </div>
          </div>
          <!-- 비밀번호 확인 -->
          <div class="form-group">
            <label for="confirm_password">비밀번호 확인</label>
            <input type="password" id="confirm_password" v-model="form.confirm_password" placeholder="다시 한번 비밀번호를 입력하세요." required>
          </div>
        </div>
        <!-- 제출 버튼 -->
        <div class="button-container">
          <input type="submit" value="가입하기">
          <button type="button" class="cancel-button" @click="resetForm">취소</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
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
        feedbackNickname: '',
        feedbackEmail: '',
        feedbackNicknameClass: '',
        feedbackEmailClass: ''
      }
    },
    methods: {
      handleRegister() {
        // 비밀번호 일치 여부 확인
        if (this.form.password !== this.form.confirm_password) {
          alert('비밀번호가 일치하지 않습니다.')
          return
        }
        // 추후 API 호출하여 회원가입 처리
        console.log('회원가입 정보:', this.form)
      },
      resetForm() {
        this.form = {
          name: '',
          nickname: '',
          email: '',
          phone_number: '',
          password: '',
          confirm_password: ''
        }
        this.errors = {}
        this.errorMessage = ''
        this.feedbackNickname = ''
        this.feedbackEmail = ''
        // 리디렉션: 홈으로 이동
        this.$router.push('/home')
      },
      async checkNickname() {
        const nickname = this.form.nickname.trim()
        if (nickname === '') {
          this.feedbackNicknameClass = 'feedback-message error'
          this.feedbackNickname = '닉네임을 입력해 주세요.'
          return
        }
  
        try {
          const response = await fetch(`/signup/check-nickname?nickname=${encodeURIComponent(nickname)}`)
          const data = await response.json()
  
          if (data.exists) {
            this.feedbackNicknameClass = 'feedback-message error'
            this.feedbackNickname = '이미 사용 중인 닉네임입니다.'
          } else {
            this.feedbackNicknameClass = 'feedback-message success'
            this.feedbackNickname = '사용 가능한 닉네임입니다.'
          }
        } catch (error) {
          this.feedbackNicknameClass = 'feedback-message error'
          this.feedbackNickname = '닉네임 중복 확인 중 오류가 발생했습니다.'
          console.error('Error checking nickname:', error)
        }
      },
      async checkEmail() {
        const email = this.form.email.trim()
        if (email === '') {
          this.feedbackEmailClass = 'feedback-message error'
          this.feedbackEmail = '이메일을 입력해 주세요.'
          return
        }
  
        try {
          const response = await fetch(`/signup/check-email?email=${encodeURIComponent(email)}`)
          const data = await response.json()
  
          if (data.exists) {
            this.feedbackEmailClass = 'feedback-message error'
            this.feedbackEmail = '이미 사용 중인 이메일입니다.'
          } else {
            this.feedbackEmailClass = 'feedback-message success'
            this.feedbackEmail = '사용 가능한 이메일입니다.'
          }
        } catch (error) {
          this.feedbackEmailClass = 'feedback-message error'
          this.feedbackEmail = '이메일 중복 확인 중 오류가 발생했습니다.'
          console.error('Error checking email:', error)
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
  
  .error-message {
    color: red;
    text-align: center;
    margin-bottom: 20px;
  }
  </style>
  