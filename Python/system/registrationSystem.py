import random
from turtle import st
from system.university import University
from system.prerequisiteTree import PrerequisiteTree
from model.student import Student


import logging
logging.basicConfig(filename='log.txt', encoding='utf-8', level=logging.DEBUG)

class RegistrationSystem:
    def __init__(self):
        self.students2020 = 0
        self.students2019 = 0
        self.students2018 = 0
        self.students2017 = 0
        self.university = University()
        self.prerequisite_tree = None
        self.semester = None
        self.create_everything()
        self.logged_student = None # Currently, no student is logged on.

    def create_everything(self):
        self.university.read_all_input()
        logging.info("Input file is read.")

        self.prerequisite_tree = PrerequisiteTree(self.university.get_all_courses())
        logging.info("Prerequisitie Tree is created.")
        # Now the tree has been constructed. We can use it to detect prerequisite course issues.

        # Randomly create all the students and add them to the university.
        # University part will take the rest, it will create a json file for each randomly created student.
        # Create randomly 270 students.
        for i in range(0, 270):
            student = self.create_random_student()
            self.university.register_student(student)


    def create_random_student(self):
        """The following code for the createRandomStudent may seem too long. But when you observe
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
        All we have to do is to generate a random number."""
        student_semester = 0

        if(self.semester == "Spring"):
            student_semester = 2*(random.randint(1, 4))
        else:
            student_semester = (2*(random.randint(0, 3)))+1
        """Now, we know her semester. It is time to assign some letter grades.
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
        probability."""

        all_courses = self.university.get_all_courses()
        num_of_courses = len(all_courses)
        
        student_letter_grades = []
        available_courses = []

        # if availableCourses[i]==false then student can't take that course.
        # We will use that array to determine prerequisite course's children courses.

        # We first assume that the student has successfully passed all the courses until his semester.
        # And then we make student fail from some of these classes.
        # We will get help of the prerequisite tree while doing that.

        for i in range(num_of_courses):
            if(int(all_courses[i].get_course_semester()) < student_semester):
                available_courses.append(True)
            else:
                available_courses.append(False)

        for i in range(num_of_courses):
            if(available_courses[i]):
                # In our simulation we assume standard deviation of the students points is 9
                # In our simulation we assume mean of the points of students are 78
                course_gpa = random.gauss(70, 30)
                if(course_gpa<45.0):
                    student_letter_grades.append("FF")
                elif (course_gpa<50.0):
                    student_letter_grades.append("FD")
                elif (course_gpa<55.0):
                    student_letter_grades.append("DD")
                elif (course_gpa<65.0):
                    student_letter_grades.append("DC")
                elif (course_gpa<75.0):
                    student_letter_grades.append("CC")
                elif (course_gpa<80.0):
                    student_letter_grades.append("CB")
                elif (course_gpa<85.0):
                    student_letter_grades.append("BB")
                elif (course_gpa<90.0):
                    student_letter_grades.append("BA")
                else:
                    student_letter_grades.append("AA")

                
                if(student_letter_grades[-1] == "FF" or student_letter_grades[-1] == "FD"):
                    # //In our simulation, we prevent a student failing from a course given
                    # //two years before his semester.
                    # //Otherwise, student would be one year behind the semester we selected.
                    # //Imagine a seventh year student's ProgrammingI course grade, was randomly assigned as FF.
                    # //That mustn't be the case, since that student wouldn't even be second grade in that situation.
                    # //We should prevent this by giving student DD instead.
                    if (student_semester - int(all_courses[i].get_course_semester()) > 4):
                         student_letter_grades[-1] = "DD"
                    else:
                        failed_course = i
                        # //Now here is the problem:
                        # //What if the course we assigned as FF is and prerequisite course?
                        # //We shouldn't allow student to take subclasses of this prerequisite course.
                        # //We get help of the prerequisiteTree object.
                        self.prerequisite_tree.fail_prerequisite_course(available_courses, failed_course);
                        # //Done. With this single line of code we made children courses of this
                        # //prerequisite course unavailable for that student.
                        available_courses[i] = True;
        # //Now we have to generate student number for the student.
        # //We have to consider semester of the student.
        # //If the student in the first and second semester, then her number will be like 150120...
        # //If the student in the third and fourth semester, then her number will be like 150119...
        # //If the student in the fifth and sixth semester, then her number will be like 150118...
        # //If the student in the seventh and eighth semester, then her number will be like 150117...
        # //Of course, this condition may not hold for all students.
        # //But in general the pattern is like this.
        student_id = None
        if(student_semester==1 or student_semester==2):
            student_id = str(150120000 + self.students2020) 
            self.students2020 += 1
        elif(student_semester==3 or student_semester==4):
            student_id = str(150119000 + self.students2019)
            self.students2019 += 1
        elif(student_semester==5 or student_semester==6):
            student_id = str(150118000 + self.students2018)
            self.students2018 += 1
        else:
            student_id = str(150117000 + self.students2017)
            self.students2017 += 1

        student = Student(self.university.get_random_student_name(), student_id, student_semester, self.university.get_random_advisor())

        # //Now we make student pass from the assigned classes.
        for i in range(len(student_letter_grades)):
            if(student_letter_grades[i] != None and available_courses[i]):
                # //If this course NTE, assign a non-technical elective course randomly.
                # //If this course FTE, assign a faculty technical elective course randomly.
                # //If this course TE, assign a technical elective course randomly.
                if(all_courses[i].get_course_type() == "Elective"):
                    course_code = all_courses[i].get_course_id()
                    technical_type = str(course_code)[0:2]
                    if(technical_type == ("NTE")):
                        student.pass_course(self.university.get_random_nte_course(), student_letter_grades[i])
                    elif(technical_type == ("FTE")):
                        student.pass_course(self.university.get_random_fte_course(), student_letter_grades[i])
                    else:
                        student.pass_course(self.university.get_random_te_course(), student_letter_grades[i])
                else:
                    student.pass_course(all_courses[i], student_letter_grades[i])
        return student

    def get_available_courses(self, student):
        # In this method, we randomly assign NTE,FTE,TE courses for the student.
        # Notice that this prerequisiteTree only returns the courses the student
        # can take. This courses maybe general types such as
        # NTE,FTE,TE. So, we handle these general types in this function.
        available_courses = self.prerequisite_tree.courses_student_can_take(student)
        for i in range(len(available_courses)):
            if(available_courses[i].get_course_type() == "Elective"):
                course_code = available_courses[i].get_course_id()
                technical_type = str(course_code)[0:2]
                if(technical_type == "NTE"):
                    available_courses[i] = self.university.get_random_nte_course()
                if(technical_type == "FTE"):
                    available_courses[i] = self.university.get_random_fte_course()
                else:
                    available_courses[i] = self.university.get_random_te_course()
        return available_courses

    def batch_process(self):
        self.university.write_whole_system(True, "")
        """OUR COURSE REGISTRATION SYSTEM MAKES STUDENT REGISTER
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

        To get the outputs above, we should keep these 2 map and this string array below."""
        quoata_problems = {}
        overlap_problems = {}
        advisor_problems = []

        all_student = self.university.get_all_students()
        for cur_student in all_student:
            available_courses = self.get_available_courses(cur_student)
            selected = []
            for i in range(len(available_courses)):
                selected.append(False)

            num_of_classes_selected = 4 + random.randint(0, 4)

            for count in range(num_of_classes_selected):
                i = random.randint(0, len(available_courses)-1)
                selected[i] = True

            for i in range(len(available_courses)):
                if(selected[i] == False):
                    continue

                check = self.send_selection_system_verification(available_courses[i], cur_student)
                if(check != "success"):
                    # Found a problem... Report it and don't add this course to the list..
                    cur_student.add_action(check)
                    logging.warning("Student {0} failed registered to the course {1}.".format(cur_student.get_student_id(), available_courses[i].get_course_id()))
                    if(check[1] == "Q"):
                        key = str(available_courses[i].get_course_id())
                        students_with_issues = []
                        if (key in quoata_problems.keys()):
                            students_with_issues = quoata_problems[key]
                        students_with_issues.append(cur_student.get_student_id())
                        quoata_problems[key] = students_with_issues

                    else:
                        key = str(available_courses[i].get_course_id())
                        students_with_issues = []
                        if (key in overlap_problems.keys()):
                            students_with_issues = overlap_problems[key]
                        students_with_issues.append(cur_student.get_student_id())
                        overlap_problems[key] = students_with_issues
                else:
                    cur_student.add_course(available_courses[i])
                    logging.info("Student {0} successfully registered to the course {1}.".format(cur_student.get_student_id(), available_courses[i].get_course_id()))

            selected_courses = cur_student.get_added_courses()

            # Advisor verification:
            check = self.send_selection_advisor_verification(selected_courses, cur_student)
            if(check[0] == "["): # Advisor didn't approve...
                advisor_problems.append(cur_student.get_student_id())  

        department_output = []
        department_output.append("Total number of students is: " + str(len(self.university.get_all_students()))+ "\n\n")
        all_courses = self.university.get_all_courses()
        for course in all_courses:
            key = course.get_course_id()
            if(key in overlap_problems):
                department_output.append("The following " + str(len(overlap_problems[key])))
                department_output.append(" students were unable to take "+course.get_course_id()+" course due " + "to the course overlaps.\n")
                department_output.append(str(overlap_problems[key]))
                department_output.append("\n\n")
            if(key in quoata_problems):
                department_output.append("The following "+ str(len(quoata_problems[key])))
                department_output.append(" students were unable to take "+course.get_course_id()+" course due " + "to the course overlaps.\n")
                department_output.append(str(quoata_problems[key]))
                department_output.append("\n\n")
        department_output.append("The course selection of the following "+ str(len(advisor_problems))+ " students " + "remained in the draft because their advisors did not approve.\n");
        department_output.append(str(advisor_problems))
        department_output.append("\n\n")
        self.university.write_whole_system(False, " ".join(department_output))


    def send_selection_system_verification(self, course, student):
        if(course.is_quota_full()):
            return "[Q][VERIFICATION-FAILED]:The system didnt allow student " + student.get_student_id()+ " to take " + course.get_course_id() + " because quota is full."

        overlap_check = student.check_course_overlap(course)
        if(overlap_check != "success"):
            return "[O]"+overlap_check
        else:
            return "success"

    def send_selection_advisor_verification(self, selected_courses, student):
        return student.get_student_advisor().verify_student_registration(student, selected_courses)