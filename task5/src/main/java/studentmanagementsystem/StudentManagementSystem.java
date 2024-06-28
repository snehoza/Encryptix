package studentmanagementsystem;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Student {
    private int enrollmentNo;
    private String name;
    private int sem;
    private String dob;
    private String grade;

    public Student(int enrollmentNo, String name, int sem, String dob, String grade) {
        this.enrollmentNo = enrollmentNo;
        this.name = name;
        this.sem = sem;
        this.dob = dob;
        this.grade = grade;
    }

    public int getEnrollmentNo() {
        return enrollmentNo;
    }

    public String getName() {
        return name;
    }

    public int getSem() {
        return sem;
    }

    public String getDob() {
        return dob;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Enrollment No: " + enrollmentNo + ", Name: " + name + ", Semester: " + sem + ", DOB: " + dob
                + ", Grade: " + grade;
    }
}

public class StudentManagementSystem extends JFrame {
    private static List<Student> students = new ArrayList<>();
    private Connection con;
    private JTextField txtEnrollmentNo, txtName, txtSem, txtDob, txtGrade;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        connectDatabase();

        createGUI();
    }

    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/students", "root", "root123");
            System.out.println("Connected to database."); 
        } catch (Exception e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(240, 248, 255));

        inputPanel.add(new JLabel("Enrollment No:"));
        txtEnrollmentNo = new JTextField();
        inputPanel.add(txtEnrollmentNo);

        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Semester:"));
        txtSem = new JTextField();
        inputPanel.add(txtSem);

        inputPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        txtDob = new JTextField();
        inputPanel.add(txtDob);

        inputPanel.add(new JLabel("Grade:"));
        txtGrade = new JTextField();
        inputPanel.add(txtGrade);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton btnAdd = new JButton("Add Student");
        btnAdd.setBackground(new Color(173, 216, 230));
        btnAdd.addActionListener(e -> addStudent());
        buttonPanel.add(btnAdd);

        JButton btnRemove = new JButton("Remove Student");
        btnRemove.setBackground(new Color(173, 216, 230));
        btnRemove.addActionListener(e -> removeStudent());
        buttonPanel.add(btnRemove);

        JButton btnSearch = new JButton("Search Student");
        btnSearch.setBackground(new Color(173, 216, 230));
        btnSearch.addActionListener(e -> searchStudent());
        buttonPanel.add(btnSearch);

        JButton btnDisplay = new JButton("Display All Students");
        btnDisplay.setBackground(new Color(173, 216, 230));
        btnDisplay.addActionListener(e -> displayAllStudents());
        buttonPanel.add(btnDisplay);

        JButton btnEdit = new JButton("Edit Student");
        btnEdit.setBackground(new Color(173, 216, 230));
        btnEdit.addActionListener(e -> editStudent());
        buttonPanel.add(btnEdit);

        JButton btnClear = new JButton("Clear Fields");
        btnClear.setBackground(new Color(173, 216, 230));
        btnClear.addActionListener(e -> clearFields());
        buttonPanel.add(btnClear);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new String[] { "Enrollment No", "Name", "Semester", "DOB", "Grade" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        addNumericValidation(txtEnrollmentNo);
        addNumericValidation(txtSem);

        add(mainPanel);
    }

    private void clearFields() {
        txtEnrollmentNo.setText("");
        txtName.setText("");
        txtSem.setText("");
        txtDob.setText("");
        txtGrade.setText("");
    }

    private void addNumericValidation(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                char c = ke.getKeyChar();
                if (!(Character.isDigit(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    textField.getToolkit().beep();
                    ke.consume();
                }
            }
        });
    }

    private boolean validateFields() {
        if (txtEnrollmentNo.getText().isEmpty() || txtName.getText().isEmpty() ||
                txtSem.getText().isEmpty() || txtDob.getText().isEmpty() || txtGrade.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isInteger(txtEnrollmentNo.getText(), "Invalid Enrollment No format.") ||
            !isInteger(txtSem.getText(), "Invalid Semester format.") ||
            !isValidDate(txtDob.getText(), "Invalid Date of Birth format.")) {
            return false;
        }

        return true;
    }

    private boolean isInteger(String text, String errorMessage) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean isValidDate(String text, String errorMessage) {
        try {
            Date.valueOf(text);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void addStudent() {
        if (!validateFields()) {
            return;
        }

        String query = "INSERT INTO student (EnrollmentNo, Name, Sem, DOB, Grade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            int enrollmentNo = Integer.parseInt(txtEnrollmentNo.getText());
            String name = txtName.getText();
            int sem = Integer.parseInt(txtSem.getText());
            String dob = txtDob.getText();
            String grade = txtGrade.getText();

            ps.setInt(1, enrollmentNo);
            ps.setString(2, name);
            ps.setInt(3, sem);
            ps.setDate(4, Date.valueOf(dob));
            ps.setString(5, grade);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Data inserted successfully :)");
                Student newStudent = new Student(enrollmentNo, name, sem, dob, grade);
                students.add(newStudent);
                tableModel.addRow(new Object[] { enrollmentNo, name, sem, dob, grade });
            } else {
                JOptionPane.showMessageDialog(this, "Insertion failed :(", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeStudent() {
        String enrollmentNoStr = JOptionPane.showInputDialog(this, "Enter Enrollment No of student you want to delete:");
        if (enrollmentNoStr != null && !enrollmentNoStr.isEmpty()) {
            if (!isInteger(enrollmentNoStr, "Invalid Enrollment No format.")) {
                return;
            }

            int enrollmentNo = Integer.parseInt(enrollmentNoStr);
            String query = "DELETE FROM student WHERE EnrollmentNo = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, enrollmentNo);
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Data deleted successfully :)");
                    students.removeIf(student -> student.getEnrollmentNo() == enrollmentNo);
                    updateTable();
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with the given Enrollment No.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchStudent() {
        String enrollmentNoStr = JOptionPane.showInputDialog(this, "Enter Enrollment No to search:");
        if (enrollmentNoStr != null && !enrollmentNoStr.isEmpty()) {
            if (!isInteger(enrollmentNoStr, "Invalid Enrollment No format.")) {
                return;
            }

            int enrollmentNo = Integer.parseInt(enrollmentNoStr);
            String query = "SELECT * FROM student WHERE EnrollmentNo = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, enrollmentNo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("Name");
                        int sem = rs.getInt("Sem");
                        String dob = rs.getDate("DOB").toString();
                        String grade = rs.getString("Grade");

                        JOptionPane.showMessageDialog(this,
                                "Enrollment No: " + enrollmentNo + "\nName: " + name + "\nSemester: " + sem
                                        + "\nDOB: " + dob + "\nGrade: " + grade);
                    } else {
                        JOptionPane.showMessageDialog(this, "No student found with the given Enrollment No.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error searching student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayAllStudents() {
        String query = "SELECT * FROM student";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            students.clear();
            while (rs.next()) {
                int enrollmentNo = rs.getInt("EnrollmentNo");
                String name = rs.getString("Name");
                int sem = rs.getInt("Sem");
                String dob = rs.getDate("DOB").toString();
                String grade = rs.getString("Grade");
                Student student = new Student(enrollmentNo, name, sem, dob, grade);
                students.add(student);
            }
            updateTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error displaying students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[] { student.getEnrollmentNo(), student.getName(), student.getSem(), student.getDob(), student.getGrade() });
        }
    }

    private void editStudent() {
        if (!validateFields()) {
            return;
        }

        String query = "UPDATE student SET Name = ?, Sem = ?, DOB = ?, Grade = ? WHERE EnrollmentNo = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            String name = txtName.getText();
            int sem = Integer.parseInt(txtSem.getText());
            String dob = txtDob.getText();
            String grade = txtGrade.getText();
            int enrollmentNo = Integer.parseInt(txtEnrollmentNo.getText());

            ps.setString(1, name);
            ps.setInt(2, sem);
            ps.setDate(3, Date.valueOf(dob));
            ps.setString(4, grade);
            ps.setInt(5, enrollmentNo);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Data updated successfully :)");
                for (Student student : students) {
                    if (student.getEnrollmentNo() == enrollmentNo) {
                        students.remove(student);
                        students.add(new Student(enrollmentNo, name, sem, dob, grade));
                        break;
                    }
                }
                updateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed :(", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}
