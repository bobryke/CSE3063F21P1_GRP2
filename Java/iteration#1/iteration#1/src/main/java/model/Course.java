package model;

import java.util.ArrayList;
import java.util.Random;

class Course {
    private Integer registeredStudents;
    private Integer courseSemester;
    private CourseID courseID;
    private String courseFullName;
    private String courseType;
    private Integer courseCredit;
    private Boolean[][] lectureHours = new Boolean[8][5];
    private ArrayList<String> prerequisites;
    private Integer quota;

    public Course(String courseID, String courseFullName, String courseType, Integer courseCredit,
                  Integer courseSemester,ArrayList<String> prerequisites, Integer quota){
        registeredStudents = 0;
        this.courseSemester = courseSemester;
        this.prerequisites = prerequisites;
        this.courseID = new CourseID(courseID);
        this.courseFullName = courseFullName;
        this.courseType = courseType;
        this.courseCredit = courseCredit;
        this.quota = quota;
        for(int i=0;i<8;i++){
            for(int j=0;j<5;j++) {
                lectureHours[i][j] = false;
            }
        }
        //Now at this point, we will assign random lecture hours for this course.
        //We will do that according to the course credit.
        //The more credits the course has, the more the course hours.

        Integer courseHours = 0;
        if(courseCredit>5)
            courseHours = 5;
        else if(courseCredit>3)
            courseHours = 3;
        else
            courseHours = 2;

        Random rand = new Random();
        for(int i=0;i<courseHours;i++){
            int hour = rand.nextInt(8);
            int day = rand.nextInt(5);
            lectureHours[hour][day] = true;
            //Randomly assigning hours for this course.
            //Notice that since the whole process is random, even the courses given in the semester may overlap.
            //We will not solve this issue in iteration 1.
            //We may use some tricky algorithms to fix this issue.
        }
    }

    void addStudent(){
        registeredStudents++;
    }

    public Integer getRegisteredStudents() {
        return registeredStudents;
    }

    public String getCourseID() {
        return courseID.toString();
    }

    public String getCourseFullName() {
        return courseFullName;
    }

    public String getCourseType() {
        return courseType;
    }

    public Integer getCourseSemester() {
        return courseSemester;
    }

    public Integer getCourseCredit() {
        return courseCredit;
    }

    public Boolean[][] getLectureHours() {
        return lectureHours;
    }

    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    public Boolean isQuotaFull(){
        return registeredStudents==quota;
    }

    public Boolean checkIfPrerequisite(Course course){
        for(String prerequisite:prerequisites){
            if(prerequisite.equals(course.getCourseID())){
                return true;
            }
        }
        return false;
    }
}
