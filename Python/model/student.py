import json
from model.studentID import StudentID
from model.studentTranscript import StudentTranscript

class Student:
    def __init__(self, student_name, student_id, student_semester, student_advisor):
        self.student_name = student_name
        self.student_advisor = student_advisor
        self.student_id = StudentID(student_id)
        self.student_semester = student_semester
        self.added_courses = []
        self.student_transcript = StudentTranscript(self)
        self.weekly_schedule = [[None for x in range(5)] for y in range(8)]  # should be course array
        self.actions = []
            
        """ IN THIS WEEKLY SCHEDULE;
        Columns indicates
        0->09:30-10:20
        1->10:30-11:20
        2->11:30-12:20
        3->13:00-13:50
        4->14:00-14:50
        5->15:00-15:50
        6->16:00-16:50
        7->17:00-17:50

        Rows indicates:
        0->Monday
        1->Tuesday
        2->Wednesday
        ...
        4->Friday"""

    def get_student_transcript(self):
        return self.student_transcript

    def print_student_transcript(self):
        self.student_transcript.print_student_transcript()

    def get_course_grade(self, course):
        return self.student_transcript.get_course_grade(course)

    def add_course(self, course):
        self.added_courses.append(course)

        lecture_hours = course.get_lecture_hours()
        for i in range(8):
            for j in range(5):
                if lecture_hours[i][j]:
                    self.weekly_schedule[i][j] = course

        course.add_student()

    def pass_course(self, course, letter_grade):
        self.student_transcript.pass_course(course, letter_grade)

    def check_course_overlap(self, course):
        lecture_hours = course.get_lecture_hours()
        overlaps = []
        for i in range(8):
            for j in range(5):
                if lecture_hours[i][j] and self.weekly_schedule[i][j] != None:
                    overlaps.append(self.weekly_schedule[i][j].get_course_id())

        if len(overlaps) > 1:
            answer = "[VERIFICATION-FAILED]:The system didn't allow " + str(self.student_id) + " to add " + course.get_course_id() + " because of " + str(len(overlaps)) + " hours of collision with."
            i = 0
            for overlap in overlaps:
                answer = answer + overlap
                i = + 1
                if i != len(overlaps):
                    answer = answer + overlap
            return answer
        return "success"

    def get_student_name(self):
        print("here")
        return self.student_name

    def get_student_id(self):
        return self.student_id.to_string()

    def get_weekly_schedule(self):
        return self.weekly_schedule

    def get_student_advisor(self):
        return self.student_advisor

    def get_added_courses(self):
        return self.added_courses

    def get_failed_courses(self):
        failed_courses = []
        grades = self.student_transcript.letter_grades

        for student_grade in grades:
            if (student_grade.get_letter_grade() == "FF") or (student_grade.get_letter_grade() == "FD"):
                failed_courses.append(student_grade.get_course())
        return failed_courses

    def get_student_semester(self):
        return self.student_semester

    def get_cgpa(self):
        return self.student_transcript.get_cgpa()

    def get_actions(self):
        return self.actions

    def add_action(self, action):
        self.actions.append(action)

    def completed_credits(self):
        return self.student_transcript.completed_credits()

    def to_json(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)