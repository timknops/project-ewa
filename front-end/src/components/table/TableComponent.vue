<template>
  <div class="mb-3" v-if="hasSearchBar">
    <input
        v-model="searchQuery"
        type="text"
        class="form-control"
        placeholder="Search..."
        @input="handleSearch"
    />
  </div>
  <div class="card border-0 pt-4 pb-2 d-flex" :style="{ width: tableWidth }">
    <div class="card-body px-4 py-0 overflow-hidden">
      <!-- Both the title and the subtitle are optional props! They will not be displayed when not specified -->
      <h5
        v-if="tableTitle !== undefined"
        class="fw-bold table-title ps-2 mb-0"
        :class="{ 'mb-2': subTitle === undefined }"
      >
        {{ tableTitle }}
      </h5>
      <p v-if="subTitle !== undefined" class="ps-2 subtitle mb-2">
        {{ subTitle }}
      </p>
      <div class="table-responsive" :style="{ height: calculateTableHeight }">
        <table class="table table-hover mb-0">
          <thead>
            <TableHeaderRow
              :table-column-names="tableColumnNames"
              :has-add-button="hasAddButton"
              :hide-id-column="hideIdColumn"
              :sort-direction-all-columns="sortDirectionAllColumns"
              :previous-sorted-column="previousSortedColumn"
              @sort="sortByColumn($event)"
              @add="$emit('add')"
            />
          </thead>

          <!-- If bold first row is set to true, then use the first version of the tbody, else use the other. -->
          <tbody v-if="boldFirstColumn && !showEmptyTable">
            <tr
              v-for="tableRow in currentlyDisplayedData"
              :key="tableRow"
              @mouseenter="mouseEnter"
              @mouseleave="mouseLeave"
            >
              <!-- Makes the first column of the row bold. -->
              <th
                v-if="!hideIdColumn"
                scope="row"
                class="py-3 table-text px-3 px-lg-4"
              >
                {{ Object.values(tableRow)[0] }}
              </th>

              <!-- Loops through the rest of the object values, except for the first. -->
              <template
                v-for="i in Object.values(tableRow).length - 1"
                :key="Object.values(tableRow)[i]"
              >
                <!-- If the field data is of type array -->
                <td
                  v-if="Array.isArray(Object.values(tableRow)[i])"
                  class="py-3 px-3 px-lg-4 array-display"
                >
                  <span
                    v-for="j in this.arrayAmountToDisplay"
                    :key="Object.values(tableRow)[i][j - 1]"
                    class="badge array-badge me-1"
                    >{{ Object.values(tableRow)[i][j - 1] }}</span
                  >
                </td>

                <!-- If the column name is 'status' -->
                <td
                  v-else-if="Object.keys(tableRow)[i] === 'status'"
                  class="py-3 px-3 px-lg-4"
                >
                  <span
                    class="badge"
                    :class="
                      STATUS_OPTIONS[Object.values(tableRow)[i].toUpperCase()]
                    "
                    >{{ Object.values(tableRow)[i].toUpperCase() }}</span
                  >
                </td>

                <td v-else class="py-3 table-text px-3 px-lg-4">
                  {{ Object.values(tableRow)[i] }}
                </td>
              </template>

              <!-- If the table has edit and delete buttons -->
              <TableButtons
                v-if="hasEditButton || hasDeleteButton"
                :table-row="tableRow"
                :row-height-large="ROW_HEIGHT_LARGE"
                :has-edit-button="hasEditButton"
                :has-delete-button="hasDeleteButton"
                :has-specific-button="hasSpecificButton"
                @edit="$emit('edit', tableRow)"
                @delete="$emit('delete', tableRow)"
                @specific="$emit('specific', tableRow)"
              />
            </tr>
          </tbody>

          <tbody v-else-if="!showEmptyTable">
            <tr
              v-for="tableRow in currentlyDisplayedData"
              :key="tableRow"
              class="position-relative"
              @mouseenter="mouseEnter"
              @mouseleave="mouseLeave"
            >
              <template
                v-for="(fieldData, index) in Object.values(tableRow)"
                :key="fieldData"
              >
                <!-- If the field data is of type array -->
                <td
                  v-if="Array.isArray(fieldData)"
                  class="py-3 px-3 px-lg-4 array-display"
                >
                  <span
                    v-for="i in this.arrayAmountToDisplay"
                    :key="fieldData[i - 1]"
                    class="badge array-badge me-1"
                    >{{ fieldData[i - 1] }}</span
                  >
                </td>

                <!-- If the column name is 'status' -->
                <td
                  v-else-if="
                    fieldData === 'COMPLETED' ||
                    fieldData === 'IN_PROGRESS' ||
                    fieldData === 'UPCOMING'
                  "
                  class="py-3 px-3 px-lg-4"
                >
                  <span
                    class="badge"
                    :class="STATUS_OPTIONS[fieldData.toUpperCase()]"
                    >{{ fieldData.toUpperCase().replace("_", " ") }}</span
                  >
                </td>

                <!-- If the column name is 'id' and the hideIdColumn prop is set to true, then hide the column. -->
                <td
                  v-else-if="
                    !hideIdColumn || Object.keys(tableRow)[index] !== 'id'
                  "
                  class="py-3 table-text px-3 px-lg-4"
                >
                  {{ fieldData }}
                </td>
              </template>

              <!-- If the table has edit and delete buttons -->
              <TableButtons
                v-if="hasEditButton || hasDeleteButton"
                :table-row="tableRow"
                :row-height-large="ROW_HEIGHT_LARGE"
                :has-edit-button="hasEditButton"
                :has-delete-button="hasDeleteButton"
                :has-specific-button="hasSpecificButton"
                @edit="$emit('edit', tableRow)"
                @delete="$emit('delete', tableRow)"
                @specific="$emit('specific', tableRow)"
              />
            </tr>
          </tbody>

          <!-- If the table data is empty, show an empty table. -->
          <tbody v-else-if="showEmptyTable">
            <tr>
              <td
                class="py-3 px-3 px-lg-4 text-center empty-text"
                colspan="100%"
              >
                No data available
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <TableFooter
      :table-data="tableDataSorted"
      :amount-to-display="amountToDisplay"
      :display-amount="savedAmountToDisplay"
      :current-start-index="currentStartIndex"
      :current-end-index="displayEndIndex()"
      @view-all="viewAllItems"
      @view-less="viewLessItems"
      @previous="handlePreviousButton"
      @next="handleNextButton"
    />
  </div>
