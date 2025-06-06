<template>
  <div>
    <Header />
    <div
      class="min-h-screen flex flex-col bg-gray-100 items-center py-8 sm:py-12"
      :style="{
        backgroundImage: `url(${bgImage})`,
        backgroundPosition: '0% 6%',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
      }"
    >
      <div v-if="isLoadingData" class="text-center text-gray-600">
        <p>Loading book details...</p>
      </div>

      <div v-else-if="bookData" class="mt-12 sm:mt-20 w-full px-4 sm:px-0">
        <div
          class="flex flex-col sm:flex-row max-w-5xl mx-auto bg-rounded-lg shadow-lg p-4 sm:p-6 bg-white/40"
        >
          <div
            class="w-full sm:w-1/3 sm:min-w-[260px] flex justify-center relative mb-4 sm:mb-0"
          >
            <div
              v-if="bookData.status === 'in_shelf' || bookData.status === 'ordered'"
              class="ribbon"
              :class="{ ordered: bookData.status === 'ordered' }"
            >
              <span v-if="bookData.status === 'in_shelf'">
                <i class="fa-solid fa-book-open-reader mr-2"></i>มีในชั้นหนังสือ
              </span>
              <span v-else-if="bookData.status === 'ordered'">
                <i class="fa-solid fa-cart-plus mr-2"></i>กำลังสั่งซื้อ
              </span>
            </div>
            <button
              @click="addToFavorite"
              :disabled="isLoading || isFavorited"
              class="absolute top-3 right-2 sm:right-12 bg-white bg-opacity-80 text-lg p-2 rounded-full transition-all duration-200 shadow-lg"
              :class="{
                'text-red-500 bg-red-200': isFavorited,
                'text-gray-400 hover:text-red-300': !isFavorited && !isLoading,
                'opacity-50 cursor-not-allowed': isLoading || isFavorited,
              }"
            >
              <i :class="[isFavorited ? 'fas' : 'far', 'fa-heart']"></i>
            </button>

            <img
              :src="bookData.coverImage"
              alt="Book Cover"
              class="book-cover-image rounded"
              :style="{
                width: isMobile ? '200px' : '260px',
                height: isMobile ? 'auto' : '380px',
                objectFit: 'cover',
              }"
            />
          </div>

          <div class="w-full sm:w-2/3 sm:ml-6">
            <h1 class="text-2xl sm:text-3xl font-bold text-gray-800">
              {{ bookData.bookTitle }}
            </h1>

            <div
              class="border-b border-gray-300 pb-4 mt-4 sm:mt-6 text-sm text-gray-600 grid gap-2 sm:gap-4"
              :style="{
                gridTemplateColumns: isMobile
                  ? '1fr'
                  : '1fr 0.5fr 0.5fr 0.5fr 0.5fr 0.7fr',
              }"
            >
              <div><p class="text-gray-400 text-xs sm:text-sm">Author</p><strong class="text-black text-sm sm:text-base text-justify">{{ bookData.author }}</strong></div>
              <div><p class="text-gray-400 text-xs sm:text-sm">Publisher</p><strong class="text-black text-sm sm:text-base">{{ bookData.publisher }}</strong></div>
              <div><p class="text-gray-400 text-xs sm:text-sm">ISBN</p><strong class="text-black text-xs sm:text-sm">{{ bookData.isbn }}</strong></div>
              <div><p class="text-gray-400 text-xs sm:text-sm">Edition</p><strong class="text-black text-sm sm:text-base">{{ bookData.edition }}</strong></div>
              <div><p class="text-gray-400 text-xs sm:text-sm">Year</p><strong class="text-black text-sm sm:text-base">{{ bookData.year }}</strong></div>
              <div><p class="text-gray-400 text-xs sm:text-sm">Price</p><strong class="text-black text-sm sm:text-base">{{ bookData.price.toLocaleString() }} THB</strong></div>
            </div>

            <p class="mt-4 text-gray-600 text-sm sm:text-base leading-relaxed sm:max-h-40 sm:overflow-y-auto tracking-wide indent-6 text-justify">
              {{ bookData.description }}
            </p>

            <div class="flex justify-end mt-4 sm:mt-6">
              <button
                @click="addToFavorite"
                :disabled="isLoading || isFavorited"
                class="px-4 py-2 text-white rounded-full flex items-center transition-all duration-200 text-sm sm:text-base"
                :class="[ isFavorited ? 'bg-gray-400' : 'bg-purple-800 hover:bg-purple-700', (isLoading || isFavorited) && 'opacity-50 cursor-not-allowed' ]"
              >
                <span>{{ isFavorited ? "Added to Favorite" : "Add to Favorite" }}</span>
                <svg class="ml-2 w-4 h-4 sm:w-5 sm:h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="container mx-auto px-4 text-center">
        <div class="flex flex-col items-center justify-center min-h-[50vh]">
          <h2 class="text-xl sm:text-2xl font-bold text-gray-800 mb-2">Book Not Found</h2>
          <p class="text-gray-600 mb-4 text-sm sm:text-base">ขออภัย ไม่พบหนังสือที่คุณต้องการ</p>
          <router-link to="/" class="inline-flex items-center px-4 py-2 sm:px-6 sm:py-2 bg-purple-600 text-white rounded-full hover:bg-purple-700 transition-colors text-sm sm:text-base">Back to Home</router-link>
        </div>
      </div>

      <Footer class="mt-8 w-full" />
    </div>

    <div v-if="showFavoriteDialog" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40 backdrop-blur-sm">
      <div class="bg-white rounded-2xl shadow-xl w-full max-w-md">
        <div class="bg-gradient-to-r from-purple-600 to-indigo-600 text-white px-6 py-4 rounded-t-2xl">
          <h2 class="text-lg font-semibold">ยืนยันการเพิ่มรายการโปรด</h2>
          <p class="text-sm text-indigo-100">คุณต้องการเพิ่มหนังสือเล่มนี้เข้าสู่รายการโปรดใช่หรือไม่?</p>
        </div>

        <div class="p-6 space-y-4">
          <p class="text-slate-700 text-center">
            <strong>{{ bookData?.bookTitle }}</strong>
          </p>

          <div class="flex justify-end gap-3">
            <button @click="showFavoriteDialog = false" class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-800 rounded-xl">ยกเลิก</button>
            <button @click="confirmAddToFavorite" class="px-4 py-2 bg-gradient-to-r from-purple-600 to-indigo-600 hover:from-purple-700 hover:to-indigo-700 text-white rounded-xl">ยืนยัน</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from "vue";
