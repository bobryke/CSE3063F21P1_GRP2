package model;

import java.util.ArrayList;

public class StudentTranscript {
    private Student student;
    private ArrayList<Grade> letterGrades;

    public StudentTranscript(Student student){
        this.student = student;
        letterGrades = new ArrayList<Grade>();
    }

    public void passCourse(Course course, String letterGrade){
        letterGrades.add(new Grade(course, letterGrade));
    }

    public Integer completedCredits(){
        Integer answer = 0;
        for(Grade grade:letterGrades){
            if(grade.getLetterGrade().equals("FF")||grade.getLetterGrade().equals("FD")){continue;}
            //FF and FD aren't counted as completed.
            answer += grade.getCourseCredit();
        }
        return answer;
    }

    public Double getCGPA(){
        Integer sumOfCredits = 0;
        Double answer = 0.0;
        for(Grade grade:letterGrades) {
            sumOfCredits += grade.getCourseCredit();
            answer = answer + ((double)grade.getCourseCredit()) * grade.getCourseGPA();
        }

        if(sumOfCredits==0){return 0.0;}
        //This student is a new university student and hasn't taken any course yet. So her CGPA is assumed to be zero.


        answer /= sumOfCredits;
        return answer;
    }

    public String getCourseGrade(Course course){
        for(Grade grade:letterGrades){
            if(grade.getCourse().getCourseID().equals(course.getCourseID())){
                return grade.getLetterGrade();
            }
        }
        return null;
    }

    public ArrayList<Grade> getLetterGrades(){
        return letterGrades;
    }

    public void printStudentInfoLine(String line){
        for(int i=0;i<59;i++){
            if(i<line.length()){
                System.out.print(line.charAt(i));
            }
            else
                System.out.print(" ");
        }
        System.out.println("|");
    }
    public void printCourseInfoLine(String code,String courseName,String credit,String grade){
        String first = "| " + code;
        for(int i=0;i<10;i++){
            if(i<first.length()){
                System.out.print(first.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        String second = "| " + courseName;
        for(int i=0;i<26;i++){
            if(i<second.length()){
                System.out.print(second.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        String third = "| " + credit;
        for(int i=0;i<11;i++){
            if(i<third.length()){
                System.out.print(third.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        String fourth = "| " + grade;
        for(int i=0;i<12;i++){
            if(i<fourth.length()){
                System.out.print(fourth.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        System.out.println("|");
    }
    public void printStudentTranscript(){
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|                     TRANSCRIPT                           |");
        printStudentInfoLine("|  Student Name: "+student.getStudentName());
        printStudentInfoLine("|  Student ID: "+student.getStudentID());
        printStudentInfoLine("|  Student CGPA: "+student.getCGPA());
        printStudentInfoLine("|  Student Semester: "+student.getStudentSemester());
        System.out.println("____________________________________________________________");
        System.out.println("| Code    |   Course Name           | Credit   | Grade     |");
        System.out.println("____________________________________________________________");
        for(int i=0;i<letterGrades.size();i++){
            Course course = letterGrades.get(i).getCourse();
            printCourseInfoLine(course.getCourseID(),course.getCourseFullName(),
                    course.getCourseCredit().toString(),letterGrades.get(i).getLetterGrade());
        }
    }
}
