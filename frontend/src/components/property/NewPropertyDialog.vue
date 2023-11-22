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
    <q-layout view="Lhh lpR fff" container class="bg-white text-dark" style="max-height: 650px; max-width: 450px">
      <q-header style="background-color: white">
        <q-toolbar>
          <q-toolbar-title>
          </q-toolbar-title>
          <div class="row justify-between items-center" style="width: 100%">
            <q-btn rount dense flat @click="checkNewProperty" style="color: black">
              읽음 처리
            </q-btn>
            <q-btn flat v-close-popup round dense icon="close" @click="closeDialog" style="color: black"/>
          </div>
        </q-toolbar>
      </q-header>

      <q-scroll-area style="height: 650px;">
        <q-page-container>
          <q-page padding>
            <div class="q-mb-sm row items-center">
              <q-btn dense round flat icon="email">
                <q-badge color="red" floating transparent v-if="newProperty.length > 0">
                  {{newProperty.length}}
                </q-badge>
              </q-btn>
              <div v-if="newProperty.length > 0" class="q-ml-xs" style="color: #14ADEA; font-weight: bold">새로운 매물이 등록 되었습니다.</div>
              <div v-else class="q-ml-xs" style="color: lightgray; font-weight: bold">새로 등록된 매물이 없습니다.</div>
            </div>
            <div v-for="property in newProperty" :key="property" style="border: 1px solid lightgray">
              <q-item clickable v-ripple @click="goToDetailPage(property.id)">
                <q-item-section side>
                  <q-avatar rounded size="48px">
                    <img :src="property.image === null ? 'https://flat-bucket.s3.ap-northeast-2.amazonaws.com/6b1b1562-2cb2-4469-940d-112b3837300b.PNG' : property.image"/>
                  </q-avatar>
                </q-item-section>
                <q-item-section>
                  <q-item-label>[ {{ property.tradeTypeName }} ] {{ property.rentPrc }}</q-item-label>
                  <q-item-label caption>{{ property.address }}</q-item-label>
                </q-item-section>
              </q-item>
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
