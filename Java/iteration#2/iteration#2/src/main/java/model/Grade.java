package model;

import java.util.logging.Logger;

public class Grade {
    private String letterGrade;
    private Double courseGPA;
    private Course course;

    public Grade(Course course, String letterGrade){
        /*
        These transformations are from:
        https://www.marmara.edu.tr/dosya/www/mevzuat/2021/mu_yonerge_basari_degerlendirme_2020_v204.02.2021.pdf?_t=1612473513
        */
        if(letterGrade.equals("AA")){this.courseGPA = 4.0;}
        else if(letterGrade.equals("BA")){this.courseGPA = 3.5;}
        else if(letterGrade.equals("BB")){this.courseGPA = 3.0;}
        else if(letterGrade.equals("CB")){this.courseGPA = 2.5;}
        else if(letterGrade.equals("CC")){this.courseGPA = 2.0;}
        else if(letterGrade.equals("DC")){this.courseGPA = 1.5;}
        else if(letterGrade.equals("DD")){this.courseGPA = 1.0;}
        else if(letterGrade.equals("FD")){this.courseGPA = 0.5;}
        else{this.courseGPA = 0.0;}

        this.letterGrade = letterGrade;
        this.course = course;
    }

    public Integer getCourseCredit(){
        return course.getCourseCredit();
    }

    public Double getCourseGPA(){
        return courseGPA;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public Course getCourse() {
        return course;
    }
}
