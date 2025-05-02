<template>
    <div class="min-h-screen flex flex-col bg-gray-100">
  
      <!-- Main Content -->
      <main class="flex-grow container mx-auto px-4 py-8">
        <h1 class="text-2xl font-bold text-gray-800 mb-6">your favourite books</h1>
        <div v-if="favourites.length > 0" class="space-y-4">
          <div v-for="book in favourites" :key="book.id" class="flex items-center bg-white rounded-lg shadow-md p-4">
            <!-- Book Cover -->
            <router-link :to="`/book/${book.id}`">
              <img :src="book.coverImage" alt="Book Cover" class="w-[100px] h-[150px] rounded-lg mr-4" />
            </router-link>
            <!-- Book Info -->
            <div class="flex-grow">
              <router-link :to="`/book/${book.id}`">
                <p class="text-gray-800 font-semibold truncate">{{ book.title }}</p>
              </router-link>
              <p class="text-gray-500 text-sm">SKU</p>
            </div>
            <!-- Delete Button -->
            <button @click="removeFromFavorite(book.id)" class="text-gray-400 hover:text-red-500">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5-4h4m-4 0h4m-6 4h8"></path>
              </svg>
            </button>
          </div>
        </div>
        <div v-else class="text-center text-gray-600">
          <p>No favourite books yet.</p>
        </div>
      </main>

    </div>
  </template>
  <script setup>
  import { useFavouritesStore } from "@/stores/favourites"; // Import Store from separate file
  import logo from "@/assets/dentdesign.svg";
  
  const favouritesStore = useFavouritesStore(); // Access the Store
  const favourites = favouritesStore.favourites; // Get favourites from Store
  
  const removeFromFavorite = async (bookId) => {
    try {
      const response = await fetch(`/api/favorites/${bookId}`, {
        method: "DELETE",
      });
      const data = await response.json();
      if (data.success) {
        // Remove from Store
        favouritesStore.removeFavourite(bookId);
  
        // Update the book's isFavorited status in the original data (optional, depends on your API)
        const allBooksForUpdate = [
          {
            id: 1,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
            price: "150.00 $",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 2,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Wild Woman’s Way",
            price: "150.00 $",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 3,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "In The Garden",
            price: "150.00 $",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 4,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
            price: "150.00 $",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 5,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
            price: "150.00 $",
            isFavorited: false,
            isLoading: false,
          },
          // Women's Murder Series
          {
            id: 101,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Women's Murder 1",
            price: "$10.00",
            publisher: "Wiley",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 102,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Women's Murder 2",
            price: "$10.00",
            publisher: "Wiley",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 103,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Women's Murder 3",
            price: "$10.00",
            publisher: "Wiley",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 104,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Women's Murder 4",
            price: "$10.00",
            publisher: "Wiley",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 105,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "Women's Murder 5",
            price: "$10.00",
            publisher: "Wiley",
            isFavorited: false,
            isLoading: false,
          },
          // The Garden Series
          {
            id: 201,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "The Garden 1",
            price: "$12.00",
            publisher: "Elsevier",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 202,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "The Garden 2",
            price: "$12.00",
            publisher: "Elsevier",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 203,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "The Garden 3",
            price: "$12.00",
            publisher: "Elsevier",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 204,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "The Garden 4",
            price: "$12.00",
            publisher: "Elsevier",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 205,
            coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
            title: "The Garden 5",
            price: "$12.00",
            publisher: "Elsevier",
            isFavorited: false,
            isLoading: false,
          },
          // Fugitive Series
          {
            id: 301,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Fugitive 1",
            price: "$15.00",
            publisher: "CRC Press",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 302,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Fugitive 2",
            price: "$15.00",
            publisher: "CRC Press",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 303,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Fugitive 3",
            price: "$15.00",
            publisher: "CRC Press",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 304,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Fugitive 4",
            price: "$15.00",
            publisher: "CRC Press",
            isFavorited: false,
            isLoading: false,
          },
          {
            id: 305,
            coverImage: "https://via.placeholder.com/150x200",
            title: "Fugitive 5",
            price: "$15.00",
            publisher: "CRC Press",
            isFavorited: false,
            isLoading: false,
          },
        ];
        const book = allBooksForUpdate.find(b => b.id === bookId);
        if (book) {
          book.isFavorited = false;
        }
      } catch (error) {
        console.error(`Error removing book ${bookId} from favorites:`, error);
      }
    };
</script>
  
  <style scoped>
  /* Ensure the layout matches the design */
  .group:hover .group-hover\:block {
    display: block;
  }
  </style>