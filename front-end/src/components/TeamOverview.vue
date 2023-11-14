<template>
  <div>
    <table-component
        :amount-to-display="5"
        :table-data="teams"
        :has-add-button="true"
        :has-delete-button="true"
        :has-edit-button="true"
        :hide-id-column="true"
        @edit="showEditModal"
        @delete="showDeleteModal"
        @add="showAddModal"
    />
    <Transition>
      <modal-component
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
import TableComponent from "@/components/table/TableComponent";
import ModalComponent from "@/components/modal/ModalComponent";


export default {
  name: "TeamOverview",
  components: {TableComponent, ModalComponent},
  inject: ['teamsService'],

  data() {
    return {
      teams: [{
        id: Number,
        teamName: String,
        warehouse: String,
        type: String,
      }],
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
      })
    };
  },
  methods: {
    showAddModal() {
      this.modalTitle = "Add team"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
    },

    showEditModal(team) {
      this.modalTitle = "Update team"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalTeam = team
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
    }
  },

  async created() {
    this.teams = await this.teamsService.findAll();
  },
};
</script>

<style scoped>

</style>