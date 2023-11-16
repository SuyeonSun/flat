<script setup>
import {ref, watch} from "vue";
import {useGeocodeStore} from "stores/common/geocode-store";
import {storeToRefs} from "pinia";

const props = defineProps(['isOpenSearchAddressDialog']);
const emit = defineEmits(['handleSearchAddressDialog', 'updateSelectedAddress'])

const closeDialog = () => {
  emit('handleSearchAddressDialog');
  addresses.value = [];
  search.value = "";
}

const geocodeStore = useGeocodeStore();

const {addresses} = storeToRefs(geocodeStore);

const search = ref("");

watch(() => search.value,  (newVal, oldVal) => {
  if (newVal === "") {
    geocodeStore.initAddresses();
  }

  if (newVal.length >= 2) {
     geocodeStore.searchAddress(newVal);
  }
})

const selectedAddress = ref({
  lat: undefined,
  lng: undefined,
  address: undefined
});

const clickAddress = (address) => {
  // 위도 lat address.y
  // 경도 lng address.x
  // address.roadAddress
  selectedAddress.value.lat = address.y;
  selectedAddress.value.lng = address.x;
  selectedAddress.value.address = address.roadAddress;
  search.value = address.roadAddress;
}

const clickOkBtn = () => {
  emit('updateSelectedAddress', selectedAddress.value);
  closeDialog();
}
</script>

<template>
  <q-dialog v-model="props.isOpenSearchAddressDialog" persistent>
    <q-card class="q-pa-lg dialog-container">
      <div class="row justify-between items-center">
        <h6 class="text-bold q-ma-none">주소를 검색해주세요.</h6>
        <q-icon name="close" size="sm" @click="closeDialog"/>
      </div>
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

        <q-list bordered seperator v-if="addresses?.length > 0">
          <div class="q-ml-md q-mt-md" style="text-decoration: none; color: #14ADEA; font-size: 13px; font-weight: bold"> 자동완성 검색어 </div>
          <q-item v-for="(address) in addresses" clickable v-ripple @click="clickAddress(address)">
            <q-item-section>
              <q-item-label>{{address.roadAddress}}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
        <div v-else class="q-mt-md q-ml-xs no-search-result-text"> 검색 결과가 없습니다. </div>

      </q-card-section>
      <q-card-actions align="right">
        <q-btn label="확인" @click="clickOkBtn" class="confirm-btn"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<style scoped lang="scss">
.dialog-container {
  min-width: 500px;
}

.no-search-result-text {
  color: $text-grey;
}

.confirm-btn {
  background-color: $dark-blue;
  color: white;
}

.cancel-btn {
  background-color: $dark-grey;
  color: white;
}
</style>