</template>

<script>
import TableButtons from "@/components/table/TableButtons.vue";
import TableFooter from "@/components/table/TableFooter.vue";
import TableHeaderRow from "@/components/table/TableHeaderRow.vue";

/**
 * Custom table component. Allows user to dynamically set the width, height, whether the first row should be bold and
 * allows for dynamic column creation depending on the array of object passed as tableData.
 *
 * @param {String} tableWidth The width of the table.
 * @param {Boolean} boldFirstColumn Whether the first column should be bold or not.
 * @param {Number} amountToDisplay The amount of rows you want to display at once.
 * @param {Array} tableData The data that is to be displayed in the table.
 * @param {Number} arrayAmountToDisplay When the table data contains an array, this is the amount of items that are displayed.
 * @param {String} tableTitle The title of the table.
 * @param {String} subTitle The subtitle of the table.
 * @param {Boolean} hasEditButton Whether the table has an edit button or not.
 * @param {Boolean} hasDeleteButton Whether the table has a delete button or not.
 * @param {Boolean} hasAddButton Whether the table has an add button or not.
 * @param {Boolean} hasSpecificButton Whether the table has a specific button or not.
 * @param {Boolean} hideIdColumn Whether the ID column should be hidden or not.
 *
 * @author Tim Knops
 */
