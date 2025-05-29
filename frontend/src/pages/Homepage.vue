<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center z-[-1]"
    :style="{
      backgroundImage: `url(${bgImage})`,
    }"
  >
    <div class="w-full">
      <div
        class="absolute top-[500px] bottom-0 left-0 right-0 bg-repeat-y"
        style="background-image: url('/images/bg_repeat.png')"
      ></div>
      <Header />
      <!-- Hero Section -->
      <section class="pt-16 md:pt-28">
        <div
          class="container mx-auto px-4 flex flex-col md:flex-row items-center gap-6"
        >
          <div
            class="w-full md:w-1/2 flex justify-center md:justify-end order-2 md:order-1"
          >
            <img
              :src="dentdesign"
              class="w-48 h-48 md:w-64 md:h-64 object-cover"
            />
          </div>
          <div
            class="w-full md:w-1/2 text-center md:text-left order-1 md:order-2"
          >
            <h1
              class="text-4xl md:text-5xl font-bold text-gray-800 mb-2 text-center"
            >
              BOOK
            </h1>
            <h1
              class="text-3xl md:text-4xl font-bold text-gray-800 mb-4 text-center"
            >
              REQUEST
            </h1>
            <div class="px-4">
              <p class="text-sm md:text-base text-gray-600 text-center">
                You can suggest books you'd like the dental library kku to
                acquire
              </p>
              <p class="text-sm md:text-base text-gray-600 text-center">
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
        <!-- Book Request Form (Above Search Bar) -->
        <div
          v-if="isLoggedIn"
          class="relative z-10 p-6 md:p-8 border-b border-gray-200"
        >
          <div class="max-w-4xl mx-auto">
            <div class="mb-6">
              <h2
                class="text-2xl font-bold text-purple-700 flex items-center gap-2"
              >
                <i class="fas fa-book-medical"></i>
                Additional Book Request
              </h2>
            </div>

            <div class="transition-all duration-300">
              <form
                @submit.prevent="submitRequest"
                class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4"
              >
                <!-- First Row -->
                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">Book Title</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.bookTitle"
                      type="text"
                      class="form-input"
                      placeholder="Enter book title"
                    />
                    <p v-if="bookTitleError" class="text-red-500 text-xs mt-1">
                      {{ bookTitleError }}
                    </p>
                  </label>
                </div>

                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">Author</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.author"
                      type="text"
                      class="form-input"
                      placeholder="Enter author name"
                    />
                    <p v-if="authorError" class="text-red-500 text-xs mt-1">
                      {{ authorError }}
                    </p>
                  </label>
                </div>

                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">Publisher</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.publisher"
                      type="text"
                      class="form-input"
                      placeholder="Enter publisher"
                    />
                    <p v-if="publisherError" class="text-red-500 text-xs mt-1">
                      {{ publisherError }}
                    </p>
                  </label>
                </div>

                <!-- Second Row -->
                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">ISBN</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.isbn"
                      type="text"
                      class="form-input"
                      placeholder="e.g. 978-3-16-148410-0"
                      @input="onISBNInput"
                    />
                    <p v-if="isbnError" class="text-red-500 text-xs mt-1">
                      {{ isbnError }}
                    </p>
                  </label>
                </div>

                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">Year</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.year"
                      type="text"
                      inputmode="numeric"
                      pattern="\d*"
                      class="form-input"
                      placeholder="Publication year"
                    />
                    <p v-if="yearError" class="text-red-500 text-xs mt-1">
                      {{ yearError }}
                    </p>
                  </label>
                </div>

                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium">Price</span>
                    <span class="text-red-500">*</span>
                    <input
                      v-model="request.price"
                      type="text"
                      inputmode="decimal"
                      class="form-input"
                      placeholder="Price in THB"
                    />
                    <p v-if="priceError" class="text-red-500 text-xs mt-1">
                      {{ priceError }}
                    </p>
                  </label>
                </div>

                <!-- Third Row -->
                <div class="md:col-span-2 space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium"
                      >Description (หรือ Link สำหรับดูเพิ่มเติม)</span
                    >
                    <textarea
                      v-model="request.description"
                      class="form-input resize-none"
                      rows="3"
                      placeholder="Additional information or links..."
                    ></textarea>
                  </label>
                </div>

                <div class="space-y-1">
                  <label class="block">
                    <span class="text-gray-700 font-medium"
                      >Reason / Notes</span
                    >
                    <span class="text-red-500">*</span>
                    <textarea
                      v-model="request.reason"
                      class="form-input resize-none"
                      rows="3"
                      placeholder="Why do you need this book?"
                    ></textarea>
                    <p v-if="reasonError" class="text-red-500 text-xs mt-1">
                      {{ reasonError }}
                    </p>
                  </label>
                </div>

                <!-- Submit Button -->
                <div class="md:col-span-2 lg:col-span-3 flex justify-end pt-4">
                  <button
                    type="submit"
                    :disabled="isLoading"
                    class="bg-purple-600 text-white px-5 py-3 rounded-full transition-all flex items-center gap-2 hover:bg-purple-700 hover:shadow-lg transform hover:scale-105"
                    :class="{ 'opacity-60 cursor-not-allowed': isLoading }"
                  >
                    <svg
                      v-if="isLoading"
                      class="animate-spin h-5 w-5 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        class="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        stroke-width="4"
                      ></circle>
                      <path
                        class="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8v4l3.5-3.5L12 0v4a8 8 0 000 16v4l3.5-3.5L12 20v-4a8 8 0 01-8-8z"
                      ></path>
                    </svg>
                    <i v-else class="fas fa-paper-plane"></i>
                    <span>{{
                      isLoading ? "Submitting..." : "Submit Request"
                    }}</span>
                  </button>
                </div>

                <div
                  v-if="globalError"
                  class="md:col-span-2 lg:col-span-3 text-right"
                >
                  <p
                    class="text-red-600 text-sm whitespace-pre-line bg-red-50 p-3 rounded-lg"
                  >
                    {{ globalError }}
                  </p>
                </div>
              </form>
            </div>
          </div>
        </div>
        <div class="p-4 md:p-12 max-w-lg mx-auto">
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
              class="px-4 md:px-6 py-2 bg-purple-600 text-white rounded-full hover:bg-purple-700 transition-colors text-sm md:text-base"
            >
              Search
            </button>
          </div>

          <!-- Search Results -->
          <div v-if="filteredBooks.length > 0" class="mt-6">
            <div
              class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 md:gap-6"
            >
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
                      class="w-[120px] h-[170px] md:w-[150px] md:h-[200px] object-cover mx-auto rounded-lg shadow-md hover:shadow-lg transition-shadow"
                    />
                  </router-link>
                  <button
                    @click="addToFavorite(book)"
                    :disabled="book.isFavorited || book.isLoading"
                    class="absolute top-2 right-2 bg-white bg-opacity-80 text-l p-1 md:p-2 rounded-full shadow-lg"
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
                  <p
                    class="mt-0 text-black-600 text-sm md:text-base font-bold truncate"
                  >
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
                    class="px-2 md:px-4 py-1 text-white rounded-full flex items-center transition-all duration-200"
                    :class="{
                      'bg-purple-600 hover:bg-purple-700':
                        !book.isFavorited && !book.isLoading,
                      'bg-gray-400': book.isFavorited,
                      'opacity-50 cursor-not-allowed':
                        book.isLoading || book.isFavorited,
                    }"
                  >
                    <span class="text-xs md:text-sm">{{
                      book.isFavorited ? "Added" : "Add to Favorite"
                    }}</span>
                    <svg
                      class="ml-1 md:ml-2 w-4 h-4 md:w-6 md:h-6"
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
        <!-- Additional Request Form -->
      </section>

      <!-- Favourite by Publisher -->
      <section id="Favbypub" class="py-4 md:py-6 pt-3 md:pt-5">
        <div class="container mx-auto px-4">
          <!-- หัวข้อ + ปุ่ม Additional Request -->
          <div
            class="flex justify-between items-center mt-4 md:mt-8 mb-2 md:mb-4"
          >
            <h2 class="text-2xl md:text-3xl font-bold text-gray-800">
              Favourite by Publisher
            </h2>
          </div>

          <!-- ลูกศร + ปุ่ม Publisher -->
          <div class="flex items-center space-x-2">
            <!-- Left Arrow -->
            <button
              v-if="showLeftArrow"
              @click="scrollLeft"
              class="relative z-10 bg-purple-600 text-white rounded-full p-1 md:p-2 hover:bg-purple-700 transition-colors"
            >
              <svg
                class="w-4 h-4 md:w-5 md:h-5"
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
              class="flex-1 flex space-x-2 md:space-x-3 overflow-x-auto pb-2 scroll-smooth"
              @scroll="updateScrollPosition"
            >
              <button
                v-for="publisher in publishers"
                :key="publisher.name"
                @click="selectPublisher(publisher.name)"
                class="relative z-10 flex items-center px-2 md:px-4 py-1 md:py-2 rounded-lg shadow-md transition-colors whitespace-nowrap text-xs md:text-base"
                :class="{
                  'bg-purple-600 text-white':
                    selectedPublisher === publisher.name,
                  'bg-white text-gray-600 hover:bg-gray-100':
                    selectedPublisher !== publisher.name,
                }"
              >
                <svg
                  class="w-4 h-4 md:w-5 md:h-5 mr-1 md:mr-2"
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
                <span class="ml-1 md:ml-2 text-xs opacity-75">
                  {{ publisher.items }}
                </span>
              </button>
            </div>

            <!-- Right Arrow -->
            <button
              v-if="showRightArrow"
              @click="scrollRight"
              class="relative z-10 bg-purple-600 text-white rounded-full p-1 md:p-2 hover:bg-purple-700 transition-colors"
            >
              <svg
                class="w-4 h-4 md:w-5 md:h-5"
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
      <section id="all-books" class="py-6 md:py-12">
        <div class="container mx-auto px-4">
          <h2 class="text-2xl md:text-3xl font-bold text-gray-800 mb-4 md:mb-8">
            {{
              selectedPublisher === "All Books"
                ? "All Books"
                : selectedPublisher
            }}
          </h2>
          <div
            v-if="filteredBooksByPublisher.length > 0"
            class="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3 md:gap-6"
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
                    class="w-[120px] h-[180px] md:w-[160px] md:h-[240px] object-cover mx-auto rounded-lg shadow-md hover:shadow-lg transition-shadow"
                  />
                </router-link>
                <!-- Heart Button -->
                <button
                  @click="addToFavorite(book)"
                  :disabled="book.isFavorited || book.isLoading"
                  class="absolute top-2 right-2 bg-white bg-opacity-80 text-l p-1 md:p-2 rounded-full shadow-lg"
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
                <p
                  class="mt-0 text-black-600 text-xs md:text-base font-bold truncate"
                >
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
                  class="px-2 md:px-4 py-1 text-white rounded-full flex items-center transition-all duration-200"
                  :class="{
                    'bg-purple-600 hover:bg-purple-700':
                      !book.isFavorited && !book.isLoading,
                    'bg-gray-400': book.isFavorited,
                    'opacity-50 cursor-not-allowed':
                      book.isLoading || book.isFavorited,
                  }"
                >
                  <span class="text-xs">{{
                    book.isFavorited ? "Added" : "Add to Favorite"
                  }}</span>
                  <svg
                    class="ml-1 md:ml-2 w-3 h-3 md:w-4 md:h-4"
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

      <!-- Why Shop with Us?? Section. -->
      <section id="why" class="bg-purple-100 w-full">
        <div
          class="container mx-auto px-4 flex flex-col md:flex-row items-center justify-center gap-6"
        >
          <div class="w-full md:w-1/2 mb-6 md:mb-0">
            <img
              :src="libdent"
              alt="Books"
              class="rounded-lg shadow-lg w-full h-auto"
            />
          </div>
          <div class="w-full md:w-1/2 md:pl-8">
            <h2 class="text-2xl md:text-3xl font-bold text-gray-800 mb-4">
              Why request Books with Us?
            </h2>
            <p class="text-sm md:text-base text-gray-600 mb-4">
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
import { ref, onMounted, computed, watch } from "vue";
import { useRouter } from "vue-router";
import { ElMessageBox } from "element-plus";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
import libdent from "@/assets/libdent.jpg";
import dentdesign from "@/assets/dentdesign.svg";
import { useFavouritesStore } from "@/stores/favourites";
import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
const baseURL = import.meta.env.VITE_API_BASE_URL;
const router = useRouter();
const authStore = useAuthStore();
const isLoggedIn = computed(() => authStore.isAuthenticated);
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
  { name: "LAP Lambert Academic Publishing", items: 0 },
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

    if (authStore.isAuthenticated) {
      await fetchAllFavoriteStatuses();
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
        : favouritesStore.allBooks.filter((book) =>
            book.publisher?.toLowerCase().includes(publisher.name.toLowerCase())
          ).length,
  }));
};

