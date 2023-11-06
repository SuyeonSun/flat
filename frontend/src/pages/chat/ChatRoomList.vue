<script setup>

import {useChatStore} from "stores/chat/chat-store";
import dialogTest from "pages/chat/ChatRoomDetail.vue";

const chatStore = useChatStore();

</script>

<template>
  <div class="container" v-cloak>
    <div class="row">
      <div class="col-md-12">
        <h3>1:1 문의하기</h3>
      </div>
    </div>
    <div class="input-group">
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="chatStore.createRoom()">문의하기</button>
      </div>
    </div>
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
