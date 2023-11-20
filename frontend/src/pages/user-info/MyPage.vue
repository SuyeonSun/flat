<script setup>
import { ref, watch, onMounted } from 'vue';
import {useAuthStore} from "stores/auth/auth-store";
import {useUserStore} from "stores/user/user-store";
import {useChatStore} from "stores/chat/chat-store";
import {usePropertyStore} from "stores/property/property-store";
import friendRequest from "pages/friend/FriendRequest.vue";
import addForm from "pages/friend/FriendAddForm.vue"
import ChatRoomDetail from "pages/chat/ChatRoomDetail.vue";
import userInfo from "pages/user-info/UserInfo.vue";
import {useRouter} from "vue-router";

const $router = useRouter();

const authStore = useAuthStore()
const userStore = useUserStore()
const chatStore = useChatStore()
const propertyStore = usePropertyStore()

const tab = ref('likes')

onMounted(async () => {
  await userStore.getUserInfo(authStore.$state.email)
  await propertyStore.getMyLikeProperty(authStore.$state.email)
})

// 매물
const goToDetailPage = (id) => {
  $router.push(`/property/${id}`)
}

// 내가 찜한 매물
watch(tab, async (n) => {
  if(n === 'likes') {
    await propertyStore.getMyLikeProperty(authStore.$state.email)
  }
})

// 내가 등록한 매물
watch(tab, async (n) => {
  if(n === 'mines') {
    await propertyStore.getMyProperty(authStore.$state.email)
  }
})

// 친구
watch(tab, async (n) => {
  if(n === 'friends') {
    await userStore.findFriends(userStore.user.id)
  }
})

watch(tab, async (n) => {
  if(n === 'chats') {
    await chatStore.findRooms(authStore.$state.name)
  }
})

const removeFriend = (friendId) => {
  userStore.deleteFriend(userStore.user.id, friendId)
}

const enterRoom = (room) => {
  const sender = authStore.$state.name
  const receiver = authStore.$state.name == room.sender ? room.receiver : room.sender
  const roomId = room.roomId
  chatStore.enterRoom(sender, receiver, roomId)
}

</script>

