<template>
  <header class="bg-white mb-5">
    <div
      class="row mx-0 px-3 py-1 justify-content-end border-bottom align-items-center"
    >
      <div class="col-auto d-flex flex-column text-end">
        <span
          >Logged in as: <strong>{{ activeUser.name }}</strong></span
        >
        <span
          >Role: <strong>{{ activeUser.role }} </strong></span
        >
      </div>
    </div>
    <div class="row mx-0 px-5 py-2 justify-content-start align-items-center">
      <div
        class="col-auto color-primary bg-body-secondary py-2 rounded-5"
        v-if="$route.meta?.icon"
      >
        <font-awesome-icon :icon="$route.meta.icon"></font-awesome-icon>
      </div>
      <h2 class="col-auto fs-4 mb-0 color-primary fw-bold">
        {{ displayCurrentPageName }}
      </h2>
    </div>
  </header>
</template>

<script>
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/**
 * Header component to display the current page the user is on and the name and role of a user
 *
 * In the future it could contain a search bar
 * @author Julian Kruithof
 */
export default {
  name: "HeaderComponent",
  components: { FontAwesomeIcon },
  data() {
    return {
      activeUser: {
        name: String,
        role: String,
        team: { name: String, warehouse: name },
      },
    };
  },
  computed: {
    /**
     * Display the name of the route which is active, it removes the / and capitalizes the first letter.
     * @return {string | void} the name of the page the user is currently on.
     */
    displayCurrentPageName() {
      // When vue initially loads header the route is / and matched is empty.
      if (!this.$route.matched[0]) {
        return;
      }

      // If the route has a name, return the name.
      if (this.$route.matched[0].name !== undefined) {
        return this.$route.matched[0].name;
      }

      const pageName = this.$route.matched[0].path;
      return pageName
        .substring(1, 2)
        .toUpperCase()
        .concat(pageName.substring(2));
    },
  },
  methods: {
    getUser() {
      return {
        name: "Julian",
        role: "admin",
        team: {
          name: "team1",
          warehouse: "Superzon",
        },
      };
    },
  },
  created() {
    this.activeUser = this.getUser();
  },
};
</script>
<style scoped>
.bg-white {
  background-color: var(--color-bg);
}
.color-primary {
  color: var(--color-primary);
}
header {
  box-shadow: var(--custom-box-shadow);
}
</style>
