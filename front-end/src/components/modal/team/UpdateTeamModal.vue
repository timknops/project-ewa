<template>
  <form>
    <div class="mb-3">
      <label for="team-name" class="form-label fw-bold">Team name</label>
      <input
          id="team-name"
          type="text"
          class="form-control"
          v-model.lazy.trim="modalItem.team">
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          v-model.lazy.trim="modalItem.warehouseName">
        <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.name">
          {{ warehouse.name}}
        </option>
      </select>
    </div>
  </form>
</template>

<script>
export default {
  name: "UpdateTeamModal",
  inject: ['warehouseService'],
  props: ["item"],
  data() {
    return {
      modalItem: {
        team: "",
        warehouseName: "",
      },
      warehouses: [],
    };
  },

  async created() {
    this.modalItem = Object.assign({}, this.item);
    try {
      this.warehouses = await this.warehouseService.findAll();
    } catch (error) {
      console.error("Error fetching warehouse:", error);
    }
  }
};
</script>

<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>
