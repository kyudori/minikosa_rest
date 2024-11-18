<template>
  <div id="app">
    <!-- 조건에 따라 Header 표시 -->
    <Header v-if="showHeader" />
    <router-view />
    <!-- 조건에 따라 Footer 표시 -->
    <Footer v-if="showFooter" />
  </div>
</template>

<script>
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';

export default {
  name: 'App',
  components: {
    Header,
    Footer,
  },
  computed: {
    showHeader() {
      // 특정 URL 경로에서만 Header 표시
      return !['/', '/home'].includes(this.$route.path);
    },
    showFooter() {
      // 특정 URL 경로에서만 Footer 표시
      return !['/', '/home'].includes(this.$route.path);
    },
  },
  watch: {
    // 경로가 변경될 때 UI 업데이트
    $route() {
      this.updateView();
    },
  },
  methods: {
    updateView() {
      // 경로 변경 시 추가 로직이 필요한 경우 사용
    },
  },
};
</script>