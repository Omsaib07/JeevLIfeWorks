import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Custom Exceptions
class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}

class BookLimitExceededException extends LibraryException {
    public BookLimitExceededException(String message) {
        super(message);
    }
}

class BookAlreadyIssuedException extends LibraryException {
    public BookAlreadyIssuedException(String message) {
        super(message);
    }
}

class DuplicateBookException extends LibraryException {
    public DuplicateBookException(String message) {
        super(message);
    }
}

class DuplicateMemberException extends LibraryException {
    public DuplicateMemberException(String message) {
        super(message);
    }
}

class UnauthorizedAccessException extends LibraryException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

// Book class with proper encapsulation
class Book {
    private final UUID bookID;
    private String title;
    private String author;
    private String genre;
    private boolean isIssued;
    private Member issuedTo;
    private LocalDate dueDate;
    private Queue<Member> reservationQueue;

    public Book(String title, String author, String genre) {
        this.bookID = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isIssued = false;
        this.issuedTo = null;
        this.dueDate = null;
        this.reservationQueue = new LinkedList<>();
    }

    // Getters
    public UUID getBookID() { return bookID; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public boolean isIssued() { return isIssued; }
    public Member getIssuedTo() { return issuedTo; }
    public LocalDate getDueDate() { return dueDate; }
    public Queue<Member> getReservationQueue() { return new LinkedList<>(reservationQueue); }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        this.author = author;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        this.genre = genre;
    }

    // Package-private methods for library operations
    void issueBook(Member member, LocalDate dueDate) {
        this.isIssued = true;
        this.issuedTo = member;
        this.dueDate = dueDate;
    }

    void returnBook() {
        this.isIssued = false;
        this.issuedTo = null;
        this.dueDate = null;
    }

    void addToReservationQueue(Member member) {
        if (!reservationQueue.contains(member)) {
            reservationQueue.offer(member);
        }
    }

