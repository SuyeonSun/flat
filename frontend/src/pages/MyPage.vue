<script setup>
import { ref, watch, onMounted } from 'vue';
import {useAuthStore} from "stores/auth/auth-store";
import {useUserStore} from "stores/user/user-store";
import {useChatStore} from "stores/chat/chat-store";
import friendRequest from "pages/FriendRequest.vue";
import addForm from "pages/FriendAddForm.vue"
import ChatRoomDetail from "pages/chat/ChatRoomDetail.vue";

const authStore = useAuthStore()
const userStore = useUserStore()
const chatStore = useChatStore()

const tab = ref('likes')

onMounted(() => {
  userStore.getUserInfo(authStore.$state.email)
})

watch(tab, async (n) => {
  if(n === 'friends') {
    await userStore.findFriends(userStore.user.id)
  }
})

watch(tab, async (n) => {
  if(n === 'chats') {
    await chatStore.findRooms(authStore.$state.name)
    console.log("@@@@@@@@", chatStore.$state.myRooms)
  }
})

const removeFriend = (friendId) => {
  userStore.deleteFriend(userStore.user.id, friendId)
}

const enterRoom = (room) => {
  const sender = authStore.$state.name
  const receiver = authStore.$state.name == room.sender ? room.receiver : room.sender
  const roomId = room.roomId
  chatStore.enterRoom(sender, receiver, roomId)
}

</script>

<template>
  <div class="q-pa-md">
    <div class="q-gutter-y-md" style="max-width: 90%">
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
          <q-tab name="likes" label="관심 매물" />
          <q-tab name="mines" label="등록 매물" />
          <q-tab name="friends" label="친구" />
          <q-tab name="chats" label="내 문의" />
        </q-tabs>

        <q-separator />

        <q-tab-panels v-model="tab" animated>
          <q-tab-panel name="likes">
            <div class="text-h6">Mails</div>
            Lorem ipsum dolor sit amet consectetur adipisicing elit.
          </q-tab-panel>

          <q-tab-panel name="mines">
            <div class="text-h6">Alarms</div>
            Lorem ipsum dolor sit amet consectetur adipisicing elit.
          </q-tab-panel>

          <q-tab-panel name="friends">
            <div class="row justify-between">
              <div class="q-pa-md row items-start q-gutter-md">
                <q-card v-for="friend in userStore.$state.friends" :key="friend" class="my-card">
                  <img :src="friend.profile">
                  <q-card-section>
                    <div class="text-subtitle2">name : {{friend.name}}</div>
                    <div class="text-subtitle2">email : {{friend.email}}</div>
                    <div class="text-subtitle2">address : {{friend.address === null ? 'null' : friend.address}}</div>
                    <q-btn @click="removeFriend(friend.id)" style="margin-top: 5px">친구 삭제</q-btn>
                  </q-card-section>
                </q-card>
              </div>
              <div>
                <q-btn style="margin-right: 10px" @click="userStore.viewAddForm">친구 추가</q-btn>
                <q-btn @click="userStore.getFriendRequest(authStore.email)">요청 확인</q-btn>
              </div>
            </div>
          </q-tab-panel>

          <q-tab-panel name="chats">
            <q-card v-for="room in chatStore.$state.myRooms" :key="room" class="my-card">
              <div class="text-subtitle2">상대방 : {{authStore.$state.name == room.sender ? room.receiver : room.sender}}</div>
              <q-btn @click="enterRoom(room)">채팅방 입장</q-btn>
            </q-card>
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
    </div>
  </div>
  <template v-if="userStore.$state.addFormDialog">
    <add-form/>
  </template>
  <template v-if="userStore.$state.reqDialog">
    <friend-request/>
  </template>
  <template v-if="chatStore.$state.isDialog">
    <chat-room-detail/>
  </template>
</template>

<style scoped>

</style>
