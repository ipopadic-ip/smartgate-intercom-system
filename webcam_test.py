import cv2

cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("Greska: Sistem ne vidi USB kameru na indeksu 0.")
else:
    # procitaj jedan frame
    ret, frame = cap.read()
    if ret:
        # sacuvaj
        cv2.imwrite("test_usb.jpg", frame)
        print("Slika 'test_usb.jpg' je sacuvana u folderu.")
    else:
        print("Kamera je otvorena, ali ne salje sliku.")

cap.release()