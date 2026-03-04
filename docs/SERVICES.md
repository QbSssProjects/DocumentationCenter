# 🛠️ Services Layer

## Spis Treści

1. [ProjectService](#projectservice)
2. [DocumentService](#documentservice)
3. [FileService](#fileservice)
4. [Best Practices](#best-practices)

---

## ProjectService

**Lokalizacja:** `src/main/java/.../service/ProjectService.java`

### Przegląd

Serwis do zarządzania projektami. Zawiera CRUD operacje i logikę biznesową.

```java
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
}
```

### Metody

#### `createProject(String name, String description): Project`

Tworzy nowy projekt

```java
public Project createProject(String name, String description) {
    if (projectRepository.existsByName(name)) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Projekt o nazwie '" + name + "' już istnieje"
        );
    }
    
    Project project = new Project(name, description);
    return projectRepository.save(project);
}
```

**Walidacje:**
- Nazwa nie może być duplikatem (UNIQUE constraint)

**Status:**
- 200: Projekt utworzony
- 409: Nazwa już istnieje

---

#### `getAllProjects(): List<Project>`

Pobiera wszystkie projekty

```java
public List<Project> getAllProjects() {
    return projectRepository.findAll();
}
```

---

#### `getProjectById(Long id): Project`

Pobiera projekt po ID

```java
public Project getProjectById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Projekt o ID " + id + " nie istnieje"
            )
        );
}
```

---

#### `updateProject(Long id, String name, String description): Project`

Aktualizuje projekt

```java
public Project updateProject(Long id, String name, String description) {
    Project project = getProjectById(id);
    project.setName(name);
    project.setDescription(description);
    return projectRepository.save(project);
}
```

---

#### `deleteProject(Long id): void`

Usuwa projekt i wszystkie dokumenty

```java
public void deleteProject(Long id) {
    Project project = getProjectById(id);
    projectRepository.delete(project);
    // Dokumenty usuwane automatycznie (CASCADE)
}
```

---

#### `projectExists(Long id): boolean`

Sprawdza czy projekt istnieje

```java
public boolean projectExists(Long id) {
    return projectRepository.existsById(id);
}
```

---

## DocumentService

**Lokalizacja:** `src/main/java/.../service/DocumentService.java`

### Przegląd

Serwis do zarządzania dokumentami. **✅ ZMIENIONO:** Wymaga `projectId`!

```java
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;
    
    public DocumentService(DocumentRepository documentRepository,
                          ProjectRepository projectRepository) {
        this.documentRepository = documentRepository;
        this.projectRepository = projectRepository;
    }
}
```

### Metody

#### `save(Long projectId, String title, String markdown): Document`

**✅ KLUCZOWE:** Tworzenie dokumentu WYMAGA `projectId`!

```java
public Document save(Long projectId, String title, String markdown) {
    // 1. Walidacja projektu
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Projekt nie istnieje"
        ));
    
    // 2. Tworzenie dokumentu
    Document doc = new Document();
    doc.setProject(project);  // ← OBOWIĄZKOWE!
    doc.setTitle(title);
    doc.setContent(markdown);
    doc.setContentType("markdown");
    
    // 3. Zapis
    return documentRepository.save(doc);
}
```

**Walidacje:**
- `projectId` nie może być null
- Projekt musi istnieć
- Title nie może być pusty
- Content nie może być puste

**Status:**
- 200: Dokument utworzony
- 404: Projekt nie istnieje

---

#### `getDocumentById(Long id): Document`

Pobiera dokument po ID

```java
public Document getDocumentById(Long id) {
    return documentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Dokument nie istnieje"
        ));
}
```

---

#### `update(Long id, String title, String markdown): Document`

Aktualizuje dokument

```java
public Document update(Long id, String title, String markdown) {
    Document doc = getDocumentById(id);
    
    // Walidacja
    if (doc.getProject() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Dokument nie jest przypisany do projektu"
        );
    }
    
    // Aktualizacja
    doc.setTitle(title);
    doc.setContent(markdown);
    return documentRepository.save(doc);
}
```

**Ważne:** Projekt dokumentu **NIE ZMIENIA SIĘ**!

---

#### `delete(Long id): void`

Usuwa dokument

```java
public void delete(Long id) {
    Document doc = getDocumentById(id);
    documentRepository.delete(doc);
    // Projekt pozostaje
}
```

---

#### `getDocumentsByProjectId(Long projectId): List<Document>`

Pobiera dokumenty projektu

```java
public List<Document> getDocumentsByProjectId(Long projectId) {
    return documentRepository.findByProjectId(projectId);
}
```

---

#### `countDocumentsInProject(Long projectId): long`

Liczy dokumenty w projekcie

```java
public long countDocumentsInProject(Long projectId) {
    return documentRepository.countByProjectId(projectId);
}
```

---

## FileService

**Lokalizacja:** `src/main/java/.../service/dbServices/FileService.java`

### Przegląd

Serwis do zarządzania plikami przechowywanymi w projektach.

```java
@Service
public class FileService {
    private final StoredFileRepository storedFileRepository;
    private final ProjectRepository projectRepository;
}
```

### Metody

#### `uploadFile(Long projectId, MultipartFile file): StoredFile`

Upload pliku do projektu

```java
public StoredFile uploadFile(Long projectId, MultipartFile file) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Projekt nie istnieje"
        ));
    
    StoredFile storedFile = new StoredFile();
    storedFile.setProject(project);
    storedFile.setFileName(file.getOriginalFilename());
    storedFile.setFilePath(saveFile(file));
    
    return storedFileRepository.save(storedFile);
}
```

---

#### `getFilesForProject(Long projectId): List<StoredFile>`

Pobiera pliki projektu

```java
public List<StoredFile> getFilesForProject(Long projectId) {
    return storedFileRepository.findByProjectId(projectId);
}
```

---

#### `deleteFile(Long fileId): void`

Usuwa plik

```java
public void deleteFile(Long fileId) {
    StoredFile file = storedFileRepository.findById(fileId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Plik nie istnieje"
        ));
    
    // Usuń fizycznie
    new File(file.getFilePath()).delete();
    
    // Usuń z bazy
    storedFileRepository.delete(file);
}
```

---

## Best Practices

### 1. Dependency Injection

```java
@Service
public class MyService {
    private final Repository repository;
    
    // ✅ Constructor injection (preferowany)
    public MyService(Repository repository) {
        this.repository = repository;
    }
    
    // ❌ Field injection (avoid)
    // @Autowired
    // private Repository repository;
}
```

### 2. Exception Handling

```java
// ✅ Rzuć ResponseStatusException
public Project getById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Nie znaleziono"
        ));
}

// ❌ Nie rób custom exceptions jeśli ResponseStatusException starczy
```

### 3. Validation

```java
// ✅ Waliduj w serwisie
public Document save(Long projectId, String title, String content) {
    if (projectId == null) {
        throw new IllegalArgumentException("projectId nie może być null");
    }
    
    Project project = projectRepository.findById(projectId)
        .orElseThrow(...);
    
    // ... rest of code
}
```

### 4. Transaction Management

```java
// ✅ Transakcje automatycznie dla CRUD
@Service  // Mamy @Transactional za darmo
public class MyService {
    public void save(...) { }  // Automatycznie w transakcji
}

// Jeśli potrzeba wielokrokowych operacji:
@Transactional
public void complexOperation() {
    // Kilka operacji w jednej transakcji
    repo1.save(...);
    repo2.save(...);
    // Jeśli coś failnie - rollback wszystko
}
```

### 5. Logging (Opcjonalne, ale dobre praktyki)

```java
@Service
@Slf4j  // Lombok
public class DocumentService {
    
    public Document save(Long projectId, String title, String content) {
        log.info("Saving document in project: {}", projectId);
        
        try {
            Document doc = new Document(...);
            return documentRepository.save(doc);
        } catch (Exception e) {
            log.error("Failed to save document", e);
            throw e;
        }
    }
}
```

---

**Koniec: SERVICES.md**

