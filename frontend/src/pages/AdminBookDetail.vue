<template>
  <div class="min-h-screen bg-gray-100 py-12 px-4">
    <div class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow-md">
      <h1 class="text-2xl font-bold text-purple-700 mb-4">Edit Book</h1>

      <div v-if="book">
        <div class="flex justify-center mb-4">
          <img
            :src="book.coverImage"
            alt="Book Cover"
            class="w-40 h-60 object-cover rounded"
          />
        </div>

        <label class="block">
          <span class="text-gray-700">Cover Image URL</span>
          <input
            v-model="editableBook.coverImage"
            class="w-full border rounded px-3 py-2 mt-1"
            type="text"
          />
        </label>

        <div class="space-y-4">
          <label class="block">
            <span class="text-gray-700">Title</span>
            <input
              v-model="editableBook.title"
              class="w-full border rounded px-3 py-2 mt-1"
              type="text"
            />
          </label>

          <label class="block">
            <span class="text-gray-700">Publisher</span>
            <input
              v-model="editableBook.publisher"
              class="w-full border rounded px-3 py-2 mt-1"
              type="text"
            />
          </label>

          <label class="block">
            <span class="text-gray-700">Price</span>
            <input
              v-model="editableBook.price"
              class="w-full border rounded px-3 py-2 mt-1"
              type="text"
            />
          </label>

          <label class="block">
            <span class="text-gray-700">Description</span>
            <textarea
              v-model="editableBook.description"
              class="w-full border rounded px-3 py-2 mt-1 resize-none"
              rows="4"
            ></textarea>
          </label>

          <button
            @click="saveChanges"
            class="w-full mt-4 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 transition"
          >
            Save Changes
          </button>
        </div>
      </div>

      <div v-else class="text-center text-gray-500">Loading book...</div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { useFavouritesStore } from "@/stores/favourites";

const route = useRoute();
const favouritesStore = useFavouritesStore();

const book = ref(null);
const editableBook = ref({
  title: "",
  price: "",
  publisher: "",
  coverImage: "",
  description: "",
});

onMounted(() => {
  const bookId = parseInt(route.params.id);
  const found = favouritesStore.allBooks.find((b) => b.id === bookId);
  if (found) {
    book.value = found;
    editableBook.value = { ...found }; // shallow copy
  }
});

const saveChanges = () => {
  // Mock update (replace with API call in real case)
  Object.assign(book.value, editableBook.value);
  alert("Book updated (mock only)");
};
</script>
