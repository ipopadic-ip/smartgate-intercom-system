import json
import threading
import os
from datetime import datetime
from config import settings
from utils.logger import log
from services.upload_service import upload_image


class IntercomService:

    def __init__(self, mqtt_client, camera_service, relay_controller):
        self.mqtt = mqtt_client
        self.camera = camera_service
        self.relay = relay_controller

    def handle_ring(self, stan_id):
        log(f"Pozvonjeno na stan {stan_id}")

        #Pokrećemo sve u pozadini (async)
        threading.Thread(
            target=self.process_ring,
            args=(stan_id,),
            daemon=True
        ).start()

    def process_ring(self, stan_id):
        try:
            # 1. Slikanje
            image_path = self.camera.capture_image(stan_id)

            image_url = None

            # 2. Upload slike
            if image_path:
                image_url = upload_image(image_path)

                # Obriši lokalnu sliku
                if os.path.exists(image_path):
                    os.remove(image_path)
                    log(f"Obrisana lokalna slika: {image_path}")

            # 3. MQTT (SAMO JEDNOM, sa kompletnim podacima)
            payload = {
                "stan": stan_id,
                "image_url": image_url,
                "timestamp": datetime.now().strftime("%H:%M:%S")
            }

            self.mqtt.publish(settings.TOPIC_POZIVI, json.dumps(payload))

        except Exception as e:
            log(f"Process ring error: {e}")

    def handle_mqtt_message(self, client, userdata, msg):
        message = msg.payload.decode()
        log(f"Primljena komanda: {message}")

        if message == "OPEN":
            self.relay.open_gate()
