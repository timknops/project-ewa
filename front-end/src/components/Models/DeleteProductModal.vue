<template>
  <p>Are you sure you want to delete product {{item.productName}}</p>
</template>
<script>
export default {
  name: "DeleteProductModal",
  props: ["item"]
}
</script>
<style scoped>

</style>