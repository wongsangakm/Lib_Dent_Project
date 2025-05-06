<template>
  <div class="min-h-screen bg-gray-50 py-12 px-4">
    <div class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow-md">
      <h1 class="text-2xl font-bold text-purple-700 mb-6 text-center">
        Add New Book
      </h1>

      <form @submit.prevent="saveBook" class="space-y-4">
        <label class="block">
          <span class="text-gray-700">Title</span>
          <input v-model="book.title" type="text" required class="form-input" />
        </label>

        <label class="block">
          <span class="text-gray-700">Publisher</span>
          <input v-model="book.publisher" type="text" class="form-input" />
        </label>

        <label class="block">
          <span class="text-gray-700">Price</span>
          <input v-model="book.price" type="text" class="form-input" />
        </label>

        <label class="block">
          <span class="text-gray-700">Cover Image URL</span>
          <input v-model="book.coverImage" type="text" class="form-input" />
        </label>

        <label class="block">
          <span class="text-gray-700">Description</span>
          <textarea
            v-model="book.description"
            class="w-full border rounded px-3 py-2 mt-1 resize-none"
            rows="4"
          ></textarea>
        </label>

        <div class="text-right">
          <button
            type="submit"
            class="bg-purple-600 text-white px-6 py-2 rounded hover:bg-purple-700 transition"
          >
            Save Book
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useFavouritesStore } from "@/stores/favourites";

const router = useRouter();
const favouritesStore = useFavouritesStore();

const book = ref({
  id: Date.now(),
  title: "",
  publisher: "",
  price: "",
  coverImage: "",
  description: "",
  isFavorited: false,
  isLoading: false,
});

const saveBook = () => {
  favouritesStore.allBooks.push({ ...book.value });
  alert("Book added (mock only)");
  router.push("/adminallbooks"); // หรือ redirect ไปหน้า all-books
};
</script>

<style scoped>
.form-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  margin-top: 0.25rem;
}
</style>
