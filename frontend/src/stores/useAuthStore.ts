import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  persist: true,
  state: () => ({
    isAuthenticated: false,
    username: "",
    role: "",
    favBooks: [] as any[],
  }),
  actions: {
    login(username: string, role: string, favBooks: any[] = []) {
      this.isAuthenticated = true;
      this.username = username;
      this.role = role;
      this.favBooks = favBooks;
    },

    logout() {
      this.isAuthenticated = false;
      this.username = "";
      this.role = "";
      this.favBooks = [];
    },

    setFavBooks(favBooks: any[]) {
      this.favBooks = favBooks;
    },
  },
});
