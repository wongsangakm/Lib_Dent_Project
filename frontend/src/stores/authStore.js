import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isAuthenticated: false,
    username: '',
    role: ''
  }),
  actions: {
    login(username, role) {
      this.isAuthenticated = true
      this.username = username
      this.role = role
    },
    logout() {
      this.isAuthenticated = false
      this.username = ''
      this.role = ''
    }
  }
})
