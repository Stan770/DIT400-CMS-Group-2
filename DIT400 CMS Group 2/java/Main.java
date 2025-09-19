import java.io.*;
import java.util.*;

public class Main {
    static final int MAX_COURSES = 1000;
    static String[] courseIds = new String[MAX_COURSES];
    static String[] titles = new String[MAX_COURSES];
    static int[] creditHours = new int[MAX_COURSES];
    static int courseCount = 0;
    static Scanner scanner = new Scanner(System.in);

    // ------------------ Helpers ------------------
    static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    static String toLowerCopy(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    // ------------------ User Authentication ------------------
    static boolean loginUser(String username, String password) {
        try (BufferedReader in = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                int p = line.indexOf(',');
                if (p == -1) continue;
                String u = trim(line.substring(0, p));
                String pword = trim(line.substring(p + 1));
                if (u.equals(username) && pword.equals(password)) return true;
            }
        } catch (IOException e) {
            return false; // no users yet
        }
        return false;
    }

    static boolean userExists(String username) {
        try (BufferedReader in = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                int p = line.indexOf(',');
                if (p == -1) continue;
                if (trim(line.substring(0, p)).equals(username)) return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    static void registerUser() {
        System.out.print("Enter new username: ");
        String username = trim(scanner.nextLine());
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (userExists(username)) {
            System.out.println("Username already exists. Choose another.");
            return;
        }
        System.out.print("Enter new password: ");
        String password = trim(scanner.nextLine());
        try (PrintWriter out = new PrintWriter(new FileWriter("users.txt", true))) {
            out.println(username + "," + password);
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to users.txt");
        }
    }

    // ------------------ File Helpers ------------------
    static void loadCourses() {
        courseCount = 0;
        try (BufferedReader in = new BufferedReader(new FileReader("courses.txt"))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) continue;
                int p1 = line.indexOf(',');
                if (p1 == -1) continue;
                int p2 = line.indexOf(',', p1 + 1);
                if (p2 == -1) continue;
                String id = trim(line.substring(0, p1));
                String title = trim(line.substring(p1 + 1, p2));
                String creditsStr = trim(line.substring(p2 + 1));
                if (id.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) continue;
                int credits = 0;
                try {
                    credits = Integer.parseInt(creditsStr);
                } catch (Exception e) {
                    continue;
                }
                if (courseCount < MAX_COURSES) {
                    courseIds[courseCount] = id;
                    titles[courseCount] = title;
                    creditHours[courseCount] = credits;
                    courseCount++;
                }
            }
        } catch (IOException e) {
            // File not found, start with empty list
        }
    }

    static void saveCourses() {
        try (PrintWriter out = new PrintWriter(new FileWriter("courses.txt"))) {
            for (int i = 0; i < courseCount; ++i) {
                out.println(courseIds[i] + "," + titles[i] + "," + creditHours[i]);
            }
        } catch (IOException e) {
            System.out.println("Error writing to courses.txt");
        }
    }

    // ------------------ Course Functions ------------------
    static void listCourses() {
        if (courseCount == 0) {
            System.out.println("No courses available.");
            return;
        }
        System.out.println("\n--- Course List ---");
        for (int i = 0; i < courseCount; ++i) {
            System.out.println(courseIds[i] + " | " + titles[i] + " | " + creditHours[i] + " credits");
        }
    }

    static void addCourse() {
        if (courseCount >= MAX_COURSES) {
            System.out.println("Course list full!");
            return;
        }
        System.out.print("Enter course ID: ");
        String id = trim(scanner.nextLine());
        if (id.isEmpty()) {
            System.out.println("Course ID cannot be empty.");
            return;
        }
        // Check duplicate
        for (int i = 0; i < courseCount; ++i) {
            if (courseIds[i].equals(id)) {
                System.out.println("Course ID already exists!");
                return;
            }
        }
        System.out.print("Enter title: ");
        String title = trim(scanner.nextLine());
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        System.out.print("Enter credit hours (1-6): ");
        String creditsStr = trim(scanner.nextLine());
        int credits = 0;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (Exception e) {
            System.out.println("Invalid credit hours (not a number).");
            return;
        }
        if (credits < 1 || credits > 6) {
            System.out.println("Invalid credit hours (must be 1-6).");
            return;
        }
        courseIds[courseCount] = id;
        titles[courseCount] = title;
        creditHours[courseCount] = credits;
        ++courseCount;
        saveCourses();
        System.out.println("Course added successfully!");
        System.out.println(id + " | " + title + " | " + credits + " credits");
    }

