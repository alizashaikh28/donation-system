import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;

public class UserDashboard extends JFrame {

    private String username;

    public UserDashboard(String username) {
        this.username = username;
        setTitle("DonateLife - User Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 243, 238));

        add(buildTopBar(), BorderLayout.NORTH);

        // Scrollable main content
        JPanel mainContent = buildMainContent();
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(245, 243, 238));
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // ── Top navigation bar ────────────────────────────
    JPanel buildTopBar() {
        JPanel bar = new JPanel(null);
        bar.setPreferredSize(new Dimension(0, 58));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 228, 220)));

        // Heart + brand
        JLabel heart = new JLabel("♥");
        heart.setForeground(new Color(220, 60, 80));
        heart.setFont(new Font("Arial", Font.BOLD, 22));
        heart.setBounds(20, 15, 28, 28);
        bar.add(heart);

        JLabel brand = new JLabel("DonateLife");
        brand.setFont(new Font("Arial", Font.BOLD, 16));
        brand.setForeground(new Color(220, 60, 80));
        brand.setBounds(50, 17, 120, 24);
        bar.add(brand);

        // User icon + name (right side)
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        userIcon.setBounds(900, 17, 28, 24);
        bar.add(userIcon);

        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userLabel.setForeground(new Color(55, 65, 81));
        userLabel.setBounds(930, 17, 100, 24);
        bar.add(userLabel);

        JButton logoutBtn = new JButton("⇒ Logout");
        logoutBtn.setBounds(1030, 13, 100, 32);
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(new Color(55, 65, 81));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(210, 207, 200), 1));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LandingPage();
        });
        bar.add(logoutBtn);

        return bar;
    }

    // ── Main scrollable content ───────────────────────
    JPanel buildMainContent() {
        JPanel content = new JPanel(null);
        content.setBackground(new Color(245, 243, 238));
        content.setPreferredSize(new Dimension(1080, 1100));

        int y = 24;
        int sideMargin = 30;
        int contentWidth = 1020;

        // ── Welcome banner ────────────────────────────
        JPanel banner = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(232, 96, 44),
                    getWidth(), getHeight(), new Color(30, 160, 140)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        banner.setOpaque(false);
        banner.setBounds(sideMargin, y, contentWidth, 110);

        JLabel welcomeText = new JLabel("Welcome, " + username + "!  \uD83D\uDC4B");
        welcomeText.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeText.setForeground(Color.WHITE);
        welcomeText.setBounds(28, 22, 700, 36);
        banner.add(welcomeText);

        JLabel subText = new JLabel("Thank you for being a generous donor. Here's your donation overview.");
        subText.setFont(new Font("Arial", Font.PLAIN, 14));
        subText.setForeground(new Color(255, 255, 255, 210));
        subText.setBounds(28, 60, 700, 22);
        banner.add(subText);

        content.add(banner);
        y += 130;

        // ── Stat cards ─────────────────────────────────
        String[] statLabels = {"My Donations", "Amount Given", "Active Campaigns"};
        String[] statValues = getStatValues();
        Color[] statColors  = {
            new Color(232, 96, 44),
            new Color(26, 176, 137),
            new Color(229, 75, 107)
        };
        String[] statIcons  = {"🎁", "💳", "🎯"};

        int cardW = (contentWidth - 40) / 3;
        for (int i = 0; i < 3; i++) {
            final Color cardColor = statColors[i];
            JPanel card = new JPanel(null) {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(cardColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                }
            };
            card.setOpaque(false);
            card.setBounds(sideMargin + i * (cardW + 20), y, cardW, 120);

            JLabel icon = new JLabel(statIcons[i]);
            icon.setFont(new Font("Arial", Font.PLAIN, 24));
            icon.setBounds(20, 18, 40, 32);
            card.add(icon);

            JLabel val = new JLabel(statValues[i]);
            val.setFont(new Font("Arial", Font.BOLD, 26));
            val.setForeground(Color.WHITE);
            val.setBounds(20, 52, cardW - 30, 34);
            card.add(val);

            JLabel lbl = new JLabel(statLabels[i]);
            lbl.setFont(new Font("Arial", Font.PLAIN, 12));
            lbl.setForeground(new Color(255, 255, 255, 200));
            lbl.setBounds(20, 86, cardW - 30, 20);
            card.add(lbl);

            content.add(card);
        }
        y += 142;

        // ── Donation type cards ────────────────────────
        String[] typeNames  = {"Blood Donation", "Money Donation", "Organ Donation"};
        String[] typeEmojis = {"🩸", "💰", "🫀"};
        Color[]  typeColors = {
            new Color(220, 60, 80),
            new Color(26, 176, 137),
            new Color(34, 120, 200)
        };

        for (int i = 0; i < 3; i++) {
            final Color borderCol = typeColors[i];
            JPanel typeCard = new JPanel(null) {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(borderCol);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 14, 14);
                }
            };
            typeCard.setOpaque(false);
            typeCard.setBounds(sideMargin + i * (cardW + 20), y, cardW, 110);

            JLabel emoji = new JLabel(typeEmojis[i], SwingConstants.CENTER);
            emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            emoji.setBounds(0, 16, cardW, 44);
            typeCard.add(emoji);

            JLabel name = new JLabel(typeNames[i], SwingConstants.CENTER);
            name.setFont(new Font("Arial", Font.BOLD, 14));
            name.setForeground(new Color(30, 36, 48));
            name.setBounds(0, 64, cardW, 24);
            typeCard.add(name);

            content.add(typeCard);
        }
        y += 134;

        // ── My Donations table ─────────────────────────
        content.add(buildSectionLabel("My Donations", sideMargin, y));
        y += 36;

        JPanel donationsCard = buildTableCard(
            new String[]{"ID", "Type", "Amount", "Date", "Status"},
            "SELECT donation_id, donation_type, amount, donation_date, status FROM donation ORDER BY donation_date DESC",
            true
        );
        donationsCard.setBounds(sideMargin, y, contentWidth, 130);
        content.add(donationsCard);
        y += 150;

        // ── My Payments table ──────────────────────────
        content.add(buildSectionLabel("My Payments", sideMargin, y));
        y += 36;

        JPanel paymentsCard = buildTableCard(
            new String[]{"ID", "Mode", "Date", "Status", "Ref"},
            "SELECT payment_id, payment_mode, payment_date, status, transaction_ref FROM payment ORDER BY payment_date DESC",
            true
        );
        paymentsCard.setBounds(sideMargin, y, contentWidth, 130);
        content.add(paymentsCard);
        y += 150;

        // ── Active Campaigns ───────────────────────────
        content.add(buildSectionLabel("Active Campaigns", sideMargin, y));
        y += 36;

        JPanel campaignsPanel = buildCampaignsPanel(sideMargin, y, contentWidth, cardW);
        campaignsPanel.setBounds(sideMargin, y, contentWidth, 200);
        content.add(campaignsPanel);

        return content;
    }

    // ── Section label helper ──────────────────────────
    JLabel buildSectionLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(new Color(30, 36, 48));
        lbl.setBounds(x, y, 400, 28);
        return lbl;
    }

    // ── White card with table ─────────────────────────
    JPanel buildTableCard(String[] columns, String sql, boolean showStatusBadge) {
        JPanel card = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleUserTable(table);

        // Status badge renderer for last status column
        if (showStatusBadge) {
            int statusCol = columns.length - 2; // second to last col
            table.getColumnModel().getColumn(statusCol).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JLabel lbl = new JLabel(val != null ? val.toString() : "");
                    lbl.setOpaque(true);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setFont(new Font("Arial", Font.BOLD, 11));
                    String v = val != null ? val.toString().toLowerCase() : "";
                    if (v.contains("complet") || v.contains("success")) {
                        lbl.setBackground(new Color(26, 176, 137));
                        lbl.setForeground(Color.WHITE);
                    } else if (v.contains("pend")) {
                        lbl.setBackground(new Color(255, 180, 50));
                        lbl.setForeground(Color.WHITE);
                    } else if (v.contains("fail") || v.contains("cancel")) {
                        lbl.setBackground(new Color(229, 75, 107));
                        lbl.setForeground(Color.WHITE);
                    } else {
                        lbl.setBackground(new Color(200, 200, 200));
                        lbl.setForeground(Color.WHITE);
                    }
                    // Round pill via border padding
                    lbl.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
                    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 4));
                    wrapper.setBackground(sel ? new Color(245, 243, 238) : Color.WHITE);
                    wrapper.add(lbl);
                    return wrapper;
                }
            });
        }

        try {
            Connection con = DBConnection.getConnection();
            if (con != null) {
                ResultSet rs = con.prepareStatement(sql).executeQuery();
                int cols = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        Object obj = rs.getObject(i + 1);
                        if (obj instanceof Double) {
                            double d = (Double) obj;
                            row[i] = d == 0 ? "—" : "₹" + (int) d;
                        } else {
                            row[i] = obj;
                        }
                    }
                    model.addRow(row);
                }
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    // ── Campaign cards panel ──────────────────────────
    JPanel buildCampaignsPanel(int sx, int sy, int totalW, int cardW) {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        try {
            Connection con = DBConnection.getConnection();
            if (con != null) {
                String sql = "SELECT name, type, start_date, end_date, target_amount FROM campaign LIMIT 3";
                ResultSet rs = con.prepareStatement(sql).executeQuery();
                int i = 0;
                while (rs.next() && i < 3) {
                    final String cName   = rs.getString("name");
                    final String cType   = rs.getString("type");
                    final String cStart  = rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "";
                    final String cEnd    = rs.getDate("end_date")   != null ? rs.getDate("end_date").toString()   : "";
                    final double target  = rs.getDouble("target_amount");
                    final int idx = i;

                    JPanel cCard = new JPanel(null) {
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(Color.WHITE);
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                        }
                    };
                    cCard.setOpaque(false);
                    cCard.setBounds(idx * (cardW + 20), 0, cardW, 185);

                    JLabel nameL = new JLabel(cName);
                    nameL.setFont(new Font("Arial", Font.BOLD, 14));
                    nameL.setForeground(new Color(30, 36, 48));
                    nameL.setBounds(16, 16, cardW - 32, 22);
                    cCard.add(nameL);

                    // Type badge
                    JLabel typeBadge = new JLabel(cType);
                    typeBadge.setFont(new Font("Arial", Font.PLAIN, 11));
                    typeBadge.setForeground(new Color(55, 65, 81));
                    typeBadge.setOpaque(true);
                    typeBadge.setBackground(new Color(240, 240, 240));
                    typeBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                    typeBadge.setBounds(16, 44, 120, 22);
                    cCard.add(typeBadge);

                    JLabel dateL = new JLabel(cStart + " → " + cEnd);
                    dateL.setFont(new Font("Arial", Font.PLAIN, 12));
                    dateL.setForeground(new Color(136, 146, 164));
                    dateL.setBounds(16, 76, cardW - 32, 20);
                    cCard.add(dateL);

                    if (target > 0) {
                        JLabel targetL = new JLabel("Target: ₹" + String.format("%,.0f", target));
                        targetL.setFont(new Font("Arial", Font.BOLD, 13));
                        targetL.setForeground(new Color(26, 176, 137));
                        targetL.setBounds(16, 104, cardW - 32, 22);
                        cCard.add(targetL);
                    }

                    panel.add(cCard);
                    i++;
                }
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return panel;
    }

    // ── Stat values from DB ───────────────────────────
    String[] getStatValues() {
        String[] vals = {"0", "₹0", "0"};
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return vals;

            ResultSet r1 = con.prepareStatement("SELECT COUNT(*) FROM donation").executeQuery();
            if (r1.next()) vals[0] = String.valueOf(r1.getInt(1));

            ResultSet r2 = con.prepareStatement("SELECT SUM(amount) FROM donation").executeQuery();
            if (r2.next()) {
                double sum = r2.getDouble(1);
                vals[1] = "₹" + String.format("%,.0f", sum);
            }

            ResultSet r3 = con.prepareStatement("SELECT COUNT(*) FROM campaign").executeQuery();
            if (r3.next()) vals[2] = String.valueOf(r3.getInt(1));

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vals;
    }

    // ── Table styling ─────────────────────────────────
    void styleUserTable(JTable table) {
        table.setRowHeight(38);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(55, 65, 81));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(245, 243, 238));
        table.setSelectionForeground(new Color(30, 36, 48));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(136, 146, 164));
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(232, 230, 224))
        );
    }
}