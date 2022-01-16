import random

import logging
logging.basicConfig(filename='log.txt', encoding='utf-8', level=logging.DEBUG)

class Advisor:
    def __init__(self, advisor_name):
        self.advisor_name = advisor_name

    def check_credit_regulation(self,student,courses):
      sum_credits = 0
      for course in courses:
          sum_credits += course.get_course_credit()
          
      if sum_credits > 35:
          return False
      else:
          return True
        
    def verify_student_registration(self,student,courses):
      problems_occurred = []
      verification_failed = "[VERIFICATION-FAILED]:The advisor "

      if self.check_credit_regulation(student,courses)==False:
        answer = verification_failed + self.advisor_name + " didnt approve" + " the registration of the student " + student.get_student_id() + " because the credits taken this semester shouldnt exceed 35."
        student.add_action(answer)
        return answer

      course_with_d = False;
      courses_higher_semesters = 0;
      for i in range(len(courses)):
        if (courses[i].get_course_id() == "CSE4197"):
          if (student.completed_credits()<165):
            reply = (verification_failed + self.advisor_name +
                            " didn't approve the registration of the student " + student.get_student_id() +
                            ". Because completed credits must be >=165 to take "+courses[i].get_course_id()+".")
            logging.warning(reply)
            problems_occurred.append(reply)
            student.add_action(reply)
        elif(courses[i].get_course_type() == "Elective" and courses[i].get_course_credit()==5):
          if (student.completed_credits()<155):
            reply = (verification_failed + self.advisor_name +
                            " didnt approve the registration of the student " + student.get_student_id() +
                            ". Because completed credits must be >=155 to take "+ courses[i].get_course_id()+".")
            problems_occurred.append(reply)
            logging.warning(reply)
            student.add_action(reply)

          letter_grade = student.get_course_grade(courses[i])
          if (letter_grade != None and letter_grade == "DD" or letter_grade == "DC"):
              course_with_d = True
            
          if((student.get_student_semester() < courses[i].get_course_semester()) and courses_higher_semesters+1 > 3):
            reply = (verification_failed + self.advisor_name +
                          " didnt approve the registration of the student " + student.get_student_id() +
                          ". A student cannot take more than three classes from higher semesters.")
            problems_occurred.append(reply)
            student.add_action(reply)
            logging.warning(reply)
      if(course_with_d and student.get_cgpa()>1.8):
        rand = random.randint(0,3)
        if(rand%3==0):
          reply = (verification_failed + self.advisor_name +
                          " didnt approve the registration of the student" + student.get_student_id() +
                          ". Advisor didnt allow student to re-take DD or DC classes.")
          problems_occurred.append(reply)
          student.add_action(reply)
          logging.warning(reply)
      if(len(problems_occurred)!=0):
        index = random.randint(0, len(problems_occurred)-1)
        return problems_occurred[index]
      else:
        response = "The advisor " + self.advisor_name + " approved the registration of the student " +student.get_student_id()+"."
        logging.info(response)
        return response
    
