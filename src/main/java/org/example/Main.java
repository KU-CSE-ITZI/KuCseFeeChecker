package org.example;

import org.example.service.StudentFeeService;

public class Main {
    public static void main(String[] args) {
        var studentFeeService = new StudentFeeService();
        studentFeeService.start();
    }
}