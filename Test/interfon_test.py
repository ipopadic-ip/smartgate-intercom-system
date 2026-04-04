from gpiozero import Button
from gpiozero.pins.mock import MockFactory
from gpiozero import Device
import cv2
import time
import os

# Postavljamo virtuelnu fabriku pinova
Device.pin_factory = MockFactory()


svi_gpio_pinovi = [2, 3, 4, 17, 27, 22, 10, 9, 11, 5, 6, 13, 19, 26, 14, 15, 18, 23, 24, 25, 8, 7, 12, 16, 20, 21]


dugmici = {}
mock_pinovi = {}

def akcija_zvona(pin_id):
    print(f"Stan na pinu {pin_id} je pozvonjen!")
    
    # Slikanje
    cap = cv2.VideoCapture(0)
    ret, frame = cap.read()
    if ret:
        ime_slike = f"stan_{pin_id}_posetilac.jpg"
        cv2.imwrite(ime_slike, frame)
        print(f"Slika uslikana i sacuvana kao: {ime_slike}")
        print(f"ID stana je {pin_id}")
    else:
        print("Greska: Kamera nije dostupna.")
    cap.release()

# Inicijalizacija pinova
for p in svi_gpio_pinovi:
    btn = Button(p)
    # Povezujemo svaki pin sa funkcijom i prosledjujemo mu njegov broj kao ID
    btn.when_pressed = lambda p_id=p: akcija_zvona(p_id)
    
    dugmici[p] = btn
    mock_pinovi[p] = Device.pin_factory.pin(p)
    
    
print(f"Sistem nadgleda {len(svi_gpio_pinovi)} mogucih stanova (pinova).")

try:
    while True:
        uneti_id = input("\nUnesi broj pina (ID stana) koji zvoni (ili 'q' za kraj): ")
        
        if uneti_id.lower() == 'q':
            break
            
        if uneti_id.isdigit():
            pin_broj = int(uneti_id)
            if pin_broj in mock_pinovi:
                # Simuliramo "pad napona" na tom konkretnom pinu
                print(f"Simuliram fizicki pritisak na pinu {pin_broj}...")
                mock_pinovi[pin_broj].drive_low()
                time.sleep(0.1)
                mock_pinovi[pin_broj].drive_high()
            else:
                print(f" Pin {pin_broj} nije u listi dozvoljenih GPIO pinova.")
        else:
            print(" Molim te unesi samo broj.")

except KeyboardInterrupt:
    print("\nSistem ugasen.")