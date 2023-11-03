import { defineStore } from 'pinia';
import { ref } from "vue";
import axios from "axios";

let requestInterceptor = ref(null);


// persist (상태 유지)를 이용해서 interceptor 추가
// Todo... 1. code: 2101, EXPIRED_REFRESH_TOKEN시에 처리 추가 -> 이것이 accessToken 만료가 맞나?
// Todo... 2. 403 에러가 오는 이유? (아주 가끔)
// Todo... 3. accessToken 만료 전에 로그인을 하면, 새로운 accessToken/refreshToken 반환해줌
// Todo... 4. refreshToken이 만료된 후에 로그인을 하면 code: 2101, EXPIRED_REFRESH_TOKEN 반환
const createApi = () => {
  const api = axios.create({
    baseURL: 'http://127.0.0.1:8000',
  })

  if(useAuthStore().$state.accessToken) {
    api.interceptors.request.use((config) => {
      config.headers.Authorization = `Bearer ${useAuthStore().$state.accessToken}`;
      return config;
    })
  }

  return api;
}


export const useAuthStore = defineStore('authStore', {
  state: () => ({
    accessToken: undefined,
    email: undefined
  }),

  actions: {
    async signUp(signUpPayload) {
      try {
        const response = await createApi().post("/auth/sign-up", signUpPayload);
        return response.data.code !== 2000;
      } catch (error) {
        console.log("error");
      }
    },

    async signIn(signInPayload) {
      try {
        // localStorage.removeItem('authStore');
        const response = await createApi().post("/auth/sign-in", signInPayload);
        if (response.data.code === 200) { // 정상적인 요청
          this.accessToken = response.data.data.accessToken;
          this.email = response.data.data.email;

          return true;
        } else if (response.data.code === 2001) { // 유효하지 않은 회원 정보
            console.log('만료')
          return false;
        }
      } catch (error) {
        console.log("signIn error");
      }
    },
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}]
  }
})

export {createApi}
