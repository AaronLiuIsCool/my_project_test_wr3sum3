package com.kuaidaoresume.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

    public static Collection<String> getKeywordsFromExcel(String filePath) {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (XSSFWorkbook keywordsWorkBook = new XSSFWorkbook(resource.getInputStream())) {
            XSSFSheet keywordsSheet = keywordsWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = keywordsSheet.iterator();
            Set<String> keywordsSet = new HashSet<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    keywordsSet.add(cell.getStringCellValue());
                }
            }
            return keywordsSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
