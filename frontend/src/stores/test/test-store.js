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
        this.testResponse = response.data.data.resource;

      } catch (error) {
        console.log("error");
      }
    },

    async insertData() {
      try {
        await api.get("/data");
      } catch (error) {
        console.log("error")
      }
    }
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
});
