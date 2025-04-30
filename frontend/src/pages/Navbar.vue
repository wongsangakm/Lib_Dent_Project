<template>
  <nav class="w-full p-4 flex justify-between items-center z-50">
    <div class="flex items-center space-x-4">
      <div class="max-w-[48px] max-h-[48px]">
        <img src="@/image/DENTISTRY_KKU.svg.png" alt="Logo" class="w-full h-full object-contain" />
      </div>
      <span class="text-violet-700 text-xl font-bold">Book Request</span>
    </div>

    <div class="flex space-x-4 flex-wrap">
      <button @click="$emit('scroll-to', 'home')" class="mx-2 hover:text-blue-300">Home</button>
      <button @click="$emit('scroll-to', 'search')" class="mx-2 hover:text-blue-300">Search</button>
      <button @click="$emit('scroll-to', 'publishers')" class="mx-2 hover:text-blue-300">Publishers</button>
      <button @click="$emit('scroll-to', 'allBooks')" class="mx-2 hover:text-blue-300">All Books</button>
      <button @click="$emit('scroll-to', 'whyShop')" class="mx-2 hover:text-blue-300">Why Shop</button>
    </div>

    <div class="flex items-center space-x-4">
      <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="w-6 h-6 text-red-500" viewBox="0 0 24 24">
        <path d="M12 21.35l-1.45-1.32C5.4 15.36..." />
      </svg>
      <button class="bg-purple-600 text-white py-2 px-4 rounded hover:bg-purple-700">Sign In</button>
    </div>
  </nav>
</template>

<script setup>
const scrollToSection = (section) => {
  const el = document.getElementById(section)
  if (el) el.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
/* ถ้าอยาก fix ว่าไม่ให้ล้นขวา */
nav {
  overflow-x: hidden;
}
</style>
