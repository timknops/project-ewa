<template>
  <form>
    <div class="mb-3">
      <label for="team-name" class="form-label fw-bold">Team name</label>
      <input
          id="team-name"
          type="text"
          class="form-control"
          :class="{'border-danger': teamEmpty}"
          v-model.lazy.trim="modalItem.team"
          @blur="validateTeam"
      />
      <p v-if="teamEmpty" class="text-danger"> The team name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          :class="{'border-danger': warehouseEmpty}"
          v-model="modalItem.warehouse"
          @blur="validateWarehouse"
      >
        <option value="" disabled>Select Warehouse</option>
        <option v-for="warehouse in warehouseOptions" :key="warehouse.id" :value="warehouse.id">
          {{ warehouse.warehouse }}
        </option>
      </select>
      <p v-if="warehouseEmpty" class="text-danger"> The warehouse can't be empty!</p>
    </div>
  </form>
</template>

<script>
export default {
  name: "UpdateTeamModal",
  inject: ['teamsService'],
  props: ["item"],
  data() {
    return {
      modalItem: {
        team: '',
        warehouse: '',
      },
      teamEmpty: false,
      warehouseEmpty: false,
      warehouseOptions: [],
    };
  },

  created() {
    this.modalItem = Object.assign({}, this.item);
    this.fetchWarehouseOptions();
    this.setDefaultWarehouse();
  },

  computed: {
    hasError() {
      return this.teamEmpty || this.warehouseEmpty;
    },
  },

  methods: {
    async fetchWarehouseOptions() {
      try {
        const data = await this.teamsService.getTeamModalData();
        this.warehouseOptions = data.warehouses;
      } catch (error) {
        console.error("Error fetching warehouse options:", error);
      }
    },

    validateTeam() {
      this.teamEmpty = this.modalItem.team.length === 0;
      this.hasError = this.modalItem.team.length === 0;
    },

    validateWarehouse() {
      this.warehouseEmpty = !this.modalItem.warehouse;
      this.hasError = !this.modalItem.warehouse;
    },

    setDefaultWarehouse() {
      this.modalItem.warehouse = this.modalItem.warehouse?.id || "";
    },
  },
};
</script>

<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>