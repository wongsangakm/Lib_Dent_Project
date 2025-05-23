<template>
  <div class="bg-gray-50">
    <Header />
    <div class="max-w-6xl mx-auto p-6 bg-gray-50 min-h-screen mt-20">
      <!-- Header -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">
          ภาพรวมและสถานะของหนังสือ
        </h1>
        <div
          v-if="authStore.isAuthenticated"
          class="bg-white px-4 py-2 rounded-lg shadow flex items-center"
        >
          <Heart class="h-5 w-5 text-red-500 mr-2" />
          <span class="font-medium">
            หนังสือที่คุณชื่นชอบ: {{ userFavorites.length }} เล่ม
          </span>
        </div>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-5 gap-4 mb-8">
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-red-100 p-3 rounded-full mr-4">
            <Heart class="h-8 w-8 text-red-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">การกดหัวใจทั้งหมด</p>
            <p class="text-2xl font-bold">
              {{
                typeof booksWithFavorites === "number"
                  ? booksWithFavorites.toLocaleString()
                  : "0"
              }}
              เล่ม
            </p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-green-100 p-3 rounded-full mr-4">
            <BookOpen class="h-8 w-8 text-green-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">รายการเสนอซื้อโดยผู้ใช้</p>
            <p class="text-2xl font-bold">
              {{ additionalSummary.count.toLocaleString() }} เล่ม
            </p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-green-100 p-3 rounded-full mr-4">
            <BookOpen class="h-8 w-8 text-green-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">มีในชั้นหนังสือแล้ว</p>
            <p class="text-2xl font-bold">
              {{ overallSummary.inShelf.toLocaleString() }} เล่ม
            </p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-blue-100 p-3 rounded-full mr-4">
            <ShoppingCart class="h-8 w-8 text-blue-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">กำลังสั่งซื้อ</p>
            <p class="text-2xl font-bold">
              {{ overallSummary?.ordered?.toLocaleString() || 0 }} เล่ม
            </p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-purple-100 p-3 rounded-full mr-4">
            <Users class="h-8 w-8 text-purple-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">กำลังพิจารณาจัดซื้อ</p>
            <p class="text-2xl font-bold">
              {{ overallSummary?.requested?.toLocaleString() || 0 }} เล่ม
            </p>
          </div>
        </div>
      </div>

      <!-- Status Ratio Bar -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <h2 class="text-lg font-semibold mb-4 flex items-center">
          <BarChart3 class="mr-2 h-5 w-5 text-gray-700" />
          สัดส่วนสถานะหนังสือในระบบ
        </h2>
        <div class="h-6 bg-pink-100 rounded-full overflow-hidden flex">
          <div
            v-if="overallSummary.inShelf > 0"
            class="h-full bg-green-500"
            :style="{
              width: `${
                (overallSummary.inShelf /
                  Math.max(1, booksWithFavorites + additionalSummary.count)) *
                100
              }%`,
            }"
          ></div>
          <div
            v-if="overallSummary.ordered > 0"
            class="h-full bg-blue-500"
            :style="{
              width: `${
                (overallSummary.ordered /
                  Math.max(1, booksWithFavorites + additionalSummary.count)) *
                100
              }%`,
            }"
          ></div>
          <div
            v-if="overallSummary.requested > 0"
            class="h-full bg-purple-500"
            :style="{
              width: `${
                (overallSummary.requested /
                  Math.max(1, booksWithFavorites + additionalSummary.count)) *
                100
              }%`,
            }"
          ></div>
        </div>
        <div class="flex justify-center mt-4 text-sm flex-wrap gap-4">
          <div class="flex items-center">
            <div class="w-3 h-3 bg-green-500 rounded-full mr-1"></div>
            <span>มีในชั้นหนังสือแล้ว {{ overallSummary.inShelf }}</span>
          </div>
          <div class="flex items-center">
            <div class="w-3 h-3 bg-blue-500 rounded-full mr-1"></div>
            <span>กำลังสั่งซื้อ {{ overallSummary.ordered }}</span>
          </div>
          <div class="flex items-center">
            <div class="w-3 h-3 bg-purple-500 rounded-full mr-1"></div>
            <span>กำลังพิจารณาจัดซื้อ {{ overallSummary.requested }}</span>
          </div>
        </div>
      </div>

      <!-- Tabs -->
      <div class="flex border-b border-gray-200 mb-6">
        <button
          @click="activeTab = 'popular'"
          :class="[
            'px-4 py-3 font-medium',
            activeTab === 'popular'
              ? 'border-b-2 border-red-500 text-red-700'
              : 'text-gray-600 hover:text-gray-900',
          ]"
        >
          <div class="flex items-center">
            <Award class="h-5 w-5 mr-2" />
            หนังสือยอดนิยม
          </div>
        </button>

        <!-- Tab สำหรับหนังสือของคุณ -->
        <button
          v-if="authStore.isAuthenticated"
          @click="activeTab = 'status'"
          :class="[
            'px-4 py-3 font-medium',
            activeTab === 'status'
              ? 'border-b-2 border-red-500 text-red-700'
              : 'text-gray-600 hover:text-gray-900',
          ]"
        >
          <div class="flex items-center">
            <BookOpen class="h-5 w-5 mr-2" />
            สถานะหนังสือของคุณ
          </div>
        </button>
      </div>
      <!-- Popular Books -->
      <div v-if="activeTab === 'popular'">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">
          หนังสือที่ได้รับความนิยมสูงสุด
        </h2>

        <div
          class="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3 md:gap-6"
        >
          <div
            v-for="(book, index) in topBooks"
            :key="book.id"
            class="text-center relative flex flex-col h-full"
          >
            <!-- Book Cover with Rank -->
            <router-link :to="`/book/${book.id}`" class="block">
              <img
                :src="book.cover || '/no-image.png'"
                :alt="book.title"
                class="w-[120px] h-[180px] md:w-[160px] md:h-[240px] object-cover mx-auto rounded-lg shadow-md hover:shadow-lg transition-shadow"
              />
              <!-- Rank Badge -->
              <div
                class="absolute top-2 left-2 bg-yellow-500 text-white font-bold py-1 px-2 text-xs rounded"
              >
                อันดับ {{ index + 1 }}
              </div>
            </router-link>

            <!-- Book Info -->
            <div class="mt-2 px-1">
              <p class="text-black-600 text-xs md:text-base font-bold">
                {{ book.title }}
              </p>
            </div>

            <!-- Bottom: Favorite + Status - Added mt-auto to push this to bottom -->
            <div
              class="flex justify-between items-center px-2 py-2 bg-gray-50 bg-opacity-90 rounded-md mt-auto"
            >
              <div class="flex items-center text-sm">
                <Heart class="h-4 w-4 text-red-500 mr-1 fill-current" />
                <span class="text-gray-700">{{
                  book.favorites.toLocaleString()
                }}</span>
              </div>
              <div
                class="px-2 py-1 text-xs font-medium text-white rounded-full"
                :class="getStatusColor(book.status)"
              >
                {{ getStatusText(book.status) }}
              </div>
            </div>
          </div>
        </div>

        <!-- Info Box -->
        <div class="mt-8 p-6 bg-yellow-50 rounded-lg border border-yellow-200">
          <h3 class="font-semibold text-yellow-800 mb-2">
            เกี่ยวกับหนังสือยอดนิยม
          </h3>
          <p class="text-yellow-700 text-sm">
            หนังสือเหล่านี้ได้รับความชื่นชอบจากผู้ใช้มากที่สุด!
            เราจะนำข้อมูลนี้ไปพิจารณาในการจัดซื้อหนังสือเพิ่มเติม
            เพื่อให้ตรงกับความต้องการของผู้ใช้มากที่สุด
          </p>
        </div>
      </div>
      <!-- User Favorites -->
      <div v-if="authStore.isAuthenticated && activeTab === 'status'">
        <h2 class="text-xl font-semibold mb-4">หนังสือที่คุณชื่นชอบ</h2>

        <div
          v-if="userFavorites.length > 0"
          class="grid grid-cols-1 md:grid-cols-2 gap-6"
        >
          <div
            v-for="book in books.filter((b) => userFavorites.includes(b.id))"
            :key="book.id"
            class="bg-white rounded-xl shadow hover:shadow-md transition-all duration-200 overflow-hidden flex"
          >
            <!-- Book cover -->
            <img
              :src="book.cover || '/no-image.png'"
              :alt="book.title"
              class="w-[100px] h-[150px] object-cover rounded-l-xl"
            />

            <!-- Book info -->
            <div class="flex flex-col justify-between p-4 flex-1">
              <div>
                <h3 class="text-md md:text-lg font-semibold text-gray-800">
                  {{ book.title }}
                </h3>
                <p class="text-sm text-gray-500 mb-2">{{ book.author }}</p>
              </div>

              <div class="flex justify-between items-center">
                <!-- Status badge -->
                <span
                  class="px-3 py-1 text-xs font-medium text-white rounded-full"
                  :class="getStatusColor(book.status)"
                >
                  {{ getStatusText(book.status) }}
                </span>

                <!-- Locked favorite icon -->
                <div
                  class="bg-red-100 text-red-500 p-2 rounded-full cursor-not-allowed"
                  title="หนังสือนี้ถูกเพิ่มในรายการโปรดแล้ว"
                >
                  <Heart class="h-5 w-5 fill-current" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- No favorites -->
        <div
          v-else
          class="bg-white rounded-lg shadow p-8 text-center text-gray-600"
        >
          <Heart class="h-10 w-10 text-gray-300 mx-auto mb-4" />
          <p>คุณยังไม่มีหนังสือที่ชื่นชอบ</p>
          <p class="text-sm mt-1">กดที่ไอคอนหัวใจเพื่อเพิ่มหนังสือที่คุณสนใจ</p>
        </div>
      </div>

      <div class="mt-8 p-6 bg-blue-50 rounded-lg border border-blue-200">
        <h3 class="font-semibold text-blue-800 mb-2">การติดตามสถานะหนังสือ</h3>
        <p class="text-blue-700">
          คุณสามารถติดตามสถานะของหนังสือที่คุณชื่นชอบได้ที่นี่
          เมื่อหนังสือเปลี่ยนสถานะ (เช่น จาก "กำลังสั่งซื้อ" เป็น
          "มีในชั้นหนังสือแล้ว")
        </p>
      </div>
    </div>
  </div>
  <Footer />
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import {
  Heart,
  BookOpen,
  ShoppingCart,
  BarChart3,
  TrendingUp,
  Award,
  Users,
} from "lucide-vue-next";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();
import axios from "axios";

