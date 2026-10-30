package service;

import java.util.ArrayList;
import java.util.List;

import model.Course;
import util.ValidationUtil;

public class CourseService {
    private List<Course> courses;

    public CourseService() {
        this.courses = new ArrayList<Course>();
    }

    public List<Course> getAllCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean addCourse(Course course) {
        if (course == null || ValidationUtil.isNullOrEmpty(course.getCourseId())) {
            return false;
        }

        if (findCourseById(course.getCourseId()) != null) {
            return false;
        }

        courses.add(course);
        return true;
    }

    public boolean updateCourse(String oldId, Course updatedCourse) {
        Course existing = findCourseById(oldId);
        if (existing == null || updatedCourse == null) {
            return false;
        }

        if (!oldId.equalsIgnoreCase(updatedCourse.getCourseId()) && findCourseById(updatedCourse.getCourseId()) != null) {
            return false;
        }

        existing.setCourseId(updatedCourse.getCourseId());
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setInstructorName(updatedCourse.getInstructorName());
        existing.setMaxCapacity(updatedCourse.getMaxCapacity());

        if (existing.getCurrentEnrollmentCount() > existing.getMaxCapacity()) {
            existing.setCurrentEnrollmentCount(existing.getMaxCapacity());
        }

        return true;
    }

    public boolean deleteCourse(String courseId) {
        Course existing = findCourseById(courseId);
        if (existing == null) {
            return false;
        }

        courses.remove(existing);
        return true;
    }

    public Course findCourseById(String courseId) {
        if (ValidationUtil.isNullOrEmpty(courseId)) {
            return null;
        }

        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseId.trim())) {
                return course;
            }
        }
        return null;
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> results = new ArrayList<Course>();

        if (ValidationUtil.isNullOrEmpty(keyword)) {
            results.addAll(courses);
            return results;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (Course course : courses) {
            if (course.getCourseId().toLowerCase().contains(lowerKeyword)
                    || course.getCourseName().toLowerCase().contains(lowerKeyword)
                    || course.getInstructorName().toLowerCase().contains(lowerKeyword)) {
                results.add(course);
            }
        }

        return results;
    }
}