    static void deleteCourse() {
        System.out.print("Enter course ID to delete: ");
        String id = trim(scanner.nextLine());
        if (id.isEmpty()) {
            System.out.println("No ID entered.");
            return;
        }
        int index = -1;
        for (int i = 0; i < courseCount; ++i) {
            if (courseIds[i].equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Course not found!");
            return;
        }
        System.out.println("Deleting: " + courseIds[index] + " | " + titles[index] + " | " + creditHours[index]);
        for (int i = index; i < courseCount - 1; ++i) {
            courseIds[i] = courseIds[i + 1];
            titles[i] = titles[i + 1];
            creditHours[i] = creditHours[i + 1];
        }
        --courseCount;
        saveCourses();
        System.out.println("Course deleted successfully!");
    }

    static void searchCourse() {
        System.out.print("Enter course ID or title keyword: ");
        String keyword = trim(scanner.nextLine());
        if (keyword.isEmpty()) {
            System.out.println("No keyword entered.");
            return;
        }
        boolean found = false;
        String kwLower = toLowerCopy(keyword);
        for (int i = 0; i < courseCount; ++i) {
            if (courseIds[i].equals(keyword) || toLowerCopy(titles[i]).contains(kwLower)) {
                System.out.println(courseIds[i] + " | " + titles[i] + " | " + creditHours[i] + " credits");
                found = true;
            }
        }
        if (!found) System.out.println("No matching course found.");
    }

    static void updateCourse() {
        System.out.print("Enter course ID to update: ");
        String id = trim(scanner.nextLine());
        if (id.isEmpty()) {
            System.out.println("No ID entered.");
            return;
        }
        int index = -1;
        for (int i = 0; i < courseCount; ++i) {
            if (courseIds[i].equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Course not found!");
            return;
        }
        System.out.println("Current: " + courseIds[index] + " | " + titles[index] + " | " + creditHours[index]);
        System.out.print("Enter new title (leave blank to keep current): ");
        String newTitle = trim(scanner.nextLine());
        if (!newTitle.isEmpty()) titles[index] = newTitle;
        System.out.print("Enter new credit hours (1-6) (leave blank to keep current): ");
        String creditsStr = trim(scanner.nextLine());
        if (!creditsStr.isEmpty()) {
            int newCredits = 0;
            try {
                newCredits = Integer.parseInt(creditsStr);
            } catch (Exception e) {
                System.out.println("Invalid number, update cancelled.");
                return;
            }
            if (newCredits < 1 || newCredits > 6) {
                System.out.println("Invalid credit hours, update cancelled.");
                return;
            }
            creditHours[index] = newCredits;
        }
        saveCourses();
        System.out.println("Course updated successfully!");
        System.out.println(courseIds[index] + " | " + titles[index] + " | " + creditHours[index] + " credits");
    }

    // ------------------ Menu ------------------
    static void menu() {
        while (true) {
            System.out.println("\n--- Course Management System ---");
            System.out.println("1. Add Course");
            System.out.println("2. Delete Course");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. List All Courses");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1: addCourse(); break;
                case 2: deleteCourse(); break;
                case 3: searchCourse(); break;
                case 4: updateCourse(); break;
                case 5: listCourses(); break;
                case 6: System.out.println("Logging out..."); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ------------------ Main ------------------
    public static void main(String[] args) {
        loadCourses();
        while (true) {
            System.out.println("\n--- Welcome ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int option = 0;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }
            if (option == 1) {
                registerUser();
            } else if (option == 2) {
                System.out.print("Username: ");
                String username = trim(scanner.nextLine());
                System.out.print("Password: ");
                String password = trim(scanner.nextLine());
                if (loginUser(username, password)) {
                    System.out.println("Login successful!");
                    menu();
                } else {
                    System.out.println("Invalid credentials!");
                }
            } else if (option == 3) {
                System.out.println("Goodbye.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}