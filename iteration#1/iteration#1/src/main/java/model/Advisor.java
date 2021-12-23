package model;

import java.util.ArrayList;
import java.util.Random;

public class Advisor {
    private String advisorName;

    public Advisor(String advisorName){
        this.advisorName = advisorName;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public Boolean checkCreditRegulation(Student student, ArrayList<Course> courses){
        Integer sumCredits = 0;
        for(Course course:courses){
            sumCredits += course.getCourseCredit();
        }
        if(sumCredits > 35){
            return false;
        }
        else{
            return true;
        }
    }

    public String verifyStudentRegistration(Student student, ArrayList<Course> courses){
        //Check all the courses one by one if Advisor will approve them.
        ArrayList<String> problemsOccurred = new ArrayList<String>();
        if(checkCreditRegulation(student,courses)==false){
            StringBuffer answer = new StringBuffer("[VERIFICATION-FAILED]:The advisor " + advisorName +
                    " didnt approve" + " the registration of the student " +
                    student.getStudentID() +
                    " because the credits taken this semester shouldnt exceed 35.");
            student.addAction(answer.toString());
            return answer.toString();
        }
        /*
        According to the Marmara University regulations, a student can take classes DD or DC
        by the approval of the advisor.
        In our simulation, the advisor will not allow the student to take DD or DC courses
        in 1/3 probability. But, on the other hand, if the student has a CGPA smaller than 1.8
        then advisor will always allow student to take these DD and DC classes.
         */
        Boolean courseWithD = false;
        Integer coursesHigherSemesters = 0;
        for(int i=0;i<courses.size();i++){
            /*
            A student cannot take more than tree courses from higher semesters. We should
            count how many courses the student has taken from higher semesters.
             */
            if(courses.get(i).getCourseID().equals("CSE4197")){
                if(student.completedCredits()<165){
                    String reply = "[VERIFICATION-FAILED]:The advisor " + advisorName +
                            " didn't approve the registration of the student " + student.getStudentID() +
                            ". Because completed credits must be >=165 to take "+courses.get(i).getCourseID()+".";
                    problemsOccurred.add(reply);
                    student.addAction(reply);
                    continue;
                }
            }
            else if(courses.get(i).getCourseType().equals("Elective")&&courses.get(i).getCourseCredit()==5){
                //Students can't take TE course if completed credits < 155.
                if(student.completedCredits()<155){
                    String reply = "[VERIFICATION-FAILED]:The advisor " + advisorName +
                            " didnt approve the registration of the student " + student.getStudentID() +
                            ". Because completed credits must be >=155 to take"+courses.get(i).getCourseID()+".";
                    problemsOccurred.add(reply);
                    student.addAction(reply);
                    continue;
                }
            }
            String letterGrade = student.getCourseGrade(courses.get(i));
            if(letterGrade!=null&&(letterGrade.equals("DD")||letterGrade.equals("DC"))){
                courseWithD = true;
            }
            if(student.getStudentSemester() < courses.get(i).getCourseSemester()){
                if( ++coursesHigherSemesters > 3){
                    String reply = "[VERIFICATION-FAILED]:The advisor " + advisorName +
                            " didnt approve the registration of the student " + student.getStudentID() +
                            ". A student cannot take more than three classes from higher semesters.";
                    problemsOccurred.add(reply);
                    student.addAction(reply);
                    continue;
                }
            }
        }
        if(courseWithD){
            if(student.getCGPA()>1.8){
                Random rand = new Random();
                if(rand.nextInt()%3==0){
                    //In 1/3 probability we get into there...
                    String reply = "[VERIFICATION-FAILED]:The advisor " + advisorName +
                            " didnt approve the registration of the student" + student.getStudentID() +
                            ". Advisor didnt allow student to re-take DD or DC classes.";
                    problemsOccurred.add(reply);
                    student.addAction(reply);
                }
            }
        }
        if(problemsOccurred.size()!=0){
            Random rand = new Random();
            Integer index = rand.nextInt(problemsOccurred.size());
            return problemsOccurred.get(index);
            //Select one of the problems occurred and print on the UI.
            //This part of the code is not important for the BatchProcess option.
        }
        else
            return "The advisor " + advisorName + " approved the registration of the student " +student.getStudentID()+".";
    }
}
