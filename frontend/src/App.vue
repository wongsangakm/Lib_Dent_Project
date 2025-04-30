<template>
  <div class="min-h-screen w-full overflow-x-hidden">
    <!-- Main background -->
    <main class="app-container min-h-screen w-full">
      <Navbar @scroll-to="scrollToSection" />

      <section id="home" ref="home"><Home /></section>
      <section id="search" ref="search"><SearchBooks /></section>
      <section id="publishers" ref="publishers"><Publishers /></section>
      <section id="allBooks" ref="allBooks"><AllBooks /></section>
      <section id="whyShop" ref="whyShop"><WhyShop /></section>
    </main>

    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Navbar from './pages/Navbar.vue'
import Home from './pages/Hero.vue'
import SearchBooks from './pages/SearchBooks.vue'
import Publishers from './pages/Publishers.vue'
import AllBooks from './pages/AllBooks.vue'
import WhyShop from './pages/WhyShop.vue'
import Footer from './component/Footer.vue'

const home = ref(null)
const search = ref(null)
const publishers = ref(null)
const allBooks = ref(null)
const whyShop = ref(null)

const scrollToSection = (section) => {
  const refs = {
    home,
    search,
    publishers,
    allBooks,
    whyShop
  }
  refs[section]?.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
/* พื้นหลังให้กับ main container */
.app-container {
  background-image: url('./image/Background.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

/* section แต่ละอันต้องสูงพอให้ scroll ได้ */
section {
  min-height: 100vh;
  width: 100%;
}
</style>
