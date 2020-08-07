package com.kuaidaoresume.resume.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUtil {

    public static Collection<String> loadLinesFromTextFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(Thread.currentThread().getContextClassLoader()
            .getResource(filePath).getFile())))) {

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
