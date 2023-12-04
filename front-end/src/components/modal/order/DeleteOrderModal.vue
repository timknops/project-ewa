<template>
  <p>Are you sure you want to delete order {{modalItem.id}} for warehouse {{modalItem.warehouseName}}</p>
</template>
<script>
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
    this.modalItem = Object.assign({}, this.item);
  }
}
</script>


<style scoped>

</style>