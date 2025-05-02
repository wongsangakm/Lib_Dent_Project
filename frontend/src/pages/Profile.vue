<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center p-6">
    <div class="bg-white shadow-lg rounded-2xl w-full max-w-md p-8">
      <div class="flex flex-col items-center">
        <!-- Profile Image Placeholder -->
        <div class="w-24 h-24 rounded-full bg-purple-200 flex items-center justify-center text-white text-4xl font-bold">
          {{ initial }}
        </div>

        <h2 class="mt-4 text-xl font-bold text-gray-800">Welcome, {{ authStore.username }}</h2>
        <p class="text-gray-500 text-sm">This is your profile page.</p>

        <div class="mt-6 w-full">
          <div class="text-sm text-gray-600 mb-2">Username</div>
          <div class="w-full bg-gray-100 px-4 py-2 rounded-lg text-gray-800 border">{{ authStore.username }}</div>
        </div>

        <button
          @click="handleLogout"
          class="mt-8 w-full py-3 bg-red-500 text-white rounded-lg font-semibold hover:bg-red-600 transition"
        >
          Logout
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()
const router = useRouter()

const initial = authStore.username ? authStore.username.charAt(0).toUpperCase() : '?'

const handleLogout = () => {
  authStore.logout()
  router.push('/Login')
}
</script>
