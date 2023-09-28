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
  margin-left: 17rem;
  max-width: 200px;
}
:root {
  --color-primary: #c7d02c;
  --color-secondary: #572700;
  --color-text: #333;
  --color-bg: #fff;
  --color-subtitle: #bfbfbf;
  --color-text-bg: #f8f8f8;
}

html, body {
  font-family: 'Montserrat', sans-serif;
  color: var(--color-text);
}
</style>
