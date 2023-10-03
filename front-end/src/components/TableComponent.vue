<template>
  <div class="card border-0" :style="{ width: tableWidth }">
    <div class="card-body">
      <div class="table-responsive">
        <table class="table table-hover mb-0">
          <thead>
            <tr>
              <th
                v-for="columnName in Object.keys(tableData[0])"
                :key="columnName"
                scope="col"
                class="py-3"
              >
                {{ columnName }}
              </th>
            </tr>
          </thead>

          <!-- If first row is set to true, then use the first version of the tbody, else use the other. -->
          <tbody v-if="highlightFirstRow">
            <tr v-for="tableRow in tableData" :key="tableRow">
              <th scope="row" class="py-3">
                {{ Object.values(tableRow)[0] }}
              </th>
              <td
                v-for="i in Object.values(tableRow).length - 1"
                :key="Object.values(tableRow)[i]"
                class="py-3"
              >
                {{ Object.values(tableRow)[i] }}
              </td>
            </tr>
          </tbody>

          <tbody v-else>
            <tr v-for="tableRow in tableData" :key="tableRow">
              <td
                v-for="fieldData in Object.values(tableRow)"
                :key="fieldData"
                class="py-3"
              >
                {{ fieldData }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TableComponent",
  props: {
    tableWidth: String,
    highlightFirstRow: Boolean,
  },
  data() {
    return {
      tableData: [
        { id: 1451, name: "name1", quantity: 4343 },
        { id: 222, name: "name2", quantity: 22 },
        { id: 123412, name: "name3", quantity: 14 },
        { id: 2, name: "name4", quantity: 14212 },
      ],
    };
  },
  created() {
    console.log(Object.values(this.tableData[0]).length);
  },
};
</script>

<style scoped>
.card {
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05),
    0 4px 6px -4px rgb(0 0 0 / 0.05);
  border-radius: 0.5rem;
}
</style>
