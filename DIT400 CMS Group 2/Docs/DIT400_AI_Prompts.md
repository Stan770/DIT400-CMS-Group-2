# DIT400 - Assignment 1: AI Prompts & Reflections

## Member A — Authentication & File I/O Lead
### AI Prompts Used
- "Generate C++ code for user login and registration using arrays only, persisting to users.txt."
- "Fix compile error: bits/stdc++.h not found."

### Changes Accepted
- Accepted the AI suggestion to replace with explicit standard headers.
- and Remove advanced liabraries
- Accepted the approach of storing usernames and passwords in parallel arrays.

### Changes Rejected
- Rejected AI’s suggestion to use structs or vectors for user data since assignment forbids them.

### Lessons Learned
- Learned the importance of following strict assignment constraints.
- Understood how to manage simple file I/O with arrays and strings.

---

## Member B — Course CRUD & Array Management Lead
### AI Prompts Used
- "Write functions in C++ for adding, deleting, listing courses using arrays only."
- "Ensure duplicate course IDs are prevented."

### Changes Accepted
- Accepted AI’s function-based design (`addCourse`, `deleteCourse`, `listCourses`).

### Changes Rejected
- Rejected AI’s initial suggestion to use `map<string, Course>` because maps are forbidden.
- Rejected recommendation to dynamically allocate arrays with pointers.

### Lessons Learned
- Learned the manual handling of arrays without higher-level data structures.
- Understood file rewrite logic after modifications.

---

## Member C — Search/Update & Java Lead
### AI Prompts Used
- "Create C++ search and update functions for courses using arrays."
- "Convert the C++ course management logic into Java, using arrays and file I/O."

### Changes Accepted
- Accepted AI’s approach for substring search in course titles.
- Accepted generated Java code that mirrors the C++ structure with arrays and file I/O.

### Changes Rejected
- Rejected AI’s suggestion to use Java `ArrayList` instead of arrays (constraint violation).
- Rejected adding GUI features, since only console-based output was required.

### Lessons Learned
- Learned differences between C++ and Java in file handling.
- Understood importance of documenting AI usage clearly.
