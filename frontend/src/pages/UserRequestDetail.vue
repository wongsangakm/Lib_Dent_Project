<template>
  <div
    class="min-h-screen flex flex-col bg-gradient-to-br from-indigo-50 via-white to-purple-50"
    :style="{
      backgroundImage: `linear-gradient(rgba(255,255,255,0.8), rgba(255,255,255,0.9)), url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
    <Header />

    <div class="container mx-auto px-4 pt-24 pb-8 w-full max-w-3xl flex-1">
      <!-- Page Header -->
      <div class="text-center mb-8">
        <h1
          class="text-3xl sm:text-4xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-2"
        >
          Your Book Request
        </h1>
        <p class="text-slate-600 max-w-2xl mx-auto">
          Track the status of your book request and see detailed information
          about your submission
        </p>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="flex justify-center py-20">
        <div class="flex flex-col items-center gap-4">
          <div class="relative">
            <div
              class="w-16 h-16 border-4 border-indigo-200 rounded-full animate-spin"
            ></div>
            <div
              class="w-16 h-16 border-4 border-indigo-600 rounded-full animate-spin absolute top-0 left-0"
              style="
                border-right-color: transparent;
                border-top-color: transparent;
              "
            ></div>
          </div>
          <p class="text-slate-600 font-medium">Loading your request...</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="!request" class="text-center py-20">
        <div
          class="bg-white/80 backdrop-blur-sm border border-red-200 rounded-2xl p-8 max-w-md mx-auto shadow-lg"
        >
          <div class="text-red-500 mb-6">
            <svg
              class="w-20 h-20 mx-auto opacity-80"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="1.5"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
              />
            </svg>
          </div>
          <h3 class="text-xl font-bold text-slate-800 mb-3">
            Request Not Found
          </h3>
          <p class="text-slate-600 mb-6">
            Sorry, we couldn't find the request you're looking for. It may have
            been removed or the link is incorrect.
          </p>
          <router-link
            to="/favbooks"
            class="inline-flex items-center gap-2 bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white font-medium px-6 py-3 rounded-xl transition-all duration-200 shadow-lg hover:shadow-xl"
          >
            <svg
              class="w-4 h-4"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M15 19l-7-7 7-7"
              />
            </svg>
            Back to History
          </router-link>
        </div>
      </div>

      <!-- Main Content -->
      <div v-else class="space-y-6">
        <!-- Status Card -->
        <div
          class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg border border-white/50 overflow-hidden"
        >
          <div
            :class="[
              'p-6 text-center relative overflow-hidden',
              request.status === 'PENDING'
                ? 'bg-gradient-to-r from-amber-400 to-orange-500'
                : request.status === 'REJECTED'
                ? 'bg-gradient-to-r from-red-500 to-pink-600'
                : request.status === 'in_shelf'
                ? 'bg-gradient-to-r from-emerald-500 to-green-600'
                : request.status === 'ordered'
                ? 'bg-gradient-to-r from-blue-500 to-indigo-600'
                : request.status === 'popular_request'
                ? 'bg-gradient-to-r from-purple-500 to-indigo-600'
                : 'bg-gradient-to-r from-slate-500 to-gray-600',
            ]"
          >
            <!-- Background Pattern -->
            <div class="absolute inset-0 opacity-10">
              <div
                class="absolute inset-0"
                style="
                  background-image: radial-gradient(
                    circle at 25% 25%,
                    white 2px,
                    transparent 2px
                  );
                  background-size: 24px 24px;
                "
              ></div>
            </div>

            <div class="relative z-10">
              <!-- Status Icon -->
              <div class="mb-4">
                <div
                  class="w-16 h-16 mx-auto bg-white/20 rounded-full flex items-center justify-center backdrop-blur-sm"
                >
                  <svg
                    v-if="request.status === 'PENDING'"
                    class="w-8 h-8 text-white animate-pulse"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                  </svg>
                  <svg
                    v-else-if="request.status === 'REJECTED'"
                    class="w-8 h-8 text-white"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                    />
                  </svg>
                  <svg
                    v-else-if="request.status === 'in_shelf'"
                    class="w-8 h-8 text-white"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M5 13l4 4L19 7"
                    />
                  </svg>
                  <svg
                    v-else-if="request.status === 'ordered'"
                    class="w-8 h-8 text-white animate-bounce"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"
                    />
                  </svg>
                  <svg
                    v-else-if="request.status === 'popular_request'"
                    class="w-8 h-8 text-white animate-pulse"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"
                    />
                  </svg>
                  <svg
                    v-else
                    class="w-8 h-8 text-white"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                  </svg>
                </div>
              </div>

              <h2 class="text-2xl font-bold text-white mb-2">
                {{ displayStatus(request.status) }}
              </h2>
              <p class="text-white/90 text-sm max-w-md mx-auto">
                {{ getStatusDescription(request.status) }}
              </p>

              <!-- Progress Bar (for pending/ordered status) -->
              <div
                v-if="['PENDING', 'ordered'].includes(request.status)"
                class="mt-6"
              >
                <div class="bg-white/20 rounded-full h-2 mx-8">
                  <div
                    :class="[
                      'h-2 rounded-full transition-all duration-1000',
                      request.status === 'PENDING'
                        ? 'bg-white/80 w-1/3 animate-pulse'
                        : request.status === 'ordered'
                        ? 'bg-white/80 w-2/3'
                        : 'bg-white/80 w-full',
                    ]"
                  ></div>
                </div>
                <div
                  class="flex justify-between text-xs text-white/70 mt-2 mx-8"
                >
                  <span>Submitted</span>
                  <span>Processing</span>
                  <span>Complete</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Book Information Card -->
        <div
          class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg border border-white/50 overflow-hidden"
        >
          <div
            class="bg-gradient-to-r from-indigo-600 to-purple-700 p-6 text-white"
          >
            <div class="flex items-center gap-3 mb-2">
              <div
                class="w-10 h-10 bg-white/20 rounded-lg flex items-center justify-center backdrop-blur-sm"
              >
                <svg
                  class="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"
                  />
                </svg>
              </div>
              <h3 class="text-xl font-bold">Book Information</h3>
            </div>
            <p class="text-indigo-100 text-sm">
              Details about your requested book
            </p>
          </div>

          <div class="p-6 space-y-6">
            <!-- Book Title -->
            <div class="text-center pb-6 border-b border-slate-100">
              <h4
                class="text-2xl sm:text-3xl font-bold text-slate-800 mb-3 leading-tight"
              >
                {{ request.bookTitle }}
              </h4>
              <div class="flex flex-wrap justify-center gap-4 text-sm">
                <span
                  v-if="request.author"
                  class="inline-flex items-center gap-2 bg-slate-100 px-3 py-1 rounded-full text-slate-700"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                    />
                  </svg>
                  {{ request.author }}
                </span>
                <span
                  v-if="request.year"
                  class="inline-flex items-center gap-2 bg-slate-100 px-3 py-1 rounded-full text-slate-700"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                    />
                  </svg>
                  {{ request.year }}
                </span>
              </div>
            </div>

            <!-- Book Details Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="space-y-4">
                <div v-if="request.publisher" class="group">
                  <label
                    class="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"
                      />
                    </svg>
                    Publisher
                  </label>
                  <div
                    class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl px-4 py-3 group-hover:shadow-md transition-all duration-200"
                  >
                    <p class="text-slate-700 font-medium">
                      {{ request.publisher }}
                    </p>
                  </div>
                </div>

                <div v-if="request.isbn" class="group">
                  <label
                    class="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M7 4V2a1 1 0 011-1h3a1 1 0 011 1v2h4a1 1 0 011 1v3a1 1 0 01-1 1h-4v9a1 1 0 01-1 1H8a1 1 0 01-1-1V9H3a1 1 0 01-1-1V5a1 1 0 011-1h4z"
                      />
                    </svg>
                    ISBN
                  </label>
                  <div
                    class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl px-4 py-3 group-hover:shadow-md transition-all duration-200"
                  >
                    <p class="text-slate-700 font-mono text-sm">
                      {{ request.isbn }}
                    </p>
                  </div>
                </div>
              </div>

              <div class="space-y-4">
                <div v-if="request.price" class="group">
                  <label
                    class="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1"
                      />
                    </svg>
                    Price
                  </label>
                  <div
                    class="bg-gradient-to-r from-green-50 to-emerald-100 rounded-xl px-4 py-3 group-hover:shadow-md transition-all duration-200"
                  >
                    <p class="text-green-700 font-bold text-lg">
                      {{ request.price }} ฿
                    </p>
                  </div>
                </div>

                <div class="group">
                  <label
                    class="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-500"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        stroke-width="2"
                        d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                      />
                    </svg>
                    Request Date
                  </label>
                  <div
                    class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl px-4 py-3 group-hover:shadow-md transition-all duration-200"
                  >
                    <p class="text-slate-700 font-medium">
                      {{ formatDate(request.requestDate) }}
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <!-- Reason Section -->
            <div v-if="request.reason" class="pt-6 border-t border-slate-100">
              <!-- description section -->
              <label
                class="block text-sm font-semibold text-slate-700 mb-3 flex items-center gap-2"
              >
                <svg
                  class="w-4 h-4 text-slate-500"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                  />
                </svg>
                Description
              </label>
              <div
                class="bg-gradient-to-br from-indigo-50 to-purple-50 rounded-xl p-4 border border-indigo-100"
              >
                <p class="text-slate-700 whitespace-pre-line leading-relaxed">
                  {{ request.description }}
                </p>
              </div>
              <label
                class="mt-4 block text-sm font-semibold text-slate-700 mb-3 flex items-center gap-2"
              >
                <svg
                  class="w-4 h-4 text-slate-500"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
                  />
                </svg>
                Request Reason
              </label>

              <div
                class="bg-gradient-to-br from-indigo-50 to-purple-50 rounded-xl p-4 border border-indigo-100"
              >
                <p class="text-slate-700 whitespace-pre-line leading-relaxed">
                  {{ request.reason }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div
          class="bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg border border-white/50 p-6"
        >
          <div class="flex justify-center">
            <router-link
              to="/favbooks"
              class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-slate-500 to-slate-600 hover:from-slate-600 hover:to-slate-700 text-white font-medium px-8 py-3 rounded-xl transition-all duration-200 shadow-lg hover:shadow-xl"
            >
              <svg
                class="w-5 h-5"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M15 19l-7-7 7-7"
                />
              </svg>
              Back to History
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <Footer class="mt-auto" />
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();

const route = useRoute();
const router = useRouter();
const requestId = route.params.id;
const request = ref(null);
const loading = ref(true);
const baseURL = import.meta.env.VITE_API_BASE_URL;
const loadRequest = async () => {
  try {
    const res = await fetch(`${baseURL}/api/requests/${requestId}`, {
      headers: authStore.getAuthHeader(),
    });
    if (!res.ok) throw new Error("Request not found");
    request.value = await res.json();
  } catch (err) {
    console.error("❌ Failed to load request:", err);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleDateString("th-TH", {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
  });
};

const displayStatus = (status) => {
  switch (status) {
    case "in_shelf":
      return "มีในชั้นหนังสือแล้ว";
    case "ordered":
      return "กำลังสั่งซื้อ";
    case "popular_request":
      return "กำลังพิจารณาจัดซื้อ";
    case "PENDING":
      return "รอการอนุมัติ";
    case "REJECTED":
      return "ถูกปฏิเสธ";
    default:
      return status;
  }
};

const getStatusDescription = (status) => {
  switch (status) {
    case "in_shelf":
      return "ยินดีด้วย! หนังสือที่คุณขอพร้อมให้บริการแล้ว สามารถไปยืมได้เลย";
    case "ordered":
      return "เรากำลังดำเนินการสั่งซื้อหนังสือเล่มนี้ อีกไม่นานจะมาถึงแน่นอน";
    case "popular_request":
      return "หนังสือของคุณอยู่ในรายการพิจารณา เนื่องจากมีผู้ขอหลายคน";
    case "PENDING":
      return "คำขอของคุณอยู่ระหว่างการพิจารณา เราจะแจ้งผลให้ทราบเร็วๆ นี้";
    case "REJECTED":
      return "ขออภัย คำขอนี้ไม่สามารถดำเนินการได้ในขณะนี้";
    default:
      return "สถานะไม่ทราบ กรุณาติดต่อเจ้าหน้าที่";
  }
};

onMounted(loadRequest);
</script>

<style scoped>
/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(241, 245, 249, 0.5);
}

::-webkit-scrollbar-thumb {
  background: rgba(203, 213, 225, 0.8);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(148, 163, 184, 0.9);
}

/* Smooth transitions */
* {
  transition: all 0.2s ease;
}

/* Backdrop blur support */
@supports (backdrop-filter: blur(12px)) {
  .backdrop-blur-sm {
    backdrop-filter: blur(12px);
  }
}

/* Animation keyframes */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fadeInUp {
  animation: fadeInUp 0.6s ease-out;
}

/* Hover effects */
.group:hover .group-hover\:shadow-md {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
    0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

/* Focus styles */
button:focus-visible,
a:focus-visible {
  outline: 2px solid #6366f1;
  outline-offset: 2px;
}
</style>