// Fetch favorite status for a book
const fetchAllFavoriteStatuses = async () => {
  try {
    const res = await fetch(`${baseURL}/api/auth/favorites/ids`, {
      headers: authStore.getAuthHeader(),
    });

    if (!res.ok) {
      console.error(`❌ Failed to fetch: ${res.status} ${res.statusText}`);
      return;
    }

    const favBookIds = await res.json();

    if (!Array.isArray(favBookIds)) {
      console.error("❌ favBookIds is not an array:", favBookIds);
      return;
    }

    favouritesStore.allBooks.forEach((book) => {
      book.isFavorited = favBookIds.includes(book.id);
    });
  } catch (err) {
    console.error("❌ Failed to load favorite status list:", err);
  }
};

// Add book to favorites
const addToFavorite = async (book) => {
  if (!isLoggedIn.value) {
    alert("กรุณาเข้าสู่ระบบก่อนกด Favorite");
    return;
  }
  if (book.isFavorited || book.isLoading) return;

  /* ✅ NEW — กล่องยืนยัน */
  const confirmed = await ElMessageBox.confirm(
    `ต้องการเพิ่ม “${book.bookTitle}” เข้ารายการโปรดใช่หรือไม่?`,
    "ยืนยันการเพิ่มรายการโปรด",
    { confirmButtonText: "ยืนยัน", cancelButtonText: "ยกเลิก", type: "warning" }
  )
    .then(() => true)
    .catch(() => false);

  if (!confirmed) return;

  // Set loading state.
  book.isLoading = true;

  try {
    const response = await fetch(`${baseURL}/api/auth/favorites/${book.id}`, {
      method: "POST",
      headers: {
        ...authStore.getAuthHeader(), // ✅ ต้องแน่ใจว่า getAuthHeader ส่ง token.
        "Content-Type": "application/json",
      },
    });
    console.log("💡 Header ที่ส่งไป:", authStore.getAuthHeader());
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
  return allBooks.value.filter((book) =>
    book.publisher
      ?.toLowerCase()
      .includes(selectedPublisher.value.toLowerCase())
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

// เพิ่ม ref สำหรับฟอร์ม
const request = ref({
  bookTitle: "",
  author: "",
  publisher: "",
  isbn: "",
  year: "",
  price: "",
  description: "",
  reason: "",
});

// เพิ่ม error และ loading states
const isbnError = ref("");
const yearError = ref("");
const priceError = ref("");
const bookTitleError = ref("");
const authorError = ref("");
const publisherError = ref("");
const reasonError = ref("");
const globalError = ref("");
const isLoading = ref(false);

// watch validations
watch(
  () => request.value.isbn,
  (val) => {
    const cleaned = val.replace(/[^0-9Xx]/g, "");
    isbnError.value =
      /^\d{9}[\dXx]$/.test(cleaned) || /^\d{13}$/.test(cleaned)
        ? ""
        : "❌ Invalid ISBN (must be 10 or 13 digits)";
  }
);
watch(
  () => request.value.year,
  (val) => {
    yearError.value = /^\d*$/.test(val) ? "" : "❌ Year must be an integer.";
  }
);
watch(
  () => request.value.price,
  (val) => {
    priceError.value = /^\d*\.?\d*$/.test(val)
      ? ""
      : "❌ Price must be a valid number (float).";
  }
);
watch(
  () => request.value.bookTitle,
  (val) => {
    bookTitleError.value =
      val.trim() === "" ? "❌ Book Title is required." : "";
  }
);
watch(
  () => request.value.author,
  (val) => {
    authorError.value = val.trim() === "" ? "❌ Author is required." : "";
  }
);
watch(
  () => request.value.publisher,
  (val) => {
    publisherError.value = val.trim() === "" ? "❌ Publisher is required." : "";
  }
);
watch(
  () => request.value.reason,
  (val) => {
    reasonError.value = val.trim() === "" ? "❌ Reason is required." : "";
  }
);

// submitRequest method
const submitRequest = async () => {
  globalError.value = "";

  const trimmed = {
    bookTitle: request.value.bookTitle.trim(),
    author: request.value.author.trim(),
    publisher: request.value.publisher.trim(),
    isbn: request.value.isbn.trim(),
    year: request.value.year.trim(),
    price: request.value.price.trim(),
    description: request.value.description.trim(),
    reason: request.value.reason.trim(),
  };

  const rawISBN = trimmed.isbn.replace(/[^0-9Xx]/g, "");
  const isbn10 = /^\d{9}[\dXx]$/;
  const isbn13 = /^\d{13}$/;

  if (!isbn10.test(rawISBN) && !isbn13.test(rawISBN)) {
  globalError.value =
    "❌ Invalid ISBN: must be ISBN-10 or ISBN-13, with or without dashes";
  return;
}

  const missingFields = [];
  if (!trimmed.bookTitle) missingFields.push("Book Title");
  if (!trimmed.author) missingFields.push("Author");
  if (!trimmed.publisher) missingFields.push("Publisher");
  if (!trimmed.isbn) missingFields.push("ISBN");
  if (!trimmed.year) missingFields.push("Year");
  if (!trimmed.price) missingFields.push("Price");
  if (!trimmed.reason) missingFields.push("Reason");

  if (missingFields.length > 0) {
    globalError.value =
      "❌ Please fill in the following fields:\n- " +
      missingFields.join("\n- ");
    return;
  }

  const isbnRegex =
    /^(\d{9}[\dXx]|\d{13}|\d{1,5}-\d{1,7}-\d{1,7}-[\dXx]|\d{3}-\d{1,5}-\d{1,7}-\d{1,7}-[\dXx])$/;
  if (!isbnRegex.test(trimmed.isbn)) {
    globalError.value =
      "❌ Invalid ISBN format (ISBN-10 or ISBN-13, with or without dashes)";
    return;
  }

  if (!/^\d+$/.test(trimmed.year)) {
    globalError.value = "❌ Year must be an integer.";
    return;
  }

  if (!/^\d+(\.\d+)?$/.test(trimmed.price)) {
    globalError.value = "❌ Price must be a valid number (float).";
    return;
  }

  isLoading.value = true;

  try {
    const payload = {
      ...trimmed,
      isbn: rawISBN,
      year: parseInt(trimmed.year),
      price: parseFloat(trimmed.price),
    };

    await axios.post(`${baseURL}/api/requests`, payload, {
      withCredentials: true,
    });

    alert("✅ Request submitted successfully!");
    globalError.value = "";
    request.value = {
      bookTitle: "",
      author: "",
      publisher: "",
      isbn: "",
      year: "",
      price: "",
      description: "",
      reason: "",
    };
  } catch (err) {
    if (err.response?.status === 400 && err.response?.data?.errors) {
      const messages = err.response.data.errors
        .map((e) => `❌ ${e.field}: ${e.defaultMessage}`)
        .join("\n");
      globalError.value = messages;
    } else {
      globalError.value =
        "❌ Failed to submit request: " +
        (err.response?.data?.message ||
          JSON.stringify(err.response?.data) ||
          err.message);
    }
  } finally {
    isLoading.value = false;
  }
};

const onISBNInput = (event) => {
  const raw = event.target.value.replace(/[^0-9Xx]/g, "");
  let formatted = "";

  //* ISBN-13 Format: 978-3-16-148410-0
  if (raw.length >= 13) {
    formatted = `${raw.slice(0, 3)}-${raw.slice(3, 4)}-${raw.slice(
      4,
      7
    )}-${raw.slice(7, 12)}-${raw.slice(12, 13)}`;
  }
  // ISBN-10 Format: 0-306-40615-2
  else if (raw.length === 10) {
    formatted = `${raw.slice(0, 1)}-${raw.slice(1, 4)}-${raw.slice(
      4,
      9
    )}-${raw.slice(9, 10)}`;
  } else {
    formatted = raw;
  }

  request.value.isbn = formatted;
};
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

/* Media query for smaller devices */
@media (max-width: 640px) {
  .container {
    padding-left: 1rem;
    padding-right: 1rem;
  }
}

/* Ensure images don't overflow on small screens */
img {
  max-width: 100%;
}

.form-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  margin-top: 0.25rem;
  background-color: white;
}
</style>
