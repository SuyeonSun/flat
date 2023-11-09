<script setup>
import { ref, watch, onMounted } from 'vue';
import {useAuthStore} from "stores/auth/auth-store";
import {useUserStore} from "stores/user/user-store";

const authStore = useAuthStore()
const userStore = useUserStore()

const tab = ref('recv')

</script>

<template>
  <div class="q-pa-md">
    <q-dialog v-model="userStore.reqDialog" persistent>
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
        <q-page-container>
          <div class="q-gutter-y-md">
            <q-card>
              <q-tabs
                v-model="tab"
                dense
                class="text-grey"
                active-color="primary"
                indicator-color="primary"
                align="justify"
                narrow-indicator
              >
                <q-tab name="recv" label="받은 요청" />
                <q-tab name="send" label="보낸 요청" />
              </q-tabs>

              <q-separator />

              <q-tab-panels v-model="tab" animated>
                <q-tab-panel name="recv">
                  <q-card v-for="friend in userStore.$state.recvRequests" :key="friend" class="my-card">
                    <img :src="friend.profile">
                    <q-card-section>
                      <div class="text-subtitle2">name : {{friend.name}}</div>
                      <div class="text-subtitle2">email : {{friend.email}}</div>
                      <div class="text-subtitle2">address : {{friend.address === null ? 'null' : friend.address}}</div>
                      <q-btn @click="userStore.acceptRequest(friend)">수락</q-btn>
                      <q-btn @click="userStore.rejectRequest(friend)">거절</q-btn>
                    </q-card-section>
                  </q-card>
                </q-tab-panel>

                <q-tab-panel name="send">
                  <q-card v-for="friend in userStore.$state.sendRequests" :key="friend" class="my-card">
                    <img :src="friend.profile">
                    <q-card-section>
                      <div class="text-subtitle2">name : {{friend.name}}</div>
                      <div class="text-subtitle2">email : {{friend.email}}</div>
                      <div class="text-subtitle2">address : {{friend.address === null ? 'null' : friend.address}}</div>
                      <q-btn @click="userStore.removeRequest(friend)">요청 삭제</q-btn>
                    </q-card-section>
                  </q-card>
                </q-tab-panel>
              </q-tab-panels>
            </q-card>
          </div>
        </q-page-container>
      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>

.logo-font {
  font-size: 30px;

}

.logo-icon {
  font-size: 37px;
  margin-bottom: 10px;
}
</style>
