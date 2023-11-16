import { defineStore } from 'pinia';
import {api} from "boot/api";
import {authApi} from "boot/auth-api";

export const useUserStore = defineStore('userStore', {
  state: () => ({
    user: {},
    friends: [],
    reqDialog: false,
    addFormDialog: false,
    changePwdDialog: false,
    recvRequests: [],
    sendRequests: [],

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

    async getUserProfile(name) {
      try {
        return await api.post('/user/profile', {name: name})
      } catch (e) {
        console.log(e)
      }
    },

    async searchUser(email) {
      try {
        return await api.post('/user', {email: email})
      } catch(e) {
        console.log(e)
      }
    },

    async getFriendState(searchUser) {
      const sendRes = await api.post('/user/friends/send', {email: this.user.email})
      this.sendRequests = sendRes.data;

      for (const s of this.sendRequests) {
        if(this.user.id === s.senderId && searchUser.id === s.receiverId) {
          return 2 // 친구 요청을 보낸 상황
        }
      }

      const recvRes = await api.post('/user/friends/recv', {email: this.user.email})
      this.recvRequests = recvRes.data;

      for (const r of this.recvRequests) {
        if(this.user.id === r.receiverId && searchUser.id === r.senderId) {
          return 2 // 친구 요청을 보낸 상황
        }
      }

      for (const friend of this.friends) {
        if(searchUser.id === friend.id) {
          return 1 // 이미 친구인 상황
        }
      }

      return 0
    },

    async sendRequest(senderId, receiverId) {
      try {
        const response = await api.post('/friend/test', {
          senderId: senderId,
          receiverId: receiverId
        })
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

    viewAddForm() {
      this.addFormDialog = true
    },

    async deleteFriend(userId, friendId) {
      try {
        const response = await api.delete('/friend', {data: {userId: userId, friendId: friendId}})
        await this.findFriends(userId)
      } catch (e) {
        console.log(e)
      }
    },

    async getFriendRequest(email) {
      this.reqDialog = true
      const sendRes = await api.post('/user/friends/send', {email: email})
      this.sendRequests = sendRes.data;
      console.log("@@@@@@@@@@@", this.sendRequests)

      const recvRes = await api.post('/user/friends/recv', {email: email})
      this.recvRequests = recvRes.data;
    },

    async acceptRequest(recvRequest) {
      const response = await api.post('/friend/connect', {
        senderId: recvRequest.senderId,
        receiverId: recvRequest.receiverId
      })

      this.recvRequests = this.recvRequests.filter((t) => t !== recvRequest)
      await this.findFriends(this.user.id)
    },

    async rejectRequest(recvRequest) {
      const response = await api.post('/friend/reject', {
        senderId: recvRequest.senderId,
        receiverId: recvRequest.receiverId
      })

      this.recvRequests = this.recvRequests.filter((t) => t !== recvRequest)

    },

    async removeRequest(sendRequest) {
      const response = await api.post('/friend/remove', {
        senderId: sendRequest.senderId,
        receiverId: sendRequest.receiverId
      })

      this.sendRequests = this.sendRequests.filter((t) => t !== sendRequest)
    },

    clear() {
      this.reqDialog = false
    },

    // 프로필 변경
    async changeProfile(changeProfileRequest) {
      return await api.post('/user/change-profile', changeProfileRequest)
    },

    // 비밀번호 변경
    async changePassword(changePwdRequest) {
      return await api.post('/auth/change-password', changePwdRequest)
    },

    // 주소 변경
    async changeAddress(changeAddressRequest) {
      return await api.post('/user/address', changeAddressRequest)
    },
  },
})
