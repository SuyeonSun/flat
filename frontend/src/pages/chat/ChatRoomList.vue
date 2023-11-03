<script setup>

import {api} from "boot/axios"
import {ref, onMounted} from "vue"
import {useChatStore} from "stores/chat/chat-store";
import dialogTest from "pages/dialog.vue";

const chatStore = useChatStore();

const roomId = ref('')
const chatrooms = ref([])
const sender = ref('userA')
const receiver = ref('userB')

onMounted(() => {
  // findAllRoom()
  //   findRoom();
})

const findAllRoom = () => {
  api.get('/chat/rooms').then((response) => {
    chatrooms.value = response.data;
  })
}

const findRoom = async () => {
    let params = new URLSearchParams();
    params.append('sender', sender.value);
    params.append('receiver', receiver.value);

    await api.post('/chat/roomId', params)
        .then((response) => {
            roomId.value = response.data
            console.log('findRoom(): '+roomId.value)
    }).catch((response) => {

    })
}

const createRoom = async () => {
  // if (room_name.value === '') {
  //   alert('방 제목을 입력해 주십시요.');
  //   return;
  // } else {
  //   console.log('roomId.value: ' + roomId.value)

  await findRoom();
  if(roomId.value === '') {
      console.log('createRoom() .......')
      let params = new URLSearchParams();
      // params.append('name', room_name.value);
      params.append('sender', sender.value);
      params.append('receiver', receiver.value);
      await api
          .post('/chat/room', params)
          .then((response) => {
              // alert(response.data.roomName + '방 개설에 성공하였습니다.');
              // alert('방 개설에 성공하였습니다.')
              // room_name.value = '';
              roomId.value = response.data.roomId
              console.log('after create Room roomId: ' + roomId.value)
              // findAllRoom();
          })
          .catch((response) => {
              // alert('채팅방 개설에 실패하였습니다.');
          });
  }

  chatStore.$state.isDialog = true
  // }
    console.log('after create Room roomId: ' + roomId.value)
  enterRoom()
}

const enterRoom = () => {
  // let sender = prompt('대화명을 입력해 주세요.');
  // if (sender !== '') {
    // localStorage.setItem('wschat.sender', sender);
    // localStorage.setItem('wschat.roomId', roomId);
  localStorage.setItem('wschat.sender', sender.value);
  localStorage.setItem('wschat.receiver', receiver.value);
  localStorage.setItem('wschat.roomId', roomId.value);
  // location.href = '/chat/room/enter/' + roomId;
  // }
}

</script>

<template>
  <div class="container" v-cloak>
    <div class="row">
      <div class="col-md-12">
        <h3>1:1 문의하기</h3>
      </div>
    </div>
    <div class="input-group">
<!--      <div class="input-group-prepend">-->
<!--        <label class="input-group-text">방제목</label>-->
<!--      </div>-->
<!--      <input type="text" class="form-control" v-model="room_name" @keyup.enter="createRoom" />-->
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="createRoom">문의하기</button>
      </div>
    </div>
    <ul class="list-group">
      <li class="list-group-item list-group-item-action" v-for="item in chatrooms" :key="item.roomId" @click="enterRoom(item.roomId)">
        {{ item.roomId }}
      </li>
    </ul>
  </div>

  <template v-if="chatStore.$state.isDialog">
    <dialog-test/>
  </template>
</template>

<style scoped>
[v-cloak] {
  display: none;
}
</style>
