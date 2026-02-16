package qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String contentType;

    private long size;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    private LocalDateTime createdAt = LocalDateTime.now();

    // gettery/settery
}
