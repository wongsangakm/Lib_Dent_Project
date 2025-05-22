import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/useAuthStore"; // 🔐 auth store
import Home from "@/pages/Homepage.vue";
import Profile from "@/pages/Profile.vue";
import Loginpage from "@/pages/Loginpage.vue";
import BookDetail from "@/pages/BookDetail.vue";
import Favbooks from "@/pages/Favbooks.vue";
import AdminDashboard from "@/pages/AdminDashboard.vue";
import AdminAllbooks from "@/pages/AdminAllbooks.vue";
import AdminBookDetail from "@/pages/AdminBookDetail.vue";
import AdminAddBook from "@/pages/AdminAddBook.vue";
import AdminRequestTable from "@/pages/AdminRequestTable.vue";
import AdminRequestDetail from "@/pages/AdminRequestDetail.vue";
import AdminRoundExport from "@/pages/AdminRoundExport.vue";
import AdminLayout from "@/pages/AdminLayout.vue";
import AdminAddUser from "@/pages/AdminAddUser.vue";
import ChangePassword from "@/pages/ChangePassword.vue";
import AdminResetPasswords from "@/pages/AdminResetPassword.vue";
import FavoritesDashboard from "@/pages/FavoritesDashboard.vue";
import AdditionalRequest from "@/pages/AdditionalRequest.vue";
import AdditionalRequestDetail from "@/pages/AdditionalRequestDetail.vue";
import UserRequestDetail from "@/pages/UserRequestDetail.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", name: "Homepage", component: Home },
    { path: "/login", name: "Loginpage", component: Loginpage },
    { path: "/profile", name: "Profile", component: Profile },
    { path: "/book/:id", component: BookDetail },
    { path: "/favbooks", name: "Favbooks", component: Favbooks },
    { path: "/favbooks", name: "Favbooks", component: Favbooks },
    {
      path: "/change-password",
      name: "ChangePassword",
      component: ChangePassword,
    },
    {
      path: "/favorites-dashboard",
      name: "FavoritesDashboard",
      component: FavoritesDashboard,
    },
    { path: "/additional", name:"Additional", component: AdditionalRequest},
    { path: "/request/:id", name: "Request", component: UserRequestDetail},

    {
      path: "/admin",
      component: AdminLayout,
      children: [
        { path: "dashboard", component: AdminDashboard },
        { path: "allbooks", component: AdminAllbooks },
        { path: "allbooks/add-book", component: AdminAddBook },
        { path: "request-table", component: AdminRequestTable },
        { path: "request/:id", component: AdminRequestDetail },
        { path: "/admin/additional-request/:id", component: AdditionalRequestDetail},
        { path: "adminbookdetail/:id", component: AdminBookDetail },
        { path: "round-export", component: AdminRoundExport },
        { path: "", redirect: "/admin/dashboard" },
        { path: "AdminAddUser", component: AdminAddUser },
        {
          path: "/admin/reset-password",
          component: AdminResetPasswords,
        },
      ],
    },
  ],
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return { el: to.hash, behavior: "smooth" };
    }
    return { top: 0 };
  },
});

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore();
  await new Promise((resolve) => setTimeout(resolve, 0));
  // 1. ป้องกัน guest เข้า favbooks หรือ profile
  const mustLoginPages = ["/favbooks"];
  if (mustLoginPages.includes(to.path) && !auth.isAuthenticated) {
    alert("กรุณาล็อกอินก่อนเข้าหน้านี้");
    return next("/login");
  }

  // 2. ป้องกัน MEMBER เข้าหน้า admin
  if (to.path.startsWith("/admin") && auth.role !== "ADMIN") {
    alert("หน้านี้สำหรับผู้ดูแลระบบเท่านั้น");
    return next("/");
  }

  next(); // ✅ อนุญาตผ่าน
});

export default router;
