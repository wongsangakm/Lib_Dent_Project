import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    username: null,
    role: null,
    isAuthenticated: false,
    favBooks: [] // ✅ เพิ่ม favBooks
  }),
  actions: {
    login(username, role, favBooks) {
      this.username = username;
      this.role = role;
      this.isAuthenticated = true;
      this.favBooks = favBooks; // ✅ อัปเดต favBooks หลังล็อกอิน
    },
    logout() {
      this.username = null;
      this.role = null;
      this.isAuthenticated = false;
      this.favBooks = []; // ✅ รีเซ็ตรายการโปรด
    },
    setFavBooks(favBooks) {
      this.favBooks = favBooks;
    }
  }
});
