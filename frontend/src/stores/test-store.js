import { defineStore } from 'pinia';
import {api} from "boot/axios";

export const useTestStore = defineStore('testStore', {
  state: () => ({
  }),

  actions: {
    async test() {
      try {
        await api.post("/auth/resource");
      } catch (error) {
        console.log("error")
      }
    },
  },
});
