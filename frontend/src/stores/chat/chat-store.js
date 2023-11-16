import { defineStore } from 'pinia';
import {api} from "boot/api";
import {useRouter} from "vue-router";
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import {charAt} from "core-js/internals/string-multibyte";
import moment from "moment"

export const useChatStore = defineStore('chatStore', {
  state: () => ({
    sender: '',
    receiver: '',
    roomId: undefined,
    isDialog: false,
    ws: null,
    reconnect: 0,
    messages: [],
    myRooms: [],
  }),

  actions: {
    setSenderReceiver(sender, receiver) {
      this.sender = sender
      this.receiver = receiver
    },

    // 방 번호 찾기: 외부에서 호출x
    async findRoomId() {
      try {
        return await api.post('/chat/find/room', {
          sender: this.sender,
          receiver: this.receiver
        })
      } catch (e) {
        console.log(e);
      }
    },

    async findRooms(user) {
      try {
        const res = await api.post('/chat/find/rooms', {
          user: user
        })
        if(res.data) {
          this.myRooms = res.data
        }
      } catch (e) {
        console.log(e)
      }
    },


    // 방 번호 찾고, 없으면 방 생성
    async createRoom(sender, receiver) {
      try {
        this.setSenderReceiver(sender, receiver)
        const res1 = await this.findRoomId();

        if(res1.data) {
          this.roomId = res1.data.roomId;
        }

        if(!this.roomId) {
          const res2 = await api.post('/chat/room', {
            sender: this.sender,
            receiver: this.receiver
          })

          this.roomId = res2.data.roomId;
        }
        this.isDialog = true;

      } catch (e) {
        console.log(e);
      }
    },

    enterRoom(sender, receiver, roomId) {
      this.sender = sender
      this.receiver = receiver
      this.roomId = roomId
      this.isDialog = true
    },

    // 소켓 연결 후, 서버에서 기존 메시지 가져오기
    async readyToChat() {
      this.connectWebSocket();
      await this.getPrevMessages();

    },

    async getPrevMessages() {
      const res = await api.post('/chat/messages', {
        roomId: this.roomId
      });
      this.messages = res.data;
    },

    connectWebSocket() {
      this.ws = Stomp.over(new SockJS("http://localhost:8000/ws/chat"));

      this.ws.connect({}, (frame) => {
        this.ws.subscribe(`/topic/chat/room/${this.roomId}`, (message) => {
          const recv = JSON.parse(message.body);
          this.recvMessage(recv);
        });
      }, (error) => {
        if (this.reconnect++ <= 5) {
          setTimeout(() => {
            console.log("connection reconnect");
            this.connectWebSocket();
          }, 10 * 1000);
        }
      })
    },

    sendMessage(message) {
      this.ws.send("/app/chat/message",
        JSON.stringify({ type: 'TALK', roomId: this.roomId, sender: this.sender, message: message, date: moment().format() }), []);
    },

    recvMessage(recv) {
      this.messages.push({ type: recv.type, sender: recv.type === 'ENTER' ? '[알림]' : recv.sender, message: recv.message });
    },

    disconnectWebSocket() {
      this.ws.disconnect();
      this.ws = null;
      this.reconnect = 0;
    },

    clear() {
      this.roomId = undefined;
      this.isDialog = !this.isDialog;
      this.messages = [];
      this.disconnectWebSocket();
    }
  },
})
