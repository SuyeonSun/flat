import { defineStore } from 'pinia';
import {authApi} from "boot/auth-api";

export const useAuthStore = defineStore('authStore', {
  state: () => ({
    accessToken: undefined,
    email: undefined,
    name: undefined
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
        const response = await authApi.post("/auth/sign-in", signInPayload);

        if (response.data.code === 200) { // 정상적인 요청
          this.accessToken = response.data.data.accessToken;
          this.email = response.data.data.email;
          this.name = response.data.data.name;
          return true;
        } else if (response.data.code === 2001) { // 유효하지 않은 회원 정보
          return false;
        }
      } catch (error) {
        console.log("error =");
      }
    },

    setAccessToken(token) {
      this.accessToken = token;
    },

    setEmail(email) {
      this.email = email;
    },

    setName(name) {
      this.name = name;
    },

    async signOut(signOutPayload) {
      try {
        await authApi.post("/auth/sign-out", signOutPayload);
      } catch (error) {
        console.log("error");
      }
    }
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
})
