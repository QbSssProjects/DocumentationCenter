package qbsss.docsCenter.docsCenterGradleGenerated.contoller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import qbsss.docsCenter.docsCenterGradleGenerated.exceptions.FileIOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileIOException.class)
    public String handleNotFound(FileIOException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error"; // Nazwa pliku HTML w src/main/resources/templates
    }
}