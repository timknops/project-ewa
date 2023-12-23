<template>
  <div>
    <warehouse-header-display
        :has-no-total-option="true"
        :active-warehouse="activeWarehouse"
        :active-user="activeUser"
        @setActiveWarehouse="setActiveWarehouse"
    />
    <table-component
        v-if="ordersAreLoaded"
        class="rounded-top-0 mt-0"
        :table-data="orders"
        :amount-to-display="5"
        :has-add-button="true"
        :has-edit-button="true"
        :has-delete-button="true"
        @add="showAddModal"
        @edit="showEditModal"
        @delete="showDeleteModal"
    />
    <spinner-component v-else/>

    <transition>
      <modal-component
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalOrderInfo"
          :ok-btn-text="okBtnText"
          @cancel-modal-btn="this.showModal = false"
          @corner-close-modal-btn="this.showModal = false"
          @ok-modal-btn="handleOk"
      ></modal-component>
    </transition>
    <transition>
      <toast-component
          v-if="showToast"
          :toast-message="toastMessage"
          :toast-title="toastTitle"
      ></toast-component>
    </transition>
  </div>

</template>

<script>
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";
import TableComponent from "@/components/table/TableComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";

/**
 * Overview for all orders for each specific warehouse
 *
 * @author Julian Kruithof
 */
export default {
  name: "orderOverview",
  components: {ToastComponent, ModalComponent, SpinnerComponent, TableComponent, WarehouseHeaderDisplay},
  inject: ["orderService", "warehouseService"],
  data() {
    return {
      activeWarehouse: {},

      // todo get from localstorage, with jwt
      activeUser: {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: {
            id: 1003,
            name: "Superzon",
          },
        },
      },
      orders: [],
      totalOrders: [],
      ordersAreLoaded: false,

      //modal info
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalOrderInfo: {},
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-order-modal",
        UPDATE: "update-order-modal",
        ADD: "add-order-modal",
      }),

      //toast variables
      showToast: false,
      toastTitle: "",
      toastMessage: ""

    }
  },
  methods: {
    /**
     * Set the active warehouse and get the orders for that warehouse
     * @param warehouse
     */
    async setActiveWarehouse(warehouse) {
      this.activeWarehouse = warehouse
      this.$router.push("/orders/" + warehouse.name)
      this.orders = await this.getOrdersForWarehouse(this.activeWarehouse.id)
      this.ordersAreLoaded = true //set to true, after first load. Thereafter, this will always be true.
    },

    /**
     * get the orders for the active warehouse and format it to the correct table format
     * @param id the id of the current active warehouse
     * @return * returns a list of all orders for a warehouse
     */
    async getOrdersForWarehouse(id) {
      const data = await this.warehouseService.findOrdersForWarehouse(id);
      return data.map(order => ({
        id: order.id,
        tag: order.tag,
        deliverDate: this.formatDate(order.deliverDate),
        status: order.status
      }))

    },

    /**
     * Formats date to, example: Jun 1, 2021
     * @param {Date} date Date to be formatted.
     * @returns {String} Formatted date.
     */
    formatDate(date) {
      // Formats date to, example: Jun 1, 2021
      return new Date(date).toLocaleDateString("en-US", {
        month: "short",
        day: "numeric",
        year: "numeric",
      });
    },

    /**
     * Formats the product data for when the data is empty.
     */
    formatEmptyTableData() {
      return {
        id: "",
        tag: "",
        deliverDate: "",
        status: "",
      };
    },

    /**
     * Show the delete modal with corresponding order info
     * @param order product to be deleted
     */
    showDeleteModal(order) {
      this.modalTitle = "Delete Order";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalOrderInfo = {
        id: order.id,
        warehouseName: this.activeWarehouse.name
      };
      this.okBtnText = "Delete";
      this.showModal = true;
    },

    /**
     * open the edit modal to update a order
     * @param order the order to be updated
     */
    async showEditModal(order) {
      this.modalTitle = "Update order";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalOrderInfo = await this.orderService.findById(order.id)
      this.okBtnText = "Save";
      this.showModal = true;
    },

    /**
     * open the add modal to add a new order and corresponding items
     */
    showAddModal() {
      this.modalTitle = "Add order";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.modalOrderInfo = {
        warehouse: this.activeWarehouse
      }
      this.okBtnText = "Add";
      this.showModal = true;
    },

    /**
     * Handle the flow, when the ok button of the modal is clicked
     * @param order the order on which a certain action should be performed
     * @param modal {String} The type of modal (delete, update, add).
     */
    handleOk(order, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteOrder(order);
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateOrder(order);
          break;
        case this.MODAL_TYPES.ADD:
          this.addOrder(order);
          break;
      }
    },

    /**
     * Delete an order
     * @param order the order which is deleted
     */
    async deleteOrder(order) {
      try {
        const deleted = await this.orderService.delete(order.id);
        this.orders = this.orders.filter(order => order.id !== deleted.id)
        this.showModal = false
        this.showTimedToast(
            "Order Deleted",
            `Successfully deleted an order with id: ${deleted.id} for warehouse ${this.activeWarehouse.name}`)
      } catch (e) {
        this.showModal = false;
        this.handleException(e, "Oops!")
      }
    },

    /**
     * Update an order
     * @param order the order to be updated
     * @return {Promise<void>}
     */
    async updateOrder(order) {
      try {
        const updated = await this.orderService.update(order);
        const formatted = {...updated}
        delete formatted.warehouse // remove warehouse from formatted object

        formatted.deliverDate = this.formatDate(updated.deliverDate)
        this.orders = this.orders.map((order) =>
            order.id === formatted.id ? formatted : order
        );
        this.showModal = false;
        this.showTimedToast(
            "Order Updated",
            `Successfully updated an order with id: ${updated.id} for warehouse ${this.activeWarehouse.name}`
        )
      } catch (e) {
        this.showModal = false;
        this.handleException(e, "Oops!")
      }
    },

    /**
     * Add an order
     * @param order the order to be added
     */
    async addOrder(order) {
      try {
        const added = await this.orderService.add(order);
        const formatted = {...added}
        delete formatted.warehouse;
        formatted.deliverDate = this.formatDate(added.deliverDate)

        this.orders.unshift(formatted);
        this.showModal = false;
        this.showTimedToast(
            "Order added",
            `Successfully added an order with id: ${added.id} from warehouse ${this.activeWarehouse.name}`)
      } catch (e) {
        this.showModal = false;
        this.handleException(e, "Oops!")
      }
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
      if (exception.code >= 400 && exception.code < 500) {
        this.showTimedToast(
            exceptionTitle,
            exception.reason
        );
      } else {
        this.showTimedToast(
            exceptionTitle,
            "Something went wrong"
        );
      }
    }
  },
}
</script>


<style scoped>
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