package dto;

import model.Advisor;
import model.Course;
import model.Student;
import model.StudentTranscript;

import java.util.ArrayList;

public class StudentDto {
    private String studentName;
    private String studentID;
    private Integer studentSemester;
    private String studentAdvisor;
    private Double gpa;
    private String[] dtoSchedule = new String[9];
    private StudentTranscriptDto studentTranscriptDto;

    public StudentDto(Student student){
        this.studentName = student.getStudentName();
        this.studentID = student.getStudentID();
        this.studentSemester = student.getStudentSemester();
        this.studentTranscriptDto = new StudentTranscriptDto(student.getStudentTranscript());
        this.studentAdvisor = student.getStudentAdvisor().getAdvisorName();
        this.gpa = student.getCGPA();

        //Include registered courses without any letter grade.
        ArrayList<Course>addedCourses = student.getAddedCourses();
        for(Course course:addedCourses){
            this.studentTranscriptDto.addLetterGrades(course.getCourseID()+": ");
        }

        //Create weekly schedule.
        createSchedule(student);
    }

    public void createSchedule(Student student){
        String[][] weeklySchedule = new String[9][6];
        weeklySchedule[0][0] = "               ";
        weeklySchedule[0][1] = " Monday        ";
        weeklySchedule[0][2] = " Tuesday       ";
        weeklySchedule[0][3] = " Wednesday     ";
        weeklySchedule[0][4] = " Thursday      ";
        weeklySchedule[0][5] = " Friday        ";
        weeklySchedule[1][0] = " 09:30 - 10:20 ";
        weeklySchedule[2][0] = " 10:30 - 11:20 ";
        weeklySchedule[3][0] = " 11:30 - 12:20 ";
        weeklySchedule[4][0] = " 13:00 - 13:50 ";
        weeklySchedule[5][0] = " 14:00 - 14:50 ";
        weeklySchedule[6][0] = " 15:00 - 15:50 ";
        weeklySchedule[7][0] = " 16:00 - 16:50 ";
        weeklySchedule[8][0] = " 17:00 - 17:50 ";
        Course[][] studentSchedule = student.getWeeklySchedule();

        for(int i=0;i<8;i++){
            for(int j=0;j<5;j++){
                if(studentSchedule[i][j]==null){
                    weeklySchedule[i+1][j+1] = String.format("%15s","");
                }
                else
                    weeklySchedule[i+1][j+1] = String.format("%15s",studentSchedule[i][j].getCourseID());
            }
        }

        for(int i=0;i<9;i++){
            String temp = new String();
            for(int j=0;j<6;j++){
                temp+=(weeklySchedule[i][j]+"|");
            }
            dtoSchedule[i] = temp;
        }
    }

    public StudentDto(String studentName, String studentID,
                      StudentTranscriptDto studentTranscriptDto, String studentAdvisor, Double gpa, ArrayList<String> actions) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.studentTranscriptDto = studentTranscriptDto;
        this.studentAdvisor = studentAdvisor;
        this.gpa = gpa;
    }

    @Override
    public String toString(){
        return studentName + " " + studentID + " Transcript:\n" + studentTranscriptDto.getLetterGrades().toString() + "\n"
                + "gpa:" + this.gpa;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public StudentTranscriptDto getStudentTranscriptDto() {
        return studentTranscriptDto;
    }

    public void setStudentTranscriptDto(StudentTranscriptDto studentTranscriptDto) {
        this.studentTranscriptDto = studentTranscriptDto;
    }

    public String getStudentAdvisor() {
        return studentAdvisor;
    }

    public void setStudentAdvisor(String studentAdvisor) {
        this.studentAdvisor = studentAdvisor;
    }
}
