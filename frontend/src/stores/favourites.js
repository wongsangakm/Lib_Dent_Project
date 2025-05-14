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
          withCredentials: true,
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
        const res = await fetch("http://localhost:8080/api/auth/favorites", {
          credentials: "include",
        });
        if (!res.ok) throw new Error("Failed to fetch favbooks");
        const data = await res.json();
        this.favourites = data;

        // ทำเครื่องหมายว่าเป็นรายการโปรดใน allBooks ด้วย
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
        const res = await fetch(
          `http://localhost:8080/api/auth/favorites/${bookId}`,
          {
            method: "POST",
            credentials: "include",
          }
        );

        if (!res.ok) {
          throw new Error("Failed to add favorite");
        }

        const book = this.allBooks.find((b) => b.id === bookId);
        if (book && !this.favourites.some((f) => f.id === bookId)) {
          book.isFavorited = true;

          // ✅ สร้าง array ใหม่เพื่อ trigger reactivity
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
