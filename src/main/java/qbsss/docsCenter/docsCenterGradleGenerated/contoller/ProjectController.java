package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Project;
import qbsss.docsCenter.docsCenterGradleGenerated.service.DocumentService;
import qbsss.docsCenter.docsCenterGradleGenerated.service.ProjectService;

import java.util.List;

/**
 * Controller zarządzający projektami.
 * Obsługuje tworzenie, edycję, usuwanie i wyświetlanie projektów.
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final DocumentService documentService;

    public ProjectController(ProjectService projectService, DocumentService documentService) {
        this.projectService = projectService;
        this.documentService = documentService;
    }

    /**
     * Wyświetlenie listy wszystkich projektów
     */
    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "viewDocumentationFiles";
    }

    /**
     * Wyświetlenie szczegółów projektu
     */
    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        long documentCount = documentService.countDocumentsInProject(id);

        model.addAttribute("project", project);
        model.addAttribute("documents", project.getDocuments());
        model.addAttribute("documentCount", documentCount);
        return "viewProject";
    }

    /**
     * Formularz tworzenia nowego projektu
     */
    @GetMapping("/new")
    public String newProjectForm() {
        return "addProject";
    }

    /**
     * Tworzenie nowego projektu
     */
    @PostMapping
    public String createProject(
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        projectService.createProject(name, description);
        return "redirect:/viewDocs";
    }

    /**
     * Formularz edycji projektu
     */
    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "addProject";
    }

    /**
     * Aktualizacja projektu
     */
    @PostMapping("/{id}/update")
    public String updateProject(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        projectService.updateProject(id, name, description);
        return "redirect:/projects/" + id;
    }

    /**
     * Usunięcie projektu (z potwierdzeniem)
     */
    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    /**
     * API REST - Pobranie wszystkich projektów (JSON)
     */
    @GetMapping("/api/all")
    @ResponseBody
    public List<Project> getAllProjectsAPI() {
        return projectService.getAllProjects();
    }

    /**
     * API REST - Pobranie projektu po ID (JSON)
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public Project getProjectAPI(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    /**
     * API REST - Tworzenie projektu
     */
    @PostMapping("/api/create")
    @ResponseBody
    public Project createProjectAPI(
            @RequestParam String name,
            @RequestParam(required = false) String description) {

        return projectService.createProject(name, description);
    }
}