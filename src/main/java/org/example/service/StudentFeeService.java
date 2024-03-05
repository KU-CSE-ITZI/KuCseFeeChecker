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
import org.example.entity.StudentInfo;
import org.example.repository.StudentFeeRepository;

public class StudentFeeService {

    Scanner scanner = new Scanner(System.in);

    // todo implement start method
    // 콘솔 화면에 처음 시작할 때 출력되는 메소드
    public void start() {
        Mode mode;
        System.out.println("++++++++++ITZI++++++++++");
        System.out.println("학생회비 납부 확인 프로그램입니다");
        while (true) {
            printModeSelect();
            mode = getSelectedMode();

            //메뉴를 올바른 형식으로 선택하지 않았을 경우
            if (mode == null) {
                continue;
            }

            //종료 메뉴를 선택한 경우
            if (mode == Mode.EXIT) {
                end();
                break;
            }

            startSelectedMode(mode);

        }
        scanner.close();
    }

    // todo implement printModeSelect method
    // 콘솔 화면에 메뉴를 출력하는 메소드
    // 1. 스캐너 모드, 2. 파일 모드, 3. 종료
    private void printModeSelect() {
        System.out.println("메뉴를 선택해주세요.");
        System.out.println("1. 스캐너 모드");
        System.out.println("2. 파일 모드");
        System.out.println("3. 종료");
        System.out.print(">> ");
    }

    //사용자가 선택한 모드를 시작하는 메소드
    private void startSelectedMode(Mode mode) {
        switch (mode) {
            case SCANNER:
                startScannerMode();
                break;
            case FILE:
                startFileMode();
                break;
        }
    }

    // todo implement getSelectedMode method
    // 사용자로부터 모드를 입력받는 메소드
    // enum을 리턴
    private Mode getSelectedMode() {

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    return Mode.SCANNER;
                case 2:
                    return Mode.FILE;
                case 3:
                    return Mode.EXIT;
                default:
                    System.out.println("1~3사이 숫자를 선택하세요!");
            }
        } catch (NumberFormatException E) {
            System.out.println("올바른 형식으로 입력하세요!");
        }
        return null;
    }

    // todo implement startScannerMode method
    // 스캐너 모드를 시작하는 메소드
    private void startScannerMode() {
        System.out.println("스캐너 모드로 전환합니다.");
        System.out.println("메뉴로 돌아가기 -> q");

        while (true) {
            String id;
            StudentInfo student;

            //////바코드 스캐너/////
            System.out.print("바코드를 스캔하세요: ");
            id = scanner.nextLine();
            /////////////////////

            if (id.trim().equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }
            student = StudentFeeRepository.getInstance().getStudentInfo(id);

            if (student == null) {
                System.out.println("해당 학생의 정보가 없습니다.");
                continue;
            }

            if (student.isPayedStudentFee) {
                System.out.println(student.name + "님은 학생회비 납부자입니다.");
            } else {
                System.out.println(student.name + "님은 학생회비 미납부자입니다.");
            }
        }

    }

    // todo implement startFileMode method
    // 파일 모드를 시작하는 메소드
    private void startFileMode() {
        System.out.println("파일 모드로 전환합니다.");

        try {
            var fileName = getFileName();
            var workbook = getWorkbook(fileName);
            checkFeeByWorkbook(workbook);
        } catch (FileNotFoundException e) {
            System.out.println("입력한 파일명의 파일을 찾을 수 없습니다. 확장자까지 입력해주세요.");
        } catch (IOException e) {
            System.out.println("파일을 여는 도중 문제가 생겼습니다.");
        }
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

    // todo implement end method
    // 프로그램을 종료하는 메소드
    private void end() {
        System.out.println("프로그램을 종료합니다");
    }
}
