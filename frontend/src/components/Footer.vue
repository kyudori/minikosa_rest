<!-- src/components/Footer.vue -->
<template>
    <footer>
      <div class="footer-container">
        <!-- 회사 소개 -->
        <div class="footer-section">
          <h4>프로젝트 소개</h4>
          <p>먹코살코은 최고의 맛집 제공하기 위해 최선을 다하고 있습니다.</p>
        </div>
        <!-- 빠른 링크 -->
        <div class="footer-section">
          <h4>빠른 링크</h4>
          <ul>
            <li><router-link to="/home">홈</router-link></li>
            <li><router-link to="/intro">소개</router-link></li>
            <li><router-link to="/suggestion">문의</router-link></li>
          </ul>
        </div>
        <!-- 연락처 -->
        <div class="footer-section">
          <h4>연락처</h4>
          <ul>
            <li>전화: 02-112-7777</li>
            <li>이메일: admin@taste_police.com</li>
            <li>주소: 서울특별시 송파구 중대로 135, 서관 12층</li>
          </ul>
        </div>
        <!-- 소셜 미디어 -->
        <div class="footer-section">
          <h4>소셜 미디어</h4>
          <div class="social-icons">
            <a href="https://ko-kr.facebook.com/" aria-label="페이스북">
              <img src="/images/facebook_icon.png" alt="Facebook">
            </a>
            <a href="https://x.com/?lang=ko" aria-label="X(트위터)">
              <img src="/images/twitter_icon.png" alt="Twitter">
            </a>
            <a href="https://www.instagram.com/" aria-label="인스타그램">
              <img src="/images/instagram_icon.png" alt="Instagram">
            </a>
          </div>
        </div>
      </div>
      <!-- 푸터 하단 정보 -->
      <div class="footer-bottom">
        &copy; 2024 먹코살코. All rights reserved.
      </div>
    </footer>
  </template>
  
  <script>
  export default {
    name: 'Footer'
  }
  </script>
  
  <style scoped>
  /* 필요한 스타일을 추가하세요 */
  
  .footer-section {
    flex: 1;
    margin: 0 10px;
  }
  
  .footer-section h4 {
    margin-bottom: 10px;
  }
  
  .footer-section ul {
    list-style: none;
    padding: 0;
  }
  
  .footer-section ul li {
    margin-bottom: 5px;
  }
  
  .social-icons a {
    margin-right: 10px;
  }
  
  </style>
  