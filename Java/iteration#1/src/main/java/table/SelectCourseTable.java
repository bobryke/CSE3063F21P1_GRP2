package table;

import javafx.scene.control.CheckBox;
import model.Course;

import java.util.ArrayList;

public class SelectCourseTable {
    private String courseCode;
    private String courseName;
    private Integer credit;
    private String instructor;
    private CheckBox selection;

    public SelectCourseTable(String courseCode, String courseName,
                             Integer credit, String instructor, CheckBox selection) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.instructor = instructor;
        this.selection = selection;
    }

    public SelectCourseTable() {
        this("","",0,"",new CheckBox());
    }

    public static ArrayList<SelectCourseTable> convertToTable(ArrayList<Course> availableCourses){
        ArrayList<SelectCourseTable> selectCourseTable = new ArrayList<SelectCourseTable>();
        for(Course course:availableCourses){
            selectCourseTable.add(new SelectCourseTable(course.getCourseID(),
                    course.getCourseFullName(),
                    course.getCourseCredit(),
                    course.getInstructor(),
                    new CheckBox()
                    ));
        }
        return selectCourseTable;
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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public CheckBox getSelection() {
        return selection;
    }

    public void setSelection(CheckBox selection) {
        this.selection = selection;
    }
}
