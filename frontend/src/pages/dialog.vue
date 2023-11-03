<script setup>
import { ref, computed, onMounted } from 'vue'
import {useChatStore} from "stores/chat/chat-store";
import {api} from 'boot/axios';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

const roomId = ref('')
const room = ref({})
const sender = ref('')
const receiver = ref('')
const message = ref('')
const messages = ref([])
const ws = ref(null)
const reconnect = ref(0)

onMounted(() => {
  // roomId.value = localStorage.getItem('wschat.roomId')
  // sender.value = localStorage.getItem('wschat.sender')
  sender.value = localStorage.getItem('wschat.sender');
  receiver.value = localStorage.getItem('wschat.receiver');
  roomId.value = localStorage.getItem('wschat.roomId');

  // let prevChatHistory = sessionStorage.getItem('chatHistory_' + roomId.value)
  // if(prevChatHistory) {
  //   prevIsConnected.value = !prevIsConnected.value
  //   messages.value = JSON.parse(prevChatHistory)
  // }

  api.get(`/chat/messages/${roomId.value}`).then((response) => {
    messages.value = response.data
  })

  findRoom()

  connectWebSocket()
})

const findRoom = () => {
  api.get(`/chat/room/${roomId.value}`).then((response) => {
    room.value = response.data;
  });
}

const sendMessage = () => {
  ws.value.send("/app/chat/message", JSON.stringify({ type: 'TALK', roomId: roomId.value, sender: sender.value, message: message.value }), []);
  message.value = '';
}

const recvMessage = (recv) => {
  messages.value.push({ type: recv.type, sender: recv.type === 'ENTER' ? '[알림]' : recv.sender, message: recv.message });
  // sessionStorage.setItem('chatHistory_' + roomId.value, JSON.stringify(messages.value));
}

const goBack = () => {
  // localStorage.removeItem('wschat.roomId')
  // localStorage.removeItem('wschat.sender')
  localStorage.removeItem('wschat.sender');
  localStorage.removeItem('wschat.receiver');
  localStorage.removeItem('wschat.roomId');
  location.href = '/chat/room'
}

const connectWebSocket = () => {

  ws.value = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));

  ws.value.connect({}, (frame) => {
    ws.value.subscribe(`/topic/chat/room/${roomId.value}`, (message) => {
      const recv = JSON.parse(message.body);
      recvMessage(recv);
    });
    //
    // let prevConnected = sessionStorage.getItem('chatHistory_' + roomId.value)
    // if(!prevConnected) {
    //   ws.value.send("/app/chat/message", JSON.stringify({ type:'ENTER', roomId: roomId.value, sender: sender.value }), {});
    // }
  }, (error) => {
    if (reconnect.value++ <= 5) {
      setTimeout(() => {
        console.log("connection reconnect");
        ws.value = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));
        connectWebSocket();
      }, 10 * 1000);
    }
  })
}
const chatStore = useChatStore()

const moreContent = ref(true)
const contentSize = computed(() => moreContent.value ? 150 : 5)
const drawer = ref(false)
const drawerR = ref(false)

const changeState = () => {
  chatStore.isDialog = !chatStore.isDialog
}
</script>

<template>
  <div class="q-pa-md q-gutter-sm">
<!--    <q-btn label="Click me" color="primary" @click="layout = true" />-->

    <q-dialog v-model="chatStore.isDialog" persistent>
      <q-layout view="Lhh lpR fff" container class="bg-white text-dark">
        <q-header class="bg-primary">
          <q-toolbar>
<!--            <q-btn flat @click="drawer = !drawer" round dense icon="menu" />-->
            <q-toolbar-title class="text-center">1:1 문의</q-toolbar-title>
<!--            <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />-->
            <q-btn flat v-close-popup round dense icon="close" @click="changeState"/>
          </q-toolbar>
        </q-header>

        <q-footer class="bg-white text-dark">
          <q-toolbar>
              <q-input type="text" v-model="message" @keyup.enter="sendMessage" ></q-input>
              <div class="input-group-append">
                  <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
              </div>
          </q-toolbar>
        </q-footer>

        <q-drawer bordered v-model="drawer" :width="200" :breakpoint="600" class="bg-grey-3 text-dark q-pa-sm">
          <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>
        </q-drawer>

        <q-drawer side="right" bordered v-model="drawerR" :width="200" :breakpoint="300" class="bg-grey-3 text-dark q-pa-sm">
          <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>
        </q-drawer>

        <q-page-container>
          <q-page padding>
            <p v-for="message in messages" :key="message">
              {{message.sender}}: {{message.message}}
            </p>
          </q-page>
        </q-page-container>
      </q-layout>
    </q-dialog>
  </div>
</template>

<style scoped>
</style>
