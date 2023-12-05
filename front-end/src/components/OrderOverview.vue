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

export default {
  name: "orderOverview",
  components: {ToastComponent, ModalComponent, SpinnerComponent, TableComponent, WarehouseHeaderDisplay},
  inject: ["orderService"],
  data() {
    return {
      activeWarehouse: {},
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

      showToast: false,
      toastTitle: "",
      toastMessage: ""

    }
  },
  methods: {
    setActiveWarehouse(warehouse) {
      this.activeWarehouse = warehouse
      this.$router.push("/orders/" + warehouse.name)
      this.orders = this.getOrdersForWarehouse(this.activeWarehouse)

    },

    getOrdersForWarehouse(warehouse) {
      return this.totalOrders.filter((order) => order.warehouse.id === warehouse.id)
          .map(order => ({
            id: order.id,
            warehouse: order.warehouse.name,
            deliverDate: this.formatDate(order.deliverDate),
            orderStatus: order.orderStatus
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
        hour: "numeric",
        minute: "numeric",
        second: "numeric",
        hour12: false

      });
    },

    /**
     * Formats the product data for when the data is empty.
     */
    formatEmptyTableData() {
      return {
        id: "",
        warehouse: "",
        deliverDate: "",
        orderStatus: "",
      };
    },

    /**
     * Show the delete modal with corresponding product info
     * @param product product to be deleted
     */
    showDeleteModal(order) {
      this.modalTitle = "Delete Order";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalOrderInfo = {
          id: order.id,
          warehouseName: order.warehouse
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

    async deleteOrder(order){
      try {
        const deleted = await this.orderService.delete(order.id);
        this.orders = this.orders.filter(order => order.id !== deleted.id)
        this.showModal = false
        this.showTimedToast(
            "Order Deleted",
            `Successfully deleted an order with id: ${deleted.id} for warehouse ${this.activeWarehouse.name}`)
      } catch (e) {
        console.error(e)
      }
    },

    async updateOrder(order) {
      try {
        const updated = await this.orderService.update(order);
        const formatted = { ...updated}
        formatted.warehouse = updated.warehouse.name
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
        console.log(e);
      }
    },

    async addOrder(order) {
      try {
        const added = await this.orderService.add(order);
        const formatted = {...added}
        formatted.warehouse = added.warehouse.name
        formatted.deliverDate = this.formatDate(added.deliverDate)
        this.orders.push(formatted);
        this.showModal = false;
        this.showTimedToast(
            "Order added",
            `Successfully added an order with id: ${added.id} from warehouse ${this.activeWarehouse.name}`)
      } catch (e) {
        console.log(e);
      }
    },

    showTimedToast(title, message) {
      this.toastTitle = title
      this.toastMessage = message
      this.showToast = true

      setTimeout(()=> this.showToast = false, 4000)
    },
  },

  async created() {
    const data = await this.orderService.findAll();
    this.totalOrders = data
    this.setActiveWarehouse(data[0].warehouse)
    this.ordersAreLoaded = true
  }

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