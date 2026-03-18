# 🏢 SmartGate – IoT Smart Intercom System

SmartGate je moderan IoT sistem za kontrolu pristupa zgradama koji omogućava da stanari na daljinu vide i odobre ulazak posetiocima putem mobilne aplikacije.

 ## 🚀 Opis projekta

Ovaj sistem predstavlja pametni interfon koji koristi Raspberry Pi kao centralnu jedinicu za obradu podataka. Umesto klasičnih interfona, SmartGate omogućava:

- 📷 snimanje slike ili kratkog videa posetilaca
- 📱 slanje obaveštenja na mobilnu aplikaciju
- 🔓 daljinsko otključavanje vrata
- 🏢 podršku za više stanova (multi-apartment system)

Cilj je da se omogući sigurniji, moderniji i praktičniji način kontrole pristupa zgradama.

---

## ⚙️ Kako sistem funkcioniše

1. Posetilac pritisne dugme za određeni stan
2. Signal se šalje Raspberry Pi uređaju
3. Raspberry Pi:
   - aktivira kameru
   - snima sliku ili kratak video (npr. 5 sekundi)
4. Podatak se šalje serveru / API-ju
5. Korisnik dobija notifikaciju na mobilnoj aplikaciji
6. Korisnik:
   - 👁️ pregleda sliku/video
   - ✅ odobrava ulazak ili ❌ odbija
7. U slučaju odobrenja → vrata se otključavaju

---

## 🧩 Arhitektura sistema

Sistem se sastoji od više komponenti:

- 🖥️ Raspberry Pi (IoT Layer)
  
  - upravljanje kamerama
  - komunikacija sa serverom
  - obrada zahteva sa tastera

- 🌐 Backend (API)
  
  - obrada zahteva
  - autentikacija korisnika
  - skladištenje podataka (slike/video)

- 📱 Mobilna aplikacija
  
  - notifikacije u realnom vremenu
  - prikaz posetilaca
  - kontrola pristupa

- 🚪 Door Control System
  
  - elektronska brava
  - signal za otključavanje

---

## 🛠️ Tehnologije (planirane)

- Backend: Spring Boot / Flask
- Database: MySQL / PostgreSQL
- IoT: Raspberry Pi (Python)
- Mobile: Android (Java/Kotlin ili Flutter)
- Communication: REST API / WebSocket
- Media handling: OpenCV / ffmpeg

---

## 🎯 Cilj projekta

- Poboljšanje bezbednosti stambenih objekata
- Modernizacija klasičnih interfona
- Omogućavanje daljinskog upravljanja pristupom
- Skaliranje za velike zgrade (100+ stanova)

---

## 🔒 Potencijalna proširenja

- Face recognition (AI)
- Istorija posetilaca
- Cloud storage
- Multi-user access (više članova domaćinstva)
- Integracija sa smart home sistemima

---

## 👨‍💻 Tim

Projekat razvija tim studenata i softverskih inženjera sa ciljem izgradnje realnog IoT proizvoda.

---

## 📌 Status

🚧 Projekat je u fazi planiranja i razvoja arhitekture.

---

## 📄 Licenca

MIT License
