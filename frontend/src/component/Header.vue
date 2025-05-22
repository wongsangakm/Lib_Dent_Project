<template>
  <header class="bg-white shadow-md fixed top-0 w-full z-20">
    <nav class="container mx-auto px-2 py-2 flex justify-between items-center">
      <!-- Logo and Brand -->
      <div class="flex items-center space-x-2">
        <Logo :size="'w-16 h-16'" />
        <div class="flex flex-col leading-tight">
          <span class="text-lg font-bold text-purple-600">Book Request</span>
          <span class="text-base text-gray-600">ห้องสมุดสมนึก พูนทรัพย์</span>
          
        </div>
      </div>
      <!-- Mobile Menu Button -->
      <button @click="isMenuOpen = !isMenuOpen" class="md:hidden ml-auto mr-4">
        <svg
          class="w-6 h-6 text-purple-600"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            v-if="!isMenuOpen"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M4 6h16M4 12h16m-7 6h7"
          />
          <path
            v-else
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M6 18L18 6M6 6l12 12"
          />
        </svg>
      </button>

      <!-- Desktop Navigation -->
      <div class="hidden md:flex items-center space-x-6 relative">
        <router-link to="/" class="text-gray-600 hover:text-purple-600"
          >Home</router-link
        >
        <router-link
          to="/"
          @click.prevent="scrollToSection('searchbook')"
          class="text-gray-600 hover:text-purple-600"
          >Search Book</router-link
        >
        <router-link to="/#Favbypub" class="text-gray-600 hover:text-purple-600"
          >Favourite By Publisher</router-link
        >
        <router-link
          to="/#all-books"
          class="text-gray-600 hover:text-purple-600"
          >All Books</router-link
        >
        <router-link to="/#why" class="text-gray-600 hover:text-purple-600"
          >Why</router-link
        >
      </div>

      <!-- User Actions (Desktop and Mobile) -->
      <div class="flex items-center space-x-2">
        <!-- Favorites Link -->
        <div class="relative dropdown-heart">
          <!-- ปุ่มหัวใจ -->
          <button
            @click="toggleHeartDropdown"
            class="w-10 h-10 flex items-center justify-center rounded-full border border-gray-300 relative"
          >
            <svg
              class="w-6 h-6 text-purple-600"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
              />
            </svg>

            <!-- Badge -->
            <span
              v-if="favouritesCount > 0"
              class="absolute top-0 right-0 transform translate-x-1/2 -translate-y-1/2 bg-indigo-300 text-yellow-300 text-xs font-bold rounded-full w-6 h-6 flex items-center justify-center"
            >
              {{ favouritesCount }}
            </span>
          </button>

          <!-- Dropdown -->
          <transition name="fade">
            <div
              v-if="openHeartDropdown"
              class="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-lg shadow-lg z-50"
            >
              <router-link
                to="/favbooks"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center"
              >
                ❤️ รายการโปรด
              </router-link>
              <router-link
                to="/favorites-dashboard"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 flex items-center"
              >
                📈 ภาพรวมและสถานะ
              </router-link>
            </div>
          </transition>
        </div>

        <!-- Sign In / Profile Button -->
        <div class="relative" @click="toggleDropdown">
          <router-link
            v-if="!authStore.isAuthenticated"
            to="/login"
            class="px-4 py-2 bg-purple-100 text-purple-600 rounded-full hover:bg-purple-200 transition-colors"
          >
            Sign In
          </router-link>

          <button
            v-else
            class="px-4 py-2 bg-purple-100 text-purple-600 rounded-full hover:bg-purple-200 transition-colors flex items-center space-x-2"
          >
            <span>{{ authStore.username }}</span>
            <svg
              class="w-4 h-4 text-purple-600"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fill-rule="evenodd"
                d="M5.23 7.21a.75.75 0 011.06.02L10 11.584l3.71-4.353a.75.75 0 011.14.976l-4.25 5a.75.75 0 01-1.14 0l-4.25-5a.75.75 0 01.02-1.06z"
                clip-rule="evenodd"
              />
            </svg>
          </button>

          <!-- Dropdown Menu -->
          <div
            v-if="authStore.isAuthenticated && showDropdown"
            class="absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded-md shadow-lg z-30"
          >
            <button
              @click="goToChangePassword"
              class="w-full text-left px-4 py-2 text-gray-700 hover:bg-purple-100"
            >
              Change Password
            </button>
            <button
              @click="handleLogout"
              class="w-full text-left px-4 py-2 text-red-700 hover:bg-purple-100"
            >
              Log out
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- Mobile Menu (Slide Down) -->
    <div
      v-if="isMenuOpen"
      class="md:hidden bg-white px-2 pt-2 pb-4 shadow-lg border-t border-gray-200 absolute w-full z-20"
    >
      <div class="flex flex-col space-y-3">
        <router-link
          to="/"
          @click="isMenuOpen = false"
          class="text-gray-600 hover:text-purple-600 px-3 py-2 rounded-md hover:bg-purple-50"
        >
          Home
        </router-link>
        <router-link
          to="/"
          @click="mobileScrollToSection('searchbook')"
          class="text-gray-600 hover:text-purple-600 px-3 py-2 rounded-md hover:bg-purple-50"
        >
          Search Book
        </router-link>
        <router-link
          to="/#Favbypub"
          @click="isMenuOpen = false"
          class="text-gray-600 hover:text-purple-600 px-3 py-2 rounded-md hover:bg-purple-50"
        >
          Favourite By Publisher
        </router-link>
        <router-link
          to="/#all-books"
          @click="isMenuOpen = false"
          class="text-gray-600 hover:text-purple-600 px-3 py-2 rounded-md hover:bg-purple-50"
        >
          All Books
        </router-link>
        <router-link
          to="/#why"
          @click="isMenuOpen = false"
          class="text-gray-600 hover:text-purple-600 px-3 py-2 rounded-md hover:bg-purple-50"
        >
          Why
        </router-link>
      </div>
    </div>
  </header>
