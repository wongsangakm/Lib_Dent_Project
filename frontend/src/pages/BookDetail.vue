<template>
  <div>
    <Header />
    <div
      class="min-h-screen flex flex-col bg-gray-100 items-center py-12"
      :style="{
        backgroundImage: `url(${bgImage})`,
        backgroundPosition: '0% 6%',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
      }"
    >
      <!-- Loading State -->
      <div v-if="isLoadingData" class="text-center text-gray-600">
        <p>Loading book details...</p>
      </div>

      <!-- Book Details -->
      <div v-else-if="bookData" class="mt-20">
        <div
          class="flex flex-col md:flex-row max-w-5xl bg-white rounded-lg shadow-lg p-6 bg-white/40"
        >
          <!-- Book Cover -->
          <div class="md:w-1/3 flex justify-center relative">
            <!-- Heart Button -->
            <!-- หัวใจบนปก -->
            <button
              @click="addToFavorite"
              :disabled="isLoading || isFavorited"
              class="absolute top-3 right-12 bg-white bg-opacity-80 text-l p-2 rounded-full transition-all duration-200 shadow-lg"
              :class="{
                'text-red-500 bg-red-200': isFavorited,
                'text-gray-400 hover:text-red-300': !isFavorited && !isLoading,
                'opacity-50 cursor-not-allowed': isLoading || isFavorited,
              }"
            >
              <i :class="[isFavorited ? 'fas' : 'far', 'fa-heart']"></i>
            </button>

            <!-- Book Image -->
            <img
              :src="bookData.coverImage"
              alt="Book Cover"
              class="w-[260px] h-[380px] object-cover rounded"
            />
          </div>

          <!-- Book Details -->
          <div class="md:w-2/3 mt-4 md:mt-0 md:ml-6">
            <h1 class="text-3xl font-bold text-gray-800">
              {{ bookData.bookTitle }}
            </h1>

            <!-- Book Info -->
            <div
              class="border-b border-gray-300 mr-4 pb-4 grid gap-4 mt-6 text-sm text-gray-600"
              style="grid-template-columns: 2fr 1fr 0.5fr 0.5fr 1fr"
            >
              <span>
                <p class="text-gray-400 text-sm">Author</p>
                <strong class="text-black text-justify text-base">{{
                  bookData.author
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Publisher</p>
                <strong class="text-black text-base">{{
                  bookData.publisher
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Edition</p>
                <strong class="text-black text-base">{{
                  bookData.edition
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Year</p>
                <strong class="text-black text-base">{{
                  bookData.year
                }}</strong>
              </span>
              <span>
                <p class="text-gray-400 text-sm">Price</p>
                <strong class="text-black text-base"
                  >{{ bookData.price.toLocaleString() }} THB</strong
                >
              </span>
            </div>

            <!-- Description -->
            <p
              class="mt-4 text-gray-600 text-base leading-relaxed max-h-40 overflow-y-auto tracking-wide indent-6 text-justify"
            >
              {{ bookData.description }}
            </p>

            <!-- Add to Favorite Button -->
            <div class="flex justify-end mt-6">
              <button
                @click="addToFavorite"
                :disabled="isLoading || isFavorited"
                class="px-4 py-2 text-white rounded-full flex items-center transition-all duration-200"
                :class="[
                  isFavorited
                    ? 'bg-gray-400'
                    : 'bg-purple-800 hover:bg-purple-700',
                  (isLoading || isFavorited) && 'opacity-50 cursor-not-allowed',
                ]"
              >
                <span>{{
                  isFavorited ? "Added to Favorite" : "Add to Favorite"
                }}</span>
                <svg
                  class="ml-2 w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                  ></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Error State: Book Not Found -->
      <div v-else class="container mx-auto px-4 text-center">
        <div class="flex flex-col items-center justify-center min-h-[50vh]">
          <svg
            class="w-16 h-16 text-gray-400 mb-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5s3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18s-3.332.477-4.5 1.253"
            ></path>
          </svg>
          <h2 class="text-2xl font-bold text-gray-800 mb-2">Book Not Found</h2>
          <p class="text-gray-600 mb-4">
            Sorry, we couldn't find the book you're looking for.
          </p>
          <router-link
            to="/"
            class="inline-flex items-center px-6 py-2 bg-purple-600 text-white rounded-full hover:bg-purple-700 transition-colors"
          >
            Back to Home
          </router-link>
        </div>
      </div>

      <Footer class="mt-8" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";

const route = useRoute();
const bookData = ref(null);
const isLoading = ref(false); // Changed initial value to false
const isFavorited = ref(false);
const isLoadingData = ref(true);

onMounted(async () => {
  const bookId = route.params.id;
  if (!bookId) {
    isLoadingData.value = false;
    return;
  }

  try {
    // Fetch book data
    await fetchBookData(bookId);

    // Check favorite status
    await fetchFavoriteStatus(bookId);
  } catch (error) {
    console.error("Error during initialization:", error);
  } finally {
    isLoadingData.value = false;
  }
});

const fetchBookData = async (bookId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/books/${bookId}`);
    if (!response.ok) throw new Error("Failed to fetch book data");
    bookData.value = await response.json();
    console.log("✅ Book data fetched:", bookData.value);
  } catch (error) {
    console.error("❌ Error fetching book data:", error);
    bookData.value = null;
  }
};

const addToFavorite = async () => {
  console.log("🔥 CLICK: AddToFavorite");
  console.log("🧪 BEFORE isFavorited:", isFavorited.value);
  console.log("🧪 isLoading:", isLoading.value);

  // Return early if already favorited or loading
  if (isFavorited.value || isLoading.value) return;

  isLoading.value = true;
  try {
    const response = await fetch(`http://localhost:8080/api/favorites`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ bookId: bookData.value.id }),
    });

    if (!response.ok) throw new Error("Failed to add favorite");

    const result = await response.json();
    console.log("✅ Favorite added response:", result);

    if (result.success) {
      isFavorited.value = true;
    }
  } catch (error) {
    console.error("❌ Error adding to favorites:", error);
  } finally {
    console.log("🧹 Setting isLoading = false");
    isLoading.value = false;
  }
};

const fetchFavoriteStatus = async (bookId) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/favorites/${bookId}`
    );

    if (!response.ok) throw new Error("Failed to fetch favorite status");

    const data = await response.json();
    console.log("✅ Favorite status:", data);
    isFavorited.value = data?.isFavorited === true;
  } catch (error) {
    console.error("❌ Error fetching favorite status:", error);
    isFavorited.value = false;
  }
};
</script>

<style scoped>
.container {
  max-width: 1200px;
}

/* Add explicit cursor styles for favorite buttons */
button:disabled {
  cursor: not-allowed !important;
}
</style>
