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
      } catch (error) {
        console.log("error");
      }
    },

    async signIn(signInPayload) {
      try {
        const response = await api.post("/auth/sign-in", signInPayload);
        return response.data.code !== 2001;
      } catch (error) {
        console.log("error");
      }
    }
  }
})
