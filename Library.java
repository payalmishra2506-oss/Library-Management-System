import java.sql.*;

public class Library {

    // ADD BOOK
    public void addBook(int id, String name, String author) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO books (id, name, author, issued) VALUES (?, ?, ?, false)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, author);

            ps.executeUpdate();
            System.out.println("Book Added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE BOOK
    public void updateBook(int id, String name, String author) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE books SET name=?, author=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, author);
            ps.setInt(3, id);

            ps.executeUpdate();
            System.out.println("Book Updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE BOOK
    public void deleteBook(int id) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM books WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Book Deleted");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public void issueBook(int bookId, int studentId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE books SET issued=true WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bookId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RETURN BOOK
    public void returnBook(int bookId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE books SET issued=false WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, bookId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL BOOKS
    public ResultSet getAllBooks() throws Exception {
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        return st.executeQuery("SELECT * FROM books");
    }

    // SEARCH BOOKS
    public ResultSet searchBooks(String keyword) throws Exception {
        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM books WHERE name LIKE ? OR author LIKE ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");

        return ps.executeQuery();
    }

    // COUNT METHODS
    public int countBooks() {
        return countQuery("SELECT COUNT(*) FROM books");
    }

    public int countIssued() {
        return countQuery("SELECT COUNT(*) FROM books WHERE issued=true");
    }

    public int countStudents() {
        return 0; // optional
    }

    private int countQuery(String query) {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}