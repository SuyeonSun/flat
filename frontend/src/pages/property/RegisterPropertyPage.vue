<script setup>
import {ref} from "vue";
import {useCommonStore} from "stores/common/common-store";

const commonStore = useCommonStore();

const file = ref(undefined);

const register = async () => {
  const fileUrl = await commonStore.uploadFile(file.value);
  console.log(fileUrl)
}
</script>

<template>
  <q-page class="q-pa-xl">
    <q-file
      v-model="file"
      label="Pick one file"
      filled
      style="max-width: 300px"
      dense
    />
    <q-btn outline dense label="등록" @click="register"/>
  </q-page>
</template>

<style scoped>

</style>
