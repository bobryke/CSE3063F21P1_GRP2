package dto;

import model.Grade;
import model.Student;
import model.StudentTranscript;

import java.util.ArrayList;

public class StudentTranscriptDto {
    private ArrayList<String> letterGrades;

    public StudentTranscriptDto(StudentTranscript studentTranscript){
        letterGrades = new ArrayList<String>();
        ArrayList<Grade> allGrades = studentTranscript.getLetterGrades();
        for(Grade grade:allGrades){
            letterGrades.add(grade.getCourse().getCourseID()+":"+grade.getLetterGrade());
        }
    }

    public StudentTranscriptDto(ArrayList<String> letterGrades) {
        this.letterGrades = letterGrades;
    }

    public ArrayList<String> getLetterGrades() {
        return letterGrades;
    }

    public void setLetterGrades(ArrayList<String> letterGrades) {
        this.letterGrades = letterGrades;
    }

    public void addLetterGrades(String letterGrade){
        letterGrades.add(letterGrade);
    }
}