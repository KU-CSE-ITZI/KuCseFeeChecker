package org.example.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.StudentInfo;

public class StudentFeeRepository {

    private static final HashMap<String, StudentInfo> studentInfoMap = new HashMap<>();
    private static StudentFeeRepository instance;

    private StudentFeeRepository() {
    }

    public static StudentFeeRepository getInstance() {
        if (instance == null) {
            instance = new StudentFeeRepository();
        }

        return instance;
    }

    public void initStudentInfo() {
        try {
            String fileName = "학생회비 납부 명단.xlsx";
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
                var row = sheet.getRow(i);

                var studendIdCell = row.getCell(0);
                studendIdCell.setCellType(CellType.STRING);
                var studentId = studendIdCell.getStringCellValue().trim();
                var studentName = row.getCell(1).getStringCellValue().trim();
                var studentIsPayedStudentFee = row.getCell(2).getStringCellValue().trim();
                var isPayed = studentIsPayedStudentFee.equals("o") ||
                        studentIsPayedStudentFee.equals("O");
                var studentInfo = new StudentInfo(studentName, studentId, isPayed);
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
