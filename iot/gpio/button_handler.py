from utils.logger import log
from config import settings

class ButtonHandler:

    def __init__(self, intercom_service):
        self.service = intercom_service

    def start(self):
        if settings.TEST_MODE:
            self.console_mode()
        else:
            self.gpio_mode()

    def console_mode(self):
        log("TEST MODE aktivan (unos preko konzole)")

        while True:
            stan = input("Unesi broj stana (ili q za izlaz): ")

            if stan.lower() == "q":
                break

            if stan.isdigit():
                self.service.handle_ring(int(stan))
            else:
                log("Neispravan unos")

    def gpio_mode(self):
        from gpiozero import Button

        buttons = []

        for pin in settings.GPIO_BUTTONS:
            btn = Button(pin)
            btn.when_pressed = lambda p=pin: self.service.handle_ring(p)
            buttons.append(btn)

        log("GPIO dugmad aktivna")

        import signal
        signal.pause()