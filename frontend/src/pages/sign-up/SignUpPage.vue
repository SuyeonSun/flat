<script setup>
import {onMounted, ref} from "vue";

const authStore = use

let email = ref(undefined);
let password = ref(undefined);
let name = ref(undefined);
let profile = ref(undefined);
let address = ref(undefined);

const onSubmit = () => {
  const signUpPayload = {
    email: email.value,
    password: password.value,
    name: name.value,
    profile: profile.value,
    address: address.value,
    lat: "test lat value",
    lng: "test lng value",
  }

}

</script>

<template>
  <div class="q-pa-md" style="max-width: 300px">
    <q-form
      @submit="onSubmit"
      class="q-gutter-md"
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

      <q-input
        v-model="name"
        label="이름"
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

      <div>
        <q-btn label="Submit" type="submit" color="primary"/>
        <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm"/>
      </div>
    </q-form>

  </div>
</template>

<style scoped>

</style>
