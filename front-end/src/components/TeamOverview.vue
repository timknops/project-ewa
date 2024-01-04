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

    <Transition>
      <ToastComponent
        v-if="showToast"
        :toast-title="toastTitle"
        :toast-message="toastMessage"
        @close-toast="showToast = false"
      />
    </Transition>
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";

export default {
  name: "TeamOverview",
  components: {
    TableComponent,
    ModalComponent,
    SpinnerComponent,
    ToastComponent,
  },
  inject: ["teamsService"],

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
      showToast: false,
      toastTitle: "",
      toastMessage: "",
    };
  },
  methods: {
    showAddModal() {
      this.modalTitle = "Add team";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.okBtnText = "Add";
      this.showModal = true;
    },

    async showEditModal(team) {
      this.modalTitle = "Update team";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalTeam = await this.teamsService.findById(team.id);
      this.okBtnText = "Save";
      this.showModal = true;
    },

    showDeleteModal(team) {
      this.modalTitle = "Delete team";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalTeam = team;
      this.okBtnText = "Ok";
      this.showModal = true;
    },

    handleOk(team, modal) {
      switch (modal) {
        case this.MODAL_TYPES.ADD:
          this.addTeam(team);
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateTeam(team);
          break;
        case this.MODAL_TYPES.DELETE:
          this.deleteTeam(team);
          break;
      }
    },

    async addTeam(team) {
      try {
        const added = await this.teamsService.add(team);
        this.teams.push(added);

        this.showModal = false;
        this.showTimedToast("Success", "Team added successfully");
      } catch (e) {
        this.showModal = false;

        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to add", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to add", e.message, 8000);
        }
      }
    },

    async updateTeam(team) {
      try {
        const updated = await this.teamsService.update(team);
        this.teams = this.teams.map((team) =>
          team.id === updated.id ? updated : team
        );

        this.showModal = false;
        this.showTimedToast("Success", "Team updated successfully");
      } catch (e) {
        this.showModal = false;

        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to update", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to update", e.message, 8000);
        }
      }
    },

    async deleteTeam(team) {
      try {
        const deleted = await this.teamsService.delete(team.id);
        this.teams = this.teams.filter((team) => team.id !== deleted.id);

        this.showModal = false;
        this.showTimedToast("Success", "Team deleted successfully");
      } catch (exception) {
        this.showModal = false;

        if (exception.code >= 400 && exception.code < 500) {
          this.showTimedToast("Failed to delete", exception.reason, 8000);
        } else {
          this.showTimedToast("Failed to delete", exception.message, 8000);
        }
      }
    },

    formatTeamForTable(team) {
      return {
        id: team.id,
        teamName: team.team,
        warehouse: team.warehouse.warehouse,
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

    /**
     * Shows a toast for 4 seconds with the given title and message
     * @param {String} title The title of the toast.
     * @param {String} message The message of the toast.
     * @param {Number} duration The duration of the toast in milliseconds. Default is 4000.
     * @author Tim Knops
     */
    showTimedToast(title, message, duration = 4000) {
      this.toastTitle = title;
      this.toastMessage = message;
      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
      }, duration);
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

<style scoped></style>
