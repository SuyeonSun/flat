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

    <q-header elevated class="bg-black text-white" height-hint="98">
      <q-toolbar class="q-pt-md" style="display: flex; justify-content: space-between; align-items: center">
        <div class="q-pl-md">
          <h5 class="q-ma-none q-mb-sm" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
        <div style="display: flex; align-items: center">
          <div>
            {{ name }} 님
          </div>
          <div>
            <q-btn size="md" unelevated @click="signOut()">로그아웃</q-btn>
          </div>
        </div>
      </q-toolbar>

      <q-tabs align="left">
        <q-route-tab to="/page1" label="Page One"/>
        <q-route-tab to="/page2" label="Page Two"/>
        <q-route-tab to="/my-page" label="마이 페이지"/>
      </q-tabs>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="text-white full-width" style="background-color: #4F4F51">
      <q-toolbar style="display: flex; justify-content: center">
        <div>© 2023 FLAT</div>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<style scoped>
.logo-font {
  font-size: 20px;
}

.logo-icon {
  font-size: 23px;
  margin-bottom: 3px;
}
</style>


