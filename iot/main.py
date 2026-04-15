from mqtt.mqtt_client import MQTTClient
from services.intercom_service import IntercomService
from camera.camera_service import CameraService
from gpio.relay_controller import RelayController
from gpio.button_handler import ButtonHandler
from utils.logger import log


def main():

    log("Pokretanje Smart Interfon sistema")

    camera = CameraService()
    relay = RelayController()

    intercom = None

    def on_message(client, userdata, msg):
        intercom.handle_mqtt_message(client, userdata, msg)

    mqtt_client = MQTTClient(on_message)
    mqtt_client.connect()

    intercom = IntercomService(mqtt_client, camera, relay)

    buttons = ButtonHandler(intercom)
    buttons.start()


if __name__ == "__main__":
    main()