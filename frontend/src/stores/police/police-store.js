import { defineStore } from 'pinia'
import axios from "axios";

export const usePoliceStore = defineStore('policeStore', {
  state: () => ({
    policeStationList : []
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
  },

  persist: {
    enabled: true,
    strategies: [{storage: localStorage}],
  }
})
