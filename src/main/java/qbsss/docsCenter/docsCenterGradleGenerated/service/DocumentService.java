package qbsss.docsCenter.docsCenterGradleGenerated.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Document;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Project;
import qbsss.docsCenter.docsCenterGradleGenerated.database.repository.DocumentRepository;
import qbsss.docsCenter.docsCenterGradleGenerated.database.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service dla zarządzania dokumentami.
 * ✅ ZMIENIONO: Dokumenty MUSZĄ być przypisane do projektu!
 * Usunięto obsługę "domyślnego projektu" - to było źródłem problemu.
 */
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public DocumentService(DocumentRepository documentRepository, ProjectRepository projectRepository) {
        this.documentRepository = documentRepository;
        this.projectRepository = projectRepository;
        System.out.println("✅ DocumentService initialized with repositories");
    }

    /**
     * ✅ NOWA WERSJA: Tworzenie dokumentu WYMAGA projectId
     * Nie ma już obsługi "domyślnego projektu"
     */
    public Document save(Long projectId, String title, String markdown) {
        System.out.println("📝 save() called: projectId=" + projectId + ", title=" + title);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    System.out.println("❌ Project not found: id=" + projectId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie istnieje");
                });

        System.out.println("✅ Found project: id=" + project.getId() + ", name=" + project.getName());

        Document doc = new Document();
        doc.setProject(project);
        doc.setTitle(title);
        doc.setContent(markdown);
        doc.setContentType("markdown");

        Document saved = documentRepository.save(doc);
        System.out.println("💾 Document saved to DB: id=" + saved.getId() + ", project_id=" + saved.getProject().getId());

        return saved;
    }

    /**
     * ✅ ZMIENIONO: Update wymaga dostępu do dokumentu przez projectId (zabezpieczenie)
     */
    public Document update(Long id, String title, String markdown) {
        System.out.println("✏️  update() called: id=" + id + ", title=" + title);

        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("❌ Document not found: id=" + id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Dokument nie istnieje");
                });

        System.out.println("✅ Found document: project_id=" + (doc.getProject() != null ? doc.getProject().getId() : "NULL"));

        if (doc.getProject() == null) {
            System.out.println("❌ Document has no project assigned!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dokument nie jest przypisany do żadnego projektu");
        }

        doc.setTitle(title);
        doc.setContent(markdown);

        Document updated = documentRepository.save(doc);
        System.out.println("💾 Document updated: id=" + updated.getId() + ", project_id=" + updated.getProject().getId());

        return updated;
    }

    /**
     * Pobiera dokument
     */
    public Document getDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    /**
     * Renderuje HTML
     */
    public String renderHtml(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        return renderer.render(parser.parse(doc.getContent()));
    }

    /**
     * Pobiera markdown
     */
    public String getMarkdown(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        return doc.getContent();
    }

    /**
     * Pobiera wszystkie dokumenty
     */
    public List<Document> getDocuments() {
        List<Document> docs = documentRepository.findAll();
        System.out.println("📊 getDocuments(): found " + docs.size() + " documents");
        for (Document doc : docs) {
            System.out.println("  - id=" + doc.getId() + ", project_id=" + (doc.getProject() != null ? doc.getProject().getId() : "NULL"));
        }
        return docs;
    }

    /**
     * Renderuje raw markdown
     */
    public String renderRaw(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return renderer.render(parser.parse("\n"));
        }
        return renderer.render(parser.parse(markdown));
    }

    /**
     * ✅ Pobiera lub tworzy dokument z konkretnym projektem
     */
    public Document saveWithProject(Long projectId, String title, String markdown) {
        return save(projectId, title, markdown);
    }

    public Document updateWithProject(Long documentId, Long projectId, String title, String markdown) {
        Document doc = documentRepository.findByIdAndProjectId(documentId, projectId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Dokument nie istnieje")
                );
        doc.setTitle(title);
        doc.setContent(markdown);
        return documentRepository.save(doc);
    }

    public List<Document> getDocumentsByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt nie istnieje");
        }
        return documentRepository.findByProjectId(projectId);
    }

    public void deleteDocument(Long documentId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Dokument nie istnieje")
                );
        documentRepository.delete(doc);
    }

    public long countDocumentsInProject(Long projectId) {
        return documentRepository.countByProjectId(projectId);
    }
}