<!-- src/views/StoreContent.vue -->
<template>
  <div id="storeWrapper">
    <!-- Store Content -->
    <div class="container">
      <div
        class="dimmed_layer"
        v-if="showEditModal || showReplyModal || showReplyEditModal"
      ></div>
      <!-- Store Top Section -->
      <div class="content_out_line">
        <div class="content_inner">
          <div class="content_top_line">
            <div class="top_image_line">
              <span
                class="image_present"
                :style="`background-image: url(${getImageUrl(
                  store.storePhoto
                )})`"
              ></span>
              <span class="frame_g"></span>

              <!-- Store Title and Admin Buttons -->
              <div class="place_out_line">
                <!-- Admin Buttons -->
                <div class="btn_box" v-if="isAdmin">
                  <form @submit.prevent="handleAdminAction('modify')">
                    <input
                      type="hidden"
                      :value="store.storeId"
                      name="storeId"
                    />
                    <button
                      type="submit"
                      class="admin_button"
                      data-result="modify"
                    >
                      수정
                    </button>
                    <button
                      type="button"
                      class="admin_button"
                      data-result="del"
                      @click="handleDeleteStore"
                    >
                      삭제
                    </button>
                  </form>
                </div>

                <!-- Store Information -->
                <div class="place_inner">
                  <h2 class="restaurant_title">{{ store.storeName }}</h2>
                  <div class="evaluation_box">
                    <img
                      width="48"
                      height="48"
                      src="https://img.icons8.com/color/48/filled-star--v1.png"
                      alt="filled-star"
                      class="icon_star"
                    />
                    <span class="evaluation_link">
                      평점 : {{ formatRating(store.ratingAvg) }}
                    </span>
                    <span class="bar"></span>
                    <span class="evaluation_link">
                      리뷰 : {{ store.countReview }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Store Detailed Information -->
          <div class="place_info_box">
            <h3 class="detail_info_title">상세정보</h3>
            <div class="place_info_detail">
              <h4 class="info_title_detail">
                <span class="place_info_icon address_icon">위치</span>
                <img
                  src="/images/ico_location.png"
                  class="address_icon"
                  alt="Location Icon"
                />
              </h4>
              <div class="place_location">
                <span class="address_text">
                  {{ store.roadAddress }} {{ store.detailAddress }} (우)
                  {{ store.postcode }}
                </span>
                <span class="address_num_text">
                  상세주소
                  <span class="bar"></span>
                  {{ store.extraAddress }}
                </span>
              </div>
            </div>

            <!-- Website Information -->
            <div
              v-if="store.websiteInfo"
              class="place_info_detail place_homepage"
            >
              <h4 class="info_title_detail">
                <span class="place_info_icon">홈페이지</span>
                <img
                  src="/images/icon-internet.png"
                  width="14"
                  height="14"
                  class="homepage_icon"
                  alt="Internet Icon"
                />
              </h4>
              <div class="place_location">
                <div class="homepage_text">
                  <a
                    :href="store.websiteInfo"
                    target="_blank"
                    class="link_homepage"
                    >{{ store.websiteInfo }}</a
                  >
                </div>
              </div>
            </div>

            <!-- Opening Hours -->
            <div
              v-if="store.openingTime && store.closingTime"
              class="place_info_detail place_business_hours"
            >
              <h4 class="info_title_detail">
                <span class="place_info_icon">영업시간</span>
                <img
                  src="https://img.icons8.com/forma-thin/48/clock.png"
                  width="16"
                  height="16"
                  class="homepage_icon"
                  alt="Clock Icon"
                />
              </h4>
              <div class="place_location">
                <div class="business_hours_text">
                  <span class="hours_text">
                    {{ formatTime(store.openingTime) }} -
                    {{ formatTime(store.closingTime) }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Contact Number -->
            <div class="place_info_detail place_phone">
              <h4 class="info_title_detail">
                <span class="place_info_icon">전화번호</span>
                <img
                  src="/images/icon_phone.png"
                  class="phone_icon"
                  alt="Phone Icon"
                />
              </h4>
              <div class="place_location">
                <span class="phone_text">
                  <span class="phone_num_text">{{ store.contactNumber }}</span>
                </span>
              </div>
            </div>

            <!-- Store Description -->
            <div class="place_info_detail">
              <h4 class="info_title_detail">
                <span class="place_info_icon">소개</span>
                <img
                  src="/images/icon-desc.png"
                  class="icon_desc"
                  alt="Description Icon"
                />
              </h4>
              <div class="place_location">
                <span class="title_info_text">{{
                  store.storeDescription
                }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Menu Section -->
        <div class="menu_info">
          <div class="menu_info_title_box">
            <h3 class="detail_info_title">메뉴</h3>
          </div>
          <div class="menu_image_wrap">
            <div class="menu_image_present">
              <!-- Placeholder or actual menu image -->
            </div>
            <strong class="screen_out">메뉴 목록</strong>
            <ul class="menu_list">
              <li v-for="menu in store.menus" :key="menu.menuId">
                <a href="#" class="link_photo">
                  <span class="inner_photo">
                    <img
                      :src="getImageUrl(menu.menuPhoto)"
                      class="img_thumb"
                      width="106"
                      height="90"
                      alt="Menu Photo"
                    />
                  </span>
                </a>
                <div class="menu_title_box">
                  <em class="screen_out">명 : </em>
                  <span class="menu_text">{{ menu.menuName }}</span>
                  <em class="screen_out">가격 : </em>
                  <span class="price_menu">{{ formatPrice(menu.price) }}</span>
                </div>
              </li>
            </ul>
          </div>
        </div>

        <!-- Review Section -->
        <template v-if="!isAdmin && !isOwner">
          <div class="rate_container">
            <form @submit.prevent="submitReview" id="userWrite">
              <div class="rate_box">
                <div class="rate_intro">
                  <p class="desc_rate_username">
                    {{ user.nickname }}님! 이 장소의 후기를 남겨주세요
                  </p>
                  <div class="user_rate">
                    <div class="grade_star_box">
                      <span class="ico_star_rate">
                        <span class="ico_star_rate inner_star"></span>
                      </span>
                    </div>
                  </div>
                  <div class="star-rating space-x-4 mx-auto">
                    <input
                      type="radio"
                      id="5-stars"
                      name="rating"
                      value="5"
                      v-model="newReview.rating"
                      checked
                    />
                    <label for="5-stars" class="star pr-4">★</label>
                    <input
                      type="radio"
                      id="4-stars"
                      name="rating"
                      value="4"
                      v-model="newReview.rating"
                    />
                    <label for="4-stars" class="star">★</label>
                    <input
                      type="radio"
                      id="3-stars"
                      name="rating"
                      value="3"
                      v-model="newReview.rating"
                    />
                    <label for="3-stars" class="star">★</label>
                    <input
                      type="radio"
                      id="2-stars"
                      name="rating"
                      value="2"
                      v-model="newReview.rating"
                    />
                    <label for="2-stars" class="star">★</label>
                    <input
                      type="radio"
                      id="1-star"
                      name="rating"
                      value="1"
                      v-model="newReview.rating"
                    />
                    <label for="1-star" class="star">★</label>
                  </div>
                </div>

                <!-- Review Textarea and Buttons -->
                <div class="comment_write_box">
                  <textarea
                    class="comment_write"
                    name="reviewText"
                    id="u-comment-write"
                    v-model="newReview.reviewText"
                    placeholder="후기를 작성해주세요."
                    required
                  ></textarea>
                  <div class="button_box">
                    <button
                      class="select_button"
                      type="reset"
                      @click="resetReview"
                    >
                      다시쓰기
                    </button>

                    <button class="select_button" type="submit">
                      작성완료
                    </button>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </template>

        <!-- Reviews List -->
        <div class="comments_box">
          <strong class="total_comment">
            전체 <span class="color_b">{{ store.countReview }}</span>
          </strong>
          <div class="user_grade_box">
            <strong class="screen_out">후기 요약</strong>
            <div class="grade_star">
              <img
                width="48"
                height="48"
                src="https://img.icons8.com/color/48/filled-star--v1.png"
                alt="filled-star"
                class="icon_star"
              />
              <em class="num_rate">
                {{ formatRating(store.ratingAvg) }}
                <span class="text_score">점</span>
              </em>
              <span class="ico_star_rate">
                <span
                  class="ico_star_rate inner_star"
                  :style="{ width: starWidth }"
                ></span>
              </span>
            </div>
          </div>

          <strong class="screen_out">추천 포인트</strong>

          <!-- Reviews List -->
          <div class="review_box">
            <strong class="screen_out">후기 리스트</strong>
            <template v-for="review in reviews" :key="review.reviewId">
              <ul class="review_list" v-if="reviews.length">
                <li>
                  <div class="unit_info">
                    <em class="screen_out">작성자 : </em>
                    <a href="#" class="link_user">
                      <div class="inner_user">
                        <span class="text_username">{{ review.nickname }}</span>
                      </div>
                    </a>
                    <span class="bar"></span>
                    <span class="text_item">별점 : </span>
                    <span class="text_desc">{{ review.rating }}</span>
                    <span class="bar"></span>
                    <span class="text_item">작성일 : </span>
                    <span class="time_write">
                      {{ formatDate(review.createdAt) }}
                    </span>
                  </div>

                  <div class="photo_group">
                    <ul class="list_photo"></ul>
                  </div>

                  <div class="comment_info">
                    <p class="text_comment">
                      <span>{{ review.reviewText }}</span>
                    </p>
                  </div>

                  <div class="wrap_util">
                    <!-- Owner Reply Button -->
                    <template
                      v-if="
                        isCurrentUser(review.memberId) || isAdmin || isOwner
                      "
                    >
                      <button
                        type="button"
                        :class="['btn_util', { util_on: isMenuOpen }]"
                        @click="toggleMenu"
                      >
                        <span class="ico_comm ico_more">메뉴 더보기</span>
                      </button>
                      <div class="layer_util" v-if="isMenuOpen">
                        <ul class="list_opt">
                          <template v-if="isOwner && !review.replyId">
                            <li>
                              <a
                                href="#none"
                                data-id="replyBtn"
                                class="link_util"
                                @click="openReplyModal(review)"
                                >답글</a
                              >
                            </li>
                          </template>
                          <template
                            v-else-if="
                              !isAdmin && isCurrentUser(review.memberId)
                            "
                          >
                            <li>
                              <a
                                href="#none"
                                data-id="updateBtn"
                                class="link_util"
                                @click="openEditModal(review)"
                                >수정</a
                              >
                            </li>
                          </template>
                          <template
                            v-if="isAdmin || isCurrentUser(review.memberId)"
                          >
                            <li>
                              <a
                                href="#none"
                                data-id="deleteBtn"
                                class="link_util"
                                @click="deleteReview(review.reviewId)"
                                >삭제</a
                              >
                            </li>
                          </template>
                        </ul>
                      </div>
                    </template>
                  </div>
                </li>

                <!-- Owner's Reply -->
                <li v-if="review.replyId" class="reply_comment_list">
                  <a href="#" class="reply_img">
                    <img
                      width="16"
                      height="16"
                      src="https://img.icons8.com/small/16/down-right.png"
                      alt="down-right"
                      class="icon_reply"
                    />
                  </a>
                  <div class="unit_info_admin">
                    <em class="screen_out">사장 : </em>
                    <div class="comment_info_reply">
                      <p class="text_comment">
                        <span>{{ review.replyText }}</span>
                      </p>
                    </div>
                    <div class="wrap_util">
                      <!-- Owner Edit/Delete Reply Buttons -->
                      <template v-if="isOwner">
                        <button
                          type="button"
                          :class="['btn_util', { util_on: isMenuOpen }]"
                          @click="toggleMenu(review.replyId)"
                        >
                          <span class="ico_comm ico_more">메뉴 더보기</span>
                        </button>
                        <div class="layer_util" v-if="isMenuOpen">
                          <ul class="list_opt">
                            <li>
                              <a
                                href="#none"
                                data-id="updateBtn"
                                class="link_util"
                                @click="openReplyEditModal(review)"
                                >수정</a
                              >
                            </li>
                            <li>
                              <a
                                href="#none"
                                data-id="deleteBtn"
                                class="link_util"
                                @click="deleteReply(review.replyId)"
                                >삭제</a
                              >
                            </li>
                          </ul>
                        </div>
                      </template>
                    </div>
                  </div>
                </li>
              </ul>
            </template>
          </div>
        </div>

        <!-- Map Section -->
        <div class="map_container">
          <h3 class="detail_info_title">찾아가는 길</h3>
          <div class="map_box">
            <h4 class="screen_out">지도 보기</h4>
            <div class="view_map">
              <div id="map" style="width: 100%; height: 350px"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Edit Review Modal -->
  <div
    v-if="showEditModal"
    data-root=""
    class="evaluation_layer"
    :style="{ top: modalTop }"
  >
    <div class="inner_layer">
      <div class="layer_head">
        <strong class="tit_layer">{{ store.storeName }}</strong>
      </div>
      <div class="layer_body">
        <form action="#">
          <fieldset>
            <legend class="screen_out">후기 작성</legend>
            <div class="group_rate">
              <div class="star-rating space-x-4 mx-auto">
                <input
                  type="radio"
                  id="5-stars"
                  name="rating"
                  value="5"
                  v-model="newReview.rating"
                  checked
                />
                <label for="5-stars" class="star pr-4">★</label>
                <input
                  type="radio"
                  id="4-stars"
                  name="rating"
                  value="4"
                  v-model="newReview.rating"
                />
                <label for="4-stars" class="star">★</label>
                <input
                  type="radio"
                  id="3-stars"
                  name="rating"
                  value="3"
                  v-model="newReview.rating"
                />
                <label for="3-stars" class="star">★</label>
                <input
                  type="radio"
                  id="2-stars"
                  name="rating"
                  value="2"
                  v-model="newReview.rating"
                />
                <label for="2-stars" class="star">★</label>
                <input
                  type="radio"
                  id="1-star"
                  name="rating"
                  value="1"
                  v-model="newReview.rating"
                />
                <label for="1-star" class="star">★</label>
              </div>
              <div class="grade_rate">
                <div class="grade_star size_l">
                  <span class="ico_star star_rate">
                    <span
                      class="ico_star inner_star"
                      :style="{ width: starWidth }"
                    ></span>
                  </span>
                </div>
              </div>
              <span class="info_rate">
                <span class="screen_out">선택한 별점</span>
                <span class="num_rate">{{
                  newReview.rating
                }}</span
                >/<span class="screen_out">선택 가능한 총 별점</span
                ><span class="num_limit">5</span>
              </span>
            </div>
            <div class="box_evaluation">
              <div class="group_review">
                <label for="tfReview" class="screen_out">후기 작성</label>
                <textarea
                  v-model="editReviewText"
                  id="tfReview"
                  name="reviewText"
                  class="tf_review"
                  placeholder="작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한 다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아 작성해 주세요."
                ></textarea>
              </div>

              <div class="group_etc">
                <a href="#none" target="_blank" class="link_notice"
                  >후기 작성 시 주의사항</a
                >
                <span class="num_letter">
                  <span class="txt_len"></span
                  ><span class="num_total"
                    >작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한
                    다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아
                    작성해 주세요."</span
                  ></span
                >
              </div>
            </div>
            <div class="group_btn">
              <button type="reset" class="btn_reset" @click="closeEditModal">
                취소
              </button>
              <button type="submit" class="btn_submit" @click="updateReview">
                등록
              </button>
            </div>
          </fieldset>
        </form>
      </div>
    </div>
  </div>

  <!-- Create Reply Modal -->
  <div
    v-if="showReplyModal"
    data-root=""
    class="evaluation_layer"
    :style="{ top: modalTop }"
  >
    <div class="inner_layer">
      <div class="layer_head">
        <strong class="tit_layer">{{ store.storeName }}</strong>
      </div>
      <div class="layer_body">
        <form action="#">
          <fieldset>
            <legend class="screen_out">답글 작성</legend>
            <div class="box_evaluation">
              <div class="group_review">
                <label for="tfReview" class="screen_out">답글 작성</label>
                <textarea
                  v-model="replyText"
                  id="tfReview"
                  name="reviewText"
                  class="tf_review"
                  placeholder="작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한 다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아 작성해 주세요."
                ></textarea>
              </div>
              <div class="group_etc">
                <a href="#none" target="_blank" class="link_notice"
                  >답글 작성 시 주의사항</a
                >
                <span class="num_letter">
                  <span class="txt_len"></span
                  ><span class="num_total"
                    >작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한
                    다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아
                    작성해 주세요."</span
                  ></span
                >
              </div>
            </div>
            <div class="group_btn">
              <button type="reset" class="btn_reset" @click="closeReplyModal">
                취소
              </button>
              <button type="submit" class="btn_submit" @click="submitReply">
                등록
              </button>
            </div>
          </fieldset>
        </form>
      </div>
    </div>
  </div>

  <!-- Edit Reply Modal -->
  <div
    v-if="showReplyEditModal"
    data-root=""
    class="evaluation_layer"
    :style="{ top: modalTop }"
  >
    <div class="inner_layer">
      <div class="layer_head">
        <strong class="tit_layer">{{ store.storeName }}</strong>
      </div>
      <div class="layer_body">
        <form action="#">
          <fieldset>
            <legend class="screen_out">답글 수정</legend>
            <div class="box_evaluation">
              <div class="group_review">
                <label for="tfReview" class="screen_out">답글 수정</label>
                <textarea
                  v-model="editReplyText"
                  id="tfReview"
                  name="reviewText"
                  class="tf_review"
                  placeholder="작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한 다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아 작성해 주세요."
                ></textarea>
              </div>
              <div class="group_etc">
                <a href="#none" target="_blank" class="link_notice"
                  >답글 작성 시 주의사항</a
                >
                <span class="num_letter">
                  <span class="txt_len"></span
                  ><span class="num_total"
                    >작성내용은 마이페이지와 장소상세에 노출되며 매장주를 포함한
                    다른 사용자들이 볼 수 있으니, 서로를 배려하는 마음을 담아
                    작성해 주세요."</span
                  ></span
                >
              </div>
            </div>
            <div class="group_btn">
              <button
                type="reset"
                class="btn_reset"
                @click="closeReplyEditModal"
              >
                취소
              </button>
              <button type="submit" class="btn_submit" @click="updateReply">
                등록
              </button>
            </div>
          </fieldset>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from "vue";
import { useAuthStore } from "../stores/auth";
import { useAdminStore } from "../stores/admin";
import api from "../axios"; // Centralized Axios instance import

export default {
  name: "StoreContent",
  props: {
    id: {
      // route param 'id' as a prop
      type: Number,
      required: true,
    },
  },

  setup(props) {
    const store = ref({});
    const reviews = ref([]);
    const authStore = useAuthStore();
    const adminStore = useAdminStore();
    const user = computed(() => authStore.user);
    const isAdmin = computed(() => user.value && user.value.roleId === 2);
    const isOwner = computed(
      () => user.value && user.value.memberId === store.value.ownerMemberId
    );
    const starWidth = computed(() => `${(store.value.ratingAvg / 5) * 100}%`);

    // Format Rating Method
    const formatRating = (rating) => {
      if (typeof rating !== "number") return "0.0";
      return rating.toFixed(1);
    };

    // Review Form
    const newReview = ref({
      rating: 5,
      reviewText: "",
    });

    // Modals
    const showReplyModal = ref(false);
    const showEditModal = ref(false);
    const showReplyEditModal = ref(false);
    const currentReview = ref(null);
    const replyText = ref("");
    let editRating = ref(0);
    const editReviewText = ref("");
    const editReplyText = ref("");
    const modalTop = ref("50%");
    const isMenuOpen = ref(false); // 메뉴 열림 여부 상태

    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value; // 클릭 시 상태 변경
    };


    // // 해당 리뷰의 메뉴 열림/닫힘 상태를 토글
    // const toggleMenu = (review) => {
    //   review.isMenuOpen = !review.isMenuOpen;
    // };

    // Function to construct image URLs
    const getImageUrl = (path) => {
      const serverHost = "http://localhost:8090"; // Update if different
      return serverHost + path;
    };

    // Fetch Store Information
    const fetchStoreInfo = async () => {
      try {
        const response = await api.get(`/store/${props.id}`);
        store.value = response.data;

        // Fetch Menus
        await fetchMenus();

        // Fetch Reviews
        await fetchReviews();
      } catch (error) {
        console.error("Error fetching store info:", error);
      }
    };

    // Fetch Menus
    const fetchMenus = async () => {
      try {
        const response = await api.get(`/menu/${props.id}`);
        store.value.menus = response.data; // Assign to store.menus
      } catch (error) {
        console.error("Error fetching menus:", error);
      }
    };

    // Fetch Reviews
    const fetchReviews = async () => {
      try {
        const response = await api.get(`/reviews/${props.id}`);
        reviews.value = response.data;
      } catch (error) {
        console.error("Error fetching reviews:", error);
      }
    };

    // Format Time
    const formatTime = (time) => {
      if (!time) return "";
      const [hours, minutes] = time.split(":");
      return `${hours}:${minutes}`;
    };

    // Format Price
    const formatPrice = (price) => {
      return `${price.toLocaleString()}원`;
    };

    // Format Date
    const formatDate = (date) => {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = `${d.getMonth() + 1}`.padStart(2, "0");
      const day = `${d.getDate()}`.padStart(2, "0");
      return `${year}.${month}.${day}.`;
    };

    // Check if current user is the review author
    const isCurrentUser = (memberId) => {
      return user.value && user.value.memberId === memberId;
    };

    // Handle Admin Actions
    const handleAdminAction = (action) => {
      if (action === "modify") {
        // Redirect to modify page or open a modal
        window.location.href = `/admin/store/${store.value.storeId}/modify`;
      }
    };

    // Delete Store
    const handleDeleteStore = async () => {
      if (confirm("가게를 삭제하시겠습니까?")) {
        try {
          await api.delete(`/admin/stores/${store.value.storeId}`);
          alert("가게가 성공적으로 삭제되었습니다.");
          window.location.href = "/admin/suggestion/list"; // Redirect to admin list page
        } catch (error) {
          console.error("Error deleting store:", error);
          alert("가게 삭제 중 오류가 발생했습니다.");
        }
      }
    };

    // Submit Review
    const submitReview = async () => {
      if (!newReview.value.reviewText) {
        alert("후기를 작성해주세요.");
        return;
      }

      try {
        await api.post(`/review/${store.value.storeId}`, newReview.value);
        alert("후기가 성공적으로 작성되었습니다.");
        newReview.value.rating = 5;
        newReview.value.reviewText = "";
        await fetchReviews();
      } catch (error) {
        console.error("Error submitting review:", error);
        alert("후기 작성 중 오류가 발생했습니다.");
      }
    };

    // Reset Review Form
    const resetReview = () => {
      newReview.value.rating = 5;
      newReview.value.reviewText = "";
    };

    // Delete Review
    const deleteReview = async (reviewId) => {
      if (confirm("후기를 삭제하시겠습니까?")) {
        try {
          await api.delete(`/reviews/${store.value.storeId}/${reviewId}`);
          alert("후기가 성공적으로 삭제되었습니다.");
          await fetchReviews();
        } catch (error) {
          console.error("Error deleting review:", error);
          alert("후기 삭제 중 오류가 발생했습니다.");
        }
      }
    };

    // Open Reply Modal
    const openReplyModal = (review) => {
      if (confirm("답글을 작성하시겠습니까?")) {
        const scrollY = window.scrollY || document.documentElement.scrollTop;
        const viewportHeight = window.innerHeight;
        modalTop.value = `${scrollY + viewportHeight / 2}px`;
        currentReview.value = review;
        replyText.value = "";
        showReplyModal.value = true;
      }
    };

    // Close Reply Modal
    const closeReplyModal = () => {
      showReplyModal.value = false;
      replyText.value = "";
      currentReview.value = null;
    };

    // Submit Reply
    const submitReply = async () => {
      if (!replyText.value) {
        alert("답글을 작성해주세요.");
        return;
      }

      try {
        await api.post(`/reply/${currentReview.value.reviewId}`, {
          reviewId: currentReview.value.reviewId,
          ownerId: user.value.memberId, // Set ownerId as current user's memberId
          replyText: replyText.value,
        });
        alert("답글이 성공적으로 작성되었습니다.");
        closeReplyModal();
        await fetchReviews();
      } catch (error) {
        console.error("Error submitting reply:", error);
        alert("답글 작성 중 오류가 발생했습니다.");
      }
    };

    // Close Edit Review Modal
    const closeEditModal = () => {
      showEditModal.value = false;
      editReviewText.value = "";
      currentReview.value = null;
    };

    // Update Review
    const updateReview = async (event) => {
      // page reload 방지
      event.preventDefault();

      if (!editReviewText.value) {
        alert("후기를 작성해주세요.");
        return;
      }

      try {
        await api.patch(
          `/reviews/${store.value.storeId}/${currentReview.value.reviewId}`,
          {
            reviewId: currentReview.value.reviewId,
            rating: newReview.value.rating, // Maintain existing rating
            reviewText: editReviewText.value,
          });
        alert("후기가 성공적으로 수정되었습니다.");
        closeEditModal();
        await fetchReviews();
      } catch (error) {
        console.error("Error updating review:", error);
        alert("후기 수정 중 오류가 발생했습니다.");
      }
    };

    // Open Edit Reply Modal
    const openReplyEditModal = (review) => {
      if (confirm("답글을 수정하시겠습니까?")) {
        const scrollY = window.scrollY || document.documentElement.scrollTop;
        const viewportHeight = window.innerHeight;
        modalTop.value = `${scrollY + viewportHeight / 2}px`;
        currentReview.value = review;
        editReplyText.value = review.replyText;
        showReplyEditModal.value = true;
      }
    };

    // Close Edit Reply Modal
    const closeReplyEditModal = () => {
      showReplyEditModal.value = false;
      editReplyText.value = "";
      currentReview.value = null;
    };

    // Update Reply
    const updateReply = async (event) => {
      // page reload 방지
      event.preventDefault();
      if (!editReplyText.value) {
        alert("답글을 작성해주세요.");
        return;
      }

      try {
        await api.patch(
          `/reply/${currentReview.value.reviewId}/${currentReview.value.replyId}`,
          {
            replyId: currentReview.value.replyId,
            reviewId: currentReview.value.reviewId,
            replyText: editReplyText.value,
            updatedAt: new Date(),
          }
        );
        alert("답글이 성공적으로 수정되었습니다.");
        closeReplyEditModal();
        await fetchReviews();
      } catch (error) {
        console.error("Error updating reply:", error);
        alert("답글 수정 중 오류가 발생했습니다.");
      }
    };

    // Delete Reply
    const deleteReply = async (replyId) => {
      if (confirm("답글을 삭제하시겠습니까?")) {
        try {
          await api.delete(`/reply/${replyId}`);
          alert("답글이 성공적으로 삭제되었습니다.");
          await fetchReviews();
        } catch (error) {
          console.error("Error deleting reply:", error);
          alert("답글 삭제 중 오류가 발생했습니다.");
        }
      }
    };

    // Initialize Map
    const initMap = () => {
      const { kakao } = window;
      if (!kakao) {
        console.error("Kakao Maps SDK not loaded.");
        return;
      }

      const storeLocation = `${store.value.roadAddress} ${store.value.detailAddress}`;
      const mapContainer = document.getElementById("map"); // The div to display the map
      const mapOption = {
        center: new kakao.maps.LatLng(37.564214, 126.977943), // Temporary center coordinates
        level: 3,
      };

      const map = new kakao.maps.Map(mapContainer, mapOption);
      const geocoder = new kakao.maps.services.Geocoder();

      geocoder.addressSearch(storeLocation, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          const marker = new kakao.maps.Marker({
            map: map,
            position: coords,
          });
          const infowindow = new kakao.maps.InfoWindow({
            content: `<div style="width:150px;text-align:center;padding:6px 0;">${store.value.storeName}</div>`,
          });
          infowindow.open(map, marker);
          map.setCenter(coords);
        }
      });
    };

    const forbiddenText = "씨발";
    const forbiddenText2 = "병신";
    const forbiddenText3 = "새끼";
    const forbiddenText4 = "좆";

    // 금지어 검사 함수
    const checkForbiddenText = (text) => {
      let pos = text.indexOf(forbiddenText);
      if (pos >= 0) {
        alert("욕설 비방 관련된 글은 입력할 수 없습니다.");
        return text.substr(0, pos);
      }

      pos = text.indexOf(forbiddenText2);
      if (pos >= 0) {
        alert("욕설 비방 관련된 글은 입력할 수 없습니다.");
        return text.substr(0, pos);
      }

      pos = text.indexOf(forbiddenText3);
      if (pos >= 0) {
        alert("욕설 비방 관련된 글은 입력할 수 없습니다.");
        return text.substr(0, pos);
      }

      pos = text.indexOf(forbiddenText4);
      if (pos >= 0) {
        alert("욕설 비방 관련된 글은 입력할 수 없습니다.");
        return text.substr(0, pos);
      }

      return text;
    };

    // reviewText,editReviewText,replyText,editReplyText 금지어 체크
    watch(
      () => newReview.value.reviewText,
      (newValue) => {
        newReview.value.reviewText = checkForbiddenText(newValue);
      }
    );

    watch(editReviewText, (newValue) => {
      editReviewText.value = checkForbiddenText(newValue);
    });
    watch(replyText, (newValue) => {
      replyText.value = checkForbiddenText(newValue);
    });
    watch(editReplyText, (newValue) => {
      editReplyText.value = checkForbiddenText(newValue);
    });

    const openEditModal = (review) => {
      if (confirm("후기를 수정하시겠습니까?")) {
        const scrollY = window.scrollY || document.documentElement.scrollTop;
        const viewportHeight = window.innerHeight;
        // 모달이 화면 중앙에 위치하도록 설정
        modalTop.value = `${scrollY + viewportHeight / 2}px`;
        currentReview.value = review;
        newReview.value.rating = review.rating;
        editReviewText.value = review.reviewText;
        showEditModal.value = true;
      }
    };

    onMounted(async () => {
      await fetchStoreInfo();
      initMap();
    });

    return {
      store,
      reviews,
      user,
      isAdmin,
      isOwner,
      starWidth,
      submitReview,
      resetReview,
      deleteReview,
      openReplyModal,
      closeReplyModal,
      submitReply,
      openEditModal,
      closeEditModal,
      updateReview,
      openReplyEditModal,
      closeReplyEditModal,
      updateReply,
      deleteReply,
      handleAdminAction,
      handleDeleteStore,
      formatTime,
      formatPrice,
      formatDate,
      formatRating,
      isCurrentUser,
      showReplyModal,
      showEditModal,
      showReplyEditModal,
      replyText,
      editReviewText,
      editReplyText,
      getImageUrl,
      modalTop,
      editRating,
      toggleMenu,
      isMenuOpen,
      newReview,
    };
  },
};
</script>

<style scoped>
@import "../../src/assets/css/content.css";
</style>
