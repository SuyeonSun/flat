<script setup>
import {useUserStore} from "stores/user/user-store";
import {useAuthStore} from "stores/auth/auth-store";
import {useS3Store} from "stores/common/s3-store";
import {Notify} from "quasar";
import {onMounted, ref, watch} from "vue"
import SearchAddressDialog from "components/property/SearchAddressDialog";
import changePassword from "pages/user-info/ChangePassword.vue"
import UploadFileDialog from "components/user/UploadFileDialog.vue";

const authStore = useAuthStore()
const userStore = useUserStore()
const s3Store = useS3Store()

const profile = ref('')
const newProfile = ref(undefined)

onMounted(async () => {
  const response = await userStore.getUserProfile(authStore.$state.name)
  profile.value = response.data
})

// 프로필 변경

const isOpenUploadFileDialog = ref(false)

const handleUploadFileDialog = () => {
  isOpenUploadFileDialog.value = !isOpenUploadFileDialog.value
}


watch(() => newProfile.value, async(newVal, oldVal) => {
  const fileUrl = await s3Store.uploadFile(newProfile.value)
  const payLoad = {
    email: authStore.$state.email,
    profile: fileUrl
  }
  const response = await userStore.changeProfile(payLoad)
  userStore.$state.user.profile = fileUrl

  if(response.status === 200) {
    Notify.create({
      message: '프로필 변경이 완료되었습니다.',
      color: "green"
    })
  }
})

const file = ref(null)

const triggerFileInput = () => {
  const el = file.value.getNativeElement();
  el.click();
}


// 비밀번호 변경
const onChangePwd = () => {
  userStore.$state.changePwdDialog = true
}

// 주소 변경

const updateSelectedAddress = async (value) => {

  const payLoad = {
    email: authStore.$state.email,
    address: value.address,
    lat: value.lat,
    lng: value.lng
  }

  await userStore.changeAddress(payLoad)

  userStore.$state.user.address = value.address;
  userStore.$state.user.lat = value.lat;
  userStore.$state.user.lng = value.lng;


}

const isOpenSearchAddressDialog = ref(false);
const handleSearchAddressDialog = () => {
  isOpenSearchAddressDialog.value = !isOpenSearchAddressDialog.value;
}

</script>

<template>
  <div style="margin-top: 20px; margin-bottom: 20px" class="row">
    <div style="margin-right: 30px">
      <img :src="userStore.$state.user.profile" style="border-radius: 70%; width: 200px; height: 200px"/>
    </div>
    <div style="width: 500px">
      <div>
        <div>
          <q-input
            v-model="userStore.$state.user.name"
            dense
            outlined
            readonly
            class="q-mb-md"
            label="이름"
          >
          </q-input>

          <q-input
            v-model="userStore.$state.user.email"
            dense
            outlined
            readonly
            class="q-mb-md"
            label="이메일"
          >
          </q-input>

          <q-input
            v-model="userStore.$state.user.address"
            dense
            outlined
            readonly
            class="q-mb-md"
            label="주소"
          >
          </q-input>
        </div>
      </div>

      <div class="row justify-start">
        <div style="width: 400px">
          <div class="row">
            <q-btn
              @click="triggerFileInput"
              class="q-mr-sm"
            >프로필 변경</q-btn>
            <q-file v-show="false"
                    ref="file"
                    v-model="newProfile"
                    label="프로필 변경"
            >
            </q-file>
            <q-btn @click="onChangePwd" class="q-mr-sm"> 비밀번호 변경</q-btn>
            <q-btn @click="handleSearchAddressDialog">주소 수정</q-btn>
          </div>
        </div>
      </div>

    </div>
  </div>

  <template v-if="userStore.$state.changePwdDialog">
    <change-password/>
  </template>

  <search-address-dialog
    :isOpenSearchAddressDialog="isOpenSearchAddressDialog"
    @handleSearchAddressDialog="handleSearchAddressDialog"
    @updateSelectedAddress="updateSelectedAddress"
  ></search-address-dialog>

  <upload-file-dialog
    :isOpenUploadFileDialog="isOpenUploadFileDialog"
    @handleUploadFileDialog="handleUploadFileDialog"
  ></upload-file-dialog>
</template>

<style scoped>
.custom-file-input {
  opacity: 0;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.custom-file-btn {
  cursor: pointer;
}
</style>
