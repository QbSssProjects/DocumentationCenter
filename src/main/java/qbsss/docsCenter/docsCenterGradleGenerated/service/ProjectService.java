package qbsss.docsCenter.docsCenterGradleGenerated.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Project;
import qbsss.docsCenter.docsCenterGradleGenerated.database.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service dla zarządzania projektami.
 * Zapewnia CRUD operacje i biznesową logikę projektów.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Utworzenie nowego projektu
     */
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

    /**
     * Pobranie wszystkich projektów
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Pobranie projektu po ID
     */
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Projekt o ID " + id + " nie istnieje"
                        )
                );
    }

    /**
     * Pobranie projektu po nazwie
     */
    public Project getProjectByName(String name) {
        return projectRepository.findByName(name)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Projekt o nazwie '" + name + "' nie istnieje"
                        )
                );
    }

    /**
     * Aktualizacja projektu
     */
    public Project updateProject(Long id, String name, String description) {
        Project project = getProjectById(id);

        // Jeśli zmienia się nazwa, sprawdzić czy nowa jest unikalna
        if (!project.getName().equals(name) && projectRepository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Projekt o nazwie '" + name + "' już istnieje"
            );
        }

        project.setName(name);
        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    /**
     * Usunięcie projektu
     * Dokumenty zostaną usunięte kaskadowo (ON DELETE CASCADE)
     */
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }

    /**
     * Sprawdzenie czy projekt istnieje
     */
    public boolean existsById(Long id) {
        return projectRepository.existsById(id);
    }

    /**
     * Policzenie wszystkich projektów
     */
    public long countProjects() {
        return projectRepository.count();
    }
}