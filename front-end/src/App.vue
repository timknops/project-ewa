<template>
  <div class="d-flex justify-content-center align-items-center" id="temp-container">
    <UserComponent
        table-width="80vw"
        :amount-to-display="6"
        :bold-first-column="false"
        :table-data="userData"
        :array-amount-to-display="2"
    />
  </div>
</template>

<script>
import UserComponent from "@/components/UserComponent.vue";
import {userData} from "@/userData";

export default {
  name: "App",
  components: { UserComponent },
  data() {
    return {
      userData: userData,
    };
  },
};
</script>

<style>
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
  font-family: "Montserrat", sans-serif;
  color: var(--color-text) !important;
  background-color: var(--bs-gray-100) !important;
  height: 100vh;
  width: 100vw;
}

#temp-container {
  height: 100vh;
}
</style>
