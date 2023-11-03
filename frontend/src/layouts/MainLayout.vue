<script setup>
import {onMounted} from "vue";
import {useAuthStore} from "stores/auth/auth-store";
import {storeToRefs} from "pinia";

const authStore = useAuthStore();

const {email} = storeToRefs(authStore);

</script>

<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white" height-hint="98">
      <q-toolbar>
        <q-toolbar-title style="display: flex; justify-content: space-between">
          <div>
            <q-avatar>
              <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
            </q-avatar>
            Title
          </div>
          <div>
            {{ email }}
          </div>
        </q-toolbar-title>
      </q-toolbar>

      <q-tabs align="left">
        <q-route-tab to="/page1" label="Page One"/>
        <q-route-tab to="/page2" label="Page Two"/>
        <q-route-tab to="/page3" label="Page Three"/>
      </q-tabs>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title>
          <div>Title</div>
        </q-toolbar-title>
      </q-toolbar>
    </q-footer>

  </q-layout>
</template>


