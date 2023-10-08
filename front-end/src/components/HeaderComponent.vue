
<template>
  <header class="bg-white">
    <div class="row justify-content-end">
      <div class="col-auto d-flex">
        <p>user</p>
        <p>role</p>
      </div>
    </div>
    <div class="row">
      <div class="rounded-5 col-auto bg-gray" v-if="$route.meta.icon">
        <font-awesome-icon  :icon="$route.meta.icon"></font-awesome-icon>
      </div>
      <h2 class="col-auto fs-3"> {{$route.path}} </h2>
    </div>
  </header>
</template>

<script>
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

export default {
  name: "HeaderComponent",
  components: {FontAwesomeIcon}
}
</script>
<style scoped>
.bg-white {
  background-color: var(--color-bg);
}
.bg-gray {
  background-color: var(--bs-gray-100);
}
</style>