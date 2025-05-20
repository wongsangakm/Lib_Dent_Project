<template>
  <div class="bg-white p-8 rounded-xl shadow-md">
    <h1 class="text-2xl font-bold text-gray-800 mb-6">
      Book Interest Survey Dashboard
    </h1>

    <!-- Tab Navigation -->
    <div class="mb-6 flex border-b">
      <button
        class="py-2 px-4 font-medium"
        :class="
          activeTab === 'overview'
            ? 'text-blue-600 border-b-2 border-blue-600'
            : 'text-gray-500'
        "
        @click="activeTab = 'overview'"
      >
        ภาพรวม
      </button>
      <button
        class="py-2 px-4 font-medium"
        :class="
          activeTab === 'byField'
            ? 'text-blue-600 border-b-2 border-blue-600'
            : 'text-gray-500'
        "
        @click="activeTab = 'byField'"
      >
        แยกตามสาขา
      </button>
      <button
        class="py-2 px-4 font-medium"
        :class="
          activeTab === 'heatmap'
            ? 'text-blue-600 border-b-2 border-blue-600'
            : 'text-gray-500'
        "
        @click="activeTab = 'heatmap'"
      >
        ความสัมพันธ์
      </button>
    </div>

    <!-- Overview Tab -->
    <OverviewTab
      v-if="activeTab === 'overview'"
      :academic-fields="academicFields"
      :top-books="topBooks"
    />

    <!-- By Field Tab -->
    <ByFieldTab
      v-if="activeTab === 'byField'"
      :academic-fields="academicFields"
      :book-interest-by-field="bookInterestByField"
      v-model:selected-field="selectedField"
    />

    <!-- Heatmap Tab -->
    <HeatmapTab v-if="activeTab === 'heatmap'" />

    <!-- Insight -->
    <div class="mt-8 bg-gray-50 p-4 rounded-lg">
      <h2 class="text-lg font-medium text-gray-700 mb-2">คำแนะนำจากข้อมูล</h2>
      <ul class="list-disc pl-5 space-y-1 text-gray-600">
        <li>
          สาขาเภสัชศาสตร์มีความสนใจในหนังสือ "Principles and Practice of
          Pharmacovigilance" มากที่สุด (65%)
        </li>
        <li>
          สาขาทันตแพทยศาสตร์มีความสนใจในหนังสือ "Manual of Orthognathic Surgery"
          มากที่สุด (70%)
        </li>
        <li>
          หนังสือ "Principles and Practice of Pharmacovigilance"
          ได้รับความนิยมสูงสุดจากทุกสาขารวมกัน
        </li>
        <li>
          สาขาสาธารณสุขศาสตร์มีความสนใจในหนังสือ "Drug Safety Guide" มากที่สุด
          (50%)
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import OverviewTab from "./tabs/OverviewTab.vue";
import ByFieldTab from "./tabs/ByFieldTab.vue";
import HeatmapTab from "./tabs/HeatmapTab.vue";
import axios from "axios";
const activeTab = ref("overview");
const bookInterestByField = ref({});
const academicFields = ref([]);
const topBooks = ref([]);

onMounted(async () => {
  try {
    const [fieldRes, bookRes, topRes] = await Promise.all([
      axios.get("http://localhost:8080/api/admin/active-users-by-field"),
      axios.get("http://localhost:8080/api/admin/favorite-books-by-field"),
      axios.get("http://localhost:8080/api/admin/top-books"),
    ]);

    academicFields.value = fieldRes.data;
    bookInterestByField.value = bookRes.data;
    topBooks.value = topRes.data; // ✅ ตัวแปรนี้ใช้ได้แล้ว
  } catch (err) {
    console.error("โหลดข้อมูล dashboard ล้มเหลว", err);
  }
});


</script>
