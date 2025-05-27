<template>
  <!-- กล่องพื้นหลัง -->
  <div
    class="scroll-mt-24 min-h-screen flex flex-col justify-between bg-gradient-to-br from-white to-purple-50"
    :style="{
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
    <!-- กล่องฟอร์ม login -->
    <div class="mt-auto mb-auto flex items-center justify-center px-4 py-12">
      <div class="w-full max-w-md p-8 bg-white rounded-2xl shadow-lg">
        <div class="flex justify-center">
          <Logo :size="'w-28 h-28'" />
        </div>

        <h2 class="text-xl font-bold text-center text-purple-700 mb-6 mt-4">
          Sign In To Book Request
        </h2>

        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <label class="block mb-1 text-gray-700 text-sm">Username</label>
            <input
              type="text"
              v-model.trim="formData.username"
              autocomplete="username"
              autocapitalize="none"
              autocorrect="off"
              spellcheck="false"
              required
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your username"
              @input="sanitizeInput"
            />
          </div>
          <div>
            <label class="block mb-1 text-gray-700 text-sm">Password</label>
            <input
              type="password"
              v-model="formData.password"
              autocomplete="current-password"
              required
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your password"
            />
          </div>
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full py-3 bg-purple-600 text-white rounded-lg font-semibold hover:bg-purple-700 transition duration-200 disabled:opacity-50"
          >
            {{ isLoading ? "Signing In..." : "Sign In" }}
          </button>
          <p class="text-center text-gray-500 text-sm pt-2">
            <a href="#" class="text-purple-600 hover:underline"
              >Forgot Password</a
            >
          </p>
        </form>
      </div>
    </div>

    <!-- Footer -->
    <Footer class="mt-8" />
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import Logo from "@/component/Logo.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
const baseURL = import.meta.env.VITE_API_BASE_URL;

const router = useRouter();
const authStore = useAuthStore();
const isLoading = ref(false);

const formData = reactive({
  username: "",
  password: "",
});

// Sanitize input to remove unwanted characters
function sanitizeInput(event) {
  // Remove any special characters that might cause validation issues
  const sanitized = event.target.value
    .replace(/[^\w@._-]/g, "") // Keep only alphanumeric, @, ., _, -
    .toLowerCase(); // Convert to lowercase

  formData.username = sanitized;
}

async function handleSubmit() {
  if (isLoading.value) return;

  try {
    isLoading.value = true;

    // Extra validation before sending
    if (!formData.username.trim() || !formData.password.trim()) {
      alert("Please fill in all fields");
      return;
    }

    // Prepare clean data
    const loginData = {
      username: formData.username.trim(),
      password: formData.password,
    };

    console.log("Sending login data:", {
      username: loginData.username,
      password: "[HIDDEN]",
    });

    const response = await fetch(`${baseURL}/api/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(loginData),
      credentials: "include",
      mode: "cors",
    });

    console.log("Response status:", response.status);
    console.log(
      "Response headers:",
      Object.fromEntries(response.headers.entries())
    );

    if (response.ok) {
      const loginResult = await response.text();
      console.log("Login response:", loginResult);

      // รอสักครู่ให้ cookie ติดตั้ง
      await new Promise((resolve) => setTimeout(resolve, 100));

      const res = await fetch(`${baseURL}/api/auth/me`, {
        credentials: "include",
        mode: "cors",
      });

      if (!res.ok) {
        // ถ้า /me ล้มเหลว อาจเป็นปัญหา cookie บน iOS
        if (res.status === 401 || res.status === 403) {
          alert(
            "เข้าสู่ระบบสำเร็จ แต่มีปัญหาในการจดจำ session กรุณาลองใหม่ หรือใช้ browser อื่น"
          );
          return;
        }
        throw new Error(`Failed to get user info: ${res.status}`);
      }

      const data = await res.json();
      console.log("📦 /me response:", data);
      authStore.login(data.username, data.role);

      // 🔄 ดึง favBooks ของผู้ใช้
      try {
        const favRes = await fetch(`${baseURL}/api/auth/favorites`, {
          method: "GET",
          credentials: "include",
          mode: "cors",
        });
        if (favRes.ok) {
          authStore.setFavBooks(await favRes.json());
        }
      } catch (favError) {
        console.warn("Could not load favorites:", favError);
      }

      if (data.role === "ADMIN") {
        router.push("/admin");
      } else if (data.role === "MEMBER") {
        router.push("/");
      } else {
        alert("ไม่สามารถเข้าระบบได้: บทบาทไม่ถูกต้อง");
      }
    } else {
      const errorText = await response.text();
      console.error("Login failed:", errorText);
      alert("❌ Login ล้มเหลว: " + errorText);
    }
  } catch (err) {
    console.error("Login error:", err);
    const errorMessage = err instanceof Error ? err.message : String(err);
    alert("❌ เกิดข้อผิดพลาด: " + errorMessage);
  } finally {
    isLoading.value = false;
  }
}
</script>

<style>
@import "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css";

html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  height: 100vh;
}
</style>
