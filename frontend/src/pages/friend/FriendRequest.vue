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
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark" style="max-height: 550px; max-width: 800px">
        <q-header style="background-color: white">
          <q-toolbar>
            <q-toolbar-title>
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
                  <div v-if="userStore.$state.recvRequests.length === 0" style="margin-left: 320px; margin-top: 200px">
                    받은 요청이 없습니다.
                  </div>
                  <q-card flat bordered
                          v-for="friend in userStore.$state.recvRequests"
                          :key="friend"
                          class="row justify-between items-center q-mb-sm"
                  >
                    <q-item clickable v-ripple>
                      <q-item-section side>
                        <q-avatar rounded size="48px">
                          <img :src="friend.profile" />
                        </q-avatar>
                      </q-item-section>
                      <q-item-section>
                        <q-item-label>{{friend.email}}</q-item-label>
                        <q-item-label caption>{{friend.name}}</q-item-label>
                        <q-item-label caption>{{friend.address === null ? 'null' : friend.address}}</q-item-label>
                      </q-item-section>
                    </q-item>
                    <div class="row justify-end">
                      <div>
                        <q-btn @click="userStore.acceptRequest(friend)" class="col-2 q-mr-sm" style="background-color: lightskyblue">수락</q-btn>
                        <q-btn @click="userStore.rejectRequest(friend)" class="col-2 q-mr-md" style="background-color: lightgray">거절</q-btn>
                      </div>
                    </div>
                  </q-card>
                </q-tab-panel>

                <q-tab-panel name="send">
                  <div v-if="userStore.$state.sendRequests.length === 0" style="margin-left: 320px; margin-top: 200px">
                    보낸 요청이 없습니다.
                  </div>
                  <q-card flat bordered v-for="friend in userStore.$state.sendRequests" :key="friend" class="row justify-between items-center q-mb-sm">
                    <q-item clickable v-ripple>
                      <q-item-section side>
                        <q-avatar rounded size="48px">
                          <img :src="friend.profile" />
                        </q-avatar>
                      </q-item-section>
                      <q-item-section>
                        <q-item-label>{{friend.email}}</q-item-label>
                        <q-item-label caption>{{friend.name}}</q-item-label>
                        <q-item-label caption>{{friend.address === null ? 'null' : friend.address}}</q-item-label>
                      </q-item-section>
                    </q-item>
                    <div class="row justify-end">
                      <div>
                        <q-btn @click="userStore.removeRequest(friend)" class="col-2 q-mr-md" style="background-color: lightgray">취소</q-btn>
                      </div>
                    </div>
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
