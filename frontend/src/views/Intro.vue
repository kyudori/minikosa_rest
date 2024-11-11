<!-- src/views/Intro.vue -->
<template>
  <div class="main-content">
    <div class="tab-buttons">
      <button :class="{ active: activeTab === 'project' }" @click="activeTab = 'project'">프로젝트 소개</button>
      <button :class="{ active: activeTab === 'team' }" @click="activeTab = 'team'">팀 소개</button>
    </div>

    <!-- 프로젝트 소개 섹션 -->
    <div v-show="activeTab === 'project'" class="tab-content active">
      <div class="subtitle">
        <h2>프로젝트 소개</h2>
        <img src="/images/main_logo.png" alt="Main Logo">
        <p class="description">먹코살코는 경찰병원역 3호선 맛집을 소개하는 맛집 커뮤니티 입니다.</p>
      </div>
      <div class="about_box">
        <ul class="project-list">
          <li>
            <img src="/images/police_station.png" alt="경찰병원역 3호선 이미지" class="police-station-photo">
            <img src="/images/KOSA.jpg" alt="KOSA 로고" class="kosa-photo">
            <p class="txt">
              경찰병원역과 KOSA 근처의 맛집을
            </p>
            <p class="txt">
              자유롭게 소개하고 공유할 수 있습니다.
            </p>
            <div class="features">
              <h2>주요 기능</h2>
              <p class="txt">
                1. 맛집 정보(리뷰) 제공
              </p>
              <p class="txt">
                2. 맛집 리뷰(평점) 작성
              </p>
              <p class="txt">
                3. 사장님 손님 소통
              </p>
            </div>
            <p class="txt">
              프로젝트 저장소: <a href="https://github.com/sg8541/minikosa" target="_blank">github.com/sg8541/minikosa</a>
            </p>
          </li>
        </ul>
      </div>
    </div>

    <!-- 팀 소개 섹션 -->
    <div v-show="activeTab === 'team'" class="tab-content active">
      <div class="subtitle">
        <h2>팀 소개</h2>
        <img src="/images/main_logo.png" alt="Main Logo">
        <p class="description">먹 코 살 코 팀원을 소개합니다.</p>
      </div>
      <div class="about_box">
        <ul class="team-list">
          <li>
            <img src="/images/kim.png" alt="김슬기" class="team-photo">
            <h4>김슬기</h4>
            <p class="txt">
              귀요미<br>
              역할: 백엔드<br>
              이메일: <a href="mailto:logging2021@gmail.com">logging2021@gmail.com</a><br>
              깃허브: <a href="https://github.com/sg8541">github.com/sg8541</a>
            </p>
          </li>
          <li>
            <img src="/images/jang.jpg" alt="장현수" class="team-photo">
            <h4>장현수</h4>
            <p class="txt">
              코딩하는 또라이<br>
              역할: 팀장, 백엔드<br>
              이메일: <a href="mailto:jhsnex3768@gmail.com">jhsnex3768@gmail.com</a><br>
              깃허브: <a href="https://github.com/jhsnex123">github.com/jhsnex123</a>
            </p>
          </li>
          <li>
            <img src="/images/jung.jpg" alt="정지용" class="team-photo">
            <h4>정지용</h4>
            <p class="txt">
              깜찍이<br>
              역할: 프론트엔드<br>
              이메일: <a href="mailto:rjchung47@gmail.com">rjchung47@gmail.com</a><br>
              깃허브: <a href="https://github.com/RyanJeeyong">github.com/RyanJeeyong</a>
            </p>
          </li>
          <li>
            <img src="/images/han.jpg" alt="한규현" class="team-photo">
            <h4>한규현</h4>
            <p class="txt">
              불타는 고구마<br>
              역할: 백엔드<br>
              이메일: <a href="mailto:khh5345@naver.com">khh5345@naver.com</a><br>
              깃허브: <a href="https://github.com/kyudori">github.com/kyudori</a>
            </p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Intro',
  data() {
    return {
      activeTab: 'project', // 기본 활성 탭 설정
    };
  },
};
</script>

