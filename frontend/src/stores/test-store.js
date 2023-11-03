import { defineStore } from 'pinia';
import {api} from "boot/axios";

export const useTestStore = defineStore('testStore', {
  state: () => ({
    count: 0,
  }),

  actions: {
    async test() {
      try {
        this.count++;
      } catch (error) {
        console.log("error")
      }
    },
  },

  persist: {
    enabled: true,
    strategies: [{ storage: localStorage }],
  },
});
