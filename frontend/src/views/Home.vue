<template>
  <div class="hero_area">
    <div class="bg-box">
      <div id="mySlider" class="slider">
        <a data-category=".korean-food" href="#">
          <div class="slide active" style="background-image: url(/images/food1.jpg);"></div>
        </a>
        <a data-category=".chinese-food" href="#">
          <div class="slide" style="background-image: url(/images/food2.jpg);"></div>
        </a>
        <a data-category=".japanese-food" href="#">
          <div class="slide" style="background-image: url(/images/food3.png);"></div>
        </a>
        <a data-category=".western-food" href="#">
          <div class="slide" style="background-image: url(/images/food4.jpg);"></div>
        </a>
        <a data-category=".cafe" href="#">
          <div class="slide" style="background-image: url(/images/food5.jpg);"></div>
        </a>
      </div>
      <!-- 점 버튼 -->
      <div class="dots">
        <span class="dot active" data-slide="0"></span>
        <span class="dot" data-slide="1"></span>
        <span class="dot" data-slide="2"></span>
        <span class="dot" data-slide="3"></span>
        <span class="dot" data-slide="4"></span>
      </div>
    </div>
  </div>

  <!-- 음식 섹션 -->
  <section class="food_section layout_padding-bottom" id="foodSection">
    <div class="container">
      <div class="heading_container heading_center">
        <!-- 필요한 경우 제목 추가 -->
      </div>

      <ul class="filters_menu">
        <li
          v-for="(filter, index) in filters"
          :key="index"
          :class="{ active: activeFilter === filter.value }"
          @click="applyFilter(filter.value)"
        >
          {{ filter.label }}
        </li>
      </ul>

      <div class="filters-content">
        <div class="container">
          <div class="row grid">
            <div
              v-for="store in filteredStores"
              :key="store.storeId"
              :class="['col-sm-6', 'col-lg-4', 'all', store.categoryName]"
            >
              <div class="box">
                <a :href="`/store/${store.storeId}`">
                  <div class="img-box">
                    <img :src="store.storePhoto" alt="가게 사진" />
                  </div>
                  <div class="detail-box">
                    <h5>{{ store.storeName }}</h5>
                    <div class="detail-desc-info">
                      <p>{{ store.storeDescription }}</p>
                      <div class="detail-rate">
                        <ul>
                          <li>
                            <img
                              width="48"
                              height="48"
                              src="https://img.icons8.com/color/48/filled-star--v1.png"
                              alt="filled-star--v1"
                              class="icon_star"
                            />
                          </li>
                          <li>
                            <span class="rate_num">{{ store.ratingAvg }}</span>
                          </li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </a>
              </div>
            </div>
          </div>
          <!-- 더보기 버튼은 필요 시 추가 -->
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import api from '../axios';

export default {
  name: 'Home',
  setup() {
    const stores = ref([]);
    const filters = ref([
      { label: 'All', value: '*' },
      { label: '한식', value: '한식' },
      { label: '중식', value: '중식' },
      { label: '일식', value: '일식' },
      { label: '양식', value: '양식' },
      { label: '카페', value: '카페' },
    ]);
    const activeFilter = ref('*');

    const fetchStores = async () => {
      try {
        const response = await api.get('/home');
        stores.value = response.data;
      } catch (error) {
        console.error('가게 정보를 가져오는 중 오류 발생:', error);
      }
    };

    const filteredStores = computed(() => {
      if (activeFilter.value === '*' || !activeFilter.value) {
        return stores.value;
      }
      return stores.value.filter(
        (store) => store.categoryName === activeFilter.value
      );
    });

    const applyFilter = (filterValue) => {
      activeFilter.value = filterValue;
    };

    onMounted(() => {
      fetchStores();
    });

    return {
      stores,
      filters,
      activeFilter,
      filteredStores,
      applyFilter,
    };
  },
};
</script>

<style>
@import '../assets/css/home/bootstrap.css';
@import '../assets/css/home/main.css';

</style>
