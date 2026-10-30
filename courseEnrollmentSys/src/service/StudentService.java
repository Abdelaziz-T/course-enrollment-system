package service;

import java.util.ArrayList;
import java.util.List;

import model.Student;
import util.ValidationUtil;

public class StudentService {
    private List<Student> students;

    public StudentService() {
        this.students = new ArrayList<Student>();
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean addStudent(Student student) {
        if (student == null || ValidationUtil.isNullOrEmpty(student.getStudentId())) {
            return false;
        }

        if (findStudentById(student.getStudentId()) != null) {
            return false;
        }

        students.add(student);
        return true;
    }

    public boolean updateStudent(String oldId, Student updatedStudent) {
        Student existing = findStudentById(oldId);
        if (existing == null || updatedStudent == null) {
            return false;
        }

        if (!oldId.equalsIgnoreCase(updatedStudent.getStudentId()) && findStudentById(updatedStudent.getStudentId()) != null) {
            return false;
        }

        existing.setStudentId(updatedStudent.getStudentId());
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        return true;
    }

    public boolean deleteStudent(String studentId) {
        Student existing = findStudentById(studentId);
        if (existing == null) {
            return false;
        }

        students.remove(existing);
        return true;
    }

    public Student findStudentById(String studentId) {
        if (ValidationUtil.isNullOrEmpty(studentId)) {
            return null;
        }

        for (Student student : students) {
            if (student.getStudentId().equalsIgnoreCase(studentId.trim())) {
                return student;
            }
        }

        return null;
    }

    public List<Student> searchStudents(String keyword) {
        List<Student> results = new ArrayList<Student>();

        if (ValidationUtil.isNullOrEmpty(keyword)) {
            results.addAll(students);
            return results;
        }

        String lowerKeyword = keyword.toLowerCase();
        for (Student student : students) {
            if (student.getStudentId().toLowerCase().contains(lowerKeyword)
                    || student.getName().toLowerCase().contains(lowerKeyword)
                    || student.getEmail().toLowerCase().contains(lowerKeyword)) {
                results.add(student);
            }
        }

        return results;
    }
}
