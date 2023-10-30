<template>
  <div>
    <table-component
        :amount-to-display="6"
        :has-edit-delete-buttons="true"
        :table-data="products"
    />
  </div>
</template>
<script>
import TableComponent from "@/components/TableComponent.vue";

export default {
  name: "ProductOverview",
  components: {TableComponent},
  inject:["productService"],
  data() {
    return {
      products: [{
        id: Number,
        productName: String,
        description: String,
      }]
    }
  },
  async created() {
    this.products = await this.productService.findAll();
  }
}
</script>


<style scoped>

</style>