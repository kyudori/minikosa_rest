<!-- src/views/Login.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>로그인</h2>
        <img src="/images/main_logo.png" alt="Main Logo">
        <p class="description">서비스를 이용하기 위해, 로그인을 해주세요.</p>
      </div>
  
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="userid">아이디 (이메일)</label>
          <input type="email" id="userid" v-model="form.email" placeholder="이메일을 입력해 주세요. Ex) example@domain.com" required>
        </div>
        <div class="form-group">
          <label for="userpw">비밀번호</label>
          <input type="password" id="userpw" v-model="form.password" placeholder="비밀번호를 입력해 주세요." required>
        </div>
  
        <div v-if="errorMessage" class="error-message">
          <p>{{ errorMessage }}</p>
        </div>
  
        <div class="button-container">
          <input type="submit" value="로그인">
          <button type="button" class="cancel-button" @click="resetForm">취소</button>
        </div>
  
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
  export default {
    name: 'Login',
    data() {
      return {
        form: {
          email: '',
          password: ''
        },
        errorMessage: ''
      }
    },
    methods: {
      handleLogin() {
        if (this.form.email.trim() === '' || this.form.password.trim() === '') {
          alert('모든 필드를 채워주세요.')
          return
        }
        // 추후 API 호출하여 로그인 처리
        console.log('로그인 정보:', this.form)
      },
      resetForm() {
        this.form = {
          email: '',
          password: ''
        }
        this.errorMessage = ''
        // 리디렉션: 홈으로 이동
        this.$router.push('/home')
      }
    }
  }
  </script>
  
  <style scoped>
  /* 기존 스타일을 여기에 추가합니다 */
  
  .main-content {
    width: 800px;
    margin: 75px auto;
    background-color: #fff;
    padding: 30px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    margin-bottom: 100px;
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
  }
  
  .form-group label {
    display: block;
    font-weight: bold;
    margin-bottom: 8px;
    color: #333;
  }
  
  .form-group input[type="text"],
  .form-group input[type="password"],
  .form-group input[type="email"] {
    width: 100%;
    padding: 12px 15px;
    border: 1.5px solid #FF885B;
    border-radius: 5px;
    font-size: 16px;
    resize: none;
    box-sizing: border-box;
    font-family: "맑은 고딕", sans-serif;
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
  