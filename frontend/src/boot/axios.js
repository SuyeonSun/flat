import { boot } from 'quasar/wrappers'
import axios from 'axios'
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

// Be careful when using SSR for cross-request state pollution
// due to creating a Singleton instance here;
// If any client changes this (global) instance, it might be a
// good idea to move this instance creation inside of the
// "export default () => {}" function below (which runs individually
// for each client)

const authStore = useAuthStore();

const token = authStore.accessToken;

const api = axios.create({
  baseURL: 'http://127.0.0.1:8000',

  headers: {
    // "Authorization":token !== null ? `Bearer ${token}` : undefined
    // "Content-Type":"application/json",
    // "Access-Control-Allow-Origin" : "*",
    // "Access-Control-Allow-Headers": "*",
    // "Access-Control-Allow-Credentials": true
  }
})
// const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdW5pbmlzeUBnbWFpbC5jb20iLCJpYXQiOjE2OTg5OTYxNzMsImV4cCI6MTY5ODk5NjIzM30.US4L1XxVPI3iC78t1arZJZXWPv3tRKskCsa3igom4R4';

export default boot(({ app }) => {
  // for use inside Vue files (Options API) through this.$axios and this.$api


  // Add a request interceptor
  /*api.interceptors.request.use((config) => {
      // Do something before request is sent
      config.headers.Authorization = `Bearer ${token}`

      // Access-Control-Allow-Credentials: true
      return config
    }, function (error) {
      // Do something with request error
      return Promise.reject(error)
      // console.log(error);
    })*/

  app.config.globalProperties.$axios = axios
  // ^ ^ ^ this will allow you to use this.$axios (for Vue Options API form)
  //       so you won't necessarily have to import axios in each vue file

  app.config.globalProperties.$api = api
  // ^ ^ ^ this will allow you to use this.$api (for Vue Options API form)
  //       so you can easily perform requests against your app's API
})

export { api }
