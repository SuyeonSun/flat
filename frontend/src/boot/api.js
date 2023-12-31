// quasar new boot axios

import { boot } from 'quasar/wrappers'
import {useAuthStore} from "stores/auth/auth-store";
import axios from "axios";
import {storeToRefs} from "pinia";

const api = axios.create({
  baseURL: 'http://127.0.0.1:8000',
  // baseURL: 'http://localhost:8000',
})

export default boot(({ app, store, router }) => {
  const authStore = useAuthStore(store);
  const {accessToken} = storeToRefs(authStore);

  api.interceptors.request.use((config) => {
    config.headers.Authorization = `Bearer ${accessToken.value}`
    return config
  }, function (error) {
    return Promise.reject(error)
  })

  api.interceptors.response.use((res) => {
    // 요청마다 accessToken 받아서 저장하기
    if(res?.data?.data?.accessToken) {
      authStore.setAccessToken(res.data.data.accessToken);
      // 새로 발급된 accessToken으로 다시 요청
      res.config.headers.Authorization = `Bearer ${accessToken.value}`
      return axios.request(res.config)
    }
    // refreshToken 만료 시 sign-in 화면으로 redirect
    if (res?.data?.code === 2101) {
      router.push("/sign-in");
      authStore.setAccessToken(undefined);
      authStore.setEmail(undefined);
      authStore.setName(undefined);
    }
    return res;
  }, function (error) {
    // 에러 발생 시 sign-in 화면으로 redirect
    // router.push("/sign-in");
    return Promise.reject(error)
    // console.log(error);
  })

  app.config.globalProperties.$axios = axios
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = api
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
})

export { api }
