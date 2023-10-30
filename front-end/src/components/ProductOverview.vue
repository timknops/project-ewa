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
      okBtnText: ""
    }
  },
  methods: {
    showDeleteModal(product) {
      this.modalTitle = "Delete product"
      this.modalBodyComponent ="delete-product-modal"
      this.modalProduct = product
      this.okBtnText = "Ok"
      this.showModal = true;
    },
    showEditModal(product) {
      this.modalTitle = "Update product"
      this.modalBodyComponent = "update-product-modal"
      this.modalProduct = product
      this.okBtnText = "Save"
      this.showModal = true;
    }
  },
  async created() {
    this.products = await this.productService.findAll();
  }
}
</script>


<style scoped>

</style>