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
  </div>

</template>

<script>
import WarehouseHeaderDisplay from "@/components/util/WarehouseHeaderDisplay.vue";
import TableComponent from "@/components/table/TableComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";

export default {
  name: "orderOverview",
  components: {ModalComponent, SpinnerComponent, TableComponent, WarehouseHeaderDisplay},
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
            deliverDate: order.deliverDate,
            orderStatus: order.orderStatus
          }))

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
    showEditModal(order) {
      this.modalTitle = "Update order";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalOrderInfo = order;
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
      } catch (e) {
        console.error(e)
      }
    },

    updateOrder(order) {
      console.log(order)
    },

    async addOrder(order) {
      try {
        const added = await this.orderService.add(order);
        const formatted = {...added}
        formatted.warehouse = added.warehouse.name
        this.orders.push(formatted);
        this.showModal = false;
      } catch (e) {
        console.log(e);
      }
    }
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