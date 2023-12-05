<template>
  <div>

    <!--Dropdown-->
    <div class="btn-group dropdown-color">
      <button class="btn dropdown-toggle" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        {{ selectedWarehouse }}
      </button>

      <div class="dropdown-menu">
        <div class="dropdown-item" :key="null" @click="warehouseSelect('All warehouses')">All warehouses</div>
        <div class="dropdown-item" v-for="warehouse in warehouses" :key="warehouse.name"
             @click="warehouseSelect(warehouse.name)">{{warehouse.name}}</div>
      </div>
    </div>

    <!--Inventory changes information-->
    <TableComponent
        :amountToDisplay="3"
        :tableWidth="'100%'"
        :boldFirstColumn="true"
        :tableData="selectedInventoryChanges"
        :arrayAmountToDisplay="10"
        table-title="Inventory changes"
        sub-title="Upcoming inventory changes of the selected warehouse"
    />


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

      <!--Project information-->
      <TableComponent
        class="user-table-overview-right"
        tableWidth="60%"
        :boldFirstColumn="true"
        :amountToDisplay="4"
        :tableData="selectedProjectData"
        table-title="Projects"
          sub-title="Upcoming projects"
      >
      </TableComponent>
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
  inject: ["warehouseService"],
  data() {
    return {
      warehouses: [], // array for dropdown

      forecastItems: [{
        date: String,
        warehouseName: String,
        productName: String,
        expectedChange: Number,
      }],

      seperatedForecastItems: [], // array to fill with seperated forecast items per warehouse
      combinedForecastData: [],

      currentInventoryStatus: [],

      projects: [{
        name: String,
        dueDate: String,
        status: String,
      }],

      selectedWarehouse: "All warehouses",

      selectedWarehouseChart: null,
      chartDataList: [],
      saveChart: null,
      chartWidth: 200,
      chartHeight: 80,
      barColors: ["rgba(91, 46, 24, 1)"],
    };
  },

  async created() {
    this.warehouses = await this.warehouseService.findAll();

    this.forecastItems = [
      {
        date: "03-12-2023",
        inventoryChange: 20,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      },
      {
        date: "03-12-2023",
        inventoryChange: -10,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      },
      {
        date: "03-12-2023",
        inventoryChange: 10,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      },
      {
        date: "04-12-2023",
        inventoryChange: 20,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      },
      {
        date: "04-12-2023",
        inventoryChange: 20,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      },
      {
        date: "04-12-2023",
        inventoryChange: -60,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+"
      },
      {
        date: "05-12-2023",
        inventoryChange: 15,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+"
      },
      {
        date: "06-12-2023",
        inventoryChange: 10,
        warehouseName : "Solar Sedum",
        productName : "Enphase IQ8+ omvormer"
      }
    ];

    this.projects = [
      {
        warehouse: "Superzon",
        name: "Plaatsing Amstelveen",
        dueDate: "03-12-2023"
      },
      {
        warehouse: "Superzon",
        name: "Plaatsing Amstelveen",
        dueDate: "07-12-2023"
      },
      {
        warehouse: "Solar Sedum",
        name: "Plaatsing Amstelveen",
        dueDate: "08-12-2023"
      }
    ];

    this.currentInventoryStatus = [
      {
        warehouse: "Solar Sedum",
        productName: "Enphase IQ8+ omvormer",
        currentStock: 30,
        minimumStock: 10
      },
      {
        warehouse: "Superzon",
        productName: "Enphase IQ8+ omvormer",
        currentStock: 40,
        minimumStock: 10
      }
    ];

    this.separateForecastData()

    this.formatChartData()

    this.createChart()
  },

  // mounted() {
  //   this.createChart(this.forecastItems);
  // },

  computed: {
    selectedInventoryChanges(){
      if(this.selectedWarehouse === "All warehouses") {
        return this.forecastItems;
      }
      else if (this.seperatedForecastItems[this.selectedWarehouse]) {
        return this.seperatedForecastItems[this.selectedWarehouse];
      }
      else {
        return [];
      }
    },

    selectedProjectData(){
      if(this.selectedWarehouse === "All warehouses") {
        return this.projects;
      }
      else {
        return this.projects.filter((item) => item.warehouse === this.selectedWarehouse)
      }
    },
  },

  methods: {
    separateForecastData(){
      let data = this.forecastItems;

      const seperatedForecastItems = {};

      seperatedForecastItems["All warehouses"] = this.forecastItems;

      data.forEach((item) => {
        const warehouseName = item.warehouseName

        if (!seperatedForecastItems[warehouseName]){
          seperatedForecastItems[warehouseName] = [];
        }

        seperatedForecastItems[warehouseName].push(item);
      })

      this.seperatedForecastItems = seperatedForecastItems;
    },

    formatChartData() {
      let originalList;
      if (this.seperatedForecastItems[this.selectedWarehouse]){
        originalList = this.seperatedForecastItems[this.selectedWarehouse]
      }
      else {
        originalList = []
      }

      if (this.selectedWarehouse === "All warehouses"){
        this.combinedForecastData = this.formatChartDataAllWarehouses(originalList)
      } else {
        this.combinedForecastData = this.formatChartDataWarehouseSpecific(originalList);
      }
    },

    formatChartDataWarehouseSpecific(originalList) {
      const combinedMap = new Map();
      const combinedList = [];
      console.log(originalList)

      // Iterate through the original list
      originalList.forEach(item => {
        const key = `${item.date}/${item.warehouseName}/${item.productName}`;
        // Check if the key already exists in the map
        if (combinedMap.has(key)) {
          // If exists, update the inventoryChange value
          combinedMap.set(key, combinedMap.get(key) + item.inventoryChange);
        } else {
          // If not exists, add a new entry in the map
          combinedMap.set(key, item.inventoryChange);
        }
      });

      // Voeg currentStock van een product uit currentInventoryStatus toe aan die in de combined map en hou dat bij voor de volgende,
      // waar het warehouse uit currentInventoryStatus overeenkomt met de warehouseName in de originalList
      // en de productName uit currentInventoryStatus overeenkomt met de productName in de originalList

      // Convert the map back to an array of objects
      combinedMap.forEach((inventoryChange, key) => {
        const [date, warehouseName, productName] = key.split('/');
        const combinedItem = { date, warehouseName, productName, inventoryChange };
        combinedList.push(combinedItem);
      });

      return combinedList;
    },

    formatChartDataAllWarehouses(originalList) {
      const combinedMap = new Map();
      const combinedList = [];

      // Iterate through the original list
      originalList.forEach(item => {
        const key = `${item.date}/${item.productName}`;
        // Check if the key already exists in the map
        if (combinedMap.has(key)) {
          // If exists, update the inventoryChange value
          combinedMap.set(key, combinedMap.get(key) + item.inventoryChange);
        } else {
          // If not exists, add a new entry in the map
          combinedMap.set(key, item.inventoryChange);
        }
      });

      // Convert the map back to an array of objects
      combinedMap.forEach((inventoryChange, key) => {
        const [date, productName] = key.split('/');
        const combinedItem = { date, productName, inventoryChange };
        combinedList.push(combinedItem);
      });

      return combinedList;
    },

    createChart() {
      if (this.saveChart) {
        this.saveChart.destroy();
      }

      const data = this.combinedForecastData;

      const chartWidth = this.chartWidth;
      const chartHeight = this.chartHeight;

      const chartData = {
        labels: Array.from(new Set(data.map(item => item.date))),
        datasets: Array.from(new Set(data.map(item => item.productName))).map(productName => ({
          label: productName,
          data: data.filter(item => item.productName === productName).map(item => item.inventoryChange),
          fill: false,
          borderColor: this.getRandomColor(), // You can use a function to generate different colors
        })),
      };

      const chartOptions = {
        legend: {display: false},
        title: {
          display: true,
          text: "Forecast Chart",
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
          },
        },
        elements: {
          bar: {
            borderRadius: 30,
          },
        },
      };

     this.saveChart = new Chart(this.$refs.combinedChart, {
        type: "line",
        data: chartData,
        options: chartOptions,
      });

    },

    getRandomColor() {
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }

      return color;
    },

    warehouseSelect(nameOfTheWarehouse){
      this.selectedWarehouse = nameOfTheWarehouse;
      this.formatChartData()
      this.createChart();
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
</style>
