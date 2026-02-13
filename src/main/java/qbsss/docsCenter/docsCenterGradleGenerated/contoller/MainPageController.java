package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainPageController {
    @RequestMapping
    public String mainPage() {
        return "index";
    }
}
