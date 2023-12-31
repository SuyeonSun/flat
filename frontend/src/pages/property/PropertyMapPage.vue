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

  script.onload = async () => {
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
          `<div style="padding:10px;">
            <div class="row justify-start items-center">
                <div class="q-mr-sm"><img src="${element.image}" style="width: 90px; height: 60px"></div>
                <div>
                    <div>${element.address} ${element.buildingName}</div>
                    <div>${element.tradeTypeName} ${element.rentPrc} ${element.area1}/${element.area2}㎡</div>
                </div>
            </div>
            <a href="http://localhost:8080/property/${element.id}" style="text-decoration: none; color: #5bc500">
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

            // marker 클릭 시 스크롤 이동
            let gap = scrollRef.value.getScroll().verticalSize / mapList.value.length
            let nextScrollPosition = idx * gap
            if(nextScrollPosition > scrollRef.value.getScroll().verticalSize) {
              nextScrollPosition = scrollRef.value.getScroll().verticalSize
            }

            scrollRef.value.setScrollPosition('vertical', nextScrollPosition, 200)

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

    if(isInterest.value) {
      // DB에 저장된 관심지역 lat, lng가 있으면 가져오기
      const response = await userStore.getUserInterestArea(authStore.$state.email)

      if(response.data.data.lat && response.data.data.lng) {
        let prevInterestPosition = new naver.maps.LatLng(response.data.data.lat, response.data.data.lng)
        interestArea.value = response.data.data.radius

        selectMarker.setMap(map)
        interestArea.setMap(map)

      }

      naver.maps.Event.addListener(map, 'click', async function(e) {
        selectMarker.setPosition(e.coord)
        interestArea.setCenter(e.coord)

        curCirclePosition.value = new naver.maps.LatLng(e.coord._lat, e.coord._lng)
        await userStore.setUserInterestArea(authStore.$state.email, e.coord._lat, e.coord._lng, interestAreaRange.value)
        await propertyStore.removeInterestAreaAllProperty(authStore.$state.email)
        await propertyStore.getInterestAreaMapList(e.coord._lat, e.coord._lng, interestAreaRange.value)
      })
    }

    if(isOnlyInterestArea.value && !isInterest.value) {

      interestArea = new naver.maps.Circle({
        map: map,
        center: new naver.maps.LatLng(map.center._lat, map.center._lng),
        radius: interestAreaRange.value*200,
        fillColor: 'green',
        fillOpacity: 0.5
      })
    }

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
    if(!isOnlyInterestArea.value) {
      // 토글 해제 시, 친구 marker 제거
      friendsMarkers.value = []
      const searchPayload = {
        address: address.value,
        tradeTypeName: tradeTypeName.value.value
      }
      await propertyStore.getMapList(searchPayload);
    } else {
      const response = await userStore.getUserInterestArea(authStore.$state.email)
      interestArea.value = response.data.data.radius
      await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng, interestAreaRange.value)
    }
  }
})

watch (() => isPoliceStationToggle.value, async (newVal, oldVal) => {
  if (newVal) {
    addPoliceStationMarker();
  } else {
    if(!isOnlyInterestArea.value) {
      policeStationMarkers.value = []
      const searchPayload = {
        address: address.value,
        tradeTypeName: tradeTypeName.value.value
      }
      await propertyStore.getMapList(searchPayload);
    } else {
      const response = await userStore.getUserInterestArea(authStore.$state.email)
      interestArea.value = response.data.data.radius
      await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng, interestAreaRange.value)
    }
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

    if(isOnlyInterestArea.value) {
      interestArea.setMap(null)
    }

    if(response.data.data.lat && response.data.data.lng) {
      let prevInterestPosition = new naver.maps.LatLng(response.data.data.lat, response.data.data.lng)

      interestAreaRange.value = response.data.data.radius
      curCirclePosition.value = new naver.maps.LatLng(response.data.data.lat, response.data.data.lng)

      selectMarker = new naver.maps.Marker({
        position: prevInterestPosition,
        map: map,
      })

      interestArea = new naver.maps.Circle({
        map: map,
        center: prevInterestPosition,
        radius: interestAreaRange.value*200,
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
        radius: interestAreaRange.value*200,
        fillColor: 'green',
        fillOpacity: 0.5
      })
    }

    naver.maps.Event.addListener(map, 'click', async function(e) {
      console.log("addEventLisnter!!!!!!!!!!")
      curCirclePosition.value = new naver.maps.LatLng(e.coord._lat, e.coord._lng)

      selectMarker.setPosition(e.coord)
      interestArea.setCenter(e.coord)

      await userStore.setUserInterestArea(authStore.$state.email, e.coord._lat, e.coord._lng, interestAreaRange.value)
      await propertyStore.removeInterestAreaAllProperty(authStore.$state.email)

      if(isOnlyInterestArea.value) {
        await propertyStore.getInterestAreaMapList(e.coord._lat, e.coord._lng, interestAreaRange.value)
      }

    })
  }
  else {
    selectMarker.setMap(null)
    interestArea.setMap(null)
    if(isOnlyInterestArea.value) {
      const response = await userStore.getUserInterestArea(authStore.$state.email)
      interestArea.value = response.data.data.radius
      map.setCenter(new naver.maps.LatLng(response.data.data.lat, response.data.data.lng))
      map.setZoom(15)
      await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng, interestAreaRange.value)
    }
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

    interestAreaRange.value = response.data.data.radius
    map.setCenter(new naver.maps.LatLng(response.data.data.lat, response.data.data.lng))
    map.setZoom(15)
    await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng, interestAreaRange.value)
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
  // 매물정보 클릭 시 스크롤 이동
  // console.log("~~~~~~~~~~", scrollRef.value.getScroll())
  // console.log("@@@@@@@@@@", scrollRef.value.getScroll().verticalSize)
  // console.log("!!!!!!!!!!", mapList.value.length)
  // console.log("##########", scrollRef.value.getScroll().verticalSize / mapList.value.length)
  let gap = scrollRef.value.getScroll().verticalSize / mapList.value.length
  let nextScrollPosition = idx * gap
  if(nextScrollPosition > scrollRef.value.getScroll().verticalSize) {
    nextScrollPosition = scrollRef.value.getScroll().verticalSize
  }

  scrollRef.value.setScrollPosition('vertical', nextScrollPosition, 200)

  // TODO: 마커 색상 변경
  selectedMarkerIdx.value = idx;
  markers.value.forEach((marker, i) => {
    if (i === idx) {
      marker.setIcon({
        content: [`<img src="/icons/selected-pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      })
      map.setCenter(new naver.maps.LatLng(marker.position._lat, marker.position._lng))

      let element = mapList.value[i]

      // 마커 클릭 시 상세 매물 정보로 이동하기
      let infoWindow = new naver.maps.InfoWindow({
        content:
          `<div style="padding:10px;">
            <div class="row justify-start items-center">
                <div class="q-mr-sm"><img src="${element.image}" style="width: 90px; height: 60px"></div>
                <div>
                    <div>${element.address} ${element.buildingName}</div>
                    <div>${element.tradeTypeName} ${element.rentPrc} ${element.area1}/${element.area2}㎡</div>
                </div>
            </div>
            <a href="http://localhost:8080/property/${element.id}" style="text-decoration: none; color: #5bc500">
               ▶ 매물 상세 보기
            </a>
          </div>`,
        backgroundColor: "#FFFFFF",
        borderColor: "#FFFFFF",
        borderWidth: 5,
      });

      if (infoWindow.getMap()) {
        infoWindow.close();
      } else {
        infoWindow.open(map, marker);
      }

      infoWindows.push(infoWindow)

    } else {
      marker.setIcon({
        content: [`<img src="/icons/pin.png" style="height: 30px; width: 30px; border-radius: 70%" />`].join(""),
        size: new naver.maps.Size(32, 32),
        anchor: new naver.maps.Point(16, 16),
      })
    }
  })
}

