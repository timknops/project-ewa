<template>
  <loginPage
    @update-logged-in="updateLoggedIn()"
    v-if="loggedInActive === 'false'"
  ></loginPage>
  <div v-else class="view">
    <sidebar />
    <header-component class="header"></header-component>
    <router-view id="component"></router-view>
  </div>
</template>

<script>
import Sidebar from "@/components/Sidebar.vue";
import LoginPage from "@/components/LoginPage.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";

localStorage.setItem("loggedIn", true);
export default {
  name: "App",
  components: {
    LoginPage,
    HeaderComponent,
    Sidebar,
  },
  data() {
    return {
      loggedInActive: {},
    };
  },
  methods: {
    updateLoggedIn() {
      this.loggedInActive = localStorage.getItem("loggedIn");
    },
  },
  created() {
    this.loggedInActive = localStorage.getItem("loggedIn");
  },
};
</script>

<style>
.view {
  grid-template-areas:
    "sidebar header"
    "sidebar component";
  grid-template-columns: var(--sidebar-width) 1fr;
  grid-template-rows: var(--navbar-height) 1fr;
  display: grid;
}

#component {
  grid-area: component;
  margin: 0 48px 48px 48px;
}

.header {
  grid-area: header;
}

/* Colors used by Solar Sedum*/
:root {
  --color-primary: #c7d02c;
  --color-secondary: #572700;
  --color-text: #333;
  --color-bg: #fff;
  --color-subtitle: #bfbfbf;
  --color-text-bg: #f8f8f8;

  --btn-secondary-shadow-color: 64, 29, 0; /*rgb because of the box-shadow*/

  --sidebar-width: 17rem;
  --navbar-height: 10rem;
  --custom-box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05),
    0 4px 6px -4px rgb(0 0 0 / 0.05);
}

body {
  font-family: "Montserrat", sans-serif !important;
  color: var(--color-text) !important;
  background-color: var(--bs-gray-100) !important;
}
</style>
