import system.RegistrationSystem;
import java.io.IOException;
import java.util.Scanner;
/*
   ___ ___ ___ ____ __   __ ____
  / __/ __| __|__ //  \ / /|__ /
 | (__\__ \ _| |_ \ () / _ \|_ \
  \___|___/___|___/\__/\___/___/__
 | _ \ _ \/ _ \ _ | | __/ __|_   _|
 |  _/   / (_) | || | _| (__  | |
 |_| |_|_\\___/ \__/|___\___| |_|
 CSE3063 PROJECT
 ONLINE STUDENT REGISTRATION SYSTEM
 ITERATION-1 CODE
 */
//  ********************************
//  * Authors:                     *
//  * ONURCAN ISLER 150120825      *
//  * BERKAY MENGUNOGUL 150119934  *
//  * SINAN DUMANSIZ 150119812     *
//  * MERVE HAZAL OZALP 150120827  *
//  * KEREM KOSIF 150119909        *
//  * MURAT TUZUN 150119633        *
//  ********************************

/*
Dear reader, please make batch process 'false'
in 'data/input.json' file if you want to control a
particular student on UI.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        RegistrationSystem registrationSystem = new RegistrationSystem();
        if(registrationSystem.getRandomPlay()){
            registrationSystem.batchProcess();
            //You can see the results before the registration and the results
            //after the registration in the 'data/students/' folder.
        }
        else {
            Scanner scan = new Scanner(System.in);
            registrationSystem.loginStudent();
            Boolean wannaQuit = false;
            Boolean errorMessage = false;
            while(!wannaQuit) {
                for(int i=0;i<6;i++)
                    System.out.println();

                registrationSystem.printWelcomeMessage(errorMessage);
                Integer ch = scan.nextInt();
                switch (ch) {
                    case 1:
                        registrationSystem.selectCourses();
                        errorMessage = false;
                        break;
                    case 2:
                        registrationSystem.printWeeklySchedule();
                        errorMessage = false;
                        break;
                    case 3:
                        registrationSystem.printStudentTranscript();
                        errorMessage = false;
                        break;
                    case 4:
                        wannaQuit = true;
                        break;
                    default:
                        errorMessage = true;
                }
            }
        }
    }
}
