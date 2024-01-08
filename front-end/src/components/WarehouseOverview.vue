<template>
  <div>
    <table-component
      v-if="!wareHousesAreLoading"
      :amount-to-display="5"
      :has-add-button="true"
      :has-delete-button="true"
      :has-edit-button="true"
      :table-data="warehouses"
      :has-search-bar="true"
      @edit="showEditModal"
      @delete="showDeleteModal"
      @add="showAddModal"
    />
    <SpinnerComponent v-else />
    <Transition>
      <modal-component
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

    <transition>
      <toast-component
        v-if="showToast"
        :toast-message="toastMessage"
        :toast-title="toastTitle"
      ></toast-component>
    </transition>
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";

/**
 * Overview for all warehouses
 *
 * @author Wilco van de Pol
 */
export default {
  name: "WarehouseOverview",
  components: {
    ToastComponent,
    ModalComponent,
    TableComponent,
    SpinnerComponent,
  },
  inject: ["warehouseService"],
  data() {
    return {
      warehouses: [
        {
          id: Number,
          name: String,
          location: String,
        },
      ],
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalWarehouse: {
        id: Number,
        name: String,
        location: String,
      },
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-warehouse-modal",
        UPDATE: "update-warehouse-modal",
        ADD: "add-warehouse-modal",
      }),

      showToast: false,
      toastTitle: "",
      toastMessage: "",
      wareHousesAreLoading: true,
    };
  },
  methods: {
    /**
     * Open the edit modal to update a warehouse
     * @param warehouse The warehouse to be updated
     */
    showEditModal(warehouse) {
      this.modalTitle = "Update warehouse";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalWarehouse = warehouse;
      this.okBtnText = "Save";
      this.showModal = true;
    },
    /**
     * Show the delete modal with corresponding warehouse info
     * @param warehouse Warehouse to be deleted
     */
    showDeleteModal(warehouse) {
      this.modalTitle = "Delete warehouse";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalWarehouse = warehouse;
      this.okBtnText = "Delete";
      this.showModal = true;
    },
    /**
     * Open the add modal to add a new warehouse and corresponding items
     */
    showAddModal() {
      this.modalTitle = "Add warehouse";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.okBtnText = "Add";
      this.showModal = true;
    },
    /**
     * Handle what happens when the ok button of the modal is clicked
     * @param warehouse The warehouse that is used
     * @param modal {String} The type of modal (delete, update, add).
     */
    handleOk(warehouse, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteWarehouse(warehouse);
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateWarehouse(warehouse);
          break;
        case this.MODAL_TYPES.ADD:
          this.addWarehouse(warehouse);
          break;
      }
    },
    /**
     * Delete a warehouse
     * @param warehouse The warehouse to delete
     */
    async deleteWarehouse(warehouse) {
      try {
        const warehouseToDelete = await this.warehouseService.delete(
          warehouse.id
        );
        this.warehouses = this.warehouses.filter(
          (warehouse) => warehouse.id !== warehouseToDelete.id
        );

        this.showModal = false;
        this.showTimedToast(
          "Warehouse deleted",
          `Warehouse ${warehouseToDelete.name} has been deleted`
        );
      } catch (exception) {
        this.showModal = false;
        if (exception.code >= 400 && exception.code < 500) {
          this.showTimedToast(
            "Failed to delete warehouse",
            exception.reason,
            8000
          );
        } else {
          this.showTimedToast(
            "Failed to delete warehouse",
            exception.message,
            8000
          );
        }
      }
    },
    /**
     * Update a warehouse
     * @param warehouse The warehouse to be updated
     */
    async updateWarehouse(warehouse) {
      try {
        const warehouseToUpdate = await this.warehouseService.update(warehouse);
        this.warehouses = this.warehouses.map((warehouse) =>
          warehouse.id === warehouseToUpdate.id ? warehouseToUpdate : warehouse
        );

        this.showModal = false;
        this.showTimedToast(
          "Warehouse updated",
          `Warehouse ${warehouseToUpdate.name} has been updated`
        );
      } catch (e) {
        this.showModal = false;
        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to update warehouse", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to update warehouse", e.message, 8000);
        }
      }
    },

    /**
     * Add a warehouse
     * @param warehouse Warehouse to be added
     */
    async addWarehouse(warehouse) {
      try {
        const warehouseToAdd = await this.warehouseService.add(warehouse);
        this.warehouses.push(warehouseToAdd);
        this.showModal = false;
        this.showTimedToast(
          "Warehouse added",
          `Warehouse ${warehouseToAdd.name} has been added`
        );
      } catch (e) {
        this.showModal = false;
        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to add warehouse", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to add warehouse", e.message, 8000);
        }
      }
    },

    /**
     * Formats the warehouse data for when the data is empty
     */
    formatEmptyWarehouseData() {
      return {
        id: "",
        name: "",
        location: "",
      };
    },

    /**
     * Shows a toast for 4 seconds with the given title and message
     * @param {String} title The title of the toast.
     * @param {String} message The message of the toast.
     * @param {Number} duration The duration of the toast in milliseconds. Default is 4000.
     * @author Tim Knops
     */
    showTimedToast(title, message, duration = 4000) {
      this.toastTitle = title;
      this.toastMessage = message;
      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
      }, duration);
    },
  },
  async created() {
    const data = await this.warehouseService.findAll();

    if (data.length === 0) {
      this.warehouses = [this.formatEmptyWarehouseData()];

      this.wareHousesAreLoading = false;
      return;
    }

    this.warehouses = data;
    this.wareHousesAreLoading = false;
  },
};
</script>

<style scoped></style>
