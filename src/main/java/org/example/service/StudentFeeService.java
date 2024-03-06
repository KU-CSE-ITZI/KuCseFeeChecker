package org.example.service;

import java.util.Scanner;

public class StudentFeeService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        Mode mode;
        System.out.println("++++++++++ITZI++++++++++");
        System.out.println("학생회비 납부 확인 프로그램입니다");
        while (true) {
            printModeSelect();
            mode = getSelectedMode();

            if (mode == null) {
                continue;
            }

            if (mode == Mode.EXIT) {
                end();
                break;
            }

            startSelectedMode(mode);

        }
        scanner.close();
    }

    private void printModeSelect() {
        System.out.println("메뉴를 선택해주세요.");
        System.out.println("1. 스캐너 모드");
        System.out.println("2. 파일 모드");
        System.out.println("3. 종료");
        System.out.print(">> ");
    }

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

    private void startScannerMode() {
        var service = new ScannerModeService();
        service.start();
    }

    private void startFileMode() {
        var service = new FileModeService();
        service.start();
    }

    private void end() {
        System.out.println("프로그램을 종료합니다");
    }
}
