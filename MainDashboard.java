import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MainDashboard extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JLabel totalDonorsVal;
    private JLabel totalDonationsVal;
    private JLabel amountRaisedVal;
    private JLabel completedVal;
    private DefaultTableModel recentModel;

    public MainDashboard() {
        setTitle("DonateLife - Admin Panel");
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 36, 48));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(null);

        JLabel heart = new JLabel("♥");
        heart.setForeground(new Color(232, 96, 44));
        heart.setFont(new Font("Arial", Font.BOLD, 22));
        heart.setBounds(16, 18, 30, 30);
        sidebar.add(heart);

        JLabel logoText = new JLabel("DonateLife");
        logoText.setForeground(Color.WHITE);
        logoText.setFont(new Font("Arial", Font.BOLD, 15));
        logoText.setBounds(48, 22, 130, 24);
        sidebar.add(logoText);

        JLabel adminLabel = new JLabel("Admin Panel");
        adminLabel.setForeground(new Color(136, 146, 164));
        adminLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        adminLabel.setBounds(16, 52, 160, 18);
        sidebar.add(adminLabel);

        String[] navNames = {"Dashboard","Donors","Donations","Payments","Receipts","Campaigns","Beneficiaries"};
        int[] navY = {82, 128, 164, 200, 236, 272, 308};
        JButton[] navBtns = new JButton[navNames.length];

        for (int i = 0; i < navNames.length; i++) {
            JButton btn = new JButton(navNames[i]);
            btn.setBounds(8, navY[i], 184, 34);
            btn.setForeground(new Color(136, 146, 164));
            btn.setBackground(new Color(30, 36, 48));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final String page = navNames[i];
            final int idx = i;
            btn.addActionListener(e -> {
                switchPage(page);
                highlightNav(navBtns, idx);
                if (page.equals("Dashboard")) refreshDashboardStats();
            });
            navBtns[i] = btn;
            sidebar.add(btn);
        }

        highlightNav(navBtns, 0);

        JLabel loggedIn = new JLabel("Logged in as");
        loggedIn.setForeground(new Color(136, 146, 164));
        loggedIn.setFont(new Font("Arial", Font.PLAIN, 11));
        loggedIn.setBounds(16, 560, 160, 16);
        sidebar.add(loggedIn);

        JButton logoutBtn = new JButton("⇒  Logout");
        logoutBtn.setBounds(8, 578, 184, 28);
        logoutBtn.setForeground(new Color(136, 146, 164));
        logoutBtn.setBackground(new Color(30, 36, 48));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.addActionListener(e -> System.exit(0));
        sidebar.add(logoutBtn);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 243, 238));

        contentPanel.add(buildDashboardPage(), "Dashboard");
        contentPanel.add(buildDonorsPage(), "Donors");
        contentPanel.add(buildDonationsPage(), "Donations");
        contentPanel.add(buildTablePage("Payments",
            new String[]{"ID","Donation ID","Status","Transaction Ref"},
            "SELECT payment_id, donation_id, status, transaction_ref FROM payment"), "Payments");
        contentPanel.add(buildTablePage("Receipts",
            new String[]{"Receipt ID","Payment ID","Date"},
            "SELECT receipt_id, payment_id, receipt_date FROM receipt"), "Receipts");
        contentPanel.add(buildTablePage("Campaigns",
            new String[]{"ID","Name","Type","Start","End","Target"},
            "SELECT campaign_id, name, type, start_date, end_date, target_amount FROM campaign"), "Campaigns");
        contentPanel.add(buildTablePage("Beneficiaries",
            new String[]{"ID","Name","Contact","Address","Type"},
            "SELECT beneficiary_id, name, contact, address, type FROM beneficiary"), "Beneficiaries");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    void highlightNav(JButton[] btns, int active) {
        for (int i = 0; i < btns.length; i++) {
            if (i == active) {
                btns[i].setBackground(new Color(232, 96, 44));
                btns[i].setForeground(Color.WHITE);
            } else {
                btns[i].setBackground(new Color(30, 36, 48));
                btns[i].setForeground(new Color(136, 146, 164));
            }
        }
    }

    void switchPage(String name) {
        cardLayout.show(contentPanel, name);
    }

    void refreshDashboardStats() {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;

            ResultSet r1 = con.prepareStatement("SELECT COUNT(*) FROM donor").executeQuery();
            if (r1.next()) totalDonorsVal.setText(String.valueOf(r1.getInt(1)));

            ResultSet r2 = con.prepareStatement("SELECT COUNT(*) FROM donation").executeQuery();
            if (r2.next()) totalDonationsVal.setText(String.valueOf(r2.getInt(1)));

            ResultSet r3 = con.prepareStatement("SELECT COALESCE(SUM(amount),0) FROM donation").executeQuery();
            if (r3.next()) amountRaisedVal.setText("₹" + String.format("%,.0f", r3.getDouble(1)));

            ResultSet r4 = con.prepareStatement(
                "SELECT COUNT(*) FROM donation WHERE status='Completed'").executeQuery();
            if (r4.next()) completedVal.setText(String.valueOf(r4.getInt(1)));

            con.close();
        } catch (SQLException e) { e.printStackTrace(); }
        loadRecentDonations(recentModel);
    }

    // ── Dashboard page ────────────────────────────────
    JPanel buildDashboardPage() {
        JPanel page = new JPanel(null);
        page.setBackground(new Color(245, 243, 238));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 36, 48));
        title.setBounds(24, 20, 400, 30);
        page.add(title);

        JButton refreshBtn = new JButton("⟳  Refresh");
        refreshBtn.setBounds(630, 18, 120, 32);
        refreshBtn.setBackground(new Color(26, 176, 137));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshDashboardStats());
        page.add(refreshBtn);

        String[] statLabels = {"Total Donors","Total Donations","Amount Raised","Completed"};
        Color[] statColors = {
            new Color(232, 96, 44), new Color(26, 176, 137),
            new Color(229, 75, 107), new Color(34, 168, 110)
        };
        String[] statValues = loadStatValues();

        for (int i = 0; i < 4; i++) {
            final Color cardColor = statColors[i];
            JPanel roundCard = new JPanel(null) {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(cardColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                }
            };
            roundCard.setOpaque(false);
            roundCard.setBounds(24 + i * 185, 62, 172, 88);

            JLabel val = new JLabel(statValues[i]);
            val.setFont(new Font("Arial", Font.BOLD, 22));
            val.setForeground(Color.WHITE);
            val.setBounds(16, 18, 150, 30);
            roundCard.add(val);

            JLabel lbl = new JLabel(statLabels[i]);
            lbl.setFont(new Font("Arial", Font.PLAIN, 11));
            lbl.setForeground(new Color(255, 255, 255, 200));
            lbl.setBounds(16, 50, 150, 18);
            roundCard.add(lbl);

            page.add(roundCard);

            if (i == 0) totalDonorsVal    = val;
            if (i == 1) totalDonationsVal = val;
            if (i == 2) amountRaisedVal   = val;
            if (i == 3) completedVal      = val;
        }

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setBounds(24, 168, 740, 330);

        JLabel cardTitle = new JLabel("Recent Donations");
        cardTitle.setFont(new Font("Arial", Font.BOLD, 14));
        cardTitle.setForeground(new Color(30, 36, 48));
        cardTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(cardTitle, BorderLayout.NORTH);

        String[] cols = {"Donor","Type","Amount","Date","Status"};
        recentModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(recentModel);
        styleTable(table);
        loadRecentDonations(recentModel);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(card);

        String[] btnLabels = {"+ Add Donor","+ Donation","+ Campaign","+ Payment"};
        Color[] btnColors = {
            new Color(232, 96, 44), new Color(26, 176, 137),
            new Color(229, 75, 107), new Color(34, 168, 110)
        };
        for (int i = 0; i < 4; i++) {
            JButton btn = makeActionBtn(btnLabels[i], btnColors[i]);
            btn.setBounds(24 + i * 140, 515, 128, 34);
            final int idx = i;
            btn.addActionListener(e -> {
                if (idx == 0) new DonorForm();
                else if (idx == 1) new DonationForm();
                else if (idx == 2) new CampaignForm();
                else new PaymentForm();
                Timer timer = new Timer(1500, ev -> refreshDashboardStats());
                timer.setRepeats(false);
                timer.start();
            });
            page.add(btn);
        }

        return page;
    }

    String[] loadStatValues() {
        String[] vals = {"0","0","₹0","0"};
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return vals;

            ResultSet r1 = con.prepareStatement("SELECT COUNT(*) FROM donor").executeQuery();
            if (r1.next()) vals[0] = String.valueOf(r1.getInt(1));

            ResultSet r2 = con.prepareStatement("SELECT COUNT(*) FROM donation").executeQuery();
            if (r2.next()) vals[1] = String.valueOf(r2.getInt(1));

            ResultSet r3 = con.prepareStatement("SELECT COALESCE(SUM(amount),0) FROM donation").executeQuery();
            if (r3.next()) vals[2] = "₹" + String.format("%,.0f", r3.getDouble(1));

            ResultSet r4 = con.prepareStatement(
                "SELECT COUNT(*) FROM donation WHERE status='Completed'").executeQuery();
            if (r4.next()) vals[3] = String.valueOf(r4.getInt(1));

            con.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return vals;
    }

    void loadRecentDonations(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;
            String sql = "SELECT d.name, dn.donation_type, dn.amount, dn.donation_date, dn.status " +
                         "FROM donation dn JOIN donor d ON dn.donor_id = d.donor_id " +
                         "ORDER BY dn.donation_date DESC LIMIT 10";
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                double amt = rs.getDouble("amount");
                model.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("donation_type"),
                    amt == 0 ? "—" : "₹" + (int) amt,
                    rs.getDate("donation_date"),
                    rs.getString("status")
                });
            }
            con.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ── Donors page with Edit + Delete ────────────────
    JPanel buildDonorsPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(245, 243, 238));
        page.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("Donors");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 36, 48));
        topBar.add(title, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn = makeActionBtn("⟳ Refresh", new Color(26, 176, 137));
        JButton editBtn    = makeActionBtn("✎ Edit",    new Color(34, 120, 200));
        JButton deleteBtn  = makeActionBtn("✕ Delete",  new Color(229, 75, 107));

        btnPanel.add(refreshBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        topBar.add(btnPanel, BorderLayout.EAST);
        page.add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID","Name","Email","Phone","Type","Blood Group"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                Connection con = DBConnection.getConnection();
                if (con != null) {
                    ResultSet rs = con.prepareStatement(
                        "SELECT donor_id, name, email, phone, donor_type, blood_group FROM donor"
                    ).executeQuery();
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6)
                        });
                    }
                    con.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        };
        loadData.run();

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(card, BorderLayout.CENTER);

        // Refresh
        refreshBtn.addActionListener(e -> loadData.run());

        // Delete
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete donor ID " + id + "? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM donor WHERE donor_id = ?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    con.close();
                    JOptionPane.showMessageDialog(this, "Donor deleted successfully!");
                    loadData.run();
                    refreshDashboardStats();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot delete — donor has linked donations.");
                }
            }
        });

        // Edit
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }
            int id       = (int)    model.getValueAt(row, 0);
            String name  = (String) model.getValueAt(row, 1);
            String email = (String) model.getValueAt(row, 2);
            String phone = (String) model.getValueAt(row, 3);
            String type  = (String) model.getValueAt(row, 4);
            String blood = (String) model.getValueAt(row, 5);

            // Edit dialog
            JDialog dialog = new JDialog(this, "Edit Donor", true);
            dialog.setSize(380, 380);
            dialog.setLayout(null);
            dialog.setLocationRelativeTo(this);
            dialog.getContentPane().setBackground(new Color(245, 243, 238));

            JTextField nameF  = addDialogField(dialog, "Name:",        name,  50, 30);
            JTextField emailF = addDialogField(dialog, "Email:",       email, 50, 80);
            JTextField phoneF = addDialogField(dialog, "Phone:",       phone, 50, 130);

            JLabel typeL = new JLabel("Type:");
            typeL.setBounds(50, 180, 100, 28);
            typeL.setFont(new Font("Arial", Font.PLAIN, 13));
            dialog.add(typeL);
            JComboBox<String> typeBox = new JComboBox<>(
                new String[]{"Individual","Organization","Blood","Organ"});
            typeBox.setSelectedItem(type);
            typeBox.setBounds(160, 180, 170, 28);
            dialog.add(typeBox);

            JLabel bloodL = new JLabel("Blood Group:");
            bloodL.setBounds(50, 220, 110, 28);
            bloodL.setFont(new Font("Arial", Font.PLAIN, 13));
            dialog.add(bloodL);
            JComboBox<String> bloodBox = new JComboBox<>(
                new String[]{"N/A","A+","A-","B+","B-","AB+","AB-","O+","O-"});
            bloodBox.setSelectedItem(blood);
            bloodBox.setBounds(160, 220, 170, 28);
            dialog.add(bloodBox);

            JButton saveBtn = makeActionBtn("Save Changes", new Color(232, 96, 44));
            saveBtn.setBounds(100, 290, 160, 36);
            dialog.add(saveBtn);

            saveBtn.addActionListener(ev -> {
                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                        "UPDATE donor SET name=?, email=?, phone=?, donor_type=?, blood_group=? " +
                        "WHERE donor_id=?");
                    ps.setString(1, nameF.getText());
                    ps.setString(2, emailF.getText());
                    ps.setString(3, phoneF.getText());
                    ps.setString(4, typeBox.getSelectedItem().toString());
                    ps.setString(5, bloodBox.getSelectedItem().toString());
                    ps.setInt(6, id);
                    ps.executeUpdate();
                    con.close();
                    JOptionPane.showMessageDialog(dialog, "Donor updated successfully!");
                    dialog.dispose();
                    loadData.run();
                    refreshDashboardStats();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        });

        return page;
    }

    // ── Donations page with Edit + Delete ─────────────
    JPanel buildDonationsPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(245, 243, 238));
        page.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("Donations");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 36, 48));
        topBar.add(title, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);

        JButton refreshBtn = makeActionBtn("⟳ Refresh", new Color(26, 176, 137));
        JButton editBtn    = makeActionBtn("✎ Edit",    new Color(34, 120, 200));
        JButton deleteBtn  = makeActionBtn("✕ Delete",  new Color(229, 75, 107));

        btnPanel.add(refreshBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        topBar.add(btnPanel, BorderLayout.EAST);
        page.add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID","Donor ID","Type","Amount","Date","Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                Connection con = DBConnection.getConnection();
                if (con != null) {
                    ResultSet rs = con.prepareStatement(
                        "SELECT donation_id, donor_id, donation_type, amount, donation_date, status FROM donation"
                    ).executeQuery();
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getInt(1), rs.getInt(2), rs.getString(3),
                            rs.getDouble(4), rs.getDate(5), rs.getString(6)
                        });
                    }
                    con.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        };
        loadData.run();

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(card, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> loadData.run());

        // Delete
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete donation ID " + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM donation WHERE donation_id = ?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    con.close();
                    JOptionPane.showMessageDialog(this, "Donation deleted successfully!");
                    loadData.run();
                    refreshDashboardStats();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Edit
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }
            int id       = (int)    model.getValueAt(row, 0);
            String dtype = (String) model.getValueAt(row, 2);
            double amt   = (double) model.getValueAt(row, 3);
            String status= (String) model.getValueAt(row, 5);

            JDialog dialog = new JDialog(this, "Edit Donation", true);
            dialog.setSize(360, 300);
            dialog.setLayout(null);
            dialog.setLocationRelativeTo(this);
            dialog.getContentPane().setBackground(new Color(245, 243, 238));

            JLabel typeL = new JLabel("Type:");
            typeL.setBounds(50, 30, 100, 28);
            typeL.setFont(new Font("Arial", Font.PLAIN, 13));
            dialog.add(typeL);
            JComboBox<String> typeBox = new JComboBox<>(
                new String[]{"Money","Blood","Organ","Clothes","Food"});
            typeBox.setSelectedItem(dtype);
            typeBox.setBounds(160, 30, 160, 28);
            dialog.add(typeBox);

            JTextField amtF = addDialogField(dialog, "Amount:", String.valueOf((int)amt), 50, 80);

            JLabel statusL = new JLabel("Status:");
            statusL.setBounds(50, 130, 100, 28);
            statusL.setFont(new Font("Arial", Font.PLAIN, 13));
            dialog.add(statusL);
            JComboBox<String> statusBox = new JComboBox<>(
                new String[]{"Completed","Pending","Cancelled"});
            statusBox.setSelectedItem(status);
            statusBox.setBounds(160, 130, 160, 28);
            dialog.add(statusBox);

            JButton saveBtn = makeActionBtn("Save Changes", new Color(232, 96, 44));
            saveBtn.setBounds(90, 210, 160, 36);
            dialog.add(saveBtn);

            saveBtn.addActionListener(ev -> {
                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(
                        "UPDATE donation SET donation_type=?, amount=?, status=? WHERE donation_id=?");
                    ps.setString(1, typeBox.getSelectedItem().toString());
                    ps.setDouble(2, Double.parseDouble(amtF.getText().trim()));
                    ps.setString(3, statusBox.getSelectedItem().toString());
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    con.close();
                    JOptionPane.showMessageDialog(dialog, "Donation updated successfully!");
                    dialog.dispose();
                    loadData.run();
                    refreshDashboardStats();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Amount must be a number.");
                }
            });

            dialog.setVisible(true);
        });

        return page;
    }

    // ── Generic table page with Refresh ───────────────
    JPanel buildTablePage(String pageName, String[] columns, String sql) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(245, 243, 238));
        page.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel(pageName);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(30, 36, 48));
        topBar.add(title, BorderLayout.WEST);

        JButton refreshBtn = makeActionBtn("⟳  Refresh", new Color(26, 176, 137));
        topBar.add(refreshBtn, BorderLayout.EAST);
        page.add(topBar, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                Connection con = DBConnection.getConnection();
                if (con != null) {
                    ResultSet rs = con.prepareStatement(sql).executeQuery();
                    int colCount = rs.getMetaData().getColumnCount();
                    while (rs.next()) {
                        Object[] row = new Object[colCount];
                        for (int i = 0; i < colCount; i++) row[i] = rs.getObject(i + 1);
                        model.addRow(row);
                    }
                    con.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        };

        loadData.run();
        refreshBtn.addActionListener(e -> loadData.run());

        card.add(new JScrollPane(table), BorderLayout.CENTER);
        page.add(card, BorderLayout.CENTER);
        return page;
    }

    // ── Helper: dialog text field ──────────────────────
    JTextField addDialogField(JDialog dialog, String label, String value, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 110, 28);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(new Color(55, 65, 81));
        dialog.add(lbl);

        JTextField field = new JTextField(value);
        field.setBounds(x + 110, y, 170, 28);
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 207, 200), 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        dialog.add(field);
        return field;
    }

    void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(55, 65, 81));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(245, 243, 238));
        table.setSelectionForeground(new Color(30, 36, 48));
        table.getTableHeader().setBackground(new Color(245, 243, 238));
        table.getTableHeader().setForeground(new Color(136, 146, 164));
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(232, 230, 224))
        );
    }

    JButton makeActionBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        new LandingPage();
    }
}