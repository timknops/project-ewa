<template>
  <div>
    <TableComponent
      v-if="!projectsAreLoading"
      :table-data="projects"
      :amount-to-display="10"
      :has-delete-button="true"
      :has-edit-button="true"
      :has-add-button="true"
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
        :item="modalProject"
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
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";
import { Project } from "@/models/project";
import { Transition } from "vue";

/**
 * Component to display all projects in a table.
 *
 * @author Tim Knops
 */
export default {
  name: "ProjectsOverview",
  inject: ["projectService"],
  components: { TableComponent, SpinnerComponent, ModalComponent, Transition },
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
    };
  },
  async created() {
    const data = await this.projectService.getAll();

    // If there are no projects, add only the table header row titles.
    if (data.length === 0) {
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
     * Formats the project to the correct format for the request.
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
      return {
        project: {
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
      } catch (error) {
        console.log(error);
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
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * Opens the edit modal to update a project.
     * @param {Object} project The project to be updated.
     */
    async updateProject(project) {
      try {
        const updatedProject = await this.projectService.update(project);
        this.projects = this.projects.map((p) =>
          p.id === updatedProject.id
            ? this.formatProjectForTable(updatedProject)
            : p
        );
        this.showModal = false;
      } catch (error) {
        console.log(error);
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
    showEditModal(project) {
      this.modalTitle = "Update project";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalProject = project;
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
