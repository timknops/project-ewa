import { createRouter, createWebHashHistory } from 'vue-router'
import Dashboard from '@/components/Dashboard'

export const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {path: '/dashboard', component: Dashboard}, // path to the dashboard
        // add paths to other components here
        {path: '/:pathMatch(.*)', component: Dashboard} // redirect non-existing path to dashboard
    ]
})