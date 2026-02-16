package qbsss.docsCenter.docsCenterGradleGenerated.contoller;


import org.apache.catalina.Globals;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import qbsss.docsCenter.docsCenterGradleGenerated.service.DocumentService;
import qbsss.docsCenter.docsCenterGradleGenerated.service.ListOfProjectsService;
import qbsss.docsCenter.docsCenterGradleGenerated.utils.ProjektyYamlReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Controller
@RequestMapping("/viewDocs")
public class ViewDocumentationFilesController {

    private final DocumentService documentService;

    public ViewDocumentationFilesController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping
    public String viewDocs(Model model) throws IOException {

        File file = new File(System.getProperty("user.dir"), "projekty.yaml");

        model.addAttribute("documents", documentService.getDocuments());


        Map<Integer, String> projekty =
                ProjektyYamlReader.readProjekty("projekty.yaml");
        // Przekazanie mapy do th
        model.addAttribute("projects", projekty);




        return "viewDocumentationFiles";
    }
}