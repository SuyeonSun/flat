import { defineStore } from 'pinia'
import axios from "axios";
import {authApi} from "boot/auth-api";

export const usePoliceStore = defineStore('policeStore', {
  state: () => ({
    policeStationList : [],
    policeStationLocationList: []
  }),
  actions: {
    async getPoliceStation() {
      try {
        const response = await axios.get("https://api.odcloud.kr/api/15054711/v1/uddi:9097ad1f-3471-42c6-a390-d85b5121816a?page=1&perPage=2051&serviceKey=SQScPIP3DijSsG4vDLDGDp6gF0i1zI6QHOGGpsZ5E1BOOYSAf4BN3TkKANTA4GhL0qPRopjEGeat278%2FVrKKRw%3D%3D");
        this.policeStationList = response.data.data;
      } catch (error) {
        console.log("error")
      }
    },

    async getPoliceStationLocation(seoulPoliceStationList) {
      // 구분, 주소, 경도, 위도
      try {
        for (const station of seoulPoliceStationList) {
          const query = station["주소"];
          const response = await authApi.get(`/geocode/${query}`);
          if (JSON.parse(response.data.data.body).addresses.length !== 0) {
            const locationInfo = {
              address: query,
              x: JSON.parse(response.data.data.body).addresses[0].x,
              y: JSON.parse(response.data.data.body).addresses[0].y,
            }
            this.policeStationLocationList.push(locationInfo);
          }
        }
      } catch (error) {
        console.log("error", error)
      }
    }
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
})
