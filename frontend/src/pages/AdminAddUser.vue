<template>
  <div
    class="min-h-screen flex justify-center items-center py-6 px-4 sm:py-12 sm:px-6"
  >
    <div
      class="bg-white rounded-2xl shadow-xl w-full max-w-2xl border border-purple-100 overflow-hidden"
    >
      <!-- Card Header with Gradient Border -->
      <div
        class="h-1.5 w-full bg-gradient-to-r from-purple-600 to-purple-800"
      ></div>

      <div class="p-4 sm:p-8">
        <!-- Form Header -->
        <div class="text-center mb-6 sm:mb-8">
          <h2 class="text-2xl sm:text-3xl font-semibold text-purple-800 mb-2">
            เพิ่มข้อมูลผู้ใช้งาน
          </h2>
          <p class="text-purple-600 text-xs sm:text-sm">
            <span class="text-red-500 font-bold mr-1">**</span>
            ชื่อผู้ใช้จะถูกสร้างอัตโนมัติเป็น
            <span class="font-medium text-purple-800"
              >ชื่อจริง_อักษรแรกของนามสกุลภาษาอังกฤษ</span
            >
            เช่น <span class="italic text-gray-600">somsak_b</span>
            <span class="text-red-500 font-bold ml-1">**</span>
          </p>
          <p class="text-purple-600 text-xs sm:text-sm mt-2">
            <span class="text-red-500 font-bold mr-1">**</span>
            รหัสผ่านผู้ใช้จะถูกสร้างอัตโนมัติเป็น
            <span class="font-medium text-purple-800">123456</span>
            <span class="text-red-500 font-bold ml-1">**</span>
          </p>
        </div>

        <div>
          <label
            for="username"
            class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
            >Username
          </label>
          <input
            v-model="form.username"
            type="text"
            readonly
            class="w-full border border-gray-300 rounded-lg px-3 py-2 text-xs sm:text-sm focus:outline-none focus:ring-2 focus:ring-purple-500 transition duration-200"
          />
        </div>
        <div>
          <input v-model="form.password" type="hidden" />
        </div>

        <!-- User Form -->
        <form @submit.prevent="handleSubmit">
          <!-- Section Divider -->
          <div class="flex items-center my-4 sm:my-6">
            <div class="flex-grow h-px bg-gray-200"></div>
            <span class="px-3 text-xs sm:text-sm text-gray-500 font-medium"
              >ข้อมูลส่วนตัว</span
            >
            <div class="flex-grow h-px bg-gray-200"></div>
          </div>

          <!-- Name Fields - Thai -->
          <div
            class="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-5 mb-4 sm:mb-5"
          >
            <div>
              <label
                for="firstName"
                class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
              >
                ชื่อ<span class="text-red-500 ml-1">*</span>
              </label>
              <input
                id="firstName"
                v-model="form.firstName"
                type="text"
                class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 text-xs sm:text-sm"
                placeholder="กรอกชื่อ"
                required
              />
            </div>
            <div>
              <label
                for="lastName"
                class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
              >
                นามสกุล<span class="text-red-500 ml-1">*</span>
              </label>
              <input
                id="lastName"
                v-model="form.lastName"
                type="text"
                class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 text-xs sm:text-sm"
                placeholder="กรอกนามสกุล"
                required
              />
            </div>
          </div>

          <!-- Name Fields - English -->
          <div
            class="grid grid-cols-1 sm:grid-cols-2 gap-4 sm:gap-5 mb-4 sm:mb-5"
          >
            <div>
              <label
                for="firstNameEn"
                class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
              >
                ชื่อ (ภาษาอังกฤษ)<span class="text-red-500 ml-1">*</span>
              </label>
              <input
                id="firstNameEn"
                v-model="form.firstNameEn"
                type="text"
                class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 text-xs sm:text-sm"
                placeholder="กรอกชื่อภาษาอังกฤษ"
                required
              />
            </div>
            <div>
              <label
                for="lastNameEn"
                class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
              >
                นามสกุล (ภาษาอังกฤษ)<span class="text-red-500 ml-1">*</span>
              </label>
              <input
                id="lastNameEn"
                v-model="form.lastNameEn"
                type="text"
                class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 text-xs sm:text-sm"
                placeholder="กรอกนามสกุลภาษาอังกฤษ"
                required
              />
            </div>
          </div>

          <!-- Email Field -->
          <div class="mb-4 sm:mb-5">
            <label
              for="email"
              class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
            >
              อีเมล
            </label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 text-xs sm:text-sm"
              placeholder="กรอกอีเมล"
            />
          </div>

          <!-- Role Selection -->
          <div class="mb-6 sm:mb-8">
            <label
              for="role"
              class="block text-xs sm:text-sm font-medium text-gray-700 mb-1"
            >
              บทบาทในระบบ<span class="text-red-500 ml-1">*</span>
            </label>
            <select
              id="role"
              v-model="form.role"
              class="w-full px-3 py-2 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 appearance-none text-xs sm:text-sm"
              required
            >
              <option value="" disabled selected>-- เลือกบทบาท --</option>
              <option value="ADMIN">ผู้ดูแลระบบ (ADMIN)</option>
              <option value="MEMBER">ผู้ใช้งานทั่วไป (MEMBER)</option>
            </select>
          </div>

          <select
            id="branch"
            v-model="form.branchId"
            :required="form.role !== 'ADMIN'"
            class="w-full px-3 py-2 mb-4 rounded-lg border border-gray-300 focus:ring-2 focus:ring-purple-500 focus:border-purple-500 bg-gray-50 transition duration-200 appearance-none text-xs sm:text-sm"
          >
            <option value="" disabled>-- เลือกสาขาวิชา --</option>
            <option
              v-for="field in academicFields"
              :key="field.id"
              :value="field.id"
            >
              {{ field.name_th }}
            </option>
          </select>

          <!-- Submit Button -->
          <button
            type="submit"
            class="w-full py-2.5 sm:py-3 px-4 sm:px-6 bg-gradient-to-r from-purple-600 to-purple-800 text-white font-medium rounded-lg shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition duration-300 flex items-center justify-center text-xs sm:text-sm"
            :disabled="isSubmitting"
          >
            <svg
              v-if="isSubmitting"
              class="animate-spin -ml-1 mr-2 h-4 sm:h-5 w-4 sm:w-5 text-white"
              xmlns="http://www.w3.org/2000/svg"
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
            <span>{{
              isSubmitting ? "กำลังบันทึก..." : "บันทึกผู้ใช้งาน"
            }}</span>
          </button>

          <!-- Success Message -->
          <transition name="fade" mode="out-in">
            <div
              v-if="successMessage"
              class="mt-3 sm:mt-4 p-2 sm:p-3 bg-green-50 border border-green-200 rounded-lg flex items-center justify-center text-xs sm:text-sm text-green-700"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 sm:h-5 w-4 sm:w-5 mr-2 text-green-500"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fill-rule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                  clip-rule="evenodd"
                />
              </svg>
              {{ successMessage }}
            </div>
          </transition>

          <!-- Error Message -->
          <transition name="fade" mode="out-in">
            <div
              v-if="errorMessage"
              class="mt-3 sm:mt-4 p-2 sm:p-3 bg-red-50 border border-red-200 rounded-lg flex items-center justify-center text-xs sm:text-sm text-red-700"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 sm:h-5 w-4 sm:w-5 mr-2 text-red-500"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fill-rule="evenodd"
                  d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-1 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
                  clip-rule="evenodd"
                />
              </svg>
              {{ errorMessage }}
            </div>
          </transition>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";

