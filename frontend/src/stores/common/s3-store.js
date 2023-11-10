import { defineStore } from 'pinia';
import {authApi} from "boot/auth-api";

export const useS3Store = defineStore('s3Store', {
  state: () => ({
  }),

  actions: {
    async uploadFile (file) {
      try {
        const formData = new FormData();
        formData.append("file", file);
        const response = await authApi.post('/user/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        return response.data;
      } catch (error) {
        console.log(error);
      }
    }
  }
})
