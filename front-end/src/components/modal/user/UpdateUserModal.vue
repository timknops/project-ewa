<template>
  <form>
    <!--    user id-->
    <div class="mb-3">
      <label for="id" class="form-label fw-bold">ID</label>
      <input id="id" type="text" class="form-control" :value="modalItem.id" disabled>
    </div>
    <!--    team id-->
    <div class="mb-3">
      <label for="id" class="form-label fw-bold">ID</label>
      <input id="id" type="text" class="form-control" :value="modalItem.id" disabled>
    </div>
    <!--    name of the user-->
    <div class="mb-3">
      <label for="user-name" class="form-label fw-bold">User name</label>
      <input id="user-name"
             type="text"
             class="form-control"
             :class="{'border-danger': nameEmpty}"
             v-model.lazy.trim="modalItem.name"
             @blur="validateName">
      <p v-if="nameEmpty" class="text-danger"> The name can't be empty!</p>
    </div>
    <!--    email of the user-->
    <div class="mb-3">
      <label for="email" class="form-label fw-bold">e-mail</label>
      <input id="email"
             type="email"
             class="form-control"
             :class="{'border-danger': emailValid}"
             v-model.lazy.trim="modalItem.email"
             @blur="validateEmail">
      <p v-if="emailEmpty" class="text-danger"> The e-mail can't be empty!</p>
      <p v-if="emailValid" class="text-danger"> The e-mail isn't valid!</p>
    </div>
    <!--    password of the user-->
    <div class="mb-3">
      <label for="password" class="form-label fw-bold">password</label>
      <input id="user-name"
             type="password"
             class="form-control"
             :class="{'border-danger': passwordEmpty}"
             v-model.lazy.trim="modalItem.password"
             @blur="validatePassword">
      <p v-if="passwordEmpty" class="text-danger"> The password can't be empty!</p>
    </div>
  </form>
</template>

<script>
export default {
  name: "UpdateUserModal",
  props: ['item'],
  data() {
    return {
      modalItem: {},
      hasError: false,
      nameEmpty: false,
      emailEmpty: false,
      emailValid: false,
      passwordEmpty: false,
    };
  },
  created() {
    this.modalItem = Object.assign({}, this.item)
  },
  methods: {
    validateName() {
      this.nameEmpty = this.modalItem.name.length === 0;
      this.hasError = this.modalItem.name.length === 0;
    },

    validateEmail() {
      this.emailEmpty = this.modalItem.email.length === 0;
      const regex = /^[^s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailValid = this.modalItem.email.match(regex);
      //this.emailValid = regex.test(this.modalItem.email)
      this.hasError = !this.emailValid;
    },

    validatePassword() {
      this.passwordEmpty = this.modalItem.password.length === 0;
      this.hasError = this.modalItem.password.length === 0;
    }
  }

}
</script>

<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>
