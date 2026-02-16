package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import qbsss.docsCenter.docsCenterGradleGenerated.exceptions.FileIOException;
import qbsss.docsCenter.docsCenterGradleGenerated.utils.ProjektyYamlReader;
import qbsss.docsCenter.docsCenterGradleGenerated.utils.ViewProjects;
import qbsss.docsCenter.docsCenterGradleGenerated.utils.YamlWriterProjectUtil;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/addProject")
public class AddProjectController {

    private final File file =
            new File(System.getProperty("user.dir"), "projekty.yaml");


    // =========================
    // GET – wyświetlenie strony
    // =========================

    ViewProjects vp = new ViewProjects();
    @RequestMapping
    public String viewDocs(Model model) throws IOException {

        return vp.viewDocs(model);


    }
    // =========================
    // POST – zapis z inputa
    // =========================
    @PostMapping

    public String addProject(@RequestParam String projectName ) throws Exception {

        // zabezpieczenie
        if ((projectName == null || projectName.isBlank() ) && !projectName.contains("{") && !projectName.contains("}")) {
            return "redirect:/addProject";
        }


        // dopisanie do pliku
        YamlWriterProjectUtil ywr = new YamlWriterProjectUtil();
        ywr.writeConfig(projectName, ViewProjects.getProjects().keySet().size() + 1);


        // redirect = odświeżenie widoku (PRG pattern)
        return "redirect:/addProject";
    }
}
