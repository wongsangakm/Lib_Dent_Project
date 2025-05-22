<template>
  <div class="bg-gray-50 p-4 rounded-lg">
    <h2 class="text-lg font-medium text-gray-700 mb-4">
      ความสัมพันธ์ระหว่างสาขาและหนังสือยอดนิยม
    </h2>
    <p class="text-sm text-gray-500 mb-4">
      แสดงจำนวนผู้ใช้ที่กด Favorite หนังสือยอดนิยมแต่ละเล่ม แยกตามสาขา
    </p>
    <div class="overflow-x-auto">
      <table class="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th class="py-2 px-4 border-b text-left bg-gray-100">
              สาขา/หนังสือ
            </th>
            <th
              v-for="col in columns"
              :key="col"
              class="py-2 px-4 border-b text-center bg-gray-100"
            >
              {{ col }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.name">
            <td class="py-2 px-4 border-b font-medium">{{ row.name }}</td>
            <td
              v-for="col in columns"
              :key="col"
              class="py-2 px-4 border-b text-center"
              :class="getCellClass(row[col])"
            >
              {{ row[col] }} คน
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mt-4 text-sm text-gray-500">
      <p>หมายเหตุ: สีพื้นหลังเข้มแสดงถึงจำนวนที่สูงกว่า</p>
    </div>
  </div>
</template>
<script setup>
import { defineProps } from "vue";

const props = defineProps({
  columns: {
    type: Array,
    required: true,
  },
  rows: {
    type: Array,
    required: true,
  },
});

const getCellClass = (value) => {
  if (value >= 10) return "bg-blue-300 text-white";
  if (value >= 5) return "bg-blue-100";
  if (value >= 1) return "bg-blue-50";
  return "";
};
</script>
