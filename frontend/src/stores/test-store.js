import { defineStore } from 'pinia';
import {api} from "boot/axios";

export const useTestStore = defineStore('testStore', {
  state: () => ({
  }),

  actions: {
    async test() {
      try {
        let userInfo = {
          firstName:"Fred",
          lastName:"A"
        }

        await api.post("/test", userInfo);
        // let firstName = "userInfo.firstName;
        // await api.post(`/test?firstName="${firstName}"`);
      } catch (error) {
        console.log("error")
      }
    },
  },
});
