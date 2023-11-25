import { createRouter, createWebHashHistory } from "vue-router";
import Dashboard from "@/components/Dashboard";
import ProductOverview from "@/components/ProductOverview.vue";
import loginPage from "@/components/LoginPage.vue";
import TeamOverview from "@/components/TeamOverview";
import UserOverview from "@/components/UserOverview";
import ProjectsOverview from "@/components/ProjectsOverview.vue";
import InventoryOverview from "@/components/InventoryOverview.vue";
import WarehouseOverview from "@/components/WarehouseOverview.vue";
import loginResetPage from "@/components/LoginResetComponent.vue";

export const router = createRouter({
  history: createWebHashHistory(),
  routes: [

    {
      path: "/loginPage",
      component: loginPage
    },
    {
      path: "/loginReset",
      component: loginResetPage
    },
    {
      path: "/dashboard",
      component: Dashboard,
      meta: { icon: "fa-solid fa-house" },
    }, // path to the dashboard
    {
      path: "/inventory",
      component: InventoryOverview,
      meta: { icon: "fa-solid fa-boxes-stacked" },
      children: [{ path: ":warehouse", component: InventoryOverview }],
    },
    {
      path: "/products",
      component: ProductOverview,
      meta: {icon: "fa-solid fa-box-open"}
    },
    {
      path: "/team",
      component: TeamOverview,
      meta: { icon: "fa-solid fa-users" },
    },
    {
      path: "/users",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user" },
    },
    {
      path: "/user",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user" },
    },
    {
      path: "/projects",
      component: ProjectsOverview,
      meta: { icon: "fa-solid fa-project-diagram" },
    },
    {
      path: "/warehouses",
      component: WarehouseOverview,
      meta: {icon: "fa-solid fa-warehouse"}
    },
    // add paths to other components here
    { path: "/:pathMatch(.*)", redirect: "/dashboard" }, // redirect non-existing path to dashboard
  ],
});
