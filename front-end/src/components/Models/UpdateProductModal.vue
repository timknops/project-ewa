<template>
  <form>
    <div class="mb-3">
      <label for="id" class="form-label fw-bold">ID</label>
      <input id="id" type="text" class="form-control" :value="itemCopy.id" disabled>
    </div>
    <div class="mb-3">
      <label for="product-name" class="form-label fw-bold">Product name</label>
      <input id="product-name" type="text" class="form-control" v-model.lazy.trim="itemCopy.productName">
    </div>
    <div class="mb-3">
      <label for="description" class="form-label fw-bold">description</label>
      <textarea id="description"  class="form-control" v-model.lazy.trim="itemCopy.description"></textarea>
    </div>
  </form>
</template>
<script>
export default {
  name: "UpdateProductModal",
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
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>