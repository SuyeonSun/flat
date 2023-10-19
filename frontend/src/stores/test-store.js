import { defineStore } from 'pinia';
import {api} from "boot/axios";

export const useTestStore = defineStore('testStore', {
  state: () => ({
    // counter: 0,
  }),

  actions: {
    async test() {
      try {
        await api.post("/test");
      } catch (error) {
        console.log("error")
      }
    },
  },
});
