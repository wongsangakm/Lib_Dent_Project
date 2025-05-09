<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center z-[-1]"
    :style="{
      backgroundImage: `url(${bgImage})`,
    }"
  >
    <div>
      <div
        class="absolute top-[500px] bottom-0 left-0 right-0 bg-repeat-y"
        style="background-image: url('/images/bg_repeat.png')"
      ></div>
      <Header />
      <!-- Hero Section -->
      <section class="pt-28">
        <div
          class="container mx-auto px-4 flex flex-col md:flex-row items-center"
        >
          <div class="md:w-1/2 flex justify-end">
            <img :src="dentdesign" class="w-64 h-64 object-cover" />
          </div>
          <div class="md:w-1/2 text-center md:text-left">
            <h1 class="text-5xl font-bold text-gray-800 mb-2 text-center">
              BOOK
            </h1>
            <h1 class="text-4xl font-bold text-gray-800 mb-4 text-center">
              REQUEST
            </h1>
            <div class="">
              <p class="text-gray-600 text-center">
                You can suggest books you'd like the dental library kku to
                acquire
              </p>
              <p class="text-gray-600 text-center">
                We welcome your recommendations and will consider them to
                support your learning and research.
              </p>
            </div>
          </div>
        </div>
      </section>

      <!-- Search Bar and Search Results -->
      <section
        id="searchbook"
        class="scroll-mt-24 container mx-auto px-4 mt-8 rounded-3xl bg-white/50"
      >
        <div class="p-12 max-w-lg mx-auto">
          <!-- Search Bar -->
          <div class="flex w-full items-center space-x-2">
            <div class="relative w-full">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search book..."
                class="bg-gradient-to-r from-purple-200 via-white-100 to-white-100 text-gray-800 w-full px-4 py-2 pl-10 border border-gray-300 rounded-full focus:outline-none focus:ring-2 focus:ring-purple-600"
                @keyup.enter="searchBooks"
              />
              <svg
                class="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                ></path>
              </svg>
            </div>
            <button
              @click="searchBooks"
              class="px-6 py-2 bg-purple-600 text-white rounded-full hover:bg-purple-700 transition-colors"
            >
              Search
            </button>
          </div>

          <!-- Search Results -->
          <div v-if="filteredBooks.length > 0" class="mt-6">
            <div class="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-3 gap-6">
              <div
                v-for="book in filteredBooks"
                :key="book.id"
                class="text-center"
              >
                <div class="relative">
                  <router-link :to="`/book/${book.id}`">
                    <img
                      :src="validCoverImage(book.coverImage)"
                      alt="Book Cover"
                      class="w-[150px] h-[200px] object-cover mx-auto rounded-lg shadow-md hover:shadow-lg transition-shadow"
                    />
                  </router-link>
                  <button
                    @click="addToFavorite(book)"
                    :disabled="book.isFavorited || book.isLoading"
                    class="absolute top-2 right-2 bg-white bg-opacity-80 text-l p-2 rounded-full shadow-lg"
                    :class="{
                      'text-red-500': book.isFavorited,
                      'text-gray-400 hover:text-red-300':
                        !book.isFavorited && !book.isLoading,
                      'opacity-50 cursor-not-allowed':
                        book.isLoading || book.isFavorited,
                    }"
                  >
                    <i
                      :class="[book.isFavorited ? 'fas' : 'far', 'fa-heart']"
                    ></i>
                  </button>
                </div>
                <router-link :to="`/book/${book.id}`" class="block">
                  <p class="text-gray-500 text-xs italic truncate mt-2">
                    By {{ book.author }}
                  </p>
                  <p class="mt-0 text-black-600 text-base font-bold truncate">
                    {{ book.bookTitle }}
                  </p>
                  <p class="text-gray-500 italic truncate text-xs">
                    price {{ book.price.toLocaleString() }} THB
                  </p>
                </router-link>

                <div class="flex justify-center mt-2">
                  <button
                    @click="addToFavorite(book)"
                    :disabled="book.isFavorited || book.isLoading"
                    class="px-4 py-1 text-white rounded-full flex items-center transition-all duration-200"
                    :class="{
                      'bg-purple-600 hover:bg-purple-700':
                        !book.isFavorited && !book.isLoading,
                      'bg-gray-400': book.isFavorited,
                      'opacity-50 cursor-not-allowed':
                        book.isLoading || book.isFavorited,
                    }"
                  >
                    <span class="text-sm">{{
                      book.isFavorited ? "Added to Favorite" : "Add to Favorite"
                    }}</span>
                    <svg
                      class="ml-2 w-6 h-6"
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
          <div
            v-else-if="hasSearched && searchQuery && filteredBooks.length === 0"
            class="mt-6 text-center"
          >
            <p class="text-gray-600">No books found for "{{ searchQuery }}".</p>
          </div>
        </div>
      </section>
      <!-- Favourite by Publisher -->
      <section id="Favbypub" class="py-6 pt-5">
        <div class="container mx-auto px-4">
          <h2 class="text-3xl mt-8 mb-4 font-bold text-gray-800 mb-4">
            Favourite by Publisher
          </h2>
          <div class="flex items-center space-x-2">
            <!-- Left Arrow -->
            <button
              v-if="showLeftArrow"
              @click="scrollLeft"
              class="relative z-10 bg-purple-600 text-white rounded-full p-2 hover:bg-purple-700 transition-colors"
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
                  d="M15 19l-7-7 7-7"
                ></path>
              </svg>
            </button>
            <!-- Publisher Buttons -->
            <div
              ref="publisherContainer"
              class="flex-1 flex space-x-3 overflow-x-auto pb-2 scroll-smooth"
              @scroll="updateScrollPosition"
            >
              <button
                v-for="publisher in publishers"
                :key="publisher.name"
                @click="selectPublisher(publisher.name)"
                class="relative z-10 flex items-center px-4 py-2 rounded-lg shadow-md transition-colors whitespace-nowrap"
                :class="{
                  'bg-purple-600 text-white':
                    selectedPublisher === publisher.name,
                  'bg-white text-gray-600 hover:bg-gray-100':
                    selectedPublisher !== publisher.name,
                }"
              >
                <svg
                  class="w-5 h-5 mr-2"
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
                <span>{{ publisher.name }}</span>
                <span class="ml-2 text-xs opacity-75"
                  >{{ publisher.items }} Items</span
                >
              </button>
            </div>
            <!-- Right Arrow -->
            <button
              v-if="showRightArrow"
              @click="scrollRight"
              class="relative z-10 bg-purple-600 text-white rounded-full p-2 hover:bg-purple-700 transition-colors"
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
                  d="M9 5l7 7-7 7"
                ></path>
              </svg>
            </button>
          </div>
        </div>
      </section>

      <!-- All Books Section -->
      <section id="all-books" class="py-12">
        <div class="container mx-auto px-4">
          <h2 class="text-3xl font-bold text-gray-800 mb-8">
            {{
              selectedPublisher === "All Books"
                ? "All Books"
                : selectedPublisher
            }}
          </h2>
          <div
            v-if="filteredBooksByPublisher.length > 0"
            class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-6"
          >
            <div
              v-for="book in filteredBooksByPublisher"
              :key="book.id"
              class="text-center"
            >
              <div class="relative">
                <router-link :to="`/book/${book.id}`">
                  <img
                    :src="validCoverImage(book.coverImage)"
                    alt="Book Cover"
                    class="w-[160px] h-[240px] object-cover mx-auto rounded-lg shadow-md hover:shadow-lg transition-shadow"
                  />
                </router-link>
                <!-- Heart Button -->
                <button
                  @click="addToFavorite(book)"
                  :disabled="book.isFavorited || book.isLoading"
                  class="absolute top-2 right-2 bg-white bg-opacity-80 text-l p-2 rounded-full shadow-lg"
                  :class="{
                    'text-red-500': book.isFavorited,
                    'text-gray-400 hover:text-red-300':
                      !book.isFavorited && !book.isLoading,
                    'opacity-50 cursor-not-allowed':
                      book.isLoading || book.isFavorited,
                  }"
                >
                  <i
                    :class="[book.isFavorited ? 'fas' : 'far', 'fa-heart']"
                  ></i>
                </button>
              </div>
              <router-link :to="`/book/${book.id}`" class="block">
                <p class="text-gray-500 text-xs italic truncate mt-2">
                  By {{ book.author }}
                </p>
                <p class="mt-0 text-black-600 text-base font-bold truncate">
                  {{ book.bookTitle }}
                </p>
                <p class="text-gray-500 italic truncate text-xs">
                  price {{ book.price.toLocaleString() }} THB
                </p>
              </router-link>
              <!-- Add to Favorite Button -->
              <div class="flex justify-center mt-2">
                <button
                  @click="addToFavorite(book)"
                  :disabled="book.isFavorited || book.isLoading"
                  class="px-4 py-1 text-white rounded-full flex items-center transition-all duration-200"
                  :class="{
                    'bg-purple-600 hover:bg-purple-700':
                      !book.isFavorited && !book.isLoading,
                    'bg-gray-400': book.isFavorited,
                    'opacity-50 cursor-not-allowed':
                      book.isLoading || book.isFavorited,
                  }"
                >
                  <span class="text-xs">{{
                    book.isFavorited ? "Added to Favorite" : "Add to Favorite"
                  }}</span>
                  <svg
                    class="ml-2 w-4 h-4"
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
          <div v-else class="text-center text-gray-600">
            <p>No books found for this publisher.</p>
          </div>
        </div>
      </section>

      <!-- Why Shop with Us? Section -->
      <section id="why" class="py-12 bg-purple-100 w-screen h-[400px]">
        <div
          class="container mx-auto px-4 h-full flex flex-col md:flex-row items-center justify-center"
        >
          <div class="md:w-1/2 mb-6 md:mb-0">
            <img
              src="https://via.placeholder.com/500x300"
              alt="Books"
              class="rounded-lg shadow-lg"
            />
          </div>
          <div class="md:w-1/2 md:pl-8 justify-en">
            <h2 class="text-3xl font-bold text-gray-800 mb-4">
              Why Shop with Us?
            </h2>
            <p class="text-gray-600 mb-4 flex d">
              We offer a wide selection of books from bestselling authors,
              competitive prices, and fast shipping. Discover your next favorite
              read with us!
            </p>
          </div>
        </div>
      </section>

      <!-- Footer -->
      <Footer class="mt-8" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
