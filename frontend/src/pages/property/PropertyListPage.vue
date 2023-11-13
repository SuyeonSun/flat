<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted} from "vue";
import {storeToRefs} from "pinia";

const propertyStore = usePropertyStore();

const {propertyList} = storeToRefs(propertyStore);

onMounted(() => {
  propertyStore.getPropertyList();
})
</script>

<template>
  <q-page class="q-pa-xl">
    {{propertyList}}
  </q-page>
</template>

<style scoped>

</style>
