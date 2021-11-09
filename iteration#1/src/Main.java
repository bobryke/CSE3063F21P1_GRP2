import model.Student;
import system.RegistrationSystem;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*
        Get the current semester from the user.
         */
        String semester = "fist check";
        String message = "WELCOME";
        Scanner scan = new Scanner(System.in);
        while(semester.equals("fall")==false&&semester.equals("spring")==false) {
            System.out.println("["+message+"]:Please enter the current semester: (fall/spring)");
            semester = scan.nextLine();
            message = "ERROR-Please try again";
        }
        RegistrationSystem registrationSystem = new RegistrationSystem(semester);

        //System.out.println("the semester you entered was "+semester);

        /*
        //Asagidaki kod ile 70 tane student in sorunsuz create edildigini check ediyoruz.
        ArrayList<Student> allStudents = registrationSystem.backendSystem.getAllStudents();
        for(Student student:allStudents){
            System.out.println("The number of the student is:"+student.getStudentID());
        }
         */

        message = "WELCOME";
        String action = "first check";
        while(action.equals("control a student")==false&&action.equals("batch process")==false) {
            System.out.println("["+message+"]:What do you want to do? (control a student/batch process)");
            action = scan.nextLine();
            message = "ERROR-Please try again";
        }

        if(action.equals("control a student")){
            registrationSystem.loginStudent();
            /*
            burda iyi bir arayuz vericem registration systemi kullanarak.
             */
        }
        else{
            /*
            not yet...
             */
        }
    }
}
