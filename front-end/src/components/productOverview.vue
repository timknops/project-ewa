<template>
  <h2>Products</h2>
</template>

<script>
import {Product} from "@/models/product";
export default {
  name: "productOverview",
  data() {
    return {
      products: []
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