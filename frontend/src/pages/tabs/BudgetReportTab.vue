<template>
  <div>
    <div class="mb-4 flex gap-4 items-end">
      <div>
        <label class="block text-sm font-medium">เลือกเดือน</label>
        <select v-model="selectedMonth" class="border px-3 py-1 rounded">
          <option value="">ทั้งหมด</option>
          <option
            v-for="month in months"
            :key="month.value"
            :value="month.value"
          >
            {{ month.label }}
          </option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium">เทอม</label>
        <select v-model="selectedTerm" class="border px-3 py-1 rounded">
          <option value="">ทั้งหมด</option>
          <option value="1">เทอม 1</option>
          <option value="2">เทอม 2</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium">ปีงบประมาณ</label>
        <input
          type="number"
          v-model="selectedYear"
          class="border px-3 py-1 rounded w-28"
        />
      </div>

      <div>
        <label class="block text-sm font-medium">สถานะ</label>
        <select v-model="selectedStatus" class="border px-3 py-1 rounded">
          <option value="">ทั้งหมด</option>
          <option value="ordered">กำลังสั่งซื้อ</option>
          <option value="in_shelf">สั่งซื้อแล้ว</option>
        </select>
      </div>

      <button
        @click="fetchReport"
        class="px-4 py-2 bg-gradient-to-r from-purple-600 to-purple-800 text-white font-medium rounded-lg shadow-md hover:shadow-lg hover:-translate-y-0.5 transition-transform duration-300 text-sm flex items-center justify-center"
      >
        ดูรายงาน
      </button>
    </div>

    <!-- รายงานสรุป -->
    <div class="bg-white rounded-lg shadow p-6">
      <h2 class="text-lg font-bold mb-4">สรุปงบสั่งซื้อ</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>จำนวนเล่มรวม: {{ reportData.totalBooks }} เล่ม</div>
        <div>
          ยอดรวม:
          {{
            reportData.totalAmount
              ? reportData.totalAmount.toLocaleString()
              : "-"
          }}
          บาท
        </div>
        <div>ปีงบประมาณ: {{ reportData.budgetYear }}</div>
        <div>สถานะ: {{ statusText(reportData.status) }}</div>
      </div>

      <!-- ตารางหนังสือ -->
      <div class="mt-6">
        <h3 class="text-lg font-semibold mb-2">รายการหนังสือ</h3>
        <div v-if="reportData.books.length === 0">ไม่พบข้อมูล</div>
        <table v-else class="w-full table-auto border border-collapse">
          <thead class="bg-gray-200">
            <tr>
              <th class="border px-2 py-1">ชื่อหนังสือ</th>
              <th class="border px-2 py-1">ISBN</th>
              <th class="border px-2 py-1">ราคา</th>
              <th class="border px-2 py-1">สำนักพิมพ์</th>
              <th class="border px-2 py-1">สถานะ</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="book in reportData.books" :key="book.isbn">
              <td class="border px-2 py-1">{{ book.title }}</td>
              <td class="border px-2 py-1">{{ book.isbn }}</td>
              <td class="border px-2 py-1">
                {{ book.price?.toLocaleString() }} บาท
              </td>
              <td class="border px-2 py-1">{{ book.publisher }}</td>
              <td class="border px-2 py-1">
                <span
                  class="px-2 py-1 rounded inline-block"
                  :class="{
                    'bg-green-100 text-green-700': book.status === 'in_shelf',
                    'bg-blue-100 text-blue-800': book.status === 'ordered',
                    'bg-purple-100 text-purple-800':
                      book.status === 'popular_request',
                    'bg-yellow-100 text-yellow-800': book.status === 'PENDING',
                    'bg-red-100 text-red-700': book.status === 'REJECTED',
                  }"
                >
                  {{ displayStatus(book.status) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- ตาราง AdditionalRequest -->
      <div class="mt-8">
        <h3 class="text-lg font-semibold mb-2">คำร้องขอเพิ่มเติม</h3>
        <h1 class="text-lg mb-2">ไม่แสดงคำร้องที่ถูกปฎิเสธ (REJECTED)</h1>
        <div v-if="reportData.additionalRequests.length === 0">ไม่พบข้อมูล</div>
        <table v-else class="w-full table-auto border border-collapse">
          <thead class="bg-gray-200">
            <tr>
              <th class="border px-2 py-1">ชื่อหนังสือ</th>
              <th class="border px-2 py-1">ISBN</th>
              <th class="border px-2 py-1">ราคา</th>
              <th class="border px-2 py-1">สำนักพิมพ์</th>
              <th class="border px-2 py-1">สถานะ</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="req in reportData.additionalRequests" :key="req.id">
              <td class="border px-2 py-1">{{ req.title }}</td>
              <td class="border px-2 py-1">{{ req.isbn }}</td>
              <td class="border px-2 py-1">
                {{ req.price?.toLocaleString() }} บาท
              </td>
              <td class="border px-2 py-1">{{ req.publisher }}</td>
              <td class="border px-2 py-1">
                <span
                  class="px-2 py-1 rounded inline-block"
                  :class="{
                    'bg-green-100 text-green-700': req.status === 'in_shelf',
                    'bg-blue-100 text-blue-800': req.status === 'ordered',
                    'bg-purple-100 text-purple-800':
                      req.status === 'popular_request',
                    'bg-yellow-100 text-yellow-800': req.status === 'PENDING',
                  }"
                >
                  {{ displayStatus(req.status) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();
const baseURL = import.meta.env.VITE_API_BASE_URL;
const selectedMonth = ref("");
const selectedYear = ref(new Date().getFullYear());
const selectedStatus = ref("");
const selectedTerm = ref("");

const reportData = ref({
  totalBooks: 0,
  totalAmount: 0,
  books: [],
  additionalRequests: [],
});

const months = [
  { value: "01", label: "มกราคม" },
  { value: "02", label: "กุมภาพันธ์" },
  { value: "03", label: "มีนาคม" },
  { value: "04", label: "เมษายน" },
  { value: "05", label: "พฤษภาคม" },
  { value: "06", label: "มิถุนายน" },
  { value: "07", label: "กรกฎาคม" },
  { value: "08", label: "สิงหาคม" },
  { value: "09", label: "กันยายน" },
  { value: "10", label: "ตุลาคม" },
  { value: "11", label: "พฤศจิกายน" },
  { value: "12", label: "ธันวาคม" },
];

const statusText = (status) => {
  return status === "ordered"
    ? "กำลังสั่งซื้อ"
    : status === "in_shelf"
    ? "สั่งซื้อแล้ว"
    : "-";
};

const displayStatus = (status) => {
  const normalized = status?.toLowerCase();
  switch (normalized) {
    case "in_shelf":
      return "มีในชั้นหนังสือแล้ว";
    case "ordered":
      return "กำลังสั่งซื้อ";
    case "popular_request":
      return "กำลังพิจารณาจัดซื้อ";
    case "pending":
      return "รอการอนุมัติ";
    case "rejected":
      return "ถูกปฏิเสธ";
    default:
      return status;
  }
};

const fetchReport = async () => {
  console.log("🔍 เรียก fetchReport แล้ว");
  try {
    const res = await axios.get(`${baseURL}/api/admin/budget-report`, {
      params: {
        month: selectedMonth.value ? parseInt(selectedMonth.value) : undefined,
        term: selectedTerm.value ? parseInt(selectedTerm.value) : undefined,
        year: selectedYear.value,
        status: selectedStatus.value,
      },
      withCredentials: true,
    });

    console.log("✅ ได้ข้อมูลจาก API", res.data);
    const data = res.data;
    reportData.value = {
      totalBooks: data.totalBooks,
      totalAmount: data.totalBudget,
      books: data.books || [],
      additionalRequests: (data.additionalRequests || []).filter(
        (req) => req.status?.toLowerCase() !== "rejected"
      ),
    };
  } catch (err) {
    alert("เกิดข้อผิดพลาดในการดึงรายงานงบประมาณ");
    console.error(err);
  }
};
</script>
