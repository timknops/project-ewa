<template>
  <div class="row w-100 px-4 align-self-center align-items-center pt-2">
    <div class="col-auto d-none d-md-flex align-items-center gap-2">
      <p class="mb-0">
        {{ currentStartIndex + 1 }} to {{ displayEndIndex() }}
        <span class="items-listing">Items of</span>
        {{ tableData.length }}
      </p>
      <!-- If the amount that is currently displayed is not equal to the entire table data length
      (meaning we're currently not in view all mode). And if the amount to be displayed that was passed as a prop
      isn't more than the total table length (there's no 'view all' when the amount to be displayed exceeds the amount
      in the table-data.) -->
      <button
        v-if="
          displayAmount !== tableData.length &&
          !(amountToDisplay >= tableData.length)
        "
        @click="$emit('view-all')"
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
        @click="$emit('view-less')"
        class="btn btn-link px-1 ms-1"
        type="button"
      >
        View less
        <font-awesome-icon icon="fa-solid fa-chevron-right" class="ms-1" />
      </button>
    </div>
    <div class="col d-flex justify-content-center justify-content-md-end">
      <button
        @click="$emit('previous')"
        class="btn btn-link px-1 me-1"
        :class="{ disabled: currentStartIndex === 0 }"
        type="button"
      >
        <font-awesome-icon
          icon="fa-solid fa-chevron-left"
          class="me-1"
        />Previous
      </button>
      <button
        @click="$emit('next')"
        class="btn btn-link px-1 ms-1"
        :class="{ disabled: this.currentEndIndex >= this.tableData.length }"
        type="button"
      >
        Next<font-awesome-icon icon="fa-solid fa-chevron-right" class="ms-1" />
      </button>
    </div>
  </div>
</template>

<script>
/**
 * Component to display the footer of a table.
 *
 * @param {Array} tableData The data of the table.
 * @param {Number} amountToDisplay The amount of rows to display.
 * @param {Number} displayAmount The amount of rows to display.
 * @param {Number} currentStartIndex The current start index of the table data that is displayed.
 * @param {Number} currentEndIndex The current end index of the table data that is displayed.
 *
 * @auhtor Tim Knops
 */
export default {
  name: "TableFooter",
  emits: ["previous", "next", "view-all", "view-less"],
  props: {
    tableData: {
      type: Array,
      required: true,
    },
    amountToDisplay: {
      type: Number,
      required: true,
    },
    displayAmount: {
      type: Number,
      required: true,
    },
    currentStartIndex: {
      type: Number,
      required: true,
    },
    currentEndIndex: {
      type: Number,
      required: true,
    },
  },

  methods: {
    /**
     * Calculates the end index of the table data that is currently displayed.
     * @returns {Number} The end index of the table data that is currently displayed.
     */
    displayEndIndex() {
      if (this.currentEndIndex > this.tableData.length) {
        return this.tableData.length;
      } else {
        return this.currentEndIndex;
      }
    },
  },
};
</script>

<style scoped>
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
</style>
