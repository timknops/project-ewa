<template>
  <sidebar/>
  <header-component class="header"></header-component>
  <router-view id="component"></router-view>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import HeaderComponent from "@/components/HeaderComponent.vue";

export default {
  name: 'App',
  components: {
    HeaderComponent,
    Sidebar
  }
}
</script>

<style>

#component{
  margin-left: 20rem;
  max-width: calc(100vw - 23rem);
}
.header {
  margin-left: 17rem;
}

/* Colors used by Solar Sedum*/
:root {
  --color-primary: #c7d02c;
  --color-secondary: #572700;
  --color-text: #333;
  --color-bg: #fff;
  --color-subtitle: #bfbfbf;
  --color-text-bg: #f8f8f8;

  --custom-box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05),
    0 4px 6px -4px rgb(0 0 0 / 0.05);
}

html,
body {
  font-family: "Montserrat", sans-serif !important;
  color: var(--color-text) !important;
  background-color: var(--bs-gray-100) !important;
  height: 100vh;
  width: 100vw;
}
</style>
