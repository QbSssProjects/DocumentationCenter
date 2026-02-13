package qbsss.docsCenter.docsCenterGradleGenerated.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProjektyYamlReader {

    private ProjektyYamlReader() {
        // blokada tworzenia instancji
    }

    public static void projektyReader(String[] args) {
        String filePath = "projekty.yaml";

        try {
            Map<Integer, String> projekty = readProjekty(filePath);

            System.out.println("Wczytane projekty:");
            projekty.forEach((id, name) ->
                    System.out.println("ID: " + id + " → NAME: " + name)
            );

        } catch (Exception e) {
            System.err.println("Błąd odczytu YAML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<Integer, String> readProjekty(String filePath) throws IOException {

        Yaml yaml = new Yaml();
        Map<Integer, String> projektyMapa = new LinkedHashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath)) {

            Map<String, Object> root = yaml.load(fis);

            if (root == null || !root.containsKey("projekty")) {
                return projektyMapa;
            }

            List<Map<String, Object>> projekty =
                    (List<Map<String, Object>>) root.get("projekty");

            for (Map<String, Object> projekt : projekty) {

                if (!projekt.containsKey("id") || !projekt.containsKey("name")) {
                    continue; // pomiń niepełne wpisy
                }

                Integer id = ((Number) projekt.get("id")).intValue();
                String name = projekt.get("name").toString();

                if (projektyMapa.containsKey(id)) {
                    throw new IllegalStateException(
                            "Duplikat ID w YAML: " + id
                    );
                }

                projektyMapa.put(id, name);
            }
        }

        return projektyMapa;
    }
}