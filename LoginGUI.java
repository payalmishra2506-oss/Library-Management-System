import java.awt.*;
import java.sql.*;
import javax.swing.*;

// ================= DB CONNECTION =================
class DB {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb",
                "root",
                "b1X#4jW*2f"
        );
    }
}

// ================= LOGIN GUI =================
public class LoginGUI extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;

    public LoginGUI() {

        setTitle("Library Login");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel title = new JLabel("Library Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel, BorderLayout.CENTER);

        // ===== ACTIONS =====
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        setVisible(true);
    }

    // ================= LOGIN =================
    private void login() {

        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }

        String sql = "SELECT role FROM users WHERE username=? AND password=?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String role = rs.getString("role");

                JOptionPane.showMessageDialog(this, "Login Successful (" + role + ")");
                dispose();

                new LibraryGUI(role); // ✅ opens working library UI

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= REGISTER =================
    private void register() {

        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields");
            return;
        }

        String roll = JOptionPane.showInputDialog(this, "Enter Roll Number");

        if (roll == null || roll.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number required!");
            return;
        }

        String check = "SELECT * FROM users WHERE username=?";
        String insert = "INSERT INTO users (username, password, role, roll_no) VALUES (?, ?, 'STUDENT', ?)";

        try (Connection con = DB.getConnection()) {

            // check duplicate
            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setString(1, user);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }

            // insert new student
            PreparedStatement ps2 = con.prepareStatement(insert);
            ps2.setString(1, user);
            ps2.setString(2, pass);
            ps2.setString(3, roll);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registered Successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new LoginGUI();
    }
}