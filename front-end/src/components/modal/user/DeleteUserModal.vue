<template>
  <p>Are you sure you want to delete user {{ modalItem.name }}</p>
</template>

<script>
/**
 * Modal for deleting a user
 */
export default {
  name: "DeleteUserModal",
  data() {
    return {
      modalItem: {},
      hasError: false
    }
  },
  props: ["item"],
  created() {
    this.modalItem = Object.assign({}, this.item)
  }
}
</script>

<style scoped>
</style>
