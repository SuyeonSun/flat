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
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark"
        style="max-height: 290px">
        <q-header class="bg-white" style="color: black">
          <q-toolbar style="height: 50px">
            <q-toolbar-title>
            </q-toolbar-title>
            <q-btn flat v-close-popup round dense icon="close"/>
          </q-toolbar>
        </q-header>

        <q-page-container>
          <q-page padding>
            <div class="row justify-end">
              <div style="width: 100%" class="row justify-center">
                <q-item-label class="q-mb-md">비밀번호를 재설정 해주세요.</q-item-label>
                <q-input
                  v-model="prevPwd"
                  style="width: 100%; max-width: 80%"
                  label="현재 비밀번호를 입력해 주십시오."
                  dense
                  outlined
                  type="password"
                  class="q-mb-sm"
                >
                </q-input>
                <q-input
                  v-model="newPwd"
                  style="width: 100%; max-width: 80%"
                  label="새로운 비밀번호를 입력해 주십시오."
                  dense
                  outlined
                  type="password"
                  class="q-mb-sm"
                >
                </q-input>
              </div>
              <div style="margin-right: 10%">
                <q-btn
                  @click="changePassword()"
                >
                  비밀번호 변경
                </q-btn>
              </div>
            </div>
          </q-page>
        </q-page-container>

      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>

</style>
