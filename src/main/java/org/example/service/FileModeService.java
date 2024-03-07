package org.example.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.repository.StudentFeeRepository;

public class FileModeService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n파일 모드로 전환합니다.");
        
        try {
            var fileName = getFileName();
            var workbook = getWorkbook(fileName);
            checkFeeByWorkbook(workbook);
        } catch (FileNotFoundException e) {
            System.out.println("입력한 파일명의 파일을 찾을 수 없습니다. 확장자까지 입력해주세요.");
        } catch (IOException e) {
            System.out.println("파일을 여는 도중 문제가 생겼습니다.");
        }

        scanner.close();
    }

    private String getFileName() {
        System.out.println("납부 확인을 진행할 파일명을 입력해주세요. 확장자까지 입력해주세요. ex) test.xls");
        return scanner.nextLine();
    }

    private Workbook getWorkbook(String fileName) throws IOException {
        var fileInputStream = new FileInputStream(fileName);
        return fileName.endsWith("xlsx")
                ? new XSSFWorkbook(fileInputStream)
                : new HSSFWorkbook(fileInputStream);
    }

    private void checkFeeByWorkbook(Workbook workbook) {
        var sheet = workbook.getSheetAt(0);
        printHeader(sheet);

        var studentIdHeaderIndex = getStudentIdHeaderIndex();
        var isPayedHeaderIndex = getIsPayedHeaderIndex();
        var isPayedCharacter = getIsPayedString();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            var row = sheet.getRow(i);

            var isPayed = row.getCell(isPayedHeaderIndex).getStringCellValue();
            if (!isPayed.equals(isPayedCharacter)) {
                continue;
            }

            var studentIdCell = row.getCell(studentIdHeaderIndex);
            studentIdCell.setCellType(CellType.STRING);
            var studentId = row.getCell(studentIdHeaderIndex).getStringCellValue();
            if (StudentFeeRepository.getInstance().getStudentInfo(studentId) == null) {
                System.out.println(studentId + " - 학생회비 납부 X");
            }
        }
    }

    private void printHeader(Sheet sheet) {
        var headerRow = sheet.getRow(0);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            var index = i + 1;
            System.out.println(index + ". " + headerRow.getCell(i));
        }
    }

    private int getStudentIdHeaderIndex() {
        System.out.println("학번으로 사용할 컬럼의 숫자를 입력해주세요.");
        return Integer.parseInt(scanner.nextLine()) - 1;
    }

    private int getIsPayedHeaderIndex() {
        System.out.println("힉생회비 납부여부로 사용할 컬럼의 숫자를 입력해주세요.");
        return Integer.parseInt(scanner.nextLine()) - 1;
    }

    private String getIsPayedString() {
        System.out.println("힉생회비 납부여부로 판단할 문자열을 넣어주세요. ex) O, 납부함 등등");
        return scanner.nextLine().trim();
    }

}
