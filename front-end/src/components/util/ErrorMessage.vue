<template>
  <div class="container text-center mt-5">
    <h2 class="fw-bold">
      {{ errorTitle }}
    </h2>
    <p class="fs-4">
      {{ errorMessage }}
    </p>
    <a @click="redirectTo" class="btn btn-primary"> Go back </a>
  </div>
</template>

<script>
/**
 * This file represents the ErrorMessage component. The component is responsible for displaying an error message.
 *
 * @param {String} errorTitle The title of the error message.
 * @param {String} errorMessage The error message.
 * @param {String} hyperlink The hyperlink to go back to whichever page.
 *
 * @author Tim Knops
 */
export default {
  name: "ErrorMessageView",
  props: {
    errorTitle: {
      type: String,
      required: true,
    },
    errorMessage: {
      type: String,
      required: true,
    },
    hyperlink: {
      type: String,
      required: true,
    },
  },
  methods: {
    /** Redirects the user to the page specified in the hyperlink prop. */
    redirectTo() {
      this.$router.push(this.hyperlink);
    },
  },
};
</script>

<style scoped></style>
