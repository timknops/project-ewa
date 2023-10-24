<template>
  <div>
    <TableComponent
      :table-data="projects"
      :amount-to-display="10"
      :has-edit-delete-buttons="true"
      @edit="editProject"
      @delete="deleteProject"
    />
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import { Project } from "@/models/project";

/**
 * Component to display all projects in a table.
 *
 * @author Tim Knops
 */
export default {
  name: "ProjectsOverview",
  components: { TableComponent },
  data() {
    return {
      projects: [],
    };
  },
  created() {
    const AMOUNT_OF_PROJECTS = 15;

    for (let i = 0; i < AMOUNT_OF_PROJECTS; i++) {
      this.projects.push(Project.createDummyProject());
    }
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
