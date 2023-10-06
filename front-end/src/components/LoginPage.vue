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
              <input type="text" class="form-control" id="usernameInputLabel" placeholder="Enter username" required>
            </div>
<!--            password-->
            <div class="row form-group password mt-5">
              <label for="passwordInputLabel">Password:</label>
              <input type="password" class="form-control" id="passwordInputLabel" placeholder="Enter password" required>
              <small id="forgotPassword" class="form-text text-muted">Forgot password?</small>
            </div>
            <div class="text-center mt-3">
            <button class="btn btn-primary login-button" type="submit">Login</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage"
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
  margin-top: -50px;
  height: 250px;
  width: 250px;
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
