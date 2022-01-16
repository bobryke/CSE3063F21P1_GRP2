package dto;

import model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDto {
    private String semester;
    private String courseCode;
    private String courseType;
    private Integer credit;
    private String courseName;
    private Integer quota;

    public CourseDto(String semester, String courseCode, String courseType,
                     Integer credit, String courseName, Integer quota) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.courseType = courseType;
        this.credit = credit;
        this.courseName = courseName;
        this.quota = quota;
    }

    public Course convertToRealObject(List<PrerequisiteDto> prerequisites, String randomInstructor){
        PrerequisiteDto prerequisiteDto = null;
        for(PrerequisiteDto prerequisite:prerequisites){
            if(prerequisite.getCourseCode().equals(this.courseCode)){
                prerequisiteDto =  prerequisite;
                break;
            }
        }

        ArrayList<String> newprerequisites = new ArrayList<String>();
        if(prerequisiteDto!=null){
            for(String prerequisite:prerequisiteDto.getPrerequisite()){
                newprerequisites.add(prerequisite);
            }
        }
        Integer semesterVal = (semester.equals("null"))? 0 : Integer.valueOf(semester);
        Course realCourse = new Course(courseCode,courseName,
                courseType,credit,semesterVal,newprerequisites,
                this.quota,randomInstructor);

        return realCourse;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
