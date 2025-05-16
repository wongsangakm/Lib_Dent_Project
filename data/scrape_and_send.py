import requests
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# ==== CONFIG ====
BASE_URL = "http://localhost:8080/api/books"

# ==== SETUP SELENIUM =====
options = Options()
options.add_argument('--headless')
options.add_argument('--disable-gpu')
driver = webdriver.Chrome(options=options)

# ==== STEP 1: โหลดรายการหนังสือทั้งหมด ====
response = requests.get(BASE_URL)
books = response.json()

for book in books:
    print("🔎 book =", book)
    isbn = book.get("isbn")
    url = book.get("url")
    has_description = book.get("description")

    if not isbn:
        print("❌ ไม่มี ISBN:", book)
        continue

    if not url or has_description:
        print(f"⏩ SKIP: ISBN {isbn} ไม่มี URL หรือมี description แล้ว")
        continue

    try:
        print(f"🔍 กำลังดึง: {url}")
        driver.get(url)
        wait = WebDriverWait(driver, 10)

        # ลองโครงสร้างแบบที่ 1
        try:
            section = wait.until(EC.presence_of_element_located(
                (By.XPATH, "//h2[@id='about-this-book']/following-sibling::div")
            ))
        except:
            try:
                section = wait.until(EC.presence_of_element_located(
                    (By.XPATH, "//section[@data-title='About this book']/div[contains(@class, 'c-book-section')]")
                ))
            except:
                print(f"❌ ไม่เจอ description section ใน {url}")
                continue

        # ลองดึง <p> ถ้ามี
        paragraphs = section.find_elements(By.TAG_NAME, "p")
        description = "\n".join([p.text for p in paragraphs]).strip()

        # ถ้าไม่มี <p> หรือข้อความว่าง ให้ fallback ไปใช้ section.text
        if not description:
            description = section.text.strip()

        if not description:
            print(f"⚠️ description ว่าง: {url}")
            continue

        if len(description) > 2000:
            description = description[:2000]

        # ==== ส่ง PUT ด้วย ISBN ====
        put_url = f"{BASE_URL}/{isbn}/description"
        res = requests.put(put_url, json={"description": description})
        print(f"✅ ISBN {isbn} updated: {res.status_code}")
        time.sleep(1)

    except Exception as e:
        print(f"❌ Error on ISBN {isbn}: {str(e)}")

driver.quit()
