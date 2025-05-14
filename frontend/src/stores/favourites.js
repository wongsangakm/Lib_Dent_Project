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
        const response = await axios.get("http://localhost:8080/api/books", {
          credentials: "include",
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
        id: book.id,
        isFavorited: false,
        isLoading: false,
      }));
    },
    async fetchFavourites() {
      try {
        const res = await fetch("http://localhost:8080/api/favorites/favbooks", {
          credentials: "include",
        });
        if (!res.ok) throw new Error("Failed to fetch favbooks");
        this.favourites = await res.json();
      } catch (error) {
        console.error("❌ Error loading favourites:", error);
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
      const res = await fetch(`http://localhost:8080/api/favorites/favbooks/${bookId}`, {
        method: "DELETE",
        credentials: "include",
      });
      if (res.ok) {
        this.favourites = this.favourites.filter((b) => b.id !== bookId);
      } else {
        throw new Error("Failed to remove favourite");
      }
    },
  },
});
