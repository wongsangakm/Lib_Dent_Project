<template>
  <div class="min-h-screen bg-gray-50 py-12 px-4">
    <div class="mb-12 max-w-xl mx-auto mt-0 bg-white p-6 rounded-xl shadow-md">
      <h2 class="text-xl font-bold text-purple-700 mb-4 text-center">
        Upload Book Excel
      </h2>

      <form @submit.prevent="uploadExcel" class="space-y-4">
        <label class="block">
          <span class="text-gray-700">Select .xlsx File</span>
          <input
            type="file"
            accept=".xlsx"
            @change="handleFile"
            class="mt-1 block w-full text-sm text-gray-600"
          />
        </label>
        <div v-if="isUploading" class="flex items-center gap-2 text-blue-600 justify-center">
  <svg class="animate-spin h-5 w-5 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 100 16v-4l-3 3 3 3v-4a8 8 0 01-8-8z" />
  </svg>
  Uploading...
</div>


        <div class="text-right">
          <button
  type="submit"
  class="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 transition disabled:opacity-50 disabled:cursor-not-allowed"
  :disabled="isUploading"
>
  {{ isUploading ? "Uploading..." : "Upload File" }}
</button>
        </div>
      </form>
    </div>
    <div class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow-md">
      <h1 class="text-2xl font-bold text-purple-700 mb-6 text-center">
        Add New Book
      </h1>

      <form @submit.prevent="saveBook" class="space-y-4">
        <label class="block">
          <span class="text-gray-700">Cover Image URL</span>
          <input v-model="book.coverImage" type="text" class="form-input" />
        </label>
        <label class="block">
          <span class="text-gray-700">Title</span>
          <input
            v-model="book.bookTitle"
            type="text"
            required
            class="form-input"
          />
        </label>

        <label class="block">
          <span class="text-gray-700">Author</span>
          <input v-model="book.author" type="text" class="form-input" />
        </label>

        <label class="block">
          <span class="text-gray-700">Publisher</span>
          <input v-model="book.publisher" type="text" class="form-input" />
        </label>

        <div class="flex felx-row gap-8">
          <label class="block">
            <span class="text-gray-700">ISBN</span>
            <input v-model="book.isbn" type="text" class="form-input" />
          </label>
          <label class="block">
            <span class="text-gray-700">Year</span>
            <input v-model="book.year" type="text" class="form-input" />
          </label>
        </div>
        <label class="block">
          <span class="text-gray-700">Price</span>
          <input v-model="book.price" type="text" class="form-input" />
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
import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
import { ElMessage } from "element-plus";
const isUploading = ref(false);
const authStore = useAuthStore();

const router = useRouter();
const favouritesStore = useFavouritesStore();
const baseURL = import.meta.env.VITE_API_BASE_URL;

const book = ref({
  bookTitle: "",
  publisher: "",
  price: "",
  coverImage: "",
  description: "",
  author: "",
  isbn: "",
  year: "",
});

const saveBook = async () => {
  try {
    const response = await axios.post(`${baseURL}/api/books`, book.value, {
      headers: authStore.getAuthHeader(),
    });
    ElMessage.success("📘 Upload successful!");

    // ดึงข้อมูลใหม่หลังเพิ่ม (optional)
    await favouritesStore.fetchAllBooks();

    router.push("/admin/allbooks");
  } catch (error) {
    ElMessage.error("❌ Upload failed: " + (error.response?.data || error.message));
  }
};

const selectedFile = ref(null);

function handleFile(event) {
  selectedFile.value = event.target.files[0];
}

async function uploadExcel() {
  if (!selectedFile.value) {
    ElMessage.warning("Please select an Excel (.xlsx) file.");
    return;
  }

  const formData = new FormData();
  formData.append("file", selectedFile.value);
  isUploading.value = true;

  try {
    const response = await axios.post(`${baseURL}/api/books/upload`, formData, {
      headers: {
        ...authStore.getAuthHeader(),
        "Content-Type": "multipart/form-data",
      },
    });
    ElMessage.success("📘 Upload successful!");
  } catch (error) {
    ElMessage.error("❌ Upload failed: " + (error.response?.data || error.message));
  } finally {
    isUploading.value = false;
  }
}

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
