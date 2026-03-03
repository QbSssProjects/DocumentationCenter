package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Project;
import qbsss.docsCenter.docsCenterGradleGenerated.service.ProjectService;

import java.util.List;

/**
 * Kontroler do przeglądania dokumentów i projektów.
 * ZMIENIONO: Projekty są teraz pobierane z SQLite zamiast YAML.
 */
@Controller
@RequestMapping("/viewDocs")
public class ViewDocumentationFilesController {

    private final ProjectService projectService;

    public ViewDocumentationFilesController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping
    public String viewDocs(Model model) {
        // ✅ Pobieranie projektów z SQLite zamiast YAML
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

        System.out.println("✅ ViewDocs: Loaded " + projects.size() + " projects from SQLite");

        return "viewDocumentationFiles";
    }
}