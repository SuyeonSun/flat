import { defineStore } from 'pinia';
import {authApi} from "boot/auth-axios";

export const useAuthStore = defineStore('authStore', {
  state: () => ({
    accessToken: undefined,
    email: undefined
  }),

  actions: {
    async signUp(signUpPayload) {
      try {
        const response = await authApi.post("/auth/sign-up", signUpPayload);
        return response.data.code !== 2000;
      } catch (error) {
        console.log("error");
      }
    },

    async signIn(signInPayload) {
      try {
        console.log("================ 1. before signIn api")
        const response = await authApi.post("/auth/sign-in", signInPayload);
        if (response.data.code === 200) { // 정상적인 요청
          // TODO
          this.accessToken = response.data.data.accessToken;
          this.email = response.data.data.email;
          console.log("================ 4.")
          return true;
        } else if (response.data.code === 2001) { // 유효하지 않은 회원 정보
          return false;
        }
      } catch (error) {
        console.log("error");
      }
    },

    setAccessToken(token) {
      console.log("================ 3. setAccessToken")
      this.accessToken = token;
    }
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
})
