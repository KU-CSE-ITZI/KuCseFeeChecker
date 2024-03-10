package org.example.service;

import java.util.Scanner;
import org.example.entity.StudentInfo;
import org.example.repository.StudentFeeRepository;

public class ScannerModeService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n스캐너 모드로 전환합니다.");
        System.out.println("메뉴로 돌아가기 -> q");

        while (true) {
            System.out.print("바코드를 스캔하세요: ");
            String id = scanner.nextLine().trim();

            System.out.print("\033[H\033[2J");
            System.out.flush();

            if (id.equals("q")) {
                System.out.println("메뉴로 돌아갑니다.");
                break;
            }

            StudentInfo student = StudentFeeRepository.getInstance().getStudentInfo(id);

            if (student == null) {
                System.out.println(id + " - 해당 학생의 정보가 없습니다.");
                continue;
            }

            if (student.isPayedStudentFee) {
                System.out.println(student.name + "(" + student.id + ")" + "님은 학생회비 납부자입니다.");
            } else {
                System.out.println(student.name + "(" + student.id + ")" + "님은 학생회비 미납부자입니다.");
            }
        }
    }

}
