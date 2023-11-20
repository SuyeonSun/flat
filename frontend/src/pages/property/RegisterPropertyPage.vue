<script setup>
import {ref} from "vue";
import {useS3Store} from "stores/common/s3-store";
import {usePropertyStore} from "stores/property/property-store";
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";
import SearchAddressDialog from "components/property/SearchAddressDialog";
import {useRouter} from "vue-router";

const authStore = useAuthStore();
const s3Store = useS3Store();
const propertyStore = usePropertyStore();

const $router = useRouter();

const {email} = storeToRefs(authStore);

const tradeTypeOptions = ["월세", "전세", "매매"];
const directionOptions = ["남향", "남동향", "남서향", "동향", "서향", "북향"];

const title = ref(undefined);
const file = ref(undefined);
const buildingName = ref(undefined);
const floorInfo = ref(undefined);
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

const selectedAddress = ref({
  address: undefined,
  lat: undefined,
  lng: undefined
})

const updateSelectedAddress = (value) => {
  selectedAddress.value.address = value.address;
  selectedAddress.value.lat = value.lat;
  selectedAddress.value.lng = value.lng;
}

const isOpenSearchAddressDialog = ref(false);
const handleSearchAddressDialog = () => {
  isOpenSearchAddressDialog.value = !isOpenSearchAddressDialog.value;
}

const register = async () => {
  const fileUrl = await s3Store.uploadFile(file.value);
  const registerPayload = {
    title: title.value,
    image: fileUrl,
    buildingName: buildingName.value,
    floorInfo: floorInfo.value,
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
    averageHeatPrice: averageHeatPrice.value,
    address: selectedAddress.value.address,
    lat: selectedAddress.value.lat,
    lng: selectedAddress.value.lng,
  }
  await propertyStore.registerProperty(email.value, registerPayload);
  $router.push("/property/list");
}
</script>

