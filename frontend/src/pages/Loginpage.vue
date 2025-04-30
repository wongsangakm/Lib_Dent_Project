<template>
  <div class="w-full min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-white to-purple-50">
    <div class="w-full max-w-md p-8 bg-white rounded-2xl shadow-lg">
      <Logo />
      <h2 class="text-2xl font-bold text-center text-purple-700 mb-6 mt-4">
        Sign In To Book Request
      </h2>

      <form @submit.prevent="handleSubmit" class="space-y-6">
        <div>
          <label class="block mb-2 text-gray-700 text-sm">Email Address</label>
          <input
            type="email"
            v-model="formData.email"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-400"
            placeholder="Enter your email"
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
        <button
          type="submit"
          class="w-full py-3 bg-purple-500 text-white rounded-lg font-semibold hover:bg-purple-600 transition duration-200"
        >
          Sign In
        </button>
        <p class="text-center text-gray-500 text-sm">
          Don’t have an account?
          <a href="#" class="text-purple-600 hover:underline">Sign Up</a>
        </p>
      </form>
    </div>

    <Footer class="mt-12" />
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import Logo from '@/components/Logo.vue';
import Footer from '@/components/Footer.vue';

// Reactive object for form data to be sent to backend
const formData = reactive({
  email: '',
  password: ''
});

// Submit handler for backend integration
const handleSubmit = async () => {
  try {
    // Example: Prepare data for backend API call
    const payload = {
      email: formData.email,
      password: formData.password
    };

    // Placeholder for backend API call
    // await fetch('your-backend-api-endpoint', {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: JSON.stringify(payload)
    // });

    console.log('Form submitted:', payload);
    // Add your backend API call logic here
  } catch (error) {
    console.error('Submission error:', error);
    // Handle errors (e.g., show error message to user)
  }
};
</script>

<style>
@import 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css';

html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

#app {
  height: 100vh;
}
</style>