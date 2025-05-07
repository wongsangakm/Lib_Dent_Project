// src/stores/authStore.js
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    username: null,
    role: null,
    isAuthenticated: false
  }),
  actions: {
    login(username, role) {
      this.username = username;
      this.role = role;
      this.isAuthenticated = true;
    },
    logout() {
      this.username = null;
      this.role = null;
      this.isAuthenticated = false;
    }
  }
});
