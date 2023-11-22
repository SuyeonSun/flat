<script setup>
import {ref, watch, onMounted} from 'vue';
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
  if (n === 'likes') {
    await propertyStore.getMyLikeProperty(authStore.$state.email)
  }
})

// 내가 등록한 매물
watch(tab, async (n) => {
  if (n === 'mines') {
    await propertyStore.getMyProperty(authStore.$state.email)
  }
})

// 친구
watch(tab, async (n) => {
  if (n === 'friends') {
    await userStore.findFriends(userStore.user.id)
  }
})

watch(tab, async (n) => {
  if (n === 'chats') {
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
            <q-tab name="likes" label="관심 매물"/>
            <q-tab name="mines" label="등록 매물"/>
            <q-tab name="friends" label="친구"/>
            <q-tab name="chats" label="내 문의"/>
          </q-tabs>

          <q-separator/>

          <q-tab-panels v-model="tab" animated style="min-height: 390px; max-height: 390px">
            <q-tab-panel name="likes">
              <div class="row justify-center">
                <q-table
                  style="max-height: 375px; width: 100%"
                  flat bordered
                  :rows="propertyStore.$state.propertyList"
                  :columns="[
                    {
                      name: 'title',
                      required: true,
                      label: '제목',
                      align: 'left',
                      field: row => row.title,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'address',
                      required: true,
                      label: '매물 주소',
                      align: 'left',
                      field: row => row.address,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'tradeTypeName',
                      required: true,
                      label: '거래 유형',
                      align: 'left',
                      field: row => row.tradeTypeName,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'rentPrc',
                      required: true,
                      label: '매매가',
                      align: 'left',
                      field: row => row.rentPrc,
                      format: val => `${val}`,
                      sortable: true
                    },
                  ]"
                  virtual-scroll
                >
                  <template v-slot:header="props">
                    <q-tr :props="props">
                      <q-th
                        v-for="col in props.cols"
                        :key="col.name"
                        :props="props"
                      >
                        {{ col.label }}
                      </q-th>
                      <q-th auto-width />
                    </q-tr>
                  </template>
                  <template v-slot:body="props">
                    <q-tr :props="props">
                      <q-td
                        v-for="col in props.cols"
                        :key="col.name"
                        :props="props"
                      >
                        {{ col.value }}
                      </q-td>
                      <q-td auto-width>
                        <q-btn @click="goToDetailPage(props.row.id)">상세</q-btn>
                      </q-td>
                    </q-tr>
                  </template>
                </q-table>
              </div>
            </q-tab-panel>

            <q-tab-panel name="mines">
              <div class="row justify-center">
                <q-table
                  style="max-height: 375px; width: 100%"
                  flat bordered
                  :rows="propertyStore.$state.propertyList"
                  :columns="[
                    {
                      name: 'title',
                      required: true,
                      label: '제목',
                      align: 'left',
                      field: row => row.title,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'address',
                      required: true,
                      label: '매물 주소',
                      align: 'left',
                      field: row => row.address,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'tradeTypeName',
                      required: true,
                      label: '거래 유형',
                      align: 'left',
                      field: row => row.tradeTypeName,
                      format: val => `${val}`,
                      sortable: true
                    },
                    {
                      name: 'rentPrc',
                      required: true,
                      label: '매매가',
                      align: 'left',
                      field: row => row.rentPrc,
                      format: val => `${val}`,
                      sortable: true
                    },
                  ]"
                  virtual-scroll
                >
                  <template v-slot:header="props">
                    <q-tr :props="props">
                      <q-th
                        v-for="col in props.cols"
                        :key="col.name"
                        :props="props"
                      >
                        {{ col.label }}
                      </q-th>
                      <q-th auto-width />
                    </q-tr>
                  </template>
                  <template v-slot:body="props">
                    <q-tr :props="props">
                      <q-td
                        v-for="col in props.cols"
                        :key="col.name"
                        :props="props"
                      >
                        {{ col.value }}
                      </q-td>
                      <q-td auto-width>
                        <q-btn @click="goToDetailPage(props.row.id)">상세</q-btn>
                      </q-td>
                    </q-tr>
                  </template>
                </q-table>
              </div>
            </q-tab-panel>

            <q-tab-panel name="friends">
              <div class="row" style="justify-content: space-between; margin-bottom: 20px">
                <div style="color: #117CE9; font-weight: bold">
                  총 {{ userStore.$state.friends.length }}명의 친구
                </div>
                <div>
                  <q-btn style="margin-right: 10px" @click="userStore.viewAddForm">친구 추가</q-btn>
                  <q-btn @click="userStore.getFriendRequest(authStore.email)">요청 확인</q-btn>
                </div>
              </div>

              <div class="">
                <div v-for="friend in userStore.$state.friends"
                     style="padding-left: 20px; padding-right: 20px">
                  <q-card flat bordered class="row justify-between items-center q-mb-sm">
                    <q-item clickable v-ripple>
                      <q-item-section side>
                        <q-avatar rounded size="48px">
                          <img :src="friend.profile" alt="친구 프로필"/>
                        </q-avatar>
                      </q-item-section>
                      <q-item-section>
                        <q-item-label>{{ friend.email }}</q-item-label>
                        <q-item-label caption>{{ friend.name }}</q-item-label>
                        <q-item-label caption>{{ friend.address === null ? 'null' : friend.address }}</q-item-label>
                      </q-item-section>
                    </q-item>
                    <div class="row justify-end">
                      <div>
                        <q-btn @click="removeFriend(friend.id)" class="col-2 q-mr-md"
                               style="background-color: lightgray">친구
                          삭제
                        </q-btn>
                      </div>
                    </div>
                  </q-card>
                </div>
              </div>
            </q-tab-panel>

            <q-tab-panel name="chats">
              <div v-for="room in chatStore.$state.myRooms"
                   style="padding-left: 20px; padding-right: 20px">
                <q-card flat bordered class="row justify-between items-center q-mb-sm">
                  <q-item clickable v-ripple>
                    <q-item-section side>
                      <q-avatar rounded size="48px">
                        <img :src="room.image" alt="채팅 이미지"/>
                      </q-avatar>
                    </q-item-section>
                    <q-item-section>
                      <q-item-label>{{ room.title }}</q-item-label>
                      <q-item-label caption>{{ authStore.$state.name == room.sender ? room.receiver : room.sender }}(와)과의 대화</q-item-label>
                    </q-item-section>
                  </q-item>
                  <div class="row justify-end">
                    <div>
                      <q-btn @click="enterRoom(room)" class="col-2 q-mr-md"
                             style="background-color: lightgray">채팅방 입장
                      </q-btn>
                    </div>
                  </div>
                </q-card>
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
