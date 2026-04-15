import paho.mqtt.client as mqtt
from config import settings
from utils.logger import log


class MQTTClient:

    def __init__(self, on_message_callback):
        self.client = mqtt.Client(
            client_id=settings.CLIENT_ID,
            protocol=mqtt.MQTTv311,
            callback_api_version=mqtt.CallbackAPIVersion.VERSION2
        )

        self.client.on_connect = self.on_connect
        self.client.on_message = on_message_callback

    def on_connect(self, client, userdata, flags, reason_code, properties):
        if reason_code == 0:
            log("MQTT povezan")
            client.subscribe(settings.TOPIC_KOMANDE)
            log(f"Subscribed na {settings.TOPIC_KOMANDE}")
        else:
            log(f"Greska konekcije: {reason_code}")

    def connect(self):
        log(f"Povezujem se na broker {settings.BROKER_IP}...")
        self.client.connect(settings.BROKER_IP, settings.PORT, 60)
        self.client.loop_start()

    def publish(self, topic, payload):
        log(f"Saljem MQTT → {topic}: {payload}")
        self.client.publish(topic, payload)
