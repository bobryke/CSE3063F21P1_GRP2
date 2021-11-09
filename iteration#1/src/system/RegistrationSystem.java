package system;

import model.Advisor;
import model.Course;
import model.Student;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RegistrationSystem {
    public BackendSystem backendSystem;
    private Student loggedStudent;
    private String semester;

    public RegistrationSystem(String semester){
        backendSystem = new BackendSystem();
        this.semester = semester;
        createAllAdvisors();
        createAllCourses();
        createAllStudents();
        loggedStudent = null;
        //Currently, no student is logged on.
    }

    public void createAllCourses(){
        //Create all the courses by READING the json files of the courses.
        //We will create json file of the courses manually.
        //Course(String courseID, String courseFullName, String courseType, Integer courseCredit)
        backendSystem.addNewCourse(new Course("CSE3063","ObjectOrientedSoftwareDesign",
                "Mandatory",5,5));
        backendSystem.addNewCourse(new Course("CSE1100","ProgrammingI",
                "Mandatory",6,1));
    }

    public void createAllStudents(){
        //Randomly create all the students and add them to the backend.
        //Backend part will take the rest, it will create a json file for each randomly created student.
        //Create randomly 70 students.
        for(int i=0;i<70;i++){
            backendSystem.addNewStudent(createRandomStudent());
        }
    }

    public void createAllAdvisors(){
        backendSystem.addNewAdvisor(new Advisor("Betul Boz"));
        backendSystem.addNewAdvisor(new Advisor("Borahan Tumer"));
    }

    public void loginStudent(){
        System.out.println("Please enter your school ID.");
        Boolean check = true;
        while(check) {
            String id;
            Scanner scan = new Scanner(System.in);
            id = scan.nextLine();
            this.loggedStudent = backendSystem.getStudentByID(id);
            check = false;
            if (this.loggedStudent == null) {
                System.out.println("Login failed. Please enter your school ID correctly.");
                check = true;
            }
        }
    }

    public Student createRandomStudent(){
        /*
        Now, we randomly determine the year of the student.
        Remember, we have already taken the semester from the user at the very beginning
        of the program.

        So:
        *if the user enters fall semester as input, then the student would be in:
            first semester, third semester, fifth semester, seventh semester
        *if the user enters spring semester as input, then the student would be in:
            second semester, fourth semester, sixth semester, eight semester

        We determine one of these options with equal probability.
        All we have to do is to generate a random number.
         */
        Integer studentSemester = 0;

        Random rand = new Random(System.currentTimeMillis());
        /*
        We use current time as our seed so that each run of the program will result
        in different simulation.
        */
        if(semester.equals("Fall")){
            studentSemester = 2*(rand.nextInt(5)); //second,fourth,sixth,eighth
        }
        else{
            studentSemester = 2*(rand.nextInt(4))+1; //first,third,fifth,seventh
        }

        /*
        Now, we know her semester. It is time to assign some letter grades.
        If we assign letter grade FF to a prerequisite course, then she would
        fail from multiple classes.
        She will have to take these FF classes.
        We will check that condition.

        We will assign letter grades based on normal distribution.
        So CGPA of the students would have normal curve shape.
        (Normally distributed Gaussian distribution)
        We will use the nextGaussian() function in Java to do that.

        We first assume the student passed all the courses until his semester.
        And then we make this student fail from some of these classes with very small
        probability.
        */
        String[] letterGradeList = {"FF","FD","DD","DC","CC","CB","BB","BA","AA"};

        ArrayList<Course> allCourses = backendSystem.getAllCourses();
        //Retrieve courses from the backend.
        Integer numOfCourses = allCourses.size();

        String[] studentLetterGrades = new String[numOfCourses];

        for(int i=0;i<numOfCourses;i++){
            if(allCourses.get(i).getCourseSemester() < studentSemester){

                //In our simulation we assume standard deviation of the students points is 9
                //In our simulation we assume mean of the points of students are 78

                Double courseGPA = rand.nextGaussian() * 9 + 78;

                if(courseGPA<45.0){studentLetterGrades[i]="FF";}
                else if(courseGPA<50.0){studentLetterGrades[i]="FD";}
                else if(courseGPA<55.0){studentLetterGrades[i]="DD";}
                else if(courseGPA<65.0){studentLetterGrades[i]="DC";}
                else if(courseGPA<75.0){studentLetterGrades[i]="CC";}
                else if(courseGPA<80.0){studentLetterGrades[i]="CB";}
                else if(courseGPA<85.0){studentLetterGrades[i]="BB";}
                else if(courseGPA<90.0){studentLetterGrades[i]="BA";}
                else{studentLetterGrades[i]="AA";}

                /*
                I HAVE NO TIME TO WRITE THE CODE MAKE STUDENT FAIL FROM THE PREREQUISITE COURSES.
                LATER, I WILL HANDLE THAT TRICKY JOB.

                if(studentLetterGrades[i].equals("FF")||studentLetterGrades[i].equals("FD")){

                    //In our simulation, we prevent a student failing from a course given
                    //two years before his semester.
                    //Otherwise, student would be one year behind the semester we selected.

                    if(studentSemester-allCourses.get(i).getCourseSemester()>2){
                        studentLetterGrades[i] = "DD";
                    }
                    else{
                        Course failedCourse = allCourses.get(i);
                        ArrayList<Course> prerequisites = failedCourse.getPrerequisites();
                        //We will make that student fail from other classes.
                    }
                }
                */
            }
        }


        //For now let's just assume student's name would be one of these options:
        String[] names = {"Andy","Ross","Bryant","Victor","Kay","Christian"};
        //While generating student number use: String.valueOf(backendSystem.getAllStudents().size())
        //Assign a random advisor in the system by: allAdvisors.get(rand.nextInt(allAdvisors.size()))
        ArrayList<Advisor> allAdvisors = backendSystem.getAllAdvisors();
        Student student = new Student(names[rand.nextInt(6)],
                String.valueOf(backendSystem.getAllStudents().size()+150119000),studentSemester,
                allAdvisors.get(rand.nextInt(allAdvisors.size())));

        //Now we make student pass from the assigned classes.
        for(int i=0;i<numOfCourses;i++){
            if(studentLetterGrades[i]!=null)
            student.passCourse(allCourses.get(i),studentLetterGrades[i]);
        }

        return student;
    }

    public void listAvailableCourses(){
        //We do some tricky stuff to list available courses for the student.
    }

    public void printWeeklySchedule(){
        String[][] weeklySchedule = new String[9][6];
        weeklySchedule[0][0] = "               ";
        weeklySchedule[0][1] = " Monday        ";
        weeklySchedule[0][2] = " Tuesday       ";
        weeklySchedule[0][3] = " Wednesday     ";
        weeklySchedule[0][4] = " Thursday      ";
        weeklySchedule[0][5] = " Friday        ";
        weeklySchedule[1][0] = " 09:30 - 10:20 ";
        weeklySchedule[2][0] = " 10:30 - 11:20 ";
        weeklySchedule[3][0] = " 11:30 - 12:20 ";
        weeklySchedule[4][0] = " 13:00 - 13:50 ";
        weeklySchedule[5][0] = " 14:00 - 14:50 ";
        weeklySchedule[6][0] = " 15:00 - 15:50 ";
        weeklySchedule[7][0] = " 16:00 - 16:50 ";
        weeklySchedule[8][0] = " 17:00 - 17:50 ";
        Course[][] studentSchedule = loggedStudent.getWeeklySchedule();

        for(int i=0;i<8;i++){
            for(int j=0;j<5;j++){
                weeklySchedule[i+1][j+1] = String.format("%15s",studentSchedule[i][j].getCourseID());
            }
        }

        for(int i=0;i<9;i++){
            for(int j=0;j<6;j++){
                System.out.print(weeklySchedule[i][j]+"|");
            }
            System.out.println();
        }
    }

    public String sendSelectionSystemVerification(Course course){
        /*
        System should check if there is course overlaps.
         */
        String overlapCheck = loggedStudent.checkCourseOverlap(course);

        if(!overlapCheck.equals("success"))
            return overlapCheck;
        else
            return "success";
    }

    public String sendSelectionAdvisorVerification(ArrayList<Course> selectedCourses){
        /*
        Advisor should check if the student took credits higher than 35.
        Advisor can check many regulations applied in our university.
         */
        String answer = loggedStudent.getStudentAdvisor().verifyStudentRegistration(loggedStudent,selectedCourses);
        return answer;
    }

    public void addSelectedCourses(){
        /*
        There would be two steps verification while adding each selected course.
        In the first step, the system verification is needed.
        In the second step, the approval of the advisor is needed.
        If both of these steps are successful, then the student would finish his registration.
        */
        String message = new String("fine");
        while(!message.equals("success")) {
            if(message.equals("fine")) {
                listAvailableCourses();
            }
            else {
                listAvailableCourses();
                System.out.println(message);
                System.out.println("Please try again.");
            }
            System.out.println("Please enter the course IDs of the courses you want to add:");
            System.out.println("Please type 'done' when you entered all the courses that you want to add.");

            Scanner scan = new Scanner(System.in);
            ArrayList<String> options = new ArrayList<String>();
            while (scan.hasNextLine()) {
                String curr = scan.nextLine();
                if (curr.equals("done")) {
                    break;
                }
                options.add(curr);
            }

            ArrayList<Course> selectedCourses = new ArrayList<Course>();
            for (int i = 0; i < options.size(); i++) {
                Course selectedCourse = backendSystem.getCourseByID(options.get(i));
                selectedCourses.add(selectedCourse);
            }

            if(selectedCourses.size()>10){
                message = "A student can register for a maximum of 10 courses per semester.";
                continue;
            }

            /*
            First step of the verification.
            System verifies the added courses.
             */
            Boolean earlyStop = false;
            for(int i=0;i<selectedCourses.size();i++){
                String firstStep = sendSelectionSystemVerification(selectedCourses.get(i));
                if(!firstStep.equals("success")){
                    message = firstStep;
                    earlyStop = true;
                    break;
                }
            }
            if(earlyStop) {
                continue;
            }

            /*
            Second step of the verification.
            Advisor verifies the added courses.
             */
            String secondStep = sendSelectionAdvisorVerification(selectedCourses);
            if(secondStep.charAt(12)=='d'){
                message = "The selection wasn't approved by the advisor.";
                continue;
            }

            /*
            The registration is successful.
            System verified selections.
            Advisor approved selections.
             */

            message = "success";
            System.out.println("Successfully registered to the courses:");
            for(int i=0;i<selectedCourses.size();i++){
                loggedStudent.addCourse(selectedCourses.get(i));
                System.out.print(selectedCourses.get(i).getCourseID());
                if(i!=selectedCourses.size()-1){
                    System.out.print(",");
                }
            }
        }
    }
}
