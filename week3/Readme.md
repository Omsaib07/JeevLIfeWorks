# Student Management System

A comprehensive Java-based Student Management System that allows users to manage student records efficiently. The system uses advanced Java collections and file I/O operations to provide a robust and user-friendly experience.

## 📁 Files Included

| File | Description |
|------|-------------|
| `Student.java` | Student entity class with all required attributes |
| `StudentManager.java` | Core management class with business logic |
| `Main.java` | Menu-driven main program with user interface |
| `README.md` | This documentation file |
| `students.dat` | Data file (created automatically when saving) |

## ✨ Features Implemented

### 🎓 Complete Student Class
- **Attributes**: id, name, age, grade, address
- **Methods**: constructors, getters/setters, toString, equals, hashCode
- **Interfaces**: Serializable for file I/O, Comparable for sorting

### 🔧 Advanced StudentManager Class
- **ArrayList&lt;Student&gt;**: Sequential storage
- **HashMap&lt;Integer, Student&gt;**: Fast O(1) lookups by ID
- **TreeSet&lt;Student&gt;**: Automatic sorting by name
- **CRUD Operations**: Complete Create, Read, Update, Delete functionality
- **File I/O**: ObjectInputStream/ObjectOutputStream implementation
- **Error Handling**: Comprehensive validation and exception management

### 🖥️ Menu-Driven Interface
- User-friendly console interface
- Input validation and error handling
- Confirmation prompts for destructive operations
- Automatic data persistence

### 🛡️ Exception Handling
- `InputMismatchException` for invalid input types
- `IllegalArgumentException` for validation errors
- `IOException` and `ClassNotFoundException` for file operations
- Custom validation for all student data

### ✅ Data Validation
- Unique ID enforcement
- Positive age validation (1-150 range)
- Non-empty field validation for name, grade, and address
- Comprehensive input sanitization

## 📋 Menu Options

| Option | Description |
|--------|-------------|
| **1** | **Add a new student** - Enter unique ID and student details |
| **2** | **Remove a student by ID** - Delete with confirmation prompt |
| **3** | **Update student details by ID** - Modify existing student information |
| **4** | **Search for a student by ID** - Find and display student details |
| **5** | **Display all students (sorted)** - Show all students sorted by name |
| **6** | **Exit and save data** - Save data and terminate program |

## 🔧 Technical Specifications

### Data Structures Used
- **ArrayList&lt;Student&gt;**: Maintaining insertion order and iteration
- **HashMap&lt;Integer, Student&gt;**: O(1) lookup operations by student ID
- **TreeSet&lt;Student&gt;**: Automatic sorting and duplicate prevention

### File Format
- Binary serialization using `ObjectOutputStream`/`ObjectInputStream`
- Automatic file creation and management
- Cross-platform compatibility

### Sorting Algorithm
- **Primary sort**: Student name (case-insensitive)
- **Secondary sort**: Student ID (ascending)

### Memory Management
- Efficient data structure usage
- Automatic resource cleanup with try-with-resources

## 🧪 Testing Instructions

1. **Start the program**
2. **Add 5 test students** with different names and IDs
3. **Test search functionality** with existing and non-existing IDs
4. **Test update functionality** by modifying student details
5. **Test remove functionality** with confirmation
6. **Test display functionality** to verify sorting
7. **Exit and restart** to verify data persistence



## 📸 Example Outputs

Below are example outputs showing the system in action:

### Example 1: 
![Example 1 - Main Menu](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week3/Example1.png?raw=true)

### Example 2:
![Example 2 - Adding Student](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week3/Example2.png?raw=true)

### Example 3:
![Example 3 - Search Student](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week3/Example3.png?raw=true)

### Example 4: 
![Example 4 - Update Student](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week3/Example4.png?raw=true)

### Example 5: 
![Example 5 - Display All Students](https://github.com/Omsaib07/JeevLIfeWorks/blob/main/week3/Example5.png?raw=true)

## 🚨 Error Handling

The system handles the following error scenarios:

- ❌ Invalid input types (non-numeric input for ID/age)
- ❌ Duplicate student IDs
- ❌ Negative or zero ages
- ❌ Empty or null fields
- ❌ File I/O errors
- ❌ Missing data files
- ❌ Corrupted data files

## 🏗️ Design Patterns Used

- **Singleton-like approach** for StudentManager
- **Data Access Object (DAO)** pattern for file operations
- **Model-View-Controller (MVC)** separation of concerns
- **Exception handling** with custom validation

## ✅ Requirements Compliance

| Requirement | Status |
|-------------|--------|
| Class Design | ✅ Complete Student and StudentManager classes |
| File Handling | ✅ ObjectInputStream/ObjectOutputStream implementation |
| Advanced Collections | ✅ HashMap for fast lookup, TreeSet for sorting |
| Menu-Driven Program | ✅ Complete console interface |
| Exception Handling | ✅ Comprehensive error management |
| Validation | ✅ All required validation rules implemented |
| Serializable | ✅ Proper serialization support |
| Documentation | ✅ Comprehensive comments and documentation |



## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. "File not found" error on first run
- ✅ This is normal - the system creates the file automatically

#### 2. "Invalid input" errors
- ✅ Ensure you enter numbers for ID and age fields
- ✅ Don't leave required fields empty

#### 3. "Duplicate ID" error
- ✅ Student IDs must be unique
- ✅ Use the search function to check existing IDs

#### 4. Data not persisting
- ✅ Always use option 6 to exit properly
- ✅ Check write permissions in the program directory

#### 5. ClassNotFoundException
- ✅ Ensure all `.class` files are present
- ✅ Recompile all Java files if needed


