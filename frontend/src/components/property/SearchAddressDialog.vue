<script setup>
import {ref, watch} from "vue";
import {useGeocodeStore} from "stores/common/geocode-store";
import {storeToRefs} from "pinia";

const props = defineProps(['isOpenSearchAddressDialog']);
const emit = defineEmits(['handleSearchAddressDialog'])

const closeDialog = () => {
  emit('handleSearchAddressDialog');
}

const geocodeStore = useGeocodeStore();

const {addresses} = storeToRefs(geocodeStore);

const search = ref(undefined);

watch(() => search.value,  (newVal, oldVal) => {
  if (newVal.length >= 2) {
     geocodeStore.searchAddress(newVal);
  }
})

// addresses.length > 0
// addresses[0].roadAddress, addresses[0].x, addresses[0].y
</script>

<template>
  <q-dialog v-model="props.isOpenSearchAddressDialog" persistent>
    <q-card class="q-pa-lg dialog-container">
      <h5 class="text-bold q-ma-none">주소를 검색해주세요.</h5>
      <q-card-section class="q-px-none">
        <q-input
          v-model="search"
          dense
          outlined
          placeholder="예) 서울특별시 강남구 논현동 195-1"
        >
          <template v-slot:append>
            <q-icon name="search"/>
          </template>
        </q-input>

        {{addresses}}

      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" label="OK" @click="closeDialog"/>
        <q-btn color="primary" label="Cancel"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<style scoped>
.dialog-container {
  min-width: 500px;
}
</style>
