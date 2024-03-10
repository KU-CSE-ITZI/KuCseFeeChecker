package org.example;

import org.example.repository.StudentFeeRepository;
import org.example.service.StudentFeeService;

public class Main {

    public static void main(String[] args) {
        StudentFeeRepository.getInstance().initStudentInfo();

        var studentFeeService = new StudentFeeService();
        studentFeeService.start();
    }
}