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
      <h1 class="text-2xl font-semibold text-gray-800 mb-6">
        Your Favourite Books
      </h1>

      <div v-if="favouritesStore.isLoading" class="text-gray-500">
        Loading...
      </div>
      <div v-else-if="favourites.length === 0" class="text-gray-500">
        You have no favourite books yet.
      </div>

      <div v-else class="space-y-4">
        <router-link
          v-for="book in favourites"
          :key="book.id"
          :to="`/book/${book.id}`"
          class="block bg-white rounded-lg shadow-md flex items-center p-4 hover:bg-gray-50 transition"
        >
          <!-- Cover -->
          <img
            :src="book.coverImage"
            alt="Book Cover"
            class="w-[80px] h-[120px] object-cover rounded"
          />

          <!-- Info -->
          <div class="ml-4 flex-1">
            <h3 class="text-lg font-semibold text-gray-800">
              {{ book.bookTitle }}
            </h3>
            <p class="text-sm text-gray-500">{{ book.author || "Unknown" }}</p>
          </div>
        </router-link>
      </div>
    </div>

    <Footer class="mt-10" />
  </div>
</template>

<script setup>
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import { useFavouritesStore } from "@/stores/favourites";
import { onMounted, computed } from "vue";
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
</script>