    Member getNextReservedMember() {
        return reservationQueue.poll();
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysUntilDue() {
        if (dueDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(bookID, book.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookID);
    }

    @Override
    public String toString() {
        return String.format("Book{ID=%s, title='%s', author='%s', genre='%s', issued=%s, dueDate=%s}",
                bookID.toString().substring(0, 8), title, author, genre, isIssued, dueDate);
    }
}

// Abstract Member class demonstrating abstraction
abstract class Member {
    protected final UUID memberID;
    protected String name;
    protected String email;
    protected String phone;
    protected final int maxBooksAllowed;
    protected List<Book> currentlyIssuedBooks;

    public Member(String name, String email, String phone, int maxBooksAllowed) {
        this.memberID = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.maxBooksAllowed = maxBooksAllowed;
        this.currentlyIssuedBooks = new ArrayList<>();
    }

    // Abstract methods for polymorphic behavior
    public abstract int getMaxAllowedDays();
    public abstract String getMemberType();

    // Getters
    public UUID getMemberID() { return memberID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public List<Book> getCurrentlyIssuedBooks() { return new ArrayList<>(currentlyIssuedBooks); }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.phone = phone;
    }

    // Package-private methods for library operations
    void addIssuedBook(Book book) {
        if (!currentlyIssuedBooks.contains(book)) {
            currentlyIssuedBooks.add(book);
        }
    }

    void removeIssuedBook(Book book) {
        currentlyIssuedBooks.remove(book);
    }

    public boolean canIssueMoreBooks() {
        return currentlyIssuedBooks.size() < maxBooksAllowed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(memberID, member.memberID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberID);
    }

    @Override
    public String toString() {
        return String.format("%s{ID=%s, name='%s', email='%s', booksIssued=%d/%d}",
                getMemberType(), memberID.toString().substring(0, 8), name, email,
                currentlyIssuedBooks.size(), maxBooksAllowed);
    }
}

// Concrete Member implementations demonstrating inheritance and polymorphism
class StudentMember extends Member {
    public StudentMember(String name, String email, String phone) {
        super(name, email, phone, 3);
    }

    @Override
    public int getMaxAllowedDays() {
        return 14;
    }

    @Override
    public String getMemberType() {
        return "Student";
    }
}

class TeacherMember extends Member {
    public TeacherMember(String name, String email, String phone) {
        super(name, email, phone, 5);
    }

    @Override
    public int getMaxAllowedDays() {
        return 30;
    }

    @Override
    public String getMemberType() {
        return "Teacher";
    }
}

class GuestMember extends Member {
    public GuestMember(String name, String email, String phone) {
        super(name, email, phone, 1);
    }

    @Override
    public int getMaxAllowedDays() {
        return 7;
    }

    @Override
    public String getMemberType() {
        return "Guest";
    }
}

// Librarian class with special privileges
class Librarian extends Member {
    public Librarian(String name, String email, String phone) {
        super(name, email, phone, 10); // Librarians can have more books
    }

    @Override
    public int getMaxAllowedDays() {
        return 60; // Extended period for librarians
    }

    @Override
    public String getMemberType() {
        return "Librarian";
    }

    public boolean hasLibrarianPrivileges() {
        return true;
    }
}

// Interface for search operations
interface Searchable {
    List<Book> searchBooks(String keyword);
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchByGenre(String genre);
}

// Interface for library operations
interface LibraryOperations {
    void issueBook(Book book, Member member) throws LibraryException;
    void returnBook(Book book, Member member) throws LibraryException;
    void addBook(Book book) throws LibraryException;
    void removeBook(Book book) throws LibraryException;
    void reserveBook(Book book, Member member) throws LibraryException;
    void registerMember(Member member) throws LibraryException;
}

// Main Library Management System
class LibraryManagementSystem implements LibraryOperations, Searchable {
    private final Map<UUID, Book> books;
    private final Map<UUID, Member> members;
    private final Map<String, Member> membersByEmail;
    private final Map<String, Member> membersByPhone;

    public LibraryManagementSystem() {
        this.books = new ConcurrentHashMap<>();
        this.members = new ConcurrentHashMap<>();
        this.membersByEmail = new ConcurrentHashMap<>();
        this.membersByPhone = new ConcurrentHashMap<>();
    }

    // Implementation of LibraryOperations interface
    @Override
    public synchronized void issueBook(Book book, Member member) throws LibraryException {
        validateBookExists(book);
        validateMemberExists(member);

        if (book.isIssued()) {
            throw new BookAlreadyIssuedException("Book is already issued to another member");
        }

        if (!member.canIssueMoreBooks()) {
            throw new BookLimitExceededException(
                String.format("Member has reached maximum book limit of %d", member.getMaxBooksAllowed()));
        }

        LocalDate dueDate = LocalDate.now().plusDays(member.getMaxAllowedDays());
        book.issueBook(member, dueDate);
        member.addIssuedBook(book);

        System.out.printf("Book '%s' issued to %s. Due date: %s%n", 
                         book.getTitle(), member.getName(), dueDate);
    }

    @Override
    public synchronized void returnBook(Book book, Member member) throws LibraryException {
        validateBookExists(book);
        validateMemberExists(member);

        if (!book.isIssued() || !book.getIssuedTo().equals(member)) {
            throw new LibraryException("Book is not issued to this member");
        }

        book.returnBook();
        member.removeIssuedBook(book);

        // Check if anyone has reserved this book
        Member nextMember = book.getNextReservedMember();
        if (nextMember != null && nextMember.canIssueMoreBooks()) {
            try {
                issueBook(book, nextMember);
                System.out.printf("Book automatically issued to next reserved member: %s%n", 
                                 nextMember.getName());
            } catch (LibraryException e) {
                System.err.printf("Failed to auto-issue to reserved member: %s%n", e.getMessage());
            }
        }

        System.out.printf("Book '%s' returned by %s%n", book.getTitle(), member.getName());
    }

    @Override
    public synchronized void addBook(Book book) throws LibraryException {
        if (books.containsKey(book.getBookID())) {
            throw new DuplicateBookException("Book with this ID already exists");
        }
        books.put(book.getBookID(), book);
        System.out.printf("Book '%s' added to library%n", book.getTitle());
    }

    @Override
    public synchronized void removeBook(Book book) throws LibraryException {
        validateBookExists(book);
        
        if (book.isIssued()) {
            throw new LibraryException("Cannot remove book that is currently issued");
        }

        books.remove(book.getBookID());
        System.out.printf("Book '%s' removed from library%n", book.getTitle());
    }

    @Override
    public synchronized void reserveBook(Book book, Member member) throws LibraryException {
        validateBookExists(book);
        validateMemberExists(member);

        if (!book.isIssued()) {
            throw new LibraryException("Book is available, no need to reserve");
        }

        book.addToReservationQueue(member);
        System.out.printf("Book '%s' reserved for %s%n", book.getTitle(), member.getName());
    }

    @Override
    public synchronized void registerMember(Member member) throws LibraryException {
        if (membersByEmail.containsKey(member.getEmail())) {
            throw new DuplicateMemberException("Member with this email already exists");
        }
        if (membersByPhone.containsKey(member.getPhone())) {
            throw new DuplicateMemberException("Member with this phone number already exists");
        }

        members.put(member.getMemberID(), member);
        membersByEmail.put(member.getEmail(), member);
        membersByPhone.put(member.getPhone(), member);
        
        System.out.printf("Member '%s' registered as %s%n", member.getName(), member.getMemberType());
    }

    // Implementation of Searchable interface
    @Override
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerKeyword = keyword.toLowerCase().trim();
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                               book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                               book.getGenre().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerTitle = title.toLowerCase().trim();
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerTitle))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerAuthor = author.toLowerCase().trim();
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(lowerAuthor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerGenre = genre.toLowerCase().trim();
        return books.values().stream()
                .filter(book -> book.getGenre().toLowerCase().contains(lowerGenre))
                .collect(Collectors.toList());
    }

