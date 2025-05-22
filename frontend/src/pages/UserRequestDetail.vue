<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center"
    :style="{
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
    <Header />
  <div class="container mx-auto px-4 pt-28 w-full max-w-4xl">
    <div class="max-w-3xl mx-auto bg-white shadow-md rounded-xl p-6 border">
      <h1 class="text-xl sm:text-2xl font-bold text-purple-700 mb-6 text-center">
        Your Book Request Details
      </h1>

      <div v-if="loading" class="text-center text-gray-500">Loading...</div>
      <div v-else-if="!request" class="text-center text-red-500">Request not found.</div>
      <div v-else class="space-y-4">
        <div><label>Book Title:</label> <div>{{ request.bookTitle }}</div></div>
        <div><label>Author:</label> <div>{{ request.author || '-' }}</div></div>
        <div><label>Publisher:</label> <div>{{ request.publisher || '-' }}</div></div>
        <div><label>ISBN:</label> <div>{{ request.isbn || '-' }}</div></div>
        <div><label>Year:</label> <div>{{ request.year || '-' }}</div></div>
        <div><label>Price:</label> <div>{{ request.price || '-' }} ฿</div></div>
        <div><label>Reason:</label> <div class="whitespace-pre-line">{{ request.reason || '-' }}</div></div>
        <div v-if="request"><label>Status:</label><div :class="[
            request.status === 'PENDING' ? 'text-yellow-500 font-semibold' :
            request.status === 'REJECTED' ? 'text-red-600 font-semibold' :
            request.status === 'in_shelf' ? 'text-green-600 font-semibold' :
            request.status === 'ordered' ? 'text-blue-600 font-semibold' :
            request.status === 'popular_request' ? 'text-purple-600 font-semibold' :
            '']">{{ displayStatus(request.status) }}
          </div></div>
        <div><label>Requested Date:</label><div>{{ formatDate(request.requestDate) }}</div></div>
      </div>

      <div class="mt-6 flex justify-center">
        <router-link
          to="/favbooks"
          class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium px-5 py-2 rounded-full transition"
        >
          Back to History
        </router-link>
      </div>
    </div>
  </div>
  <Footer class="mt-8" />
</div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import Header from "@/component/Header.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";

const route = useRoute();
const router = useRouter();
const requestId = route.params.id;

const request = ref(null);
const loading = ref(true);

const loadRequest = async () => {
  try {
    const res = await fetch(`http://localhost:8080/api/requests/${requestId}`, {
      credentials: 'include'
    });
    if (!res.ok) throw new Error("Request not found");
    request.value = await res.json();
  } catch (err) {
    console.error("❌ Failed to load request:", err);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleDateString("th-TH", {
    year: "numeric",
    month: "long",
    day: "numeric"
  });
};

const isApprovedStatus = (status) => {
  return ['APPROVED', 'in_shelf', 'ordered', 'popular_request'].includes(status);
};

const displayStatus = (status) => {
  switch (status) {
    case 'in_shelf':
      return 'มีในชั้นหนังสือแล้ว';
    case 'ordered':
      return 'กำลังสั่งซื้อ';
    case 'popular_request':
      return 'กำลังพิจารณาจัดซื้อ';
    case 'PENDING':
      return 'รอการอนุมัติ';
    case 'REJECTED':
      return 'ถูกปฏิเสธ';
    default:
      return status;
  }
};


onMounted(loadRequest);
</script>

<style scoped>
label {
  font-weight: 600;
  color: #4b5563;
  display: block;
  margin-bottom: 2px;
}
</style>
