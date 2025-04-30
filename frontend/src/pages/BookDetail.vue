<template>
  <div
    class="min-h-screen flex flex-col bg-gray-100 items-center"
    :style="{
      backgroundImage: `url(${bgImage})`,
      backgroundPosition: '0% 6%',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat',
    }"
  >
  <div>
    <div
      class="flex flex-col md:flex-row max-w-4xl bg-white rounded-lg shadow-lg p-6"
    >
      <!-- Book Cover -->
      <div class="md:w-1/3 flex justify-center">
        <img
          :src="bookData.coverImage"
          alt="Book Cover"
          class="w-48 h-72 object-cover rounded"
        />
      </div>

      <!-- Book Details -->
      <div class="md:w-2/3 mt-4 md:mt-0 md:ml-6">
        <h1 class="text-2xl font-bold text-gray-800">{{ bookData.title }}</h1>

        <!-- Book Info -->
        <div class="flex flex-wrap mt-2 text-gray-600 text-sm">
          <span class="mr-4"
            ><strong>Author:</strong> {{ bookData.author }}</span
          >
          <span class="mr-4"
            ><strong>Publisher:</strong> {{ bookData.publisher }}</span
          >
          <span class="mr-4"
            ><strong>Publish Date:</strong> {{ bookData.publishDate }}</span
          >
          <span class="text-lg font-semibold text-purple-600">{{
            bookData.price
          }}</span>
        </div>

        <!-- Description -->
        <p class="mt-4 text-gray-600 text-sm leading-relaxed">
          {{ bookData.description }}
        </p>

        <!-- Add to Favourite Button -->
        <button
          @click="addToFavourite"
          class="mt-6 px-4 py-2 bg-purple-600 text-white rounded-full flex items-center"
        >
          <span>Add to Favourite</span>
          <svg
            class="ml-2 w-5 h-5"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
            ></path>
          </svg>
        </button>
      </div>
    </div>
</div>
<Footer class="mt-8" />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import Logo from "@/component/Logo.vue";
import Footer from "@/component/Footer.vue";
import bgImage from "@/image/Background.png";
</script>
<script>
export default {
  name: "App",
  data() {
    return {
      bookData: {
        coverImage: "https://via.placeholder.com/200x300", // URL รูปปกหนังสือ
        title: "Cone Beam CT in Dentists and Medical Radiologists, 1 ed",
        author: "Suk",
        publisher: "Springer",
        publishDate: "2023",
        price: "150.00 $",
        description:
          "The superbly illustrated book is a comprehensive introduction to CBCT and its use for diagnosis of dental conditions and treatment planning in the dental practice. After an introductory chapter on the basics of CBCT scans, detailed description is provided on dental anatomic structures and their appearance on CBCT. Various common dental disorders are shown and discussed, as well as signs of malignancy. The book will arm the reader with fundamental knowledge of the radiological appearance of dento-alveolar anatomy and provide guidance on the interpretation of artefacts, disease and some well-known anatomical variations. While it is intended primarily for dentists, this book will be of high value for multiple other health care professionals including medical radiologists. Videos via app: download the SN More Media app for free, scan a link with play button and access videos directly on your smartphone or tablet.",
      },
    };
  },
  methods: {
    addToFavourite() {
      const bookId = "12345"; // ตัวอย่าง ID ของหนังสือ (ควรมาจาก database)
      console.log(`Adding book with ID: ${bookId} to favourites`);

      // ตัวอย่างการเรียก API ไปยัง backend (ในอนาคต)
      /*
        fetch("/api/favourites", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ bookId: bookId }),
        })
          .then((response) => response.json())
          .then((data) => {
            console.log("Book added to favourites:", data);
            alert("Added to favourites!");
          })
          .catch((error) => {
            console.error("Error adding to favourites:", error);
          });
        */
    },
  },
};
</script>

<style scoped>
/* สามารถเพิ่มสไตล์เพิ่มเติมได้ที่นี่ */
</style>
