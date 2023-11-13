import { defineStore } from 'pinia';
import {api} from "boot/api";
import {useAuthStore} from "stores/auth/auth-store";

export const usePropertyStore = defineStore('propertyStore', {
  state: () => ({
    propertyDetail: undefined,
  }),

  actions: {
    async getPropertyDetail(propertyId, email) {
      try {
        const response = await api.get(`/property/detail/${propertyId}?email=${email}`);
        this.propertyDetail = response.data.data;
      } catch (error) {
        console.log("error");
      }
    },

    async registerProperty(email, registerPayload) {
      try {
        await api.post(`/property?email=${email}`, registerPayload);
      } catch (error) {
        console.log("error");
      }
    },

    async like(likePayload) {
      try {
        await api.post("/like", likePayload);
        console.log(likePayload)
      } catch (error) {
        console.log("error");
      }
    }
  },
})
