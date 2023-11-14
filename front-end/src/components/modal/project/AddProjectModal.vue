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
      hasError: false,
      nameEmpty: false,
      dueDateEmpty: false,
    };
  },
  methods: {
    validateName() {
      this.nameEmpty = this.modalItem.projectName.length === 0;
      this.hasError = this.modalItem.projectName.length === 0;
    },

    validateDueDate() {
      this.dueDateEmpty = this.modalItem.dueDate.length === 0;
      this.hasError = this.modalItem.dueDate.length === 0;
    },
  },
};
</script>

<style></style>
