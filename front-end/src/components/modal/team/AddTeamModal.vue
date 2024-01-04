<template>
  <form>
    <div class="mb-3">
      <label for="team-name" class="form-label fw-bold">Team name</label>
      <input
          id="team-name"
          type="text"
          class="form-control"
          v-model.lazy.trim="modalItem.team"
          @blur="validateName"
          :class="{ 'border-danger': teamEmpty }"
          placeholder="Enter team name">
      <p v-if="teamEmpty" class="text-danger"> The team name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          v-model="modalItem.warehouse.id"
          @blur="validateWarehouse"
          :class="{ 'border-danger': warehouseEmpty }">
        <option value="" disabled selected>Select a warehouse</option>
        <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">
          {{ warehouse.name }}
        </option>
      </select>
      <p v-if="warehouseEmpty" class="text-danger">The warehouse can't be empty!</p>
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
        warehouse: {
          id: '',
        },
      },
      teamEmpty: false,
      warehouseEmpty: false,
      warehouses: [],
    };
  },

  computed: {
    hasError() {
      this.validateName();
      this.validateWarehouse();
      return (this.teamEmpty || this.warehouseEmpty);
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

    validateName() {
      this.teamEmpty = this.modalItem.team.trim().length === 0;
    },

    validateWarehouse() {
      this.warehouseEmpty = !this.modalItem.warehouse.id;
    }
  },

  async created() {
    await this.fetchWarehouseOptions();
  },
}
</script>

<style scoped>

</style>