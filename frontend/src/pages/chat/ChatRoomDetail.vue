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

<script>
import {api} from 'boot/axios';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default {
  data() {
    return {
      roomId: '',
      room: {},
      sender: '',
      message: '',
      messages: [],
      ws: null,
      reconnect: 0,
    };
  },
  created() {
    this.roomId = localStorage.getItem('wschat.roomId');
    this.sender = localStorage.getItem('wschat.sender');
    this.findRoom();
    this.connectWebSocket();
  },
  methods: {
    findRoom() {
      api.get(`/chat/room/${this.roomId}`).then((response) => {
        this.room = response.data;
        console.log(this.room)
      });
    },
    sendMessage() {
      // send('url', 'body', {}) 순서 주의
      this.ws.send("/app/chat/message", JSON.stringify({ type: 'TALK', roomId: this.roomId, sender: this.sender, message: this.message }), []);
      this.message = '';
    },
    recvMessage(recv) {
      this.messages.unshift({ type: recv.type, sender: recv.type === 'ENTER' ? '[알림]' : recv.sender, message: recv.message });
    },
    goBack() {
      this.$router.push('/chat/room');
    },
    connectWebSocket() {
      this.ws = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));
      this.ws.connect({}, (frame) => {
        this.ws.subscribe(`/topic/chat/room/${this.roomId}`, (message) => {
          const recv = JSON.parse(message.body);
          this.recvMessage(recv);
        });
        this.ws.send("/app/chat/message", JSON.stringify({ type:'ENTER', roomId: this.roomId, sender: this.sender }), {});
      }, (error) => {
        if (this.reconnect++ <= 5) {
          setTimeout(() => {
            console.log("connection reconnect");
            this.ws = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));
            this.connectWebSocket();
          }, 10 * 1000);
        }
      });
    },
  },
};
</script>

<style scoped>
[v-cloak] {
  display: none;
}
</style>
