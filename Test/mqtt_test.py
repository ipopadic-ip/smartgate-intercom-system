import paho.mqtt.client as mqtt
import time

# === KONFIGURACIJA ===
BROKER_IP = "192.168.1.16" 
PORT = 1883
TOPIC = "smartgate/test"

# Funkcija koja se poziva kada se povezemo na broker
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Uspesno povezan na Broker!")
    else:
        print(f"Greska pri povezivanju, kod: {rc}")

# Kreiranje klijenta
client = mqtt.Client()
client.on_connect = on_connect

try:
    print(f"Pokusavam povezivanje na {BROKER_IP}...")
    client.connect(BROKER_IP, PORT, 60)
except Exception as e:
    print(f"Ne mogu da se povezem: {e}")
    exit()

# Pokretanje u pozadini
client.loop_start()

try:
    while True:
        poruka = f"Pozdrav! Vreme: {time.strftime('%H:%M:%S')}"
        print(f"Saljem: {poruka}")
        client.publish(TOPIC, poruka)
        time.sleep(5)
except KeyboardInterrupt:
    print("\nZaustavljam skriptu...")
    client.loop_stop()
    client.disconnect()