<script setup>
import { ref, watch, onMounted } from 'vue';
import {useAuthStore} from "stores/auth/auth-store";
import {useUserStore} from "stores/user/user-store";
import {Notify} from "quasar";

const authStore = useAuthStore()
const userStore = useUserStore()

const text = ref('')
const searchUser = ref({})
const isUser = ref(false)
const friendState = ref(0)

const search = async (email) => {
  if(email === authStore.email) {
    Notify.create({
      message: '본인은 친구 추가 불가능합니다.',
      color: "red"
    })

    isUser.value = false
    text.value = ''
    return
  }

  const response = await userStore.searchUser(email)

  if(response.data.status === "INVALID_USER_INFO") {
    Notify.create({
      message: '존재하지 않은 계정입니다.',
      color: "red"
    })

    isUser.value = false
    text.value = ''
    return
  }

  searchUser.value = response.data
  isUser.value = true

  text.value = ''
  friendState.value = await userStore.getFriendState(searchUser.value)
}

const sendReq = async (senderId, receiverId) => {

  isUser.value = false
  searchUser.value = {}
  text.value = ''

  await userStore.sendRequest(senderId, receiverId)
}

</script>

<template>
  <div class="q-pa-md">
    <q-dialog v-model="userStore.addFormDialog" persistent>
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark">
        <q-header style="background-color: white">
          <q-toolbar>
            <q-toolbar-title>
              <div class="text-center q-mt-sm">
                <h5 class="q-ma-none" style="font-weight: bold; color: black">
                  <span class="logo-font">F</span>
                  <span class="logo-font">L</span>
                  <q-icon name="house" class="logo-icon"/>
                  <span class="logo-font">T</span>
                </h5>
              </div>
            </q-toolbar-title>
            <q-btn flat v-close-popup round dense icon="close" style="color: black"/>
          </q-toolbar>
        </q-header>
        <q-page-container style="display: flex; justify-content: center;">
          <div>
            <q-input bottom-slots v-model="text" label="email" @keyup.enter="search(text)" dense style="min-width: 300px">
              <template v-slot:append>
                <q-icon v-if="text !== ''" name="close" @click="text = ''" class="cursor-pointer" />
                <q-icon name="search" @click="search(text)"/>
              </template>
            </q-input>
          </div>
        </q-page-container>
        <div style="display: flex; justify-content: center;">
          <template v-if="isUser">
            <q-card class="my-card">
              <img :src="searchUser.profile">
              <q-card-section>
                <div class="text-subtitle2">name : {{searchUser.name}}</div>
                <div class="text-subtitle2">email : {{searchUser.email}}</div>
                <div class="text-subtitle2">address : {{searchUser.address === null ? 'null' : friend.address}}</div>
                <q-btn v-if="friendState === 0" @click="sendReq(userStore.$state.user.id, searchUser.id)">요청 보내기</q-btn>
                <div v-else-if="friendState === 1" class="text-subtitle2">이미 친구 입니다.</div>
                <div v-else class="text-subtitle2">요청 대기중</div>
              </q-card-section>
            </q-card>
          </template>
        </div>
      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>

</style>
