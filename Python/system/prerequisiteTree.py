import logging
logging.basicConfig(filename='log.txt', encoding='utf-8', level=logging.DEBUG)

class PrerequisiteTree:
    def __init__(self, courses):
      self.course_tree = {} 
      self.courses = courses
      self.course_index = {}
      # CourseIndex will help us to construct the tree.
      # We will be able to get children classes of prerequisite classes easily.
      for i in range(len(self.courses)):
        self.course_index[self.courses[i].get_course_id()] = i
      self.construct_tree(self.courses)

    def construct_tree(self,courses):
      for i in range(len(courses)):
        self.course_tree[i] = []
        """
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
        """
      
      for i in range(len(courses)):
        if(self.check_if_mandatory_course(courses[i]) == False):
          continue
        child_courses = courses[i].get_prerequisites()
        if(child_courses == None):
          continue
        for child_course in child_courses:
          # courseTree[i].add(courseIndex.get(childCourse));
          self.course_tree[self.course_index[child_course]].append(i)
          logging.info("Course {0} requires {1} as prerequisite.".format(courses[i].course_id, child_course))
        # Each relationship is implemented with this single line of code.
    # Tree is ready.

    def fail_prerequisite_course(self,available_courses, prerequisite):
      # We have to apply Depth First Search algorithm to get all children courses
      # of this prerequisite course. We will use a stack.
      stack = []
      stack.append(prerequisite)
      while(len(stack)>0):
        current_course = stack.pop()
        for children_course in self.course_tree[current_course]:
          available_courses[children_course] = False
          # It is not available for the student anymore:( since he has failed from this
          # prerequisite course.
      # Done. Now he can't take children courses of that prerequisite course anymore...

    def check_if_mandatory_course(self, course):
      for check in self.courses:
        if(check.get_course_id() == course.get_course_id()):
            return True;
      return False

    def courses_student_can_take(self, student):
      """We will get available courses for the student.
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

        //Now, we also have to check prerequisite issues.
        //Who knows maybe the student failed a prerequisite class?
        //To check that, we use failPrerequisiteCourse() method."""
      available_courses = [True for i in range(len(self.courses))]
      
      # We will not list the courses the student has already added. So:
      added_courses = student.get_added_courses()
      for i in range(len(added_courses)):
        if(self.check_if_mandatory_course(added_courses[i]) == False):
          continue
        available_courses[self.course_index[added_courses[i].get_course_id()]] = False

      # Now we get the courses she failed.
      temp = student.get_failed_courses()
      # But we need the indexes of the these courses. So, create another array which is indexing all
      # these failed courses.
      failed_courses = []
      for course in temp:
        if(self.check_if_mandatory_course(course) == False):
          continue
        failed_courses.append(self.course_index[course.get_course_id()])
      
      for failed_course in failed_courses:
        self.fail_prerequisite_course(available_courses, failed_course)
        available_courses[failed_course] = True # Student should take FF and FD classes. Allow it.

      student_gpa = student.get_cgpa()
      
      courses_can_take = []
      for i in range(len(self.courses)):
        if(student.get_student_semester()%2 != int(self.courses[i].get_course_semester())%2):
          continue
        # Mandatory courses are only available in particular semesters.
        # If the semester of the student is fall and the semester of the course is spring,
        # then we mustn't allow the student to take this class.
        if(available_courses[i]==False):
          continue
        # Has prerequisite issue just skip it.
        if(int(self.courses[i].get_course_semester())>student.get_student_semester() and student_gpa<3):
          continue
        # Student with CGPA below 3 is not allowed to take classes from higher semesters.
        course_grade = student.get_course_grade(self.courses[i])
        if(course_grade!= None and course_grade != "DD" and course_grade != "DC" and course_grade != "FD" and course_grade != "FF"):
          continue
        # The student has already successfully passed this course. He cannot re-take that course!
        courses_can_take.append(self.courses[i])
      return courses_can_take