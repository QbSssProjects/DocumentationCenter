package qbsss.docsCenter.docsCenterGradleGenerated.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Document;

import java.util.List;
import java.util.Optional;

/**
 * Repository dla zarządzania dokumentami w bazie danych SQLite.
 * Zawiera query do filtrowania dokumentów po projektach.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {



    /**
     * Pobranie wszystkich dokumentów dla danego projektu
     */
    List<Document> findByProjectId(Long projectId);

    /**
     * Pobranie dokumentu po ID i projekcie (zabezpieczenie)
     */
    Optional<Document> findByIdAndProjectId(Long id, Long projectId);

    /**
     * Policzenie dokumentów w projekcie
     */
    long countByProjectId(Long projectId);

    /**
     * Usunięcie wszystkich dokumentów dla projektu (kaskadowe)
     */
    void deleteByProjectId(Long projectId);

    Optional<Document> findById(Long id);
}