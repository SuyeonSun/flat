<script setup>
import {ref} from "vue";
import {useCommonStore} from "stores/common/common-store";
import {usePropertyStore} from "stores/property/property-store";
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

const authStore = useAuthStore();
const commonStore = useCommonStore();
const propertyStore = usePropertyStore();

const {email} = storeToRefs(authStore);

const title = ref(undefined);
const file = ref(undefined);
const address = ref(undefined);
const buildingName = ref(undefined);
const floorInfo = ref(undefined);
const lat = ref(undefined);
const lng = ref(undefined);
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


const register = async () => {
  const fileUrl = await commonStore.uploadFile(file.value);
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
        label="제목"
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
        label="주소"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">동</div>
      <q-input
        class="col-4"
        v-model="buildingName"
        label="동"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">층</div>
      <q-input
        class="col-4"
        v-model="floorInfo"
        label="층"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">태그</div>
      <q-input
        class="col-4"
        v-model="tagList"
        label="태그"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">매물 특징</div>
      <q-input
        class="col-4"
        v-model="articleFeatureDesc"
        label="매물 특징"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">거래 유형</div> <!-- TODO: select box: 월세 전세 매매-->
      <q-input
        class="col-4"
        v-model="tradeTypeName"
        label="거래 유형"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">가격</div>
      <q-input
        class="col-4"
        v-model="rentPrc"
        label="가격"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">방향</div> <!-- TODO: select box: 남향, 남동향, 남서향, 동향, 서향, 북향 -->
      <q-input
        class="col-4"
        v-model="direction"
        label="방향"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">전용 면적</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="area1"
        label="전용 면적"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">공용 면적</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="area2"
        label="공 면적"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">방 개수</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="roomCnt"
        label="방 개수"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">화장실 개수</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="bathroomCnt"
        label="화장실 개수"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">관리비</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="averageCommonPrice"
        label="관리비"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">전기 요금</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="averageEtcPrice"
        label="전기 요금"
        dense
        outlined
      />
    </div>

    <div class="row items-center q-mb-md">
      <div class="col-2">난방비</div> <!-- TODO: 숫자로 변경-->
      <q-input
        class="col-4"
        v-model="averageHeatPrice"
        label="난방비"
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
