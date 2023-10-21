<template>
  <div>
    <!--Inventory-->
    <TableComponent
      :tableWidth="'100%'"
      :boldFirstColumn="true"
      :amountToDisplay="3"
      :tableData="tableData"
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

      <TableComponent
        class="user-table-overview-right"
        tableWidth="60%"
        :boldFirstColumn="true"
        :amountToDisplay="4"
        :tableData="userData"
        table-title="Users"
        sub-title="Current active users"
      >
      </TableComponent>
    </div>
    <!--User information-->
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
            backgroundColor: this.barColors[0],
            data: data,
          },
          {
            label: "Expected",
            backgroundColor: "rgba(199, 208, 44, 1)",
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
</style>
