package system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.StudentDto;
import model.Course;
import model.Student;
import model.StudentTranscript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


public class RegistrationSystem {
    public University university;
    private Student loggedStudent;
    private String semester;
    private PrerequisiteTree prerequisiteTree;
    private Integer students2017;
    private Integer students2018;
    private Integer students2019;
    private Integer students2020;
    private static final Logger logger = Logger.getLogger("");

    public RegistrationSystem(String semester) throws IOException {

        students2020 = 0;
        students2019 = 0;
        students2018 = 0;
        students2017 = 0;
        university = new University();
        this.prerequisiteTree = null;
        this.semester = semester;
        //Construction of prerequisite tree happens right after creating
        //all the courses on the system.
        createEverything();
        loggedStudent = null;
        //Currently, no student is logged on.
    }

    public void createEverything() throws IOException {
        //Create all the courses and advisors by READING the INPUT.json file.
        university.readAllInput();
        this.prerequisiteTree = new PrerequisiteTree(university.getAllCourses());
        //Now the tree has been constructed. We can use it to detect prerequisite course issues.

        //Randomly create all the students and add them to the university.
        //University part will take the rest, it will create a json file for each randomly created student.
        //Create randomly 270 students.
        for(int i=0;i<270;i++){
            university.registerStudent(createRandomStudent());
        }
        logger.info("Total number of registered students:"+ university.getAllStudents().size());
    }

    public Student getStudentByID(String id){
        return university.getStudentByID(id);
    }

    public void loginStudent(Student loggedStudent){
        this.loggedStudent = loggedStudent;
    }

    public StudentTranscript getLoggedStudentTranscript(){
        return loggedStudent.getStudentTranscript();
    }

    public String getSemester() {
        return this.semester;
    }

    public Student getLoggedStudent() {
        return loggedStudent;
    }

    public String getLoggedStudentAdvisor(){
        return loggedStudent.getStudentAdvisor().getAdvisorName();
    }

    public Double getLoggedStudentGPA(){
        return loggedStudent.getCGPA();
    }

    public Integer getLoggedStudentCompletedCredit(){
        return loggedStudent.completedCredits();
    }

    public String getLoggedStudentName(){
        return loggedStudent.getStudentName();
    }

    public String getLoggedStudentID(){
        return loggedStudent.getStudentID();
    }

    public Course[][] getLoggedStudentWeeklySchedule(){return loggedStudent.getWeeklySchedule();}

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
        logger.info("Student "+studentID+" registered.");
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
        logger.info("Available Students fetched for student "+ student.getStudentID());
        return availableCourses;
    }

    public String sendSelectionSystemVerification(Course course, Student student){
        if(course.isQuotaFull()){
            logger.warning("The system didnt allow student " + student.getStudentID()+
                    " to take " + course.getCourseID() + " because quota is full.");

            return "[Q][VERIFICATION-FAILED]:The system didnt allow student " + student.getStudentID()+
                    " to take " + course.getCourseID() + " because quota is full.";
        }
        //System should check if there is a course overlaps.
        String overlapCheck = student.checkCourseOverlap(course);
        // With this single line of code, the system verification is done thanks to Student object.
        if(!overlapCheck.equals("success")) {
            logger.warning(overlapCheck);
            return "[O]" + overlapCheck;
        }else {
            return "success";
        }
    }

    public String sendSelectionAdvisorVerification(ArrayList<Course> selectedCourses, Student student){
        /*
        Advisor should check if the student took credits higher than 35.
        Advisor can check many regulations applied in our university.
        */
        logger.info("Student "+student.getStudentID()+" has sent the selected to courses to the advisor.");
        //All these checks are done by with this single line of code. Thanks to Advisor object.
        return student.getStudentAdvisor().verifyStudentRegistration(student,selectedCourses);
    }

    public String makeSelection(ArrayList<String> courses) throws IOException {
        ArrayList<Course> coursesSelected = new ArrayList<Course>();
        for(String courseCode:courses){
            Course check = university.getCourseByID(courseCode);
            if(check==null){
                return "SYSTEM ERROR";
            }
            coursesSelected.add(check);
        }
        for(Course course:coursesSelected){
            String checkSystemVerification = sendSelectionSystemVerification(course,loggedStudent);
            if(checkSystemVerification.equals("success")==false){
                if(checkSystemVerification.charAt(1)=='Q'){
                    return "Couldn't register "+ course.getCourseID() + " because the quota is full.";
                }
                else{
                    int i = 0;
                    for(i=0;i<checkSystemVerification.length();i++){
                        if(checkSystemVerification.charAt(i)==':'){
                            i++;
                            break;
                        }
                    }
                    String check = checkSystemVerification.substring(i);
                    if(check==null){
                        return "SYSTEM ERROR";
                    }
                    return check;
                }
            }
        }
        /*
        D:Didn't approve classes with D.
        H:Didn't approve more than 3 classes from higher semesters.
        T:Completed credits should be greater than 155 to take TE
        P:Completed credits should be greater than 165 to take first term project 4197.
        C:Max 35 credits...
        */
        String checkAdvisorVerification = sendSelectionAdvisorVerification(coursesSelected,loggedStudent);
        if(checkAdvisorVerification.equals("success")==false){
            if(checkAdvisorVerification.charAt(1)=='H'){
                logger.warning("Advisor did not approve student "+loggedStudent.getStudentID()+" 's registration. Student selected more than 3 classes from higher semesters.");
                return "Advisor didn't approve your registration.+"+"\n"+"You cannot register more than 3 classes from higher semesters.";
            }
            if(checkAdvisorVerification.charAt(1)=='D'){
                logger.warning("Advisor did not approve student "+loggedStudent.getStudentID()+" 's registration. Advisor didn't allow student to take a class with D.");
                return "Advisor didn't approve your registration."+"\n"+" Advisor didn't allow you to take a class with D.";
            }
            if(checkAdvisorVerification.charAt(1)=='T'){
                logger.warning("Advisor did not approve student "+loggedStudent.getStudentID()+" 's registration. Completed credits should be greater than 155 to take a Technical Elective course.");
                return "Advisor didn't approve your registration."+"\n"+"Completed credits should be greater than 155 to take a Technical Elective course.";
            }
            if(checkAdvisorVerification.charAt(1)=='P'){
                logger.warning("Advisor did not approve student "+loggedStudent.getStudentID()+" 's registration. Completed credits should be greater than 165 to take CSE4191 course.");
                return "Advisor didn't approve your registration."+"\n"+"Completed credits should be greater than 165 to take CSE4191 course.";
            }
            if(checkAdvisorVerification.charAt(1)=='C'){
                logger.warning("Advisor did not approve student "+loggedStudent.getStudentID()+" 's registration. Student cannot take more than 35 credits in a semester.");
                return "Advisor didn't approve your registration."+"\n"+"You cannot take more than 35 credits in a semester.";
            }
        }
        for(Course course:coursesSelected){
            loggedStudent.addCourse(course);
        }
        // Write current status of the student to json file
        logger.info("Student "+loggedStudent.getStudentID()+" successfully registered to selected courses.");

        String pathName = loggedStudent.getStudentID() + "after";
        FileWriter writer = new FileWriter("iteration#2"+ File.separator+"iteration#2"+ File.separator +"data"+ File.separator +"student"+ File.separator +pathName+".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        StudentDto studentDto = new StudentDto(loggedStudent);
        //With this single line of code Student object is converted to StudentDto object.
        //Now we are ready to write the student.
        gson.toJson(studentDto,writer);
        writer.close();

        return "success";
    }
}
