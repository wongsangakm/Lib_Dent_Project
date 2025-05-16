<template>
  <footer
    class="bg-white/30 w-full bg-transparent text-sm text-gray-600 pt-5 md:pt-10 px-4"
  >
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
      <!-- คอลัมน์แรก - โลโก้และข้อมูลหลัก -->
      <div class="flex flex-col gap-2 items-center sm:items-start">
        <div>
          <Logo :size="'w-24 h-24 md:w-28 md:h-28'" />
        </div>
        <div class="text-center sm:text-left">
          <h2 class="text-base md:text-lg font-bold text-purple-700 mt-1">
            Book Request
          </h2>
          <p class="text-sm md:text-base mt-1 font-bold text-purple-700">
            คณะทันตแพทยศาสตร์
          </p>
          <p class="text-sm md:text-base mt-1 text-purple-700">
            มหาวิทยาลัยขอนแก่น
          </p>
          <p class="text-xs md:text-sm mt-1 text-slate-400">
            123 ถ.มิตรภาพ ต.ในเมือง อ.เมือง จ.ขอนแก่น 40002
          </p>
          <p
            class="text-sm md:text-base font-semibold text-purple-700 mb-2 mt-4"
          >
            Follow Us
          </p>
          <div class="flex gap-3 justify-center sm:justify-start">
            <a
              href="https://www.facebook.com/dentistrykku/"
              class="text-base text-center w-8 h-8 md:w-10 md:h-10 bg-purple-200 text-purple-900 flex items-center justify-center rounded-xl hover:bg-purple-300 transition"
            >
              <i class="fab fa-facebook-f"></i>
            </a>
            <a
              href="https://www.youtube.com/channel/UCcrnS8WWDZsyrPfwh_rkZ9w"
              class="text-base text-center w-8 h-8 md:w-10 md:h-10 bg-purple-200 text-purple-900 flex items-center justify-center rounded-xl hover:bg-purple-300 transition"
            >
              <i class="fab fa-youtube"></i>
            </a>
            <a
              href="https://maps.app.goo.gl/35WenZjnCvA837oWA"
              class="text-base text-center w-8 h-8 md:w-10 md:h-10 bg-purple-200 text-purple-900 flex items-center justify-center rounded-xl hover:bg-purple-300 transition"
            >
              <i class="fas fa-map-marker-alt"></i>
            </a>
          </div>
        </div>
      </div>

      <!-- คอลัมน์ที่สอง - สำนักงานคณบดี -->
      <div class="text-center">
        <h3 class="text-base md:text-lg font-bold text-purple-700 mb-2">
          สำนักงานคณบดี
        </h3>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรศัพท์ 043 009 700
        </p>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรศัพท์ 043 202 405
        </p>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรสาร 043 202 862
        </p>
      </div>

      <!-- คอลัมน์ที่สาม - โรงพยาบาลทันตกรรม -->
      <div class="text-center">
        <h3 class="text-base md:text-lg font-bold text-purple-700 mb-2">
          โรงพยาบาลทันตกรรม
        </h3>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรศัพท์ 04203 704
        </p>
        <h3
          class="text-base md:text-lg font-bold text-purple-700 mb-2 mt-4 md:mt-8"
        >
          คลินิกพิเศษ
        </h3>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรศัพท์ 043 203 623
        </p>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          โทรศัพท์ 086 458 2164
        </p>
      </div>

      <!-- คอลัมน์ที่สี่ - ความโปร่งใส -->
      <div class="text-center">
        <h3 class="text-base md:text-lg font-bold text-purple-700 mb-2">
          ความโปร่งใส
        </h3>
        <p class="text-xs md:text-sm mt-1 text-purple-800">
          ITA คุณธรรม/ความโปร่งใส
        </p>
        <p class="text-xs md:text-sm mt-1 text-purple-800">การจัดซื้อจัดจ้าง</p>
        <p class="text-xs md:text-sm mt-1 text-purple-800">Q&A/ข้อร้องเรียน</p>
      </div>
    </div>

    <!-- ลิขสิทธิ์ -->
    <p class="text-center mt-6 pb-4 text-xs md:text-sm">
      All Right Reserved. Copyright © 2024
    </p>
  </footer>
</template>

<script setup>
import Logo from "./Logo.vue";
</script>
