<template>
  <form>
    <div class="mb-3">
      <label for="team-name" class="form-label fw-bold">Team name</label>
      <input id="team-name"
             type="text"
             class="form-control"
             :class="{'border-danger': teamEmpty}"
             v-model.lazy.trim="modalItem.team"
             @blur="validateName">
      <p v-if="teamEmpty" class="text-danger"> The team name can't be empty!</p>
    </div>
    <div class="mb-3">
      <label for="warehouse" class="form-label fw-bold">Select Warehouse</label>
      <select
          id="warehouse"
          class="form-select"
          :class="{'border-danger': warehouseEmpty}"
          v-model="modalItem.warehouse"
          @blur="validateWarehouse">
        <option value="" disabled>Select a warehouse</option>
        <option v-for="warehouse in warehouseOptions" :key="warehouse.id" :value="warehouse.id">
          {{ warehouse.warehouse }}
        </option>
      </select>
      <p v-if="warehouseEmpty" class="text-danger">The warehouse can't be empty!</p>
    </div>
  </form>
</template>

<script>
export default {
  name: "AddTeamModal",
  inject: ['teamsService'],
  data() {
    return {
      modalItem:{
        team: '',
        warehouse: '',
      },
      teamEmpty: false,
      warehouseEmpty: false,
      warehouseOptions: [],
    }
  },

  computed: {
    hasError() {
      return this.teamEmpty || this.warehouseEmpty;
    }
  },

  methods: {
    validateName() {
      this.teamEmpty = this.modalItem.team.length === 0;
      this.hasError = this.modalItem.team.length === 0;
    },

    validateWarehouse() {
      this.warehouseEmpty = this.modalItem.warehouse.length === 0;
      this.hasError = this.modalItem.warehouse.length === 0;
    }
  },
  async created() {
    const data = await this.teamsService.getTeamModalData();

    this.warehouseOptions = data.warehouses;
  },
}
</script>

<style scoped>

</style>