<script setup>
  import {useTestStore} from "stores/test-store";
  import {onMounted} from "vue";

  const testStore = useTestStore();
  onMounted(async () => {
    await testStore.test();
  })
</script>

<template>
  <div>Test Pagejhgjhgjh</div>
</template>

<style scoped>

</style>
