<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {storeToRefs} from "pinia";

const $route = useRoute();
const propertyStore = usePropertyStore();
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
const email = ref(undefined);

onMounted(async () => {
  // http://localhost:8080/property/352
  const propertyId = $route.params.propertyId;
  await propertyStore.getPropertyDetail(propertyId);

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
  email.value = propertyDetail.value.email;
})

</script>

<template>
  <q-page class="q-pa-xl">
    <div class="row justify-between">
      <q-card class="property-info-card">
        <q-card-section>
          <!-- 제목 -->
          <h6 class="q-ma-none q-mb-md">{{ title }}</h6>
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

      <div class="user-info-card">
        <q-card>
          <q-list bordered separator>
            <q-item>
              <q-item-section>게시자: {{ name }}</q-item-section>
            </q-item>
            <q-item>
              <q-item-section>이메일: {{ email }}</q-item-section>
            </q-item>
          </q-list>
        </q-card>
      </div>
    </div>

    <div>
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn fab icon="headset" style="background-color: #F2C4CE;"/>
      </q-page-sticky>
    </div>

  </q-page>
</template>

<style scoped>
.property-info-card {
  width: 100%;
  max-width: 55%;
}

.user-info-card {
  width: 100%;
  max-width: 40%;
}
</style>
