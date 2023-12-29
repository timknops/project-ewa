<template>
  <form>
    <div class="mb-3">
      <label for="team-name" class="form-label fw-bold">Team name</label>
      <input id="team-name"
             type="text"
             class="form-control"
             v-model.lazy.trim="modalItem.team">
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          v-model="modalItem.warehouse.id">
        <option value="" disabled>Select a warehouse</option>
        <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">
          {{ warehouse.name }}
        </option>
      </select>
    </div>
  </form>
</template>

<script>
export default {
  name: "AddTeamModal",
  inject: ['warehouseService'],
  data() {
    return {
      modalItem: {
        team: '',
        warehouse: {},
      },
      warehouses: [],
    }
  },

  methods: {
    async fetchWarehouseOptions() {
      try {
        const options = await this.warehouseService.findAll();
        this.warehouses = options || [];
      } catch (error) {
        console.error("Error fetching warehouse options:", error);
      }
    },
  },

  async created() {
    await this.fetchWarehouseOptions();
  },
}
</script>

<style scoped>

</style>