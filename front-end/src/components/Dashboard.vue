<template>
  <div>

    <!--Dropdown-->
    <div class="btn-group dropdown-color">
      <button class="btn dropdown-toggle background-dropdown" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        {{selectedWarehouse ? selectedWarehouse : " Choose warehouse"}}
      </button>

      <div class="dropdown-menu">
        <a class="dropdown-item" :key="null" @click="warehouseSelect(null)">All warehouses</a>
        <a  v-for="warehouse in wareHouseNameData" :key="warehouse" class="dropdown-item" @click="warehouseSelect(warehouse)">{{warehouse}}</a>

    </div>
    </div>

    <!--Inventory-->
    <TableComponent
      :tableWidth="'100%'"
      :boldFirstColumn="true"
      :amountToDisplay="3"
      :tableData="selectedWarehouseData"
      :arrayAmountToDisplay="10"
      table-title="Inventory"
      sub-title="Current inventory of my warehouse"
    >
    </TableComponent>

    <div class="table-container mb-5 gap-5 d-flex w-100">
      <!--Forecast-->
      <div class="user-table-overview-left card border-0">
        <div class="table-container card-body align-items-center d-flex">
          <canvas
            ref="combinedChart"
            style="width: calc(50vw - 23rem); height: 100%"
            class="my-chart"
          ></canvas>
        </div>
      </div>

      <!--User information-->
      <TableComponent
        class="user-table-overview-right"
        tableWidth="60%"
        :boldFirstColumn="true"
        :amountToDisplay="4"
        :tableData="selectedWarehouseUserData"
        table-title="Users"
        sub-title="Current active users"
      >
      </TableComponent>
    </div>


  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import Chart from "chart.js/auto";

export default {
  // eslint-disable-next-line
  name: "Dashboard",
  components: {
    TableComponent,
  },
  data() {
    return {
      tableData: [
        {
          Warehouse: "Solar Clarity",
          Name: "enphase",
          Quantity: 9,
          Expected: 32,
        },
        {
          Warehouse: "Solar Clarity",
          Name: "Gateway",
          Quantity: 18,
          Expected: 30,
        },
        {
          Warehouse: "4Blue",
          Name: "MB 385 (white)",
          Quantity: 18,
          Expected: 30,
        },
      ],
      userData: [
        {
          Username: "hx",
          Warehouse: "Solar Clarity",
          Project: "Team 1"
        },
        {
          Username: "test",
          Warehouse: "4Blue",
          Project: "Team 2"
        },
        {
          Username: "hi",
          Warehouse: "Solar Clarity",
          Project: "Team 3"
        },

      ],
      forecastData: [
        {
          Forecast: "",
        },
      ],
      wareHouseNameData:["Solar Clarity", "4Blue"],
      selectedWarehouse: null,
      selectedWarehouseChart: null,

      chartDataList: [],
      //    chart: null,
      saveChart: null,
      chartWidth: 200,
      chartHeight: 80,
      // xValues: ["This week", "Expected"],
      //yValues: [0, 36],
      barColors: ["rgba(91, 46, 24, 1)"],
    };
  },

  mounted() {
    this.createChart(this.tableData);
  },

  computed: {
    selectedWarehouseData(){
      if(this.selectedWarehouse) {
        if (this.selectedWarehouse === null) {
          return this.tableData;
        } else {
          return this.tableData.filter((item) => item.Warehouse === this.selectedWarehouse);
        }
      }
      return this.tableData;
    },

    selectedWarehouseUserData() {
      if (this.selectedWarehouse) {
        if (this.selectedWarehouse === null) {
          return this.userData;
        } else {
          return this.userData.filter(
              (user) => user.Warehouse === this.selectedWarehouse
          );
        }
      }
      return this.userData;
    },
    },


  methods: {
    createChart(data) {

      if (this.saveChart) {
        this.saveChart.destroy();
      }

      const chartWidth = this.chartWidth;
      const chartHeight = this.chartHeight;


      const labels = data.map((item) => item.Name);
      const qdata = data.map((item) => item.Quantity);
      const expectedData = data.map((item) => item.Expected);

      qdata.push(50);

      const chartData = {
        labels: labels,
        datasets: [
          {
            label: "Quantity",
            backgroundColor: this.barColors[0],
            data: qdata,
          },
          {
            label: "Expected",
            backgroundColor: "rgba(199, 208, 44, 1)",
            data: expectedData,
          },
        ],
      };

      const chartOptions = {
        legend: {display: false},
        title: {
          display: true,
          text: "Inventory Chart",
        },
        width: chartWidth,
        height: chartHeight,
        // responsive: true,
        // maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
        plugins: {
          tooltip: {
            enabled: true,
          },
        },
        elements: {
          bar: {
            borderRadius: 30,
          },
        },
      };

     this.saveChart = new Chart(this.$refs.combinedChart, {
        type: "bar",
        data: chartData,
        options: chartOptions,
      });
    },

    warehouseSelect(nameOfTheWarehouse){
      this.selectedWarehouse = nameOfTheWarehouse;
      this.createChart(this.selectedWarehouseData);
    },
  },
};
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
  /*box-shadow: var(--custom-box-shadow);*/
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
