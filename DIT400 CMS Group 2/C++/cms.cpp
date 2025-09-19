// main.cpp
#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <limits>
#include <sstream>
#include <algorithm>
using namespace std;

// ------------------ Constants ------------------
const int MAX_COURSES = 1000;

// ------------------ Global Arrays ------------------
string courseIds[MAX_COURSES];
string titles[MAX_COURSES];
int creditHours[MAX_COURSES];
int courseCount = 0;

// ------------------ Helpers ------------------
string trim(const string &s) {
    size_t a = s.find_first_not_of(" \t\r\n");
    if (a == string::npos) return "";
    size_t b = s.find_last_not_of(" \t\r\n");
    return s.substr(a, b - a + 1);
}

string toLowerCopy(const string &s) {
    string r = s;
    transform(r.begin(), r.end(), r.begin(), ::tolower);
    return r;
}

void clearCin() {
    cin.clear();
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
}

// ------------------ User Authentication ------------------
bool loginUser(const string &username, const string &password) {
    ifstream in("users.txt");
    if (!in) return false; // no users yet
    string line;
    while (getline(in, line)) {
        if (line.empty()) continue;
        size_t p = line.find(',');
        if (p == string::npos) continue;
        string u = trim(line.substr(0, p));
        string pword = trim(line.substr(p + 1));
        if (u == username && pword == password) return true;
    }
    return false;
}

bool userExists(const string &username) {
    ifstream in("users.txt");
    if (!in) return false;
    string line;
    while (getline(in, line)) {
        if (line.empty()) continue;
        size_t p = line.find(',');
        if (p == string::npos) continue;
        if (trim(line.substr(0, p)) == username) return true;
    }
    return false;
}

void registerUser() {
    string username, password;
    cout << "Enter new username: ";
    getline(cin, username);
    username = trim(username);
    if (username.empty()) {
        cout << "Username cannot be empty.\n";
        return;
    }
    if (userExists(username)) {
        cout << "Username already exists. Choose another.\n";
        return;
    }
    cout << "Enter new password: ";
    getline(cin, password);
    password = trim(password);
    ofstream out("users.txt", ios::app);
    out << username << "," << password << "\n";
    cout << "User registered successfully!\n";
}

// ------------------ File Helpers ------------------
void loadCourses() {
    ifstream in("courses.txt");
    courseCount = 0;
    if (!in) return;
    string line;
    while (getline(in, line)) {
        if (line.empty()) continue;
        size_t p1 = line.find(',');
        if (p1 == string::npos) continue;
        size_t p2 = line.find(',', p1 + 1);
        if (p2 == string::npos) continue;
        string id = trim(line.substr(0, p1));
        string title = trim(line.substr(p1 + 1, p2 - p1 - 1));
        string creditsStr = trim(line.substr(p2 + 1));
        if (id.empty() || title.empty() || creditsStr.empty()) continue;
        int credits = 0;
        try {
            credits = stoi(creditsStr);
        } catch (...) {
            continue;
        }
        if (courseCount < MAX_COURSES) {
            courseIds[courseCount] = id;
            titles[courseCount] = title;
            creditHours[courseCount] = credits;
            courseCount++;
        }
    }
}

void saveCourses() {
    ofstream out("courses.txt");
    for (int i = 0; i < courseCount; ++i) {
        out << courseIds[i] << "," << titles[i] << "," << creditHours[i] << "\n";
    }
}

// ------------------ Course Functions ------------------
void listCourses() {
    if (courseCount == 0) {
        cout << "No courses available.\n";
        return;
    }
    cout << "\n--- Course List ---\n";
    for (int i = 0; i < courseCount; ++i) {
        cout << courseIds[i] << " | " << titles[i] << " | " << creditHours[i] << " credits\n";
    }
}

void addCourse() {
    if (courseCount >= MAX_COURSES) {
        cout << "Course list full!\n";
        return;
    }

    string id, title, creditsStr;
    cout << "Enter course ID: ";
    getline(cin, id);
    id = trim(id);
    if (id.empty()) {
        cout << "Course ID cannot be empty.\n";
        return;
    }

    // Check duplicate
    for (int i = 0; i < courseCount; ++i) {
        if (courseIds[i] == id) {
            cout << "Course ID already exists!\n";
            return;
        }
    }

    cout << "Enter title: ";
    getline(cin, title);
    title = trim(title);
    if (title.empty()) {
        cout << "Title cannot be empty.\n";
        return;
    }

    cout << "Enter credit hours (1-6): ";
    getline(cin, creditsStr);
    creditsStr = trim(creditsStr);
    int credits = 0;
    try {
        credits = stoi(creditsStr);
    } catch (...) {
        cout << "Invalid credit hours (not a number).\n";
        return;
    }
    if (credits < 1 || credits > 6) {
        cout << "Invalid credit hours (must be 1-6).\n";
        return;
    }

    courseIds[courseCount] = id;
    titles[courseCount] = title;
    creditHours[courseCount] = credits;
    ++courseCount;
    saveCourses();
    cout << "Course added successfully!\n";
    cout << id << " | " << title << " | " << credits << " credits\n";
}

