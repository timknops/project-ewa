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
          :class="{ 'border-danger': teamEmpty }">
      <p v-if="teamEmpty" class="text-danger"> The team name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          v-model.lazy.trim="modalItem.warehouseName">
        <option value="" disabled>Select a warehouse</option>
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
      teamEmpty: false,
      warehouses: [],
    };
  },

  computed: {
    hasError() {
      this.validateName();
      return (this.teamEmpty);
    }
  },

  async created() {
    this.modalItem = Object.assign({}, this.item);
    try {
      this.warehouses = await this.warehouseService.findAll();
    } catch (error) {
      console.error("Error fetching warehouse:", error);
    }
  },

  methods: {
    validateName() {
      this.teamEmpty = this.modalItem.team.trim().length === 0;
    },
  },
};
</script>

<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>
