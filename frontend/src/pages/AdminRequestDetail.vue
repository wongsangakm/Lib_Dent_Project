<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 px-6 py-10"
  >
    <div v-if="book" class="max-w-6xl mx-auto">
      <div class="flex flex-col md:flex-row gap-8 mb-8">
        <!-- Book Cover -->
        <img
          :src="book.coverImage"
          alt="Book Cover"
          class="w-40 h-60 object-cover rounded shadow-md"
        />

        <!-- Book Info -->
        <div class="flex-1">
          <h2 class="text-2xl font-bold text-gray-800 mb-2">
            {{ book.title }}
          </h2>
          <div
            class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-700"
          >
            <div><strong>Author:</strong> {{ book.author || "N/A" }}</div>
            <div><strong>Publisher:</strong> {{ book.publisher }}</div>
            <div><strong>Publisher Date:</strong> {{ book.year || "—" }}</div>
            <div><strong>Price:</strong> {{ book.price }}</div>
          </div>
        </div>

        <!-- Quantity -->
        <div class="text-right">
          <p class="text-sm text-gray-500 mb-1">favourite</p>
          <div
            class="bg-purple-500 text-white font-bold w-10 h-10 rounded-full flex items-center justify-center"
          >
            {{ book.favoritedBy?.length || 0 }}
          </div>
        </div>
      </div>

      <!-- Table of users who favorited the book -->
      <div class="bg-white rounded-xl shadow overflow-hidden">
        <table class="min-w-full table-auto text-sm text-left">
          <thead class="bg-gray-100 text-gray-700">
            <tr>
              <th class="px-4 py-2">ID</th>
              <th class="px-4 py-2">Name</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(user, index) in book.favoritedBy"
              :key="user.id || index"
              class="border-b hover:bg-gray-50"
            >
              <td class="px-4 py-2">{{ user.id }}</td>
              <td class="px-4 py-2">{{ user.name }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Loading fallback -->
    <div v-else class="text-center text-gray-600">Loading book detail...</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useFavouritesStore } from "@/stores/favourites";

const route = useRoute();
const favouritesStore = useFavouritesStore();

const book = ref(null);

onMounted(() => {
  const bookId = parseInt(route.params.id);
  const found = favouritesStore.allBooks.find((b) => b.id === bookId);

  if (found) {
    // Mock รายชื่อคนที่ชอบหนังสือเล่มนี้
    found.favoritedBy = [
      { id: "U001", name: "John Smith" },
      { id: "U002", name: "Jenny Wilson" },
      { id: "U003", name: "Cameron Williamson" },
      { id: "U004", name: "Esther Howard" },
      { id: "U005", name: "Kristin Watson" },
    ];
    book.value = found;
  }
});
</script>
