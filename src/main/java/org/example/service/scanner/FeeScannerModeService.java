package org.example.service.scanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Scanner;
import org.example.entity.StudentInfo;
import org.example.repository.StudentFeeRepository;

public class FeeScannerModeService {

    HashMap<String, LocalDateTime> payedStudentMap = new HashMap<>();

    Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("학생회비 납부 여부 스캐너 모드로 전환합니다.");
        while (true) {
            System.out.print("바코드를 스캔하세요(종료하려면 q를 입력하세요): ");
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

            if (payedStudentMap.containsKey(student.id)) {
                System.out.println(student.name + "(" + student.id + ")" + "님은 이미 납부 확인되셨습니다.");
                continue;
            }

            if (student.isPayedStudentFee) {
                System.out.println(student.name + "(" + student.id + ")" + "님은 학생회비 납부자입니다.");
                payedStudentMap.put(student.id, LocalDateTime.now());
            } else {
                System.out.println(student.name + "(" + student.id + ")" + "님은 학생회비 미납부자입니다.");
            }
        }

        System.out.print("학생회비 납부자 목록 저장을 위한 파일 명을 입력해주세요. (.csv같은 확장자를 포함하지 말고 적어주세요): ");
        var fileName = scanner.nextLine().trim();
        var file = new File(fileName + ".csv");

        try {
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bw.write("학번,이름,납부일자");
            bw.newLine();

            for (var key : payedStudentMap.keySet()) {
                var user = StudentFeeRepository.getInstance().getStudentInfo(key);
                bw.write(user.id + "," + user.name + "," + payedStudentMap.get(key));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
