<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 px-3 sm:px-6 py-6 sm:py-10 overflow-x-hidden"
  >
    <h1
      class="text-xl sm:text-2xl font-bold text-center uppercase mb-4 sm:mb-8"
    >
      All Book Requested
    </h1>

    <div class="bg-white shadow-md rounded-xl overflow-hidden">
      <!-- Header Controls -->
      <div class="flex flex-col sm:flex-row justify-between gap-2 p-4 border-b">
        <div class="w-full sm:w-auto">
          <select
            v-model="filters.status"
            class="border rounded px-3 py-1 text-sm w-full"
          >
            <option value="">All Status</option>
            <option value="in_shelf">มีในชั้นหนังสือแล้ว</option>
            <option value="ordered">กำลังสั่งซื้อ</option>
            <option value="popular_request">กำลังพิจารณาจัดซื้อ</option>
          </select>
        </div>
        <input
          v-model="filters.search"
          type="text"
          placeholder="Search..."
          class="border rounded px-3 py-1 text-sm w-full sm:w-auto"
        />
      </div>

      <!-- Desktop view -->
      <div v-if="!isMobile" class="w-full overflow-auto max-w-full">
        <table class="table-auto text-sm text-left w-full">
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
                    'bg-green-100 text-green-700': book.status === 'in_shelf',
                    'bg-blue-100 text-blue-800': book.status === 'ordered',
                    'bg-purple-100 text-purple-800':
                      book.status === 'popular_request',
                      

                  }"
                >
                  <option value="in_shelf">มีในชั้นหนังสือแล้ว</option>
                  <option value="ordered">กำลังสั่งซื้อ</option>
                  <option value="popular_request">กำลังพิจารณาจัดซื้อ</option>
                </select>
              </td>
              <td class="px-4 py-2">{{ book.Favorites }}</td>
              <td class="px-4 py-2 truncate max-w-xs">{{ book.name }}</td>
              <td
                class="px-4 py-2 font-semibold text-purple-700 truncate max-w-xs"
              >
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
      </div>

      <!-- Mobile view (Cards) -->
      <div v-else class="space-y-4 w-full py-2 px-3">
        <div
          v-for="book in paginatedData"
          :key="book.id"
          class="bg-white rounded-xl shadow p-4 border"
        >
          <div class="text-xs text-gray-400 mb-2">
            Book No. {{ book.bookNo }}
          </div>
          <div class="font-semibold text-purple-800 mb-1 truncate">
            {{ book.name }}
          </div>
          <div class="text-sm text-gray-600 mb-1">ISBN: {{ book.isbn }}</div>
          <div class="text-sm text-gray-600 mb-1 truncate">
            Publisher: {{ book.publisher }}
          </div>
          <div class="text-base text-blue-600 mb-1">
            Favorites: {{ book.Favorites }}
          </div>
          <div class="flex justify-between items-center mt-2">
            <select
              v-model="book.status"
              @change="handleStatusChange(book)"
              class="text-xs font-semibold px-2 py-1 rounded-full"
              :class="{
                'bg-green-100 text-green-700': book.status === 'in_shelf',
                'bg-blue-100 text-blue-800': book.status === 'ordered',
                'bg-purple-100 text-purple-800':
                  book.status === 'popular_request',
              }"
            >
              <option value="in_shelf">มีในชั้นหนังสือแล้ว</option>
              <option value="ordered">กำลังสั่งซื้อ</option>
              <option value="popular_request">กำลังพิจารณาจัดซื้อ</option>
            </select>

            <router-link
              :to="`request/${book.id}`"
              class="text-purple-600 hover:text-purple-800"
            >
              <i class="fas fa-pen-to-square"></i>
            </router-link>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div class="flex justify-center sm:justify-end flex-wrap gap-1 p-4">
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
import { ref, computed, onMounted, onUnmounted } from "vue";

const allRequests = ref([]);
const isMobile = ref(window.innerWidth <= 768);
const filters = ref({
  search: "",
  status: "",
});

const checkIsMobile = () => {
  isMobile.value = window.innerWidth <= 768;
};

const handleStatusChange = async (book) => {
  try {
    const res = await fetch(
      `http://localhost:8080/api/books/${book.id}/status`,
      {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status: book.status }),
      }
    );

    if (!res.ok) throw new Error("Failed to update status");
    console.log("✅ Status updated:", book.name, "->", book.status);
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
              status: book.status || "popular_request",
            }
          : null;
      })
      .filter(Boolean); // ตัด null ทิ้ง

    allRequests.value = booksWithFav;
  } catch (error) {
    console.error("❌ Failed to load admin fav data:", error);
  }

  // Set up event listener for resize
  window.addEventListener("resize", checkIsMobile);
});

// Clean up event listener when component unmounts
onUnmounted(() => {
  window.removeEventListener("resize", checkIsMobile);
});

const currentPage = ref(1);
const pageSize = computed(() => (isMobile.value ? 5 : 10)); // Smaller page size on mobile

const filtered = computed(() => {
  return allRequests.value.filter((b) => {
    const matchStatus =
      !filters.value.status || b.status === filters.value.status;
    const matchSearch =
      !filters.value.search ||
      b.name.toLowerCase().includes(filters.value.search.toLowerCase()) ||
      (b.publisher &&
        b.publisher
          .toLowerCase()
          .includes(filters.value.search.toLowerCase())) ||
      (b.isbn && b.isbn.includes(filters.value.search)) ||
      b.bookNo.toString().includes(filters.value.search);
    return matchStatus && matchSearch;
  });
});

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filtered.value.length / pageSize.value))
);

// Reset to page 1 when filters change
const watchFilters = computed(() => JSON.stringify(filters.value));
const resetPage = () => {
  currentPage.value = 1;
};

// Watch for filter changes to reset pagination
watchFilters; // Just accessing it to trigger reactivity
if (watchFilters.value) resetPage();

const paginatedData = computed(() => {
  // Make sure current page is valid
  if (currentPage.value > totalPages.value) {
    currentPage.value = totalPages.value;
  }

  return filtered.value.slice(
    (currentPage.value - 1) * pageSize.value,
    currentPage.value * pageSize.value
  );
});

const getStatusText = (status) => {
  switch (status) {
    case "in_shelf":
      return "มีในชั้นหนังสือแล้ว";
    case "ordered":
      return "กำลังสั่งซื้อ";
    case "popular_request":
      return "กำลังพิจารณาจัดซื้อ";
    default:
      return "ไม่ทราบสถานะ";
  }
};
</script>

<style scoped>
i {
  font-size: 1rem;
}

/* Ensure table doesn't cause horizontal overflow */
@media (max-width: 768px) {
  .overflow-auto {
    max-width: 100%;
    -webkit-overflow-scrolling: touch;
  }
}
</style>
