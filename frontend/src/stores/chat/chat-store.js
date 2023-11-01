import { defineStore } from 'pinia';
import {api} from "boot/axios";
import {useRouter} from "vue-router";

const $router = useRouter();

export const useChatStore = defineStore('chatStore', {
  state: () -> ({}),

  actions: {
    // Todo...
  }
})
