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
    <div class="form-outer-container">
      <q-form
        @submit="onSubmit"
        class="form-container"
      >
        <div>
          <h5 class="q-ma-none" style="font-weight: bold">
            <span class="logo-font">F</span>
            <span class="logo-font">L</span>
            <q-icon name="house" class="logo-icon"/>
            <span class="logo-font">T</span>
          </h5>
        </div>
        <h5 class="text-bold q-mt-none">로그인 정보를 입력해주세요.</h5>

        <q-input
          v-model="email"
          label="이메일"
          dense
          outlined
          class="q-mb-md"
        />

        <q-input
          v-model="password"
          label="비밀번호"
          dense
          outlined
        />

        <q-btn label="로그인" type="submit" class="full-width q-mt-lg q-mb-lg submit-btn"/>

        <div class="text-subtitle1 text-center q-ma-none text">
          가입이 필요하신가요? <a href="/sign-up" style="text-decoration: none;" class="text"> | 회원가입 </a>
        </div>
      </q-form>
    </div>

  </div>
</template>

<style scoped lang="scss">
.main-container {
  padding: 80px;
  background-color: white;
  border-radius: 10px;
  width: 60%;
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
  background-color: $dark-blue; // #117CE9
  color: #FFFFFF
}

.text {
  color: #4F4F51;
}
</style>
