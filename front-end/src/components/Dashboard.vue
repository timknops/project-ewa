<template>
  <div>
    <h2 class="mb-4">Dashboard</h2>


    <TableComponent
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :amountToDisplay="2"
        :tableData="tableData"
        :arrayAmountToDisplay="10"
    >
    </TableComponent>

    <div>
      <canvas ref="myChart" class="my-chart" ></canvas>
    </div>


  </div>
</template>


<script>
// Import the TableComponent
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

      chart: null,
      chartWidth: 50,
      chartHeight: 40,
      xValues: ["This week", "Expected"],
      yValues: [55, 49],
      barColors: ["brown", "Yellow"],
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
          responsive: false,
         // maintainAspectRatio: true,
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
  /*background-color: white;*/
  box-shadow: var(--custom-box-shadow);

}

</style>
