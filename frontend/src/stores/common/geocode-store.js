import {defineStore} from 'pinia';
import {api} from "boot/api";

export const useGeocodeStore = defineStore('geocodeStore', {
  state: () => ({
    addresses: undefined
  }),

  actions: {
    async searchAddress(query) {
      try {
        // TODO: 추후 주석 해제
        // query = "논현동"
        // const response = await api.get(`/geocode/${query}`);
        // this.addresses = JSON.parse(response.data.data.body).addresses;

        this.addresses = [{
          "roadAddress": "서울특별시 강남구 논현동",
          "jibunAddress": "서울특별시 강남구 논현동",
          "englishAddress": "Nonhyeon-dong, Gangnam-gu, Seoul, Republic of Korea",
          "addressElements": [{
            "types": ["SIDO"],
            "longName": "서울특별시",
            "shortName": "서울특별시",
            "code": ""
          }, {"types": ["SIGUGUN"], "longName": "강남구", "shortName": "강남구", "code": ""}, {
            "types": ["DONGMYUN"],
            "longName": "논현동",
            "shortName": "논현동",
            "code": ""
          }, {"types": ["RI"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["ROAD_NAME"],
            "longName": "",
            "shortName": "",
            "code": ""
          }, {"types": ["BUILDING_NUMBER"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["BUILDING_NAME"],
            "longName": "",
            "shortName": "",
            "code": ""
          }, {"types": ["LAND_NUMBER"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["POSTAL_CODE"],
            "longName": "",
            "shortName": "",
            "code": ""
          }],
          "x": "127.028461",
          "y": "37.5115706",
          "distance": 0
        }, {
          "roadAddress": "인천광역시 남동구 논현동",
          "jibunAddress": "인천광역시 남동구 논현동",
          "englishAddress": "Nonhyeon-dong, Namdong-gu, Incheon, Republic of Korea",
          "addressElements": [{
            "types": ["SIDO"],
            "longName": "인천광역시",
            "shortName": "인천광역시",
            "code": ""
          }, {"types": ["SIGUGUN"], "longName": "남동구", "shortName": "남동구", "code": ""}, {
            "types": ["DONGMYUN"],
            "longName": "논현동",
            "shortName": "논현동",
            "code": ""
          }, {"types": ["RI"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["ROAD_NAME"],
            "longName": "",
            "shortName": "",
            "code": ""
          }, {"types": ["BUILDING_NUMBER"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["BUILDING_NAME"],
            "longName": "",
            "shortName": "",
            "code": ""
          }, {"types": ["LAND_NUMBER"], "longName": "", "shortName": "", "code": ""}, {
            "types": ["POSTAL_CODE"],
            "longName": "",
            "shortName": "",
            "code": ""
          }],
          "x": "126.7293076",
          "y": "37.4057553",
          "distance": 0
        }]
      } catch (e) {
        console.log("error", e);
      }
    }
  }
})
