// quasar new boot auth-axios

import { boot } from 'quasar/wrappers'
import {useAuthStore} from "stores/auth/auth-store";
import axios from "axios";
import {api} from "boot/axios"

const authApi = axios.create({
  // baseURL: 'http://127.0.0.1:8000',
  baseURL: 'http://localhost:8000',
})

export default boot(({ app, store }) => {
  const authStore = useAuthStore(store);

  api.interceptors.response.use((res) => {
    // 요청마다 accessToken 받아서 저장하기
    if(res?.data?.data?.accessToken){
      authStore.setAccessToken(res.data.data.accessToken);
    }

  }, function (error) {
    return Promise.reject(error)
  })


  app.config.globalProperties.$axios = axios

  app.config.globalProperties.$api = authApi
})

export { authApi }
