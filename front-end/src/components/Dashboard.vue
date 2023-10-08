<template>
  <div>
    <h2 class="mb-4">Dashboard</h2>

    <!--Inventory-->
    <strong class="table-title">Current inventory of my warehouse</strong>
    <TableComponent
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :amountToDisplay="3"
        :tableData="tableData"
        :arrayAmountToDisplay="10"

    >
    </TableComponent>

    <div class="table-container">
      <!--Forecast-->
      <div class="user-table-overview-left">
        <div class="table-container">
<!--          <div v-for="(item, index) in tableData" :key="index" class="forecast-chart">-->
<!--            <canvas  :ref="'myChart_' + index" style="width:600px; height:300px" class="my-chart"></canvas>-->
          <canvas  ref="combinedChart" style="width:600px; height:300px" class="my-chart"></canvas>
          </div>
        </div>


      <!--User information-->
      <TableComponent class="user-table-overview-right"
                      :tableWidth="'40%'"
                      :boldFirstColumn="true"
                      :amountToDisplay="2"
                      :tableData="userData"
                      :arrayAmountToDisplay="10"
      >
      </TableComponent>
    </div>

  </div>

</template>


<script>

import TableComponent from "@/components/TableComponent.vue";
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
          Name: "enphase",
          Quantity: 9,
          Expected: 32,
        },
        {
          Name: "Gateway",
          Quantity: 18,
          Expected: 30,
        },
        {
          Name: "MB 385 (white)",
          Quantity: 18,
          Expected: 30,
        },
        {
          Name: "DENIM 405 black",
          Quantity: 18,
          Expected: 45,
        },

      ],
      userData: [
        {
          Username: "hx",
          Warehouse: "Solar clarity",
        },
      ],
      forecastData: [
        {
          Forecast: "",

        },
      ],

      chartDataList: [],
  //    chart: null,
      chartWidth: 200,
      chartHeight: 80,
     // xValues: ["This week", "Expected"],
      //yValues: [0, 36],
      barColors: ["rgba(91, 46, 24, 1)"],

    };
  },

  mounted() {
    this.createChart();
  },


  methods: {
    createChart() {
      const chartWidth = this.chartWidth;
      const chartHeight = this.chartHeight;

      const labels = this.tableData.map((item) => item.Name);
      const data = this.tableData.map((item) => item.Quantity);
      const expectedData = this.tableData.map((item) => item.Expected);

      data.push(50);

      const chartData = {
        labels: labels,
        datasets: [
          {
            label: "Quantity",
            backgroundColor: this.barColors[0], // Use the first color for Quantity
            data: data,
          },
          {
            label: "Expected",
            backgroundColor: "rgba(199, 208, 44, 1)", // Use the specified color for Expected
            data: expectedData,
          },
        ],
      };

      const chartOptions = {
        legend: { display: false },
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

      new Chart(this.$refs.combinedChart, {
        type: "bar",
        data: chartData,
        options: chartOptions,
      });
    },
  },







  // watch: {
  //   xValues: "createChart",
  //   yValues: "createChart",
  // },



};
</script>


<style scoped>
h2 {
  color: var(--color-primary);
}

.my-chart {
  width: 100%;
  height: auto;
  /*box-shadow: var(--custom-box-shadow);*/

}

.table-container {
  display: flex;
  justify-content: space-between;

}

.user-table-overview-right {
  margin: 5px;
  margin-top: 50px;
}

.user-table-overview-left {
  margin: 5px;
  margin-top: 50px;
}


</style>
