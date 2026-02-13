package qbsss.docsCenter.docsCenterGradleGenerated.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class odpowiedzialna za zapis oraz odczyt konfiguracji projektów
 * w formacie YAML przy użyciu biblioteki SnakeYAML.
 *
 * <p>Struktura pliku YAML:
 * <pre>
 * projekty:
 *   - name: Projekt1
 *     id: 1
 *   - name: Projekt2
 *     id: 2
 * </pre>
 *
 * <p>Klasa zapewnia:
 * <ul>
 *     <li>Automatyczne tworzenie struktury root jeśli plik nie istnieje</li>
 *     <li>Kompatybilność wsteczną dla pojedynczego wpisu typu Map</li>
 *     <li>Formatowanie YAML w stylu blokowym</li>
 * </ul>
 *
 * <p>Uwaga: Metoda {@link #writeConfig(String, Integer)} nadpisuje cały plik
 * jednym dokumentem YAML (nie dopisuje wielu dokumentów YAML).
 */
public class YamlWriterProjectUtil {

    /**
     * Ścieżka do pliku konfiguracyjnego YAML.
     */
    private final Path configPath = Path.of("projekty.yaml");

    /**
     * Dodaje nowy wpis projektu do sekcji {@code projekty} w pliku YAML.
     *
     * <p>Jeżeli:
     * <ul>
     *     <li>plik nie istnieje — zostanie utworzony</li>
     *     <li>sekcja {@code projekty} nie istnieje — zostanie utworzona jako lista</li>
     *     <li>sekcja jest pojedynczą mapą — zostanie przekonwertowana na listę (kompatybilność wstecz)</li>
     * </ul>
     *
     * @param name nazwa projektu (nie powinna być null)
     * @param id   identyfikator projektu
     * @throws Exception gdy wystąpi błąd I/O lub niepoprawna struktura YAML
     * @throws IllegalStateException gdy struktura pola {@code projekty} jest nieobsługiwana
     */
    public void writeConfig(String name, Integer id) throws Exception {

        // Konfiguracja formatowania YAML
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(4);
        options.setIndicatorIndent(2);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        Yaml yaml = new Yaml(options);

        // Odczyt aktualnego root dokumentu
        Map<String, Object> root = readRoot(yaml);

        // Tworzenie nowego wpisu projektu
        Map<String, Object> newEntry = new LinkedHashMap<>();
        newEntry.put("name", name);
        newEntry.put("id", id);

        Object projektyNode = root.get("projekty");
        List<Map<String, Object>> projektyList;

        if (projektyNode == null) {
            // Sekcja nie istnieje — tworzymy nową listę
            projektyList = new ArrayList<>();
            root.put("projekty", projektyList);

        } else if (projektyNode instanceof List<?> list) {
            // Standardowa sytuacja — sekcja jest listą
            projektyList = (List<Map<String, Object>>) list;

        } else if (projektyNode instanceof Map<?, ?> singleMap) {
            projektyList = new ArrayList<>();
            projektyList.add((Map<String, Object>) singleMap);
            root.put("projekty", projektyList);

        } else {
            throw new IllegalStateException(
                    "Nieobsługiwany typ pola 'projekty' w YAML: "
                            + projektyNode.getClass()
            );
        }

        // Dodanie nowego wpisu
        projektyList.add(newEntry);

        // Nadpisanie pliku jednym dokumentem YAML
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            yaml.dump(root, writer);
        }
    }

    /**
     * Odczytuje główny dokument YAML jako mapę root.
     *
     * <p>Jeżeli plik:
     * <ul>
     *     <li>nie istnieje</li>
     *     <li>jest pusty</li>
     *     <li>zawiera null</li>
     * </ul>
     * zwracana jest nowa, pusta mapa.
     *
     * @param yaml skonfigurowana instancja SnakeYAML
     * @return mapa reprezentująca root dokumentu YAML
     * @throws Exception gdy wystąpi błąd I/O
     * @throws IllegalStateException gdy root dokumentu nie jest mapą
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> readRoot(Yaml yaml) throws Exception {

        if (!Files.exists(configPath) || Files.size(configPath) == 0) {
            return new LinkedHashMap<>();
        }

        try (BufferedReader reader = Files.newBufferedReader(configPath)) {

            Object loaded = yaml.load(reader);

            if (loaded == null) {
                return new LinkedHashMap<>();
            }

            if (loaded instanceof Map<?, ?> map) {
                return (Map<String, Object>) map;
            }

            throw new IllegalStateException(
                    "Główny dokument YAML musi być mapą (root), a jest: "
                            + loaded.getClass()
            );
        }
    }
}