<template>
  <q-page class="q-pa-xl row justify-center">
    <div style="width: 70%">
      <div>
        <div class="row items-center justify-between q-mb-md">
          <h5 class="q-ma-none q-mb-md">매물 등록</h5>
          <q-btn outline dense label="등록" @click="register" class="register-btn"/>
        </div>

        <!-- 기본 정보 -->
        <q-card bordered flat class="q-mb-lg card q-pa-lg">
          <div style="font-size: 18px; font-weight: bold">기본 정보</div>
          <!-- 주소 -->
          <div class="row items-center col-12 q-mb-sm">
            <div class="col-3">주소</div>
            <q-input
              v-model="selectedAddress.address"
              @click="handleSearchAddressDialog"
              dense
              outlined
              readonly
              label="주소 검색"
            />
          </div>
          <!-- 동, 층 -->
          <div class="row col-12">
            <div class="row items-center col-6">
              <div class="col-6">동</div>
              <q-input
                v-model="buildingName"
                dense
                outlined
                placeholder="예) 1동"
              />
            </div>
            <div class="row items-center col-6">
              <div class="col-6">층</div>
              <q-input
                v-model="floorInfo"
                dense
                outlined
                placeholder="예) 9/19"
              />
            </div>
          </div>
        </q-card>

        <!-- 설명 -->
        <q-card bordered flat class="q-mb-lg q-pa-lg card">
          <div style="font-size: 18px; font-weight: bold">설명</div>
          <!-- 제목 -->
          <div class="row items-center col-12 q-mb-sm">
            <div class="col-3">제목</div>
            <q-input
              v-model="title"
              dense
              outlined
              placeholder="조용한 위치"
            />
          </div>
          <!-- 이미지 -->
          <div class="row items-center col-12 q-mb-sm">
            <div class="col-3">이미지</div>
            <q-file
              v-model="file"
              outlined
              dense
              label="이미지 등록"
            />
          </div>
          <!-- 태그 -->
          <div class="row items-center col-12 q-mb-sm">
            <div class="col-3">태그</div>
            <div>
              <q-input
                v-model="tagList"
                dense
                outlined
                placeholder="조용한 역근처 신축"
              />
              <q-item-label caption class="q-mt-xs">띄어쓰기를 기준으로 태그가 구분됩니다.</q-item-label>
            </div>
          </div>
          <!-- 매물 특징 -->
          <div class="row items-center col-12">
            <div class="col-3">매물 특징</div>
            <q-input
              type="textarea"
              class="col-4"
              v-model="articleFeatureDesc"
              dense
              outlined
              placeholder="매물 특징을 입력해 주십시오."
            />
          </div>
        </q-card>

        <!-- 상세 정보 -->
        <q-card bordered flat class="q-mb-lg q-pa-lg card">
          <div style="font-size: 18px; font-weight: bold">상세 정보</div>
          <!-- 거래 유형 -->
          <!-- 방향 -->
          <div class="row col-12 q-mb-sm">
            <div class="row items-center col-6">
              <div class="col-6">거래 유형</div>
              <q-select class="col-4" outlined v-model="tradeTypeName" :options="tradeTypeOptions" stack-label dense />
            </div>
            <div class="row items-center col-6">
              <div class="col-6">방향</div>
              <q-select class="col-4" outlined v-model="direction" :options="directionOptions" stack-label dense />
            </div>
          </div>
          <!-- 가격 -->
          <div class="row items-center col-12 q-mb-sm">
            <div class="col-3">가격</div>
            <q-input
              v-model="rentPrc"
              dense
              outlined
              placeholder="예) 1억 3천만"
            />
          </div>
          <!-- 공용 면적, 전용 면적 -->
          <div class="row col-12 q-mb-sm">
            <div class="row items-center col-6">
              <div class="col-6">전용 면적 (제곱 미터)</div>
              <q-input
                v-model="area1"
                type="number"
                dense
                outlined
              />
            </div>
            <div class="row items-center col-6">
              <div class="col-6">공용 면적 (제곱 미터)</div>
              <q-input
                v-model="area2"
                type="number"
                dense
                outlined
              />
            </div>
          </div>

          <!-- 방 개수, 화장실 개수 -->
          <div class="row col-12">
            <div class="row items-center col-6">
              <div class="col-6">방 개수</div>
              <q-input
                v-model="roomCnt"
                type="number"
                dense
                outlined
              />
            </div>
            <div class="row items-center col-6">
              <div class="col-6">화장실 개수</div>
              <q-input
                v-model="bathroomCnt"
                type="number"
                dense
                outlined
              />
            </div>
          </div>
        </q-card>

        <!-- 요금 -->
        <q-card bordered flat class="q-mb-lg q-pa-lg card">
          <div style="font-size: 18px; font-weight: bold">요금</div>
          <!-- 관리비 -->
          <!-- 전기 요금 -->
          <!-- 난방비 -->
          <div class="row col-12">
            <div class="row items-center col-4">
              <div class="col-6">관리비 (만원)</div>
              <q-input
                class="col-4"
                v-model="averageCommonPrice"
                type="number"
                dense
                outlined
              />
            </div>
            <div class="row items-center col-4">
              <div class="col-6">전기 요금 (만원)</div>
              <q-input
                class="col-4"
                v-model="averageEtcPrice"
                type="number"
                dense
                outlined
              />
            </div>
            <div class="row items-center col-4">
              <div class="col-6">난방비 (만원)</div>
              <q-input
                class="col-4"
                v-model="averageHeatPrice"
                type="number"
                dense
                outlined
              />
            </div>
          </div>
        </q-card>
      </div>
    </div>

    <search-address-dialog
      :isOpenSearchAddressDialog="isOpenSearchAddressDialog"
      @handleSearchAddressDialog="handleSearchAddressDialog"
      @updateSelectedAddress="updateSelectedAddress"
    ></search-address-dialog>
  </q-page>
</template>

<style scoped>
.card {
}

.register-btn {
  min-width: 70px;
}
</style>
