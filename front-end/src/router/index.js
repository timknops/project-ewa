import {createRouter, createWebHashHistory} from 'vue-router'
import Dashboard from '@/components/Dashboard'
import ProductOverview from "@/components/ProductOverview.vue";
import TeamOverview from "@/components/TeamOverview";
import UserOverview from "@/components/UserOverview";

export const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {path: '/dashboard', component: Dashboard, meta: {icon: "fa-solid fa-house"}}, // path to the dashboard
    {
      path: '/inventory', component: ProductOverview, meta: {icon: "fa-solid fa-boxes-stacked"}, // path to the product-overview
      children: [{path: ':warehouse', component: ProductOverview}]
    },
    {path: '/team', component: TeamOverview, meta:{icon: "fa-solid fa-users"}}, // path to the team-overview
    {path: '/user', component: UserOverview, meta:{icon: "fa-solid fa-user"}}, // path to the user-overview
    // add paths to other components here
    {path: '/:pathMatch(.*)', redirect: '/dashboard'} // redirect non-existing path to dashboard
  ]
})