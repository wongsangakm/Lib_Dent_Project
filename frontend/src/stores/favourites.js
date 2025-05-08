import { defineStore } from "pinia";
import axios from "axios";

import { defineStore } from "pinia";
import axios from "axios";

export const useFavouritesStore = defineStore("favourites", {
  state: () => ({
    allBooks: [],
    favourites: [],
    userId: "1",
  }),
  actions: {
    async fetchAllBooks() {
      try {
        const response = await axios.get("http://localhost:8080/api/books");
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
        id: book.id,
        isFavorited: false,
        isLoading: false,
      }));
    },
    async fetchFavourites() {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const storedFavourites = this.allBooks.filter(
          (book) => book.isFavorited
        );
        this.favourites = storedFavourites;
      } catch (error) {
        console.error("Error fetching favourites:", error.message);
      }
    },
    async addFavourite(bookId) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const book = this.allBooks.find((b) => b.id === bookId);
        console.log("✅ Book in store", book); // เช็คค่าจริง
        if (book && !this.favourites.some((fav) => fav.id === bookId)) {
          book.isFavorited = true;
          this.favourites.push({ ...book });
          return { success: true };
        }
        return { success: false };
      } catch (error) {
        console.error("Error adding favourite:", error.message);
      }
    }
    
    ,
    async removeFavourite(bookId) {
      try {
        await axios.post(`/api/actions/unfav/${bookId}`, {}, { withCredentials: true });
        await this.fetchFavourites(); // รีเฟรชรายการ
      } catch (error) {
        console.error("Error removing favourite:", error.message);
      }
    },
  },
});
