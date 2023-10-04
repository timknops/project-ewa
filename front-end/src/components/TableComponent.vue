<template>
  <div class="card border-0 pt-4 pb-2 d-flex" :style="{ width: tableWidth }">
    <div class="card-body px-4 py-0 overflow-hidden">
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
            <tr v-for="tableRow in currentlyDisplayedData" :key="tableRow">
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
            <tr v-for="tableRow in currentlyDisplayedData" :key="tableRow">
              <template
                v-for="fieldData in Object.values(tableRow)"
                :key="fieldData"
              >
                <td v-if="Array.isArray(fieldData)" class="px-3">
                  <button
                    type="button"
                    class="btn btn-link custom-popover-btn ps-2 fw-medium"
                    data-bs-toggle="popover"
                    data-bs-title="Popover title"
                    :data-bs-content="displayPopoverArrayItems(fieldData)"
                  >
                    See all {{ this.arrayColumnName }}
                    <span class="badge text-bg-secondary ms-1">{{
                      fieldData.length
                    }}</span>
                  </button>
                </td>
                <td v-else class="py-lg-3 table-text px-3 px-lg-4">
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
          View all<svg
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
        <button
          v-else-if="!(this.amountToDisplay >= this.tableData.length)"
          @click="viewLessItems()"
          class="btn btn-link px-1 ms-1"
          type="button"
        >
          View less<svg
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
      <div class="col d-flex justify-content-center justify-content-md-end">
        <button
          @click="handlePreviousButton()"
          class="btn btn-link px-1 me-1"
          :class="{ disabled: this.currentStartIndex === 0 }"
          type="button"
        >
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
        <button
          @click="handleNextButton()"
          class="btn btn-link px-1 ms-1"
          :class="{ disabled: this.currentEndIndex >= this.tableData.length }"
          type="button"
        >
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
import { Popover } from "bootstrap";

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
      arrayColumnName: "",
    };
  },
  methods: {
    handleNextButton() {
      this.currentStartIndex = this.currentEndIndex;
      this.currentEndIndex += this.displayAmount;
      this.updateDisplayedData();
    },

    handlePreviousButton() {
      this.currentEndIndex = this.currentStartIndex;
      this.currentStartIndex -= this.displayAmount;
      this.updateDisplayedData();
    },

    updateDisplayedData() {
      this.currentlyDisplayedData = this.tableData.slice(
        this.currentStartIndex,
        this.currentEndIndex
      );

      this.$nextTick(() => {
        this.applyPopovers();
      });
    },

    displayEndIndex() {
      if (this.currentEndIndex >= this.tableData.length) {
        return this.tableData.length;
      }

      return this.currentEndIndex;
    },

    viewAllItems() {
      this.savedAmountToDisplay = this.displayAmount;
      this.displayAmount = this.tableData.length;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.tableData.length;
      this.updateDisplayedData();
    },

    viewLessItems() {
      this.displayAmount = this.savedAmountToDisplay;
      this.currentStartIndex = 0;
      this.currentEndIndex = this.displayAmount;
      this.updateDisplayedData();
    },

    findArrayColumnName() {
      Object.values(this.currentlyDisplayedData[0]).forEach((value, index) => {
        if (Array.isArray(value)) {
          this.arrayColumnName = Object.keys(this.currentlyDisplayedData[0])[
            index
          ];
        }
      });
    },

    applyPopovers() {
      const popoverTriggerList = document.querySelectorAll(
        '[data-bs-toggle="popover"]'
      );
      [...popoverTriggerList].map(
        (popoverTriggerEl) =>
          new Popover(popoverTriggerEl, { trigger: "hover", placement: "left" })
      );
    },

    displayPopoverArrayItems(arr) {
      console.log(arr);

      return 0;
    },
  },
  created() {
    this.updateDisplayedData();
    this.findArrayColumnName();
  },
  computed: {
    calculateTableHeight() {
      const TABLE_ROW_HEIGHT_BREAKPOINT_PX = 992;
      const rowHeightLarge = 57;
      const rowHeightSmall = 41;

      if (TABLE_ROW_HEIGHT_BREAKPOINT_PX < window.innerWidth) {
        return rowHeightLarge * this.displayAmount + rowHeightLarge + "px";
      }

      return rowHeightSmall * this.displayAmount + rowHeightSmall + "px";
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

.custom-popover-btn {
  color: var(--color-primary);
  cursor: default !important;
}

.badge {
  background-color: var(--color-primary) !important;
}
</style>
