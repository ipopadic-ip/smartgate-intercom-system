import time
from config import settings
from utils.logger import log

try:
    from gpiozero import OutputDevice
    REAL_GPIO = True
except:
    REAL_GPIO = False


class RelayController:

    def __init__(self):
        if REAL_GPIO:
            self.relay = OutputDevice(settings.RELAY_PIN, active_high=True, initial_value=False)
        else:
            self.relay = None

    def open_gate(self):
        log("OTVARAM KAPIJU")

        if REAL_GPIO:
            self.relay.on()
            time.sleep(1)
            self.relay.off()
        else:
            log("(SIMULACIJA) Relej uključen 1s")
            time.sleep(1)