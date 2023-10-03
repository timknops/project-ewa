<template>
  <div
    class="card border-0"
    :style="{ width: tableWidth, 'max-height': tableMaxHeight }"
  >
    <div class="card-body p-4 overflow-y-scroll">
      <div class="table-responsive">
        <table class="table table-hover mb-0">
          <thead>
            <tr>
              <th
                v-for="name in tableColumnNames"
                :key="name"
                scope="col"
                class="py-lg-3 table-header-text px-3 px-lg-4"
              >
                {{ name.toUpperCase() }}
              </th>
            </tr>
          </thead>

          <!-- If bold first row is set to true, then use the first version of the tbody, else use the other. -->
          <tbody v-if="boldFirstColumn">
            <tr v-for="tableRow in tableData" :key="tableRow">
              <!-- Makes the first column of the row bold. -->
              <th scope="row" class="py-lg-3 table-text px-3 px-lg-4">
                {{ Object.values(tableRow)[0] }}
              </th>

              <!-- Loops through the rest of the object values, except for the first value. -->
              <td
                v-for="i in Object.values(tableRow).length - 1"
                :key="Object.values(tableRow)[i]"
                class="py-lg-3 table-text px-3 px-lg-4"
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
                class="py-lg-3 table-text px-3 px-lg-4"
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
/**
 * Custom table component. Allows user to dynamically set the width, height, whether the first row should be bold and
 * allows for dynamic column creation depending on the array of object passed as tableData.
 *
 * @author Tim Knops
 */
export default {
  name: "TableComponent",
  props: {
    /** Width of the table, can be any unit. */
    tableWidth: String,
    /** The max height of the table, will become scrollable when max height is hit. Can be any unit */
    tableMaxHeight: String,
    /** Whether the first column should be text-bold or not. */
    boldFirstColumn: Boolean,
    /** An array of objects which contain the data to be displayed in the table. */
    tableData: Array,
  },
  data() {
    return {
      /** The names of the columns that are displayed in the table header */
      tableColumnNames: Object.keys(this.tableData[0]),
    };
  },
};
</script>

<style scoped>
.card {
  box-shadow: var(--custom-box-shadow);
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
