package entity;

public class StudentInfo {

    public String name;
    public String id;
    public boolean isPayedStudentFee;

    public StudentInfo(String name, String id, boolean isPayedStudentFee) {
        this.name = name;
        this.id = id;
        this.isPayedStudentFee = isPayedStudentFee;
    }
}
