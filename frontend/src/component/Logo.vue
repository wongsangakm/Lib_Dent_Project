<template>
   <img
    src="@/assets/faculty.svg"
    alt="Faculty Logo"
    :class="`${size}`"
  />
</template>

<script setup>
defineProps({
  size: {
    type: String,
    default: 'w-24 h-24'
  }
})
</script>