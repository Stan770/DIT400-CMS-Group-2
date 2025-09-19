# DIT400 - Course Management System (CMS)

## 📌 Assignment Overview
This project is for **DIT400 - Object Oriented Programming (Semester 2, Assignment 1)**.  
It is a pre-OOP assignment that reinforces arrays and file I/O by building a **Course Management System (CMS)**.

The system was implemented first in **C++ (arrays only, no structs/classes/pointers/vectors)**, then translated to **Java**.

---

## 👥 Team Members
| Name              | Student ID | Role |
|-------------------|------------|------|
| Stanley nswana        | 2410440     | Authentication & File I/O Lead |
| Rose chifuti         | 2410472    | Course CRUD & Array Management Lead |
| Esther lwenje        | 2410160    | Search/Update & Java Lead |

---

## 📂 Repository Structure
```
DIT400_CMS_GroupXX/
│── C++/
│   ├── main.cpp
│   ├── build.sh / build.bat
│   └── cms_cpp.exe (or cms_cpp)
│
│── Java/
│   ├── Main.java
│   ├── build.sh / build.bat
│   └── cms_java.jar
│
│── docs/
│   ├── README.md
│   └── ai_prompts.md
│
│── audio/
│   └── discussion.mp3
```
---

## ⚙️ Build & Run Instructions


Windows (MinGW):
```bash
g++ -std=c++11 -O2 -o cms_cpp.exe main.cpp
cms_cpp.exe
```

### Java Version
Compile and package into JAR:
```bash
javac Main.java
jar cfe cms_java.jar Main Main.class
java -jar cms_java.jar
```

---

## 🗂 File Formats

**users.txt**
```
alice,alice123
bob,bob789
```

**courses.txt**
```
DIT101,Intro to IT,3
DIT205,Data Structures,4
```

---

## 🔑 Test Credentials
- Username: `alice`  
- Password: `alice123`  

---

## 📝 Known Limitations
- Plaintext password storage (per assignment instructions).
- Title search is case-sensitive.
- Commas inside titles may cause parsing issues.

---

## ✅ Lessons Learned
- Team collaboration using GitHub and clear roles.
- Importance of respecting assignment constraints (arrays only, no advanced data structures).
- Differences between C++ and Java file I/O handling.
- Documenting AI usage for transparency and learning.

---

## 📌 Deliverables Checklist
- [x] C++ executable (`cms_cpp` / `cms_cpp.exe`)  
- [x] Java JAR (`cms_java.jar`)  
- [x] Documentation (`README.md`, `ai_prompts.md`)  
- [x] Audio discussion (`discussion.mp3`)  
- [x] GitHub repo (`DIT400_CMS_GroupXX`)  
 

