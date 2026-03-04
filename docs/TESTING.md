# 🐛 Testing & Debugging

## Spis Treści

1. [Unit Tests](#unit-tests)
2. [Integration Tests](#integration-tests)
3. [Manual Testing](#manual-testing)
4. [Debugging](#debugging)

---

## Unit Tests

### Lokalizacja

`src/test/java/qbsss/docsCenter/docsCenterGradleGenerated/DocsCenterGradleGeneratedApplicationTests.java`

### Uruchomienie

```bash
# Wszystkie testy
./gradlew test

# Konkretny test
./gradlew test --tests "DocsCenterGradleGeneratedApplicationTests"

# Verbose output
./gradlew test --info

# Wyłącz cache
./gradlew test --no-build-cache
```

### Test Report

```
build/reports/tests/test/index.html
```

---

## Integration Tests

### Spring Boot Test

```java
@SpringBootTest
class DocumentServiceTest {
    
    @MockBean
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentService documentService;
    
    @Test
    void testSaveDocument() {
        // Given
        Long projectId = 1L;
        Project project = new Project("Test");
        
        // When
        when(projectRepository.findById(projectId))
            .thenReturn(Optional.of(project));
        
        Document result = documentService.save(
            projectId, "Title", "Content"
        );
        
        // Then
        assertNotNull(result);
        assertEquals(projectId, result.getProject().getId());
    }
}
```

---

## Manual Testing

### Checklist

- [ ] Strona główna (`/`)
- [ ] Lista projektów (`/viewDocs`)
- [ ] Dodaj projekt (`/add-project`)
- [ ] Szczegóły projektu (`/projects/1`)
- [ ] Nowy dokument (`/docs/new/1`)
- [ ] Wyświetl dokument (`/docs/view/1`)
- [ ] Edytuj dokument (`/docs/edit/1`)
- [ ] Usuń dokument (`DELETE /docs/1`)
- [ ] Usuń projekt (`DELETE /projects/1`)

---

## Debugging

### Logowanie

```yaml
logging:
  level:
    qbsss.docsCenter: DEBUG
    org.springframework: INFO
    org.hibernate: DEBUG
```

### Debug Mode (IDE)

```
IntelliJ: Right-click Main.java → Debug 'Main.main()'
Eclipse: Right-click Main.java → Debug As → Java Application
VS Code: Click "Debug" button above main()
```

### SQL Logging

```yaml
spring:
  jpa:
    show-sql: true
```

---

**Koniec: TESTING.md**

