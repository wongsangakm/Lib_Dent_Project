<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center pt-[80px] bg-cover bg-center"
    :style="{ backgroundImage: `url(${bgImage})` }"
  >
    <Header />
    <div class="w-full">
      <div
        class="absolute top-[500px] bottom-0 left-0 right-0 bg-repeat-y z-0"
        style="background-image: url('/images/bg_repeat.png')"
      ></div>
      <div class="pt-8 md:pt-8">
        <!-- ปุ่ม view history -->
        <div class="max-w-xl mx-auto text-right mb-4">
          <router-link
            to="/favbooks"
            class="inline-block bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 transition"
          >
            🧾 View Request History
          </router-link>
        </div>
      </div>

      <div class="pt-8 md:pt-8">
        <div
          class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow-md relative z-10"
        >
          <h1 class="text-2xl font-bold text-purple-700 mb-4 text-center">
            Additional Book Request
          </h1>

          <form @submit.prevent="submitRequest" class="space-y-4">
            <label class="block">
              <span class="text-gray-700">Book Title</span
              ><span class="text-red-500">*</span>
              <input
                v-model="request.bookTitle"
                type="text"
                class="form-input"
              />
              <p v-if="bookTitleError" class="text-red-500 text-sm mt-1">
                {{ bookTitleError }}
              </p>
            </label>
            <label class="block">
              <span class="text-gray-700">Author</span
              ><span class="text-red-500">*</span>
              <input v-model="request.author" type="text" class="form-input" />
              <p v-if="authorError" class="text-red-500 text-sm mt-1">
                {{ authorError }}
              </p>
            </label>

            <label class="block">
              <span class="text-gray-700">Publisher</span
              ><span class="text-red-500">*</span>
              <input
                v-model="request.publisher"
                type="text"
                class="form-input"
              />
              <p v-if="publisherError" class="text-red-500 text-sm mt-1">
                {{ publisherError }}
              </p>
            </label>

            <div class="flex flex-row gap-8">
              <label class="block flex-1">
                <span class="text-gray-700">ISBN</span
                ><span class="text-red-500">*</span>
                <input
                  v-model="request.isbn"
                  type="text"
                  inputmode="numeric"
                  pattern="\d*"
                  class="form-input"
                />
                <p v-if="isbnError" class="text-red-500 text-sm mt-1">
                  {{ isbnError }}
                </p>
              </label>
              <label class="block flex-1">
                <span class="text-gray-700">Year</span
                ><span class="text-red-500">*</span>
                <input
                  v-model="request.year"
                  type="text"
                  inputmode="numeric"
                  pattern="\d*"
                  class="form-input"
                />
                <p v-if="yearError" class="text-red-500 text-sm mt-1">
                  {{ yearError }}
                </p>
              </label>
            </div>

            <label class="block">
              <span class="text-gray-700">Price</span
              ><span class="text-red-500">*</span>
              <input
                v-model="request.price"
                type="text"
                inputmode="decimal"
                class="form-input"
              />
              <p v-if="priceError" class="text-red-500 text-sm mt-1">
                {{ priceError }}
              </p>
            </label>

            <label class="block">
              <span class="text-gray-700"
                >Description (หรือ Link สำหรับดูเพิ่มเติม)</span
              >
              <textarea
                v-model="request.description"
                class="w-full border rounded px-3 py-2 mt-1 resize-none"
                rows="4"
              ></textarea>
            </label>

            <label class="block">
              <span class="text-gray-700">Reason / Notes</span
              ><span class="text-red-500">*</span>
              <textarea
                v-model="request.reason"
                class="w-full border rounded px-3 py-2 mt-1 resize-none"
                rows="4"
                placeholder="Why do you need this book?"
              ></textarea>
              <p v-if="reasonError" class="text-red-500 text-sm mt-1">
                {{ reasonError }}
              </p>
            </label>

            <div class="text-right">
              <div class="text-right flex justify-end">
                <button
                  type="submit"
                  :disabled="isLoading"
                  class="bg-purple-600 text-white px-6 py-2 rounded transition flex items-center gap-2 ml-auto"
                  :class="{ 'opacity-60 cursor-not-allowed': isLoading }"
                >
                  <svg
                    v-if="isLoading"
                    class="animate-spin h-5 w-5 text-white"
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
                    />
                    <path
                      class="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8v4l3.5-3.5L12 0v4a8 8 0 000 16v4l3.5-3.5L12 20v-4a8 8 0 01-8-8z"
                    />
                  </svg>
                  <span>{{
                    isLoading ? "Submitting..." : "Submit Request"
                  }}</span>
                </button>
              </div>
              <p
                v-if="globalError"
                class="text-red-600 text-sm mt-2 whitespace-pre-line"
              >
                {{ globalError }}
              </p>
            </div>
          </form>
        </div>
      </div>
      <Footer class="mt-8 w-full" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import axios from "axios";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
