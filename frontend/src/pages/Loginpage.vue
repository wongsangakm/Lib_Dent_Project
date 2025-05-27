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
              v-model="formData.username"
              autocomplete="off"
              required
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your username"
            />
          </div>
          <div>
            <label class="block mb-1 text-gray-700 text-sm">Password</label>
            <input
              type="password"
              v-model="formData.password"
              autocomplete="off"
              required
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your password"
            />
          </div>
          <button
            type="submit"
            class="w-full py-3 bg-purple-600 text-white rounded-lg font-semibold hover:bg-purple-700 transition duration-200"
          >
            Sign In
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
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore";
import Logo from "@/component/Logo.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
const baseURL = import.meta.env.VITE_API_BASE_URL;

const router = useRouter();
const authStore = useAuthStore();

const formData = reactive({
  username: "",
  password: "",
});
async function handleSubmit() {
  try {
    const response = await fetch(`${baseURL}/api/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: formData.username,
        password: formData.password,
      }),
      credentials: "include",
    });

    if (response.ok) {
      await response.text();
      const res = await fetch(`${baseURL}/api/auth/me`, {
        credentials: "include",
      });
      const data = await res.json();
      console.log("📦 /me response:", data);
      authStore.login(data.username, data.role);

      // 🔄 ดึง favBooks ของผู้ใช้
      const favRes = await fetch(`${baseURL}/api/auth/favorites`, {
        method: "GET",
        credentials: "include",
      });
      authStore.setFavBooks(await favRes.json());

      if (data.role === "ADMIN") {
        router.push("/admin");
      } else if (data.role === "MEMBER") {
        router.push("/");
      } else {
        alert("ไม่สามารถเข้าระบบได้: บทบาทไม่ถูกต้อง");
      }
    } else {
      const text = await response.text();
      alert("❌ Login ล้มเหลว: " + text);
    }
  } catch (err) {
    const errorMessage =
      err instanceof Error ? err.message : JSON.stringify(err);
    alert("❌ เกิดข้อผิดพลาด: " + errorMessage);
    // 👇 แสดง log บนหน้าเว็บชั่วคราว
    document.body.innerHTML += `<pre style="color:red;">[DEBUG LOG]<br>${errorMessage}</pre>`;
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
