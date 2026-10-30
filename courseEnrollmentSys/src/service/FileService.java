package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<String>();

        try {
            Path path = Paths.get(filePath);
            ensureFileExists(path);
            lines = Files.readAllLines(path);
        } catch (IOException ex) {
            System.out.println("Could not read file: " + filePath);
        }

        return lines;
    }

    public void writeLines(String filePath, List<String> lines) {
        BufferedWriter writer = null;

        try {
            Path path = Paths.get(filePath);
            ensureFileExists(path);
            writer = new BufferedWriter(new FileWriter(path.toFile(), false));

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Could not write file: " + filePath);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("Could not close file writer for: " + filePath);
                }
            }
        }
    }

    private void ensureFileExists(Path path) throws IOException {
        File parent = path.toFile().getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!path.toFile().exists()) {
            Files.createFile(path);
        }
    }
}
