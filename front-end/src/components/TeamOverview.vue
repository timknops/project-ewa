<template>
  <div>
    <TableComponent
        v-if="!teamsAreLoading"
        :amount-to-display="4"
        :has-add-button="true"
        :has-delete-button="true"
        :has-edit-button="true"
        :table-data="teams"
        :has-search-bar="true"
        @edit="showEditModal"
        @delete="showDeleteModal"
        @add="showAddModal"
    />
    <SpinnerComponent v-else />
    <Transition>
      <ModalComponent
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalTeam"
          :ok-btn-text="okBtnText"
          @cancel-modal-btn="this.showModal = false"
          @corner-close-modal-btn="this.showModal = false"
          @ok-modal-btn="handleOk"
      />
    </Transition>
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";

export default {
  name: "TeamOverview",
  components: {TableComponent, ModalComponent, SpinnerComponent},
  inject: ['teamsService'],

  data() {
    return {
      teams: [],
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalTeam: {
        id: Number,
        teamName: String,
        warehouse: String,
        type: String,
      },
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        ADD: "add-team-modal",
        UPDATE: "update-team-modal",
        DELETE: "delete-team-modal",
      }),
      teamsAreLoading: true,
    };
  },
  methods: {
    showAddModal() {
      this.modalTitle = "Add team"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
    },

    async showEditModal(team) {
      this.modalTitle = "Update team"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalTeam = await this.teamsService.findById(team.id)
      this.okBtnText = "Save"
      this.showModal = true;
    },

    showDeleteModal(team) {
      this.modalTitle = "Delete team"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalTeam = team
      this.okBtnText = "Ok"
      this.showModal = true;
    },

    handleOk(team, modal) {
      switch (modal) {
        case this.MODAL_TYPES.ADD:
          this.addTeam(team)
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateTeam(team)
          break;
        case this.MODAL_TYPES.DELETE:
          this.deleteTeam(team)
          break;
      }
    },

    async addTeam(team){
      try {
        const added = await this.teamsService.add(team)
        this.teams.push(added)
        this.showModal =false;

      } catch (e){
        console.log(e)
      }
    },

    async updateTeam(team) {
      try {
        const updated = await this.teamsService.update(team)
        this.teams = this.teams.map((team) => team.id === updated.id ? updated : team);
        this.showModal = false
      } catch (e) {
        console.log(e)
      }
    },

    async deleteTeam(team) {
      try {
        const deleted = await this.teamsService.delete(team.id)
        this.teams = this.teams.filter((team) => team.id !== deleted.id)
        this.showModal = false;
      } catch (exception) {
        console.log(exception)
      }
    },

    formatTeamForTable(team) {
      return {
        id: team.id,
        teamName: team.team,
        warehouse: team.warehouse.name,
        type: team.type,
      };
    },

    formatEmptyTableData() {
      return {
        id: "",
        teamName: "",
        warehouse: "",
        type: "",
      };
    },
  },

  async created() {
    const data = await this.teamsService.findAll();

    if (data.length === 0) {
      this.teams = [this.formatEmptyTableData()];

      this.teamsAreLoading = false;
      return;
    }

    this.teams = data.map((team) => {
      return this.formatTeamForTable(team);
    });

    this.teamsAreLoading = false;
  },
};
</script>

<style scoped>

</style>