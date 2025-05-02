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
      <!-- Back Button -->
      <div class="container mx-auto px-4 mb-6">
        <router-link
          to="/"
          class="inline-flex items-center px-4 py-2 bg-gray-200 text-gray-800 rounded-full hover:bg-gray-300 transition-colors"
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
              d="M15 19l-7-7 7-7"
            ></path>
          </svg>
          Back to Home
        </router-link>
      </div>

      <!-- Loading State -->
      <div v-if="isLoadingData" class="text-center text-gray-600">
        <p>Loading book details...</p>
      </div>

      <!-- Book Details -->
      <div v-else-if="bookData" class="">
        <div
          class="flex flex-col md:flex-row max-w-4xl bg-white rounded-lg shadow-lg p-6 bg-white/50"
        >
          <!-- Book Cover -->
          <div class="md:w-1/3 flex justify-center relative">
            <!-- Heart Button -->
            <button
              @click="addToFavorite"
              :disabled="isFavorited || isLoading"
              class="absolute top-3 right-12 bg-white bg-opacity-80 text-l p-2 rounded-full transition-all duration-200 shadow-lg"
              :class="{
                'text-red-500': isFavorited,
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
              class="w-48 h-72 object-cover rounded"
            />
          </div>

          <!-- Book Details -->
          <div class="md:w-2/3 mt-4 md:mt-0 md:ml-6">
            <h1 class="text-2xl font-bold text-gray-800">{{ bookData.title }}</h1>

            <!-- Book Info -->
            <div
              class="border-b border-gray-300 mr-4 pb-4 grid grid-cols-2 sm:grid-cols-4 gap-4 mt-6 text-sm text-gray-600"
            >
              <span>
                <p class="text-gray-400">Author</p>
                <strong class="text-black text-justify">{{ bookData.author }}</strong>
              </span>
              <span>
                <p class="text-gray-400">Publisher</p>
                <strong class="text-black">{{ bookData.publisher }}</strong>
              </span>
              <span>
                <p class="text-gray-400">Publisher Date</p>
                <strong class="text-black">{{ bookData.publishDate }}</strong>
              </span>
              <span>
                <p class="text-gray-400">Price</p>
                <strong class="text-black">{{ bookData.price }}</strong>
              </span>
            </div>

            <!-- Description -->
            <p
              class="mt-4 text-gray-600 text-sm leading-relaxed max-h-40 overflow-y-auto tracking-wide indent-6 text-justify"
            >
              {{ bookData.description }}
            </p>

            <!-- Add to Favorite Button -->
            <div class="flex justify-end mt-6">
              <button
                @click="addToFavorite"
                :disabled="isFavorited || isLoading"
                class="px-4 py-2 bg-purple-600 text-white rounded-full flex items-center transition-all duration-200"
                :class="{
                  'bg-purple-600 hover:bg-purple-700': !isFavorited && !isLoading,
                  'bg-gray-400': isFavorited,
                  'opacity-50 cursor-not-allowed': isLoading || isFavorited,
                }"
              >
                <span>{{ isFavorited ? 'Added to Favorite' : 'Add to Favorite' }}</span>
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

// Mock data for testing
const mockBooks = {
  "12345": {
    id: "12345",
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
    author: "Suk",
    publisher: "Springer",
    publishDate: "2023",
    price: "150.00 $",
    description:
      "The superbly illustrated book is a comprehensive introduction to CBCT and its use for diagnosis of dental conditions and treatment planning in the dental practice. After an introductory chapter on the basics of CBCT scans, detailed description is provided on dental anatomic structures and their appearance on CBCT. Various common dental disorders are shown and discussed, as well as signs of malignancy. The book will arm the reader with fundamental knowledge of the radiological appearance of dento-alveolar anatomy and provide guidance on the interpretation of artefacts, disease and some well-known anatomical variations. While it is intended primarily for dentists, this book will be of high value for multiple other health care professionals including medical radiologists. Videos via app: download the SN More Media app for free, scan a link with play button and access videos directly on your smartphone or tablet.",
  },
  "101": {
    id: "101",
    coverImage: "https://via.placeholder.com/150x200",
    title: "Women's Murder 1",
    author: "Jane Doe",
    publisher: "Mystery Press",
    publishDate: "2022",
    price: "$10.00",
    description: "A thrilling mystery novel in the Women's Murder series.",
  },
  "201": {
    id: "201",
    coverImage: "https://via.placeholder.com/150x200",
    title: "The Garden 1",
    author: "John Smith",
    publisher: "Nature Books",
    publishDate: "2021",
    price: "$12.00",
    description: "A beautiful story set in a magical garden.",
  },
  "301": {
    id: "301",
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 1",
    author: "Alex Brown",
    publisher: "Thriller House",
    publishDate: "2020",
    price: "$15.00",
    description: "This superbly illustrated book gives a comprehensive introduction to CBCT and its use for diagnosis of dental conditions and treatment planning in the dental practice. After an introductory chapter on the basics of CBCT scans, detailed description is provided on dental anatomic structures and their appearance on CBCT. Various common dental diseases are shown and discussed, as well as signs of malignancy. The book will arm the reader with fundamental knowledge of the radiological appearance of dento-alveolar anatomy and provide guidance on the interpretation of artefacts, disease and some well-known anatomical variations. While it is intended primarily for dentists, this book will be of high value for multiple other health care professionals including medical radiologists.",
  },
};

const route = useRoute();
const bookData = ref(null);
const isFavorited = ref(false);
const isLoading = ref(false);
const isLoadingData = ref(true);

onMounted(async () => {
  const bookId = route.params.id; // Get bookId from URL
  if (!bookId) {
    isLoadingData.value = false;
    return;
  }
  await fetchBookData(bookId);
  await fetchFavoriteStatus(bookId);
  isLoadingData.value = false;
});

const fetchBookData = async (bookId) => {
  try {
    // Simulate fetching book data from backend
    // const response = await fetch(`/api/books/${bookId}`);
    // const data = await response.json();
    // bookData.value = data;

    // Mock data for testing
    return new Promise((resolve) => {
      setTimeout(() => {
        const book = mockBooks[bookId];
        if (book) {
          bookData.value = book;
        } else {
          bookData.value = null;
        }
        resolve();
      }, 500);
    });
  } catch (error) {
    console.error("Error fetching book data:", error);
    bookData.value = null; // Set to null if error occurs
  }
};

const fetchFavoriteStatus = async (bookId) => {
  try {
    const response = await fetch(`/api/favorites/${bookId}`);
    const data = await response.json();
    isFavorited.value = data.isFavorited;
  } catch (error) {
    console.error("Error fetching favorite status:", error);
  }
};

const addToFavorite = async () => {
  if (isFavorited.value) return; // Prevent further action if already favorited

  isLoading.value = true;
  try {
    const response = await updateFavoriteStatus(bookData.value.id);
    if (response.success) {
      isFavorited.value = true;
    }
  } catch (error) {
    console.error("Error adding to favorites:", error);
  } finally {
    isLoading.value = false;
  }
};

const updateFavoriteStatus = async (bookId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      console.log(`Adding book ${bookId} to favorites`);
      resolve({ success: true });
    }, 500);
  });

  /*
  try {
    const response = await fetch('/api/favorites', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ bookId }),
    });
    return await response.json();
  } catch (error) {
    throw new Error('Failed to add to favorites');
  }
  */
};
</script>

<style scoped>
.container {
  max-width: 1200px;
}
</style>