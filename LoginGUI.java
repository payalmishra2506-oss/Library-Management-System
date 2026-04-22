import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginGUI extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public LoginGUI() {

        setTitle("Login");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Username:"));
        userField = new JTextField();
        add(userField);

        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, userField.getText());
            ps.setString(2, String.valueOf(passField.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new LibraryGUI();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}