# Advanced Java OOP Assignment: Full-Fledged Library Management System

## 📋 Objective

To evaluate your deep understanding and practical application of core Object-Oriented Programming (OOP) principles in Java — abstraction, encapsulation, inheritance, polymorphism, interfaces, and additional architectural practices such as exception handling, persistence simulation, concurrency, and modular design.

## 🎯 Problem Statement

Design a robust, extensible, and maintainable Library Management System that manages books, members, staff, and library operations like issuing, returning, searching, and reserving books.

## 🏗️ Core Requirements

### 1. Class Design & OOP Principles

#### 📚 Book Class
**Attributes:**
- `bookID` (UUID) - Unique identifier for each book
- `title` (String) - Book title
- `author` (String) - Book author
- `genre` (String) - Book genre/category
- `isIssued` (boolean) - Current issue status
- `issuedTo` (Member) - Reference to member who issued the book
- `dueDate` (LocalDate) - Return due date
- `reservationQueue` (Queue<Member>) - Queue of members waiting to reserve

#### 👤 Member (Abstract Class)
**Attributes:**
- `memberID` (UUID) - Unique member identifier
- `name` (String) - Member's full name
- `email` (String) - Contact email
- `phone` (String) - Contact phone number
- `maxBooksAllowed` (int) - Maximum books this member type can issue
- `currentlyIssuedBooks` (List<Book>) - List of currently issued books

**Abstract Methods:**
- `int getMaxAllowedDays()` - Returns maximum days allowed for book possession
- `String getMemberType()` - Returns the type of member

#### 🎓 Member Subclasses

| Member Type | Max Books Allowed | Max Allowed Days |
|-------------|-------------------|------------------|
| **StudentMember** | 3 | 14 |
| **TeacherMember** | 5 | 30 |
| **GuestMember** | 1 | 7 |

#### 👨‍💼 Librarian Class (extends Member)
**Responsibilities:**
- Add/remove books from catalog
- View all issued books
- Override return deadlines
- Manage member registrations
- Access overdue book reports

## ⚙️ Core Functionality & Methods

### 📖 Book Management Operations

#### `issueBook(Book book, Member member)`
- **Purpose:** Issues a book to the specified member
- **Validation:** 
  - Check if book is available
  - Verify member hasn't reached book limit
- **Actions:**
  - Set book as issued
  - Assign book to member
  - Set due date based on member type
  - Add book to member's issued list

#### `returnBook(Book book, Member member)`
- **Purpose:** Process book return from member
- **Actions:**
  - Remove book from member's issued list
  - Mark book as available
  - Check reservation queue
  - Auto-assign to next reserved member if applicable

#### `addBook(Book book)` 🔒 *Librarian Only*
- **Purpose:** Add new book to library catalog
- **Validation:** Check for duplicate book ID
- **Error Handling:** Raise duplication error if book exists

#### `removeBook(Book book)` 🔒 *Librarian Only*
- **Purpose:** Remove book from catalog
- **Validation:** Ensure book is not currently issued
- **Restriction:** Only available books can be removed

### 🔍 Search & Discovery

#### `searchBooks(String keyword)`
- **Purpose:** Search books by multiple criteria
- **Search Fields:**
  - Title
  - Author
  - Genre
- **Returns:** List of matching books with availability status

### 📅 Reservation System

#### `reserveBook(Book book, Member member)`
- **Purpose:** Allow members to reserve currently issued books
- **Action:** Add member to book's reservation queue
- **Condition:** Only works if book is currently issued

### 📊 Information & Reports

#### `viewIssuedBooks(Member member)`
- **Purpose:** Display member's current book loans
- **Information Shown:**
  - Book details
  - Due dates
  - Remaining days until due

#### `registerMember(Member member)` 🔒 *Librarian Only*
- **Purpose:** Register new library member
- **Validation:** 
  - Check email uniqueness
  - Check phone number uniqueness
- **Error Handling:** Prevent duplicate registrations

#### `viewOverdueBooks()` 🔒 *Librarian Only*
- **Purpose:** Generate overdue books report
- **Information Included:**
  - Overdue book details
  - Corresponding member information
  - Days overdue

## 🎯 Learning Objectives

This assignment tests your understanding of:

### Core OOP Principles
- **Abstraction:** Abstract Member class with concrete implementations
- **Encapsulation:** Private attributes with controlled access
- **Inheritance:** Member hierarchy with specialized subclasses
- **Polymorphism:** Different member types with varying behaviors

### Advanced Java Concepts
- **Collections Framework:** Lists, Queues for data management
- **Exception Handling:** Custom exceptions for business logic
- **Date/Time API:** LocalDate for due date management
- **UUID:** Unique identifier generation

### Software Design Patterns
- **Template Method:** Abstract methods in Member class
- **Strategy Pattern:** Different member behaviors
- **Observer Pattern:** Reservation queue management

## 🚀 Implementation Guidelines

### Class Structure
```
LibraryManagementSystem/
├── src/
│   ├── models/
│   │   ├── Book.java
│   │   ├── Member.java (abstract)
│   │   ├── StudentMember.java
│   │   ├── TeacherMember.java
│   │   ├── GuestMember.java
│   │   └── Librarian.java
│   ├── services/
│   │   └── LibraryService.java
│   ├── exceptions/
│   │   ├── BookNotFoundException.java
│   │   ├── MemberNotFoundException.java
│   │   └── DuplicateEntryException.java
│   └── Main.java
└── README.md
```

### Key Implementation Points
1. **Use proper encapsulation** with private fields and public methods
2. **Implement abstract methods** in all Member subclasses
3. **Handle edge cases** with appropriate exceptions
4. **Use collections efficiently** for book and member management
5. **Follow naming conventions** and code documentation standards

## 🧪 Testing Scenarios

Consider implementing test cases for:
- Issuing books to different member types
- Handling book limits and overdue scenarios
- Reservation queue functionality
- Search operations with various keywords
- Librarian-specific operations
- Exception handling for invalid operations

## 📈 Extension Opportunities

- Add fine calculation for overdue books
- Implement book categories with different rules
- Add notification system for due dates
- Create GUI interface
- Add database persistence
- Implement concurrent access handling

## 📊 Visual Documentation

### Class Diagram
![Class Diagram](week2/ClassDiagram.png)

### Output Screenshot
![Output](week2/Output.png)

