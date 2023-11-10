<script setup>
import {ref} from "vue";
import {useS3Store} from "stores/common/s3-store";
import {usePropertyStore} from "stores/property/property-store";
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

const authStore = useAuthStore();
const s3Store = useS3Store();
const propertyStore = usePropertyStore();

const {email} = storeToRefs(authStore);

const tradeTypeOptions = ["월세", "전세", "매매"];
const directionOptions = ["남향", "남동향", "남서향", "동향", "서향", "북향"];

const title = ref(undefined);
const file = ref(undefined);
const address = ref(undefined);
const buildingName = ref(undefined);
const floorInfo = ref(undefined);
const lat = ref(undefined);
const lng = ref(undefined);
const tagList = ref(undefined);
const articleFeatureDesc = ref(undefined);
const tradeTypeName = ref(tradeTypeOptions[0]);
const rentPrc = ref(undefined);
const direction = ref(directionOptions[0]);
const area1 = ref(0);
const area2 = ref(0);
const roomCnt = ref(0);
const bathroomCnt = ref(0);
const averageCommonPrice = ref(0);
const averageEtcPrice = ref(0);
const averageHeatPrice = ref(0);


const register = async () => {
  const fileUrl = await s3Store.uploadFile(file.value);
  const registerPayload = {
    title: title.value,
    image: fileUrl,
    address: address.value,
    buildingName: buildingName.value,
    floorInfo: floorInfo.value,
    lat: lat.value,
    lng: lng.value,
    tagList: tagList.value,
    articleFeatureDesc: articleFeatureDesc.value,
    tradeTypeName: tradeTypeName.value,
    rentPrc: rentPrc.value,
    direction: direction.value,
    area1: area1.value,
    area2: area2.value,
    roomCnt: roomCnt.value,
    bathroomCnt: bathroomCnt.value,
    averageCommonPrice: averageCommonPrice.value,
    averageEtcPrice: averageEtcPrice.value,
    averageHeatPrice: averageHeatPrice.value
  }
  await propertyStore.registerProperty(email.value, registerPayload);
  // TODO: 추후 목록 페이지로 이동
}
</script>

<template>
  <q-page class="q-pa-xl">
    <h5 class="q-ma-none q-mb-md">매물 등록</h5>
    <div class="row items-center q-mb-md">
      <div class="col-2">제목</div>
      <q-input
        class="col-4"
        v-model="title"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">이미지</div>
      <q-file
        class="col-4"
        v-model="file"
        label="Pick one file"
        outlined
        dense
      />
    </div>

    <!-- TODO: 추후 API 연동 필요 -->
    <div class="row items-center q-mb-md">
      <div class="col-2">주소</div>
      <q-input
        class="col-4"
        v-model="address"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">동</div>
      <q-input
        class="col-4"
        v-model="buildingName"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">층</div>
      <q-input
        class="col-4"
        v-model="floorInfo"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">태그</div>
      <q-input
        class="col-4"
        v-model="tagList"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">매물 특징</div>
      <q-input
        type="textarea"
        class="col-4"
        v-model="articleFeatureDesc"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">거래 유형</div>
      <q-select outlined class="col-4" v-model="tradeTypeName" :options="tradeTypeOptions" stack-label dense />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">가격</div>
      <q-input
        class="col-4"
        v-model="rentPrc"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">방향</div>
      <q-select outlined class="col-4" v-model="direction" :options="directionOptions" stack-label dense />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">전용 면적 (제곱 미터)</div>
      <q-input
        class="col-4"
        v-model="area1"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">공용 면적 (제곱 미터)</div>
      <q-input
        class="col-4"
        v-model="area2"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">방 개수</div>
      <q-input
        class="col-4"
        v-model="roomCnt"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">화장실 개수</div>
      <q-input
        class="col-4"
        v-model="bathroomCnt"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">관리비 (만원)</div>
      <q-input
        class="col-4"
        v-model="averageCommonPrice"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">전기 요금 (만원)</div>
      <q-input
        class="col-4"
        v-model="averageEtcPrice"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">난방비 (만원)</div>
      <q-input
        class="col-4"
        v-model="averageHeatPrice"
        type="number"
        dense
        outlined
      />
    </div>

    <div class="row items-center justify-end">
      <q-btn outline dense label="등록" @click="register" class="register-btn"/>
    </div>
  </q-page>
</template>

<style scoped>
.input {
  max-width: 300px;
}

.register-btn {
  min-width: 70px;
}
</style>
