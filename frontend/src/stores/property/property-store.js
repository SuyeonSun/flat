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


  },
})
