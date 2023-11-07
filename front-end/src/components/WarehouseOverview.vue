<template>
  <div>
    <table-component
      :amount-to-display="5"
      :has-edit-delete-buttons="true"
      :table-data="warehouses"
      @edit="showEditModal"
      @delete="showDeleteModal"
      @add="showAddModal"
    />
  </div>

</template>

<script>
import TableComponent from "@/components/TableComponent.vue";
import ModelComponent from "@/components/Models/ModelComponent.vue";

export default {
  name: "WarehouseOverview",
  components: {ModelComponent, TableComponent},
  inject:["warehouseService"],
  data() {
    return {
      warehouses: [],
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalWarehouse: {
        id: Number,
        name: String,
        location: String
      },
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-warehouse-modal",
        UPDATE: "update-warehouse-modal",
        ADD: "add-warehouse-modal"
      })
    };
  },
  async created() {
    this.warehouses = await this.warehouseService.findAll();
  },
};
</script>

<style scoped>

</style>