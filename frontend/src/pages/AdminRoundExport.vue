<template>
  <div
    class="min-h-screen bg-gradient-to-br from-white to-purple-50 py-10 px-6"
  >
    <h1
      class="text-2xl font-bold text-center text-gray-800 mb-10 uppercase tracking-wider"
    >
      รอบการตัดยอดคำสั่งซื้อหนังสือ
    </h1>

    <div class="space-y-6 max-w-4xl mx-auto">
      <div
        v-for="round in exportRounds"
        :key="round.id"
        class="bg-white shadow rounded-2xl p-6 flex justify-between items-center border border-gray-200"
      >
        <div>
          <h2 class="text-xl font-extrabold text-gray-800">
            รอบที่ {{ round.id }}
          </h2>
          <p class="text-sm text-gray-500 mt-1">
            ส่งรายชื่อภายใน {{ round.deadline }}
          </p>
          <p class="text-md font-semibold mt-1">
            สั่งซื้อเดือน{{ round.month }}
          </p>
        </div>

        <!-- Export Button Group -->
        <div class="flex gap-2">
          <button
            @click="exportToExcel(round)"
            class="bg-green-600 text-white px-3 py-1 rounded"
          >
            Excel
          </button>
          <button
            @click="exportToPDF(round)"
            class="bg-red-600 text-white px-3 py-1 rounded"
          >
            PDF
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import { useAuthStore } from "@/stores/useAuthStore";
const authStore = useAuthStore();
const baseURL = import.meta.env.VITE_API_BASE_URL;

const exportRounds = [
  { id: 1, name: "รอบที่ 1", deadline: "30 December", label: "May–December" },
  {
    id: 2,
    name: "รอบที่ 2",
    deadline: "28 Febuary",
    label: "January–Febuary",
  },
  { id: 3, name: "รอบที่ 3", deadline: "30 Aprill", label: "March–April" },
];

const exportToExcel = async (round) => {
  try {
    const year = new Date().getFullYear();
    const res = await fetch(
      `${baseURL}/api/admin/export/favorites?round=${round.id}&year=${year}`,
      {
        headers: {
          ...authStore.getAuthHeader(),
        },
      }
    );
    const data = await res.json();

    // ✅ สร้างใหม่ทุกครั้ง
    const sheetData = data.map((book, index) => ({
      "No.": index + 1,
      ISBN: book.isbn || " ",
      ชื่อหนังสือ: book.title || " ",
      ราคา: book.price || " ",
      ปีที่พิมพ์: book.year || " ",
      สำนักพิมพ์: book.publisher || " ",
      "จำนวน Fav": book.favorites || 0,
      Edition: book.edition || " ",
      "รายชื่อผู้กด Favorite (TH)": book.users || "",
      "รายชื่อผู้กด Favorite (EN)": book.usersEn || "",
    }));

    // ✅ สร้าง workbook/sheet ใหม่ทุกครั้ง
    const workbook = XLSX.utils.book_new();
    const worksheet = XLSX.utils.json_to_sheet(sheetData);

    worksheet["!cols"] = [
      { wch: 5 },
      { wch: 15 },
      { wch: 40 },
      { wch: 10 },
      { wch: 10 },
      { wch: 20 },
      { wch: 12 },
      { wch: 10 },
      { wch: 40 },
      { wch: 40 },
    ];

    XLSX.utils.book_append_sheet(workbook, worksheet, `Export`);

    const excelBuffer = XLSX.write(workbook, {
      bookType: "xlsx",
      type: "array",
    });

    const blob = new Blob([excelBuffer], { type: "application/octet-stream" });
    saveAs(blob, `BookExport_Round${round.id}_${year}.xlsx`);
  } catch (error) {
    console.error("❌ Failed to export Excel:", error);
    alert("เกิดข้อผิดพลาดในการ export Excel");
  }
};

const exportToPDF = async (round) => {
  try {
    const year = new Date().getFullYear(); // ✅ ดึง year จาก system
    const res = await fetch(
      `${baseURL}/api/admin/export/favorites?round=${round.id}&year=${year}`,
      {
        headers: {
          ...authStore.getAuthHeader(),
        },
      }
    );
    const data = await res.json();

    const doc = new jsPDF();

    doc.setFontSize(14);
    doc.text(
      `Book Export - Round ${round.id} (${round.deadline} ${year})`,
      20,
      20
    );

    const tableData = data.map((book, i) => [
      i + 1,
      book.isbn || "-",
      book.title || "-",
      `${book.price?.toLocaleString() || "-"} THB`,
      book.year || "-",
      book.publisher || "-",
      book.favorites || 0,
      book.edition || "-",
      Array.isArray(book.usersEn)
        ? book.usersEn.join(", ")
        : book.usersEn || "-",
    ]);

    autoTable(doc, {
      head: [
        [
          "No.",
          "ISBN",
          "Title",
          "Price",
          "Year",
          "Publisher",
          "Favorites",
          "Edition",
          "Users",
        ],
      ],
      body: tableData,
      startY: 30,
      styles: { fontSize: 10 },
      theme: "striped",
      headStyles: { fillColor: [128, 90, 213] },
    });

    doc.save(`BookExport_Round${round.id}_${year}.pdf`);
  } catch (error) {
    console.error("❌ Failed to export PDF:", error);
    alert("เกิดข้อผิดพลาดในการ export PDF");
  }
};
</script>
