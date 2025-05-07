import { defineStore } from "pinia";

const mockBooks = [
  {
    id: 1,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
    price: "150.00 $",
    publisher: "Springer",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 2,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Wild Woman’s Way",
    price: "150.00 $",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 3,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "In The Garden",
    price: "150.00 $",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 4,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 1",
    price: "15.00 $",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 101,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 1",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
    description: "During dental/orthodontic imaging, the CBCT scanner rotates around the patient's head, obtaining up to nearly 600 distinct images. For interventional radiology, the patient is positioned offset to the table so that the region of interest is centered in the field of view for the cone beam. A single 200 degree rotation over the region of interest acquires a volumetric data set. The scanning software collects the data and reconstructs it, producing what is termed a digital volume composed of three-dimensional voxels of anatomical data that can then be manipulated and visualized with specialized software.[2][3] CBCT shares many similarities with traditional (fan beam) CT however there are important differences, particularly for reconstruction. CBCT has been described as the gold standard for imaging the oral and maxillofacial area."
  },
  {
    id: 102,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 2",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 103,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 3",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 104,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 4",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 105,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 5",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 201,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 1",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 202,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 2",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 203,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 3",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 204,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 4",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 205,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 5",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 301,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 1",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 302,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 2",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 303,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 3",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 304,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 4",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 305,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 5",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
];

const mockFavourites = [
    {
      id: 999,
      coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
      title: "Test Layout Book",
      author: "Suk",
      price: "99.99 $",
      publisher: "Springer",
      isFavorited: true,
      isLoading: false,
    },
    {
        id: 999,
        coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
        title: "Test Layout Book",
        author: "Suk",
        price: "99.99 $",
        publisher: "Springer",
        isFavorited: true,
        isLoading: false,
      }
,
{
    id: 999,
    coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Test Layout Book",
    author: "Suk",
    price: "99.99 $",
    publisher: "Springer",
    isFavorited: true,
    isLoading: false,
  }


  ];

export const useFavouritesStore = defineStore("favourites", {
  state: () => ({
    allBooks: [...mockBooks],
    favourites: [...mockFavourites],
    userId: "1",
  }),
  actions: {
    async fetchFavourites() {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const storedFavourites =
          mockFavourites.length > 0
            ? mockFavourites
            : this.allBooks.filter((book) => book.isFavorited);
        this.favourites = storedFavourites;
      } catch (error) {
        console.error("Error fetching favourites:", error.message);
      }
    },
    async addFavourite(bookId) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const book = this.allBooks.find((b) => b.id === bookId);
        if (book && !this.favourites.some((fav) => fav.id === bookId)) {
          book.isFavorited = true;
          this.favourites.push({ ...book });
          return { success: true };
        }
        return { success: false };
      } catch (error) {
        console.error("Error adding favourite:", error.message);
        return { success: false };
      }
    },
    async removeFavourite(bookId) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const book = this.allBooks.find((b) => b.id === bookId);
        if (book) {
          book.isFavorited = false;
          this.favourites = this.favourites.filter((fav) => fav.id !== bookId);
          return { success: true };
        }
        return { success: false };
      } catch (error) {
        console.error("Error removing favourite:", error.message);
        return { success: false };
      }
    },
  },
});

import { defineStore } from "pinia";

const mockBooks = [
  {
    id: 1,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
    price: "150.00 $",
    publisher: "Springer",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 2,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Wild Woman’s Way",
    price: "150.00 $",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 3,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "In The Garden",
    price: "150.00 $",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 4,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 1",
    price: "15.00 $",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 101,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 1",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 102,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 2",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 103,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 3",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 104,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 4",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 105,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Women's Murder 5",
    price: "$10.00",
    publisher: "Wiley",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 201,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 1",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 202,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 2",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 203,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 3",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 204,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 4",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 205,
    coverImage:
      "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "The Garden 5",
    price: "$12.00",
    publisher: "Elsevier",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 301,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 1",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 302,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 2",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 303,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 3",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 304,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 4",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
  {
    id: 305,
    coverImage: "https://via.placeholder.com/150x200",
    title: "Fugitive 5",
    price: "$15.00",
    publisher: "CRC Press",
    isFavorited: false,
    isLoading: false,
  },
];

const mockFavourites = [
    {
      id: 999,
      coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
      title: "Test Layout Book",
      author: "Suk",
      price: "99.99 $",
      publisher: "Springer",
      isFavorited: true,
      isLoading: false,
    },
    {
        id: 999,
        coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
        title: "Test Layout Book",
        author: "Suk",
        price: "99.99 $",
        publisher: "Springer",
        isFavorited: true,
        isLoading: false,
      }
,
{
    id: 999,
    coverImage: "https://media.springernature.com/full/springer-static/cover-hires/book/978-3-031-25480-2?as=webp",
    title: "Test Layout Book",
    author: "Suk",
    price: "99.99 $",
    publisher: "Springer",
    isFavorited: true,
    isLoading: false,
  }


  ];

export const useFavouritesStore = defineStore("favourites", {
  state: () => ({
    allBooks: [...mockBooks],
    favourites: [...mockFavourites],
    userId: "1",
  }),
  actions: {
    async fetchFavourites() {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const storedFavourites =
          mockFavourites.length > 0
            ? mockFavourites
            : this.allBooks.filter((book) => book.isFavorited);
        this.favourites = storedFavourites;
      } catch (error) {
        console.error("Error fetching favourites:", error.message);
      }
    },
    async addFavourite(bookId) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const book = this.allBooks.find((b) => b.id === bookId);
        if (book && !this.favourites.some((fav) => fav.id === bookId)) {
          book.isFavorited = true;
          this.favourites.push({ ...book });
          return { success: true };
        }
        return { success: false };
      } catch (error) {
        console.error("Error adding favourite:", error.message);
        return { success: false };
      }
    },
    async removeFavourite(bookId) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 500));
        const book = this.allBooks.find((b) => b.id === bookId);
        if (book) {
          book.isFavorited = false;
          this.favourites = this.favourites.filter((fav) => fav.id !== bookId);
          return { success: true };
        }
        return { success: false };
      } catch (error) {
        console.error("Error removing favourite:", error.message);
        return { success: false };
      }
    },
  },
});
