<template>
  <div class="min-h-screen px-4 py-6 sm:px-8 sm:py-10 bg-gradient-to-br from-white to-purple-50">
    <div class="max-w-3xl mx-auto bg-white shadow-md rounded-xl p-6 border">
      <h1 class="text-xl sm:text-2xl font-bold text-purple-700 mb-6 text-center">
        Additional Request Details
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
        
        <div>
          <label>Status:</label>
          <div :class="[
            request.status === 'PENDING' ? 'text-yellow-500 font-semibold' :
            request.status === 'REJECTED' ? 'text-red-600 font-semibold' :
            request.status === 'in_shelf' ? 'text-green-600 font-semibold' :
            request.status === 'ordered' ? 'text-blue-600 font-semibold' :
            request.status === 'popular_request' ? 'text-purple-600 font-semibold' :
            ''
          ]">{{ displayStatus(request.status) }}</div>
        </div>

        <div><label>Requested By:</label> <div>{{ request.requestedBy || 'N/A' }}</div></div>
        <div><label>Requested Date:</label><div>{{ formatDate(request.requestDate) }}</div></div>
      </div>

      <!-- ✅ ปุ่ม Approve / Update Status -->
      <div class="mt-6 flex flex-wrap justify-center gap-4" v-if="request && request.status !== 'REJECTED'">
        <button
          @click="showApproveDialog = true"
          class="bg-green-600 hover:bg-green-700 text-white font-medium px-5 py-2 rounded-full transition"
        >
          {{ request.status === 'PENDING' ? 'Approve Request' : 'Update Status' }}
        </button>

        <button
          v-if="request.status === 'PENDING'"
          @click="rejectRequest"
          class="bg-red-600 hover:bg-red-700 text-white font-medium px-5 py-2 rounded-full transition"
        >
          Reject Request
        </button>
      </div>

      <!-- ปุ่มกลับ -->
      <div class="mt-6 flex flex-wrap justify-center gap-4">
        <router-link
          to="/admin/request-table"
          class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium px-5 py-2 rounded-full transition"
        >
          Back
        </router-link>
      </div>
    </div>

    <!-- ✅ กล่องยืนยัน Approve / Update -->
    <div v-if="showApproveDialog" class="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-30">
      <div class="bg-white border rounded-lg shadow-lg p-6 w-full max-w-md">
        <h2 class="text-lg font-semibold text-gray-800 mb-4">
          {{ request.status === 'PENDING' ? 'Are you sure you want to approve this request?' : 'Update the request status:' }}
        </h2>

        <select
          v-model="finalStatus"
          class="w-full border rounded px-3 py-2 mb-4"
        >
          <option disabled value="">-- กรุณาเลือกสถานะ --</option>
          <option value="in_shelf">มีในชั้นหนังสือแล้ว</option>
          <option value="ordered">กำลังสั่งซื้อ</option>
          <option value="popular_request">กำลังพิจารณาจัดซื้อ</option>
        </select>

        <div class="flex justify-end gap-3">
          <button @click="showApproveDialog = false" class="px-4 py-2 bg-gray-300 hover:bg-gray-400 rounded">ยกเลิก</button>
          <button
            @click="confirmApprove"
            :disabled="!finalStatus"
            class="px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded disabled:opacity-50"
          >ยืนยัน</button>
        </div>

        <p v-if="successMessage" class="text-green-600 font-semibold text-sm mt-2">{{ successMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const requestId = route.params.id;

const request = ref(null);
const loading = ref(true);
const showApproveDialog = ref(false);
const finalStatus = ref("");
const successMessage = ref("");

// โหลดคำขอ
const loadRequest = async () => {
  try {
    const res = await fetch(`http://localhost:8080/api/admin/request-table/${requestId}`, {
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

// อัปเดตสถานะ / อนุมัติ
const confirmApprove = async () => {
  try {
    const res = await fetch(`http://localhost:8080/api/admin/request-table/${requestId}/approve`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ finalStatus: finalStatus.value })
    });

    if (!res.ok) throw new Error("Approval failed");

    successMessage.value = "✅ Request updated successfully.";
    setTimeout(() => {
      successMessage.value = "";
    }, 3000);

    showApproveDialog.value = false;
    finalStatus.value = "";
    await loadRequest();

  } catch (err) {
    console.error("❌ Approval error:", err);
    alert("Update failed. Please try again.");
  }
};

// ปฏิเสธคำขอ
const rejectRequest = async () => {
  const confirmReject = confirm("Are you sure you want to reject this request?");
  if (!confirmReject) return;

  try {
    const res = await fetch(`http://localhost:8080/api/admin/request-table/${requestId}/reject`, {
      method: 'PUT',
      credentials: 'include'
    });
    if (!res.ok) throw new Error("Reject failed");
    alert("🚫 Request rejected successfully.");
    await loadRequest();
  } catch (err) {
    console.error("❌ Reject error:", err);
    alert("Reject failed. Please try again.");
  }
};

// แสดงสถานะในภาษาไทย
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

// รูปแบบวันที่
const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  const date = new Date(dateStr);
  return date.toLocaleDateString("en-GB", {
    year: "numeric",
    month: "short",
    day: "2-digit"
  });
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
