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
          />Dashboard
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/inventory" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-boxes-stacked"
            class="sidebar-icons"
          />Inventory
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/products" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-box-open"
            class="sidebar-icons"
          />Products
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/orders" class="nav-link" active-class="active">
          <font-awesome-icon
              icon="fa-solid fa-truck-fast"
              class="sidebar-icons"
          /> Orders
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/projects" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-diagram-project"
            class="sidebar-icons"
          />Projects
        </router-link>
      </li>

      <!-- TODO: Make use of submenu for projects when specific view is implemented.
      <li
        @click="submenuProjects = !submenuProjects"
        href="#homeSubmenu"
        data-toggle="collapse"
        aria-expanded="false"
        class="dropdown-toggle collapsed nav-item"
        active-class="active"
      >
        Home
      </li>
      <ul
        class="list-unstyled collapse"
        id="homeSubmenu"
        :class="submenuProjects ? 'show' : ''"
      >
        <li>
          <a href="#">Home 1</a>
        </li>
        <li>
          <a href="#">Home 2</a>
        </li>
        <li>
          <a href="#">Home 3</a>
        </li> -->
      <!-- </ul> -->

      <li class="nav-item">
        <router-link to="/warehouses" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-warehouse"
            class="sidebar-icons"
          />Warehouses
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/team" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-users"
            class="sidebar-icons"
          />Teams
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/users" class="nav-link" active-class="active">
          <font-awesome-icon
            icon="fa-solid fa-user"
            class="sidebar-icons"
          />Users
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/loginPage" class="nav-link" active-class="active"
        @click="logOut"
        >
          <font-awesome-icon
            icon="fa-solid fa-arrow-right-from-bracket"
            class="sidebar-icons"
            />Log out
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  // eslint-disable-next-line
  name: "sidebar",
  data() {
    return {
      dashboardRoute: "/dashboard",
      submenuProjects: false,
    };
  },
  methods: {
    logOut(){
      localStorage.setItem("loggedIn", false);
      this.$emit("updateLoggedIn", true);
    }
  }
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
