package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import qbsss.docsCenter.docsCenterGradleGenerated.database.dbItems.Document;
import qbsss.docsCenter.docsCenterGradleGenerated.service.DocumentService;

@Controller
@RequestMapping("/docs")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String newDoc() {
        return "editor";
    }

    @PostMapping("/save")
    public String save(@RequestParam String title,
                       @RequestParam String content) {

        Document doc = service.save(title, content);
        return "redirect:/docs/edit/" + doc.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("content", service.getMarkdown(id));
        return "editor";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("html", service.renderHtml(id));
        return "viewer";
    }
    @PostMapping("/preview")
    @ResponseBody
    public String preview(@RequestBody String markdown) {
        System.out.println("DEBUG: " +  markdown);
        if(markdown==null)
        {
            return service.renderRaw("\n");
        }
        else
        {
            return service.renderRaw(markdown);
        }

    }
}