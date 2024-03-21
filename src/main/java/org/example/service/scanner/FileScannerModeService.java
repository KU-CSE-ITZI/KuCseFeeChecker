package org.example.service.scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.repository.FileStudentRepository;

public class FileScannerModeService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("폼 신청 여부 스캐너 모드로 전환합니다.");
        initData();
        while (true) {
            System.out.print("바코드를 스캔하세요(종료하려면 q를 입력하세요): ");
            var id = scanner.nextLine().trim();

            System.out.print("\033[H\033[2J");
            System.out.flush();

            if (id.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                break;
            }

            var student = FileStudentRepository.getInstance().getStudentInfo(id);

            if (student == null) {
                System.out.println(id + " - 해당 학생의 정보가 없습니다.");
                continue;
            }

            if (student.isPayedStudentFee) {
                System.out.println(student.name + "(" + student.id + ")" + "님은 폼 신청자입니다.");
            } else {
                System.out.println(student.name + "(" + student.id + ")" + "님은 폼 신청자가 아닙니다.");
            }
        }
    }

    private void initData() {
        try {
            var fileName = getFileName();
            var workbook = getWorkbook(fileName);

            var sheet = workbook.getSheetAt(0);
            printHeader(sheet);
            var studentIdHeaderIndex = getStudentIdHeaderIndex();
            var studentNameHeaderIndex = getStudentNameHeaderIndex();

            FileStudentRepository.getInstance().initStudentInfo(
                    fileName,
                    studentIdHeaderIndex,
                    studentNameHeaderIndex
            );
        } catch (FileNotFoundException e) {
            System.out.println("입력한 파일명의 파일을 찾을 수 없습니다. 확장자까지 입력해주세요.");
        } catch (IOException e) {
            System.out.println("파일을 여는 도중 문제가 생겼습니다.");
        }
    }

    private void printHeader(Sheet sheet) {
        var headerRow = sheet.getRow(0);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            var index = i + 1;
            System.out.println(index + ". " + headerRow.getCell(i));
        }
    }

    private Workbook getWorkbook(String fileName) throws IOException {
        var fileInputStream = new FileInputStream(fileName);
        return fileName.endsWith("xlsx")
                ? new XSSFWorkbook(fileInputStream)
                : new HSSFWorkbook(fileInputStream);
    }

    private String getFileName() {
        System.out.println("납부 확인을 진행할 파일명을 입력해주세요. 확장자까지 입력해주세요. ex) test.xls");
        return scanner.nextLine();
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

    private int getStudentNameHeaderIndex() {
        System.out.println("학생 이름으로 사용할 컬럼의 숫자를 입력해주세요.");
        return Integer.parseInt(scanner.nextLine()) - 1;
    }

}
