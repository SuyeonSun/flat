<script setup>
import {onMounted, ref, watch} from "vue";
import {usePropertyStore} from "stores/property/property-store";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import {useAuthStore} from "stores/auth/auth-store";
import {useUserStore} from "stores/user/user-store";
import {usePoliceStore} from "stores/police/police-store";
import {Notify} from "quasar";

const $router = useRouter();

const propertyStore = usePropertyStore();
const authStore = useAuthStore();
const userStore = useUserStore();
const policeStore = usePoliceStore();

const {mapList} = storeToRefs(propertyStore);
const {email} = storeToRefs(authStore);
const {user, friends} = storeToRefs(userStore); // user.id
const {policeStationLocationList} = storeToRefs(policeStore);

let map;
let markers = ref([]);
let friendsMarkers = ref([]);
let policeStationMarkers = ref([]);
let infoWindows = [];

const isToggle = ref(false);
const isPoliceStationToggle = ref(false);
const isSearch = ref(true);

const selectedMarkerIdx = ref(0);

onMounted(async () => {
  await userStore.getUserInfo(email.value);
  await userStore.findFriends(user.value.id);

  const searchPayload = {
    address: address.value,
    tradeTypeName: tradeTypeName.value.value
  }
  await propertyStore.getMapList(searchPayload);

  await policeStore.getPoliceStationLocation();
})

watch(() => mapList.value, (newVal, oldVal) => {
  markers.value = []
  mapList.value = newVal;
  const script = document.createElement("script");
  script.src =
    `https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${process.env["CLIENT_ID"]}`;
  script.async = true;
  script.defer = true;
  document.head.appendChild(script);

  script.onload = () => {
    if (isSearch.value) {
      const center = [newVal[0].lat, newVal[0].lng]

      // 네이버 지도 생성
      map = new window.naver.maps.Map("map", {
        center: new window.naver.maps.LatLng(center[0], center[1]),
        zoom: 15
      });

      isSearch.value = false
    } else {
      map = new window.naver.maps.Map("map", {
        center: new window.naver.maps.LatLng(map.center._lat, map.center._lng),
        zoom: map.zoom
      })
    }

    // for (let element in newVal) {
    //   console.log(element + "===============")
    // }
    // forEach 문으로 돌리기
    newVal.forEach((element, idx) => {
      // 마커
      let marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(element.lat, element.lng),
        map: map,
        icon: {
          content: selectedMarkerIdx.value === idx ? [`<img src="/icons/selected-pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join("") : [`<img src="/icons/pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
          size: new naver.maps.Size(32, 32),
          anchor: new naver.maps.Point(16, 16),
        }
      });
      // 마커 클릭 시 상세 매물 정보로 이동하기
      let infoWindow = new naver.maps.InfoWindow({
        content:
          `<div style="text-align:center; padding:10px;">
            <div>${element.address} ${element.buildingName}</div>
            <div>${element.tradeTypeName} ${element.rentPrc} ${element.area1}/${element.area2}㎡</div>
            <a href="http://localhost:8080/property/${element.id}" style="text-decoration: none">
               ▶ 매물 상세 보기
            </a>
          </div>`,
        backgroundColor: "#FFFFFF",
        borderColor: "#FFFFFF",
        borderWidth: 5,
      });
      naver.maps.Event.addListener(marker, "click", function (e) {
        // 마커 색상 변경
        selectedMarkerIdx.value = idx;
        markers.value.forEach((marker, i) => {
          if (i === idx) {
            marker.setIcon({
              content: [`<img src="/icons/selected-pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
              size: new naver.maps.Size(32, 32),
              anchor: new naver.maps.Point(16, 16),
            })
          } else {
            marker.setIcon({
              content: [`<img src="/icons/pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
              size: new naver.maps.Size(32, 32),
              anchor: new naver.maps.Point(16, 16),
            })
          }
        })

        if (infoWindow.getMap()) {
          infoWindow.close();
        } else {
          infoWindow.open(map, marker);
        }
      });

      markers.value.push(marker);
      infoWindows.push(infoWindow);
    })

    // isToggle.value가 true라면 친구 marker 추가
    if (isToggle.value) {
      addFriendMarker();
    }

    // 경찰서 마커
    if (isPoliceStationToggle.value) {
      addPoliceStationMarker();
    }
  };
})

