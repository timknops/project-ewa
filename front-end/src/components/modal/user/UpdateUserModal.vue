<template>
  <form>
    <!--    user id-->
    <div class="mb-3">
      <label for="id" class="form-label fw-bold">ID</label>
      <input id="id" type="text" class="form-control" :value="modalItem.id" disabled>
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
          {{ team.team }}
        </option>
      </select>
      <p v-if="teamUnselected" class="text-danger"> Team has to be selected!</p>
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
      <p v-else-if="emailValid" class="text-danger"> The e-mail isn't valid!</p>
    </div>
    <!--    type of user-->
    <div class="mb-3">
      <label for="userType" class="form-label fw-bold">Type</label>
      <select class="form-select" v-model="modalItem.type">
        <option>{{ "VIEWER" }}</option>
        <option>{{ "ADMIN" }}</option>
      </select>
    </div>
  </form>
</template>

<script>
/**
 * Modal for editing a user
 */
export default {
  name: "UpdateUserModal",
  inject: ['teamsService'],
  computed: {
    hasError() {
      this.validateName();
      this.validateEmail();
      this.validateTeam();
      return this.nameEmpty || this.emailEmpty || this.emailValid || this.teamUnselected;
    }
  },
  props: ['item'],
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
      teams: []
    };
  },
  async created() {
    await this.getTeams();
    this.modalItem = Object.assign({}, this.item)
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
    }
  }

}
</script>

<style scoped>
.form-control:disabled {
  background-color: var(--bs-body-bg);
}
</style>
