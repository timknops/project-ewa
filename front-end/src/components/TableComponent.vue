<template>
  <div class="card border-0 pt-4 pb-2 d-flex" :style="{ width: tableWidth }">
    <div class="card-body px-4 py-0 overflow-y-scroll">
      <div class="table-responsive" :style="{ height: calculateTableHeight }">
        <table class="table table-hover mb-0">
          <thead>
            <tr ref="headerRow">
              <th
                v-for="name in tableColumnNames"
                :key="name"
                scope="col"
                class="py-lg-3 pt-0 table-header-text px-3 px-lg-4"
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
    <div class="row w-100 px-4 align-self-center align-items-center pt-2">
      <div class="col d-flex">
        <p class="mb-0">
          1 to 6 <span class="items-listing">Items of</span> 15
        </p>
      </div>
      <div class="col d-flex justify-content-end">
        <button class="btn btn-link px-1 me-1 disabled" type="button">
          <svg
            class="svg-inline--fa fa-chevron-left me-2"
            aria-hidden="true"
            focusable="false"
            data-prefix="fas"
            data-icon="chevron-left"
            role="img"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 320 512"
            data-fa-i2svg=""
          >
            <path
              fill="currentColor"
              d="M224 480c-8.188 0-16.38-3.125-22.62-9.375l-192-192c-12.5-12.5-12.5-32.75 0-45.25l192-192c12.5-12.5 32.75-12.5 45.25 0s12.5 32.75 0 45.25L77.25 256l169.4 169.4c12.5 12.5 12.5 32.75 0 45.25C240.4 476.9 232.2 480 224 480z"
            ></path></svg
          >Previous
        </button>
        <button class="btn btn-link px-1 ms-1" type="button">
          Next<svg
            class="svg-inline--fa fa-chevron-right ms-2"
            aria-hidden="true"
            focusable="false"
            data-prefix="fas"
            data-icon="chevron-right"
            role="img"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 320 512"
            data-fa-i2svg=""
          >
            <path
              fill="currentColor"
              d="M96 480c-8.188 0-16.38-3.125-22.62-9.375c-12.5-12.5-12.5-32.75 0-45.25L242.8 256L73.38 86.63c-12.5-12.5-12.5-32.75 0-45.25s32.75-12.5 45.25 0l192 192c12.5 12.5 12.5 32.75 0 45.25l-192 192C112.4 476.9 104.2 480 96 480z"
            ></path>
          </svg>
        </button>
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
    /** The height of the table, can be any unit */
    tableHeight: String,
    /** Whether the first column should be text-bold or not. */
    boldFirstColumn: Boolean,
    /** The amount of rows you want to display at once. */
    amountToDisplay: Number,
    /** An array of objects which contain the data to be displayed in the table. */
    tableData: Array,
  },
  data() {
    return {
      /** The names of the columns that are displayed in the table header */
      tableColumnNames: Object.keys(this.tableData[0]),
      /** Height that the table rows height becomes smaller */
      TABLE_ROW_HEIGHT_BREAKPOINT_PX: 992,
    };
  },
  computed: {
    calculateTableHeight() {
      const rowHeightLarge = 57;
      const rowHeightSmall = 41;

      if (this.TABLE_ROW_HEIGHT_BREAKPOINT_PX < window.innerWidth) {
        return rowHeightLarge * this.amountToDisplay + rowHeightLarge + "px";
      }

      return rowHeightSmall * this.amountToDisplay + rowHeightSmall + "px";
    },
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

svg {
  height: 1em;
  margin-bottom: 2px;
}

p {
  font-size: 0.875rem !important;
  color: var(--bs-gray-900);
}

.items-listing {
  color: var(--bs-gray-600);
}

button {
  text-decoration: none;
  font-size: 0.875rem;
}
button:hover {
  text-decoration: underline;
}
</style>
