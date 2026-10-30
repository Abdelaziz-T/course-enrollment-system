[README.md](https://github.com/user-attachments/files/34205254/README.md)
# Online Course Enrollment System

A beginner-friendly Java Swing desktop application for managing online course enrollment activities.

## Features
- Course management: add, update, delete, search
- Student management: add, update, delete, search
- Enrollment management: enroll, unenroll, and course student listing
- Reports generation and saving to file
- Analytics view with totals and most popular course
- File-based persistence with automatic load/save

## Folder Structure
- `src/model` - data models (`Course`, `Student`, `Enrollment`)
- `src/service` - business logic and file persistence
- `src/ui` - Swing UI tabs and dashboard
- `src/util` - validation and dialog helpers
- `data` - text files for saved application data

## Compile
From project root:

```bash
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
```

## Run

```bash
java -cp out Main
```

## Data Storage
- `data/courses.txt` stores course records
- `data/students.txt` stores student records
- `data/enrollments.txt` stores enrollment records
- `data/reports.txt` stores the last saved report

## Screenshots
You can place screenshots in the `screenshots/` folder.
