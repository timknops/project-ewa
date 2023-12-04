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


<!--    Dropdown for months-->


    <div class="table-container mb-5 gap-5 d-flex w-100">
      <!--Forecast-->
      <div class="user-table-overview-left card border-0">

        <div class="btn-group dropdown-color">
          <button class="btn dropdown-toggle background-dropdown" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            {{ selectedMonth ? selectedMonth : currentMonth }}
          </button>


          <div class="dropdown-menu position-absolute">
            <a v-for="(month, index) in allMonths" :key="index" class="dropdown-item" @click="selectMonth(month)">{{ month }}</a>
          </div>
        </div>

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
          Month: "December",
          Date: 4,
        },

        {
          Warehouse: "Solar Clarity",
          Name: "enphase",
          Quantity: 15,
          Expected: 32,
          Month: "December",
          Date: 6,
        },

        {
          Warehouse: "Solar Clarity",
          Name: "Gateway",
          Quantity: 18,
          Expected: 30,
          Month: "January"

        },
        {
          Warehouse: "Solar Clarity",
          Name: "MB 385 (white)",
          Quantity: 18,
          Expected: 30,
          Month: "December",
          Date: 5
        },
        {
          Warehouse: "4Blue",
          Name: "MB 385 (white)",
          Quantity: 33,
          Expected: 30,
          Month: "January"
        },
        {
          Warehouse: "4Blue",
          Name: "MB 385 (white)",
          Quantity: 33,
          Expected: 30,
          Month: "March"
        },
        {
          Warehouse: "4Blue",
          Name: "Gateway",
          Quantity: 11,
          Expected: 30,
          Month: "December"
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

      //dropdown chart
      selectedMonth: null,
      currentMonth: "",
      allMonths: [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
      ],
      chartDataList: [],
      //    chart: null,
      saveChart: null,
      chartWidth: 200,
      chartHeight: 80,
      // xValues: ["This week", "Expected"],
      // yValues: [0, 36],
      barColors: ["rgba(91, 46, 24, 1)"],
    };
  },

  mounted() {
    this.currentMonth = this.getMonthName(new Date().getMonth());
    this.selectedMonth = this.currentMonth;
    this.createChart(this.tableData);
  },

  computed: {
    selectedWarehouseData(){
      if(this.selectedWarehouse) {
        if (this.selectedWarehouse === null) {
          return this.tableData.map(item => ({ Warehouse: item.Warehouse, Name: item.Name}));
        } else {
          return this.tableData.filter((item) => item.Warehouse === this.selectedWarehouse)
              .map(item => ({Warehosue: item.Warehouse, Name: item.Name}));
        }
      }
      return this.tableData.map(item => ({Warehouse: item.Warehouse, Name: item.Name}));
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
    selectMonth(monthIsSelected){
      this.selectedMonth = monthIsSelected;
      this.createChart();
    },

    getMonthName(monthI){
      return this.allMonths[monthI]
    },


    createChart() {
      if (this.saveChart) {
        this.saveChart.destroy();
      }

      const chartWidth = this.chartWidth;
      const chartHeight = this.chartHeight;

      const colorLegend = [
          'rgba(199, 208, 44, 1)',
          'rgba(91, 46, 24, 1)',
          '#000000FF'
      ];

      const dataBasedOnTheMonth = this.tableData.filter(item => (!this.selectedWarehouse || item.Warehouse === this.selectedWarehouse) && item.Month === this.selectedMonth);
      const nameLegend = [...new Set(dataBasedOnTheMonth.map(item => item.Name))];

      const datasets = nameLegend.map((name, index) => {
            const qdata = dataBasedOnTheMonth
                .filter(item => item.Name === name)
                .map(item => ({
                      x: `${item.Date}/${this.allMonths.indexOf(item.Month) + 1}`,
                      y: item.Quantity
                    })
                );

        // const backgroundColor = colorLegend[index % colorLegend.length];

          return {
            label: name,
            backgroundColor: colorLegend[index % colorLegend.length],
            borderColor: colorLegend[index % colorLegend.length],
            data: qdata,

          };
        });

      const currentDate = new Date();
      const dateLabels = Array.from({ length: 14 }, (_, index) => {
        const nextDate = new Date(currentDate);
        nextDate.setDate(currentDate.getDate() + index);
        const day = nextDate.getDate();
        const month = nextDate.getMonth() + 1;
        return `${day}/${month}`;
      });


      const chartData = {
        labels:  dateLabels,
        datasets: datasets,
      };


      const chartOptions = {
        legend: {display: false},
        title: {
          display: true,
          text: "Inventory Chart",
        },
        width: chartWidth,
        height: chartHeight,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
        plugins: {
          tooltip: {
            enabled: true,
            callbacks: {
              label: (context) => {
                const item = context.dataset.data[context.dataIndex];
                return `${context.dataset.label}: ${item.y}`;
              }
            }
          },
        },
        elements: {
          // bar: {
          //   borderRadius: 30,
          // },
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
