<template>
  <img alt="Solar Sedum logo" src="./assets/solar_logo.svg">
  <product-overview></product-overview>
</template>

<script>
import ProductOverview from "@/components/productOverview.vue";

export default {
  name: 'App',
  components: {ProductOverview},
}
</script>

<style>
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
