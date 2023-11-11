<template>
  <p>Are you sure you want to delete product {{modalItem.productName}}</p>
</template>
<script>
/**
 * Modal for deleting a product
 *
 * @author Julian Kruithof
 */
export default {
  name: "DeleteProductModal",
  data() {
    return {
      modalItem: {},
      hasError: false,
    }
  },
  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item)
  }
}
</script>
<style scoped>

</style>