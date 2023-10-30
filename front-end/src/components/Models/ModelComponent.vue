<template>
  <div
      class="modal fade bg-dark bg-opacity-25 show d-block"
      id="static-modal"
      data-bs-backdrop="static"
      data-bs-keyboard="false"
      tabindex="-1"
  >
    <div class="modal-dialog modal-dialog-centered">
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
          <button
              @click="handleOk()"
              type="button"
              class="btn btn-primary"
          >
            {{okBtnText}}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DeleteProductModal from "@/components/Models/DeleteProductModal.vue";
import UpdateProductModal from "@/components/Models/UpdateProductModal.vue";

export default {
  name: "ModelComponent",
  components: {DeleteProductModal, UpdateProductModal},
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
     * get the itemCopy form the child modal, this is for example an updated product
     * and emit this data which can be undefined with the current modal which was active.
     */
    handleOk() {
        this.$emit("okModalBtn", this.$refs.modalRef.itemCopy, this.activeModal)
    }
  }
}
</script>

<style scoped>

</style>