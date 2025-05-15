<!-- AdminLayout.vue -->
<template>
  <div class="min-h-screen flex">
    
    <AdminSidebar />
    <main class="flex-1">
      <router-view />
    </main>
  </div>
</template>
  <script setup>
  import AdminSidebar from '@/component/AdminSidebar.vue'
  </script>
  