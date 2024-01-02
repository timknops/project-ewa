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
      <label for="minimum" class="form-label fw-bold"> Minimum</label>
      <input type="number"
             id="minimum"
             class="form-control"
             :class="{'border-danger': negativeMinimum}"
             v-model="modalItem.minimum"
             @blur="validateMinimum"
      >
      <p v-if="negativeMinimum" class="text-danger"> The minimum can't be a negative number</p>
    </div>

    <div class="mb-3">
      <label for="quantity" class="form-label fw-bold">Quantity</label>
      <input id="quantity"
             type="number"
             class="form-control"
             :class="{'border-danger': decimalError || emptyQuantity}"
             v-model="modalItem.quantity"
             @blur="validateQuantity">
      <p v-if="decimalError" class="text-danger"> Quantity should be a whole number</p>
      <p v-if="emptyQuantity" class="text-danger"> Quantity can not be empty</p>
    </div>
  </form>
</template>
<script>

/**
 * Modal for updating the quantity in the inventory overview
 *
 * @author Julian Kruithof
 */
export default {
  name: "UpdateInventoryModal",
  data() {
    return {
      modalItem: {},
      decimalError: false,
      negativeMinimum: false,
      emptyQuantity: false,
    }
  },
  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item)
  },

  computed: {
    hasError() {
      this.validateQuantity();
      this.validateMinimum();

      return this.decimalError || this.negativeMinimum || this.emptyQuantity;
    }
  },
  methods: {

    /**
     * Validate the quantity input
     *
     * A quantity can't be a decimal number and can't contain any letters or symbols
     * input type number, in vue sets the value to empty so only check on empty string
     */
    validateQuantity() {
      if (this.modalItem.quantity === "") {
        this.emptyQuantity = true
      } else if (this.modalItem.quantity !== "" && !Number.isInteger(this.modalItem.quantity)) {
        this.emptyQuantity = false
        this.decimalError = true
      } else {
        this.emptyQuantity = false
        this.decimalError = false
      }
    },

    validateMinimum() {
      this.negativeMinimum = this.modalItem.minimum < 0;
    }
  }
}
</script>


<style scoped>

</style>