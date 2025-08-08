package com.telerik.testdatareaders;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private String filePath;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getFieldNamesFromExcel(String sheetName) throws IOException {
        List<String> fieldNames = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int row = 1; row < rows; row++) {
                Row currentRow = sheet.getRow(row);

                if (currentRow == null) continue;

                Cell fieldNameCell = currentRow.getCell(0);
                Cell flagCell = currentRow.getCell(1);

                if (fieldNameCell != null && flagCell != null) {
                    String flagValue = flagCell.getStringCellValue().trim();
                    if ("Y".equalsIgnoreCase(flagValue)) {
                        fieldNames.add(fieldNameCell.getStringCellValue().trim());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("IOException occurred while reading the Excel file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        return fieldNames;
    }
}
