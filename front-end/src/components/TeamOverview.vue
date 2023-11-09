<template>
  <div>
    <table-component
      :amount-to-display="5"
      :table-data="teams"
    ></table-component>
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import { teams } from "@/models/team.js";

export default {
  components: {
    TableComponent,
  },
  data() {
    return {
      teams,
    };
  },
};
</script>

<style scoped></style>
