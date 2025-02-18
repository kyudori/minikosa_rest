# 먹코살코 💥

<img src="https://github.com/sg8541/minikosa/blob/develop-rest/frontend/public/images/main_logo.png?raw=true" alt="표지" style="width:200px; height:auto;">


### Index

- [⚡️ 프로젝트 정보](#⚡️-프로젝트-정보)
- [🔥 작업 기간](#🔥-작업-기간)
- [📌 프로젝트 및 기능 소개](#📌-프로젝트-및-기능-소개)
- [🌳 프로젝트 목표](#🌳-프로젝트-목표)
- [✨️ DataBase Schema](#✨️-DataBase-Schema)
- [🏕️ 최종 아키텍처](#🏕️-최종-아키텍처)
- [🛠️ 기술 스택](#🛠️-기술-스택)
- [🦸🏻‍ 팀원 및 역할](#🦸🏻‍-팀원-및-역할)
- [📚 프로젝트 정보](#📚-프로젝트-정보)

<br/>

## ⚡️ 프로젝트 정보

- [한국소프트웨어산업협회(KOSA)](https://www.sw.or.kr/site/sw/edu/selectEduView.do?eduNo=1715) Full Stack 양성 과정 1~2차 미니 프로젝트
- 발표 자료: [다운로드](https://github.com/user-attachments/files/17963770/_.pdf)
- 노션: [링크](https://kyudori.notion.site/fffec16cd2d781dabd96e9297f752b38?v=fffec16cd2d7812fa482000c2c56e00f&pvs=4)

<br/>

## 🔥 작업 기간

- 1차 (기능 개발): 2024.10.14 - 2024.10.23
- 2차 (기술 전환): 2024.11.13 - 2024.11.21

<br/>

## 📌 프로젝트 및 기능 소개

> 3호선 경찰병원 역 근처 맛집 커뮤니티

맛집 정보를 공유하는 플랫폼, **먹코살코**입니다.

시연 영상: [YouTube](https://youtu.be/js_8dlwmXRs)

API Docs: [POSTMAN](https://documenter.getpostman.com/view/39478882/2sAY4ydLKy)

- 💬 맛집에 대한 정보를 얻을 수 있습니다.
- 💺 맛집에 대해 손님과 사장님 간 댓글 소통이 가능합니다.
- ✅ 고객(손님, 사장)에 피드백(회원 제안)을 받아 서비스를 개선합니다.

<br>

## ✨️ DataBase Schema
DataBase Schema: [DB diagram](https://dbdiagram.io/d/mini-DB-diagram-66f8fd5b3430cb846cf4b1fc)

<img src="https://github.com/user-attachments/assets/cf970d25-3129-4a9b-97e8-794691efb101" height=350 width=450>
<br/>

## 🏕️ 최종 아키텍처
<img src="https://github.com/user-attachments/assets/63d3cc0c-aab8-4556-ad3f-efc7c8ced845">
<br/>

## 🛠️ 기술 스택

#### Environment

<img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/VS%20Code-007ACC?style=flat&logo=visualstudiocode&logoColor=white">

#### Development 
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=Spring%20Boot&logoColor=white">
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat&logo=Javascript&logoColor=white"> <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=Bootstrap&logoColor=white">
##### 1차 (기능 개발)
<img src="https://img.shields.io/badge/MyBatis-4479A1?style=flat&logo=MyBatis&logoColor=white"> <img src="https://img.shields.io/badge/Thymeleaf-005F73?style=flat&logo=Thymeleaf&logoColor=white"> 

- Server Side MVC 
- 백엔드 : Spring MVC, MyBatis, 세션에 기반한 인증처리, Thymeleaf 
- 프론트엔드 : HTM5, CSS3, JavaScript, HttpServletRequest 기반의 AJAX
##### 2차 (기술 전환)
<img src="https://img.shields.io/badge/JPA-000000?style=flat&logo=Java%20Persistence%20API&logoColor=white"> <img src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat&logo=vue.js&logoColor=white">

- Client Side MVC 
- 백엔드 : Spring Rest, JPA, Spring Security 를 활용한 JWT 토큰 기반의 인증처리 
- 프론트엔드 : Vue 3(HTM5, CSS3, JavaScript), Axios 기반의 AJAX
#### DataBase

<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=Redis&logoColor=white">

#### Communication

 <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white">

<br/>

## 🦸🏻‍ 팀원 및 역할

| **김슬기** | **장현수** | **정지용** | **한규현** |
|:----------:|:----------:|:----------:|:----------:|
| [<img src="https://avatars.githubusercontent.com/u/138750403?v=4" height=200 width=200>](https://github.com/ksks1234) | [<img src="https://avatars.githubusercontent.com/u/182710725?v=4" height=200 width=200>](https://github.com/jhsnex123) | [<img src="https://avatars.githubusercontent.com/u/135500953?v=4" height=200 width=200>](https://github.com/RyanJeeyong) | [<img src="https://avatars.githubusercontent.com/u/57388014?v=4" height=200 width=200>](https://github.com/kyudori) |
| 메인 기능<br>가게 상세 기능<br>댓글, 대댓글 기능 | 팀장<br>내 정보 수정<br>Spring Security | 소개 페이지<br>댓글 금지어 | 데이터베이스 설계<br>관리자 기능<br>JWT & Redis|

<br>

## 📚 프로젝트 정보

#### 핵심 코드 분석
- [**controller**](https://kyudori.notion.site/Controller-147ec16cd2d780a99509cf5400864d67?pvs=4)
- [**entity**](https://kyudori.notion.site/Entity-147ec16cd2d78083bb55cc1d5b32e7c6?pvs=4)
- [**service**](https://kyudori.notion.site/Service-147ec16cd2d7805d883be230bc43d3b5?pvs=4)
- [**exception**](https://kyudori.notion.site/Exception-147ec16cd2d780bcaaa1e63c245028a4?pvs=4)
- [**repository**](https://kyudori.notion.site/Repository-147ec16cd2d780818bc9d472a76503f3?pvs=4)
- [**vue.js**](https://kyudori.notion.site/Vue-147ec16cd2d78022a041f3ead26c1885?pvs=4)

#### 커밋 컨벤션 

- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **docs**: 문서 수정
- **style**: 코드 포맷팅, 세미콜론 누락 등 코드 변경 없이 스타일 수정
- **refactor**: 코드 리팩토링 (기능 변경 없음)
- **test**: 테스트 코드 추가 또는 수정
- **chore**: 빌드 작업 또는 설정 변경
- **perf**: 성능 개선을 위한 코드 변경
- **ci**: CI 설정 파일 및 스크립트 변경

<br/>
