<!-- src/views/Suggestion.vue -->
<template>
  <div class="main-content">
    <div class="subtitle">
      <h2>회원 제안</h2>
      <img src="/images/main_logo.png" alt="Main Logo" />
      <p class="description">서비스를 위해 제안을 보내주세요.</p>
    </div>

    <!-- 오류 메시지 표시 -->
    <div v-if="errorMessage" class="description error">
      {{ errorMessage }}
    </div>
    <!-- 성공 메시지 표시 -->
    <div v-if="successMessage" class="description success">
      {{ successMessage }}
    </div>

    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="title">제목</label>
        <input
          type="text"
          id="title"
          v-model="form.title"
          placeholder="장소 제안 / 장소 수정 / 사장님 등록 요청 등"
          required
        />
      </div>
      <div class="form-group">
        <label for="storeName">가게 명</label>
        <input
          type="text"
          id="storeName"
          v-model="form.storeName"
          placeholder="가게 명을 입력해 주세요."
          required
        />
      </div>
      <div class="form-group">
        <label for="content">내용</label>
        <textarea
          id="content"
          v-model="form.content"
          placeholder="제안 내용:
(사장님 등록 요청의 경우 이메일을 정확하게 입력해 주세요.)"
          required
        ></textarea>
      </div>
      <div class="button-container">
        <input type="submit" value="등록" :disabled="isLoading" />
        <button type="button" class="cancel-button" @click="cancel" :disabled="isLoading">취소</button>
      </div>
      <!-- 버튼 아래에 추가된 링크 -->
      <p class="form-footer">
        로그인에 문제가 있나요?<br />
        <router-link to="/signup">회원가입</router-link>
        <router-link to="/findemail">ID 찾기</router-link>
        <router-link to="/reset/password">PW 초기화</router-link>
      </p>
      <!-- 로딩 인디케이터 추가 -->
      <div v-if="isLoading" class="loading-indicator">
        제안 등록 중...
      </div>
    </form>
  </div>
</template>

<script>
import api from '../axios' // 중앙집중식 Axios 인스턴스 임포트
import { useAuthStore } from '../stores/auth'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'Suggestion',
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()

    const form = ref({
      title: '',
      storeName: '',
      content: '',
    })

    const errorMessage = ref('')
    const successMessage = ref('')
    const isLoading = ref(false)

    const handleSubmit = async () => {
      // 폼 유효성 검사
      if (
        !form.value.title.trim() ||
        !form.value.storeName.trim() ||
        !form.value.content.trim()
      ) {
        errorMessage.value = '제목, 가게명, 내용을 모두 입력해 주세요.'
        successMessage.value = ''
        return
      }

      isLoading.value = true
      try {
        // 제안 데이터 전송
        const response = await api.post('/suggestion', form.value)

        // 성공 응답 처리
        successMessage.value = '제안이 성공적으로 등록되었습니다.'
        errorMessage.value = ''
        resetForm()
      } catch (error) {
        // 오류 응답 처리
        if (error.response) {
          if (error.response.status === 401) {
            errorMessage.value = '인증이 필요합니다. 다시 로그인해주세요.'
            await authStore.logout()
            router.push('/login')
          } else {
            errorMessage.value = error.response.data.message || '제안 등록 중 오류가 발생했습니다.'
          }
        } else {
          errorMessage.value = '제안 등록 중 오류가 발생했습니다.'
        }
        successMessage.value = ''
      } finally {
        isLoading.value = false
      }
    }

    const cancel = () => {
      // 취소 버튼 클릭 시 이전 페이지로 이동
      router.go(-1)
    }

    const resetForm = () => {
      form.value.title = ''
      form.value.storeName = ''
      form.value.content = ''
    }

    return {
      form,
      errorMessage,
      successMessage,
      isLoading,
      handleSubmit,
      cancel,
    }
  }
}
</script>

<style scoped>
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
  color: #ff885b;
  margin-bottom: 20px;
  text-align: center;
}

.main-content img {
  display: block;
  margin: 0 auto 20px auto; /* 가운데 정렬 및 하단 여백 추가 */
  width: 150px; /* 원하는 크기로 조정 */
  height: auto;
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

.description {
  font-size: 15px;
  color: #666;
  margin-top: 20px;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.form-group input[type='text'],
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1.5px solid #ff885b;
  border-radius: 5px;
  font-size: 16px;
  resize: none;
  box-sizing: border-box;
  font-family: '맑은 고딕', sans-serif;
}

.form-group textarea {
  height: 200px;
  line-height: 1.5;
}

textarea::placeholder {
  white-space: pre-line;
  color: #999;
}

.button-container {
  text-align: center;
  margin-top: 30px;
}

.button-container input[type='submit'],
.button-container .cancel-button {
  background-color: #ff885b;
  color: #fff;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin: 0 10px;
}

.button-container .cancel-button {
  background-color: #ccc;
  color: #333;
}

.button-container input[type='submit']:hover {
  background-color: #e07a4d;
}

.button-container .cancel-button:hover {
  background-color: #999;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.form-footer a {
  color: #ff885b;
  text-decoration: none;
  margin: 0 10px;
  transition: color 0.3s;
}

.form-footer a:hover {
  color: #e07a4d;
}

.error {
  color: red;
}

.success {
  color: green;
}

.loading-indicator {
  text-align: center;
  margin-top: 20px;
  font-size: 16px;
  color: #666;
}
</style>
