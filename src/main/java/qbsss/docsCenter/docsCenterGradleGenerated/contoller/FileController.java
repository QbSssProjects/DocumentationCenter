package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.StoredFile;
import qbsss.docsCenter.docsCenterGradleGenerated.service.dbServices.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public Long upload(@RequestParam("file") MultipartFile file) throws Exception {
        return service.save(file).getId();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        StoredFile file = service.get(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .body(file.getData());
    }
}