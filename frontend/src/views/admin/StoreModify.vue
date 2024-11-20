<!-- src/views/StoreModify.vue -->
<template>
    <div class="main-content">
      <h2>가게 수정</h2>
      <img src="/images/main_logo.png" alt="Main Logo" class="main-logo">
      <p class="description">가게 정보를 수정하고 업데이트하세요.</p>
  
      <!-- 오류 메시지 표시 -->
      <div v-if="errorMessage" class="description error">{{ errorMessage }}</div>
      <!-- 성공 메시지 표시 -->
      <div v-if="successMessage" class="description success">{{ successMessage }}</div>
  
      <form @submit.prevent="submitForm">
        <!-- 가게 이름 -->
        <div class="form-group">
          <label for="storeName">가게 이름</label>
          <input type="text" id="storeName" v-model="storeName" placeholder="가게 이름을 입력해 주세요." required>
        </div>
  
        <!-- 가게 주소 및 우편번호 검색 -->
        <div class="form-group">
          <label for="roadAddress">가게 주소</label>
          <input type="text" id="roadAddress" v-model="roadAddress" placeholder="가게 주소를 입력해 주세요." readonly>
          <button type="button" class="postcode-button" @click="openPostcode">우편번호 찾기</button>
        </div>
  
        <!-- 우편번호 -->
        <div class="form-group">
          <label for="postcode">우편번호</label>
          <input type="text" id="postcode" v-model="postcode" placeholder="우편번호" readonly>
        </div>
  
        <!-- 상세주소 및 참고항목 -->
        <div class="form-group">
          <label for="detailAddress">상세주소</label>
          <input type="text" id="detailAddress" v-model="detailAddress" placeholder="상세주소" required>
        </div>
        <div class="form-group">
          <label for="extraAddress">참고항목</label>
          <input type="text" id="extraAddress" v-model="extraAddress" placeholder="참고항목">
        </div>
  
        <!-- 카카오맵 표시 영역 -->
        <div id="map" style="width:100%;height:350px;margin-bottom:20px;"></div>
  
        <!-- 카테고리 선택 -->
        <div class="form-group">
          <label for="categoryId">카테고리</label>
          <select id="categoryId" v-model="categoryId" required>
            <option value="">카테고리를 선택하세요.</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}</option>
          </select>
        </div>
  
        <!-- 가게 사진 업로드 및 미리보기 -->
        <div class="form-group">
          <label>가게 사진</label>
          <button type="button" class="custom-file-button" @click="triggerStorePhoto">가게 사진 선택</button>
          <span class="file-name">{{ storePhotoName || '선택된 파일 없음' }}</span>
          <input type="file" ref="storePhotoInput" @change="handleStorePhoto" accept="image/*" class="hidden-file-input">
          <img
            :src="storePhotoPreview ? getImageUrl(storePhotoPreview) : '/images/main_logo.png'"
            alt="가게 사진 미리보기"
            class="store-photo-preview"
            @error="onImageError"
          >
        </div>
  
        <!-- 영업 시간 -->
        <div class="time-group">
          <div class="form-group">
            <label for="openingTime">영업 시작 시간</label>
            <input type="time" id="openingTime" v-model="openingTime">
          </div>
          <div class="form-group">
            <label for="closingTime">영업 종료 시간</label>
            <input type="time" id="closingTime" v-model="closingTime">
          </div>
        </div>
  
        <!-- 홈페이지 정보 -->
        <div class="form-group">
          <label for="websiteInfo">홈페이지 정보</label>
          <input type="text" id="websiteInfo" v-model="websiteInfo" placeholder="홈페이지 URL을 입력해 주세요.">
        </div>
  
        <!-- 전화번호 -->
        <div class="form-group">
          <label for="contactNumber">전화번호</label>
          <input type="text" id="contactNumber" v-model="contactNumber" placeholder="전화번호를 입력해 주세요.">
        </div>
  
        <!-- 가게 설명 -->
        <div class="form-group">
          <label for="storeDescription">가게 설명</label>
          <textarea id="storeDescription" v-model="storeDescription" placeholder="가게에 대한 설명을 입력해 주세요." required></textarea>
        </div>
  
        <!-- 메뉴 등록 컴포넌트 -->
        <div class="form-group">
          <MenuRegister v-model="menus" />
        </div>
  
        <!-- 버튼 컨테이너 -->
        <div class="button-container">
          <input type="submit" value="수정">
          <button type="button" class="cancel-button" @click="cancel">취소</button>
        </div>
      </form>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue'
  import { useAdminStore } from '../../stores/admin'
  import { useRouter, useRoute } from 'vue-router'
  import MenuRegister from '../../components/MenuRegister.vue'
  
  export default {
    name: 'StoreModify',
    components: {
      MenuRegister
    },
    setup() {
      const adminStore = useAdminStore()
      const router = useRouter()
      const route = useRoute()
  
      const storeId = route.params.id
  
      // 가게 정보
      const storeName = ref('')
      const roadAddress = ref('')
      const postcode = ref('')
      const detailAddress = ref('')
      const extraAddress = ref('')
      const categoryId = ref('')
      const openingTime = ref('')
      const closingTime = ref('')
      const websiteInfo = ref('')
      const contactNumber = ref('')
      const storeDescription = ref('')
  
      // 가게 사진
      const storePhoto = ref(null)
      const storePhotoName = ref('')
      const storePhotoPreview = ref('')
      const storePhotoInput = ref(null)
  
      // 메뉴 정보
      const menus = ref([])
  
      // 메시지
      const errorMessage = ref('')
      const successMessage = ref('')
  
      // 카테고리 목록
      const categories = ref([
        { id: 1, name: '한식' },
        { id: 2, name: '일식' },
        { id: 3, name: '중식' },
        { id: 4, name: '양식' },
        { id: 5, name: '카페' },
        { id: 6, name: '디저트' }
      ])
  
      // 가게 사진 업로드 트리거
      const triggerStorePhoto = () => {
        if (storePhotoInput.value) {
          storePhotoInput.value.click()
        } else {
          console.error('storePhotoInput이 정의되지 않았습니다.')
        }
      }
  
      // 가게 사진 처리
      const handleStorePhoto = (event) => {
        const file = event.target.files[0]
        if (file) {
          storePhoto.value = file
          storePhotoName.value = file.name
          const reader = new FileReader()
          reader.onload = (e) => {
            storePhotoPreview.value = e.target.result
          }
          reader.readAsDataURL(file)
        } else {
          storePhoto.value = null
          storePhotoName.value = ''
          storePhotoPreview.value = ''
        }
      }
  
      // 카카오맵 초기화
      let map = null
      let geocoder = null
      let marker = null
      let infowindow = null
  
      const initMap = () => {
        if (!window.kakao || !window.kakao.maps) {
          console.error('Kakao Maps API 로드 실패')
          errorMessage.value = '카카오맵을 로드할 수 없습니다. 잠시 후 다시 시도해 주세요.'
          return
        }
  
        geocoder = new window.kakao.maps.services.Geocoder()
  
        map = new window.kakao.maps.Map(document.getElementById('map'), {
          center: new window.kakao.maps.LatLng(37.5665, 126.9780),
          level: 3
        })
      }
  
      // 우편번호 찾기 및 지도 표시
      const openPostcode = () => {
        new daum.Postcode({
          oncomplete: function(data) {
            // 선택한 주소 정보를 해당 필드에 입력
            roadAddress.value = data.roadAddress
            postcode.value = data.zonecode
            detailAddress.value = data.buildingName || ''
            extraAddress.value = data.bname || ''
  
            // 지도를 업데이트하여 마커를 표시
            geocoder.addressSearch(data.roadAddress, function(result, status) {
              if (status === window.kakao.maps.services.Status.OK) {
                const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x)
                map.setCenter(coords)
  
                // 기존 마커 제거
                if (marker) {
                  marker.setMap(null)
                }
  
                // 마커 표시
                marker = new window.kakao.maps.Marker({
                  map: map,
                  position: coords
                })
  
                // 인포윈도우로 가게 이름 표시
                if (infowindow) {
                  infowindow.close()
                }
  
                infowindow = new window.kakao.maps.InfoWindow({
                  content: `<div style="width:150px;text-align:center;padding:6px 0;">${storeName.value || '가게 위치'}</div>`
                })
                infowindow.open(map, marker)
              } else {
                alert('주소 검색에 실패했습니다. 정확한 주소를 입력해 주세요.')
              }
            })
          }
        }).open()
      }
  
      // 가게 정보 로드
      const loadStoreData = async () => {
        try {
          // 가게 정보 가져오기
          const storeData = await adminStore.getStore(storeId)
  
          storeName.value = storeData.storeName
          postcode.value = storeData.postcode
          roadAddress.value = storeData.roadAddress
          detailAddress.value = storeData.detailAddress
          extraAddress.value = storeData.extraAddress
          categoryId.value = storeData.categoryId
          openingTime.value = storeData.openingTime
          closingTime.value = storeData.closingTime
          websiteInfo.value = storeData.websiteInfo
          contactNumber.value = storeData.contactNumber
          storeDescription.value = storeData.storeDescription
  
          // 가게 사진
          storePhotoPreview.value = storeData.storePhoto
  
          // 메뉴 정보 가져오기
          const menusData = await adminStore.getMenus(storeId)
          menus.value = menusData.map(menu => ({
            menuId: menu.menuId,
            menuName: menu.menuName,
            price: menu.price,
            menuPhotoPreview: menu.menuPhoto,
            menuPhotoName: '', // 새로 업로드하지 않으면 빈 문자열 유지
            menuPhoto: null // 새로 업로드하지 않으면 null 유지
          }))
  
          // 지도 업데이트
          geocoder.addressSearch(roadAddress.value, function(result, status) {
            if (status === window.kakao.maps.services.Status.OK) {
              const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x)
              map.setCenter(coords)
  
              // 기존 마커 제거
              if (marker) {
                marker.setMap(null)
              }
  
              // 마커 표시
              marker = new window.kakao.maps.Marker({
                map: map,
                position: coords
              })
  
              // 인포윈도우로 가게 이름 표시
              if (infowindow) {
                infowindow.close()
              }
  
              infowindow = new window.kakao.maps.InfoWindow({
                content: `<div style="width:150px;text-align:center;padding:6px 0;">${storeName.value || '가게 위치'}</div>`
              })
              infowindow.open(map, marker)
            }
          })
  
        } catch (error) {
          errorMessage.value = adminStore.errorMessage || '가게 정보를 가져오는 데 실패했습니다.'
        }
      }
  
      // 폼 제출
      const submitForm = async () => {
        // 폼 유효성 검사
        if (!storeName.value || !roadAddress.value || !categoryId.value || !storeDescription.value) {
          alert('가게 이름, 가게 주소, 카테고리, 가게 설명을 모두 입력해 주세요.')
          return
        }
  
        // 메뉴 유효성 검사
        if (menus.value.length === 0) {
          alert('최소 하나의 메뉴를 등록해야 합니다.')
          return
        }
        for (let i = 0; i < menus.value.length; i++) {
          const menu = menus.value[i]
          if (!menu.menuName || !menu.price) {
            alert(`메뉴 ${i + 1}의 이름과 가격을 모두 입력해 주세요.`)
            return
          }
        }
  
        // StoreCreateDTO 객체 생성
        const storeData = {
          storeName: storeName.value,
          postcode: postcode.value,
          roadAddress: roadAddress.value,
          detailAddress: detailAddress.value,
          extraAddress: extraAddress.value,
          categoryId: categoryId.value,
          openingTime: openingTime.value || null,
          closingTime: closingTime.value || null,
          websiteInfo: websiteInfo.value,
          contactNumber: contactNumber.value,
          storeDescription: storeDescription.value,
        }
  
        // 메뉴 데이터 준비
        const menuData = menus.value.map(menu => ({
          menuId: menu.menuId,
          menuName: menu.menuName,
          price: menu.price,
          menuPhoto: menu.menuPhoto,
          menuPhotoName: menu.menuPhotoName
        }))
  
        try {
          // 가게 수정
          await adminStore.updateStore(storeId, storeData, storePhoto.value)
  
          // 메뉴 수정 및 생성
          for (const menu of menuData) {
            if (menu.menuId) {
              // 기존 메뉴 업데이트
              await adminStore.updateMenu(menu.menuId, {
                menuName: menu.menuName,
                price: menu.price
              }, menu.menuPhoto)
            } else {
              // 새로운 메뉴 생성
              await adminStore.createMenu(storeId, {
                menuName: menu.menuName,
                price: menu.price
              }, menu.menuPhoto)
            }
          }
  
          successMessage.value = '가게와 메뉴가 성공적으로 수정되었습니다.'
          errorMessage.value = ''
  
          // 수정된 가게의 상세 페이지로 이동
          router.push({ name: 'StoreContent', params: { id: storeId } })
        } catch (error) {
          errorMessage.value = adminStore.errorMessage || '가게 및 메뉴 수정에 실패했습니다.'
          successMessage.value = ''
        }
      }
  
      // 취소 버튼 핸들러
      const cancel = () => {
        router.push({ name: 'StoreContent', params: { id: storeId } })
      }
  
      // 이미지 URL 처리 함수 추가
      const getImageUrl = (path) => {
        if (path.startsWith('data:')) {
          return path
        } else {
          const serverHost = 'http://localhost:8090' // 실제 백엔드 서버 주소로 변경 필요
          return serverHost + path
        }
      }
  
      // 이미지 로드 실패 시 대체 이미지 설정
      const onImageError = (event) => {
        event.target.onerror = null
        event.target.src = '/images/main_logo.png'
      }
  
      onMounted(() => {
        // 페이지 로드 시 맵 초기화
        initMap()
        // 가게 데이터 로드
        loadStoreData()
      })
  
      return {
        storeName,
        roadAddress,
        postcode,
        detailAddress,
        extraAddress,
        categoryId,
        openingTime,
        closingTime,
        websiteInfo,
        contactNumber,
        storeDescription,
        storePhoto,
        storePhotoName,
        storePhotoPreview,
        errorMessage,
        successMessage,
        categories,
        storePhotoInput,
        triggerStorePhoto,
        handleStorePhoto,
        menus,
        submitForm,
        cancel,
        openPostcode,
        getImageUrl,
        onImageError
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
  
  .store-photo-preview {
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
  
  .postcode-button {
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
  
  .postcode-button:hover {
      background-color: #e07a4d;
  }
  </style>
  