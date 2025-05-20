<!-- OverviewTab.vue -->
<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
    <div class="bg-gray-50 p-4 rounded-lg">
      <h2 class="text-lg font-medium text-gray-700 mb-4">
        สัดส่วนผู้ใช้แยกตามสาขา
      </h2>
      <v-chart class="h-72 w-full" :option="pieOption" autoresize />
    </div>
    
    <div class="bg-gray-50 p-4 rounded-lg">
      <h2 class="text-lg font-medium text-gray-700 mb-4">
        หนังสือยอดนิยมโดยรวม
      </h2>
      <div
        class="grid grid-cols-1 sm:grid-cols-2 gap-4 max-h-72 overflow-y-auto pr-2"
      >
        <div
          v-for="book in topBooks"
          :key="book.name"
          class="flex justify-between items-center bg-white border rounded-lg px-4 py-2 shadow-sm"
        >
          <span class="text-gray-700 text-sm truncate w-48" :title="book.name">
            {{ book.name }}
          </span>
          <span
            class="bg-purple-100 text-purple-700 text-xs font-semibold px-3 py-1 rounded-full"
          >
            {{ book.value }} คน
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { use } from "echarts/core";
import VChart from "vue-echarts";
import { PieChart, BarChart } from "echarts/charts";
import {
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from "echarts/components";
import { CanvasRenderer } from "echarts/renderers";

use([
  PieChart,
  BarChart,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer,
]);

const props = defineProps({
  academicFields: Array,
  topBooks: Array,
});

const pieOption = computed(() => ({
  tooltip: { trigger: "item" },
  series: [
    {
      name: "สาขา",
      type: "pie",
      radius: "50%",
      data: props.academicFields,
      label: {
        formatter: "{b}: {@value}%",
      },
    },
  ],
}));

const barOption = computed(() => ({
  tooltip: {},
  xAxis: {
    type: "value",
    axisLabel: { formatter: "{value}%" },
  },
  yAxis: {
    type: "category",
    data: props.topBooks.map((book) => book.name),
  },
  series: [
    {
      type: "bar",
      data: props.topBooks.map((book) => book.value),
      itemStyle: { color: "#8884d8" },
    },
  ],
}));
</script>
