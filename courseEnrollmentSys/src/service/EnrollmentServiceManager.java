package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Course;
import model.Enrollment;
import model.Student;

public class EnrollmentServiceManager {
    private static final String COURSE_FILE = "data/courses.txt";
    private static final String STUDENT_FILE = "data/students.txt";
    private static final String ENROLLMENT_FILE = "data/enrollments.txt";
    private static final String REPORT_FILE = "data/reports.txt";

    private FileService fileService;
    private CourseService courseService;
    private StudentService studentService;
    private EnrollmentService enrollmentService;

    public EnrollmentServiceManager() {
        fileService = new FileService();
        courseService = new CourseService();
        studentService = new StudentService();
        enrollmentService = new EnrollmentService(courseService, studentService);
        loadAllData();
    }

    public CourseService getCourseService() {
        return courseService;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public EnrollmentService getEnrollmentService() {
        return enrollmentService;
    }

    public void loadAllData() {
        loadCourses();
        loadStudents();
        loadEnrollments();
        rebuildCountsAndStudentCourseLists();
    }

    public void saveAllData() {
        saveCourses();
        saveStudents();
        saveEnrollments();
    }

    public void saveReport(String reportText) {
        List<String> lines = Arrays.asList(reportText.split("\\n"));
        fileService.writeLines(REPORT_FILE, lines);
    }

    private void loadCourses() {
        List<String> lines = fileService.readLines(COURSE_FILE);
        List<Course> courses = new ArrayList<Course>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length < 4) {
                continue;
            }

            int max = parseInt(parts[3], 0);
            int current = parts.length >= 5 ? parseInt(parts[4], 0) : 0;
            courses.add(new Course(parts[0], parts[1], parts[2], max, current));
        }

        courseService.setCourses(courses);
    }

    private void loadStudents() {
        List<String> lines = fileService.readLines(STUDENT_FILE);
        List<Student> students = new ArrayList<Student>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length < 3) {
                continue;
            }

            List<String> enrolledIds = new ArrayList<String>();
            if (parts.length >= 4 && !parts[3].trim().isEmpty()) {
                String[] ids = parts[3].split(",");
                for (String id : ids) {
                    if (!id.trim().isEmpty()) {
                        enrolledIds.add(id.trim());
                    }
                }
            }

            students.add(new Student(parts[0], parts[1], parts[2], enrolledIds));
        }

        studentService.setStudents(students);
    }

    private void loadEnrollments() {
        List<String> lines = fileService.readLines(ENROLLMENT_FILE);
        List<Enrollment> enrollments = new ArrayList<Enrollment>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length < 3) {
                continue;
            }

            enrollments.add(new Enrollment(parts[0], parts[1], parts[2]));
        }

        enrollmentService.setEnrollments(enrollments);
    }

    private void saveCourses() {
        List<String> lines = new ArrayList<String>();
        for (Course course : courseService.getAllCourses()) {
            lines.add(course.toFileString());
        }
        fileService.writeLines(COURSE_FILE, lines);
    }

    private void saveStudents() {
        List<String> lines = new ArrayList<String>();
        for (Student student : studentService.getAllStudents()) {
            lines.add(student.toFileString());
        }
        fileService.writeLines(STUDENT_FILE, lines);
    }

    private void saveEnrollments() {
        List<String> lines = new ArrayList<String>();
        for (Enrollment enrollment : enrollmentService.getAllEnrollments()) {
            lines.add(enrollment.toFileString());
        }
        fileService.writeLines(ENROLLMENT_FILE, lines);
    }

    private void rebuildCountsAndStudentCourseLists() {
        for (Course course : courseService.getAllCourses()) {
            course.setCurrentEnrollmentCount(0);
        }

        for (Student student : studentService.getAllStudents()) {
            student.getEnrolledCourseIds().clear();
        }

        for (Enrollment enrollment : enrollmentService.getAllEnrollments()) {
            Course course = courseService.findCourseById(enrollment.getCourseId());
            Student student = studentService.findStudentById(enrollment.getStudentId());

            if (course != null) {
                course.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount() + 1);
            }

            if (student != null && !student.getEnrolledCourseIds().contains(enrollment.getCourseId())) {
                student.getEnrolledCourseIds().add(enrollment.getCourseId());
            }
        }
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            return fallback;
        }
    }
}
