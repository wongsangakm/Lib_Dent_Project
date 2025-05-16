import requests
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# ===== CONFIG =====
BASE_URL = "http://localhost:8080/api/books"

# ===== SETUP DRIVER =====
options = Options()
options.add_argument("--headless")
options.add_argument("--disable-gpu")
options.add_argument("--log-level=3")
driver = webdriver.Chrome(options=options)

# ===== LOAD BOOK DATA =====
try:
    response = requests.get(BASE_URL)
    response.raise_for_status()
    books = response.json()
except Exception as e:
    print(f"❌ ไม่สามารถโหลดข้อมูลหนังสือจาก backend ได้: {e}")
    driver.quit()
    exit()

# ===== LOOP BOOKS =====
for book in books:
    isbn = book.get("isbn")
    has_cover = book.get("coverImage")

    if not isbn:
        print("❌ ไม่มี ISBN:", book)
        continue

    if has_cover:
        print(f"⏩ ข้าม ISBN {isbn} ที่มีภาพแล้ว")
        continue

    try:
        print(f"🔍 ค้นหา Amazon: ISBN {isbn}")
        search_url = f"https://www.amazon.com/s?k={isbn}"
        print(f"➡️ ไปยัง Amazon search: {search_url}")
        driver.get(search_url)

        # เข้าหน้าสินค้าเล่มแรก
        first_link = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "div.s-main-slot a.a-link-normal.s-no-outline"))
        )
        product_url = first_link.get_attribute("href")
        print(f"➡️ ไปยังหน้าสินค้า: {product_url}")
        driver.get(product_url)

        # รอดึงภาพปก
        img_element = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "#imgTagWrapperId img"))
        )
        img_url = img_element.get_attribute("src")

        if not img_url:
            print(f"❌ ไม่พบภาพปกใน Amazon: ISBN {isbn}")
            continue

        print(f"✅ พบภาพ: {img_url}")

        # ==== PUT กลับเข้า database ====
        put_url = f"{BASE_URL}/{isbn}/cover"
        res = requests.put(put_url, json={"coverImage": img_url})
        print(f"📦 PUT status = {res.status_code}")
        time.sleep(1)

    except Exception as e:
        print(f"❌ Error ISBN {isbn}: {e}")

driver.quit()
