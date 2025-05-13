import { createRouter, createWebHistory } from "vue-router";
import Home from "../pages/Homepage.vue";
import Profile from "@/pages/Profile.vue";
import Loginpage from "@/pages/Loginpage.vue";
import BookDetail from "@/pages/BookDetail.vue";
import Homepage from "@/pages/Homepage.vue";
import Favbooks from "@/pages/Favbooks.vue";
import AdminDashboard from "@/pages/AdminDashboard.vue";
import AdminAllbooks from "@/pages/AdminAllbooks.vue";
import AdminBookDetail from "@/pages/AdminBookDetail.vue";
import AdminAddBook from "@/pages/AdminAddBook.vue";
import AdminRequestTable from "@/pages/AdminRequestTable.vue";
import AdminRequestDetail from "@/pages/AdminRequestDetail.vue";
import AdminRoundExport from "@/pages/AdminRoundExport.vue";
import AdminLayout from "@/pages/AdminLayout.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/Profile",
      name: "Profile",
      component: Profile,
    },
    {
      path: "/book/:id",
      component: BookDetail,
    },
    {
      path: "/Login",
      name: "Loginpage",
      component: Loginpage,
    },
    {
      path: "/",
      name: "Homepage",
      component: Homepage,
    },
    {
      path: "/favbooks",
      name: "Favbooks",
      component: Favbooks,
    },
    {
      path: "/dashboard",
      name: "AdminDashboard",
      component: AdminDashboard,
    },
    {
      path: "/adminallbooks",
      name: "AdminAllbooks",
      component: AdminAllbooks,
    },
    {
      path: "/RequestTable",
      name: "AdminRequestTable",
      component: AdminRequestTable,
    },
    {
      path: "/AdminRoundExport",
      name: "AdminRoundExport",
      component: AdminRoundExport,
    },
    {
      path: "/admin",
      component: AdminLayout,
      children: [
        { path: "dashboard", component: AdminDashboard },
        { path: "allbooks", component: AdminAllbooks },
        { path: "allbooks/add-book", component: AdminAddBook }, // moved here
        { path: "request-table", component: AdminRequestTable },
        { path: "request/:id", component: AdminRequestDetail }, // moved here
        { path: "adminbookdetail/:id", component: AdminBookDetail }, // moved here
        { path: "round-export", component: AdminRoundExport },
        { path: "", redirect: "/admin/dashboard" },
      ],
    },
  ],
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return {
        el: to.hash,
        behavior: "smooth",
      };
    }
    return { top: 0 };
  },
});





export default router;
