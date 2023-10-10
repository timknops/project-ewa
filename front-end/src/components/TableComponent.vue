<template>
  <div class="card border-0 pt-4 pb-2 d-flex" :style="{ width: tableWidth }">
    <div class="card-body px-4 py-0 overflow-hidden">
      <!-- Both the title and the subtitle are optional props! They will not be displayed when not specified -->
      <h5
        v-if="tableTitle !== undefined"
        class="fw-bold table-title ps-lg-4 ps-3 mb-0"
        :class="{ 'mb-2': subTitle === undefined }"
      >
        {{ tableTitle }}
      </h5>
      <p v-if="subTitle !== undefined" class="ps-lg-4 ps-3 subtitle mb-2">
        {{ subTitle }}
      </p>
      <div class="table-responsive" :style="{ height: calculateTableHeight }">
        <table class="table table-hover mb-0">
          <thead>
            <tr ref="headerRow">
              <th
                v-for="name in tableColumnNames"
                :key="name"
                scope="col"
                class="py-3 pt-2 table-header-text px-3 px-lg-4"
              >
                {{ name.toUpperCase() }}
              </th>
            </tr>
          </thead>

          <!-- If bold first row is set to true, then use the first version of the tbody, else use the other. -->
          <tbody v-if="boldFirstColumn">
            <tr v-for="tableRow in currentlyDisplayedData" :key="tableRow">
              <!-- Makes the first column of the row bold. -->
              <th scope="row" class="py-3 table-text px-3 px-lg-4">
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
                    class="badge me-1"
                    >{{ Object.values(tableRow)[i][j - 1] }}</span
                  >
                </td>

                <td v-else class="py-3 table-text px-3 px-lg-4">
                  {{ Object.values(tableRow)[i] }}
                </td>
              </template>
            </tr>
          </tbody>

          <tbody v-else>
            <tr v-for="tableRow in currentlyDisplayedData" :key="tableRow">
              <template
                v-for="fieldData in Object.values(tableRow)"
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
                    class="badge me-1"
                    >{{ fieldData[i - 1] }}</span
                  >
                </td>

                <td v-else class="py-3 table-text px-3 px-lg-4">
                  {{ fieldData }}
                </td>
              </template>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row w-100 px-4 align-self-center align-items-center pt-2">
      <div class="col-auto d-none d-md-flex align-items-center gap-2">
        <p class="mb-0">
          {{ this.currentStartIndex + 1 }} to {{ this.displayEndIndex() }}
          <span class="items-listing">Items of</span>
          {{ this.tableData.length }}
        </p>
        <!-- If the amount that is currently displayed is not equal to the entire table data length
        (meaning we're currently not in view all mode). And if the amount to be displayed that was passed as a prop
        isn't more than the total table length (there's no 'view all' when the amount to be displayed exceeds the amount
        in the table-data.) -->
        <button
          v-if="
            this.displayAmount !== this.tableData.length &&
            !(this.amountToDisplay >= this.tableData.length)
          "
          @click="viewAllItems()"
          class="btn btn-link px-1 ms-1"
          type="button"
        >
          View all<font-awesome-icon
            icon="fa-solid fa-chevron-right"
            class="ms-1"
          />
        </button>
        <button
          v-else-if="!(this.amountToDisplay >= this.tableData.length)"
          @click="viewLessItems()"
          class="btn btn-link px-1 ms-1"
          type="button"
        >
          View less
          <font-awesome-icon icon="fa-solid fa-chevron-right" class="ms-1" />
        </button>
      </div>
      <div class="col d-flex justify-content-center justify-content-md-end">
        <button
          @click="handlePreviousButton()"
          class="btn btn-link px-1 me-1"
          :class="{ disabled: this.currentStartIndex === 0 }"
          type="button"
        >
          <font-awesome-icon
            icon="fa-solid fa-chevron-left"
            class="me-1"
          />Previous
        </button>
        <button
          @click="handleNextButton()"
          class="btn btn-link px-1 ms-1"
          :class="{ disabled: this.currentEndIndex >= this.tableData.length }"
          type="button"
        >
          Next<font-awesome-icon
            icon="fa-solid fa-chevron-right"
            class="ms-1"
          />
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
    /** Whether the first column should be text-bold or not. */
    boldFirstColumn: Boolean,
    /** The amount of rows you want to display at once. */
    amountToDisplay: Number,
    /** An array of objects which contain the data to be displayed in the table. */
    tableData: Array,
    /** The amount of the array items you want to display whenever you want to have a td with array items. Can be unspecified. */
    arrayAmountToDisplay: Number,
    /** The title that you want to display within the table. OPTIONAL! */
    tableTitle: String,
    /** The subtitle that you want to display within the table. OPTIONAL */
    subTitle: String,
  },
  data() {
    return {
      /** The names of the columns that are displayed in the table header. */
      tableColumnNames: Object.keys(this.tableData[0]),
      /** Height that the table rows height becomes smaller. */
      currentlyDisplayedData: [],
      /** The current index of the starting item that is displayed. */
      currentStartIndex: 0,
      /** The last index of the item that displayed */
      currentEndIndex: this.amountToDisplay,
      /** The amount that is to be displayed at once, saved in data because it is manipulated, unlike the other props. */
      displayAmount: this.amountToDisplay,
      /** Saves the amount that is supposed to be displayed, used in the 'view all' and 'view less'. */
      savedAmountToDisplay: 0,
    };
  },
  methods: {
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

    /** Updates the currently displayed data by slicing the total table data which contain the start an ending index */
    updateDisplayedData() {
      this.currentlyDisplayedData = this.tableData.slice(
        this.currentStartIndex,
        this.currentEndIndex
      );
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
      this.savedAmountToDisplay = this.displayAmount;
      this.displayAmount = this.tableData.length;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.tableData.length;
      this.updateDisplayedData();
    },

    /** The opposite of the view all, revert back to the original. */
    viewLessItems() {
      this.displayAmount = this.savedAmountToDisplay;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.displayAmount;
      this.updateDisplayedData();
    },
  },
  created() {
    this.updateDisplayedData();
  },
  computed: {
    /** Calculates the table height depending on the amount of items to be displayed at once. */
    calculateTableHeight() {
      const rowHeightLarge = 57;

      return rowHeightLarge * this.displayAmount + rowHeightLarge + "px";
    },
  },
  watch: {
    tableData() {
      //reset display if tableData changes
      this.currentStartIndex = 0;
      this.currentEndIndex = this.amountToDisplay;
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

.table-header-text {
  color: var(--bs-gray-900);
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

.items-listing {
  color: var(--bs-gray-600);
}

button {
  text-decoration: none;
  font-size: 0.875rem;
  color: var(--bs-gray-900);
  transition: none !important;
}
button:hover {
  text-decoration: underline;
  color: var(--color-primary);
}
button:active {
  color: var(--color-primary) !important;
}

.badge {
  background-color: var(--color-primary) !important;
}

.array-display {
  width: 1%;
  white-space: nowrap !important;
}
</style>
