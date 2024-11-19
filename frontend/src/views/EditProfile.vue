<!-- src/views/EditProfile.vue -->
<template>
  <div class="main-content">
    <div class="subtitle">
      <h2>내 정보 수정</h2>
      <img src="/images/main_logo.png" alt="Main Logo" />
      <p class="description">필요한 정보를 수정해 주세요.</p>
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
        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          v-model="form.name"
          placeholder="이름을 입력하세요."
          required
        />
      </div>

      <div class="form-group">
        <label for="nickname">닉네임</label>
        <input
          type="text"
          id="nickname"
          v-model="form.nickname"
          placeholder="닉네임을 입력하세요."
          required
        />
        <button type="button" class="check-button" @click="checkNickname">중복 확인</button>
        <!-- 닉네임 중복 확인 피드백 메시지 -->
        <span :class="nicknameFeedbackClass">{{ nicknameFeedback }}</span>
      </div>

      <div class="form-group">
        <label for="email">이메일 (수정 불가)</label>
        <input type="email" id="email" v-model="form.email" readonly />
      </div>

      <div class="form-group">
        <label for="phoneNumber">연락처</label>
        <input
          type="tel"
          id="phoneNumber"
          v-model="form.phoneNumber"
          placeholder="연락처를 입력해 주세요."
          required
          pattern="^[0-9\-+() ]+$"
          title="유효한 연락처를 입력해 주세요."
        />
      </div>

      <!-- 현재 비밀번호 입력 필드 -->
      <div class="form-group">
        <label for="currentPassword">현재 비밀번호</label>
        <input
          type="password"
          id="currentPassword"
          v-model="form.currentPassword"
          placeholder="현재 비밀번호를 입력하세요."
          required
          @blur="verifyCurrentPassword"
        />
        <span v-if="passwordValid === false" class="feedback-message error">
          현재 비밀번호가 일치하지 않습니다.
        </span>
        <span v-if="passwordValid === true" class="feedback-message success">
          현재 비밀번호가 확인되었습니다.
        </span>
      </div>

      <!-- 비밀번호 변경 섹션 -->
      <div class="form-group">
        <button type="button" class="change-password-button" @click="togglePasswordChange">
          비밀번호 변경하기
        </button>
      </div>

      <div v-if="isPasswordChanging" class="form-group">
        <label for="password">새 비밀번호</label>
        <input
          type="password"
          id="password"
          v-model="form.password"
          placeholder="새 비밀번호를 입력하세요."
        />
      </div>

      <div v-if="isPasswordChanging" class="form-group">
        <label for="confirmPassword">비밀번호 확인</label>
        <input
          type="password"
          id="confirmPassword"
          v-model="form.confirmPassword"
          placeholder="비밀번호를 다시 한번 입력하세요."
        />
        <span v-if="passwordMismatch" class="feedback-message error">
          비밀번호가 일치하지 않습니다.
        </span>
      </div>

      <div class="form-group">
        <label>사용자 유형</label>
        <label>
          <input
            type="radio"
            name="user_type"
            value="사장님"
            :checked="form.roleId === 3"
            disabled
          />
          사장님
        </label>
        <label>
          <input
            type="radio"
            name="user_type"
            value="손님"
            :checked="form.roleId !== 3"
            disabled
          />
          손님
        </label>
      </div>

      <!-- 사장님일 경우 가게명 표시 -->
      <div v-if="form.roleId === 3" class="form-group">
        <label for="storeName">가게명</label>
        <input
          type="text"
          id="storeName"
          v-model="form.storeName"
          placeholder="가게명을 입력하세요."
          readonly
        />
      </div>

      <!-- 제출 버튼 -->
      <div class="button-container">
        <input type="submit" value="수정 완료" :disabled="isSubmitDisabled" />
        <button type="button" class="cancel-button" @click="cancel" :disabled="isLoading">
          취소
        </button>
      </div>

      <!-- 회원 탈퇴 링크 -->
      <div class="button-container">
        <a href="#" class="delete-link" @click.prevent="openDeleteModal">회원 탈퇴</a>
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
        정보 수정 중...
      </div>
    </form>

    <!-- 회원 탈퇴 모달 -->
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content">
        <h3>회원 탈퇴</h3>
        <p>정말로 회원을 탈퇴하시겠습니까? 탈퇴 후에는 모든 데이터가 삭제됩니다.</p>
        <form @submit.prevent="handleDelete">
          <div class="form-group">
            <label for="deletePassword">비밀번호 확인</label>
            <input
              type="password"
              id="deletePassword"
              v-model="deletePassword"
              placeholder="비밀번호를 입력하세요."
              required
            />
          </div>
          <div class="button-container">
            <input type="submit" value="탈퇴하기" :disabled="isDeleting" />
            <button type="button" class="cancel-button" @click="closeDeleteModal" :disabled="isDeleting">
              취소
            </button>
          </div>
          <div v-if="deleteErrorMessage" class="description error">
            {{ deleteErrorMessage }}
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import api from '../axios'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

