<template>
  <div class="grid">
    <div class="col-12">
      <layout-header />
    </div>
    <div class="col-12">
      <div class="layout-component">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import LayoutHeader from '@/app/layout/components/LayoutHeader.vue';
import { RouterView } from "vue-router";
</script>

<style scoped>
.layout-component {
  margin: 10pt 10pt 10pt 10pt;
}

</style>
