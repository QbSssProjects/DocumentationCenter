# 🔧 Setup & Instalacja

## Spis Treści

1. [Wymagania](#wymagania)
2. [Instalacja](#instalacja)
3. [Uruchomienie](#uruchomienie)
4. [Weryfikacja](#weryfikacja)
5. [Docker Setup](#docker-setup)
6. [Troubleshooting](#troubleshooting)

---

## Wymagania

### System Requirements

| Komponent | Wersja | Opis |
|-----------|--------|------|
| Java | 17+ | JDK (nie JRE!) |
| Gradle | 8.x | Build tool (opcjonalny - używamy wrapper) |
| Git | 2.x | Version control |
| RAM | 4GB+ | Minimum dla IDE |
| Disk Space | 1GB+ | Dla build'ów i bazy danych |

### Operating Systems

- ✅ Windows 10/11
- ✅ macOS 10.15+
- ✅ Ubuntu 20.04+

### Optional

- Docker 20.x (dla konteneryzacji)
- Docker Compose 1.29+

---

## Instalacja

### Krok 1: Klonowanie Repozytorium

```bash
# Klonuj repozytorium
git clone https://github.com/qbsss/docsCenterGradleGenerated.git

# Wejdź do folderu projektu
cd docsCenterGradleGenerated

# Sprawdź strukturę
ls -la
```

### Krok 2: Sprawdzenie Java

```bash
# Sprawdź czy Java jest zainstalowana
java -version

# Wymagane: Java 17+
# Wynik powinien być podobny do:
# openjdk version "17.0.x" 2021-09-14
# OpenJDK Runtime Environment (build 17.0.x+x)
```

**Jeśli nie masz Java:**

```bash
# Windows (usando Chocolatey)
choco install openjdk17

# macOS (używając Homebrew)
brew install openjdk@17

# Linux (Ubuntu/Debian)
sudo apt-get install openjdk-17-jdk
```

### Krok 3: Sprawdzenie Gradle

```bash
# Gradle wrapper już jest w projekcie, ale sprawdź
./gradlew --version

# Wynik powinien zawierać:
# Gradle 8.x
```

### Krok 4: Przygotowanie Bazy Danych

```bash
# Utwórz folder na bazę danych
mkdir -p data

# Folder powinien istnieć w głównym katalogu projektu
# Baza danych storage.db będzie utworzona automatycznie przy pierwszym uruchomieniu
```

### Krok 5: Build Projektu

```bash
# Czyszczenie poprzednich build'ów
./gradlew clean

# Build projektu (pobierze wszystkie zależności)
./gradlew build

# Wynik powinien być:
# BUILD SUCCESSFUL in Xs
```

**Co się dzieje podczas build'u:**
1. Pobieranie zależności z Maven Central
2. Kompilacja Java code
3. Uruchomienie testów
4. Tworzenie JAR file

### Krok 6: Instalacja Zależności (Automatyczna)

Gradle automatycznie pobierze wszystkie zależności zdefiniowane w `build.gradle`:

```
spring-boot-starter-web       ~50 MB
spring-boot-starter-data-jpa  ~40 MB
spring-boot-starter-thymeleaf ~20 MB
hibernate                      ~30 MB
sqlite-jdbc                    ~5 MB
commonmark (Markdown)          ~1 MB
junit (Testing)                ~10 MB

Razem: ~150 MB+ (zależności)
```

---

## Uruchomienie

### Metoda 1: Gradle

```bash
# Uruchom aplikację (development mode)
./gradlew bootRun

# Wynik powinien zawierać:
# Started Main in x.xxx seconds
# Tomcat started on port(s): 8080

# Aplikacja dostępna na:
# http://localhost:8080
```

### Metoda 2: JAR File

```bash
# Build JAR
./gradlew bootJar

# JAR file będzie w: build/libs/docsCenterGradleGenerated-0.0.1-SNAPSHOT.jar

# Uruchom JAR
java -jar build/libs/docsCenterGradleGenerated-0.0.1-SNAPSHOT.jar

# Aplikacja dostępna na:
# http://localhost:8080
```

### Metoda 3: IDE

#### IntelliJ IDEA
```
1. Otwórz projekt w IntelliJ
2. Right-click na Main.java
3. Run 'Main.main()'
4. Aplikacja uruchomi się z debug'ingiem
```

#### Eclipse
```
1. Otwórz projekt
2. Right-click na Main.java
3. Run As → Java Application
4. Aplikacja uruchomi się
```

#### VS Code
```
1. Zainstaluj Extension Pack for Java
2. Otwórz Main.java
3. Click "Run" nad metodą main()
4. Aplikacja uruchomi się
```

---

## Weryfikacja

### Krok 1: Sprawdzenie Portu

```bash
# Windows
netstat -ano | findstr :8080

# macOS/Linux
lsof -i :8080

# Wynik powinien pokazać proces Java słuchający na porcie 8080
```

### Krok 2: Sprawdzenie HTTP

```bash
# Curl
curl http://localhost:8080/

# Wynik: HTML home page

# Lub otwórz w przeglądarce:
# http://localhost:8080
```

### Krok 3: Sprawdzenie Logów

**Logi startupowe powinny zawierać:**

```
== STARTUP LOGIC ==
App name: Dynamic Spring Server
Version : 1.0.0
```

### Krok 4: Sprawdzenie Bazy Danych

```bash
# Baza powinna być utworzona w ./data/storage.db
ls -la data/

# Output:
# storage.db

# Sprawdź rozmiar
du -sh data/storage.db

# Powinno być kilka KB
```

### Krok 5: Testy Functionality'ego

```bash
# 1. Home page
curl http://localhost:8080/
# Status: 200

# 2. Projects list (empty initially)
curl http://localhost:8080/viewDocs
# Status: 200

# 3. Add project form
curl http://localhost:8080/add-project
# Status: 200
```

---

## Docker Setup

### Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build -x test

# Run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/docsCenterGradleGenerated-0.0.1-SNAPSHOT.jar"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    volumes:
      - ./data:/data
    environment:
      - SPRING_DATASOURCE_URL=jdbc:sqlite:/data/storage.db
```

### Docker Build & Run

```bash
# Build Docker image
docker build -t docscenter:latest .

# Run container
docker run -p 8080:8080 -v $(pwd)/data:/data docscenter:latest

# Using docker-compose
docker-compose up --build

# Stop container
docker-compose down
```

---

## Troubleshooting

### Problem: "Java nie znaleziona"

```bash
# Error:
# 'java' is not recognized as an internal or external command

# Rozwiązanie:
# 1. Sprawdź czy Java jest zainstalowana
java -version

# 2. Dodaj Java do PATH (Windows)
setx JAVA_HOME "C:\Program Files\Java\openjdk-17"
setx PATH "%JAVA_HOME%\bin;%PATH%"

# 3. Uruchom ponownie terminal i spróbuj znowu
```

### Problem: "Port 8080 już w użyciu"

```bash
# Error:
# Address already in use: bind
# Port: 8080

# Rozwiązanie 1: Zmień port
./gradlew bootRun --args='--server.port=9090'

# Rozwiązanie 2: Zabij proces na porcie 8080 (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Rozwiązanie 3: Zabij proces na porcie 8080 (macOS/Linux)
lsof -i :8080
kill -9 <PID>
```

### Problem: "Database file not found"

```bash
# Error:
# Unable to locate storage.db
# SQLite file does not exist

# Rozwiązanie:
# 1. Utwórz folder data
mkdir -p data

# 2. Baza będzie utworzona automatycznie przy uruchomieniu
# (JPA z ddl-auto: update)

# 3. Sprawdź uprawnienia do zapisu
ls -l data/
chmod 755 data/
```

### Problem: "Gradle build failed"

```bash
# Error:
# Could not download org.springframework.boot:spring-boot-starter-web:...

# Rozwiązanie 1: Czyszczenie cache
./gradlew clean --no-daemon

# Rozwiązanie 2: Pełna przebudowa
rm -rf .gradle build
./gradlew clean build

# Rozwiązanie 3: Offline mode (jeśli masz już cache)
./gradlew build --offline
```

### Problem: "OutOfMemoryError"

```bash
# Error:
# Exception in thread "main" java.lang.OutOfMemoryError: Java heap space

# Rozwiązanie: Zwiększ heap size
./gradlew bootRun --jvmArgs="-Xmx1024m"

# Lub trwale ustawić
export GRADLE_OPTS="-Xmx1024m -Xms512m"
./gradlew bootRun
```

### Problem: "Thymeleaf template not found"

```bash
# Error:
# No part of URL matched DispatcherServlet mapping
# Resource handling chain was processed but no handler

# Rozwiązanie:
# 1. Sprawdź czy template jest w resources/templates/
ls -la src/main/resources/templates/

# 2. Sprawdź czy nazwa się zgadza (case-sensitive!)
# controller: return "index";
# plik: templates/index.html (mała litera!)

# 3. Sprawdź czy template jest prawidłowy HTML
# (Thymeleaf jest wrażliwy na błędy HTML)
```

### Problem: "Column not found" (Database)

```
# Error:
# [SQLITE_ERROR] SQL error or missing database (no such column: project_id)

# Rozwiązanie:
# 1. Usuń starą bazę
rm data/storage.db

# 2. Uruchom aplikację (nowa baza zostanie utworzona z nowym schematem)
./gradlew bootRun

# Jeśli to nie pomoże:
# 3. Sprawdź czy Entity jest prawidłowy
#    (wszystkie pola mają gettery/settery)
```

### Problem: "Can't start application in IDE"

```
# Error:
# ClassNotFoundException: com.example.Main
# org.springframework.boot.loader.ClassPathIndexException

# Rozwiązanie:
# 1. Clean build w IDE
# IntelliJ: Build → Clean Build
# Eclipse: Project → Clean

# 2. Invalidate cache (IntelliJ)
# File → Invalidate Caches

# 3. Rebuild project
# Build → Rebuild Project
```

---

## Post-Installation

### Pierwsza Uruchomienie

Po uruchomieniu aplikacja powinni:
- ✅ Otworzyć się na `http://localhost:8080`
- ✅ Wyświetlić home page
- ✅ Utworzyć bazę danych `storage.db`
- ✅ Wyświetlić "== STARTUP LOGIC ==" w logach

### Zmiana Konfiguracji

Aby zmienić konfigurację bez rebuildu:

```bash
# Method 1: Command line
./gradlew bootRun --args='--spring.datasource.url=jdbc:sqlite:custom.db'

# Method 2: Environment variable
export SPRING_DATASOURCE_URL=jdbc:sqlite:custom.db
./gradlew bootRun

# Method 3: Create application-custom.yaml
# Copy application.yaml to application-custom.yaml
# Modify settings
# Run with profile:
./gradlew bootRun --args='--spring.profiles.active=custom'
```

### Zatwierdzenie Instalacji

Powinna być możliwość:
1. ✅ Odwiedzić http://localhost:8080
2. ✅ Zobaczyć home page
3. ✅ Kliknąć "Przeglądaj projekty"
4. ✅ Zobaczyć pustą listę (baza jest pusta)
5. ✅ Kliknąć "Nowy projekt"
6. ✅ Dodać projekt
7. ✅ Zobaczyć projekt na liście

Jeśli wszystko działa - **instalacja jest kompletna!**

---

**Koniec: INSTALACJA.md**

