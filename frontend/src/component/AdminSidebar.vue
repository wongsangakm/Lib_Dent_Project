<template>
  <aside class="w-64 h-screen bg-white shadow-md flex flex-col">
    <!-- Logo + Title -->
    <div class="p-6 border-b border-gray-200">
      <h1 class="text-xl font-bold text-purple-700">Book Request</h1>
    </div>

    <!-- Admin Name -->
    <div class="px-6 py-4 border-b border-gray-200 text-gray-800 font-semibold">
      Sunny
    </div>

    <!-- Navigation -->
    <nav class="flex-1 px-4 py-6 space-y-2 text-gray-600">
      <RouterLink
        to="/admin/dashboard"
        class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-purple-50"
        :class="{
          'text-purple-600 font-semibold':
            $route.path.includes('/admin/dashboard'),
        }"
      >
        <i class="fas fa-th-large"></i>
        Dashboard
      </RouterLink>

      <RouterLink
        to="/admin/allbooks"
        class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-purple-50"
        :class="{
          'text-purple-600 font-semibold':
            $route.path.includes('/admin/allbooks'),
        }"
      >
        <i class="fas fa-book"></i>
        Book
      </RouterLink>

      <RouterLink
        to="/admin/request-table"
        class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-purple-50"
        :class="{
          'text-purple-600 font-semibold': $route.path.includes(
            '/admin/request-table'
          ),
        }"
      >
        <i class="fas fa-user-friends"></i>
        Request
      </RouterLink>

      <RouterLink
        to="/admin/round-export"
        class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-purple-50"
        :class="{
          'text-purple-600 font-semibold': $route.path.includes(
            '/admin/round-export'
          ),
        }"
      >
        <i class="fas fa-comment-dots"></i>
        Ordering Cycle
      </RouterLink>

      <!-- Optional Future Items -->
      <div class="text-sm text-gray-400 mt-4">Coming Soon</div>
      <div class="flex items-center gap-3 px-3 py-2 rounded-lg">
        <i class="fas fa-database"></i>
        DataBase
      </div>
      <div class="flex items-center gap-3 px-3 py-2 rounded-lg">
        <i class="fas fa-bell"></i>
        Notifications
      </div>
      <div class="flex items-center gap-3 px-3 py-2 rounded-lg">
        <i class="fas fa-cog"></i>
        Settings
      </div>
    </nav>
    <button
      @click="logout"
      class="w-full text-left text-red-500 hover:text-red-600 font-semibold flex items-center gap-2 pl-20"
    >
      <i class="fas fa-sign-out-alt"></i>
      Logout
    </button>
  </aside>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/useAuthStore'

const router = useRouter()
const authStore = useAuthStore()

const logout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style scoped>
.router-link-exact-active {
  @apply text-purple-600 font-semibold;
}
</style>
