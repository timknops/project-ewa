<template>
  <tr ref="headerRow">
    <!-- If the id column should be hidden, slice the array to remove the first element (the id column) -->
    <template v-if="hideIdColumn">
      <th
        v-for="(name, index) in tableColumnNames.slice(1)"
        :key="name"
        scope="col"
        class="py-3 pt-2 table-header-text px-3 px-lg-4"
        :class="{
          'pe-lg-0':
            hasAddButton && index === tableColumnNames.slice(1).length - 1,
        }"
      >
        <div class="d-flex justify-content-between align-items-end">
          <div @click="emitSortDataToParent(name)" class="header-container">
            {{ name.toUpperCase() }}
            <!-- icons for sorting -->
            <TableSortingIcons
              :sort-direction="sortDirectionAllColumns"
              :column-name="name"
              :data-sorting-column-name="'sortingIcons' + name"
              :ref="'sortingIcons' + name"
            />
          </div>

          <button
            v-if="
              hasAddButton && index === tableColumnNames.slice(1).length - 1
            "
            class="btn btn-primary align-middle"
            @click="$emit('add')"
          >
            <font-awesome-icon icon="fa-solid fa-plus" />
            add
          </button>
        </div>
      </th>
    </template>
    <template v-else>
      <th
        v-for="(name, index) in tableColumnNames"
        :key="name"
        scope="col"
        class="py-3 pt-2 table-header-text px-3 px-lg-4"
        :class="{
          'pe-lg-0': hasAddButton && index === tableColumnNames.length - 1,
        }"
      >
        <div class="d-flex justify-content-between align-items-end">
          <div @click="emitSortDataToParent(name)" class="header-container">
            {{ name.toUpperCase() }}
            <!-- icons for sorting -->
            <TableSortingIcons
              :sort-direction="sortDirectionAllColumns"
              :column-name="name"
              :ref="'sortingIcons' + name"
            />
          </div>

          <button
            v-if="hasAddButton && index === tableColumnNames.length - 1"
            class="btn btn-primary align-middle"
            @click="$emit('add')"
          >
            <font-awesome-icon icon="fa-solid fa-plus" />
            add
          </button>
        </div>
      </th>
    </template>
  </tr>
</template>

<script>
import TableSortingIcons from "@/components/table/TableSortingIcons.vue";

/**
 * Component to display the header row of a table.
 *
 * @param {Array} tableColumnNames The names of the columns.
 * @param {Boolean} hasAddButton Whether the table has an add button.
 * @param {Boolean} hideIdColumn Whether the id column should be hidden.
 * @param {String} sortDirectionAllColumns The sort direction of all columns.
 * @param {String} previousSortedColumn The name of the previous sorted column.
 *
 * @author Tim Knops
 */
export default {
  name: "TableHeaderRow",
  components: { TableSortingIcons },
  emits: ["add", "sort"],
  props: {
    tableColumnNames: {
      type: Array,
      required: true,
    },
    hasAddButton: {
      type: Boolean,
      required: false,
      default: false,
    },
    hideIdColumn: {
      type: Boolean,
      required: false,
      default: false,
    },
    sortDirectionAllColumns: {
      type: String,
      required: true,
    },
    previousSortedColumn: {
      type: String,
      required: true,
    },
  },
  methods: {
    /**
     * Emits the sort data to the parent component.
     * @param {String} name The name of the column.
     */
    emitSortDataToParent(name) {
      this.$emit("sort", {
        columnName: name,
        columnIconRef: this.$refs["sortingIcons" + name][0],
      });
    },
  },
};
</script>

<style scoped>
.table-header-text {
  color: var(--bs-gray-900);
}

.header-container {
  cursor: pointer;
}
</style>
