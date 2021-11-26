package system;

import model.Course;
import model.Student;

import java.io.IOException;
import java.util.*;

public class RegistrationSystem {
    public University university;
    private Student loggedStudent;
    private String semester;
    private PrerequisiteTree prerequisiteTree;
    private Boolean randomPlay;
    private Integer students2017;
    private Integer students2018;
    private Integer students2019;
    private Integer students2020;

    public RegistrationSystem() throws IOException {
        students2020 = 0;
        students2019 = 0;
        students2018 = 0;
        students2017 = 0;
        university = new University();
        this.prerequisiteTree = null;
        //Construction of prerequisite tree happens right after creating
        //all the courses on the system.
        createEverything();
        loggedStudent = null;
        //Currently, no student is logged on.
    }

    public Boolean getRandomPlay(){return randomPlay;}

    public void createEverything() throws IOException {
        //Create all the courses and advisors by READING the INPUT.json file.
        university.readAllInput();
        this.semester = university.getSemester();
        this.randomPlay = university.getRandomPlay();
        this.prerequisiteTree = new PrerequisiteTree(university.getAllCourses());
        //Now the tree has been constructed. We can use it to detect prerequisite course issues.

        //Randomly create all the students and add them to the university.
        //University part will take the rest, it will create a json file for each randomly created student.
        //Create randomly 270 students.
        for(int i=0;i<270;i++){
            university.registerStudent(createRandomStudent());
        }
        System.out.println("Total number of students is:"+ university.getAllStudents().size());
    }

    public void loginStudent(){
        System.out.println("Please enter your school ID.");
        Boolean check = true;
        while(check) {
            String id;
            Scanner scan = new Scanner(System.in);
            id = scan.nextLine();
            this.loggedStudent = university.getStudentByID(id);
            check = false;
            if (this.loggedStudent == null) {
                System.out.println("Login failed. Please enter your school ID correctly.");
                check = true;
            }
        }
    }

