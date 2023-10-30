<template>
  <p>Are you sure you want to delete product {{item.productName}}</p>
</template>
<script>
export default {
  name: "DeleteProductModal",
  data() {
    return {
      itemCopy: {}
    }
  },
  props: ["item"],
  created() {
    this.itemCopy = Object.assign({}, this.item)
  }
}
</script>
<style scoped>

</style>