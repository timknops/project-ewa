<template>
  <div
    class="d-none end-0 position-absolute me-3 me-lg-4 my-1 edit-btn-container"
    :style="{ height: rowHeightLarge - 8 + 'px' }"
  >
    <div class="d-flex align-items-center h-100 gap-1">
      <button
        v-if="hasSpecificButton"
        class="btn border text-body edit-btn modify-btn"
        type="button"
        @click="$emit('specific', tableRow)"
      >
        <FontAwesomeIcon icon="fa-solid fa-eye" />
      </button>
      <button
        v-if="hasEditButton"
        class="btn border text-body edit-btn modify-btn"
        type="button"
        @click="$emit('edit', tableRow)"
      >
        <FontAwesomeIcon icon="fa-solid fa-pen-to-square" />
      </button>
      <button
        v-if="hasDeleteButton"
        class="btn text-danger border edit-btn delete-btn"
        type="button"
        @click="$emit('delete', tableRow)"
      >
        <FontAwesomeIcon icon="fa-solid fa-trash" />
      </button>
    </div>
  </div>
</template>

<script>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/**
 * Component to display edit and delete buttons in a table.
 *
 * @param {Object} tableRow The row of the table.
 * @param {Number} rowHeightLarge The height of the row.
 * @param {Boolean} hasEditButton Whether the edit button should be displayed.
 * @param {Boolean} hasDeleteButton Whether the delete button should be displayed.
 * @param {Boolean} hasSpecificButton Whether the specific button should be displayed.
 *
 * @author Tim Knops
 */
export default {
  name: "TableEditDeleteButtons",
  emits: ["edit", "delete", "specific"],
  props: {
    tableRow: {
      type: Object,
      required: true,
    },
    rowHeightLarge: {
      type: Number,
      required: true,
    },
    hasEditButton: Boolean,
    hasDeleteButton: Boolean,
    hasSpecificButton: Boolean,
  },
  components: { FontAwesomeIcon },
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
