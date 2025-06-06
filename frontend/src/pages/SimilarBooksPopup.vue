<template>
  <div v-if="visible" class="overlay" @click.self="cancelRequest">
    <div class="popup">
      <div class="popup-header">
        <h2>
          <i class="fa-solid fa-triangle-exclamation"></i>
          พบหนังสือที่คล้ายกันที่มีอยู่แล้วในห้องสมุด
        </h2>
        <p>กรุณาตรวจสอบหนังสือที่มีอยู่แล้วก่อนส่งคำขอ</p>
        <p>สามารถนำหมายเลขหมวดหมู่ไปติดต่อขอยืมหนังสือที่ห้องสมุดได้</p>
      </div>

      <div class="popup-content" id="booksList">
        <div v-if="similarBooks && similarBooks.length" class="books-container">
          <div
            v-for="(book, index) in similarBooks"
            :key="index"
            class="book-item"
          >
            <div class="book-header">
              <div class="book-number">{{ index + 1 }}</div>
              <div class="book-title">
                <h3>{{ book.title || "ไม่ระบุชื่อ" }}</h3>
                <span
                  class="similarity-badge"
                  :class="getSimilarityClass(book.similarity || 0)"
                >
                  ความคล้าย {{ Math.round((book.similarity || 0) * 100) }}%
                </span>
              </div>
            </div>

            <div class="book-details">
              <div class="detail-item">
                <span class="detail-label">ผู้แต่ง</span>
                <span class="detail-value">{{
                  book.author || "ไม่ระบุผู้แต่ง"
                }}</span>
              </div>

              <div class="detail-item">
                <span class="detail-label">หมายเลขหมวดหมู่</span>
                <span class="detail-value">
                  <span class="call-number">{{
                    book.callNo || "ไม่ระบุ"
                  }}</span>
                </span>
              </div>

              <div class="detail-item">
                <span class="detail-label">ปีที่พิมพ์</span>
                <span class="detail-value">{{
                  book.pubbeyear || "ไม่ระบุ"
                }}</span>
              </div>

              <div class="detail-item">
                <span class="detail-label">ภาษา</span>
                <span class="detail-value">
                  <span
                    class="language-tag"
                    :class="getLanguageClass(book.lang)"
                  >
                    {{ getLanguageText(book.lang) }}
                  </span>
                </span>
              </div>

              <div class="detail-item">
                <span class="detail-label">copy</span>
                <span class="detail-value">{{ book.copy || "ไม่ระบุ" }}</span>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="empty-state">
          <div class="empty-state-icon">📚</div>
          <p>ไม่พบหนังสือที่คล้ายกัน</p>
        </div>
      </div>

      <div class="popup-footer">
        <button class="btn btn-secondary" @click="cancelRequest">
          <i class="fa-solid fa-xmark"></i> ยกเลิกคำขอ
        </button>
        <button class="btn btn-primary" @click="confirmRequest">
          <i class="fa-solid fa-check"></i> ส่งคำขอต่อไป
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const props = defineProps({
  similarBooks: {
    type: Array,
    default: () => [],
  },
  visible: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["confirm", "cancel"]);

// Helper functions
const getSimilarityClass = (similarity) => {
  if (similarity >= 0.8) return "similarity-high";
  if (similarity >= 0.6) return "similarity-medium";
  return "similarity-low";
};

const getLanguageClass = (lang) => {
  switch (lang?.toLowerCase()) {
    case "th":
      return "lang-th";
    case "en":
      return "lang-en";
    default:
      return "lang-other";
  }
};

const getLanguageText = (lang) => {
  switch (lang?.toLowerCase()) {
    case "th":
      return "ไทย";
    case "en":
      return "English";
    case "zh":
      return "中文";
    case "ja":
      return "日本語";
    default:
      return lang || "ไม่ระบุ";
  }
};

function cancelRequest() {
  emit("cancel");
}

function confirmRequest() {
  emit("confirm");
}

// Handle ESC key
const handleEscKey = (e) => {
  if (e.key === "Escape" && props.visible) {
    cancelRequest();
  }
};

onMounted(() => {
  document.addEventListener("keydown", handleEscKey);
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleEscKey);
});
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(5px);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.popup {
  background: white;
  border-radius: 16px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
  max-width: 900px;
  width: 90%;
  max-height: 80vh;
  overflow: hidden;
  animation: slideIn 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);

  display: flex;
  flex-direction: column;
}

