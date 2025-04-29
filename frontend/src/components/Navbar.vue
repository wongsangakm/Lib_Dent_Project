<template>
  <nav class="bg-blue-800 p-4 flex justify-between items-center">
    <!-- Left: Logo and System Name -->
    <div class="flex items-center space-x-8">
      <div class="max-w-[48px] max-h-[48px]">
        <img src="@/image/DENTISTRY_KKU.svg.png" alt="Logo" class="w-full h-full object-contain" />
      </div>
      <span class="text-violet-700 text-xl font-bold">Book Request</span>
    </div>

    <!-- Center: Section Navigation -->
    <div class="flex space-x-8">
      <a href="#hero" class="mx-4 hover:text-blue-300">Home</a>
      <a href="#search" class="mx-4 hover:text-blue-300">Search</a>
      <a href="#publishers" class="mx-4 hover:text-blue-300">Publishers</a>
      <a href="#allBooks" class="mx-4 hover:text-blue-300">All Books</a>
      <a href="#whyShop" class="mx-4 hover:text-blue-300">Why Shop</a>
    </div>

    <!-- Right: Favorite Icon and Sign In Button -->
    <div class="flex items-center space-x-4">
      <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="w-6 h-6 text-red-500" viewBox="0 0 24 24">
          <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5..."/>
      </svg>
      <button class="bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700">Sign In</button>
    </div>
  </nav>
</template>

<script setup>
const scrollToSection = (section) => {
  const el = document.getElementById(section)
  if (el) {
    el.scrollIntoView({ behavior: 'smooth' })
  }
}
</script>

<style scoped>
/* You can add custom styles here if needed */
</style>
