package org.example.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.StudentInfo;

public class FileStudentRepository {

    private static final HashMap<String, StudentInfo> studentInfoMap = new HashMap<>();
    private static FileStudentRepository instance;

    private FileStudentRepository() {
    }

    public static FileStudentRepository getInstance() {
        if (instance == null) {
            instance = new FileStudentRepository();
        }

        return instance;
    }

    public void initStudentInfo(
            String fileName,
            int studentIdHeaderIndex,
            int studentNameHeaderIndex
    ) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                var row = sheet.getRow(i);

                var studendIdCell = row.getCell(studentIdHeaderIndex);
                studendIdCell.setCellType(CellType.STRING);
                var studentId = studendIdCell.getStringCellValue().trim();
                var studentName = row.getCell(studentNameHeaderIndex).getStringCellValue().trim();
                var studentInfo = new StudentInfo(studentName, studentId, true);
                studentInfoMap.put(studentId, studentInfo);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StudentInfo getStudentInfo(String id) {
        return studentInfoMap.getOrDefault(id, null);
    }
}
