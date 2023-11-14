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
        <q-route-tab to="/property/main" label="메인"/>
        <q-route-tab to="/property/map" label="지도"/>
        <q-route-tab to="/property/list" label="매물"/>
        <q-route-tab to="/my-page" label="MY"/>
      </q-tabs>
    </q-header>

    <div class="">
      <div class="row justify-between">
        <q-parallax
          src="images/wallpaper.jpg"
        >
          <h3 class="text-white">Basic</h3>
        </q-parallax>
      </div>
    </div>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="footer full-width q-py-lg q-px-md">
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
    </q-footer>
  </q-layout>
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
