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
        :tableData="filteredInventoryData"
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
      chart: null,
    };
  },
  mounted() {
    // this.updateChart();
    this.fetchInventoryData();
  },
  watch: {
    selectedWarehouse: 'updateChartOnWarehouseChange',
    inventoryData: {
      handler: 'updateChart',
      immediate: true, // Trigger the handler on component mount
    },
    immediate: true,
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
          .map(({productName, quantity, deliverDate, inventoryQuantity }) => ({
            productName,
            quantity,
            deliverDate,
            inventoryQuantity,
          }));

    },

    uniqueWarehouseNames() {
      return Array.from(new Set(this.inventoryData.map((item) => item.warehouseName)));
    },
  },
  created() {
    this.fetchInventoryData();
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
    updateChartOnWarehouseChange() {
      this.updateChart();
    },

    warehouseSelect(warehouse) {
      this.selectedWarehouse = warehouse;
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
      // const currentDate = new Date();


      const currentDateFormattedValueTrimmed = new Date().toISOString().split("T")[0].trim();
      const dataBasedOnTheMonth = this.filteredInventoryData;


      const currentInventoryMap = {};
      dataBasedOnTheMonth.forEach(item => {
          currentInventoryMap[item.productName] = item.inventoryQuantity;
      });


      const nameLegend = [...new Set(dataBasedOnTheMonth.map(item => item.productName))];

      const datasets = nameLegend.map((name, index) => {
        const quantityData = dataBasedOnTheMonth
            .filter(item => item.productName === name)
            .map(item => ({
              x: item.deliverDate,
              y: item.quantity
            }));

        const currentDateFormattedValue = currentDateFormattedValueTrimmed;

        const currentInventoryQuantity = {
          x: currentDateFormattedValue,
          y: currentInventoryMap[name] || 0,
        };

        console.log('Dataset for', name, ':', [currentInventoryQuantity, ...quantityData]);

        return {
          label: name,
          backgroundColor: colorLegend[index % colorLegend.length],
          borderColor: colorLegend[index % colorLegend.length],
          data: [currentInventoryQuantity, ...quantityData],
          fill: false,
        };
      });

      const dateLabels = Array.from({length: 21}, (_, index) => {
        const nextDate = new Date(currentDateFormattedValueTrimmed);
        nextDate.setDate(nextDate.getDate() + index);
        const formattedDate = nextDate.toISOString().split("T")[0];
        return formattedDate;
      });

      const chartData = {
        labels: dateLabels,
        datasets: datasets,
      };

      const chartOptions = {
        // legend: { display: false },
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
            stepped: 'before',
          }
        },
      };

      console.log('Chart Data:', chartData);

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
//width: 100%; width: 800px;
  height: 450px;
  margin: 0 auto;
}

.colorTest {
  color: #7c7321;
}

</style>
