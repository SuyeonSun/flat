<script setup>

import {api} from "boot/axios"
import {ref, onMounted} from "vue"

const room_name = ref('')
const chatrooms = ref([])

onMounted(() => {
  findAllRoom()
})

const findAllRoom = () => {
  api.get('/chat/rooms').then((response) => {
    chatrooms.value = response.data;
  })
}

const createRoom = () => {
  if (room_name.value === '') {
    alert('방 제목을 입력해 주십시요.');
    return;
  } else {
    let params = new URLSearchParams();
    params.append('name', room_name.value);
    api
      .post('/chat/room', params)
      .then((response) => {
        alert(response.data.roomName + '방 개설에 성공하였습니다.');
        room_name.value = '';
        findAllRoom();
      })
      .catch((response) => {
        alert('채팅방 개설에 실패하였습니다.');
      });
  }
}

const enterRoom = (roomId) => {
  let sender = prompt('대화명을 입력해 주세요.');
  if (sender !== '') {
    localStorage.setItem('wschat.sender', sender);
    localStorage.setItem('wschat.roomId', roomId);
    location.href = '/chat/room/enter/' + roomId;
  }
}

</script>

<template>
  <div class="container" v-cloak>
    <div class="row">
      <div class="col-md-12">
        <h3>채팅방 리스트</h3>
      </div>
    </div>
    <div class="input-group">
      <div class="input-group-prepend">
        <label class="input-group-text">방제목</label>
      </div>
      <input type="text" class="form-control" v-model="room_name" @keyup.enter="createRoom" />
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="createRoom">채팅방 개설</button>
      </div>
    </div>
    <ul class="list-group">
      <li class="list-group-item list-group-item-action" v-for="item in chatrooms" :key="item.roomId" @click="enterRoom(item.roomId)">
        {{ item.roomName }}
      </li>
    </ul>
  </div>
</template>

<style scoped>
[v-cloak] {
  display: none;
}
</style>
