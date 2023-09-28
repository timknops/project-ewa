<template>
  <h2>Products</h2>
</template>

<script>
import {Product} from "@/models/product";
export default {
  name: "productOverview",
  data() {
    return {
      products: [],
      Warehouses: ["Solar", "Superzon", "EHES", "The switch"],
      activeWarehouse: String
    }
  },
  created() {
   this.products = Product.createDummyProduct()
    this.products.forEach((product)=> {
      console.log(product)
    })
  }
}

</script>

<style scoped>

</style>