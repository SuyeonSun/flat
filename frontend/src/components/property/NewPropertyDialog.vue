<script setup>

import {usePropertyStore} from "stores/property/property-store";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const $router = useRouter();

const propertyStore = usePropertyStore()
const props = defineProps(['isOpenNewPropertyDialog']);
const emit = defineEmits(['handleNewPropertyDialog'])

const {newProperty} = storeToRefs(propertyStore)

const closeDialog = () => {
  emit('handleNewPropertyDialog')

}

const goToDetailPage = (id) => {
  $router.push(`/property/${id}`)
}

const checkNewProperty = () => {
  let properties = []
  for (const p of newProperty.value) {
    properties.push(p.id)
  }
  propertyStore.removeInterestAreaPropertyList(properties)
}
</script>

<template>
  <q-dialog v-model="props.isOpenNewPropertyDialog" persistent>
    <q-layout view="Lhh lpR fff" container class="bg-white text-dark">

      <q-header style="background-color: black">
        <q-toolbar style="height: 50px">
          <q-toolbar-title class="text-center" style="font-size: 16px;">알림</q-toolbar-title>
          <q-btn flat v-close-popup round dense icon="close" @click="closeDialog"/>
        </q-toolbar>
      </q-header>

      <q-footer class="bg-white text-dark" bordered>
        <q-toolbar>
          <q-btn rount dense flat @click="checkNewProperty">읽음 처리</q-btn>
        </q-toolbar>
      </q-footer>

      <q-scroll-area style="height: 915px;">
        <q-page-container>
          <q-page padding>
            <div class="row justify-center">
              <div style="width: 100%; max-width: 400px">
                <div v-for="property in newProperty" :key="property">
                  <div @click="goToDetailPage(property.id)" class="property-card">
                    <q-img :src="property.image === null ? 'https://flat-bucket.s3.ap-northeast-2.amazonaws.com/6b1b1562-2cb2-4469-940d-112b3837300b.PNG' : property.image" style="max-width: 300px; max-height: 190px;">
                      <q-badge
                        v-if="property.tradeTypeName === '월세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '전세'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                      <q-badge
                        v-if="property.tradeTypeName === '매매'"
                        class="absolute" style="top: 8px; left: 8px; padding: 11px; width: 60px;">
                        <div class="q-ml-sm text-weight-bolder">{{ property.tradeTypeName }}</div>
                      </q-badge>
                    </q-img>

                    <q-card-section class="q-px-none">
                      <h6 class="q-ma-none q-mb-xs text-weight-bold">{{ property.rentPrc }}</h6>
                      <div class="q-mb-sm sub-text">{{ property.address }}</div>
                      <div class="sub-text">{{property.direction}}, {{ property.floorInfo }}층, 관리비 {{property.averageCommonPrice}} 만원</div>
                    </q-card-section>
                  </div>
                </div>
              </div>
            </div>
          </q-page>
        </q-page-container>
      </q-scroll-area>
    </q-layout>
  </q-dialog>
</template>

<style scoped>
.property-card {
  cursor: pointer;
}
</style>
