<template>
  <div>
    <!-- Dropdown เลือกสาขา -->
    <div class="mb-4">
      <label class="block text-sm font-medium text-gray-700 mb-1"
        >เลือกสาขาวิชา:</label
      >
      <select
        class="border border-gray-300 rounded-md p-2 w-full md:w-64"
        v-model="selected"
      >
        <option
          v-for="field in academicFields"
          :key="field.name"
          :value="field.name"
        >
          {{ field.name }}
        </option>
      </select>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Pie Chart -->
      <div class="bg-gray-50 p-4 rounded-lg">
        <h2 class="text-lg font-medium text-gray-700 mb-4">
          หนังสือยอดนิยมในสาขา {{ selected }}
          <p class="text-base">
            <span class="text-red-500 ml-1">***</span>
            แสดงเพียงหนังสือ Top 10 เท่านั้น
            <span class="text-red-500 ml-1">***</span>
          </p>
        </h2>
        <v-chart class="h-72 w-full" :option="pieOption" autoresize />
      </div>

      <!-- แสดงหนังสือทั้งหมดแบบ Card -->

      <div class="bg-gray-50 p-4 rounded-lg">
        <h2 class="text-lg font-medium text-gray-700 mb-4">
          หนังสือทั้งหมดที่สาขา {{ selected }} ชื่นชอบ
        </h2>

        <div
          class="bg-purple-100 border mb-8 border-purple-300 rounded-lg p-4 text-purple-800 shadow-sm"
        >
          <p class="text-sm sm:text-base font-medium">
            สาขา <span class="font-semibold">{{ selected }}</span>
            ได้กด Favorite หนังสือไปแล้วทั้งหมด
            <span class="font-bold text-lg text-purple-900">{{
              books.length
            }}</span>
            เล่ม
          </p>
        </div>

        <div
          class="grid grid-cols-1 sm:grid-cols-2 gap-4 max-h-72 overflow-y-auto pr-2"
        >
          <div
            v-for="book in books"
            :key="book.name"
            class="flex justify-between items-center bg-white border rounded-lg px-4 py-2 shadow-sm"
          >
            <span class="text-gray-700 text-sm w-4/5">
              {{ book.name }}
            </span>

            <!-- ตัวเลขวงกลมขนาดคงที่ -->
            <span
              class="bg-purple-100 text-purple-700 text-xs font-semibold w-8 h-8 flex items-center justify-center rounded-full"
            >
              {{ book.value }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, watchEffect } from "vue";
import VChart from "vue-echarts";

const props = defineProps({
  academicFields: Array,
  bookInterestByField: Object,
  selectedField: String,
});
const emit = defineEmits(["update:selectedField"]);
const books = computed(() => props.bookInterestByField[selected.value] ?? []);

const selected = ref(props.selectedField);
watch(
  () => props.selectedField,
  (val) => {
    if (val) selected.value = val;
  }
);
watch(selected, (val) => emit("update:selectedField", val));
watchEffect(() => {
  if (!selected.value && props.academicFields?.length > 0) {
    selected.value = props.academicFields[0].name;
  }
});
const pieOption = computed(() => ({
  tooltip: { trigger: "item" },
  color: [
    "#76C7C0", // deeper teal
    "#FF9AA2", // richer pink
    "#FFB570", // deeper orange
    "#FFF275", // bright yellow
    "#A0E7A0", // green pastel
    "#A29BFE", // lavender
    "#E0BBE4", // purple-pink
    "#80CED6", // sky blue
    "#FF6666", // soft red
    "#B5EAD7", // mint
  ],
  series: [
    {
      type: "pie",
      radius: "70%",
      data: books.value,
      label: { formatter: "{b}" },
    },
  ],
}));
</script>
