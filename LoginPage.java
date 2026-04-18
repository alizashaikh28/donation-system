import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    private boolean isAdminMode = false;
    private JLabel modeTitle;
    private JButton signInBtn;
    private JButton userTab, adminTab;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("DonateLife - Login");
        setSize(520, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient background panel
        JPanel bg = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(220, 80, 60),
                    getWidth(), getHeight(), new Color(30, 160, 140)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(null);

        // White card
        JPanel card = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBounds(60, 40, 390, 460);

        // Heart icon
        JLabel heart = new JLabel("♥", SwingConstants.CENTER);
        heart.setForeground(new Color(220, 60, 80));
        heart.setFont(new Font("Arial", Font.BOLD, 36));
        heart.setBounds(0, 20, 390, 46);
        card.add(heart);

        // Title
        JLabel title = new JLabel("Welcome to DonateLife", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(30, 36, 48));
        title.setBounds(0, 70, 390, 26);
        card.add(title);

        JLabel subtitle = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(136, 146, 164));
        subtitle.setBounds(0, 98, 390, 22);
        card.add(subtitle);

        // Tab strip background
        JPanel tabBg = new JPanel(null);
        tabBg.setBounds(0, 128, 390, 40);
        tabBg.setBackground(new Color(240, 240, 240));

        userTab = new JButton("  ♟  User Login");
        userTab.setBounds(0, 0, 195, 40);
        userTab.setBackground(new Color(26, 176, 137));
        userTab.setForeground(Color.WHITE);
        userTab.setFocusPainted(false);
        userTab.setBorderPainted(false);
        userTab.setFont(new Font("Arial", Font.BOLD, 13));
        userTab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        adminTab = new JButton("  ⊙  Admin Login");
        adminTab.setBounds(195, 0, 195, 40);
        adminTab.setBackground(new Color(240, 240, 240));
        adminTab.setForeground(new Color(100, 100, 100));
        adminTab.setFocusPainted(false);
        adminTab.setBorderPainted(false);
        adminTab.setFont(new Font("Arial", Font.PLAIN, 13));
        adminTab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        tabBg.add(userTab);
        tabBg.add(adminTab);
        card.add(tabBg);

        // Username label + field
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Arial", Font.BOLD, 13));
        userLbl.setForeground(new Color(30, 36, 48));
        userLbl.setBounds(30, 182, 330, 20);
        card.add(userLbl);

        usernameField = new JTextField();
        usernameField.setBounds(30, 205, 330, 40);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 13));
        usernameField.setBackground(new Color(245, 243, 238));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 207, 200), 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        card.add(usernameField);

        // Password label + field
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Arial", Font.BOLD, 13));
        passLbl.setForeground(new Color(30, 36, 48));
        passLbl.setBounds(30, 258, 330, 20);
        card.add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, 280, 330, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBackground(new Color(245, 243, 238));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 207, 200), 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        card.add(passwordField);

        // Sign in button
        signInBtn = new JButton("Sign In as User");
        signInBtn.setBounds(30, 338, 330, 44);
        signInBtn.setBackground(new Color(232, 96, 44));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setFocusPainted(false);
        signInBtn.setBorderPainted(false);
        signInBtn.setFont(new Font("Arial", Font.BOLD, 14));
        signInBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(signInBtn);

        // Demo hint
        JLabel demo = new JLabel("Demo: Enter any credentials to login", SwingConstants.CENTER);
        demo.setFont(new Font("Arial", Font.PLAIN, 12));
        demo.setForeground(new Color(136, 146, 164));
        demo.setBounds(0, 396, 390, 20);
        card.add(demo);

        bg.add(card);
        add(bg, BorderLayout.CENTER);

        // ── Tab switching ──────────────────────────────
        userTab.addActionListener(e -> {
            isAdminMode = false;
            userTab.setBackground(new Color(26, 176, 137));
            userTab.setForeground(Color.WHITE);
            userTab.setFont(new Font("Arial", Font.BOLD, 13));
            adminTab.setBackground(new Color(240, 240, 240));
            adminTab.setForeground(new Color(100, 100, 100));
            adminTab.setFont(new Font("Arial", Font.PLAIN, 13));
            signInBtn.setText("Sign In as User");
            signInBtn.setBackground(new Color(232, 96, 44));
        });

        adminTab.addActionListener(e -> {
            isAdminMode = true;
            adminTab.setBackground(new Color(232, 96, 44));
            adminTab.setForeground(Color.WHITE);
            adminTab.setFont(new Font("Arial", Font.BOLD, 13));
            userTab.setBackground(new Color(240, 240, 240));
            userTab.setForeground(new Color(100, 100, 100));
            userTab.setFont(new Font("Arial", Font.PLAIN, 13));
            signInBtn.setText("Sign In as Admin");
            signInBtn.setBackground(new Color(232, 96, 44));
        });

        // ── Login action ──────────────────────────────
        signInBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }

            // Demo mode: any credentials work
            if (isAdminMode) {
                new MainDashboard();
            } else {
                new UserDashboard(user);
            }
            dispose();
        });

        setVisible(true);
    }
}