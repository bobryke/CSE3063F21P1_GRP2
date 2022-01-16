class CourseID:  
    def __init__(self,course_id):
      self.letter_count = 0
      for char in course_id:
        if char.isalpha():
          self.letter_count += 1

      self.department_code = course_id[0:self.letter_count]
      self.postfix_code = course_id[self.letter_count:]
            

    def to_string(self):
      return self.department_code + self.postfix_code
    
    def equals(self, rhs):
      return self.to_string() == rhs.to_string()

    def get_department_code(self):
      return self.department_code

    def get_postfix_code(self):
        return self.postfix_code