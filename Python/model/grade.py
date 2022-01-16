class Grade:
    def __init__(self, course, letter_grade):
      self.letter_grade=letter_grade
      self.course_gpa=""
      self.course=course
      if letter_grade == "AA": 
        self.course_gpa = 4.0
      elif letter_grade == "BA":
        self.course_gpa = 3.5
      elif letter_grade == "BB": 
        self.course_gpa = 3.0
      elif letter_grade == "CB":
        self.course_gpa = 2.5
      elif letter_grade == "CC":
        self.course_gpa = 2.0
      elif letter_grade == "DC":
        self.course_gpa = 1.5
      elif letter_grade == "DD":
        self.course_gpa = 1.0
      elif letter_grade == "FD":
        self.course_gpa = 0.5
      else:
        self.course_gpa = 0.0

    def get_course_credit(self):
      return self.course.get_course_credit()

    def get_course_gpa(self):
      return self.course_gpa

    def get_letter_grade(self):
      return self.letter_grade

    def get_course(self):
      return self.course