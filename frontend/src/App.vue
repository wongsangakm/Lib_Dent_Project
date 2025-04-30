<template>
  <div class="bg-cover bg-center" :style="{ backgroundImage: `url(${bgImage})` }">
    <!-- Navbar -->
    <Navbar @scroll-to="scrollToSection" />

    <!-- Hero Section -->
    <section id="home" ref="home"><Home /></section>

    <!-- Search Books Section -->
    <section id="search" ref="search"><SearchBooks /></section>

    <!-- Publishers Section -->
    <section id="publishers" ref="publishers"><Publishers /></section>

    <!-- All Books Section -->
    <section id="allBooks" ref="allBooks"><AllBooks /></section>

    <!-- Why Shop Section -->
    <section id="whyShop" ref="whyShop"><WhyShop /></section>

    <!-- Footer -->
    <<footer class="bg-blue-800 text-white py-6 text-center">
      <p>&copy; 2025 Book Request. All rights reserved.</p>
    </footer>>
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
import Footer from './pages/Footer.vue'

import bgImage from '@/image/Background.png' // ใส่รูปพื้นหลังที่ต้องการ

const home = ref(null)
const search = ref(null)
const publishers = ref(null)
const allBooks = ref(null)
const whyShop = ref(null)

// Function to scroll to specific section
const scrollToSection = (section) => {
  const el = {
    home,
    search,
    publishers,
    allBooks,
    whyShop
  }[section]
  el?.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
/* Section Styles */
section {
  margin-top: 3rem;
  padding: 2rem;
  background-color: rgba(255, 255, 255, 0.9); /* ทำให้พื้นหลังโปร่งใสเล็กน้อยเพื่อให้ดูสวย */
  border-radius: 8px; /* ให้มุมของ section ดูโค้งมน */
}

/* Footer style */
footer {
  background-color: #616161;
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: 3rem;
}

/* Responsive design (adjust for smaller screens) */
@media (max-width: 768px) {
  section {
    padding: 1rem;  /* ลดขนาด padding สำหรับหน้าจอเล็ก */
  }
}
</style>
