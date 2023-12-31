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
    mapList: [],
    newProperty: []
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

    async getMyProperty(email) {
      try {
        const response = await api.get(`/property?email=${email}`)
        this.propertyList = response.data.data
      } catch (error) {
        console.log(error)
      }
    },

    async getMyLikeProperty(email) {
      try {
        const response = await api.get(`/property/like?email=${email}`)
        this.propertyList = response.data.data
      } catch (error) {
        console.log(error)
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
    },

    async getInterestAreaMapList(lat, lng, radius) {
      try {
        const response = await api.post('/property/map/interestArea', {
          lat: lat,
          lng: lng,
          radius: radius
        })
        this.mapList = response.data.data
      } catch (error) {
        console.log(error)
      }
    },

    async getInterestAreaPropertyList(email) {
      try {
        return await api.get(`/property/list/interestArea?email=${email}`)
      } catch (error) {
        console.log(error)
      }
    },

    async removeInterestAreaAllProperty(email) {
      try {
        const response = await api.get(`/property/remove/interestArea?email=${email}`)
        this.newProperty = []
      } catch (error) {
        console.log(error)
      }
    },

    async removeInterestAreaPropertyList(properties) {
      try {
        console.log("!@#!@#@!#", properties)
        const response =  await api.post('/property/remove/interestArea', {
          properties
        })
        this.newProperty = []
      } catch (error) {
        console.log(error)
      }
    },
  },
})
