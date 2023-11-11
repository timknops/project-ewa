<template>
  <p>Are you sure you want to delete product {{modalItem.name}}</p>
</template>

<script>
export default {
  name: "DeleteWarehouseModal",
  data() {
    return {
      modalItem: {},
      hasError: false
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