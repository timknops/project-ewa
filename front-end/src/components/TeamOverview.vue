<template>
  <div>
    <table-component
        :amount-to-display="5"
        :table-data="teams"
        :has-edit-delete-buttons="true"
    />
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent";

export default {
  components: { TableComponent },
  inject: ['teamsService'],

  data() {
    return {
      teams: [{
        id: Number,
        teamName: String,
        warehouse: String,
        type: String,
      }],
    };
  },

  async created() {
    this.teams = await this.teamsService.findAll();
  },
};
</script>

<style scoped>

</style>