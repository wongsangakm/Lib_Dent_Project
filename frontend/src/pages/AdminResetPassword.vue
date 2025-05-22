<!-- AdminResetPassword.vue -->
<template>
  <div class="min-h-screen bg-white">
    <div class="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
      <!-- Page Header -->
      <div class="border-b border-gray-200 pb-5 mb-6">
        <h2 class="text-2xl font-bold text-gray-800">รายการผู้ใช้งาน</h2>
        <p class="text-purple-600 text-xs sm:text-sm mt-4">
          <span class="text-red-500 font-bold mr-1">**</span>
          รหัสผ่านจะรีเซ็ทเป็น 123456
          <span class="text-red-500 font-bold ml-1">**</span>
        </p>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="flex justify-center py-12">
        <div class="animate-pulse flex flex-col items-center">
          <div
            class="w-12 h-12 rounded-full border-4 border-t-purple-500 border-r-transparent border-b-purple-500 border-l-transparent animate-spin"
          ></div>
          <p class="mt-4 text-gray-600">กำลังโหลดข้อมูล...</p>
        </div>
      </div>

      <!-- Error State -->
      <div
        v-else-if="error"
        class="bg-red-50 border-l-4 border-red-500 p-4 rounded-md mb-6"
      >
        <div class="flex">
          <div class="flex-shrink-0">
            <svg
              class="h-5 w-5 text-red-500"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fill-rule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <div class="ml-3">
            <p class="text-sm text-red-700">{{ error }}</p>
          </div>
        </div>
      </div>

      <!-- Content (when not loading or error) -->
      <div v-else>
        <!-- Search and Actions -->
        <div class="mb-6">
          <div class="sm:flex sm:justify-between">
            <div class="relative flex-grow max-w-lg">
              <input
                v-model="searchText"
                placeholder="ค้นหาชื่อ, username"
                class="block w-full rounded-md border-gray-300 shadow-sm focus:border-purple-500 focus:ring focus:ring-purple-500 focus:ring-opacity-50 pl-10 py-2"
                type="text"
              />
              <div
                class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
              >
                <svg
                  class="h-5 w-5 text-gray-400"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fill-rule="evenodd"
                    d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                    clip-rule="evenodd"
                  />
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div
          v-if="filteredUsers.length === 0"
          class="bg-gray-50 rounded-lg p-12 text-center"
        >
          <svg
            class="mx-auto h-12 w-12 text-gray-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"
            />
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">
            ไม่พบข้อมูลผู้ใช้งาน
          </h3>
          <p class="mt-1 text-sm text-gray-500">
            ลองเปลี่ยนคำค้นหาหรือตรวจสอบการเชื่อมต่อ
          </p>
        </div>

        <!-- User Data (when not empty) -->
        <div v-else>
          <!-- User Table (Desktop) -->
          <div class="hidden sm:block overflow-hidden shadow-md rounded-lg">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-sm font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Username
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-sm font-medium text-gray-500 uppercase tracking-wider"
                  >
                    ชื่อจริง
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-sm font-medium text-gray-500 uppercase tracking-wider"
                  >
                    นามสกุล
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-left text-sm font-medium text-gray-500 uppercase tracking-wider"
                  >
                    บทบาท
                  </th>
                  <th
                    scope="col"
                    class="px-6 py-3 text-center text-sm font-medium text-gray-500 uppercase tracking-wider"
                  >
                    รีเซ็ตรหัสผ่าน
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr
                  v-for="user in filteredUsers"
                  :key="user.id"
                  class="hover:bg-gray-50"
                >
                  <td
                    class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                  >
                    {{ user.username }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ user.firstName }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ user.lastName }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    <span
                      class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-purple-100 text-purple-800"
                    >
                      {{ user.role }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-center text-sm">
                    <button
                      @click="resetPassword(user.id)"
                      class="inline-flex items-center px-3 py-1.5 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors"
                      :disabled="resetting === user.id"
                    >
                      <svg
                        v-if="resetting === user.id"
                        class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
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
                      {{ resetting === user.id ? "กำลังรีเซ็ต..." : "รีเซ็ต" }}
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- User Cards (Mobile) -->
          <div class="sm:hidden space-y-4">
            <div
              v-for="user in filteredUsers"
              :key="user.id"
              class="bg-white shadow rounded-lg overflow-hidden border border-gray-200"
            >
              <div class="p-4">
                <div class="flex items-center justify-between mb-3">
                  <div class="flex items-center">
                    <div
                      class="flex-shrink-0 h-10 w-10 bg-purple-100 rounded-full flex items-center justify-center"
                    >
                      <span class="font-medium text-purple-800">{{
                        user.firstName?.[0] || "?"
                      }}</span>
                    </div>
                    <div class="ml-4">
                      <h3 class="text-sm font-medium text-gray-900">
                        {{ user.username }}
                      </h3>
                      <p class="text-sm text-gray-500">
                        {{ user.firstName }} {{ user.lastName }}
                      </p>
                    </div>
                  </div>
                  <span
                    class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-purple-100 text-purple-800"
                  >
                    {{ user.role }}
                  </span>
                </div>
                <div class="mt-3">
                  <button
                    @click="resetPassword(user.id)"
                    class="w-full inline-flex justify-center items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors"
                    :disabled="resetting === user.id"
                  >
                    <svg
                      v-if="resetting === user.id"
                      class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
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
                    {{
                      resetting === user.id
                        ? "กำลังรีเซ็ต..."
                        : "รีเซ็ตรหัสผ่าน"
                    }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";

const users = ref([]);
const searchText = ref("");
const loading = ref(true);
const error = ref(null);
const resetting = ref(null);

const baseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";

onMounted(async () => {
  try {
    loading.value = true;
    const res = await fetch(`${baseUrl}/api/admin/users`, {
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.error || `HTTP error: ${res.status}`);
    }

    const data = await res.json();
    if (Array.isArray(data)) {
      users.value = data;
      console.log("📦 loaded users:", users.value);
    } else {
      throw new Error("Server returned invalid data format");
    }
  } catch (err) {
    console.error("Fetch failed:", err);
    error.value = `ไม่สามารถโหลดข้อมูลผู้ใช้งานได้: ${err.message}`;
    users.value = [];
  } finally {
    loading.value = false;
  }
});

const filteredUsers = computed(() =>
  users.value.filter(
    (u) =>
      u.username?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      u.firstName?.toLowerCase().includes(searchText.value.toLowerCase()) ||
      u.lastName?.toLowerCase().includes(searchText.value.toLowerCase())
  )
);

const resetPassword = async (userId) => {
  try {
    const confirmed = confirm(
      "คุณต้องการรีเซ็ตรหัสผ่านเป็น 123456 ใช่หรือไม่?"
    );
    if (!confirmed) return;

    resetting.value = userId;

    const res = await fetch(
      `${baseUrl}/api/admin/users/${userId}/reset-password`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
        credentials: "include",
      }
    );

    const data = await res.json();

    if (res.ok) {
      alert("✅ รีเซ็ตรหัสผ่านเรียบร้อยแล้ว");
    } else {
      alert(`❌ เกิดข้อผิดพลาด: ${data.error || "ไม่ทราบสาเหตุ"}`);
    }
  } catch (err) {
    console.error("Reset password failed:", err);
    alert(`❌ เกิดข้อผิดพลาดในการเชื่อมต่อ: ${err.message}`);
  } finally {
    resetting.value = null;
  }
};
</script>