import dentdesign from "@/assets/dentdesign.svg";
import { useFavouritesStore } from "@/stores/favourites";
import axios from "axios";

const favouritesStore = useFavouritesStore(); // Initialize Pinia store

const scrollToSection = (sectionId) => {
  const element = document.getElementById(sectionId);
  if (element) {
    element.scrollIntoView({ behavior: "smooth" });
  }
};

const publishers = ref([
  { name: "All Books", items: 0 },
  { name: "Springer", items: 0 },
  { name: "Quintessence", items: 0 },
  { name: "Wiley", items: 0 },
  { name: "Elsevier", items: 0 },
  { name: "CRC Press", items: 0 },
  { name: "Jones & Bartlett Learning", items: 0 },
]);

const selectedPublisher = ref("All Books");

const selectPublisher = (publisherName) => {
  selectedPublisher.value = publisherName;
};

const publisherContainer = ref(null);
const scrollPosition = ref(0);
const showLeftArrow = ref(false);
const showRightArrow = ref(true);

const updateScrollPosition = () => {
  if (publisherContainer.value) {
    scrollPosition.value = publisherContainer.value.scrollLeft;
    const maxScroll =
      publisherContainer.value.scrollWidth -
      publisherContainer.value.clientWidth;
    showLeftArrow.value = scrollPosition.value > 0;
    showRightArrow.value = scrollPosition.value < maxScroll - 1;
  }
};

