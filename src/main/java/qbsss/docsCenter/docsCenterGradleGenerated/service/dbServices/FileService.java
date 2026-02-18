package qbsss.docsCenter.docsCenterGradleGenerated.service.dbServices;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.StoredFile;
import qbsss.docsCenter.docsCenterGradleGenerated.database.repository.StoredFileRepository;

import java.io.IOException;

@Service
public class FileService {

    private final StoredFileRepository repository;

    public FileService(StoredFileRepository repository) {
        this.repository = repository;
    }

    public StoredFile save(MultipartFile file) throws IOException {
        StoredFile entity = new StoredFile();
        entity.setFilename(file.getOriginalFilename());
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());
        entity.setData(file.getBytes());

        return repository.save(entity);
    }

    public StoredFile get(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }
}

