<script setup>
import {api} from 'boot/axios';
import {ref, onMounted} from "vue";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import {useChatStore} from "stores/chat/chat-store";

const roomId = ref('')
const room = ref({})
const sender = ref('')
const message = ref('')
const messages = ref([])
const ws = ref(null)
const reconnect = ref(0)
const prevIsConnected = ref(false)
const chatStore = useChatStore()

onMounted(() => {
  roomId.value = localStorage.getItem('wschat.roomId')
  sender.value = localStorage.getItem('wschat.sender')

  let prevChatHistory = sessionStorage.getItem('chatHistory_' + roomId.value)
  if(prevChatHistory) {
    prevIsConnected.value = !prevIsConnected.value
    messages.value = JSON.parse(prevChatHistory)
  }

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
  messages.value.unshift({ type: recv.type, sender: recv.type === 'ENTER' ? '[알림]' : recv.sender, message: recv.message });
  sessionStorage.setItem('chatHistory_' + roomId.value, JSON.stringify(messages.value));
}

const goBack = () => {
  localStorage.removeItem('wschat.roomId')
  localStorage.removeItem('wschat.sender')
  location.href = '/chat/room'
}

const connectWebSocket = () => {

  ws.value = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));

  ws.value.connect({}, (frame) => {
    ws.value.subscribe(`/topic/chat/room/${roomId.value}`, (message) => {
      const recv = JSON.parse(message.body);
      recvMessage(recv);
    });

    let prevConnected = sessionStorage.getItem('chatHistory_' + roomId.value)
    if(!prevConnected) {
      ws.value.send("/app/chat/message", JSON.stringify({ type:'ENTER', roomId: roomId.value, sender: sender.value }), {});
    }
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
</script>

<template>
  <div>
    <button @click="goBack">돌아가기</button>
    <div class="container">
      <h2>{{ room.name }}</h2>
      <div class="input-group">
        <div class="input-group-prepend">
          <label class="input-group-text">내용</label>
        </div>
        <input type="text" class="form-control" v-model="message" @keyup.enter="sendMessage" />
        <div class="input-group-append">
          <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
        </div>
      </div>
      <ul class="list-group">
        <li class="list-group-item" v-for="message in messages" :key="message.id">
          {{ message.sender }} - {{ message.message }}
        </li>
      </ul>
      <div></div>
    </div>
  </div>
</template>

<style scoped>
[v-cloak] {
  display: none;
}
</style>