import { useRoute } from "vue-router";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
import { useAuthStore } from "@/stores/useAuthStore";
import { ElMessage } from "element-plus";

const route = useRoute();
const authStore = useAuthStore();
const isLoggedIn = computed(() => authStore.isAuthenticated);
const bookData = ref(null);
const isLoading = ref(false);
const isFavorited = ref(false);
const isLoadingData = ref(true);
const baseURL = import.meta.env.VITE_API_BASE_URL;
const showFavoriteDialog = ref(false);

const isMobile = ref(window.innerWidth < 640);
const updateIsMobile = () => (isMobile.value = window.innerWidth < 640);
onMounted(() => window.addEventListener("resize", updateIsMobile));
onUnmounted(() => window.removeEventListener("resize", updateIsMobile));

onMounted(async () => {
  const bookId = route.params.id;
  if (!bookId) return (isLoadingData.value = false);
  try {
    await fetchBookData(bookId);
    if (isLoggedIn.value) await fetchFavoriteStatus(bookId);
  } catch (error) {
    console.error("Error during init:", error);
  } finally {
    isLoadingData.value = false;
  }
});

const fetchBookData = async (bookId) => {
  try {
    const response = await fetch(`${baseURL}/api/books/${bookId}`, {
      headers: { ...authStore.getAuthHeader() },
    });
    if (!response.ok) throw new Error("Failed to fetch book data");
    const data = await response.json();
    bookData.value = data;
    isFavorited.value = data?.isFavorited === true;
  } catch (error) {
    console.error("❌ Book fetch error:", error);
    bookData.value = null;
  }
};

const fetchFavoriteStatus = async (bookId) => {
  try {
    const response = await fetch(`${baseURL}/api/auth/favorites/${bookId}`, {
      headers: { ...authStore.getAuthHeader() },
    });
    if (!response.ok) throw new Error("Failed to fetch favorite status");
    const data = await response.json();
    isFavorited.value = data?.isFavorited === true;
  } catch (error) {
    console.error("❌ Favorite status error:", error);
    isFavorited.value = false;
  }
};

const addToFavorite = () => {
  if (isFavorited.value || isLoading.value) return;
  if (!isLoggedIn.value) {
  ElMessage.warning("กรุณาเข้าสู่ระบบก่อนกด Favorite");
  return;
}
  showFavoriteDialog.value = true;
};

const confirmAddToFavorite = async () => {
  showFavoriteDialog.value = false;
  isLoading.value = true;
  try {
    const response = await fetch(`${baseURL}/api/auth/favorites/${bookData.value.id}`, {
      method: "POST",
      headers: {
        ...authStore.getAuthHeader(),
        "Content-Type": "application/json",
      },
    });
    const result = await response.json();
    if (result.success || result.message === "Already favorited") {
      isFavorited.value = true;
      ElMessage.success("เพิ่มหนังสือเข้ารายการโปรดเรียบร้อยแล้ว");
    }
  } catch (error) {
    console.error("❌ Error adding to favorites:", error);
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.ribbon {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: #16db93;
  color: white;
  font-size: 12px;
  font-weight: 600;
  padding: 4px 10px;
  border-radius: 4px;
  z-index: 10;
  display: flex;
  align-items: center;
  gap: 4px;
}
.ribbon.ordered {
  background-color: #f29e4c;
}
</style>
