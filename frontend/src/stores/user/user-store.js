import { defineStore } from 'pinia';
import {api} from "boot/api";

export const useUserStore = defineStore('userStore', {
  state: () => ({
    user: {},
    friends: [],

  }),

  actions: {

    async getUserInfo(email) {
      try {
        const response = await api.post(`/user`, {email: email})
        this.user = response.data
      } catch (e) {
        console.log(e)
      }
    },

    async findFriends(userId) {
      try {
        const response =  await api.post('/friend/list', {userId: userId})
        this.friends = response.data
      } catch (e) {
        console.log(e)
      }
    },

    async deleteFriend(userId, friendId) {
      try {
        const response = await api.delete('/friend', {data: {userId: userId, friendId: friendId}})
        await this.findFriends(userId)
      } catch (e) {
        console.log(e)
      }
    },
  },
})