export default {
  name: 'EditProfile',
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()

    const form = reactive({
      name: '',
      nickname: '',
      email: '',
      phoneNumber: '',
      password: '',
      confirmPassword: '',
      currentPassword: '',
      roleId: null,
      storeName: ''
    })

    const errorMessage = ref('')
    const successMessage = ref('')
    const isLoading = ref(false)

    const nicknameFeedback = ref('')
    const nicknameFeedbackClass = ref('')

    const isPasswordChanging = ref(false)
    const passwordValid = ref(null) // null: not checked, true: valid, false: invalid
    const passwordMismatch = ref(false)

    const deletePassword = ref('')
    const deleteErrorMessage = ref('')
    const isDeleting = ref(false)
    const showDeleteModal = ref(false)

    // 초기 사용자 정보 가져오기
    const fetchUserInfo = async () => {
      try {
        const response = await api.get('/userinfo')
        if (response.data) {
          form.name = response.data.name || ''
          form.nickname = response.data.nickname || ''
          form.email = response.data.email || ''
          form.phoneNumber = response.data.phoneNumber || ''
          form.roleId = response.data.roleId || null
          form.storeName = response.data.storeName || ''
        }
      } catch (error) {
        console.error('사용자 정보 가져오기 실패:', error)
        errorMessage.value = '사용자 정보를 가져오는 데 실패했습니다.'
      }
    }

    onMounted(() => {
      fetchUserInfo()
    })

    // 닉네임 중복 검사
    const checkNickname = async () => {
      const nickname = form.nickname.trim()
      if (nickname === '') {
        nicknameFeedback.value = '닉네임을 입력해 주세요.'
        nicknameFeedbackClass.value = 'error'
        return
      }

      try {
        const response = await api.get('/check-editnickname', {
          params: { nickname }
        })
        if (response.data.exists) {
          nicknameFeedback.value = '이미 사용 중인 닉네임입니다.'
          nicknameFeedbackClass.value = 'error'
        } else {
          nicknameFeedback.value = '사용 가능한 닉네임입니다.'
          nicknameFeedbackClass.value = 'success'
        }
      } catch (error) {
        console.error('닉네임 중복 검사 실패:', error)
        nicknameFeedback.value = '닉네임 중복 검사 중 오류가 발생했습니다.'
        nicknameFeedbackClass.value = 'error'
      }
    }

    // 비밀번호 변경 토글
    const togglePasswordChange = () => {
      isPasswordChanging.value = !isPasswordChanging.value
      if (!isPasswordChanging.value) {
        // 비밀번호 변경을 취소하면 관련 필드 초기화
        form.password = ''
        form.confirmPassword = ''
        passwordMismatch.value = false
      }
    }

    // 현재 비밀번호 확인
    const verifyCurrentPassword = async () => {
      const currentPassword = form.currentPassword.trim()
      if (currentPassword === '') {
        passwordValid.value = false
        return
      }

      try {
        const payload = {
          name: form.name.trim(),
          nickname: form.nickname.trim(),
          phoneNumber: form.phoneNumber.trim(),
          currentPassword: form.currentPassword.trim(),
          password: isPasswordChanging.value ? (form.password.trim() || null) : null
        }

        const response = await api.put('/editprofile', payload)
        // 현재 비밀번호가 유효하면
        passwordValid.value = true
        errorMessage.value = ''
        successMessage.value = '현재 비밀번호가 확인되었습니다.'
      } catch (error) {
        console.error('비밀번호 확인 실패:', error)
        passwordValid.value = false
        errorMessage.value = '현재 비밀번호가 일치하지 않습니다.'
      }
    }

    // 폼 제출 핸들러
    const handleSubmit = async () => {
      // 폼 유효성 검사
      if (
        !form.name.trim() ||
        !form.nickname.trim() ||
        !form.phoneNumber.trim()
      ) {
        errorMessage.value = '이름, 닉네임, 연락처를 모두 입력해 주세요.'
        successMessage.value = ''
        return
      }

      if (isPasswordChanging.value) {
        if (form.password !== form.confirmPassword) {
          passwordMismatch.value = true
          errorMessage.value = '비밀번호가 일치하지 않습니다.'
          successMessage.value = ''
          return
        } else {
          passwordMismatch.value = false
        }
      }

      if (isPasswordChanging.value && !passwordValid.value) {
        errorMessage.value = '현재 비밀번호가 일치하지 않습니다.'
        successMessage.value = ''
        return
      }

      isLoading.value = true
      errorMessage.value = ''
      successMessage.value = ''

      // 준비된 데이터 전송
      const payload = {
        name: form.name.trim(),
        nickname: form.nickname.trim(),
        phoneNumber: form.phoneNumber.trim(),
        currentPassword: form.currentPassword.trim(),
        password: isPasswordChanging.value ? (form.password.trim() || null) : null
      }

      try {
        const response = await api.put('/editprofile', payload)
        successMessage.value = '정보가 성공적으로 수정되었습니다.'
        // 업데이트된 사용자 정보를 스토어와 로컬 스토리지에 반영
        authStore.user = {
          ...authStore.user,
          name: response.data.name,
          nickname: response.data.nickname,
          phoneNumber: response.data.phoneNumber,
          storeName: response.data.storeName || ''
        }
        localStorage.setItem('user', JSON.stringify(authStore.user))

        // 비밀번호 변경 후 필드 초기화
        if (isPasswordChanging.value) {
          form.password = ''
          form.confirmPassword = ''
          isPasswordChanging.value = false
        }
      } catch (error) {
        console.error('정보 수정 실패:', error)
        if (error.response) {
          if (error.response.status === 401) {
            errorMessage.value = '인증이 필요합니다. 다시 로그인해주세요.'
            await authStore.logout()
            router.push('/login')
          } else if (error.response.status === 400) {
            errorMessage.value = error.response.data || '잘못된 요청입니다. 입력 값을 확인해주세요.'
          } else {
            errorMessage.value = error.response.data || '정보 수정 중 오류가 발생했습니다.'
          }
        } else {
          errorMessage.value = '정보 수정 중 오류가 발생했습니다.'
        }
      } finally {
        isLoading.value = false
      }
    }

    // 취소 버튼 핸들러
    const cancel = () => {
      router.go(-1)
    }

    // 회원 탈퇴 모달 열기
    const openDeleteModal = () => {
      showDeleteModal.value = true
      deletePassword.value = ''
      deleteErrorMessage.value = ''
    }

    // 회원 탈퇴 모달 닫기
    const closeDeleteModal = () => {
      showDeleteModal.value = false
      deletePassword.value = ''
      deleteErrorMessage.value = ''
    }

    // 회원 탈퇴 핸들러
    const handleDelete = async () => {
      if (deletePassword.value.trim() === '') {
        deleteErrorMessage.value = '비밀번호를 입력해 주세요.'
        return
      }

      isDeleting.value = true
      deleteErrorMessage.value = ''

      try {
        const response = await api.delete('/delete-account', {
          data: { currentPassword: deletePassword.value }
        })
        alert('회원 탈퇴가 성공적으로 완료되었습니다.')
        await authStore.logout()
        router.push('/home')
      } catch (error) {
        console.error('회원 탈퇴 실패:', error)
        if (error.response && error.response.data) {
          deleteErrorMessage.value = error.response.data
        } else {
          deleteErrorMessage.value = '회원 탈퇴 중 오류가 발생했습니다.'
        }
      } finally {
        isDeleting.value = false
      }
    }

    // "내 정보 수정" 버튼 비활성화 조건
    const isSubmitDisabled = computed(() => {
      if (isPasswordChanging.value) {
        if (!passwordValid.value) {
          return true
        }
        if (form.password || form.confirmPassword) {
          return form.password !== form.confirmPassword
        }
      }
      return false
    })

    return {
      form,
      errorMessage,
      successMessage,
      isLoading,
      checkNickname,
      nicknameFeedback,
      nicknameFeedbackClass,
      handleSubmit,
      cancel,
      isPasswordChanging,
      togglePasswordChange,
      passwordValid,
      verifyCurrentPassword,
      passwordMismatch,
      isSubmitDisabled,
      showDeleteModal,
      deletePassword,
      deleteErrorMessage,
      isDeleting,
      openDeleteModal,
      closeDeleteModal,
      handleDelete
    }
  }
}
</script>

