package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Student {
    private String studentName;
    private StudentID studentID;
    private ArrayList<Course> addedCourses;
    private StudentTranscript studentTranscript;
    private Advisor studentAdvisor;
    private Course[][] weeklySchedule = new Course[8][5];
    private ArrayList<String> actions;
    /*
    IN THIS WEEKLY SCHEDULE;
    Columns indicates
    0->09:30-10:20
    1->10:30-11:20
    2->11:30-12:20
    3->13:00-13:50
    4->14:00-14:50
    5->15:00-15:50
    6->16:00-16:50
    7->17:00-17:50

    Rows indicates:
    0->Monday
    1->Tuesday
    2->Wednesday
    ...
    4->Friday
    */
    private Integer studentSemester;

    public Student(String studentName, String studentID, Integer studentSemester,
                   Advisor studentAdvisor) {
        this.studentName = studentName;
        this.studentAdvisor = studentAdvisor;
        this.studentID = new StudentID(studentID);
        this.studentSemester = studentSemester;
        this.addedCourses = new ArrayList<Course>();
        this.studentTranscript = new StudentTranscript(this);
        this.actions = new ArrayList<String>();
    }

    public StudentTranscript getStudentTranscript() {
        return studentTranscript;
    }

    public void printStudentTranscript(){
        studentTranscript.printStudentTranscript();
    }

    public String getCourseGrade(Course course){
        return studentTranscript.getCourseGrade(course);
    }

    public void addCourse(Course course){
        addedCourses.add(course);
        Boolean[][] lectureHours = course.getLectureHours();
        for(int i=0;i<8;i++){
            for(int j=0;j<5;j++){
                if(lectureHours[i][j]!=false){
                    this.weeklySchedule[i][j] = course;
                }
            }
        }
        course.addStudent();
    }

    public void passCourse(Course course, String letterGrade){
        studentTranscript.passCourse(course, letterGrade);
    }

    public String checkCourseOverlap(Course course){
        Boolean[][] lectureHours = course.getLectureHours();
        Set<String> overlaps = new HashSet<String>();
        for(int i=0;i<8;i++){
            for(int j=0;j<5;j++){
                if(lectureHours[i][j]!=false){
                    if(weeklySchedule[i][j]!=null){
                        overlaps.add(weeklySchedule[i][j].getCourseID());
                    }
                }
            }
        }

        if(overlaps.size()>1){
            /*
            The course causes multiple hours of overlap which is a huge issue...
            */
            StringBuffer answer = new StringBuffer("[VERIFICATION-FAILED]:The system didnt allow " + studentID +
                    " to add " + course.getCourseID() + " because of " +
                    String.format("%d",overlaps.size()) + " hours of collision with ");

            int i=0;
            for(String overlap:overlaps){
                answer.append(overlap);
                i++;
                if(i!=overlaps.size()){
                    answer.append(",");
                }
            }
            return answer.toString();
        }
        return "success";
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentID() {
        return studentID.toString();
    }

    public Course[][] getWeeklySchedule() {
        return weeklySchedule;
    }

    public Advisor getStudentAdvisor() {
        return studentAdvisor;
    }

    public ArrayList<Course> getAddedCourses() {
        return addedCourses;
    }

    public ArrayList<Course> getFailedCourses(){
        ArrayList<Course> failedCourses = new ArrayList<Course>();
        ArrayList<Grade> grades = studentTranscript.getLetterGrades();

        for(Grade studentGrade:grades){
            if(studentGrade.getLetterGrade().equals("FF")||studentGrade.getLetterGrade().equals("FD")){
                //He failed from that class. Add it to the failedCourses list.
                failedCourses.add(studentGrade.getCourse());
            }
        }
        return failedCourses;
    }

    public Integer getStudentSemester() {
        return studentSemester;
    }

    public Double getCGPA(){
        return studentTranscript.getCGPA();
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    public void addAction(String action){
        actions.add(action);
    }

    public Integer completedCredits(){
        return studentTranscript.completedCredits();
    }
}
