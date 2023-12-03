<template>
  <form >
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Warehouse</label>
      <input id="warehouse" type="text" class="form-control" :value="item.warehouseName" disabled>
    </div>
    <div class="mb-3">
      <label for="product" class="form-label fw-bold">Product</label>
      <select
          v-model="modalItem.product.id"
          class="form-select"
          :class="{'border-danger': noProductSelectedError}"
          id="product"
          aria-label="product"
          @change="noProductSelectedError = false"
      >
        <option v-for="product in products" :key="product.id" :value="product.id">
          {{product.productName}}
        </option>
      </select>
      <p v-if="noProductSelectedError" class="text-danger"> Please select a product!</p>
    </div>
    <div class="mb-3">
      <label for="quantity" class="form-label fw-bold">Quantity</label>
      <input id="quantity"
             type="number"
             class="form-control"
             :class="{'border-danger': emptyQuantityError || decimalError}"
             v-model="modalItem.quantity"
             @blur="validateQuantity">
    </div>
    <p v-if="decimalError" class="text-danger"> Quantity should be a whole number!</p>
    <p v-if="emptyQuantityError" class="text-danger"> Quantity cant be empty!</p>
  </form>
</template>

<script>
/**
 * Component modal for adding an inventory
 *
 * @author Julian Kruithof
 */
export default {
  name: "AddInventoryModal.vue",
  data() {
    return {
      modalItem: {
        warehouse: {
          id: this.item.warehouseId
        },
        product: {
          id: null
        },
        quantity: 0
      },
      activeWarehouseId: Number,
      products: Array,
      decimalError: false,
      emptyQuantityError: false,
      noProductSelectedError: true,
    }
  },
  computed: {
    /**
     * Check if an error has occurred
     * @return {boolean} true if an error has occurred, otherwise false
     */
    hasError() {
      return this.noProductSelectedError && this.emptyQuantityError && this.decimalError
    }
  },
  inject: ["inventoryService"],
  props : ["item"],
  methods: {
    /**
     * validate the quantity.
     * a quantity should be an Integer
     */
    validateQuantity() {
      if (this.modalItem.quantity === "") {
        this.emptyQuantityError = true
      } else if (this.modalItem.quantity !== "" && !Number.isInteger(this.modalItem.quantity)) {
        this.emptyQuantityError = false
        this.decimalError = true
      } else {
        this.emptyQuantityError = false
        this.decimalError = false
      }
    }
  },
  created() {
    this.activeWarehouseId = this.item.warehouseId
    this.products = this.item.products
  }
}
</script>

<style scoped>

</style>