<!-- AdminLayout.vue -->
<template>
   <div class="min-h-screen flex">
      <!-- Fixed Sidebar -->
    <aside
      class="w-64 h-screen fixed top-0 left-0 bg-white shadow-md z-20"
    >
      <AdminSidebar />
    </aside>
      <!-- Main Content Area (with margin-left) -->
      <main class="flex-1 ml-64">
      <router-view />
    </main>
  </div>

  </template>
  
  <script setup>
  import AdminSidebar from '@/component/AdminSidebar.vue'
  </script>
  