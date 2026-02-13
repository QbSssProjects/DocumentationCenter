package qbsss.docsCenter.docsCenterGradleGenerated;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import qbsss.docsCenter.docsCenterGradleGenerated.service.VariableService;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Kod wykonywany PO starcie webserwera
    @Bean
    CommandLineRunner runAtStartup(VariableService variableService) {
        return args -> {
            System.out.println("== STARTUP LOGIC ==");

            variableService.set("appName", "Dynamic Spring Server");
            variableService.set("version", "1.0.0");

            System.out.println("App name: " + variableService.get("appName"));
            System.out.println("Version : " + variableService.get("version"));
        };
    }
}
