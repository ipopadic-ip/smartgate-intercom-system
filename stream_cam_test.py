import cv2

# ZAMENI SA IP ADRESOM SVOG LAPTOPA
url = "http://192.168.10.48:8080" 
cap = cv2.VideoCapture(url)

while True:
    ret, frame = cap.read()
    if not ret:
        print("Čekam strim sa laptopa...")
        break
    
    cv2.imshow('Interfon Kamera sa Laptopa', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()