.popup-header {
  background: linear-gradient(135deg, #7070ff, #1cb5e0);
  color: white;
  padding: 24px;
  text-align: center;
}

.popup-header h2 {
  font-size: 24px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.warning-icon {
  font-size: 28px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.popup-content {
  padding: 0;
  max-height: none;
  overflow-y: auto;
  flex: 1 1 auto;
  overflow-y: auto;
  padding: 0;
}

.popup-content::-webkit-scrollbar {
  width: 8px;
}

.popup-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.popup-content::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 4px;
}

.popup-content::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

.book-item {
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s ease;
}

.book-item:hover {
  background-color: #f8f9ff;
}

.book-item:last-child {
  border-bottom: none;
}

.book-header {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 12px;
}

.book-number {
  background: linear-gradient(135deg, #4ca1af, #c4e0e5);
  color: white;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
  flex-shrink: 0;
}

.book-title {
  flex: 1;
}

.book-title h3 {
  color: #2c3e50;
  font-size: 18px;
  line-height: 1.4;
  margin-bottom: 4px;
}

.similarity-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.similarity-high {
  background: linear-gradient(135deg, #e74c3c, #c0392b);
}

.similarity-medium {
  background: linear-gradient(135deg, #f39c12, #e67e22);
}

.similarity-low {
  background: linear-gradient(135deg, #3498db, #2980b9);
}

.book-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  margin-top: 12px;
}

/* Responsive สำหรับหน้าจอเล็ก */
@media (max-width: 768px) {
  .book-details {
    grid-template-columns: repeat(2, 1fr); /* แสดง 2 คอลัมน์ */
    gap: 8px; /* ลด gap */
    padding: 12px; /* ลด padding */
  }

  .popup {
    max-width: 95%; /* ขยายให้เต็มหน้าจอมากขึ้น */
    width: 95%;
    padding: 16px; /* ลด padding */
    max-height: 80vh; /* ลดความสูง */
    overflow: hidden;
  }

  .popup h2 {
    font-size: 18px; /* ลดขนาดหัวข้อ */
    margin-bottom: 12px;
  }
}

@media (max-width: 480px) {
  .book-details {
    grid-template-columns: 1fr; /* แสดง 1 คอลัมน์ในมือถือเล็ก */
    gap: 6px;
    padding: 8px;
  }

  .popup {
    max-width: 98%;
    width: 98%;
    padding: 12px;
    max-height: 80vh;
    border-radius: 8px; /* ลด border radius */
  }

  .popup h2 {
    font-size: 16px;
    margin-bottom: 8px;
  }

  .book-item {
    margin-bottom: 12px;
    padding: 8px;
  }

  .popup-actions {
    flex-direction: column; /* ปุ่มเรียงแนวตั้ง */
    gap: 8px;
  }

  .popup-actions button {
    width: 100%; /* ปุ่มเต็มความกว้าง */
    padding: 12px;
  }
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.detail-label {
  font-size: 12px;
  color: #666;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-value {
  color: #2c3e50;
  font-weight: 500;
  word-break: break-word;
}

.call-number {
  background: #e8f4fd;
  color: #1e88e5;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: "Courier New", monospace;
  font-weight: bold;
}

.language-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: bold;
  text-transform: uppercase;
}

.lang-th {
  background: #e8f5e8;
  color: #2e7d32;
}

.lang-en {
  background: #e3f2fd;
  color: #1565c0;
}

.lang-other {
  background: #fce4ec;
  color: #c2185b;
}

.popup-footer {
  flex-shrink: 0;
  padding: 24px;
  background: #f8f9fa;
  display: flex;
  gap: 12px;
  justify-content: center;
  border-top: 1px solid #eee;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
}

.btn-primary {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
}

.btn-primary:hover {
  background: linear-gradient(135deg, #218838, #1ea080);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(40, 167, 69, 0.3);
}

.btn-secondary {
  background: linear-gradient(135deg, #6c757d, #5a6268);
  color: white;
}

.btn-secondary:hover {
  background: linear-gradient(135deg, #5a6268, #495057);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(108, 117, 125, 0.3);
}

.empty-state {
  text-align: center;
  padding: 40px 24px;
  color: #666;
}

.empty-state-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

@media (max-width: 768px) {
  .popup {
    width: 95%;
    max-height: 85vh;
  }

  .book-details {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .popup-footer {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
