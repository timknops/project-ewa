<template>
  <form>
    <div class="mb-3">
      <label for="project-name" class="form-label fw-bold"
        >Project Name <span class="text-danger">*</span></label
      >
      <input
        v-model.lazy.trim="modalItem.projectName"
        @blur="validateName"
        :class="{ 'border-danger': nameEmpty }"
        type="text"
        class="form-control"
        id="project-name"
        placeholder="Enter the project name"
      />
      <small v-if="nameEmpty" class="text-danger">
        The project name can't be empty!
      </small>
    </div>

    <div class="mb-3 grid row">
      <div class="col">
        <label for="client" class="form-label fw-bold">Client</label>
        <input
          v-model.lazy.trim="modalItem.client"
          type="text"
          class="form-control"
          id="client"
          placeholder="Enter the client name"
        />
      </div>
      <div class="col">
        <label for="due-date" class="form-label fw-bold"
          >Due date <span class="text-danger">*</span></label
        >
        <input
          v-model.lazy.trim="modalItem.dueDate"
          @blur="validateDueDate"
          :class="{ 'border-danger': dueDateEmpty }"
          type="date"
          class="form-control"
          id="due-date"
        />
        <small v-if="dueDateEmpty" class="text-danger">
          The due date can't be empty!
        </small>
      </div>
    </div>

    <div class="mb-3 grid row">
      <div class="col">
        <label for="team-select" class="form-label fw-bold"
          >Team <span class="text-danger">*</span></label
        >
        <select
          v-model="modalItem.team"
          @blur="validateTeam"
          class="form-select"
          :class="{ 'border-danger': teamUnselected }"
          id="team-select"
          aria-label="team"
        >
          <option selected value="">Assign team</option>
          <option v-for="team in TEAM_OPTIONS" :key="team" :value="team">
            {{ team }}
          </option>
        </select>
        <small v-if="teamUnselected" class="text-danger"
          >The team can't be unassigned!</small
        >
      </div>

      <div class="col">
        <label for="status-select" class="form-label fw-bold"
          >Status <span class="text-danger">*</span></label
        >
        <select class="form-select" id="status-select" aria-label="status">
          <option selected>Assign status</option>
          <option
            v-for="status in STATUS_OPTIONS"
            :key="status"
            :value="status"
          >
            {{ status }}
          </option>
        </select>
      </div>
    </div>
  </form>
</template>

<script>
/**
 * The modal for adding a project
 *
 * @Author Tim Knops
 */
export default {
  name: "AddProjectModal",
  data() {
    return {
      modalItem: {
        id: 0,
        projectName: "",
        client: "",
        dueDate: "",
        team: "",
        status: "",
        description: "",
        products: [],
      },
      nameEmpty: false,
      dueDateEmpty: false,
      teamUnselected: false,
      TEAM_OPTIONS: ["Team 1", "Team 2", "Team 3"],
      STATUS_OPTIONS: ["Upcoming", "In Progress", "Completed"],
    };
  },
  computed: {
    hasError() {
      return this.nameEmpty || this.dueDateEmpty || this.teamUnselected;
    },
  },
  methods: {
    validateName() {
      this.nameEmpty = this.modalItem.projectName.length === 0;
    },

    validateDueDate() {
      this.dueDateEmpty = this.modalItem.dueDate.length === 0;
    },

    validateTeam() {
      this.teamUnselected = this.modalItem.team.length === 0;
    },
  },
};
</script>

<style>
.modal-dialog {
  min-width: 600px !important;
}
</style>
