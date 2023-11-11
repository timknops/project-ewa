<template>
  <p>Are you sure you want to delete team {{modalItem.type}}</p>
</template>

<script>
export default {
  name: "DeleteTeamModal",
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