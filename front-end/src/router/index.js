import {createRouter, createWebHashHistory} from 'vue-router'
import Dashboard from '@/components/Dashboard'
import ProductOverview from "@/components/ProductOverview.vue";

export const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {path: '/dashboard', component: Dashboard, meta: {icon: "fa-solid fa-house"}}, // path to the dashboard
    {
      path: '/inventory', component: ProductOverview,
      children: [{path: ':warehouse', component: ProductOverview}]
    },
    // add paths to other components here
    {path: '/:pathMatch(.*)', component: Dashboard} // redirect non-existing path to dashboard
  ]
})