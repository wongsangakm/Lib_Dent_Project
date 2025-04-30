<template>

  <footer class="bg-gradient-to-t from-white to-transparent text-sm text-gray-600 pt-10 pb-4 mt-20">
    <div class="max-w-6xl mx-auto px-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
      <div>
        <Logo />
        <h2 class="text-lg font-bold mt-2">Book Request</h2>
        <p class="text-sm">คณะทันตแพทยศาสตร์ มหาวิทยาลัยขอนแก่น</p>
        <p class="text-sm">123 ถ.มิตรภาพ ต.ในเมือง อ.เมือง จ.ขอนแก่น 40002</p>
        <div class="flex gap-2 mt-2">
          <a href="#"><i class="fa-brands fa-facebook"></i></a>
          <a href="#"><i class="fa-brands fa-twitter"></i></a>
          <a href="#"><i class="fa-brands fa-instagram"></i></a>
        </div>
      </div>
      <div>
        <h3 class="font-bold">สำนักงานคณบดี</h3>
        <p>โทรศัพท์ 043 009 700</p>
        <p>โทรศัพท์ 043 202 405</p>
        <p>โทรศัพท์ 043 202 862</p>
      </div>
      <div>
        <h3 class="font-bold">โรงพยาบาลทันตกรรม</h3>
        <p>โทรศัพท์ 043 203 704</p>
        <h3 class="font-bold mt-4">คลินิกพิเศษ</h3>
        <p>โทรศัพท์ 043 203 623</p>
        <p>โทรศัพท์ 086 458 2164</p>
      </div>
      <div>
        <h3 class="font-bold">ความโปร่งใส</h3>
        <ul>
          <li><a href="#" class="hover:underline">ITA คุณธรรม/ความโปร่งใส</a></li>
          <li><a href="#" class="hover:underline">การจัดซื้อจัดจ้าง</a></li>
          <li><a href="#" class="hover:underline">Q&A/ข้อร้องเรียน</a></li>
        </ul>
      </div>
    </div>
    <p class="text-center mt-10">All Right Reserved. Copyright © 2024</p>
  </footer>
</template>

<script setup>
import Logo from './Logo.vue';
</script>