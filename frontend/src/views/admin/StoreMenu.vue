<template>
    <div class="main-content">
      <h2>메뉴 관리</h2>
      <img src="/images/main_logo.png" alt="Main Logo" class="main-logo">
      <p class="description">가게의 메뉴를 추가하거나 삭제하세요.</p>
      
      <!-- 오류 메시지 표시 -->
      <div v-if="errorMessage" class="description error">{{ errorMessage }}</div>
      <!-- 성공 메시지 표시 -->
      <div v-if="successMessage" class="description success">{{ successMessage }}</div>
      
      <!-- 메뉴 추가 폼 -->
      <form @submit.prevent="addMenu" enctype="multipart/form-data">
        <div class="form-group">
          <label for="menuName">메뉴 이름</label>
          <input type="text" id="menuName" v-model="newMenu.menuName" placeholder="메뉴 이름을 입력해 주세요." required>
        </div>
        <div class="form-group">
          <label for="price">가격</label>
          <input type="number" id="price" v-model="newMenu.price" placeholder="가격을 입력해 주세요." required>
        </div>
        <div class="form-group">
          <label>메뉴 사진</label>
          <button type="button" class="custom-file-button" @click="triggerMenuPhoto">메뉴 사진 선택</button>
          <span class="file-name">{{ newMenu.menuPhotoName || '선택된 파일 없음' }}</span>
          <input type="file" ref="menuPhotoInput" @change="handleMenuPhoto" accept="image/*" class="hidden-file-input">
          <img v-if="newMenu.menuPhotoPreview" :src="newMenu.menuPhotoPreview" alt="메뉴 사진 미리보기" class="menu-photo-preview">
        </div>
        <div class="button-container">
          <input type="submit" value="메뉴 추가">
        </div>
      </form>
      
      <!-- 기존 메뉴 목록 -->
      <div class="menu-list">
        <h3>추가된 메뉴</h3>
        <ul>
          <li v-for="menu in menus" :key="menu.id">
            <img :src="menu.menuPhotoUrl" alt="메뉴 사진" class="menu-photo">
            <div>
              <strong>{{ menu.menuName }}</strong> - {{ menu.price }}원
              <button @click="deleteMenu(menu.id)" class="delete-button">삭제</button>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, reactive, onMounted } from 'vue'
  import { useAdminStore } from '../../stores/admin'
  import { useRouter, useRoute } from 'vue-router'
  
  export default {
    name: 'StoreMenu',
    setup() {
      const adminStore = useAdminStore()
      const router = useRouter()
      const route = useRoute()
      
      // 라우트에서 storeId 가져오기
      const storeId = route.params.storeId
      
      // 새로운 메뉴 데이터
      const newMenu = reactive({
        menuName: '',
        price: null,
        menuPhoto: null,
        menuPhotoName: '',
        menuPhotoPreview: ''
      })
      
      // 파일 입력 트리거
      const menuPhotoInput = ref(null)
      
      const triggerMenuPhoto = () => {
        menuPhotoInput.value.click()
      }
      
      const handleMenuPhoto = (event) => {
        const file = event.target.files[0]
        if (file) {
          newMenu.menuPhoto = file
          newMenu.menuPhotoName = file.name
          const reader = new FileReader()
          reader.onload = (e) => {
            newMenu.menuPhotoPreview = e.target.result
          }
          reader.readAsDataURL(file)
        } else {
          newMenu.menuPhoto = null
          newMenu.menuPhotoName = ''
          newMenu.menuPhotoPreview = ''
        }
      }
      
      // 메시지
      const errorMessage = ref('')
      const successMessage = ref('')
      
      // 기존 메뉴 목록
      const menus = ref([])
      
      // 초기 메뉴 목록 로드
      const loadMenus = async () => {
        // 메뉴 목록을 가져오는 API가 없는 경우, 가게 정보를 통해 메뉴를 가져옴
        try {
          const store = await adminStore.getStore(storeId)
          menus.value = store.menus
          errorMessage.value = ''
        } catch (error) {
          console.error('메뉴 목록 로드 실패:', error)
          errorMessage.value = '메뉴 목록을 로드하는 데 실패했습니다.'
        }
      }
      
      // 메뉴 추가
      const addMenu = async () => {
        // 폼 유효성 검사
        if (!newMenu.menuName || !newMenu.price) {
          alert('메뉴 이름과 가격을 모두 입력해 주세요.')
          return
        }
        
        try {
          const createdMenu = await adminStore.createMenu(storeId, newMenu, newMenu.menuPhoto)
          menus.value.push(createdMenu)
          successMessage.value = '메뉴가 성공적으로 추가되었습니다.'
          errorMessage.value = ''
          // 폼 초기화
          newMenu.menuName = ''
          newMenu.price = null
          newMenu.menuPhoto = null
          newMenu.menuPhotoName = ''
          newMenu.menuPhotoPreview = ''
        } catch (error) {
          errorMessage.value = adminStore.errorMessage || '메뉴 추가에 실패했습니다.'
          successMessage.value = ''
        }
      }
      
      // 메뉴 삭제
      const deleteMenu = async (menuId) => {
        if (!confirm('정말로 메뉴를 삭제하시겠습니까?')) {
          return
        }
        
        try {
          await adminStore.deleteMenu(menuId)
          menus.value = menus.value.filter(menu => menu.id !== menuId)
          successMessage.value = '메뉴가 성공적으로 삭제되었습니다.'
          errorMessage.value = ''
        } catch (error) {
          errorMessage.value = adminStore.errorMessage || '메뉴 삭제에 실패했습니다.'
          successMessage.value = ''
        }
      }
      
      onMounted(() => {
        loadMenus()
      })
      
      const cancel = () => {
        router.push('/admin/suggestion/list')
      }
      
      return {
        newMenu,
        triggerMenuPhoto,
        handleMenuPhoto,
        addMenu,
        deleteMenu,
        menus,
        errorMessage,
        successMessage,
        menuPhotoInput,
        cancel
      }
    }
  }
  </script>
  
  <style scoped>
  /* 기존 스타일 유지 */
  .main-content {
      width: 800px;
      margin: 75px auto;
      background-color: #fff;
      padding: 30px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      border-radius: 8px;
      margin-bottom: 100px;
  }
  
  .main-content h2 {
      font-size: 28px;
      color: #FF885B;
      margin-bottom: 20px;
      text-align: center;
  }
  
  .main-logo {
      display: block;
      margin: 0 auto 20px auto;
      width: 150px;
      height: auto;
  }
  
  .description {
      text-align: center;
      font-size: 15px;
      color: #666;
      margin-top: 20px;
      margin-bottom: 20px;
  }
  
  .description.error {
      color: red;
  }
  
  .description.success {
      color: green;
  }
  
  .form-group {
      margin-bottom: 20px;
  }
  
  .form-group label {
      display: block;
      font-weight: bold;
      margin-bottom: 8px;
      color: #FF885B;
  }
  
  .form-group input[type="text"],
  .form-group input[type="email"],
  .form-group input[type="password"],
  .form-group input[type="number"],
  .form-group select,
  .form-group textarea {
      width: 100%;
      padding: 12px 15px;
      border: 1.5px solid #FF885B;
      border-radius: 5px;
      font-size: 16px;
      resize: none;
      box-sizing: border-box;
      font-family: "맑은 고딕", sans-serif;
  }
  
  .form-group textarea {
      height: 200px;
      line-height: 1.5;
  }
  
  .hidden-file-input {
      display: none;
  }
  
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
  
  .file-name {
      display: inline-block;
      margin-left: 10px;
      font-size: 14px;
      color: #333;
      vertical-align: middle;
  }
  
  .store-photo-preview,
  .menu-photo-preview {
      display: block;
      width: 150px;
      height: auto;
      margin-top: 10px;
      border: 1px solid #ddd;
      border-radius: 5px;
  }
  
  .button-container {
      text-align: center;
      margin-top: 30px;
  }
  
  .button-container input[type="submit"],
  .button-container .cancel-button {
      background-color: #FF885B;
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
  
  .button-container input[type="submit"]:hover {
      background-color: #e07a4d;
  }
  
  .button-container .cancel-button:hover {
      background-color: #999;
  }
  
  .postcode-button,
  .add-menu-button,
  .delete-button {
      background-color: #FF885B;
      color: #fff;
      border: none;
      padding: 8px 16px;
      font-size: 14px;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s;
      margin-top: 10px;
  }
  
  .postcode-button:hover,
  .add-menu-button:hover,
  .delete-button:hover {
      background-color: #e07a4d;
  }
  
  .menu-list {
      margin-top: 40px;
  }
  
  .menu-list ul {
      list-style-type: none;
      padding: 0;
  }
  
  .menu-list li {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
  }
  
  .menu-photo {
      width: 100px;
      height: auto;
      margin-right: 20px;
      border: 1px solid #ddd;
      border-radius: 5px;
  }
  
  .delete-button {
      margin-left: auto;
      background-color: #ff4d4d;
  }
  
  .delete-button:hover {
      background-color: #e60000;
  }
  </style>
  