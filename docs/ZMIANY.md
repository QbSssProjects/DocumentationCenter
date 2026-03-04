# 📝 Historia Zmian (ZMIANY.md)

## Spis Treści

1. [Problem #1](#problem-1-projekty-z-yaml)
2. [Problem #2](#problem-2-dokumenty-poza-projektami)
3. [Problem #3](#problem-3-dokumenty-widoczne-globalnie)
4. [Timeline](#timeline)

---

## Problem #1: Projekty z YAML

### Status: ✅ ROZWIĄZANE

### Opis Problemu

Aplikacja czytała projekty z pliku YAML (`projekty.yaml`) zamiast z bazy SQLite.

```java
// ❌ STARA IMPLEMENTACJA
List<Project> projects = ProjektyYamlReader.readProjekty("projekty.yaml");
model.addAttribute("projects", projects);
```

### Przyczyna

- `ViewDocumentationFilesController` używał `ProjektyYamlReader`
- Baza SQLite była ignorowana
- Zmiany w bazie nie były widoczne w UI

### Rozwiązanie

**ViewDocumentationFilesController.java:**

```java
// ✅ NOWA IMPLEMENTACJA
@GetMapping("/viewDocs")
public String viewDocs(Model model) {
    // Pobieranie z SQLite
    List<Project> projects = projectService.getAllProjects();
    model.addAttribute("projects", projects);
    System.out.println("✅ Loaded " + projects.size() + " projects from SQLite");
    return "viewDocumentationFiles";
}
```

**viewDocumentationFiles.html:**

```html
<!-- Przed: -->
<a th:each="entry : ${projects}"
   th:text="${entry.value}"
   th:href="@{/viewProject(id=${entry.key})}">

<!-- Po: -->
<a th:each="project : ${projects}"
   th:text="${project.name}"
   th:href="@{/projects/{id}(id=${project.id})}">
```

### Rezultat

✅ Aplikacja teraz czyta z SQLite  
✅ Zmiany są natychmiastowe  
✅ Brak potrzeby edytowania YAML

---

## Problem #2: Dokumenty Poza Projektami

### Status: ✅ ROZWIĄZANE

### Opis Problemu

Dokumenty mogły być tworzone bez powiązania z projektem.

```java
// ❌ STARA IMPLEMENTACJA
public Document save(String title, String markdown) {
    Project defaultProject = getOrCreateDefaultProject();
    // ... dokument bez bezpośredniego projektu!
}
```

### Przyczyna

- Automatyczne przypisanie do "domyślnego projektu"
- Formularz nie wymagał `projectId`
- Dokumenty mogły istnieć bez jasnego kontekstu

### Rozwiązanie

**DocumentService.java:**

```java
// ✅ NOWA IMPLEMENTACJA
public Document save(Long projectId, String title, String markdown) {
    // Walidacja projektu
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Projekt nie istnieje"
        ));
    
    Document document = new Document();
    document.setProject(project);  // ← OBOWIĄZKOWE!
    document.setTitle(title);
    document.setContent(markdown);
    
    return documentRepository.save(document);
}
```

**DocumentController.java:**

```java
// ✅ NOWY ENDPOINT
@GetMapping("/new/{projectId}")
public String newDoc(@PathVariable Long projectId, Model model) {
    projectService.getProjectById(projectId);  // Walidacja
    model.addAttribute("projectId", projectId);
    return "editor";
}

// ✅ ZMIENIONY ENDPOINT
@PostMapping("/save")
public String save(
    @RequestParam(required = false) Long id,
    @RequestParam Long projectId,  // ← OBOWIĄZKOWY!
    @RequestParam String title,
    @RequestParam String content) {
    
    if (id != null) {
        documentService.update(id, title, content);
    } else {
        documentService.save(projectId, title, content);  // Wymaga projektId
    }
    return "redirect:/projects/" + projectId;
}
```

**Document.java (Entity):**

```java
@ManyToOne(optional = false)  // ← NOT NULL
@JoinColumn(name = "project_id", nullable = false)
private Project project;
```

### Rezultat

✅ Każdy dokument MUSI mieć projekt  
✅ Brak "domyślnego projektu"  
✅ Dokumenty zawsze w kontekście projektów  
✅ Walidacja na wielu poziomach (Entity, Service, Controller)

---

## Problem #3: Dokumenty Widoczne Globalnie

### Status: ✅ ROZWIĄZANE

### Opis Problemu

Dokumenty były widoczne na głównej liście zamiast być dostępne tylko w projektach.

### Przyczyna

- Brak filtrowania dokumentów po `project_id`
- Brak relacji między dokumentami i projektami w UI

### Rozwiązanie

**ProjectController.java:**

```java
@GetMapping("/{id}")
public String viewProject(@PathVariable Long id, Model model) {
    Project project = projectService.getProjectById(id);
    long documentCount = documentService.countDocumentsInProject(id);
    
    model.addAttribute("project", project);
    model.addAttribute("documents", project.getDocuments());
    model.addAttribute("documentCount", documentCount);
    return "viewProject";
}
```

**viewProject.html:**

```html
<!-- Dokumenty widoczne TYLKO w projekcie -->
<div th:each="doc : ${project.documents}">
    <a th:href="@{/docs/view/{id}(id=${doc.id})}">
        <h3 th:text="${doc.title}"></h3>
    </a>
</div>

<!-- Przycisk do nowego dokumentu W PROJEKCIE -->
<a th:href="@{/docs/new/{projectId}(projectId=${project.id})}">
    Dodaj nowy dokument
</a>
```

### Rezultat

✅ Dokumenty dostępne tylko w kontekście projektu  
✅ Przycisk "Dodaj dokument" w projekcie wymusza projektId  
✅ Nie ma globalnej listy dokumentów

---

## Timeline

### Wersja 0.0.1-SNAPSHOT

| Data | Problem | Status | Notatki |
|------|---------|--------|---------|
| 2025-01-XX | Initial Setup | ✅ | Spring Boot 4.0.2 |
| 2025-01-XX | Problem #1 (YAML) | ✅ | Migracja na SQLite |
| 2025-01-XX | Problem #2 (Brak ProjectId) | ✅ | Dokumenty w projektach |
| 2025-01-XX | Problem #3 (Dokumenty globalnie) | ✅ | Izolacja do projektów |

### Commits (Example)

```
commit abc123 - "Setup initial Spring Boot project"
commit def456 - "Migrate from YAML to SQLite database"
commit ghi789 - "Make documents belong to projects (required projectId)"
commit jkl012 - "Isolate documents to project context"
commit mno345 - "Add comprehensive documentation"
```

---

## Kluczowe Zmiany

### Architektura

```
Przed:                          Po:
┌──────────────┐               ┌──────────────┐
│   YAML File  │               │  SQLite DB   │
└──────┬───────┘               └──────┬───────┘
       │                              │
       ├─ Projects                     ├─ Projects
       │  ├─ Default Project          │  ├─ Project 1
       │  └─ Documents (anywhere)      │  │  └─ Documents (w projekcie)
       │     ├─ Doc 1                 │  ├─ Project 2
       │     ├─ Doc 2                 │  │  └─ Documents (w projekcie)
       │     └─ Doc N                 │  └─ ...
```

### Endpoints

```
Przed:                    Po:
GET /viewDocs            GET /viewDocs
  └─ YAML projekty         └─ SQLite projekty

POST /docs/save          GET /docs/new/{projectId}
  └─ Brak projectId        └─ Wymaga projectId

                        POST /docs/save [projectId, ...]
                          └─ Wymaga projectId

                        GET /projects/{id}
                          └─ Wyświetla dokumenty projektu
```

### Data Model

```
Przed:                      Po:
Project                     Project
  ├─ id                       ├─ id
  ├─ name                     ├─ name
  └─ [loose reference]        └─ documents (OneToMany, FK)

Document                    Document
  ├─ id                       ├─ id
  ├─ title                    ├─ project_id (NOT NULL) ← OBOWIĄZKOWY!
  └─ project: ???             ├─ title
                              └─ content
```

---

## Lessons Learned

1. **Zawsze definiuj relacje JPA** - `@OneToMany`, `@ManyToOne` są ważne
2. **NOT NULL constraints** - Wymuszaj logikę na poziomie bazy
3. **Migrate from YAML to DB** - Bazy danych są lepsze dla aplikacji
4. **Filtruj dane w UI** - Nie pokazuj wszystkiego, filtruj po kontekście
5. **Comprehensive Testing** - Testuj wszystkie scenariusze użycia

---

**Koniec: ZMIANY.md**

