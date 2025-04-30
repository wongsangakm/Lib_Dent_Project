<template>
  <img :src="logoSrc" alt="Faculty Logo" class="w-24 h-24 mx-auto" />
</template>

<script setup>
const logoSrc = new URL('@/assets/faculty.svg', import.meta.url).href;
</script>