import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Hero.vue'
import AllBooks from '@/pages/AllBooks.vue'
import Publishers from '@/pages/Publishers.vue'
import SearchBooks from '@/pages/SearchBooks.vue'
import WhyShop from '@/pages/WhyShop.vue'
import Navbar from '@/pages/Navbar.vue'
import Footer from '@/component/Footer.vue'
import Loginpage from '@/pages/Loginpage.vue'
import BookDetail from '@/pages/BookDetail.vue' 

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/#home',
    //   name: 'home',
    //   component: Home // จะใช้ component เดียวกับหน้า Home
    // },
    // {
    //   path: '/#search',
    //   name: 'search',
    //   component: Home // จะใช้ component เดียวกับหน้า Home
    // },
    // {
    //   path: '/#allbooks',
    //   name: 'allbooks',
    //   component: Home // จะใช้ component เดียวกับหน้า Home
    // },
    // {
    //   path: '/#publishers',
    //   name: 'publisehers',
    //   component: Home // จะใช้ component เดียวกับหน้า Home
    // },
    // {
    //   path: '/#whyShop',
    //   name: 'whyShop',
    //   component: Home // จะใช้ component เดียวกับหน้า Home
    // }
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
