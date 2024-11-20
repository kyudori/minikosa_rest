// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import AccessDenied from '../views/AccessDenied.vue'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import Intro from '../views/Intro.vue'
import Suggestion from '../views/Suggestion.vue'
import FindEmail from '../views/FindEmail.vue'
import ResetPassword from '../views/ResetPassword.vue'
import EditProfile from '../views/EditProfile.vue'
import StoreContent from '../views/StoreContent.vue'
import AdminSuggestionList from '../views/admin/AdminSuggestionList.vue'
import AdminSuggestionView from '../views/admin/AdminSuggestionView.vue'
import RegisterOwner from '../views/admin/RegisterOwner.vue'
import Search from '../views/Search.vue'
import Home from '../views/Home.vue'
import StoreRegister from '../views/admin/StoreRegister.vue'
import StoreModify from '../views/admin/StoreModify.vue'

// 다른 뷰를 추가로 임포트

import { useAuthStore } from '../stores/auth'

const routes = [
  // **루트 경로를 /home으로 리디렉션하는 라우트 추가**
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/error',
    name: 'Error',
    component: AccessDenied
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/store/:id', 
    name: 'StoreContent',
    component: StoreContent,
    meta: { requiresAuth: true },
    props: true
  },
  {
    path: '/intro',
    name: 'Intro',
    component: Intro,
  },
  {
    path: '/findemail',
    name: 'FindEmail',
    component: FindEmail
  },
  {
    path: '/reset/password',
    name: 'ResetPassword',
    component: ResetPassword
  },
  {
    path: '/suggestion',
    name: 'Suggestion',
    component: Suggestion,
    meta: { requiresAuth: true }
  },
  {
    path: '/editprofile',
    name: 'EditProfile',
    component: EditProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/search',
    name: 'Search',
    component: Search
  },
  // Admin Routes
  {
    path: '/admin/suggestion/list',
    name: 'AdminSuggestionList',
    component: AdminSuggestionList,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/suggestion/:id',
    name: 'AdminSuggestionView',
    component: AdminSuggestionView,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/admin/register/owner',
    name: 'RegisterOwner',
    component: RegisterOwner,
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/admin/create/store',
    name: 'StoreRegister',
    component: StoreRegister,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/store/:id/modify',
    name: 'StoreModify',
    component: StoreModify,
    meta: { requiresAuth: true, requiresAdmin: true },
    props: true
  },
  // 기타 라우트
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  // 모든 비정의된 경로를 /error로 리디렉션**
  {
    path: '/:pathMatch(.*)*',
    redirect: '/error'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 라우터 가드 설정
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!authStore.accessToken) {
      next({ name: 'Login' })
    } else if (to.matched.some(record => record.meta.requiresAdmin) && authStore.user.roleId !== 2) {
      // 관리자 권한 확인 (roleId === 2이 ADMIN)
      next({ name: 'Intro' })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
