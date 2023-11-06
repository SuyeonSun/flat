import { defineStore } from 'pinia';
import {apiA} from "boot/axios";

export const useTestStore = defineStore('testStore', {
  state: () => ({
    testResponse: "init value"
  }),

  actions: {
    async test() {
      try {
        const response = await apiA.get("/resource");
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
