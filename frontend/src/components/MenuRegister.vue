<!-- src/components/MenuRegister.vue -->
<template>
  <div>
    <label>메뉴</label>
    <div v-for="(menu, index) in menus" :key="menu.menuId || index" class="menu-section">
      <button type="button" class="remove-menu" @click="removeMenu(index)">×</button>
      <div class="form-group">
        <label>메뉴 이름</label>
        <input type="text" v-model="menu.menuName" placeholder="메뉴 이름을 입력해 주세요." required>
      </div>
      <div class="form-group">
        <label>가격</label>
        <input type="number" v-model="menu.price" placeholder="가격을 입력해 주세요." required>
      </div>
      <div class="form-group">
        <label>메뉴 사진</label>
        <button type="button" class="custom-file-button" @click="triggerMenuPhoto(index)">메뉴 사진 선택</button>
        <span class="file-name">{{ menu.menuPhotoName || getFileName(menu.menuPhotoPreview) || '선택된 파일 없음' }}</span>
        <input
          type="file"
          :ref="el => menuPhotoInputs[index] = el"
          @change="handleMenuPhoto(index, $event)"
          accept="image/*"
          class="hidden-file-input"
        >
        <img
          v-if="menu.menuPhotoPreview"
          :src="getImageUrl(menu.menuPhotoPreview)"
          alt="메뉴 사진 미리보기"
          class="menu-photo-preview"
          @error="onImageError($event)"
        >
      </div>
    </div>
    <button type="button" class="add-menu-button" @click="addMenu">+ 메뉴 추가</button>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'MenuRegister',
  props: {
    modelValue: {
      type: Array,
      default: () => []
    }
  },
  emits: ['update:modelValue', 'deleteMenu'], // 'deleteMenu' 이벤트 추가
  setup(props, { emit }) {
    const menus = ref([])

    // props.modelValue 변경 감지 및 menus 업데이트
    watch(
      () => props.modelValue,
      (newVal) => {
        menus.value = newVal.map(menu => ({
          ...menu,
          menuPhotoName: menu.menuPhotoName || '',
          menuPhotoPreview: menu.menuPhotoPreview || menu.menuPhoto || ''
        }))
      },
      { immediate: true, deep: true }
    )

    const menuPhotoInputs = ref([])

    const triggerMenuPhoto = (index) => {
      const input = menuPhotoInputs.value[index]
      if (input) {
        input.click()
      } else {
        console.error(`menuPhotoInput${index}가 정의되지 않았습니다.`)
      }
    }

    const handleMenuPhoto = (index, event) => {
      const file = event.target.files[0]
      if (file) {
        menus.value[index].menuPhoto = file
        menus.value[index].menuPhotoName = file.name
        const reader = new FileReader()
        reader.onload = (e) => {
          menus.value[index].menuPhotoPreview = e.target.result
        }
        reader.readAsDataURL(file)
      } else {
        menus.value[index].menuPhoto = null
        menus.value[index].menuPhotoName = ''
        menus.value[index].menuPhotoPreview = ''
      }
    }

    const addMenu = () => {
      menus.value.push({
        menuId: null,
        menuName: '',
        price: '',
        menuPhoto: null,
        menuPhotoName: '',
        menuPhotoPreview: ''
      })
    }

    const removeMenu = (index) => {
      const menu = menus.value[index]
      if (menu.menuId) {
        // 기존 메뉴인 경우 부모 컴포넌트로 이벤트 전달
        emit('deleteMenu', menu.menuId, index)
      } else {
        // 신규 추가된 메뉴는 로컬에서만 제거
        menus.value.splice(index, 1)
        menuPhotoInputs.value.splice(index, 1)
        alert('메뉴가 삭제되었습니다.')
      }
    }

    // menus 변경 시 부모 컴포넌트로 emit
    watch(
      menus,
      (newMenus) => {
        emit('update:modelValue', newMenus)
      },
      { deep: true }
    )

    // getImageUrl 함수 추가
    const getImageUrl = (path) => {
      if (path.startsWith('data:')) {
        // Base64 데이터 URI인 경우 그대로 반환
        return path
      } else {
        const serverHost = 'http://localhost:8090' // 백엔드 서버 주소로 변경 필요
        return serverHost + path
      }
    }

    // 파일 이름 추출 함수 추가
    const getFileName = (path) => {
      if (path) {
        return path.split('/').pop()
      }
      return ''
    }

    // 이미지 로드 실패 시 대체 이미지 설정
    const onImageError = (event) => {
      event.target.onerror = null
      event.target.src = '/images/main_logo.png'
    }

    return {
      menus,
      menuPhotoInputs,
      triggerMenuPhoto,
      handleMenuPhoto,
      addMenu,
      removeMenu,
      getImageUrl,
      getFileName,
      onImageError
    }
  }
}
</script>

<style scoped>
/* 메뉴 섹션 스타일 */
.menu-section {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
  position: relative;
}

/* 삭제 버튼 스타일 */
.remove-menu {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  color: #ff4d4d;
  font-size: 18px;
  cursor: pointer;
}

/* 라벨 스타일 */
.form-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #FF885B;
}

/* 입력 필드 스타일 */
.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1.5px solid #FF885B;
  border-radius: 5px;
  font-size: 16px;
  box-sizing: border-box;
  font-family: "맑은 고딕", sans-serif;
}

/* 숨겨진 파일 입력 필드 */
.hidden-file-input {
  display: none;
}

/* 커스텀 파일 버튼 */
.custom-file-button {
  display: inline-block;
  background-color: #FF885B;
  color: #fff;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
  text-align: center;
  text-decoration: none;
}

.custom-file-button:hover {
  background-color: #e07a4d;
}

/* 파일 이름 표시 */
.file-name {
  display: inline-block;
  margin-left: 10px;
  font-size: 14px;
  color: #333;
  vertical-align: middle;
}

/* 메뉴 사진 미리보기 */
.menu-photo-preview {
  display: block;
  width: 150px;
  height: auto;
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

/* 메뉴 추가 버튼 */
.add-menu-button {
  display: inline-block;
  background-color: #FF885B;
  color: #fff;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 10px;
}

.add-menu-button:hover {
  background-color: #e07a4d;
}
</style>
