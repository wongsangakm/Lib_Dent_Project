<template>
  <div class="flex h-screen">
    <!-- Mobile hamburger -->
    <div class="md:hidden fixed top-4 left-4 z-50">
      <button @click="isSidebarOpen = !isSidebarOpen">
        <i class="fas fa-bars text-2xl text-purple-700"></i>
      </button>
    </div>

    <!-- Sidebar -->
    <aside
      class="fixed z-40 top-0 left-0 h-full w-64 bg-white shadow-md flex flex-col transition-transform duration-300 md:translate-x-0 md:block"
      :class="{
        '-translate-x-full': !isSidebarOpen,
        'translate-x-0': isSidebarOpen,
      }"
    >
      <!-- Header -->
      <div
        class="mt-8 p-6 border-b border-gray-200 flex justify-between items-center"
      >
        <h1 class="text-xl font-bold text-purple-700">Book Request</h1>
      </div>

      <!-- Admin Name -->
      <div
        class="px-6 py-4 border-b border-gray-200 text-gray-800 font-semibold"
      >
        {{ authStore.username }}
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

        <RouterLink
          to="/admin/AdminAddUser"
          class="flex items-center gap-3 px-3 py-2 rounded-lg hover:bg-purple-50"
          :class="{
            'text-purple-600 font-semibold': $route.path.includes(
              '/admin/AdminAddUser'
            ),
          }"
        >
          <i class="fa-solid fa-plus"></i>
          Add User
        </RouterLink>

        <!-- Optional -->
        <!-- <div class="text-sm text-gray-400 mt-4">Coming Soon</div>
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
        </div>  -->
      </nav>

      <button
        @click="logout"
        class="w-full text-left text-red-500 hover:text-red-600 font-semibold flex items-center gap-2 pl-20 pb-6"
      >
        <i class="fas fa-sign-out-alt"></i>
        Logout
      </button>
    </aside>

    <!-- Content Overlay (click to close sidebar on mobile) -->
    <div
      v-if="isSidebarOpen"
      class="fixed inset-0 bg-black opacity-25 z-30 md:hidden"
      @click="isSidebarOpen = false"
    ></div>

    <!-- Main content slot (example) -->
    <main class="flex-1 ml-64 p-6 hidden md:block">
      <!-- Your main admin content goes here -->
      <slot />
    </main>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import { ref, onMounted } from "vue";

onMounted(() => {
  if (window.innerWidth >= 768) {
    isSidebarOpen.value = true;
  }
});
const router = useRouter();
const authStore = useAuthStore();
const isSidebarOpen = ref(false);
const logout = () => {
  authStore.logout();
  router.push("/");
};
</script>

<style scoped>
.router-link-exact-active {
  @apply text-purple-600 font-semibold;
}
</style>
