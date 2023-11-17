<template>
  <div
    class="modal fade bg-dark bg-opacity-25 show d-block"
    id="static-modal"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
  >
    <div
      class="modal-dialog modal-dialog-centered"
      :class="{ 'wide-modal': this.activeModal === 'add-project-modal' }"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ title }}</h5>
          <button
            @click="$emit('cornerCloseModalBtn')"
            type="button"
            class="btn-close"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <component :is="activeModal" ref="modalRef" :item="item"></component>
        </div>
        <div class="modal-footer">
          <button
            @click="$emit('cancelModalBtn')"
            type="button"
            class="btn btn-secondary"
          >
            Cancel
          </button>
          <button @click="handleOk()" type="button" class="btn btn-primary">
            {{ okBtnText }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DeleteProductModal from "@/components/modal/product/DeleteProductModal.vue";
import UpdateProductModal from "@/components/modal/product/UpdateProductModal.vue";
import AddProductModal from "@/components/modal/product/AddProductModal.vue";
import DeleteWarehouseModal from "@/components/modal/warehouse/DeleteWarehouseModal.vue";
import AddWarehouseModal from "@/components/modal/warehouse/AddWarehouseModal.vue";
import UpdateWarehouseModal from "@/components/modal/warehouse/UpdateWarehouseModal.vue";
import UpdateInventoryModal from "@/components/modal/inventory/UpdateInventoryModal.vue";
import DeleteUserModal from "@/components/modal/user/DeleteUserModal.vue";
import UpdateUserModal from "@/components/modal/user/UpdateUserModal.vue";
import AddUserModal from "@/components/modal/user/AddUserModal.vue";
import AddTeamModal from "@/components/modal/team/AddTeamModal";
import UpdateTeamModal from "@/components/modal/team/UpdateTeamModal";
import DeleteTeamModal from "@/components/modal/team/DeleteTeamModal";
import DeleteProjectModal from "@/components/modal/project/DeleteProjectModal.vue";
import AddProjectModal from "./project/AddProjectModal.vue";

/**
 * General modal component, for the styling of the header and footer,
 * also creates a placeholder for custom modal bodies.
 * Custom bodies should contain the <b>hasError</b> and <b>modalItem</b> in its data directive for the modals to work .
 * correctly.
 * Each custom body can receive the item from an overview, via the general modal. By passing an item to the modal with props
 * This is not required i.e. for adding, you don't have an item yet, so you can't send it.
 *
 * @author Julian Kruithof
 */
export default {
  name: "ModalComponent",
  components: {
    DeleteProductModal,
    UpdateProductModal,
    AddProductModal,
    DeleteUserModal,
    UpdateUserModal,
    AddUserModal,
    DeleteWarehouseModal,
    UpdateWarehouseModal,
    AddWarehouseModal,
    UpdateInventoryModal,
    DeleteTeamModal,
    UpdateTeamModal,
    AddTeamModal,
    DeleteProjectModal,
    AddProjectModal,
  },
  /**
   * props
   * {String} title - title of the modal. For example delete product or edit project.
   * {String} activeModal - the name of the modal component which needs to be loaded into the body of the modal
   * {Object} item - An object of a certain item. For example a product, a project or a user.
   * {String} okBtnText - the text of the ok button in the modal.
   */
  props: ["title", "activeModal", "item", "okBtnText"],
  emits: ["okModalBtn", "cornerCloseModalBtn", "cancelModalBtn"],
  methods: {
    /**
     * Handles the logic when the ok button is clicked
     * get the itemCopy from the child modal, this is for example an updated product or to be deleted or to be added.
     * and emit this data which can be undefined with the current modal which was active.
     */
    handleOk() {
      if (!this.$refs.modalRef.hasError) {
        this.$emit(
          "okModalBtn",
          this.$refs.modalRef.modalItem,
          this.activeModal
        );
      }
    },
  },
};
</script>

<style scoped>
.modal-header {
  color: var(--bs-white);
  background-color: var(--color-primary);
}
.modal-title {
  font-weight: 700;
}

.wide-modal {
  min-width: 800px !important;
}
</style>
