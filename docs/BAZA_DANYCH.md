# 🗄️ Baza Danych

## Spis Treści

1. [Overview](#overview)
2. [SQLite Setup](#sqlite-setup)
3. [Schema](#schema)
4. [Relacje](#relacje)
5. [Indeksy](#indeksy)
6. [Operacje](#operacje)
7. [Backupy](#backupy)

---

## Overview

### Database Engine

- **Type:** SQLite 3
- **Location:** `./data/storage.db`
- **Size:** ~1-50 MB (zależy od ilości dokumentów)
- **Connection:** JDBC (SQLite driver)
- **Management:** Automatyczne (Hibernate DDL-auto)

### Advantages

✅ Nie potrzebna osobna konfiguracja serwera  
✅ Portable - jeden plik `storage.db`  
✅ Szybka dla małych/średnich aplikacji  
✅ ACID compliance  
✅ Support dla relacji, indeksów, triggers  

---

## SQLite Setup

### Plik Bazy Danych

```
./data/storage.db
```

**Lokalizacja relatywna do głównego folderu projektu**

### JDBC Connection String

```
jdbc:sqlite:./data/storage.db
```

**Zdefiniowana w:** `src/main/resources/application.yaml`

```yaml
spring:
  datasource:
    url: jdbc:sqlite:/data/storage.db
    driver-class-name: org.sqlite.JDBC
```

### Hibernate Configuration

```yaml
spring:
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update           # Auto-update schema
    show-sql: true               # Log SQL statements
```

### Instalacja SQLite CLI (opcjonalny)

```bash
# Windows (Chocolatey)
choco install sqlite

# macOS (Homebrew)
brew install sqlite3

# Linux (Ubuntu/Debian)
sudo apt-get install sqlite3

# Sprawdzenie
sqlite3 --version
```

---

## Schema

### Tabela: PROJECTS

```sql
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

**Pola:**

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | BIGINT | PRIMARY KEY | Unikalny identyfikator |
| `name` | VARCHAR(255) | NOT NULL, UNIQUE | Nazwa projektu (unikalna!) |
| `description` | TEXT | NULLABLE | Opis projektu |
| `created_at` | TIMESTAMP | NOT NULL | Data utworzenia |
| `updated_at` | TIMESTAMP | NOT NULL | Data ostatniej edycji |

**Przykładowe dane:**

```sql
INSERT INTO projects (name, description, created_at, updated_at)
VALUES (
    'Dokumentacja API',
    'Dokumentacja REST API v2.0',
    '2025-01-01 10:00:00',
    '2025-01-01 10:00:00'
);
```

---

### Tabela: DOCUMENTS

```sql
CREATE TABLE documents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    content_type VARCHAR(50) NOT NULL DEFAULT 'MARKDOWN',
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);
```

**Pola:**

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | BIGINT | PRIMARY KEY | Unikalny ID |
| `project_id` | BIGINT | FK, NOT NULL | ID projektu (OBOWIĄZKOWY!) |
| `title` | VARCHAR(255) | NOT NULL | Tytuł dokumentu |
| `content` | TEXT | NOT NULL | Zawartość (Markdown/HTML) |
| `content_type` | VARCHAR(50) | DEFAULT 'MARKDOWN' | Typ zawartości |
| `description` | VARCHAR(500) | NULLABLE | Krótki opis |
| `created_at` | TIMESTAMP | NOT NULL | Data utworzenia |
| `updated_at` | TIMESTAMP | NOT NULL | Data edycji |

**Kluczowe założenie:**
> `project_id` jest `NOT NULL` - każdy dokument MUSI mieć projekt!

**Przykładowe dane:**

```sql
INSERT INTO documents (project_id, title, content, content_type, created_at, updated_at)
VALUES (
    1,
    'GET /api/projects',
    '# Pobieranie listy projektów\n\n## Endpoint\n`GET /api/projects`',
    'MARKDOWN',
    '2025-01-01 10:05:00',
    '2025-01-01 10:05:00'
);
```

---

### Tabela: STORED_FILES

```sql
CREATE TABLE stored_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);
```

**Pola:**

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | BIGINT | PRIMARY KEY | Unikalny ID |
| `project_id` | BIGINT | FK, NOT NULL | ID projektu |
| `file_name` | VARCHAR(255) | NOT NULL | Nazwa pliku (np. "document.pdf") |
| `file_path` | VARCHAR(500) | NOT NULL | Ścieżka do pliku |
| `created_at` | TIMESTAMP | NOT NULL | Data upload'u |
| `updated_at` | TIMESTAMP | NOT NULL | Data ostatniej edycji |

---

## Relacje

### Diagram ER (Entity Relationship)

```
┌─────────────────┐
│    PROJECT      │
├─────────────────┤
│ id (PK)         │
│ name (UNIQUE)   │
│ description     │
│ created_at      │
│ updated_at      │
└────────┬────────┘
         │ 1
         │
         │ * (OneToMany)
         │
┌────────▼────────────────┐
│      DOCUMENT           │
├─────────────────────────┤
│ id (PK)                 │
│ project_id (FK) ────────┘
│ title                   │
│ content                 │
│ content_type            │
│ description             │
│ created_at              │
│ updated_at              │
└────────┬────────────────┘
         │
         │ 1:N
         │
         ▼
     [W bazie]
     project_id
     linkuje do
     projects.id
```

### Cascade Delete

```sql
FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
```

**Znaczenie:**
- Usunięcie `Project` → automatycznie usuwane wszystkie `Documents` w tym projekcie
- Chroni spójność danych (brak sierót)

**Przykład:**
```sql
-- Usunięcie projektu
DELETE FROM projects WHERE id = 1;

-- Automatycznie usunięte zostaną:
-- - Wszystkie documents gdzie project_id = 1
-- - Wszystkie stored_files gdzie project_id = 1
```

---

## Indeksy

### Istniejące Indeksy

```sql
CREATE INDEX idx_documents_project_id ON documents(project_id);
CREATE INDEX idx_documents_created_at ON documents(created_at);
```

### Performance

**Bez indeksu:**
```sql
SELECT * FROM documents WHERE project_id = 1;
-- Skanuje całą tabelę: O(n)
```

**Z indeksem:**
```sql
SELECT * FROM documents WHERE project_id = 1;
-- Skanuje tylko plikę: O(log n)
```

### Rekomendowane Indeksy

```sql
-- Już istniejące:
CREATE INDEX idx_documents_project_id ON documents(project_id);
CREATE INDEX idx_documents_created_at ON documents(created_at);

-- Mogą być przydatne:
CREATE UNIQUE INDEX idx_projects_name ON projects(name);
CREATE INDEX idx_documents_title ON documents(title);
```

---

## Operacje

### SQL Queries

#### Pobranie Wszystkich Projektów

```sql
SELECT * FROM projects
ORDER BY created_at DESC;
```

#### Pobranie Projektu z Dokumentami

```sql
SELECT 
    p.id, p.name, p.description,
    d.id as doc_id, d.title, d.content_type
FROM projects p
LEFT JOIN documents d ON p.id = d.project_id
WHERE p.id = 1;
```

#### Liczba Dokumentów w Projekcie

```sql
SELECT project_id, COUNT(*) as count
FROM documents
WHERE project_id = 1
GROUP BY project_id;
```

#### Najnowsze Dokumenty

```sql
SELECT * FROM documents
ORDER BY created_at DESC
LIMIT 10;
```

#### Usunięcie Starego Projektu

```sql
DELETE FROM projects WHERE id = 123;
-- Dokumenty zostaną automatycznie usunięte (CASCADE)
```

### JPA Queries (Java)

```java
// Pobranie wszystkich projektów
List<Project> projects = projectRepository.findAll();

// Pobranie po ID
Optional<Project> project = projectRepository.findById(1L);

// Pobranie dokumentów projektu
List<Document> docs = documentRepository.findByProjectId(1L);

// Liczenie dokumentów
long count = documentRepository.countByProjectId(1L);

// Sprawdzenie czy projekt istnieje
boolean exists = projectRepository.existsById(1L);
```

---

## Backupy

### Ręczny Backup

```bash
# Windows
copy data\storage.db data\storage_backup_$(date /T).db

# macOS/Linux
cp data/storage.db data/storage_backup_$(date +%Y%m%d_%H%M%S).db
```

### Automatyczny Backup (Script)

**Plik: `backup.sh`**

```bash
#!/bin/bash

BACKUP_DIR="./backups"
DB_FILE="./data/storage.db"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/storage_$TIMESTAMP.db"

# Utwórz folder backup
mkdir -p $BACKUP_DIR

# Skopiuj plik
cp $DB_FILE $BACKUP_FILE

# Usuń stare backupy (starsze niż 30 dni)
find $BACKUP_DIR -name "storage_*.db" -mtime +30 -delete

echo "Backup created: $BACKUP_FILE"
```

**Uruchomienie:**
```bash
chmod +x backup.sh
./backup.sh
```

### Scheduled Backup (Cron/Task Scheduler)

**Linux (Crontab):**
```
0 3 * * * cd /path/to/project && ./backup.sh
# Każdego dnia o 3:00 AM
```

**Windows (Task Scheduler):**
```
1. Task Scheduler → Create Task
2. Schedule: Daily at 3:00 AM
3. Action: Run script `backup.bat`
```

### Export do CSV

```bash
# Eksport projektów
sqlite3 data/storage.db "SELECT * FROM projects;" > projects.csv

# Eksport dokumentów
sqlite3 data/storage.db ".mode csv" ".output documents.csv" "SELECT * FROM documents;"
```

### Restore z Backupu

```bash
# Backup Current
cp data/storage.db data/storage_current.db

# Restore
cp data/storage_backup_20250101_120000.db data/storage.db

# Restart aplikacja
./gradlew bootRun
```

---

## Maintenance

### Optymizacja (VACUUM)

```bash
# Czyści nieużywaną przestrzeń
sqlite3 data/storage.db "VACUUM;"

# Zmniejszy rozmiar pliku
```

### Sprawdzenie Integralności

```bash
# Sprawdź spójność bazy
sqlite3 data/storage.db "PRAGMA integrity_check;"

# Wynik: "ok" = baza jest OK
```

### Statystyki

```bash
# Rozmiar bazy
du -h data/storage.db

# Liczba wierszy w tabeli
sqlite3 data/storage.db "SELECT COUNT(*) FROM projects;"
sqlite3 data/storage.db "SELECT COUNT(*) FROM documents;"
```

---

**Koniec: BAZA_DANYCH.md**

