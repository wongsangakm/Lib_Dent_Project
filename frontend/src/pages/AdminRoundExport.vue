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
            <button @click="exportToExcel(round)" class="bg-green-600 text-white px-3 py-1 rounded">Excel</button>
            <button @click="exportToPDF(round)" class="bg-red-600 text-white px-3 py-1 rounded">PDF</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
const exportRounds = [
  { id: 1, month: "ธันวาคม", deadline: "30 ธันวาคม" },
  { id: 2, month: "กุมภาพันธ์", deadline: "28 กุมภาพันธ์" },
  { id: 3, month: "เมษายน", deadline: "30 เมษายน" },
];

const exportData = (round) => {
  // 📝 ตรงนี้คือ mock logic ในอนาคตคุณจะเชื่อม API สำหรับ export
  alert(`Export ข้อมูลรอบที่ ${round.id} เดือน${round.month} สำเร็จ (mock)`);
};

import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'

const exportToExcel = (round) => {
  const data = [
    { ID: 1, Name: 'Cone Beam CT', Favorites: 44 },
    { ID: 2, Name: 'Wild Woman’s Way', Favorites: 21 },
    // สมมุติข้อมูลที่ export จริง
  ]

  const worksheet = XLSX.utils.json_to_sheet(data)
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, `Round-${round.id}`)

  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' })
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' })
  saveAs(blob, `BookExport_Round${round.id}.xlsx`)
}
import jsPDF from 'jspdf'

const exportToPDF = (round) => {
  const doc = new jsPDF()
  doc.text(`Book Export - Round ${round.id}`, 20, 20)

  const mockBooks = [
    'Cone Beam CT in Dentists and Medical Radiologists',
    'Wild Woman’s Way',
    'The Garden',
  ]

  mockBooks.forEach((book, index) => {
    doc.text(`${index + 1}. ${book}`, 20, 30 + index * 10)
  })

  doc.save(`BookExport_Round${round.id}.pdf`)
}

</script>
