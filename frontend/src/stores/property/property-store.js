import { defineStore } from 'pinia';
import {api} from "boot/api";
import {useAuthStore} from "stores/auth/auth-store";

export const usePropertyStore = defineStore('propertyStore', {
  state: () => ({
    propertyDetail: undefined,
    propertyList: []
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

    async getPropertyList() {
      try {
        // http://localhost:8000/property/list?page=0&size=10
        const response = await api.get("/property/list?page=0&size=10");
        this.propertyList = response.data.data;
      } catch (error) {
        console.log("error")
      }
    },

    async like(likePayload) {
      try {
        const response = await api.post("/like", likePayload);
        return response.data.data;
      } catch (error) {
        console.log("error");
      }
    }
  },
})
