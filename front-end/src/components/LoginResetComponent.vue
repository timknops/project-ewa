<template>
  <!--  TODO add redirecting delays with information to the user that something is happening-->
  <div class="container-fluid">
    <!--    success alert when successfully logged in-->
    <div v-if="alert1" class="alert alert-success text-center">
      <p>Success! An email with a link to reset your password has been sent to your email address</p>
    </div>
    <div class="col-md-4 offset-md-4 card my-5">
      <!--      header of email request-->
      <div v-if="!newPassword" class="text-center reset-header">
        <font-awesome-icon
            icon="fa-regular fa-envelope"
            class="lock-icon"
        />
      </div>
      <!--      header of password reset-->
      <div v-else class="text-center reset-header">
        <font-awesome-icon
            icon="fa-solid fa-user-lock"
            class="lock-icon"
        />
      </div>
      <div v-if="!newPassword">
        <!--        cancel button-->
        <div class="float-end rounded-start-2 mt-3 cancel-button text-center p-2 px-4"
             @click="navigateToLogin">
          go back
        </div>
        <!--        grey card border-->
        <form
            class="card-body p-lg-5 card-color-grey set-font needs-validation"
        >
          <h2>Recover Password</h2>
          <p>Enter the email address of the account that you want to recover</p>

          <div class="row form-group email">
            <label for="emailInputLabel">E-mail:</label>
            <input
                type="email"
                class="form-control"
                id="emailInputLabel"
                placeholder="example@domain.com"
                required
                v-model.lazy.trim="input.email"
            />
            <p v-if="!correctEmail" class="error-message">{{ errorMessage }}</p>
            <div class="text-center mt-3">
              <!--              button-->
              <button
                  class="btn btn-primary login-button"
                  type="button"
                  v-on:click="mailSubmit()">
                Submit
              </button>
            </div>
          </div>
        </form>
      </div>
      <div v-else>
        <!--        cancel button-->
        <div class="float-end rounded-start-2 mt-3 cancel-button text-center p-2 px-4"
             @click="navigateToLogin">
          go back
        </div>
        <form
            class="card-body p-lg-5 card-color-grey set-font needs-validation"
        >
          <h2>Create a new password</h2>
          <p>Enter a new and secure password for your account.
            You'll be redirected when the password has been changed.
          </p>

          <!--            password-->
          <div class="form-group password mt-5">
            <label for="password1InputLabel">New password:</label>
            <input
                type="password"
                class="form-control"
                id="password1InputLabel"
                placeholder="Enter password"
                v-model.trim.lazy="password1"
                required
            />
            <!--        confirm password-->
            <div class="form-group password mt-3">
              <label for="password2InputLabel">Confirm password:</label>
              <input
                  type="password"
                  class="form-control"
                  id="password2InputLabel"
                  placeholder="Enter password"
                  v-model.trim.lazy="password2"
                  required
              />
            </div>
            <p v-if="!correctEmail" class="error-message">{{ errorMessage }}</p>
            <div class="text-center mt-3">
              <!--              button-->
              <button
                  class="btn btn-primary password-button"
                  type="button"
                  v-on:click="passwordSubmit()"
              >
                Submit
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginResetComponent",
  inject: ['userService',
    'emailService'],
  data() {
    return {
      users: [],
      input: {
        email: "",
      },
      user: null,
      password1: null,
      password2: null,
      correctEmail: null,
      correctPassword: null,
      errorMessage: null,
      newPassword: false,
      alert1: false,
      bodySplitter: []
    }
  },
  watch: {
    '$route'(){
      this.bodySplitter = this.$route.params.email.split("_", 3);
      this.user = this.findByEmail(this.bodySplitter[0]);
      if (this.bodySplitter.length === 3) {
        this.newPassword = true;
      }
    }
  },
  async created() {
    this.bodySplitter = this.$route.params.email.split("_", 3);
    this.users = await this.userService.asyncFindAll();
    this.user = this.findByEmail(this.bodySplitter[0]);
    if (this.bodySplitter.length === 3) {
      this.newPassword = true;
    }
  },
  methods: {
    //TODO seperate clear fields method
    mailSubmit() {
      if (this.input.email === "") {
        this.correctEmail = false;
        this.errorMessage = "Email cannot be empty";
      } else {
        if (this.validEmail()) {
          if (this.findByEmail(this.input.email) != null) {
            this.sendEmail();
            this.alert1 = true;
            console.log(this.alert1)
            this.errorMessage = null;
          } else {
            this.correctEmail = false;
            this.errorMessage = "User for given email does not exist"
          }
        } else {
          this.correctEmail = false;
          this.errorMessage = "Did not enter a valid email"
        }
      }
    },
    validEmail() {
      const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return this.input.email.match(regex);
    },
    passwordSubmit() {
      if (!this.password1 || !this.password2) {
        this.correctPassword = false;
        this.errorMessage = "Please fill in both forms"
      } else if (this.password1 === this.password2) {
        if (this.password1 === this.user.password) {
          this.correctPassword = false;
          this.errorMessage = "New password can't be the same as old password"
        } else {
          this.correctPassword = true;
          this.newPassword = false;
          this.passwordUpdate(this.password1);
          this.navigateToLogin();
          this.clearFields();
        }
      } else {
        this.correctPassword = false;
        this.errorMessage = "Passwords do not match"
      }
    },
    findByEmail(email) {
      this.user = this.users.find((user) => user.email === email);
      return this.user
    },
    navigateToLogin() {
      this.clearFields();
      localStorage.setItem("resetLogin", false);
      this.$emit("updateResetLogin", false);
      this.$router.push("/loginPage");
    },
    clearFields() {
      this.user = null;
      this.password1 = null;
      this.password2 = null;
      this.errorMessage = null;
    },
    async passwordUpdate(password) {
      try {
        this.user.password = password
        await this.userService.asyncSave(this.user);
      } catch (e) {
        console.log(e)
      }
    },
    sendEmail() {
      this.user = this.findByEmail(this.input.email);
      try {
        this.emailService.asyncSendMail(this.input.email + "_" + this.user.name + "_" + this.user.id);
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>

<style scoped>
.cancel-button {
  color: white;
  background-color: var(--color-primary);
  cursor: pointer;
}

.card-color-grey {
  background-color: #f9fafb;
}

.lock-icon {
  height: 12rem;
  width: 12rem;
  color: var(--color-secondary);
}

.email {
  margin-top: 3rem;
}

.reset-header {
  background-color: var(--color-primary);
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
