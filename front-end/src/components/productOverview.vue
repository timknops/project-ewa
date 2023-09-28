<template>
  <!--    display the current warehouse which the user is assigned to-->
  <div class="container" v-if="activeUser.role === 'viewer'">
    <h2 class="mb-4">Inventory</h2>
    <div class="row bg-solar-grey rounded-top mx-0">
      <div class="col">
        <strong>{{ activeUser.team.warehouse }}</strong>
      </div>
    </div>

<!--    TODO display table with info waiting on component-->
<!--    For now simple display shall be changed to table component when created -->
    <div class="row mx-0 bg-solar-grey">
      <div class="col">product</div>
      <div class="col">description</div>
      <div class="col">stock</div>
    </div>
    <div v-for="(product, index) in products" :key="product.id"  class="row mx-0 bg-solar-grey" :class="{'rounded-bottom': index === products.length -1}">
      <div class="col">{{ product.productName}}</div>
      <div class="col">{{ product.description}}</div>
      <div class="col">{{product.quantity}}</div>
    </div>
  </div>

<!--  TODO display for admin with v-else-->
</template>

<script>
import {Product} from "@/models/product";

export default {
  name: "product-overview",
  data() {
    return {
      products: [],
      activeUser: {name: String, role: String, team: {name: String, warehouse: name}},
    }
  },

  methods: {
    // TODO should be available globally, and not stored directly in the component (comes with jwt)
    getUser() {
      return {
        name: "Julian",
        role: "viewer",
        team: {
          name: "team1",
          warehouse: "Superzon"
        }
      }
    },

    //TODO use database to get products for certain warehouse
    getProducts(warehouse = null) {
      //if only get products from a certain warehouse
      if (warehouse != null) {
        return Product.createDummyProduct();
      }
      //TODO get list of product for all warehouses
    }
  },

  created() {
    this.activeUser = this.getUser()

    //get list of products depending on the users role i.e. the total inventory or inventory of the warehouse of the user
    if (this.activeUser.role === "viewer") {
      this.products = this.getProducts(this.activeUser.team.warehouse)
    }
  }
}

</script>

<style scoped>
h2 {
  color: var(--color-primary);
}

.bg-solar-grey {
  background-color: var(--color-text-bg);
}
</style>