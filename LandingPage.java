import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("DonateLife");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Top navbar ────────────────────────────────
        JPanel navbar = new JPanel(null);
        navbar.setPreferredSize(new Dimension(0, 55));
        navbar.setBackground(new Color(255, 240, 240));

        JLabel heartLogo = new JLabel("♥");
        heartLogo.setForeground(new Color(220, 60, 80));
        heartLogo.setFont(new Font("Arial", Font.BOLD, 20));
        heartLogo.setBounds(20, 14, 28, 28);
        navbar.add(heartLogo);

        JLabel brandName = new JLabel("DonateLife");
        brandName.setForeground(new Color(30, 36, 48));
        brandName.setFont(new Font("Arial", Font.BOLD, 16));
        brandName.setBounds(50, 16, 120, 24);
        navbar.add(brandName);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(790, 13, 80, 30);
        loginBtn.setBackground(Color.WHITE);
        loginBtn.setForeground(new Color(232, 96, 44));
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 13));
        loginBtn.setBorder(BorderFactory.createLineBorder(new Color(232, 96, 44), 1));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> {
            new LoginPage();
            dispose();
        });
        navbar.add(loginBtn);

        add(navbar, BorderLayout.NORTH);

        // ── Hero section ──────────────────────────────
        JPanel hero = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background: orange-red to teal
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(220, 80, 60),
                    getWidth(), getHeight(), new Color(30, 160, 140)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        hero.setPreferredSize(new Dimension(0, 380));

        // Left text
        JLabel heading1 = new JLabel("Every Donation");
        heading1.setFont(new Font("Arial", Font.BOLD, 38));
        heading1.setForeground(Color.WHITE);
        heading1.setBounds(40, 40, 420, 50);
        hero.add(heading1);

        JLabel heading2 = new JLabel("Makes a");
        heading2.setFont(new Font("Arial", Font.BOLD, 38));
        heading2.setForeground(Color.WHITE);
        heading2.setBounds(40, 90, 420, 50);
        hero.add(heading2);

        JLabel heading3 = new JLabel("Difference");
        heading3.setFont(new Font("Arial", Font.BOLD, 38));
        heading3.setForeground(Color.WHITE);
        heading3.setBounds(40, 140, 420, 50);
        hero.add(heading3);

        // Wavy underline (drawn as colored line)
        JPanel waveLine = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 180, 0));
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] xs = {0,10,20,30,40,50,60,70,80,90,100,110,120,130,140,150,160,170,180,190,200,210,220,230,240,250,260,270,280,290,300,310,320,330,340,350,360};
                int[] ys = {5, 2, 5, 8, 5, 2, 5, 8, 5, 2,  5,  8,  5,  2,  5,  8,  5,  2,  5,  8,  5,  2,  5,  8,  5,  2,  5,  8,  5,  2,  5,  8,  5,  2,  5,  8,  5};
                for (int i = 0; i < xs.length - 1; i++) {
                    g2.drawLine(xs[i], ys[i], xs[i+1], ys[i+1]);
                }
            }
        };
        waveLine.setOpaque(false);
        waveLine.setBounds(40, 193, 370, 14);
        hero.add(waveLine);

        JLabel subText1 = new JLabel("Join thousands of donors making the world a better place.");
        subText1.setFont(new Font("Arial", Font.PLAIN, 14));
        subText1.setForeground(new Color(255, 255, 255, 220));
        subText1.setBounds(40, 218, 500, 22);
        hero.add(subText1);

        JLabel subText2 = new JLabel("Donate blood, organs, money, or food — every contribution saves lives.");
        subText2.setFont(new Font("Arial", Font.PLAIN, 14));
        subText2.setForeground(new Color(255, 255, 255, 220));
        subText2.setBounds(40, 240, 560, 22);
        hero.add(subText2);

        JButton getStartedBtn = new JButton("Get Started");
        getStartedBtn.setBounds(40, 285, 155, 42);
        getStartedBtn.setBackground(Color.WHITE);
        getStartedBtn.setForeground(new Color(232, 96, 44));
        getStartedBtn.setFocusPainted(false);
        getStartedBtn.setBorderPainted(false);
        getStartedBtn.setFont(new Font("Arial", Font.BOLD, 14));
        getStartedBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        getStartedBtn.addActionListener(e -> {
            new LoginPage();
            dispose();
        });
        hero.add(getStartedBtn);

        // Right illustration box
        JPanel illustBox = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 235, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Draw simple donation illustration with shapes
                // Boxes
                g2.setColor(new Color(232, 96, 44));
                g2.fillRoundRect(160, 130, 70, 60, 8, 8);
                g2.setColor(new Color(255, 140, 80));
                g2.fillRoundRect(80, 150, 60, 50, 8, 8);
                g2.setColor(new Color(30, 160, 140));
                g2.fillRoundRect(240, 100, 55, 80, 8, 8);
                g2.setColor(new Color(255, 100, 80));
                g2.fillRoundRect(310, 120, 45, 65, 8, 8);

                // Coin circle
                g2.setColor(new Color(255, 200, 50));
                g2.fillOval(100, 100, 55, 55);
                g2.setColor(new Color(220, 160, 30));
                g2.drawOval(100, 100, 55, 55);

                // Heart shape (simple)
                g2.setColor(new Color(220, 60, 80));
                g2.fillOval(170, 30, 30, 30);
                g2.fillOval(195, 30, 30, 30);
                int[] hx = {170, 210, 195};
                int[] hy = {50,  50,  75};
                g2.fillPolygon(hx, hy, 3);

                // Donation bag
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(50, 60, 55, 70, 12, 12);
                g2.setColor(new Color(220, 60, 80));
                g2.setFont(new Font("Arial", Font.BOLD, 9));
                g2.drawString("DONAE", 55, 100);

                // Dollar signs
                g2.setColor(new Color(30, 160, 140));
                g2.setFont(new Font("Arial", Font.BOLD, 22));
                g2.drawString("$", 130, 95);

                // Leaves
                g2.setColor(new Color(30, 160, 140));
                g2.fillOval(290, 50, 20, 35);
                g2.fillOval(310, 40, 18, 30);
                g2.fillOval(270, 45, 16, 28);
            }
        };
        illustBox.setBounds(500, 30, 370, 320);
        hero.add(illustBox);

        add(hero, BorderLayout.CENTER);

        // ── Stats bar ─────────────────────────────────
        JPanel statsBar = new JPanel(new GridLayout(1, 4, 0, 0));
        statsBar.setBackground(new Color(245, 243, 238));
        statsBar.setPreferredSize(new Dimension(0, 165));

        String[][] stats = {
            {"5,000+", "Donors"},
            {"12,000+", "Lives Saved"},
            {"₹50L+", "Funds Raised"},
            {"3,200+", "Blood Units"}
        };
        for (String[] s : stats) {
            JPanel statCard = new JPanel(null);
            statCard.setBackground(new Color(245, 243, 238));
            statCard.setBorder(BorderFactory.createLineBorder(new Color(230, 228, 220), 1));

            JLabel num = new JLabel(s[0], SwingConstants.CENTER);
            num.setFont(new Font("Arial", Font.BOLD, 22));
            num.setForeground(new Color(30, 36, 48));
            num.setBounds(0, 45, 225, 35);
            statCard.add(num);

            JLabel lbl = new JLabel(s[1], SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.PLAIN, 13));
            lbl.setForeground(new Color(136, 146, 164));
            lbl.setBounds(0, 82, 225, 22);
            statCard.add(lbl);

            statsBar.add(statCard);
        }

        add(statsBar, BorderLayout.SOUTH);

        setVisible(true);
    }
}