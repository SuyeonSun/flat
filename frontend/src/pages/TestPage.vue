<script setup>
  import {useTestStore} from "stores/test-store";
  import {onMounted} from "vue";

  const testStore = useTestStore();
</script>

<template>
  <div>Test {{ testStore.count }}</div>
  <button @click="testStore.test()">증가</button>
</template>

<style scoped>

</style>
