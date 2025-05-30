import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  persist: true,
  state: () => ({
    isAuthenticated: false,
    username: "",
    role: "",
    jwt: "",
    favBooks: [] as any[],
  }),
  actions: {
    login(username: string, role: string, jwt: string, favBooks: any[] = []) {
      this.isAuthenticated = true;
      this.username = username;
      this.role = role;
      this.jwt = jwt;
      this.favBooks = favBooks;
    },

    logout() {
      this.isAuthenticated = false;
      this.username = "";
      this.role = "";
      this.jwt = "";
      this.favBooks = [];
    },

    setFavBooks(favBooks: any[]) {
      this.favBooks = favBooks;
    },

    getAuthHeader() {
      return this.jwt ? { Authorization: `Bearer ${this.jwt}` } : {};
    },
  },
});
