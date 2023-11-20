<template>
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

  <div class="row items-center q-mb-md">
    <div class="col-2">주소</div>
    <q-input
      class="col-4"
      v-model="selectedAddress.address"
      @click="handleSearchAddressDialog"
      dense
      outlined
      readonly
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
</template>
