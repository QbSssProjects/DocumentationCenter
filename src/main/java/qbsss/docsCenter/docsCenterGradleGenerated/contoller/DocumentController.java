package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Document;
import qbsss.docsCenter.docsCenterGradleGenerated.service.DocumentService;
import qbsss.docsCenter.docsCenterGradleGenerated.service.ProjectService;

/**
 * Controller zarządzający dokumentami.
 * ✅ ZMIENIONO: Dokumenty MUSZĄ być przypisane do projektu!
 * Nowe dokumenty wymuszają podanie projectId.
 */
@Controller
@RequestMapping("/docs")
public class DocumentController {

    private final DocumentService documentService;
    private final ProjectService projectService;

    public DocumentController(DocumentService documentService, ProjectService projectService) {
        this.documentService = documentService;
        this.projectService = projectService;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // NOWE ENDPOINTY (wymagają projectId)
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * ✅ NOWY: Formularz nowego dokumentu - wymaga projectId
     */
    @GetMapping("/new/{projectId}")
    public String newDoc(@PathVariable Long projectId, Model model) {
        // Sprawdzenie czy projekt istnieje
        projectService.getProjectById(projectId);

        model.addAttribute("projectId", projectId);
        System.out.println("📝 newDoc() - Opening editor for projectId=" + projectId);
        return "editor";
    }

    /**
     * ✅ ZMIENIONO: Zapis dokumentu - wymaga projectId
     * - id: opcjonalny, jeśli przesłany to edycja, jeśli nie to tworzenie
     * - projectId: OBOWIĄZKOWY - ID projektu
     * - title: tytuł dokumentu
     * - content: zawartość markdown'u
     */
    @PostMapping("/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam Long projectId,
            @RequestParam String title,
            @RequestParam String content) {

        System.out.println("💾 save() called: projectId=" + projectId + ", id=" + id + ", title=" + title);

        Document doc;
        if (id != null) {
            // UPDATE - edycja istniejącego dokumentu
            doc = documentService.update(id, title, content);
            System.out.println("✅ Document updated: id=" + id);
        } else {
            // CREATE - nowy dokument MUSI mieć projectId!
            doc = documentService.save(projectId, title, content);
            System.out.println("✅ Document created: id=" + doc.getId() + ", projectId=" + projectId);
        }
        return "redirect:/projects/" + projectId;
    }

    /**
     * ✅ ZMIENIONO: Formularz edycji dokumentu
     * Pokazuje editor ze wskazaniem projektu
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Document doc = documentService.getDocument(id);

        if (doc.getProject() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dokument nie jest przypisany do projektu");
        }

        model.addAttribute("id", id);
        model.addAttribute("projectId", doc.getProject().getId());
        model.addAttribute("title", doc.getTitle());
        model.addAttribute("content", documentService.getMarkdown(id));
        System.out.println("📝 edit() - Opening editor for documentId=" + id + ", projectId=" + doc.getProject().getId());
        return "editor";
    }

    /**
     * ✅ Wyświetlenie dokumentu
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Document doc = documentService.getDocument(id);

        if (doc.getProject() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dokument nie jest przypisany do projektu");
        }

        model.addAttribute("html", documentService.renderHtml(id));
        return "viewer";
    }

    /**
     * ✅ Preview markdown'u (live preview w edytorze)
     */
    @PostMapping("/preview")
    @ResponseBody
    public String preview(@RequestBody String markdown) {
        if (markdown == null) {
            return documentService.renderRaw("\n");
        } else {
            return documentService.renderRaw(markdown);
        }
    }

    /**
     * ✅ Usunięcie dokumentu
     */
    @PostMapping("/{id}/delete")
    public String deleteDocument(@PathVariable Long id) {
        Document doc = documentService.getDocument(id);
        Long projectId = (doc.getProject() != null) ? doc.getProject().getId() : 1L;

        documentService.deleteDocument(id);
        System.out.println("🗑️  Document deleted: id=" + id);
        return "redirect:/projects/" + projectId;
    }
}