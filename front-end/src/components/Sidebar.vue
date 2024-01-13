<template>
  <nav class="sidebar">
    <img
        alt="Solar Sedum logo"
        id="sidebar-logo"
        src="../assets/solar_logo.svg"
    />
    <ul class="nav flex-column" id="sidebar-items-list">
      <li class="nav-item">
        <router-link
            :to="dashboardRoute"
            active-class="active"
            class="nav-link"
        >
          <font-awesome-icon
              icon="fa-solid fa-house"
              class="sidebar-icons"
          />
          Dashboard
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/inventory" class="nav-link" active-class="active">
          <span v-if="isActiveUserAdmin">
            <font-awesome-icon
                icon="fa-solid fa-boxes-stacked"
                class="sidebar-icons"
            />Inventory
          </span>
          <span v-else>
            <font-awesome-icon
                icon="fa-solid fa-boxes-stacked"
                class="sidebar-icons"
            />My Inventory
          </span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/products" class="nav-link" active-class="active">
          <font-awesome-icon
              icon="fa-solid fa-box-open"
              class="sidebar-icons"
          />
          Products
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/orders" class="nav-link" active-class="active">
          <font-awesome-icon
              icon="fa-solid fa-truck-fast"
              class="sidebar-icons"
          />
          Orders
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/projects" class="nav-link" active-class="active">
          <span v-if="isActiveUserAdmin"
          ><font-awesome-icon
              icon="fa-solid fa-diagram-project"
              class="sidebar-icons"
          />Projects</span
          >
          <span v-else>
            <font-awesome-icon
                icon="fa-solid fa-diagram-project"
                class="sidebar-icons"
            />My Projects
          </span>
        </router-link>
      </li>
      <li class="nav-item" v-if="isActiveUserAdmin">
        <router-link to="/warehouses" class="nav-link" active-class="active">
          <font-awesome-icon
              icon="fa-solid fa-warehouse"
              class="sidebar-icons"
          />
          Warehouses
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/team" class="nav-link" active-class="active">
          <span v-if="isActiveUserAdmin"
          ><font-awesome-icon
              icon="fa-solid fa-users"
              class="sidebar-icons"
          />Teams</span
          >
          <span v-else
          ><font-awesome-icon
              icon="fa-solid fa-user"
              class="sidebar-icons"
          />My Team</span
          >
        </router-link>
      </li>
      <!--      User list item only shows if the user is an admin-->
      <li class="nav-item" v-if="isActiveUserAdmin">
        <router-link to="/users" class="nav-link" active-class="active">
          <font-awesome-icon
              icon="fa-solid fa-user"
              class="sidebar-icons"
          />
          Users
        </router-link>
      </li>
      <li class="nav-item">
        <router-link
            to="/loginPage"
            class="nav-link"
            active-class="active"
            @click="logOut()"
        >
          <font-awesome-icon
              icon="fa-solid fa-arrow-right-from-bracket"
              class="sidebar-icons"
          />
          Log out
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  // eslint-disable-next-line
  name: "sidebar",
  inject: ["sessionService"],
  data() {
    return {
      dashboardRoute: "/dashboard",
      submenuProjects: false,
      isActiveUserAdmin: null,
    };
  },
  methods: {
    logOut() {
      //calls method in app.vue to receive token from storage
      this.$router.push("/logout");
    },
  },
  //checks if the user is an admin
  created() {
    this.isActiveUserAdmin = this.sessionService.isAdmin();
    console.log(this.isActiveUserAdmin);
  },
};
</script>

<style scoped>
#sidebar-logo {
  max-width: 180px;
  margin-top: 25px;
}

#sidebar-items-list {
  margin-top: 50px;
}

.sidebar-icons {
  margin-right: 10px;
  height: 18px;
  width: 18px;
}

.nav-link {
  text-align: left;
  color: var(--color-text) !important;
  padding-left: 75px;
  margin-bottom: 4px;
}

.nav-link:hover,
.nav-link:active {
  color: var(--color-primary) !important;
}

.active {
  color: var(--color-primary) !important;
}

.sidebar {
  grid-area: sidebar;
  background-color: var(--color-bg);
  text-align: center;
  width: var(--sidebar-width);
  height: 100vh;
  position: fixed;
  box-shadow: 3px 3px 10px rgba(0, 0, 0, 0.1);
}
</style>
