// stores/useAuthStore.ts
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  persist: true, // ให้จำค่าไว้ใน localStorage ด้วย
  state: () => ({
    isAuthenticated: false,
    username: "",
    role: "",
    favBooks: [] // ✅ เพิ่ม favBooks
  }),
  actions: {
    login(username: string, role: string, favBooks: any[] = []) {
      this.isAuthenticated = true;
      this.username = username;
      this.role = role;
      this.favBooks = favBooks;

      localStorage.setItem(
        "auth",
        JSON.stringify({
          isAuthenticated: true,
          username,
          role,
          favBooks,
        })
      );
    },

    logout() {
      this.isAuthenticated = false;
      this.username = "";
      this.role = "";
      this.favBooks = [];

      localStorage.removeItem("auth");
    },

    loadFromStorage() {
      const saved = localStorage.getItem("auth");
      if (saved) {
        const data = JSON.parse(saved);
        this.isAuthenticated = data.isAuthenticated;
        this.username = data.username;
        this.role = data.role;
        this.favBooks = data.favBooks || [];
      }
    },

    setFavBooks(favBooks: any[]) {
      this.favBooks = favBooks;

      // อัปเดตใน localStorage ด้วย
      const saved = localStorage.getItem("auth");
      if (saved) {
        const data = JSON.parse(saved);
        data.favBooks = favBooks;
        localStorage.setItem("auth", JSON.stringify(data));
      }
    },
  },
});
