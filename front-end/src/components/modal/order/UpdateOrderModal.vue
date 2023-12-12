<template>
  <form >
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Warehouse</label>
      <input
          id="warehouse"
          type="text"
          class="form-control"
          :value="item.warehouse.name"
          disabled
      />
    </div>
    <div class="mb-3">
      <label for="tag" class="form-label fw-bold">Tag</label>
      <input
          type="text"
          id="tag"
          class="form-control"
          :class="{'border-danger': emptyTag}"
          v-model.trim="modalItem.tag"
          @blur="validateTag"
      >
    </div>
    <div class="row grid mb3">
      <div class="col">
        <label for="deliver-date" class="form-label fw-bold">Deliver date</label>
        <input
            id="deliver-date"
            type="date"
            class="form-control"
            :class="{'border-danger': incorrectDate}"
            v-model.trim="modalItem.deliverDate"
        >
        <small v-if="incorrectDate" class="text-danger"> A deliver date can't be in the past</small>
      </div>
      <div class="col">
        <label for="order-status" class="form-label fw-bold">Order status</label>
        <select id="order-status" class="form-select" v-model="modalItem.status">
          <option v-for="status in STATUS" :key="status" :value="status">
            {{status.toLowerCase()}}
          </option>
        </select>
      </div>
    </div>
    <hr>

    <!-- items -->
    <div class="mb-3">
      <div
          class="d-flex gap-2 justify-content-between align-items-center mb-2 px-2"
      >
        <label for="products" class="form-label fw-bold mt-1">Products</label>
        <div class="d-flex gap-2">
          <button @click="addProduct" class="btn py-1 custom-btn" type="button">
            <FontAwesomeIcon icon="fa-solid fa-plus" size="sm"/>
          </button>
          <button
              @click="deleteSelectedProducts(itemsList)"
              class="btn py-1 custom-btn"
              type="button"
          >
            <FontAwesomeIcon icon="fa-solid fa-trash" size="sm" />
          </button>
        </div>
      </div>

      <div class="card border-0">
        <div class="table-responsive">
          <table class="table mb-0" id="products">
            <thead>
            <tr>
              <th scope="col" class="font-small">NAME</th>
              <th scope="col" class="font-small">QUANTITY</th>
              <th scope="col" class="font-small"></th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="modalItem.items.length === 0">
              <td colspan="3" class="text-center empty-text">
                No products added yet.
              </td>
            </tr>
            <tr
                v-else
                v-for="(item, index) in modalItem.items"
                :key="index"
            >
              <td>
                <select
                    v-model="item.product.id"
                    class="form-select"
                    :class="{'border-danger': itemNotFilled && item.product.id === undefined}"
                >
                  <option
                      v-for="product in products"
                      :key="product.id"
                      :value="product.id"
                  >
                    {{ product.productName }}
                  </option>
                </select>
                <small v-if="itemNotFilled && item.product.id === undefined" class="text-danger">Select a product!</small>
              </td>

              <td>
                <input
                    v-model.lazy.trim="item.quantity"
                    type="number"
                    class="form-control w-50"
                    :class="{
                      'border-danger': itemNotFilled && validateQuantity(item)}"
                />
                <small v-if="itemNotFilled && validateQuantity(item)" class="text-danger"> Fill in a quantity!</small>
              </td>

              <td>
                <div class="checkbox-container">
                  <input
                      @change="selectProduct(item)"
                      class="form-check-input border-dark align-middle m-0"
                      type="checkbox"
                      :checked="itemsList.includes(item)"
                  />
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </form>
</template>

<script>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
export default {
  name: "AddUpdateModal",
  components: {FontAwesomeIcon},
  props: ["item"],
  inject: ["productService", "orderService"],
  data() {
    return {
      modalItem: {
        id: Number,
        deliverDate: Date,
        orderStatus: String,
        items: []
      },
      products: {},
      STATUS: ["DELIVERED", "PENDING", "CANCELED"],
      incorrectDate: false,
      itemNotFilled: false,
      emptyTag: false,
      //track items which are added in modal
      itemsList: []
    }
  },

  async created() {
    this.products = await this.productService.findAll();
    const items = await this.orderService.getItemsForOrder(this.item.id)
    this.modalItem = {
      ...this.item,
      items: items
    }
  },

  computed: {
    emptyItems() {
      return this.modalItem.items.length === 0;
    },
    hasError() {
      const isEmpty = this.emptyItems;
      this.validateItems()


      return this.incorrectDate || isEmpty || this.itemNotFilled
    }
  },

  methods: {
    validateItems() {
      this.itemNotFilled = false;
      for (const item of this.modalItem.items) {
        if (this.validateQuantity(item)) {
          this.itemNotFilled = true
          break;
        }
        if (this.productSelected(item.product.id)) {
          this.itemNotFilled = true
          break;
        }
      }
    },

    validateQuantity(item) {
      return item.quantity === "" || item.quantity === undefined
    },

    validateTag() {
      this.emptyTag = this.modalItem.tag === ""
    },

    productSelected(item) {
      return item === undefined
    },

    addProduct(product) {
      this.modalItem.items.push({
        product: {
          id: product.id
        },
        order: {
          id: this.item.id
        },
        quantity: product.quantity
      })
    },

    selectProduct(product) {
      this.itemsList.push(product)
    },


    deleteSelectedProducts(products) {
      this.modalItem.items = this.modalItem.items.filter(item => !products.includes(item))
      this.itemsList = []
    }
  }
}
</script>


<style scoped>
label {
  margin-bottom: 4px !important;
}

.font-small {
  font-size: 0.9rem !important;
  color: var(--bs-gray-800) !important;
}

.custom-btn {
  background-color: var(--bs-gray-200) !important;
}
.custom-btn:hover {
  background-color: var(--bs-gray-400) !important;
}

.checkbox-container {
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
  height: 38px !important;
}

.empty-text {
  color: var(--bs-gray-800) !important;
}
</style>