import { useAuthStore } from "@/stores/useAuthStore";
import { ElMessage } from "element-plus";
const authStore = useAuthStore();
const baseURL = import.meta.env.VITE_API_BASE_URL;

const isbnError = ref("");
const yearError = ref("");
const priceError = ref("");
const bookTitleError = ref("");
const authorError = ref("");
const publisherError = ref("");
const reasonError = ref("");
const globalError = ref("");
const isLoading = ref(false);

const request = ref({
  bookTitle: "",
  author: "",
  publisher: "",
  isbn: "",
  year: "",
  price: "",
  description: "",
  reason: "",
});

// Watch for field errors
watch(
  () => request.value.isbn,
  (val) => {
    isbnError.value = /^\d*$/.test(val) ? "" : "❌ ISBN must be an integer.";
  }
);

watch(
  () => request.value.year,
  (val) => {
    yearError.value = /^\d*$/.test(val) ? "" : "❌ Year must be an integer.";
  }
);

watch(
  () => request.value.price,
  (val) => {
    priceError.value = /^\d*\.?\d*$/.test(val)
      ? ""
      : "❌ Price must be a valid number (float).";
  }
);

watch(
  () => request.value.bookTitle,
  (val) => {
    bookTitleError.value =
      val.trim() === "" ? "❌ Book Title is required." : "";
  }
);

watch(
  () => request.value.author,
  (val) => {
    authorError.value = val.trim() === "" ? "❌ Author is required." : "";
  }
);

watch(
  () => request.value.publisher,
  (val) => {
    publisherError.value = val.trim() === "" ? "❌ Publisher is required." : "";
  }
);

watch(
  () => request.value.reason,
  (val) => {
    reasonError.value = val.trim() === "" ? "❌ Reason is required." : "";
  }
);

const submitRequest = async () => {
  globalError.value = "";

  const trimmed = {
    bookTitle: request.value.bookTitle.trim(),
    author: request.value.author.trim(),
    publisher: request.value.publisher.trim(),
    isbn: request.value.isbn.trim(),
    year: request.value.year.trim(),
    price: request.value.price.trim(),
    description: request.value.description.trim(),
    reason: request.value.reason.trim(),
  };

  const missingFields = [];
  if (!trimmed.bookTitle) missingFields.push("Book Title");
  if (!trimmed.author) missingFields.push("Author");
  if (!trimmed.publisher) missingFields.push("Publisher");
  if (!trimmed.isbn) missingFields.push("ISBN");
  if (!trimmed.year) missingFields.push("Year");
  if (!trimmed.price) missingFields.push("Price");
  if (!trimmed.reason) missingFields.push("Reason");

  if (missingFields.length > 0) {
    globalError.value =
      "❌ Please fill in the following fields:\n- " +
      missingFields.join("\n- ");
    return;
  }

  if (!/^\d+$/.test(trimmed.isbn)) {
    globalError.value = "❌ ISBN must be an integer.";
    return;
  }

  if (!/^\d+$/.test(trimmed.year)) {
    globalError.value = "❌ Year must be an integer.";
    return;
  }

  if (!/^\d+(\.\d+)?$/.test(trimmed.price)) {
    globalError.value = "❌ Price must be a valid number (float).";
    return;
  }

  isLoading.value = true;

  try {
    const payload = {
      ...trimmed,
      isbn: parseInt(trimmed.isbn),
      year: parseInt(trimmed.year),
      price: parseFloat(trimmed.price),
    };

    await axios.post(`${baseURL}/api/requests`, payload, {
      headers: authStore.getAuthHeader(),
    });

    ElMessage({
    type: "success",
    message: "✅ Request submitted successfully!",
  });

  globalError.value = "";
  request.value = {
    bookTitle: "",
    author: "",
    publisher: "",
    isbn: "",
    year: "",
    price: "",
    description: "",
    reason: "",
  };

} catch (err) {
  if (err.response?.status === 400 && err.response?.data?.errors) {
    const messages = err.response.data.errors
      .map((e) => `❌ ${e.field}: ${e.defaultMessage}`)
      .join("\n");

    ElMessage({
      type: "error",
      message: messages,
      duration: 5000, // แสดงนานหน่อยกรณี error หลายบรรทัด
      showClose: true
    });

  } else {
    ElMessage({
      type: "error",
      message:
        "❌ Failed to submit request: " +
        (err.response?.data?.message ||
          JSON.stringify(err.response?.data) ||
          err.message),
      duration: 5000,
      showClose: true
    });
  }

} finally {
  isLoading.value = false;
}

};
</script>

<style scoped>
.form-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  margin-top: 0.25rem;
}
</style>
