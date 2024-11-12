// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import Intro from '../views/Intro.vue'
import Suggestion from '../views/Suggestion.vue'
import FindEmail from '../views/FindEmail.vue'
import ResetPassword from '../views/ResetPassword.vue'
// 다른 뷰를 추가로 임포트

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
    component: Intro
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
    component: Suggestion
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

export default router
