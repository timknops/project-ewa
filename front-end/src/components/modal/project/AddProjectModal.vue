<template>
  <form>
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
          <option v-for="team in TEAM_OPTIONS" :key="team" :value="team">
            {{ team }}
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
          <option selected value="">Assign status</option>
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
        <label for="products" class="form-label fw-bold m-0">Products</label>
        <div class="d-flex gap-2">
          <button class="btn py-1 custom-btn">
            <FontAwesomeIcon icon="fa-solid fa-plus" size="sm" />
          </button>
          <button class="btn py-1 custom-btn">
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
                <th scope="col" class="font-small">WAREHOUSE</th>
                <th scope="col" class="font-small">QUANTITY</th>
                <th scope="col" class="font-small"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(product, index) in modalItem.products" :key="index">
                <td>
                  <select class="form-select" aria-label="product">
                    <option selected value="">Choose product</option>
                    <option
                      v-for="productName in PRODUCT_NAMES"
                      :key="productName"
                      :value="productName"
                    >
                      {{ productName }}
                    </option>
                  </select>
                </td>

                <td>
                  <select class="form-select" aria-label="warehouse">
                    <option selected value="">Assign warehouse</option>
                    <option
                      v-for="warehouse in WAREHOUSE_OPTIONS"
                      :key="warehouse"
                      :value="warehouse"
                    >
                      {{ warehouse }}
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
                      class="form-check-input align-middle m-0"
                      type="checkbox"
                      value=""
                      id=""
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
        products: [
          {
            id: 0,
            name: "",
            quantity: 0,
            warehouse: "",
          },
        ],
      },
      nameEmpty: false,
      dueDateEmpty: false,
      teamUnselected: false,
      statusUnselected: false,
      // Should all be retrieved from the database.
      TEAM_OPTIONS: ["Team 1", "Team 2", "Team 3"],
      STATUS_OPTIONS: ["Upcoming", "In Progress", "Completed"],
      WAREHOUSE_OPTIONS: ["Warehouse 1", "Warehouse 2", "Warehouse 3"],
      PRODUCT_NAMES: ["Product 1", "Product 2", "Product 3"],
    };
  },
  computed: {
    hasError() {
      this.validateName();
      this.validateDueDate();
      this.validateTeam();
      this.validateStatus();
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
    validateStatus() {
      this.statusUnselected = this.modalItem.status.length === 0;
    },
  },
  components: { FontAwesomeIcon },
};
</script>

<style scope>
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
</style>
