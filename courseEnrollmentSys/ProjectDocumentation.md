# Project Documentation

## 1. Project Overview
Online Course Enrollment System is a Java Swing desktop application designed for university-level coursework. It follows a simple MVC-like structure where data classes live in `model`, business logic in `service`, and screens in `ui`.

## 2. Main Features
1. Course management
2. Student management
3. Enrollment management with validation rules
4. Reports generation
5. Analytics dashboard
6. File-based persistence

## 3. Folder and File Explanation
### src/model
- `Course.java`: stores course details and capacity tracking
- `Student.java`: stores student profile and enrolled course IDs
- `Enrollment.java`: stores enrollment relation and date

### src/service
- `FileService.java`: read/write helper for text files
- `CourseService.java`: course CRUD and search logic
- `StudentService.java`: student CRUD and search logic
- `EnrollmentService.java`: enrollment rules and processing
- `EnrollmentServiceManager.java`: service orchestration and persistence lifecycle

### src/ui
- `MainDashboard.java`: main window with tab navigation
- `CoursePanel.java`: course operations
- `StudentPanel.java`: student operations
- `EnrollmentPanel.java`: enrollment operations
- `ReportsPanel.java`: report generation and save
- `AnalyticsPanel.java`: stats and overview

### src/util
- `ValidationUtil.java`: field and format validation
- `DialogUtil.java`: common dialog methods

## 4. Data Storage Format
Data is stored in simple pipe-separated text format:
- courses: `id|name|instructor|maxCapacity|currentEnrollment`
- students: `id|name|email|courseId1,courseId2,...`
- enrollments: `studentId|courseId|enrollmentDate`

## 5. Build and Run Instructions
Compile:
```bash
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
```

Run:
```bash
java -cp out Main
```

## 6. Validation Rules
- Empty fields are rejected
- Duplicate student/course IDs are prevented
- Email format is checked
- Capacity must be positive number
- Enrollment prevents duplicates, invalid IDs, and full courses

## 7. Testing Instructions
1. Start the app and verify tabs load.
2. Add/edit/delete courses and students.
3. Enroll students and verify capacity updates.
4. Unenroll students and verify counts decrease.
5. Generate each report type and save report.
6. Close/reopen app and confirm data persistence.