<template>
  <div class="row justify-center">
    <div style="width: 63%; padding-top: 50px; padding-bottom: 50px ">
      <main>
        <user-info/>
      </main>
    </div>
  </div>

  <div class="row justify-center">
    <div style="width: 63%">
      <div class="row justify-center">
        <q-card style="min-width: 100%">
          <q-tabs
            v-model="tab"
            dense
            class="text-grey"
            active-color="primary"
            indicator-color="primary"
            align="justify"
            narrow-indicator
          >
            <q-tab name="likes" label="관심 매물" />
            <q-tab name="mines" label="등록 매물" />
            <q-tab name="friends" label="친구" />
            <q-tab name="chats" label="내 문의" />
          </q-tabs>

          <q-separator />

          <q-tab-panels v-model="tab" animated>
            <q-tab-panel name="likes">
              <div class="row justify-center">
                <div v-for="property in propertyStore.$state.propertyList"
                     style="padding-left: 20px; padding-right: 20px">
                  <q-card class="property-card" @click="goToDetailPage(property.id)"
                    style="width: 320px; height: 320px">
                    <q-img
                      :src=property.image
                      class="q-mb-md"
                      style="width: 320px; height: 180px">
                      <q-badge
                        v-if="property.tradeTypeName === '월세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #14ADEA">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '전세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #4F5569">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '매매'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #444444">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                    </q-img>
                    <q-card-section>
                      <h6 class="q-ma-none q-mb-xs">{{ property.title === null ? "-" : property.title }}</h6>
                      <div class="q-mb-sm">{{ property.address }}</div>
                      <div>
                        <q-btn dense disable label="매물 특징">
                          <q-tooltip>
                            {{ property.articleFeatureDesc }}
                          </q-tooltip>
                        </q-btn>
                      </div>
                    </q-card-section>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>

            <q-tab-panel name="mines">
              <div class="row justify-center">
                <div v-for="property in propertyStore.$state.propertyList"
                  style="padding-left: 20px; padding-right: 20px">
                  <q-card class="property-card" @click="goToDetailPage(property.id)"
                    style="width: 320px; height: 320px">
                    <q-img
                      :src="property.image"
                      no-native-menu class="q-mb-md"
                      style="width: 320px; height: 180px">
                      <q-badge
                        v-if="property.tradeTypeName === '월세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #14ADEA">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '전세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #4F5569">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '매매'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #444444">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                    </q-img>
                    <q-card-section>
                      <h6 class="q-ma-none q-mb-xs">{{ property.title === null ? "-" : property.title }}</h6>
                      <div class="q-mb-sm">{{ property.address }}</div>
                      <div>
                        <q-btn dense disable label="매물 특징">
                          <q-tooltip>
                            {{ property.articleFeatureDesc }}
                          </q-tooltip>
                        </q-btn>
                      </div>
                    </q-card-section>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>

            <q-tab-panel name="friends">
              <div class="row" style="justify-content: space-between; margin-bottom: 20px">
                <div style="color: blue; font-weight: bold">
                  친구 수 : {{userStore.$state.friends.length}}
                </div>
                <div>
                  <q-btn style="margin-right: 10px" @click="userStore.viewAddForm">친구 추가</q-btn>
                  <q-btn @click="userStore.getFriendRequest(authStore.email)">요청 확인</q-btn>
                </div>
              </div>

              <div class="row justify-center">
                <div v-for="friend in userStore.$state.friends"
                     style="padding-left: 20px; padding-right: 20px">
                  <q-card class="my-card"
                    style="width: 320px; height: 320px">
                    <q-img
                      :src="friend.profile"
                      no-native-menu class="q-mb-md"
                      style="width: 320px; height: 180px"/>
                    <q-card-section>
                      <div class="text-subtitle2" >
                        <span style="font-weight: bold">
                          이름
                        </span>
                        <span>
                          : {{friend.name}}
                        </span>
                      </div>
                      <div class="text-subtitle2">
                        <span style="font-weight: bold">
                          이메일
                        </span>
                        <span>
                          : {{friend.email}}
                        </span>
                      </div>
                      <div class="text-subtitle2">
                        <span style="padding-right: 5px">
                          <q-btn disable label="주소" style="margin-top: 5px">
                          <q-tooltip>
                            {{friend.address === null ? 'null' : friend.address}}
                          </q-tooltip>
                        </q-btn>
                        </span>
                        <span>
                          <q-btn @click="removeFriend(friend.id)" style="margin-top: 5px">친구 삭제</q-btn>
                        </span>
<!--                        <span style="font-weight: bold">-->
<!--                          주소-->
<!--                        </span>-->
<!--                        <span>-->
<!--                          : {{friend.address === null ? 'null' : friend.address}}-->
<!--                        </span>-->
                      </div>
                    </q-card-section>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>

            <q-tab-panel name="chats">
              <div class="row justify-center">
                <div v-for="room in chatStore.$state.myRooms"
                     style="padding-left: 20px; padding-right: 20px">
                  <q-card class="my-card"
                    style="width: 320px; height: 320px">
                    <q-img
                      :src="room.image"
                      no-native-menu class="q-mb-md"
                      style="width: 320px; height: 180px"/>
                    <q-card-section>
                      <div class="text-subtitle2">매물 : {{room.title}}</div>
                      <div class="text-subtitle2">상대방 : {{authStore.$state.name == room.sender ? room.receiver : room.sender}}</div>
                      <q-btn @click="enterRoom(room)">채팅방 입장</q-btn>
                    </q-card-section>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>
          </q-tab-panels>
        </q-card>
      </div>
    </div>
  </div>

  <template v-if="userStore.$state.addFormDialog">
    <add-form/>
  </template>
  <template v-if="userStore.$state.reqDialog">
    <friend-request/>
  </template>
  <template v-if="chatStore.$state.isDialog">
    <chat-room-detail/>
  </template>
</template>

<style scoped>
.property-card {
  cursor: pointer;
}
</style>
