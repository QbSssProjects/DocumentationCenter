# 📦 Struktura Projektu

## Spis Treści

1. [Hierarchia Folderów](#hierarchia-folderów)
2. [Organizacja Java](#organizacja-java)
3. [Organizacja Resources](#organizacja-resources)
4. [Testy](#testy)
5. [Build Output](#build-output)

---

## Hierarchia Folderów

```
docsCenterGradleGenerated/
│
├── src/                                   # Kod źródłowy
│   ├── main/
│   │   ├── java/qbsss/docsCenter/...      # Java source code
│   │   └── resources/                      # Configuration & templates
│   └── test/
│       └── java/qbsss/docsCenter/...      # Test code
│
├── docs/                                  # 📚 DOKUMENTACJA (ten folder)
│   ├── README.md                          # Przegląd dokumentacji
│   ├── ARCHITEKTURA.md                    # Architektura systemu
│   ├── STRUKTURA.md                       # Ten plik
│   ├── KONFIGURACJA.md                    # Ustawienia
│   ├── INSTALACJA.md                      # Setup instrukcje
│   ├── BAZA_DANYCH.md                     # DB schema
│   ├── API.md                             # HTTP endpoints
│   ├── ENTITY_MODELS.md                   # Entity classes
│   ├── SERVICES.md                        # Service layer
│   ├── CONTROLLERS.md                     # Controller layer
│   ├── TESTING.md                         # Testing guide
│   ├── ZMIANY.md                          # Change history
│   └── FAQ.md                             # Q&A
│
├── data/                                  # 🗄️ DATABASE
│   └── storage.db                         # SQLite database file
│
├── gradle/                                # Gradle wrapper
│   └── wrapper/
│
├── build/                                 # Build output (generated)
│   ├── classes/
│   ├── generated/
│   ├── libs/
│   └── reports/
│
├── .gradle/                               # Gradle cache
│
├── build.gradle                           # 🔧 BUILD CONFIGURATION
├── settings.gradle                        # Gradle settings
├── gradlew                                # Gradle wrapper (Linux/Mac)
├── gradlew.bat                            # Gradle wrapper (Windows)
│
├── Dockerfile                             # 🐳 Docker configuration
├── docker-compose.yml                     # Docker compose
├── docker.bat                             # Docker automation script
│
├── ROZWIĄZANIA_PROBLEMY.md                # 📝 Problem solutions
├── LISTA_ZMIAN.md                         # 📋 Change log
├── HELP.md                                # Spring Boot help
└── README.md (root)                       # Project root README
```

---

## Organizacja Java

### Package Structure

```
src/main/java/qbsss/docsCenter/docsCenterGradleGenerated/
│
├── Main.java                              # 🚀 Entry point - @SpringBootApplication
│
├── contoller/                             # 🎮 CONTROLLERS
│   ├── ProjectController.java             # Projects management
│   ├── DocumentController.java            # Documents management (✅ ZMIENIONO)
│   ├── FileController.java                # File operations
│   ├── MainPageController.java            # Home page
│   ├── ViewDocumentationFilesController.java  # Projects list (✅ ZMIENIONO)
│   ├── ViewProjectController.java         # Project detail
│   ├── AddProjectController.java          # Add project form
│   ├── ErrorController.java               # Error handling
│   ├── GlobalExceptionHandler.java        # Global exception handler
│   ├── DocumentationFilesControllerEditor.java # (Legacy)
│   └── getYamlProjectInfo.java            # (Legacy - YAML)
│
├── service/                               # 🛠️ SERVICES
│   ├── ProjectService.java                # Project business logic
│   ├── DocumentService.java               # Document business logic (✅ ZMIENIONO)
│   ├── FileService.java                   # File operations
│   ├── ListOfProjectsService.java         # Projects listing
│   ├── VariableService.java               # App variables
│   └── dbServices/
│       └── FileService.java               # DB file operations
│
├── database/                              # 🗄️ DATABASE LAYER
│   ├── dbItems/                           # 📦 ENTITY MODELS
│   │   ├── Project.java                   # Project entity
│   │   ├── Document.java                  # Document entity (✅ ZMIENIONO)
│   │   └── StoredFile.java                # File entity
│   │
│   └── repository/                        # 📥 REPOSITORIES (JPA)
│       ├── ProjectRepository.java         # Project data access
│       ├── DocumentRepository.java        # Document data access
│       └── StoredFileRepository.java      # File data access
│
├── exceptions/                            # ⚠️ CUSTOM EXCEPTIONS
│   └── FileIOException.java               # File I/O error
│
├── items/                                 # 📋 DTOs & VALUE OBJECTS
│   └── DocumentItem.java                  # Document DTO
│
├── globalVariables/                       # 🌍 GLOBAL CONFIG
│   └── (configuration classes)
│
└── utils/                                 # 🔧 UTILITIES
    ├── ProjektyYamlReader.java            # YAML reader (DEPRECATED)
    ├── ViewProjects.java                  # Project viewer (Legacy)
    └── YamlWriterProjectUtil.java         # YAML writer (DEPRECATED)
```

### Package Naming Convention

- **contoller** - HTTP request handlers (note: typo "contoller" zamiast "controller")
- **service** - Business logic layer
- **database/dbItems** - Entity models (JPA)
- **database/repository** - Data access (Spring Data JPA)
- **exceptions** - Custom exceptions
- **items** - Data Transfer Objects (DTO)
- **utils** - Utility classes

---

## Organizacja Resources

```
src/main/resources/
│
├── application.yaml                       # 🔧 SPRING BOOT CONFIG
│   ├── Database configuration
│   ├── Port settings
│   └── Upload limits
│
├── projectsList.txt                       # Project list (Legacy)
│
├── static/                                # 🎨 STATIC FILES
│   └── css/
│       └── mainStyles.css                 # Main stylesheet
│
└── templates/                             # 🎯 THYMELEAF TEMPLATES
    ├── index.html                         # Home page
    ├── viewDocumentationFiles.html        # Projects list (✅ ZMIENIONO)
    ├── viewProject.html                   # Project detail
    ├── addProject.html                    # Add project form
    ├── editor.html                        # Document editor (✅ ZMIENIONO)
    ├── viewer.html                        # Document viewer
    └── error.html                         # Error page
```

### Templates Description

| Template | Route | Purpose |
|----------|-------|---------|
| `index.html` | `/` | Home page |
| `viewDocumentationFiles.html` | `/viewDocs` | Projects list (from SQLite) |
| `viewProject.html` | `/projects/{id}` | Project details + docs |
| `addProject.html` | `/add-project` | New project form |
| `editor.html` | `/docs/new/{id}` | Document editor |
| `viewer.html` | `/docs/view/{id}` | Document viewer |
| `error.html` | `/error` | Error page |

---

## Testy

### Test Structure

```
src/test/java/qbsss/docsCenter/docsCenterGradleGenerated/
│
└── DocsCenterGradleGeneratedApplicationTests.java
    ├── @SpringBootTest                    # Full application test
    ├── Project creation tests
    ├── Document creation tests
    └── Service layer tests
```

### Test Naming Convention

```
TestClassNameTests.java

@Test
void testDescriptionOfWhatIsTested() {
    // Given
    // When
    // Then
}
```

### Running Tests

```bash
# All tests
./gradlew test

# Specific test
./gradlew test --tests "DocsCenterGradleGeneratedApplicationTests"

# With detailed output
./gradlew test --info

# Generate report
./gradlew test --no-parallel
```

---

## Build Output

### Build Directory Structure

```
build/
│
├── classes/
│   └── java/main/
│       └── qbsss/docsCenter/docsCenterGradleGenerated/
│           ├── Main.class
│           ├── contoller/
│           │   ├── ProjectController.class
│           │   ├── DocumentController.class
│           │   └── ...
│           ├── service/
│           │   ├── ProjectService.class
│           │   ├── DocumentService.class
│           │   └── ...
│           ├── database/
│           │   ├── dbItems/
│           │   │   ├── Project.class
│           │   │   ├── Document.class
│           │   │   └── StoredFile.class
│           │   └── repository/
│           │       └── ...
│           └── ...
│
├── libs/
│   └── docsCenterGradleGenerated-0.0.1-SNAPSHOT.jar
│       └── (Executable JAR file)
│
├── reports/
│   ├── tests/
│   │   └── test/
│   │       └── index.html              # Test report
│   └── problems/
│       └── problems-report.html        # Build problems
│
├── resources/main/
│   ├── application.yaml                # Compiled config
│   ├── templates/
│   │   ├── index.html
│   │   ├── editor.html
│   │   └── ...
│   └── static/
│       └── css/mainStyles.css
│
├── generated/
│   └── sources/
│       └── annotationProcessor/
│           └── java/main/              # Generated sources
│
└── tmp/
    ├── bootJar/
    │   └── MANIFEST.MF                 # JAR manifest
    ├── jar/
    │   └── MANIFEST.MF
    └── compileJava/
        ├── previous-compilation-data.bin
        └── compileTransaction/
            └── stash-dir/              # Incremental compilation cache
```

### Build Artifacts

| Artifact | Location | Description |
|----------|----------|-------------|
| JAR File | `build/libs/*.jar` | Executable JAR (Spring Boot) |
| Test Report | `build/reports/tests/` | HTML test report |
| Classes | `build/classes/` | Compiled `.class` files |

---

## File Dependencies

### Source Code Dependencies

```
Main.java
  ├─> Spring Boot annotations
  ├─> CommandLineRunner
  └─> VariableService

Controllers
  ├─> Services
  │   ├─> Repositories
  │   │   └─> Entities
  │   └─> Business logic
  └─> Models
      └─> Entities

Services
  ├─> Repositories
  └─> Entities

Repositories
  └─> Entities (via JPA)

Templates
  └─> Controllers (via Thymeleaf)

CSS
  └─> Templates (via HTML)
```

### Build Dependencies

```
build.gradle (defines dependencies)
  ├─> Spring Boot Starters
  ├─> Spring Data JPA
  ├─> Thymeleaf
  ├─> CommonMark (Markdown parser)
  ├─> SQLite JDBC driver
  └─> JUnit 5 (for testing)
```

---

## Naming Conventions

### Java Classes

```
Controllers: <Domain>Controller.java
Services: <Domain>Service.java
Repositories: <Domain>Repository.java
Entities: <Domain>.java
Exceptions: <Name>Exception.java
```

### Methods

```
Get: get<Thing>()
List: getAll<Things>(), list<Things>()
Find: find<Thing>()
Create: create<Thing>(), add<Thing>()
Update: update<Thing>()
Delete: delete<Thing>(), remove<Thing>()
Check: exists<Thing>(), is<Condition>()
Count: count<Things>()
```

### Variables

```
Private fields: camelCase (projectId, document, etc.)
Constants: UPPER_SNAKE_CASE (DEFAULT_SIZE)
Local variables: camelCase
```

### Packages

```
com.company.product.module
qbsss.docsCenter.docsCenterGradleGenerated.<layer>
```

---

## Size & Metrics

### Project Size

```
Source Code (src/main/java/): ~2000+ lines of Java
Templates (templates/): ~500+ lines of HTML
CSS (static/css/): ~300+ lines
Tests: ~200+ lines

Total: ~3000+ lines (excluding comments)
```

### Cloc (Count Lines of Code)

```bash
# Install cloc
choco install cloc  # Windows
brew install cloc   # macOS

# Count lines
cloc src/
```

---

## Git Structure (if applicable)

```
.git/                     # Git repository
.gitignore                # Ignored files
  - /build/
  - /gradle/
  - .gradle/
  - *.class
  - *.db
```

---

## Important Notes

1. **Note on Package Name:** Controller package jest nazwany `contoller` (z literówką) zamiast `controller`. To jest umyślne i zostało zachowane dla kompatybilności z istniejącym kodem.

2. **Deprecated Classes:** Klasy związane z YAML (`ProjektyYamlReader`, itd.) są oznaczone jako DEPRECATED - można je usunąć w przyszłości.

3. **Database Location:** SQLite database `storage.db` jest przechowywany w `/data` folder. Upewnij się, że folder istnieje i ma prawa zapisu.

4. **Build Artifacts:** Zawartość `/build` jest generowana automatycznie. Nie należy commitować tego do git'a.

---

**Koniec: STRUKTURA.md**

