package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.Enrollment;
import model.Student;
import util.ValidationUtil;

public class EnrollmentService {
    private List<Enrollment> enrollments;
    private CourseService courseService;
    private StudentService studentService;

    public EnrollmentService(CourseService courseService, StudentService studentService) {
        this.enrollments = new ArrayList<Enrollment>();
        this.courseService = courseService;
        this.studentService = studentService;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public String enrollStudent(String studentId, String courseId) {
        if (ValidationUtil.isNullOrEmpty(studentId) || ValidationUtil.isNullOrEmpty(courseId)) {
            return "Student ID and Course ID are required.";
        }

        Student student = studentService.findStudentById(studentId);
        if (student == null) {
            return "Student ID not found.";
        }

        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            return "Course ID not found.";
        }

        if (isStudentEnrolledInCourse(studentId, courseId)) {
            return "Student is already enrolled in this course.";
        }

        if (course.isFull()) {
            return "Course is already full.";
        }

        Enrollment enrollment = new Enrollment(studentId.trim(), courseId.trim(), LocalDate.now().toString());
        enrollments.add(enrollment);

        student.getEnrolledCourseIds().add(course.getCourseId());
        course.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount() + 1);

        return "Student enrolled successfully.";
    }

    public String unenrollStudent(String studentId, String courseId) {
        Enrollment enrollment = findEnrollment(studentId, courseId);
        if (enrollment == null) {
            return "Enrollment not found.";
        }

        enrollments.remove(enrollment);

        Student student = studentService.findStudentById(studentId);
        if (student != null) {
            student.getEnrolledCourseIds().removeIf(id -> id.equalsIgnoreCase(courseId));
        }

        Course course = courseService.findCourseById(courseId);
        if (course != null && course.getCurrentEnrollmentCount() > 0) {
            course.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount() - 1);
        }

        return "Student unenrolled successfully.";
    }

    public List<Student> getStudentsInCourse(String courseId) {
        List<Student> students = new ArrayList<Student>();

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourseId().equalsIgnoreCase(courseId)) {
                Student student = studentService.findStudentById(enrollment.getStudentId());
                if (student != null) {
                    students.add(student);
                }
            }
        }

        return students;
    }

    public void removeStudentFromAllEnrollments(String studentId) {
        List<Enrollment> toRemove = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equalsIgnoreCase(studentId)) {
                toRemove.add(enrollment);
            }
        }

        for (Enrollment enrollment : toRemove) {
            unenrollStudent(enrollment.getStudentId(), enrollment.getCourseId());
        }
    }

    public void removeCourseFromAllEnrollments(String courseId) {
        List<Enrollment> toRemove = new ArrayList<Enrollment>();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourseId().equalsIgnoreCase(courseId)) {
                toRemove.add(enrollment);
            }
        }

        for (Enrollment enrollment : toRemove) {
            unenrollStudent(enrollment.getStudentId(), enrollment.getCourseId());
        }
    }

    public boolean isStudentEnrolledInCourse(String studentId, String courseId) {
        return findEnrollment(studentId, courseId) != null;
    }

    private Enrollment findEnrollment(String studentId, String courseId) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equalsIgnoreCase(studentId.trim())
                    && enrollment.getCourseId().equalsIgnoreCase(courseId.trim())) {
                return enrollment;
            }
        }

        return null;
    }
}
