package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import model.Course;
import service.EnrollmentServiceManager;
import util.DialogUtil;
import util.ValidationUtil;

public class CoursePanel extends JPanel {
    private EnrollmentServiceManager manager;
    private Runnable onDataChanged;

    private JTextField txtCourseId;
    private JTextField txtCourseName;
    private JTextField txtInstructor;
    private JTextField txtCapacity;
    private JTextField txtSearch;
    private JLabel lblStatus;

    private JTable table;
    private DefaultTableModel tableModel;

    public CoursePanel(EnrollmentServiceManager manager, Runnable onDataChanged) {
        this.manager = manager;
        this.onDataChanged = onDataChanged;
        initializeUI();
        refreshTable(manager.getCourseService().getAllCourses());
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCourseId = new JTextField(7);
        txtCourseName = new JTextField(12);
        txtInstructor = new JTextField(12);
        txtCapacity = new JTextField(5);

        formPanel.add(new JLabel("Course ID:"));
        formPanel.add(txtCourseId);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtCourseName);
        formPanel.add(new JLabel("Instructor:"));
        formPanel.add(txtInstructor);
        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(txtCapacity);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(18);
        JButton btnSearch = new JButton("Search");
        JButton btnShowAll = new JButton("Show All");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        tableModel = new DefaultTableModel(new String[] { "Course ID", "Name", "Instructor", "Capacity", "Enrolled" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    fillFormFromSelectedRow();
                }
            }
        });

        lblStatus = new JLabel("Ready");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnSearch.addActionListener(e -> searchCourses());
        btnShowAll.addActionListener(e -> refreshTable(manager.getCourseService().getAllCourses()));
    }

    private void addCourse() {
        String id = txtCourseId.getText().trim();
        String name = txtCourseName.getText().trim();
        String instructor = txtInstructor.getText().trim();
        String capacityText = txtCapacity.getText().trim();

        if (ValidationUtil.isNullOrEmpty(id) || ValidationUtil.isNullOrEmpty(name) || ValidationUtil.isNullOrEmpty(instructor)
                || ValidationUtil.isNullOrEmpty(capacityText)) {
            DialogUtil.showError(this, "Please fill all course fields.");
            return;
        }

        if (!ValidationUtil.isPositiveNumber(capacityText)) {
            DialogUtil.showError(this, "Capacity must be a positive number.");
            return;
        }

        Course course = new Course(id, name, instructor, Integer.parseInt(capacityText));
        if (!manager.getCourseService().addCourse(course)) {
            DialogUtil.showError(this, "Course ID already exists.");
            return;
        }

        manager.saveAllData();
        refreshTable(manager.getCourseService().getAllCourses());
        lblStatus.setText("Course added: " + id);
        clearForm();
        onDataChanged.run();
    }

    private void updateCourse() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            DialogUtil.showError(this, "Select a course row first.");
            return;
        }

        String oldId = table.getValueAt(selected, 0).toString();
        String id = txtCourseId.getText().trim();
        String name = txtCourseName.getText().trim();
        String instructor = txtInstructor.getText().trim();
        String capacityText = txtCapacity.getText().trim();

        if (ValidationUtil.isNullOrEmpty(id) || ValidationUtil.isNullOrEmpty(name) || ValidationUtil.isNullOrEmpty(instructor)
                || ValidationUtil.isNullOrEmpty(capacityText)) {
            DialogUtil.showError(this, "Please fill all course fields.");
            return;
        }

        if (!ValidationUtil.isPositiveNumber(capacityText)) {
            DialogUtil.showError(this, "Capacity must be a positive number.");
            return;
        }

        Course existing = manager.getCourseService().findCourseById(oldId);
        if (existing != null && existing.getCurrentEnrollmentCount() > Integer.parseInt(capacityText)) {
            DialogUtil.showError(this, "Capacity cannot be less than current enrollment.");
            return;
        }

        Course updated = new Course(id, name, instructor, Integer.parseInt(capacityText));
        updated.setCurrentEnrollmentCount(existing != null ? existing.getCurrentEnrollmentCount() : 0);

        if (!manager.getCourseService().updateCourse(oldId, updated)) {
            DialogUtil.showError(this, "Could not update course. ID may already exist.");
            return;
        }

        if (!oldId.equalsIgnoreCase(id)) {
            for (model.Enrollment enrollment : manager.getEnrollmentService().getAllEnrollments()) {
                if (enrollment.getCourseId().equalsIgnoreCase(oldId)) {
                    enrollment.setCourseId(id);
                }
            }
            for (model.Student student : manager.getStudentService().getAllStudents()) {
                for (int i = 0; i < student.getEnrolledCourseIds().size(); i++) {
                    if (student.getEnrolledCourseIds().get(i).equalsIgnoreCase(oldId)) {
                        student.getEnrolledCourseIds().set(i, id);
                    }
                }
            }
        }

        manager.saveAllData();
        refreshTable(manager.getCourseService().getAllCourses());
        lblStatus.setText("Course updated: " + id);
        onDataChanged.run();
    }

    private void deleteCourse() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            DialogUtil.showError(this, "Select a course row first.");
            return;
        }

        String courseId = table.getValueAt(selected, 0).toString();
        if (!DialogUtil.showConfirm(this, "Delete course " + courseId + " and its enrollments?")) {
            return;
        }

        manager.getEnrollmentService().removeCourseFromAllEnrollments(courseId);
        manager.getCourseService().deleteCourse(courseId);
        manager.saveAllData();

        refreshTable(manager.getCourseService().getAllCourses());
        lblStatus.setText("Course deleted: " + courseId);
        clearForm();
        onDataChanged.run();
    }

    private void searchCourses() {
        List<Course> result = manager.getCourseService().searchCourses(txtSearch.getText().trim());
        refreshTable(result);
        lblStatus.setText("Search found " + result.size() + " course(s).");
    }

    private void refreshTable(List<Course> courses) {
        tableModel.setRowCount(0);
        for (Course course : courses) {
            tableModel.addRow(new Object[] {
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getInstructorName(),
                    course.getMaxCapacity(),
                    course.getCurrentEnrollmentCount()
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }

        txtCourseId.setText(table.getValueAt(selected, 0).toString());
        txtCourseName.setText(table.getValueAt(selected, 1).toString());
        txtInstructor.setText(table.getValueAt(selected, 2).toString());
        txtCapacity.setText(table.getValueAt(selected, 3).toString());
        lblStatus.setText("Course loaded into form.");
    }

    private void clearForm() {
        txtCourseId.setText("");
        txtCourseName.setText("");
        txtInstructor.setText("");
        txtCapacity.setText("");
    }

    public void refreshData() {
        refreshTable(manager.getCourseService().getAllCourses());
    }
}
