<template>
  <!--    display the current warehouse which the user is assigned to-->
  <div>
    <warehouse-header-display :active-user="activeUser" :active-warehouse="activeWarehouse" total-text="Total Inventory"
                              @setActiveWarehouse="setActiveWarehouse" v-if="!productsAreLoading">
    </warehouse-header-display>
    <table-component
        v-if="!productsAreLoading"
        class="rounded-top-0 mt-0"
        :amount-to-display="6"
        :table-data="products"
        :has-edit-button="activeWarehouse != null && activeWarehouse !== 'Total'"
        :has-add-button="activeWarehouse != null && activeWarehouse !== 'Total'"
        :hide-id-column="true"
        @edit="showUpdateModal"
        @add="showAddModal"
    ></table-component>

    <!--    Templated doesn't wait for loading so show spinner for user information-->
    <spinner-component v-else></spinner-component>

    <transition>
      <modal-component
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalResource"
          :ok-btn-text="okBtnText"
          @cancel-modal-btn="this.showModal = false"
          @corner-close-modal-btn="this.showModal = false"
          @ok-modal-btn="handleOk"
      ></modal-component>
    </transition>

    <transition>
      <toast-component
          v-if="showToast"
          :toast-title="toastTitle"
          :toast-message="toastMessage"
      ></toast-component>
    </transition>
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";

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
  components: {
    WarehouseHeaderDisplay,
    ToastComponent,
    SpinnerComponent,
    ModalComponent,
    TableComponent,
  },
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
        team: {name: String, warehouse: name},
      },

      activeWarehouse: {},

      //modal variables
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      MODAL_TYPES: Object.freeze({
        UPDATE: "update-inventory-modal",
        ADD: "add-inventory-modal",
      }),
      okBtnText: "",
      modalResource: {},

      productsAreLoading: true,
      showToast: false,
      toastMessage: "",
      toastTitle: "",
    };
  },

  inject: ["inventoryService"],

  methods: {
    // TODO should be available globally, and not stored directly in the component (comes with jwt)
    getUser() {
      return {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: {
            id: 1003,
            name: "Superzon",
          },
        },
      };
    },


    /**
     * Set the active warehouse
     * @param warehouse a warehouse object
     */
    setActiveWarehouse(warehouse) {
      this.activeWarehouse = warehouse;
      if (warehouse === "Total") {
        this.products = this.getTotalProductInfo()
        this.$router.push(this.$route.matched[0].path);
      } else {
        this.products = this.getWarehouseProductInfo(warehouse)
        this.$router.push(`${this.$route.matched[0].path}/${warehouse.name}`);
      }
    },

    /**
     * Get the products and stock information for a certain warehouse
     * @param warehouse the warehouse which has been selected
     * @return {} an array of product objects or empty array if an error has occurred
     */
    getWarehouseProductInfo(warehouse) {
      const productsObjectArray = this.totalProducts.filter(
          (totalList) => totalList.warehouse.id === warehouse.id
      );

      // filter should return one element in the array, because there is only one warehouse active
      if (productsObjectArray.length > 1) {
        console.error(
            "There were multiple or no warehouses trying to receive their products"
        );
        return [];
      }

      if (productsObjectArray.length === 0) {
        return [this.formatEmptyTableData()];
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
              quantity: product.quantity,
            };
          }
        });
      });
      //turn the object of product objects into an array of product objects
      return Object.values(productObjects);
    },

    //   methods for modal
    /**
     * show the update modal
     * @param inventory the inventory object on which the edit button was clicked
     *
     *
     */
    showUpdateModal(inventory) {
      /*format the inventory to the format the back-end expect to receive, which is:
      {
        product: {id, productName, description},
        warehouse: {id, name},
        quantity: Number,
      }
       */
      this.modalTitle = "Update inventory";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.okBtnText = "Save";
      this.modalResource = {
        product: {
          id: inventory.id,
          productName: inventory.productName,
        },
        warehouse: this.activeWarehouse,
        minimum: inventory.minimum,
        quantity: inventory.quantity,
      };

      // show the modal
      this.showModal = true;
    },

    async showAddModal() {
      const productsWithoutInventory =
          await this.inventoryService.getProductWithoutInventory(
              this.activeWarehouse.id
          );
      if (productsWithoutInventory.length === 0) {
        this.showTimedToast("All products have an inventory", "No Inventory to be added")
        return
      }
      this.modalTitle = "Add inventory";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.modalResource = {
        warehouseId: this.activeWarehouse.id,
        warehouseName: this.activeWarehouse.name,
        products: productsWithoutInventory,
      };
      this.okBtnText = "Add";
      this.showModal = true;
    },

    handleOk(inventory, modal) {
      switch (modal) {
        case this.MODAL_TYPES.UPDATE:
          this.handleUpdate(inventory);
          break;
        case this.MODAL_TYPES.ADD:
          this.handleAdd(inventory);
          break;
      }
    },

    /**
     *
     * @param inventory -  an inventory object in the format of the back-end i,e

     *
     * @return {Promise<void>}
     */
    async handleUpdate(inventory) {
      try {
        //returns the same format as the inventory explained above
        const updated = await this.inventoryService.updateInventory(inventory);

        //find the correct warehouse where a quantity is updated for
        const warehouseIndex = this.totalProducts.findIndex(
            (resource) => resource.warehouse.id === updated.warehouse.id
        );

        if (warehouseIndex !== -1) {
          //find the correct product to update the quantity for
          const productIndex = this.totalProducts[
              warehouseIndex
              ].products.findIndex((product) => product.id === updated.product.id);

          if (productIndex !== -1) {
            //update the correct product
            this.totalProducts[warehouseIndex].products[productIndex].quantity =
                updated.quantity;
            this.totalProducts[warehouseIndex].products[productIndex].minimum = updated.minimum;
          }
        }
        this.showModal = false;
        this.showTimedToast("Inventory updated!", `Successfully updated inventory for Product: ${updated.product.productName} and warehouse: ${this.activeWarehouse.name}`)
      } catch (e) {
        this.showModal = false;
        this.handleException(e, "Failed to update Inventory");
      }
    },

    /**
     * Handles the addition of a new inventory item.
     *
     * @param {Object} inventory - The inventory item to be added.
     * @param {number} inventory.warehouse.id - The ID of the warehouse associated with the inventory.
     * @param {Object} inventory.product - The product associated with the inventory.
     * @param {number} inventory.product.id - The ID of the product.
     * @param {string} inventory.product.productName - The name of the product.
     * @param {string} inventory.product.description - The description of the product.
     * @param {number} inventory.quantity - The quantity of the product in the inventory.
     */
    async handleAdd(inventory) {
      try {
        const saved = await this.inventoryService.addInventory(inventory);
        const warehouseIndex = this.totalProducts.findIndex(
            (inventory) => inventory.warehouse.id === saved.warehouse.id
        );
        //reformat the saved inventory object to an object used in the products list of the inventory
        const inventoryObj = {
          id: saved.product.id,
          productName: saved.product.productName,
          minimum: saved.minimum,
          quantity: saved.quantity,
        };

        if (warehouseIndex !== -1) {
          //if warehouse already has inventory items existing add the new inventory to the list
          this.totalProducts[warehouseIndex].products.unshift(inventoryObj);
        } else {
          //no inventory exist for the warehouse, push the correct warehouse to the total list and add inventory to the list
          this.totalProducts.push({
            warehouse: this.activeWarehouse,
            products: [{...inventoryObj}],
          });
          this.products = [{...inventoryObj}];
        }
        this.showModal = false;
        this.showTimedToast("Inventory added!", `Successfully added inventory for Product: ${inventoryObj.productName} and warehouse: ${this.activeWarehouse.name}`)
      } catch (e) {
        this.showModal = false;
        this.handleException(e, "Failed to add Inventory")
      }
    },

    /**
     * Formats the product data for when the data is empty.
     * @return {{id: "", productName: "", description: ""}}
     */
    formatEmptyTableData() {
      return {
        id: "",
        productName: "",
        description: "",
        quantity: "",
      };
    },

    /**
     * Show a toast to give the user some information about how the Crud operation went
     * @param title the title of the toast
     * @param message the to show to the user
     */
    showTimedToast(title, message) {
      this.toastTitle = title
      this.toastMessage = message
      this.showToast = true

      // after 4 seconds remove the toast from view
      setTimeout(() => this.showToast = false, 4000)
    },

    /**
     * Handles the exception and shows a toast to the user.
     * @param {{code: Number, reason: String}} exception The exception to be handled.
     * @param {String} exceptionTitle The title of the exception.
     */
    handleException(exception, exceptionTitle) {
      // If the exception is a client-error, show the reason of the exception.
      if (exception.code >= 400 && exception.code < 500) {
        this.showTimedToast(
            exceptionTitle,
            exception.reason
        );
      } else {
        // If the exception is a server-error, show a generic message.
        this.showTimedToast(
            exceptionTitle,
            "Something went wrong"
        );
      }
    }
  },

  async created() {
    this.activeUser = this.getUser();

    //get list of products depending on the users role i.e. the total inventory or inventory of the warehouse of the user
    if (this.activeUser.role === "admin") {
      this.totalProducts = await this.inventoryService.findAll();
      //set the products to the products for all warehouses, i.e. when admin choses total as view.
      this.products = this.getTotalProductInfo();
    } else {
      //only get inventory for one warehouse
      this.products = await this.inventoryService.findAllForWarehouse(
          this.activeUser.team.warehouse.id
      );
    }

    // If there are no products, add only the table headers.
    if (this.products.length === 0) {
      this.products = [this.formatEmptyTableData()];
      this.productsAreLoading = false;
    }

    this.productsAreLoading = false;
  },
};
</script>

<style scoped>
h2 {
  color: var(--color-primary);
}

/* styling for transitions*/
.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
