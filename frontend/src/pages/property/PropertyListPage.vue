<script setup>
import {usePropertyStore} from "stores/property/property-store";
import {onMounted, ref} from "vue";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const $router = useRouter();

const propertyStore = usePropertyStore();

const {propertyList} = storeToRefs(propertyStore);

onMounted(() => {
  propertyStore.getPropertyList();
})

const goToDetailPage = (id) => {
  $router.push(`/property/${id}`)
}
</script>

<template>
  <q-page class="q-pa-xl row q-col-gutter-x-xl q-col-gutter-y-xl">
    <div v-for="property in propertyList" class="col-3">
      <q-card class="property-card" @click="goToDetailPage(property.id)">
        <q-card-section>
          <q-img :src="property.image === null ? 'https://flat-bucket.s3.ap-northeast-2.amazonaws.com/e2560779-3efb-4815-91b9-14e4b1e1afd7.png' : property.image" no-native-menu class="q-mb-md">
            <q-badge
              v-if="property.tradeTypeName === '월세'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #14ADEA">
              <div class="q-ml-sm text-weight-bolder">{{property.tradeTypeName}}</div>
            </q-badge>
            <q-badge
              v-if="property.tradeTypeName === '전세'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #4F5569">
              <div class="q-ml-sm text-weight-bolder">{{property.tradeTypeName}}</div>
            </q-badge>
            <q-badge
              v-if="property.tradeTypeName === '매매'"
              class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px; background-color: #444444">
              <div class="q-ml-sm text-weight-bolder">{{property.tradeTypeName}}</div>
            </q-badge>
          </q-img>

          <h6 class="q-ma-none q-mb-xs">{{ property.title === null ? "현대 아파트" : property.title }}</h6>
          <div class="q-mb-sm">{{property.address}}</div>
          <div class="sub-text">{{property.articleFeatureDesc}}</div>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<style scoped lang="scss">
.property-card {
  min-height: 350px;
}

.sub-text {
  color: $text-grey;
}

</style>

