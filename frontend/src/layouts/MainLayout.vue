<script setup>
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {useUserStore} from "stores/user/user-store";

const $router = useRouter();
const authStore = useAuthStore();
const userStore = useUserStore();

const {email, name} = storeToRefs(authStore);

const profile = ref();

onMounted(async () => {
  const profileResponse = await userStore.getUserProfile(name.value);
  profile.value = profileResponse.data;
})

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
      <q-toolbar class="q-pt-md q-px-xl q-pb-sm tool-bar" style="display: flex; justify-content: space-between; align-items: center">
        <div class="q-pl-xl">
          <h5 class="q-ma-none" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon" style="color: #ed9b33"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
        <div style="display: flex; align-items: center" class="q-pr-xl q-mb-xs">
<!--          <div class="q-mr-sm">-->
<!--            <q-btn unelevated>-->
<!--              <span style="scale: 2" class="material-icons">notifications</span>-->
<!--              <q-badge color="red" floating rounded>NEW</q-badge>-->
<!--            </q-btn>-->
<!--          </div>-->
          <div class="q-mr-md">
            <q-avatar round size="35px" class="q-mr-xs">
              <img :src="profile"/>
            </q-avatar>
            {{ name }} 님, 환영합니다.
          </div>
          <div>
            <q-btn size="md" unelevated outline @click="signOut()" class="bg-black text-white">로그아웃</q-btn>
          </div>
        </div>
      </q-toolbar>

      <q-tabs align="left" style="padding-left: 80px; background-color: whitesmoke; color:black" indicator-color="amber-8">
        <q-route-tab to="/property/map"><span style="font-weight: bolder">지도</span></q-route-tab>
        <q-route-tab to="/property/list"><span style="font-weight: bolder">검색</span></q-route-tab>
        <q-route-tab to="/property/register"><span style="font-weight: bolder">방내놓기</span></q-route-tab>
        <q-route-tab to="/my-page"><span style="font-weight: bolder">MY</span></q-route-tab>
      </q-tabs>
    </q-header>

    <q-page-container>
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

.tool-bar {
  background-color: white;
  color: black;
}

.tabs {
  color: white;
}

.footer {
  background-color: $dark-grey;
  color: #b3b1b1;
}

:deep(.q-tab__label) {
  font-weight: bolder;
}
</style>


