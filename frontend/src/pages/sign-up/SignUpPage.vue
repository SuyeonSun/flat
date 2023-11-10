<script setup>
import {ref} from "vue";
import {useRouter} from "vue-router";
import {useAuthStore} from "stores/auth/auth-store";
import {useS3Store} from "stores/common/s3-store";
import {Notify} from "quasar";

const authStore = useAuthStore();
const s3Store = useS3Store();

const email = ref(undefined);
const password = ref(undefined);
const name = ref(undefined);
const profile = ref(undefined);
const address = ref(undefined);
const phoneNumber = ref(undefined);

const $router = useRouter();

const onSubmit = async () => {
  const fileUrl = await s3Store.uploadFile(profile.value);
  const signUpPayload = {
    email: email.value,
    password: password.value,
    name: name.value,
    profile: fileUrl,
    address: address.value,
    addressLat: "test lat value",
    addressLng: "test lng value",
    phoneNumber: phoneNumber.value
  }

  const response = await authStore.signUp(signUpPayload);
  if (response) {
    Notify.create({
      message: '회원 가입이 완료되었습니다.',
      color: "green"
    })
    await $router.push("/sign-in");
  } else {
      Notify.create({
        message: '이미 존재하는 계정입니다.',
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
          label="이메일"
          dense
          outlined
          class="q-mb-md"
        />

        <q-input
          v-model="name"
          label="이름"
          dense
          outlined
          class="q-mb-md"
        />

        <q-input
          v-model="password"
          label="비밀번호"
          dense
          outlined
          class="q-mb-md"
        />

        <q-file
          v-model="profile"
          label="프로필"
          outlined
          dense
          class="q-mb-md"
        />

        <q-input
          v-model="address"
          label="주소"
          dense
          outlined
          class="q-mb-md"
        />

        <q-input
          v-model="phoneNumber"
          label="전화번호"
          dense
          outlined
          class="q-mb-md"
        />

        <q-btn label="회원가입" type="submit" class="full-width q-mb-md submit-btn"/>

        <div class="text-subtitle1 text-center q-ma-none text">
          이미 계정이 있으신가요? | <a href="/sign-in" style="text-decoration: none;" class="text"> 로그인 </a>
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
  width: 50%;
}

.logo-font {
  font-size: 30px;
}

.logo-icon {
  font-size: 37px;
  margin-bottom: 10px;
}

.form-outer-container {
  display: flex;
  justify-content: center;
}

.form-container {
  min-width: 300px;
}

.submit-btn {
  background-color: #F2C4CE;
}

.text {
  color: #4F4F51;
}
</style>
