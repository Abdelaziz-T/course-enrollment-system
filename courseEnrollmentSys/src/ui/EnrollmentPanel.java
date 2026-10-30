package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Course;
import model.Enrollment;
import model.Student;
import service.EnrollmentServiceManager;
import util.DialogUtil;

public class EnrollmentPanel extends JPanel {
    private EnrollmentServiceManager manager;
    private Runnable onDataChanged;

    private JComboBox<String> cmbStudent;
    private JComboBox<String> cmbCourse;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblStatus;

    public EnrollmentPanel(EnrollmentServiceManager manager, Runnable onDataChanged) {
        this.manager = manager;
        this.onDataChanged = onDataChanged;

        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbStudent = new JComboBox<String>();
        cmbCourse = new JComboBox<String>();
        cmbStudent.setPrototypeDisplayValue("S000000 - Example Name");
        cmbCourse.setPrototypeDisplayValue("C000000 - Example Course");

        JButton btnEnroll = new JButton("Enroll");
        JButton btnUnenroll = new JButton("Unenroll Selected");
        JButton btnViewCourseStudents = new JButton("View Students in Selected Course");

        topPanel.add(new JLabel("Student:"));
        topPanel.add(cmbStudent);
        topPanel.add(new JLabel("Course:"));
        topPanel.add(cmbCourse);
        topPanel.add(btnEnroll);
        topPanel.add(btnUnenroll);
        topPanel.add(btnViewCourseStudents);

        tableModel = new DefaultTableModel(new String[] { "Student ID", "Course ID", "Enrollment Date" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        lblStatus = new JLabel("Ready");

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        btnEnroll.addActionListener(e -> enrollSelected());
        btnUnenroll.addActionListener(e -> unenrollSelected());
        btnViewCourseStudents.addActionListener(e -> viewStudentsInCourse());
    }

    private void enrollSelected() {
        String studentText = (String) cmbStudent.getSelectedItem();
        String courseText = (String) cmbCourse.getSelectedItem();

        if (studentText == null || courseText == null) {
            DialogUtil.showError(this, "Please select student and course.");
            return;
        }

        String studentId = studentText.split(" - ")[0];
        String courseId = courseText.split(" - ")[0];

        String result = manager.getEnrollmentService().enrollStudent(studentId, courseId);
        if (!result.toLowerCase().contains("success")) {
            DialogUtil.showError(this, result);
            return;
        }

        manager.saveAllData();
        refreshData();
        lblStatus.setText(result);
        onDataChanged.run();
    }

    private void unenrollSelected() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            DialogUtil.showError(this, "Select enrollment row first.");
            return;
        }

        String studentId = table.getValueAt(selected, 0).toString();
        String courseId = table.getValueAt(selected, 1).toString();

        if (!DialogUtil.showConfirm(this, "Unenroll student " + studentId + " from course " + courseId + "?")) {
            return;
        }

        String result = manager.getEnrollmentService().unenrollStudent(studentId, courseId);
        manager.saveAllData();
        refreshData();
        lblStatus.setText(result);
        onDataChanged.run();
    }

    private void viewStudentsInCourse() {
        String courseText = (String) cmbCourse.getSelectedItem();
        if (courseText == null) {
            DialogUtil.showError(this, "Select a course.");
            return;
        }

        String courseId = courseText.split(" - ")[0];
        List<Student> students = manager.getEnrollmentService().getStudentsInCourse(courseId);

        StringBuilder builder = new StringBuilder();
        builder.append("Students in ").append(courseId).append(":\n\n");
        if (students.isEmpty()) {
            builder.append("No students enrolled.");
        } else {
            for (Student student : students) {
                builder.append("- ").append(student.getStudentId()).append(" | ")
                        .append(student.getName()).append(" | ")
                        .append(student.getEmail()).append("\n");
            }
        }

        DialogUtil.showInfo(this, builder.toString());
    }

    public void refreshData() {
        loadComboBoxes();
        refreshTable();
    }

    private void loadComboBoxes() {
        cmbStudent.removeAllItems();
        for (Student student : manager.getStudentService().getAllStudents()) {
            cmbStudent.addItem(student.getStudentId() + " - " + student.getName());
        }

        cmbCourse.removeAllItems();
        for (Course course : manager.getCourseService().getAllCourses()) {
            cmbCourse.addItem(course.getCourseId() + " - " + course.getCourseName());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Enrollment enrollment : manager.getEnrollmentService().getAllEnrollments()) {
            tableModel.addRow(new Object[] {
                    enrollment.getStudentId(),
                    enrollment.getCourseId(),
                    enrollment.getEnrollmentDate()
            });
        }
    }
}
