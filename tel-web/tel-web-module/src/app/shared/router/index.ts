import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/app/modules/home/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/",
    }
  ]
})

export default router
