<template>
  <div class="min-h-screen bg-purple-50 flex items-center justify-center p-4">
    <Header />

    <div class="w-full max-w-md bg-white rounded-xl shadow-lg overflow-hidden">
      <!-- หัวข้อด้านบนสุด -->
      <div class="bg-gradient-to-r from-purple-600 to-purple-800 px-6 py-4">
        <h2 class="text-xl font-bold text-white flex items-center">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6 mr-2"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"
            />
          </svg>
          เปลี่ยนรหัสผ่านใหม่
        </h2>
      </div>

      <!-- เนื้อหาฟอร์ม -->
      <div class="px-6 py-8">
        <form @submit.prevent="submitForm">
          <!-- Password Strength Indicator -->
          <div class="mb-6" v-if="newPassword">
            <p class="text-sm text-gray-600 mb-1">ความปลอดภัยของรหัสผ่าน</p>
            <div class="h-2 w-full bg-gray-200 rounded-full overflow-hidden">
              <div
                :class="{
                  'bg-red-500': passwordStrength === 'weak',
                  'bg-yellow-500': passwordStrength === 'medium',
                  'bg-green-500': passwordStrength === 'strong',
                }"
                :style="{ width: passwordStrengthPercent + '%' }"
                class="h-full transition-all duration-300"
              ></div>
            </div>
            <p
              class="text-xs mt-1"
              :class="{
                'text-red-500': passwordStrength === 'weak',
                'text-yellow-500': passwordStrength === 'medium',
                'text-green-500': passwordStrength === 'strong',
              }"
            >
              {{ passwordStrengthMessage }}
            </p>
          </div>

          <!-- รหัสผ่านใหม่ -->
          <div class="mb-6">
            <label
              for="new-password"
              class="block text-sm font-medium text-gray-700 mb-1"
              >รหัสผ่านใหม่</label
            >
            <div class="relative">
              <input
                id="new-password"
                v-model="newPassword"
                :type="showPassword ? 'text' : 'password'"
                class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                placeholder="กรอกรหัสผ่านใหม่"
                required
                @input="checkPasswordMatch"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-purple-700 focus:outline-none"
              >
                <svg
                  v-if="!showPassword"
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  />
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                  />
                </svg>
                <svg
                  v-else
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-5 w-5"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"
                  />
                </svg>
              </button>
            </div>
          </div>

          <!-- ยืนยันรหัสผ่าน -->
          <div class="mb-8">
            <label
              for="confirm-password"
              class="block text-sm font-medium text-gray-700 mb-1"
              >ยืนยันรหัสผ่าน</label
            >
            <div class="relative">
              <input
                id="confirm-password"
                v-model="confirmPassword"
                :type="showPassword ? 'text' : 'password'"
                class="w-full px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all"
                :class="{
                  'border-red-500 focus:ring-red-500':
                    !passwordsMatch && confirmPassword,
                }"
                placeholder="กรอกรหัสผ่านอีกครั้ง"
                required
                @input="checkPasswordMatch"
              />
            </div>
            <p
              v-if="!passwordsMatch && confirmPassword"
              class="text-xs text-red-500 mt-1"
            >
              รหัสผ่านไม่ตรงกัน กรุณาตรวจสอบอีกครั้ง
            </p>
          </div>

          <!-- ปุ่มบันทึก -->
          <p class="text-xs text-gray-500 mb-1">
            * คุณสามารถตั้งรหัสผ่านได้แม้ระบบจะเตือนว่าอ่อนแอ
          </p>
          <div class="flex flex-col sm:flex-row gap-3">
            <button
              type="submit"
              class="flex-1 bg-purple-600 text-white py-3 px-6 rounded-lg font-medium focus:outline-none hover:bg-purple-700 transition-colors duration-300 flex items-center justify-center disabled:opacity-50 disabled:cursor-not-allowed"
            >
              บันทึกรหัสผ่าน
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5 ml-1"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M5 13l4 4L19 7"
                />
              </svg>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <Footer class="" />
</template>
<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";

const authStore = useAuthStore();
const router = useRouter();
const baseURL = import.meta.env.VITE_API_BASE_URL;
const newPassword = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);

// ✅ ตรวจสอบว่ารหัสผ่านตรงกันไหม
const passwordsMatch = computed(() => {
  return newPassword.value === confirmPassword.value;
});

// ✅ ความปลอดภัยของรหัสผ่าน
const passwordStrength = computed(() => {
  const password = newPassword.value;
  if (!password) return "";

  const hasUppercase = /[A-Z]/.test(password);
  const hasLowercase = /[a-z]/.test(password);
  const hasNumbers = /\d/.test(password);
  const hasSpecialChars = /[!@#$%^&*(),.?":{}|<>]/.test(password);

  let strength = 0;
  if (password.length >= 8) strength++;
  if (password.length >= 12) strength++;
  if (hasUppercase) strength++;
  if (hasLowercase) strength++;
  if (hasNumbers) strength++;
  if (hasSpecialChars) strength++;

  if (strength <= 2) return "weak";
  if (strength <= 4) return "medium";
  return "strong";
});

const passwordStrengthPercent = computed(() => {
  switch (passwordStrength.value) {
    case "weak":
      return 33;
    case "medium":
      return 66;
    case "strong":
      return 100;
    default:
      return 0;
  }
});

const passwordStrengthMessage = computed(() => {
  switch (passwordStrength.value) {
    case "weak":
      return "รหัสผ่านอ่อนแอเกินไป";
    case "medium":
      return "รหัสผ่านความปลอดภัยปานกลาง";
    case "strong":
      return "รหัสผ่านความปลอดภัยสูง";
    default:
      return "";
  }
});

// ✅ ส่งข้อมูล
const submitForm = async () => {
  if (!passwordsMatch.value) {
    alert("รหัสผ่านไม่ตรงกัน");
    return;
  }

  try {
    const res = await fetch(`${baseURL}/api/auth/change-password`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ newPassword: newPassword.value }),
    });

    if (!res.ok) {
      const err = await res.text();
      alert("❌ เปลี่ยนรหัสไม่สำเร็จ: " + err);
      return;
    }

    // ✅ ดึงข้อมูลผู้ใช้ใหม่
    const meRes = await fetch(`${baseURL}/api/auth/me`, {
      credentials: "include",
    });

    if (meRes.ok) {
      const meData = await meRes.json();
      authStore.login(meData.username, meData.role, meData.firstName || "", []);
    }

    alert("✅ เปลี่ยนรหัสผ่านเรียบร้อยแล้ว");
    router.push("/");
  } catch (err) {
    console.error("Error:", err);
    alert("เกิดข้อผิดพลาด กรุณาลองใหม่");
  }
};
</script>
