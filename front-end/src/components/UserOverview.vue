<template>
  <div>
    <table-component
        v-if="usersAdmin.length > 0"
        :amount-to-display="5"
        :has-add-button="true"
        :has-delete-button="true"
        :has-edit-button="true"
        :table-data="usersAdmin"
        @edit="showEditModal"
        @delete="showDeleteModal"
        @add="showAddModal"
    />
    <p v-else>Waiting....</p>
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
  </div>
</template>

<script>
import TableComponent from "@/components/table/TableComponent.vue";
import ModalComponent from "@/components/modal/ModalComponent.vue";


/**
 * Component to display and manage the users for the admins
 *
 * Currently, the admin view is being displayed. At this moment this excludes the password.
 * This is done so that the table can be displayed properly, since it bugs out at too many fields,
 * but also for security/privacy reasons.
 * The password should at least be able to be changed by the user using the forgot password function in the user.
 */
export default {
  name: "UserOverview",
  components: {TableComponent, ModalComponent},
  inject: ['userService'],
  data() {
    return {
      /**
       * Array of the full view including the passwords
       * Currently not used however can probably be used to change the password temporarily or crud from there instead
       * sending a whole new request
       */
      users: [],
      //array of the admin view excluding the password
      usersAdmin: [],
      showModal: false,
      modalTitle: "",
      modalBodyComponent: "",
      modalUser: null,
      okBtnText: "",
      MODAL_TYPES: Object.freeze({
        DELETE: "delete-user-modal",
        UPDATE: "update-user-modal",
        ADD: "add-user-modal"
      })
    };
  },
  async created() {
    this.users = await this.userService.asyncFindAll();
    this.usersAdmin = await this.userService.asyncFindAdmin();
  },
  methods: {
    /**
     * When clicked on the delete button in the table, it will show the modal for deleting
     * @param user that's being selected for deletion
     */
    showDeleteModal(user) {
      this.modalTitle = "Delete user"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalUser = user
      this.okBtnText = "Delete"
      this.showModal = true
    },
    /**
     * When clicked on the edit button in the table, it will show the modal for editing an existing user
     * @param user that's being selected for editing
     */
    async showEditModal(user) {
      this.modalTitle = "Update user"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalUser = user
      this.okBtnText = "Save"
      this.showModal = true;
    },
    /**
     * When clicked on the add button in the table, it will show the modal for adding a new user
     */
    showAddModal() {
      this.modalTitle = "Add user"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
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
    /**
     * Adds a user to the backend user list, also to the user arrays
     * @param user that got created to be added
     * @returns {Promise<void>}
     */
    async onUserAdd(user) {
      try {
        const addedUser = await this.userService.asyncAdd(user);
        //remove the user password, so it doesn't bug out the table
        delete addedUser.password;
        this.usersAdmin.push(addedUser);
        this.users.push(addedUser);
        this.showModal = false;
      } catch (e) {
        console.log(e)
      }
    },
    /**
     * Updates a user in the backend, also updates it in the two user arrays
     * @param user that's been edited in the modal and is about to be edited in the backend
     * @returns {Promise<void>}
     */
    async onUserUpdate(user) {
      try {
        //get the full current user
        const userFromBack = await this.userService.asyncFindById(user.id);
        //temporarily add the password to save it with the user
        user.password = userFromBack.password
        const updatedUser = await this.userService.asyncSave(user)
        //delete the password so that the table doesn't break
        delete updatedUser.password;
        this.usersAdmin = this.usersAdmin.map((user) => user.id === updatedUser.id ? updatedUser : user);
        this.users = this.users.map((user) => user.id === updatedUser.id ? updatedUser : user);
        this.showModal = false;
      } catch (e) {
        console.log(e)
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
        this.usersAdmin = this.usersAdmin.filter((user) => user.id !== deletedUser.id);
        this.users = this.users.filter((user) => user.id !== deletedUser.id);
        this.showModal = false;
      } catch (e) {
        console.log(e)
      }
    },
  }
};
</script>

<style scoped></style>
