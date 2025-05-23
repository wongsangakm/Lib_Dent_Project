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
              v-model="editableBook.bookTitle"
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
            <span class="text-gray-700">Author</span>
            <input
              v-model="editableBook.author"
              class="w-full border rounded px-3 py-2 mt-1"
              type="text"
            />
          </label>



          <div class="flex felx-row gap-8">
            <label class="block">
              <span class="text-gray-700">ISBN</span>
              <input v-model="editableBook.isbn" type="text" class="w-full border rounded px-3 py-2 mt-1" />
            </label>
            <label class="block">
              <span class="text-gray-700">Year</span>
              <input v-model="editableBook.year" type="text" class="w-full border rounded px-3 py-2 mt-1" />
            </label>
          </div>

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
const baseURL = import.meta.env.VITE_API_BASE_URL;

const route = useRoute();
const book = ref(null);
const editableBook = ref({
  bookTitle: "",
  price: "",
  publisher: "",
  coverImage: "",
  description: "",
});

// ✅ Load book by ID from backend
const fetchBookById = async () => {
  const bookId = route.params.id;
  try {
    const response = await fetch(`${baseURL}/api/books/${bookId}`);
    const data = await response.json();
    book.value = data;
    editableBook.value = { ...data };
  } catch (error) {
    console.error("❌ Failed to load book:", error);
  }
};

onMounted(fetchBookById);

// ✅ Save changes to backend
const saveChanges = async () => {
  const bookId = route.params.id;
  try {
    const response = await fetch(`${baseURL}/api/books/${bookId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(editableBook.value),
    });

    if (!response.ok) throw new Error("Failed to update");
    const updated = await response.json();
    book.value = updated;

    alert("✅ Book updated successfully!");
  } catch (error) {
    console.error("❌ Failed to update book:", error);
    alert("❌ Failed to update book.");
  }
};
</script>
