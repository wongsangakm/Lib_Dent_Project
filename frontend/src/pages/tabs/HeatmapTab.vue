<template>
  <div class="bg-gray-50 p-4 rounded-lg">
    <h2 class="text-lg font-medium text-gray-700 mb-4">
      ความสัมพันธ์ระหว่างสาขาและหนังสือยอดนิยม
    </h2>
    <p class="text-sm text-gray-500 mb-4">
      แสดงความสนใจในหนังสือแต่ละเล่มแยกตามสาขาวิชา (เปอร์เซ็นต์)
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
              {{ row[col] }}%
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mt-4 text-sm text-gray-500">
      <p>หมายเหตุ: สีพื้นหลังเข้มแสดงถึงความนิยมที่สูงกว่า</p>
    </div>
  </div>
</template>

<script setup>
const columns = [
  "Principles and Practice of Pharmacovigilance",
  "Drug Safety Guide",
  "Manual of Orthognathic Surgery",
  "Cervical Neck Swellings",
];

const rows = [
  {
    name: "แพทยศาสตร์",
    "Principles and Practice of Pharmacovigilance": 45,
    "Drug Safety Guide": 10,
    "Manual of Orthognathic Surgery": 30,
    "Cervical Neck Swellings": 15,
  },
  {
    name: "เภสัชศาสตร์",
    "Principles and Practice of Pharmacovigilance": 65,
    "Drug Safety Guide": 25,
    "Manual of Orthognathic Surgery": 5,
    "Cervical Neck Swellings": 5,
  },
  {
    name: "ทันตแพทยศาสตร์",
    "Principles and Practice of Pharmacovigilance": 5,
    "Drug Safety Guide": 5,
    "Manual of Orthognathic Surgery": 70,
    "Cervical Neck Swellings": 20,
  },
  {
    name: "พยาบาลศาสตร์",
    "Principles and Practice of Pharmacovigilance": 30,
    "Drug Safety Guide": 20,
    "Manual of Orthognathic Surgery": 10,
    "Cervical Neck Swellings": 40,
  },
  {
    name: "สาธารณสุขศาสตร์",
    "Principles and Practice of Pharmacovigilance": 35,
    "Drug Safety Guide": 50,
    "Manual of Orthognathic Surgery": 5,
    "Cervical Neck Swellings": 10,
  },
];

const getCellClass = (value) => {
  if (value >= 65) return "bg-blue-300";
  if (value >= 50) return "bg-blue-200";
  if (value >= 30) return "bg-blue-100";
  if (value >= 10) return "bg-blue-50";
  return "";
};
</script>
