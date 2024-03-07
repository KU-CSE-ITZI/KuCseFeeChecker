package org.example.repository;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.example.entity.StudentInfo;

public class StudentFeeRepository {

    // key: 학번(id), value: 학생 정보 객체
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

    // todo implement this method
    // 레포에 들어있는 csv 파일을 읽어와서 StudentInfo 객체를 만들고 HashMap에 저장하는 메소드
    // 필요할 경우 private method를 추가하여 구현
    public void initStudentInfo() {
        try {
            // 엑셀 파일 읽기
            FileInputStream fis = new FileInputStream("23.xlsx");
            Workbook workbook = WorkbookFactory.create(fis);
            // 첫번째 시트(하나밖에 없으므로)
            Sheet sheet = workbook.getSheetAt(0);
            // 각 행(학생 정보)
            for (Row row : sheet) {
                // 첫번째 셀과 마지막 셀 건너뜀
                if (row.getRowNum() == 0 || row.getRowNum() == sheet.getLastRowNum()-1 || row.getRowNum() == sheet.getLastRowNum()) {
                    continue;
                }
                else {
                    // 각 셀의 값(학번, 이름, 학생회비 납부) 가져오기
                    String studentId = row.getCell(1).toString();
                    String studentName = row.getCell(2).toString();
                    String studentIsPayedStudentFee = row.getCell(4).toString();
                    boolean isPayed = false;
                    if (studentIsPayedStudentFee.equals("o")) {
                        isPayed = true;
                    }
                    StudentInfo studentInfo = new StudentInfo(studentName, studentId, isPayed);
                    studentInfoMap.put(studentId, studentInfo);
                }
                // 한 행의 셀들 출력
                // for (Cell cell : row) {
                //     System.out.print(cell.toString() + "\t");
                // }
                // System.out.print(isPayed);
                // System.out.println();
            }
            fis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // todo implement this method
    // id에 해당하는 StudentInfo 객체를 반환하는 메소드
    public StudentInfo getStudentInfo(String id) {
        return studentInfoMap.getOrDefault(id, null);
    }

    // todo implement this method
    // id 배열에 해당하는 StudentInfo 객체를 반환하는 메소드
    public List<StudentInfo> getStudentInfos(List<String> ids) {
        return null;
    }
}
