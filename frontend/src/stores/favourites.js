import { defineStore } from "pinia";
import axios from "axios";
import { useAuthStore } from "@/stores/useAuthStore";
const baseURL = import.meta.env.VITE_API_BASE_URL;

const getAuthHeader = () => {
  const token = localStorage.getItem("jwt");
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const useFavouritesStore = defineStore("favourites", {
  state: () => ({
    allBooks: [],
    favourites: [],
    userId: "1",
  }),
  actions: {
    getAuthHeader() {
      const authStore = useAuthStore(); // ✅ ดึงจาก store ที่ reactive
      return authStore.jwt ? { Authorization: `Bearer ${authStore.jwt}` } : {};
    },
    async fetchAllBooks() {
      try {
        const response = await axios.get(`${baseURL}/api/books`, {
          headers: getAuthHeader(),
        });
        this.allBooks = response.data.map((book) => ({
          ...book,
          isFavorited: false,
          isLoading: false,
        }));
      } catch (error) {
        console.error("Error fetching books:", error.message);
      }
    },

    setAllBooks(books) {
      this.allBooks = books.map((book) => ({
        ...book,
        isFavorited: false,
        isLoading: false,
      }));
    },

    async fetchFavourites() {
      try {
        const res = await fetch(`${baseURL}/api/auth/favorites`, {
          method: "GET",
          headers: {
            Accept: "application/json",
            ...this.getAuthHeader(),
          },
        });

        if (!res.ok) throw new Error("Failed to fetch favbooks");
        const data = await res.json();
        this.favourites = data;

        this.allBooks = this.allBooks.map((book) => ({
          ...book,
          isFavorited: data.some((fav) => fav.id === book.id),
        }));
      } catch (error) {
        console.error("❌ Error loading favourites:", error);
      }
    },

    async addFavourite(bookId) {
      try {
        const res = await fetch(`${baseURL}/api/auth/favorites/${bookId}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            ...getAuthHeader(),
          },
        });

        if (!res.ok) throw new Error("Failed to add favorite");

        const book = this.allBooks.find((b) => b.id === bookId);
        if (book && !this.favourites.some((f) => f.id === bookId)) {
          book.isFavorited = true;
          this.favourites = [...this.favourites, { ...book }];
        }

        return { success: true };
      } catch (error) {
        console.error("❌ Error adding to favorites:", error.message);
        return { success: false };
      }
    },
  },
});
