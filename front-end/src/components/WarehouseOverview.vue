<template>
  <div>
    <table-component
      :amount-to-display="5"
      :has-edit-delete-buttons="true"
      :table-data="warehouses"
    />
  </div>

</template>

<script>
import TableComponent from "@/components/TableComponent.vue";
import {Warehouse} from "@/models/warehouse";

export default {
  name: "WarehouseOverview",
  components: {TableComponent},
  data() {
    return {
      warehouses: []
    };
  },
  created() {
    this.warehouses = Warehouse.createDummyWarehouses()
  },
};
</script>

<style scoped>

</style>