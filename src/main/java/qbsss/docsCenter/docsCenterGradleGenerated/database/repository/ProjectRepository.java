package qbsss.docsCenter.docsCenterGradleGenerated.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Project;

import java.util.Optional;

/**
 * Repository dla zarządzania projektami w bazie danych SQLite.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Wyszukiwanie projektu po nazwie
     */
    Optional<Project> findByName(String name);

    /**
     * Sprawdzenie czy projekt o danej nazwie istnieje
     */
    boolean existsByName(String name);
}