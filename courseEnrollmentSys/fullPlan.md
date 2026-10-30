You are a senior software engineer and university project architect.

Your task is to create a COMPLETE Java desktop application project from scratch using the SAME TECHNOLOGY STYLE and DEVELOPMENT STYLE as the project below:

Cinema Scheduling System
- Java Swing GUI
- Simple MVC-like structure
- Plain Java project
- Business logic separated from UI
- Simple persistence using files
- Beginner-friendly architecture
- University-level coding style

IMPORTANT:
The project MUST feel like it was created by the same developer/team that made the Cinema Scheduling System project.

That means:
- Similar structure
- Similar naming style
- Similar coding style
- Similar simplicity level
- Similar organization
- Similar separation between UI and logic

DO NOT:
- Use enterprise architecture
- Use advanced design patterns
- Use Spring
- Use JavaFX
- Use Maven or Gradle
- Use databases
- Use complicated abstractions
- Over-engineer the code

The project should look realistic for university coursework.

==================================================
PROJECT TITLE
==================================================

Online Course Enrollment System

==================================================
PROJECT OVERVIEW
==================================================

Create a Java Swing desktop application for managing online course enrollments.

The system should allow administrators to:
- Manage courses
- Manage students
- Enroll students into courses
- Unenroll students
- View reports
- View analytics/dashboard
- Save and load data from files

==================================================
IMPORTANT TECHNOLOGY REQUIREMENTS
==================================================

Use:
- Java
- Java Swing
- Plain text file storage OR simple JSON text storage
- Basic Java collections
- Standard Java libraries only

OPTIONAL:
- Gson may be used ONLY if necessary
- JFreeChart may be used for bonus dashboard

DO NOT use:
- JavaFX
- Spring
- Maven
- Gradle
- Databases
- Complex frameworks
- Dependency injection
- Advanced patterns

==================================================
PROJECT STRUCTURE
==================================================

Use a structure VERY SIMILAR to the Cinema Scheduling System.

CourseEnrollmentSystem/
│
├── src/
│   ├── model/
│   │   ├── Course.java
│   │   ├── Student.java
│   │   └── Enrollment.java
│   │
│   ├── service/
│   │   ├── CourseService.java
│   │   ├── StudentService.java
│   │   ├── EnrollmentService.java
│   │   ├── FileService.java
│   │   └── EnrollmentServiceManager.java
│   │
│   ├── ui/
│   │   ├── MainDashboard.java
│   │   ├── CoursePanel.java
│   │   ├── StudentPanel.java
│   │   ├── EnrollmentPanel.java
│   │   ├── ReportsPanel.java
│   │   └── AnalyticsPanel.java
│   │
│   ├── util/
│   │   ├── ValidationUtil.java
│   │   └── DialogUtil.java
│   │
│   └── Main.java
│
├── data/
│   ├── courses.txt
│   ├── students.txt
│   ├── enrollments.txt
│   └── reports.txt
│
├── screenshots/
│
├── README.md
└── ProjectDocumentation.md

==================================================
IMPORTANT ARCHITECTURE RULES
==================================================

The structure must follow the same philosophy as the Cinema Scheduling System.

Meaning:
- model/ contains only data classes
- service/ contains application logic and file handling
- ui/ contains all Swing screens and panels
- util/ contains validation and helper methods

The UI should NOT contain heavy logic.
Business logic should stay inside service classes.

==================================================
CODING STYLE REQUIREMENTS
==================================================

The code MUST:
- Be beginner-friendly
- Be readable
- Use simple methods
- Use realistic student-level logic
- Include comments naturally
- Avoid advanced syntax
- Avoid unnecessary abstraction
- Avoid complicated inheritance

The project should feel naturally written, not AI-generated.

==================================================
APPLICATION FEATURES
==================================================

========================
1. COURSE MANAGEMENT
========================

Admin can:
- Add courses
- Edit courses
- Delete courses
- Search courses
- View all courses

Each course includes:
- Course ID
- Course Name
- Instructor Name
- Maximum Capacity
- Current Enrollment Count

========================
2. STUDENT MANAGEMENT
========================

Admin can:
- Add students
- Edit students
- Delete students
- Search students
- View all students

Each student includes:
- Student ID
- Name
- Email
- List of enrolled courses

========================
3. ENROLLMENT MANAGEMENT
========================

Admin can:
- Enroll student into course
- Unenroll student from course
- View students inside a course

Enrollment rules:
- Prevent duplicate enrollments
- Prevent over-capacity enrollment
- Prevent invalid student/course IDs

========================
4. ANALYTICS DASHBOARD (BONUS)
========================

Create a simple analytics dashboard.

Include:
- Total students
- Total courses
- Most popular course
- Enrollment statistics

If charts are used:
- Keep them simple
- Use JFreeChart only if necessary

========================
5. REPORTS
========================

Create reports for:
- All courses
- All students
- Course enrollments

Allow saving reports to text files.

========================
6. FILE STORAGE
========================

Use simple text files similar to the Cinema Scheduling System.

Examples:
- courses.txt
- students.txt
- enrollments.txt

Requirements:
- Auto-save data
- Auto-load data
- Handle missing files safely

==================================================
SWING GUI REQUIREMENTS
==================================================

Use Java Swing only.

Use:
- JFrame
- JPanel
- JTable
- JScrollPane
- JTextField
- JComboBox
- JButton
- JLabel
- JTextArea
- JOptionPane
- JTabbedPane

GUI should contain:
- Dashboard tab
- Courses tab
- Students tab
- Enrollment tab
- Reports tab

Add:
- Search bars
- Status messages
- Confirmation dialogs
- Table row selection
- Double-click autofill

==================================================
VALIDATION REQUIREMENTS
==================================================

Validate:
- Empty fields
- Duplicate IDs
- Invalid emails
- Invalid capacities
- Invalid enrollments

Use:
- try/catch blocks
- JOptionPane error dialogs

==================================================
DOCUMENTATION REQUIREMENTS
==================================================

Generate:
1. README.md
2. ProjectDocumentation.md

Include:
- Project overview
- Features
- Folder structure
- File explanations
- How to compile
- How to run
- Data storage explanation
- Screenshots section

==================================================
OUTPUT REQUIREMENTS
==================================================

Generate output in this exact order:

1. Full folder structure
2. Build instructions
3. Every Java file one-by-one
4. Sample text data files
5. README.md
6. ProjectDocumentation.md
7. Testing instructions

==================================================
IMPORTANT FINAL RULES
==================================================

DO NOT:
- Skip files
- Skip methods
- Use placeholders
- Use pseudo-code
- Leave TODO comments
- Leave incomplete logic

Everything must be fully implemented and directly runnable.