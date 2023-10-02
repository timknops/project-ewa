<template>
  <!--    display the current warehouse which the user is assigned to-->
  <div class="container" >
    <h2 class="mb-4">Inventory</h2>
    <div class="row bg-solar-grey rounded-top mx-0" v-if="activeUser.role === 'viewer'">
      <div class="col">
        <strong>{{ activeUser.team.warehouse }}</strong>
      </div>
    </div>

    <!-- row containing all names of warehouses and total which the admin can pick    -->
    <div class="row border border-2 border-bottom-0 border-black rounded-top mx-0 p-1 pb-0" v-else>
      <div class="col-auto">
        <button type="button" class="warehouse-select btn btn-link p-0" :class="{active: activeWarehouse === 'Total'}"
                @click="setActiveWarehouse('Total')">
          Total inventory
        </button>
      </div>
      <div class="col-auto" v-for="warehouse in WAREHOUSES" :key="warehouse">
        <button type="button" class="warehouse-select btn btn-link p-0" :class="{active: warehouse === activeWarehouse}"
                @click="setActiveWarehouse(warehouse)">
          {{ warehouse }}
        </button>
      </div>
    </div>

    <!--    TODO display table with info waiting on component-->
    <!--    For now simple display shall be changed to table component when created -->
    <div class="row mx-0 bg-solar-grey">
      <div class="col">product</div>
      <div class="col">description</div>
      <div class="col">stock</div>
    </div>
    <div v-for="(product, index) in products" :key="product.id" class="row mx-0 bg-solar-grey"
         :class="{'rounded-bottom': index === products.length -1}">
      <div class="col">{{ product.productName }}</div>
      <div class="col">{{ product.description }}</div>
      <div class="col">{{ product.quantity }}</div>
    </div>
  </div>
</template>

<script>
import {Product} from "@/models/product";

export default {
  name: "product-overview",
  data() {
    return {
      totalProducts: [], //list of objects containing the warehouse and its products
      products: [], // the product and stock of the current active view, i.e. total or a certain warehouse

      activeUser: {name: String, role: String, team: {name: String, warehouse: name}},

      //for now only the name, could change to objects if needed.
      WAREHOUSES: ["Solar Sedum", "Superzon", "EHES", "The switch"],
      activeWarehouse: "Total", //total selected by default.

    }
  },

  methods: {
    // TODO should be available globally, and not stored directly in the component (comes with jwt)
    getUser() {
      return {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: "Superzon"
        }
      }
    },

    setActiveWarehouse(warehouse) {
      this.activeWarehouse = warehouse;
      //TODO push router with params
    },

    /**
     * Get the products and stock information for a certain warehouse
     * @param warehouse the warehouse which has been selected
     * @return {[Product]} an array of product objects or empty array if an error has occurred
     */
    getWarehouseProductInfo(warehouse) {
      const productsObjectArray = this.totalProducts.filter((totalList) => totalList.warehouse === warehouse)

      // filter should return one element in the array, because there is only one warehouse active
      if (productsObjectArray.length === 0 || productsObjectArray.length > 1) {
        console.error("their were multiple or no warehouses trying to receive their products")
        return [];
      }
      return productsObjectArray[0].products
    },

    getTotalProductInfo() {
      const productObjects = {} // create an object where all products objects are stored in with accumulated stock
      this.totalProducts.forEach((warehouseData) => {
        warehouseData.products.forEach((product) => {
            if (productObjects[product.productName]) {
              productObjects[product.productName].quantity += product.quantity //add the quantity to the total stock
            }
            else {
              //if product doesn't exist yet initiate the object to be put into the productsObject
              productObjects[product.productName] = {
                productName: product.productName,
                description: product.description,
                quantity: product.quantity
              }
            }
        })
      })
      return Object.values(productObjects)
    }
  },


  watch: {
    activeWarehouse() {
      if (this.activeWarehouse === "Total") {
        this.products = this.getTotalProductInfo()
      } else {
        this.products = this.getWarehouseProductInfo(this.activeWarehouse)
      }

    }
  },

  created() {
    this.activeUser = this.getUser()
    //get list of products depending on the users role i.e. the total inventory or inventory of the warehouse of the user
    if (this.activeUser.role === "viewer") {
      this.products = Product.createDummyProduct();
    } else {
      const array = []
      this.WAREHOUSES.forEach((warehouse) => {
        const obj = {warehouse: warehouse, products: Product.createDummyProduct()}
        array.push(obj)
      })
      this.totalProducts = array;
      this.products = this.getTotalProductInfo()
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

.warehouse-select {
  position: relative;
  cursor: pointer;
  transition: 200ms ease-out;
  color: var(--color-text);
}

.warehouse-select:hover,
.warehouse-select:focus{
  color: var(--color-secondary);
  outline: none;
}

/*
Overwriting bootstrap active class
 */
.warehouse-select.active,
.warehouse-select:first-child:active{
  color: var(--color-primary);
}


.warehouse-select::before {
  content: '';
  position: absolute;
  width: 0;
  height: 3px;
  bottom: -0.25em;
  border-radius: 10px 10px 0 0;
  transition: 200ms ease-out;
}

.warehouse-select.active::before,
.warehouse-select:hover::before,
.warehouse-select:focus::before{
  width: 100%;
  background-color: var(--color-primary);
}

.warehouse-select:not(.active):hover::before,
.warehouse-select:not(.active):focus::before{
  background-color: var(--color-secondary);
}


</style>