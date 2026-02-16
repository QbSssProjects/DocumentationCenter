package qbsss.docsCenter.docsCenterGradleGenerated.service;


import org.springframework.stereotype.Service;
import qbsss.docsCenter.docsCenterGradleGenerated.exceptions.FileIOException;


import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Service
public class ListOfProjectsService {

    static File file = new File("projekty.yaml");
    static List<String> listOfProjects = new java.util.ArrayList<String>();
    public static List<String> getListOfProjects() {
        System.out.println(System.getProperty("user.dir"));

        try (Scanner myReader = new Scanner(file)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                listOfProjects.add(data);
            }


            return listOfProjects;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            try {
                File file = new File(System.getProperty("user.dir"), "projekty.yaml");

                file.createNewFile();
            } catch (IOException ex) {
                throw new FileIOException("File creation failed: projekty.yaml");
            }

            throw new FileIOException("file not found: projekty.yaml");
        }
    }


}