const scrollLeft = () => {
  if (publisherContainer.value) {
    publisherContainer.value.scrollBy({ left: -200, behavior: "smooth" });
  }
};

const scrollRight = () => {
  if (publisherContainer.value) {
    publisherContainer.value.scrollBy({ left: 200, behavior: "smooth" });
  }
};

const searchQuery = ref("");
const hasSearched = ref(false);

// Use allBooks from Pinia Store
const allBooks = computed(() => favouritesStore.allBooks);

// Update publisher items count based on allBooks
onMounted(async () => {
  try {
    // Fetch all books
    await favouritesStore.fetchAllBooks();

    // Check favorite status for each book
    for (const book of favouritesStore.allBooks) {
      await fetchFavoriteStatus(book);
    }

    // Update publisher counts
    updatePublisherCounts();
  } catch (error) {
    console.error("Error loading books:", error);
  }
});

// Update publisher counts
const updatePublisherCounts = () => {
  publishers.value = publishers.value.map((publisher) => ({
    ...publisher,
    items:
      publisher.name === "All Books"
        ? favouritesStore.allBooks.length
        : favouritesStore.allBooks.filter(
            (book) => book.publisher === publisher.name
          ).length,
  }));
};

// Fetch favorite status for a book
const fetchFavoriteStatus = async (book) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/favorites/${book.id}`,{
      credentials: "include"
    });
    if (!response.ok) throw new Error("Failed to fetch favorite status");

    const data = await response.json();
    book.isFavorited = data?.isFavorited === true;
    book.isLoading = false;

    return data;
  } catch (error) {
    console.error(`Error fetching favorite status for book ${book.id}:`, error);
    book.isFavorited = false;
    book.isLoading = false;
    return { isFavorited: false };
  }
};

// Add book to favorites
const addToFavorite = async (book) => {
  // Return early if already favorited or loading
  if (book.isFavorited || book.isLoading) return;

  // Set loading state
  book.isLoading = true;

  try {
    const response = await fetch(`http://localhost:8080/api/favorites`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ bookId: book.id }),
    });

    if (!response.ok) throw new Error("Failed to add favorite");

    const result = await response.json();
    console.log("✅ Favorite added response:", result);

    if (result.success) {
      book.isFavorited = true;
    }
  } catch (error) {
    console.error("❌ Error adding to favorites:", error);
  } finally {
    book.isLoading = false;
  }
};

// Computed property to filter books by selected publisher
const filteredBooksByPublisher = computed(() => {
  if (!allBooks.value) return [];
  if (selectedPublisher.value === "All Books") {
    return allBooks.value;
  }
  return allBooks.value.filter(
    (book) => book.publisher === selectedPublisher.value
  );
});

// Computed property for search results
const filteredBooks = computed(() => {
  if (!allBooks.value) return [];
  if (!searchQuery.value.trim()) return [];
  return allBooks.value.filter((book) =>
    book.bookTitle.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const searchBooks = () => {
  hasSearched.value = true;
};

function validCoverImage(url) {
  // กรอง URL ที่ขึ้นต้นด้วย springernature และมีชื่อไฟล์ pattern ที่ชัดเจน
  if (!url || url.includes("3-540-29011-7.jpg")) {
    return "https://via.placeholder.com/150x200?text=No+Image";
  }
  return url;
}
</script>


<style scoped>
/* Hide scrollbar but keep functionality */
.overflow-x-auto::-webkit-scrollbar {
  display: none;
}
.overflow-x-auto {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}

/* Add explicit cursor styles for favorite buttons */
button:disabled {
  cursor: not-allowed !important;
}
</style>
