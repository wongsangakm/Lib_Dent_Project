<template>
  <div
    class="min-h-screen bg-gradient-to-br from-slate-50 via-white to-indigo-50"
  >
    <!-- Header Section -->
    <div
      class="bg-white/80 backdrop-blur-sm border-b border-slate-200 sticky top-0 z-10"
    >
      <div class="max-w-6xl mx-auto px-4 py-4 sm:px-6">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-3">
            <button
              @click="$router.push('/admin/request-table')"
              class="p-2 rounded-lg hover:bg-slate-100 transition-colors"
            >
              <svg
                class="w-5 h-5 text-slate-600"
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
            </button>
            <h1 class="text-xl sm:text-2xl font-bold text-slate-800">
              Request Details
            </h1>
          </div>
          <div class="text-sm text-slate-500">ID: #{{ requestId }}</div>
        </div>
      </div>
    </div>

    <div class="max-w-4xl mx-auto px-4 py-8 sm:px-6">
      <!-- Loading State -->
      <div v-if="loading" class="flex justify-center py-20">
        <div class="flex flex-col items-center gap-4">
          <div
            class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"
          ></div>
          <p class="text-slate-600">Loading request details...</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="!request" class="text-center py-20">
        <div
          class="bg-red-50 border border-red-200 rounded-xl p-8 max-w-md mx-auto"
        >
          <div class="text-red-500 mb-4">
            <svg
              class="w-16 h-16 mx-auto"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
              />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-red-800 mb-2">
            Request Not Found
          </h3>
          <p class="text-red-600">
            The requested item could not be found or may have been removed.
          </p>
        </div>
      </div>

      <!-- Main Content -->
      <div v-else class="space-y-8">
        <!-- Status Banner -->
        <div
          class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden"
        >
          <div
            :class="[
              'p-6 text-center',
              request.status === 'PENDING'
                ? 'bg-gradient-to-r from-amber-50 to-orange-50 border-b border-amber-200'
                : request.status === 'REJECTED'
                ? 'bg-gradient-to-r from-red-50 to-pink-50 border-b border-red-200'
                : request.status === 'in_shelf'
                ? 'bg-gradient-to-r from-emerald-50 to-green-50 border-b border-emerald-200'
                : request.status === 'ordered'
                ? 'bg-gradient-to-r from-blue-50 to-indigo-50 border-b border-blue-200'
                : request.status === 'popular_request'
                ? 'bg-gradient-to-r from-purple-50 to-indigo-50 border-b border-purple-200'
                : 'bg-gradient-to-r from-slate-50 to-gray-50 border-b border-slate-200',
            ]"
          >
            <div class="flex items-center justify-center gap-3 mb-2">
              <div
                :class="[
                  'w-3 h-3 rounded-full',
                  request.status === 'PENDING'
                    ? 'bg-amber-400 animate-pulse'
                    : request.status === 'REJECTED'
                    ? 'bg-red-500'
                    : request.status === 'in_shelf'
                    ? 'bg-emerald-500'
                    : request.status === 'ordered'
                    ? 'bg-blue-500 animate-pulse'
                    : request.status === 'popular_request'
                    ? 'bg-purple-500'
                    : 'bg-slate-400',
                ]"
              ></div>
              <h2
                :class="[
                  'text-xl font-bold',
                  request.status === 'PENDING'
                    ? 'text-amber-700'
                    : request.status === 'REJECTED'
                    ? 'text-red-700'
                    : request.status === 'in_shelf'
                    ? 'text-emerald-700'
                    : request.status === 'ordered'
                    ? 'text-blue-700'
                    : request.status === 'popular_request'
                    ? 'text-purple-700'
                    : 'text-slate-700',
                ]"
              >
                {{ displayStatus(request.status) }}
              </h2>
            </div>
            <p class="text-sm opacity-75">
              {{ getStatusDescription(request.status) }}
            </p>
          </div>
        </div>

        <!-- Book Information Card -->
        <div
          class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden"
        >
          <div
            class="bg-gradient-to-r from-indigo-500 to-purple-600 p-6 text-white"
          >
            <div class="flex items-center gap-3 mb-2">
              <svg
                class="w-6 h-6"
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
              <h3 class="text-lg font-semibold">Book Information</h3>
            </div>
          </div>

          <div class="p-6 space-y-6">
            <!-- Book Title -->
            <div class="pb-4 border-b border-slate-100">
              <h4 class="text-2xl font-bold text-slate-800 mb-2">
                {{ request.bookTitle }}
              </h4>
              <div class="flex flex-wrap gap-4 text-sm text-slate-600">
                <span v-if="request.author" class="flex items-center gap-1">
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
                <span v-if="request.year" class="flex items-center gap-1">
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
                <div v-if="request.publisher">
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >Publisher</label
                  >
                  <p class="text-slate-600 bg-slate-50 rounded-lg px-3 py-2">
                    {{ request.publisher }}
                  </p>
                </div>

                <div v-if="request.isbn">
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >ISBN</label
                  >
                  <p
                    class="text-slate-600 bg-slate-50 rounded-lg px-3 py-2 font-mono"
                  >
                    {{ request.isbn }}
                  </p>
                </div>
              </div>

              <div class="space-y-4">
                <div v-if="request.price">
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >Price</label
                  >
                  <p class="text-slate-600 bg-slate-50 rounded-lg px-3 py-2">
                    <span class="text-lg font-semibold"
                      >{{ request.price }} ฿</span
                    >
                  </p>
                </div>
                <div v-if="request.description">
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >description</label
                  >
                  <p class="text-slate-600 bg-slate-50 rounded-lg px-3 py-2">
                    <span class="text-lg font-semibold"
                      >{{ request.description }} ฿</span
                    >
                  </p>
                </div>
              </div>
            </div>

            <!-- Reason Section -->
            <div v-if="request.reason" class="pt-4 border-t border-slate-100">
              <label class="block text-sm font-medium text-slate-700 mb-2"
                >Request Reason</label
              >
              <div class="bg-slate-50 rounded-lg p-4">
                <p class="text-slate-600 whitespace-pre-line leading-relaxed">
                  {{ request.reason }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Request Information Card -->
        <div
          class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden"
        >
          <div
            class="bg-gradient-to-r from-slate-600 to-slate-700 p-6 text-white"
          >
            <div class="flex items-center gap-3">
              <svg
                class="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
                />
              </svg>
              <h3 class="text-lg font-semibold">Request Information</h3>
            </div>
          </div>

          <div class="p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >Requested By</label
                  >
                  <div
                    class="flex items-center gap-2 bg-slate-50 rounded-lg px-3 py-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-400"
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
                    <span class="text-slate-600">{{
                      request.requestedBy || "N/A"
                    }}</span>
                  </div>
                </div>
              </div>

              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-slate-700 mb-1"
                    >Request Date</label
                  >
                  <div
                    class="flex items-center gap-2 bg-slate-50 rounded-lg px-3 py-2"
                  >
                    <svg
                      class="w-4 h-4 text-slate-400"
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
                    <span class="text-slate-600">{{
                      formatDate(request.requestDate)
                    }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div
          v-if="request && request.status !== 'REJECTED'"
          class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6"
        >
          <h3 class="text-lg font-semibold text-slate-800 mb-4">Actions</h3>
          <div class="flex flex-wrap gap-4">
            <button
              @click="showApproveDialog = true"
              class="flex items-center gap-2 bg-gradient-to-r from-green-500 to-emerald-600 hover:from-green-600 hover:to-emerald-700 text-white font-medium px-6 py-3 rounded-xl transition-all duration-200 shadow-lg hover:shadow-xl"
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
                  d="M5 13l4 4L19 7"
                />
              </svg>
              {{
                request.status === "PENDING"
                  ? "Approve Request"
                  : "Update Status"
              }}
            </button>

            <button
              v-if="request.status === 'PENDING'"
              @click="showRejectDialog = true"
              class="flex items-center gap-2 bg-gradient-to-r from-red-500 to-red-600 hover:from-red-600 hover:to-red-700 text-white font-medium px-6 py-3 rounded-xl transition-all duration-200 shadow-lg hover:shadow-xl"
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
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
              Reject Request
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Approve/Update Dialog -->
    <div
      v-if="showApproveDialog"
      class="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50 backdrop-blur-sm p-4"
    >
      <div
        class="bg-white rounded-2xl shadow-2xl w-full max-w-md transform transition-all"
      >
        <div
          class="bg-gradient-to-r from-indigo-500 to-purple-600 p-6 text-white rounded-t-2xl"
        >
          <h2 class="text-xl font-bold">
            {{
              request.status === "PENDING" ? "Approve Request" : "Update Status"
            }}
          </h2>
          <p class="text-indigo-100 text-sm mt-1">
            {{
              request.status === "PENDING"
                ? "Choose the final status for this request"
                : "Select a new status for this request"
            }}
          </p>
        </div>

        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2"
              >Select Status</label
            >
            <select
              v-model="finalStatus"
              class="w-full border border-slate-300 rounded-xl px-4 py-3 focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
            >
              <option disabled value="">-- กรุณาเลือกสถานะ --</option>
              <option value="in_shelf">✅ มีในชั้นหนังสือแล้ว</option>
              <option value="ordered">🛒 กำลังสั่งซื้อ</option>
              <option value="popular_request">⭐ กำลังพิจารณาจัดซื้อ</option>
            </select>
          </div>

          <div
            v-if="successMessage"
            class="bg-green-50 border border-green-200 rounded-xl p-4"
          >
            <div class="flex items-center gap-2 text-green-700">
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
                  d="M5 13l4 4L19 7"
                />
              </svg>
              <span class="font-medium">{{ successMessage }}</span>
            </div>
          </div>

          <div class="flex gap-3 pt-4">
            <button
              @click="showApproveDialog = false"
              class="flex-1 px-4 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium rounded-xl transition-colors"
            >
              Cancel
            </button>
            <button
              @click="confirmApprove"
              :disabled="!finalStatus || isUpdating"
              class="flex-1 px-4 py-3 bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white font-medium rounded-xl disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 flex items-center justify-center gap-2"
            >
              <svg
                v-if="isUpdating"
                class="w-4 h-4 animate-spin"
                fill="none"
                viewBox="0 0 24 24"
              >
                <circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                ></circle>
                <path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path>
              </svg>
              {{ isUpdating ? "Updating..." : "Confirm" }}
            </button>
          </div>
        </div>
      </div>
    </div>
    <!-- Reject Dialog -->
    <div
      v-if="showRejectDialog"
      class="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50 backdrop-blur-sm p-4"
    >
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-md">
        <div
          class="bg-gradient-to-r from-red-500 to-pink-600 p-6 text-white rounded-t-2xl"
        >
          <h2 class="text-xl font-bold">Reject Request</h2>
          <p class="text-red-100 text-sm mt-1">
            Please provide a reason for rejecting this request.
          </p>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2"
              >Reason</label
            >
            <textarea
              v-model="rejectReason"
              rows="4"
              class="w-full border border-slate-300 rounded-xl px-4 py-3 focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
              placeholder="เช่น ซ้ำกับที่มีอยู่แล้ว, ไม่เหมาะสม, ฯลฯ"
            ></textarea>
          </div>

          <div class="flex gap-3 pt-4">
            <button
              @click="showRejectDialog = false"
              class="flex-1 px-4 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium rounded-xl transition-colors"
            >
              Cancel
            </button>
            <button
              @click="confirmReject"
              :disabled="!rejectReason"
              class="flex-1 px-4 py-3 bg-gradient-to-r from-red-500 to-pink-600 hover:from-red-600 hover:to-pink-700 text-white font-medium rounded-xl disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 flex items-center justify-center gap-2"
            >
              Reject
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import { ElMessage } from "element-plus";
const authStore = useAuthStore();

const route = useRoute();
const router = useRouter();
const requestId = route.params.id;
const baseURL = import.meta.env.VITE_API_BASE_URL;

const request = ref(null);
const loading = ref(true);
const showApproveDialog = ref(false);
const finalStatus = ref("");
const successMessage = ref("");
const isUpdating = ref(false);
const rejectReason = ref("");
const showRejectDialog = ref(false);

// โหลดคำขอ
const loadRequest = async () => {
  try {
    const res = await fetch(`${baseURL}/api/admin/request-table/${requestId}`, {
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

// อัปเดตสถานะ / อนุมัติ
const confirmApprove = async () => {
  isUpdating.value = true;
  try {
    const res = await fetch(
      `${baseURL}/api/admin/request-table/${requestId}/approve`,
      {
        method: "PUT",

        headers: {
          ...authStore.getAuthHeader(),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ finalStatus: finalStatus.value }),
      }
    );

    if (!res.ok) throw new Error("Approval failed");

    // ซ่อน popup ทันที
    showApproveDialog.value = false;
    finalStatus.value = "";
    successMessage.value = "";

    // โหลดข้อมูลใหม่
    await loadRequest();

    // แสดงข้อความสำเร็จแบบ El
    ElMessage({
      type: "success",
      message: "Request updated successfully!",
    });
  } catch (err) {
    console.error("❌ Approval error:", err);

    ElMessage({
      type: "error",
      message: "Update failed. Please try again.",
    });
  } finally {
    isUpdating.value = false;
  }
};

// ปฏิเสธคำขอ
const confirmReject = async () => {
  if (!rejectReason.value) return;

  try {
    const res = await fetch(
      `${baseURL}/api/admin/request-table/${requestId}/reject`,
      {
        method: "PUT",
        mode: "cors",
        credentials: "include",
        headers: {
          ...authStore.getAuthHeader(),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ reason: rejectReason.value }),
      }
    );

    if (!res.ok) throw new Error("Reject failed");

    ElMessage({
      type: "success",
      message: "Request rejected successfully.",
    });

    await loadRequest();
    showRejectDialog.value = false;
    rejectReason.value = "";
  } catch (err) {
    console.error("❌ Reject error:", err);

    ElMessage({
      type: "error",
      message: "Reject failed. Please try again.",
    });
  }
};

// แสดงสถานะในภาษาไทย
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

// คำอธิบายสถานะ
const getStatusDescription = (status) => {
  switch (status) {
    case "in_shelf":
      return "หนังสือเล่มนี้พร้อมให้บริการแล้ว";
    case "ordered":
      return "กำลังดำเนินการสั่งซื้อหนังสือเล่มนี้";
    case "popular_request":
      return "อยู่ระหว่างการพิจารณาจัดซื้อ";
    case "PENDING":
      return "รอการพิจารณาจากผู้ดูแลระบบ";
    case "REJECTED":
      return "คำขอนี้ถูกปฏิเสธแล้ว";
    default:
      return "สถานะไม่ทราบ";
  }
};

// รูปแบบวันที่
const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleDateString("en-GB", {
    year: "numeric",
    month: "short",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

onMounted(loadRequest);
</script>

<style scoped>
/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Smooth transitions */
* {
  transition: all 0.2s ease;
}

/* Focus styles */
button:focus-visible,
select:focus-visible {
  outline: 2px solid #6366f1;
  outline-offset: 2px;
}
</style>
