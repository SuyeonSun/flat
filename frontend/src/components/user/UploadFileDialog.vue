<script setup>

const props = defineProps(['isOpenUploadFileDialog']);

</script>

<template>
  <q-dialog v-model="props.isOpenUploadFileDialog" persistent>

  </q-dialog>
</template>

<style scoped>

</style>
