import { defineStore } from 'pinia';
import {api} from "boot/api";

export const useTestStore = defineStore('testStore', {
  state: () => ({
    testResponse: "init value"
  }),

  actions: {
    async test() {
      try {
        const response = await api.get("/resource");
        console.log("---------------------", response)
      } catch (error) {
        console.log("error");
      }
    },
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
});
