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
    <q-header elevated height-hint="98">
      <q-toolbar class="q-pt-md q-pb-sm q-px-xl tool-bar" style="display: flex; justify-content: space-between; align-items: center">
        <div class="q-pl-md">
          <h5 class="q-ma-none q-mb-sm" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
        <div style="display: flex; align-items: center">
          <div class="q-mr-sm">
            <q-btn size="md" unelevated outline>{{ name }} 님</q-btn>
          </div>
          <div>
            <q-btn size="md" unelevated outline @click="signOut()" class="bg-black text-white">로그아웃</q-btn>
          </div>
        </div>
      </q-toolbar>

      <q-tabs align="left" class="tabs q-px-xl">
        <q-route-tab to="/page1" label="Page One"/>
        <q-route-tab to="/page2" label="Page Two"/>
        <q-route-tab to="/my-page" label="마이 페이지"/>
      </q-tabs>
    </q-header>

    <q-page-container class="q-px-xl ">
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
  font-size: 20px;
}

.logo-icon {
  font-size: 23px;
  margin-bottom: 3px;
}

.tool-bar {
  background-color: white;
  color: black;
}

.tabs {
  background-color: $dark-blue;
  color: white;
}

.footer {
  background-color: $dark-grey;
  color: #b3b1b1;
}
</style>


