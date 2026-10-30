package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Course;
import model.Enrollment;
import model.Student;
import service.EnrollmentServiceManager;
import util.DialogUtil;

public class ReportsPanel extends JPanel {
    private EnrollmentServiceManager manager;
    private JTextArea txtReport;

    public ReportsPanel(EnrollmentServiceManager manager) {
        this.manager = manager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCourses = new JButton("Generate Courses Report");
        JButton btnStudents = new JButton("Generate Students Report");
        JButton btnEnrollments = new JButton("Generate Enrollments Report");
        JButton btnSave = new JButton("Save Current Report");

        topPanel.add(btnCourses);
        topPanel.add(btnStudents);
        topPanel.add(btnEnrollments);
        topPanel.add(btnSave);

        txtReport = new JTextArea();
        txtReport.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtReport), BorderLayout.CENTER);

        btnCourses.addActionListener(e -> generateCoursesReport());
        btnStudents.addActionListener(e -> generateStudentsReport());
        btnEnrollments.addActionListener(e -> generateEnrollmentsReport());
        btnSave.addActionListener(e -> saveReport());
    }

    private void generateCoursesReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("COURSES REPORT\n");
        builder.append("==============================\n");

        for (Course course : manager.getCourseService().getAllCourses()) {
            builder.append("ID: ").append(course.getCourseId()).append("\n");
            builder.append("Name: ").append(course.getCourseName()).append("\n");
            builder.append("Instructor: ").append(course.getInstructorName()).append("\n");
            builder.append("Capacity: ").append(course.getCurrentEnrollmentCount()).append(" / ").append(course.getMaxCapacity())
                    .append("\n");
            builder.append("------------------------------\n");
        }

        txtReport.setText(builder.toString());
    }

    private void generateStudentsReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("STUDENTS REPORT\n");
        builder.append("==============================\n");

        for (Student student : manager.getStudentService().getAllStudents()) {
            builder.append("ID: ").append(student.getStudentId()).append("\n");
            builder.append("Name: ").append(student.getName()).append("\n");
            builder.append("Email: ").append(student.getEmail()).append("\n");
            builder.append("Courses: ").append(student.getEnrolledCoursesAsString()).append("\n");
            builder.append("------------------------------\n");
        }

        txtReport.setText(builder.toString());
    }

    private void generateEnrollmentsReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("ENROLLMENTS REPORT\n");
        builder.append("==============================\n");

        for (Enrollment enrollment : manager.getEnrollmentService().getAllEnrollments()) {
            builder.append("Student ID: ").append(enrollment.getStudentId()).append(" | ");
            builder.append("Course ID: ").append(enrollment.getCourseId()).append(" | ");
            builder.append("Date: ").append(enrollment.getEnrollmentDate()).append("\n");
        }

        txtReport.setText(builder.toString());
    }

    private void saveReport() {
        String text = txtReport.getText().trim();
        if (text.isEmpty()) {
            DialogUtil.showError(this, "No report generated yet.");
            return;
        }

        manager.saveReport(text);
        DialogUtil.showInfo(this, "Report saved to data/reports.txt");
    }
}
