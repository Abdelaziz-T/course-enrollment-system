package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private List<String> enrolledCourseIds;

    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrolledCourseIds = new ArrayList<String>();
    }

    public Student(String studentId, String name, String email, List<String> enrolledCourseIds) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.enrolledCourseIds = enrolledCourseIds;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEnrolledCourseIds() {
        return enrolledCourseIds;
    }

    public void setEnrolledCourseIds(List<String> enrolledCourseIds) {
        this.enrolledCourseIds = enrolledCourseIds;
    }

    public String getEnrolledCoursesAsString() {
        if (enrolledCourseIds == null || enrolledCourseIds.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < enrolledCourseIds.size(); i++) {
            builder.append(enrolledCourseIds.get(i));
            if (i < enrolledCourseIds.size() - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public String toFileString() {
        return studentId + "|" + name + "|" + email + "|" + getEnrolledCoursesAsString();
    }
}
