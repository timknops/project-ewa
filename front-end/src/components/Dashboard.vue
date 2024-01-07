<template>
  <div>

    <!-- Warehouse Dropdown-->
    <div class="btn-group dropdown-color mb-3 w-100">
      <button
          class="btn dropdown-toggle background-dropdown "
          data-bs-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
      >
        {{ selectedWarehouse ? selectedWarehouse : " Choose warehouse" }}
      </button>

      <div class="dropdown-menu">
        <a
            v-for="warehouse in uniqueWarehouseNames"
            :key="warehouse"
            class="dropdown-item"
            @click="warehouseSelect(warehouse)"
        >
          {{ warehouse }}
        </a>

      </div>
    </div>

    <!--Inventory-->
    <TableComponent
        v-if="inventoryData.length > 0"
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :amountToDisplay="4"
        :tableData="tableData"
        :arrayAmountToDisplay="10"
        table-title="Inventory"
        sub-title="Future deliveries of my warehouse"
        hasSearchBar="true"
    >
    </TableComponent>


    <!--Chart forecasting-->
    <div class="table-container mb-5 gap-5 d-flex w-100 ">
      <div class="user-table-overview-left card border-0">
        <div class="table-container card-body align-items-center d-flex chart-container">
          <canvas ref="combinedChart" class="my-chart"></canvas>
        </div>
      </div>
    </div>

    <!--Confirm order-->
    <TableComponent
        v-if="orderData.length > 0"
        :hide-id-column="true"
        :table-width="'100%'"
        :amount-to-display="3"
        :table-data="orderData"
        :table-title="'Pending deliveries'"
        :sub-title="'Please confirm if a delivery has arrived'"
        :has-add-button="false"
        :has-delete-button="false"
        :has-edit-button="true"
        :has-search-bar="false"
        @edit="showConfirmModal"
    />

    <Transition>
      <modal-component
          v-if="showModal"
          :title="modalTitle"
          :item="modalOrder"
          :ok-btn-text="okBtnText"
          @cancel-modal-btn="this.showModal = false"
          @corner-close-modal-btn="this.showModal = false"
          @ok-modal-btn="handleConfirm"
      />
    </Transition>

  </div>
</template>

<script>
import Chart from "chart.js/auto";
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";

