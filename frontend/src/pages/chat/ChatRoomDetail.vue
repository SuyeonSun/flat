<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import {useChatStore} from "stores/chat/chat-store";
import {useUserStore} from "stores/user/user-store";
import {storeToRefs} from "pinia";
import moment from "moment"

const chatStore = useChatStore();
const userStore = useUserStore();

const {messages} = storeToRefs(chatStore)

const moreContent = ref(true)
const contentSize = computed(() => moreContent.value ? 150 : 5)
const drawer = ref(false)
const drawerR = ref(false)

const input = ref('');

const receiverProfile = ref('')
const senderProfile = ref('')
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
  const response = await userStore.getUserProfile(chatStore.$state.sender)
  senderProfile.value = response.data
  const response2 = await userStore.getUserProfile(chatStore.$state.receiver)
  receiverProfile.value = response2.data
  scrollRef.value.setScrollPercentage('vertical', 1)
})

// 스크롤 맨 아래로 유지
watch(messages, (n) => {

  setTimeout(function () {
    scrollRef.value.setScrollPercentage('vertical', 1)
  }, 100)},

  {
    deep: true
  }
)

</script>

<template>
  <div class="q-pa-md q-gutter-sm">

    <q-dialog v-model="chatStore.$state.isDialog" persistent>
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark">

        <q-header style="background-color: black">
          <q-toolbar>
            <q-toolbar-title class="text-center" style="font-size: 16px;">1:1 문의</q-toolbar-title>
            <q-btn flat v-close-popup round dense icon="close" @click="chatStore.clear()"/>
          </q-toolbar>
        </q-header>

        <q-footer class="bg-white text-dark" bordered>
          <q-toolbar>
            <q-input bottom-slots v-model="input" label="채팅을 입력해주세요." @keyup.enter="sendMsg" style="width: 100%">
              <template v-slot:append>
                <q-icon v-if="input !== ''" name="close" @click="input = ''" class="cursor-pointer" />
              </template>
              <template v-slot:after>
                <q-btn round dense flat icon="send" @click="sendMsg"/>
              </template>
            </q-input>
          </q-toolbar>
        </q-footer>

        <q-scroll-area style="height: 915px;" ref="scrollRef">
          <q-page-container>
            <q-page padding>
              <div class="row justify-center">
                <div style="width: 100%; max-width: 400px">
                  <div v-for="(message, index) in chatStore.$state.messages" :key="message">
                    <q-chat-message
                      v-if="message.sender === chatStore.$state.sender"
                      :name=message.sender
                      :avatar=senderProfile
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
