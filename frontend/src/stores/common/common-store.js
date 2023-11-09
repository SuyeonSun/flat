import { defineStore } from 'pinia';
import {api} from "boot/api";

export const useCommonStore = defineStore('commonStore', {
  state: () => ({
  }),

  actions: {
    async uploadFile (file) {
      try {
        const formData = new FormData();
        formData.append("file", file);
        const response = await api.post('/user/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        return response.data;
      } catch (error) {
        console.log(error);
      }
    }
  }
})
