<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 px-6 py-10"
  >
    <div v-if="book" class="w-full max-w-screen-lg mx-auto px-4">
      <div class="flex flex-col md:flex-row gap-6 md:gap-8 mb-8">
        <div class="flex items-start items-center gap-4 md:block">
          <!-- Book Cover -->
          <img
            :src="book.coverImage"
            alt="Book Cover"
            class="w-32 h-48 md:w-40 md:h-60 object-cover rounded shadow-md"
          />
          <div class="flex flex-col items-center md:hidden">
            <p class="text-xs text-gray-500">favourite</p>
            <div
              class="bg-purple-500 text-white font-bold w-8 h-8 rounded-full flex items-center justify-center text-sm mt-1"
            >
              {{ book.favoritedBy?.length || 0 }}
            </div>
          </div>
        </div>
        <div class="md:w-2/3 mt-4 md:mt-0 md:ml-6">
          <h1 class="text-xl md:text-3xl font-bold text-gray-800 mb-8">
            {{ book.bookTitle }}
          </h1>
          <!-- Book Info -->
          <div class="flex-1">
            <h2 class="text-2xl font-bold text-gray-800 mb-2">
              {{ book.title }}
            </h2>
            <div
              class="grid grid-cols-2 md:grid-cols-6 gap-4 text-sm text-gray-700"
            >
              <span>
                <p class="text-gray-400 text-sm">Author</p>
                <strong class="text-black text-justify text-base">{{
                  book.author
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Publisher</p>
                <strong class="text-black text-base">{{
                  book.publisher
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">ISBN</p>
                <strong class="text-black text-sm mt-1">{{ book.isbn }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Edition</p>
                <strong class="text-black text-base">{{ book.edition }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Year</p>
                <strong class="text-black text-base">{{ book.year }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Price</p>
                <strong class="text-black text-base"
                  >{{ book.price.toLocaleString() }} THB</strong
                >
              </span>
            </div>
          </div>
        </div>

        <!-- Quantity (Favorite Count) -->
        <!-- ✅ Favorite Count (เฉพาะจอใหญ่) -->
        <div class="hidden md:flex flex-col items-end text-right">
          <p class="text-sm text-gray-500 mb-1">favourite</p>
          <div
            class="bg-purple-500 text-white font-bold w-10 h-10 rounded-full flex items-center justify-center"
          >
            {{ book.favoritedBy?.length || 0 }}
          </div>
        </div>
      </div>

      <!-- Table of users who favorited the book -->
      <div class="bg-white rounded-xl shadow overflow-x-auto">
        <table class="w-full table-auto text-sm text-left">
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
onMounted(async () => {
  const bookId = parseInt(route.params.id);

  // 🔁 ถ้า allBooks ยังไม่มี ให้โหลดใหม่
  if (favouritesStore.allBooks.length === 0) {
    await favouritesStore.fetchAllBooks();
  }

  const found = favouritesStore.allBooks.find((b) => b.id === bookId);

  if (found) {
    try {
      const res = await fetch(
        `http://localhost:8080/api/auth/favorites/book/${bookId}/users`,
        {
          credentials: "include",
        }
      );
      const users = await res.json();
      found.favoritedBy = users;
      book.value = found;

      console.log("👤 Users:", book.value.favoritedBy);
    } catch (error) {
      console.error("❌ Failed to fetch users:", error);
      found.favoritedBy = [];
      book.value = found;
    }
  }
});
</script>
