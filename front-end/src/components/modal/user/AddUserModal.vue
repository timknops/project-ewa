<template>
  <form>
    <!--    name of the user-->
    <div class="mb-3">
      <label for="user-name" class="form-label fw-bold">Name</label>
      <input id="user-name"
             type="text"
             class="form-control"
             :class="{'border-danger': nameEmpty}"
             v-model.lazy.trim="modalItem.name"
             placeholder="name"
             @blur="validateName">
      <p v-if="nameEmpty" class="text-danger"> The name can't be empty!</p>
    </div>

    <!--    team of the user-->
    <div class="mb-3">
      <label for="team" class="form-label fw-bold">Team</label>
      <select id="team"
             class="form-select"
             :class="{'border-danger': teamUnselected}"
             v-model="modalItem.team.id"
             @blur="validateTeam">
        <option selected value="">Select a team</option>
        <option v-for="team in teams" :key="team.id" :value="team.id">
          {{team.team}}
        </option>
      </select>
      <p v-if="teamUnselected" class="text-danger"> Team has to be selected!</p>
    </div>

    <!--    email of the user-->
    <div class="mb-3">
      <label for="email" class="form-label fw-bold">E-mail</label>
      <input id="email"
             type="email"
             class="form-control"
             :class="{'border-danger': emailValid}"
             v-model.lazy.trim="modalItem.email"
             placeholder="example@domain.com"
             @blur="validateEmail">
      <p v-if="emailEmpty" class="text-danger"> The e-mail can't be empty!</p>
      <p v-else-if="emailValid" class="text-danger"> The e-mail isn't valid!</p>
    </div>
    <!--    password of the user-->
    <div class="mb-3">
      <label for="password" class="form-label fw-bold">Password</label>
      <input id="password"
             type="password"
             class="form-control"
             :class="{'border-danger': passwordEmpty}"
             v-model.lazy.trim="modalItem.password"
             placeholder="password"
             @blur="validatePassword">
      <p v-if="passwordEmpty" class="text-danger"> The password can't be empty!</p>
    </div>
    <!--    type of user-->
    <div class="mb-3">
      <label for="userType" class="form-label fw-bold">Type</label>
      <select id="user-type-select" class="form-select" v-model="modalItem.type">
        <option>{{ "ADMIN" }}</option>
        <option>{{ "VIEWER" }}</option>
      </select>
    </div>
  </form>
</template>

<script>

/**
 * The modal for adding a user
 */
export default {
  name: "AddUserModal",
  inject: ['teamsService'],
  data() {
    return {
      modalItem: {
        id: 0,
        team: {
          id: ""
        },
        name: "",
        email: "",
        password: "",
        type: "VIEWER"
      },
      nameEmpty: false,
      teamUnselected: false,
      emailEmpty: false,
      emailValid: false,
      passwordEmpty: false,
      teams: []
    };
  },
  computed: {
    hasError() {
      this.validateName();
      this.validateTeam();
      this.validateEmail();
      this.validatePassword();
      return this.nameEmpty || this.emailEmpty || this.emailValid || this.passwordEmpty || this.teamUnselected;
    }
  },
  methods: {
    async getTeams() {
      this.teams = await this.teamsService.findAll();
    },
    validateName() {
      this.nameEmpty = this.modalItem.name.length === 0;
    },

    validateTeam(){
      this.teamUnselected = !this.modalItem.team.id;
    },

    validateEmail() {
      this.emailEmpty = this.modalItem.email.length === 0;
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.emailValid = !this.modalItem.email.match(regex);
    },

    validatePassword() {
      this.passwordEmpty = this.modalItem.password.length === 0;
    }
  },
  async created(){
    await this.getTeams();
  }
}
</script>

<style scoped>

</style>
