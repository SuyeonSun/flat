import { defineStore } from 'pinia';
import {api} from "boot/axios";
import {useRouter} from "vue-router";
import {ref} from "vue"
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

const $router = useRouter();

export const useChatStore = defineStore('chatStore', {
  state: () => ({
    // socket: ref(null),
    // chatrooms: ref([])
    isDialog: ref(false)
  }),

  actions: {
    // Todo...

  //   async init() {
  //     this.setupWebSocket();
  //
  //     await this.loadChatRooms();
  //   },
  //
  //   setupWebSocket() {
  //     // SockJS 및 Stomp 설정
  //     const socket = new SockJS('http://localhost:8000/ws/chat');
  //     const stompClient = Stomp.over(socket);
  //
  //     stompClient.connect({}, (frame) => {
  //       console.log('Connected to WebSocket');
  //       // 원하는 채팅방들을 여기에서 subscribe
  //       this.chatrooms.forEach((chatroom) => {
  //         stompClient.subscribe(`/topic/chat/room/${chatroom.roomId}`, (message) => {
  //
  //           const recv = JSON.parse(message.body);
  //           console.log(`Received message: ${message.body}`);
  //         });
  //       });
  //     });
  //
  //     this.socket = stompClient;
  //   },
  //
  //   async loadChatRooms() {
  //     try {
  //       const response = await api.get('/chat/rooms');
  //       this.chatrooms = response.data;
  //     } catch (error) {
  //       console.error('Failed to load chatrooms', error);
  //     }
  //   },
  },
})
