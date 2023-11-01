import { defineStore } from 'pinia';
import {api} from "boot/axios";
import {Notify, Quasar} from "quasar";
import {useRouter} from "vue-router";

const $router = useRouter();

export const useAuthStore = defineStore('authStore', {
  state: () => ({
  }),

  actions: {
    async signUp(signUpPayload) {
      try {

        const response = await api.post("/auth/sign-up", signUpPayload);
        return response.data.code !== 2000;
        // await $router.push("/test");

      } catch (error) {
        console.log("error");
      }
    }
  }
})
