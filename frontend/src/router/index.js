// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import Intro from '../views/Intro.vue'
import Suggestion from '../views/Suggestion.vue'
import FindEmail from '../views/FindEmail.vue'
import ResetPassword from '../views/ResetPassword.vue'
// 다른 뷰를 추가로 임포트

import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/intro',
    name: 'Intro',
    component: Intro,
    meta: { requiresAuth: true }
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
    path: '/',
    name: 'Home',
    redirect: '/login'
  },
  // 추가적인 라우트 설정
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
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
