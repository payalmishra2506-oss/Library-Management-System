import java.awt.*;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI extends JFrame {

    Library library = new Library();

    DefaultTableModel model;
    JTable table;

    CardLayout cardLayout;
    JPanel mainPanel;

    public LibraryGUI() {

        setTitle("Library Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sideBar = new JPanel(new GridLayout(5, 1, 10, 10));
        sideBar.setBackground(new Color(30, 100, 30));

        JButton dashboardBtn = new JButton("Dashboard");
        JButton booksBtn = new JButton("Books");
        JButton studentsBtn = new JButton("Students");
        JButton refreshBtn = new JButton("Refresh");

        sideBar.add(dashboardBtn);
        sideBar.add(booksBtn);
        sideBar.add(studentsBtn);
        sideBar.add(refreshBtn);

        add(sideBar, BorderLayout.WEST);

        // ================= MAIN PANEL =================
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createDashboard(), "Dashboard");
        mainPanel.add(createBooksPanel(), "Books");
        mainPanel.add(createStudentsPanel(), "Students");

        add(mainPanel, BorderLayout.CENTER);

        dashboardBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        booksBtn.addActionListener(e -> cardLayout.show(mainPanel, "Books"));
        studentsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Students"));

        refreshBtn.addActionListener(e -> {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Data Refreshed");
        });

        setVisible(true);
    }

    // ================= DASHBOARD =================
    private JPanel createDashboard() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        panel.add(createCard("Total Books", library.countBooks()));
        panel.add(createCard("Issued Books", library.countIssued()));
        panel.add(createCard("Students", library.countStudents()));

        return panel;
    }

    private JPanel createCard(String title, int value) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(144, 238, 144));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        JLabel valueLabel = new JLabel(String.valueOf(value), JLabel.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= BOOKS PANEL =================
    private JPanel createBooksPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        JTextField searchField = new JTextField(15);
        searchField.setToolTipText("Search by name or author");

        JButton searchBtn = new JButton("Search");

        top.add(addBtn);
        top.add(updateBtn);
        top.add(deleteBtn);
        top.add(searchField);
        top.add(searchBtn);

        panel.add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Author", "Issued"}, 0
        );

        table = new JTable(model);
        table.setSelectionBackground(Color.GREEN);
        table.setFillsViewportHeight(true);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();

        // ================= ADD =================
        addBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter ID");
            if (idStr == null) return;

            String name = JOptionPane.showInputDialog("Enter Name");
            if (name == null || name.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog("Enter Author");
            if (author == null || author.trim().isEmpty()) return;

            try {
                int id = Integer.parseInt(idStr);
                library.addBook(id, name, author);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        // ================= UPDATE =================
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            String currentName = (String) model.getValueAt(row, 1);
            String currentAuthor = (String) model.getValueAt(row, 2);

            String name = JOptionPane.showInputDialog("New Name", currentName);
            if (name == null || name.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog("New Author", currentAuthor);
            if (author == null || author.trim().isEmpty()) return;

            library.updateBook(id, name, author);
            refreshTable();
        });

        // ================= DELETE =================
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?");
            if (confirm != JOptionPane.YES_OPTION) return;

            int id = (int) model.getValueAt(row, 0);

            library.deleteBook(id);
            refreshTable();
        });

        // ================= SEARCH =================
        searchBtn.addActionListener(e -> {
            try {
                model.setRowCount(0);
                ResultSet rs = library.searchBooks(searchField.getText());

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("author"),
                            rs.getBoolean("issued") ? "Yes" : "No"
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        return panel;
    }

    // ================= STUDENTS PANEL =================
    private JPanel createStudentsPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        DefaultTableModel studentModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Course"}, 0
        );

        JTable studentTable = new JTable(studentModel);

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        refreshStudents(studentModel);

        return panel;
    }

    // ================= REFRESH BOOK TABLE =================
    private void refreshTable() {
        try {
            model.setRowCount(0);

            ResultSet rs = library.getAllBooks();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getBoolean("issued") ? "Yes" : "No"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= REFRESH STUDENTS =================
    private void refreshStudents(DefaultTableModel model) {
        try {
            ResultSet rs = library.getAllStudents();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new LibraryGUI();
    }
}