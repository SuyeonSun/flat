<script setup>
import {ref} from "vue";
import {useAuthStore} from "stores/auth/auth-store";

import {useRouter} from "vue-router";
import {Notify} from "quasar";

const authStore = useAuthStore();

const email = ref(undefined);
const password = ref(undefined);

const $router = useRouter();

const onSubmit = async () => {
  const signInPayload = {
    email: email.value,
    password: password.value
  }
  const response = await authStore.signIn(signInPayload);
  if (response) {
    await $router.push("/");
  } else {
    Notify.create({
      message: '유효하지 않은 회원 정보입니다.',
      color: "red"
    })
  }
}

</script>

<template>
  <div class="q-pa-md">
    <div class="text-center">
      <h5 class="q-ma-none q-mb-md">FLAT</h5>
      <h5 class="q-mt-none">SIGN IN</h5>
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
        v-model="password"
        label="비밀번호"
        dense
        outlined
      />

      <q-btn label="SIGN IN" type="submit" color="primary" class="full-width"/>

      <div class="text-subtitle1 text-center q-ma-none">
        Create account? <a href="/sign-up"> Sign up here </a>
      </div>
    </q-form>

  </div>
</template>

<style scoped>
.form-container {
  max-width: 300px;
}
</style>
