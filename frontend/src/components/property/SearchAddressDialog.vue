<script setup>
const props = defineProps(['isOpenSearchAddressDialog']);
const emit = defineEmits(['handleSearchAddressDialog'])

const closeDialog = () => {
  emit('handleSearchAddressDialog');
}
</script>

<template>
  <q-dialog v-model="props.isOpenSearchAddressDialog" persistent>
    <q-card class="q-dialog-plugin">

      search address dialog

      <q-card-actions align="right">
        <q-btn color="primary" label="OK" @click="closeDialog"/>
        <q-btn color="primary" label="Cancel"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<style scoped>

</style>
