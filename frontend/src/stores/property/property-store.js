import { defineStore } from 'pinia';
import {api} from "boot/api";

export const usePropertyStore = defineStore('propertyStore', {
  state: () => ({
    propertyDetail: undefined,
  }),

  actions: {
    async getPropertyDetail(propertyId) {
      try {
        const response = await api.get(`/property/detail/${propertyId}`);
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
    }
  },
})
