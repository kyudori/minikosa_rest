<!-- src/components/Header.vue -->
<template>
  <header class="header">
    <div class="container-inner">
      <div class="logo">
        <router-link to="/home">
          <img src="/images/main_logo.png" alt="Main Logo">
        </router-link>
      </div>
      <nav class="nav" id="mainNav">
        <form method="get" action="/search" class="search-form d-inline-block">
          <div class="d-flex">
            <input type="text" name="q" class="form-control" placeholder="Search..." maxlength="20">
            <button type="submit" class="btn btn-search">
              <img width="32" height="32" src="/images/icon_search2.png" alt="검색하기" />
              <span class="icon-search"></span>
            </button>
          </div>
        </form>
        <router-link to="/home">홈</router-link>
        <router-link to="/intro">소개</router-link>
        <router-link to="/suggestion">문의</router-link>
        <!-- 드롭다운 메뉴 추가 -->
        <div class="dropdown">
          <button id="userMenuButton" @click="toggleUserMenu">▼</button>
          <div class="dropdown-content" v-if="showUserMenu">
            <template v-if="!isLoggedIn">
              <router-link to="/login" id="loginLink">로그인</router-link>
            </template>
            <template v-else>
              <template v-if="userRoleId === 2">
                <router-link to="/admin/create/store" id="createLink">가게 등록</router-link>
                <router-link to="/admin/register/owner" id="registerLink">사장 등록</router-link>
                <router-link to="/admin/suggestion/list" id="suggestionLink">고객 요청 사항</router-link>
                <router-link to="/editprofile" id="profileLink">내 정보 페이지</router-link>
                <a href="#" @click.prevent="handleLogout" id="logoutLink">로그아웃</a>
              </template>
              <template v-else>
                <router-link to="/editprofile" id="profileLink">내 정보 페이지</router-link>
                <a href="#" @click.prevent="handleLogout" id="logoutLink">로그아웃</a>
              </template>
            </template>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script>
import { ref, computed } from 'vue'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'Header',
  setup() {
    const authStore = useAuthStore()
    const showUserMenu = ref(false)

    const isLoggedIn = computed(() => !!authStore.accessToken)
    const userRoleId = computed(() => authStore.user?.roleId || null)

    const toggleUserMenu = () => {
      showUserMenu.value = !showUserMenu.value
    }

    const handleLogout = async () => {
      try {
        await authStore.logout()
        showUserMenu.value = false
      } catch (error) {
        console.error('로그아웃 실패:', error)
      }
    }

    return {
      showUserMenu,
      toggleUserMenu,
      isLoggedIn,
      userRoleId,
      handleLogout
    }
  }
}
</script>

<style scoped>
/* 필요한 스타일을 추가하세요 */
.header {
  background-color: #fff;
  border-bottom: 1px solid #ddd;
  padding: 10px 20px;
}

.container-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo img {
  height: 50px;
}

.nav {
  display: flex;
  align-items: center;
}

.nav a {
  margin: 0 10px;
  text-decoration: none;
  color: #333;
}

.search-form {
  margin-right: 20px;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  position: absolute;
  right: 0;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown-content a {
  display: block;
  padding: 12px 16px;
  text-decoration: none;
  color: black;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}

.btn-search {
  background: none;
  border: none;
  cursor: pointer;
}
</style>
