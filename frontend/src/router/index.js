import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Homepage.vue'
import Profile from '@/pages/Profile.vue'
import Loginpage from '@/pages/Loginpage.vue'
import BookDetail from '@/pages/BookDetail.vue' 

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Home',
      name: 'Home',
      component: Home
    },
    {
      path: '/Profile',
      name: 'Profile',
      component: Profile
    },
    {
      path: '/BookDetail',
      name: 'BookDetail',
      component: BookDetail
    },
    {
      path: '/Login',
      name: 'Loginpage',
      component: Loginpage
    }
  ]
})







export default router
