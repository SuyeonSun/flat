<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {storeToRefs} from "pinia";
import {useChatStore} from "stores/chat/chat-store";
import chatRoom from "pages/chat/ChatRoomDetail.vue";
import {useAuthStore} from "stores/auth/auth-store";

const chatStore = useChatStore();
const authStore = useAuthStore();

const $route = useRoute();
const propertyStore = usePropertyStore();
const {email} = storeToRefs(authStore);
const {propertyDetail} = storeToRefs(propertyStore);

const title = ref(undefined);
const image = ref(undefined);
const address = ref(undefined);
const buildingName = ref(undefined);
const floorInfo = ref(undefined);
const tagList = ref(undefined);
const articleFeatureDesc = ref(undefined);
const tradeTypeName = ref(undefined);
const rentPrc = ref(undefined);
const direction = ref(undefined);
const area1 = ref(undefined);
const area2 = ref(undefined);
const roomCnt = ref(undefined);
const bathroomCnt = ref(undefined);
const averageCommonPrice = ref(undefined);
const averageEtcPrice = ref(undefined);
const averageHeatPrice = ref(undefined);

const name = ref(undefined);
const writerEmail = ref(undefined);
const phoneNumber = ref(undefined);

const isUserLiked = ref(false);
const likeCount = ref(0);

onMounted(async () => {
  // http://localhost:8080/property/502
  const propertyId = $route.params.propertyId;
  await propertyStore.getPropertyDetail(propertyId, email.value);

  title.value = propertyDetail.value.title;
  image.value = propertyDetail.value.image;
  address.value = propertyDetail.value.address;
  buildingName.value = propertyDetail.value.buildingName;
  floorInfo.value = propertyDetail.value.floorInfo;
  tagList.value = propertyDetail.value.tagList.split(" ");
  articleFeatureDesc.value = propertyDetail.value.articleFeatureDesc;
  tradeTypeName.value = propertyDetail.value.tradeTypeName;
  rentPrc.value = propertyDetail.value.rentPrc;
  direction.value = propertyDetail.value.direction;
  area1.value = propertyDetail.value.area1;
  area2.value = propertyDetail.value.area2;
  roomCnt.value = propertyDetail.value.roomCnt;
  bathroomCnt.value = propertyDetail.value.bathroomCnt;
  averageCommonPrice.value = propertyDetail.value.averageCommonPrice;
  averageEtcPrice.value = propertyDetail.value.averageEtcPrice;
  averageHeatPrice.value = propertyDetail.value.averageHeatPrice;

  name.value = propertyDetail.value.name;
  writerEmail.value = propertyDetail.value.email;
  phoneNumber.value = propertyDetail.value.phoneNumber;

  isUserLiked.value = propertyDetail.value.isUserLiked;
  likeCount.value = propertyDetail.value.likeCount;
})

const clickLikeBtn = async (status) => {
  const likePayload = {
    email: email.value,
    isLike: status,
    propertyId: Number($route.params.propertyId)
  }
  const response = await propertyStore.like(likePayload);
  likeCount.value = response.likeCount;
  isUserLiked.value = response.isUserLiked;
}
</script>

<template>
  <q-page class="q-pa-xl row justify-center">
    <div style="min-width: 50%">
      <h5 class="q-ma-none q-mb-md">{{ title }}</h5>
      <div class="user-info-card q-mb-sm">
        <q-card>
          <q-list bordered separator>
            <q-item>
              <q-item-section>게시자: {{ name }}</q-item-section>
            </q-item>
            <q-item>
              <q-item-section>이메일: {{ writerEmail }}</q-item-section>
            </q-item>
            <q-item>
              <q-item-section>전화번호: {{ phoneNumber }}</q-item-section>
            </q-item>
          </q-list>
        </q-card>
      </div>

      <q-card class="property-info-card">
        <q-card-section>
          <div class="row justify-between items-center q-mb-md">
            <div>
              <span v-if="isUserLiked" class="q-mr-sm">
                <q-icon name="favorite" size="sm" @click="clickLikeBtn(false)"/>
              </span>
              <span v-else class="q-mr-sm">
                <q-icon name="favorite_border" size="sm" @click="clickLikeBtn(true)"/>
              </span>
              <span>좋아요 {{likeCount}}개</span>
            </div>
          </div>
          <!-- 이미지 -->
          <q-img :src="image" no-native-menu class="q-mb-md">
            <q-icon class="absolute all-pointer-events" size="32px" name="info" color="white"
                    style="top: 8px; left: 8px">
              <q-tooltip>
                매물 이미지
              </q-tooltip>
            </q-icon>
          </q-img>

          <div class="q-mb-md">
            <!-- 주소 -->
            <div class="text-subtitle1">
              {{ address }}  ({{ buildingName }} {{ floorInfo }})
            </div>
            <!-- 태그 -->
            <div class="q-mb-sm">
              <q-badge v-for="tag in tagList" outline align="middle" color="grey" class="q-mr-xs">
                {{tag}}
              </q-badge>
            </div>
            <!-- 매물 특징 -->
            <div>{{ articleFeatureDesc }}</div>
          </div>

          <hr/>

          <div class="q-my-md">
            <!-- 거래 유형 및 가격 -->
            <div>- 거래 유형 및 가격: {{ tradeTypeName }} ({{ rentPrc }})</div>
          </div>

          <div class="q-mb-md">
            <!-- 방향 -->
            <div>- 방향 : {{ direction }}</div>
            <!-- 공급 면적 -->
            <div>- 공급 / 전용 면적 : {{ area1 }} / {{ area2 }} (단위 : 제곱미터)</div>
          </div>

          <div class="q-mb-md">
            <!-- 개수 -->
            <div>- 방 개수 : {{ roomCnt }}</div>
            <div>- 화장실 개수 : {{ bathroomCnt }}</div>
          </div>

          <div class="q-mb-md">
            <!-- 비용 -->
            <div>- 평균 관리비 : {{ averageCommonPrice }} (단위 : 만원)</div>
            <div>- 전기 요금 : {{ averageEtcPrice }} (단위 : 만원)</div>
            <div>- 난방비 : {{ averageHeatPrice }} (단위 : 만원)</div>
          </div>
        </q-card-section>
      </q-card>
    </div>

    <div>
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn fab icon="headset" style="background-color: #F2C4CE;" @click="chatStore.createRoom(authStore.$state.name, name)" />
      </q-page-sticky>
    </div>

  </q-page>

  <template v-if="chatStore.$state.isDialog">
    <chat-room/>
  </template>
</template>

<style scoped>
.property-info-card {
  width: 100%;
  //max-width: 55%;
}

.user-info-card {
  width: 100%;
  //max-width: 40%;
}
</style>
