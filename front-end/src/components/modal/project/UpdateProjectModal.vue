<template>
  <form v-if="this.dropdownData !== null">
    <div class="mb-3">
      <!-- Project name -->
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
      <!-- Client -->
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

      <!-- Due date -->
      <div class="col">
        <label for="due-date" class="form-label fw-bold"
          >Due date <span class="text-danger">*</span></label
        >
        <input
          v-model.lazy.trim="modalItem.dueDate"
          @change="validateDueDate"
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
      <!-- Team -->
      <div class="col">
        <label for="team-select" class="form-label fw-bold"
          >Team <span class="text-danger">*</span></label
        >
        <select
          v-model="modalItem.team"
          @change="validateTeam"
          class="form-select"
          :class="{ 'border-danger': teamUnselected }"
          id="team-select"
          aria-label="team"
        >
          <option selected value="">Assign team</option>
          <option v-for="team in TEAM_OPTIONS" :key="team.id" :value="team.id">
            {{ team.team }}
          </option>
        </select>
        <small v-if="teamUnselected" class="text-danger"
          >The team can't be unassigned!</small
        >
      </div>

      <!-- Status -->
      <div class="col">
        <label for="status-select" class="form-label fw-bold"
          >Status <span class="text-danger">*</span></label
        >
        <select
          v-model="modalItem.status"
          @change="validateStatus"
          :class="{ 'border-danger': statusUnselected }"
          class="form-select"
          id="status-select"
          aria-label="status"
        >
          <option value="">Assign status</option>
          <option
            v-for="status in STATUS_OPTIONS"
            :key="status"
            :value="status"
          >
            {{ status }}
          </option>
        </select>
        <small v-if="statusUnselected" class="text-danger"
          >The status can't be unassigned!</small
        >
      </div>
    </div>

    <!-- Description -->
    <div class="mb-2">
      <label for="description" class="form-label fw-bold">Description</label>
      <textarea
        v-model.lazy.trim="modalItem.description"
        type="text"
        class="form-control"
        id="description"
        placeholder="Enter the project description"
      ></textarea>
    </div>

    <hr class="hr" />

    <!-- Products -->
    <div class="mb-3">
      <div
        class="d-flex gap-2 justify-content-between align-items-center mb-2 px-2"
      >
        <label for="products" class="form-label fw-bold mt-1">Products</label>
        <div class="d-flex gap-2">
          <button @click="addProduct" class="btn py-1 custom-btn" type="button">
            <FontAwesomeIcon icon="fa-solid fa-plus" size="sm" />
          </button>
          <button
            @click="deleteSelectedProducts"
            class="btn py-1 custom-btn"
            type="button"
          >
            <FontAwesomeIcon icon="fa-solid fa-trash" size="sm" />
          </button>
        </div>
      </div>

      <div class="card border-0">
        <div class="table-responsive">
          <table class="table mb-0" id="products">
            <thead>
              <tr>
                <th scope="col" class="font-small">NAME</th>
                <th scope="col" class="font-small">QUANTITY</th>
                <th scope="col" class="font-small"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="modalItem.products.length === 0">
                <td colspan="3" class="text-center empty-text">
                  No products added yet.
                </td>
              </tr>
              <tr
                v-else
                v-for="(product, index) in modalItem.products"
                :key="index"
              >
                <td>
                  <select
                    v-model="product.product_id"
                    class="form-select"
                    aria-label="product"
                  >
                    <option value="">Choose product</option>
                    <option
                      v-for="product in PRODUCT_NAMES"
                      :key="product.id"
                      :value="product.id"
                    >
                      {{ product.product_name }}
                    </option>
                  </select>
                </td>

                <td>
                  <input
                    v-model.lazy.trim="product.quantity"
                    type="number"
                    class="form-control w-50"
                  />
                </td>

                <td>
                  <div class="checkbox-container">
                    <input
                      v-model="product.selected"
                      class="form-check-input align-middle m-0"
                      type="checkbox"
                    />
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </form>
</template>

<script>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/**
 * Update project modal component.
 *
 * @param {Object} item The project to be updated.
 * @returns {Object} The update project modal component.
 * @author Tim Knops
 */
export default {
  name: "UpdateProjectModal",
  components: { FontAwesomeIcon },
  inject: ["projectService"],
  props: ["item"],
  data() {
    return {
      dropdownData: null,
      modalItem: {},
      nameEmpty: false,
      teamUnselected: false,
      dueDateEmpty: false,
      statusUnselected: false,
      STATUS_OPTIONS: ["Upcoming", "In Progress", "Completed"],
      TEAM_OPTIONS: [],
      PRODUCT_NAMES: [],
    };
  },
  computed: {
    /** Checks if the modal item has an error. */
    hasError() {
      this.validateName();
      this.validateDueDate();
      this.validateTeam();
      this.validateStatus();

      return (
        this.nameEmpty ||
        this.dueDateEmpty ||
        this.teamUnselected ||
        this.statusUnselected
      );
    },
  },
  async created() {
    this.dropdownData = await this.projectService.getProjectModalData(); // Get the dropdown data for the project.

    this.TEAM_OPTIONS = this.dropdownData.teams;
    this.PRODUCT_NAMES = this.dropdownData.products;

    this.formatModalItem();
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
    validateStatus() {
      this.statusUnselected = this.modalItem.status.length === 0;
    },

    /** Format the item so that it matches the form and the add modal. */
    formatModalItem() {
      this.modalItem = {
        id: this.item.id,
        projectName: this.item.projectName,
        client: this.item.client,
        dueDate: this.item.dueDate.split("T")[0],
        team: this.item.teamId,
        status: this.formatStatus(this.item.status),
        description: this.item.description,
        products: this.item.resources.map((product) => ({
          product_id: product.product.id,
          selected: false,
          quantity: product.quantity,
        })),
      };
    },

    /**
     * Formats the status to the correct format for the request.
     * @param {String} status The status to be formatted.
     */
    formatStatus(status) {
      switch (status) {
        case "IN_PROGRESS":
          return "In Progress";
        case "COMPLETED":
          return "Completed";
        default:
          return "Upcoming";
      }
    },

    /** Adds a new product to the products array. */
    addProduct() {
      this.modalItem.products.push({
        id: this.modalItem.products.length,
        product_id: "",
        quantity: 0,
        selected: false,
      });
    },

    /** Deletes the selected products from the products array. */
    deleteSelectedProducts() {
      if (this.modalItem.products.length === 0) return;

      this.modalItem.products = this.modalItem.products.filter(
        (product) => !product.selected
      );
    },
  },
};
</script>

<style>
label {
  margin-bottom: 4px !important;
}

.font-small {
  font-size: 0.9rem !important;
  color: var(--bs-gray-800) !important;
}

.custom-btn {
  background-color: var(--bs-gray-200) !important;
}
.custom-btn:hover {
  background-color: var(--bs-gray-400) !important;
}

.checkbox-container {
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
  height: 38px !important;
}

.empty-text {
  color: var(--bs-gray-800) !important;
}
</style>
