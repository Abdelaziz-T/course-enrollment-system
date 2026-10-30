package ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Course;
import service.EnrollmentServiceManager;

public class AnalyticsPanel extends JPanel {
    private EnrollmentServiceManager manager;
    private JLabel lblStudents;
    private JLabel lblCourses;
    private JLabel lblMostPopular;
    private JTextArea txtStats;

    public AnalyticsPanel(EnrollmentServiceManager manager) {
        this.manager = manager;
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel();
        top.setLayout(new javax.swing.BoxLayout(top, javax.swing.BoxLayout.Y_AXIS));

        lblStudents = new JLabel();
        lblCourses = new JLabel();
        lblMostPopular = new JLabel();

        top.add(lblStudents);
        top.add(lblCourses);
        top.add(lblMostPopular);

        txtStats = new JTextArea();
        txtStats.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new javax.swing.JScrollPane(txtStats), BorderLayout.CENTER);
    }

    public void refreshData() {
        int studentCount = manager.getStudentService().getAllStudents().size();
        int courseCount = manager.getCourseService().getAllCourses().size();

        Course popularCourse = null;
        for (Course course : manager.getCourseService().getAllCourses()) {
            if (popularCourse == null || course.getCurrentEnrollmentCount() > popularCourse.getCurrentEnrollmentCount()) {
                popularCourse = course;
            }
        }

        lblStudents.setText("Total Students: " + studentCount);
        lblCourses.setText("Total Courses: " + courseCount);

        if (popularCourse == null) {
            lblMostPopular.setText("Most Popular Course: N/A");
        } else {
            lblMostPopular.setText("Most Popular Course: " + popularCourse.getCourseName() + " ("
                    + popularCourse.getCurrentEnrollmentCount() + " enrollments)");
        }

        StringBuilder statsBuilder = new StringBuilder();
        statsBuilder.append("Enrollment Statistics\n");
        statsBuilder.append("==============================\n");

        for (Course course : manager.getCourseService().getAllCourses()) {
            statsBuilder.append(course.getCourseId()).append(" - ")
                    .append(course.getCourseName()).append(": ")
                    .append(course.getCurrentEnrollmentCount()).append("/")
                    .append(course.getMaxCapacity()).append("\n");
        }

        txtStats.setText(statsBuilder.toString());
    }
}
