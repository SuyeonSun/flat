import { defineStore } from 'pinia';
import {createApi} from "stores/auth/auth-store";
import {useAuthStore} from "stores/auth/auth-store";

const authStore = useAuthStore();

export const useTestStore = defineStore('testStore', {
  state: () => ({
  }),

  actions: {
    async test() {
      try {
        console.log(authStore.accessToken)
        await createApi().get("/auth/resource");
      } catch (error) {
        console.log("testStore error")
      }
    },
  },
});
