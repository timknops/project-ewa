
<template>
  <div class="toast-container position-fixed top-0 end-0 p-3" >
    <div id="liveToast" class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="toast-header">
        <strong class="me-auto">
          {{toastTitle}}
        </strong>
        <small>now</small>
        <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
      <div class="toast-body">
          {{toastMessage}}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ToastComponent",
  props: {
    toastTitle: String,
    toastMessage: String
  },
}
</script>
<style scoped>

</style>