<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted, onUnmounted, ref, watch} from "vue";
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
    size: 8
  }
  const searchPayload = {
    tradeTypeName: tradeTypeName.value.value,
    direction: direction.value.value,
    searchKeyword: searchKeyword.value
  }
  propertyStore.getPropertyList(pageablePayload, searchPayload);
})

onUnmounted(() => {
  propertyList.value = []
})

watch(() => current.value, (newVal, oldVal) => {
  const pageablePayload = {
    page: newVal - 1,
    size: 8
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
    size: 8
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

const slide = ref(1);
</script>

<template>
  <div >
    <q-carousel
      swipeable
      animated
      v-model="slide"
      thumbnails
      infinite
      :autoplay="1500"
    >
      <q-carousel-slide :name="1" img-src="images/wallpaper2.jpg" />
      <q-carousel-slide :name="2" img-src="images/wallpaper3.jpg" />
      <q-carousel-slide :name="3" img-src="images/wallpaper4.jpg" />
      <q-carousel-slide :name="4" img-src="images/wallpaper6.jpg" />
    </q-carousel>
  </div>

  <q-page class="q-py-xl" style="padding-left: 200px; padding-right: 200px">
    <!--검색-->
    <div class="row justify-center">
      <div style="width: 86%">
        <div class="row justify-between items-center">
          <div>
            <q-btn size="md" @click="goToRegisterPage" style="background-color: #5bc500; color: white">방 내놓기</q-btn>
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
      </div>
    </div>

    <!-- 목록 -->
    <div class="row justify-center">
      <div v-for="property in propertyList" class="q-ma-md" style="min-width: 300px; max-width: 300px">
        <div @click="goToDetailPage(property.id)" style="min-height: 350px" class="property-card">
          <q-img :src="property.image === null ? 'https://flat-bucket.s3.ap-northeast-2.amazonaws.com/6b1b1562-2cb2-4469-940d-112b3837300b.PNG' : property.image" style="max-width: 300px; max-height: 190px;">
            <q-badge
              v-if="property.tradeTypeName === '월세'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
              <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
            </q-badge>
            <q-badge
              v-if="property.tradeTypeName === '전세'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
              <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
            </q-badge>
            <q-badge
              v-if="property.tradeTypeName === '매매'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
              <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
            </q-badge>
          </q-img>

          <q-card-section class="q-px-none">
            <h6 class="q-ma-none q-mb-xs text-weight-bold">{{ property.rentPrc }}</h6>
            <div class="q-mb-sm sub-text">{{ property.address }}</div>
            <div class="sub-text">{{property.direction}}, {{ property.floorInfo }}층, 관리비 {{property.averageCommonPrice}} 만원</div>
          </q-card-section>
        </div>
      </div>
    </div>

    <div class="q-pt-xl flex flex-center">
      <q-pagination
        v-model="current"
        :max="pageable.totalPages"
        direction-links
        :max-pages="5"
        flat
        color="black"
      />
    </div>
  </q-page>
</template>

<style scoped lang="scss">
.property-card {
  cursor: pointer;
}

.sub-text {
  color: $text-grey;
}

.search-btn {
  background-color: $dark-blue;
  color: white;
}
</style>

