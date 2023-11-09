<template>
  <span
    v-if="currentSortDirection === SORT_DIRECTION_OPTIONS.DEFAULT"
    :data-sorting-column-name="columnName"
    @ref="columnName"
  >
    <font-awesome-icon
      icon="fa-solid fa-sort-up"
      size="xs"
      fixed-width
      transform="up-1"
    />
    <font-awesome-icon
      icon="fa-solid fa-sort-down"
      size="xs"
      fixed-width
      transform="left-20 down-1"
      class="mt-1"
    />
  </span>
  <span v-else-if="currentSortDirection === SORT_DIRECTION_OPTIONS.ASCENDING">
    <font-awesome-icon icon="fa-solid fa-sort-up" size="xs" fixed-width />
  </span>
  <span v-else-if="currentSortDirection === SORT_DIRECTION_OPTIONS.DESCENDING">
    <font-awesome-icon icon="fa-solid fa-sort-down" size="xs" fixed-width />
  </span>
</template>

<script>
/**
 * Component to display sorting icons in a table.
 *
 * @param {String} sortDirection The current sort direction of the column.
 * @param {String} columnName The name of the column.
 * @auhtor Tim Knops
 */
export default {
  name: "TableSortingIcons",
  props: {
    sortDirection: {
      type: String,
      required: true,
    },
    columnName: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      currentSortDirection: this.sortDirection,
      SORT_DIRECTION_OPTIONS: {
        DEFAULT: "default",
        ASCENDING: "ascending",
        DESCENDING: "descending",
      },
    };
  },
};
</script>

<style scoped></style>
