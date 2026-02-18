package qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

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

    public void setFilename(@Nullable String originalFilename) {
        this.filename = originalFilename;
    }
    public String getFilename() { return filename; }
    public String getContentType() { return contentType; }
    public long getSize() { return size; }
    public byte[] getData() { return data; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getId() { return id; }

    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setData(byte[] bytes) {
        this.data = bytes;
    }


    // gettery/settery
}
