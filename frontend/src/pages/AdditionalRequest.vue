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
  <div class="min-h-screen bg-gray-50 py-12 px-4">
    <div class="max-w-xl mx-auto bg-white p-6 rounded-xl shadow-md relative z-10">
      <h1 class="text-2xl font-bold text-purple-700 mb-6 text-center">
        Additional Book Request
      </h1>

      <form @submit.prevent="submitRequest" class="space-y-4">
        <label class="block">
          <span class="text-gray-700">Book Title</span>
          <input v-model="request.bookTitle" type="text" class="form-input" />
          <p v-if="bookTitleError" class="text-red-500 text-sm mt-1">{{ bookTitleError }}</p>
        </label>
        <label class="block">
          <span class="text-gray-700">Author</span>
          <input v-model="request.author" type="text" class="form-input" />
          <p v-if="authorError" class="text-red-500 text-sm mt-1">{{ authorError }}</p>
        </label>

        <label class="block">
          <span class="text-gray-700">Publisher</span>
          <input v-model="request.publisher" type="text" class="form-input" />
          <p v-if="publisherError" class="text-red-500 text-sm mt-1">{{ publisherError }}</p>
        </label>

        <div class="flex felx-row gap-8">
          <label class="block">
            <span class="text-gray-700">ISBN</span>
            <input v-model="request.isbn" type="text" class="form-input" />
            <p v-if="isbnError" class="text-red-500 text-sm mt-1">{{ isbnError }}</p>
          </label>
          <label class="block">
            <span class="text-gray-700">Year</span>
            <input v-model="request.year" type="text" class="form-input" />
            <p v-if="yearError" class="text-red-500 text-sm mt-1">{{ yearError }}</p>
          </label>
        </div>
        <label class="block">
          <span class="text-gray-700">Price</span>
          <input v-model="request.price" type="text" class="form-input" />
          <p v-if="priceError" class="text-red-500 text-sm mt-1">{{ priceError }}</p>
        </label>

        <label class="block">
          <span class="text-gray-700">Description (หรือ Link สำหรับดูเพิ่มเติม)</span>
          <textarea
            v-model="request.description"
            class="w-full border rounded px-3 py-2 mt-1 resize-none"
            rows="4"
          ></textarea>
        </label>
        <label class="block">
          <span class="text-gray-700">Reason / Notes</span>
          <textarea
            v-model="request.reason"
            class="w-full border rounded px-3 py-2 mt-1 resize-none"
            rows="4"
            placeholder="Why do you need this book?"
          ></textarea>
          <p v-if="reasonError" class="text-red-500 text-sm mt-1">{{ reasonError }}</p>
        </label>

        <div class="text-right">
          <button
            type="submit"
            class="bg-purple-600 text-white px-6 py-2 rounded hover:bg-purple-700 transition"
          >
            Submit Request
          </button>
          <p v-if="error" class="text-red-500 font-medium">{{ error }}</p>

        </div>
      </form>
    </div>
  </div>
  <Footer class="mt-8 w-full"  />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import axios from 'axios'
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";

const isbnError = ref('')
const yearError = ref('')
const priceError = ref('')
const bookTitleError = ref('')
const authorError = ref('')
const publisherError = ref('')
const reasonError = ref('')
const error = ref('')

const request = ref({
  bookTitle: '',
  author: '',
  publisher: '',
  isbn: '',
  year: '',
  price: '',
  description: '',
  reason: '',
})

watch(() => request.value.isbn, (val) => {
  if (!/^\d*$/.test(val)) {
    isbnError.value = '❌ ISBN must be an integer.'
  } else {
    isbnError.value = ''
  }
})

watch(() => request.value.year, (val) => {
  if (!/^\d*$/.test(val)) {
    yearError.value = '❌ Year must be an integer.'
  } else {
    yearError.value = ''
  }
})

watch(() => request.value.price, (val) => {
  if (!/^\d*\.?\d*$/.test(val)) {
    priceError.value = '❌ Price must be a valid number (float).'
  } else {
    priceError.value = ''
  }
})

watch(() => request.value.bookTitle, (val) => {
  bookTitleError.value = val.trim() === '' ? '❌ Book Title is required.' : ''
})

watch(() => request.value.author, (val) => {
  authorError.value = val.trim() === '' ? '❌ Author is required.' : ''
})

watch(() => request.value.publisher, (val) => {
  publisherError.value = val.trim() === '' ? '❌ Publisher is required.' : ''
})

watch(() => request.value.reason, (val) => {
  reasonError.value = val.trim() === '' ? '❌ Reason is required.' : ''
})

const submitRequest = async () => {
  // เช็คว่างครบไหม
  if (
    !request.value.bookTitle ||
    !request.value.author ||
    !request.value.publisher ||
    !request.value.isbn ||
    !request.value.year ||
    !request.value.price ||
    !request.value.reason
  ) {
    error.value = '❌ Please fill in all required fields.'
    return
  }

  // เช็ค error จาก watcher
  if (isbnError.value || yearError.value || priceError.value) {
    error.value = '❌ Please fix input errors before submitting.'
    return
  }

  // ถ้าไม่ผ่าน regex กรณีไม่เคยกรอกข้อมูล (อีกชั้น)
  if (!/^\d+$/.test(request.value.isbn)) {
    error.value = '❌ ISBN must be an integer.'
    return
  }

  if (!/^\d+$/.test(request.value.year)) {
    error.value = '❌ Year must be an integer.'
    return
  }

  if (!/^\d+(\.\d+)?$/.test(request.value.price)) {
    error.value = '❌ Price must be a valid number (float).'
    return
  }

  try {
    const payload = {
      ...request.value,
      isbn: parseInt(request.value.isbn),
      year: parseInt(request.value.year),
      price: parseFloat(request.value.price)
    }
    await axios.post('http://localhost:8080/api/requests', payload)
    alert('✅ Request submitted successfully!')
    error.value = ''
    // reset form
    request.value = {
      bookTitle: '',
      author: '',
      publisher: '',
      isbn: '',
      year: '',
      price: '',
      description: '',
      reason: '',
    }
  }  catch (err) {
  if (err.response?.status === 400 && err.response?.data?.errors) {
  // ถ้ามี error ราย field
  const messages = err.response.data.errors.map(e => `❌ ${e.field}: ${e.defaultMessage}`).join('\n');
  error.value = messages;
} else {
  // fallback ทั่วไป
  error.value =
    '❌ Failed to submit request: ' +
    (err.response?.data?.message || JSON.stringify(err.response?.data) || err.message);
}

}
}
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
