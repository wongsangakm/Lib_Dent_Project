<template>
  <div>
    <div class="mb-4 flex gap-4 items-end">
      <div>
        <label class="block text-sm font-medium">เลือกเดือน</label>
        <select v-model="selectedMonth" class="border px-3 py-1 rounded">
          <option value="">ทั้งหมด</option>
          <option v-for="month in months" :key="month.value" :value="month.value">
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
          <option value="pending">กำลังสั่งซื้อ</option>
          <option value="ordered">สั่งซื้อแล้ว</option>
        </select>
      </div>

      <button
        @click="fetchReport"
        class="bg-blue-600 text-white px-4 py-2 rounded"
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
          ยอดรวม: {{ reportData.totalAmount ? reportData.totalAmount.toLocaleString() : '-' }} บาท
        </div>
        <div>ปีงบประมาณ: {{ reportData.budgetYear }}</div>
        <div>สถานะ: {{ statusText(reportData.status) }}</div>
      </div>
      <!-- รายการหนังสือ -->
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
        <td class="border px-2 py-1">{{ book.price?.toLocaleString() }} บาท</td>
        <td class="border px-2 py-1">{{ book.publisher }}</td>
        <td class="border px-2 py-1">{{ statusText(book.status) }}</td>
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

const selectedMonth = ref("");
const selectedYear = ref(new Date().getFullYear());
const selectedStatus = ref("");
const selectedTerm = ref("");

const reportData = ref({
  totalBooks: 0,
  totalAmount: 0,
  books: [],
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
  return status === "pending"
    ? "กำลังสั่งซื้อ"
    : status === "ordered"
    ? "สั่งซื้อแล้ว"
    : "-";
};

const fetchReport = async () => {
    console.log("🔍 เรียก fetchReport แล้ว");
  try {
    const res = await axios.get("http://localhost:8080/api/admin/budget-report", {
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
    };
  } catch (err) {
    alert("เกิดข้อผิดพลาดในการดึงรายงานงบประมาณ");
    console.error(err);
  }
};
</script>

