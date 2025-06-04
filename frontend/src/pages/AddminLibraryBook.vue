<template>
  <div class="p-4">
    <h2>Upload Library Books (Excel)</h2>

    <!-- 1) ไฟล์อินพุต ให้ชื่อ name="file" เพื่อให้ Backend รับได้กับ @RequestPart("file") -->
    <input type="file" ref="fileInput" accept=".xlsx" @change="onFileChange" />

    <!-- 2) ปุ่มอัปโหลด -->
    <button
      class="mt-2 px-4 py-2 bg-blue-500 text-white rounded"
      @click="uploadFile"
      :disabled="!selectedFile"
    >
      Upload
    </button>

    <!-- 3) แสดงสถานะการอัปโหลด -->
    <div v-if="uploadStatus" class="mt-4">
      {{ uploadStatus }}
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminLibraryBook",

  data() {
    return {
      // เก็บไฟล์ที่ผู้ใช้เลือก
      selectedFile: null,
      // สถานะตอบกลับ (success / error) จาก server
      uploadStatus: "",
    };
  },

  methods: {
    /**
     * เมื่อมีการเลือกไฟล์ที่ <input type="file" />, ฟังก์ชันนี้จะถูกเรียก
     * ตรวจสอบว่า user เลือกไฟล์ .xlsx จริง ๆ หรือไม่ แล้วเก็บไว้ที่ selectedFile
     */
    onFileChange(event) {
      this.uploadStatus = "";
      const file = event.target.files[0];
      if (!file) {
        this.selectedFile = null;
        return;
      }

      // ตรวจสอบนามสกุล (ถ้าอยากบังคับเฉพาะ .xlsx)
      const validExtensions = ["xlsx"];
      const fileExt = file.name.split(".").pop().toLowerCase();
      if (!validExtensions.includes(fileExt)) {
        this.uploadStatus = "Please select a .xlsx file only.";
        this.selectedFile = null;
        // เคลียร์ input อีกครั้ง เพื่อให้ user สามารถเลือกใหม่ได้
        this.$refs.fileInput.value = null;
      } else {
        this.selectedFile = file;
      }
    },

    /**
     * เมื่อคลิกปุ่ม Upload จะสร้าง FormData ขึ้นมา
     * แล้ว append ฟิลด์ชื่อ "file" พร้อมไฟล์ที่เลือก ส่งไปที่ backend ด้วย axios
     */
    async uploadFile() {
      if (!this.selectedFile) return;

      // สร้าง FormData และ append ชื่อ field "file"
      const formData = new FormData();
      formData.append("file", this.selectedFile);

      try {
        // XX: อย่าเซ็ต Content-Type เอง เพราะ axios จะตั้ง boundary ให้เอง
        //    ถ้าบังคับเอง มักทำให้ไม่ได้ boundary ถูกต้อง → backend อ่าน multipart ผิด → 400
        const response = await axios.post(
          "http://localhost:8080/api/library-books/import",
          formData,
          {
            headers: {
              // บอกว่าเป็น multipart/form-data แต่ให้ axios สร้าง boundary ให้อัตโนมัติ
              "Content-Type": "multipart/form-data",
            },
          }
        );

        // ถ้าได้ 2xx ตอบกลับ แสดงว่าอัปโหลดสำเร็จ
        this.uploadStatus = "Upload successful: " + response.data;
        // เคลียร์ input
        this.selectedFile = null;
        this.$refs.fileInput.value = null;
      } catch (error) {
        // มักได้ 400 หรือ 500 ถ้า backend อ่าน multipart ผิด หรือ exception ใน service
        console.error("Upload failed:", error);

        if (error.response) {
          // ถ้า Spring ตอบกลับ error code + ข้อความ
          this.uploadStatus =
            `Upload failed: (${error.response.status}) ` +
            `${error.response.data}`;
        } else {
          this.uploadStatus = "Upload failed: Unknown error";
        }
      }
    },
  },
};
</script>

<style scoped>
/* ปรับแต่งตามต้องการ */
</style>
