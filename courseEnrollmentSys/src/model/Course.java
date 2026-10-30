package model;

public class Course {
    private String courseId;
    private String courseName;
    private String instructorName;
    private int maxCapacity;
    private int currentEnrollmentCount;

    public Course(String courseId, String courseName, String instructorName, int maxCapacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.maxCapacity = maxCapacity;
        this.currentEnrollmentCount = 0;
    }

    public Course(String courseId, String courseName, String instructorName, int maxCapacity, int currentEnrollmentCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.maxCapacity = maxCapacity;
        this.currentEnrollmentCount = currentEnrollmentCount;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentEnrollmentCount() {
        return currentEnrollmentCount;
    }

    public void setCurrentEnrollmentCount(int currentEnrollmentCount) {
        this.currentEnrollmentCount = currentEnrollmentCount;
    }

    public boolean isFull() {
        return currentEnrollmentCount >= maxCapacity;
    }

    public String toFileString() {
        return courseId + "|" + courseName + "|" + instructorName + "|" + maxCapacity + "|" + currentEnrollmentCount;
    }
}
