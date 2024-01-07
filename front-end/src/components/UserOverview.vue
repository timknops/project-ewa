<template>
  <div>
    <table-component
        v-if="userList.length > 0"
        :amount-to-display="5"
        :has-add-button="true"
        :has-delete-button="true"
        :has-edit-button="true"
        :table-data="userList"
        @edit="showEditModal"
        @delete="showDeleteModal"
        @add="showAddModal"
    />
    <SpinnerComponent v-else/>
    <Transition>
      <modal-component
          v-if="showModal"
          :title="modalTitle"
          :active-modal="modalBodyComponent"
          :item="modalUser"
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
import ModalComponent from "@/components/modal/ModalComponent.vue";
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";
import ToastComponent from "@/components/util/ToastComponent.vue";

/**
 * Component to display and manage the users for the admins
 *
 * Not all fields of the user are displayed, password for example is not displayed for security reasons
 */
export default {
  name: "UserOverview",
  components: {
    TableComponent,
    ModalComponent,
    SpinnerComponent,
    ToastComponent,
  },
  inject: ["userService"],
  data() {
    return {
      userList: {
        id: Number,
        team: String,
        email: String,
        name: String,
        type: String
      },
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalUser: null,
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-user-modal",
        UPDATE: "update-user-modal",
        ADD: "add-user-modal",
      }),
      showToast: false,
      toastTitle: "",
      toastMessage: "",
    };
  },
  async created() {
    const data = await this.userService.asyncFindAll();

    this.userList = data.map((user) => {
      return this.formatUserForTable(user)
    })
  },
  methods: {
    /**
     * When clicked on the delete button in the table, it will show the modal for deleting
     * @param user that's being selected for deletion
     */
    showDeleteModal(user) {
      this.modalTitle = "Delete user";
      this.modalBodyComponent = this.MODAL_TYPES.DELETE;
      this.modalUser = user;
      this.okBtnText = "Delete";
      this.showModal = true;
    },
    /**
     * When clicked on the edit button in the table, it will show the modal for editing an existing user
     * @param user that's being selected for editing
     */
    async showEditModal(user) {
      this.modalTitle = "Update user";
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE;
      this.modalUser = await this.userService.asyncFindById(user.id);
      this.okBtnText = "Save";
      this.showModal = true;
    },
    /**
     * When clicked on the add button in the table, it will show the modal for adding a new user
     */
    showAddModal() {
      this.modalTitle = "Add user";
      this.modalBodyComponent = this.MODAL_TYPES.ADD;
      this.okBtnText = "Add";
      this.showModal = true;
    },

    /**
     * Method that decides what type of method needs to be executed depending on what type of modal was selected
     * Delete is delete, update is update and add is add
     * @param user that's currently selected for the modal methods
     * @param modal the type of modal that was clicked on
     */
    handleOk(user, modal) {
      switch (modal) {
        case this.MODAL_TYPES.DELETE:
          this.onUserDelete(user);
          break;
        case this.MODAL_TYPES.UPDATE:
          this.onUserUpdate(user);
          break;
        case this.MODAL_TYPES.ADD:
          this.onUserAdd(user);
          break;
      }
    },

    //helper method to transform the user data received from the back-end to a more appropriate format for the table
    formatUserForTable(user) {
      return {
        id: user.id,
        team: user.team?.team,
        email: user.email,
        name: user.name,
        type: user.type
      }
    },
    /**
     * Adds a user to the backend user list, also to the user arrays
     * @param user that got created to be added
     * @returns {Promise<void>}
     */
    async onUserAdd(user) {
      try {
        const addedUser = await this.userService.asyncAdd(user);
        this.userList.push(this.formatUserForTable(addedUser));

        this.showModal = false;
        this.showTimedToast("Added user", "Successfully added the user");
      } catch (e) {
        this.showModal = false;

        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to add user", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to add user", e.message, 8000);
        }
      }
    },
    /**
     * Updates a user in the backend, also updates it in the two user arrays
     * @param user that's been edited in the modal and is about to be edited in the backend
     * @returns {Promise<void>}
     */
    async onUserUpdate(user) {
      try {
        const updatedUser = await this.userService.asyncSave(user);
        this.userList =
            this.userList.map((user) => user.id === updatedUser.id ? this.formatUserForTable(updatedUser) : user)

        this.showModal = false;
        this.showTimedToast("Updated user", "Successfully updated the user");
      } catch (e) {
        this.showModal = false;

        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to update user", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to update user", e.message, 8000);
        }
      }
    },
    /**
     * Deletes a user from the back end, also deletes it from the user arrays
     * @param user given to the backend to be deleted
     * @returns {Promise<void>}
     */
    async onUserDelete(user) {
      try {
        const deletedUser = await this.userService.asyncDelete(user.id);
        this.userList = this.userList.filter((user) => user.id !== deletedUser.id);

        this.showModal = false;
        this.showTimedToast("Deleted user", "Successfully deleted the user");
      } catch (e) {
        this.showModal = false;

        if (e.code >= 400 && e.code < 500) {
          this.showTimedToast("Failed to delete user", e.reason, 8000);
        } else {
          this.showTimedToast("Failed to delete user", e.message, 8000);
        }
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
  },
};
</script>

<style scoped></style>
