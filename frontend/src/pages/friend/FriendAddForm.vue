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
  isUser.value = false

  if(email === authStore.email) {
    Notify.create({
      message: '본인은 친구 추가 불가능합니다.',
      color: "red"
    })

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
  text.value = ''
  friendState.value = await userStore.getFriendState(searchUser.value)
  isUser.value = true
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
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark" style="max-height: 550px; max-width: 430px">
        <q-header style="background-color: white">
          <q-toolbar>
            <q-toolbar-title>
            </q-toolbar-title>
            <q-btn flat v-close-popup round dense icon="close" style="color: black"/>
          </q-toolbar>
        </q-header>

        <q-page-container style="display: flex; justify-content: center;">
          <div>
            <q-item-label caption class="q-mb-xs">추가하고 싶은 친구의 이메일을 검색해주세요.</q-item-label>
            <q-input bottom-slots v-model="text" label="이메일" @keyup.enter="search(text)" dense style="min-width: 300px">
              <template v-slot:append>
                <q-icon v-if="text !== ''" name="close" @click="text = ''" class="cursor-pointer" />
                <q-icon name="search" @click="search(text)"/>
              </template>
            </q-input>
          </div>
        </q-page-container>

        <div style="display: flex; justify-content: center;">
          <div v-if="isUser" class="q-pa-md">
            <q-item clickable v-ripple>
              <q-item-section side>
                <q-avatar rounded size="48px">
                  <img :src="searchUser.profile" />
                </q-avatar>
              </q-item-section>
              <q-item-section>
                <q-item-label>{{searchUser.email}}</q-item-label>
                <q-item-label caption>{{searchUser.name}}</q-item-label>
                <q-item-label caption>{{searchUser.address === null ? 'null' : searchUser.address}}</q-item-label>
              </q-item-section>
            </q-item>
            <div class="row justify-end q-mb-xs">
              <q-btn dense class="col-2" outline v-if="friendState === 0" @click="sendReq(userStore.$state.user.id, searchUser.id)" style="background-color: lightskyblue">요청</q-btn>
              <q-item-label style="font-size: 12px; color: darkred" v-if="friendState === 1">이미 친구 입니다.</q-item-label>
              <q-item-label style="font-size: 12px; color: #117CE9" v-if="friendState === 2">요청 대기중</q-item-label>
            </div>
          </div>
          <div v-else style="padding-top: 180px">
            검색 결과가 없습니다.
          </div>
        </div>
      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>

</style>
