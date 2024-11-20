<template>
    <div>
      <label>메뉴</label>
      <div v-for="(menu, index) in menus" :key="index" class="menu-section">
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
          <span class="file-name">{{ menu.menuPhotoName || '선택된 파일 없음' }}</span>
        </div>
      </div>
      <button type="button" class="add-menu-button" @click="addMenu">+ 메뉴 추가</button>
    </div>
  </template>
  
  <script>
  import { reactive, ref, watch } from 'vue'
  
  export default {
    name: 'MenuRegister',
    props: {
      modelValue: {
        type: Array,
        default: () => []
      }
    },
    emits: ['update:modelValue'],
    setup(props, { emit }) {
      // 메뉴 정보
      const menus = reactive(props.modelValue)
  
      // 메뉴 사진 입력 필드 참조 배열
      const menuPhotoInputs = ref([])
  
      // 메뉴 사진 업로드 트리거
      const triggerMenuPhoto = (index) => {
        const input = menuPhotoInputs.value[index]
        if (input) {
          input.click()
        } else {
          console.error(`menuPhotoInput${index}가 정의되지 않았습니다.`)
        }
      }
  
      // 메뉴 사진 처리
      const handleMenuPhoto = (index, event) => {
        const file = event.target.files[0]
        if (file) {
          menus[index].menuPhoto = file
          menus[index].menuPhotoName = file.name
          const reader = new FileReader()
          reader.onload = (e) => {
            menus[index].menuPhotoPreview = e.target.result
          }
          reader.readAsDataURL(file)
        } else {
          menus[index].menuPhoto = null
          menus[index].menuPhotoName = ''
          menus[index].menuPhotoPreview = ''
        }
      }
  
      // 메뉴 추가
      const addMenu = () => {
        menus.push({
          menuName: '',
          price: '',
          menuPhoto: null,
          menuPhotoName: '',
          menuPhotoPreview: ''
        })
      }
  
      // 메뉴 제거
      const removeMenu = (index) => {
        if (menus.length > 1) {
          menus.splice(index, 1)
          // 메뉴를 제거하면 해당 ref도 제거
          menuPhotoInputs.value.splice(index, 1)
        } else {
          alert('최소 하나의 메뉴는 등록해야 합니다.')
        }
      }
  
      // 메뉴 데이터 변경 시 부모 컴포넌트로 emit
      watch(
        () => menus,
        (newMenus) => {
          emit('update:modelValue', newMenus)
        },
        { deep: true }
      )
  
      return {
        menus,
        menuPhotoInputs,
        triggerMenuPhoto,
        handleMenuPhoto,
        addMenu,
        removeMenu
      }
    }
  }
  </script>
  
  <style scoped>
  /* 메뉴 섹션 스타일 */
  .menu-section {
      margin-bottom: 20px;
      padding: 15px;
      border: 1.5px solid #FF885B;
      border-radius: 5px;
      background-color: #fff;
      position: relative;
  }
  
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
  
  .menu-photo-preview {
      display: block;
      width: 150px;
      height: auto;
      margin-top: 10px;
      border: 1px solid #ddd;
      border-radius: 5px;
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
      margin-top: 20px;
  }
  
  .add-menu-button:hover {
      background-color: #e07a4d;
  }
  </style>
  