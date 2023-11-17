<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import {useChatStore} from "stores/chat/chat-store";
import {useUserStore} from "stores/user/user-store";
import moment from "moment"

const chatStore = useChatStore();
const userStore = useUserStore();

const moreContent = ref(true)
const contentSize = computed(() => moreContent.value ? 150 : 5)
const drawer = ref(false)
const drawerR = ref(false)

const input = ref('');

const receiverProfile = ref('')
const scrollRef = ref(null)

const sendMsg = async () => {
  await chatStore.sendMessage(input.value);
  input.value='';
};

const getGap = (date) => {
  const gapMin = moment().diff(date, "minutes")
  let res = gapMin + " minutes ago"

  if(gapMin > 60) {
    res = moment().diff(date, "hours") + " hours ago"
  }

  if(gapMin > 60*24) {
    res = moment().diff(date, "days") + " days ago"
  }

  return res
}

onMounted(async () => {
  await chatStore.readyToChat();
  const response = await userStore.getUserProfile(chatStore.$state.receiver)
  receiverProfile.value = response.data
  scrollRef.value.setScrollPercentage('vertical', 1)
})

// 스크롤 맨 아래로 유지
watch(() => {
  if(scrollRef.value && scrollRef.value.getScroll().verticalPercentage !== 1) {
    scrollRef.value.getScroll().verticalPercentage
  }},  () => {
    scrollRef.value.setScrollPercentage('vertical', 1)
  }, {
    deep: true
  }
)

</script>

<template>
  <div class="q-pa-md q-gutter-sm">
<!--    <q-btn label="Click me" color="primary" @click="layout = true" />-->

    <q-dialog v-model="chatStore.$state.isDialog" persistent>
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark">
        <q-header class="bg-primary">
          <q-toolbar>
<!--            <q-btn flat @click="drawer = !drawer" round dense icon="menu" />-->
            <q-toolbar-title class="text-center">1:1 문의</q-toolbar-title>
<!--            <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />-->
            <q-btn flat v-close-popup round dense icon="close" @click="chatStore.clear()"/>
          </q-toolbar>
        </q-header>

        <q-footer class="bg-white text-dark">
          <q-toolbar>
              <q-input type="text" v-model="input" @keyup.enter="sendMsg" ></q-input>
              <div class="input-group-append">
                  <button class="btn btn-primary" type="button" @click="sendMsg">보내기</button>
              </div>
          </q-toolbar>
        </q-footer>

<!--        <q-drawer bordered v-model="drawer" :width="200" :breakpoint="600" class="bg-grey-3 text-dark q-pa-sm">-->
<!--          <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>-->
<!--        </q-drawer>-->

<!--        <q-drawer side="right" bordered v-model="drawerR" :width="200" :breakpoint="300" class="bg-grey-3 text-dark q-pa-sm">-->
<!--          <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>-->
<!--        </q-drawer>-->

        <q-scroll-area style="height: 800px;" ref="scrollRef">
          <q-page-container>
            <q-page padding>
              <div class="row justify-center">
                <div style="width: 100%; max-width: 400px">
                  <div v-for="(message, index) in chatStore.$state.messages" :key="message">
                    <q-chat-message
                      v-if="message.sender === chatStore.$state.sender"
                      :name=message.sender
                      :avatar=userStore.user.profile
                      :text="[message.message]"
                      :stamp= getGap(message.date)
                      sent
                    />
                    <q-chat-message
                      v-else
                      :name=message.sender
                      :avatar=receiverProfile
                      :text="[message.message]"
                      :stamp= getGap(message.date)
                    />
                  </div>
                </div>
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
