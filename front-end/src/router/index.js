import { createRouter, createWebHashHistory } from "vue-router";
import Dashboard from "@/components/Dashboard";
import ProductOverview from "@/components/ProductOverview.vue";
import loginPage from "@/components/LoginPage.vue";
import UserOverview from "@/components/UserOverview";

export const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/dashboard",
      component: Dashboard,
      meta: { icon: "fa-solid fa-house" },
    }, // path to the dashboard
    {
      path: "/inventory",
      component: ProductOverview,
      meta: { icon: "fa-solid fa-boxes-stacked" },
      children: [{ path: ":warehouse", component: ProductOverview }],
    },
    {
      path: "/users",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user" },
    },
    { path: "/loginPage", component: loginPage },
    {
      path: "/user",
      component: UserOverview,
      meta: { icon: "fa-solid fa-user" },
    },
    // add paths to other components here
    { path: "/:pathMatch(.*)", redirect: "/dashboard" }, // redirect non-existing path to dashboard
  ],
});
