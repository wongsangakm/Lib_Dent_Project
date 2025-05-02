<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { ref } from "vue";
import Navbar from "./pages/Navbar.vue";
import Home from "./pages/Profile.vue";
import SearchBooks from "./pages/SearchBooks.vue";
import Publishers from "./pages/Publishers.vue";
import AllBooks from "./pages/AllBooks.vue";
import WhyShop from "./pages/System.vue";
import Footer from "./component/Footer.vue";
import Loginpage from "./pages/Loginpage.vue";
import bookdetail from "./pages/BookDetail.vue";
import bgImage from "@/image/Background.png"; // ใส่รูปพื้นหลังที่ต้องการ

const home = ref(null);
const search = ref(null);
const publishers = ref(null);
const allBooks = ref(null);
const whyShop = ref(null);

// Function to scroll to specific section
const scrollToSection = (section) => {
  const el = {
    home,
    search,
    publishers,
    allBooks,
    whyShop,
  }[section];
  el?.value?.scrollIntoView({ behavior: "smooth" });
};
</script>

<style scoped>
/* Section Styles */
section {
  margin-top: 3rem;
  padding: 2rem;
  background-color: rgba(
    255,
    255,
    255,
    0.9
  ); /* ทำให้พื้นหลังโปร่งใสเล็กน้อยเพื่อให้ดูสวย */
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
    padding: 1rem; /* ลดขนาด padding สำหรับหน้าจอเล็ก */
  }
}
</style>
