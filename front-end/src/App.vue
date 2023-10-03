<template>
  <div
    class="d-flex justify-content-center align-items-center"
    id="temp-container"
  >
    <TableComponent table-width="50%" :highlight-first-row="true" />
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent.vue";

export default {
  name: "App",
  components: { TableComponent },
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
