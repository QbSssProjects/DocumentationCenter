package qbsss.docsCenter.docsCenterGradleGenerated.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Document;
import qbsss.docsCenter.docsCenterGradleGenerated.database.repository.DocumentRepository;
import qbsss.docsCenter.docsCenterGradleGenerated.items.DocumentItem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();



    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }


    public Document save(String title, String markdown) {
        Document doc = new Document();
        doc.setTitle(title);
        doc.setContent(Arrays.toString(markdown.getBytes(StandardCharsets.UTF_8)));
        return repository.save(doc);
    }

    public Document update(Long id, String markdown) {
        Document doc = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        doc.setContent(Arrays.toString(markdown.getBytes(StandardCharsets.UTF_8)));
        return repository.save(doc);
    }

    public String renderHtml(Long id) {
        Document doc = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        String markdown = new String(doc.getContent().getBytes(), StandardCharsets.UTF_8);
        return renderer.render(parser.parse(markdown));
    }

    public String getMarkdown(Long id) {
        Document doc = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        return new String(doc.getContent().getBytes(), StandardCharsets.UTF_8);
    }

    public List<Document> getDocuments() {
        return repository.findAll();
    }
    public String renderRaw(String markdown) {
        return renderer.render(parser.parse(markdown));
    }

}

