import requests
from utils.logger import log

def upload_image(image_path):
    url = "http://192.168.1.100:8080/api/upload"

    try:
        with open(image_path, "rb") as f:
            files = {"file": f}
            response = requests.post(url, files=files, timeout=5)

        if response.status_code == 200:
            return response.json().get("url")

        log(f"Upload failed: {response.status_code}")
        return None

    except Exception as e:
        log(f"Upload error: {e}")
        return None