const additionalSummary = ref({
  count: 0,
  inShelf: 0,
  ordered: 0,
  requested: 0,
});
const booksWithFavorites = ref(0);
const topBooks = ref([]);
const userFavorites = ref([]);
const books = ref([]);
const overallSummary = ref({ inShelf: 0, ordered: 0, requested: 0, total: 0 });
const summary = ref({ inShelf: 0, ordered: 0, requested: 0, total: 0 });
const baseURL = import.meta.env.VITE_API_BASE_URL;

onMounted(async () => {
  try {
    const { data } = await axios.get(
      `${baseURL}/api/user/dashboard`,
      {
        withCredentials: true,
      }
    );

    // เพิ่ม fallback กรณีไม่ได้ค่าอะไรเลย (guest หรือ error ฝั่ง backend)
    if (!booksWithFavorites.value || booksWithFavorites.value === 0) {
      booksWithFavorites.value = 100; // 🔧 ใส่ค่าประมาณตาม logic ระบบคุณ
    }
    console.log("📦 โหลดข้อมูล:", data);
    additionalSummary.value = data.additionalSummary;
    books.value = data.books;
    summary.value = data.summary;
    topBooks.value = data.topBooks;
    overallSummary.value = data.overallSummary;
    userFavorites.value = data.userFavorites;
    booksWithFavorites.value = data.booksWithFavorites;
  } catch (err) {
    console.error("โหลด dashboard ล้มเหลว", err);
    booksWithFavorites.value = 100;
    books.value = [];
    userFavorites.value = [];
    summary.value = { inShelf: 0, ordered: 0, requested: 0, total: 0 };
  }
});
// Computed properties for stats
const totalBooks = computed(() => books.value.length);
const totalFavorites = computed(() =>
  books.value.reduce((sum, book) => sum + book.favorites, 0)
);
const inShelfCount = computed(
  () => books.value.filter((book) => book.status === "in_shelf").length
);
const orderedCount = computed(
  () => books.value.filter((book) => book.status === "ordered").length
);
const requestedCount = computed(
  () => books.value.filter((book) => book.status === "popular_request").length
);

// Reactive state
const activeTab = ref("popular");

// Functions
const toggleFavorite = (id) => {
  if (userFavorites.value.includes(id)) {
    userFavorites.value = userFavorites.value.filter((bookId) => bookId !== id);
  } else {
    userFavorites.value = [...userFavorites.value, id];
  }
};

const getStatusText = (status) => {
  switch (status) {
    case "in_shelf":
      return "มีในชั้นหนังสือแล้ว";
    case "ordered":
      return "กำลังสั่งซื้อ";
    case "popular_request":
      return "พิจารณาจัดซื้อ";
    default:
      return status;
  }
};

const getStatusColor = (status) => {
  switch (status) {
    case "in_shelf":
      return "bg-green-500";
    case "ordered":
      return "bg-blue-500";
    case "popular_request":
      return "bg-purple-500";
    default:
      return "bg-gray-500";
  }
};
</script>
