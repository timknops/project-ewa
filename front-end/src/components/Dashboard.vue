<template>
  <div>

    <!-- Warehouse Dropdown-->
    <div class="btn-group dropdown-color">
      <button
          class="btn dropdown-toggle background-dropdown"
          data-bs-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
      >
        {{ selectedWarehouse ? selectedWarehouse : " Choose warehouse" }}
      </button>

      <div class="dropdown-menu">
        <a
            class="dropdown-item"
            :key="null"
            @click="warehouseSelect(null)"
        >
          All warehouses
        </a>
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
        :tableData="filteredInventoryData"
        :arrayAmountToDisplay="10"
        table-title="Inventory"
        sub-title="Current inventory of my warehouse"
        hasSearchBar="true"
    >
    </TableComponent>

    <!-- Item Dropdown -->
    <div class="btn-group dropdown-color mt-3">
      <button
          class="btn dropdown-toggle background-dropdown"
          data-bs-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
      >
        {{ selectedItem ? selectedItem : " Choose item" }}
      </button>
      <div class="dropdown-menu">
        <a
            v-for="item in uniqueItemNames"
            :key="item"
            class="dropdown-item"
            @click="itemSelect(item)"
        >
          {{ item }}
        </a>
      </div>
    </div>

    <!--Chart forecasting-->
    <div class="table-container mb-5 gap-5 d-flex w-100">
      <div class="user-table-overview-left card border-0">
        <div class="table-container card-body align-items-center d-flex">
          <canvas ref="combinedChart" style="width: calc(50vw - 23rem); height: 100%" class="my-chart"></canvas>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import Chart from "chart.js/auto";
import TableComponent from "@/components/table/TableComponent.vue";


export default {
  // eslint-disable-next-line
  name: "Dashboard",
  components: {
    TableComponent
  },
  inject: ["dashboardService"],
  data() {
    return {
      inventoryData: [],
      selectedWarehouse: "Solar Sedum", //default warehouse
      selectedItem: null,
      chart: null,
    };
  },
  mounted() {
    this.initializeChart();
  },
  computed: {
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
          .map(({itemName, quantity, deliverDate}) => ({
            itemName,
            quantity,
            deliverDate,
          }));
    },
    uniqueWarehouseNames() {
      return Array.from(new Set(this.inventoryData.map((item) => item.warehouseName)));
    },
    uniqueItemNames() {
      const itemsForSelectedWarehouse = this.inventoryData
          .filter(
              (item) =>
                  this.selectedWarehouse === null ||
                  item.warehouseName === this.selectedWarehouse
          )
          .map((item) => item.itemName);

      return Array.from(new Set(itemsForSelectedWarehouse));
    },
  },
  created() {
    this.fetchInventoryData();
    this.initializeChart();
  },
  methods: {
    async fetchInventoryData() {
      try {
        this.inventoryData = await this.dashboardService.findAll();

        this.setDefaultItem();
      } catch (error) {
        console.error("Error fetching inventory data:", error);
      }
    },
    warehouseSelect(warehouse) {
      this.selectedWarehouse = warehouse;
      this.setDefaultItem();
      // this.updateChart();
    },
    itemSelect(item) {
      this.selectedItem = item;
      this.updateChart();
    },
    setDefaultItem() {
      const itemsForSelectedWarehouse = this.inventoryData
          .filter(
              (item) =>
                  this.selectedWarehouse === null ||
                  item.warehouseName === this.selectedWarehouse
          )
          .map((item) => item.itemName);

      // The first item as the default value
      this.selectedItem = itemsForSelectedWarehouse.length ? itemsForSelectedWarehouse[0] : null;
    },

    initializeChart() {
      const chartCanvas = this.$refs.combinedChart;
      if (!chartCanvas) {
        console.error("Canvas context is not available");
        return;
      }

      const ctx = chartCanvas.getContext("2d");

      if (!ctx) {
        console.error("Canvas context is not available");
        return;
      }

      // max. quantity dor the y-as
      const maxQuantity = Math.max(...this.inventoryData.map(item => item.quantity));

      /**
       * Returns the dates on the x-as
       * @type {Date}
       */
      const currentDate = new Date();
      const labels = [];
      for (let i = 0; i < 20; i++) {
        const date = new Date(currentDate);
        date.setDate(currentDate.getDate() + i);
        const formattedDate = date.toISOString().split("T")[0];
        labels.push(formattedDate);
      }

      const datasets = this.uniqueWarehouseNames.map(warehouse => {
        const data = this.filteredInventoryData
            .filter(item => item.warehouseName === warehouse)
            .map(item => item.quantity);
        return {
          label: warehouse,
          data,
          borderColor: "black",
        };
      });

      /**
       *
       * @type {Chart<ChartType, DefaultDataPoint<ChartType>, *>}
       */
      this.chart = new Chart(ctx, {
        type: "line",
        data: {
          labels: labels, //  labels (days)
          datasets: datasets, // datasets (item quantities)
        },
        options: {
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: "Forecasting",
            },
          },
          scales: {
            x: {
              type: "category",
              labels: labels, //Date
              title: {
                display: true,
                text: "Days",
              },
            },
            y: {
              type: 'linear',
              title: {
                display: true,
                text: "Amount",
              },
              min: 0,
              max: maxQuantity + 10,
              ticks: {
                stepSize: Math.ceil(maxQuantity / 10),
                beginAtZero: true,
              },
            },
          },
        },
      });
    },


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
</style>
