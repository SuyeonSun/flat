<script setup>
import {ref} from "vue";
import {useAuthStore} from "stores/auth/auth-store";

import {useRouter} from "vue-router";
import {Notify} from "quasar";

const authStore = useAuthStore();

const email = ref(undefined);
const password = ref(undefined);
const name = ref(undefined);
const profile = ref(undefined);
const address = ref(undefined);

const $router = useRouter();

const onSubmit = async () => {
  const signUpPayload = {
    email: email.value,
    password: password.value,
    name: name.value,
    profile: profile.value,
    address: address.value,
    addressLat: "test lat value",
    addressLng: "test lng value",
  }
  const response = await authStore.signUp(signUpPayload);
  if (response) {
    await $router.push("/");
  } else {
      Notify.create({
        message: '이미 존재하는 계정입니다.',
        color: "red"
      })
  }
}

</script>

<template>
  <div class="q-pa-md">
    <div class="text-center">
      <h5 class="q-ma-none q-mb-md">FLAT</h5>
      <h5 class="q-mt-none">CREATE YOUR ACCOUNT</h5>
    </div>

    <q-form
      @submit="onSubmit"
      class="form-container"
    >
      <q-input
        v-model="email"
        label="이메일"
        dense
        outlined
      />

      <q-input
        v-model="name"
        label="이름"
        dense
        outlined
      />

      <q-input
        v-model="password"
        label="비밀번호"
        dense
        outlined
      />


      <q-input
        v-model="profile"
        label="프로필"
        dense
        outlined
      />

      <q-input
        v-model="address"
        label="주소"
        dense
        outlined
      />

      <q-btn label="Submit" type="submit" color="primary" class="full-width"/>

      <div class="text-subtitle1 text-center q-ma-none">
        이미 계정이 있으신가요? <a href=""> Sign in here </a>
      </div>
    </q-form>

  </div>
</template>

<style scoped>
.form-container {
  max-width: 300px;
}
</style>