void deleteCourse() {
    string id;
    cout << "Enter course ID to delete: ";
    getline(cin, id);
    id = trim(id);
    if (id.empty()) {
        cout << "No ID entered.\n";
        return;
    }

    int index = -1;
    for (int i = 0; i < courseCount; ++i) {
        if (courseIds[i] == id) { index = i; break; }
    }
    if (index == -1) {
        cout << "Course not found!\n";
        return;
    }

    // show what will be deleted
    cout << "Deleting: " << courseIds[index] << " | " << titles[index] << " | " << creditHours[index] << "\n";

    // Shift left
    for (int i = index; i < courseCount - 1; ++i) {
        courseIds[i] = courseIds[i + 1];
        titles[i] = titles[i + 1];
        creditHours[i] = creditHours[i + 1];
    }
    --courseCount;
    saveCourses();
    cout << "Course deleted successfully!\n";
}

void searchCourse() {
    string keyword;
    cout << "Enter course ID or title keyword: ";
    getline(cin, keyword);
    keyword = trim(keyword);
    if (keyword.empty()) {
        cout << "No keyword entered.\n";
        return;
    }

    bool found = false;
    string kwLower = toLowerCopy(keyword);
    for (int i = 0; i < courseCount; ++i) {
        if (courseIds[i] == keyword || toLowerCopy(titles[i]).find(kwLower) != string::npos) {
            cout << courseIds[i] << " | " << titles[i] << " | " << creditHours[i] << " credits\n";
            found = true;
        }
    }
    if (!found) cout << "No matching course found.\n";
}

void updateCourse() {
    string id;
    cout << "Enter course ID to update: ";
    getline(cin, id);
    id = trim(id);
    if (id.empty()) {
        cout << "No ID entered.\n";
        return;
    }

    int index = -1;
    for (int i = 0; i < courseCount; ++i) {
        if (courseIds[i] == id) { index = i; break; }
    }
    if (index == -1) {
        cout << "Course not found!\n";
        return;
    }

    cout << "Current: " << courseIds[index] << " | " << titles[index] << " | " << creditHours[index] << "\n";
    string newTitle, creditsStr;
    cout << "Enter new title (leave blank to keep current): ";
    getline(cin, newTitle);
    newTitle = trim(newTitle);
    if (!newTitle.empty()) titles[index] = newTitle;

    cout << "Enter new credit hours (1-6) (leave blank to keep current): ";
    getline(cin, creditsStr);
    creditsStr = trim(creditsStr);
    if (!creditsStr.empty()) {
        int newCredits = 0;
        try { newCredits = stoi(creditsStr); }
        catch (...) { cout << "Invalid number, update cancelled.\n"; return; }
        if (newCredits < 1 || newCredits > 6) { cout << "Invalid credit hours, update cancelled.\n"; return; }
        creditHours[index] = newCredits;
    }

    saveCourses();
    cout << "Course updated successfully!\n";
    cout << courseIds[index] << " | " << titles[index] << " | " << creditHours[index] << " credits\n";
}

// ------------------ Menu ------------------
void menu() {
    while (true) {
        cout << "\n--- Course Management System ---\n";
        cout << "1. Add Course\n";
        cout << "2. Delete Course\n";
        cout << "3. Search Course\n";
        cout << "4. Update Course\n";
        cout << "5. List All Courses\n";
        cout << "6. Logout\n";
        cout << "Enter choice: ";

        int choice;
        if (!(cin >> choice)) {
            clearCin();
            cout << "Invalid input. Please enter a number.\n";
            continue;
        }
        clearCin(); // consume newline so next getline works

        switch (choice) {
            case 1: addCourse(); break;
            case 2: deleteCourse(); break;
            case 3: searchCourse(); break;
            case 4: updateCourse(); break;
            case 5: listCourses(); break;
            case 6: cout << "Logging out...\n"; return;
            default: cout << "Invalid choice!\n";
        }
    }
}

// ------------------ Main ------------------
int main() {
    loadCourses();

    while (true) {
        cout << "\n--- Welcome ---\n1. Register\n2. Login\n3. Exit\nChoose option: ";
        int option;
        if (!(cin >> option)) {
            clearCin();
            cout << "Invalid input.\n";
            continue;
        }
        clearCin();

        if (option == 1) {
            registerUser();
        } else if (option == 2) {
            string username, password;
            cout << "Username: ";
            getline(cin, username);
            username = trim(username);
            cout << "Password: ";
            getline(cin, password);
            password = trim(password);
            if (loginUser(username, password)) {
                cout << "Login successful!\n";
                menu();
            } else {
                cout << "Invalid credentials!\n";
            }
        } else if (option == 3) {
            cout << "Goodbye.\n";
            break;
        } else {
            cout << "Invalid option.\n";
        }
    }

    return 0;
}
