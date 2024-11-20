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
        <!-- 검색 폼을 조건부로 렌더링 -->
        <form
          v-if="showSearchForm"
          @submit.prevent="handleSearch"
          class="search-form d-inline-block"
        >
          <div class="d-flex">
            <input
              type="text"
              v-model="searchQuery"
              class="form-control"
              placeholder="Search..."
              maxlength="20"
              @keyup.enter="handleSearch"
            />
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
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'Header',
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()
    const route = useRoute() // 현재 라우트 정보를 가져옵니다.
    const showUserMenu = ref(false)
    const searchQuery = ref('')

    const isLoggedIn = computed(() => !!authStore.accessToken)
    const userRoleId = computed(() => authStore.user?.roleId || null)

    // 현재 라우트가 /search가 아닌 경우에만 검색 폼을 표시합니다.
    const showSearchForm = computed(() => route.path !== '/search')

    const toggleUserMenu = () => {
      showUserMenu.value = !showUserMenu.value
    }

    const handleLogout = async () => {
      try {
        await authStore.logout()
        showUserMenu.value = false
        router.push('/home') // 로그아웃 후 홈으로 이동
      } catch (error) {
        console.error('로그아웃 실패:', error)
      }
    }

    const handleSearch = () => {
      if (searchQuery.value.trim() !== '') {
        router.push({ name: 'Search', query: { q: searchQuery.value.trim() } })
      }
    }

    return {
      showUserMenu,
      toggleUserMenu,
      isLoggedIn,
      userRoleId,
      handleLogout,
      searchQuery,
      handleSearch,
      showSearchForm // 템플릿에서 사용할 수 있도록 반환
    }
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */
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
  min-width: 170px;
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

.nav a:hover {
  color: #FF885B;
  transition: color 0.3s;
}
</style>
