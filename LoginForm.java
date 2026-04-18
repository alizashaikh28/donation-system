import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public LoginForm() {
        setTitle("DonateLife - Login");
        setSize(380, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(231, 76, 60));
        header.setBounds(0, 0, 380, 70);
        header.setLayout(null);

        JLabel logo = new JLabel("❤ DonateLife");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 22));
        logo.setBounds(100, 20, 200, 30);
        header.add(logo);
        add(header);

        // Username
        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(50, 95, 100, 25);
        add(userLbl);

        userField = new JTextField();
        userField.setBounds(150, 95, 180, 30);
        add(userField);

        // Password
        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(50, 140, 100, 25);
        add(passLbl);

        passField = new JPasswordField();
        passField.setBounds(150, 140, 180, 30);
        add(passField);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(130, 195, 120, 35);
        loginBtn.setBackground(new Color(231, 76, 60));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        add(loginBtn);

        // Press Enter to login
        passField.addActionListener(e -> doLogin());
        loginBtn.addActionListener(e -> doLogin());

        setVisible(true);
    }

    void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        // Simple hardcoded admin check
        // You can change these credentials as you like
        if (username.equals("admin") && password.equals("admin123")) {
            dispose(); // close login window
            new MainDashboard(); // open dashboard
        } else {
            // Optional: check from database too
            try {
                Connection con = DBConnection.getConnection();
                if (con != null) {
                    PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM admin_users WHERE username=? AND password=?");
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        con.close();
                        dispose();
                        new MainDashboard();
                        return;
                    }
                    con.close();
                }
            } catch (SQLException ex) {
                // Table doesn't exist yet — just use hardcoded login above
            }
            JOptionPane.showMessageDialog(this,
                "Wrong username or password!\nDefault: admin / admin123",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }

    // This is the main entry point — start here, not MainDashboard
    public static void main(String[] args) {
        new LoginForm();
    }
}