export default {
  // eslint-disable-next-line
  name: "Dashboard",
  components: {
    ModalComponent,
    TableComponent
  },
  inject: ["dashboardService", "orderService", "projectService"],
  data() {
    return {
      inventoryData: [],
      selectedWarehouse: "Solar Sedum", //default warehouse
      chart: null,
      orders: [],
      orderData: [],
      selectedOrder: "",
      showModal: false,
      modalTitle: "",
      modalOrder: "",
      okBtnText: "",

      projectData: [],
      inventoryQuantities: [],
    };
  },
  mounted() {
    this.fetchInventoryData();
    this.fetchOrderData();
  },
  watch: {
    selectedWarehouse: {
      handler: 'updateChartOnWarehouseChange',
    },

    inventoryData: {
      handler: 'updateChart',
      immediate: true, // Trigger the handler on component mount
    },
  },
  computed: {
    tableData() {
      return this.filteredInventoryData.map(({productName, quantity, deliverDate}) => ({
        productName,
        quantity,
        deliverDate,
      }));
    },

    chartData() {
      return this.filteredInventoryData.map(({productName, quantity, deliverDate, inventoryQuantity}) => ({
        productName,
        quantity,
        deliverDate,
        inventoryQuantity,
      }));
    },
    //table only shows the ones that have an upcoming date
    filteredInventoryData() {
      const currentDate = new Date();
      return this.inventoryData.filter((item) => {
        const deliverDate = new Date(item.deliverDate);
        return (
            (this.selectedWarehouse === null ||
                item.warehouseName === this.selectedWarehouse) &&
            deliverDate > currentDate
        );
      })
          // Sort the deliverDate in the table from low to high
          .sort((a, b) => {
            const firstDate = new Date(a.deliverDate);
            const lastDate = new Date(b.deliverDate);
            return firstDate - lastDate;
          })

          .map(({productName, quantity, deliverDate, inventoryQuantity}) => ({
            productName,
            quantity,
            deliverDate,
            inventoryQuantity,
          }));
    },
    filteredProjectData() {
      const currentDate = new Date();
      return this.projectData.filter((project) => {
        const dueDate = new Date(project.dueDate);
        return dueDate > currentDate;
      }).filter(project => project.warehouseName === this.selectedWarehouse);
    },
    filteredInventoryQuantity() {
      const productsInTable = new Set(this.tableData.map(item => item.productName));
      const filteredQuantities = this.inventoryQuantities.filter(item =>
          !productsInTable.has(item.productName) && item.warehouseName === this.selectedWarehouse);
      // console.log("Filtered Inventory Quantities:", filteredQuantities);
      return filteredQuantities;
    },
    uniqueWarehouseNames() {
      return Array.from(new Set(this.inventoryData.map((item) => item.warehouseName)));
    },
  },
  async created() {
    await this.fetchInventoryData();
    await this.fetchOrderData();
    await this.fetchProjectData();
    await this.fetchInventoryQuantity();
    this.updateChart();
  },
  methods: {
    async fetchInventoryData() {
      try {
        this.inventoryData = await this.dashboardService.findAll();
      } catch (error) {
        console.error("Error fetching inventory data:", error);
      }
    },
    async fetchOrderData() {
      this.orders = await this.orderService.findAll()
      this.orderData = this.getOrdersBySelectedWarehouse(this.orders)
    },
    getOrdersBySelectedWarehouse(data){
      return data
          .filter(item => item.warehouse.name === this.selectedWarehouse
              && item.status === 'PENDING')
          .map(({ id, deliverDate, tag, status }) => ({
            id,
            deliverDate,
            tag,
            status }));
    },
    showConfirmModal(order) {
      this.modalTitle = "Confirm delivery?";
      this.okBtnText = "Confirm";
      this.selectedOrder = order;
      this.showModal = true;
    },
    async handleConfirm() {
      try {
        await this.orderService.updateStatusOnly(this.selectedOrder.id)
        await this.fetchOrderData();
        this.showModal = false;
      } catch (e){
        console.error(e)
      }
    },
    async fetchProjectData() {
      try {
        this.projectData = await this.dashboardService.findAllProjects();
      } catch (error) {
        console.error("Error fetching project data:", error);
      }
    },
    async fetchInventoryQuantity() {
      try {
        this.inventoryQuantities = await this.dashboardService.findAllInventoryQuantity();
      } catch (error) {
        console.error("Error fetching project data:", error);
      }
    },
    updateChartOnWarehouseChange() {
      this.updateChart();
      this.orderData = this.getOrdersBySelectedWarehouse(this.orders)
    },
    warehouseSelect(warehouse) {
      this.selectedWarehouse = warehouse;
      this.fetchInventoryQuantity(warehouse);
      this.updateChart();
    },

    updateChart() {
      if (this.saveChart) {
        this.saveChart.destroy();
      }
      const colorLegend = [
        'rgba(199, 208, 44, 1)',
        'rgba(91, 46, 24, 1)',
        '#000000FF',
        '#7a7272',
        '#444b65',
        '#988960',
        '#7c7321'
      ];
      const currentDateFormattedValueTrimmed = new Date().toISOString().split("T")[0].trim();

      const dataBasedOnTheMonth = this.chartData;
      const dataProject = this.filteredProjectData;
      const constInventoryQuantity = this.filteredInventoryQuantity;

      const currentInventoryMaps = {};
      const amountOfProductMap = {};
      const totalInventoriesMap = {};

      dataBasedOnTheMonth.forEach(item => {
        currentInventoryMaps[item.productName] = item.inventoryQuantity;
      });
      dataProject.forEach(item => {
        amountOfProductMap[item.productName] = item.amountOfProduct;
      });
      constInventoryQuantity.forEach(item => {
        totalInventoriesMap[item.productName] = item.inventoryQuantity;
      });

      const combinedData = [...dataBasedOnTheMonth, ...constInventoryQuantity];
      const nameLegend = [...new Set(combinedData.map(item => item.productName))];
      /**
       * second point
       * @type {{backgroundColor: string, borderColor: string, data: [{x: string, y},...{x: *, y}[]], label: *, fill: boolean}[]}
       */
      const datasets = nameLegend.map((name, index) => {

        let doubleOrderCounting = 0;
        const quantityData = dataBasedOnTheMonth
            .filter(item => item.productName === name)
            .map(item => {
              doubleOrderCounting += item.quantity
              const totalQuantity = doubleOrderCounting + item.inventoryQuantity;
              return {
                x: item.deliverDate,
                y: totalQuantity,
              };
            });

        const currentDateFormattedValue = currentDateFormattedValueTrimmed;
        /**
         * Inventory for all the products
         * @type {{}}
         */
        const inventoryQuantityDataForItem = constInventoryQuantity
            .filter(item => item.productName === name)
            .map(item => {
              currentInventoryMaps[item.productName] = item.inventoryQuantity;
                return {
                  x: currentDateFormattedValue,
                  y: item.inventoryQuantity,
                };
            });

        /**
         * checks the project from the inventory
         * @type {{x: *, y}[]}
         */
        const amountOfProductOnDueDate = dataProject
            .filter(item => item.productName === name)
            .map(item => {
              const quantitySum = constInventoryQuantity
                  .filter(monthItem => monthItem.productName === name)
                  .reduce((sum, monthItem) => sum + monthItem.inventoryQuantity, 0);
              const amountOfProductTest = dataProject
                  .filter(projectItem => projectItem.productName === name)
                  .reduce((sum, projectItem) => sum + projectItem.amountOfProduct, 0);
              const calculatedValues = quantitySum - amountOfProductTest;
              return {
                x: item.dueDate,
                y: calculatedValues || 0,
              };
      });

        /**
         * first point
         * @type {{x: string, y: (*|number)}}
         */
        const currentInventoryQuantity = {
          x: currentDateFormattedValue,
          y: currentInventoryMaps[name] || 0,
        };
        /**
         * third point
         */
        // const projectMinusQuanity = dataProject
        //     .filter(item => item.productName === name)
        //     .map(item => {
        //       const quantitySum = dataBasedOnTheMonth
        //           .filter(monthItem => monthItem.productName === name)
        //           .reduce((sum, monthItem) => sum + monthItem.quantity + monthItem.inventoryQuantity, 0);
        //       const amountOfProductSum = dataProject
        //           .filter(projectItem => projectItem.productName === name)
        //           .reduce((sum, projectItem) => sum + projectItem.amountOfProduct, 0);
        //       const calculatedValue = quantitySum - amountOfProductSum;
        //       return {
        //         x: item.dueDate,
        //         y: calculatedValue || 0,
        //       };
        //     });
        // console.log('Dataset for', name, ':', [currentInventoryQuantity, ...quantityData, ...projectMinusQuanity]);
        const allDataPoints = [currentInventoryQuantity, ...quantityData, ...inventoryQuantityDataForItem, ...amountOfProductOnDueDate];


        return {
          label: name,
          backgroundColor: colorLegend[index % colorLegend.length],
          borderColor: colorLegend[index % colorLegend.length],
          data: allDataPoints,
          fill: false,
        };
      });

      const dateLabels = Array.from({length: 60}, (_, index) => {
        const nextDate = new Date(currentDateFormattedValueTrimmed);
        nextDate.setDate(nextDate.getDate() + index);
        const formattedDate = nextDate.toISOString().split("T")[0];
        return formattedDate;
      });
      datasets.sort((a, b) => {
        const dateA = new Date(a.data[0].x);
        const dateB = new Date(b.data[0].x);
        return dateA - dateB;
      });
      /**
       * Makes the line continue straight from it's last point
       * @type {string}
       */
      const lastDate = dateLabels[dateLabels.length - 1];
      datasets.forEach(dataset => {
        const lastDataPoint = dataset.data[dataset.data.length - 1];
        dataset.data.push({x: lastDate, y: lastDataPoint.y});
      });
      const chartData = {
        labels: dateLabels,
        datasets: datasets,
      };
      const chartOptions = {
        plugins: {
          title: {
            display: true,
            text: "Forecasting",
          },
        },
        legend: {
          display: true,
          labels: {
            filter: function (item, chart) {
              // Filter legend items based on selected warehouse
              const warehouseName = chart.data.datasets[item.datasetIndex].label;
              return !this.selectedWarehouse || warehouseName === this.selectedWarehouse;
            }.bind(this),
          },
        },
        scales: {
          x: {
            type: "category",
            title: {
              display: true,
              text: "Days",
            },
          },
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: "Quantity",
            },
          },
        },
        tooltip: {
          enabled: true,
          callbacks: {
            label: (context) => {
              const item = context.dataset.data[context.dataIndex];
              return `${context.dataset.label}: ${item.y}`;
            }
          },
        },
        elements: {
          line: {
            tension: 0,
          }
        },
      };

      this.saveChart = new Chart(this.$refs.combinedChart, {
        type: "line",
        data: chartData,
        options: chartOptions,
      });
    }
  },
}
</script>


<style scoped>
h2 {
  color: var(--color-primary);
}

.dropdown-color .dropdown-menu {
  background-color: white;

}

.dropdown-color .dropdown-item {
  color: black;
}

.my-chart {
  width: 100%;
  height: auto;
}

.table-container {
  margin: 0 1px;
}

.user-table-overview-right {
  margin-top: 50px;
}

.user-table-overview-left {
  margin-top: 50px;
  box-shadow: var(--custom-box-shadow);
  border-radius: 0.5rem;
}

.background-dropdown {
  background-color: rgba(199, 208, 44, 1);
}

.chart-container {
//width: 100%; width: 800px; height: 450px; margin: 0 auto;
}

.colorTest {
  color: #7c7321;
}

</style>
