<template>
  <!-- กล่องพื้นหลัง -->
  <div
    class="min-h-screen flex flex-col bg-gray-100"
    :style="{
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
    <!-- กล่องฟอร์ม login -->
    <div class="flex-grow flex justify-center items-center px-10 pt-10 pb-10">
      <div class="w-full max-w-md p-8 bg-white rounded-2xl shadow-lg">
        <div class="flex justify-center">
          <Logo :size="'w-28 h-28'" />
        </div>

        <h2 class="text-xl font-bold text-center text-purple-700 mb-6 mt-4">
          Sign In To Book Request
        </h2>

        <form @submit.prevent="handleSubmit" class="space-y-2">
          <div>
            <label class="block mb-2 text-gray-700 text-sm">Username</label>
            <input
              type="text"
              v-model="formData.username"
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your username"
            />
          </div>
          <div>
            <label class="block mb-2 text-gray-700 text-sm">Password</label>
            <input
              type="password"
              v-model="formData.password"
              class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
              placeholder="Enter your password"
            />
          </div>
          <div class="pt-4">
            <button
              type="submit"
              class="w-full py-3 bg-purple-500 text-white rounded-lg font-semibold hover:bg-purple-600 transition duration-200"
            >
              Sign In
            </button>
          </div>
          <p class="text-center text-gray-500 text-sm pt-2">
            <a href="#" class="text-purple-600 hover:underline">Forgot Password</a>
          </p>
        </form>
      </div>
    </div>

    <!-- Footer ด้านล่าง -->
    <Footer class="mt-8" />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import Logo from "@/component/Logo.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";

const formData = reactive({
  username: "",
  password: "",
});

const handleSubmit = async () => {
  if (!formData.username || !formData.password) {
    alert("กรุณากรอกชื่อผู้ใช้และรหัสผ่าน");
    return;
  }

  const payload = {
    username: formData.username,
    password: formData.password,
  };

  try {
    const response = await fetch("https://your-backend-api.com/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    const result = await response.json();

    if (response.ok) {
      // Login สำเร็จ
      console.log("Login success:", result);
      // localStorage.setItem("token", result.token); // ถ้ามี token
      // redirect เช่น: window.location.href = "/dashboard";
    } else {
      alert(result.message || "Login failed");
    }
  } catch (error) {
    console.error("Submission error:", error);
    alert("เกิดข้อผิดพลาดในการเชื่อมต่อ");
  }
};
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
