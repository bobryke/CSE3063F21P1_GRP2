package system;

import model.Course;
import model.Student;

import java.util.*;

public class PrerequisiteTree {

    private ArrayList<Integer>[] courseTree;
    private ArrayList<Course> courses;
    private Map<String,Integer> courseIndex;
    //CourseIndex will help us to construct the tree.
    //We will be able to get children classes of prerequisite classes easily.

    public PrerequisiteTree(ArrayList<Course> courses){
        this.courses = courses;
        courseIndex = new HashMap<>();
        for(int i=0;i<courses.size();i++)
            courseIndex.put(courses.get(i).getCourseID(),i);

        constructTree(courses);
    }

    private void constructTree(ArrayList<Course> courses){
        Integer n = courses.size();
        courseTree = new ArrayList[n];
        for(int i=0;i<n;i++){
            courseTree[i] = new ArrayList<Integer>();
        }
        /*
        Here is the logic of that tree. Assume courseIndex array is as follows:
        0> CSE2025
        1> CSE2046
        2> CSE3055
        3> CSE3033
        4> CSE3044
        ...

        Then we would have a tree as follows:
        courseTree[0]={1,2,3} //It indicates, CSE2025 is prerequisite of CSE2046,CSE3055,CSE3033
        courseTree[1]={}
        courseTree[2]={4}
        courseTree[3]={}
        courseTree[4]={}
        ...
        So we need courseIndex array to indicate relations between the courses.
         */
        for(int i=0;i<n;i++){
            ArrayList<String> childCourses = courses.get(i).getPrerequisites();
            for(String childCourse: childCourses){
                //courseTree[i].add(courseIndex.get(childCourse));
                courseTree[courseIndex.get(childCourse)].add(i);
                //Each relationship is implemented with this single line of code.
            }
        }
        //Tree is ready.
    }

    public void failPrerequisiteCourse(Boolean[] availableCourses, Integer prerequisite){
        //We have to apply Depth First Search algorithm to get all children courses
        //of this prerequisite course. We will use a stack.
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(prerequisite);
        while(!stack.empty()){
            Integer currentCourse = stack.pop();
            for(Integer childrenCourse:courseTree[currentCourse]){
                availableCourses[childrenCourse] = false;
                //It is not available for the student anymore:( since he has failed from this
                //prerequisite course.
            }
        }
        //Done. Now he can't take children courses of that prerequisite course anymore...
    }

    public ArrayList<Course> coursesStudentCanTake(Student student){
        /*
        We will get available courses for the student.
        To do that, we will consider prerequisite tree and also student's CGPA.

        If the student has CGPA higher than 3, he can also add courses from higher semester.
        BUT...
        IN OUR SIMULATION WE HAVE ASSUMED MANDATORY COURSES ARE ONLY AVAILABLE FOR A
        PARTICULAR SEMESTER. SO, CSE3063 WILL ONLY BE LISTED IN FALL SEMESTER AND IT
        WILL NEVER BE LISTED IN SPRING SEMESTER.

        In Marmara University, a student can retake classes with latter grades DC and DD.
        But approval of the advisor is needed. We will allow student to add these DD and DC
        classes if she wants to do so. But, advisor will say the final word.
        We will generate a random integer. If it is divisible by two then, the advisor will allow
        student to take these DD and DC classes. Otherwise, student will not be able to take
        these DD,DC classes.

        Little not that a student with CGPA lower than 1.8 MUST retake these DD and DC classes.
        Later, the registration system will handle this condition.
         */

        //Now, we also have to check prerequisite issues.
        //Who knows maybe the student failed a prerequisite class?
        //To check that, we use failPrerequisiteCourse() method.
        Integer numOfCourses = courses.size();
        Boolean[] availableCourses = new Boolean[numOfCourses];
        for(int i=0;i<numOfCourses;i++){
            availableCourses[i] = true;
        }

        //We will not list the courses the student has already added. So:
        ArrayList<Course> addedCourses = student.getAddedCourses();
        for(int i=0;i<addedCourses.size();i++){
            availableCourses[courseIndex.get(addedCourses.get(i).getCourseID())] = false;
        }

        //Now we get the courses she failed.
        ArrayList<Course> temp = student.getFailedCourses();
        //But we need the indexes of the these courses. So, create another array which is indexing all
        //these failed courses.
        ArrayList<Integer> failedCourses = new ArrayList<Integer>();
        for(Course course:temp) {
            if(course.getCourseType().equals("Elective")){continue;}
            failedCourses.add(courseIndex.get(course.getCourseID()));
        }

        for(Integer failedCourse:failedCourses){
            failPrerequisiteCourse(availableCourses,failedCourse);
            availableCourses[failedCourse] = true; //Student should take FF and FD classes. Allow it.
        }

        //Now it is time to get the list of all the courses made available by the system.
        Double studentCGPA = student.getCGPA();
        ArrayList<Course> coursesCanTake = new ArrayList<Course>();
        for(int i=0;i<numOfCourses;i++){
            if(student.getStudentSemester()%2!=courses.get(i).getCourseSemester()%2){continue;}
            //Mandatory courses are only available in particular semesters.
            //If the semester of the student is fall and the semester of the course is spring,
            //then we mustn't allow the student to take this class.
            if(availableCourses[i]==false){continue;}
            //Has prerequisite issue just skip it.
            if(courses.get(i).getCourseSemester()>student.getStudentSemester() && studentCGPA<3){continue;}
            //Student with CGPA below 3 is not allowed to take classes from higher semesters.
            String courseGrade = student.getCourseGrade(courses.get(i));
            if(courseGrade!=null && courseGrade.equals("DD")==false &&
                    courseGrade.equals("DC")==false && courseGrade.equals("FD")==false &&
                    courseGrade.equals("FF")==false){continue;}
            //The student has already successfully passed this course. He cannot re-take that course!
            coursesCanTake.add(courses.get(i));
        }
        return coursesCanTake;
    }
}
