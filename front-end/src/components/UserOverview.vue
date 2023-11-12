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

export default {
  name: "UserOverview",
  components: {TableComponent, ModalComponent},
  inject: ['userService'],
  data() {
    return {
      users: [],
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
    showDeleteModal(user) {
      this.modalTitle = "Delete user"
      this.modalBodyComponent = this.MODAL_TYPES.DELETE
      this.modalUser = user
      this.okBtnText = "Delete"
      this.showModal = true
    },

    showEditModal(user) {
      this.modalTitle = "Update user"
      this.modalBodyComponent = this.MODAL_TYPES.UPDATE
      this.modalUser = user
      this.okBtnText = "Save"
      this.showModal = true;
    },

    showAddModal() {
      this.modalTitle = "Add user"
      this.modalBodyComponent = this.MODAL_TYPES.ADD
      this.okBtnText = "Add"
      this.showModal = true
    },

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

    async onUserAdd(user) {
      try {
        const addedUser = await this.userService.asyncAdd(user)
        this.users.push(addedUser)
        this.showModal = false;
      } catch (e) {
        console.log(e)
      }
    },

    async onUserUpdate(user) {
      try {
        const updatedUser = await this.userService.asyncSave(user)
        this.users = this.users.map((user) => user.id === updatedUser.id ? updatedUser : user)
        this.showModal = false;
      } catch (e) {
        console.log(e)
      }
    },

    async onUserDelete(user) {
      try {
        const deletedUser = await this.userService.asyncDelete(user.id);
        this.users = this.users.filter((user) => user.id !== deletedUser.id)
        this.showModal = false;
      } catch (e) {
        console.log(e)
      }
    },
  }
};
</script>

<style scoped></style>