</template>

<script setup>
import Logo from "./Logo.vue";
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import { useFavouritesStore } from "@/stores/favourites";

const favouritesStore = useFavouritesStore();
const favouritesCount = computed(() => favouritesStore.favourites.length);
const authStore = useAuthStore();
const router = useRouter();
const showDropdown = ref(false);
const isMenuOpen = ref(false);
const openHeartDropdown = ref(false);
const toggleHeartDropdown = () => {
  openHeartDropdown.value = !openHeartDropdown.value;
};

// Close mobile menu when screen size changes to desktop
const checkScreenSize = () => {
  if (window.innerWidth >= 768 && isMenuOpen.value) {
    isMenuOpen.value = false;
  }
};
const handleClickOutsideHeart = (event) => {
  const heartDropdown = document.querySelector(".dropdown-heart");
  if (heartDropdown && !heartDropdown.contains(event.target)) {
    openHeartDropdown.value = false;
  }
};

onMounted(() => {
  window.addEventListener("resize", checkScreenSize);
  // Close the mobile menu when clicking outside
  document.addEventListener("click", handleClickOutside);
  document.addEventListener("click", handleClickOutsideHeart);
});
const goToChangePassword = () => {
  showDropdown.value = false;
  router.push("/change-password");
};

onUnmounted(() => {
  window.removeEventListener("resize", checkScreenSize);
  document.removeEventListener("click", handleClickOutside);
  document.removeEventListener("click", handleClickOutsideHeart);
});

const handleClickOutside = (event) => {
  // Close dropdown when clicking outside
  if (showDropdown.value && !event.target.closest(".relative")) {
    showDropdown.value = false;
  }
};

const toggleDropdown = () => {
  if (authStore.isAuthenticated) {
    showDropdown.value = !showDropdown.value;
  }
};

const handleLogout = () => {
  authStore.logout();
  showDropdown.value = false;
  router.push("/login");
};

const scrollToSection = (id) => {
  const target = document.getElementById(id);
  const offset = 100; // ความสูงของ header

  if (target) {
    const top = target.getBoundingClientRect().top + window.scrollY - offset;
    window.scrollTo({ top, behavior: "smooth" });
  }
};

const mobileScrollToSection = (id) => {
  isMenuOpen.value = false;
  setTimeout(() => {
    scrollToSection(id);
  }, 100);
};

onMounted(() => {
  if (authStore.isAuthenticated) {
    favouritesStore.fetchFavourites();
  }
});
</script>

<style scoped>
/* Animation for mobile menu */
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Apply animation to mobile menu */
.md\:hidden.bg-white {
  animation: slideDown 0.2s ease-out;
}
</style>
