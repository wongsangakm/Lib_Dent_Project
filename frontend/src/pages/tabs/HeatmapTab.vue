<template>
  <div class="bg-gray-50 p-4 rounded-lg">
    <h2 class="text-lg font-medium text-gray-700 mb-4">
      ความสัมพันธ์ระหว่างหนังสือยอดนิยมและสาขา
    </h2>
    <p class="text-sm text-gray-500 mb-4">
      แสดงจำนวนผู้ใช้ที่กด Favorite หนังสือยอดนิยมแต่ละเล่ม แยกตามสาขา
    </p>

    <div class="overflow-x-auto">
      <div class="max-h-[500px] overflow-y-auto">
        <table class="min-w-full bg-white border border-gray-200">
          <thead>
            <tr>
              <th class="py-2 px-4 border-b text-left bg-gray-100">
                หนังสือ/สาขา
              </th>
              <th
                v-for="row in rows"
                :key="row.name"
                class="py-2 px-4 border-b text-center bg-gray-100"
              >
                {{ row.name }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="col in paginatedColumns" :key="col">
              <td class="py-2 px-4 border-b font-medium">{{ col }}</td>
              <td
                v-for="row in rows"
                :key="row.name"
                class="py-2 px-4 border-b text-center"
                :class="getCellClass(row[col])"
              >
                {{ row[col] }} คน
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="flex justify-between items-center mt-4 text-sm text-gray-600">
      <button
        @click="prevPage"
        :disabled="currentPage === 1"
        class="px-3 py-1 border rounded disabled:opacity-50"
      >
        หน้าก่อนหน้า
      </button>
      <span>หน้า {{ currentPage }} / {{ totalPages }}</span>
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="px-3 py-1 border rounded disabled:opacity-50"
      >
        หน้าถัดไป
      </button>
    </div>

    <div class="mt-4 text-sm text-gray-500">
      <p>หมายเหตุ: สีพื้นหลังเข้มแสดงถึงจำนวนที่สูงกว่า</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, computed } from 'vue';

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

// Paging logic
const currentPage = ref(1);
const pageSize = 10; // ปรับจำนวนหนังสือต่อหน้าได้

const totalPages = computed(() =>
  Math.ceil(props.columns.length / pageSize)
);

const paginatedColumns = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return props.columns.slice(start, start + pageSize);
});

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const prevPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};

// Cell highlight
const getCellClass = (value) => {
  if (value >= 10) return 'bg-blue-300 text-white';
  if (value >= 5) return 'bg-blue-100';
  if (value >= 1) return 'bg-blue-50';
  return '';
};
</script>
