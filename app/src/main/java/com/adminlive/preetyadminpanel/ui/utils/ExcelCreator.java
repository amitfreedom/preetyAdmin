package com.adminlive.preetyadminpanel.ui.utils;

import android.os.Environment;
import android.util.Log;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelCreator {

    private static final String TAG = "ExcelCreator";

//    public static void createExcelSheet(List<Employee> employees) {
//        // Create a new workbook
//        Workbook workbook = new XSSFWorkbook();
//
//        // Create a new sheet
//        Sheet sheet = workbook.createSheet("Employee Data");
//
//        // Create header row
//        Row headerRow = sheet.createRow(0);
//        List<String> headers = Arrays.asList("S.No", "UID", "Name", "Email", "Phone", "Salary", "JoiningDate");
//        for (int i = 0; i < headers.size(); i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers.get(i));
//        }
//
//        // Write data rows
//        for (int i = 0; i < employees.size(); i++) {
//            Employee employee = employees.get(i);
//            Row row = sheet.createRow(i + 1);
//            row.createCell(0).setCellValue(i + 1); // S.No (auto increment)
//            row.createCell(1).setCellValue(employee.getUid());
//            row.createCell(2).setCellValue(employee.getName());
//            row.createCell(3).setCellValue(employee.getEmail());
//            row.createCell(4).setCellValue(employee.getPhone());
//            row.createCell(5).setCellValue(employee.getSalary());
//            row.createCell(6).setCellValue(employee.getJoiningDate());
//        }
//
//        // Write the workbook to a file
//        try {
//            FileOutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/employee_data.xlsx");
//            workbook.write(outputStream);
//            outputStream.close();
//            Log.i(TAG, "Excel file created successfully.");
//        } catch (IOException e) {
//            Log.e(TAG, "Error creating Excel file: " + e.getMessage());
//        }
//    }
}

