package com.kuaidaoresume.resume.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUtil {

    public static Collection<String> loadLinesFromTextFile(String filePath) {

        ClassPathResource resource = new ClassPathResource(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
