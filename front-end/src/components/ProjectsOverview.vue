<template>
  <div>
    <TableComponent
      v-if="projects.length > 0"
      :table-data="projects"
      :amount-to-display="10"
      :has-delete-button="true"
      :has-edit-button="true"
      :has-add-button="true"
      @edit="editProject"
      @delete="deleteProject"
    />
    <SpinnerComponent v-else />
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import { Project } from "@/models/project";

/**
 * Component to display all projects in a table.
 *
 * @author Tim Knops
 */
export default {
  name: "ProjectsOverview",
  inject: ["projectService"],
  components: { TableComponent, SpinnerComponent },
  data() {
    return {
      projects: [],
      PROJECT_STATUS_OPTIONS: Object.freeze(Project.status),
    };
  },
  async created() {
    const data = await this.projectService.getAll();

    // Modify the data to fit the table component.
    this.projects = data.map((project) => {
      console.log(project);
      return {
        id: project.id,
        name: project.projectName,
        client: project.client,
        dueDate: project.dueDate,
        team: project.team.team,
        status: project.status,
      };
    });

    console.log(this.projects);
  },
  methods: {
    editProject(project) {
      console.log(project);
    },
    deleteProject(project) {
      this.projects = this.projects.filter((p) => p.id !== project.id);
    },
  },
};
</script>

<style scoped></style>
