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
    <Transition>
      <model-component
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalWarehouse"
          :ok-btn-text="okBtnText"
          @cancel-modal-btn="this.showModal = false"
          @corner-close-modal-btn="this.showModal = false"
          @ok-modal-btn="handleOk"
      />
    </Transition>
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
      warehouses: [{
        id: Number,
        name: String,
        location: String
      }],
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
  methods: {
    showEditModal(warehouse) {
      this.modalTitle = "Update warehouse"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalWarehouse = warehouse
      this.okBtnText = "Save"
      this.showModal = true;
    },
    showDeleteModal(warehouse) {
      this.modalTitle = "Delete warehouse"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalWarehouse = warehouse
      this.okBtnText = "Delete"
      this.showModal = true;

    },
    showAddModal() {
      this.modalTitle = "Add warehouse"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
    },
    handleOk(warehouse, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteWarehouse(warehouse)
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateWarehouse(warehouse)
          break;
        case this.MODAL_TYPES.ADD:
          this.addWarehouse(warehouse)
          break;
      }
    },
    async deleteWarehouse(warehouse) {
      try {
        const warehouseToDelete = await this.warehouseService.delete(warehouse.id)
        this.warehouses = this.warehouses.filter((warehouse) => warehouse.id !== warehouseToDelete.id)
        this.showModal = false;
      } catch (exception) {
        console.log(exception)
      }
    },
    async updateWarehouse(warehouse) {
      try {
        const warehouseToUpdate = await this.warehouseService.update(warehouse)
        this.warehouses = this.warehouses.map((warehouse) => warehouse.id === warehouseToUpdate.id ? warehouseToUpdate : warehouse);
        this.showModal = false
      } catch (e) {
        console.log(e)
      }
    },
    async addWarehouse(warehouse){
      try {
        const warehouseToAdd = await this.warehouseService.add(warehouse)
        this.warehouses.push(warehouseToAdd)
        this.showModal =false;

      } catch (e){
        console.log(e)
      }
    }
  },
  async created() {
    this.warehouses = await this.warehouseService.findAll();
  },
};
</script>

<style scoped>

</style>