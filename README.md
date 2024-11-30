# 먹코살코 💥

<img src="https://github.com/sg8541/minikosa/blob/develop-rest/frontend/public/images/main_logo.png?raw=true" alt="표지" style="width:200px; height:auto;">


### Index

- [⚡️ 프로젝트 정보](#⚡️-프로젝트-정보)
- [🔥 작업 기간](#🔥-작업-기간)
- [📌 프로젝트 및 기능 소개](#📌-프로젝트-및-기능-소개)
- [🌳 프로젝트 목표](#🌳-프로젝트-목표)
- [🏕️ 아키텍처](#🏕️-아키텍처)
- [🛠️ 기술 스택](#🛠️-기술-스택)
- [🦸🏻‍ 팀원 및 역할](#🦸🏻‍-팀원-및-역할)
- [📚 프로젝트 정보](#📚-프로젝트-정보)

<br/>

## ⚡️ 프로젝트 정보

- [한국소프트웨어산업협회(KOSA)](https://www.sw.or.kr/site/sw/edu/selectEduView.do?eduNo=1715) Full Stack 양성 과정 1~2차 미니 프로젝트

<br/>

## 🔥 작업 기간

- 1차: 2024.10.14 - 2024.10.23
- 2차: 2024.11.13 - 2024.11.21

<br/>

## 📌 프로젝트 및 기능 소개

> 3호선 경찰병원 역 근처 맛집 커뮤니티

맛집 정보를 공유하는 플랫폼, **먹코살코**입니다.

- 💬 맛집에 대한 정보를 얻을 수 있습니다.
- 💺 맛집에 대해 손님과 사장님 간 댓글 소통이 가능합니다.
- ✅ 고객(손님, 사장)에 피드백(회원 제안)을 받아 서비스를 개선합니다.

<br>

## 🏕️ 아키텍처
추가예정입니다.

<br/>

## 🛠️ 기술 스택

#### Environment

<img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/VS%20Code-007ACC?style=flat&logo=visualstudiocode&logoColor=white">

#### Development 
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=Spring%20Boot&logoColor=white">
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat&logo=Javascript&logoColor=white"> <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=Bootstrap&logoColor=white">
##### 1차
<img src="https://img.shields.io/badge/MyBatis-4479A1?style=flat&logo=MyBatis&logoColor=white"> <img src="https://img.shields.io/badge/Thymeleaf-005F73?style=flat&logo=Thymeleaf&logoColor=white"> 
##### 2차
<img src="https://img.shields.io/badge/JPA-000000?style=flat&logo=Java%20Persistence%20API&logoColor=white"> 
<img src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat&logo=vue.js&logoColor=white">

#### DataBase

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white">

#### Communication

 <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white">

<br/>

## 🦸🏻‍ 팀원 및 역할

| **김슬기** | **장현수** | **정지용** | **한규현** |
|:----------:|:----------:|:----------:|:----------:|
| [<img src="https://avatars.githubusercontent.com/u/138750403?v=4" height=150 width=250>](https://github.com/ksks1234) | [<img src="https://avatars.githubusercontent.com/u/182710725?v=4" height=150 width=200>](https://github.com/jhsnex123) | [<img src="https://avatars.githubusercontent.com/u/135500953?v=4" height=150 width=250>](https://github.com/RyanJeeyong) | [<img src="https://avatars.githubusercontent.com/u/57388014?v=4" height=150 width=250>](https://github.com/kyudori) |
| 메인 기능<br>가게 상세 기능<br>댓글, 대댓글 기능 | 팀장<br>내 정보 수정<br>Spring Security | 소개 페이지<br>댓글 금지어 | 데이터베이스 설계<br>관리자 기능<br>JWT & Redis|

<br>

## 📚 프로젝트 정보

<details>
<summary>커밋 컨벤션</summary>

- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **docs**: 문서 수정
- **style**: 코드 포맷팅, 세미콜론 누락 등 코드 변경 없이 스타일 수정
- **refactor**: 코드 리팩토링 (기능 변경 없음)
- **test**: 테스트 코드 추가 또는 수정
- **chore**: 빌드 작업 또는 설정 변경
- **perf**: 성능 개선을 위한 코드 변경
- **ci**: CI 설정 파일 및 스크립트 변경

</details>

<details>
<summary>파일트리</summary>

```
📦 
minikosa/
├── frontend/
│   ├── public/
│   │   ├── images/
│   │   │   └── main_logo.png
│   │   └── index.html
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.js
│   ├── .gitignore
│   ├── babel.config.js
│   ├── package.json
│   └── README.md
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── minikosa/
│   │   │               ├── controller/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── MinikosaApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── minikosa/
│                       └── MinikosaApplicationTests.java
├── .gitignore
├── README.md
├── build.gradle
├── gradlew
├── gradlew.bat
└── settings.gradle
```

</details>


<br/>