    // Additional utility methods
    public List<Book> viewIssuedBooks(Member member) throws LibraryException {
        validateMemberExists(member);
        return member.getCurrentlyIssuedBooks();
    }

    public List<Book> viewOverdueBooks(Member requester) throws LibraryException {
        validateLibrarianAccess(requester);
        
        return books.values().stream()
                .filter(Book::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Member> getAllMembers(Member requester) throws LibraryException {
        validateLibrarianAccess(requester);
        return new ArrayList<>(members.values());
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public void displayLibraryStatistics() {
        long totalBooks = books.size();
        long issuedBooks = books.values().stream().mapToLong(book -> book.isIssued() ? 1 : 0).sum();
        long availableBooks = totalBooks - issuedBooks;
        long overdueBooks = books.values().stream().mapToLong(book -> book.isOverdue() ? 1 : 0).sum();

        System.out.println("\n=== Library Statistics ===");
        System.out.printf("Total Books: %d%n", totalBooks);
        System.out.printf("Available Books: %d%n", availableBooks);
        System.out.printf("Issued Books: %d%n", issuedBooks);
        System.out.printf("Overdue Books: %d%n", overdueBooks);
        System.out.printf("Total Members: %d%n", members.size());
        System.out.println("==========================\n");
    }

    // Validation methods
    private void validateBookExists(Book book) throws BookNotFoundException {
        if (!books.containsKey(book.getBookID())) {
            throw new BookNotFoundException("Book not found in library");
        }
    }

    private void validateMemberExists(Member member) throws MemberNotFoundException {
        if (!members.containsKey(member.getMemberID())) {
            throw new MemberNotFoundException("Member not registered in library");
        }
    }

    private void validateLibrarianAccess(Member member) throws UnauthorizedAccessException {
        if (!(member instanceof Librarian)) {
            throw new UnauthorizedAccessException("This operation requires librarian privileges");
        }
    }
}

// Demo class to test the system
public class LibraryDemo {
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();

        try {
            // Create and register members
            Member student = new StudentMember("Alice Johnson", "alice@email.com", "+1234567890");
            Member teacher = new TeacherMember("Dr. Bob Smith", "bob@email.com", "+1234567891");
            Member guest = new GuestMember("Charlie Brown", "charlie@email.com", "+1234567892");
            Librarian librarian = new Librarian("Eve Wilson", "eve@library.com", "+1234567893");

            library.registerMember(student);
            library.registerMember(teacher);
            library.registerMember(guest);
            library.registerMember(librarian);

            // Add books to library
            Book book1 = new Book("Java: The Complete Reference", "Herbert Schildt", "Programming");
            Book book2 = new Book("Clean Code", "Robert Martin", "Programming");
            Book book3 = new Book("Design Patterns", "Gang of Four", "Software Engineering");
            Book book4 = new Book("Effective Java", "Joshua Bloch", "Programming");

            library.addBook(book1);
            library.addBook(book2);
            library.addBook(book3);
            library.addBook(book4);

            // Test book operations
            System.out.println("\n=== Testing Book Operations ===");
            library.issueBook(book1, student);
            library.issueBook(book2, teacher);

            // Test reservation
            library.reserveBook(book1, teacher);

            // Test search functionality
            System.out.println("\n=== Search Results for 'Java' ===");
            List<Book> searchResults = library.searchBooks("Java");
            searchResults.forEach(System.out::println);

            // Display issued books
            System.out.println("\n=== Student's Issued Books ===");
            List<Book> studentBooks = library.viewIssuedBooks(student);
            studentBooks.forEach(book -> {
                System.out.printf("%s - Due: %s (Days remaining: %d)%n", 
                                 book.getTitle(), book.getDueDate(), book.getDaysUntilDue());
            });

            // Test return book
            library.returnBook(book1, student);

            // Display library statistics
            library.displayLibraryStatistics();

            // Test librarian-only operations
            System.out.println("\n=== Overdue Books (Librarian Access) ===");
            List<Book> overdueBooks = library.viewOverdueBooks(librarian);
            if (overdueBooks.isEmpty()) {
                System.out.println("No overdue books found.");
            } else {
                overdueBooks.forEach(System.out::println);
            }

        } catch (LibraryException e) {
            System.err.println("Library Operation Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}