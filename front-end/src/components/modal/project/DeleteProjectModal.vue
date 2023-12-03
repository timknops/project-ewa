<template>
  <p>
    Are you sure you want to delete the project: <b> {{ modalItem.name }}</b>
  </p>
</template>

<script>
/**
 * Modal for deleting a project
 *
 * @author Tim Knops
 */
export default {
  name: "DeleteProductModal",
  data() {
    return {
      modalItem: {},
      hasError: false,
    };
  },
  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item);
    console.log(this.modalItem);
  },
};
</script>

<style scoped></style>
