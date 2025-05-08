
import requests
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
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
    print("❌ ไม่สามารถดึงข้อมูลหนังสือ:", str(e))
    driver.quit()
    exit(1)

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
        driver.get("https://www.google.com/")
        search_box = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.NAME, "q"))
        )
        search_box.send_keys(f"{isbn} site:amazon.com")
        search_box.send_keys(Keys.RETURN)

        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "a"))
        )
        links = driver.find_elements(By.CSS_SELECTOR, "a")
        amazon_link = next((link.get_attribute("href") for link in links if "amazon.com" in link.get_attribute("href")), None)

        if not amazon_link:
            print(f"❌ ไม่พบลิงก์ Amazon สำหรับ ISBN {isbn}")
            continue

        print(f"➡️ ไปยัง: {amazon_link}")
        driver.get(amazon_link)

        time.sleep(2)
        try:
            img_element = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, "#imgTagWrapperId img"))
            )
            img_url = img_element.get_attribute("src")
        except:
            print(f"❌ ไม่พบภาพปกใน Amazon: ISBN {isbn}")
            continue

        if not img_url:
            print(f"❌ URL ของภาพปกว่างเปล่า: ISBN {isbn}")
            continue

        print(f"✅ พบภาพ: {img_url}")

        # PUT ส่งกลับไป
        put_url = f"{BASE_URL}/{isbn}/cover"
        res = requests.put(put_url, json={"coverImage": img_url})
        print(f"📦 PUT status = {res.status_code}")
        time.sleep(1)

    except Exception as e:
        print(f"❌ Error ISBN {isbn}: {e}")

driver.quit()
