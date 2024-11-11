<!-- src/views/FindEmail.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>이메일 찾기</h2>
        <img src="/images/main_logo.png" alt="Main Logo">
        <p class="description">이메일을 찾기 위해 필요한 정보를 입력해 주세요.</p>
      </div>
  
      <!-- 오류 메시지 표시 -->
      <div v-if="errorMessage" class="result-message error">
        <p>{{ errorMessage }}</p>
      </div>
      <!-- 이메일 찾기 성공 시 이메일 표시 -->
      <div v-if="foundEmail" class="result-message success">
        <p>찾으신 이메일은 다음과 같습니다:</p>
        <p>{{ foundEmail }}</p>
      </div>
  
      <form @submit.prevent="handleFindEmail">
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input type="text" id="nickname" v-model="form.nickname" placeholder="가입 시 입력한 닉네임을 입력해 주세요. Ex) 홍길동" required>
        </div>
        <div class="form-group">
          <label for="contact">연락처</label>
          <input type="tel" id="contact" v-model="form.contact" placeholder="'-'를 포함한 연락처를 입력해 주세요. Ex) 010-1234-5678" required pattern="^[0-9\-+() ]+$" title="유효한 연락처를 입력해 주세요.">
        </div>
        <div class="button-container">
          <input type="submit" value="이메일 찾기">
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
    name: 'FindEmail',
    data() {
      return {
        form: {
          nickname: '',
          contact: ''
        },
        errorMessage: '',
        foundEmail: ''
      }
    },
    methods: {
      async handleFindEmail() {
        if (this.form.nickname.trim() === '' || this.form.contact.trim() === '') {
          alert('모든 필드를 채워주세요.');
          return;
        }
  
        try {
          // 백엔드 API 호출 (예시: POST /findemail)
          const response = await fetch('/findemail', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.form)
          });
  
          const data = await response.json();
  
          if (data.success) {
            this.foundEmail = data.email;
            this.errorMessage = '';
          } else {
            this.errorMessage = data.message;
            this.foundEmail = '';
          }
        } catch (error) {
          console.error('이메일 찾기 오류:', error);
          this.errorMessage = '이메일 찾기 중 오류가 발생했습니다.';
          this.foundEmail = '';
        }
      },
      resetForm() {
        this.form.nickname = '';
        this.form.contact = '';
        this.errorMessage = '';
        this.foundEmail = '';
        this.$router.push('/login');
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
  
  .form-group textarea {
    height: 200px;
    line-height: 1.5;
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
  