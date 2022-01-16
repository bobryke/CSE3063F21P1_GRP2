import json
import os
import random

from collections import OrderedDict
from model.course import Course

from model.advisor import Advisor
from system.prerequisiteTree import PrerequisiteTree

import logging
logging.basicConfig(filename='log.txt', encoding='utf-8', level=logging.DEBUG)

class University:
  def __init__(self):
      self.students = []
      self.courses = []
      self.nte_courses = []
      self.fte_courses = []
      self.te_courses = []
      self.advisors = []
      self.student_names = []
      self.student_transcripts = []
      self.semester = "fall"
      self.prerequisites = []

  def register_student(self,student):
    logging.info("Student {0} has been registered to university".format(student.get_student_id()))
    self.students.append(student)

  def hire_advisor(self,advisor):
    logging.info("Advisor {0} has been hired to university".format(advisor.advisor_name))
    self.advisors.append(advisor)

  def add_new_course(self,course):
    logging.info("Course {0} has been added to curriculum".format(course.course_id))
    self.courses.append(course)

  def read_all_input(self):
    input_file_path = os.path.join("Python","data", "input.json")
    f = open(input_file_path)
    input_file = json.loads(f.read())
    f.close()

    nte_courses=[]
    courses = []
    te_courses = []
    fte_courses = []
    prerequisites = []

    for key in input_file.keys():
        if(key == "semester"):
            self.semester = input_file[key]
        elif(key == "nonTechnicalElectiveCourses"):
            nte_courses = input_file[key]
        elif(key == "compulsoryCourses"):
            courses = input_file[key]
        elif(key == "technicalElectiveCourses"):
            te_courses = input_file[key]
        elif(key == "facultyTechnicalElectiveCourses"):
            fte_courses = input_file[key]
        elif(key == "prerequisites"):
            prerequisites = input_file[key]
        elif(key == "studentNames"):
            self.student_names =input_file[key]
    
    advisors = [
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
    ]
    for i in range(len(advisors)):
        self.hire_advisor(Advisor(advisors[i]))
    
    for course in courses:
      self.courses.append(Course(course["courseCode"],
                                  course["courseName"],
                                  course["courseType"],
                                  course["credit"],
                                  course["semester"],
                                  self.find_prerequisites(prerequisites, course["courseCode"]),
                                  course["quota"]))
      
    for course in fte_courses:
      self.fte_courses.append(Course(course["courseCode"],
                                  course["courseName"],
                                  course["courseType"],
                                  course["credit"],
                                  course["semester"],
                                  self.find_prerequisites(prerequisites, course["courseCode"]),
                                  course["quota"]))
    
    for course in nte_courses:
      self.nte_courses.append(Course(course["courseCode"],
                                  course["courseName"],
                                  course["courseType"],
                                  course["credit"],
                                  course["semester"],
                                  self.find_prerequisites(prerequisites, course["courseCode"]),
                                  course["quota"]))
    for course in te_courses:
      self.te_courses.append(Course(course["courseCode"],
                                  course["courseName"],
                                  course["courseType"],
                                  course["credit"],
                                  course["semester"],
                                  self.find_prerequisites(prerequisites, course["courseCode"]),
                                  course["quota"]))
    
  def find_prerequisites(self, courses, key_course):
    for course in courses:
      if course["courseCode"] == key_course:
        return course["prerequisite"]
    return None

  def write_whole_system(self, before_registration, department_output):
    postfix = None
    if (before_registration):
      postfix = "before.json"
    else:
      postfix = "after.json"

    for cur_student in self.students:
      output_json_path = os.path.join("Python","data", "student", cur_student.get_student_id() + postfix)
      temp_dict = OrderedDict({})
      """self.student_name = student_name
        self.student_advisor = student_advisor
        self.student_id = StudentID(student_id)
        self.student_semester = student_semester
        self.added_courses = []
        self.student_transcript = StudentTranscript(self)
        self.weekly_schedule = [[None for x in range(5)] for y in range(8)]  # should be course array
        self.actions = []"""
      temp_dict["studentName"] = cur_student.student_name
      temp_dict["studentID"] = cur_student.get_student_id()
      temp_dict["studentSemester"] = cur_student.student_semester
      temp_dict["studentAdvisor"] = cur_student.student_advisor.advisor_name
      temp_dict["gpa"] = cur_student.get_cgpa()
      temp_dict["weeklySchedule"] = ({"Monday": [], "Tuesday": [], "Wednesday": [], "Thursday": [], "Friday": []})
      for hours in cur_student.weekly_schedule:
        day = 1
        for course in hours:
          if course != None:
            if day == 1:
              temp_dict["weeklySchedule"]["Monday"].append(course.course_id)
            elif day == 2:
              temp_dict["weeklySchedule"]["Tuesday"].append(course.course_id)
            elif day == 3:
              temp_dict["weeklySchedule"]["Wednesday"].append(course.course_id)
            elif day == 4:
              temp_dict["weeklySchedule"]["Thursday"].append(course.course_id)
            elif day == 5:
              temp_dict["weeklySchedule"]["Friday"].append(course.course_id)
          day += 1
      temp_dict["letterGrades"] = {}
      for grade in cur_student.student_transcript.get_letter_grades():
        temp_dict["letterGrades"][grade.course.course_id] = grade.letter_grade
      
      with open(output_json_path, 'w') as outfile:
        json.dump(temp_dict, outfile)

    if(before_registration == False):
      output_results_path = os.path.join("Python","data", "department", "department_output.txt")
      with open(output_results_path, 'w') as outfile:
        outfile.write(department_output)

  def get_student_by_id(self, student_id):
    for student in self.students:
      if(student.get_student_id() == student_id):
        return student
    return None

  def get_course_by_id(self, course_id):
    for course in self.courses:
      if(course.get_course_id() == course_id):
        return course
    return None

  def get_all_courses(self):
    return self.courses

  def get_all_te_courses(self):
    return self.te_courses

  def get_all_fte_courses(self):
    return self.fte_courses

  def get_all_nte_courses(self):
    return self.nte_courses

  def get_all_students(self):
    return self.students

  def get_random_fte_course(self):
    return self.fte_courses[random.randint(0, len(self.fte_courses)-1)]

  def get_random_te_course(self):
    return self.te_courses[random.randint(0, len(self.te_courses)-1)]

  def get_random_nte_course(self):
    return self.nte_courses[random.randint(0, len(self.nte_courses)-1)]

  def get_semester(self):
    return self.semester

  def get_random_advisor(self):
    return self.advisors[random.randint(0, len(self.advisors)-1)]

  def get_random_student_name(self):
    return self.student_names[random.randint(0, len(self.student_names)-1)]