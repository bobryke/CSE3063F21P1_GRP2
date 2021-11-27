package model;

public class StudentID {
    private String departmentCode;
    private String yearCode;
    private String postfixCode;

    public StudentID(String studentID){
        this.departmentCode = studentID.substring(0,3);
        this.yearCode = studentID.substring(3,6);
        this.postfixCode = studentID.substring(6);
    }

    @Override
    public String toString(){
        return departmentCode+yearCode+postfixCode;
    }

    @Override
    public boolean equals(Object rhs){
        return this.toString().equals(rhs.toString());
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getYearCode() {
        return yearCode;
    }

    public String getPostfixCode() {
        return postfixCode;
    }
}
