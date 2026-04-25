import java.sql.*;
import java.time.LocalDate;
import java.util.*;

// ================= MODEL =================
class Book {
    int id;
    String name;
    String author;
    boolean issued;
    Integer studentId;
    LocalDate issueDate;

    public Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }
}

// ================= DAO =================
class LibraryDAO {

    public void addBook(Book b) throws Exception {
        String sql = "INSERT INTO books (id, name, author, issued, student_id, issue_date) VALUES (?, ?, ?, false, NULL, NULL)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, b.id);
            ps.setString(2, b.name);
            ps.setString(3, b.author);

            ps.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws Exception {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Book b = new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author")
                );

                b.issued = rs.getBoolean("issued");
                b.studentId = (Integer) rs.getObject("student_id");

                if (rs.getDate("issue_date") != null) {
                    b.issueDate = rs.getDate("issue_date").toLocalDate();
                }

                list.add(b);
            }
        }
        return list;
    }

    public boolean issueBook(int bookId, int studentId) throws Exception {

        String check = "SELECT issued FROM books WHERE id=?";
        String update = "UPDATE books SET issued=true, student_id=?, issue_date=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(check)) {

            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next() && rs.getBoolean("issued")) {
                return false;
            }

            try (PreparedStatement ps2 = con.prepareStatement(update)) {
                ps2.setInt(1, studentId);

                // ✅ FIXED DATE ERROR HERE
                ps2.setDate(2, java.sql.Date.valueOf(LocalDate.now()));

                ps2.setInt(3, bookId);
                ps2.executeUpdate();
            }
        }
        return true;
    }

    public boolean returnBook(int bookId) throws Exception {

        String check = "SELECT issued FROM books WHERE id=?";
        String update = "UPDATE books SET issued=false, student_id=NULL, issue_date=NULL WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(check)) {

            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next() && !rs.getBoolean("issued")) {
                return false;
            }

            try (PreparedStatement ps2 = con.prepareStatement(update)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }
        }
        return true;
    }
}

// ================= SERVICE =================
class LibraryService {

    LibraryDAO dao = new LibraryDAO();

    public void addBook(int id, String name, String author) {
        try {
            dao.addBook(new Book(id, name, author));
            System.out.println("Book added successfully");
        } catch (Exception e) {
            System.out.println("Error adding book");
        }
    }

    public void viewBooks() {
        try {
            List<Book> books = dao.getAllBooks();

            for (Book b : books) {
                System.out.println(
                        b.id + " | " + b.name + " | " + b.author + " | " +
                        (b.issued ? "Issued to " + b.studentId : "Available")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void issueBook(int bookId, int studentId) {
        try {
            if (dao.issueBook(bookId, studentId))
                System.out.println("Book issued");
            else
                System.out.println("Book already issued");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int bookId) {
        try {
            if (dao.returnBook(bookId))
                System.out.println("Book returned");
            else
                System.out.println("Book was not issued");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ================= MAIN =================
public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LibraryService service = new LibraryService();

        while (true) {

            System.out.println("\n1.Add 2.View 3.Issue 4.Return 5.Exit");

            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Author: ");
                    String author = sc.nextLine();

                    service.addBook(id, name, author);
                    break;

                case 2:
                    service.viewBooks();
                    break;

                case 3:
                    System.out.print("Book ID: ");
                    int bid = sc.nextInt();

                    System.out.print("Student ID: ");
                    int sid = sc.nextInt();

                    service.issueBook(bid, sid);
                    break;

                case 4:
                    System.out.print("Book ID: ");
                    int rid = sc.nextInt();

                    service.returnBook(rid);
                    break;

                case 5:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}