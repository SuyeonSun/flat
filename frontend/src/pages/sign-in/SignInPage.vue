<script setup>
import {ref} from "vue";
import {useAuthStore} from "stores/auth/auth-store";

import {useRouter} from "vue-router";
import {Notify} from "quasar";
import {useTestStore} from "stores/test/test-store";

const authStore = useAuthStore();

const email = ref(undefined);
const password = ref(undefined);

const $router = useRouter();
const testStore = useTestStore();


const onSubmit = async () => {
  const signInPayload = {
    email: email.value,
    password: password.value
  }
  const response = await authStore.signIn(signInPayload);
  if (response) {
    await $router.push("/test");
  } else {
    Notify.create({
      message: '유효하지 않은 회원 정보입니다.',
      color: "red"
    })
  }
}

</script>

<template>
  <div class="main-container">
    <div class="text-center">
      <h5 class="q-ma-none q-mb-sm" style="font-weight: bold">
        <span class="logo-font">F</span>
        <span class="logo-font">L</span>
        <q-icon name="house" class="logo-icon"/>
        <span class="logo-font">T</span>
      </h5>
    </div>

    <div class="form-outer-container">
      <q-form
        @submit="onSubmit"
        class="form-container"
      >
        <q-input
          v-model="email"
          label="Email"
          dense
          outlined
          class="q-mb-md"
        />

        <q-input
          v-model="password"
          label="Password"
          dense
          outlined
          class="q-mb-md"
        />

        <q-btn label="SIGN IN" type="submit" class="full-width q-mb-md submit-btn"/>

        <div class="text-subtitle1 text-center q-ma-none text">
          가입이 필요하신가요? <a href="/sign-up" style="text-decoration: none;" class="text"> | 회원가입 </a>
        </div>
      </q-form>
    </div>

  </div>
</template>

<style scoped>
.main-container {
  padding: 80px;
  background-color: white;
  border-radius: 10px;
  width: 40%;
}

.form-outer-container {
  display: flex;
  justify-content: center;
}

.form-container {
  min-width: 300px;
}

.logo-font {
  font-size: 30px;
}

.logo-icon {
  font-size: 37px;
  margin-bottom: 10px;
}

.submit-btn {
  background-color: #F2C4CE;
}

.text {
  color: #4F4F51;
}
</style>
