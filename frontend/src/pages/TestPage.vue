<script setup>
  import {useTestStore} from "stores/test/test-store";
  import {onMounted} from "vue";
  import {storeToRefs} from "pinia";

  const testStore = useTestStore();

  const {testResponse} = storeToRefs(testStore);

  onMounted( async () => {
    console.log("on mount")
    await testStore.test();
  })
</script>

<template>
  <div>{{testResponse}}</div>

</template>

<style scoped>

</style>
