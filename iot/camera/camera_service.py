import cv2
import os
from datetime import datetime
from config import settings
from utils.logger import log

class CameraService:

    def __init__(self):
        self.camera_index = settings.CAMERA_INDEX

    def capture_image(self, stan_id):
        cap = cv2.VideoCapture(self.camera_index)

        if not cap.isOpened():
            log("Kamera nije dostupna")
            return None

        ret, frame = cap.read()

        if ret:
            folder = "images"
            os.makedirs(folder, exist_ok=True)

            filename = f"{folder}/stan_{stan_id}_{datetime.now().strftime('%H%M%S')}.jpg"
            cv2.imwrite(filename, frame)

            log(f"Slika sacuvana: {filename}")
            cap.release()
            return filename
        else:
            log("Ne mogu da uhvatim frame")
            cap.release()
            return None