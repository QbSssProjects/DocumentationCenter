package qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity reprezentujący projekt w systemie dokumentacji.
 * Projekt zawiera wiele dokumentów.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    // Konstruktory
    public Project() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Project(String name) {
        this();
        this.name = name;
    }

    public Project(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    // Gettery
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    // Settery
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * Dodanie dokumentu do projektu
     */
    public void addDocument(Document document) {
        this.documents.add(document);
        document.setProject(this);
    }

    /**
     * Usunięcie dokumentu z projektu
     */
    public void removeDocument(Document document) {
        this.documents.remove(document);
        document.setProject(null);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}