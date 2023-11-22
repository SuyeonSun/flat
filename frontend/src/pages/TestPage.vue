<script setup>
  import {useTestStore} from "stores/test/test-store";
  import {onMounted, ref} from "vue";
  import {storeToRefs} from "pinia";
  import {useS3Store} from "stores/common/s3-store";

  const testStore = useTestStore();
  const s3Store = useS3Store();

  const {testResponse} = storeToRefs(testStore);

  const file = ref(undefined);
  const onsubmit = async () => {
    const url = await s3Store.uploadFile(file.value)
    console.log("@@@", url)
  }
</script>

<template>
  <div>{{testResponse}}</div>

  <q-btn @click="testStore.insertData()" label="테스트 데이터 요청"/>

  <div class="q-mb-xl"></div>

  <q-file
    v-model="file"
    outlined
    dense
    label="이미지 등록"
  />
  <q-btn @click="onsubmit" label="이미지 업로드"/>
</template>

<style scoped>

</style>
