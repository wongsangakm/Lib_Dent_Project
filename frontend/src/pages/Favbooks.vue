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

    <div class="container mx-auto px-4 pt-28 w-full max-w-4xl">
      <h1 class="text-2xl font-semibold text-gray-800 mb-6 text-center">
        Your Library
      </h1>

      <!-- Tabs -->
      <div class="flex justify-center space-x-4 mb-6">
        <button
          @click="activeTab = 'favourites'"
          :class="[
            'px-4 py-2 rounded-md font-medium transition',
            activeTab === 'favourites'
              ? 'bg-purple-600 text-white'
              : 'bg-white text-purple-600 border border-purple-600 hover:bg-purple-50',
          ]"
        >
          Favourite Books
        </button>
        <button
          @click="activeTab = 'requests'"
          :class="[
            'px-4 py-2 rounded-md font-medium transition',
            activeTab === 'requests'
              ? 'bg-purple-600 text-white'
              : 'bg-white text-purple-600 border border-purple-600 hover:bg-purple-50',
          ]"
        >
          Additional Request History
        </button>
      </div>

      <!-- Favourite Books -->
      <div
        v-if="activeTab === 'favourites'"
        class="bg-white shadow-md rounded-xl overflow-hidden p-4"
      >
        <div
          v-if="favouritesStore.isLoading"
          class="text-lg font-semibold mb-3 text-purple-700"
        >
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
            <img
              :src="book.coverImage"
              alt="Book Cover"
              class="w-[80px] h-[120px] object-cover rounded"
            />
            <div class="ml-4 flex-1">
              <h3 class="text-lg font-semibold text-gray-800">
                {{ book.bookTitle }}
              </h3>
              <p class="text-sm text-gray-500">
                {{ book.author || "Unknown" }}
              </p>
            </div>
          </router-link>
        </div>
      </div>

      <!-- Request History -->
      <div
        v-else-if="activeTab === 'requests'"
        class="bg-white shadow-md rounded-xl overflow-hidden p-4"
      >
        <div v-if="requestHistory.length === 0" class="text-gray-500">
          No additional requests yet.
        </div>
        <ul v-else class="space-y-4">
          <li
            v-for="(request, index) in requestHistory"
            :key="index"
            class="bg-gray-50 rounded-lg shadow-sm p-4 flex justify-between items-center"
          >
            <div>
              <p class="text-gray-800 font-semibold">
                Requested: {{ request.title }}
              </p>
              <p class="text-sm text-gray-500">Date: {{ request.date }}</p>
              <p class="text-sm text-gray-500">
                Status:
                <span
                  class="ml-1"
                  :class="{
                    'text-yellow-500 font-semibold':
                      request.status === 'PENDING',
                    'text-green-600 font-semibold': isApprovedStatus(
                      request.status
                    ),
                    'text-red-600 font-semibold': request.status === 'REJECTED',
                  }"
                  >{{ displayStatus(request.status) }}</span
                >
              </p>
            </div>

            <router-link
              :to="`/request/${request.id}`"
              class="ml-4 bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-md text-sm transition"
            >
              View
            </router-link>
          </li>
        </ul>
      </div>
    </div>

    <Footer class="mt-10" />
  </div>
</template>

<script setup>
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import { useFavouritesStore } from "@/stores/favourites";
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import bgImage from "@/image/Background.png";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();
const favouritesStore = useFavouritesStore();
const favourites = computed(() => favouritesStore.favourites);

// Request history (ดึงจาก backend จริง)
const requestHistory = ref([]);
const baseURL = import.meta.env.VITE_API_BASE_URL;
const fetchRequestHistory = async () => {
  try {
    const response = await axios.get(`${baseURL}/api/requests/history`, {
      headers: authStore.getAuthHeader(),
    });
    requestHistory.value = response.data.map((req) => ({
      id: req.id,
      title: req.bookTitle || req.title || "No Title",
      date: new Date(req.requestDate).toLocaleDateString(),
      status: req.status || "Unknown",
    }));
  } catch (error) {
    console.error("❌ Error fetching request history:", error);
  }
};

onMounted(async () => {
  console.log("📦 JWT จาก authStore:", authStore.jwt);
  try {
    await favouritesStore.fetchFavourites();
    console.log("📚 Fetched favourites:", favourites.value);
    await fetchRequestHistory(); // โหลด request history จริงจาก backend
  } catch (error) {
    console.error("❌ Error fetching data:", error);
  }
});

// Tabs
const activeTab = ref("favourites");

// onMounted(async () => {
//   try {
//     await favouritesStore.fetchFavourites();
//   } catch (error) {
//     console.error("❌ Error fetching favourites:", error);
//   }
// });

const isApprovedStatus = (status) => {
  return ["APPROVED", "in_shelf", "ordered", "popular_request"].includes(
    status
  );
};

const displayStatus = (status) => {
  if (["in_shelf", "ordered", "popular_request"].includes(status))
    return "APPROVED";
  return status;
};
</script>
