<template>
  <!--    display the current warehouse which the user is assigned to-->
  <div>
    <div
      class="row warehouse-display rounded-top mx-0 p-1 pb-0"
      v-if="activeUser.role === 'viewer'"
    >
      <div class="col">
        <strong class="warehouse-select active">{{
          activeUser.team.warehouse
        }}</strong>
      </div>
    </div>

    <!-- row containing all names of warehouses and total which the admin can pick    -->
    <div
      class="row warehouse-display rounded-top mx-0 p-1 pb-0"
      v-else-if="activeUser.role === 'admin'"
    >
      <div class="col-auto">
        <button
          type="button"
          class="warehouse-select btn btn-link p-0"
          :class="{ active: activeWarehouse === 'Total' }"
          @click="setActiveWarehouse('Total')"
        >
          <strong>Total inventory</strong>
        </button>
      </div>
      <div class="col-auto" v-for="warehouse in WAREHOUSES" :key="warehouse.id">
        <button
          type="button"
          class="warehouse-select btn btn-link p-0"
          :class="{ active: warehouse.warehouseName === activeWarehouse }"
          @click="setActiveWarehouse(warehouse)"
        >
          <strong>{{ warehouse.warehouseName }}</strong>
        </button>
      </div>
    </div>
    <table-component
      class="rounded-top-0 mt-0"
      :amount-to-display="6"
      :table-data="products"
    ></table-component>
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent.vue";

/**
 * Component handling the logic of displaying the inventory.
 *
 * if a user is an admin the user can view the total inventory of all warehouses,
 * or the user can view the inventory of the selected warehouse
 *
 * if a user is a viewer the user can only view the inventory of the warehouse the user is associated to
 *
 * @author Julian Kruithof
 */
export default {
  name: "InventoryOverview",
  components: { TableComponent },
  data() {
    return {
      /* list of objects containing the warehouse and its products
       * format of array is [{warehouse: String, products: Product[]}]
       */
      totalProducts: [],

      /* The product and stock of the current active view, i.e. total or a certain warehouse.
       * This is an array of products objects
       */
      products: [],

      activeUser: {
        name: String,
        role: String,
        team: { name: String, warehouse: name },
      },

      //for now only the name, could change to objects if needed.
      WAREHOUSES: [
        {
          id: 3000,
          warehouseName: "Solar Sedum"
        },
        {
          id: 3003,
          warehouseName: "Superzon"},
        {
          id: 3006,
          warehouseName: "EHES"
        },
        {
          id: 3009,
          warehouseName: "The Switch"
        }],
      activeWarehouse: "Total", //total selected by default.
    };
  },

  inject: ["resourceService"],

  methods: {
    // TODO should be available globally, and not stored directly in the component (comes with jwt)
    getUser() {
      return {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: {
            id: 3003,
            warehouseName: "Superzon"
          },
        },
      };
    },

    findWarehouseByName(name) {
      if (name === "Total") return "Total"
      return this.WAREHOUSES.find((warehouse) => name === warehouse.warehouseName)
    },

    setActiveWarehouse(warehouse) {
      if (warehouse.id){
        this.activeWarehouse = warehouse;
      } else {
        warehouse = this.findWarehouseByName(warehouse);
        this.activeWarehouse = warehouse;
      }

      if (warehouse === "Total") {
        this.$router.push("/inventory");
      } else {
        this.$router.push("/inventory/" + warehouse.warehouseName);
      }
    },

    /**
     * Get the products and stock information for a certain warehouse
     * @param warehouse the warehouse which has been selected
     * @return {[Product]} an array of product objects or empty array if an error has occurred
     */
    getWarehouseProductInfo(warehouse) {
      const productsObjectArray = this.totalProducts.filter(
        (totalList) => totalList.warehouse.id === warehouse.id
      );

      // filter should return one element in the array, because there is only one warehouse active
      if (productsObjectArray.length === 0 || productsObjectArray.length > 1) {
        console.error(
          "There were multiple or no warehouses trying to receive their products"
        );
        return [];
      }

      return productsObjectArray[0].products;
    },

    /**
     * Reformat the totalProduct array to a format accepted by the products array
     * aggregating the quantity of all products in all warehouses.
     *
     * This function checks for all warehouses which products it has, for each of these products it gets the quantity.
     * Of the product already exist in a warehouse that was checked, it adds the quantity to the already known stock
     *
     * @return {[Product]} array of product objects containing productName, description and quantity
     */
    getTotalProductInfo() {
      const productObjects = {}; // create an object where all products objects are stored in with accumulated stock
      this.totalProducts.forEach((warehouseData) => {
        warehouseData.products.forEach((product) => {
          //if product already exists as key value pair in the object of product objects. increment the stock by the quantity of the current product
          if (productObjects[product.productName]) {
            productObjects[product.productName].quantity += product.quantity;
          } else {
            //if product doesn't exist yet initiate the object to be put into the productsObject
            productObjects[product.productName] = {
              productName: product.productName,
              description: product.description,
              quantity: product.quantity,
            };
          }
        });
      });
      //turn the object of product objects into an array of product objects
      return Object.values(productObjects);
    },
  },

  watch: {
    /**
     * If the active warehouse changes, the products array should update, so that the table gets re-rendered
     */
    activeWarehouse() {
      if (this.activeWarehouse === "Total") {
        this.products = this.getTotalProductInfo();
      } else {
        this.products = this.getWarehouseProductInfo(this.activeWarehouse);
      }
    },

    $route() {
      //activeWarehouse should not change when user is a viewer
      if (this.activeUser.role === "viewer") return;

      if (this.$route.params.warehouse == null) {
        this.activeWarehouse = "Total";
      } else {
        this.setActiveWarehouse(this.$route.params.warehouse);
      }
    },
  },

  async created() {
    this.activeUser = this.getUser();

    //get list of products depending on the users role i.e. the total inventory or inventory of the warehouse of the user
    if (this.activeUser.role === "admin") {
      this.totalProducts = await this.resourceService.findAll();
      console.log(this.totalProducts)
      //set the products to the products for all warehouses, i.e. when admin choses total as view.
      this.products = this.getTotalProductInfo();
    } else {
      this.products = await this.resourceService.findAllForWarehouse(this.activeUser.team.warehouse.id);
    }

    //set active if there is a param in the url
    if (this.$route.params.warehouse) {
      //activeWarehouse should not change when user is a viewer
      if (this.activeUser.role === "viewer") return;
      this.setActiveWarehouse(this.$route.params.warehouse);
    }
  },
};
</script>

<style scoped>
h2 {
  color: var(--color-primary);
}

.warehouse-display {
  background-color: var(--color-bg);
}

.warehouse-select {
  position: relative;
  transition: 200ms ease-out;
  color: var(--color-text);
  text-decoration: none;
  z-index: 2;
}

.warehouse-select:hover,
.warehouse-select:focus {
  color: var(--color-secondary);
  outline: none;
}

/*
Overwriting bootstrap active class
 */
.warehouse-select.active,
.warehouse-select:first-child:active {
  color: var(--color-primary);
}

.warehouse-select::before {
  content: "";
  position: absolute;
  width: 0;
  height: 3px;
  bottom: -0.25em;
  border-radius: 10px 10px 0 0;
  transition: 200ms ease-out;
}

.warehouse-select.active::before,
.warehouse-select:hover::before,
.warehouse-select:focus::before {
  width: 100%;
  background-color: var(--color-primary);
}

.warehouse-select:not(.active):hover::before,
.warehouse-select:not(.active):focus::before {
  background-color: var(--color-secondary);
}
</style>
