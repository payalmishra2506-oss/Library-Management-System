import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class LibraryGUI extends JFrame {

    JTable table;
    DefaultTableModel model;
    String role;

    JLabel totalLabel, issuedLabel;

    public LibraryGUI(String role) {

        this.role = role;

        setTitle("Library Management System");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== STATS =====
        JPanel stats = new JPanel();
        stats.setBackground(new Color(230, 240, 255));

        totalLabel = new JLabel("Total: 0");
        issuedLabel = new JLabel("Issued: 0");

        stats.add(totalLabel);
        stats.add(issuedLabel);

        add(stats, BorderLayout.NORTH);

        // ===== TOP PANEL =====
        JPanel top = new JPanel(new FlowLayout());

        JButton addBtn = createButton("Add");
        JButton updateBtn = createButton("Update");
        JButton deleteBtn = createButton("Delete");
        JButton issueBtn = createButton("Issue");
        JButton returnBtn = createButton("Return");
        JButton refreshBtn = createButton("Refresh");
        JButton studentsBtn = createButton("Students");

        JTextField searchField = new JTextField(12);
        JButton clearBtn = createButton("Clear");

        JComboBox<String> filterBox = new JComboBox<>(new String[]{"All", "Issued", "Available"});

        top.add(addBtn);
        top.add(updateBtn);
        top.add(deleteBtn);
        top.add(issueBtn);
        top.add(returnBtn);
        top.add(refreshBtn);
        top.add(studentsBtn);
        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(clearBtn);
        top.add(filterBox);

        add(top, BorderLayout.SOUTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Author", "Category", "Issued"},
                0
        );

        table = new JTable(model);

        table.setRowHeight(28);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 255));
                }

                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();

        // ===== ACTIONS =====
        addBtn.addActionListener(e -> showAddBookForm());
        updateBtn.addActionListener(e -> updateBook());
        deleteBtn.addActionListener(e -> deleteBook());
        issueBtn.addActionListener(e -> issueBook());
        returnBtn.addActionListener(e -> returnBook());
        refreshBtn.addActionListener(e -> refreshTable());
        studentsBtn.addActionListener(e -> showStudents());

        // SEARCH
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBooks(searchField.getText());
            }
        });

        // CLEAR
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        // FILTER
        filterBox.addActionListener(e -> filterBooks((String) filterBox.getSelectedItem()));

        // 🔒 ROLE RESTRICTION
        if (role.equalsIgnoreCase("STUDENT")) {
            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            issueBtn.setEnabled(false);
            returnBtn.setEnabled(false);
            studentsBtn.setVisible(false);
        }

        setVisible(true);
    }

    // ===== BUTTON STYLE =====
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    // ===== ADD FORM =====
    private void showAddBookForm() {

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Book",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());

                String sql = "INSERT INTO books VALUES (?, ?, ?, ?, false, NULL, NULL)";

                try (Connection con = DB.getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setInt(1, id);
                    ps.setString(2, nameField.getText());
                    ps.setString(3, authorField.getText());
                    ps.setString(4, categoryField.getText());
                    ps.executeUpdate();
                }

                refreshTable();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    // ===== UPDATE =====
    private void updateBook() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);
            String name = JOptionPane.showInputDialog("New Name:");
            String author = JOptionPane.showInputDialog("New Author:");

            String sql = "UPDATE books SET name=?, author=? WHERE id=?";

            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, author);
                ps.setInt(3, id);
                ps.executeUpdate();
            }

            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== DELETE =====
    private void deleteBook() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);

            String sql = "DELETE FROM books WHERE id=?";

            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== ISSUE =====
    private void issueBook() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);
            String roll = JOptionPane.showInputDialog("Enter Student Roll No");

            String sql = "UPDATE books SET issued=true, student_id=?, issue_date=CURDATE() WHERE id=?";

            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, roll);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== RETURN =====
    private void returnBook() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);

            String sql = "UPDATE books SET issued=false, student_id=NULL, issue_date=NULL WHERE id=?";

            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== REFRESH =====
    private void refreshTable() {
        try (Connection con = DB.getConnection();
             ResultSet rs = con.createStatement().executeQuery("SELECT * FROM books")) {

            model.setRowCount(0);

            int total = 0, issued = 0;

            while (rs.next()) {
                boolean isIssued = rs.getBoolean("issued");

                if (isIssued) issued++;
                total++;

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("category"),
                        isIssued ? "Yes" : "No"
                });
            }

            totalLabel.setText("Total: " + total);
            issuedLabel.setText("Issued: " + issued);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== SEARCH =====
    private void searchBooks(String keyword) {
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM books WHERE name LIKE ? OR author LIKE ? OR category LIKE ?")) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getBoolean("issued") ? "Yes" : "No"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== FILTER =====
    private void filterBooks(String type) {
        String query = "SELECT * FROM books";

        if (type.equals("Issued")) query += " WHERE issued=true";
        if (type.equals("Available")) query += " WHERE issued=false";

        try (Connection con = DB.getConnection();
             ResultSet rs = con.createStatement().executeQuery(query)) {

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getBoolean("issued") ? "Yes" : "No"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ===== STUDENTS =====
    private void showStudents() {

        DefaultTableModel studentModel = new DefaultTableModel(
                new String[]{"Username", "Roll No"},
                0
        );

        JTable studentTable = new JTable(studentModel);

        try (Connection con = DB.getConnection();
             ResultSet rs = con.createStatement().executeQuery(
                     "SELECT username, roll_no FROM users WHERE role='STUDENT'")) {

            while (rs.next()) {
                studentModel.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("roll_no")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(studentTable), "Students", JOptionPane.PLAIN_MESSAGE);
    }
}