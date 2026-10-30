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

import model.Student;
import service.EnrollmentServiceManager;
import util.DialogUtil;
import util.ValidationUtil;

public class StudentPanel extends JPanel {
    private EnrollmentServiceManager manager;
    private Runnable onDataChanged;

    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtSearch;
    private JLabel lblStatus;

    private JTable table;
    private DefaultTableModel tableModel;

    public StudentPanel(EnrollmentServiceManager manager, Runnable onDataChanged) {
        this.manager = manager;
        this.onDataChanged = onDataChanged;

        initializeUI();
        refreshTable(manager.getStudentService().getAllStudents());
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtStudentId = new JTextField(8);
        txtName = new JTextField(12);
        txtEmail = new JTextField(16);

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(txtStudentId);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

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

        tableModel = new DefaultTableModel(new String[] { "Student ID", "Name", "Email", "Enrolled Courses" }, 0) {
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

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudents());
        btnShowAll.addActionListener(e -> refreshTable(manager.getStudentService().getAllStudents()));
    }

    private void addStudent() {
        String id = txtStudentId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (ValidationUtil.isNullOrEmpty(id) || ValidationUtil.isNullOrEmpty(name) || ValidationUtil.isNullOrEmpty(email)) {
            DialogUtil.showError(this, "Please fill all student fields.");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            DialogUtil.showError(this, "Please enter a valid email.");
            return;
        }

        Student student = new Student(id, name, email);
        if (!manager.getStudentService().addStudent(student)) {
            DialogUtil.showError(this, "Student ID already exists.");
            return;
        }

        manager.saveAllData();
        refreshTable(manager.getStudentService().getAllStudents());
        lblStatus.setText("Student added: " + id);
        clearForm();
        onDataChanged.run();
    }

    private void updateStudent() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            DialogUtil.showError(this, "Select a student row first.");
            return;
        }

        String oldId = table.getValueAt(selected, 0).toString();
        String id = txtStudentId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (ValidationUtil.isNullOrEmpty(id) || ValidationUtil.isNullOrEmpty(name) || ValidationUtil.isNullOrEmpty(email)) {
            DialogUtil.showError(this, "Please fill all student fields.");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            DialogUtil.showError(this, "Please enter a valid email.");
            return;
        }

        Student existing = manager.getStudentService().findStudentById(oldId);
        Student updated = new Student(id, name, email, existing.getEnrolledCourseIds());

        if (!manager.getStudentService().updateStudent(oldId, updated)) {
            DialogUtil.showError(this, "Could not update student. ID may already exist.");
            return;
        }

        if (!oldId.equalsIgnoreCase(id)) {
            for (model.Enrollment enrollment : manager.getEnrollmentService().getAllEnrollments()) {
                if (enrollment.getStudentId().equalsIgnoreCase(oldId)) {
                    enrollment.setStudentId(id);
                }
            }
        }

        manager.saveAllData();
        refreshTable(manager.getStudentService().getAllStudents());
        lblStatus.setText("Student updated: " + id);
        onDataChanged.run();
    }

    private void deleteStudent() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            DialogUtil.showError(this, "Select a student row first.");
            return;
        }

        String studentId = table.getValueAt(selected, 0).toString();
        if (!DialogUtil.showConfirm(this, "Delete student " + studentId + " and related enrollments?")) {
            return;
        }

        manager.getEnrollmentService().removeStudentFromAllEnrollments(studentId);
        manager.getStudentService().deleteStudent(studentId);
        manager.saveAllData();

        refreshTable(manager.getStudentService().getAllStudents());
        lblStatus.setText("Student deleted: " + studentId);
        clearForm();
        onDataChanged.run();
    }

    private void searchStudents() {
        List<Student> result = manager.getStudentService().searchStudents(txtSearch.getText().trim());
        refreshTable(result);
        lblStatus.setText("Search found " + result.size() + " student(s).");
    }

    private void refreshTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[] {
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getEnrolledCoursesAsString()
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }

        txtStudentId.setText(table.getValueAt(selected, 0).toString());
        txtName.setText(table.getValueAt(selected, 1).toString());
        txtEmail.setText(table.getValueAt(selected, 2).toString());
        lblStatus.setText("Student loaded into form.");
    }

    private void clearForm() {
        txtStudentId.setText("");
        txtName.setText("");
        txtEmail.setText("");
    }

    public void refreshData() {
        refreshTable(manager.getStudentService().getAllStudents());
    }
}
