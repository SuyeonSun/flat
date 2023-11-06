import { boot } from 'quasar/wrappers'
import axios from 'axios'
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

const api = axios.create({
  // baseURL: 'http://127.0.0.1:8000',
  baseURL: 'http://localhost:8000',
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
    if(res?.data?.data?.accessToken){
      authStore.setAccessToken(res.data.data.accessToken);
    }
  }, function (error) {
    // refreshToken 만료 시 login으로 redirect
    router.push("/sign-in");
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