<style scoped>
/* 메인 콘텐츠 스타일 */
.main-content {
  width: 800px;
  margin: 75px auto 100px auto; /* 헤더와 본문 간격, 본문과 푸터 간격 */
  background-color: #fff;
  padding: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
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

.description {
  text-align: center;
  font-size: 15px;
  color: #666;
  margin-top: 20px;
  margin-bottom: 20px;
}

.error {
  color: red;
}

.success {
  color: green;
}

/* 폼 요소 스타일 */
.form-group {
  margin-bottom: 20px;
  position: relative;
}

.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.form-group input[type="text"],
.form-group input[type="password"],
.form-group input[type="email"],
.form-group input[type="tel"] {
  width: 100%;
  padding: 12px 15px;
  border: 1.5px solid #ff885b;
  border-radius: 5px;
  font-size: 16px;
  resize: none;
  box-sizing: border-box;
  font-family: "맑은 고딕", sans-serif;
}

/* 중복 체크 버튼 스타일 */
.form-group .check-button {
  position: absolute;
  right: 10px;
  top: 43px;
  padding: 6px 12px;
  background-color: #ff885b;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 12px;
  transition: background-color 0.3s;
}

.form-group .check-button:hover {
  background-color: #e07a4d;
}

/* 비밀번호 변경 버튼 스타일 */
.change-password-button {
  background-color: #ff885b; /* 주황색 버튼 */
  color: #fff;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.change-password-button:hover {
  background-color: #e07a4d;
}

/* 플레이스홀더 스타일 */
input::placeholder {
  white-space: pre-line;
  color: #999;
}

/* 버튼 컨테이너 스타일 */
.button-container {
  text-align: center;
  margin-top: 30px;
}

/* 버튼 스타일 */
.button-container input[type="submit"],
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

.button-container input[type="submit"]:hover,
.button-container .cancel-button:hover {
  background-color: #e07a4d;
}

/* 회원 탈퇴 링크 스타일 */
.button-container .delete-link {
  color: #666;
  text-decoration: underline;
  cursor: pointer;
  font-size: 14px;
}

.button-container .delete-link:hover {
  color: #333;
}

/* 폼 하단 추가 링크 스타일 */
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

/* 피드백 메시지 스타일 */
.feedback-message {
  display: block;
  margin-top: 5px;
  font-size: 12px;
}

.feedback-message.success {
  color: green;
}

.feedback-message.error {
  color: red;
}

/* 로딩 인디케이터 스타일 */
.loading-indicator {
  text-align: center;
  margin-top: 20px;
  font-size: 16px;
  color: #666;
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #ff885b;
  text-align: center;
}

.modal-content p {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.modal-content .form-group {
  margin-bottom: 20px;
}

.modal-content .button-container input[type="submit"],
.modal-content .button-container .cancel-button {
  background-color: #ff885b;
  color: #fff;
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin: 0 10px;
}

.modal-content .button-container .cancel-button {
  background-color: #ccc;
  color: #333;
}

.modal-content .button-container input[type="submit"]:hover,
.modal-content .button-container .cancel-button:hover {
  background-color: #e07a4d;
}

.modal-content .description.error {
  color: red;
  text-align: center;
  margin-top: 10px;
}
</style>
