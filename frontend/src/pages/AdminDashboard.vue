<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 py-12 px-4"
  >
    <h1 class="text-2xl font-bold text-center text-gray-800 mb-8">
      Book Interest Survey Report
    </h1>
    <div class="max-w-xl mx-auto">
      <Pie :data="chartData" :options="chartOptions" />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { Pie } from "vue-chartjs";
import { Chart as ChartJS, Title, Tooltip, ArcElement, Legend } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";
import AdminSidebar from '@/component/AdminSidebar.vue';

// Register chart features
ChartJS.register(Title, Tooltip, ArcElement, Legend, ChartDataLabels);

// Mockup data
const labels = [
  "Cone Beam CT in Dentists",
  "Wild Woman’s Way",
  "In the Garden of the Fugitives",
  "Others",
];
const dataValues = [50, 20, 15, 15];

const chartData = {
  labels,
  datasets: [
    {
      data: dataValues,
      backgroundColor: ["#1E3A8A", "#F59E0B", "#EC4899", "#6366F1"],
      borderColor: "#ffffff",
      borderWidth: 2,
    },
  ],
};

const chartOptions = {
  plugins: {
    legend: {
      display: false,
    },
    datalabels: {
      color: "#fff",
      font: {
        weight: "bold",
        size: 14,
      },
      formatter: (value, context) => {
        const label = context.chart.data.labels[context.dataIndex];
        const total = context.chart.data.datasets[0].data.reduce(
          (a, b) => a + b,
          0
        );
        const percentage = ((value / total) * 100).toFixed(0);
        return `${wrapText(label)}\n${percentage}%`;
      },
    },
  },
  
};

function wrapText(text, maxCharPerLine = 15) {
  const words = text.split(' ')
  const lines = []
  let currentLine = ''

  for (const word of words) {
    if ((currentLine + ' ' + word).trim().length <= maxCharPerLine) {
      currentLine += ' ' + word
    } else {
      lines.push(currentLine.trim())
      currentLine = word
    }
  }
  lines.push(currentLine.trim())
  return lines.join('\n') // ใส่ \n เพื่อขึ้นบรรทัดใหม่
}

</script>
