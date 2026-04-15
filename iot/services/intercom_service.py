import json
from datetime import datetime
from config import settings
from utils.logger import log


class IntercomService:

    def __init__(self, mqtt_client, camera_service, relay_controller):
        self.mqtt = mqtt_client
        self.camera = camera_service
        self.relay = relay_controller

    def handle_ring(self, stan_id):
        log(f"Pozvonjeno na stan {stan_id}")

        # 1. slika
        self.camera.capture_image(stan_id)

        # 2. MQTT poruka
        payload = {
            "stan": stan_id,
            "timestamp": datetime.now().strftime("%H:%M:%S")
        }

        self.mqtt.publish(settings.TOPIC_POZIVI, json.dumps(payload))

    def handle_mqtt_message(self, client, userdata, msg):
        message = msg.payload.decode()
        log(f"Primljena komanda: {message}")

        if message == "OPEN":
            self.relay.open_gate()