package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import service.EnrollmentServiceManager;

public class MainDashboard extends JFrame {
    private EnrollmentServiceManager manager;

    private JLabel lblDashboardInfo;
    private CoursePanel coursePanel;
    private StudentPanel studentPanel;
    private EnrollmentPanel enrollmentPanel;
    private ReportsPanel reportsPanel;
    private AnalyticsPanel analyticsPanel;

    public MainDashboard() {
        manager = new EnrollmentServiceManager();

        setTitle("Online Course Enrollment System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        JPanel dashboardPanel = new JPanel(new BorderLayout());
        lblDashboardInfo = new JLabel();
        dashboardPanel.add(lblDashboardInfo, BorderLayout.NORTH);

        coursePanel = new CoursePanel(manager, this::refreshAllPanels);
        studentPanel = new StudentPanel(manager, this::refreshAllPanels);
        enrollmentPanel = new EnrollmentPanel(manager, this::refreshAllPanels);
        reportsPanel = new ReportsPanel(manager);
        analyticsPanel = new AnalyticsPanel(manager);

        dashboardPanel.add(analyticsPanel, BorderLayout.CENTER);

        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Courses", coursePanel);
        tabs.addTab("Students", studentPanel);
        tabs.addTab("Enrollment", enrollmentPanel);
        tabs.addTab("Reports", reportsPanel);
        tabs.addTab("Analytics", analyticsPanel);

        add(tabs, BorderLayout.CENTER);

        refreshDashboardHeader();
    }

    private void refreshAllPanels() {
        coursePanel.refreshData();
        studentPanel.refreshData();
        enrollmentPanel.refreshData();
        analyticsPanel.refreshData();
        refreshDashboardHeader();
    }

    private void refreshDashboardHeader() {
        int students = manager.getStudentService().getAllStudents().size();
        int courses = manager.getCourseService().getAllCourses().size();
        int enrollments = manager.getEnrollmentService().getAllEnrollments().size();

        lblDashboardInfo.setText("Welcome Admin! Students: " + students + " | Courses: " + courses + " | Enrollments: " + enrollments);
    }
}
