<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 px-6 py-10"
  >
    <h1 class="text-2xl font-bold text-center uppercase mb-8">
      All Book Requested
    </h1>

    <div class="bg-white shadow-md rounded-xl overflow-hidden">
      <!-- Header Controls -->
      <div class="flex flex-wrap justify-between gap-2 p-4 border-b">
        <div>
          <select
            v-model="filters.status"
            class="border rounded px-3 py-1 text-sm"
          >
            <option value="">All Status</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
            <option value="Ordering">Ordering</option>
          </select>
        </div>
        <input
          v-model="filters.search"
          type="text"
          placeholder="Search..."
          class="border rounded px-3 py-1 text-sm"
        />
      </div>

      <!-- Table -->
      <table class="w-full table-auto text-sm text-left">
        <thead class="bg-gray-100 text-gray-600">
          <tr>
            <th class="px-4 py-2">Book No.</th>
            <th class="px-4 py-2">ISBN</th>
            <th class="px-4 py-2">Status</th>
            <th class="px-4 py-2">Favorites</th>
            <th class="px-4 py-2">Name</th>
            <th class="px-4 py-2">Publisher</th>
            <th class="px-4 py-2 text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="book in paginatedData"
            :key="book.id"
            class="border-b hover:bg-gray-50"
          >
            <td class="px-4 py-2">{{ book.bookNo }}</td>
            <td class="px-4 py-2">{{ book.isbn }}</td>
            <td class="px-4 py-2">
              <select
                v-model="book.status"
                @change="handleStatusChange(book)"
                class="text-xs font-semibold px-2 py-0.5 rounded-full"
                :class="{
                  'bg-green-100 text-green-700': book.status === 'Yes',
                  'bg-yellow-100 text-yellow-800': book.status === 'Ordering',
                  'bg-gray-200 text-gray-600': book.status === 'No',
                }"
              >
                <option value="Yes">Yes</option>
                <option value="Ordering">Ordering</option>
                <option value="No">No</option>
              </select>
            </td>

            <td class="px-4 py-2">{{ book.Favorites }}</td>
            <td class="px-4 py-2 truncate">{{ book.name }}</td>
            <td class="px-4 py-2 font-semibold text-purple-700">
              {{ book.publisher }}
            </td>
            <td class="px-4 py-2 text-center">
              <router-link
                :to="`request/${book.id}`"
                class="text-purple-600 hover:text-purple-800"
              >
                <i class="fas fa-pen-to-square"></i>
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination -->
      <div class="flex justify-end gap-1 p-4">
        <button
          v-for="page in totalPages"
          :key="page"
          @click="currentPage = page"
          class="px-3 py-1 rounded text-sm"
          :class="{
            'bg-purple-600 text-white': currentPage === page,
            'bg-gray-100 text-gray-700': currentPage !== page,
          }"
        >
          {{ page }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";

const allRequests = ref([]);

const filters = ref({
  search: "",
  status: "",
});

const handleStatusChange = async (book) => {
  try {
    const res = await fetch(`http://localhost:8080/api/books/${book.id}/status`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status: book.status }),
    });

    if (!res.ok) throw new Error("Failed to update status");
    console.log("✅ Status updated:", book.bookTitle, "->", book.status);
  } catch (err) {
    console.error("❌ Error updating status:", err);
    alert("Error saving status. Please try again.");
  }
};


onMounted(async () => {
  try {
    const resBooks = await fetch("http://localhost:8080/api/books");
    const books = await resBooks.json();

    const resCounts = await fetch(
      "http://localhost:8080/api/admin/favorite-counts",
      { credentials: "include" }
    );
    if (!resCounts.ok) throw new Error("Favorite counts not found");
    const counts = await resCounts.json();

    const booksWithFav = books
      .map((book) => {
        const match = counts.find((c) => c.bookId === book.id);
        return match
          ? {
              id: book.id,
              bookNo: book.id,
              isbn: book.isbn,
              name: book.bookTitle,
              publisher: book.publisher,
              Favorites: match.favoriteCount,
              status: book.status || "No",
            }
          : null;
      })
      .filter(Boolean); // ตัด null ทิ้ง

    allRequests.value = booksWithFav;
  } catch (error) {
    console.error("❌ Failed to load admin fav data:", error);
  }
});

const currentPage = ref(1);
const pageSize = 10;

const filtered = computed(() => {
  return allRequests.value.filter((b) => {
    const matchStatus =
      !filters.value.status || b.status === filters.value.status;
    const matchSearch =
      !filters.value.search ||
      b.name.toLowerCase().includes(filters.value.search.toLowerCase()) ||
      b.bookNo.toString().includes(filters.value.search);
    return matchStatus && matchSearch;
  });
});

const totalPages = computed(() => Math.ceil(filtered.value.length / pageSize));

const paginatedData = computed(() =>
  filtered.value.slice(
    (currentPage.value - 1) * pageSize,
    currentPage.value * pageSize
  )
);
</script>

<style scoped>
i {
  font-size: 1rem;
}
</style>
