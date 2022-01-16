from tkinter.messagebox import NO
from model.grade import Grade

class StudentTranscript:
  def __init__(self,student):
      self.student = student
      self.letter_grades = []

  def pass_course(self, course,letter_grade):
    self.letter_grades.append(Grade(course, letter_grade))

  def completed_credits(self):
    answer = 0
    for grade in self.letter_grades:
      if(grade.get_letter_grade() == "FF" or grade.get_letter_grade() == "FD"):
        continue
      answer += grade.get_course_credit()
    return answer

  def get_cgpa(self):
    sum_of_credits = 0
    answer = 0.0
    for grade in self.letter_grades:
      sum_of_credits += grade.get_course_credit()
      answer = answer + (float(grade.get_course_credit())) * grade.get_course_gpa()

    if(sum_of_credits==0):
      return 0.0
      # This student is a new university student and hasn't taken any course yet. So her CGPA is assumed to be zero.
    answer /= sum_of_credits
    return answer

  def get_course_grade(self, course):
      for grade in self.letter_grades:
        if(grade.get_course().get_course_id() == course.get_course_id()):
          return grade.get_letter_grade()
      return None

  def get_letter_grades(self):
    return self.letter_grades

  def print_student_info_line(self,line):
    for i in range(0, 59):
      if(i < len(line)):
        print(line[i])
      else:
        print(" ")
    print("|")

  def print_course_info_line(self, code, course_name, credit, grade):
    first = "| " + code
    for i in range(0, 10):
      if(i < len(first)):
        print(first[i])
      else:
        print(" ")

    second = "| " + course_name
    for i in range(0, 26):
      if(i < len(second)):
        print(second[i])
      else:
        print(" ")

    third = "| " + credit
    for i in range(0, 11):
      if(i < len(third)):
        print(third[i])
      else:
        print(" ")
    
    fourth = "| " + grade
    for i in range(0, 12):
      if(i < len(fourth)):
        print(fourth[i])
      else:
        print(" ")
    print("|")

  def print_student_transcript(self):
    print("____________________________________________________________");
    print("|                                                          |");
    print("|                     TRANSCRIPT                           |");
    self.print_student_info_line("|  Student Name: "+self.student.get_student_name())
    self.print_student_info_line("|  Student ID: "+self.student.get_student_id())
    self.print_student_info_line("|  Student CGPA: "+self.student.get_cgpa())
    self.print_student_info_line("|  Student Semester: "+self.student.getStudentSemester())
    print("____________________________________________________________")
    print("| Code    |   Course Name           | Credit   | Grade     |")
    print("____________________________________________________________")
    for i in range(len(self.letter_grades)):
            course = self.letter_grades[i].get_course();
            self.print_course_info_line(course.get_course_id(), course.get_course_full_name(), str(course.get_course_credit()), self.letter_grades[i].get_letter_grade())
