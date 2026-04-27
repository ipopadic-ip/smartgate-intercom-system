# SmartGate – IoT Smart Intercom System

> Moderni IoT sistem za pametnu kontrolu pristupa zgradama zasnovan na Raspberry Pi uređaju, MQTT protokolu, Spring Boot backendu i Angular web aplikaciji.

---

## Sadržaj

- [Opis projekta](#opis-projekta)
- [Arhitektura sistema](#arhitektura-sistema)
- [Tehnologije](#tehnologije)
- [Komponente sistema](#komponente-sistema)
  - [IoT sloj – Raspberry Pi (Python)](#iot-sloj--raspberry-pi-python)
  - [Backend – Spring Boot](#backend--spring-boot)
  - [Frontend – Angular](#frontend--angular)
- [Tok podataka](#tok-podataka)
- [Konfiguracija](#konfiguracija)
- [Pokretanje sistema](#pokretanje-sistema)
- [API referenca](#api-referenca)
- [WebSocket / STOMP](#websocket--stomp)
- [MQTT topici](#mqtt-topici)
- [Status projekta](#status-projekta)
- [Buduća proširenja](#buduća-proširenja)
- [Licenca](#licenca)

---

## Opis projekta

**SmartGate** je IoT sistem za pametnu kontrolu pristupa koji zamenjuje klasične interfone. Kada posetilac pritisne dugme ispred zgrade, Raspberry Pi uređaj:

1. Automatski snima fotografiju posetioca putem kamere (OpenCV)
2. Uploaduje sliku na Spring Boot backend
3. Prosleđuje event stanar putem MQTT → WebSocket bridge-a
4. Stanar prima real-time notifikaciju u Angular web aplikaciji sa slikom posetioca
5. Jednim klikom stanar otvara kapiju – komanda se šalje nazad kroz MQTT do releja

Sistem podržava više stanova (multi-apartment), radi u lokalnoj mreži i projektovan je da bude skalabilan.

---

## Arhitektura sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                         LOKALNA MREŽA                           │
│                                                                  │
│  ┌──────────────┐    HTTP POST     ┌───────────────────────┐    │
│  │  Raspberry Pi│ ─────────────── ▶│   Spring Boot Backend │    │
│  │  (Python)    │                  │   (port 8080)         │    │
│  │              │    MQTT Publish  │                        │    │
│  │  - Kamera    │ ─────────────── ▶│   MQTT Subscriber     │    │
│  │  - GPIO/Relay│                  │   WebSocket Broker    │    │
│  │  - MQTT Klijent◀─────────────── │   REST API            │    │
│  └──────────────┘    MQTT Subscribe│   H2 / Image Store    │    │
│                                    └──────────┬────────────┘    │
│                                               │ WebSocket/STOMP  │
│                                    ┌──────────▼────────────┐    │
│                                    │   Angular Frontend     │    │
│                                    │   (SSR + Client)       │    │
│                                    │   - Login              │    │
│                                    │   - Dashboard          │    │
│                                    └───────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                               │
                         MQTT Broker
                      (Mosquitto, port 1883)
```

---

## Tehnologije

### IoT sloj

| Komponenta | Tehnologija |
|------------|-------------|
| Platforma | Raspberry Pi (Linux) |
| Jezik | Python 3 |
| Kamera | OpenCV (`cv2`) |
| MQTT klijent | `paho-mqtt` |
| GPIO upravljanje | `gpiozero` |
| HTTP upload | `requests` |

### Backend

| Komponenta | Tehnologija |
|------------|-------------|
| Framework | Spring Boot **4.0.5** |
| Java verzija | **Java 26** |
| Build alat | Maven |
| MQTT integracija | Spring Integration MQTT + Eclipse Paho 1.2.5 |
| WebSocket | Spring WebSocket + STOMP |
| Persistencija | Spring Data JPA |
| Baza podataka | H2 (in-memory, dev) |
| Serializacija | Jackson Databind |
| Validacija | Spring Boot Validation |

### Frontend

| Komponenta | Tehnologija |
|------------|-------------|
| Framework | Angular (standalone komponente) |
| Arhitektura | SSR (Server-Side Rendering) + klijentska aplikacija |
| WebSocket klijent | `@stomp/stompjs` |
| HTTP komunikacija | Angular `HttpClient` |
| Stilizacija | CSS (per-komponenta) |
| Routing | Angular Router |

### Infrastruktura

| Komponenta | Tehnologija |
|------------|-------------|
| Message broker | Mosquitto MQTT (port 1883) |
| Slike na disku | Lokalni `uploads/` direktorijum |
| Serviranje statičkih fajlova | Spring MVC Resource Handler |

---

## Komponente sistema

### IoT sloj – Raspberry Pi (Python)

Raspberry Pi je centralna IoT jedinica sistema. Modularna je struktura sledećih servisa:

#### Struktura projekta (IoT)

```
iot/
├── main.py                  # Entry point
├── config/
│   └── settings.py          # Konfiguracija (broker IP, pinovi, topic-i)
├── camera/
│   └── camera_service.py    # Snimanje slike (OpenCV)
├── gpio/
│   ├── button_handler.py    # Dugmad (GPIO ili konzolni test mode)
│   └── relay_controller.py  # Upravljanje relejom (gpiozero)
├── mqtt/
│   ├── mqtt_client.py       # MQTT klijent (paho-mqtt, MQTTv3.1.1)
│   └── topics.py            # Centralizovane MQTT topic konstante
├── services/
│   ├── intercom_service.py  # Centralna business logika
│   └── upload_service.py    # HTTP upload slike na backend
└── utils/
    └── logger.py            # Timestampovano logovanje
```

#### Tok izvršavanja

1. `ButtonHandler` detektuje pritisak dugmeta (GPIO pin ili konzolni `TEST_MODE`)
2. Poziva `IntercomService.handle_ring(stan_id)` – pokreće asinhronizovani thread
3. `CameraService` snima fotografiju putem OpenCV i čuva je lokalno (`images/`)
4. `UploadService` šalje sliku na backend endpoint `POST /api/upload` i dobija URL
5. Lokalna slika se briše; MQTT poruka sa JSON payload-om se publikuje na `interfon/pozivi`
6. `MQTTClient` se pretplaćuje na `interfon/kapija/komande`; po prijemu `{"type":"OPEN"}` aktivira relej 1 sekundu

#### Konfiguracija (settings.py)

```python
BROKER_IP = "localhost"          # IP MQTT brokera
PORT = 1883
TOPIC_POZIVI = "interfon/pozivi"
TOPIC_KOMANDE = "interfon/kapija/komande"
CLIENT_ID = "raspberry_pi_interfon"
GPIO_BUTTONS = [2, 3, 4, 17]    # GPIO pinovi za dugmad po stanovima
RELAY_PIN = 18                   # Pin za relej (kapija)
CAMERA_INDEX = 0                 # Index kamere (0 = prva)
TEST_MODE = True                 # True = konzolni unos umesto GPIO
```

**Test mode** (`TEST_MODE = True`): sistem se može pokrenuti i testirati na bilo kom računaru bez fizičkih GPIO pinova ili kamere (simulator loguje akcije).

---

### Backend – Spring Boot

#### Struktura projekta

```
src/main/java/com/example/demo/
├── config/
│   ├── MqttConfig.java        # MQTT inbound/outbound integration flow
│   ├── WebConfig.java         # Serviranje statičkih slika (/images/**)
│   └── WebSocketConfig.java   # STOMP WebSocket konfiguracija (/ws-intercom)
├── controller/
│   ├── GateController.java    # POST /api/open – šalje OPEN komandu
│   └── UploadController.java  # POST /api/upload – prima sliku
├── dto/
│   ├── IntercomEventDto.java  # DTO za MQTT event (stan, image_url, timestamp)
│   └── UploadResponse.java    # DTO odgovor sa URL-om slike
├── model/
│   └── ImageRecord.java       # JPA entitet za čuvanje metapodataka slike
├── repository/
│   └── ImageRepository.java   # Spring Data JPA repozitorijum
└── service/
    ├── ImageService.java      # Čuvanje slike na disk + DB zapis
    └── MqttService.java       # MQTT poruke → WebSocket forward
```

#### Ključni tokovi

**Upload slike:**
- `POST /api/upload` prima `multipart/form-data`
- Fajl se čuva u `uploads/` sa UUID prefiksom
- Metapodaci (ime, putanja, vreme) se upisuju u H2 bazu
- Vraća JSON `{ "url": "http://<ip>:8080/images/<filename>" }`

**MQTT → WebSocket bridge:**
- `MqttConfig` definiše `IntegrationFlow` koji sluša topic `interfon/pozivi`
- Paho klijent prima poruku → `MqttService.receive()` je deserijalizuje u `IntercomEventDto`
- `SimpMessagingTemplate` prosleđuje DTO na WebSocket destination `/topic/intercom`

**Otvaranje kapije:**
- Frontend poziva `POST /api/open`
- `GateController` → `MqttService.sendOpenCommand()` publikuje `{"type":"OPEN"}` na `interfon/kapija/komande`
- Raspberry Pi prima komandu i aktivira relej

#### Konfiguracija (application.properties)

```properties
server.port=8080
server.address=0.0.0.0

# H2 in-memory baza
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Upload
file.upload-dir=uploads
spring.servlet.multipart.max-file-size=5MB

# MQTT
mqtt.broker=tcp://192.168.100.112:1883
mqtt.client-id=spring-backend
mqtt.topic.events=interfon/pozivi
mqtt.topic.commands=interfon/kapija/komande
```

---

### Frontend – Angular

#### Struktura projekta

```
src/
├── app/
│   ├── models/
│   │   └── intercom-event.ts      # TypeScript interfejs za event
│   ├── pages/
│   │   ├── login/
│   │   │   ├── login.html         # UI forme za prijavu
│   │   │   ├── login.css
│   │   │   └── login.ts           # Standalone komponenta, navigacija na dashboard
│   │   └── dashboard/
│   │       ├── dashboard.html     # Prikaz slike, info, dugmad
│   │       ├── dashboard.css
│   │       └── dashboard.ts       # WebSocket pretplata, open/reject logika
│   ├── services/
│   │   └── intercom.ts            # IntercomService: WebSocket + HTTP
│   ├── app.routes.ts              # Routing (/ → login, /dashboard)
│   ├── app.config.ts
│   └── app.ts
└── main.ts                        # bootstrapApplication entry point
```

#### Ključne funkcionalnosti

**Login:** Jednostavna stranica sa unosom korisničkog imena i lozinke. Po prijavi postavlja `localStorage['loggedIn']` i rutira na `/dashboard`.

**Dashboard:**
- Inicijalizacija STOMP klijenta (`@stomp/stompjs`) na `ws://localhost:8080/ws-intercom`
- Pretplata na `/topic/intercom` – svaki novi event ažurira prikaz slike, broja stana i vremena
- Dugme **"Otvori"** → `POST /api/open` → kapija se otvara
- Dugme **"Ne otvaraj"** → lokalna poruka, bez akcije na backendu
- `ChangeDetectorRef.detectChanges()` osigurava UI refresh van Angular zone-e

**IntercomEvent model:**

```typescript
export interface IntercomEvent {
  id?: number;
  stan?: number;
  image_url: string;
  timestamp: string;  // format "HH:mm:ss"
}
```

---

## Tok podataka

```
[Dugme na interfonu]
        │
        ▼
[Raspberry Pi – ButtonHandler]
        │
        ▼
[CameraService – OpenCV snima sliku]
        │
        ▼
[UploadService – POST /api/upload → backend]
        │              │
        │              ▼
        │       [ImageService – čuva na disk + H2 DB]
        │              │
        │              ▼
        │       [Vraća image URL]
        │
        ▼
[MQTT Publish → interfon/pozivi]
{
  "stan": 3,
  "image_url": "http://192.168.x.x:8080/images/uuid_filename.jpg",
  "timestamp": "14:32:07"
}
        │
        ▼
[Spring Boot – MqttConfig IntegrationFlow prima poruku]
        │
        ▼
[MqttService.receive() → deserijalizuje u IntercomEventDto]
        │
        ▼
[SimpMessagingTemplate → /topic/intercom (WebSocket STOMP)]
        │
        ▼
[Angular Dashboard – prima event, ažurira UI]
        │
  ┌─────┴──────┐
  │            │
[Otvori]   [Ne otvaraj]
  │
  ▼
[POST /api/open]
  │
  ▼
[MqttService.sendOpenCommand()]
  │
  ▼
[MQTT Publish → interfon/kapija/komande]
{"type":"OPEN"}
  │
  ▼
[Raspberry Pi – RelayController.open_gate() → 1s impuls]
```

---

## Pokretanje sistema

### Preduslovi

- Mosquitto MQTT broker pokrenut lokalno ili na mreži (port 1883)
- Java 26, Maven
- Python 3.9+, pip
- Node.js + Angular CLI (za frontend)

### 1. MQTT Broker

```bash
# Instalacija (Linux)
sudo apt install mosquitto mosquitto-clients
sudo systemctl start mosquitto
```

### 2. Spring Boot Backend

```bash
cd backend/
mvn spring-boot:run
# Backend dostupan na http://0.0.0.0:8080
# H2 konzola: http://localhost:8080/h2-console
```

### 3. IoT – Raspberry Pi

```bash
cd iot/
pip install paho-mqtt opencv-python requests gpiozero
python main.py
# U TEST_MODE: unesi broj stana u konzoli
```

### 4. Angular Frontend

```bash
cd intercom-frontend/
npm install
ng serve
# Dostupno na http://localhost:4200
```

---

## API referenca

### `POST /api/upload`

Prima sliku sa Raspberry Pi i čuva je na server.

**Request:** `multipart/form-data`, polje `file` (JPEG/PNG, max 5MB)

**Response:**
```json
{
  "url": "http://192.168.100.109:8080/images/uuid_filename.jpg"
}
```

---

### `POST /api/open`

Šalje komandu za otvaranje kapije putem MQTT.

**Response:** `200 OK` – `"Kapija komanda poslata"`

---

### `GET /images/{filename}`

Servira sačuvanu sliku direktno iz `uploads/` direktorijuma.

---

## WebSocket / STOMP

| Parametar | Vrednost |
|-----------|----------|
| Endpoint | `ws://localhost:8080/ws-intercom` |
| Protokol | STOMP over WebSocket |
| Subscribe destination | `/topic/intercom` |
| Poruka | JSON `IntercomEventDto` |

**Primer poruke:**
```json
{
  "stan": 3,
  "image_url": "http://192.168.100.109:8080/images/abc_stan_3_143207.jpg",
  "timestamp": "14:32:07"
}
```

---

## MQTT topici

| Topic | Direkcija | Sadržaj |
|-------|-----------|---------|
| `interfon/pozivi` | RPi → Backend | JSON event (stan, image_url, timestamp) |
| `interfon/kapija/komande` | Backend → RPi | JSON komanda `{"type":"OPEN"}` |

---

## Status projekta

| Komponenta | Status |
|------------|--------|
| IoT – kamera + upload | ✅ Implementirano |
| IoT – MQTT publish | ✅ Implementirano |
| IoT – relej (GPIO) | ✅ Implementirano (sa simulacijom za dev) |
| Backend – upload API | ✅ Implementirano |
| Backend – MQTT bridge | ✅ Implementirano |
| Backend – WebSocket STOMP | ✅ Implementirano |
| Frontend – login | ✅ Implementirano |
| Frontend – dashboard + real-time | ✅ Implementirano |
| Autentikacija (JWT/OAuth) | 🔄 Planirana |
| Produkciona baza (PostgreSQL) | 🔄 Planirana |

---

## Buduća proširenja

- **JWT autentikacija** – zaštita API endpointa i WebSocket konekcija
- **PostgreSQL** – zamena H2 in-memory baze za produkciju
- **Istorija poseta** – pregled svih posetilaca po stanovima
- **Cloud storage** – Amazon S3 ili slično za slike
- **Face recognition** – AI prepoznavanje poznatih lica
- **Push notifikacije** – Firebase Cloud Messaging za mobilne uređaje
- **Multi-user** – više stanara po stanu, različita prava pristupa
- **Video stream** – RTSP/HLS umesto statičke slike

---

## Licenca

MIT License – slobodna upotreba, izmena i distribucija uz navođenje autora.
