<template>
  <sidebar/>
  <router-view id="component"></router-view>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'

export default {
  name: 'App',
  components: {
    Sidebar
  }
}
</script>

<style>

#component{
  margin-left: 20rem;
  max-width: calc(100vw - 23rem);
}

/* Colors used by Solar Sedum*/
:root {
  --color-primary: #c7d02c;
  --color-secondary: #572700;
  --color-text: #333;
  --color-bg: #fff;
  --color-subtitle: #bfbfbf;
  --color-text-bg: #f8f8f8;
}

html, body {
  font-family: 'Montserrat', sans-serif !important; /*important while bootstrap tries to overwrite it */
  color: var(--color-text);
}
</style>
