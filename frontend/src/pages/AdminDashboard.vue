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
import { ref, onMounted } from "vue";
import { Pie } from "vue-chartjs";
import { Chart as ChartJS, Title, Tooltip, ArcElement, Legend } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";

ChartJS.register(Title, Tooltip, ArcElement, Legend, ChartDataLabels);

const chartData = ref({ labels: [], datasets: [] });
const chartOptions = {
  plugins: {
    legend: { display: false },
    datalabels: {
      color: "#fff",
      font: { weight: "bold", size: 14 },
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
  const words = text.split(" ");
  const lines = [];
  let currentLine = "";

  for (const word of words) {
    if ((currentLine + " " + word).trim().length <= maxCharPerLine) {
      currentLine += " " + word;
    } else {
      lines.push(currentLine.trim());
      currentLine = word;
    }
  }
  lines.push(currentLine.trim());
  return lines.join("\n");
}

onMounted(async () => {
  try {
    const res = await fetch("http://localhost:8080/api/admin/favorite-counts", {
      credentials: "include",
    });
    const raw = await res.json(); // [{ bookTitle, favoriteCount }]

    console.log("📊 Raw data:", raw);
    // จัดอันดับมาก → น้อย
    const sorted = raw.sort((a, b) => b.favoriteCount - a.favoriteCount);
    const top3 = sorted.slice(0, 3);
    const othersTotal = sorted
      .slice(3)
      .reduce((sum, b) => sum + b.favoriteCount, 0);

    const labels = top3.map((b) => b.bookTitle);
    const dataValues = top3.map((b) => b.favoriteCount);

    if (othersTotal > 0) {
      labels.push("Others");
      dataValues.push(othersTotal);
    }

    chartData.value = {
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
  } catch (err) {
    console.error("❌ Failed to load chart data:", err);
  }
});
</script>
