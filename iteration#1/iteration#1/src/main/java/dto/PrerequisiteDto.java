package dto;
import java.util.ArrayList;

public class PrerequisiteDto {
    private String courseCode;
    private String courseName;
    private ArrayList<String> prerequisite;

    public PrerequisiteDto(String courseCode, String courseName, ArrayList<String> prerequisite) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.prerequisite = prerequisite;
    }

    @Override
    public String toString(){
        return courseName + "," + courseName + ":" + prerequisite.toString();
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

    public ArrayList<String> getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(ArrayList<String> prerequisite) {
        this.prerequisite = prerequisite;
    }
}

