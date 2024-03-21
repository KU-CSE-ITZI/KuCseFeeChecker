package org.example.service.scanner;

import java.util.Scanner;
import org.example.enums.Mode;

public class ScannerModeService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n스캐너 모드로 전환합니다.");
        Mode mode;

        while (true) {
            printModeSelect();
            mode = getSelectedMode();

            if (mode == null) {
                continue;
            }

            if (mode == Mode.EXIT) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            startSelectedMode(mode);
        }
    }

    private void startSelectedMode(Mode mode) {
        switch (mode) {
            case FEE_SCANNER:
                startFeeScannerMode();
                break;
            case FILE_SCANNER:
                startFileScannerMode();
                break;
        }
    }

    private void startFileScannerMode() {
        FileScannerModeService fileScannerModeService = new FileScannerModeService();
        fileScannerModeService.start();
    }

    private void startFeeScannerMode() {
        FeeScannerModeService feeScannerModeService = new FeeScannerModeService();
        feeScannerModeService.start();
    }

    private Mode getSelectedMode() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    return Mode.FEE_SCANNER;
                case 2:
                    return Mode.FILE_SCANNER;
                case 3:
                    return Mode.EXIT;
                default:
                    System.out.println("잘못된 입력입니다.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다.");
            return null;
        }
    }

    private void printModeSelect() {
        System.out.println("메뉴를 선택해주세요.");
        System.out.println("1. 학생회비 납부 확인");
        System.out.println("2. 폼 신청 여부 확인");
        System.out.println("3. 종료");
        System.out.print(">> ");
    }

}
