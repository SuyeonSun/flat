import { defineStore } from 'pinia';
import {api} from "boot/api";
import {useAuthStore} from "stores/auth/auth-store";

export const usePropertyStore = defineStore('propertyStore', {
  state: () => ({
    propertyDetail: undefined,
    propertyList: [],
    pageable: {
      totalPages: 0,
    },
    mapList: []
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

    async getPropertyList(pageablePayload, searchPayload) {
      try {
        // ?page=0&size=20&searchKeyword=&tradeTypeName=&direction=남향
        const response = await api.get(`/property/list?page=${pageablePayload.page}&size=${pageablePayload.size}&searchKeyword=${searchPayload.searchKeyword}&tradeTypeName=${searchPayload.tradeTypeName}&direction=${searchPayload.direction}`);
        this.propertyList = response.data.data.content;
        this.pageable.totalPages = response.data.data.totalPages;
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
    },

    async getMapList(searchPayload) {
      try {
        const response = await api.get(`/property/map?address=${searchPayload.address}&tradeTypeName=${searchPayload.tradeTypeName}`);
        this.mapList = response.data.data;

      } catch (error) {
        console.log("error");
      }
    }
  },
})
