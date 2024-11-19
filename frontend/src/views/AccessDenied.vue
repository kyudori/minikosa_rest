<!-- src/views/AccessDenied.vue -->
<template>
    <div class="main-content">
      <div class="subtitle">
        <h2>접근 거부</h2>
        <img src="/images/main_logo.png" alt="Main Logo" />
        <p class="description">이 페이지에 접근할 권한이 없습니다.</p>
        <router-link to="/home" class="description">홈으로 돌아가기</router-link>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "AccessDenied",
  };
  </script>
  
  <style scoped>
  /* 기본 리셋 및 스타일 */
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }
  
  body {
    font-family: "맑은 고딕", sans-serif;
    background-color: #f9f9f9;
    color: #333;
    line-height: 1.6;
  }
  
  /* 메인 콘텐츠 스타일 */
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
  
  .description a,
  .main-content a.description {
    text-align: center;
    font-size: 15px;
    color: #FF885B;
    margin-top: 10px;
    display: block;
    text-decoration: none;
    transition: color 0.3s;
  }
  
  .description a:hover,
  .main-content a.description:hover {
    color: #e07a4d;
  }
  </style>
  