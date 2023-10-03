<template>
  <div class="card border-0" :style="{ width: tableWidth }">
    <div class="card-body p-4">
      <div class="table-responsive">
        <table class="table table-hover mb-0">
          <thead>
            <tr>
              <th
                v-for="name in tableColumnNames"
                :key="name"
                scope="col"
                class="py-3 table-header-text px-4"
              >
                {{ name.toUpperCase() }}
              </th>
            </tr>
          </thead>

          <!-- If first row is set to true, then use the first version of the tbody, else use the other. -->
          <tbody v-if="boldFirstRow">
            <tr v-for="tableRow in tableData" :key="tableRow">
              <th scope="row" class="py-3 table-text px-4">
                {{ Object.values(tableRow)[0] }}
              </th>
              <td
                v-for="i in Object.values(tableRow).length - 1"
                :key="Object.values(tableRow)[i]"
                class="py-3 table-text px-4"
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
                class="py-3 table-text px-4"
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
    boldFirstRow: Boolean,
    tableData: Array,
  },
  data() {
    return {
      tableColumnNames: [],
    };
  },
  created() {
    this.tableColumnNames = Object.keys(this.tableData[0]);
  },
};
</script>

<style scoped>
.card {
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.05),
    0 4px 6px -4px rgb(0 0 0 / 0.05);
  border-radius: 0.5rem;
}

table td,
table th {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.table-text {
  color: var(--bs-gray-700);
}

.table-header-text {
  color: var(--bs-gray-900);
}
</style>
