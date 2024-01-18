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
        v-if="inventoryQuantities.length > 0"
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :amountToDisplay="4"
        :tableData="tableDatas"
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

/**
 * The Dashboard component provides a dashboard view that displays information related to inventory, orders,
 * and project data. It includes a warehouse dropdown, a table for inventory data, a chart for forecasting,
 * and a table for pending order confirmations.
 *
 * @component Dashboard
 * @author Hanan Ouardi
 */
export default {
  // eslint-disable-next-line
  name: "Dashboard",
  components: {
    ModalComponent,
    TableComponent
  },
  inject: ["dashboardService", "orderService", "projectService"],
  /**
   * Vue.js data properties for the Dashboard component.
   *
   * @returns {{okBtnText: string, lastDataPoints: *[], inventoryData: *[], selectedWarehouse: string, inventoryQuantities: *[], projectData: *[], modalTitle: string, orderData: *[], orders: *[], selectedOrder: string, modalOrder: string, chart: null, showModal: boolean}}
   */
  data() {
    return {
      inventoryData: [],
      selectedWarehouse: "The switch", //default warehouse
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
      lastDataPoints: [],
    };
  },
  /**
   * Called after the component has been mounted.
   * Fetches inventory data and updates the chart.
   */
  mounted() {
    //Chart will be updated after the update
    this.$nextTick(() => {
      this.updateChart();
    });
    this.fetchInventoryData();
  },
  /**
   * Watcher for the changes in the selectedWarehouse property
   */
  watch: {
    selectedWarehouse: {
      handler: 'updateChartOnWarehouseChange',
    },
    inventoryData: {
      handler: 'updateChart',
      immediate: true,
    },
  },
  computed: {
    /**
     *  Computed property for the inventoryQuantitiesData into table format.
     *  Maps each item to a tableDataItem object.
     *
     * @returns {{inventoryQuantity: *, expected: (*|null), productName: *}[]}
     */
    tableDatas() {
      const data = this.inventoryQuantitiesData.map(({productName, inventoryQuantity}) => {
        const expected = this.lastDataPoints.find(item => item.productName === productName);
        const tableDataItem = {
          productName,
          inventoryQuantity,
          expected: expected ? expected.expected : null,
        };
        return tableDataItem;
      });
      return data;
    },
    /**
     * Computed property for the filteredInventoryData into the format required for the table.
     *
     * @returns {{quantity: *, inventoryQuantity: *, deliverDate: *, productName: *}[]}
     */
    tableData() {
      return this.filteredInventoryData.map(({productName, quantity, deliverDate, inventoryQuantity}) => ({
        productName,
        quantity,
        deliverDate,
        inventoryQuantity
      }));
    },
    /**
     * Computed property for the inventoryQuantitiesData into table format.
     * Maps each item to a filteredInventoryData object.
     * @returns {{quantity: *, inventoryQuantity: *, deliverDate: *, productName: *}[]}
     */
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
    inventoryQuantitiesData() {
      return this.inventoryQuantities.filter((item) => {
        return (
            this.selectedWarehouse === null ||
            item.warehouseName === this.selectedWarehouse
        );
      });

    },
    filteredProjectData() {
      const currentDate = new Date();
      return this.projectData.filter((project) => {
        const dueDate = new Date(project.dueDate);
        return dueDate > currentDate;
      }).filter(project => project.warehouseName === this.selectedWarehouse);
    },
    filteredInventoryQuantity() {
      const productsInTable = this.tableData.map(item => item.productName);
      const filteredQuantities = this.inventoryQuantities.filter(item =>
          !productsInTable.includes(item) && item.warehouseName === this.selectedWarehouse);
      return filteredQuantities;
    },
    uniqueWarehouseNames() {
      return Array.from(new Set(this.inventoryQuantities.map((item) => item.warehouseName)));
    },
  },
  /**
   * Vue.js created
   *
   * @returns {Promise<void>}
   * @throws {Error} Throws an error if data fetching fails.
   */
  async created() {
    try {
      await this.fetchInventoryData();
      await this.fetchOrderData();
      await this.fetchProjectData();
      await this.fetchInventoryQuantity();
      this.updateChart();
    } catch (error) {
      console.error("Error in created hook:", error);
    }
  },
  methods: {
    /**
     * Async fetches inventory data from the dashboard service
     * An error message will show if the request fails.
     * @returns {Promise<void>}
     */
    async fetchInventoryData() {
      try {
        this.inventoryData = await this.dashboardService.findAll();
      } catch (error) {
        console.error("Error fetching inventory data:", error);
      }
    },
    async fetchOrderData() {
      this.orders = await this.orderService.findAllPending()
      this.orderData = this.getOrdersBySelectedWarehouse(this.orders)
    },
    getOrdersBySelectedWarehouse(data) {
      return data
          .filter(item => item.warehouse.name === this.selectedWarehouse)
          .map(({id, deliverDate, tag, status}) => ({
            id,
            deliverDate,
            tag,
            status
          }));
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
        await this.fetchInventoryData();
        await this.fetchOrderData();
        await this.fetchProjectData();
        await this.fetchInventoryQuantity();
        this.updateChart();
        this.showModal = false;
      } catch (e) {
        console.error(e)
      }
    },
    /**
     * Async fetches project data from the dashboard service
     * An error message will show if the request fails.
     * @returns {Promise<void>}
     */
    async fetchProjectData() {
      try {
        this.projectData = await this.dashboardService.findAllProjects();
      } catch (error) {
        console.error("Error fetching project data:", error);
      }
    },
    /**
     * Async fetches inventory quantity data from the dashboard service
     * An error message will show if the request fails.
     * @returns {Promise<void>}
     */
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
    /**
     * Updates the forecasting chart with the latest data based on warehouse selection
     * Destroys the existing chart and creates a new one with updated information.
     */
    updateChart() {
      // Check if there is an existing chart, and destroy it before creating a new one
      if (this.saveChart) {
        this.saveChart.destroy();
      }
      // Array to store the last data points for each product
      this.lastDataPoints = [];

      // Color legend for different chart datasets
      const colorLegend = [
        'rgba(199, 208, 44, 1)',
        'rgba(91, 46, 24, 1)',
        '#000000FF',
        '#7a7272',
        '#444b65',
        '#988960',
        '#7c7321'
      ];
      // Get the current date in ISO format and trim the time part
      const currentDateFormattedValueTrimmed = new Date().toISOString().split("T")[0].trim();

      // Data for the chart based on the month, project data, inventory quantity, and test line
      const dataBasedOnTheMonth = this.chartData;
      const dataProject = this.filteredProjectData;
      const constInventoryQuantity = this.filteredInventoryQuantity;
      const currentInventory = this.inventoryQuantitiesData;

      // Maps to store relevant data for each product
      const currentInventoryMaps = {};
      const amountOfProductMap = {};
      const totalInventoriesMap = {};
      const testLineOne = {};

      dataBasedOnTheMonth.forEach(item => {
        currentInventoryMaps[item.productName] = item.inventoryQuantity;
      });
      dataProject.forEach(item => {
        amountOfProductMap[item.productName] = item.amountOfProduct;
      });
      constInventoryQuantity.forEach(item => {
        totalInventoriesMap[item.productName] = item.inventoryQuantity;
      });
      currentInventory.forEach(item => {
        testLineOne[item.productName] = item.inventoryQuantity;
      });

      //Combined data from different places
      const combinedData = [...dataBasedOnTheMonth, ...constInventoryQuantity];
      const nameLegend = [...new Set(combinedData.map(item => item.productName))];

      /**
       * Datasets for the chart, a product with its quantity.
       *
       * @type {{backgroundColor: string, borderColor: string, data: [{x: string, y},...{x: *, y}[]], label: *, fill: boolean}[]}
       */
      const datasets = nameLegend.map((name, index) => {
        //Order quantities
        let doubleOrderCounting = 0;
        //Quantity data for the current product
        const quantityData = dataBasedOnTheMonth
            .filter(item => item.productName === name)
            .map(item => {
              doubleOrderCounting += item.quantity
              return {
                date: item.deliverDate,
                itemChange: doubleOrderCounting,
                type: 'order',
              };
            });
        const currentDateFormattedValue = currentDateFormattedValueTrimmed;

        // Current inventory quantity for the product
        const currentInventoryQuantity = {
          date: currentDateFormattedValue,
          itemChange: totalInventoriesMap[name] || 0,
          type: 'current',
        };

        // Project quantities
        let doubleProjectCounting = 0;
        /**
         * Get all the quantity changes for a product on a certain date
         * @type {{x: *, y}[]}
         */
        const projectQuantityUsedForProduct = dataProject
            .filter(item => item.productName === name)
            .map(item => {
              doubleProjectCounting += item.amountOfProduct;
              return {
                date: item.dueDate,
                itemChange: doubleProjectCounting,
                type: 'project',
              };
            });

        //sort all stock for a product by date
        const allStockInteractionsForProduct = [...quantityData, currentInventoryQuantity, ...projectQuantityUsedForProduct];
        allStockInteractionsForProduct.sort((a, b) => {
          const dateA = new Date(a.date);
          const dateB = new Date(b.date);
          return dateA - dateB;
        });
        //create the chart points for the product
        let stockAtThisMoment = 0;
        const createChartPoints = allStockInteractionsForProduct.map((item) => {
          if (item.type === 'order') {
            // a order adds to the stock
            stockAtThisMoment += item.itemChange;
          } else if (item.type === 'current') {
            // current stock should be the first point
            stockAtThisMoment = item.itemChange;
          } else if (item.type === 'project') {
            // a project removes from the stock
            stockAtThisMoment -= item.itemChange;
          }
          return {
            x: item.date,
            y: stockAtThisMoment,
          };
        })

        const allDataPoints = [...createChartPoints];

        return {
          label: name,
          backgroundColor: colorLegend[index % colorLegend.length],
          borderColor: colorLegend[index % colorLegend.length],
          data: allDataPoints,
          fill: false,
        };
      });

      // Date labels for the chart
      const dateLabels = Array.from({length: 60}, (_, index) => {
        const nextDate = new Date(currentDateFormattedValueTrimmed);
        nextDate.setDate(nextDate.getDate() + index);
        const formattedDate = nextDate.toISOString().split("T")[0];
        return formattedDate;
      });

      // Sort datasets based on the date of their first data point
      datasets.sort((a, b) => {
        const dateA = new Date(a.data[0].x);
        const dateB = new Date(b.data[0].x);
        return dateA - dateB;
      });

      // Sort date labels to make the line continue straight from its last point
      dateLabels.sort((a, b) => {
        const dateA = new Date(a);
        const dateB = new Date(b);
        return dateA - dateB;
      });

      // Get the last date in the date labels
      const lastDate = dateLabels[dateLabels.length - 1];

      // Add a point to the datasets to make the line continue straight from its last point
      datasets.forEach(dataset => {
        if (dataset.data.length === 1) {
          dataset.data.push({
            x: lastDate,
            y: dataset.data[0].y,
          });
        }
        // Get the expected quantity for the last data point
        const expected = dataset.data[dataset.data.length - 1];
        this.lastDataPoints.push({
          productName: dataset.label,
          expected: expected.y,
        });
      });
      // Chart data configuration
      const chartData = {
        labels: [...dateLabels],
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
            tension: 0, //straight lines
          }
        },
      };

      this.$nextTick(() => {
        this.saveChart = new Chart(this.$refs.combinedChart, {
          type: "line",
          data: chartData,
          options: chartOptions,
        });
      });
    },

  },
  /**
   * Ensures that the forecasting chart is destroyed before the component is unmounted.
   *
   * @method beforeUnmount
   */
  beforeUnmount() {
    //chart is destroyed before the component is destroyed
    if (this.saveChart) {
      this.saveChart.destroy();
    }
  }
  // if (this.saveChart) {
  //   this.saveChart.destroy();
  // }
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