watch(() => isToggle.value, async (newVal, oldVal) => {
  // isToggle.value가 true라면 친구 marker 추가
  if (newVal) {
    addFriendMarker();
  } else {
    // 토글 해제 시, 친구 marker 제거
    friendsMarkers.value = []
    const searchPayload = {
      address: address.value,
      tradeTypeName: tradeTypeName.value.value
    }
    await propertyStore.getMapList(searchPayload);
  }
})

watch (() => isPoliceStationToggle.value, async (newVal, oldVal) => {
  if (newVal) {
    addPoliceStationMarker();
  } else {
    policeStationMarkers.value = []
    const searchPayload = {
      address: address.value,
      tradeTypeName: tradeTypeName.value.value
    }
    await propertyStore.getMapList(searchPayload);
  }
})

const addFriendMarker = () => {
  friends.value.forEach((friend) => {
    let content = [
      "<div>",
      `<img src="${friend.profile}" alt="친구 위치" style="height: 30px; width: 30px; border-radius: 70%" />`,
      "</div>",
    ].join("");

    let marker = new naver.maps.Marker({
      position: new naver.maps.LatLng(friend.addressLat, friend.addressLng),
      icon: {
        content: content,
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      },
      map: map
    });

    let infoWindow = new naver.maps.InfoWindow({
      content: `<div style="text-align:center;padding:10px;">
                    <div>${friend.name}</div>
                    <div>${friend.address}</div>
                </div>`,
      backgroundColor: "#FFFFFF",
      borderColor: "#FFFFFF",
      borderWidth: 5,
    });

    naver.maps.Event.addListener(marker, "click", function (e) {
      if (infoWindow.getMap()) {
        infoWindow.close();
      } else {
        infoWindow.open(map, marker);
      }
    });

    friendsMarkers.value.push(marker);
    infoWindows.push(infoWindow);
  })
}

const addPoliceStationMarker = () => {
  policeStationLocationList.value.forEach((station) => {
    let content = [
      "<div>",
      `<img src="/icons/police-station-pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`,
      "</div>",
    ].join("");

    let marker = new naver.maps.Marker({
      position: new naver.maps.LatLng(station.lat, station.lng),
      icon: {
        content: content,
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      },
      map: map
    });

    let infoWindow = new naver.maps.InfoWindow({
      content: `<div style="text-align:center;padding:10px;">
                    <div>${station.address}</div>
                </div>`,
      backgroundColor: "#FFFFFF",
      borderColor: "#FFFFFF",
      borderWidth: 5,
    });

    naver.maps.Event.addListener(marker, "click", function (e) {
      if (infoWindow.getMap()) {
        infoWindow.close();
      } else {
        infoWindow.open(map, marker);
      }
    });

    policeStationMarkers.value.push(marker);
    infoWindows.push(infoWindow);
  })
}


// 관심 지역 설정
const isInterest = ref(false)
let selectMarker = []
let interestArea = []

watch(() => isInterest.value, async(newVal) => {

  if(newVal) {

    // DB에 저장된 관심지역 lat, lng가 있으면 가져오기
    const response = await userStore.getUserInterestArea(authStore.$state.email)

    if(response.data.data.lat && response.data.data.lng) {
      let prevInterestPosition = new naver.maps.LatLng(response.data.data.lat, response.data.data.lng)

      selectMarker = new naver.maps.Marker({
        position: prevInterestPosition,
        map: map,
      })

      interestArea = new naver.maps.Circle({
        map: map,
        center: prevInterestPosition,
        radius: 1000,
        fillColor: 'green',
        fillOpacity: 0.5
      })
    } else {
      selectMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(map.center._lat, map.center._lng),
        map: map,
      })

      interestArea = new naver.maps.Circle({
        map: map,
        center: new naver.maps.LatLng(map.center._lat, map.center._lng),
        radius: 1000,
        fillColor: 'green',
        fillOpacity: 0.5
      })
    }

    naver.maps.Event.addListener(map, 'click', async function(e) {
      selectMarker.setPosition(e.coord)
      interestArea.setCenter(e.coord)
      await userStore.setUserInterestArea(authStore.$state.email, e.coord._lat, e.coord._lng)
      await propertyStore.removeInterestAreaAllProperty(authStore.$state.email)
    })
  }
  else {
    selectMarker.setMap(null)
    interestArea.setMap(null)
  }
})

// 관심지역 매물만 검색
const isOnlyInterestArea = ref(false)

