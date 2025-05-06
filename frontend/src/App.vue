<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { ref } from "vue";
import Loginpage from "./pages/Loginpage.vue";
import bookdetail from "./pages/BookDetail.vue";
import Homepage from "./pages/Homepage.vue";
import Favbooks from "./pages/Favbooks.vue";
import AdminDashboard from "./pages/AdminDashboard.vue";
import AdminAllbooks from "./pages/AdminAllbooks.vue";
import AdminBookDetail from "./pages/AdminBookDetail.vue";
import AdminRequestTable from "./pages/AdminRequestTable.vue";
import AdminRequestDetail from "./pages/AdminRequestDetail.vue";
import AdminRoundExport from "./pages/AdminRoundExport.vue";
import AdminLayout from "./pages/AdminLayout.vue";
</script>

<style scoped></style>
