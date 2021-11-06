public class Course {
    String courseName;
    String courseID;
    String courseType;
    int courseCredit;
    Course[] prerequisitesCourses;
    boolean[][] lectureHours;
    Student[] registeredStudents; 

    public Course(String courseName, String courseID, String courseType, int courseCredit) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.courseType = courseType;
        this.courseCredit = courseCredit;
    }

    public void addStudent(){
    }
    private boolean checkIfPrerequisite(Course course){}

}
