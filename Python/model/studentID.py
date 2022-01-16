class StudentID:
    def __init__(self, student_id):
        self.department_code = student_id[0:3]
        self.year_code = student_id[3:6]
        self.postfix_code = student_id[6:]
    
    def to_string(self):
       return self.department_code + self.year_code + self.postfix_code;

    def equals(self,rhs):
      return self.to_string == rhs.to_string()

    def get_department_code(self):
      return self.department_code

    def get_year_code(self):
      return self.year_code

    def get_postfix_code(self):
      return self.postfix_code