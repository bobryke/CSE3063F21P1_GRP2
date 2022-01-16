package table;

import model.Grade;
import model.StudentTranscript;

import java.util.ArrayList;

public class TranscriptTable {
    private String courseCode;
    private String courseName;
    private Integer credit;
    private String finalGrade;

    public TranscriptTable(String courseCode, String courseName, Integer credit, String finalGrade) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.finalGrade = finalGrade;
    }

    public TranscriptTable(){
        courseCode="";
        courseName="";
        credit=0;
        finalGrade="";
    }

    public static ArrayList<TranscriptTable> convertToTable(StudentTranscript transcript){
        ArrayList<TranscriptTable> table = new ArrayList<TranscriptTable>();
        ArrayList<Grade> grades = transcript.getLetterGrades();
        for(int i=0;i<grades.size();i++){
            table.add(new TranscriptTable(grades.get(i).getCourse().getCourseID(),
                    grades.get(i).getCourse().getCourseFullName(),
                    grades.get(i).getCourse().getCourseCredit(),
                    grades.get(i).getLetterGrade()));
        }
        return table;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }
}