watch(() => isOnlyInterestArea.value, async(newVal) => {

  if(newVal) {
    const response = await userStore.getUserInterestArea(authStore.$state.email)

    if(!response.data.data.lat && !response.data.data.lng) {
      Notify.create({
        message: "관심지역을 먼저 등록해주세요.",
        color: "red"
      })
      return
    }

    await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng)
    map.setCenter(new naver.maps.LatLng(response.data.data.lat, response.data.data.lng))
    map.setZoom(15)
  } else {

    const searchPayload = {
      address: address.value,
      tradeTypeName: tradeTypeName.value.value
    }
    await propertyStore.getMapList(searchPayload);
  }
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
  backgroundColor: '#F0F0F0',
  width: '10px',
  opacity: '0.75'
}

const tradeTypeOptions = [{label: "전체", value: ""}, {label: "월세", value: "월세"}, {
  label: "전세",
  value: "전세"
}, {label: "매매", value: "매매"}];
const address = ref("")
const tradeTypeName = ref(tradeTypeOptions[0])

watch(() => tradeTypeName.value, (newVal, oldVal) => {
  isSearch.value = true
  const searchPayload = {
    address: address.value,
    tradeTypeName: newVal.value
  }
  propertyStore.getMapList(searchPayload);
})

watch(() => address.value, (newVal, oldVal) => {
  isSearch.value = true
  const searchPayload = {
    address: newVal,
    tradeTypeName: tradeTypeName.value.value
  }
  propertyStore.getMapList(searchPayload);
})

const clickProperty = (idx) => {
  // TODO: 마커 색상 변경
  selectedMarkerIdx.value = idx;
  markers.value.forEach((marker, i) => {
    if (i === idx) {
      marker.setIcon({
        content: [`<img src="/icons/selected-pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      })
    } else {
      marker.setIcon({
        content: [`<img src="/icons/pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      })
    }
  })
}
</script>

<template>
  <q-page>
    <div>
      <!-- 검색 바-->
      <!-- address, tradeTypeName -->
      <div class="q-pa-lg q-px-xl">
        <div class="row justify-between items-center">
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
          <div>
            <q-toggle
              v-model="isInterest"
              color="red"
              label="관심지역 설정"
              unchecked-icon="clear"
            />
            <q-toggle
              v-model="isOnlyInterestArea"
              color="red"
              label="관심지역 매물보기"
              unchecked-icon="clear"
            />
            <q-toggle
              v-model="isPoliceStationToggle"
              checked-icon="check"
              color="yellow"
              label="주변 경찰서 위치도 함께 표시할래요"
              unchecked-icon="clear"
            />
            <q-toggle
              v-model="isToggle"
              checked-icon="person"
              color="red"
              label="친구 집 위치도 함께 표시할래요"
              unchecked-icon="clear"
            />
          </div>
        </div>
        <div class="row justify-between">
          <q-item-label caption class="q-mt-xs">검색 조건을 변경 시, 자동 검색 됩니다.</q-item-label>
          <div style="color: #117CE9;">총 {{ mapList.length }}개의 검색 결과</div>
        </div>
      </div>

      <!-- 컨텐트 -->
      <div class="row q-mx-xl">
        <!-- 지도 -->
        <div class="col-8">
          <div id="map" style="height: 75vh"></div>
        </div>
        <!-- 목록 -->
        <div class="col-4" style="height: 75vh">
          <q-scroll-area
            :thumb-style="thumbStyle"
            :content-style="contentStyle"
            :content-active-style="contentActiveStyle"
            class="full-height"
          >
            <q-list bordered separator>
              <q-item v-for="(map, idx) in mapList" clickable v-ripple @click="clickProperty(idx)"
                      :class="[selectedMarkerIdx === idx && 'selected-class']">
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
                    <div class="q-ml-sm">{{ map.rentPrc }}</div>
                  </div>
                  <q-item-label caption> {{ map.floorInfo }}층 {{ map.area1 }}/{{ map.area2 }}㎡ {{
                      map.direction
                    }}
                  </q-item-label>
                  <q-item-label caption>{{ map.tagList }}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-scroll-area>
        </div>
      </div>
    </div>

    <div v-show="isInterest">
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-card style="height: 100px; width: 500px; background-color: rgba(255, 0, 0, 0.8);" class="row justify-center items-center">
          <div style="font-weight: bold">
            <div>관심 지역 설정 중입니다.</div>
            <div>지도 영역 클릭 시 관심 지역이 업데이트 됩니다.</div>
            <div>관심 지역 설정을 중단하고 싶다면, 관심 지역 설정 토글을 꺼주세요.</div>
          </div>
        </q-card>
      </q-page-sticky>
    </div>
  </q-page>
</template>

<style scoped lang="scss">
.selected-class {
  background-color: $light-grey;
}
</style>
