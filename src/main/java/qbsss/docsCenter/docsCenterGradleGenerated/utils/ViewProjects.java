package qbsss.docsCenter.docsCenterGradleGenerated.utils;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import qbsss.docsCenter.docsCenterGradleGenerated.utils.ProjektyYamlReader;

import java.io.IOException;
import java.util.Map;

public class ViewProjects
{
     public static Map<Integer, String> projekty;

    @RequestMapping
    public String viewDocs(Model model) throws IOException {
         projekty =
                ProjektyYamlReader.readProjekty("projekty.yaml");


        // Przekazanie mapy do th
        model.addAttribute("projects", projekty);
        return "addProject";
    }
    public static Map<Integer, String> getProjects() {
        return projekty;
    }

}


