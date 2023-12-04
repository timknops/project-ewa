<template>
  <div class="grid row gap-4">
    <div class="card border-0 d-flex col-xl p-4">
      <div v-if="projectLoaded" class="px-1">
        <h3 class="fw-bold fs-2 project-title">
          {{ project.projectName }}
        </h3>
        <span class="badge mb-4" :class="STATUS_OPTIONS[project.status]">{{
          projectStatus
        }}</span>
        <p class="fw-bold">
          Client: <span class="fw-normal">{{ project.client }}</span>
        </p>
        <p class="fw-bold">
          Due Date:
          <span class="fw-normal">{{ dueDate }}</span>
        </p>
        <p class="fw-bold">
          Assigned Team: <span class="fw-normal">{{ project.teamName }}</span>
        </p>
      </div>
      <SpinnerComponent v-else />
    </div>

    <div class="col-xl p-0">
      <TableComponent
        v-if="productsLoaded"
        :table-data="products"
        :amount-to-display="9"
        table-title="Products"
        sub-title="Current products in this project"
      />
      <SpinnerComponent v-else />
    </div>
  </div>
</template>

<script>
import TableComponent from "./table/TableComponent.vue";
import SpinnerComponent from "./util/SpinnerComponent.vue";

export default {
  name: "SpecificView",
  inject: ["projectService"],
  components: {
    TableComponent,
    SpinnerComponent,
  },
  data() {
    return {
      project: {},
      products: [],
      productsLoaded: false,
      projectLoaded: false,
      STATUS_OPTIONS: Object.freeze({
        COMPLETED: "success-badge",
        IN_PROGRESS: "in-progress-badge",
        UPCOMING: "upcoming-badge",
      }),
    };
  },
  async created() {
    // Get the specific project data by using the project id from the route.
    this.project = await this.projectService.get(this.$route.params.id);

    // TODO: Error handling if project was not found.

    console.log(this.project);

    this.formatProducts();

    console.log(this.products);
    this.productsLoaded = true;
    this.projectLoaded = true;
  },
  computed: {
    /** Computed value for the project status. */
    projectStatus() {
      return this.project.status.toUpperCase().replace("_", " ");
    },

    /** Computed value for the project due date. */
    dueDate() {
      return this.project.dueDate.split("T")[0];
    },
  },

  methods: {
    /** Formats the resources so that they can be displayed in the table. */
    formatProducts() {
      if (!this.project.resources) {
        return; // TODO: Add empty table values.
      }

      this.project.resources.forEach((resource) => {
        this.products.push({
          id: resource.product.id,
          product: resource.product.productName,
          quantity: resource.quantity,
        });
      });
    },
  },
};
</script>

<style scoped>
.card {
  box-shadow: var(--custom-box-shadow);
}

.project-title {
  color: var(--bs-gray-900);
}

.success-badge {
  color: #15803d;
  background-color: #bbf7d0;
}

.in-progress-badge {
  color: #a16207;
  background-color: #fef08a;
}

.upcoming-badge {
  color: #1d4ed8;
  background-color: #bfdbfe;
}

.fw-normal {
  color: var(--bs-gray-700);
}
</style>
