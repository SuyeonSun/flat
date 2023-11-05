import { boot } from 'quasar/wrappers'
import axios from 'axios'
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

// import {LocalStorage} from "quasar";

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
    // TODO: 요청마다 accessToken 받아서 저장하기
    if(res?.data?.data?.accessToken){
      authStore.setAccessToken(res.data.data.accessToken);
    }

    console.log("res: ",res);
    // TODO: refreshToken 만료 시 login으로 redirect
    if(res?.data?.data?.code == '2101'){
      router.push({
        path: '/sign-in'
      })
    }

  }, function (error) {
    // Do something with request error
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