export default {
  name: "TableComponent",
  components: {
    TableHeaderRow,
    TableFooter,
    TableButtons,
  },
  props: {
    tableWidth: String,
    boldFirstColumn: Boolean,
    amountToDisplay: {
      type: Number,
      required: true,
    },
    tableData: {
      type: Array,
      required: true,
    },
    arrayAmountToDisplay: Number,
    tableTitle: String,
    subTitle: String,
    hasEditButton: Boolean,
    hasDeleteButton: Boolean,
    hasAddButton: Boolean,
    hasSpecificButton: Boolean,
    hideIdColumn: Boolean,
    hasSearchBar: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      /** The names of the columns that are displayed in the table header. */
      tableColumnNames: Object.keys(this.tableData[0]),
      /** The data that is currently displayed in the table. */
      currentlyDisplayedData: [],
      /** The current index of the starting item that is displayed. */
      currentStartIndex: 0,
      /** The last index of the item that displayed */
      currentEndIndex: this.amountToDisplay,
      /** The amount that is to be displayed at once, saved in data because it is manipulated, unlike the other props. */
      displayAmount: this.amountToDisplay,
      /** Saves the amount that is supposed to be displayed, used in the 'view all' and 'view less'. */
      savedAmountToDisplay: 0,
      /** The height of a single row in the table. */
      ROW_HEIGHT_LARGE: 57,
      /** The options for the status badge. This will be used if you have a column with the name "status".
       * Feel free to add your own additional status's as needed. */
      STATUS_OPTIONS: Object.freeze({
        COMPLETED: "success-badge",
        IN_PROGRESS: "in-progress-badge",
        UPCOMING: "upcoming-badge",
      }),
      /** shallow Copy of the table data, used for sorting. */
      tableDataSorted: this.tableData,
      /** The name of the previous sorted column, used for sorting. */
      previousSortedColumn: "",
      /** The options for the sorting icons. */
      SORT_DIRECTION_OPTIONS: {
        DEFAULT: "default",
        ASCENDING: "ascending",
        DESCENDING: "descending",
      },
      /** The current sort direction of all columns. */
      sortDirectionAllColumns: "default",
      /** The reference to the previous column icon that was sorted. */
      previousColumnIconRef: "",
      /** Whether the table should show an empty table or not. */
      showEmptyTable: false,
      /** The search query for filtering table data. */
      searchQuery: "",
    };
  },
  created() {
    this.updateDisplayedData();
  },
  methods: {
    /** Handles the search functionality for the table. */
    handleSearch() {
      const query = this.searchQuery.toLowerCase().trim();
      this.currentlyDisplayedData = this.tableDataSorted.filter((row) =>
          this.rowContainsSearchQuery(row, query)
      );
    },

    /** Checks if a table row contains the search query. */
    rowContainsSearchQuery(row, query) {
      const rowValues = Object.values(row);
      return rowValues.some((value) =>
          value.toString().toLowerCase().includes(query)
      );
    },

    /** Whenever the next button is pressed, the data is updated to show the next items depending on the display amount. */
    handleNextButton() {
      this.currentStartIndex = this.currentEndIndex;
      this.currentEndIndex += this.displayAmount;
      this.updateDisplayedData();
    },

    /** Whenever the previous button is pressed, the data is updated to show the previous items depending on the display
     * amount and the indexes.
     */
    handlePreviousButton() {
      this.currentEndIndex = this.currentStartIndex;
      this.currentStartIndex -= this.displayAmount;
      this.updateDisplayedData();
    },

    /** Updates the displayed data depending on the current start and end index. */
    updateDisplayedData() {
      // If the tableData has empty object values, show an empty table.
      if (
        typeof Object.values(this.tableData[0])[0] === "function" ||
        Object.values(this.tableData[0])[0] === ""
      ) {
        this.showEmptyTable = true;

        return;
      }

      this.showEmptyTable = false;

      this.tableDataSorted = this.tableData;
      this.currentlyDisplayedData = this.tableDataSorted.slice(
        this.currentStartIndex,
        this.currentEndIndex
      );

      if (this.currentlyDisplayedData.length === 0) {
        this.handlePreviousButton();
      }
    },

    /** Ensures that the ending index is the right number in the bottom left range display. */
    displayEndIndex() {
      if (this.currentEndIndex >= this.tableData.length) {
        return this.tableData.length;
      }

      return this.currentEndIndex;
    },

    /** Whenever the view all button is pressed, data gets changed and the view is updated to display all items. */
    viewAllItems() {
      this.savedAmountToDisplay = this.tableData.length;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.tableData.length;
      this.updateDisplayedData();
    },

    /** The opposite of the view all, revert back to the original. */
    viewLessItems() {
      this.savedAmountToDisplay = 0;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.displayAmount;
      this.updateDisplayedData();
    },

    /** Whenever the mouse enters a row, the edit and delete buttons are displayed. */
    mouseEnter(e) {
      e.target.lastElementChild.classList.add("d-md-block");
    },

    /** Whenever the mouse leaves a row, the edit and delete buttons are hidden. */
    mouseLeave(e) {
      e.target.lastElementChild.classList.remove("d-md-block");
    },

    /** Sorts the table data by the column name that is passed as a parameter. */
    sortByColumn(clickedColumnInfo) {
      const columnName = clickedColumnInfo.columnName;
      const columnIconRef = clickedColumnInfo.columnIconRef;

      // If the previous sorted column is the same as the current one, reverse the array.
      if (this.previousSortedColumn === columnName) {
        // If the current sort direction is descending, set it to ascending and vice versa.
        if (
          columnIconRef.currentSortDirection ===
          this.SORT_DIRECTION_OPTIONS.DESCENDING
        ) {
          columnIconRef.currentSortDirection =
            this.SORT_DIRECTION_OPTIONS.ASCENDING;
        } else {
          columnIconRef.currentSortDirection =
            this.SORT_DIRECTION_OPTIONS.DESCENDING;
        }

        this.tableDataSorted.reverse();
        this.updateDisplayedData();

        return;
      }

      // Sort the array by the column name.
      this.tableDataSorted.sort((a, b) => {
        if (a[columnName] < b[columnName]) {
          return -1;
        }

        if (a[columnName] > b[columnName]) {
          return 1;
        }

        return 0;
      });

      // Set the previous sorted column icons to default.
      if (this.previousSortedColumn !== "") {
        // If the previous sorted column is not empty.

        this.previousColumnIconRef.currentSortDirection =
          this.SORT_DIRECTION_OPTIONS.DEFAULT;
      }

      // Set the sorting icon for the column that was clicked to descending.
      columnIconRef.currentSortDirection =
        this.SORT_DIRECTION_OPTIONS.DESCENDING;

      // Update the displayed data and save the previous sorted column.
      this.previousSortedColumn = columnName;
      this.previousColumnIconRef = clickedColumnInfo.columnIconRef;
      this.updateDisplayedData();
    },
  },
  computed: {
    /** Calculates the table height depending on the amount of items to be displayed at once. */
    calculateTableHeight() {
      const TABLE_HEADER_HEIGHT = 63;
      return (
        this.ROW_HEIGHT_LARGE * this.displayAmount + TABLE_HEADER_HEIGHT + "px"
      );
    },

    // Updates if table data has changed
    tableDataWatcher() {
      return [...this.tableData];
    },
  },
  watch: {
    /** Whenever the table data changes, update the table. */
    tableDataWatcher() {
      if (this.tableData.length === 0) {
        // If the table data is empty, show an empty table.
        this.showEmptyTable = true;

        return;
      }

      this.showEmptyTable = false;
      this.tableColumnNames = Object.keys(this.tableData[0]);

      if (this.savedAmountToDisplay > this.displayAmount) {
        this.currentEndIndex = this.tableData.length;
      }
      this.updateDisplayedData();
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

.table-title {
  color: var(--bs-gray-900);
}

.subtitle {
  color: var(--bs-gray-600);
  font-size: 1rem !important;
}

p {
  font-size: 0.875rem !important;
  color: var(--bs-gray-900);
}

button:not(.btn-primary) {
  text-decoration: none;
  font-size: 0.875rem;
  color: var(--bs-gray-900);
  transition: none !important;
}

button:hover:not(.btn-primary) {
  text-decoration: underline;
  color: var(--color-primary);
}

button:active {
  color: var(--color-primary) !important;
}

.array-badge {
  background-color: var(--color-primary) !important;
}

.array-display {
  width: 1%;
  white-space: nowrap !important;
}

.success-badge {
  color: #15803d;
  background-color: #bbf7d0;
}

.in-progress-badge {
  color: #a16207;
  background-color: #fef08a;
}

.upcoming-badge {
  color: #1d4ed8;
  background-color: #bfdbfe;
}

.empty-text {
  color: var(--bs-gray-700);
}
</style>
