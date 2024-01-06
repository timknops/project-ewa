<template>
  <p>Log out in progress, please wait...</p>
  <spinner-component></spinner-component>
</template>

<script>
import SpinnerComponent from "@/components/util/SpinnerComponent.vue";

export default {
  name: "LogoutPage",
  components: {SpinnerComponent},
  inject: ['sessionService'],
  //log the user out and redirect to the login page
  created() {
    this.sessionService.logOut();
    this.$router.push("/login")
  }
}
</script>

<style scoped>

</style>
