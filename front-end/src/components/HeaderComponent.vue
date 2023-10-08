
<template>
  <header class="bg-white mb-3">
    <div class="row mx-0 px-3 justify-content-end border-bottom align-items-center">
      <div class="col-auto d-flex flex-column text-end">
        <span>logged in as: user</span>
        <span>role</span>
      </div>
    </div>
    <div class="row mx-0 px-3 py-1 justify-content-start align-items-center">
      <div class="col-auto color-primary" v-if="$route.meta?.icon">
        <font-awesome-icon  :icon="$route.meta.icon"></font-awesome-icon>
      </div>
      <h2 class="col-auto fs-4 mb-0 color-primary"> {{displayCurrentPageName}} </h2>
    </div>
  </header>
</template>

<script>
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

export default {
  name: "HeaderComponent",
  components: {FontAwesomeIcon},
  computed: {
    displayCurrentPageName() {
      const pageName = this.$route.matched[0].path
      return pageName.substring(1,2).toUpperCase().concat(pageName.substring(2))
    }
  }
}
</script>
<style scoped>
.bg-white {
  background-color: var(--color-bg);
}
.color-primary {
  color: var(--color-primary);
}
</style>