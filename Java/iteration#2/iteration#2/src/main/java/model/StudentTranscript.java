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
}
