<script setup>
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const $router = useRouter();
const authStore = useAuthStore();

const {email, name} = storeToRefs(authStore);

const signOut = async () => {
  // localStorage 및 store email, accessToken 값 지우기
  const signOutPayload = {
    email: email.value
  }
  await authStore.signOut(signOutPayload);
  authStore.setAccessToken(undefined);
  authStore.setEmail(undefined);
  authStore.setName(undefined);

  $router.push("/sign-in");
}

const clickGoToRegisterBtn = () => {
  $router.push("/property/register");
}
</script>

<template>
  <q-layout view="hHh lpR fFf">
    <q-header height-hint="98" style="background: transparent">
      <q-toolbar class="q-pt-md q-px-xl q-pb-sm tool-bar"
                 style="display: flex; justify-content: space-between; align-items: center">
        <div class="q-pl-xl">
          <h5 class="q-ma-none q-mb-sm" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
        <div style="display: flex; align-items: center" class="q-pr-xl q-mb-xs">
          <div class="q-mr-sm">
            <q-btn size="md" unelevated outline>{{ name }} 님</q-btn>
          </div>
          <div>
            <q-btn size="md" unelevated outline @click="signOut()">로그아웃</q-btn>
          </div>
        </div>
      </q-toolbar>

      <q-tabs align="left" style="padding-left: 80px;">
        <q-route-tab to="/property/map"><span style="font-weight: bolder">지도</span></q-route-tab>
        <q-route-tab to="/property/list"><span style="font-weight: bolder">검색</span></q-route-tab>
        <q-route-tab to="/property/register"><span style="font-weight: bolder">방내놓기</span></q-route-tab>
        <q-route-tab to="/my-page"><span style="font-weight: bolder">MY</span></q-route-tab>
      </q-tabs>
    </q-header>

    <div class="">
      <div class="row justify-between">
        <q-parallax
          src="images/wallpaper.jpg"
        >
          <h3 class="text-white text-bold" style="font-size: 30px; margin-bottom: 0px">FLAT에서 원하는 조건의 아파트를 탐색하고 골라보세요.</h3>
          <h3 class="text-white" style="font-size: 20px; margin-top: 0px">
            <span class="text-bold"> 매물을 등록하고 홍보 해보세요! </span>
            <q-btn rounded flat class="q-mb-xs q-ml-sm bg-white text-black text-bold" @click="clickGoToRegisterBtn">매물 등록하기</q-btn>
          </h3>
        </q-parallax>
      </div>
    </div>

    <q-page-container style="padding-top: 0px">
      <router-view/>
    </q-page-container>
  </q-layout>
  <div elevated class="footer full-width q-py-lg q-px-md">
    <div class="q-pl-lg">
      <div class="row items-center">
        <div class="text-subtitle1 text-bold q-mr-sm">FLAT</div>
        <div style="font-size: 13px">서비스 이용약관 | 개인정보처리방침 | 인재채용 | 제휴 문의</div>
      </div>
      <div class="row q-mb-xs" style="font-size: 13px">
        <div class="q-mr-sm">사업자등록번호: 123-45-67899</div>
        <div>전화문의 1234-5678</div>
      </div>
      <div class="q-mb-xs" style="font-size: 13px">주소: 서울특별시 강남구 역삼동 테헤란로 212</div>
      <div style="font-size: 13px">© FLAT. All Rights Reserved.</div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.logo-font {
  font-size: 25px;
}

.logo-icon {
  font-size: 30px;
  margin-bottom: 7px;
}

.tabs {
  color: white;
}

.footer {
  background-color: $dark-grey;
  color: #b3b1b1;
}
</style>
