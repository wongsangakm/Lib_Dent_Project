<template>
  <header class="bg-white shadow-md fixed top-0 w-full z-10">
    <nav class="container mx-auto px-2 py-2 flex justify-between items-center">
      <div class="flex items-center space-x-2">
        <Logo :size="'w-16 h-16'" />
        <span class="text-lg font-bold text-purple-600">Book Request</span>
      </div>

      <div class="flex items-center space-x-6 relative">
        <a href="#" class="text-gray-600 hover:text-purple-600">Home</a>
        <a href="#searchbook" class="text-gray-600 hover:text-purple-600">Search Book</a>
        <a href="#Favbypub" class="text-gray-600 hover:text-purple-600">Favourite By Publisher</a>
        <a href="#all-books" class="text-gray-600 hover:text-purple-600">All Books</a>
        <a href="#why" class="text-gray-600 hover:text-purple-600">Why</a>

        <!-- ปุ่ม Sign In หรือ Profile -->
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
            ><path
              fill-rule="evenodd"
              d="M5.23 7.21a.75.75 0 011.06.02L10 11.584l3.71-4.353a.75.75 0 011.14.976l-4.25 5a.75.75 0 01-1.14 0l-4.25-5a.75.75 0 01.02-1.06z"
              clip-rule="evenodd"
              />
            </svg>
          </button>


          <!-- Dropdown Logout -->
          <div
            v-if="authStore.isAuthenticated && showDropdown"
            class="absolute right-0 mt-2 w-32 bg-white border border-gray-200 rounded-md shadow-lg z-20"
          >
            <button
              @click="handleLogout"
              class="w-full text-left px-4 py-2 text-gray-700 hover:bg-purple-100"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import Logo from "./Logo.vue"
import { ref } from "vue"
import { useRouter } from "vue-router"
import { useAuthStore } from "@/stores/authStore"

const authStore = useAuthStore()
const router = useRouter()
const showDropdown = ref(false)

const toggleDropdown = () => {
  if (authStore.isAuthenticated) {
    showDropdown.value = !showDropdown.value
  }
}

const handleLogout = () => {
  authStore.logout()
  showDropdown.value = false
  router.push("/login")
}
</script>
