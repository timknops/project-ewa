<template>
  <div>
    <table-component
        :amount-to-display="6"
        :has-edit-delete-buttons="true"
        :table-data="products"
        @edit="showEditModal"
        @delete="showDeleteModal"
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
        UPDATE: "update-product-modal"
      })
    }
  },
  methods: {
    showDeleteModal(product) {
      this.modalTitle = "Delete product"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalProduct = product
      this.okBtnText = "Ok"
      this.showModal = true;
    },
    showEditModal(product) {
      this.modalTitle = "Update product"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalProduct = product
      this.okBtnText = "Save"
      this.showModal = true;
    },
    handleOk(product, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteProduct(product)
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateProduct(product)
          break;
      }
    },
    async deleteProduct(product) {
      try {
        const deleted = await this.productService.delete(product.id)
        this.products = this.products.filter((product) => product.id !== deleted.id)
        this.showModal = false;
      } catch (exception) {
        console.log(exception)
      }
    },

    async updateProduct(product) {
      try {
        const updated = await this.productService.update(product)
        this.products = this.products.map((product) => product.id === updated.id ? updated : product);
        this.showModal = false
      } catch (e) {
        //TODO give user error feedback
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