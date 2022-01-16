import random

class Course:
    def __init__(self, course_id, course_full_name, course_type, course_credit, course_semester, prerequisites, quota):
        self.registered_students = 0
        self.course_semester = course_semester
        self.prerequisites = prerequisites
        self.course_id = course_id
        self.course_full_name = course_full_name
        self.course_type = course_type
        self.course_credit = course_credit
        self.quota = quota
        self.lecture_hours = [[False for j in range(5)] for x in range(8)]

        # Now at this point, we will assign random lecture hours for this course.
        # We will do that according to the course credit.
        # The more credits the course has, the more the course hours.

        self.course_hours = 0
        if self.course_credit > 5:
            self.course_hours = 5
        elif self.course_credit > 3:
            self.course_hours = 3
        else:
            self.course_hours = 2

        for i in range(self.course_hours):
            hour = random.randint(0,7)  
            day = random.randint(0,4)
            self.lecture_hours[hour][day] = True

    def add_student(self):
        self.registered_students += 1

    def get_registered_students(self):
        return self.registered_students

    def get_course_id(self):
        return self.course_id

    def get_course_full_name(self):
        return self.course_full_name

    def get_course_type(self):
        return self.course_type

    def get_course_semester(self):
        return self.course_semester

    def get_course_credit(self):
        return self.course_credit

    def get_lecture_hours(self):
        return self.lecture_hours

    def get_prerequisites(self):
        return self.prerequisites

    def is_quota_full(self):
        return self.registered_students == self.quota

    def check_if_prerequisite(self, course):
        for prerequisite in self.prerequisites:
            if prerequisite == course.get_course_id():
                return True
        return False
