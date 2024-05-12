package com.adminlive.preetyadminpanel.global;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtils {

    public interface ExcelCreationListener {
        void onExcelFileCreated(String zipFilePath);
    }

    public static void createHostExcelFile(Context context, String fileName, List<HostModal> hostList, ExcelCreationListener listener) {
        WritableWorkbook workbook = null;
        String zipFilePath = null;
        try {
            // Create a new workbook
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.adminlive.preetyadminpanel/" + "ExcelFiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            WorkbookSettings wbSettings = new WorkbookSettings();
            WritableFont titleFont = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD, true);

            WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
            titleFormat.setAlignment(Alignment.CENTRE);
            wbSettings.setLocale(new Locale("en", "EN"));

            File excelFile = new File(directory, fileName);
            workbook = Workbook.createWorkbook(excelFile,wbSettings);

            // Create a new sheet
            WritableSheet sheet = workbook.createSheet("Agency sheet", 0);

            // Write header row
            String[] headers = {"Real Name", "Phone Number", "Agency Code", "Email Address", "Doc Type", "ID Card Number", "ID Card Image", "Status", "User ID", "UID", "Photo", "Joining Date"};
            for (int i = 0; i < headers.length; i++) {
                Label label = new Label(i, 0, headers[i]);
                sheet.addCell(label);
            }

            // Write data rows
            for (int i = 0; i < hostList.size(); i++) {
                HostModal host = hostList.get(i);
                Label realName = new Label(0, i + 1, host.getRealName());
                Label phoneNumber = new Label(1, i + 1, host.getPhoneNumber());
                Label agencyCode = new Label(2, i + 1, host.getAgencyCode());
                Label emailAddress = new Label(3, i + 1, host.getEmailAddress());
                Label docType = new Label(4, i + 1, host.getDocType());
                Label idCardNumber = new Label(5, i + 1, host.getIdCardNumber());
                Label idCardImage = new Label(6, i + 1, host.getIdCardImage());
                Label status = new Label(7, i + 1, host.getStatus());
                Label userId = new Label(8, i + 1, host.getUserId());
                Label uid = new Label(9, i + 1, host.getUid());
                Label photo = new Label(10, i + 1, host.getPhoto());
                Label joiningDate = new Label(11, i + 1, host.getJoiningDate());

                sheet.addCell(realName);
                sheet.addCell(phoneNumber);
                sheet.addCell(agencyCode);
                sheet.addCell(emailAddress);
                sheet.addCell(docType);
                sheet.addCell(idCardNumber);
                sheet.addCell(idCardImage);
                sheet.addCell(status);
                sheet.addCell(userId);
                sheet.addCell(uid);
                sheet.addCell(photo);
                sheet.addCell(joiningDate);
            }

            // Write the workbook to the file
            workbook.write();
            workbook.close();


            // Generate a unique file name for the zip file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            // Create a zip file containing the Excel file
            File zipFile = new File(directory, "SalarySheet_" + timeStamp + ".zip");
            zipFilePath = zipFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            addToZipFile(excelFile, excelFile.getName(), zos);
            zos.close();
            fos.close();

            // Delete the Excel file
            excelFile.delete();

            // Notify the listener with the zip file path
            listener.onExcelFileCreated(zipFilePath);
        } catch (Exception e) {
            Log.e("ExcelUtils", "Error creating Excel file: " + e.getMessage());
            // Notify the listener with the zip file path
            listener.onExcelFileCreated(zipFilePath);
        }
    }
    public static String createExcelFile(Context context, String fileName, List<String[]> data) {
        WritableWorkbook workbook = null;
        String zipFilePath = null;
        try {
            // Create a new workbook
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.adminlive.preetyadminpanel/"+"ExcelFiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File excelFile = new File(directory, fileName);
            workbook = Workbook.createWorkbook(excelFile);

            // Create a new sheet
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            // Write data to the sheet
            for (int i = 0; i < data.size(); i++) {
                String[] rowData = data.get(i);
                for (int j = 0; j < rowData.length; j++) {
                    Label label = new Label(j, i, rowData[j]);
                    sheet.addCell(label);
                }
            }

            // Write the workbook to the file
            workbook.write();

            // Close the workbook
            workbook.close();

            // Generate a unique file name for the zip file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            // Create a zip file containing the Excel file
            File zipFile = new File(directory, "SalarySheet_" + timeStamp + ".zip");
            zipFilePath = zipFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            addToZipFile(excelFile, excelFile.getName(), zos);
            zos.close();
            fos.close();

            // Delete the Excel file
            excelFile.delete();

            return zipFilePath;
        } catch (Exception e) {
            Log.e("ExcelUtils", "Error creating Excel file: " + e.getMessage());
            return "Error creating Excel file: " + e.getMessage();
        }
    }

    private static void addToZipFile(File file, String fileName, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void shareZipFileViaEmail(Context context, String zipFilePath) {
        try {
            // Get the File object for the zip file
            File zipFile = new File(zipFilePath);

            // Create a content:// URI using FileProvider
            Uri zipUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", zipFile);

            // Create an intent to share via email
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/zip");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Salary Sheet");
            emailIntent.putExtra(Intent.EXTRA_STREAM, zipUri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(emailIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

