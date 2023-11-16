<script setup>
import {onMounted, watch} from "vue";
import {usePropertyStore} from "stores/property/property-store";
import {storeToRefs} from "pinia";

const propertyStore = usePropertyStore();
const {mapList} = storeToRefs(propertyStore);

let map;
let markers = [];

onMounted(async () => {
  await propertyStore.getMapList();

  /*
  const script = document.createElement("script");
  script.src =
    "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=osz1qut3m0";
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);

  script.onload = () => {
    // 네이버 지도 생성
    map = new window.naver.maps.Map("map", {
      center: new window.naver.maps.LatLng(37.5670135, 126.9783740),
      zoom: 10
    });

    for (let map in mapList) {
      console.log(map.lat + "=============")
    }

    // 마커
    // let marker1 = new naver.maps.Marker({
    //   position: new naver.maps.LatLng(37.360343510097, 127.11000482191),
    //   map: map
    // });
    // markers.push(marker1)
  };
  */
})

watch(() => mapList.value, (newVal, oldVal) => {
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
</script>

<template>
  <q-page class="q-pa-lg">
    <div id="map" style="width:500px; height:500px"></div>
  </q-page>
</template>
