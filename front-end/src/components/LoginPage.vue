<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4 offset-md-4">
        <!--        grey card border-->
        <div class="card my-5">
          <form class="card-body p-lg-5 card-color-grey set-font needs-validation">
            <!--            logo-->
            <div class="text-center">
              <img src="../assets/solar_logo.svg" class="logo-image-pic" alt="logo">
            </div>
            <!--            username-->
            <div class="row form-group username">
              <label for="usernameInputLabel">Username:</label>
              <input type="text" class="form-control" id="usernameInputLabel" placeholder="Enter username"
                     v-model.trim.lazy="input.username1" required>
            </div>
            <!--            password-->
            <div class="row form-group password mt-5">
              <label for="passwordInputLabel">Password:</label>
              <input type="password" class="form-control" id="passwordInputLabel" placeholder="Enter password"
                     v-model.trim.lazy="input.password1" required>
              <!--              Forgot password-->
              <small style="cursor: pointer;" @click="forgotPassword" id="forgotPassword" class="form-text text-muted">Forgot
                password?</small>
            </div>
            <!--            error message-->
            <p v-if="correctLogin" class="error-message">{{ errorMessage }}</p>
            <div class="text-center mt-3">
              <!--              button-->
              <button class="btn btn-primary login-button" type="button" v-on:click="login()">Login</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {userLogin} from "@/models/userLogin";
//localStorage.setItem('loggedIn', false)
export default {
  name: "LoginPage",
  data() {
    return {
      //input variables for the entered userdata
      input: {
        username1: "",
        password1: ""
      },
      loggedInActive: {},
      user1: {},
      correctLogin: null,
      errorMessage: ""
    }
  },
  watch: {},
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
        console.log("One of the fields is empty")
        this.errorMessage = "One of the fields is empty"
        this.correctLogin = true
      } else if (this.input.username1 === this.user1.username &&
          this.input.password1 === this.user1.password) {
        localStorage.setItem('loggedIn', true)
        this.$emit('updateLoggedIn', true)
        this.$router.push("/dashboard")
      } else {
        this.errorMessage = "Your login details are incorrect"
        this.correctLogin = true
      }
    },
    /**
     * very simple method to give someone an alert if they request a password reset
     */
    forgotPassword() {
      alert("A request to reset your password has beent sent your e-mail")
    }
  },
  created() {
    this.user1 = userLogin.dummyData()
  }
}

</script>

<style scoped>
.set-font {
  font-family: 'Montserrat', sans-serif;
}

.card-color-grey {
  background-color: #F9FAFB;
}

.logo-image-pic {
  margin-top: -3rem;
  height: 15rem;
  width: 15rem;
}

/*normal state of button*/
.login-button {
  border-color: white;
  background-color: var(--color-primary);
  color: var(--color-text);
}

/*state when hover or focused*/
.login-button:hover, .login-button:focus {
  border-color: white;
  background-color: var(--color-secondary) !important;
  color: var(--color-text-bg) !important;
}

/*state when clicked*/
.login-button:active {
  border-color: white;
  background-color: var(--color-primary) !important;
  color: var(--color-text-bg);
}

.error-message {
  color: red;
}
</style>
