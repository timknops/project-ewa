<template>
  <div>
    <h2 class="mb-4">Dashboard</h2>

    <!--Inventory-->
    <strong class="table-title">Current inventory of my warehouse</strong>
    <TableComponent
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :amountToDisplay="2"
        :tableData="tableData"
        :arrayAmountToDisplay="10"
    >
    </TableComponent>

    <div class="table-container">
      <!--Forecast-->
      <div class="user-table-overview-left">
        <div class="table-container">
          <div class="forecast-chart">
            <canvas ref="myChart" style="width:600px; height:300px" class="my-chart"></canvas>
          </div>
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
        },
        {
          Name: "Gateway",
          Quantity: 18,
        },

      ],
      userData: [
        {
          Username: "hx",
          Warehouse: "Solar",
        },
      ],
      forecastData: [
        {
          Forecast: "",

        },
      ],

      chart: null,
      chartWidth: 200,
      chartHeight: 80,
      xValues: ["This week", "Expected"],
      yValues: [55, 49],
      barColors: ["rgba(91, 46, 24, 1)", "rgba(199, 208, 44, 1)"],

    };
  },

  mounted() {
    this.createChart();
  },


  methods: {
    createChart() {
      if (this.chart) {
        this.chart.destroy();
      }


      const chartWidth = this.chartWidth;
      const chartHeight = this.chartHeight;

      this.chart = new Chart(this.$refs.myChart, {
        type: "bar",
        data: {
          labels: this.xValues,
          datasets: [{
            backgroundColor: this.barColors,
            // fillColor: ,
            data: this.yValues,
          },
          ],
        },
        options: {
          legend: {display: false},
          title: {
            display: true,
            text: "World Wine Production 2018",
          },

          width: chartWidth,
          height: chartHeight,

        },
      });
    },
  },
  watch: {
    xValues: "createChart",
    yValues: "createChart",
  },


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
