<template>
  <div class="min-h-screen bg-gradient-to-br p-6">
    <div class="max-w-4xl mx-auto">
      <!-- Header Section -->
      <div class="text-center mb-8 mt-10">
        <div
          class="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-r from-blue-200 to-indigo-400 rounded-2xl mb-4 shadow-lg"
        >
          <svg
            class="w-8 h-8 text-white"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"
            ></path>
          </svg>
        </div>
        <h1 class="text-3xl font-bold text-gray-800 mb-2">
          Library Book Management
        </h1>
        <p class="text-gray-600">
          เมื่อมีหนังสือเพิ่มเข้ามาใหม่ในห้องสมุดให้กดลบหนังสือทั้งหมดก่อนแล้วค่อยอัพโหลดหนังสือใหม่
        </p>
      </div>

      <!-- Main Content Cards -->
      <div class="grid md:grid-cols-2 gap-6">
        <!-- Upload Card -->
        <div
          class="bg-white rounded-2xl shadow-xl p-8 border border-gray-100 hover:shadow-2xl transition-all duration-300"
        >
          <div class="flex items-center mb-6">
            <div
              class="w-10 h-10 bg-green-100 rounded-xl flex items-center justify-center mr-4"
            >
              <svg
                class="w-6 h-6 text-green-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
                ></path>
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-800">Upload Books</h2>
              <p class="text-sm text-gray-500">Import from Excel file</p>
            </div>
          </div>

          <!-- File Upload Area -->
          <div class="mb-6">
            <div
              class="relative border-2 border-dashed border-gray-300 rounded-xl p-8 text-center hover:border-blue-400 transition-colors duration-300"
              :class="{
                'border-blue-400 bg-blue-50': selectedFile,
                'hover:bg-gray-50': !selectedFile,
              }"
            >
              <input
                type="file"
                ref="fileInput"
                accept=".xlsx"
                @change="onFileChange"
                class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
              />

              <div v-if="!selectedFile" class="space-y-2">
                <svg
                  class="mx-auto h-12 w-12 text-gray-400"
                  stroke="currentColor"
                  fill="none"
                  viewBox="0 0 48 48"
                >
                  <path
                    d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <div class="text-gray-600">
                  <p class="font-medium">Click to upload or drag and drop</p>
                  <p class="text-sm">Excel files (.xlsx) only</p>
                </div>
              </div>

              <div v-else class="space-y-2">
                <svg
                  class="mx-auto h-12 w-12 text-green-500"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                  ></path>
                </svg>
                <div class="text-green-600">
                  <p class="font-medium">{{ selectedFile.name }}</p>
                  <p class="text-sm">Ready to upload</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Upload Button -->
          <button
            @click="uploadFile"
            :disabled="!selectedFile"
            class="w-full py-3 px-6 rounded-xl font-medium transition-all duration-300 transform hover:scale-[1.02] focus:outline-none focus:ring-4 focus:ring-blue-200"
            :class="
              selectedFile
                ? 'bg-gradient-to-r from-blue-500 to-indigo-600 text-white shadow-lg hover:shadow-xl'
                : 'bg-gray-200 text-gray-400 cursor-not-allowed'
            "
          >
            <span v-if="!uploading" class="flex items-center justify-center">
              <svg
                class="w-5 h-5 mr-2"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
                ></path>
              </svg>
              Upload Books
            </span>
            <span v-else class="flex items-center justify-center">
              <svg
                class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
              >
                <circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                ></circle>
                <path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path>
              </svg>
              Uploading...
            </span>
          </button>
        </div>

        <!-- Management Card -->
        <div
          class="bg-white rounded-2xl shadow-xl p-8 border border-gray-100 hover:shadow-2xl transition-all duration-300"
        >
          <div class="flex items-center mb-6">
            <div
              class="w-10 h-10 bg-red-100 rounded-xl flex items-center justify-center mr-4"
            >
              <svg
                class="w-6 h-6 text-red-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                ></path>
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-800">
                Manage Collection
              </h2>
              <p class="text-sm text-gray-500">Database operations</p>
            </div>
          </div>

          <div class="space-y-4">
            <div class="bg-red-50 border border-red-200 rounded-xl p-4">
              <div class="flex items-start">
                <svg
                  class="w-5 h-5 text-red-400 mt-0.5 mr-3"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z"
                  ></path>
                </svg>
                <div class="flex-1">
                  <h3 class="text-sm font-medium text-red-800">Danger Zone</h3>
                  <p class="text-sm text-red-600 mt-1">
                    การดำเนินการนี้ไม่สามารถย้อนกลับได้
                    ข้อมูลหนังสือทั้งหมดจะถูกลบอย่างถาวร
                  </p>
                </div>
              </div>
            </div>

            <button
              @click="deleteAllLibraryBooks"
              class="w-full py-3 px-6 bg-red-500 hover:bg-red-600 text-white rounded-xl font-medium transition-all duration-300 hover:shadow-lg focus:outline-none focus:ring-4 focus:ring-red-200 transform hover:scale-[1.02]"
            >
              <span class="flex items-center justify-center">
                <svg
                  class="w-5 h-5 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                  ></path>
                </svg>
                Delete All Books
              </span>
            </button>
          </div>
        </div>
      </div>

      <!-- Status Messages -->
      <div v-if="uploadStatus" class="mt-8">
        <div
          class="rounded-xl p-4 border-l-4 shadow-lg"
          :class="
            uploadStatus.includes('successful') ||
            uploadStatus.includes('เรียบร้อย')
              ? 'bg-green-50 border-green-400 text-green-800'
              : 'bg-red-50 border-red-400 text-red-800'
          "
        >
          <div class="flex items-center">
            <svg
              v-if="
                uploadStatus.includes('successful') ||
                uploadStatus.includes('เรียบร้อย')
              "
              class="w-5 h-5 mr-3 text-green-500"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>
            <svg
              v-else
              class="w-5 h-5 mr-3 text-red-500"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>
            <p class="font-medium">{{ uploadStatus }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref } from "vue";
