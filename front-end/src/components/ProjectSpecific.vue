<template>
  <h1>Specific View</h1>
</template>

<script>
export default {
  name: "SpecificView",
  inject: ["projectService"],
  data() {
    return {
      project: {},
    };
  },
  async created() {
    // Get the specific project data by using the project id from the route.
    this.project = await this.projectService.get(this.$route.params.id);
    console.log(this.project);
  },
  methods: {},
};
</script>

<style></style>
