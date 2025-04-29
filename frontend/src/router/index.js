import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/Hero.vue'
import AllBooks from '@/components/AllBooks.vue'
import Publishers from '@/components/Publishers.vue'
import SearchBooks from '@/components/SearchBooks.vue'
import WhyShop from '@/components/WhyShop.vue'
import Navbar from '@/components/Navbar.vue'
import Footer from '@/components/Footer.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/#home',
      name: 'home',
      component: Home // จะใช้ component เดียวกับหน้า Home
    },
    {
      path: '/#search',
      name: 'search',
      component: Home // จะใช้ component เดียวกับหน้า Home
    },
    {
      path: '/#allbooks',
      name: 'allbooks',
      component: Home // จะใช้ component เดียวกับหน้า Home
    },
    {
      path: '/#publishers',
      name: 'publisehers',
      component: Home // จะใช้ component เดียวกับหน้า Home
    },
    {
      path: '/#whyShop',
      name: 'whyShop',
      component: Home // จะใช้ component เดียวกับหน้า Home
    }
  ]
})

export default router