<style scoped>
/* 기존 스타일을 컴포넌트에 맞게 조정 */
.main-content {
  max-width: 1000px;
  margin: 75px auto;
  background-color: #fff;
  padding: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  margin-bottom: 100px;
}

.tab-buttons {
  width: 40%;
  display: flex;
  justify-content: center;
  margin-bottom: 50px;
  border: 1px solid #ff885b;
  border-radius: 5px;
  overflow: hidden;
  margin: 0 auto 30px auto;
}

.tab-buttons button {
  padding: 10px 20px;
  border: none;
  background-color: #fff;
  color: #ff885b;
  font-size: 16px;
  cursor: pointer;
  flex: 1;
  transition: background-color 0.3s, color 0.3s;
}

.tab-buttons button.active {
  background-color: #ff885b;
  color: #fff;
}

.tab-buttons button:hover {
  background-color: #e07a4d;
  color: #fff;
}

.tab-content {
  display: none;
}

.tab-content.active {
  display: block;
}

.subtitle {
  text-align: center;
  margin-bottom: 40px;
}

.subtitle h2 {
  font-size: 28px;
  color: #ff885b;
  margin-bottom: 20px;
}

.subtitle img {
  display: block;
  margin: 0 auto 20px auto;
  width: 150px;
  height: auto;
}

.description {
  font-size: 15px;
  color: #666;
  margin-top: 20px;
  margin-bottom: 20px;
}

.team-list,
.project-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.team-list li,
.project-list li {
  list-style: none;
  text-align: center;
  margin: 20px;
  flex: 1 1 45%;
  max-width: 45%;
}

.team-photo,
.police-station-photo,
.kosa-photo {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.kosa-photo {
  width: 220px;
}

.project-image {
  border-radius: 8px;
}

.team-list h4,
.project-list h4 {
  font-size: 22px;
  margin-bottom: 10px;
  color: #333;
}

.txt {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  margin-top: 10px;
}

.txt a {
  color: #ff885b;
  text-decoration: none;
  transition: color 0.3s;
}

.txt a:hover {
  color: #e07a4d;
}

.features {
  margin-top: 30px;
  margin-bottom: 15px;
}

.social-icons a img {
  width: 24px;
  height: 24px;
  filter: invert(100%);
  transition: filter 0.3s;
}

.social-icons a:hover img {
  filter: invert(75%) sepia(100%) saturate(500%) hue-rotate(300deg);
}

footer {
  background-color: #ff885b;
  color: #fff;
  padding: 20px 10px;
  margin-top: 40px;
}

.footer-container {
  max-width: 1500px;
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 100px;
  padding: 10px 0px;
}

.footer-section {
  flex: 1 1 200px;
}

.footer-section h4 {
  margin-bottom: 15px;
  font-size: 18px;
  border-bottom: 2px solid #fff;
  display: inline-block;
  padding-bottom: 5px;
}

.footer-section ul {
  list-style: none;
}

.footer-section ul li {
  margin-bottom: 10px;
}

.footer-section ul li a {
  color: #fff;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section ul li a:hover {
  color: #e07a4d;
}

.social-icons {
  display: flex;
  gap: 10px;
}

.social-icons a img {
  width: 24px;
  height: 24px;
  filter: invert(100%);
  transition: filter 0.3s;
}

.social-icons a:hover img {
  filter: invert(75%) sepia(100%) saturate(500%) hue-rotate(300deg);
}

.footer-bottom {
  text-align: center;
  font-size: 14px;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  padding-top: 10px;
}

@media (max-width: 768px) {
  .header .container-inner {
    flex-direction: column;
    align-items: center;
  }

  .nav {
    flex-direction: column;
    gap: 10px;
    margin-top: 10px;
  }

  .footer-container {
    flex-direction: column;
    align-items: center;
  }

  .footer-section {
    flex: 1 1 100%;
    text-align: center;
  }

  .social-icons {
    justify-content: center;
  }

  .team-list li,
  .project-list li {
    flex: 1 1 100%;
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .header .logo h1 {
    font-size: 20px;
  }

  .nav a {
    font-size: 14px;
  }

  .team-list h4,
  .project-list h4 {
    font-size: 18px;
  }

  .txt {
    font-size: 14px;
  }
}
</style>