import { useAuthStore } from "@/stores/useAuthStore";
const baseURL = import.meta.env.VITE_API_BASE_URL;
export default {
  name: "AdminLibraryBook",

  data() {
    return {
      selectedFile: null,
      uploadStatus: "",
      uploading: false,
    };
  },

  methods: {
    onFileChange(event) {
      this.uploadStatus = "";
      const file = event.target.files[0];
      if (!file) {
        this.selectedFile = null;
        return;
      }

      const validExtensions = ["xlsx"];
      const fileExt = file.name.split(".").pop().toLowerCase();
      if (!validExtensions.includes(fileExt)) {
        this.uploadStatus = "Please select a .xlsx file only.";
        this.selectedFile = null;
        this.$refs.fileInput.value = null;
      } else {
        this.selectedFile = file;
      }
    },

    async deleteAllLibraryBooks() {
      const authStore = useAuthStore();
      if (!confirm("⚠️ คุณแน่ใจหรือไม่ว่าต้องการลบรายการทั้งหมด?")) return;

      try {
        await axios.delete(`${baseURL}/api/library-books/all`, {
          headers: authStore.getAuthHeader(),
        });
        this.uploadStatus = "✅ ลบข้อมูลทั้งหมดเรียบร้อยแล้ว";
      } catch (error) {
        console.error("❌ ลบทั้งหมดไม่สำเร็จ", error);
        this.uploadStatus = "❌ ลบข้อมูลไม่สำเร็จ";
      }
    },

    async uploadFile() {
      if (!this.selectedFile) return;

      this.uploading = true;
      const formData = new FormData();
      formData.append("file", this.selectedFile);

      try {
        const response = await axios.post(
          `${baseURL}/api/library-books/import`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        );

        this.uploadStatus = "Upload successful: " + response.data;
        this.selectedFile = null;
        this.$refs.fileInput.value = null;
      } catch (error) {
        console.error("Upload failed:", error);

        if (error.response) {
          this.uploadStatus =
            `Upload failed: (${error.response.status}) ` +
            `${error.response.data}`;
        } else {
          this.uploadStatus = "Upload failed: Unknown error";
        }
      } finally {
        this.uploading = false;
      }
    },
  },
};
</script>

<style scoped>
/* Additional custom styles if needed */
.hover\:scale-\[1\.02\]:hover {
  transform: scale(1.02);
}
</style>
