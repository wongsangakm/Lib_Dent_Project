<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center"
    :style="{
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
      <Header />
  
      <div class="container mx-auto px-4 pt-28">
        <h1 class="text-2xl font-semibold text-gray-800 mb-6">Your Favourite Books</h1>
  
        <div v-if="favouritesStore.isLoading" class="text-gray-500">Loading...</div>
        <div v-else-if="favourites.length === 0" class="text-gray-500">You have no favourite books yet.</div>
  
        <div
          v-else
          class="space-y-4"
        >
          <div
            v-for="book in favourites"
            :key="book.id"
            class="bg-white rounded-lg shadow-md flex items-center p-4"
          >
            <!-- Cover -->
            <img
              :src="book.coverImage"
              alt="Book Cover"
              class="w-[80px] h-[120px] object-cover rounded"
            />
  
            <!-- Info -->
            <div class="ml-4 flex-1">
              <h3 class="text-lg font-semibold text-gray-800">{{ book.bookTitle }}</h3>
              <p class="text-sm text-gray-500">{{ book.author || 'Unknown' }}</p>
            </div>
  
            <!-- Delete -->
            <button
              @click="removeFromFavourite(book.id)"
              class="text-gray-400 hover:text-red-500 transition"
              title="Remove from favourites"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>
  
      <Footer class="mt-10" />
    </div>
  </template>
  
<script setup>
import Header from '@/component/Header.vue';
import Footer from '@/component/Footer.vue';
import { useFavouritesStore } from '@/stores/favourites';
import { onMounted, computed } from 'vue';
import bgImage from "@/image/Background.png";

const favouritesStore = useFavouritesStore();

onMounted(async () => {
  try {
    const checkLogin = await fetch("http://localhost:8080/api/auth/me", {
      credentials: "include"
    });
    if (!checkLogin.ok) {
      alert("กรุณาล็อกอินก่อนดูรายการโปรด");
      window.location.href = "/login";
      return;
    }

    await favouritesStore.fetchFavourites(); // ดึง favbooks
  } catch (error) {
    console.error("❌ Error fetching favourites:", error);
  }
});

onMounted(async () => {
  try {
    // เรียกใช้ mock หรือ API จริงในอนาคต
    await favouritesStore.fetchFavourites();
  } catch (error) {
    console.error("❌ Error fetching favourites:", error);
  }
});

const favourites = computed(() => favouritesStore.favourites);

const removeFromFavourite = async (bookId) => {
  try {
    await favouritesStore.removeFavourite(bookId);
    console.log(`✅ Removed book ${bookId} from favourites`);
  } catch (error) {
    console.error(`❌ Error removing book ${bookId} from favourites:`, error);
  }
};
</script>
