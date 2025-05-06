<template>
  <div class="container mx-auto px-4 py-12">
    <h1 class="text-2xl font-bold text-center mb-8">All Books</h1>

    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">All Books</h1>
      <router-link
        to="/admin/allbooks/add-book"
        class="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 transition"
      >
        + Add Book
      </router-link>
    </div>

    <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-6">
      <div
        v-for="book in allBooks"
        :key="book.id"
        class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center"
      >
        <img
          :src="book.coverImage"
          :alt="book.title"
          class="w-[120px] h-[180px] object-cover rounded"
        />
        <p class="mt-3 text-center font-semibold text-gray-700 truncate w-full">
          {{ book.title }}
        </p>
        <p class="text-xs text-gray-500">{{ book.publisher }}</p>
        <p class="text-sm font-bold text-purple-700 mt-1">{{ book.price }}</p>

        <router-link
          :to="`/admin/adminbookdetail/${book.id}`"
          class="mt-3 px-4 py-1 bg-purple-500 text-white rounded-full text-sm hover:bg-purple-600 transition"
        >
          More
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useFavouritesStore } from "@/stores/favourites";
import { computed } from "vue";

const favouritesStore = useFavouritesStore();
const allBooks = computed(() => favouritesStore.allBooks);
</script>
