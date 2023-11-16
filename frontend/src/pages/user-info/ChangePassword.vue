<script setup>
import {useUserStore} from "stores/user/user-store";
import { ref } from "vue"
import {Notify} from "quasar";

const userStore = useUserStore()

const prevPwd = ref('')
const newPwd = ref('')



const changePassword = async () => {
  const payLoad = {
    email: userStore.$state.user.email,
    prevPwd: prevPwd.value,
    newPwd: newPwd.value
  }

  const response = await userStore.changePassword(payLoad)

  if(response.data.code === 200) {
    Notify.create({
      message: '비밀번호 변경이 완료되었습니다.',
      color: "green"
    })
    userStore.$state.changePwdDialog = false
  } else if(response.data.code === 2002) {
    Notify.create({
      message: '비밀번호가 일치하지 않습니다.',
      color: "red"
    })
  } else if(response.data.code === 2003) {
    Notify.create({
      message: '동일한 비밀번호로 변경할 수 없습니다.',
      color: "red"
    })
  }
}

</script>

<template>
  <div class="q-pa-md q-gutter-sm">
    <!--    <q-btn label="Click me" color="primary" @click="layout = true" />-->

    <q-dialog v-model="userStore.$state.changePwdDialog" persistent>
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark">
        <q-header class="bg-primary">
          <q-toolbar>
            <q-toolbar-title class="text-center">비밀번호 변경</q-toolbar-title>
            <q-btn flat v-close-popup round dense icon="close"/>
          </q-toolbar>
        </q-header>

        <q-scroll-area style="height: 800px;" ref="scrollRef">
          <q-page-container>
            <q-page padding>
              <div class="row justify-center">
                <q-input
                  v-model="prevPwd"
                  style="width: 100%; max-width: 400px"
                  label="현재 비밀번호를 입력해 주십시오."
                  dense
                  outlined
                  type="password"
                  class="q-mb-sm"
                >
                </q-input>
                <q-input
                  v-model="newPwd"
                  style="width: 100%; max-width: 400px"
                  label="새로운 비밀번호를 입력해 주십시오."
                  dense
                  outlined
                  type="password"
                  class="q-mb-sm"
                >
                </q-input>
              </div>
              <div>
                <q-btn
                  @click="changePassword()"
                >
                  비밀번호 변경
                </q-btn>
              </div>
            </q-page>
          </q-page-container>

        </q-scroll-area>
      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>

</style>
