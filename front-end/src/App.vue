<template>
  <loginPage @update-logged-in="updateLoggedIn()" v-if="loggedInActive === 'false'"></loginPage>
  <div v-else>
    <sidebar></sidebar>
    <router-view id="component"></router-view>
  </div>
</template>

<script>
import Sidebar from '@/components/Sidebar.vue'
import LoginPage from "@/components/LoginPage.vue";

// localStorage.setItem('loggedIn', false)
export default {
  name: 'App',
  components: {
    LoginPage,
    Sidebar
  },
  data() {
    return {
      loggedInActive: {},
    }
  },
  methods: {
    updateLoggedIn() {
      this.loggedInActive = localStorage.getItem('loggedIn');
    }
  },
  created() {
    this.loggedInActive = localStorage.getItem('loggedIn')
  }
}
</script>

<style>

#component {
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
