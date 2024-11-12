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
          <label for="name">이름</label>
          <input type="text" id="name" v-model="form.name" placeholder="가입 시 입력한 이름을 입력해 주세요. Ex) 홍길동" required>
        </div>
        <div class="form-group">
          <label for="contact">연락처</label>
          <input type="tel" id="contact" v-model="form.phoneNumber" placeholder="'-'를 포함한 연락처를 입력해 주세요. Ex) 010-1234-5678" required pattern="^[0-9\-+() ]+$" title="유효한 연락처를 입력해 주세요.">
        </div>
        <div class="form-group">
          <label for="email">이메일</label>
          <input type="email" id="email" v-model="form.email" placeholder="가입 시 사용한 이메일을 입력해 주세요. Ex) example@domain.com" required>
        </div>
        <div class="button-container">
          <input type="submit" value="회원 조회">
          <button type="button" class="cancel-button" @click="resetForm">취소</button>
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
        <input type="hidden" v-model="email" />
        <div class="form-group">
          <label for="new_password">새 비밀번호</label>
          <input type="password" id="new_password" v-model="form.newPassword" placeholder="새로운 비밀번호를 입력해 주세요." required>
        </div>
        <div class="form-group">
          <label for="confirm_password">비밀번호 확인</label>
          <input type="password" id="confirm_password" v-model="form.confirmPassword" placeholder="비밀번호를 다시 입력해 주세요." required>
        </div>
        <div class="button-container">
          <input type="submit" value="비밀번호 초기화">
          <button type="button" class="cancel-button" @click="resetForm">취소</button>
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
  export default {
    name: 'ResetPassword',
    data() {
      return {
        form: {
          name: '',
          phoneNumber: '',
          email: '',
          newPassword: '',
          confirmPassword: ''
        },
        errorMessage: '',
        successMessage: '',
        passwordReset: false,
        email: ''
      }
    },
    methods: {
      async handleSearchMember() {
        if (this.form.name.trim() === '' || this.form.phoneNumber.trim() === '' || this.form.email.trim() === '') {
          alert('모든 필드를 채워주세요.');
          return;
        }
  
        try {
          // 백엔드 API 호출하여 사용자 정보 확인 (예시: POST /search/member)
          const response = await fetch('/search/member', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              name: this.form.name,
              phoneNumber: this.form.phoneNumber,
              email: this.form.email
            })
          });
  
          const data = await response.json();
  
          if (data.success) {
            this.email = data.email;
            this.passwordReset = true;
            this.errorMessage = '';
          } else {
            this.errorMessage = data.message;
            this.passwordReset = false;
          }
        } catch (error) {
          console.error('회원 조회 오류:', error);
          this.errorMessage = '회원 조회 중 오류가 발생했습니다.';
          this.passwordReset = false;
        }
      },
      async handleResetPassword() {
        if (this.form.newPassword !== this.form.confirmPassword) {
          alert('비밀번호가 일치하지 않습니다.');
          return;
        }
  
        try {
          // 백엔드 API 호출하여 비밀번호 초기화 (예시: POST /reset/password)
          const response = await fetch('/reset/password', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify({
              email: this.email,
              newPassword: this.form.newPassword
            })
          });
  
          const data = await response.json();
  
          if (data.success) {
            this.successMessage = '비밀번호가 성공적으로 초기화되었습니다.';
            this.errorMessage = '';
            // 폼을 초기화하고 로그인 페이지로 이동
            setTimeout(() => {
              this.$router.push('/login');
            }, 2000);
          } else {
            this.errorMessage = data.message;
            this.successMessage = '';
          }
        } catch (error) {
          console.error('비밀번호 초기화 오류:', error);
          this.errorMessage = '비밀번호 초기화 중 오류가 발생했습니다.';
          this.successMessage = '';
        }
      },
      resetForm() {
        this.form = {
          name: '',
          phoneNumber: '',
          email: '',
          newPassword: '',
          confirmPassword: ''
        }
        this.errorMessage = ''
        this.successMessage = ''
        this.passwordReset = false
        this.email = ''
        this.$router.push('/login')
      }
    }
  }
  </script>
  
  <style scoped>
  /* 기존 스타일을 여기에 추가합니다 */
  
  .main-content {
    width: 800px;
    margin: 75px auto; /* 상단 여백 추가 및 가운데 정렬 */
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
  .form-group input[type="password"],
  .form-group textarea {
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
  </style>
  