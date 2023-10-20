<template>
  <div>
    <TableComponent
      :table-data="projects"
      :amount-to-display="10"
      :has-edit-delete-buttons="true"
    />
  </div>
</template>

<script>
import TableComponent from "@/components/TableComponent.vue";
import { Project } from "@/models/project";

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
};
</script>

<style scoped></style>
