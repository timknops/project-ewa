<template>
  <div>
    <TableComponent
      v-if="!teamsAreLoading"
      :amount-to-display="amountToDisplay"
      :has-add-button="hasAddButton"
      :has-delete-button="hasDeleteButton"
      :has-edit-button="hasEditButton"
      :table-data="teams"
      :has-search-bar="hasSearchBar"
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
import ToastComponent from "@/components/util/ToastComponent";

export default {
  name: "TeamOverview",
  components: {
    TableComponent,
    ModalComponent,
    SpinnerComponent,
    ToastComponent,
  },
  inject: ["teamsService", "sessionService"],

  data() {
    return {
      teams: [
        {
          id: Number,
          teamName: String,
          warehouse: String,
          type: String,
        },
      ],
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
      userTypes: Object.freeze({
        ADMIN: "ADMIN",
        VIEWER: "VIEWER",
      }),
      hasAddButton: true,
      hasDeleteButton: true,
      hasEditButton: true,
      hasSearchBar: true,
      amountToDisplay: 10,
    };
  },
  methods: {
    showAddModal() {
      this.modalTitle = "Add team";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.modalTeam = {
        team: "",
        warehouse: "",
      };
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
      this.okBtnText = "Delete";
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
        this.teams.push(this.formatTeamForTable(added));
        this.showModal = false;
        this.showTimedToast("Success", "Team added.");
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
          team.id === updated.id ? this.formatTeamForTable(updated) : team
        );
        this.showModal = false;
        this.showTimedToast("Updated successfully", "Team updated.");
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
        await this.teamsService.delete(team.id);
        const index = this.teams.findIndex((t) => t.id === team.id);
        if (index !== -1) {
          this.teams.splice(index, 1);
        }
        this.showModal = false;
        this.showTimedToast("Deleted successfully", "Team deleted.");
      } catch (e) {
        this.showModal = false;
        if (e.response && e.response.status === 412) {
          this.showTimedToast(
            "Failed to delete",
            e.response.data.message,
            8000
          );
        } else if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to delete", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to delete", e.message, 8000);
        }
      }
    },

    formatTeamForTable(team) {
      return {
        id: team.id,
        team: team.team,
        warehouse: team.warehouseName,
        type:
          team.type.charAt(0).toUpperCase() + team.type.slice(1).toLowerCase(),
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
    // Get the current active user.
    const user = await this.sessionService.currentUser;

    let data;
    // If the user is a viewer, show only the teams that are assigned to the user.
    if (user.type === this.userTypes.VIEWER && user.team !== null) {
      data = await this.teamsService.findById(user.team.id);
      this.hasAddButton = false;
      this.hasDeleteButton = false;
      this.hasEditButton = false;
      this.hasSearchBar = false;
    } else if (user.type === this.userTypes.ADMIN) {
      // If the user is an admin, show all teams.
      data = await this.teamsService.findAll();
    }

    // If there are no teams, show an empty table.
    if (data === undefined || data.length === 0) {
      this.teams = [this.formatEmptyTableData()];

      this.teamsAreLoading = false;
      return;
    }

    // If there is only one team, don't map the data, because it's not an array.
    if (data.id !== undefined) {
      this.teams = [this.formatTeamForTable(data)];
    } else {
      this.teams = data.map((team) => {
        return this.formatTeamForTable(team);
      });
    }

    if (this.teams.length < this.amountToDisplay) {
      this.amountToDisplay = this.teams.length;
    }

    this.teamsAreLoading = false;
  },
};
</script>

<style scoped></style>
