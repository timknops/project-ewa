<template>
  <div class="container-fluid">
    <div class="col-md-4 offset-md-4 card my-5">
      <!--            logo-->
      <div class="text-center logo-header">
        <img src="../assets/solar_logo.svg" class="logo-image-pic" alt="logo" />
      </div>
      <!--        grey card border-->
      <form class="card-body p-lg-5 card-color-grey set-font needs-validation">
        <h2 class="login-h2">Login</h2>
        <!--            username-->
        <div class="row form-group username">
          <label for="usernameInputLabel">Name:</label>
          <input
            type="text"
            class="form-control"
            id="usernameInputLabel"
            placeholder="Enter name"
            v-model.trim.lazy="input.username1"
            required
          />
        </div>
        <!--            password-->
        <div class="row form-group password mt-4">
          <label for="passwordInputLabel">Password:</label>
          <input
            type="password"
            class="form-control"
            id="passwordInputLabel"
            placeholder="Enter password"
            v-model.trim.lazy="input.password1"
            required
          />
          <!--              Forgot password-->
          <small
            style="cursor: pointer"
            @click="forgotPassword"
            id="forgotPassword"
            class="form-text text-muted"
            >Forgot password?</small
          >
        </div>
        <!--            error message-->
        <p v-if="correctLogin" class="error-message">{{ errorMessage }}</p>
        <div class="text-center mt-3">
          <!--              button-->
          <button
            class="btn btn-primary login-button"
            type="button"
            v-on:click="login()"
          >
            Login
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { userLogin } from "@/models/userLogin";

export default {
  name: "LoginPage",
  data() {
    return {
      //input variables for the entered userdata
      input: {
        username1: "",
        password1: "",
      },
      loggedInActive: {},
      user1: {},
      correctLogin: null,
      errorMessage: "",
    };
  },
  methods: {
    /**
     * Login method that checks the entered userinfo
     *
     * Checks if one of the fields is empty, if it is then change the error message and display it
     *
     * if the entered username and password are the same as the dummy data then change the localstorage item and
     * redirect to the dashboard
     *
     * if not then notify the user with an error message that login details are incorrect
     */
    login() {
      if (this.input.username1 === "" || this.input.password1 === "") {
        this.errorMessage = "One of the fields is empty";
        this.correctLogin = true;
      } else if (
        this.input.username1 === this.user1.username &&
        this.input.password1 === this.user1.password
      ) {
        localStorage.setItem("loggedIn", true);
        this.$emit("updateLoggedIn", true);
        this.$router.push("/dashboard");
      } else {
        this.errorMessage = "Your login details are incorrect";
        this.correctLogin = true;
      }
    },
    /**
     * very simple method to give someone an alert if they request a password reset
     */
    forgotPassword() {
      localStorage.setItem("resetLogin", true);
      this.$emit("updateResetLogin", true);
      this.$router.push("/loginReset");
    },
  },
  created() {
    this.user1 = userLogin.dummyData();
    console.log(this.user1);
  },
};
</script>

<style scoped>
.card-color-grey {
  background-color: #f9fafb;
}
.logo-header {
  background-color: #f9fafb;
}

.logo-image-pic {
  height: 15rem;
  width: 15rem;
}

.login-h2 {
  margin-top: -3rem;
}

.username {
  margin-top: 3rem;
}

.error-message {
  color: red;
}

.form-control:hover,
.form-control:focus,
.form-control:valid {
  border-color: var(--color-primary);
  box-shadow: var(--custom-box-shadow);
}

.form-control:-webkit-autofill,
.form-control:-webkit-autofill:focus {
  -webkit-box-shadow: 0 0 0px 1000px white inset;
}
</style>
