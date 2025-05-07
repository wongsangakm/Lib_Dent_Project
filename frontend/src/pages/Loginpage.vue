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
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your username"
            />
          </div>
          <div>
            <label class="block mb-1 text-gray-700 text-sm">Password</label>
            <input
              type="password"
              v-model="formData.password"
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
            <a href="#" class="text-purple-600 hover:underline">Forgot Password</a>
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
import { useAuthStore } from "@/stores/authStore";
import Logo from "@/component/Logo.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";

const router = useRouter();
const authStore = useAuthStore();

const formData = reactive({
  username: "",
  password: ""
});

async function handleSubmit() {
  try {
    const form = new URLSearchParams();
    form.append("username", formData.username);
    form.append("password", formData.password);

    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: form,
      credentials: "include" // ⚠️ สำคัญมาก: บอกให้ fetch ส่ง cookie (JSESSIONID)
    });

    if (response.ok) {
      // 🔄 ดึงข้อมูลผู้ใช้หลังจาก login สำเร็จ
      const res = await fetch("http://localhost:8080/api/auth/me", {
        credentials: "include"
      });
      const data = await res.json();
      authStore.login(data.username, data.role);

      // 🧭 เปลี่ยนเส้นทาง
      if (data.role === "ADMIN") {
        router.push("/system");
      } else {
        router.push("/Home");
      }
    } else {
      alert("❌ Username หรือ Password ไม่ถูกต้อง");
    }
  } catch (err) {
    alert("❌ เกิดข้อผิดพลาด: " + err.message);
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
