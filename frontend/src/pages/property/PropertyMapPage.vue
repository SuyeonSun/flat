<script setup>
import {onMounted, ref, watch} from "vue";
import {usePropertyStore} from "stores/property/property-store";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const $router = useRouter();

const propertyStore = usePropertyStore();
const {mapList} = storeToRefs(propertyStore);

let map;
let markers = [];

onMounted(async () => {
  const searchPayload = {
    address: address.value,
    tradeTypeName: tradeTypeName.value.value
  }
  await propertyStore.getMapList(searchPayload);
})

watch(() => mapList.value, (newVal, oldVal) => {
  mapList.value = newVal;
  const script = document.createElement("script");
  script.src =
    "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=osz1qut3m0";
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);

  script.onload = () => {
    const center = [newVal[0].lat, newVal[0].lng]

    // 네이버 지도 생성
    map = new window.naver.maps.Map("map", {
      center: new window.naver.maps.LatLng(center[0], center[1]),
      zoom: 15
    });

    // for (let element in newVal) {
    //   console.log(element + "===============")
    // }
    // forEach 문으로 돌리기
    newVal.forEach((element) => {
      // 마커
      let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(element.lat, element.lng),
        map: map
      });
      markers.push(marker);
    })
  };
})

const contentStyle = {
  // backgroundColor: 'rgba(0,0,0,0.02)',
  color: '#555'
}

const contentActiveStyle = {
  // backgroundColor: '#eee',
  color: 'black'
}

const thumbStyle = {
  right: '2px',
  borderRadius: '5px',
  backgroundColor: '#027be3',
  width: '5px',
  opacity: '0.75'
}

const tradeTypeOptions = [{label: "전체", value: ""}, {label: "월세", value: "월세"}, {
  label: "전세",
  value: "전세"
}, {label: "매매", value: "매매"}];
const address = ref("")
const tradeTypeName = ref(tradeTypeOptions[0])

watch(() => tradeTypeName.value, (newVal, oldVal) => {
  const searchPayload = {
    address: address.value,
    tradeTypeName: newVal.value
  }
  propertyStore.getMapList(searchPayload);
})

watch(() => address.value, (newVal, oldVal) => {
  const searchPayload = {
    address: newVal,
    tradeTypeName: tradeTypeName.value.value
  }
  propertyStore.getMapList(searchPayload);
})

const clickProperty = (mapId) => {
  $router.push(`/property/${mapId}`)
}
</script>

<template>
  <q-page>
    <div>
      <!-- 검색 바-->
      <!-- address, tradeTypeName -->
      <div class="q-pa-lg">
        <div class="row">
          <q-select outlined v-model="tradeTypeName" :options="tradeTypeOptions" stack-label dense class="q-mr-sm"
                    style="width: 100px" label="거래 유형"/>
          <q-input
            v-model="address"
            dense
            outlined
            label="검색할 매물 주소"
          >
            <template v-slot:append>
              <q-icon name="search"/>
            </template>
          </q-input>
        </div>
        <div class="row justify-between">
          <q-item-label caption class="q-mt-xs">검색 조건을 변경 시, 자동 검색 됩니다.</q-item-label>
          <div style="color: #117CE9;">총 {{ mapList.length }}개의 검색 결과</div>
        </div>
      </div>

      <!-- 컨텐트 -->
      <div class="row">
        <!-- 지도 -->
        <div class="col-8">
          <div id="map" style="height: 100vh"></div>
        </div>
        <!-- 목록 -->
        <div class="col-4" style="height: 100vh">
          <q-scroll-area
            :thumb-style="thumbStyle"
            :content-style="contentStyle"
            :content-active-style="contentActiveStyle"
            class="full-height"
          >
            <q-list bordered separator>
              <q-item v-for="(map, idx) in mapList" clickable v-ripple @click="clickProperty(map.id)">
                <q-item-section>
                  <!-- address, buildingName, floorInfo-->
                  <div class="text-bold">
                    {{ map.address }} {{ map.buildingName }}
                  </div>
                  <!-- rentPrc -->
                  <div class="row">
                    <div>
                      <div
                        v-if="map.tradeTypeName === '월세'">
                        <div class="text-weight-bolder">{{ map.tradeTypeName }}</div>
                      </div>
                      <div
                        v-if="map.tradeTypeName === '전세'">
                        <div class="text-weight-bolder">{{ map.tradeTypeName }}</div>
                      </div>
                      <div
                        v-if="map.tradeTypeName === '매매'">
                        <div class="text-weight-bolder">{{ map.tradeTypeName }}</div>
                      </div>
                    </div>
                    <div class="q-ml-sm">{{map.rentPrc}}</div>
                  </div>
                  <q-item-label caption> {{ map.floorInfo }}층 {{ map.area1 }}/{{ map.area2 }}㎡ {{ map.direction }}</q-item-label>
                  <q-item-label caption>{{map.tagList}}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-scroll-area>
        </div>
      </div>
    </div>

  </q-page>
</template>
