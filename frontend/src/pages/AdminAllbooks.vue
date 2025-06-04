<template>
  <div class="container mx-auto px-4 py-12">
    <h1 class="text-2xl font-bold text-center mb-8">All Books</h1>

    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">All Books</h1>
      <router-link
        to="/admin/allbooks/add-book"
        class="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 transition"
      >
        + Add Book
      </router-link>
    </div>

    <div class="flex justify-between items-center mb-4 gap-4 flex-wrap">
      <input
        v-model="searchText"
        type="text"
        placeholder="Search by title or publisher"
        class="border px-4 py-2 rounded w-full max-w-md"
      />
      <button
        @click="showDeleteDialog = true"
        class="px-4 py-2 bg-red-100 text-red-700 border border-red-400 rounded hover:bg-red-200 transition-colors"
      >
        🗑️ Delete All Data
      </button>
    </div>

    <p class="text-sm text-gray-600 mb-4">
      Showing {{ filteredBooks.length }} book<span
        v-if="filteredBooks.length !== 1"
        >s</span
      >
    </p>

    <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-6">
      <div
        v-for="book in filteredBooks"
        :key="book.id"
        class="bg-white rounded-xl shadow-md p-4 flex flex-col items-center"
      >
        <img
          :src="book.coverImage"
          :alt="book.title"
          class="w-[120px] h-[180px] object-cover rounded"
        />
        <p class="mt-3 text-center font-semibold text-gray-700 truncate w-full">
          {{ book.bookTitle }}
        </p>
        <p class="text-xs text-gray-500">{{ book.publisher }}</p>
        <p class="text-sm font-bold text-purple-700 mt-1">
          {{ book.price.toLocaleString() }} THB
        </p>

        <router-link
          :to="`/admin/adminbookdetail/${book.id}`"
          class="mt-3 px-4 py-1 bg-purple-500 text-white rounded-full text-sm hover:bg-purple-600 transition"
        >
          edit
        </router-link>
      </div>
    </div>

    <!-- Confirm Delete Dialog -->
    <div
      v-if="showDeleteDialog"
      class="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50 backdrop-blur-sm p-4"
    >
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div class="bg-gradient-to-r from-red-500 to-pink-600 p-6 text-white rounded-t-2xl">
          <h2 class="text-xl font-bold">Delete All Books</h2>
          <p class="text-red-100 text-sm mt-1">
            This action will permanently delete all book records. This cannot be undone.
          </p>
        </div>
        <div class="p-6 space-y-4">
          <p class="text-slate-600 text-sm">
            Are you sure you want to proceed with deleting <strong>all books</strong> from the system?
          </p>

          <div class="flex gap-3 pt-4">
            <button
              @click="showDeleteDialog = false"
              class="flex-1 px-4 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium rounded-xl transition-colors"
            >
              Cancel
            </button>
            <button
              @click="confirmDeleteAll"
              class="flex-1 px-4 py-3 bg-gradient-to-r from-red-500 to-pink-600 hover:from-red-600 hover:to-pink-700 text-white font-medium rounded-xl transition-all duration-200 flex items-center justify-center gap-2"
            >
              Yes, Delete All
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useFavouritesStore } from "@/stores/favourites";
import { ElMessage } from "element-plus";

const favouritesStore = useFavouritesStore();
const searchText = ref("");
const showDeleteDialog = ref(false);

// ✅ ฟิลเตอร์หนังสือ
const allBooks = computed(() => favouritesStore.allBooks);
const filteredBooks = computed(() => {
  const keyword = searchText.value.toLowerCase();
  return allBooks.value.filter(
    (book) =>
      book.bookTitle?.toLowerCase().includes(keyword) ||
      book.publisher?.toLowerCase().includes(keyword)
  );
});

// ✅ โหลดตอนเริ่ม
onMounted(() => {
  favouritesStore.fetchAllBooks();
});

// ✅ ยืนยันลบ
const confirmDeleteAll = async () => {
  try {
    await favouritesStore.deleteAllBooks();
    await favouritesStore.fetchAllBooks();
    ElMessage({
      type: "success",
      message: "All books deleted successfully.",
    });
    showDeleteDialog.value = false;
  } catch (error) {
    ElMessage({
      type: "error",
      message: "Failed to delete books.",
    });
  }
};
</script>
