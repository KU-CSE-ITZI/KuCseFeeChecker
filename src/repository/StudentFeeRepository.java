package repository;

import entity.StudentInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    }

    // todo implement this method
    // id에 해당하는 StudentInfo 객체를 반환하는 메소드
    public StudentInfo getStudentInfo(String id) {
        //학번이 존재하지 않을때 예외처리
        StudentInfo studentInfo = studentInfoMap.getOrDefault(id, null);
        if (studentInfo == null) {
            System.out.println("일치하는 학번이 존재하지 않습니다.");
        }
        return studentInfo;

    }

    // todo implement this method
    // id 배열에 해당하는 StudentInfo 객체를 반환하는 메소드
    public List<StudentInfo> getStudentInfos(List<String> ids) {
        return null;
    }
}