const scrollRef = ref(null)
const interestAreaRange = ref(1)
const interestAreaRangeLabel = (val) => `${val*200}m`
const curCirclePosition = ref(undefined)

watch(() => interestAreaRange.value, async (n) => {

  interestArea.setMap(null)

  interestArea = new naver.maps.Circle({
    map: map,
    center: curCirclePosition.value,
    radius: n*200,
    fillColor: 'green',
    fillOpacity: 0.5
  })
  await userStore.setUserInterestAreaRange(authStore.$state.email, interestAreaRange.value)

  if(isOnlyInterestArea.value) {
    await propertyStore.removeInterestAreaAllProperty(authStore.$state.email)
    const response = await userStore.getUserInterestArea(authStore.$state.email)
    await propertyStore.getInterestAreaMapList(response.data.data.lat, response.data.data.lng, interestAreaRange.value)
  }

})
</script>

<template>
  <q-page>
    <div>
      <!-- 검색 바-->
      <!-- address, tradeTypeName -->
      <div class="q-pa-lg q-px-xl">
        <div class="row justify-between items-center">
          <div v-if="!isOnlyInterestArea">
            <div style="font-weight: bold; font-size: 17px" class="q-mb-xs">검색</div>
            <q-item-label caption class="q-mb-sm">검색 조건을 변경 시, 자동 검색 됩니다.</q-item-label>
            <div class="row" >
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
          </div>
          <div class="row" v-else>
            <q-select outlined v-model="tradeTypeName" :options="tradeTypeOptions" stack-label dense readonly class="q-mr-sm"
                      style="width: 100px" label="거래 유형"/>
            <q-input
              v-model="address"
              dense
              outlined
              readonly
              label="검색할 매물 주소"
            >
              <template v-slot:append>
                <q-icon name="search"/>
              </template>
            </q-input>
          </div>
          <div>
            <div class="row items-center">
              <div class="q-mr-xl">
                <q-toggle
                  v-model="isInterest"
                  checked-icon="check"
                  color="green"
                  label="관심 지역 설정"
                  unchecked-icon="clear"
                />
              </div>
              <div v-if="isInterest" class="row">
                <q-badge color="green">
                  범위: {{ interestAreaRange * 200 }}m
                </q-badge>
                <q-slider
                  style="min-width: 150px"
                  v-model="interestAreaRange"
                  color="green"
                  markers
                  :marker-labels="interestAreaRangeLabel"
                  :min="1"
                  :max="5"
                />
              </div>
            </div>
            <div>
              <q-toggle
                v-model="isOnlyInterestArea"
                checked-icon="check"
                color="red"
                label="관심 지역 매물 보기"
                unchecked-icon="clear"
              />
            </div>
            <div>
              <q-toggle
                v-model="isPoliceStationToggle"
                checked-icon="check"
                color="amber-8"
                label="주변 경찰서 위치도 함께 표시할래요"
                unchecked-icon="clear"
              />
              <q-toggle
                v-model="isToggle"
                checked-icon="person"
                color="grey"
                label="친구 집 위치도 함께 표시할래요"
                unchecked-icon="clear"
              />
            </div>
          </div>
        </div>
        <div class="row justify-between">
          <q-item-label caption class="q-mt-xs"></q-item-label>
          <div style="color: #117CE9;">총 {{ mapList.length }}개의 검색 결과</div>
        </div>
      </div>

      <!-- 컨텐트 -->
      <div class="row q-mx-xl">
        <!-- 지도 -->
        <div class="col-8">
          <div id="map" style="height: 60vh"></div>
        </div>
        <!-- 목록 -->
        <div class="col-4" style="height: 60vh">
          <q-scroll-area
            :thumb-style="thumbStyle"
            :content-style="contentStyle"
            :content-active-style="contentActiveStyle"
            class="full-height"
            ref="scrollRef"
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
                  <q-item-label caption>{{ map.tagList ? map.tagList : '등록된 태그가 없습니다.'}}</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-scroll-area>
        </div>
      </div>
    </div>

    <div v-show="isInterest">
      <q-page-sticky position="bottom-left" :offset="[18, 18]">
        <q-card style="height: 100px; width: 500px; color: black; background-color: rgba(255, 0, 0, 0.5);" class="row justify-center items-center">
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