import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();

const STORAGE_KEY = "user-form";
const baseURL = import.meta.env.VITE_API_BASE_URL;

// Form state
const form = ref({
  username: "",
  password: "",
  firstName: "",
  lastName: "",
  firstNameEn: "",
  lastNameEn: "",
  email: "",
  role: "",
  branchId: "",
});
const academicFields = ref([]);

onMounted(async () => {
  // โหลด academicFields
  try {
    const res = await fetch(`${baseURL}/api/fields`, {
      headers: authStore.getAuthHeader(),
    });
    academicFields.value = await res.json();
    console.log("📦 ได้ข้อมูลสาขา:", academicFields.value);
  } catch (err) {
    console.error("โหลดสาขาวิชาไม่สำเร็จ", err);
  }

  // โหลด localStorage
  const saved = localStorage.getItem(STORAGE_KEY);
  if (saved) {
    try {
      form.value = JSON.parse(saved);
    } catch (e) {
      console.error("Failed to parse saved form data", e);
    }
  }
});

// UI state
const isSubmitting = ref(false);
const successMessage = ref("");
const errorMessage = ref("");

// Generate username automatically
watch(
  () => [form.value.firstNameEn, form.value.lastNameEn],
  ([firstNameEn, lastNameEn]) => {
    if (firstNameEn && lastNameEn) {
      const fname = firstNameEn.trim().toLowerCase();
      const lnameInitial = lastNameEn.trim().charAt(0).toLowerCase();
      form.value.username = `${fname}_${lnameInitial}`;
    }
  }
);

// Update localStorage on form change
watch(
  form,
  (newVal) => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(newVal));
  },
  { deep: true }
);

// Form submission handler
const handleSubmit = async () => {
  // Reset messages
  successMessage.value = "";
  errorMessage.value = "";

  // Start loading state
  isSubmitting.value = true;

  try {
    form.value.password = "123456";
    // Simulate API call to backend
    // In production, replace with actual API call
    await new Promise((resolve) => setTimeout(resolve, 1500));

    if (form.value.role !== "ADMIN" && !form.value.branchId) {
      errorMessage.value = "กรุณาเลือกสาขาวิชา";
      isSubmitting.value = false;
      return;
    }

    const res = await fetch(`${baseURL}/api/users`, {
      method: "POST",
      headers: {
        ...authStore.getAuthHeader(),
        "Content-Type": "application/json",
      },
      body: JSON.stringify(form.value),
    });

    if (!res.ok) {
      throw new Error("เกิดข้อผิดพลาดจากเซิร์ฟเวอร์");
    }

    // Handle success
    successMessage.value = "เพิ่มผู้ใช้งานสำเร็จแล้ว!";

    // Reset form
    form.value = {
      username: "",
      password: "",
      firstName: "",
      lastName: "",
      firstNameEn: "",
      lastNameEn: "",
      email: "",
      role: "",
    };

    // Clear localStorage
    localStorage.removeItem(STORAGE_KEY);
  } catch (err) {
    // Handle error
    console.error("Error submitting form:", err);
    errorMessage.value = "เพิ่มข้อมูลไม่สำเร็จ กรุณาลองใหม่";
  } finally {
    // End loading state
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
/* Fade transition for messages */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

select {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
}
</style>
