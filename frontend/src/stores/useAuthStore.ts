// stores/useAuthStore.ts
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    isAuthenticated: false, // True if user is logged in
    username: "", // Current user's username
    role: "", // Current user's role (e.g., ADMIN, USER)
  }),
  actions: {
    // Called when login is successful
    login(username: string, role: string) {
      this.isAuthenticated = true;
      this.username = username;
      this.role = role;

      // Save to localStorage so login state persists after refresh
      localStorage.setItem(
        "auth",
        JSON.stringify({
          isAuthenticated: true,
          username,
          role,
        })
      );
    },

    // Clear all auth data
    logout() {
      this.isAuthenticated = false;
      this.username = "";
      this.role = "";

      // Remove from localStorage
      localStorage.removeItem("auth");
    },

    // Load auth state from localStorage when app starts
    loadFromStorage() {
      const saved = localStorage.getItem("auth");
      if (saved) {
        const data = JSON.parse(saved);
        this.isAuthenticated = data.isAuthenticated;
        this.username = data.username;
        this.role = data.role;
      }
    },
  },
});
