<template>
  <div class="bg-gray-50">
    <Header />
    <div class="max-w-6xl mx-auto p-6 bg-gray-50 min-h-screen mt-20">
      <!-- Header -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-bold text-gray-800">
          ภาพรวมและสถานะของหนังสือ
        </h1>
        <div class="bg-white px-4 py-2 rounded-lg shadow flex items-center">
          <Heart class="h-5 w-5 text-red-500 mr-2" />
          <span class="font-medium"
            >หนังสือที่คุณชื่นชอบ: {{ userFavorites.length }} เล่ม</span
          >
        </div>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-red-100 p-3 rounded-full mr-4">
            <Heart class="h-8 w-8 text-red-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">การกดหัวใจทั้งหมด</p>
            <p class="text-2xl font-bold">
              {{ booksWithFavorites?.toLocaleString() || 0 }} เล่ม
            </p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-green-100 p-3 rounded-full mr-4">
            <BookOpen class="h-8 w-8 text-green-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">มีในชั้นหนังสือแล้ว</p>
            <p class="text-2xl font-bold">{{ inShelfCount }} เล่ม</p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-blue-100 p-3 rounded-full mr-4">
            <ShoppingCart class="h-8 w-8 text-blue-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">กำลังสั่งซื้อ</p>
            <p class="text-2xl font-bold">{{ orderedCount }} เล่ม</p>
          </div>
        </div>
        <div class="bg-white rounded-lg shadow p-6 flex items-center">
          <div class="bg-purple-100 p-3 rounded-full mr-4">
            <Users class="h-8 w-8 text-purple-500" />
          </div>
          <div>
            <p class="text-sm text-gray-500">กำลังพิจารณาจัดซื้อ</p>
            <p class="text-2xl font-bold">{{ requestedCount }} เล่ม</p>
          </div>
        </div>
      </div>

      <!-- Status Ratio Bar -->
      <div class="bg-white rounded-lg shadow p-6 mb-8">
        <h2 class="text-lg font-semibold mb-4 flex items-center">
          <BarChart3 class="mr-2 h-5 w-5 text-gray-700" />
          สัดส่วนสถานะหนังสือในระบบ
        </h2>
        <div class="h-6 bg-gray-200 rounded-full overflow-hidden flex">
          <div
            v-if="inShelfCount > 0"
            class="h-full bg-green-500"
            :style="{ width: `${(inShelfCount / booksWithFavorites) * 100}%` }"
          ></div>
          <div
            v-if="orderedCount > 0"
            class="h-full bg-blue-500"
            :style="{ width: `${(orderedCount / booksWithFavorites) * 100}%` }"
          ></div>
          <div
            v-if="requestedCount > 0"
            class="h-full bg-purple-500"
            :style="{
              width: `${(requestedCount / booksWithFavorites) * 100}%`,
            }"
          ></div>
        </div>
        <div class="flex justify-center mt-4 text-sm flex-wrap gap-4">
          <div class="flex items-center">
            <div class="w-3 h-3 bg-green-500 rounded-full mr-1"></div>
            <span>มีในชั้นหนังสือแล้ว {{ inShelfCount }}</span>
          </div>
          <div class="flex items-center">
            <div class="w-3 h-3 bg-blue-500 rounded-full mr-1"></div>
            <span>กำลังสั่งซื้อ {{ orderedCount }}</span>
          </div>
          <div class="flex items-center">
            <div class="w-3 h-3 bg-purple-500 rounded-full mr-1"></div>
            <span>กำลังพิจารณาจัดซื้อ {{ requestedCount }}</span>
          </div>
        </div>
        <p class="mt-6 text-center text-gray-600 italic">
          "เราฟังเสียงของคุณ!
          หนังสือที่ได้รับความนิยมสูงจะถูกจัดซื้อเข้าห้องสมุดเร็วๆ นี้"
        </p>
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

        <button
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
                :src="book.cover"
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
              class="flex justify-between items-center px-2 py-2 bg-white bg-opacity-90 rounded-md mt-auto"
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
      <div v-if="activeTab === 'status'">
        <h2 class="text-xl font-semibold mb-4">หนังสือที่คุณชื่นชอบ</h2>
        <div v-if="userFavorites.length > 0" class="grid grid-cols-1 gap-4">
          <div
            v-for="book in books.filter((b) => userFavorites.includes(b.id))"
            :key="book.id"
            class="bg-white rounded-lg shadow overflow-hidden"
          >
            <div class="flex">
              <img
                :src="book.cover"
                :alt="book.title"
                class="w-20 h-28 object-cover"
              />
              <div class="p-4 flex-1">
                <h3 class="font-bold text-gray-800 mb-1">{{ book.title }}</h3>
                <div class="flex items-center">
                  <span
                    :class="[
                      'px-3 py-1 text-xs font-medium text-white rounded-full',
                      getStatusColor(book.status),
                    ]"
                  >
                    {{ getStatusText(book.status) }}
                  </span>
                </div>
              </div>
              <button
                class="p-4 flex items-center justify-center cursor-default"
                disabled
              >
                <Heart class="h-6 w-6 text-red-500 fill-current" />
              </button>
            </div>
          </div>
        </div>
        <div v-else class="bg-white rounded-lg shadow p-8 text-center">
          <Heart class="h-12 w-12 text-gray-300 mx-auto mb-4" />
          <p class="text-gray-500">
            คุณยังไม่มีหนังสือที่ชื่นชอบ
            กดที่ไอคอนหัวใจเพื่อเพิ่มหนังสือที่คุณสนใจ
          </p>
        </div>
        <div class="mt-8 p-6 bg-blue-50 rounded-lg border border-blue-200">
          <h3 class="font-semibold text-blue-800 mb-2">
            การติดตามสถานะหนังสือ
          </h3>
          <p class="text-blue-700">
            คุณสามารถติดตามสถานะของหนังสือที่คุณชื่นชอบได้ที่นี่
            เมื่อหนังสือเปลี่ยนสถานะ (เช่น จาก "กำลังสั่งซื้อ" เป็น
            "มีในชั้นหนังสือแล้ว")
            ระบบจะแจ้งเตือนให้คุณทราบผ่านอีเมลและการแจ้งเตือนในแอป
          </p>
        </div>
      </div>

      <!-- Footer -->
      <div class="mt-8 text-center p-6 bg-gray-100 rounded-lg">
        <h3 class="font-bold text-xl text-gray-800 mb-2">
          ขอบคุณสำหรับการมีส่วนร่วม!
        </h3>
        <p class="text-gray-600 max-w-2xl mx-auto">
          การกดหัวใจช่วยให้เราทราบความต้องการของผู้ใช้ ในแต่ละเดือน
          เราจะพิจารณาหนังสือที่ได้รับความนิยมสูงสุด เพื่อนำเข้าห้องสมุดของเรา
          นี่คือวิธีที่เราปรับปรุงคอลเลกชันหนังสือให้ตรงกับความต้องการของผู้ใช้อย่างแท้จริง
        </p>
      </div>
    </div>
    <Footer />
  </div>
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

import axios from "axios";

const books = ref([]);
const summary = ref({ inShelf: 0, ordered: 0, requested: 0, total: 0 });
const booksWithFavorites = ref(0);
onMounted(async () => {
  try {
    const { data } = await axios.get(
      "http://localhost:8080/api/user/dashboard",
      {
        withCredentials: true,
      }
    );
    books.value = data.books;
    summary.value = data.summary;
    userFavorites.value = data.userFavorites;
    booksWithFavorites.value = data.booksWithFavorites;
  } catch (err) {
    console.error("โหลด dashboard ล้มเหลว", err);
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
const topBooks = computed(() =>
  [...books.value].sort((a, b) => b.favorites - a.favorites).slice(0, 5)
);
const trendingBooks = computed(() =>
  [...books.value]
    .sort((a, b) => b.trendingStatus - a.trendingStatus)
    .slice(0, 3)
);

// Reactive state
const activeTab = ref("popular");
const userFavorites = ref([1, 3, 4, 6]);

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
