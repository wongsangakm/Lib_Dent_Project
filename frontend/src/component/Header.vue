<template>
  <header class="bg-white shadow-md fixed top-0 w-full z-10">
    <nav class="container mx-auto px-2 py-2 flex justify-between items-center">
      <div class="flex items-center space-x-2">
        <Logo :size="'w-16 h-16'" />
        <span class="text-lg font-bold text-purple-600">Book Request</span>
      </div>
      <div class="flex items-center space-x-6">
        <a href="#" class="text-gray-600 hover:text-purple-600">Home</a>
        <a href="#searchbook" class="text-gray-600 hover:text-purple-600">Search Book</a>
        <a href="#Favbypub" class="text-gray-600 hover:text-purple-600"
          >Favourite By Publisher</a
        >
        <a href="#all-books" class="text-gray-600 hover:text-purple-600"
          >All Books</a
        >
        <a href="#why" class="text-gray-600 hover:text-purple-600">Why</a>
        <div class="flex items-center space-x-2">
          
            <router-link
                to="/login"
                class="px-4 py-2 bg-purple-100 text-purple-600 rounded-full hover:bg-purple-200 transition-colors"
                >
                Sign In
                </router-link>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import Logo from "./Logo.vue";

</script>
