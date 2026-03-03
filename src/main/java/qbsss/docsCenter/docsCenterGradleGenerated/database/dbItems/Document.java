package qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity reprezentujący dokument w systemie dokumentacji.
 * Każdy dokument musi być przypisany do dokładnie jednego projektu.
 */
@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_documents_project_id", columnList = "project_id"),
        @Index(name = "idx_documents_created_at", columnList = "created_at")
})
public class Document {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;


    /**
     * Relacja ManyToOne - dokument należy do jednego projektu
     */


    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // JSON z Tiptap lub Markdown

    @Column(nullable = false)
    private String contentType; // markdown / html / json

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Konstruktory
    public Document() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Document(Project project, String title, String content, String contentType) {
        this();
        this.project = project;
        this.title = title;
        this.content = content;
        this.contentType = contentType;
    }

    // Gettery
    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getDescription() {
        return description;
    }

    // Settery
    public void setProject(Project project) {
        this.project = project;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }







    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", projectId=" + (project != null ? project.getId() : null) +
                ", title='" + title + '\'' +
                ", contentType='" + contentType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}