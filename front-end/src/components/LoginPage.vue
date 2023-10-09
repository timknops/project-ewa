<template>
  <div class="container">
    <div class="row">
      <div class="col-md-4 offset-md-4">
<!--        grey card border-->
        <div class="card my-5">
          <form class="card-body p-lg-5 cardcolorGrey setfont needs-validation">
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
              <small id="forgotPassword" class="form-text text-muted">Forgot password?</small>
            </div>
            <div class="text-center mt-3">
            <button class="btn btn-primary login-button" type="button" v-on:click = "login()">Login</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {user} from "@/models/user";
export default {
  name: "LoginPage",
  data(){
    return {
      //input variables for the entered userdata
      input: {
        username1: "",
        password1: ""
      },
      loggedInActive: {},
      user1: {}

    }
  },
  methods:{
    /**
     * Login method that checks the entered userinfo
     *
     * Checks if one of the fields is empty
     *
     * if the entered username and password are the same as the dummy data then redirect to the dashboard
     * if not then notify the user with an alert that login details are incorrect
     */
    login(){
      if (this.input.username1 === "" || this.input.password1 === ""){
        console.log("One of the fields is empty")
      } else if (this.input.username1 === this.user1.username &&
          this.input.password1 === this.user1.password){
        localStorage.setItem('loggedIn', true)
        console.log("Login:" + localStorage.getItem('loggedIn'))
        this.$router.push("/dashboard")
      } else {
        alert("Your login details are incorrect")
        console.log("Your login details are incorrect")
      }
    }
  },
  created() {
    this.user1 = user.dummyData()
  }
}

</script>

<style scoped>
.setfont{
  font-family: 'Montserrat', sans-serif;
}
.cardcolorGrey{
  background-color: #F9FAFB;
}
.logo-image-pic{
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
.login-button:hover, .login-button:focus{
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

</style>
