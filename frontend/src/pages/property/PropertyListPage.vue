<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted, ref, watch} from "vue";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const $router = useRouter();

const propertyStore = usePropertyStore();

const {propertyList, pageable} = storeToRefs(propertyStore);

const tradeTypeOptions = [{label: "전체", value: ""}, {label: "월세", value: "월세"}, {label: "전세", value: "전세"}, {label: "매매", value: "매매"}];
const directionOptions = [{label: "전체", value: ""}, {label: "남향", value: "남향"}, {label: "남동향", value: "남동향"}, {label: "남서향", value: "남서향"}, {label: "동향", value: "동향"}, {label: "서향", value: "서향"}, {label: "북향", value: "북향"}];

const searchKeyword = ref("");
const tradeTypeName = ref(tradeTypeOptions[0]);
const direction = ref(directionOptions[0]);
const current = ref(1);

onMounted(() => {
  const pageablePayload = {
    page: 0,
    size: 20
  }
  const searchPayload = {
    tradeTypeName: tradeTypeName.value.value,
    direction: direction.value.value,
    searchKeyword: searchKeyword.value
  }
  propertyStore.getPropertyList(pageablePayload, searchPayload);
})

watch(() => current.value, (newVal, oldVal) => {
  const pageablePayload = {
    page: newVal - 1,
    size: 20
  }
  const searchPayload = {
    tradeTypeName: tradeTypeName.value.value,
    direction: direction.value.value,
    searchKeyword: searchKeyword.value
  }
  propertyStore.getPropertyList(pageablePayload, searchPayload);
})

const search = () => {
  const pageablePayload = {
    page: current.value - 1,
    size: 20
  }
  const searchPayload = {
    tradeTypeName: tradeTypeName.value.value,
    direction: direction.value.value,
    searchKeyword: searchKeyword.value
  }
  propertyStore.getPropertyList(pageablePayload, searchPayload);
}

watch(() => tradeTypeName.value, (newVal, oldVal) => {
  tradeTypeName.value = newVal;
})

watch(() => direction.value, (newVal, oldVal) => {
  direction.value = newVal;
})

const goToDetailPage = (id) => {
  $router.push(`/property/${id}`)
}

const goToRegisterPage = () => {
  $router.push("/property/register")
}
</script>

<template>
  <q-page class="q-pa-xl">
    <!-- 검색 -->
    <div class="row justify-between items-center q-mb-lg">
      <div>
        <q-btn size="md" unelevated outline @click="goToRegisterPage">매물 등록</q-btn>
      </div>
      <div class="row">
        <q-select outlined v-model="tradeTypeName" :options="tradeTypeOptions" stack-label dense class="q-mr-sm" style="width: 100px" label="거래 유형"/>
        <q-select outlined v-model="direction" :options="directionOptions" stack-label dense class="q-mr-sm" style="width: 100px" label="방향 유형"/>

        <div class="row">
          <q-input
            v-model="searchKeyword"
            dense
            outlined
            label="검색 키워드"
          >
            <template v-slot:append>
              <q-icon name="search" @click="search"/>
            </template>
          </q-input>
        </div>
      </div>
    </div>

    <!-- 목록 -->
    <div class="row q-col-gutter-x-xl q-col-gutter-y-xl">
      <div v-for="property in propertyList" class="col-3">
        <q-card class="property-card" @click="goToDetailPage(property.id)">
          <q-card-section>
            <q-img
              :src="property.image === null ? 'https://flat-bucket.s3.ap-northeast-2.amazonaws.com/e2560779-3efb-4815-91b9-14e4b1e1afd7.png' : property.image"
              no-native-menu class="q-mb-md">
              <q-badge
                v-if="property.tradeTypeName === '월세'"
                class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #14ADEA">
                <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
              </q-badge>
              <q-badge
                v-if="property.tradeTypeName === '전세'"
                class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #4F5569">
                <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
              </q-badge>
              <q-badge
                v-if="property.tradeTypeName === '매매'"
                class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #444444">
                <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
              </q-badge>
            </q-img>

            <h6 class="q-ma-none q-mb-xs">{{ property.title === null ? "-" : property.title }}</h6>
            <div class="q-mb-sm">{{ property.address }}</div>
            <div class="sub-text">{{ property.articleFeatureDesc }}</div>
          </q-card-section>
        </q-card>
      </div>
    </div>

    <div class="q-pt-xl flex flex-center">
      <q-pagination
        v-model="current"
        :max="pageable.totalPages"
        direction-links
        :max-pages="5"
        flat
        color="grey"
      />
    </div>
  </q-page>
</template>

<style scoped lang="scss">
.property-card {
  min-height: 400px;
}

.sub-text {
  color: $text-grey;
}

.search-btn {
  background-color: $dark-blue;
  color: white;
}
</style>

