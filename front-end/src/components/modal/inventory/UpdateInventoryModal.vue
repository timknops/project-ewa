<template>
  <form>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Warehouse</label>
      <input id="warehouse" type="text" class="form-control" :value="modalItem.warehouse.name" disabled>
    </div>
    <div class="mb-3">
      <label for="product" class="form-label fw-bold">Product</label>
      <input id="product" type="text" class="form-control" :value="modalItem.product.productName" disabled>
    </div>
    <div class="mb-3">
      <label for="quantity" class="form-label fw-bold">Quantity</label>
      <input id="quantity"
             type="number"
             class="form-control"
             :class="{'border-danger': hasError || decimalError}"
             v-model="modalItem.quantity"
             @blur="validateQuantity">
    </div>
    <p v-if="decimalError" class="text-danger"> Quantity should be a whole number!</p>
    <p v-else-if="hasError" class="text-danger"> Quantity input should only contain numbers!</p>
  </form>
</template>
<script>
export default {
  name: "UpdateInventoryModal",
  data() {
    return {
      modalItem: {},
      hasError: false,
      decimalError: false,
    }
  },
  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item)
  },
  methods: {
    validateQuantity() {
      if (this.modalItem.quantity !== "" && !Number.isInteger(this.modalItem.quantity)) {
        this.decimalError = true
        this.hasError = true
      } else {
        this.decimalError = false;
        this.hasError = this.modalItem.quantity === ""
      }
    }
  }
}
</script>


<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>