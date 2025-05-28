<template>
  <div class="max-w-md mx-auto mt-10 p-6 bg-white rounded-2xl shadow">
    <h2 class="text-2xl font-semibold mb-4">แก้ไขอีเมลสำหรับรับการแจ้งเตือน</h2>

    <p class="mb-4 text-gray-700">
      📧 อีเมลปัจจุบัน: <span class="font-medium">{{ currentEmail }}</span>
    </p>

    <div
      v-if="successMessage"
      class="bg-green-100 text-green-800 p-2 mb-4 rounded"
    >
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="bg-red-100 text-red-800 p-2 mb-4 rounded">
      {{ errorMessage }}
    </div>

    <form @submit.prevent="updateEmail">
      <label class="block mb-2 font-medium">อีเมลผู้ดูแลระบบ</label>
      <input
        type="email"
        v-model="newEmail"
        required
        class="w-full px-4 py-2 border rounded-xl focus:outline-none focus:ring focus:border-blue-300"
      />

      <button
        type="submit"
        class="mt-4 w-full py-2.5 sm:py-3 px-4 sm:px-6 bg-gradient-to-r from-purple-600 to-purple-800 text-white font-medium rounded-lg shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition duration-300 flex items-center justify-center text-xs sm:text-sm"
      >
        บันทึก
      </button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();
const currentEmail = ref("");
const newEmail = ref("");
const successMessage = ref("");
const errorMessage = ref("");
const baseURL = import.meta.env.VITE_API_BASE_URL;
const fetchEmail = async () => {
  try {
    const res = await axios.get(`${baseURL}/api/admin/settings/email`, {
      headers: authStore.getAuthHeader(),
    });
    currentEmail.value = res.data.recipientEmail;
  } catch (err) {
    console.error("โหลดอีเมลล้มเหลว:", err);
    errorMessage.value = "ไม่สามารถโหลดอีเมลปัจจุบันได้";
  }
};

const updateEmail = async () => {
  try {
    await axios.post(
      `${baseURL}/api/admin/settings/update-email`,
      { email: newEmail.value },
      { withCredentials: true }
    );
    successMessage.value = "บันทึกอีเมลเรียบร้อยแล้ว";
    errorMessage.value = "";
    currentEmail.value = newEmail.value; // อัปเดตให้แสดงผลใหม่
  } catch (err) {
    successMessage.value = "";
    errorMessage.value = "เกิดข้อผิดพลาดในการบันทึก";
  }
};

onMounted(() => {
  fetchEmail();
});
</script>
