import java.sql.Connection;
import java.util.Scanner;

public class LibrarySystem {

    public static void main(String[] args) {

        // =========================================
        // STEP 1: CHECK DATABASE CONNECTION
        // =========================================

        // Try to connect to MySQL database
        Connection con = DBConnection.getConnection();

        // If connection fails → stop program
        if (con == null) {
            System.out.println("Database connection FAILED ❌");
            return;  // Exit program
        } else {
            System.out.println("Database connected successfully ✅");
        }

        // =========================================
        // STEP 2: CREATE OBJECTS
        // =========================================

        // Library object (handles all DB operations)
        Library library = new Library();

        // Scanner object (takes user input)
        Scanner sc = new Scanner(System.in);

        // =========================================
        // STEP 3: MENU LOOP (RUNS UNTIL USER EXITS)
        // =========================================

        while (true) {

            // Display menu options
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");

            // Take user choice
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            // =========================================
            // STEP 4: PERFORM ACTION BASED ON CHOICE
            // =========================================

            switch (choice) {

                // -------------------------------------
                // CASE 1: ADD BOOK
                // -------------------------------------
                case 1:
                    System.out.println("Enter Book ID:");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Enter Book Name:");
                    String name = sc.nextLine();

                    System.out.println("Enter Author:");
                    String author = sc.nextLine();

                    // Call method to insert book into database
                    library.addBook(id, name, author);
                    break;

                // -------------------------------------
                // CASE 2: VIEW ALL BOOKS
                // -------------------------------------
                case 2:
                    // Fetch and display all books from DB
                    library.viewBooks();
                    break;

                // -------------------------------------
                // CASE 3: ISSUE BOOK
                // -------------------------------------
                case 3:
                    System.out.println("Enter Book ID to issue:");
                    int bookId = sc.nextInt();

                    System.out.println("Enter Student ID:");
                    int studentId = sc.nextInt();

                    // Call method to issue book
                    library.issueBook(bookId, studentId);
                    break;

                // -------------------------------------
                // CASE 4: RETURN BOOK
                // -------------------------------------
                case 4:
                    System.out.println("Enter Book ID to return:");
                    int returnId = sc.nextInt();

                    // Call method to return book
                    library.returnBook(returnId);
                    break;

                // -------------------------------------
                // CASE 5: EXIT PROGRAM
                // -------------------------------------
                case 5:
                    System.out.println("Exiting system... 👋");
                    System.exit(0);

                // -------------------------------------
                // DEFAULT: INVALID INPUT
                // -------------------------------------
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }
}