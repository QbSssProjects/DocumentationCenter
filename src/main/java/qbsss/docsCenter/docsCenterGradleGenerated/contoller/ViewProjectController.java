package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/viewProject")
public class ViewProjectController {
    @RequestMapping
    public String viewProject() {
       // Integer id = ;

        return "viewProject";
    }


}
