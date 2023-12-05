import { createRouter, createWebHashHistory } from "vue-router";
import Dashboard from "@/components/Dashboard";
import ProductOverview from "@/components/ProductOverview.vue";
import loginPage from "@/components/LoginPage.vue";
import TeamOverview from "@/components/TeamOverview";
import UserOverview from "@/components/UserOverview";
import ProjectsOverview from "@/components/ProjectsOverview.vue";
import InventoryOverview from "@/components/InventoryOverview.vue";
import WarehouseOverview from "@/components/WarehouseOverview.vue";
import ProjectSpecific from "@/components/ProjectSpecific.vue";
import loginResetPage from "@/components/LoginResetComponent.vue";
import OrderOverview from "@/components/OrderOverview.vue";

export const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/loginPage",
      component: loginPage,
      meta: {
        requiresLogin: false,
      },
    },
    {
      path: "/loginReset/:email?",
      component: loginResetPage,
      meta: {
        requiresLogin: false,
      },
    },
    {
      path: "/dashboard",
      component: Dashboard,
      meta: {
        icon: "fa-solid fa-house",
        requiresLogin: true,
      },
    }, // path to the dashboard
    {
      path: "/inventory",
      component: InventoryOverview,
      meta: { icon: "fa-solid fa-boxes-stacked", requiresLogin: true },
      children: [{ path: ":warehouse", component: InventoryOverview }],
    },
    {
      path: "/products",
      component: ProductOverview,
      meta: { icon: "fa-solid fa-box-open", requiresLogin: true },
    },
    {
      path: "/team",
      component: TeamOverview,
      meta: { icon: "fa-solid fa-users", requiresLogin: true },
    },
    {
      path: "/users",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user", requiresLogin: true },
    },
    { path: "/loginPage", component: loginPage },
    {
      path: "/user",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user", requiresLogin: true },
    },
    {
      path: "/projects",
      component: ProjectsOverview,
      meta: { icon: "fa-solid fa-project-diagram", requiresLogin: true },
    },
    {
      path: "/projects/:id",
      component: ProjectSpecific,
      meta: { icon: "fa-solid fa-bars-progress", requiresLogin: true },
      name: "Project Specific",
    },
    {
      path: "/warehouses",
      component: WarehouseOverview,
      meta: { icon: "fa-solid fa-warehouse", requiresLogin: true },
    },
    {
      path: "/orders",
      component: OrderOverview,
      meta: { icon: "fa-solid fa-truck-fast", requiresLogin: true },
      children: [{ path: ":warehouse", component: OrderOverview }],
    },
    // add paths to other components here
    {
      path: "/:pathMatch(.*)",
      redirect: "/dashboard",
      meta: {
        requiresLogin: true,
      },
    }, // redirect non-existing path to dashboard
  ],
});

router.beforeEach((to) => {
  const requiresLogin = to.matched.some((route) => route.meta.requiresLogin);
  const loggedStorage = localStorage.getItem("loggedIn");
  //if the user has been logged in and is and is on page that requires login then let the user navigate
  if (loggedStorage === "true" && requiresLogin) {
    return true;
  }
  // if the user navigates to a required login page and isn't logged in, redirect the user to the login page
  else if (loggedStorage !== "true" && requiresLogin) {
    return "/loginPage";
    // if the user is not logged in and navigates to a page that doesn't require to be logged in
    // it will let the user navigate
    // if the user is logged in and nagivates to a page that doesn't require it, dont let the user navigate
  } else return loggedStorage !== "true" && !requiresLogin;
});
