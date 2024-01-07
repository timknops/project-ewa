<template>
  <div>
    <TableComponent
      v-if="!projectsAreLoading"
      :table-data="projects"
      :amount-to-display="10"
      :has-delete-button="true"
      :has-edit-button="true"
      :has-add-button="true"
      :has-specific-button="true"
      :has-search-bar="true"
      @edit="showEditModal"
      @delete="showDeleteModal"
      @add="showAddModal"
      @specific="showSpecificView"
    />
    <SpinnerComponent v-else />

    <Transition>
      <ModalComponent
        v-if="showModal"
        :title="modalTitle"
        :active-modal="modalBodyComponent"
        :item="modalProject"
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
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";
import { Project } from "@/models/project";
import { Transition } from "vue";

/**
 * Component to display all projects in a table.
 *
 * @author Tim Knops
 */
export default {
  name: "ProjectsOverview",
  inject: ["projectService", "sessionService"],
  components: {
    TableComponent,
    SpinnerComponent,
    ModalComponent,
    Transition,
    ToastComponent,
  },
  data() {
    return {
      projects: [],
      projectsAreLoading: true,
      PROJECT_STATUS_OPTIONS: Object.freeze(Project.status),
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalProject: {
        id: Number,
        projectName: String,
        client: String,
        dueDate: Date,
        team: String,
        status: String,
      },
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-project-modal",
        UPDATE: "update-project-modal",
        ADD: "add-project-modal",
      }),
      showToast: false,
      toastTitle: "",
      toastMessage: "",
      userTypes: Object.freeze({
        ADMIN: "ADMIN",
        VIEWER: "VIEWER",
      }),
    };
  },
  async created() {
    // Get the current active user type.
    const user = await this.sessionService.currentUser;

    let data;
    if (user.type === this.userTypes.VIEWER && user.team !== null) {
      data = await this.projectService.getAllForTeam(user.team);
    }

    if (user.type === this.userTypes.ADMIN) {
      data = await this.projectService.getAll();
    }

    // If there are no projects, add only the table header row titles.
    if (data === undefined || data.length === 0) {
      this.projects = [this.formatEmptyTableData()];

      this.projectsAreLoading = false;
      return;
    }

    // Modify the data so that it is displayed correctly in the table.
    this.projects = data.map((project) => {
      return this.formatProjectForTable(project);
    });

    this.projectsAreLoading = false;
  },
  methods: {
    /**
     * Formats the project to the correct format for the table.
     * @param {Object} project The project to be formatted.
     * @returns {Object} The formatted project.
     */
    formatProjectForTable(project) {
      return {
        id: project.id,
        name: project.projectName,
        client: project.client,
        dueDate: this.formatDate(project.dueDate),
        team: project.team.team,
        status: project.status,
      };
    },

    /**
     * Formats the project to the correct format for the POST/PUT request.
     *
      {
        "project": {
          "projectName": "Project name",
          "team": {
            "id": 1,
            "teamName": "Team name"
          },
          "client": "Client Name",
          "dueDate": "2023-12-31",
          "description": "Project Description",
          "status": "" // UPCOMING, IN_PROGRESS, COMPLETED
        },
        "resources": [
          {
            "product": {
              "id": 1
            },
            "quantity": 500
          },
          {
            "product": {
              "id": 2
            },
            "quantity": 300
          }
        ]
      }
     * @param {Object} project the project to be formatted.
     * @returns {Object} The formatted project.
     */
    formatProjectForRequest(project) {
      // Loop through the products and remove those that have a product_id of "". Meaning that they do not have a product selected in the dropdown.
      project.products = project.products.filter(
        (product) => product.product_id !== ""
      );

      return {
        project: {
          id: project.id,
          projectName: project.projectName,
          team: {
            id: project.team,
          },
          client: project.client,
          dueDate: project.dueDate,
          description: project.description,
          status:
            project.status === "In Progress"
              ? "IN_PROGRESS"
              : project.status.toUpperCase(),
        },
        resources: project.products.map((product) => {
          return {
            product: {
              id: product.product_id,
            },
            quantity: product.quantity,
          };
        }),
      };
    },

    /**
     * Formats date to, example: Jun 1, 2021
     * @param {Date} date Date to be formatted.
     * @returns {String} Formatted date.
     */
    formatDate(date) {
      // Formats date to, example: Jun 1, 2021
      return new Date(date).toLocaleDateString("en-US", {
        month: "short",
        day: "numeric",
        year: "numeric",
      });
    },

    /**
     * Formats the table data when there are no projects.
     * @returns {{id: "", name: "", client: "", dueDate: "", team: "", status: ""}}
     */
    formatEmptyTableData() {
      return {
        id: "",
        name: "",
        client: "",
        dueDate: "",
        team: "",
        status: "",
      };
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    async deleteProject(project) {
      try {
        await this.projectService.delete(project.id);
        this.projects = this.projects.filter((p) => p.id !== project.id);

        this.showModal = false;
        this.showTimedToast("Deleted successfully", "Project deleted.");
      } catch (error) {
        this.showModal = false;

        if (error.code >= 400 && error.code < 500) {
          this.showTimedToast("Failed to delete", error.reason, 8000);
        } else {
          this.showTimedToast("Failed to delete", error.message, 8000);
        }
      }
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    async addProject(project) {
      try {
        const newProject = await this.projectService.add(
          this.formatProjectForRequest(project)
        );
        this.projects.push(this.formatProjectForTable(newProject));

        this.showModal = false;
        this.showTimedToast("Success", "Project added.");
      } catch (error) {
        this.showModal = false;

        if (error.code >= 400 && error.code < 500) {
          this.showTimedToast("Failed to add", error.reason, 8000);
        } else {
          this.showTimedToast("Failed to add", error.message, 8000);
        }
      }
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    async updateProject(project) {
      try {
        const updatedProject = await this.projectService.update(
          this.formatProjectForRequest(project)
        );
        this.projects = this.projects.map((p) =>
          p.id === updatedProject.id
            ? this.formatProjectForTable(updatedProject)
            : p
        );

        this.showModal = false;
        this.showTimedToast("Updated successfully", "Project updated.");
      } catch (error) {
        this.showModal = false;

        if (error.code >= 400 && error.code < 500) {
          this.showTimedToast("Failed to update", error.reason, 8000);
        } else {
          this.showTimedToast("Failed to update", error.message, 8000);
        }
      }
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    showDeleteModal(project) {
      this.modalTitle = "Delete project";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalProject = project;
      this.okBtnText = "Delete";
      this.showModal = true;
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    async showEditModal(project) {
      this.modalProject = await this.projectService.get(project.id);
      this.modalTitle = "Update project";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.okBtnText = "Save";
      this.showModal = true;
    },

    /** Opens the edit modal to update a project. */
    showAddModal() {
      this.modalTitle = "Add project";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.okBtnText = "Add";
      this.showModal = true;
    },

    /**
     * Handles the logic when the ok button is clicked.
     * @param {Object} project The project to be updated.
     * @param {String} modal The modal type.
     */
    handleOk(project, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.deleteProject(project);
          break;
        case this.MODAL_TYPES.UPDATE:
          this.updateProject(project);
          break;
        case this.MODAL_TYPES.ADD:
          this.addProject(project);
          break;
        default:
          break;
      }
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

    showSpecificView(project) {
      this.$router.push(`/projects/${project.id}`);
    },
  },
};
</script>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.1s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}
</style>
