# ❓ FAQ & Troubleshooting

## Spis Treści

1. [Instalacja](#instalacja)
2. [Uruchomienie](#uruchomienie)
3. [Baza Danych](#baza-danych)
4. [API & Endpoints](#api--endpoints)
5. [Dokumenty & Projekty](#dokumenty--projekty)
6. [Debugging](#debugging)

---

## Instalacja

### P: Jak zainstalować Java?

**O:** Pobierz JDK 17+ z oracle.com lub użyj package manager:

```bash
# Windows
choco install openjdk17

# macOS
brew install openjdk@17

# Linux
sudo apt-get install openjdk-17-jdk
```

Weryfikuj: `java -version`

---

### P: Co zrobić, gdy Gradle pobiera zależności przez długi czas?

**O:** To normalne - pierwsze pobranie trwa. Następne razy będą szybsze (cache).

```bash
# Jeśli utknął, spróbuj:
./gradlew --stop
./gradlew clean build
```

---

### P: Czy potrzebuję zainstalować SQLite osobno?

**O:** Nie! JDBC driver jest pobierany automatycznie w `build.gradle`.

---

## Uruchomienie

### P: Gdzie mogę znaleźć aplikację po uruchomieniu?

**O:** http://localhost:8080

---

### P: Port 8080 jest zajęty. Co robić?

**O:** Zmień port w command line:

```bash
./gradlew bootRun --args='--server.port=9090'
```

Lub zabij proces:

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

---

### P: Aplikacja startuje ale nie widzę strony

**O:** Sprawdź logi:

```
Tomcat started on port(s): 8080
Started Main in X.XXX seconds
== STARTUP LOGIC ==
```

Jeśli logów nie ma - proces się crasha. Sprawdź błędy wyżej w logach.

---

## Baza Danych

### P: Gdzie przechowywana jest baza danych?

**O:** Plik `./data/storage.db` w głównym folderze projektu.

---

### P: Baza się popsuta. Jak ją zresetować?

**O:** Usuń plik i uruchom aplikację - baza zostanie odtworzona:

```bash
rm data/storage.db
./gradlew bootRun
```

**Uwaga:** Stracimy wszystkie dane!

---

### P: Jak zrobić backup bazy?

**O:**

```bash
cp data/storage.db data/storage_backup.db
```

---

### P: Ile danych można przechować w SQLite?

**O:** Teoretycznie do 281 TB, praktycznie ~1-10 GB jest OK dla SQLite. Dla większych aplikacji użyj PostgreSQL/MySQL.

---

### P: Jak sprawdzić zawartość bazy?

**O:** Zainstaluj SQLite3 CLI:

```bash
# Pobierz: sqlite.org/download.html

# Lub
choco install sqlite3  # Windows
brew install sqlite3   # macOS

# Użycie
sqlite3 data/storage.db
> SELECT * FROM projects;
> SELECT COUNT(*) FROM documents;
```

---

## API & Endpoints

### P: Jaka jest różnica między GET, POST, DELETE?

**O:**
- **GET** - Pobieranie danych (bezpieczne)
- **POST** - Tworzenie/aktualizacja (zmienia dane)
- **DELETE** - Usuwanie (niebezpieczne!)

---

### P: Jak wysłać POST request do aplikacji?

**O:** Używając curl:

```bash
curl -X POST http://localhost:8080/docs/save \
  -d "projectId=1&title=Test&content=Hello"
```

Lub form w HTML:

```html
<form method="post" action="/docs/save">
    <input name="projectId" value="1">
    <input name="title" value="Test">
    <textarea name="content">Hello</textarea>
    <button type="submit">Save</button>
</form>
```

---

### P: Status 404 - co to znaczy?

**O:** Zasób nie istnieje. Sprawdź:
- ID projektu/dokumentu jest poprawne?
- Projekt/dokument istnieje w bazie?

---

### P: Status 409 - Conflict?

**O:** Konflikt - zazwyczaj duplikat nazwy projektu. Każdy projekt musi mieć unikalną nazwę.

---

## Dokumenty & Projekty

### P: Czy mogę tworzyć dokument bez projektu?

**O:** **Nie!** ✅ To jest założenie projektowe.

Każdy dokument MUSI mieć projekt:

```
❌ POST /docs/save (bez projectId)
✅ POST /docs/save (projectId=1)
```

---

### P: Co się stanie, gdy usunę projekt?

**O:** **Wszystkie dokumenty w projekcie zostaną usunięte** (CASCADE delete).

```
DELETE /projects/1
  ↓
Projekt usunięty
  ↓
Wszystkie dokumenty w projekcie usunięte
  ↓
Wszystkie pliki w projekcie usunięte
```

---

### P: Mogę przenieść dokument do innego projektu?

**O:** Bezpośrednio **nie**. Musisz:
1. Skopiować zawartość dokumentu
2. Usunąć stary dokument
3. Stworzyć nowy dokument w innym projekcie

---

### P: Dlaczego dokumenty nie widać na głównej liście?

**O:** **By design!** Dokumenty są dostępne TYLKO w kontekście projektu:

```
❌ GET /viewDocs  - Lista projektów, NIE dokumentów
✅ GET /projects/1 - Projekt z jego dokumentami
```

---

### P: Jak mogę wyszukać dokument?

**O:** Wyszukiwanie nie jest zaimplementowane. Możesz:
- Przejść do projektu
- Odczytać dokumenty w projekcie

---

## Debugging

### P: Jak włączyć DEBUG mode?

**O:**

```yaml
# application.yaml
logging:
  level:
    qbsss.docsCenter: DEBUG
    org.springframework: DEBUG
```

Lub via CLI:

```bash
./gradlew bootRun --args='--logging.level.qbsss.docsCenter=DEBUG'
```

---

### P: Jak widzieć SQL queries?

**O:**

```yaml
spring:
  jpa:
    show-sql: true
```

Lub:

```bash
./gradlew bootRun --args='--spring.jpa.show-sql=true'
```

---

### P: Jak debugować w IDE?

**O:**

**IntelliJ:**
- Right-click Main.java
- Debug 'Main.main()'
- Ustaw breakpoints (kliknij na lewej stronie linii)
- Patrz zmienne w panelu Debug

**VS Code:**
- Zainstaluj Extension Pack for Java
- Kliknij Debug nad metodą main()

---

### P: Aplikacja crashuje bez komunikatu błędu

**O:** Sprawdź:
1. Czy Java jest poprawnie zainstalowana? `java -version`
2. Czy folder `/data` istnieje? `mkdir -p data`
3. Czy masz uprawnienia do zapisu? `chmod 755 data/`
4. Uruchom z logami: `./gradlew bootRun --info`

---

### P: OutOfMemoryError

**O:** Zwiększ heap memory:

```bash
./gradlew bootRun --jvmArgs="-Xmx1024m"
```

---

## Pytania o Kod

### P: Co to jest `@Entity`?

**O:** Adnotacja JPA oznaczająca, że klasa reprezentuje tabelę w bazie danych.

```java
@Entity
@Table(name = "projects")
public class Project { }
```

---

### P: Co to jest `@Repository`?

**O:** Interfejs Spring Data JPA dla dostępu do danych:

```java
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);
}
```

---

### P: Co to jest `@Service`?

**O:** Klasa zawierająca logikę biznesową:

```java
@Service
public class ProjectService {
    public Project create(String name) { }
}
```

---

### P: Co to jest `@Controller`?

**O:** Klasa obsługująca HTTP requests:

```java
@Controller
public class ProjectController {
    @GetMapping("/projects")
    public String list(Model model) { }
}
```

---

### P: Jaka jest różnica między `@Component`, `@Service`, `@Controller`?

**O:** Techniczne - są to variacje `@Component`. Najlepiej używać odpowiednie:
- `@Component` - Generic
- `@Service` - Business logic
- `@Controller` - HTTP handling
- `@Repository` - Data access

---

## Wsparcie

### P: Gdzie mogę zgłosić błędy?

**O:** Sprawdź plik `ROZWIĄZANIA_PROBLEMY.md` w głównym folderze.

---

### P: Gdzie mogę znaleźć więcej dokumentacji?

**O:** Folder `/docs` zawiera kompletną dokumentację:
- `README.md` - Główny przegląd
- `ARCHITEKTURA.md` - Struktura systemu
- `API.md` - Lista endpointów
- `BAZA_DANYCH.md` - Schema bazy
- itd.

---

### P: Czy kod jest open source?

**O:** Tak - sprawdź licencję w repozytorium.

---

### P: Mogę zmienić kod?

**O:** Tak! To jest twój projekt. Fork na GitHubie i rób PR.

---

## Quick Reference

```bash
# Build
./gradlew clean build

# Run
./gradlew bootRun

# Test
./gradlew test

# Docker
docker-compose up --build

# Database Backup
cp data/storage.db data/storage_backup.db

# Database Reset
rm data/storage.db && ./gradlew bootRun

# View Logs
./gradlew bootRun --info

# Change Port
./gradlew bootRun --args='--server.port=9090'

# Debug Mode
./gradlew bootRun --args='--logging.level.root=DEBUG'
```

---

**Koniec: FAQ.md**

