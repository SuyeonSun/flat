<script setup>
import {onMounted} from "vue";

onMounted(() => {
  const script = document.createElement("script");
  script.src =
    "https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=osz1qut3m0";
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);

  let map;

  script.onload = () => {
    // 네이버 지도 생성
    map = new window.naver.maps.Map("map", {
      center: new window.naver.maps.LatLng(37.5670135, 126.9783740),
      zoom: 10
    });
  };

  let marker = new naver.maps.Marker({
    position: new naver.maps.LatLng(37.5670135, 126.9783740),
    map: map
  })
})
</script>

<template>
  <q-page class="q-pa-lg">
    <div id="map" style="width:500px; height:500px"></div>
  </q-page>
</template>