    public Student createRandomStudent(){
        /*
        The following code for the createRandomStudent may seem too long. But when you observe
        carefully you will notice that actually there are only three for loops and that's all.
        We have put long descriptions to indicate what is going on. So that's why the actual
        code written here is very little. Without the other objects, the code we would write
        in this createRandomStudent() method would probably be around 300-400 lines.
        Each job in this function is shared among the other objects such as:
        Student,Course,PrerequisiteTree,BackendSystem etc.
        That's the beauty of the Object-Oriented Programming.
        */

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

        Random rand = new Random();
        /*
        We use current time as our seed so that each run of the program will result
        in different simulation.
        */
        if(semester.equals("Spring")){
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

        ArrayList<Course> allCourses = university.getAllCourses();
        //Retrieve courses from the backend.
        Integer numOfCourses = allCourses.size();

        String[] studentLetterGrades = new String[numOfCourses];
        Boolean[] availableCourses = new Boolean[numOfCourses];
        //if availableCourses[i]==false then student can't take that course.
        //We will use that array to determine prerequisite course's children courses.

        //We first assume that the student has successfully passed all the courses until his semester.
        //And then we make student fail from some of these classes.
        //We will get help of the prerequisite tree while doing that.
        for(int i=0;i<numOfCourses;i++){
            if(allCourses.get(i).getCourseSemester() < studentSemester)
                availableCourses[i] = true;
            else
                availableCourses[i] = false;
        }

        for(int i=0;i<numOfCourses;i++){
            if(availableCourses[i]){

                //In our simulation we assume standard deviation of the students points is 9
                //In our simulation we assume mean of the points of students are 78

                Double courseGPA = rand.nextGaussian() * 20 + 78;

                if(courseGPA<45.0){studentLetterGrades[i]="FF";}
                else if(courseGPA<50.0){studentLetterGrades[i]="FD";}
                else if(courseGPA<55.0){studentLetterGrades[i]="DD";}
                else if(courseGPA<65.0){studentLetterGrades[i]="DC";}
                else if(courseGPA<75.0){studentLetterGrades[i]="CC";}
                else if(courseGPA<80.0){studentLetterGrades[i]="CB";}
                else if(courseGPA<85.0){studentLetterGrades[i]="BB";}
                else if(courseGPA<90.0){studentLetterGrades[i]="BA";}
                else{studentLetterGrades[i]="AA";}



                if(studentLetterGrades[i].equals("FF")||studentLetterGrades[i].equals("FD")) {
                    //In our simulation, we prevent a student failing from a course given
                    //two years before his semester.
                    //Otherwise, student would be one year behind the semester we selected.
                    //Imagine a seventh year student's ProgrammingI course grade, was randomly assigned as FF.
                    //That mustn't be the case, since that student wouldn't even be second grade in that situation.
                    //We should prevent this by giving student DD instead.
                    if (studentSemester - allCourses.get(i).getCourseSemester() > 4) {
                        studentLetterGrades[i] = "DD";
                    } else {
                        Integer failedCourse = i;
                        //Now here is the problem:
                        //What if the course we assigned as FF is and prerequisite course?
                        //We shouldn't allow student to take subclasses of this prerequisite course.
                        //We get help of the prerequisiteTree object.
                        prerequisiteTree.failPrerequisiteCourse(availableCourses,failedCourse);
                        //Done. With this single line of code we made children courses of this
                        //prerequisite course unavailable for that student.
                        availableCourses[i] = true;
                    }
                }
            }
        }
        //Now we have to generate student number for the student.
        //We have to consider semester of the student.
        //If the student in the first and second semester, then her number will be like 150120...
        //If the student in the third and fourth semester, then her number will be like 150119...
        //If the student in the fifth and sixth semester, then her number will be like 150118...
        //If the student in the seventh and eighth semester, then her number will be like 150117...
        //Of course, this condition may not hold for all students.
        //But in general the pattern is like this.
        String studentID = null;
        if(studentSemester==1||studentSemester==2){
            studentID = String.valueOf(150120000 + students2020++);
        }
        else if(studentSemester==3||studentSemester==4){
            studentID = String.valueOf(150119000 + students2019++);
        }
        else if(studentSemester==5||studentSemester==6){
            studentID = String.valueOf(150118000 + students2018++);
        }
        else{
            studentID = String.valueOf(150117000 + students2017++);
        }
        Student student = new Student(
                university.getRandomStudentName(),
                studentID,
                studentSemester,
                university.getRandomAdvisor()
        );

        //Now we make student pass from the assigned classes.
        for(int i=0;i<numOfCourses;i++){
            if(studentLetterGrades[i]!=null && availableCourses[i]) {
                //If this course NTE, assign a non-technical elective course randomly.
                //If this course FTE, assign a faculty technical elective course randomly.
                //If this course TE, assign a technical elective course randomly.
                if(allCourses.get(i).getCourseType().equals("Elective")){
                    String courseCode = allCourses.get(i).getCourseID();
                    String technicalType = String.valueOf(courseCode.charAt(0))+
                            String.valueOf(courseCode.charAt(1))+
                            String.valueOf(courseCode.charAt(2));
                    if(technicalType.equals("NTE")){
                        student.passCourse(university.getRandomNTECourse(), studentLetterGrades[i]);
                    }
                    else if(technicalType.equals("FTE")){
                        student.passCourse(university.getRandomFTECourse(), studentLetterGrades[i]);
                    }
                    else{
                        student.passCourse(university.getRandomTECourse(), studentLetterGrades[i]);
                    }
                }
                else {
                    student.passCourse(allCourses.get(i), studentLetterGrades[i]);
                }
            }
        }

        return student;
    }

    public ArrayList<Course> getAvailableCourses(Student student){
        /*
        In this method, we randomly assign NTE,FTE,TE courses for the student.
        Notice that this prerequisiteTree only returns the courses the student
        can take. This courses maybe general types such as
        NTE,FTE,TE. So, we handle these general types in this function.
         */
        ArrayList<Course> availableCourses =  prerequisiteTree.coursesStudentCanTake(student);
        for(int i=0;i<availableCourses.size();i++){
            if(availableCourses.get(i).getCourseType().equals("Elective")){
                String courseCode = availableCourses.get(i).getCourseID();
                String technicalType = String.valueOf(courseCode.charAt(0))+
                        String.valueOf(courseCode.charAt(1))+
                        String.valueOf(courseCode.charAt(2));
                if(technicalType.equals("NTE")){
                    availableCourses.set(i, university.getRandomNTECourse());
                }
                else if(technicalType.equals("FTE")){
                    availableCourses.set(i, university.getRandomFTECourse());
                }
                else{
                    availableCourses.set(i, university.getRandomTECourse());
                }
            }
        }
        return availableCourses;
    }

    public void batchProcess() throws IOException {
        university.writeWholeSystem(true, new String());
        /*
        OUR COURSE REGISTRATION SYSTEM MAKES STUDENT REGISTER
        COURSES ONE BY ONE. So the students with smaller
        student IDs will finish their registrations first.
        We will iterate students array and make each student
        register for the courses one by one.

        The steps we follow when choosing a course for a
        particular student are as follows:
        -We get available course that student can take.
        -We randomly select a some of these courses for the student.
        -We send this selection to the verification of the system.
        -We send these selected courses to the approval of the advisor.
        -END.
         */



        /*
        -The course selection of the following 14 students remained in
        the draft because their advisors did not approve.
        -The following 10 students were unable to take CSE3063 course
        due to the course overlaps.
        -The following 10 students were unable to take CSE3063 course
        due to the quota problems.

        To get the outputs above, we should keep these 2 map and this string array below.
         */
        Map<String,ArrayList<String>> quotaProblems = new HashMap<String,ArrayList<String>>();
        Map<String,ArrayList<String>> overlapProblems = new HashMap<String,ArrayList<String>>();
        ArrayList<String> advisorProblems = new ArrayList<String>();


        ArrayList<Student> allStudents = university.getAllStudents();
        Random rand = new Random();
        for(Student currStudent:allStudents){
            ArrayList<Course> availableCourses = getAvailableCourses(currStudent);
            //We now have all availableCourses for that student.
            //It is time to select randomly courses from these available courses.
            //We will run a set selection algorithm to select courses.
            ArrayList<Boolean> selected = new ArrayList<Boolean>();
            //false means the course at index 'i' is not selected.
            //true means the course at index 'i' is selected.
            for(int i=0;i<availableCourses.size();i++)
                selected.add(false);

            //In general a student chooses 5 courses. But he or she may be failed
            //from some previous classes. So, he or she may choose 7-8 classes per semester.
            //So;
            Integer numOfClassesSelected = 4 + rand.nextInt(5);

            for(int count=0;count<numOfClassesSelected;count++){
                Integer i = rand.nextInt(availableCourses.size());
                selected.set(i,true);
                //The course at index 'i' is selected.
            }

            //SYSTEM VERIFICATION:
            for(int i=0;i<availableCourses.size();i++){
                //If the current course is not selected, then continue...
                if(selected.get(i)==false){continue;}

                String check = sendSelectionSystemVerification(availableCourses.get(i),currStudent);
                if(check.equals("success")==false){
                    //Found a problem... Report it and don't add this course to the list..
                    currStudent.addAction(check);
                    if(check.charAt(1)=='Q'){
                        String key = availableCourses.get(i).getCourseID().toString();

                        ArrayList<String> studentsWithIssues =
                                quotaProblems.getOrDefault(key,new ArrayList<String>());
                        studentsWithIssues.add(currStudent.getStudentID());

                        quotaProblems.put(key,studentsWithIssues);
                    }
                    else{
                        String key = availableCourses.get(i).getCourseID().toString();

                        ArrayList<String> studentsWithIssues =
                                overlapProblems.getOrDefault(key,new ArrayList<String>());
                        studentsWithIssues.add(currStudent.getStudentID());

                        overlapProblems.put(key,studentsWithIssues);
                    }
                }
                else{
                    currStudent.addCourse(availableCourses.get(i));
                }
            }

            ArrayList<Course> selectedCourses = currStudent.getAddedCourses();

            //ADVISOR VERIFICATION:
            String check = sendSelectionAdvisorVerification(selectedCourses,currStudent);
            if(check.charAt(0)=='['){//Advisor didn't approve...
                advisorProblems.add(currStudent.getStudentID());
            }
        }

        //Now it is time to print department output.

        StringBuffer departmentOutput = new StringBuffer();
        ArrayList<Course> allCourses = university.getAllCourses();
        for(Course course:allCourses){
            String key = course.getCourseID();
            if(overlapProblems.containsKey(key)){
                departmentOutput.append("The following "+overlapProblems.get(key).size());
                departmentOutput.append(" students were unable to take "+course.getCourseID()+" course due " +
                        "to the course overlaps.\n");
                departmentOutput.append(overlapProblems.get(key).toString());
                departmentOutput.append("\n\n");
            }
            if(quotaProblems.containsKey(key)){
                departmentOutput.append("The following "+quotaProblems.get(key).size());
                departmentOutput.append(" students were unable to take "+course.getCourseID()+" course due " +
                        "to the course overlaps.\n");
                departmentOutput.append(quotaProblems.get(key).toString());
                departmentOutput.append("\n\n");
            }
        }
        departmentOutput.append("The course selection of the following "+advisorProblems.size()+" students " +
                "remained in the draft because their advisors did not approve.\n");
        departmentOutput.append(advisorProblems.toString());
        departmentOutput.append("\n\n");
        System.out.println(departmentOutput.toString());
        university.writeWholeSystem(false,departmentOutput.toString());
    }

    public void printCourseInfoLine(String code,String courseName,String credit){
        String first = "| " + code;
        for(int i=0;i<10;i++){
            if(i<first.length()){
                System.out.print(first.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        String second = "| " + courseName;
        for(int i=0;i<37;i++){
            if(i<second.length()){
                System.out.print(second.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        String third = "| " + credit;
        for(int i=0;i<12;i++){
            if(i<third.length()){
                System.out.print(third.charAt(i));
            }
            else{
                System.out.print(" ");
            }
        }
        System.out.println("|");
    }

    public void listAvailableCourses(){
        ArrayList<Course> coursesCanTake = getAvailableCourses(loggedStudent);
        //Getting all the available courses with this single line of code...

        System.out.println("____________________________________________________________");
        System.out.println("|                  COURSE REGISTRATION                     |");
        System.out.println("____________________________________________________________");
        System.out.println("| Code    |   Course Name                      | Credit    |");
        System.out.println("____________________________________________________________");
        for(Course course:coursesCanTake){
            printCourseInfoLine(course.getCourseID(),course.getCourseFullName(),course.getCourseCredit().toString());
            System.out.println("____________________________________________________________");
        }
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
                if(studentSchedule[i][j]==null){
                    weeklySchedule[i+1][j+1] = String.format("%15s","");
                }
                else
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

    public String sendSelectionSystemVerification(Course course, Student student){
        if(course.isQuotaFull()){
            return "[Q][VERIFICATION-FAILED]:The system didnt allow student " + student.getStudentID()+
                    " to take " + course.getCourseID() + " because quota is full.";
        }
        //System should check if there is a course overlaps.
        String overlapCheck = student.checkCourseOverlap(course);
        // With this single line of code, the system verification is done thanks to Student object.
        if(!overlapCheck.equals("success"))
            return "[O]"+overlapCheck;
        else
            return "success";
    }

    public String sendSelectionAdvisorVerification(ArrayList<Course> selectedCourses, Student student){
        /*
        Advisor should check if the student took credits higher than 35.
        Advisor can check many regulations applied in our university.
        */
        String answer = student.getStudentAdvisor().verifyStudentRegistration(student,selectedCourses);
        //All these checks are done by with this single line of code. Thanks to Advisor object.
        return answer;
    }

    public void selectCourses(){
        ArrayList<Course> courses = getAvailableCourses(loggedStudent);
        /*
        There will be two steps verification while adding each selected course.
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

            Boolean earlyStop = false;
            ArrayList<Course> selectedCourses = new ArrayList<Course>();
            for (int i = 0; i < options.size(); i++) {
                Course selectedCourse = university.getCourseByID(options.get(i));

                Boolean isProblem = true;
                //Determine if the user enters garbage instead of the course ID.
                if (selectedCourse != null) {
                    //Prevent user to typing not available courses.
                    //She may write the higher semester's courses' IDs to take them.
                    for (Course checkCourse : courses) {
                        if (checkCourse.getCourseID().equals(selectedCourse.getCourseID())) {
                            isProblem = false;
                            break;
                        }
                    }
                }

                if(isProblem){
                    earlyStop = true;
                    break;
                }
                selectedCourses.add(selectedCourse);
            }
            if(earlyStop){
                message = "Please enter the name of the course correctly.";
                continue;
            }
            if(selectedCourses.size()>10){
                message = "A student can register for maximum of 10 courses per semester.";
                continue;
            }

            /*
            First step of the verification.
            System verifies the added courses.
             */
            earlyStop = false;
            for(int i=0;i<selectedCourses.size();i++){
                String firstStep = sendSelectionSystemVerification(selectedCourses.get(i),loggedStudent);
                if(!firstStep.equals("success")){
                    message = firstStep;
                    earlyStop = true;
                    break;
                }
            }
            if(earlyStop) {
                //The system has not verified the courses.
                //Ask logged student to try again.
                continue;
            }

            /*
            Second step of the verification.
            Advisor verifies the added courses.
             */
            String secondStep = sendSelectionAdvisorVerification(selectedCourses,loggedStudent);
            if(secondStep.charAt(0)=='['){
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

    public void printStudentTranscript() {
        loggedStudent.printStudentTranscript();
    }

    public void printStudentTranscript(Student student){
        student.printStudentTranscript();
    }

    public void printWelcomeMessage(Boolean errorMessage){
        System.out.println("Welcome " + loggedStudent.getStudentName() + "!");
        System.out.println("Please enter one of the actions:");
        System.out.println("1.Add Course");
        System.out.println("2.Print Weekly Schedule");
        System.out.println("3.Print Transcript");
        System.out.println("4.Exit");
        if(errorMessage)
            System.out.println("Please enter the indexes correctly.");
    }
}
