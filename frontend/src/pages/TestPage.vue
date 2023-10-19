<script setup>
  import {useTestStore} from "stores/test-store";
  import {onMounted} from "vue";

  const testStore = useTestStore();
  onMounted(() => {
    testStore.test();
  })
</script>

<template>
  <div>Test Page</div>
</template>

<style scoped>

</style>
