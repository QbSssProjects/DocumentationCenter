# 🎯 Entity Models

## Spis Treści

1. [Project Entity](#project-entity)
2. [Document Entity](#document-entity)
3. [StoredFile Entity](#storedfile-entity)
4. [Relacje JPA](#relacje-jpa)
5. [Lifecycle Hooks](#lifecycle-hooks)

---

## Project Entity

**Lokalizacja:** `src/main/java/qbsss/docsCenter/docsCenterGradleGenerated/database/dbItems/Project.java`

### Kod

```java
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, 
               orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();
    
    // Konstruktory, gettery, settery...
}
```

### Pola

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | Long | PK, AUTO | Unikalny identyfikator |
| `name` | String | UNIQUE, NOT NULL | Nazwa projektu |
| `description` | String | TEXT | Opis (opcjonalny) |
| `createdAt` | LocalDateTime | NOT NULL, UPDATABLE=false | Data utworzenia |
| `updatedAt` | LocalDateTime | NOT NULL | Data edycji |
| `documents` | List<Document> | OneToMany, LAZY | Dokumenty projektu |

### Konstruktory

```java
// Default
Project p = new Project();

// Z nazwą
Project p = new Project("Mój Projekt");

// Z nazwą i opisem
Project p = new Project("Mój Projekt", "Opis projektu");
```

### Relacja OneToMany

```java
@OneToMany(
    mappedBy = "project",              // Pole w Document
    cascade = CascadeType.ALL,         // Kaskadowe operacje
    orphanRemoval = true,              // Usuwaj sieroty
    fetch = FetchType.LAZY             // Lazy loading
)
private List<Document> documents;
```

**Znaczenie:**
- 1 projekt → N dokumentów
- Usunięcie projektu usuwa dokumenty (CASCADE)
- Dokumenty bez projektu są usuwane (orphanRemoval)
- Dokumenty ładowane na żądanie (LAZY)

---

## Document Entity

**Lokalizacja:** `src/main/java/qbsss/docsCenter/docsCenterGradleGenerated/database/dbItems/Document.java`

### Kod

```java
@Entity
@Table(name = "documents", indexes = {
    @Index(name = "idx_documents_project_id", columnList = "project_id"),
    @Index(name = "idx_documents_created_at", columnList = "created_at")
})
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;  // ✅ OBOWIĄZKOWE!
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(nullable = false)
    private String contentType;  // "markdown", "html", etc.
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Konstruktory, gettery, settery...
}
```

### Pola

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | Long | PK, AUTO | Unikalny ID |
| `project` | Project | ManyToOne, NOT NULL | Projekt zawierający dokument |
| `title` | String | NOT NULL | Tytuł dokumentu |
| `content` | String | TEXT, NOT NULL | Zawartość (Markdown/HTML) |
| `contentType` | String | NOT NULL | Typ: "markdown", "html" |
| `description` | String | TEXT | Krótki opis |
| `createdAt` | LocalDateTime | NOT NULL, UPDATABLE=false | Data utworzenia |
| `updatedAt` | LocalDateTime | NOT NULL | Data edycji |

### ✅ KLUCZOWE: Project jest Obowiązkowy

```java
@ManyToOne(optional = false)  // ← NOT NULL
@JoinColumn(name = "project_id", nullable = false)
private Project project;
```

**Znaczenie:**
- Każdy dokument MUSI mieć projekt
- Brak możliwości tworzenia dokumentów bez projektu
- `project_id` nigdy nie może być NULL w bazie

### Konstruktory

```java
// Default
Document doc = new Document();

// Z pełnymi danymi
Document doc = new Document(
    project,
    "Tytuł",
    "# Markdown content",
    "markdown"
);
```

### Relacja ManyToOne

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "project_id", nullable = false)
private Project project;
```

**Znaczenie:**
- N dokumentów → 1 projekt
- Lazy loading dla wydajności
- Klucz obcy `project_id` przechowywany w `documents` tabeli
- `nullable = false` wymusza projekt w bazie

---

## StoredFile Entity

**Lokalizacja:** `src/main/java/qbsss/docsCenter/docsCenterGradleGenerated/database/dbItems/StoredFile.java`

### Kod

```java
@Entity
@Table(name = "stored_files")
public class StoredFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false, length = 500)
    private String filePath;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Konstruktory, gettery, settery...
}
```

### Pola

| Pole | Typ | Constraints | Opis |
|------|-----|-------------|------|
| `id` | Long | PK, AUTO | Unikalny ID |
| `project` | Project | ManyToOne, NOT NULL | Projekt zawierający plik |
| `fileName` | String | NOT NULL | Nazwa pliku |
| `filePath` | String | NOT NULL | Ścieżka do pliku |
| `createdAt` | LocalDateTime | NOT NULL, UPDATABLE=false | Data upload'u |
| `updatedAt` | LocalDateTime | NOT NULL | Data edycji |

---

## Relacje JPA

### OneToMany (Project → Documents)

```
Project (1) ──── (N) Document
  ├─ id                 ├─ id
  ├─ name               ├─ project_id (FK)
  └─ documents          └─ title
       └─ List<Document>

Kod:
@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
private List<Document> documents;
```

### ManyToOne (Document → Project)

```
Document (N) ────── (1) Project
  ├─ id                  ├─ id
  ├─ project_id (FK) ──→ ├─ name
  └─ title               └─ documents

Kod:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "project_id", nullable = false)
private Project project;
```

### Cascade Delete

```
DELETE FROM projects WHERE id = 1
    ↓
CASCADE: DELETE FROM documents WHERE project_id = 1
    ↓
CASCADE: DELETE FROM stored_files WHERE project_id = 1
```

**Konfiguracja:**
```java
@OneToMany(cascade = CascadeType.ALL)
private List<Document> documents;
```

---

## Lifecycle Hooks

### @PrePersist - Przed Zapisaniem

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}
```

**Kiedy:** Przed pierwszym `save()`
**Użycie:** Ustawianie daty utworzenia

### @PreUpdate - Przed Aktualizacją

```java
@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

**Kiedy:** Przed `save()` na istniejącej encji
**Użycie:** Aktualizacja `updatedAt`

### Przykład Użycia

```java
Project p = new Project("Test");
projectRepository.save(p);  // @PrePersist → createdAt = now()

p.setName("Updated");
projectRepository.save(p);  // @PreUpdate → updatedAt = now()
```

---

## Best Practices

### ✅ DO's

```java
// ✅ Zawsze inicjalizuj List
private List<Document> documents = new ArrayList<>();

// ✅ Używaj Lazy Loading dla OneToMany
@OneToMany(fetch = FetchType.LAZY)

// ✅ Sprawdzaj czy relacja istnieje
if (document.getProject() != null) { ... }

// ✅ Usuwaj dokumenty przez projekt
projectRepository.delete(project);  // Docs będą usunięte
```

### ❌ DON'Ts

```java
// ❌ Nie rób null w List
// private List<Document> documents = null;

// ❌ Nie używaj EAGER jeśli niepotrzebny
// @OneToMany(fetch = FetchType.EAGER)

// ❌ Nie usuwaj dokumentów bez projektu
// documentRepository.delete(doc) bez sprawdzenia relacji

// ❌ Nie miej cyklicznych relacji
// Project → Document ← (nie rób backref do Project z Document)
```

---

**Koniec: ENTITY_MODELS.md**

