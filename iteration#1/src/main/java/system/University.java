package system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import dto.CourseDto;
import dto.PrerequisiteDto;
import dto.StudentDto;
import model.Advisor;
import model.Course;
import model.Student;
import model.StudentTranscript;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.List.of;

public class University {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private ArrayList<Course> nteCourses;
    private ArrayList<Course> fteCourses;
    private ArrayList<Course> teCourses;
    private ArrayList<Advisor> advisors;
    private List<String> studentNames;
    private ArrayList<StudentTranscript> studentTranscripts;
    private String semester;
    private Boolean randomPlay;


    public University() {
        this.students = new ArrayList<Student>();
        this.courses = new ArrayList<Course>();
        this.fteCourses = new ArrayList<Course>();
        this.nteCourses = new ArrayList<Course>();
        this.teCourses = new ArrayList<Course>();
        this.advisors = new ArrayList<Advisor>();
        this.studentTranscripts = new ArrayList<StudentTranscript>();
        semester = "fall";
        //it will change once we read the input.json.
        studentNames = new ArrayList<String>();
        randomPlay = true;
    }

    public void registerStudent(Student student){
        students.add(student);
    }

    public void hireAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }

    public void addNewCourse(Course course){
        this.courses.add(course);
    }

    public void readAllInput() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Reader reader = Files.newBufferedReader(Paths.get("iteration#1/data/input.json"));

        Map<?,?> map = gson.fromJson(reader,Map.class);

        List<CourseDto> courseDtos = new ArrayList<CourseDto>();
        List<CourseDto> teCourseDtos = new ArrayList<CourseDto>();
        List<CourseDto> nteCourseDtos = new ArrayList<CourseDto>();
        List<CourseDto> fteCourseDtos = new ArrayList<CourseDto>();

        List<PrerequisiteDto> prerequisiteDtos = new ArrayList<PrerequisiteDto>();

        String batchProcessPlay = "true";

        for(Map.Entry<?,?> entry:map.entrySet()){
            String key = (String) entry.getKey();
            if(key.equals("semester")){//
                semester = (String) entry.getValue();
            }
            else if(key.equals("nonTechnicalElectiveCourses")){
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                nteCourseDtos = Arrays.asList(gson.fromJson(jsonArray,CourseDto[].class));
            }
            else if(key.equals("compulsoryCourses")){
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                courseDtos = Arrays.asList(gson.fromJson(jsonArray,CourseDto[].class));
            }
            else if(key.equals("technicalElectiveCourses")){
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                teCourseDtos = Arrays.asList(gson.fromJson(jsonArray,CourseDto[].class));
            }
            else if(key.equals("facultyTechnicalElectiveCourses")){
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                fteCourseDtos = Arrays.asList(gson.fromJson(jsonArray,CourseDto[].class));
            }
            else if(key.equals("prerequisites")){//
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                prerequisiteDtos = Arrays.asList(gson.fromJson(jsonArray,PrerequisiteDto[].class));
            }
            else if(key.equals("studentNames")){//
                JsonArray jsonArray = gson.toJsonTree(entry.getValue()).getAsJsonArray();
                studentNames = Arrays.asList(gson.fromJson(jsonArray,String[].class));
            }
            else if(key.equals("batchProcess")){//
                batchProcessPlay = (String)entry.getValue();
            }
        }
        this.randomPlay = (batchProcessPlay.equals("true")) ? true : false;
        reader.close();
        String[] temp = {
                "Prof. Dr. Haluk Rahmi TOCUOGLU",
                "Prof.Dr. Cigdem Eroglu ERDEM",
                "Doc. Dr. Ali Fuat ALKAYA",
                "Doc. Dr. Murat Can GANIZ",
                "Doc. Dr. Mujdat SOYTURK",
                "Doc. Dr. Borahan TUMER",
                "Doc. Dr. Mustafa AGAOGLU",
                "Dr. Ogr. Uyesi Mehmet BARAN",
                "Dr. Ogr. Uyesi Betul Demiroz BOZ",
                "Dr. Ogr. Uyesi Fatma Corut ERGIN",
                "Doc. Dr. Omer KORCAK",
                "Dr. Ogr. Uyesi Ali Haydar OZER",
                "Dr. Ogr. Uyesi Sanem Arslan"
        };
        //System.out.println("The size of the courseDtos is:"+courseDtos.size());
        //System.out.println("The size of the nteCourseDtos is:"+nteCourseDtos.size());
        //System.out.println("The size of the teCourseDtos is:"+teCourseDtos.size());
        //System.out.println("The size of the fteCourseDtos is:"+fteCourseDtos.size());
        //System.out.println("The size of the prerequisiteDtos is:"+prerequisiteDtos.size());
        //System.out.println("The size of the student names is:"+studentNames.size());
        for(int i=0;i<temp.length;i++){
            hireAdvisor(new Advisor(temp[i]));
        }

        for(CourseDto courseDto:courseDtos){
            courses.add(courseDto.convertToRealObject(prerequisiteDtos));
        }
        for(CourseDto courseDto:fteCourseDtos){
            fteCourses.add(courseDto.convertToRealObject(prerequisiteDtos));
        }
        for(CourseDto courseDto:nteCourseDtos){
            nteCourses.add(courseDto.convertToRealObject(prerequisiteDtos));
        }
        for(CourseDto courseDto:teCourseDtos){
            teCourses.add(courseDto.convertToRealObject(prerequisiteDtos));
        }
    }

    public void writeWholeSystem(Boolean beforeRegistration, String departmentOutput) throws IOException {
        String postfix = (beforeRegistration)? "before.json" : "after.json";
        //'before' means the student file before she finished her registration.
        //'after' means the student file after she finished her registration.
        //so we create two files for each student:
        //150120825before.json
        //150120825after.json
        //So, observing the results would become much easier.


        for(Student currStudent:students){
            String pathName = currStudent.getStudentID() + postfix;
            FileWriter writer = new FileWriter("iteration#1/data/student/"+pathName);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();


            StudentDto studentDto = new StudentDto(currStudent);
            //With this single line of code Student object is converted to StudentDto object.
            //Now we are ready to write the student.
            gson.toJson(studentDto,writer);
            writer.close();
        }

        if(beforeRegistration==false){
            String pathName = "Department_Output_"+semester+".txt";
            FileWriter writer = new FileWriter("iteration#1/data/department/"+pathName);
            writer.write(departmentOutput);
            writer.close();
        }
    }

    public Student getStudentByID(String studentID){
        //Later we will get the student by reading the json file of that student.
        //For now, we just get the student by looking into the student array.
        for(Student searchStudent:students){
            if(searchStudent.getStudentID().equals(studentID)){
                return searchStudent;
            }
        }
        return null;
    }

    public Course getCourseByID(String courseID){
        for(Course course:courses){
            if(course.getCourseID().equals(courseID)){
                return course;
            }
        }
        return null;
    }

    public ArrayList<Course> getAllCourses(){
        return courses;
    }

    public ArrayList<Course> getAllTECourses(){
        return teCourses;
    }

    public ArrayList<Course> getAllFTECourses(){
        return fteCourses;
    }

    public ArrayList<Course> getAllNTECourses(){
        return nteCourses;
    }

    public ArrayList<Student> getAllStudents(){
        return students;
    }

    public Course getRandomFTECourse(){
        Random rand = new Random();
        Integer index = rand.nextInt(fteCourses.size());
        return fteCourses.get(index);
    }

    public Course getRandomTECourse(){
        Random rand = new Random();
        Integer index = rand.nextInt(teCourses.size());
        return teCourses.get(index);
    }

    public Course getRandomNTECourse(){
        Random rand = new Random();
        Integer index = rand.nextInt(nteCourses.size());
        return nteCourses.get(index);
    }

    public String getSemester() {
        return semester;
    }

    public Boolean getRandomPlay() {
        return randomPlay;
    }

    public Advisor getRandomAdvisor(){
        Random rand = new Random();
        Integer index = rand.nextInt(advisors.size());
        return advisors.get(index);
    }

    public String getRandomStudentName(){
        Random rand = new Random();
        Integer index = rand.nextInt(studentNames.size());
        return studentNames.get(index);
    }
}
