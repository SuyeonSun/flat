<script setup>
import {ref} from "vue";
import {useAuthStore} from "stores/auth/auth-store";

import {useRouter} from "vue-router";
import {Notify} from "quasar";
import {useTestStore} from "stores/test/test-store";

const authStore = useAuthStore();

const email = ref("");
const password = ref("");

const $router = useRouter();
const testStore = useTestStore();


const onSubmit = async () => {
  const signInPayload = {
    email: email.value,
    password: password.value
  }
  const response = await authStore.signIn(signInPayload);
  if (response) {
    await $router.push("/property/main");
  } else {
    Notify.create({
      message: '유효하지 않은 회원 정보입니다.',
      color: "red"
    })
  }
}

</script>

<template>
  <q-header elevated height-hint="98">
    <q-toolbar class="q-pt-md q-px-xl q-pb-sm" style="display: flex; justify-content: space-between; align-items: center; background-color: #FFFFFF">
      <div class="q-pl-xl">
        <div>
          <h5 class="q-ma-none" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
      </div>
    </q-toolbar>
  </q-header>

  <div>
    <div class="main-container">
      <div class="form-outer-container">
        <q-form
          @submit="onSubmit"
          class="form-container"
        >
          <h5 class="q-mt-none" style="font-weight: bold">로그인</h5>
          <q-btn label="메인 계정" unelevated class="q-mb-md" style="color: #117CE9; border: 2px solid #117CE9; min-width: 145px; background-color: #F0F0F0"/>

          <q-input
            v-model="email"
            label="아이디(이메일)를 입력해 주십시오."
            dense
            outlined
            class="q-mb-md"
          >
            <template v-slot:append>
              <q-icon v-if="email !== ''" name="close" @click="email = ''"/>
            </template>
          </q-input>

          <q-input
            v-model="password"
            label="비밀번호를 입력해 주십시오."
            dense
            outlined
            type="password"
            class="q-mb-sm"
          >
            <template v-slot:append>
              <q-icon v-if="password !== ''" name="close" @click="password = ''"/>
            </template>
          </q-input>
          <div class="row items-center">
            <input type="checkbox" class="q-mr-sm"/>
            <label style="padding-top: 3px">아이디 저장</label>
          </div>

          <q-btn label="로그인" type="submit" class="full-width q-mt-lg q-mb-lg submit-btn"/>

          <div class="text-subtitle1 q-ma-none text">
            <a href="/sign-up" style="text-decoration: none; color: #117CE9; font-size: 15px; font-weight: bold" class="text"> 회원가입 </a>
          </div>
        </q-form>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.main-container {
  padding: 45px;
  background-color: white;
  border-radius: 10px;
  min-width: 550px;
  min-height: 500px;
}

.form-outer-container {
  display: flex;
  justify-content: center;
}

.form-container {
  min-width: 400px;
}

.logo-font {
  color: black;
  font-size: 25px;
}

.logo-icon {
  color: black;
  font-size: 30px;
  margin-bottom: 7px;
}

.submit-btn {
  background-color: $dark-blue; // #117CE9
  color: #FFFFFF
}

.text {
  color: #4F4F51;
}
</style>
