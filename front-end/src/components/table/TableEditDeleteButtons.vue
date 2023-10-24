<template>
  <div
    class="d-none end-0 position-absolute me-3 me-lg-4 my-1 edit-btn-container"
    :style="{ height: rowHeightLarge - 8 + 'px' }"
  >
    <div class="d-flex align-items-center h-100 gap-1">
      <button
        class="btn border text-body edit-btn modify-btn"
        type="button"
        @click="$emit('edit', tableRow)"
      >
        <font-awesome-icon icon="fa-solid fa-pen-to-square" />
      </button>
      <button
        class="btn text-danger border edit-btn delete-btn"
        type="button"
        @click="$emit('delete', tableRow)"
      >
        <font-awesome-icon icon="fa-solid fa-trash" />
      </button>
    </div>
  </div>
</template>

<script>
/**
 * Component to display edit and delete buttons in a table.
 *
 * @param {Object} tableRow The row of the table.
 * @param {Number} rowHeightLarge The height of the row.
 *
 * @auhtor Tim Knops
 */
export default {
  name: "TableEditDeleteButtons",
  emits: ["edit", "delete"],
  props: {
    tableRow: {
      type: Object,
      required: true,
    },
    rowHeightLarge: {
      type: Number,
      required: true,
    },
  },
};
</script>

<style scoped>
.edit-btn {
  background-color: var(--bs-white);
}
.edit-btn:hover {
  background-color: var(--bs-gray-100) !important;
}

.delete-btn:hover {
  color: var(--color-secondary) !important;
}

.modify-btn:hover {
  color: var(--color-primary) !important;
}
</style>
