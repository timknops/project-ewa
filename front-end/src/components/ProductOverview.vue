<template>
  <div>
    <table-component
        :amount-to-display="6"
        :has-edit-delete-buttons="true"
        :table-data="products"
        @edit="showEditModal"
        @delete="showDeleteModal"
        @add="showAddModal"
    />
    <Transition>
      <model-component
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalProduct"
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

/**
 * Component for the product overview. This overview give info about the different products of solar sedum.
 * The information include the id, name and description.
 *
 * Furthermore, the crud operation add, delete and update are handle by the overview, via the corresponding modals
 *
 * @author Julian Kruithof
 */
export default {
  name: "ProductOverview",
  components: {ModelComponent, TableComponent},
  inject:["productService"],
  data() {
    return {
      products: [{
        id: Number,
        productName: String,
        description: String,
      }],
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalProduct: {
        id: Number,
        productName: String,
        description: String
      },
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-product-modal",
        UPDATE: "update-product-modal",
        ADD: "add-product-modal"
      })
    }
  },
  methods: {
    /**
     * Show the delete modal with corresponding product info
     * @param product product to be deleted
     */
    showDeleteModal(product) {
      this.modalTitle = "Delete product"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalProduct = product
      this.okBtnText = "Delete"
      this.showModal = true;
    },

    /**
     * open the edit modal to update a product
     * @param product the product to be updated
     */
    showEditModal(product) {
      this.modalTitle = "Update product"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalProduct = product
      this.okBtnText = "Save"
      this.showModal = true;
    },

    /**
     * open the add modal to add a new product
     */
    showAddModal() {
      this.modalTitle = "Add product"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
    },

    /**
     * excute the correct method depending on which modal the ok button was clicked on
     * For updating it is Save for delete it is Delete etc.
     * @param product the product to be processed by the method, for example for the delete this is the product to be deleted
     * @param modal the modal on which the ok button was pressed
     */
    handleOk(product, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteProduct(product)
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateProduct(product)
          break;
        case this.MODAL_TYPES.ADD:
          this.addProduct(product)
          break;
      }
    },

    /**
     * Delete a product in the back-end and front-end
     * @param product the product to be deleted
     * @return {Promise<void>}
     */
    async deleteProduct(product) {
      try {
        const deleted = await this.productService.delete(product.id)
        this.products = this.products.filter((product) => product.id !== deleted.id)
        this.showModal = false;
      } catch (exception) {
        console.log(exception)
      }
    },

    /**
     * Update a product in the back-end and front-end
     * @param product the product to be updated
     * @return {Promise<void>}
     */
    async updateProduct(product) {
      try {
        const updated = await this.productService.update(product)
        this.products = this.products.map((product) => product.id === updated.id ? updated : product);
        this.showModal = false
      } catch (e) {
        //TODO give user error feedback
        console.log(e)
      }
    },

    /**
     * Add a product to the back-end and front-end
     * @param product the product to be added
     * @return {Promise<void>}
     */
    async addProduct(product){
      try {
        const added = await this.productService.add(product)
        this.products.push(added)
        this.showModal =false;

      } catch (e){
        console.log(e)
      }
    }
  },
  async created() {
    this.products = await this.productService.findAll();
  }
}
</script>


<style scoped>

</style>